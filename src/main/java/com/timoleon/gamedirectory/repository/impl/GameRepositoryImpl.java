package com.timoleon.gamedirectory.repository.impl;

import com.timoleon.gamedirectory.domain.*;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.GameRepositoryCustom;
import com.timoleon.gamedirectory.repository.specifications.AbstractApiSpecification;
import com.timoleon.gamedirectory.service.dto.GameGridDTO;
import com.timoleon.gamedirectory.service.mapper.GameGridMapper;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class GameRepositoryImpl extends AbstractApiSpecification<Game> implements GameRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Game> search(SearchCriteria criteria, Pageable pageable) {
        EntityGraph<Game> gameEntityGraph = entityManager.createEntityGraph(Game.class);

        return search(criteria, pageable, entityManager, Game.class, gameEntityGraph, null);
    }

    @Override
    public Page<Long> searchForGameIds(SearchCriteria criteria, Pageable pageable, Boolean isEnabledNSFW) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Query for List of rows
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Game> root = query.from(Game.class);

        List<Predicate> predicates = getPredicatesFromCriteria(root, builder, query, criteria);
        if (!isEnabledNSFW) {
            predicates.add(produceSFWPredicate(root, builder, query));
        }
        List<Order> orderList = getOrderFromCriteria(root, builder, criteria);

        query.multiselect(root.get("id")).where(builder.and(predicates.toArray(new Predicate[0]))).orderBy(orderList);

        TypedQuery<Long> tp = entityManager.createQuery(query);

        List<Long> list = tp
            .setFirstResult((pageable.getPageNumber()) * pageable.getPageSize())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        // Query for count of rows
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Game> countRoot = countQuery.from(Game.class);

        List<Predicate> countPredicates = getPredicatesFromCriteria(countRoot, builder, countQuery, criteria);
        if (!isEnabledNSFW) {
            countPredicates.add(produceSFWPredicate(countRoot, builder, countQuery));
        }

        countQuery.select(builder.countDistinct(countRoot)).where(builder.and(countPredicates.toArray(new Predicate[0])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<Game> searchForGames(SearchCriteria criteria, List<Long> ids) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Game> query = builder.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);

        List<Order> orderList = getOrderFromCriteria(root, builder, criteria);

        query.select(root).where(root.get("id").in(ids)).orderBy(orderList);

        TypedQuery<Game> tp = entityManager.createQuery(query);

        return tp.getResultList();
    }

    @Override
    public List<GameGridDTO> searchForGamesUser(SearchCriteria criteria, List<Long> ids, User user, GameGridMapper gameGridMapper) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Game> query = builder.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);

        List<Order> orderList = getOrderFromCriteria(root, builder, criteria);

        query.select(root).where(root.get("id").in(ids)).orderBy(orderList);

        TypedQuery<Game> tp = entityManager.createQuery(query);
        List<GameGridDTO> gameList = tp.getResultList().stream().map(gameGridMapper::toDto).toList();

        Subquery<Long> idsSubquery = query.subquery(Long.class);
        Root<Game> idsRoot = idsSubquery.from(Game.class);
        idsSubquery.select(idsRoot.get("id")).where(idsRoot.get("id").in(ids));

        CriteriaQuery<UserGame> userGameQuery = builder.createQuery(UserGame.class);
        Root<UserGame> userGameRoot = userGameQuery.from(UserGame.class);
        userGameQuery.select(userGameRoot).where(builder.in(userGameRoot.get("game")).value(idsSubquery));

        TypedQuery<UserGame> tpU = entityManager.createQuery(userGameQuery);
        List<UserGame> userGameList = tpU.getResultList();

        for (UserGame userGame : userGameList) {
            Optional<GameGridDTO> gameDTO = gameList.stream().filter(element -> element.getId() == userGame.getGame().getId()).findFirst();
            if (gameDTO.isPresent()) {
                gameDTO.get().setAddedByCurrentUser(true);
                gameDTO.get().setFavourite(userGame.getFavourite());
            }
        }

        return gameList;
    }

    private Predicate produceSFWPredicate(Root root, CriteriaBuilder builder, CriteriaQuery<Long> query) {
        Subquery<String> excludedGamesSubquery = query.subquery(String.class);
        Root<Game> excludedRoot = excludedGamesSubquery.from(Game.class);
        Join<Game, GameDetails> excludedGameDetailsJoin = excludedRoot.join("gameDetails");
        Join<GameDetails, Category> excludedCategoriesJoin = excludedGameDetailsJoin.join("categories");

        excludedGamesSubquery
            .select(excludedRoot.get("title"))
            .distinct(true)
            .where(builder.equal(excludedCategoriesJoin.get("description"), "Adult Content"));

        return builder.not(root.get("title").in(excludedGamesSubquery));
    }
}

package com.timoleon.gamedirectory.repository.impl;

import com.timoleon.gamedirectory.domain.Category;
import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.GameDetails;
import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.UserGameRepositoryCustom;
import com.timoleon.gamedirectory.repository.specifications.AbstractApiSpecification;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class UserGameRepositoryImpl extends AbstractApiSpecification<UserGame> implements UserGameRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserGame> search(SearchCriteria criteria, Pageable pageable) {
        EntityGraph<UserGame> gameEntityGraph = entityManager.createEntityGraph(UserGame.class);

        return search(criteria, pageable, entityManager, UserGame.class, gameEntityGraph, null);
    }

    @Override
    public Page<Long> searchForSFWGamesIds(SearchCriteria criteria, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Query for List of rows
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UserGame> root = query.from(UserGame.class);

        List<Predicate> predicates = produceSFWPredicates(root, builder, query, criteria);
        List<Order> orderList = getOrderFromCriteria(root, builder, criteria);

        query.multiselect(root.get("id")).where(builder.and(predicates.toArray(new Predicate[0]))).orderBy(orderList);

        TypedQuery<Long> tp = entityManager.createQuery(query);

        List<Long> list = tp
            .setFirstResult((pageable.getPageNumber()) * pageable.getPageSize())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        // Query for count of rows
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<UserGame> countRoot = countQuery.from(UserGame.class);

        List<Predicate> countPredicates = produceSFWPredicates(countRoot, builder, countQuery, criteria);

        countQuery.select(builder.countDistinct(countRoot)).where(builder.and(countPredicates.toArray(new Predicate[0])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }

    private List<Predicate> produceSFWPredicates(Root root, CriteriaBuilder builder, CriteriaQuery<Long> query, SearchCriteria criteria) {
        List<Predicate> predicates = getPredicatesFromCriteria(root, builder, query, criteria);

        Subquery<String> excludedGamesSubquery = query.subquery(String.class);
        Root<UserGame> excludedRoot = excludedGamesSubquery.from(UserGame.class);
        Join<UserGame, Game> excludedGameJoin = excludedRoot.join("game");
        Join<Game, GameDetails> excludedGameDetailsJoin = excludedGameJoin.join("gameDetails");
        Join<GameDetails, Category> excludedCategoriesJoin = excludedGameDetailsJoin.join("categories");

        excludedGamesSubquery
            .select(excludedRoot.get("title"))
            .distinct(true)
            .where(builder.equal(excludedCategoriesJoin.get("description"), "Adult Content"));

        predicates.add(builder.not(root.get("title").in(excludedGamesSubquery)));
        return predicates;
    }

    public List<UserGame> searchForSFWGames(SearchCriteria criteria, List<Long> ids) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserGame> query = builder.createQuery(UserGame.class);
        Root<UserGame> root = query.from(UserGame.class);

        List<Order> orderList = getOrderFromCriteria(root, builder, criteria);

        query.select(root).where(root.get("id").in(ids)).orderBy(orderList);

        TypedQuery<UserGame> tp = entityManager.createQuery(query);

        return tp.getResultList();
    }
}

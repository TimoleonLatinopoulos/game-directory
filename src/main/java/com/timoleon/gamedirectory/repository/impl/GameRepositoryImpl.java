package com.timoleon.gamedirectory.repository.impl;

import com.timoleon.gamedirectory.domain.Category;
import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.GameDetails;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.GameRepositoryCustom;
import com.timoleon.gamedirectory.repository.specifications.AbstractApiSpecification;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
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
    public Page<Long> searchForSFWGamesIds(SearchCriteria criteria, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Query for List of rows
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Game> root = query.from(Game.class);

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
        Root<Game> countRoot = countQuery.from(Game.class);

        List<Predicate> countPredicates = produceSFWPredicates(countRoot, builder, countQuery, criteria);

        countQuery.select(builder.countDistinct(countRoot)).where(builder.and(countPredicates.toArray(new Predicate[0])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }

    private List<Predicate> produceSFWPredicates(Root root, CriteriaBuilder builder, CriteriaQuery<Long> query, SearchCriteria criteria) {
        List<Predicate> predicates = getPredicatesFromCriteria(root, builder, query, criteria);

        Subquery<String> excludedGamesSubquery = query.subquery(String.class);
        Root<Game> excludedRoot = excludedGamesSubquery.from(Game.class);
        Join<Game, GameDetails> excludedGameDetailsJoin = excludedRoot.join("gameDetails");
        Join<GameDetails, Category> excludedCategoriesJoin = excludedGameDetailsJoin.join("categories");

        excludedGamesSubquery
            .select(excludedRoot.get("title"))
            .distinct(true)
            .where(builder.equal(excludedCategoriesJoin.get("description"), "Adult Content"));

        predicates.add(builder.not(root.get("title").in(excludedGamesSubquery)));
        return predicates;
    }

    public List<Game> searchForSFWGames(SearchCriteria criteria, List<Long> ids) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Game> query = builder.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);

        List<Order> orderList = getOrderFromCriteria(root, builder, criteria);

        query.select(root).where(root.get("id").in(ids)).orderBy(orderList);

        TypedQuery<Game> tp = entityManager.createQuery(query);

        return tp.getResultList();
    }
}

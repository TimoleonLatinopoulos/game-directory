package com.timoleon.gamedirectory.repository.impl;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.User;
import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.UserGameRepository;
import com.timoleon.gamedirectory.repository.UserGameRepositoryCustom;
import com.timoleon.gamedirectory.repository.specifications.AbstractApiSpecification;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UserGameRepositoryImpl extends AbstractApiSpecification<UserGame> implements UserGameRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserGame> search(SearchCriteria criteria, Pageable pageable) {
        EntityGraph<UserGame> gameEntityGraph = entityManager.createEntityGraph(UserGame.class);

        return search(criteria, pageable, entityManager, UserGame.class, gameEntityGraph, null);
    }
    //    @Override
    //    public Page<Game> searchUserGames(SearchCriteria criteria, Pageable pageable, User currentUser) {
    //        EntityGraph<Game> gameEntityGraph = entityManager.createEntityGraph(Game.class);

    //        EntityGraph<Game> gameEntityGraph = (EntityGraph<Game>) entityManager.getEntityGraph("game-report");
    //        Map<String, Object> properties = new HashMap<>();
    //        properties.put("javax.persistence.loadgraph", entityGraph);
    //        Post post = entityManager.find(Foo.class, id, properties);

    //        EntityGraph<UserGame> userGameEntityGraph = entityManager.createEntityGraph(UserGame.class);
    //
    //        Subgraph<UserGame> userGameSubgraph = gameEntityGraph.(userGameEntityGraph);
    //        userGameSubgraph.addAttributeNodes("user");
    //        userGameSubgraph.addAttributeNodes("game");
    //
    //        return search(criteria, pageable, entityManager, Game.class, gameEntityGraph, null);
    //    }

    //    @Override
    //    public Page<Game> searchUserGames(SearchCriteria criteria, Pageable pageable, User currentUser) {
    //        String query = "SELECT DISTINCT g FROM Game g " +
    //            "INNER JOIN UserGame ug ON g.id = ug.game.id " +
    //            "INNER JOIN User u ON ug.user.id = u.id " +
    //            "WHERE u.id = :userId";
    //
    //        TypedQuery<Game> typedQuery = entityManager.createQuery(query, Game.class);
    //
    //        // Set parameters from the search criteria
    //        typedQuery.setParameter("userId", currentUser.getId());
    //
    //        // Set pagination
    //        typedQuery.setFirstResult((int) pageable.getOffset());
    //        typedQuery.setMaxResults(pageable.getPageSize());
    //
    //        List<Game> games = typedQuery.getResultList();
    //
    //        // Count total results without pagination
    //        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    //        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    //        Root<Game> countRoot = countQuery.from(Game.class);
    //
    //        List<Predicate> countPredicates = getPredicatesFromCriteria(countRoot, criteriaBuilder, countQuery, criteria);
    //        countQuery
    //            .where(countPredicates.toArray(new Predicate[countPredicates.size()]))
    //            .select(criteriaBuilder.countDistinct(countRoot));
    //
    //
    //        Long totalItems = entityManager.createQuery(countQuery).getSingleResult();
    //
    //        return new PageImpl<>(games, pageable, totalItems);
    //    }
}

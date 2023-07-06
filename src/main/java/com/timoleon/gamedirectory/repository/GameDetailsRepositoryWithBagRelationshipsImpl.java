package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.GameDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class GameDetailsRepositoryWithBagRelationshipsImpl implements GameDetailsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GameDetails> fetchBagRelationships(Optional<GameDetails> gameDetails) {
        return gameDetails.map(this::fetchPlatforms).map(this::fetchDevelopers).map(this::fetchPublishers).map(this::fetchCategories);
    }

    @Override
    public Page<GameDetails> fetchBagRelationships(Page<GameDetails> gameDetails) {
        return new PageImpl<>(fetchBagRelationships(gameDetails.getContent()), gameDetails.getPageable(), gameDetails.getTotalElements());
    }

    @Override
    public List<GameDetails> fetchBagRelationships(List<GameDetails> gameDetails) {
        return Optional
            .of(gameDetails)
            .map(this::fetchPlatforms)
            .map(this::fetchDevelopers)
            .map(this::fetchPublishers)
            .map(this::fetchCategories)
            .orElse(Collections.emptyList());
    }

    GameDetails fetchPlatforms(GameDetails result) {
        return entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.platforms where gameDetails.id = :id",
                GameDetails.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<GameDetails> fetchPlatforms(List<GameDetails> gameDetails) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, gameDetails.size()).forEach(index -> order.put(gameDetails.get(index).getId(), index));
        List<GameDetails> result = entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.platforms where gameDetails in :gameDetails",
                GameDetails.class
            )
            .setParameter("gameDetails", gameDetails)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    GameDetails fetchDevelopers(GameDetails result) {
        return entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.developers where gameDetails.id = :id",
                GameDetails.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<GameDetails> fetchDevelopers(List<GameDetails> gameDetails) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, gameDetails.size()).forEach(index -> order.put(gameDetails.get(index).getId(), index));
        List<GameDetails> result = entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.developers where gameDetails in :gameDetails",
                GameDetails.class
            )
            .setParameter("gameDetails", gameDetails)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    GameDetails fetchPublishers(GameDetails result) {
        return entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.publishers where gameDetails.id = :id",
                GameDetails.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<GameDetails> fetchPublishers(List<GameDetails> gameDetails) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, gameDetails.size()).forEach(index -> order.put(gameDetails.get(index).getId(), index));
        List<GameDetails> result = entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.publishers where gameDetails in :gameDetails",
                GameDetails.class
            )
            .setParameter("gameDetails", gameDetails)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    GameDetails fetchCategories(GameDetails result) {
        return entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.categories where gameDetails.id = :id",
                GameDetails.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<GameDetails> fetchCategories(List<GameDetails> gameDetails) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, gameDetails.size()).forEach(index -> order.put(gameDetails.get(index).getId(), index));
        List<GameDetails> result = entityManager
            .createQuery(
                "select gameDetails from GameDetails gameDetails left join fetch gameDetails.categories where gameDetails in :gameDetails",
                GameDetails.class
            )
            .setParameter("gameDetails", gameDetails)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

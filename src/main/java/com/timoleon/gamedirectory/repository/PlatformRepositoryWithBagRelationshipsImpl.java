package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Platform;
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
public class PlatformRepositoryWithBagRelationshipsImpl implements PlatformRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Platform> fetchBagRelationships(Optional<Platform> platform) {
        return platform.map(this::fetchGameDetails);
    }

    @Override
    public Page<Platform> fetchBagRelationships(Page<Platform> platforms) {
        return new PageImpl<>(fetchBagRelationships(platforms.getContent()), platforms.getPageable(), platforms.getTotalElements());
    }

    @Override
    public List<Platform> fetchBagRelationships(List<Platform> platforms) {
        return Optional.of(platforms).map(this::fetchGameDetails).orElse(Collections.emptyList());
    }

    Platform fetchGameDetails(Platform result) {
        return entityManager
            .createQuery(
                "select platform from Platform platform left join fetch platform.gameDetails where platform.id = :id",
                Platform.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Platform> fetchGameDetails(List<Platform> platforms) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, platforms.size()).forEach(index -> order.put(platforms.get(index).getId(), index));
        List<Platform> result = entityManager
            .createQuery(
                "select platform from Platform platform left join fetch platform.gameDetails where platform in :platforms",
                Platform.class
            )
            .setParameter("platforms", platforms)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

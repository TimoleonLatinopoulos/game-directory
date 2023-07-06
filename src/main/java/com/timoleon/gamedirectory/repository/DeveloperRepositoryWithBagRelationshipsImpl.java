package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Developer;
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
public class DeveloperRepositoryWithBagRelationshipsImpl implements DeveloperRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Developer> fetchBagRelationships(Optional<Developer> developer) {
        return developer.map(this::fetchGameDetails);
    }

    @Override
    public Page<Developer> fetchBagRelationships(Page<Developer> developers) {
        return new PageImpl<>(fetchBagRelationships(developers.getContent()), developers.getPageable(), developers.getTotalElements());
    }

    @Override
    public List<Developer> fetchBagRelationships(List<Developer> developers) {
        return Optional.of(developers).map(this::fetchGameDetails).orElse(Collections.emptyList());
    }

    Developer fetchGameDetails(Developer result) {
        return entityManager
            .createQuery(
                "select developer from Developer developer left join fetch developer.gameDetails where developer.id = :id",
                Developer.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Developer> fetchGameDetails(List<Developer> developers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, developers.size()).forEach(index -> order.put(developers.get(index).getId(), index));
        List<Developer> result = entityManager
            .createQuery(
                "select developer from Developer developer left join fetch developer.gameDetails where developer in :developers",
                Developer.class
            )
            .setParameter("developers", developers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Publisher;
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
public class PublisherRepositoryWithBagRelationshipsImpl implements PublisherRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Publisher> fetchBagRelationships(Optional<Publisher> publisher) {
        return publisher.map(this::fetchGameDetails);
    }

    @Override
    public Page<Publisher> fetchBagRelationships(Page<Publisher> publishers) {
        return new PageImpl<>(fetchBagRelationships(publishers.getContent()), publishers.getPageable(), publishers.getTotalElements());
    }

    @Override
    public List<Publisher> fetchBagRelationships(List<Publisher> publishers) {
        return Optional.of(publishers).map(this::fetchGameDetails).orElse(Collections.emptyList());
    }

    Publisher fetchGameDetails(Publisher result) {
        return entityManager
            .createQuery(
                "select publisher from Publisher publisher left join fetch publisher.gameDetails where publisher.id = :id",
                Publisher.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Publisher> fetchGameDetails(List<Publisher> publishers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, publishers.size()).forEach(index -> order.put(publishers.get(index).getId(), index));
        List<Publisher> result = entityManager
            .createQuery(
                "select publisher from Publisher publisher left join fetch publisher.gameDetails where publisher in :publishers",
                Publisher.class
            )
            .setParameter("publishers", publishers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

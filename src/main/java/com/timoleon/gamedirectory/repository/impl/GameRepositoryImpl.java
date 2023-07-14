package com.timoleon.gamedirectory.repository.impl;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.GameRepositoryCustom;
import com.timoleon.gamedirectory.repository.specifications.AbstractApiSpecification;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GameRepositoryImpl extends AbstractApiSpecification<Game> implements GameRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Game> search(SearchCriteria criteria, Pageable pageable) {
        EntityGraph<Game> applicationEntityGraph = entityManager.createEntityGraph(Game.class);

        //        applicationEntityGraph.addAttributeNodes("lochiaInfo");
        //        applicationEntityGraph.addAttributeNodes("gestationInfo");
        //        applicationEntityGraph.addAttributeNodes("applicationAllowances");
        //        applicationEntityGraph.addAttributeNodes("mainInsuranceInstitution");
        //        applicationEntityGraph.addAttributeNodes("branch");
        //        applicationEntityGraph.addAttributeNodes("employee");
        //        applicationEntityGraph.addSubgraph("insurancePeriods").addAttributeNodes("insuranceInstitution");
        //        applicationEntityGraph.addSubgraph("applicationDecisions").addAttributeNodes("decisionAllowances");
        //        applicationEntityGraph.addSubgraph("employers").addAttributeNodes("employer");
        return search(criteria, pageable, entityManager, Game.class, applicationEntityGraph, null);
    }
}

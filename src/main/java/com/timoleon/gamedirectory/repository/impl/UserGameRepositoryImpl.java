package com.timoleon.gamedirectory.repository.impl;

import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
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
}

package com.timoleon.gamedirectory.repository.impl;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.GameRepositoryCustom;
import com.timoleon.gamedirectory.repository.specifications.AbstractApiSpecification;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GameRepositoryImpl extends AbstractApiSpecification<Game> implements GameRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Game> search(SearchCriteria criteria, Pageable pageable) {
        EntityGraph<Game> gameEntityGraph = entityManager.createEntityGraph(Game.class);

        return search(criteria, pageable, entityManager, Game.class, gameEntityGraph, null);
    }
}

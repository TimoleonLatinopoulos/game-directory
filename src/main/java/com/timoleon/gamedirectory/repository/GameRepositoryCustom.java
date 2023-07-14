package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface GameRepositoryCustom {
    Page<Game> search(SearchCriteria criteria, Pageable pageable);
}

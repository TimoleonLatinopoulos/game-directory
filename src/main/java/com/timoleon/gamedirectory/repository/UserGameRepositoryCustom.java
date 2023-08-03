package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface UserGameRepositoryCustom {
    Page<UserGame> search(SearchCriteria criteria, Pageable pageable);
}

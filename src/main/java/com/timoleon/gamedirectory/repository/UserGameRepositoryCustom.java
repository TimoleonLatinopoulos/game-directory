package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGameRepositoryCustom {
    Page<UserGame> search(SearchCriteria criteria, Pageable pageable);
    Page<Long> searchForGameIds(SearchCriteria criteria, Pageable pageable, Boolean isEnabledNSFW);
    List<UserGame> searchForGames(SearchCriteria criteria, List<Long> ids);
}

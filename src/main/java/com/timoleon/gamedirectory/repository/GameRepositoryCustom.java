package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface GameRepositoryCustom {
    Page<Game> search(SearchCriteria criteria, Pageable pageable);
    Page<Long> searchForSFWGamesIds(SearchCriteria criteria, Pageable pageable);
    List<Game> searchForSFWGames(SearchCriteria criteria, List<Long> ids);
}

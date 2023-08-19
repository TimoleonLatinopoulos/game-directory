package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.User;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.service.dto.GameGridDTO;
import com.timoleon.gamedirectory.service.mapper.GameGridMapper;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface GameRepositoryCustom {
    Page<Game> search(SearchCriteria criteria, Pageable pageable);
    Page<Long> searchForGameIds(SearchCriteria criteria, Pageable pageable, Boolean isEnabledNSFW);
    List<Game> searchForGames(SearchCriteria criteria, List<Long> ids);
    List<GameGridDTO> searchForGamesUser(SearchCriteria criteria, List<Long> ids, User user, GameGridMapper gameGridMapper);
}

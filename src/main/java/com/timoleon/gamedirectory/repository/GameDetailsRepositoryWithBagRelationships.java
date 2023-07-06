package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.GameDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface GameDetailsRepositoryWithBagRelationships {
    Optional<GameDetails> fetchBagRelationships(Optional<GameDetails> gameDetails);

    List<GameDetails> fetchBagRelationships(List<GameDetails> gameDetails);

    Page<GameDetails> fetchBagRelationships(Page<GameDetails> gameDetails);
}

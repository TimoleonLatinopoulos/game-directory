package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("select game from Game game where game.title = ?1")
    List<Game> findByTitle(@Param("title") String title);
}

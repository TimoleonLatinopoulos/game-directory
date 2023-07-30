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
public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {
    @Query("select game from Game game where game.title = ?1")
    List<Game> findByTitle(@Param("title") String title);

    @Query("select game from Game game where game.title = ?1 and game.id != ?2")
    List<Game> findByTitleAndDifferentId(@Param("title") String title, @Param("id") Long id);

    @Query(
        "select game from Game game " +
        "inner join fetch game.gameDetails gameDetails " +
        "left join fetch gameDetails.platforms " +
        "left join fetch gameDetails.developers " +
        "left join fetch gameDetails.publishers " +
        "left join fetch gameDetails.categories where game.id =:id"
    )
    Optional<Game> findOneWithEagerRelationships(@Param("id") Long id);
}

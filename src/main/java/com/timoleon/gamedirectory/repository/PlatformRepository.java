package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Developer;
import com.timoleon.gamedirectory.domain.Platform;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Platform entity.
 */
@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByDescription(String description);

    @Query("select distinct platform from Platform platform " + "where platform.description like ?1")
    List<Platform> findAllLike(String input);

    @Query(
        "select distinct platform from Platform platform " +
        "where platform in (select distinct p from GameDetails gameDetails join gameDetails.platforms p)"
    )
    List<Platform> findAllUsed();

    @Query(
        "select distinct platform from Platform platform " +
        "where platform in (select distinct p from GameDetails gameDetails join gameDetails.platforms p) " +
        "and platform.description like ?1"
    )
    List<Platform> findLikeUsed(String input, Pageable pageable);
}

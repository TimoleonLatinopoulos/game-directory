package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Developer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Developer entity.
 */
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByDescription(String description);

    @Query("select developer from Developer developer where developer.description like ?1")
    List<Developer> findLike(String input, Pageable pageable);
}

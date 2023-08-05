package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Developer;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Developer entity.
 */
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByDescription(String description);
}

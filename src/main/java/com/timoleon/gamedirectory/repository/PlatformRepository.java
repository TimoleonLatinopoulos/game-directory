package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Platform;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Platform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {}

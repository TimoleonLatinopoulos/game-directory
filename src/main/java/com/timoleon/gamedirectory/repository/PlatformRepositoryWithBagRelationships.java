package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Platform;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PlatformRepositoryWithBagRelationships {
    Optional<Platform> fetchBagRelationships(Optional<Platform> platform);

    List<Platform> fetchBagRelationships(List<Platform> platforms);

    Page<Platform> fetchBagRelationships(Page<Platform> platforms);
}

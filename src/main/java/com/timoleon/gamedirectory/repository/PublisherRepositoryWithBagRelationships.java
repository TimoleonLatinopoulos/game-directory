package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Publisher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PublisherRepositoryWithBagRelationships {
    Optional<Publisher> fetchBagRelationships(Optional<Publisher> publisher);

    List<Publisher> fetchBagRelationships(List<Publisher> publishers);

    Page<Publisher> fetchBagRelationships(Page<Publisher> publishers);
}

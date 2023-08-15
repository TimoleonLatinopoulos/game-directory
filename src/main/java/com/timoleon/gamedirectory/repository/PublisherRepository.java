package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Publisher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Publisher entity.
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByDescription(String description);

    @Query("select publisher from Publisher publisher where publisher.description like ?1")
    List<Publisher> findLike(String input, Pageable pageable);
}

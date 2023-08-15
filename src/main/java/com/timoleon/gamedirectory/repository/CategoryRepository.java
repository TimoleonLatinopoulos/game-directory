package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Category entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByDescription(String description);

    @Query(
        "select distinct category from Category category " +
        "where category in (select distinct c from GameDetails gameDetails join gameDetails.categories c)"
    )
    List<Category> findAllUsed();

    @Query(
        "select distinct category from Category category " +
        "where category in (select distinct c from GameDetails gameDetails join gameDetails.categories c) " +
        "and category.description like ?1"
    )
    List<Category> findLikeUsed(String input, Pageable pageable);
}

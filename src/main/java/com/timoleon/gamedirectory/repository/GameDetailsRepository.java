package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.GameDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameDetails entity.
 *
 * When extending this class, extend GameDetailsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface GameDetailsRepository extends GameDetailsRepositoryWithBagRelationships, JpaRepository<GameDetails, Long> {
    default Optional<GameDetails> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<GameDetails> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<GameDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

package com.timoleon.gamedirectory.repository;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.User;
import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserGame entity.
 */
@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long>, UserGameRepositoryCustom {
    @Query("select userGame from UserGame userGame where userGame.user.login = ?#{principal.username}")
    List<UserGame> findByUserIsCurrentUser();

    default Optional<UserGame> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserGame> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserGame> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userGame from UserGame userGame left join fetch userGame.user",
        countQuery = "select count(userGame) from UserGame userGame"
    )
    Page<UserGame> findAllWithToOneRelationships(Pageable pageable);

    @Query("select userGame from UserGame userGame left join fetch userGame.user")
    List<UserGame> findAllWithToOneRelationships();

    @Query("select userGame from UserGame userGame left join fetch userGame.user where userGame.id =:id")
    Optional<UserGame> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select userGame from UserGame userGame where userGame.game.id = :gameId and userGame.user.id = :userId")
    Optional<UserGame> findOneByGameIdAndUserId(@Param("gameId") Long gameId, @Param("userId") Long userId);

    @Query("select userGame from UserGame userGame where userGame.game.id = :gameId")
    List<UserGame> findAllByGameId(@Param("gameId") Long gameId);

    @Modifying
    @Query("delete UserGame userGame where userGame.game.id = :gameId")
    void deleteAllByGameId(@Param("gameId") Long gameId);

    @Modifying
    @Query(
        "update UserGame userGame " +
        "set userGame.favourite = " +
        "   case userGame.favourite " +
        "       when true then false " +
        "       else true end " +
        "where userGame.id = :id"
    )
    void reverseFavourite(@Param("id") Long id);
}

package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.User;
import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.UserGameRepository;
import com.timoleon.gamedirectory.service.UserGameService;
import com.timoleon.gamedirectory.service.UserService;
import com.timoleon.gamedirectory.service.dto.GameGridDTO;
import com.timoleon.gamedirectory.web.rest.entity.PageResponse;
import com.timoleon.gamedirectory.web.rest.errors.BadRequestAlertException;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.UserGame}.
 */
@RestController
@RequestMapping("/api")
public class UserGameResource extends AbstractApiResource {

    private final Logger log = LoggerFactory.getLogger(UserGameResource.class);

    private static final String ENTITY_NAME = "userGame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserGameService userGameService;

    private final UserGameRepository userGameRepository;

    private final UserService userService;

    public UserGameResource(UserGameService userGameService, UserGameRepository userGameRepository, UserService userService) {
        this.userGameService = userGameService;
        this.userGameRepository = userGameRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /user-games} : Create a new userGame.
     *
     * @param id the game id for gameUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userGame, or with status {@code 400 (Bad Request)} if the userGame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-games")
    public ResponseEntity<UserGame> createUserGame(@RequestBody Long id) throws URISyntaxException {
        log.debug("REST request to save UserGame with game id: {}", id);
        if (id == null) {
            throw new BadRequestAlertException("The game id should not be null", ENTITY_NAME, "nullId");
        }

        UserGame result = userGameService.save(id);
        return ResponseEntity
            .created(new URI("/api/user-games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-games/:id} : Updates an existing userGame.
     *
     * @param id the id of the userGame to save.
     * @param userGame the userGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGame,
     * or with status {@code 400 (Bad Request)} if the userGame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userGame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-games/{id}")
    public ResponseEntity<UserGame> updateUserGame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserGame userGame
    ) throws URISyntaxException {
        log.debug("REST request to update UserGame : {}, {}", id, userGame);
        if (userGame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userGame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userGameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserGame result = userGameService.update(userGame);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userGame.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-games/:id} : Partial updates given fields of an existing userGame, field will ignore if it is null
     *
     * @param id the id of the userGame to save.
     * @param userGame the userGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGame,
     * or with status {@code 400 (Bad Request)} if the userGame is not valid,
     * or with status {@code 404 (Not Found)} if the userGame is not found,
     * or with status {@code 500 (Internal Server Error)} if the userGame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-games/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserGame> partialUpdateUserGame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserGame userGame
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserGame partially : {}, {}", id, userGame);
        if (userGame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userGame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userGameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserGame> result = userGameService.partialUpdate(userGame);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userGame.getId().toString())
        );
    }

    /**
     * {@code GET  /user-games} : get all the userGames.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userGames in body.
     */
    @GetMapping("/user-games")
    public List<UserGame> getAllUserGames(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all UserGames");
        return userGameService.findAll();
    }

    /**
     * {@code GET  /user-games/set-favourite/:id} : update the favourite field of the "id" userGame .
     *
     * @param id the id of the userGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userGame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-games/set-favourite/{id}")
    public ResponseEntity<UserGame> setFavourite(@PathVariable Long id) {
        log.debug("REST request to set the favourite field of the id UserGame : {}", id);
        Optional<UserGame> userGame = userGameService.setFavourite(id);
        return ResponseUtil.wrapOrNotFound(userGame);
    }

    /**
     * {@code GET  /user-games/:id} : get the "id" userGame.
     *
     * @param id the id of the userGame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userGame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-games/{id}")
    public ResponseEntity<UserGame> getUserGame(@PathVariable Long id) {
        log.debug("REST request to get UserGame : {}", id);
        Optional<UserGame> userGame = userGameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userGame);
    }

    /**
     * {@code GET  /user-games/game/:id} : get the "id" userGame.
     *
     * @param id the id of the game of the userGame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userGame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-games/game/{id}")
    public ResponseEntity<UserGame> getUserGameByGameId(@PathVariable Long id) {
        log.debug("REST request to get UserGame of Game: {}", id);

        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("User not found", "userGame", "userNotFound"));

        Optional<UserGame> userGame = userGameService.findOneByGameIdAndUserId(id, currentUser.getId());
        return ResponseUtil.wrapOrNotFound(userGame);
    }

    @GetMapping("/user-games/search")
    @Timed
    public PageResponse<GameGridDTO> search(HttpServletRequest request, Authentication authentication) {
        log.debug("REST request to search a page of Games linked to the User");

        PageRequest pageRequest = this.extractPageRequestFromRequest(request);
        SearchCriteria searchCriteria = this.extractSearchCriteriaFromRequest(request);
        return new PageResponse<>(userGameService.search(searchCriteria, pageRequest));
    }

    /**
     * {@code DELETE  /user-games/:id} : delete the "id" userGame.
     *
     * @param id the id of the userGame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-games/{id}")
    public ResponseEntity<Void> deleteUserGame(@PathVariable Long id) {
        log.debug("REST request to delete UserGame : {}", id);
        userGameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

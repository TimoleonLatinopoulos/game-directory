package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.GameDetails;
import com.timoleon.gamedirectory.repository.GameDetailsRepository;
import com.timoleon.gamedirectory.service.GameDetailsService;
import com.timoleon.gamedirectory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.GameDetails}.
 */
@RestController
@RequestMapping("/api")
public class GameDetailsResource {

    private final Logger log = LoggerFactory.getLogger(GameDetailsResource.class);

    private static final String ENTITY_NAME = "gameDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameDetailsService gameDetailsService;

    private final GameDetailsRepository gameDetailsRepository;

    public GameDetailsResource(GameDetailsService gameDetailsService, GameDetailsRepository gameDetailsRepository) {
        this.gameDetailsService = gameDetailsService;
        this.gameDetailsRepository = gameDetailsRepository;
    }

    /**
     * {@code POST  /game-details} : Create a new gameDetails.
     *
     * @param gameDetails the gameDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameDetails, or with status {@code 400 (Bad Request)} if the gameDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-details")
    public ResponseEntity<GameDetails> createGameDetails(@RequestBody GameDetails gameDetails) throws URISyntaxException {
        log.debug("REST request to save GameDetails : {}", gameDetails);
        if (gameDetails.getId() != null) {
            throw new BadRequestAlertException("A new gameDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameDetails result = gameDetailsService.save(gameDetails);
        return ResponseEntity
            .created(new URI("/api/game-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-details/:id} : Updates an existing gameDetails.
     *
     * @param id the id of the gameDetails to save.
     * @param gameDetails the gameDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameDetails,
     * or with status {@code 400 (Bad Request)} if the gameDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-details/{id}")
    public ResponseEntity<GameDetails> updateGameDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameDetails gameDetails
    ) throws URISyntaxException {
        log.debug("REST request to update GameDetails : {}, {}", id, gameDetails);
        if (gameDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameDetails result = gameDetailsService.update(gameDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-details/:id} : Partial updates given fields of an existing gameDetails, field will ignore if it is null
     *
     * @param id the id of the gameDetails to save.
     * @param gameDetails the gameDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameDetails,
     * or with status {@code 400 (Bad Request)} if the gameDetails is not valid,
     * or with status {@code 404 (Not Found)} if the gameDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameDetails> partialUpdateGameDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameDetails gameDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameDetails partially : {}, {}", id, gameDetails);
        if (gameDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameDetails> result = gameDetailsService.partialUpdate(gameDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /game-details} : get all the gameDetails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameDetails in body.
     */
    @GetMapping("/game-details")
    public List<GameDetails> getAllGameDetails(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all GameDetails");
        return gameDetailsService.findAll();
    }

    /**
     * {@code GET  /game-details/:id} : get the "id" gameDetails.
     *
     * @param id the id of the gameDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-details/{id}")
    public ResponseEntity<GameDetails> getGameDetails(@PathVariable Long id) {
        log.debug("REST request to get GameDetails : {}", id);
        Optional<GameDetails> gameDetails = gameDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameDetails);
    }

    /**
     * {@code DELETE  /game-details/:id} : delete the "id" gameDetails.
     *
     * @param id the id of the gameDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-details/{id}")
    public ResponseEntity<Void> deleteGameDetails(@PathVariable Long id) {
        log.debug("REST request to delete GameDetails : {}", id);
        gameDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

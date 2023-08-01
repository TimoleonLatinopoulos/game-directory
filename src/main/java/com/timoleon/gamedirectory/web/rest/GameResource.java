package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.repository.GameRepository;
import com.timoleon.gamedirectory.security.AuthoritiesConstants;
import com.timoleon.gamedirectory.service.GameService;
import com.timoleon.gamedirectory.service.dto.GameDTO;
import com.timoleon.gamedirectory.service.dto.GameGridDTO;
import com.timoleon.gamedirectory.service.mapper.GameMapper;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.Game}.
 */
@RestController
@RequestMapping("/api")
public class GameResource extends AbstractApiResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameService gameService;

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    public GameResource(GameService gameService, GameRepository gameRepository, GameMapper gameMapper) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    /**
     * {@code POST  /games} : Create a new game.
     *
     * @param game the game to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new game, or with status {@code 400 (Bad Request)} if the game has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/games")
    @Secured({ AuthoritiesConstants.ADMIN })
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO game) throws URISyntaxException {
        log.debug("REST request to save Game : {}", game);
        if (game.getId() != null) {
            throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
        }

        List<Game> games = gameRepository.findByTitle(game.getTitle());
        if (!games.isEmpty()) {
            throw new IllegalStateException("This game title is already stored in the database!");
        }

        GameDTO result = gameService.save(game);
        return ResponseEntity
            .created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /games/:id} : Updates an existing game.
     *
     * @param id the id of the game to save.
     * @param game the game to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated game,
     * or with status {@code 400 (Bad Request)} if the game is not valid,
     * or with status {@code 500 (Internal Server Error)} if the game couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/games/{id}")
    @Secured({ AuthoritiesConstants.ADMIN })
    public ResponseEntity<GameDTO> updateGame(@PathVariable(value = "id", required = false) final Long id, @RequestBody GameDTO game)
        throws URISyntaxException {
        log.debug("REST request to update Game : {}, {}", id, game);
        if (game.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, game.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        List<Game> games = gameRepository.findByTitleAndDifferentId(game.getTitle(), game.getId());
        if (!games.isEmpty()) {
            throw new IllegalStateException("This game title is given to a different game stored in the database!");
        }

        GameDTO result = gameService.update(game);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, game.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /games/:id} : Partial updates given fields of an existing game, field will ignore if it is null
     *
     * @param id the id of the game to save.
     * @param game the game to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated game,
     * or with status {@code 400 (Bad Request)} if the game is not valid,
     * or with status {@code 404 (Not Found)} if the game is not found,
     * or with status {@code 500 (Internal Server Error)} if the game couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/games/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Secured({ AuthoritiesConstants.ADMIN })
    public ResponseEntity<Game> partialUpdateGame(@PathVariable(value = "id", required = false) final Long id, @RequestBody Game game)
        throws URISyntaxException {
        log.debug("REST request to partial update Game partially : {}, {}", id, game);
        if (game.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, game.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Game> result = gameService.partialUpdate(game);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, game.getId().toString())
        );
    }

    /**
     * {@code GET  /games} : get all the games.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of games in body.
     */
    @GetMapping("/games")
    @Secured({ AuthoritiesConstants.ADMIN })
    public List<Game> getAllGames() {
        log.debug("REST request to get all Games");
        return gameService.findAll();
    }

    /**
     * {@code GET  /games/:id} : get the "id" game.
     *
     * @param id the id of the game to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the game, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        log.debug("REST request to get Game : {}", id);
        Optional<Game> game = gameService.findOne(id);
        GameDTO gameDTO = gameMapper.toDto(game.get());
        return ResponseUtil.wrapOrNotFound(Optional.of(gameDTO));
    }

    @GetMapping("/games/search")
    @Timed
    public PageResponse<GameGridDTO> search(HttpServletRequest request, Authentication authentication) {
        log.debug("REST request to search a page of ApplicationNational");

        PageRequest pageRequest = this.extractPageRequestFromRequest(request);
        SearchCriteria searchCriteria = this.extractSearchCriteriaFromRequest(request);
        return new PageResponse<>(gameService.search(searchCriteria, pageRequest));
    }

    /**
     * {@code DELETE  /games/:id} : delete the "id" game.
     *
     * @param id the id of the game to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/games/{id}")
    @Secured({ AuthoritiesConstants.ADMIN })
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

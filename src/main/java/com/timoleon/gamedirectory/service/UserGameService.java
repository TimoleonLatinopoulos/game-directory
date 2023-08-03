package com.timoleon.gamedirectory.service;

import static com.timoleon.gamedirectory.domain.search.SearchFilterItem.OPERATOR_EQUALS;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.User;
import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.domain.search.SearchFilterItem;
import com.timoleon.gamedirectory.repository.UserGameRepository;
import com.timoleon.gamedirectory.service.dto.GameGridDTO;
import com.timoleon.gamedirectory.service.mapper.UserGameGridMapper;
import com.timoleon.gamedirectory.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserGame}.
 */
@Service
@Transactional
public class UserGameService {

    private final Logger log = LoggerFactory.getLogger(UserGameService.class);

    private final UserGameRepository userGameRepository;
    private final UserService userService;
    private final GameService gameService;

    private final UserGameGridMapper userGameGridMapper;

    public UserGameService(
        UserGameRepository userGameRepository,
        UserService userService,
        GameService gameService,
        UserGameGridMapper userGameGridMapper
    ) {
        this.userGameRepository = userGameRepository;
        this.userService = userService;
        this.gameService = gameService;
        this.userGameGridMapper = userGameGridMapper;
    }

    /**
     * Save a userGame.
     *
     * @param gameId the id of the game.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class, timeout = 60)
    public UserGame save(Long gameId) {
        log.debug("Request to save UserGame by gameId: {}", gameId);

        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("User not found", "userGame", "userNotFound"));

        Game game = gameService
            .findOne(gameId)
            .orElseThrow(() -> new BadRequestAlertException("Game not found", "userGame", "gameNotFound"));

        Optional<UserGame> userGameFound = findOneByGameIdAndUserId(gameId, currentUser.getId());
        if (userGameFound.isPresent()) {
            throw new BadRequestAlertException("UserGame already exists", "userGame", "userGameExists");
        }

        UserGame userGame = new UserGame();
        userGame.setGame(game);
        userGame.setTitle(game.getTitle());
        userGame.setUser(currentUser);
        userGame.setFavourite(false);
        userGame.setDateAdded(LocalDate.now());

        return userGameRepository.save(userGame);
    }

    /**
     * Update a userGame.
     *
     * @param userGame the entity to save.
     * @return the persisted entity.
     */
    public UserGame update(UserGame userGame) {
        log.debug("Request to update UserGame : {}", userGame);
        return userGameRepository.save(userGame);
    }

    /**
     * Partially update a userGame.
     *
     * @param userGame the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserGame> partialUpdate(UserGame userGame) {
        log.debug("Request to partially update UserGame : {}", userGame);

        return userGameRepository
            .findById(userGame.getId())
            .map(existingUserGame -> {
                if (userGame.getFavourite() != null) {
                    existingUserGame.setFavourite(userGame.getFavourite());
                }
                if (userGame.getDateAdded() != null) {
                    existingUserGame.setDateAdded(userGame.getDateAdded());
                }

                return existingUserGame;
            })
            .map(userGameRepository::save);
    }

    /**
     * Get all the userGames.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserGame> findAll() {
        log.debug("Request to get all UserGames");
        return userGameRepository.findAll();
    }

    /**
     * Get all the userGames with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserGame> findAllWithEagerRelationships(Pageable pageable) {
        return userGameRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one userGame by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserGame> findOne(Long id) {
        log.debug("Request to get UserGame : {}", id);
        return userGameRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Toggle the favourite field.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(rollbackFor = Exception.class, timeout = 60)
    public Optional<UserGame> setFavourite(Long id) {
        log.debug("Request to toggle the favourite field of UserGame : {}", id);
        userGameRepository.reverseFavourite(id);
        return findOne(id);
    }

    /**
     * Get one userGame by game id.
     *
     * @param gameId the id of the game.
     * @param userId the id of the user.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserGame> findOneByGameIdAndUserId(Long gameId, Long userId) {
        log.debug("Request to get UserGame by gameId and userId: {}, {}", gameId, userId);
        return userGameRepository.findOneByGameIdAndUserId(gameId, userId);
    }

    /**
     * Search user page.
     *
     * @param criteria    the criteria
     * @param pageRequest the page request
     * @return the page
     */
    @Transactional(readOnly = true, timeout = 60)
    public Page<GameGridDTO> search(SearchCriteria criteria, PageRequest pageRequest) {
        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("User not found", "userGame", "userNotFound"));

        if (criteria.getFilter() != null) {
            SearchFilterItem filterItem = new SearchFilterItem("user.id", OPERATOR_EQUALS, currentUser.getId().toString());
            criteria.getFilter().getFilters().add(filterItem);
        }

        Page<UserGame> page = userGameRepository.search(criteria, pageRequest);
        List<GameGridDTO> list = new ArrayList<>(page.getContent()).stream().map(userGameGridMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    /**
     * Delete the userGame by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserGame : {}", id);
        userGameRepository.deleteById(id);
    }
}

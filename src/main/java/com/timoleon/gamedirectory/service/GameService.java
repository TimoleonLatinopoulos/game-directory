package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.domain.GameDetails;
import com.timoleon.gamedirectory.repository.GameRepository;
import com.timoleon.gamedirectory.service.dto.GameDTO;
import com.timoleon.gamedirectory.service.mapper.GameMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Game}.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    public GameService(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    /**
     * Save a game.
     *
     * @param gameDTO the entity to save.
     * @return the persisted entity.
     */
    public GameDTO save(GameDTO gameDTO) {
        log.debug("Request to save Game : {}", gameDTO);

        Game game = gameMapper.toEntity(gameDTO);
        Game result = gameRepository.save(game);

        return gameMapper.toDto(result);
    }

    /**
     * Update a game.
     *
     * @param game the entity to save.
     * @return the persisted entity.
     */
    public Game update(Game game) {
        log.debug("Request to update Game : {}", game);
        return gameRepository.save(game);
    }

    /**
     * Partially update a game.
     *
     * @param game the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Game> partialUpdate(Game game) {
        log.debug("Request to partially update Game : {}", game);

        return gameRepository
            .findById(game.getId())
            .map(existingGame -> {
                if (game.getTitle() != null) {
                    existingGame.setTitle(game.getTitle());
                }

                return existingGame;
            })
            .map(gameRepository::save);
    }

    /**
     * Get all the games.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Game> findAll() {
        log.debug("Request to get all Games");
        return gameRepository.findAll();
    }

    /**
     * Get one game by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Game> findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        return gameRepository.findById(id);
    }

    /**
     * Delete the game by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.deleteById(id);
    }
}

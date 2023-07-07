package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.GameDetails;
import com.timoleon.gamedirectory.repository.GameDetailsRepository;
import com.timoleon.gamedirectory.service.dto.GameDetailsDTO;
import com.timoleon.gamedirectory.service.mapper.GameDetailsMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameDetails}.
 */
@Service
@Transactional
public class GameDetailsService {

    private final Logger log = LoggerFactory.getLogger(GameDetailsService.class);

    private final GameDetailsRepository gameDetailsRepository;

    private final GameDetailsMapper gameDetailsMapper;

    public GameDetailsService(GameDetailsRepository gameDetailsRepository, GameDetailsMapper gameDetailsMapper) {
        this.gameDetailsRepository = gameDetailsRepository;
        this.gameDetailsMapper = gameDetailsMapper;
    }

    /**
     * Save a gameDetails.
     *
     * @param gameDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public GameDetailsDTO save(GameDetailsDTO gameDetailsDTO) {
        log.debug("Request to save GameDetails : {}", gameDetailsDTO);
        GameDetails result = gameDetailsRepository.save(gameDetailsMapper.toEntity(gameDetailsDTO));

        return gameDetailsMapper.toDto(result);
    }

    /**
     * Update a gameDetails.
     *
     * @param gameDetails the entity to save.
     * @return the persisted entity.
     */
    public GameDetails update(GameDetails gameDetails) {
        log.debug("Request to update GameDetails : {}", gameDetails);
        return gameDetailsRepository.save(gameDetails);
    }

    /**
     * Partially update a gameDetails.
     *
     * @param gameDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GameDetails> partialUpdate(GameDetails gameDetails) {
        log.debug("Request to partially update GameDetails : {}", gameDetails);

        return gameDetailsRepository
            .findById(gameDetails.getId())
            .map(existingGameDetails -> {
                if (gameDetails.getReleaseDate() != null) {
                    existingGameDetails.setReleaseDate(gameDetails.getReleaseDate());
                }
                if (gameDetails.getPegiRating() != null) {
                    existingGameDetails.setPegiRating(gameDetails.getPegiRating());
                }
                if (gameDetails.getMetacriticScore() != null) {
                    existingGameDetails.setMetacriticScore(gameDetails.getMetacriticScore());
                }
                if (gameDetails.getImageUrl() != null) {
                    existingGameDetails.setImageUrl(gameDetails.getImageUrl());
                }
                if (gameDetails.getThumbnailUrl() != null) {
                    existingGameDetails.setThumbnailUrl(gameDetails.getThumbnailUrl());
                }
                if (gameDetails.getPrice() != null) {
                    existingGameDetails.setPrice(gameDetails.getPrice());
                }
                if (gameDetails.getDescription() != null) {
                    existingGameDetails.setDescription(gameDetails.getDescription());
                }
                if (gameDetails.getNotes() != null) {
                    existingGameDetails.setNotes(gameDetails.getNotes());
                }
                if (gameDetails.getSteamAppid() != null) {
                    existingGameDetails.setSteamAppid(gameDetails.getSteamAppid());
                }

                return existingGameDetails;
            })
            .map(gameDetailsRepository::save);
    }

    /**
     * Get all the gameDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GameDetails> findAll() {
        log.debug("Request to get all GameDetails");
        return gameDetailsRepository.findAll();
    }

    /**
     * Get all the gameDetails with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GameDetails> findAllWithEagerRelationships(Pageable pageable) {
        return gameDetailsRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get all the gameDetails where Game is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GameDetails> findAllWhereGameIsNull() {
        log.debug("Request to get all gameDetails where Game is null");
        return StreamSupport
            .stream(gameDetailsRepository.findAll().spliterator(), false)
            .filter(gameDetails -> gameDetails.getGame() == null)
            .toList();
    }

    /**
     * Get one gameDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GameDetails> findOne(Long id) {
        log.debug("Request to get GameDetails : {}", id);
        return gameDetailsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the gameDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GameDetails : {}", id);
        gameDetailsRepository.deleteById(id);
    }
}

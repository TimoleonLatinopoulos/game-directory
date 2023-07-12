package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.*;
import com.timoleon.gamedirectory.repository.GameRepository;
import com.timoleon.gamedirectory.service.dto.GameDTO;
import com.timoleon.gamedirectory.service.mapper.GameMapper;
import java.util.*;
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

    private final PlatformService platformService;
    private final DeveloperService developerService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;

    private final GameMapper gameMapper;

    public GameService(
        GameRepository gameRepository,
        PlatformService platformService,
        DeveloperService developerService,
        PublisherService publisherService,
        CategoryService categoryService,
        GameMapper gameMapper
    ) {
        this.gameRepository = gameRepository;
        this.platformService = platformService;
        this.developerService = developerService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
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

        List<Game> games = this.findByTitle(gameDTO.getTitle());
        if (!games.isEmpty()) {
            throw new IllegalStateException("This game title is already stored in the database!");
        }

        Set<Platform> platforms = gameDTO.getGameDetails().getPlatforms();
        Set<Developer> developers = gameDTO.getGameDetails().getDevelopers();
        Set<Category> categories = gameDTO.getGameDetails().getCategories();
        Set<Publisher> publishers = gameDTO.getGameDetails().getPublishers();

        if (!platforms.isEmpty()) {
            List<Platform> platformList = new ArrayList<>(platforms);

            for (int i = 0; i < platformList.size(); i++) {
                Optional<Platform> result = this.platformService.findOneByDescription(platformList.get(i).getDescription());
                if (!result.isPresent()) {
                    platformList.get(i).setId(null);
                } else {
                    platformList.set(i, result.get());
                }
            }
            gameDTO.getGameDetails().setPlatforms(new HashSet<>(platformList));
        }
        if (!developers.isEmpty()) {
            List<Developer> developerList = new ArrayList<>(developers);

            for (int i = 0; i < developerList.size(); i++) {
                Optional<Developer> result = this.developerService.findOneByDescription(developerList.get(i).getDescription());
                if (!result.isPresent()) {
                    developerList.get(i).setId(null);
                    Developer response = this.developerService.save(developerList.get(i));
                    developerList.set(i, response);
                } else {
                    developerList.set(i, result.get());
                }
            }
            gameDTO.getGameDetails().setDevelopers(new HashSet<>(developerList));
        }
        if (!categories.isEmpty()) {
            List<Category> categoryList = new ArrayList<>(categories);

            for (int i = 0; i < categoryList.size(); i++) {
                Optional<Category> result = this.categoryService.findOneByDescription(categoryList.get(i).getDescription());
                if (!result.isPresent()) {
                    categoryList.get(i).setId(null);
                    Category response = this.categoryService.save(categoryList.get(i));
                    categoryList.set(i, response);
                } else {
                    categoryList.set(i, result.get());
                }
            }
            gameDTO.getGameDetails().setCategories(new HashSet<>(categoryList));
        }
        if (!publishers.isEmpty()) {
            List<Publisher> publisherList = new ArrayList<>(publishers);

            for (int i = 0; i < publisherList.size(); i++) {
                Optional<Publisher> result = this.publisherService.findOneByDescription(publisherList.get(i).getDescription());
                if (!result.isPresent()) {
                    publisherList.get(i).setId(null);
                    Publisher response = this.publisherService.save(publisherList.get(i));
                    publisherList.set(i, response);
                } else {
                    publisherList.set(i, result.get());
                }
            }
            gameDTO.getGameDetails().setPublishers(new HashSet<>(publisherList));
        }

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
     * Get games by title.
     *
     * @param title the title of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public List<Game> findByTitle(String title) {
        log.debug("Request to get Game : {}", title);
        return gameRepository.findByTitle(title);
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

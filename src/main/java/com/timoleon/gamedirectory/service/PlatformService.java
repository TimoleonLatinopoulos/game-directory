package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.Platform;
import com.timoleon.gamedirectory.repository.PlatformRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Platform}.
 */
@Service
@Transactional
public class PlatformService {

    private final Logger log = LoggerFactory.getLogger(PlatformService.class);

    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    /**
     * Save a platform.
     *
     * @param platform the entity to save.
     * @return the persisted entity.
     */
    public Platform save(Platform platform) {
        log.debug("Request to save Platform : {}", platform);
        return platformRepository.save(platform);
    }

    /**
     * Update a platform.
     *
     * @param platform the entity to save.
     * @return the persisted entity.
     */
    public Platform update(Platform platform) {
        log.debug("Request to update Platform : {}", platform);
        return platformRepository.save(platform);
    }

    /**
     * Partially update a platform.
     *
     * @param platform the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Platform> partialUpdate(Platform platform) {
        log.debug("Request to partially update Platform : {}", platform);

        return platformRepository
            .findById(platform.getId())
            .map(existingPlatform -> {
                if (platform.getDescription() != null) {
                    existingPlatform.setDescription(platform.getDescription());
                }

                return existingPlatform;
            })
            .map(platformRepository::save);
    }

    /**
     * Get all the platforms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Platform> findAll() {
        log.debug("Request to get all Platforms");
        return platformRepository.findAll();
    }

    /**
     * Get one platform by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Platform> findOne(Long id) {
        log.debug("Request to get Platform : {}", id);
        return platformRepository.findById(id);
    }

    /**
     * Get one platform by description.
     *
     * @param description the description of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Platform> findOneByDescription(String description) {
        log.debug("Request to get Platform : {}", description);
        return platformRepository.findByDescription(description);
    }

    /**
     * Delete the platform by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Platform : {}", id);
        platformRepository.deleteById(id);
    }
}

package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.Developer;
import com.timoleon.gamedirectory.repository.DeveloperRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Developer}.
 */
@Service
@Transactional
public class DeveloperService {

    private final Logger log = LoggerFactory.getLogger(DeveloperService.class);

    private final DeveloperRepository developerRepository;

    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    /**
     * Save a developer.
     *
     * @param developer the entity to save.
     * @return the persisted entity.
     */
    public Developer save(Developer developer) {
        log.debug("Request to save Developer : {}", developer);
        return developerRepository.save(developer);
    }

    /**
     * Update a developer.
     *
     * @param developer the entity to save.
     * @return the persisted entity.
     */
    public Developer update(Developer developer) {
        log.debug("Request to update Developer : {}", developer);
        return developerRepository.save(developer);
    }

    /**
     * Partially update a developer.
     *
     * @param developer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Developer> partialUpdate(Developer developer) {
        log.debug("Request to partially update Developer : {}", developer);

        return developerRepository
            .findById(developer.getId())
            .map(existingDeveloper -> {
                if (developer.getDescription() != null) {
                    existingDeveloper.setDescription(developer.getDescription());
                }

                return existingDeveloper;
            })
            .map(developerRepository::save);
    }

    /**
     * Get all the developers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Developer> findAll() {
        log.debug("Request to get all Developers");
        return developerRepository.findAll();
    }

    /**
     * Get one developer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Developer> findOne(Long id) {
        log.debug("Request to get Developer : {}", id);
        return developerRepository.findById(id);
    }

    /**
     * Delete the developer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Developer : {}", id);
        developerRepository.deleteById(id);
    }
}
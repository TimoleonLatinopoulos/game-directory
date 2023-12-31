package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.Publisher;
import com.timoleon.gamedirectory.repository.PublisherRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Publisher}.
 */
@Service
@Transactional
public class PublisherService {

    private final Logger log = LoggerFactory.getLogger(PublisherService.class);

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * Save a publisher.
     *
     * @param publisher the entity to save.
     * @return the persisted entity.
     */
    public Publisher save(Publisher publisher) {
        log.debug("Request to save Publisher : {}", publisher);
        return publisherRepository.save(publisher);
    }

    /**
     * Update a publisher.
     *
     * @param publisher the entity to save.
     * @return the persisted entity.
     */
    public Publisher update(Publisher publisher) {
        log.debug("Request to update Publisher : {}", publisher);
        return publisherRepository.save(publisher);
    }

    /**
     * Partially update a publisher.
     *
     * @param publisher the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Publisher> partialUpdate(Publisher publisher) {
        log.debug("Request to partially update Publisher : {}", publisher);

        return publisherRepository
            .findById(publisher.getId())
            .map(existingPublisher -> {
                if (publisher.getDescription() != null) {
                    existingPublisher.setDescription(publisher.getDescription());
                }

                return existingPublisher;
            })
            .map(publisherRepository::save);
    }

    /**
     * Get all the publishers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Publisher> findAll() {
        log.debug("Request to get all Publishers");
        return publisherRepository.findAll();
    }

    /**
     * Search for publishers.
     *
     * @param input the input for the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Publisher> findLike(String input) {
        log.debug("Request to search for Publishers");
        if (input != null) {
            input = "%" + input + "%";
        }
        return publisherRepository.findLike(input, PageRequest.of(0, 20));
    }

    /**
     * Get one publisher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Publisher> findOne(Long id) {
        log.debug("Request to get Publisher : {}", id);
        return publisherRepository.findById(id);
    }

    /**
     * Get one publisher by description.
     *
     * @param description the description of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Publisher> findOneByDescription(String description) {
        log.debug("Request to get Publisher : {}", description);
        return publisherRepository.findByDescription(description);
    }

    /**
     * Delete the publisher by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Publisher : {}", id);
        publisherRepository.deleteById(id);
    }
}

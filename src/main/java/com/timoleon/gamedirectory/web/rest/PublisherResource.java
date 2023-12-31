package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.Publisher;
import com.timoleon.gamedirectory.service.PublisherService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.Publisher}.
 */
@RestController
@RequestMapping("/api")
public class PublisherResource {

    private final Logger log = LoggerFactory.getLogger(PublisherResource.class);

    private final PublisherService publisherService;

    public PublisherResource(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * {@code GET  /publishers} : get all the publishers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishers in body.
     */
    @GetMapping("/publishers")
    public List<Publisher> getAllPublishers() {
        log.debug("REST request to get all Publishers");
        return publisherService.findAll();
    }

    /**
     * {@code GET  /publishers/search} : search for publishers.
     *
     * @param input the input for the search.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishers in body.
     */
    @GetMapping("/publishers/search")
    public List<Publisher> searchPublishers(@RequestParam String input) {
        log.debug("REST request to search for Publishers");
        return publisherService.findLike(input);
    }

    /**
     * {@code GET  /publishers/:id} : get the "id" publisher.
     *
     * @param id the id of the publisher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publisher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/publishers/{id}")
    public ResponseEntity<Publisher> getPublisher(@PathVariable Long id) {
        log.debug("REST request to get Publisher : {}", id);
        Optional<Publisher> publisher = publisherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publisher);
    }
}

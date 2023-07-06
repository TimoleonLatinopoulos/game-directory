package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.Developer;
import com.timoleon.gamedirectory.repository.DeveloperRepository;
import com.timoleon.gamedirectory.service.DeveloperService;
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
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.Developer}.
 */
@RestController
@RequestMapping("/api")
public class DeveloperResource {

    private final Logger log = LoggerFactory.getLogger(DeveloperResource.class);

    private final DeveloperService developerService;

    private final DeveloperRepository developerRepository;

    public DeveloperResource(DeveloperService developerService, DeveloperRepository developerRepository) {
        this.developerService = developerService;
        this.developerRepository = developerRepository;
    }

    /**
     * {@code GET  /developers} : get all the developers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of developers in body.
     */
    @GetMapping("/developers")
    public List<Developer> getAllDevelopers() {
        log.debug("REST request to get all Developers");
        return developerService.findAll();
    }

    /**
     * {@code GET  /developers/:id} : get the "id" developer.
     *
     * @param id the id of the developer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the developer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/developers/{id}")
    public ResponseEntity<Developer> getDeveloper(@PathVariable Long id) {
        log.debug("REST request to get Developer : {}", id);
        Optional<Developer> developer = developerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(developer);
    }
}

package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.Platform;
import com.timoleon.gamedirectory.repository.PlatformRepository;
import com.timoleon.gamedirectory.service.PlatformService;
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
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.Platform}.
 */
@RestController
@RequestMapping("/api")
public class PlatformResource {

    private final Logger log = LoggerFactory.getLogger(PlatformResource.class);

    private final PlatformService platformService;

    private final PlatformRepository platformRepository;

    public PlatformResource(PlatformService platformService, PlatformRepository platformRepository) {
        this.platformService = platformService;
        this.platformRepository = platformRepository;
    }

    /**
     * {@code GET  /platforms} : get all the platforms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of platforms in body.
     */
    @GetMapping("/platforms")
    public List<Platform> getAllPlatforms() {
        log.debug("REST request to get all Platforms");
        return platformService.findAll();
    }

    /**
     * {@code GET  /platforms/:id} : get the "id" platform.
     *
     * @param id the id of the platform to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the platform, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/platforms/{id}")
    public ResponseEntity<Platform> getPlatform(@PathVariable Long id) {
        log.debug("REST request to get Platform : {}", id);
        Optional<Platform> platform = platformService.findOne(id);
        return ResponseUtil.wrapOrNotFound(platform);
    }
}

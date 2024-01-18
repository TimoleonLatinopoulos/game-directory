package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.Platform;
import com.timoleon.gamedirectory.service.PlatformService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.Platform}.
 */
@RestController
@RequestMapping("/api")
public class PlatformResource {

    private final Logger log = LoggerFactory.getLogger(PlatformResource.class);

    private final PlatformService platformService;

    public PlatformResource(PlatformService platformService) {
        this.platformService = platformService;
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
     * {@code GET  /platforms/search} : search for platforms.
     *
     * @param input the input for the search.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of platforms in body.
     */
    @GetMapping("/platforms/search")
    public List<Platform> searchPlatforms(@RequestParam String input) {
        log.debug("REST request to search for Platforms");
        return platformService.findLike(input);
    }

    /**
     * {@code GET  /platforms/search_used} : search for platforms used in GameDetails.
     *
     * @param input the input for the search.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of platforms in body.
     */
    @GetMapping("/platforms/search_used")
    public List<Platform> searchPlatformsUsed(@RequestParam String input) {
        log.debug("REST request to search for Platforms used in GameDetails");
        return platformService.findLikeUsed(input);
    }

    /**
     * {@code GET  /platforms/used} : get all the platforms used in gameDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of platforms in body.
     */
    @GetMapping("/platforms/used")
    public List<Platform> getAllUsedPlatforms() {
        log.debug("REST request to get all Platforms");
        return platformService.findAllUsed();
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

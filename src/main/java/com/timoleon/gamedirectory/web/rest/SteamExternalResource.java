package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.service.SteamExternalService;
import com.timoleon.gamedirectory.service.dto.steam.SteamGameDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Game}.
 */
@RestController
@RequestMapping("/api")
public class SteamExternalResource extends AbstractApiResource {

    private final Logger log = LoggerFactory.getLogger(SteamExternalResource.class);

    private static final String ENTITY_NAME = "steam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private SteamExternalService steamExternalService;

    public SteamExternalResource(SteamExternalService steamExternalService) {
        this.steamExternalService = steamExternalService;
    }

    /**
     * {@code GET  /steam-games/:apiid} : get the "apiid" game from steam external api.
     *
     * @param apiid the apiid of the game to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the game, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/steam-games/{apiid}")
    public ResponseEntity<SteamGameDTO> getGameBySteamApiid(@PathVariable Long apiid) {
        log.debug("REST request to get Steam Game with apiid : {}", apiid);
        SteamGameDTO game = steamExternalService.getSteamGame(apiid);
        return ResponseUtil.wrapOrNotFound(Optional.of(game));
    }
}

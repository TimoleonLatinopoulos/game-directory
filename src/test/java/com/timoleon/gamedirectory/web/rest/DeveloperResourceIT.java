package com.timoleon.gamedirectory.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.timoleon.gamedirectory.IntegrationTest;
import com.timoleon.gamedirectory.domain.Developer;
import com.timoleon.gamedirectory.repository.DeveloperRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeveloperResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeveloperResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/developers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeveloperMockMvc;

    private Developer developer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Developer createEntity(EntityManager em) {
        Developer developer = new Developer().description(DEFAULT_DESCRIPTION);
        return developer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Developer createUpdatedEntity(EntityManager em) {
        Developer developer = new Developer().description(UPDATED_DESCRIPTION);
        return developer;
    }

    @BeforeEach
    public void initTest() {
        developer = createEntity(em);
    }

    @Test
    @Transactional
    void getAllDevelopers() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get all the developerList
        restDeveloperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(developer.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get the developer
        restDeveloperMockMvc
            .perform(get(ENTITY_API_URL_ID, developer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(developer.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDeveloper() throws Exception {
        // Get the developer
        restDeveloperMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}

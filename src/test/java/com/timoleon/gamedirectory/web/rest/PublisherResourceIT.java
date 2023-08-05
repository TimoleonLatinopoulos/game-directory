package com.timoleon.gamedirectory.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.timoleon.gamedirectory.IntegrationTest;
import com.timoleon.gamedirectory.domain.Publisher;
import com.timoleon.gamedirectory.repository.PublisherRepository;
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
 * Integration tests for the {@link PublisherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PublisherResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/publishers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublisherMockMvc;

    private Publisher publisher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createEntity(EntityManager em) {
        Publisher publisher = new Publisher().description(DEFAULT_DESCRIPTION);
        return publisher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createUpdatedEntity(EntityManager em) {
        Publisher publisher = new Publisher().description(UPDATED_DESCRIPTION);
        return publisher;
    }

    @BeforeEach
    public void initTest() {
        publisher = createEntity(em);
    }

    @Test
    @Transactional
    void getAllPublishers() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get the publisher
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL_ID, publisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publisher.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPublisher() throws Exception {
        // Get the publisher
        restPublisherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}

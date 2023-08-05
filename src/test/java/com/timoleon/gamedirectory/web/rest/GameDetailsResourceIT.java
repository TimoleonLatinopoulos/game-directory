package com.timoleon.gamedirectory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.timoleon.gamedirectory.IntegrationTest;
import com.timoleon.gamedirectory.domain.GameDetails;
import com.timoleon.gamedirectory.domain.enumerations.PegiRating;
import com.timoleon.gamedirectory.repository.GameDetailsRepository;
import com.timoleon.gamedirectory.service.GameDetailsService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GameDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GameDetailsResourceIT {

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PegiRating DEFAULT_PEGI_RATING = PegiRating.Seven;
    private static final PegiRating UPDATED_PEGI_RATING = PegiRating.Eighteen;

    private static final Integer DEFAULT_METACRITIC_SCORE = 1;
    private static final Integer UPDATED_METACRITIC_SCORE = 2;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_URL = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Long DEFAULT_STEAM_APPID = 1L;
    private static final Long UPDATED_STEAM_APPID = 2L;

    private static final String ENTITY_API_URL = "/api/game-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameDetailsRepository gameDetailsRepository;

    @Mock
    private GameDetailsRepository gameDetailsRepositoryMock;

    @Mock
    private GameDetailsService gameDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameDetailsMockMvc;

    private GameDetails gameDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameDetails createEntity(EntityManager em) {
        GameDetails gameDetails = new GameDetails()
            .releaseDate(DEFAULT_RELEASE_DATE)
            .pegiRating(DEFAULT_PEGI_RATING)
            .metacriticScore(DEFAULT_METACRITIC_SCORE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .thumbnailUrl(DEFAULT_THUMBNAIL_URL)
            .price(DEFAULT_PRICE)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .steamAppid(DEFAULT_STEAM_APPID);
        return gameDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameDetails createUpdatedEntity(EntityManager em) {
        GameDetails gameDetails = new GameDetails()
            .releaseDate(UPDATED_RELEASE_DATE)
            .pegiRating(UPDATED_PEGI_RATING)
            .metacriticScore(UPDATED_METACRITIC_SCORE)
            .imageUrl(UPDATED_IMAGE_URL)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .price(UPDATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .steamAppid(UPDATED_STEAM_APPID);
        return gameDetails;
    }

    @BeforeEach
    public void initTest() {
        gameDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createGameDetails() throws Exception {
        int databaseSizeBeforeCreate = gameDetailsRepository.findAll().size();
        // Create the GameDetails
        restGameDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDetails)))
            .andExpect(status().isCreated());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        GameDetails testGameDetails = gameDetailsList.get(gameDetailsList.size() - 1);
        assertThat(testGameDetails.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testGameDetails.getPegiRating()).isEqualTo(DEFAULT_PEGI_RATING);
        assertThat(testGameDetails.getMetacriticScore()).isEqualTo(DEFAULT_METACRITIC_SCORE);
        assertThat(testGameDetails.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testGameDetails.getThumbnailUrl()).isEqualTo(DEFAULT_THUMBNAIL_URL);
        assertThat(testGameDetails.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testGameDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGameDetails.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testGameDetails.getSteamAppid()).isEqualTo(DEFAULT_STEAM_APPID);
    }

    @Test
    @Transactional
    void createGameDetailsWithExistingId() throws Exception {
        // Create the GameDetails with an existing ID
        gameDetails.setId(1L);

        int databaseSizeBeforeCreate = gameDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDetails)))
            .andExpect(status().isBadRequest());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameDetails() throws Exception {
        // Initialize the database
        gameDetailsRepository.saveAndFlush(gameDetails);

        // Get all the gameDetailsList
        restGameDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].pegiRating").value(hasItem(DEFAULT_PEGI_RATING)))
            .andExpect(jsonPath("$.[*].metacriticScore").value(hasItem(DEFAULT_METACRITIC_SCORE)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].thumbnailUrl").value(hasItem(DEFAULT_THUMBNAIL_URL)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].steamAppid").value(hasItem(DEFAULT_STEAM_APPID.intValue())));
    }

    void getAllGameDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(gameDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        restGameDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gameDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    void getAllGameDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gameDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        restGameDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(gameDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGameDetails() throws Exception {
        // Initialize the database
        gameDetailsRepository.saveAndFlush(gameDetails);

        // Get the gameDetails
        restGameDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, gameDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameDetails.getId().intValue()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.pegiRating").value(DEFAULT_PEGI_RATING))
            .andExpect(jsonPath("$.metacriticScore").value(DEFAULT_METACRITIC_SCORE))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.thumbnailUrl").value(DEFAULT_THUMBNAIL_URL))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.steamAppid").value(DEFAULT_STEAM_APPID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingGameDetails() throws Exception {
        // Get the gameDetails
        restGameDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameDetails() throws Exception {
        // Initialize the database
        gameDetailsRepository.saveAndFlush(gameDetails);

        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();

        // Update the gameDetails
        GameDetails updatedGameDetails = gameDetailsRepository.findById(gameDetails.getId()).get();
        // Disconnect from session so that the updates on updatedGameDetails are not
        // directly saved in db
        em.detach(updatedGameDetails);
        updatedGameDetails
            .releaseDate(UPDATED_RELEASE_DATE)
            .pegiRating(UPDATED_PEGI_RATING)
            .metacriticScore(UPDATED_METACRITIC_SCORE)
            .imageUrl(UPDATED_IMAGE_URL)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .price(UPDATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .steamAppid(UPDATED_STEAM_APPID);

        restGameDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameDetails))
            )
            .andExpect(status().isOk());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
        GameDetails testGameDetails = gameDetailsList.get(gameDetailsList.size() - 1);
        assertThat(testGameDetails.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testGameDetails.getPegiRating()).isEqualTo(UPDATED_PEGI_RATING);
        assertThat(testGameDetails.getMetacriticScore()).isEqualTo(UPDATED_METACRITIC_SCORE);
        assertThat(testGameDetails.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testGameDetails.getThumbnailUrl()).isEqualTo(UPDATED_THUMBNAIL_URL);
        assertThat(testGameDetails.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testGameDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGameDetails.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testGameDetails.getSteamAppid()).isEqualTo(UPDATED_STEAM_APPID);
    }

    @Test
    @Transactional
    void putNonExistingGameDetails() throws Exception {
        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();
        gameDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameDetails() throws Exception {
        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();
        gameDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameDetails() throws Exception {
        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();
        gameDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameDetailsWithPatch() throws Exception {
        // Initialize the database
        gameDetailsRepository.saveAndFlush(gameDetails);

        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();

        // Update the gameDetails using partial update
        GameDetails partialUpdatedGameDetails = new GameDetails();
        partialUpdatedGameDetails.setId(gameDetails.getId());

        partialUpdatedGameDetails.imageUrl(UPDATED_IMAGE_URL).thumbnailUrl(UPDATED_THUMBNAIL_URL);

        restGameDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameDetails))
            )
            .andExpect(status().isOk());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
        GameDetails testGameDetails = gameDetailsList.get(gameDetailsList.size() - 1);
        assertThat(testGameDetails.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testGameDetails.getPegiRating()).isEqualTo(DEFAULT_PEGI_RATING);
        assertThat(testGameDetails.getMetacriticScore()).isEqualTo(DEFAULT_METACRITIC_SCORE);
        assertThat(testGameDetails.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testGameDetails.getThumbnailUrl()).isEqualTo(UPDATED_THUMBNAIL_URL);
        assertThat(testGameDetails.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testGameDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGameDetails.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testGameDetails.getSteamAppid()).isEqualTo(DEFAULT_STEAM_APPID);
    }

    @Test
    @Transactional
    void fullUpdateGameDetailsWithPatch() throws Exception {
        // Initialize the database
        gameDetailsRepository.saveAndFlush(gameDetails);

        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();

        // Update the gameDetails using partial update
        GameDetails partialUpdatedGameDetails = new GameDetails();
        partialUpdatedGameDetails.setId(gameDetails.getId());

        partialUpdatedGameDetails
            .releaseDate(UPDATED_RELEASE_DATE)
            .pegiRating(UPDATED_PEGI_RATING)
            .metacriticScore(UPDATED_METACRITIC_SCORE)
            .imageUrl(UPDATED_IMAGE_URL)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .price(UPDATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .steamAppid(UPDATED_STEAM_APPID);

        restGameDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameDetails))
            )
            .andExpect(status().isOk());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
        GameDetails testGameDetails = gameDetailsList.get(gameDetailsList.size() - 1);
        assertThat(testGameDetails.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testGameDetails.getPegiRating()).isEqualTo(UPDATED_PEGI_RATING);
        assertThat(testGameDetails.getMetacriticScore()).isEqualTo(UPDATED_METACRITIC_SCORE);
        assertThat(testGameDetails.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testGameDetails.getThumbnailUrl()).isEqualTo(UPDATED_THUMBNAIL_URL);
        assertThat(testGameDetails.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testGameDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGameDetails.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testGameDetails.getSteamAppid()).isEqualTo(UPDATED_STEAM_APPID);
    }

    @Test
    @Transactional
    void patchNonExistingGameDetails() throws Exception {
        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();
        gameDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameDetails() throws Exception {
        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();
        gameDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameDetails() throws Exception {
        int databaseSizeBeforeUpdate = gameDetailsRepository.findAll().size();
        gameDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameDetails in the database
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameDetails() throws Exception {
        // Initialize the database
        gameDetailsRepository.saveAndFlush(gameDetails);

        int databaseSizeBeforeDelete = gameDetailsRepository.findAll().size();

        // Delete the gameDetails
        restGameDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameDetails> gameDetailsList = gameDetailsRepository.findAll();
        assertThat(gameDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

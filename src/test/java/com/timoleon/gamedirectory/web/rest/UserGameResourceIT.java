package com.timoleon.gamedirectory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.timoleon.gamedirectory.IntegrationTest;
import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.repository.UserGameRepository;
import com.timoleon.gamedirectory.service.UserGameService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserGameResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserGameResourceIT {

    private static final Boolean DEFAULT_FAVOURITE = false;
    private static final Boolean UPDATED_FAVOURITE = true;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserGameRepository userGameRepository;

    @Mock
    private UserGameRepository userGameRepositoryMock;

    @Mock
    private UserGameService userGameServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserGameMockMvc;

    private UserGame userGame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGame createEntity(EntityManager em) {
        UserGame userGame = new UserGame().favourite(DEFAULT_FAVOURITE).dateAdded(DEFAULT_DATE_ADDED).title(DEFAULT_TITLE);
        return userGame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGame createUpdatedEntity(EntityManager em) {
        UserGame userGame = new UserGame().favourite(UPDATED_FAVOURITE).dateAdded(UPDATED_DATE_ADDED).title(UPDATED_TITLE);
        return userGame;
    }

    @BeforeEach
    public void initTest() {
        userGame = createEntity(em);
    }

    @Test
    @Transactional
    void createUserGame() throws Exception {
        int databaseSizeBeforeCreate = userGameRepository.findAll().size();
        // Create the UserGame
        restUserGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userGame)))
            .andExpect(status().isCreated());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeCreate + 1);
        UserGame testUserGame = userGameList.get(userGameList.size() - 1);
        assertThat(testUserGame.getFavourite()).isEqualTo(DEFAULT_FAVOURITE);
        assertThat(testUserGame.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testUserGame.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createUserGameWithExistingId() throws Exception {
        // Create the UserGame with an existing ID
        userGame.setId(1L);

        int databaseSizeBeforeCreate = userGameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userGame)))
            .andExpect(status().isBadRequest());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserGames() throws Exception {
        // Initialize the database
        userGameRepository.saveAndFlush(userGame);

        // Get all the userGameList
        restUserGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGame.getId().intValue())))
            .andExpect(jsonPath("$.[*].favourite").value(hasItem(DEFAULT_FAVOURITE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserGamesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userGameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userGameServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserGamesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userGameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userGameRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserGame() throws Exception {
        // Initialize the database
        userGameRepository.saveAndFlush(userGame);

        // Get the userGame
        restUserGameMockMvc
            .perform(get(ENTITY_API_URL_ID, userGame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userGame.getId().intValue()))
            .andExpect(jsonPath("$.favourite").value(DEFAULT_FAVOURITE.booleanValue()))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingUserGame() throws Exception {
        // Get the userGame
        restUserGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserGame() throws Exception {
        // Initialize the database
        userGameRepository.saveAndFlush(userGame);

        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();

        // Update the userGame
        UserGame updatedUserGame = userGameRepository.findById(userGame.getId()).get();
        // Disconnect from session so that the updates on updatedUserGame are not directly saved in db
        em.detach(updatedUserGame);
        updatedUserGame.favourite(UPDATED_FAVOURITE).dateAdded(UPDATED_DATE_ADDED).title(UPDATED_TITLE);

        restUserGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserGame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserGame))
            )
            .andExpect(status().isOk());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
        UserGame testUserGame = userGameList.get(userGameList.size() - 1);
        assertThat(testUserGame.getFavourite()).isEqualTo(UPDATED_FAVOURITE);
        assertThat(testUserGame.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserGame.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingUserGame() throws Exception {
        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();
        userGame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userGame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserGame() throws Exception {
        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();
        userGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserGame() throws Exception {
        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();
        userGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userGame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserGameWithPatch() throws Exception {
        // Initialize the database
        userGameRepository.saveAndFlush(userGame);

        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();

        // Update the userGame using partial update
        UserGame partialUpdatedUserGame = new UserGame();
        partialUpdatedUserGame.setId(userGame.getId());

        partialUpdatedUserGame.favourite(UPDATED_FAVOURITE).dateAdded(UPDATED_DATE_ADDED);

        restUserGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserGame))
            )
            .andExpect(status().isOk());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
        UserGame testUserGame = userGameList.get(userGameList.size() - 1);
        assertThat(testUserGame.getFavourite()).isEqualTo(UPDATED_FAVOURITE);
        assertThat(testUserGame.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserGame.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateUserGameWithPatch() throws Exception {
        // Initialize the database
        userGameRepository.saveAndFlush(userGame);

        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();

        // Update the userGame using partial update
        UserGame partialUpdatedUserGame = new UserGame();
        partialUpdatedUserGame.setId(userGame.getId());

        partialUpdatedUserGame.favourite(UPDATED_FAVOURITE).dateAdded(UPDATED_DATE_ADDED).title(UPDATED_TITLE);

        restUserGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserGame))
            )
            .andExpect(status().isOk());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
        UserGame testUserGame = userGameList.get(userGameList.size() - 1);
        assertThat(testUserGame.getFavourite()).isEqualTo(UPDATED_FAVOURITE);
        assertThat(testUserGame.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserGame.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingUserGame() throws Exception {
        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();
        userGame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserGame() throws Exception {
        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();
        userGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserGame() throws Exception {
        int databaseSizeBeforeUpdate = userGameRepository.findAll().size();
        userGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userGame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserGame in the database
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserGame() throws Exception {
        // Initialize the database
        userGameRepository.saveAndFlush(userGame);

        int databaseSizeBeforeDelete = userGameRepository.findAll().size();

        // Delete the userGame
        restUserGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, userGame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserGame> userGameList = userGameRepository.findAll();
        assertThat(userGameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

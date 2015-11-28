package io.gof.peyek.web.rest;

import io.gof.peyek.Application;
import io.gof.peyek.domain.UserActivity;
import io.gof.peyek.repository.UserActivityRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.gof.peyek.domain.enumeration.Type;

/**
 * Test class for the UserActivityResource REST controller.
 *
 * @see UserActivityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserActivityResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";


private static final Type DEFAULT_TYPE = Type.COMMENT;
    private static final Type UPDATED_TYPE = Type.VOTE;

    @Inject
    private UserActivityRepository userActivityRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserActivityMockMvc;

    private UserActivity userActivity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserActivityResource userActivityResource = new UserActivityResource();
        ReflectionTestUtils.setField(userActivityResource, "userActivityRepository", userActivityRepository);
        this.restUserActivityMockMvc = MockMvcBuilders.standaloneSetup(userActivityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userActivity = new UserActivity();
        userActivity.setName(DEFAULT_NAME);
        userActivity.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createUserActivity() throws Exception {
        int databaseSizeBeforeCreate = userActivityRepository.findAll().size();

        // Create the UserActivity

        restUserActivityMockMvc.perform(post("/api/userActivitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userActivity)))
                .andExpect(status().isCreated());

        // Validate the UserActivity in the database
        List<UserActivity> userActivitys = userActivityRepository.findAll();
        assertThat(userActivitys).hasSize(databaseSizeBeforeCreate + 1);
        UserActivity testUserActivity = userActivitys.get(userActivitys.size() - 1);
        assertThat(testUserActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserActivity.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userActivityRepository.findAll().size();
        // set the field null
        userActivity.setName(null);

        // Create the UserActivity, which fails.

        restUserActivityMockMvc.perform(post("/api/userActivitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userActivity)))
                .andExpect(status().isBadRequest());

        List<UserActivity> userActivitys = userActivityRepository.findAll();
        assertThat(userActivitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userActivityRepository.findAll().size();
        // set the field null
        userActivity.setType(null);

        // Create the UserActivity, which fails.

        restUserActivityMockMvc.perform(post("/api/userActivitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userActivity)))
                .andExpect(status().isBadRequest());

        List<UserActivity> userActivitys = userActivityRepository.findAll();
        assertThat(userActivitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserActivitys() throws Exception {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity);

        // Get all the userActivitys
        restUserActivityMockMvc.perform(get("/api/userActivitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userActivity.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getUserActivity() throws Exception {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity);

        // Get the userActivity
        restUserActivityMockMvc.perform(get("/api/userActivitys/{id}", userActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userActivity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserActivity() throws Exception {
        // Get the userActivity
        restUserActivityMockMvc.perform(get("/api/userActivitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserActivity() throws Exception {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity);

		int databaseSizeBeforeUpdate = userActivityRepository.findAll().size();

        // Update the userActivity
        userActivity.setName(UPDATED_NAME);
        userActivity.setType(UPDATED_TYPE);

        restUserActivityMockMvc.perform(put("/api/userActivitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userActivity)))
                .andExpect(status().isOk());

        // Validate the UserActivity in the database
        List<UserActivity> userActivitys = userActivityRepository.findAll();
        assertThat(userActivitys).hasSize(databaseSizeBeforeUpdate);
        UserActivity testUserActivity = userActivitys.get(userActivitys.size() - 1);
        assertThat(testUserActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserActivity.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteUserActivity() throws Exception {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity);

		int databaseSizeBeforeDelete = userActivityRepository.findAll().size();

        // Get the userActivity
        restUserActivityMockMvc.perform(delete("/api/userActivitys/{id}", userActivity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserActivity> userActivitys = userActivityRepository.findAll();
        assertThat(userActivitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}

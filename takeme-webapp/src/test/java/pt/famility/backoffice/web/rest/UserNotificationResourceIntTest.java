package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.UserNotification;
import pt.famility.backoffice.repository.UserNotificationRepository;
import pt.famility.backoffice.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static pt.famility.backoffice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserNotificationResource REST controller.
 *
 * @see UserNotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class UserNotificationResourceIntTest {

    private static final Instant DEFAULT_SENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserNotificationMockMvc;

    private UserNotification userNotification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserNotificationResource userNotificationResource = new UserNotificationResource(userNotificationRepository);
        this.restUserNotificationMockMvc = MockMvcBuilders.standaloneSetup(userNotificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserNotification createEntity(EntityManager em) {
        UserNotification userNotification = new UserNotification()
            .sentDate(DEFAULT_SENT_DATE)
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY);

        return userNotification;
    }

    @Before
    public void initTest() {
        userNotification = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserNotification() throws Exception {
        int databaseSizeBeforeCreate = userNotificationRepository.findAll().size();

        // Create the UserNotification
        restUserNotificationMockMvc.perform(post("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userNotification)))
            .andExpect(status().isCreated());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getSentDate()).isEqualTo(DEFAULT_SENT_DATE);
        assertThat(testUserNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testUserNotification.getBody()).isEqualTo(DEFAULT_BODY);
    }

    @Test
    @Transactional
    public void createUserNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userNotificationRepository.findAll().size();

        // Create the UserNotification with an existing ID
        userNotification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserNotificationMockMvc.perform(post("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userNotification)))
            .andExpect(status().isBadRequest());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userNotificationRepository.findAll().size();
        // set the field null
        userNotification.setUser(null);

        // Create the UserNotification, which fails.

        restUserNotificationMockMvc.perform(post("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userNotification)))
            .andExpect(status().isBadRequest());

        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserNotifications() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList
        restUserNotificationMockMvc.perform(get("/api/user-notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].sentDate").value(hasItem(DEFAULT_SENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getUserNotification() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get the userNotification
        restUserNotificationMockMvc.perform(get("/api/user-notifications/{id}", userNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userNotification.getId().intValue()))
            .andExpect(jsonPath("$.sentDate").value(DEFAULT_SENT_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserNotification() throws Exception {
        // Get the userNotification
        restUserNotificationMockMvc.perform(get("/api/user-notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserNotification() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();

        // Update the userNotification
        UserNotification updatedUserNotification = userNotificationRepository.findById(userNotification.getId()).get();
        // Disconnect from session so that the updates on updatedUserNotification are not directly saved in db
        em.detach(updatedUserNotification);
        updatedUserNotification
            .sentDate(UPDATED_SENT_DATE)
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY);

        restUserNotificationMockMvc.perform(put("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserNotification)))
            .andExpect(status().isOk());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getSentDate()).isEqualTo(UPDATED_SENT_DATE);
        assertThat(testUserNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserNotification.getBody()).isEqualTo(UPDATED_BODY);
    }

    @Test
    @Transactional
    public void updateNonExistingUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();

        // Create the UserNotification

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserNotificationMockMvc.perform(put("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userNotification)))
            .andExpect(status().isBadRequest());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserNotification() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        int databaseSizeBeforeDelete = userNotificationRepository.findAll().size();

        // Get the userNotification
        restUserNotificationMockMvc.perform(delete("/api/user-notifications/{id}", userNotification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserNotification.class);
        UserNotification userNotification1 = new UserNotification();
        userNotification1.setId(1L);
        UserNotification userNotification2 = new UserNotification();
        userNotification2.setId(userNotification1.getId());
        assertThat(userNotification1).isEqualTo(userNotification2);
        userNotification2.setId(2L);
        assertThat(userNotification1).isNotEqualTo(userNotification2);
        userNotification1.setId(null);
        assertThat(userNotification1).isNotEqualTo(userNotification2);
    }
}

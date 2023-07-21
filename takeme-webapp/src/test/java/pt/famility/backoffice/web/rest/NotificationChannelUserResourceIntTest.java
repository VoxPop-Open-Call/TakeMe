package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.NotificationChannelUser;
import pt.famility.backoffice.repository.NotificationChannelUserRepository;
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
import java.util.List;


import static pt.famility.backoffice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NotificationChannelUserResource REST controller.
 *
 * @see NotificationChannelUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class NotificationChannelUserResourceIntTest {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private NotificationChannelUserRepository notificationChannelUserRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationChannelUserMockMvc;

    private NotificationChannelUser notificationChannelUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationChannelUserResource notificationChannelUserResource = new NotificationChannelUserResource(notificationChannelUserRepository);
        this.restNotificationChannelUserMockMvc = MockMvcBuilders.standaloneSetup(notificationChannelUserResource)
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
    public static NotificationChannelUser createEntity(EntityManager em) {
        NotificationChannelUser notificationChannelUser = new NotificationChannelUser()
            .active(DEFAULT_ACTIVE);
        return notificationChannelUser;
    }

    @Before
    public void initTest() {
        notificationChannelUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationChannelUser() throws Exception {
        int databaseSizeBeforeCreate = notificationChannelUserRepository.findAll().size();

        // Create the NotificationChannelUser
        restNotificationChannelUserMockMvc.perform(post("/api/notification-channel-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationChannelUser)))
            .andExpect(status().isCreated());

        // Validate the NotificationChannelUser in the database
        List<NotificationChannelUser> notificationChannelUserList = notificationChannelUserRepository.findAll();
        assertThat(notificationChannelUserList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationChannelUser testNotificationChannelUser = notificationChannelUserList.get(notificationChannelUserList.size() - 1);
        assertThat(testNotificationChannelUser.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createNotificationChannelUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationChannelUserRepository.findAll().size();

        // Create the NotificationChannelUser with an existing ID
        notificationChannelUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationChannelUserMockMvc.perform(post("/api/notification-channel-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationChannelUser)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationChannelUser in the database
        List<NotificationChannelUser> notificationChannelUserList = notificationChannelUserRepository.findAll();
        assertThat(notificationChannelUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationChannelUserRepository.findAll().size();
        // set the field null
        notificationChannelUser.setUser(null);

        // Create the NotificationChannelUser, which fails.

        restNotificationChannelUserMockMvc.perform(post("/api/notification-channel-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationChannelUser)))
            .andExpect(status().isBadRequest());

        List<NotificationChannelUser> notificationChannelUserList = notificationChannelUserRepository.findAll();
        assertThat(notificationChannelUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationChannelUsers() throws Exception {
        // Initialize the database
        notificationChannelUserRepository.saveAndFlush(notificationChannelUser);

        // Get all the notificationChannelUserList
        restNotificationChannelUserMockMvc.perform(get("/api/notification-channel-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationChannelUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getNotificationChannelUser() throws Exception {
        // Initialize the database
        notificationChannelUserRepository.saveAndFlush(notificationChannelUser);

        // Get the notificationChannelUser
        restNotificationChannelUserMockMvc.perform(get("/api/notification-channel-users/{id}", notificationChannelUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificationChannelUser.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationChannelUser() throws Exception {
        // Get the notificationChannelUser
        restNotificationChannelUserMockMvc.perform(get("/api/notification-channel-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationChannelUser() throws Exception {
        // Initialize the database
        notificationChannelUserRepository.saveAndFlush(notificationChannelUser);

        int databaseSizeBeforeUpdate = notificationChannelUserRepository.findAll().size();

        // Update the notificationChannelUser
        NotificationChannelUser updatedNotificationChannelUser = notificationChannelUserRepository.findById(notificationChannelUser.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationChannelUser are not directly saved in db
        em.detach(updatedNotificationChannelUser);
        updatedNotificationChannelUser
            .active(UPDATED_ACTIVE);

        restNotificationChannelUserMockMvc.perform(put("/api/notification-channel-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotificationChannelUser)))
            .andExpect(status().isOk());

        // Validate the NotificationChannelUser in the database
        List<NotificationChannelUser> notificationChannelUserList = notificationChannelUserRepository.findAll();
        assertThat(notificationChannelUserList).hasSize(databaseSizeBeforeUpdate);
        NotificationChannelUser testNotificationChannelUser = notificationChannelUserList.get(notificationChannelUserList.size() - 1);
        assertThat(testNotificationChannelUser.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationChannelUser() throws Exception {
        int databaseSizeBeforeUpdate = notificationChannelUserRepository.findAll().size();

        // Create the NotificationChannelUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationChannelUserMockMvc.perform(put("/api/notification-channel-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationChannelUser)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationChannelUser in the database
        List<NotificationChannelUser> notificationChannelUserList = notificationChannelUserRepository.findAll();
        assertThat(notificationChannelUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotificationChannelUser() throws Exception {
        // Initialize the database
        notificationChannelUserRepository.saveAndFlush(notificationChannelUser);

        int databaseSizeBeforeDelete = notificationChannelUserRepository.findAll().size();

        // Get the notificationChannelUser
        restNotificationChannelUserMockMvc.perform(delete("/api/notification-channel-users/{id}", notificationChannelUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NotificationChannelUser> notificationChannelUserList = notificationChannelUserRepository.findAll();
        assertThat(notificationChannelUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationChannelUser.class);
        NotificationChannelUser notificationChannelUser1 = new NotificationChannelUser();
        notificationChannelUser1.setId(1L);
        NotificationChannelUser notificationChannelUser2 = new NotificationChannelUser();
        notificationChannelUser2.setId(notificationChannelUser1.getId());
        assertThat(notificationChannelUser1).isEqualTo(notificationChannelUser2);
        notificationChannelUser2.setId(2L);
        assertThat(notificationChannelUser1).isNotEqualTo(notificationChannelUser2);
        notificationChannelUser1.setId(null);
        assertThat(notificationChannelUser1).isNotEqualTo(notificationChannelUser2);
    }
}

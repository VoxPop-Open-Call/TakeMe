package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.NotificationChannel;
import pt.famility.backoffice.repository.NotificationChannelRepository;
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

import pt.famility.backoffice.domain.enumeration.NotificationChannelType;
/**
 * Test class for the NotificationChannelResource REST controller.
 *
 * @see NotificationChannelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class NotificationChannelResourceIntTest {

    private static final NotificationChannelType DEFAULT_TYPE = NotificationChannelType.EMAIL;
    private static final NotificationChannelType UPDATED_TYPE = NotificationChannelType.PUSH_NOTIFICATION;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private NotificationChannelRepository notificationChannelRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationChannelMockMvc;

    private NotificationChannel notificationChannel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationChannelResource notificationChannelResource = new NotificationChannelResource(notificationChannelRepository);
        this.restNotificationChannelMockMvc = MockMvcBuilders.standaloneSetup(notificationChannelResource)
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
    public static NotificationChannel createEntity(EntityManager em) {
        NotificationChannel notificationChannel = new NotificationChannel()
            .type(DEFAULT_TYPE)
            .active(DEFAULT_ACTIVE);
        return notificationChannel;
    }

    @Before
    public void initTest() {
        notificationChannel = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationChannel() throws Exception {
        int databaseSizeBeforeCreate = notificationChannelRepository.findAll().size();

        // Create the NotificationChannel
        restNotificationChannelMockMvc.perform(post("/api/notification-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationChannel)))
            .andExpect(status().isCreated());

        // Validate the NotificationChannel in the database
        List<NotificationChannel> notificationChannelList = notificationChannelRepository.findAll();
        assertThat(notificationChannelList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationChannel testNotificationChannel = notificationChannelList.get(notificationChannelList.size() - 1);
        assertThat(testNotificationChannel.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testNotificationChannel.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createNotificationChannelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationChannelRepository.findAll().size();

        // Create the NotificationChannel with an existing ID
        notificationChannel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationChannelMockMvc.perform(post("/api/notification-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationChannel)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationChannel in the database
        List<NotificationChannel> notificationChannelList = notificationChannelRepository.findAll();
        assertThat(notificationChannelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNotificationChannels() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);

        // Get all the notificationChannelList
        restNotificationChannelMockMvc.perform(get("/api/notification-channels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getNotificationChannel() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);

        // Get the notificationChannel
        restNotificationChannelMockMvc.perform(get("/api/notification-channels/{id}", notificationChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificationChannel.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationChannel() throws Exception {
        // Get the notificationChannel
        restNotificationChannelMockMvc.perform(get("/api/notification-channels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationChannel() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);

        int databaseSizeBeforeUpdate = notificationChannelRepository.findAll().size();

        // Update the notificationChannel
        NotificationChannel updatedNotificationChannel = notificationChannelRepository.findById(notificationChannel.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationChannel are not directly saved in db
        em.detach(updatedNotificationChannel);
        updatedNotificationChannel
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE);

        restNotificationChannelMockMvc.perform(put("/api/notification-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotificationChannel)))
            .andExpect(status().isOk());

        // Validate the NotificationChannel in the database
        List<NotificationChannel> notificationChannelList = notificationChannelRepository.findAll();
        assertThat(notificationChannelList).hasSize(databaseSizeBeforeUpdate);
        NotificationChannel testNotificationChannel = notificationChannelList.get(notificationChannelList.size() - 1);
        assertThat(testNotificationChannel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNotificationChannel.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationChannel() throws Exception {
        int databaseSizeBeforeUpdate = notificationChannelRepository.findAll().size();

        // Create the NotificationChannel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationChannelMockMvc.perform(put("/api/notification-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationChannel)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationChannel in the database
        List<NotificationChannel> notificationChannelList = notificationChannelRepository.findAll();
        assertThat(notificationChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotificationChannel() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);

        int databaseSizeBeforeDelete = notificationChannelRepository.findAll().size();

        // Get the notificationChannel
        restNotificationChannelMockMvc.perform(delete("/api/notification-channels/{id}", notificationChannel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NotificationChannel> notificationChannelList = notificationChannelRepository.findAll();
        assertThat(notificationChannelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationChannel.class);
        NotificationChannel notificationChannel1 = new NotificationChannel();
        notificationChannel1.setId(1L);
        NotificationChannel notificationChannel2 = new NotificationChannel();
        notificationChannel2.setId(notificationChannel1.getId());
        assertThat(notificationChannel1).isEqualTo(notificationChannel2);
        notificationChannel2.setId(2L);
        assertThat(notificationChannel1).isNotEqualTo(notificationChannel2);
        notificationChannel1.setId(null);
        assertThat(notificationChannel1).isNotEqualTo(notificationChannel2);
    }
}

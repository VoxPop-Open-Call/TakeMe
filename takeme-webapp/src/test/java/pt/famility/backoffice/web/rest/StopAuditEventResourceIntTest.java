package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.StopAuditEvent;
import pt.famility.backoffice.repository.StopAuditEventRepository;
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

import pt.famility.backoffice.domain.enumeration.StopEventType;
/**
 * Test class for the StopAuditEventResource REST controller.
 *
 * @see StopAuditEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class StopAuditEventResourceIntTest {

    private static final Instant DEFAULT_EVENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final StopEventType DEFAULT_EVENT_TYPE = StopEventType.CHECK_IN;
    private static final StopEventType UPDATED_EVENT_TYPE = StopEventType.CHECK_OUT;

    @Autowired
    private StopAuditEventRepository stopAuditEventRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStopAuditEventMockMvc;

    private StopAuditEvent stopAuditEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StopAuditEventResource stopAuditEventResource = new StopAuditEventResource(stopAuditEventRepository);
        this.restStopAuditEventMockMvc = MockMvcBuilders.standaloneSetup(stopAuditEventResource)
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
    public static StopAuditEvent createEntity(EntityManager em) {
        StopAuditEvent stopAuditEvent = new StopAuditEvent()
            .eventTime(DEFAULT_EVENT_TIME)
            .eventType(DEFAULT_EVENT_TYPE);
        return stopAuditEvent;
    }

    @Before
    public void initTest() {
        stopAuditEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createStopAuditEvent() throws Exception {
        int databaseSizeBeforeCreate = stopAuditEventRepository.findAll().size();

        // Create the StopAuditEvent
        restStopAuditEventMockMvc.perform(post("/api/stop-audit-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopAuditEvent)))
            .andExpect(status().isCreated());

        // Validate the StopAuditEvent in the database
        List<StopAuditEvent> stopAuditEventList = stopAuditEventRepository.findAll();
        assertThat(stopAuditEventList).hasSize(databaseSizeBeforeCreate + 1);
        StopAuditEvent testStopAuditEvent = stopAuditEventList.get(stopAuditEventList.size() - 1);
        assertThat(testStopAuditEvent.getEventTime()).isEqualTo(DEFAULT_EVENT_TIME);
        assertThat(testStopAuditEvent.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void createStopAuditEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stopAuditEventRepository.findAll().size();

        // Create the StopAuditEvent with an existing ID
        stopAuditEvent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStopAuditEventMockMvc.perform(post("/api/stop-audit-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopAuditEvent)))
            .andExpect(status().isBadRequest());

        // Validate the StopAuditEvent in the database
        List<StopAuditEvent> stopAuditEventList = stopAuditEventRepository.findAll();
        assertThat(stopAuditEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEventTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stopAuditEventRepository.findAll().size();
        // set the field null
        stopAuditEvent.setEventTime(null);

        // Create the StopAuditEvent, which fails.

        restStopAuditEventMockMvc.perform(post("/api/stop-audit-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopAuditEvent)))
            .andExpect(status().isBadRequest());

        List<StopAuditEvent> stopAuditEventList = stopAuditEventRepository.findAll();
        assertThat(stopAuditEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stopAuditEventRepository.findAll().size();
        // set the field null
        stopAuditEvent.setEventType(null);

        // Create the StopAuditEvent, which fails.

        restStopAuditEventMockMvc.perform(post("/api/stop-audit-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopAuditEvent)))
            .andExpect(status().isBadRequest());

        List<StopAuditEvent> stopAuditEventList = stopAuditEventRepository.findAll();
        assertThat(stopAuditEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStopAuditEvents() throws Exception {
        // Initialize the database
        stopAuditEventRepository.saveAndFlush(stopAuditEvent);

        // Get all the stopAuditEventList
        restStopAuditEventMockMvc.perform(get("/api/stop-audit-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stopAuditEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTime").value(hasItem(DEFAULT_EVENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getStopAuditEvent() throws Exception {
        // Initialize the database
        stopAuditEventRepository.saveAndFlush(stopAuditEvent);

        // Get the stopAuditEvent
        restStopAuditEventMockMvc.perform(get("/api/stop-audit-events/{id}", stopAuditEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stopAuditEvent.getId().intValue()))
            .andExpect(jsonPath("$.eventTime").value(DEFAULT_EVENT_TIME.toString()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStopAuditEvent() throws Exception {
        // Get the stopAuditEvent
        restStopAuditEventMockMvc.perform(get("/api/stop-audit-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStopAuditEvent() throws Exception {
        // Initialize the database
        stopAuditEventRepository.saveAndFlush(stopAuditEvent);

        int databaseSizeBeforeUpdate = stopAuditEventRepository.findAll().size();

        // Update the stopAuditEvent
        StopAuditEvent updatedStopAuditEvent = stopAuditEventRepository.findById(stopAuditEvent.getId()).get();
        // Disconnect from session so that the updates on updatedStopAuditEvent are not directly saved in db
        em.detach(updatedStopAuditEvent);
        updatedStopAuditEvent
            .eventTime(UPDATED_EVENT_TIME)
            .eventType(UPDATED_EVENT_TYPE);

        restStopAuditEventMockMvc.perform(put("/api/stop-audit-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStopAuditEvent)))
            .andExpect(status().isOk());

        // Validate the StopAuditEvent in the database
        List<StopAuditEvent> stopAuditEventList = stopAuditEventRepository.findAll();
        assertThat(stopAuditEventList).hasSize(databaseSizeBeforeUpdate);
        StopAuditEvent testStopAuditEvent = stopAuditEventList.get(stopAuditEventList.size() - 1);
        assertThat(testStopAuditEvent.getEventTime()).isEqualTo(UPDATED_EVENT_TIME);
        assertThat(testStopAuditEvent.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingStopAuditEvent() throws Exception {
        int databaseSizeBeforeUpdate = stopAuditEventRepository.findAll().size();

        // Create the StopAuditEvent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStopAuditEventMockMvc.perform(put("/api/stop-audit-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopAuditEvent)))
            .andExpect(status().isBadRequest());

        // Validate the StopAuditEvent in the database
        List<StopAuditEvent> stopAuditEventList = stopAuditEventRepository.findAll();
        assertThat(stopAuditEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStopAuditEvent() throws Exception {
        // Initialize the database
        stopAuditEventRepository.saveAndFlush(stopAuditEvent);

        int databaseSizeBeforeDelete = stopAuditEventRepository.findAll().size();

        // Get the stopAuditEvent
        restStopAuditEventMockMvc.perform(delete("/api/stop-audit-events/{id}", stopAuditEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StopAuditEvent> stopAuditEventList = stopAuditEventRepository.findAll();
        assertThat(stopAuditEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StopAuditEvent.class);
        StopAuditEvent stopAuditEvent1 = new StopAuditEvent();
        stopAuditEvent1.setId(1L);
        StopAuditEvent stopAuditEvent2 = new StopAuditEvent();
        stopAuditEvent2.setId(stopAuditEvent1.getId());
        assertThat(stopAuditEvent1).isEqualTo(stopAuditEvent2);
        stopAuditEvent2.setId(2L);
        assertThat(stopAuditEvent1).isNotEqualTo(stopAuditEvent2);
        stopAuditEvent1.setId(null);
        assertThat(stopAuditEvent1).isNotEqualTo(stopAuditEvent2);
    }
}

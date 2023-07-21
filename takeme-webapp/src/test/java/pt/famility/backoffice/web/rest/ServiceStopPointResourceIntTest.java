package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.ServiceStopPoint;
import pt.famility.backoffice.repository.ServiceStopPointRepository;
import pt.famility.backoffice.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;


import static pt.famility.backoffice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pt.famility.backoffice.domain.enumeration.StopPointType;
import pt.famility.backoffice.domain.enumeration.StatusType;
/**
 * Test class for the ServiceStopPointResource REST controller.
 *
 * @see ServiceStopPointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class ServiceStopPointResourceIntTest {

    private static final StopPointType DEFAULT_STOP_POINT_TYPE = StopPointType.COLLECTION;
    private static final StopPointType UPDATED_STOP_POINT_TYPE = StopPointType.DELIVER;

    private static final String DEFAULT_START_HOUR = "AAAAAAAAAA";
    private static final String UPDATED_START_HOUR = "BBBBBBBBBB";

    private static final String DEFAULT_COMBINED_TIME = "AAAAAAAAAA";
    private static final String UPDATED_COMBINED_TIME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_FREQUENCY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_FREQUENCY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final StatusType DEFAULT_STATUS_TYPE = StatusType.NEW;
    private static final StatusType UPDATED_STATUS_TYPE = StatusType.ACTIVE;

    @Autowired
    private ServiceStopPointRepository serviceStopPointRepository;

    @Mock
    private ServiceStopPointRepository serviceStopPointRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceStopPointMockMvc;

    private ServiceStopPoint serviceStopPoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceStopPointResource serviceStopPointResource = new ServiceStopPointResource(serviceStopPointRepository);
        this.restServiceStopPointMockMvc = MockMvcBuilders.standaloneSetup(serviceStopPointResource)
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
    public static ServiceStopPoint createEntity(EntityManager em) {
        ServiceStopPoint serviceStopPoint = new ServiceStopPoint()
            .stopPointType(DEFAULT_STOP_POINT_TYPE)
            .startHour(DEFAULT_START_HOUR)
            .combinedTime(DEFAULT_COMBINED_TIME)
            .startFrequencyDate(DEFAULT_START_FREQUENCY_DATE)
            .statusType(DEFAULT_STATUS_TYPE);
        return serviceStopPoint;
    }

    @Before
    public void initTest() {
        serviceStopPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceStopPoint() throws Exception {
        int databaseSizeBeforeCreate = serviceStopPointRepository.findAll().size();

        // Create the ServiceStopPoint
        restServiceStopPointMockMvc.perform(post("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isCreated());

        // Validate the ServiceStopPoint in the database
        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceStopPoint testServiceStopPoint = serviceStopPointList.get(serviceStopPointList.size() - 1);
        assertThat(testServiceStopPoint.getStopPointType()).isEqualTo(DEFAULT_STOP_POINT_TYPE);
        assertThat(testServiceStopPoint.getStartHour()).isEqualTo(DEFAULT_START_HOUR);
        assertThat(testServiceStopPoint.getCombinedTime()).isEqualTo(DEFAULT_COMBINED_TIME);
        assertThat(testServiceStopPoint.getStartFrequencyDate()).isEqualTo(DEFAULT_START_FREQUENCY_DATE);
        assertThat(testServiceStopPoint.getStatusType()).isEqualTo(DEFAULT_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void createServiceStopPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceStopPointRepository.findAll().size();

        // Create the ServiceStopPoint with an existing ID
        serviceStopPoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceStopPointMockMvc.perform(post("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceStopPoint in the database
        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStopPointTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceStopPointRepository.findAll().size();
        // set the field null
        serviceStopPoint.setStopPointType(null);

        // Create the ServiceStopPoint, which fails.

        restServiceStopPointMockMvc.perform(post("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isBadRequest());

        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartHourIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceStopPointRepository.findAll().size();
        // set the field null
        serviceStopPoint.setStartHour(null);

        // Create the ServiceStopPoint, which fails.

        restServiceStopPointMockMvc.perform(post("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isBadRequest());

        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCombinedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceStopPointRepository.findAll().size();
        // set the field null
        serviceStopPoint.setCombinedTime(null);

        // Create the ServiceStopPoint, which fails.

        restServiceStopPointMockMvc.perform(post("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isBadRequest());

        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartFrequencyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceStopPointRepository.findAll().size();
        // set the field null
        serviceStopPoint.setStartFrequencyDate(null);

        // Create the ServiceStopPoint, which fails.

        restServiceStopPointMockMvc.perform(post("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isBadRequest());

        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceStopPointRepository.findAll().size();
        // set the field null
        serviceStopPoint.setStatusType(null);

        // Create the ServiceStopPoint, which fails.

        restServiceStopPointMockMvc.perform(post("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isBadRequest());

        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceStopPoints() throws Exception {
        // Initialize the database
        serviceStopPointRepository.saveAndFlush(serviceStopPoint);

        // Get all the serviceStopPointList
        restServiceStopPointMockMvc.perform(get("/api/service-stop-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceStopPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].stopPointType").value(hasItem(DEFAULT_STOP_POINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR.toString())))
            .andExpect(jsonPath("$.[*].combinedTime").value(hasItem(DEFAULT_COMBINED_TIME.toString())))
            .andExpect(jsonPath("$.[*].startFrequencyDate").value(hasItem(DEFAULT_START_FREQUENCY_DATE.toString())))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllServiceStopPointsWithEagerRelationshipsIsEnabled() throws Exception {
        ServiceStopPointResource serviceStopPointResource = new ServiceStopPointResource(serviceStopPointRepositoryMock);
        when(serviceStopPointRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restServiceStopPointMockMvc = MockMvcBuilders.standaloneSetup(serviceStopPointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restServiceStopPointMockMvc.perform(get("/api/service-stop-points?eagerload=true"))
        .andExpect(status().isOk());

        verify(serviceStopPointRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllServiceStopPointsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ServiceStopPointResource serviceStopPointResource = new ServiceStopPointResource(serviceStopPointRepositoryMock);
            when(serviceStopPointRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restServiceStopPointMockMvc = MockMvcBuilders.standaloneSetup(serviceStopPointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restServiceStopPointMockMvc.perform(get("/api/service-stop-points?eagerload=true"))
        .andExpect(status().isOk());

            verify(serviceStopPointRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getServiceStopPoint() throws Exception {
        // Initialize the database
        serviceStopPointRepository.saveAndFlush(serviceStopPoint);

        // Get the serviceStopPoint
        restServiceStopPointMockMvc.perform(get("/api/service-stop-points/{id}", serviceStopPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceStopPoint.getId().intValue()))
            .andExpect(jsonPath("$.stopPointType").value(DEFAULT_STOP_POINT_TYPE.toString()))
            .andExpect(jsonPath("$.startHour").value(DEFAULT_START_HOUR.toString()))
            .andExpect(jsonPath("$.combinedTime").value(DEFAULT_COMBINED_TIME.toString()))
            .andExpect(jsonPath("$.startFrequencyDate").value(DEFAULT_START_FREQUENCY_DATE.toString()))
            .andExpect(jsonPath("$.statusType").value(DEFAULT_STATUS_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceStopPoint() throws Exception {
        // Get the serviceStopPoint
        restServiceStopPointMockMvc.perform(get("/api/service-stop-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceStopPoint() throws Exception {
        // Initialize the database
        serviceStopPointRepository.saveAndFlush(serviceStopPoint);

        int databaseSizeBeforeUpdate = serviceStopPointRepository.findAll().size();

        // Update the serviceStopPoint
        ServiceStopPoint updatedServiceStopPoint = serviceStopPointRepository.findById(serviceStopPoint.getId()).get();
        // Disconnect from session so that the updates on updatedServiceStopPoint are not directly saved in db
        em.detach(updatedServiceStopPoint);
        updatedServiceStopPoint
            .stopPointType(UPDATED_STOP_POINT_TYPE)
            .startHour(UPDATED_START_HOUR)
            .combinedTime(UPDATED_COMBINED_TIME)
            .startFrequencyDate(UPDATED_START_FREQUENCY_DATE)
            .statusType(UPDATED_STATUS_TYPE);

        restServiceStopPointMockMvc.perform(put("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceStopPoint)))
            .andExpect(status().isOk());

        // Validate the ServiceStopPoint in the database
        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeUpdate);
        ServiceStopPoint testServiceStopPoint = serviceStopPointList.get(serviceStopPointList.size() - 1);
        assertThat(testServiceStopPoint.getStopPointType()).isEqualTo(UPDATED_STOP_POINT_TYPE);
        assertThat(testServiceStopPoint.getStartHour()).isEqualTo(UPDATED_START_HOUR);
        assertThat(testServiceStopPoint.getCombinedTime()).isEqualTo(UPDATED_COMBINED_TIME);
        assertThat(testServiceStopPoint.getStartFrequencyDate()).isEqualTo(UPDATED_START_FREQUENCY_DATE);
        assertThat(testServiceStopPoint.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = serviceStopPointRepository.findAll().size();

        // Create the ServiceStopPoint

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceStopPointMockMvc.perform(put("/api/service-stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPoint)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceStopPoint in the database
        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceStopPoint() throws Exception {
        // Initialize the database
        serviceStopPointRepository.saveAndFlush(serviceStopPoint);

        int databaseSizeBeforeDelete = serviceStopPointRepository.findAll().size();

        // Get the serviceStopPoint
        restServiceStopPointMockMvc.perform(delete("/api/service-stop-points/{id}", serviceStopPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceStopPoint> serviceStopPointList = serviceStopPointRepository.findAll();
        assertThat(serviceStopPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceStopPoint.class);
        ServiceStopPoint serviceStopPoint1 = new ServiceStopPoint();
        serviceStopPoint1.setId(1L);
        ServiceStopPoint serviceStopPoint2 = new ServiceStopPoint();
        serviceStopPoint2.setId(serviceStopPoint1.getId());
        assertThat(serviceStopPoint1).isEqualTo(serviceStopPoint2);
        serviceStopPoint2.setId(2L);
        assertThat(serviceStopPoint1).isNotEqualTo(serviceStopPoint2);
        serviceStopPoint1.setId(null);
        assertThat(serviceStopPoint1).isNotEqualTo(serviceStopPoint2);
    }
}

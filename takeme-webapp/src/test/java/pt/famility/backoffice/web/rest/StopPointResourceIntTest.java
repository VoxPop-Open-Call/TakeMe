package pt.famility.backoffice.web.rest;

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
import pt.famility.backoffice.FamilityBackofficeApp;
import pt.famility.backoffice.domain.StopPoint;
import pt.famility.backoffice.domain.enumeration.StopPointType;
import pt.famility.backoffice.repository.StopPointRepository;
import pt.famility.backoffice.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pt.famility.backoffice.web.rest.TestUtil.createFormattingConversionService;
/**
 * Test class for the StopPointResource REST controller.
 *
 * @see StopPointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class StopPointResourceIntTest {

    private static final StopPointType DEFAULT_STOP_POINT_TYPE = StopPointType.COLLECTION;
    private static final StopPointType UPDATED_STOP_POINT_TYPE = StopPointType.DELIVER;

    private static final String DEFAULT_COMBINED_TIME = "AAAAAAAAAA";
    private static final String UPDATED_COMBINED_TIME = "BBBBBBBBBB";

    @Autowired
    private StopPointRepository stopPointRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStopPointMockMvc;

    private StopPoint stopPoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StopPointResource stopPointResource = new StopPointResource(stopPointRepository);
        this.restStopPointMockMvc = MockMvcBuilders.standaloneSetup(stopPointResource)
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
    public static StopPoint createEntity(EntityManager em) {
        StopPoint stopPoint = new StopPoint()
            .stopPointType(DEFAULT_STOP_POINT_TYPE);
        return stopPoint;
    }

    @Before
    public void initTest() {
        stopPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createStopPoint() throws Exception {
        int databaseSizeBeforeCreate = stopPointRepository.findAll().size();

        // Create the StopPoint
        restStopPointMockMvc.perform(post("/api/stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopPoint)))
            .andExpect(status().isCreated());

        // Validate the StopPoint in the database
        List<StopPoint> stopPointList = stopPointRepository.findAll();
        assertThat(stopPointList).hasSize(databaseSizeBeforeCreate + 1);
        StopPoint testStopPoint = stopPointList.get(stopPointList.size() - 1);
        assertThat(testStopPoint.getStopPointType()).isEqualTo(DEFAULT_STOP_POINT_TYPE);
    }

    @Test
    @Transactional
    public void createStopPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stopPointRepository.findAll().size();

        // Create the StopPoint with an existing ID
        stopPoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStopPointMockMvc.perform(post("/api/stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopPoint)))
            .andExpect(status().isBadRequest());

        // Validate the StopPoint in the database
        List<StopPoint> stopPointList = stopPointRepository.findAll();
        assertThat(stopPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStopPointTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stopPointRepository.findAll().size();
        // set the field null
        stopPoint.setStopPointType(null);

        // Create the StopPoint, which fails.

        restStopPointMockMvc.perform(post("/api/stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopPoint)))
            .andExpect(status().isBadRequest());

        List<StopPoint> stopPointList = stopPointRepository.findAll();
        assertThat(stopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCombinedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stopPointRepository.findAll().size();
        // set the field null

        // Create the StopPoint, which fails.

        restStopPointMockMvc.perform(post("/api/stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopPoint)))
            .andExpect(status().isBadRequest());

        List<StopPoint> stopPointList = stopPointRepository.findAll();
        assertThat(stopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStopPoints() throws Exception {
        // Initialize the database
        stopPointRepository.saveAndFlush(stopPoint);

        // Get all the stopPointList
        restStopPointMockMvc.perform(get("/api/stop-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stopPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].stopPointType").value(hasItem(DEFAULT_STOP_POINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].combinedTime").value(hasItem(DEFAULT_COMBINED_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getStopPoint() throws Exception {
        // Initialize the database
        stopPointRepository.saveAndFlush(stopPoint);

        // Get the stopPoint
        restStopPointMockMvc.perform(get("/api/stop-points/{id}", stopPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stopPoint.getId().intValue()))
            .andExpect(jsonPath("$.stopPointType").value(DEFAULT_STOP_POINT_TYPE.toString()))
            .andExpect(jsonPath("$.combinedTime").value(DEFAULT_COMBINED_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStopPoint() throws Exception {
        // Get the stopPoint
        restStopPointMockMvc.perform(get("/api/stop-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStopPoint() throws Exception {
        // Initialize the database
        stopPointRepository.saveAndFlush(stopPoint);

        int databaseSizeBeforeUpdate = stopPointRepository.findAll().size();

        // Update the stopPoint
        StopPoint updatedStopPoint = stopPointRepository.findById(stopPoint.getId()).get();
        // Disconnect from session so that the updates on updatedStopPoint are not directly saved in db
        em.detach(updatedStopPoint);
        updatedStopPoint
            .stopPointType(UPDATED_STOP_POINT_TYPE);

        restStopPointMockMvc.perform(put("/api/stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStopPoint)))
            .andExpect(status().isOk());

        // Validate the StopPoint in the database
        List<StopPoint> stopPointList = stopPointRepository.findAll();
        assertThat(stopPointList).hasSize(databaseSizeBeforeUpdate);
        StopPoint testStopPoint = stopPointList.get(stopPointList.size() - 1);
        assertThat(testStopPoint.getStopPointType()).isEqualTo(UPDATED_STOP_POINT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = stopPointRepository.findAll().size();

        // Create the StopPoint

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStopPointMockMvc.perform(put("/api/stop-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopPoint)))
            .andExpect(status().isBadRequest());

        // Validate the StopPoint in the database
        List<StopPoint> stopPointList = stopPointRepository.findAll();
        assertThat(stopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStopPoint() throws Exception {
        // Initialize the database
        stopPointRepository.saveAndFlush(stopPoint);

        int databaseSizeBeforeDelete = stopPointRepository.findAll().size();

        // Get the stopPoint
        restStopPointMockMvc.perform(delete("/api/stop-points/{id}", stopPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StopPoint> stopPointList = stopPointRepository.findAll();
        assertThat(stopPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StopPoint.class);
        StopPoint stopPoint1 = new StopPoint();
        stopPoint1.setId(1L);
        StopPoint stopPoint2 = new StopPoint();
        stopPoint2.setId(stopPoint1.getId());
        assertThat(stopPoint1).isEqualTo(stopPoint2);
        stopPoint2.setId(2L);
        assertThat(stopPoint1).isNotEqualTo(stopPoint2);
        stopPoint1.setId(null);
        assertThat(stopPoint1).isNotEqualTo(stopPoint2);
    }
}

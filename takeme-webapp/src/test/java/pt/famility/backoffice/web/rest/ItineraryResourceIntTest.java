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
import pt.famility.backoffice.converter.ItineraryConverter;
import pt.famility.backoffice.domain.Itinerary;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;
import pt.famility.backoffice.repository.ItineraryRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.service.ItineraryService;
import pt.famility.backoffice.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the ItineraryResource REST controller.
 *
 * @see ItineraryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class ItineraryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ESTIMATED_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_ESTIMATED_START_TIME = "BBBBBBBBBB";

    private static final Instant DEFAULT_EFFECTIVE_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EFFECTIVE_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ItineraryStatusType DEFAULT_ITINERARY_STATUS_TYPE = ItineraryStatusType.READY_TO_START;
    private static final ItineraryStatusType UPDATED_ITINERARY_STATUS_TYPE = ItineraryStatusType.IN_PROGRESS;

    private static final Double DEFAULT_ESTIMATED_KM = 1D;
    private static final Double UPDATED_ESTIMATED_KM = 2D;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryConverter itineraryConverter;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private AccessValidator accessValidator;

    private MockMvc restItineraryMockMvc;

    private Itinerary itinerary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItineraryResource itineraryResource = new ItineraryResource(itineraryRepository, itineraryService, itineraryConverter, accessValidator);
        this.restItineraryMockMvc = MockMvcBuilders.standaloneSetup(itineraryResource)
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
    public static Itinerary createEntity(EntityManager em) {
        Itinerary itinerary = new Itinerary()
            .name(DEFAULT_NAME)
            .effectiveStartTime(DEFAULT_EFFECTIVE_START_TIME)
            .effectiveEndTime(DEFAULT_EFFECTIVE_END_TIME)
            .itineraryStatusType(DEFAULT_ITINERARY_STATUS_TYPE)
            .estimatedKM(DEFAULT_ESTIMATED_KM);
        return itinerary;
    }

    @Before
    public void initTest() {
        itinerary = createEntity(em);
    }

    @Test
    @Transactional
    public void createItinerary() throws Exception {
        int databaseSizeBeforeCreate = itineraryRepository.findAll().size();

        // Create the Itinerary
        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isCreated());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeCreate + 1);
        Itinerary testItinerary = itineraryList.get(itineraryList.size() - 1);
        assertThat(testItinerary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItinerary.getEffectiveStartTime()).isEqualTo(DEFAULT_EFFECTIVE_START_TIME);
        assertThat(testItinerary.getEffectiveEndTime()).isEqualTo(DEFAULT_EFFECTIVE_END_TIME);
        assertThat(testItinerary.getItineraryStatusType()).isEqualTo(DEFAULT_ITINERARY_STATUS_TYPE);
        assertThat(testItinerary.getEstimatedKM()).isEqualTo(DEFAULT_ESTIMATED_KM);
    }

    @Test
    @Transactional
    public void createItineraryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itineraryRepository.findAll().size();

        // Create the Itinerary with an existing ID
        itinerary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraryRepository.findAll().size();
        // set the field null
        itinerary.setName(null);

        // Create the Itinerary, which fails.

        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstimatedStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraryRepository.findAll().size();
        // set the field null

        // Create the Itinerary, which fails.

        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEffectiveStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraryRepository.findAll().size();
        // set the field null
        itinerary.setEffectiveStartTime(null);

        // Create the Itinerary, which fails.

        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEffectiveEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraryRepository.findAll().size();
        // set the field null
        itinerary.setEffectiveEndTime(null);

        // Create the Itinerary, which fails.

        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItineraryStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraryRepository.findAll().size();
        // set the field null
        itinerary.setItineraryStatusType(null);

        // Create the Itinerary, which fails.

        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstimatedKMIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraryRepository.findAll().size();
        // set the field null
        itinerary.setEstimatedKM(null);

        // Create the Itinerary, which fails.

        restItineraryMockMvc.perform(post("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItineraries() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList
        restItineraryMockMvc.perform(get("/api/itineraries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itinerary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].estimatedStartTime").value(hasItem(DEFAULT_ESTIMATED_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].effectiveStartTime").value(hasItem(DEFAULT_EFFECTIVE_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].effectiveEndTime").value(hasItem(DEFAULT_EFFECTIVE_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].itineraryStatusType").value(hasItem(DEFAULT_ITINERARY_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].estimatedKM").value(hasItem(DEFAULT_ESTIMATED_KM.doubleValue())));
    }

    @Test
    @Transactional
    public void getItinerary() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get the itinerary
        restItineraryMockMvc.perform(get("/api/itineraries/{id}", itinerary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itinerary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.estimatedStartTime").value(DEFAULT_ESTIMATED_START_TIME.toString()))
            .andExpect(jsonPath("$.effectiveStartTime").value(DEFAULT_EFFECTIVE_START_TIME.toString()))
            .andExpect(jsonPath("$.effectiveEndTime").value(DEFAULT_EFFECTIVE_END_TIME.toString()))
            .andExpect(jsonPath("$.itineraryStatusType").value(DEFAULT_ITINERARY_STATUS_TYPE.toString()))
            .andExpect(jsonPath("$.estimatedKM").value(DEFAULT_ESTIMATED_KM.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItinerary() throws Exception {
        // Get the itinerary
        restItineraryMockMvc.perform(get("/api/itineraries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItinerary() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        int databaseSizeBeforeUpdate = itineraryRepository.findAll().size();

        // Update the itinerary
        Itinerary updatedItinerary = itineraryRepository.findById(itinerary.getId()).get();
        // Disconnect from session so that the updates on updatedItinerary are not directly saved in db
        em.detach(updatedItinerary);
        updatedItinerary
            .name(UPDATED_NAME)
            .effectiveStartTime(UPDATED_EFFECTIVE_START_TIME)
            .effectiveEndTime(UPDATED_EFFECTIVE_END_TIME)
            .itineraryStatusType(UPDATED_ITINERARY_STATUS_TYPE)
            .estimatedKM(UPDATED_ESTIMATED_KM);

        restItineraryMockMvc.perform(put("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItinerary)))
            .andExpect(status().isOk());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeUpdate);
        Itinerary testItinerary = itineraryList.get(itineraryList.size() - 1);
        assertThat(testItinerary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItinerary.getEffectiveStartTime()).isEqualTo(UPDATED_EFFECTIVE_START_TIME);
        assertThat(testItinerary.getEffectiveEndTime()).isEqualTo(UPDATED_EFFECTIVE_END_TIME);
        assertThat(testItinerary.getItineraryStatusType()).isEqualTo(UPDATED_ITINERARY_STATUS_TYPE);
        assertThat(testItinerary.getEstimatedKM()).isEqualTo(UPDATED_ESTIMATED_KM);
    }

    @Test
    @Transactional
    public void updateNonExistingItinerary() throws Exception {
        int databaseSizeBeforeUpdate = itineraryRepository.findAll().size();

        // Create the Itinerary

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItineraryMockMvc.perform(put("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerary)))
            .andExpect(status().isBadRequest());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItinerary() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        int databaseSizeBeforeDelete = itineraryRepository.findAll().size();

        // Get the itinerary
        restItineraryMockMvc.perform(delete("/api/itineraries/{id}", itinerary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Itinerary.class);
        Itinerary itinerary1 = new Itinerary();
        itinerary1.setId(1L);
        Itinerary itinerary2 = new Itinerary();
        itinerary2.setId(itinerary1.getId());
        assertThat(itinerary1).isEqualTo(itinerary2);
        itinerary2.setId(2L);
        assertThat(itinerary1).isNotEqualTo(itinerary2);
        itinerary1.setId(null);
        assertThat(itinerary1).isNotEqualTo(itinerary2);
    }
}

package pt.famility.backoffice.web.rest;

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
import pt.famility.backoffice.FamilityBackofficeApp;
import pt.famility.backoffice.converter.ChildConverter;
import pt.famility.backoffice.converter.ItineraryStopPointChildViewConverter;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ItineraryStopPointChildViewRepository;
import pt.famility.backoffice.repository.TutorRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.service.TutorService;
import pt.famility.backoffice.service.UserService;
import pt.famility.backoffice.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pt.famility.backoffice.web.rest.TestUtil.createFormattingConversionService;
/**
 * Test class for the TutorResource REST controller.
 *
 * @see TutorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class TutorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIF_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_NIF_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NIF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NIF_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATION_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION_CARD_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FAMILITY = false;
    private static final Boolean UPDATED_FAMILITY = true;

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final StatusType DEFAULT_STATUS_TYPE = StatusType.NEW;
    private static final StatusType UPDATED_STATUS_TYPE = StatusType.ACTIVE;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private ChildService childService;

    @Autowired
    private AccessValidator accessValidator;

    @Mock
    private TutorRepository tutorRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private ChildConverter childConverter;

    @Autowired
    private ItineraryStopPointChildViewRepository itineraryStopPointChildViewRepository;

    @Autowired
    private ItineraryStopPointChildViewConverter itineraryStopPointChildViewConverter;

    private MockMvc restTutorMockMvc;

    private Tutor tutor;

    private UserService userService;

    private ChildSubscriptionService childSubscriptionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TutorResource tutorResource = new TutorResource(tutorRepository, tutorService, childService, childSubscriptionService, itineraryStopPointChildViewRepository, itineraryStopPointChildViewConverter, accessValidator);
        this.restTutorMockMvc = MockMvcBuilders.standaloneSetup(tutorResource)
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
    public static Tutor createEntity(EntityManager em) {
        Tutor tutor = new Tutor()
            .name(DEFAULT_NAME)
            .nifCountry(DEFAULT_NIF_COUNTRY)
            .nifNumber(DEFAULT_NIF_NUMBER)
            .identificationCardNumber(DEFAULT_IDENTIFICATION_CARD_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .famility(DEFAULT_FAMILITY)
            .statusType(DEFAULT_STATUS_TYPE);
        return tutor;
    }

    @Before
    public void initTest() {
        tutor = createEntity(em);
    }

    @Test
    @Transactional
    public void createTutor() throws Exception {
        int databaseSizeBeforeCreate = tutorRepository.findAll().size();

        // Create the Tutor
        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isCreated());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate + 1);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTutor.getNifCountry()).isEqualTo(DEFAULT_NIF_COUNTRY);
        assertThat(testTutor.getNifNumber()).isEqualTo(DEFAULT_NIF_NUMBER);
        assertThat(testTutor.getIdentificationCardNumber()).isEqualTo(DEFAULT_IDENTIFICATION_CARD_NUMBER);
        assertThat(testTutor.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTutor.isFamility()).isEqualTo(DEFAULT_FAMILITY);
        assertThat(testTutor.getStatusType()).isEqualTo(DEFAULT_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void createTutorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tutorRepository.findAll().size();

        // Create the Tutor with an existing ID
        tutor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setName(null);

        // Create the Tutor, which fails.

        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNifCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setNifCountry(null);

        // Create the Tutor, which fails.

        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNifNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setNifNumber(null);

        // Create the Tutor, which fails.

        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentificationCardNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setIdentificationCardNumber(null);

        // Create the Tutor, which fails.

        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setPhoneNumber(null);

        // Create the Tutor, which fails.

        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setUser(null);

        // Create the Tutor, which fails.

        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTutors() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList
        restTutorMockMvc.perform(get("/api/tutors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tutor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nifCountry").value(hasItem(DEFAULT_NIF_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].nifNumber").value(hasItem(DEFAULT_NIF_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].identificationCardNumber").value(hasItem(DEFAULT_IDENTIFICATION_CARD_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].famility").value(hasItem(DEFAULT_FAMILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTutorsWithEagerRelationshipsIsEnabled() throws Exception {
        TutorResource tutorResource = new TutorResource(tutorRepositoryMock, tutorService, childService, childSubscriptionService, itineraryStopPointChildViewRepository, itineraryStopPointChildViewConverter, accessValidator);
        when(tutorRepositoryMock.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTutorMockMvc = MockMvcBuilders.standaloneSetup(tutorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTutorMockMvc.perform(get("/api/tutors?eagerload=true"))
        .andExpect(status().isOk());

        verify(tutorRepositoryMock, times(1)).findAll(PageRequest.of(0, 20));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTutorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        TutorResource tutorResource = new TutorResource(tutorRepositoryMock, tutorService, childService, childSubscriptionService, itineraryStopPointChildViewRepository, itineraryStopPointChildViewConverter, accessValidator);
            when(tutorRepositoryMock.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTutorMockMvc = MockMvcBuilders.standaloneSetup(tutorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTutorMockMvc.perform(get("/api/tutors?eagerload=true"))
        .andExpect(status().isOk());

            verify(tutorRepositoryMock, times(1)).findAll(PageRequest.of(0, 20));
    }

    @Test
    @Transactional
    public void getTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get the tutor
        restTutorMockMvc.perform(get("/api/tutors/{id}", tutor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tutor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.nifCountry").value(DEFAULT_NIF_COUNTRY.toString()))
            .andExpect(jsonPath("$.nifNumber").value(DEFAULT_NIF_NUMBER.toString()))
            .andExpect(jsonPath("$.identificationCardNumber").value(DEFAULT_IDENTIFICATION_CARD_NUMBER.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.famility").value(DEFAULT_FAMILITY.booleanValue()))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.statusType").value(DEFAULT_STATUS_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTutor() throws Exception {
        // Get the tutor
        restTutorMockMvc.perform(get("/api/tutors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor
        Tutor updatedTutor = tutorRepository.findById(tutor.getId()).get();
        // Disconnect from session so that the updates on updatedTutor are not directly saved in db
        em.detach(updatedTutor);
        updatedTutor
            .name(UPDATED_NAME)
            .nifCountry(UPDATED_NIF_COUNTRY)
            .nifNumber(UPDATED_NIF_NUMBER)
            .identificationCardNumber(UPDATED_IDENTIFICATION_CARD_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .famility(UPDATED_FAMILITY)
            .statusType(UPDATED_STATUS_TYPE);

        restTutorMockMvc.perform(put("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTutor)))
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTutor.getNifCountry()).isEqualTo(UPDATED_NIF_COUNTRY);
        assertThat(testTutor.getNifNumber()).isEqualTo(UPDATED_NIF_NUMBER);
        assertThat(testTutor.getIdentificationCardNumber()).isEqualTo(UPDATED_IDENTIFICATION_CARD_NUMBER);
        assertThat(testTutor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTutor.isFamility()).isEqualTo(UPDATED_FAMILITY);
        assertThat(testTutor.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Create the Tutor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTutorMockMvc.perform(put("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeDelete = tutorRepository.findAll().size();

        // Get the tutor
        restTutorMockMvc.perform(delete("/api/tutors/{id}", tutor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tutor.class);
        Tutor tutor1 = new Tutor();
        tutor1.setId(1L);
        Tutor tutor2 = new Tutor();
        tutor2.setId(tutor1.getId());
        assertThat(tutor1).isEqualTo(tutor2);
        tutor2.setId(2L);
        assertThat(tutor1).isNotEqualTo(tutor2);
        tutor1.setId(null);
        assertThat(tutor1).isNotEqualTo(tutor2);
    }
}

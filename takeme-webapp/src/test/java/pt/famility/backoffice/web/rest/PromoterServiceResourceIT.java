package pt.famility.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import pt.famility.backoffice.IntegrationTest;
import pt.famility.backoffice.domain.PromoterService;
import pt.famility.backoffice.repository.PromoterServiceRepository;
import pt.famility.backoffice.service.dto.PromoterServiceDTO;
import pt.famility.backoffice.service.mapper.PromoterServiceMapper;

/**
 * Integration tests for the {@link PromoterServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PromoterServiceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_NEEDS_ETA = false;
    private static final Boolean UPDATED_NEEDS_ETA = true;

    private static final String DEFAULT_ENROLLMENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_ENROLLMENT_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/promoter-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromoterServiceRepository promoterServiceRepository;

    @Autowired
    private PromoterServiceMapper promoterServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromoterServiceMockMvc;

    private PromoterService promoterService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoterService createEntity(EntityManager em) {
        PromoterService promoterService = new PromoterService()
            .name(DEFAULT_NAME)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .needsETA(DEFAULT_NEEDS_ETA)
            .enrollmentURL(DEFAULT_ENROLLMENT_URL);
        return promoterService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoterService createUpdatedEntity(EntityManager em) {
        PromoterService promoterService = new PromoterService()
            .name(UPDATED_NAME)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .needsETA(UPDATED_NEEDS_ETA)
            .enrollmentURL(UPDATED_ENROLLMENT_URL);
        return promoterService;
    }

    @BeforeEach
    public void initTest() {
        promoterService = createEntity(em);
    }

    @Test
    @Transactional
    void createPromoterService() throws Exception {
        int databaseSizeBeforeCreate = promoterServiceRepository.findAll().size();
        // Create the PromoterService
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);
        restPromoterServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeCreate + 1);
        PromoterService testPromoterService = promoterServiceList.get(promoterServiceList.size() - 1);
        assertThat(testPromoterService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPromoterService.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testPromoterService.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testPromoterService.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPromoterService.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPromoterService.getNeedsETA()).isEqualTo(DEFAULT_NEEDS_ETA);
        assertThat(testPromoterService.getEnrollmentURL()).isEqualTo(DEFAULT_ENROLLMENT_URL);
    }

    @Test
    @Transactional
    void createPromoterServiceWithExistingId() throws Exception {
        // Create the PromoterService with an existing ID
        promoterService.setId(1L);
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        int databaseSizeBeforeCreate = promoterServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromoterServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterServiceRepository.findAll().size();
        // set the field null
        promoterService.setName(null);

        // Create the PromoterService, which fails.
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        restPromoterServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterServiceRepository.findAll().size();
        // set the field null
        promoterService.setStartDate(null);

        // Create the PromoterService, which fails.
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        restPromoterServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNeedsETAIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterServiceRepository.findAll().size();
        // set the field null
        promoterService.setNeedsETA(null);

        // Create the PromoterService, which fails.
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        restPromoterServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnrollmentURLIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterServiceRepository.findAll().size();
        // set the field null
        promoterService.setEnrollmentURL(null);

        // Create the PromoterService, which fails.
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        restPromoterServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPromoterServices() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList
        restPromoterServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoterService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].needsETA").value(hasItem(DEFAULT_NEEDS_ETA.booleanValue())))
            .andExpect(jsonPath("$.[*].enrollmentURL").value(hasItem(DEFAULT_ENROLLMENT_URL)));
    }

    @Test
    @Transactional
    void getPromoterService() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get the promoterService
        restPromoterServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, promoterService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promoterService.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.needsETA").value(DEFAULT_NEEDS_ETA.booleanValue()))
            .andExpect(jsonPath("$.enrollmentURL").value(DEFAULT_ENROLLMENT_URL));
    }

    @Test
    @Transactional
    void getPromoterServicesByIdFiltering() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        Long id = promoterService.getId();

        defaultPromoterServiceShouldBeFound("id.equals=" + id);
        defaultPromoterServiceShouldNotBeFound("id.notEquals=" + id);

        defaultPromoterServiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPromoterServiceShouldNotBeFound("id.greaterThan=" + id);

        defaultPromoterServiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPromoterServiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where name equals to DEFAULT_NAME
        defaultPromoterServiceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the promoterServiceList where name equals to UPDATED_NAME
        defaultPromoterServiceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPromoterServiceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the promoterServiceList where name equals to UPDATED_NAME
        defaultPromoterServiceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where name is not null
        defaultPromoterServiceShouldBeFound("name.specified=true");

        // Get all the promoterServiceList where name is null
        defaultPromoterServiceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNameContainsSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where name contains DEFAULT_NAME
        defaultPromoterServiceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the promoterServiceList where name contains UPDATED_NAME
        defaultPromoterServiceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where name does not contain DEFAULT_NAME
        defaultPromoterServiceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the promoterServiceList where name does not contain UPDATED_NAME
        defaultPromoterServiceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where startDate equals to DEFAULT_START_DATE
        defaultPromoterServiceShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the promoterServiceList where startDate equals to UPDATED_START_DATE
        defaultPromoterServiceShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultPromoterServiceShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the promoterServiceList where startDate equals to UPDATED_START_DATE
        defaultPromoterServiceShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where startDate is not null
        defaultPromoterServiceShouldBeFound("startDate.specified=true");

        // Get all the promoterServiceList where startDate is null
        defaultPromoterServiceShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterServicesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultPromoterServiceShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the promoterServiceList where startDate is greater than or equal to UPDATED_START_DATE
        defaultPromoterServiceShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where startDate is less than or equal to DEFAULT_START_DATE
        defaultPromoterServiceShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the promoterServiceList where startDate is less than or equal to SMALLER_START_DATE
        defaultPromoterServiceShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where startDate is less than DEFAULT_START_DATE
        defaultPromoterServiceShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the promoterServiceList where startDate is less than UPDATED_START_DATE
        defaultPromoterServiceShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where startDate is greater than DEFAULT_START_DATE
        defaultPromoterServiceShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the promoterServiceList where startDate is greater than SMALLER_START_DATE
        defaultPromoterServiceShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where endDate equals to DEFAULT_END_DATE
        defaultPromoterServiceShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the promoterServiceList where endDate equals to UPDATED_END_DATE
        defaultPromoterServiceShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultPromoterServiceShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the promoterServiceList where endDate equals to UPDATED_END_DATE
        defaultPromoterServiceShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where endDate is not null
        defaultPromoterServiceShouldBeFound("endDate.specified=true");

        // Get all the promoterServiceList where endDate is null
        defaultPromoterServiceShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultPromoterServiceShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the promoterServiceList where endDate is greater than or equal to UPDATED_END_DATE
        defaultPromoterServiceShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where endDate is less than or equal to DEFAULT_END_DATE
        defaultPromoterServiceShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the promoterServiceList where endDate is less than or equal to SMALLER_END_DATE
        defaultPromoterServiceShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where endDate is less than DEFAULT_END_DATE
        defaultPromoterServiceShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the promoterServiceList where endDate is less than UPDATED_END_DATE
        defaultPromoterServiceShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where endDate is greater than DEFAULT_END_DATE
        defaultPromoterServiceShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the promoterServiceList where endDate is greater than SMALLER_END_DATE
        defaultPromoterServiceShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNeedsETAIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where needsETA equals to DEFAULT_NEEDS_ETA
        defaultPromoterServiceShouldBeFound("needsETA.equals=" + DEFAULT_NEEDS_ETA);

        // Get all the promoterServiceList where needsETA equals to UPDATED_NEEDS_ETA
        defaultPromoterServiceShouldNotBeFound("needsETA.equals=" + UPDATED_NEEDS_ETA);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNeedsETAIsInShouldWork() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where needsETA in DEFAULT_NEEDS_ETA or UPDATED_NEEDS_ETA
        defaultPromoterServiceShouldBeFound("needsETA.in=" + DEFAULT_NEEDS_ETA + "," + UPDATED_NEEDS_ETA);

        // Get all the promoterServiceList where needsETA equals to UPDATED_NEEDS_ETA
        defaultPromoterServiceShouldNotBeFound("needsETA.in=" + UPDATED_NEEDS_ETA);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByNeedsETAIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where needsETA is not null
        defaultPromoterServiceShouldBeFound("needsETA.specified=true");

        // Get all the promoterServiceList where needsETA is null
        defaultPromoterServiceShouldNotBeFound("needsETA.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEnrollmentURLIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where enrollmentURL equals to DEFAULT_ENROLLMENT_URL
        defaultPromoterServiceShouldBeFound("enrollmentURL.equals=" + DEFAULT_ENROLLMENT_URL);

        // Get all the promoterServiceList where enrollmentURL equals to UPDATED_ENROLLMENT_URL
        defaultPromoterServiceShouldNotBeFound("enrollmentURL.equals=" + UPDATED_ENROLLMENT_URL);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEnrollmentURLIsInShouldWork() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where enrollmentURL in DEFAULT_ENROLLMENT_URL or UPDATED_ENROLLMENT_URL
        defaultPromoterServiceShouldBeFound("enrollmentURL.in=" + DEFAULT_ENROLLMENT_URL + "," + UPDATED_ENROLLMENT_URL);

        // Get all the promoterServiceList where enrollmentURL equals to UPDATED_ENROLLMENT_URL
        defaultPromoterServiceShouldNotBeFound("enrollmentURL.in=" + UPDATED_ENROLLMENT_URL);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEnrollmentURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where enrollmentURL is not null
        defaultPromoterServiceShouldBeFound("enrollmentURL.specified=true");

        // Get all the promoterServiceList where enrollmentURL is null
        defaultPromoterServiceShouldNotBeFound("enrollmentURL.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEnrollmentURLContainsSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where enrollmentURL contains DEFAULT_ENROLLMENT_URL
        defaultPromoterServiceShouldBeFound("enrollmentURL.contains=" + DEFAULT_ENROLLMENT_URL);

        // Get all the promoterServiceList where enrollmentURL contains UPDATED_ENROLLMENT_URL
        defaultPromoterServiceShouldNotBeFound("enrollmentURL.contains=" + UPDATED_ENROLLMENT_URL);
    }

    @Test
    @Transactional
    void getAllPromoterServicesByEnrollmentURLNotContainsSomething() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        // Get all the promoterServiceList where enrollmentURL does not contain DEFAULT_ENROLLMENT_URL
        defaultPromoterServiceShouldNotBeFound("enrollmentURL.doesNotContain=" + DEFAULT_ENROLLMENT_URL);

        // Get all the promoterServiceList where enrollmentURL does not contain UPDATED_ENROLLMENT_URL
        defaultPromoterServiceShouldBeFound("enrollmentURL.doesNotContain=" + UPDATED_ENROLLMENT_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPromoterServiceShouldBeFound(String filter) throws Exception {
        restPromoterServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoterService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].needsETA").value(hasItem(DEFAULT_NEEDS_ETA.booleanValue())))
            .andExpect(jsonPath("$.[*].enrollmentURL").value(hasItem(DEFAULT_ENROLLMENT_URL)));

        // Check, that the count call also returns 1
        restPromoterServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPromoterServiceShouldNotBeFound(String filter) throws Exception {
        restPromoterServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPromoterServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPromoterService() throws Exception {
        // Get the promoterService
        restPromoterServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPromoterService() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();

        // Update the promoterService
        PromoterService updatedPromoterService = promoterServiceRepository.findById(promoterService.getId()).get();
        // Disconnect from session so that the updates on updatedPromoterService are not directly saved in db
        em.detach(updatedPromoterService);
        updatedPromoterService
            .name(UPDATED_NAME)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .needsETA(UPDATED_NEEDS_ETA)
            .enrollmentURL(UPDATED_ENROLLMENT_URL);
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(updatedPromoterService);

        restPromoterServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoterServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
        PromoterService testPromoterService = promoterServiceList.get(promoterServiceList.size() - 1);
        assertThat(testPromoterService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterService.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testPromoterService.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testPromoterService.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPromoterService.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPromoterService.getNeedsETA()).isEqualTo(UPDATED_NEEDS_ETA);
        assertThat(testPromoterService.getEnrollmentURL()).isEqualTo(UPDATED_ENROLLMENT_URL);
    }

    @Test
    @Transactional
    void putNonExistingPromoterService() throws Exception {
        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();
        promoterService.setId(count.incrementAndGet());

        // Create the PromoterService
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoterServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoterServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromoterService() throws Exception {
        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();
        promoterService.setId(count.incrementAndGet());

        // Create the PromoterService
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromoterService() throws Exception {
        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();
        promoterService.setId(count.incrementAndGet());

        // Create the PromoterService
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterServiceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromoterServiceWithPatch() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();

        // Update the promoterService using partial update
        PromoterService partialUpdatedPromoterService = new PromoterService();
        partialUpdatedPromoterService.setId(promoterService.getId());

        partialUpdatedPromoterService
            .name(UPDATED_NAME)
            .endDate(UPDATED_END_DATE)
            .needsETA(UPDATED_NEEDS_ETA)
            .enrollmentURL(UPDATED_ENROLLMENT_URL);

        restPromoterServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoterService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoterService))
            )
            .andExpect(status().isOk());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
        PromoterService testPromoterService = promoterServiceList.get(promoterServiceList.size() - 1);
        assertThat(testPromoterService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterService.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testPromoterService.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testPromoterService.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPromoterService.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPromoterService.getNeedsETA()).isEqualTo(UPDATED_NEEDS_ETA);
        assertThat(testPromoterService.getEnrollmentURL()).isEqualTo(UPDATED_ENROLLMENT_URL);
    }

    @Test
    @Transactional
    void fullUpdatePromoterServiceWithPatch() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();

        // Update the promoterService using partial update
        PromoterService partialUpdatedPromoterService = new PromoterService();
        partialUpdatedPromoterService.setId(promoterService.getId());

        partialUpdatedPromoterService
            .name(UPDATED_NAME)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .needsETA(UPDATED_NEEDS_ETA)
            .enrollmentURL(UPDATED_ENROLLMENT_URL);

        restPromoterServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoterService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoterService))
            )
            .andExpect(status().isOk());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
        PromoterService testPromoterService = promoterServiceList.get(promoterServiceList.size() - 1);
        assertThat(testPromoterService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterService.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testPromoterService.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testPromoterService.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPromoterService.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPromoterService.getNeedsETA()).isEqualTo(UPDATED_NEEDS_ETA);
        assertThat(testPromoterService.getEnrollmentURL()).isEqualTo(UPDATED_ENROLLMENT_URL);
    }

    @Test
    @Transactional
    void patchNonExistingPromoterService() throws Exception {
        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();
        promoterService.setId(count.incrementAndGet());

        // Create the PromoterService
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoterServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promoterServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromoterService() throws Exception {
        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();
        promoterService.setId(count.incrementAndGet());

        // Create the PromoterService
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromoterService() throws Exception {
        int databaseSizeBeforeUpdate = promoterServiceRepository.findAll().size();
        promoterService.setId(count.incrementAndGet());

        // Create the PromoterService
        PromoterServiceDTO promoterServiceDTO = promoterServiceMapper.toDto(promoterService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterServiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoterService in the database
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromoterService() throws Exception {
        // Initialize the database
        promoterServiceRepository.saveAndFlush(promoterService);

        int databaseSizeBeforeDelete = promoterServiceRepository.findAll().size();

        // Delete the promoterService
        restPromoterServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, promoterService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PromoterService> promoterServiceList = promoterServiceRepository.findAll();
        assertThat(promoterServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package pt.famility.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.IntegrationTest;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.domain.PromoterService;
import pt.famility.backoffice.repository.PromoterItineraryRepository;
import pt.famility.backoffice.service.PromoterItineraryService;
import pt.famility.backoffice.service.criteria.PromoterItineraryCriteria;
import pt.famility.backoffice.service.dto.PromoterItineraryDTO;
import pt.famility.backoffice.service.mapper.PromoterItineraryMapper;

/**
 * Integration tests for the {@link PromoterItineraryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PromoterItineraryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/promoter-itineraries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromoterItineraryRepository promoterItineraryRepository;

    @Mock
    private PromoterItineraryRepository promoterItineraryRepositoryMock;

    @Autowired
    private PromoterItineraryMapper promoterItineraryMapper;

    @Mock
    private PromoterItineraryService promoterItineraryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromoterItineraryMockMvc;

    private PromoterItinerary promoterItinerary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoterItinerary createEntity(EntityManager em) {
        PromoterItinerary promoterItinerary = new PromoterItinerary()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        // Add required entity
        PromoterService promoterService;
        if (TestUtil.findAll(em, PromoterService.class).isEmpty()) {
            promoterService = PromoterServiceResourceIT.createEntity(em);
            em.persist(promoterService);
            em.flush();
        } else {
            promoterService = TestUtil.findAll(em, PromoterService.class).get(0);
        }
        promoterItinerary.setService(promoterService);
        // Add required entity
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            organization = OrganizationResourceIT.createEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        promoterItinerary.setOrganization(organization);
        return promoterItinerary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoterItinerary createUpdatedEntity(EntityManager em) {
        PromoterItinerary promoterItinerary = new PromoterItinerary()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        // Add required entity
        PromoterService promoterService;
        if (TestUtil.findAll(em, PromoterService.class).isEmpty()) {
            promoterService = PromoterServiceResourceIT.createUpdatedEntity(em);
            em.persist(promoterService);
            em.flush();
        } else {
            promoterService = TestUtil.findAll(em, PromoterService.class).get(0);
        }
        promoterItinerary.setService(promoterService);
        // Add required entity
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            organization = OrganizationResourceIT.createUpdatedEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        promoterItinerary.setOrganization(organization);
        return promoterItinerary;
    }

    @BeforeEach
    public void initTest() {
        promoterItinerary = createEntity(em);
    }

    @Test
    @Transactional
    void createPromoterItinerary() throws Exception {
        int databaseSizeBeforeCreate = promoterItineraryRepository.findAll().size();
        // Create the PromoterItinerary
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);
        restPromoterItineraryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeCreate + 1);
        PromoterItinerary testPromoterItinerary = promoterItineraryList.get(promoterItineraryList.size() - 1);
        assertThat(testPromoterItinerary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPromoterItinerary.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPromoterItinerary.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createPromoterItineraryWithExistingId() throws Exception {
        // Create the PromoterItinerary with an existing ID
        promoterItinerary.setId(1L);
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        int databaseSizeBeforeCreate = promoterItineraryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromoterItineraryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterItineraryRepository.findAll().size();
        // set the field null
        promoterItinerary.setName(null);

        // Create the PromoterItinerary, which fails.
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        restPromoterItineraryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterItineraryRepository.findAll().size();
        // set the field null
        promoterItinerary.setStartDate(null);

        // Create the PromoterItinerary, which fails.
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        restPromoterItineraryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPromoterItineraries() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList
        restPromoterItineraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoterItinerary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromoterItinerariesWithEagerRelationshipsIsEnabled() throws Exception {
        when(promoterItineraryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromoterItineraryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(promoterItineraryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromoterItinerariesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(promoterItineraryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromoterItineraryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(promoterItineraryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPromoterItinerary() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get the promoterItinerary
        restPromoterItineraryMockMvc
            .perform(get(ENTITY_API_URL_ID, promoterItinerary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promoterItinerary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getPromoterItinerariesByIdFiltering() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        Long id = promoterItinerary.getId();

        defaultPromoterItineraryShouldBeFound("id.equals=" + id);
        defaultPromoterItineraryShouldNotBeFound("id.notEquals=" + id);

        defaultPromoterItineraryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPromoterItineraryShouldNotBeFound("id.greaterThan=" + id);

        defaultPromoterItineraryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPromoterItineraryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where name equals to DEFAULT_NAME
        defaultPromoterItineraryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the promoterItineraryList where name equals to UPDATED_NAME
        defaultPromoterItineraryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPromoterItineraryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the promoterItineraryList where name equals to UPDATED_NAME
        defaultPromoterItineraryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where name is not null
        defaultPromoterItineraryShouldBeFound("name.specified=true");

        // Get all the promoterItineraryList where name is null
        defaultPromoterItineraryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByNameContainsSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where name contains DEFAULT_NAME
        defaultPromoterItineraryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the promoterItineraryList where name contains UPDATED_NAME
        defaultPromoterItineraryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where name does not contain DEFAULT_NAME
        defaultPromoterItineraryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the promoterItineraryList where name does not contain UPDATED_NAME
        defaultPromoterItineraryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where startDate equals to DEFAULT_START_DATE
        defaultPromoterItineraryShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the promoterItineraryList where startDate equals to UPDATED_START_DATE
        defaultPromoterItineraryShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultPromoterItineraryShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the promoterItineraryList where startDate equals to UPDATED_START_DATE
        defaultPromoterItineraryShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where startDate is not null
        defaultPromoterItineraryShouldBeFound("startDate.specified=true");

        // Get all the promoterItineraryList where startDate is null
        defaultPromoterItineraryShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultPromoterItineraryShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the promoterItineraryList where startDate is greater than or equal to UPDATED_START_DATE
        defaultPromoterItineraryShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where startDate is less than or equal to DEFAULT_START_DATE
        defaultPromoterItineraryShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the promoterItineraryList where startDate is less than or equal to SMALLER_START_DATE
        defaultPromoterItineraryShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where startDate is less than DEFAULT_START_DATE
        defaultPromoterItineraryShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the promoterItineraryList where startDate is less than UPDATED_START_DATE
        defaultPromoterItineraryShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where startDate is greater than DEFAULT_START_DATE
        defaultPromoterItineraryShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the promoterItineraryList where startDate is greater than SMALLER_START_DATE
        defaultPromoterItineraryShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where endDate equals to DEFAULT_END_DATE
        defaultPromoterItineraryShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the promoterItineraryList where endDate equals to UPDATED_END_DATE
        defaultPromoterItineraryShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultPromoterItineraryShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the promoterItineraryList where endDate equals to UPDATED_END_DATE
        defaultPromoterItineraryShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where endDate is not null
        defaultPromoterItineraryShouldBeFound("endDate.specified=true");

        // Get all the promoterItineraryList where endDate is null
        defaultPromoterItineraryShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultPromoterItineraryShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the promoterItineraryList where endDate is greater than or equal to UPDATED_END_DATE
        defaultPromoterItineraryShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where endDate is less than or equal to DEFAULT_END_DATE
        defaultPromoterItineraryShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the promoterItineraryList where endDate is less than or equal to SMALLER_END_DATE
        defaultPromoterItineraryShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where endDate is less than DEFAULT_END_DATE
        defaultPromoterItineraryShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the promoterItineraryList where endDate is less than UPDATED_END_DATE
        defaultPromoterItineraryShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        // Get all the promoterItineraryList where endDate is greater than DEFAULT_END_DATE
        defaultPromoterItineraryShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the promoterItineraryList where endDate is greater than SMALLER_END_DATE
        defaultPromoterItineraryShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByServiceIsEqualToSomething() throws Exception {
        PromoterService service;
        if (TestUtil.findAll(em, PromoterService.class).isEmpty()) {
            promoterItineraryRepository.saveAndFlush(promoterItinerary);
            service = PromoterServiceResourceIT.createEntity(em);
        } else {
            service = TestUtil.findAll(em, PromoterService.class).get(0);
        }
        em.persist(service);
        em.flush();
        promoterItinerary.setService(service);
        promoterItineraryRepository.saveAndFlush(promoterItinerary);
        Long serviceId = service.getId();

        // Get all the promoterItineraryList where service equals to serviceId
        defaultPromoterItineraryShouldBeFound("serviceId.equals=" + serviceId);

        // Get all the promoterItineraryList where service equals to (serviceId + 1)
        defaultPromoterItineraryShouldNotBeFound("serviceId.equals=" + (serviceId + 1));
    }

    @Test
    @Transactional
    void getAllPromoterItinerariesByOrganizationIsEqualToSomething() throws Exception {
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            promoterItineraryRepository.saveAndFlush(promoterItinerary);
            organization = OrganizationResourceIT.createEntity(em);
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        em.persist(organization);
        em.flush();
        promoterItinerary.setOrganization(organization);
        promoterItineraryRepository.saveAndFlush(promoterItinerary);
        Long organizationId = organization.getId();

        // Get all the promoterItineraryList where organization equals to organizationId
        defaultPromoterItineraryShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the promoterItineraryList where organization equals to (organizationId + 1)
        defaultPromoterItineraryShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPromoterItineraryShouldBeFound(String filter) throws Exception {
        restPromoterItineraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoterItinerary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restPromoterItineraryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPromoterItineraryShouldNotBeFound(String filter) throws Exception {
        restPromoterItineraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPromoterItineraryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPromoterItinerary() throws Exception {
        // Get the promoterItinerary
        restPromoterItineraryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPromoterItinerary() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();

        // Update the promoterItinerary
        PromoterItinerary updatedPromoterItinerary = promoterItineraryRepository.findById(promoterItinerary.getId()).get();
        // Disconnect from session so that the updates on updatedPromoterItinerary are not directly saved in db
        em.detach(updatedPromoterItinerary);
        updatedPromoterItinerary.name(UPDATED_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(updatedPromoterItinerary);

        restPromoterItineraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoterItineraryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isOk());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
        PromoterItinerary testPromoterItinerary = promoterItineraryList.get(promoterItineraryList.size() - 1);
        assertThat(testPromoterItinerary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterItinerary.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPromoterItinerary.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPromoterItinerary() throws Exception {
        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();
        promoterItinerary.setId(count.incrementAndGet());

        // Create the PromoterItinerary
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoterItineraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoterItineraryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromoterItinerary() throws Exception {
        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();
        promoterItinerary.setId(count.incrementAndGet());

        // Create the PromoterItinerary
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterItineraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromoterItinerary() throws Exception {
        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();
        promoterItinerary.setId(count.incrementAndGet());

        // Create the PromoterItinerary
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterItineraryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromoterItineraryWithPatch() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();

        // Update the promoterItinerary using partial update
        PromoterItinerary partialUpdatedPromoterItinerary = new PromoterItinerary();
        partialUpdatedPromoterItinerary.setId(promoterItinerary.getId());

        restPromoterItineraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoterItinerary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoterItinerary))
            )
            .andExpect(status().isOk());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
        PromoterItinerary testPromoterItinerary = promoterItineraryList.get(promoterItineraryList.size() - 1);
        assertThat(testPromoterItinerary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPromoterItinerary.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPromoterItinerary.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePromoterItineraryWithPatch() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();

        // Update the promoterItinerary using partial update
        PromoterItinerary partialUpdatedPromoterItinerary = new PromoterItinerary();
        partialUpdatedPromoterItinerary.setId(promoterItinerary.getId());

        partialUpdatedPromoterItinerary.name(UPDATED_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restPromoterItineraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoterItinerary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoterItinerary))
            )
            .andExpect(status().isOk());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
        PromoterItinerary testPromoterItinerary = promoterItineraryList.get(promoterItineraryList.size() - 1);
        assertThat(testPromoterItinerary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterItinerary.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPromoterItinerary.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPromoterItinerary() throws Exception {
        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();
        promoterItinerary.setId(count.incrementAndGet());

        // Create the PromoterItinerary
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoterItineraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promoterItineraryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromoterItinerary() throws Exception {
        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();
        promoterItinerary.setId(count.incrementAndGet());

        // Create the PromoterItinerary
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterItineraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromoterItinerary() throws Exception {
        int databaseSizeBeforeUpdate = promoterItineraryRepository.findAll().size();
        promoterItinerary.setId(count.incrementAndGet());

        // Create the PromoterItinerary
        PromoterItineraryDTO promoterItineraryDTO = promoterItineraryMapper.toDto(promoterItinerary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterItineraryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterItineraryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoterItinerary in the database
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromoterItinerary() throws Exception {
        // Initialize the database
        promoterItineraryRepository.saveAndFlush(promoterItinerary);

        int databaseSizeBeforeDelete = promoterItineraryRepository.findAll().size();

        // Delete the promoterItinerary
        restPromoterItineraryMockMvc
            .perform(delete(ENTITY_API_URL_ID, promoterItinerary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PromoterItinerary> promoterItineraryList = promoterItineraryRepository.findAll();
        assertThat(promoterItineraryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

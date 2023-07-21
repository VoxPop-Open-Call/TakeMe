package pt.famility.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.domain.PromoterStopPoint;
import pt.famility.backoffice.repository.PromoterStopPointRepository;
import pt.famility.backoffice.service.PromoterStopPointService;
import pt.famility.backoffice.service.criteria.PromoterStopPointCriteria;
import pt.famility.backoffice.service.dto.PromoterStopPointDTO;
import pt.famility.backoffice.service.mapper.PromoterStopPointMapper;

/**
 * Integration tests for the {@link PromoterStopPointResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PromoterStopPointResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_SCHEDULED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/promoter-stop-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromoterStopPointRepository promoterStopPointRepository;

    @Mock
    private PromoterStopPointRepository promoterStopPointRepositoryMock;

    @Autowired
    private PromoterStopPointMapper promoterStopPointMapper;

    @Mock
    private PromoterStopPointService promoterStopPointServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromoterStopPointMockMvc;

    private PromoterStopPoint promoterStopPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoterStopPoint createEntity(EntityManager em) {
        PromoterStopPoint promoterStopPoint = new PromoterStopPoint().name(DEFAULT_NAME).scheduledTime(DEFAULT_SCHEDULED_TIME);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        promoterStopPoint.setLocation(location);
        return promoterStopPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoterStopPoint createUpdatedEntity(EntityManager em) {
        PromoterStopPoint promoterStopPoint = new PromoterStopPoint().name(UPDATED_NAME).scheduledTime(UPDATED_SCHEDULED_TIME);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        promoterStopPoint.setLocation(location);
        return promoterStopPoint;
    }

    @BeforeEach
    public void initTest() {
        promoterStopPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createPromoterStopPoint() throws Exception {
        int databaseSizeBeforeCreate = promoterStopPointRepository.findAll().size();
        // Create the PromoterStopPoint
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);
        restPromoterStopPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeCreate + 1);
        PromoterStopPoint testPromoterStopPoint = promoterStopPointList.get(promoterStopPointList.size() - 1);
        assertThat(testPromoterStopPoint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPromoterStopPoint.getScheduledTime()).isEqualTo(DEFAULT_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    void createPromoterStopPointWithExistingId() throws Exception {
        // Create the PromoterStopPoint with an existing ID
        promoterStopPoint.setId(1L);
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        int databaseSizeBeforeCreate = promoterStopPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromoterStopPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterStopPointRepository.findAll().size();
        // set the field null
        promoterStopPoint.setName(null);

        // Create the PromoterStopPoint, which fails.
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        restPromoterStopPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduledTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoterStopPointRepository.findAll().size();
        // set the field null
        promoterStopPoint.setScheduledTime(null);

        // Create the PromoterStopPoint, which fails.
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        restPromoterStopPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isBadRequest());

        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPromoterStopPoints() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList
        restPromoterStopPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoterStopPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scheduledTime").value(hasItem(DEFAULT_SCHEDULED_TIME.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromoterStopPointsWithEagerRelationshipsIsEnabled() throws Exception {
        when(promoterStopPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromoterStopPointMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(promoterStopPointServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromoterStopPointsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(promoterStopPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromoterStopPointMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(promoterStopPointRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPromoterStopPoint() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get the promoterStopPoint
        restPromoterStopPointMockMvc
            .perform(get(ENTITY_API_URL_ID, promoterStopPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promoterStopPoint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.scheduledTime").value(DEFAULT_SCHEDULED_TIME.toString()));
    }

    @Test
    @Transactional
    void getPromoterStopPointsByIdFiltering() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        Long id = promoterStopPoint.getId();

        defaultPromoterStopPointShouldBeFound("id.equals=" + id);
        defaultPromoterStopPointShouldNotBeFound("id.notEquals=" + id);

        defaultPromoterStopPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPromoterStopPointShouldNotBeFound("id.greaterThan=" + id);

        defaultPromoterStopPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPromoterStopPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where name equals to DEFAULT_NAME
        defaultPromoterStopPointShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the promoterStopPointList where name equals to UPDATED_NAME
        defaultPromoterStopPointShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPromoterStopPointShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the promoterStopPointList where name equals to UPDATED_NAME
        defaultPromoterStopPointShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where name is not null
        defaultPromoterStopPointShouldBeFound("name.specified=true");

        // Get all the promoterStopPointList where name is null
        defaultPromoterStopPointShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByNameContainsSomething() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where name contains DEFAULT_NAME
        defaultPromoterStopPointShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the promoterStopPointList where name contains UPDATED_NAME
        defaultPromoterStopPointShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where name does not contain DEFAULT_NAME
        defaultPromoterStopPointShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the promoterStopPointList where name does not contain UPDATED_NAME
        defaultPromoterStopPointShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByScheduledTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where scheduledTime equals to DEFAULT_SCHEDULED_TIME
        defaultPromoterStopPointShouldBeFound("scheduledTime.equals=" + DEFAULT_SCHEDULED_TIME);

        // Get all the promoterStopPointList where scheduledTime equals to UPDATED_SCHEDULED_TIME
        defaultPromoterStopPointShouldNotBeFound("scheduledTime.equals=" + UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByScheduledTimeIsInShouldWork() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where scheduledTime in DEFAULT_SCHEDULED_TIME or UPDATED_SCHEDULED_TIME
        defaultPromoterStopPointShouldBeFound("scheduledTime.in=" + DEFAULT_SCHEDULED_TIME + "," + UPDATED_SCHEDULED_TIME);

        // Get all the promoterStopPointList where scheduledTime equals to UPDATED_SCHEDULED_TIME
        defaultPromoterStopPointShouldNotBeFound("scheduledTime.in=" + UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByScheduledTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        // Get all the promoterStopPointList where scheduledTime is not null
        defaultPromoterStopPointShouldBeFound("scheduledTime.specified=true");

        // Get all the promoterStopPointList where scheduledTime is null
        defaultPromoterStopPointShouldNotBeFound("scheduledTime.specified=false");
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByPromoterItineraryIsEqualToSomething() throws Exception {
        PromoterItinerary promoterItinerary;
        if (TestUtil.findAll(em, PromoterItinerary.class).isEmpty()) {
            promoterStopPointRepository.saveAndFlush(promoterStopPoint);
            promoterItinerary = PromoterItineraryResourceIT.createEntity(em);
        } else {
            promoterItinerary = TestUtil.findAll(em, PromoterItinerary.class).get(0);
        }
        em.persist(promoterItinerary);
        em.flush();
        promoterStopPoint.setPromoterItinerary(promoterItinerary);
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);
        Long promoterItineraryId = promoterItinerary.getId();

        // Get all the promoterStopPointList where promoterItinerary equals to promoterItineraryId
        defaultPromoterStopPointShouldBeFound("promoterItineraryId.equals=" + promoterItineraryId);

        // Get all the promoterStopPointList where promoterItinerary equals to (promoterItineraryId + 1)
        defaultPromoterStopPointShouldNotBeFound("promoterItineraryId.equals=" + (promoterItineraryId + 1));
    }

    @Test
    @Transactional
    void getAllPromoterStopPointsByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            promoterStopPointRepository.saveAndFlush(promoterStopPoint);
            location = LocationResourceIT.createEntity(em);
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        promoterStopPoint.setLocation(location);
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);
        Long locationId = location.getId();

        // Get all the promoterStopPointList where location equals to locationId
        defaultPromoterStopPointShouldBeFound("locationId.equals=" + locationId);

        // Get all the promoterStopPointList where location equals to (locationId + 1)
        defaultPromoterStopPointShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPromoterStopPointShouldBeFound(String filter) throws Exception {
        restPromoterStopPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoterStopPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scheduledTime").value(hasItem(DEFAULT_SCHEDULED_TIME.toString())));

        // Check, that the count call also returns 1
        restPromoterStopPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPromoterStopPointShouldNotBeFound(String filter) throws Exception {
        restPromoterStopPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPromoterStopPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPromoterStopPoint() throws Exception {
        // Get the promoterStopPoint
        restPromoterStopPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPromoterStopPoint() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();

        // Update the promoterStopPoint
        PromoterStopPoint updatedPromoterStopPoint = promoterStopPointRepository.findById(promoterStopPoint.getId()).get();
        // Disconnect from session so that the updates on updatedPromoterStopPoint are not directly saved in db
        em.detach(updatedPromoterStopPoint);
        updatedPromoterStopPoint.name(UPDATED_NAME).scheduledTime(UPDATED_SCHEDULED_TIME);
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(updatedPromoterStopPoint);

        restPromoterStopPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoterStopPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isOk());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
        PromoterStopPoint testPromoterStopPoint = promoterStopPointList.get(promoterStopPointList.size() - 1);
        assertThat(testPromoterStopPoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterStopPoint.getScheduledTime()).isEqualTo(UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    void putNonExistingPromoterStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();
        promoterStopPoint.setId(count.incrementAndGet());

        // Create the PromoterStopPoint
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoterStopPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoterStopPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromoterStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();
        promoterStopPoint.setId(count.incrementAndGet());

        // Create the PromoterStopPoint
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterStopPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromoterStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();
        promoterStopPoint.setId(count.incrementAndGet());

        // Create the PromoterStopPoint
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterStopPointMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromoterStopPointWithPatch() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();

        // Update the promoterStopPoint using partial update
        PromoterStopPoint partialUpdatedPromoterStopPoint = new PromoterStopPoint();
        partialUpdatedPromoterStopPoint.setId(promoterStopPoint.getId());

        partialUpdatedPromoterStopPoint.name(UPDATED_NAME).scheduledTime(UPDATED_SCHEDULED_TIME);

        restPromoterStopPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoterStopPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoterStopPoint))
            )
            .andExpect(status().isOk());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
        PromoterStopPoint testPromoterStopPoint = promoterStopPointList.get(promoterStopPointList.size() - 1);
        assertThat(testPromoterStopPoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterStopPoint.getScheduledTime()).isEqualTo(UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    void fullUpdatePromoterStopPointWithPatch() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();

        // Update the promoterStopPoint using partial update
        PromoterStopPoint partialUpdatedPromoterStopPoint = new PromoterStopPoint();
        partialUpdatedPromoterStopPoint.setId(promoterStopPoint.getId());

        partialUpdatedPromoterStopPoint.name(UPDATED_NAME).scheduledTime(UPDATED_SCHEDULED_TIME);

        restPromoterStopPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoterStopPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoterStopPoint))
            )
            .andExpect(status().isOk());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
        PromoterStopPoint testPromoterStopPoint = promoterStopPointList.get(promoterStopPointList.size() - 1);
        assertThat(testPromoterStopPoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromoterStopPoint.getScheduledTime()).isEqualTo(UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingPromoterStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();
        promoterStopPoint.setId(count.incrementAndGet());

        // Create the PromoterStopPoint
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoterStopPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promoterStopPointDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromoterStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();
        promoterStopPoint.setId(count.incrementAndGet());

        // Create the PromoterStopPoint
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterStopPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromoterStopPoint() throws Exception {
        int databaseSizeBeforeUpdate = promoterStopPointRepository.findAll().size();
        promoterStopPoint.setId(count.incrementAndGet());

        // Create the PromoterStopPoint
        PromoterStopPointDTO promoterStopPointDTO = promoterStopPointMapper.toDto(promoterStopPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoterStopPointMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoterStopPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoterStopPoint in the database
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromoterStopPoint() throws Exception {
        // Initialize the database
        promoterStopPointRepository.saveAndFlush(promoterStopPoint);

        int databaseSizeBeforeDelete = promoterStopPointRepository.findAll().size();

        // Delete the promoterStopPoint
        restPromoterStopPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, promoterStopPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PromoterStopPoint> promoterStopPointList = promoterStopPointRepository.findAll();
        assertThat(promoterStopPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

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
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.ChildItinerarySubscription;
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ChildItinerarySubscriptionRepository;
import pt.famility.backoffice.service.ChildItinerarySubscriptionService;
import pt.famility.backoffice.service.criteria.ChildItinerarySubscriptionCriteria;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.mapper.ChildItinerarySubscriptionMapper;

/**
 * Integration tests for the {@link ChildItinerarySubscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ChildItinerarySubscriptionResourceIT {

    private static final StatusType DEFAULT_STATUS_TYPE = StatusType.NEW;
    private static final StatusType UPDATED_STATUS_TYPE = StatusType.ACTIVE;

    private static final Instant DEFAULT_SUBSCRIPTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBSCRIPTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACTIVATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DEACTIVATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEACTIVATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/child-itinerary-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository;

    @Mock
    private ChildItinerarySubscriptionRepository childItinerarySubscriptionRepositoryMock;

    @Autowired
    private ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper;

    @Mock
    private ChildItinerarySubscriptionService childItinerarySubscriptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChildItinerarySubscriptionMockMvc;

    private ChildItinerarySubscription childItinerarySubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChildItinerarySubscription createEntity(EntityManager em) {
        ChildItinerarySubscription childItinerarySubscription = new ChildItinerarySubscription()
            .statusType(DEFAULT_STATUS_TYPE)
            .subscriptionDate(DEFAULT_SUBSCRIPTION_DATE)
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .deactivationDate(DEFAULT_DEACTIVATION_DATE)
            .comments(DEFAULT_COMMENTS)
            .additionalInformation(DEFAULT_ADDITIONAL_INFORMATION)
            .cardNumber(DEFAULT_CARD_NUMBER);
        // Add required entity
        Child child;
        if (TestUtil.findAll(em, Child.class).isEmpty()) {
            child = ChildResourceIT.createEntity(em);
            em.persist(child);
            em.flush();
        } else {
            child = TestUtil.findAll(em, Child.class).get(0);
        }
        childItinerarySubscription.setChild(child);
        // Add required entity
        PromoterItinerary promoterItinerary;
        if (TestUtil.findAll(em, PromoterItinerary.class).isEmpty()) {
            promoterItinerary = PromoterItineraryResourceIT.createEntity(em);
            em.persist(promoterItinerary);
            em.flush();
        } else {
            promoterItinerary = TestUtil.findAll(em, PromoterItinerary.class).get(0);
        }
        childItinerarySubscription.setPromoterItinerary(promoterItinerary);
        return childItinerarySubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChildItinerarySubscription createUpdatedEntity(EntityManager em) {
        ChildItinerarySubscription childItinerarySubscription = new ChildItinerarySubscription()
            .statusType(UPDATED_STATUS_TYPE)
            .subscriptionDate(UPDATED_SUBSCRIPTION_DATE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .deactivationDate(UPDATED_DEACTIVATION_DATE)
            .comments(UPDATED_COMMENTS)
            .additionalInformation(UPDATED_ADDITIONAL_INFORMATION)
            .cardNumber(UPDATED_CARD_NUMBER);
        // Add required entity
        Child child;
        if (TestUtil.findAll(em, Child.class).isEmpty()) {
            child = ChildResourceIT.createUpdatedEntity(em);
            em.persist(child);
            em.flush();
        } else {
            child = TestUtil.findAll(em, Child.class).get(0);
        }
        childItinerarySubscription.setChild(child);
        // Add required entity
        PromoterItinerary promoterItinerary;
        if (TestUtil.findAll(em, PromoterItinerary.class).isEmpty()) {
            promoterItinerary = PromoterItineraryResourceIT.createUpdatedEntity(em);
            em.persist(promoterItinerary);
            em.flush();
        } else {
            promoterItinerary = TestUtil.findAll(em, PromoterItinerary.class).get(0);
        }
        childItinerarySubscription.setPromoterItinerary(promoterItinerary);
        return childItinerarySubscription;
    }

    @BeforeEach
    public void initTest() {
        childItinerarySubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createChildItinerarySubscription() throws Exception {
        int databaseSizeBeforeCreate = childItinerarySubscriptionRepository.findAll().size();
        // Create the ChildItinerarySubscription
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);
        restChildItinerarySubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        ChildItinerarySubscription testChildItinerarySubscription = childItinerarySubscriptionList.get(
            childItinerarySubscriptionList.size() - 1
        );
        assertThat(testChildItinerarySubscription.getStatusType()).isEqualTo(DEFAULT_STATUS_TYPE);
        assertThat(testChildItinerarySubscription.getSubscriptionDate()).isEqualTo(DEFAULT_SUBSCRIPTION_DATE);
        assertThat(testChildItinerarySubscription.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getDeactivationDate()).isEqualTo(DEFAULT_DEACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testChildItinerarySubscription.getAdditionalInformation()).isEqualTo(DEFAULT_ADDITIONAL_INFORMATION);
        assertThat(testChildItinerarySubscription.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
    }

    @Test
    @Transactional
    void createChildItinerarySubscriptionWithExistingId() throws Exception {
        // Create the ChildItinerarySubscription with an existing ID
        childItinerarySubscription.setId(1L);
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        int databaseSizeBeforeCreate = childItinerarySubscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildItinerarySubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = childItinerarySubscriptionRepository.findAll().size();
        // set the field null
        childItinerarySubscription.setStatusType(null);

        // Create the ChildItinerarySubscription, which fails.
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        restChildItinerarySubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = childItinerarySubscriptionRepository.findAll().size();
        // set the field null
        childItinerarySubscription.setSubscriptionDate(null);

        // Create the ChildItinerarySubscription, which fails.
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        restChildItinerarySubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = childItinerarySubscriptionRepository.findAll().size();
        // set the field null
        childItinerarySubscription.setActivationDate(null);

        // Create the ChildItinerarySubscription, which fails.
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        restChildItinerarySubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptions() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList
        restChildItinerarySubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(childItinerarySubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionDate").value(hasItem(DEFAULT_SUBSCRIPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deactivationDate").value(hasItem(DEFAULT_DEACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].additionalInformation").value(hasItem(DEFAULT_ADDITIONAL_INFORMATION)))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChildItinerarySubscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(childItinerarySubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChildItinerarySubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(childItinerarySubscriptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChildItinerarySubscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(childItinerarySubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChildItinerarySubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(childItinerarySubscriptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getChildItinerarySubscription() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get the childItinerarySubscription
        restChildItinerarySubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, childItinerarySubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(childItinerarySubscription.getId().intValue()))
            .andExpect(jsonPath("$.statusType").value(DEFAULT_STATUS_TYPE.toString()))
            .andExpect(jsonPath("$.subscriptionDate").value(DEFAULT_SUBSCRIPTION_DATE.toString()))
            .andExpect(jsonPath("$.activationDate").value(DEFAULT_ACTIVATION_DATE.toString()))
            .andExpect(jsonPath("$.deactivationDate").value(DEFAULT_DEACTIVATION_DATE.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.additionalInformation").value(DEFAULT_ADDITIONAL_INFORMATION))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER));
    }

    @Test
    @Transactional
    void getChildItinerarySubscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        Long id = childItinerarySubscription.getId();

        defaultChildItinerarySubscriptionShouldBeFound("id.equals=" + id);
        defaultChildItinerarySubscriptionShouldNotBeFound("id.notEquals=" + id);

        defaultChildItinerarySubscriptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChildItinerarySubscriptionShouldNotBeFound("id.greaterThan=" + id);

        defaultChildItinerarySubscriptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChildItinerarySubscriptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where statusType equals to DEFAULT_STATUS_TYPE
        defaultChildItinerarySubscriptionShouldBeFound("statusType.equals=" + DEFAULT_STATUS_TYPE);

        // Get all the childItinerarySubscriptionList where statusType equals to UPDATED_STATUS_TYPE
        defaultChildItinerarySubscriptionShouldNotBeFound("statusType.equals=" + UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where statusType in DEFAULT_STATUS_TYPE or UPDATED_STATUS_TYPE
        defaultChildItinerarySubscriptionShouldBeFound("statusType.in=" + DEFAULT_STATUS_TYPE + "," + UPDATED_STATUS_TYPE);

        // Get all the childItinerarySubscriptionList where statusType equals to UPDATED_STATUS_TYPE
        defaultChildItinerarySubscriptionShouldNotBeFound("statusType.in=" + UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where statusType is not null
        defaultChildItinerarySubscriptionShouldBeFound("statusType.specified=true");

        // Get all the childItinerarySubscriptionList where statusType is null
        defaultChildItinerarySubscriptionShouldNotBeFound("statusType.specified=false");
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsBySubscriptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where subscriptionDate equals to DEFAULT_SUBSCRIPTION_DATE
        defaultChildItinerarySubscriptionShouldBeFound("subscriptionDate.equals=" + DEFAULT_SUBSCRIPTION_DATE);

        // Get all the childItinerarySubscriptionList where subscriptionDate equals to UPDATED_SUBSCRIPTION_DATE
        defaultChildItinerarySubscriptionShouldNotBeFound("subscriptionDate.equals=" + UPDATED_SUBSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsBySubscriptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where subscriptionDate in DEFAULT_SUBSCRIPTION_DATE or UPDATED_SUBSCRIPTION_DATE
        defaultChildItinerarySubscriptionShouldBeFound(
            "subscriptionDate.in=" + DEFAULT_SUBSCRIPTION_DATE + "," + UPDATED_SUBSCRIPTION_DATE
        );

        // Get all the childItinerarySubscriptionList where subscriptionDate equals to UPDATED_SUBSCRIPTION_DATE
        defaultChildItinerarySubscriptionShouldNotBeFound("subscriptionDate.in=" + UPDATED_SUBSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsBySubscriptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where subscriptionDate is not null
        defaultChildItinerarySubscriptionShouldBeFound("subscriptionDate.specified=true");

        // Get all the childItinerarySubscriptionList where subscriptionDate is null
        defaultChildItinerarySubscriptionShouldNotBeFound("subscriptionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByActivationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where activationDate equals to DEFAULT_ACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldBeFound("activationDate.equals=" + DEFAULT_ACTIVATION_DATE);

        // Get all the childItinerarySubscriptionList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldNotBeFound("activationDate.equals=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByActivationDateIsInShouldWork() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where activationDate in DEFAULT_ACTIVATION_DATE or UPDATED_ACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldBeFound("activationDate.in=" + DEFAULT_ACTIVATION_DATE + "," + UPDATED_ACTIVATION_DATE);

        // Get all the childItinerarySubscriptionList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldNotBeFound("activationDate.in=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByActivationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where activationDate is not null
        defaultChildItinerarySubscriptionShouldBeFound("activationDate.specified=true");

        // Get all the childItinerarySubscriptionList where activationDate is null
        defaultChildItinerarySubscriptionShouldNotBeFound("activationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByDeactivationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where deactivationDate equals to DEFAULT_DEACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldBeFound("deactivationDate.equals=" + DEFAULT_DEACTIVATION_DATE);

        // Get all the childItinerarySubscriptionList where deactivationDate equals to UPDATED_DEACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldNotBeFound("deactivationDate.equals=" + UPDATED_DEACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByDeactivationDateIsInShouldWork() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where deactivationDate in DEFAULT_DEACTIVATION_DATE or UPDATED_DEACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldBeFound(
            "deactivationDate.in=" + DEFAULT_DEACTIVATION_DATE + "," + UPDATED_DEACTIVATION_DATE
        );

        // Get all the childItinerarySubscriptionList where deactivationDate equals to UPDATED_DEACTIVATION_DATE
        defaultChildItinerarySubscriptionShouldNotBeFound("deactivationDate.in=" + UPDATED_DEACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByDeactivationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where deactivationDate is not null
        defaultChildItinerarySubscriptionShouldBeFound("deactivationDate.specified=true");

        // Get all the childItinerarySubscriptionList where deactivationDate is null
        defaultChildItinerarySubscriptionShouldNotBeFound("deactivationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where comments equals to DEFAULT_COMMENTS
        defaultChildItinerarySubscriptionShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the childItinerarySubscriptionList where comments equals to UPDATED_COMMENTS
        defaultChildItinerarySubscriptionShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultChildItinerarySubscriptionShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the childItinerarySubscriptionList where comments equals to UPDATED_COMMENTS
        defaultChildItinerarySubscriptionShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where comments is not null
        defaultChildItinerarySubscriptionShouldBeFound("comments.specified=true");

        // Get all the childItinerarySubscriptionList where comments is null
        defaultChildItinerarySubscriptionShouldNotBeFound("comments.specified=false");
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where comments contains DEFAULT_COMMENTS
        defaultChildItinerarySubscriptionShouldBeFound("comments.contains=" + DEFAULT_COMMENTS);

        // Get all the childItinerarySubscriptionList where comments contains UPDATED_COMMENTS
        defaultChildItinerarySubscriptionShouldNotBeFound("comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where comments does not contain DEFAULT_COMMENTS
        defaultChildItinerarySubscriptionShouldNotBeFound("comments.doesNotContain=" + DEFAULT_COMMENTS);

        // Get all the childItinerarySubscriptionList where comments does not contain UPDATED_COMMENTS
        defaultChildItinerarySubscriptionShouldBeFound("comments.doesNotContain=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByAdditionalInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where additionalInformation equals to DEFAULT_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldBeFound("additionalInformation.equals=" + DEFAULT_ADDITIONAL_INFORMATION);

        // Get all the childItinerarySubscriptionList where additionalInformation equals to UPDATED_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldNotBeFound("additionalInformation.equals=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByAdditionalInformationIsInShouldWork() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where additionalInformation in DEFAULT_ADDITIONAL_INFORMATION or UPDATED_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldBeFound(
            "additionalInformation.in=" + DEFAULT_ADDITIONAL_INFORMATION + "," + UPDATED_ADDITIONAL_INFORMATION
        );

        // Get all the childItinerarySubscriptionList where additionalInformation equals to UPDATED_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldNotBeFound("additionalInformation.in=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByAdditionalInformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where additionalInformation is not null
        defaultChildItinerarySubscriptionShouldBeFound("additionalInformation.specified=true");

        // Get all the childItinerarySubscriptionList where additionalInformation is null
        defaultChildItinerarySubscriptionShouldNotBeFound("additionalInformation.specified=false");
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByAdditionalInformationContainsSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where additionalInformation contains DEFAULT_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldBeFound("additionalInformation.contains=" + DEFAULT_ADDITIONAL_INFORMATION);

        // Get all the childItinerarySubscriptionList where additionalInformation contains UPDATED_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldNotBeFound("additionalInformation.contains=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByAdditionalInformationNotContainsSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where additionalInformation does not contain DEFAULT_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldNotBeFound("additionalInformation.doesNotContain=" + DEFAULT_ADDITIONAL_INFORMATION);

        // Get all the childItinerarySubscriptionList where additionalInformation does not contain UPDATED_ADDITIONAL_INFORMATION
        defaultChildItinerarySubscriptionShouldBeFound("additionalInformation.doesNotContain=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCardNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where cardNumber equals to DEFAULT_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldBeFound("cardNumber.equals=" + DEFAULT_CARD_NUMBER);

        // Get all the childItinerarySubscriptionList where cardNumber equals to UPDATED_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldNotBeFound("cardNumber.equals=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCardNumberIsInShouldWork() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where cardNumber in DEFAULT_CARD_NUMBER or UPDATED_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldBeFound("cardNumber.in=" + DEFAULT_CARD_NUMBER + "," + UPDATED_CARD_NUMBER);

        // Get all the childItinerarySubscriptionList where cardNumber equals to UPDATED_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldNotBeFound("cardNumber.in=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCardNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where cardNumber is not null
        defaultChildItinerarySubscriptionShouldBeFound("cardNumber.specified=true");

        // Get all the childItinerarySubscriptionList where cardNumber is null
        defaultChildItinerarySubscriptionShouldNotBeFound("cardNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCardNumberContainsSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where cardNumber contains DEFAULT_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldBeFound("cardNumber.contains=" + DEFAULT_CARD_NUMBER);

        // Get all the childItinerarySubscriptionList where cardNumber contains UPDATED_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldNotBeFound("cardNumber.contains=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByCardNumberNotContainsSomething() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        // Get all the childItinerarySubscriptionList where cardNumber does not contain DEFAULT_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldNotBeFound("cardNumber.doesNotContain=" + DEFAULT_CARD_NUMBER);

        // Get all the childItinerarySubscriptionList where cardNumber does not contain UPDATED_CARD_NUMBER
        defaultChildItinerarySubscriptionShouldBeFound("cardNumber.doesNotContain=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByChildIsEqualToSomething() throws Exception {
        Child child;
        if (TestUtil.findAll(em, Child.class).isEmpty()) {
            childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);
            child = ChildResourceIT.createEntity(em);
        } else {
            child = TestUtil.findAll(em, Child.class).get(0);
        }
        em.persist(child);
        em.flush();
        childItinerarySubscription.setChild(child);
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);
        Long childId = child.getId();

        // Get all the childItinerarySubscriptionList where child equals to childId
        defaultChildItinerarySubscriptionShouldBeFound("childId.equals=" + childId);

        // Get all the childItinerarySubscriptionList where child equals to (childId + 1)
        defaultChildItinerarySubscriptionShouldNotBeFound("childId.equals=" + (childId + 1));
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByPromoterItineraryIsEqualToSomething() throws Exception {
        PromoterItinerary promoterItinerary;
        if (TestUtil.findAll(em, PromoterItinerary.class).isEmpty()) {
            childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);
            promoterItinerary = PromoterItineraryResourceIT.createEntity(em);
        } else {
            promoterItinerary = TestUtil.findAll(em, PromoterItinerary.class).get(0);
        }
        em.persist(promoterItinerary);
        em.flush();
        childItinerarySubscription.setPromoterItinerary(promoterItinerary);
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);
        Long promoterItineraryId = promoterItinerary.getId();

        // Get all the childItinerarySubscriptionList where promoterItinerary equals to promoterItineraryId
        defaultChildItinerarySubscriptionShouldBeFound("promoterItineraryId.equals=" + promoterItineraryId);

        // Get all the childItinerarySubscriptionList where promoterItinerary equals to (promoterItineraryId + 1)
        defaultChildItinerarySubscriptionShouldNotBeFound("promoterItineraryId.equals=" + (promoterItineraryId + 1));
    }

    @Test
    @Transactional
    void getAllChildItinerarySubscriptionsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);
            user = UserResourceIntTest.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        childItinerarySubscription.setUser(user);
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);
        Long userId = user.getId();

        // Get all the childItinerarySubscriptionList where user equals to userId
        defaultChildItinerarySubscriptionShouldBeFound("userId.equals=" + userId);

        // Get all the childItinerarySubscriptionList where user equals to (userId + 1)
        defaultChildItinerarySubscriptionShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChildItinerarySubscriptionShouldBeFound(String filter) throws Exception {
        restChildItinerarySubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(childItinerarySubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionDate").value(hasItem(DEFAULT_SUBSCRIPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deactivationDate").value(hasItem(DEFAULT_DEACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].additionalInformation").value(hasItem(DEFAULT_ADDITIONAL_INFORMATION)))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)));

        // Check, that the count call also returns 1
        restChildItinerarySubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChildItinerarySubscriptionShouldNotBeFound(String filter) throws Exception {
        restChildItinerarySubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChildItinerarySubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChildItinerarySubscription() throws Exception {
        // Get the childItinerarySubscription
        restChildItinerarySubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChildItinerarySubscription() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();

        // Update the childItinerarySubscription
        ChildItinerarySubscription updatedChildItinerarySubscription = childItinerarySubscriptionRepository
            .findById(childItinerarySubscription.getId())
            .get();
        // Disconnect from session so that the updates on updatedChildItinerarySubscription are not directly saved in db
        em.detach(updatedChildItinerarySubscription);
        updatedChildItinerarySubscription
            .statusType(UPDATED_STATUS_TYPE)
            .subscriptionDate(UPDATED_SUBSCRIPTION_DATE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .deactivationDate(UPDATED_DEACTIVATION_DATE)
            .comments(UPDATED_COMMENTS)
            .additionalInformation(UPDATED_ADDITIONAL_INFORMATION)
            .cardNumber(UPDATED_CARD_NUMBER);
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(
            updatedChildItinerarySubscription
        );

        restChildItinerarySubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childItinerarySubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
        ChildItinerarySubscription testChildItinerarySubscription = childItinerarySubscriptionList.get(
            childItinerarySubscriptionList.size() - 1
        );
        assertThat(testChildItinerarySubscription.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
        assertThat(testChildItinerarySubscription.getSubscriptionDate()).isEqualTo(UPDATED_SUBSCRIPTION_DATE);
        assertThat(testChildItinerarySubscription.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getDeactivationDate()).isEqualTo(UPDATED_DEACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testChildItinerarySubscription.getAdditionalInformation()).isEqualTo(UPDATED_ADDITIONAL_INFORMATION);
        assertThat(testChildItinerarySubscription.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingChildItinerarySubscription() throws Exception {
        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();
        childItinerarySubscription.setId(count.incrementAndGet());

        // Create the ChildItinerarySubscription
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildItinerarySubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childItinerarySubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChildItinerarySubscription() throws Exception {
        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();
        childItinerarySubscription.setId(count.incrementAndGet());

        // Create the ChildItinerarySubscription
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildItinerarySubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChildItinerarySubscription() throws Exception {
        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();
        childItinerarySubscription.setId(count.incrementAndGet());

        // Create the ChildItinerarySubscription
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildItinerarySubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChildItinerarySubscriptionWithPatch() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();

        // Update the childItinerarySubscription using partial update
        ChildItinerarySubscription partialUpdatedChildItinerarySubscription = new ChildItinerarySubscription();
        partialUpdatedChildItinerarySubscription.setId(childItinerarySubscription.getId());

        partialUpdatedChildItinerarySubscription
            .statusType(UPDATED_STATUS_TYPE)
            .subscriptionDate(UPDATED_SUBSCRIPTION_DATE)
            .deactivationDate(UPDATED_DEACTIVATION_DATE);

        restChildItinerarySubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChildItinerarySubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChildItinerarySubscription))
            )
            .andExpect(status().isOk());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
        ChildItinerarySubscription testChildItinerarySubscription = childItinerarySubscriptionList.get(
            childItinerarySubscriptionList.size() - 1
        );
        assertThat(testChildItinerarySubscription.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
        assertThat(testChildItinerarySubscription.getSubscriptionDate()).isEqualTo(UPDATED_SUBSCRIPTION_DATE);
        assertThat(testChildItinerarySubscription.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getDeactivationDate()).isEqualTo(UPDATED_DEACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testChildItinerarySubscription.getAdditionalInformation()).isEqualTo(DEFAULT_ADDITIONAL_INFORMATION);
        assertThat(testChildItinerarySubscription.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateChildItinerarySubscriptionWithPatch() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();

        // Update the childItinerarySubscription using partial update
        ChildItinerarySubscription partialUpdatedChildItinerarySubscription = new ChildItinerarySubscription();
        partialUpdatedChildItinerarySubscription.setId(childItinerarySubscription.getId());

        partialUpdatedChildItinerarySubscription
            .statusType(UPDATED_STATUS_TYPE)
            .subscriptionDate(UPDATED_SUBSCRIPTION_DATE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .deactivationDate(UPDATED_DEACTIVATION_DATE)
            .comments(UPDATED_COMMENTS)
            .additionalInformation(UPDATED_ADDITIONAL_INFORMATION)
            .cardNumber(UPDATED_CARD_NUMBER);

        restChildItinerarySubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChildItinerarySubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChildItinerarySubscription))
            )
            .andExpect(status().isOk());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
        ChildItinerarySubscription testChildItinerarySubscription = childItinerarySubscriptionList.get(
            childItinerarySubscriptionList.size() - 1
        );
        assertThat(testChildItinerarySubscription.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
        assertThat(testChildItinerarySubscription.getSubscriptionDate()).isEqualTo(UPDATED_SUBSCRIPTION_DATE);
        assertThat(testChildItinerarySubscription.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getDeactivationDate()).isEqualTo(UPDATED_DEACTIVATION_DATE);
        assertThat(testChildItinerarySubscription.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testChildItinerarySubscription.getAdditionalInformation()).isEqualTo(UPDATED_ADDITIONAL_INFORMATION);
        assertThat(testChildItinerarySubscription.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingChildItinerarySubscription() throws Exception {
        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();
        childItinerarySubscription.setId(count.incrementAndGet());

        // Create the ChildItinerarySubscription
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildItinerarySubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, childItinerarySubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChildItinerarySubscription() throws Exception {
        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();
        childItinerarySubscription.setId(count.incrementAndGet());

        // Create the ChildItinerarySubscription
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildItinerarySubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChildItinerarySubscription() throws Exception {
        int databaseSizeBeforeUpdate = childItinerarySubscriptionRepository.findAll().size();
        childItinerarySubscription.setId(count.incrementAndGet());

        // Create the ChildItinerarySubscription
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = childItinerarySubscriptionMapper.toDto(childItinerarySubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildItinerarySubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childItinerarySubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChildItinerarySubscription in the database
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChildItinerarySubscription() throws Exception {
        // Initialize the database
        childItinerarySubscriptionRepository.saveAndFlush(childItinerarySubscription);

        int databaseSizeBeforeDelete = childItinerarySubscriptionRepository.findAll().size();

        // Delete the childItinerarySubscription
        restChildItinerarySubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, childItinerarySubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChildItinerarySubscription> childItinerarySubscriptionList = childItinerarySubscriptionRepository.findAll();
        assertThat(childItinerarySubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

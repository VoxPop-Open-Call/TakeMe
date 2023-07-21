package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.repository.ChildSubscriptionRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.service.ChildSubscriptionService;
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

import pt.famility.backoffice.domain.enumeration.StatusType;
/**
 * Test class for the ChildSubscriptionResource REST controller.
 *
 * @see ChildSubscriptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class ChildSubscriptionResourceIntTest {

    private static final StatusType DEFAULT_STATUS_TYPE = StatusType.NEW;
    private static final StatusType UPDATED_STATUS_TYPE = StatusType.ACTIVE;

    private static final Boolean DEFAULT_FAMILITY = false;
    private static final Boolean UPDATED_FAMILITY = true;

    private static final Instant DEFAULT_SUBSCRIPTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBSCRIPTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACTIVATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private ChildSubscriptionRepository childSubscriptionRepository;

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

    private MockMvc restChildSubscriptionMockMvc;

    private ChildSubscription childSubscription;

    private ChildSubscriptionService childSubscriptionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChildSubscriptionResource childSubscriptionResource = new ChildSubscriptionResource(childSubscriptionRepository, childSubscriptionService, accessValidator);
        this.restChildSubscriptionMockMvc = MockMvcBuilders.standaloneSetup(childSubscriptionResource)
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
    public static ChildSubscription createEntity(EntityManager em) {
        ChildSubscription childSubscription = new ChildSubscription()
            .statusType(DEFAULT_STATUS_TYPE)
            .famility(DEFAULT_FAMILITY)
            .subscriptionDate(DEFAULT_SUBSCRIPTION_DATE)
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .comments(DEFAULT_COMMENTS);
        return childSubscription;
    }

    @Before
    public void initTest() {
        childSubscription = createEntity(em);
    }

    @Test
    @Transactional
    public void createChildSubscription() throws Exception {
        int databaseSizeBeforeCreate = childSubscriptionRepository.findAll().size();

        // Create the ChildSubscription
        restChildSubscriptionMockMvc.perform(post("/api/child-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childSubscription)))
            .andExpect(status().isCreated());

        // Validate the ChildSubscription in the database
        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        ChildSubscription testChildSubscription = childSubscriptionList.get(childSubscriptionList.size() - 1);
        assertThat(testChildSubscription.getStatusType()).isEqualTo(DEFAULT_STATUS_TYPE);
        assertThat(testChildSubscription.isFamility()).isEqualTo(DEFAULT_FAMILITY);
        assertThat(testChildSubscription.getSubscriptionDate()).isEqualTo(DEFAULT_SUBSCRIPTION_DATE);
        assertThat(testChildSubscription.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testChildSubscription.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createChildSubscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = childSubscriptionRepository.findAll().size();

        // Create the ChildSubscription with an existing ID
        childSubscription.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildSubscriptionMockMvc.perform(post("/api/child-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childSubscription)))
            .andExpect(status().isBadRequest());

        // Validate the ChildSubscription in the database
        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = childSubscriptionRepository.findAll().size();
        // set the field null
        childSubscription.setStatusType(null);

        // Create the ChildSubscription, which fails.

        restChildSubscriptionMockMvc.perform(post("/api/child-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childSubscription)))
            .andExpect(status().isBadRequest());

        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubscriptionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = childSubscriptionRepository.findAll().size();
        // set the field null
        childSubscription.setSubscriptionDate(null);

        // Create the ChildSubscription, which fails.

        restChildSubscriptionMockMvc.perform(post("/api/child-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childSubscription)))
            .andExpect(status().isBadRequest());

        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActivationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = childSubscriptionRepository.findAll().size();
        // set the field null
        childSubscription.setActivationDate(null);

        // Create the ChildSubscription, which fails.

        restChildSubscriptionMockMvc.perform(post("/api/child-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childSubscription)))
            .andExpect(status().isBadRequest());

        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChildSubscriptions() throws Exception {
        // Initialize the database
        childSubscriptionRepository.saveAndFlush(childSubscription);

        // Get all the childSubscriptionList
        restChildSubscriptionMockMvc.perform(get("/api/child-subscriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(childSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].famility").value(hasItem(DEFAULT_FAMILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].subscriptionDate").value(hasItem(DEFAULT_SUBSCRIPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }
    
    @Test
    @Transactional
    public void getChildSubscription() throws Exception {
        // Initialize the database
        childSubscriptionRepository.saveAndFlush(childSubscription);

        // Get the childSubscription
        restChildSubscriptionMockMvc.perform(get("/api/child-subscriptions/{id}", childSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(childSubscription.getId().intValue()))
            .andExpect(jsonPath("$.statusType").value(DEFAULT_STATUS_TYPE.toString()))
            .andExpect(jsonPath("$.famility").value(DEFAULT_FAMILITY.booleanValue()))
            .andExpect(jsonPath("$.subscriptionDate").value(DEFAULT_SUBSCRIPTION_DATE.toString()))
            .andExpect(jsonPath("$.activationDate").value(DEFAULT_ACTIVATION_DATE.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChildSubscription() throws Exception {
        // Get the childSubscription
        restChildSubscriptionMockMvc.perform(get("/api/child-subscriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChildSubscription() throws Exception {
        // Initialize the database
        childSubscriptionRepository.saveAndFlush(childSubscription);

        int databaseSizeBeforeUpdate = childSubscriptionRepository.findAll().size();

        // Update the childSubscription
        ChildSubscription updatedChildSubscription = childSubscriptionRepository.findById(childSubscription.getId()).get();
        // Disconnect from session so that the updates on updatedChildSubscription are not directly saved in db
        em.detach(updatedChildSubscription);
        updatedChildSubscription
            .statusType(UPDATED_STATUS_TYPE)
            .famility(UPDATED_FAMILITY)
            .subscriptionDate(UPDATED_SUBSCRIPTION_DATE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .comments(UPDATED_COMMENTS);

        restChildSubscriptionMockMvc.perform(put("/api/child-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChildSubscription)))
            .andExpect(status().isOk());

        // Validate the ChildSubscription in the database
        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        ChildSubscription testChildSubscription = childSubscriptionList.get(childSubscriptionList.size() - 1);
        assertThat(testChildSubscription.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
        assertThat(testChildSubscription.isFamility()).isEqualTo(UPDATED_FAMILITY);
        assertThat(testChildSubscription.getSubscriptionDate()).isEqualTo(UPDATED_SUBSCRIPTION_DATE);
        assertThat(testChildSubscription.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testChildSubscription.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingChildSubscription() throws Exception {
        int databaseSizeBeforeUpdate = childSubscriptionRepository.findAll().size();

        // Create the ChildSubscription

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildSubscriptionMockMvc.perform(put("/api/child-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childSubscription)))
            .andExpect(status().isBadRequest());

        // Validate the ChildSubscription in the database
        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChildSubscription() throws Exception {
        // Initialize the database
        childSubscriptionRepository.saveAndFlush(childSubscription);

        int databaseSizeBeforeDelete = childSubscriptionRepository.findAll().size();

        // Get the childSubscription
        restChildSubscriptionMockMvc.perform(delete("/api/child-subscriptions/{id}", childSubscription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAll();
        assertThat(childSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChildSubscription.class);
        ChildSubscription childSubscription1 = new ChildSubscription();
        childSubscription1.setId(1L);
        ChildSubscription childSubscription2 = new ChildSubscription();
        childSubscription2.setId(childSubscription1.getId());
        assertThat(childSubscription1).isEqualTo(childSubscription2);
        childSubscription2.setId(2L);
        assertThat(childSubscription1).isNotEqualTo(childSubscription2);
        childSubscription1.setId(null);
        assertThat(childSubscription1).isNotEqualTo(childSubscription2);
    }
}

package pt.famility.backoffice.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.FamilityBackofficeApp;
import pt.famility.backoffice.converter.ChildStopPointsServicesConverter;
import pt.famility.backoffice.converter.ItineraryConverter;
import pt.famility.backoffice.converter.LocationConverter;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.OrganizationRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.service.DriverService;
import pt.famility.backoffice.service.ItineraryService;
import pt.famility.backoffice.service.OrganizationService;
import pt.famility.backoffice.service.ServiceService;
import pt.famility.backoffice.service.TutorService;
import pt.famility.backoffice.service.UserService;
import pt.famility.backoffice.service.VehicleService;
import pt.famility.backoffice.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.any;
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
 * Test class for the OrganizationResource REST controller.
 *
 * @see OrganizationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class OrganizationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIF_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_NIF_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NIF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NIF_NUMBER = "BBBBBBBBBB";

    private static final OrganizationType DEFAULT_ORGANIZATION_TYPE = OrganizationType.BUS_COMPANY;

    private static final StatusType DEFAULT_STATUS_TYPE = StatusType.NEW;
    private static final StatusType UPDATED_STATUS_TYPE = StatusType.ACTIVE;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationRepository organizationRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ChildSubscriptionService childSubscriptionService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private ChildService childService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private LocationConverter locationConverter;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryConverter itineraryConverter;

    @Autowired
    private ChildStopPointsServicesConverter childStopPointsServicesConverter;

    @Autowired
    private AccessValidator accessValidator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganizationResource organizationResource = new OrganizationResource(organizationRepository, organizationService, childSubscriptionService, tutorService, childService, vehicleService, driverService, userService, serviceService, locationConverter, accessValidator);
        this.restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource)
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
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .name(DEFAULT_NAME)
            .nifCountry(DEFAULT_NIF_COUNTRY)
            .nifNumber(DEFAULT_NIF_NUMBER)
            .organizationType(DEFAULT_ORGANIZATION_TYPE)
            .statusType(DEFAULT_STATUS_TYPE);
        return organization;
    }

    @Before
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganization.getNifCountry()).isEqualTo(DEFAULT_NIF_COUNTRY);
        assertThat(testOrganization.getNifNumber()).isEqualTo(DEFAULT_NIF_NUMBER);
        assertThat(testOrganization.getOrganizationType()).isEqualTo(DEFAULT_ORGANIZATION_TYPE);
        assertThat(testOrganization.getStatusType()).isEqualTo(DEFAULT_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void createOrganizationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization with an existing ID
        organization.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setName(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNifCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setNifCountry(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNifNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setNifNumber(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrganizationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setOrganizationType(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nifCountry").value(hasItem(DEFAULT_NIF_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].nifNumber").value(hasItem(DEFAULT_NIF_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].organizationType").value(hasItem(DEFAULT_ORGANIZATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllOrganizationsWithEagerRelationshipsIsEnabled() throws Exception {
        OrganizationResource organizationResource = new OrganizationResource(organizationRepositoryMock, organizationService, childSubscriptionService, tutorService, childService, vehicleService, driverService, userService, serviceService, locationConverter, accessValidator);
        when(organizationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrganizationMockMvc.perform(get("/api/organizations?eagerload=true"))
        .andExpect(status().isOk());

        verify(organizationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllOrganizationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrganizationResource organizationResource = new OrganizationResource(organizationRepositoryMock, organizationService, childSubscriptionService, tutorService, childService, vehicleService, driverService, userService, serviceService, locationConverter, accessValidator);
            when(organizationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrganizationMockMvc.perform(get("/api/organizations?eagerload=true"))
        .andExpect(status().isOk());

            verify(organizationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.nifCountry").value(DEFAULT_NIF_COUNTRY.toString()))
            .andExpect(jsonPath("$.nifNumber").value(DEFAULT_NIF_NUMBER.toString()))
            .andExpect(jsonPath("$.organizationType").value(DEFAULT_ORGANIZATION_TYPE.toString()))
            .andExpect(jsonPath("$.statusType").value(DEFAULT_STATUS_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).get();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization
            .name(UPDATED_NAME)
            .nifCountry(UPDATED_NIF_COUNTRY)
            .nifNumber(UPDATED_NIF_NUMBER)
            .statusType(UPDATED_STATUS_TYPE);

        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganization)))
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getNifCountry()).isEqualTo(UPDATED_NIF_COUNTRY);
        assertThat(testOrganization.getNifNumber()).isEqualTo(UPDATED_NIF_NUMBER);
        assertThat(testOrganization.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Create the Organization

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Get the organization
        restOrganizationMockMvc.perform(delete("/api/organizations/{id}", organization.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organization.class);
        Organization organization1 = new Organization();
        organization1.setId(1L);
        Organization organization2 = new Organization();
        organization2.setId(organization1.getId());
        assertThat(organization1).isEqualTo(organization2);
        organization2.setId(2L);
        assertThat(organization1).isNotEqualTo(organization2);
        organization1.setId(null);
        assertThat(organization1).isNotEqualTo(organization2);
    }
}

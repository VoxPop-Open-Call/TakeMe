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
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ChildRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Test class for the ChildResource REST controller.
 *
 * @see ChildResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class ChildResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIF_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_NIF_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NIF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NIF_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_FAMILITY = false;
    private static final Boolean UPDATED_FAMILITY = true;

    private static final StatusType DEFAULT_STATUS_TYPE = StatusType.NEW;
    private static final StatusType UPDATED_STATUS_TYPE = StatusType.ACTIVE;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildService childService;

    @Mock
    private ChildRepository childRepositoryMock;

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

    private MockMvc restChildMockMvc;

    private Child child;

    @Autowired
    private ChildSubscriptionService childSubscriptionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChildResource childResource = new ChildResource(childRepository, childService, childSubscriptionService, accessValidator);
        this.restChildMockMvc = MockMvcBuilders.standaloneSetup(childResource)
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
    public static Child createEntity(EntityManager em) {
        Child child = new Child()
            .name(DEFAULT_NAME)
            .nifCountry(DEFAULT_NIF_COUNTRY)
            .nifNumber(DEFAULT_NIF_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .famility(DEFAULT_FAMILITY)
            .statusType(DEFAULT_STATUS_TYPE);
        return child;
    }

    @Before
    public void initTest() {
        child = createEntity(em);
    }

    @Test
    @Transactional
    public void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // Create the Child
        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(child)))
            .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChild.getNifCountry()).isEqualTo(DEFAULT_NIF_COUNTRY);
        assertThat(testChild.getNifNumber()).isEqualTo(DEFAULT_NIF_NUMBER);
        assertThat(testChild.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testChild.isFamility()).isEqualTo(DEFAULT_FAMILITY);
        assertThat(testChild.getStatusType()).isEqualTo(DEFAULT_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void createChildWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // Create the Child with an existing ID
        child.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(child)))
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setName(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(child)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNifCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setNifCountry(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(child)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNifNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setNifNumber(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(child)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setDateOfBirth(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(child)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChildren() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList
        restChildMockMvc.perform(get("/api/children?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nifCountry").value(hasItem(DEFAULT_NIF_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].nifNumber").value(hasItem(DEFAULT_NIF_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].famility").value(hasItem(DEFAULT_FAMILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllChildrenWithEagerRelationshipsIsEnabled() throws Exception {
        ChildResource childResource = new ChildResource(childRepositoryMock, childService, childSubscriptionService, accessValidator);
        when(childRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restChildMockMvc = MockMvcBuilders.standaloneSetup(childResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChildMockMvc.perform(get("/api/children?eagerload=true"))
        .andExpect(status().isOk());

        verify(childRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllChildrenWithEagerRelationshipsIsNotEnabled() throws Exception {
        ChildResource childResource = new ChildResource(childRepositoryMock, childService, childSubscriptionService, accessValidator);
            when(childRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restChildMockMvc = MockMvcBuilders.standaloneSetup(childResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChildMockMvc.perform(get("/api/children?eagerload=true"))
        .andExpect(status().isOk());

            verify(childRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc.perform(get("/api/children/{id}", child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.nifCountry").value(DEFAULT_NIF_COUNTRY.toString()))
            .andExpect(jsonPath("$.nifNumber").value(DEFAULT_NIF_NUMBER.toString()))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.famility").value(DEFAULT_FAMILITY.booleanValue()))
            .andExpect(jsonPath("$.statusType").value(DEFAULT_STATUS_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get("/api/children/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        Child updatedChild = childRepository.findById(child.getId()).get();
        // Disconnect from session so that the updates on updatedChild are not directly saved in db
        em.detach(updatedChild);
        updatedChild
            .name(UPDATED_NAME)
            .nifCountry(UPDATED_NIF_COUNTRY)
            .nifNumber(UPDATED_NIF_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .famility(UPDATED_FAMILITY)
            .statusType(UPDATED_STATUS_TYPE);

        restChildMockMvc.perform(put("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChild)))
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getNifCountry()).isEqualTo(UPDATED_NIF_COUNTRY);
        assertThat(testChild.getNifNumber()).isEqualTo(UPDATED_NIF_NUMBER);
        assertThat(testChild.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testChild.isFamility()).isEqualTo(UPDATED_FAMILITY);
        assertThat(testChild.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Create the Child

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc.perform(put("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(child)))
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Get the child
        restChildMockMvc.perform(delete("/api/children/{id}", child.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Child.class);
        Child child1 = new Child();
        child1.setId(1L);
        Child child2 = new Child();
        child2.setId(child1.getId());
        assertThat(child1).isEqualTo(child2);
        child2.setId(2L);
        assertThat(child1).isNotEqualTo(child2);
        child1.setId(null);
        assertThat(child1).isNotEqualTo(child2);
    }
}

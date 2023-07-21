package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.ServiceStopPointFrequency;
import pt.famility.backoffice.repository.ServiceStopPointFrequencyRepository;
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
import java.util.List;


import static pt.famility.backoffice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ServiceStopPointFrequencyResource REST controller.
 *
 * @see ServiceStopPointFrequencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class ServiceStopPointFrequencyResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEEK_INTERVAL = 1;
    private static final Integer UPDATED_WEEK_INTERVAL = 2;

    @Autowired
    private ServiceStopPointFrequencyRepository serviceStopPointFrequencyRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceStopPointFrequencyMockMvc;

    private ServiceStopPointFrequency serviceStopPointFrequency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceStopPointFrequencyResource serviceStopPointFrequencyResource = new ServiceStopPointFrequencyResource(serviceStopPointFrequencyRepository);
        this.restServiceStopPointFrequencyMockMvc = MockMvcBuilders.standaloneSetup(serviceStopPointFrequencyResource)
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
    public static ServiceStopPointFrequency createEntity(EntityManager em) {
        ServiceStopPointFrequency serviceStopPointFrequency = new ServiceStopPointFrequency()
            .description(DEFAULT_DESCRIPTION)
            .weekInterval(DEFAULT_WEEK_INTERVAL);
        return serviceStopPointFrequency;
    }

    @Before
    public void initTest() {
        serviceStopPointFrequency = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceStopPointFrequency() throws Exception {
        int databaseSizeBeforeCreate = serviceStopPointFrequencyRepository.findAll().size();

        // Create the ServiceStopPointFrequency
        restServiceStopPointFrequencyMockMvc.perform(post("/api/service-stop-point-frequencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointFrequency)))
            .andExpect(status().isCreated());

        // Validate the ServiceStopPointFrequency in the database
        List<ServiceStopPointFrequency> serviceStopPointFrequencyList = serviceStopPointFrequencyRepository.findAll();
        assertThat(serviceStopPointFrequencyList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceStopPointFrequency testServiceStopPointFrequency = serviceStopPointFrequencyList.get(serviceStopPointFrequencyList.size() - 1);
        assertThat(testServiceStopPointFrequency.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceStopPointFrequency.getWeekInterval()).isEqualTo(DEFAULT_WEEK_INTERVAL);
    }

    @Test
    @Transactional
    public void createServiceStopPointFrequencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceStopPointFrequencyRepository.findAll().size();

        // Create the ServiceStopPointFrequency with an existing ID
        serviceStopPointFrequency.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceStopPointFrequencyMockMvc.perform(post("/api/service-stop-point-frequencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointFrequency)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceStopPointFrequency in the database
        List<ServiceStopPointFrequency> serviceStopPointFrequencyList = serviceStopPointFrequencyRepository.findAll();
        assertThat(serviceStopPointFrequencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceStopPointFrequencyRepository.findAll().size();
        // set the field null
        serviceStopPointFrequency.setDescription(null);

        // Create the ServiceStopPointFrequency, which fails.

        restServiceStopPointFrequencyMockMvc.perform(post("/api/service-stop-point-frequencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointFrequency)))
            .andExpect(status().isBadRequest());

        List<ServiceStopPointFrequency> serviceStopPointFrequencyList = serviceStopPointFrequencyRepository.findAll();
        assertThat(serviceStopPointFrequencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekIntervalIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceStopPointFrequencyRepository.findAll().size();
        // set the field null
        serviceStopPointFrequency.setWeekInterval(null);

        // Create the ServiceStopPointFrequency, which fails.

        restServiceStopPointFrequencyMockMvc.perform(post("/api/service-stop-point-frequencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointFrequency)))
            .andExpect(status().isBadRequest());

        List<ServiceStopPointFrequency> serviceStopPointFrequencyList = serviceStopPointFrequencyRepository.findAll();
        assertThat(serviceStopPointFrequencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceStopPointFrequencies() throws Exception {
        // Initialize the database
        serviceStopPointFrequencyRepository.saveAndFlush(serviceStopPointFrequency);

        // Get all the serviceStopPointFrequencyList
        restServiceStopPointFrequencyMockMvc.perform(get("/api/service-stop-point-frequencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceStopPointFrequency.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].weekInterval").value(hasItem(DEFAULT_WEEK_INTERVAL)));
    }
    
    @Test
    @Transactional
    public void getServiceStopPointFrequency() throws Exception {
        // Initialize the database
        serviceStopPointFrequencyRepository.saveAndFlush(serviceStopPointFrequency);

        // Get the serviceStopPointFrequency
        restServiceStopPointFrequencyMockMvc.perform(get("/api/service-stop-point-frequencies/{id}", serviceStopPointFrequency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceStopPointFrequency.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.weekInterval").value(DEFAULT_WEEK_INTERVAL));
    }

    @Test
    @Transactional
    public void getNonExistingServiceStopPointFrequency() throws Exception {
        // Get the serviceStopPointFrequency
        restServiceStopPointFrequencyMockMvc.perform(get("/api/service-stop-point-frequencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceStopPointFrequency() throws Exception {
        // Initialize the database
        serviceStopPointFrequencyRepository.saveAndFlush(serviceStopPointFrequency);

        int databaseSizeBeforeUpdate = serviceStopPointFrequencyRepository.findAll().size();

        // Update the serviceStopPointFrequency
        ServiceStopPointFrequency updatedServiceStopPointFrequency = serviceStopPointFrequencyRepository.findById(serviceStopPointFrequency.getId()).get();
        // Disconnect from session so that the updates on updatedServiceStopPointFrequency are not directly saved in db
        em.detach(updatedServiceStopPointFrequency);
        updatedServiceStopPointFrequency
            .description(UPDATED_DESCRIPTION)
            .weekInterval(UPDATED_WEEK_INTERVAL);

        restServiceStopPointFrequencyMockMvc.perform(put("/api/service-stop-point-frequencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceStopPointFrequency)))
            .andExpect(status().isOk());

        // Validate the ServiceStopPointFrequency in the database
        List<ServiceStopPointFrequency> serviceStopPointFrequencyList = serviceStopPointFrequencyRepository.findAll();
        assertThat(serviceStopPointFrequencyList).hasSize(databaseSizeBeforeUpdate);
        ServiceStopPointFrequency testServiceStopPointFrequency = serviceStopPointFrequencyList.get(serviceStopPointFrequencyList.size() - 1);
        assertThat(testServiceStopPointFrequency.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceStopPointFrequency.getWeekInterval()).isEqualTo(UPDATED_WEEK_INTERVAL);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceStopPointFrequency() throws Exception {
        int databaseSizeBeforeUpdate = serviceStopPointFrequencyRepository.findAll().size();

        // Create the ServiceStopPointFrequency

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceStopPointFrequencyMockMvc.perform(put("/api/service-stop-point-frequencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointFrequency)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceStopPointFrequency in the database
        List<ServiceStopPointFrequency> serviceStopPointFrequencyList = serviceStopPointFrequencyRepository.findAll();
        assertThat(serviceStopPointFrequencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceStopPointFrequency() throws Exception {
        // Initialize the database
        serviceStopPointFrequencyRepository.saveAndFlush(serviceStopPointFrequency);

        int databaseSizeBeforeDelete = serviceStopPointFrequencyRepository.findAll().size();

        // Get the serviceStopPointFrequency
        restServiceStopPointFrequencyMockMvc.perform(delete("/api/service-stop-point-frequencies/{id}", serviceStopPointFrequency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceStopPointFrequency> serviceStopPointFrequencyList = serviceStopPointFrequencyRepository.findAll();
        assertThat(serviceStopPointFrequencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceStopPointFrequency.class);
        ServiceStopPointFrequency serviceStopPointFrequency1 = new ServiceStopPointFrequency();
        serviceStopPointFrequency1.setId(1L);
        ServiceStopPointFrequency serviceStopPointFrequency2 = new ServiceStopPointFrequency();
        serviceStopPointFrequency2.setId(serviceStopPointFrequency1.getId());
        assertThat(serviceStopPointFrequency1).isEqualTo(serviceStopPointFrequency2);
        serviceStopPointFrequency2.setId(2L);
        assertThat(serviceStopPointFrequency1).isNotEqualTo(serviceStopPointFrequency2);
        serviceStopPointFrequency1.setId(null);
        assertThat(serviceStopPointFrequency1).isNotEqualTo(serviceStopPointFrequency2);
    }
}

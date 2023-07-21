package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek;
import pt.famility.backoffice.repository.ServiceStopPointDaysOfWeekRepository;
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

import pt.famility.backoffice.domain.enumeration.DaysOfWeekType;
/**
 * Test class for the ServiceStopPointDaysOfWeekResource REST controller.
 *
 * @see ServiceStopPointDaysOfWeekResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class ServiceStopPointDaysOfWeekResourceIntTest {

    private static final DaysOfWeekType DEFAULT_DAY = DaysOfWeekType.SUNDAY;
    private static final DaysOfWeekType UPDATED_DAY = DaysOfWeekType.MONDAY;

    @Autowired
    private ServiceStopPointDaysOfWeekRepository serviceStopPointDaysOfWeekRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceStopPointDaysOfWeekMockMvc;

    private ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceStopPointDaysOfWeekResource serviceStopPointDaysOfWeekResource = new ServiceStopPointDaysOfWeekResource(serviceStopPointDaysOfWeekRepository);
        this.restServiceStopPointDaysOfWeekMockMvc = MockMvcBuilders.standaloneSetup(serviceStopPointDaysOfWeekResource)
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
    public static ServiceStopPointDaysOfWeek createEntity(EntityManager em) {
        ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek = new ServiceStopPointDaysOfWeek()
            .day(DEFAULT_DAY);
        return serviceStopPointDaysOfWeek;
    }

    @Before
    public void initTest() {
        serviceStopPointDaysOfWeek = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceStopPointDaysOfWeek() throws Exception {
        int databaseSizeBeforeCreate = serviceStopPointDaysOfWeekRepository.findAll().size();

        // Create the ServiceStopPointDaysOfWeek
        restServiceStopPointDaysOfWeekMockMvc.perform(post("/api/service-stop-point-days-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointDaysOfWeek)))
            .andExpect(status().isCreated());

        // Validate the ServiceStopPointDaysOfWeek in the database
        List<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeekList = serviceStopPointDaysOfWeekRepository.findAll();
        assertThat(serviceStopPointDaysOfWeekList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceStopPointDaysOfWeek testServiceStopPointDaysOfWeek = serviceStopPointDaysOfWeekList.get(serviceStopPointDaysOfWeekList.size() - 1);
        assertThat(testServiceStopPointDaysOfWeek.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    @Transactional
    public void createServiceStopPointDaysOfWeekWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceStopPointDaysOfWeekRepository.findAll().size();

        // Create the ServiceStopPointDaysOfWeek with an existing ID
        serviceStopPointDaysOfWeek.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceStopPointDaysOfWeekMockMvc.perform(post("/api/service-stop-point-days-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointDaysOfWeek)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceStopPointDaysOfWeek in the database
        List<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeekList = serviceStopPointDaysOfWeekRepository.findAll();
        assertThat(serviceStopPointDaysOfWeekList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiceStopPointDaysOfWeeks() throws Exception {
        // Initialize the database
        serviceStopPointDaysOfWeekRepository.saveAndFlush(serviceStopPointDaysOfWeek);

        // Get all the serviceStopPointDaysOfWeekList
        restServiceStopPointDaysOfWeekMockMvc.perform(get("/api/service-stop-point-days-of-weeks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceStopPointDaysOfWeek.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())));
    }
    
    @Test
    @Transactional
    public void getServiceStopPointDaysOfWeek() throws Exception {
        // Initialize the database
        serviceStopPointDaysOfWeekRepository.saveAndFlush(serviceStopPointDaysOfWeek);

        // Get the serviceStopPointDaysOfWeek
        restServiceStopPointDaysOfWeekMockMvc.perform(get("/api/service-stop-point-days-of-weeks/{id}", serviceStopPointDaysOfWeek.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceStopPointDaysOfWeek.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceStopPointDaysOfWeek() throws Exception {
        // Get the serviceStopPointDaysOfWeek
        restServiceStopPointDaysOfWeekMockMvc.perform(get("/api/service-stop-point-days-of-weeks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceStopPointDaysOfWeek() throws Exception {
        // Initialize the database
        serviceStopPointDaysOfWeekRepository.saveAndFlush(serviceStopPointDaysOfWeek);

        int databaseSizeBeforeUpdate = serviceStopPointDaysOfWeekRepository.findAll().size();

        // Update the serviceStopPointDaysOfWeek
        ServiceStopPointDaysOfWeek updatedServiceStopPointDaysOfWeek = serviceStopPointDaysOfWeekRepository.findById(serviceStopPointDaysOfWeek.getId()).get();
        // Disconnect from session so that the updates on updatedServiceStopPointDaysOfWeek are not directly saved in db
        em.detach(updatedServiceStopPointDaysOfWeek);
        updatedServiceStopPointDaysOfWeek
            .day(UPDATED_DAY);

        restServiceStopPointDaysOfWeekMockMvc.perform(put("/api/service-stop-point-days-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceStopPointDaysOfWeek)))
            .andExpect(status().isOk());

        // Validate the ServiceStopPointDaysOfWeek in the database
        List<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeekList = serviceStopPointDaysOfWeekRepository.findAll();
        assertThat(serviceStopPointDaysOfWeekList).hasSize(databaseSizeBeforeUpdate);
        ServiceStopPointDaysOfWeek testServiceStopPointDaysOfWeek = serviceStopPointDaysOfWeekList.get(serviceStopPointDaysOfWeekList.size() - 1);
        assertThat(testServiceStopPointDaysOfWeek.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceStopPointDaysOfWeek() throws Exception {
        int databaseSizeBeforeUpdate = serviceStopPointDaysOfWeekRepository.findAll().size();

        // Create the ServiceStopPointDaysOfWeek

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceStopPointDaysOfWeekMockMvc.perform(put("/api/service-stop-point-days-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceStopPointDaysOfWeek)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceStopPointDaysOfWeek in the database
        List<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeekList = serviceStopPointDaysOfWeekRepository.findAll();
        assertThat(serviceStopPointDaysOfWeekList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceStopPointDaysOfWeek() throws Exception {
        // Initialize the database
        serviceStopPointDaysOfWeekRepository.saveAndFlush(serviceStopPointDaysOfWeek);

        int databaseSizeBeforeDelete = serviceStopPointDaysOfWeekRepository.findAll().size();

        // Get the serviceStopPointDaysOfWeek
        restServiceStopPointDaysOfWeekMockMvc.perform(delete("/api/service-stop-point-days-of-weeks/{id}", serviceStopPointDaysOfWeek.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeekList = serviceStopPointDaysOfWeekRepository.findAll();
        assertThat(serviceStopPointDaysOfWeekList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceStopPointDaysOfWeek.class);
        ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek1 = new ServiceStopPointDaysOfWeek();
        serviceStopPointDaysOfWeek1.setId(1L);
        ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek2 = new ServiceStopPointDaysOfWeek();
        serviceStopPointDaysOfWeek2.setId(serviceStopPointDaysOfWeek1.getId());
        assertThat(serviceStopPointDaysOfWeek1).isEqualTo(serviceStopPointDaysOfWeek2);
        serviceStopPointDaysOfWeek2.setId(2L);
        assertThat(serviceStopPointDaysOfWeek1).isNotEqualTo(serviceStopPointDaysOfWeek2);
        serviceStopPointDaysOfWeek1.setId(null);
        assertThat(serviceStopPointDaysOfWeek1).isNotEqualTo(serviceStopPointDaysOfWeek2);
    }
}

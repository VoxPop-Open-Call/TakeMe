package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.IdentificationCardType;
import pt.famility.backoffice.repository.IdentificationCardTypeRepository;
import pt.famility.backoffice.security.AccessValidator;
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
 * Test class for the IdentificationCardTypeResource REST controller.
 *
 * @see IdentificationCardTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class IdentificationCardTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private IdentificationCardTypeRepository identificationCardTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIdentificationCardTypeMockMvc;

    private IdentificationCardType identificationCardType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IdentificationCardTypeResource identificationCardTypeResource = new IdentificationCardTypeResource(identificationCardTypeRepository);
        this.restIdentificationCardTypeMockMvc = MockMvcBuilders.standaloneSetup(identificationCardTypeResource)
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
    public static IdentificationCardType createEntity(EntityManager em) {
        IdentificationCardType identificationCardType = new IdentificationCardType()
            .name(DEFAULT_NAME);
        return identificationCardType;
    }

    @Before
    public void initTest() {
        identificationCardType = createEntity(em);
    }

    @Test
    @Transactional
    public void createIdentificationCardType() throws Exception {
        int databaseSizeBeforeCreate = identificationCardTypeRepository.findAll().size();

        // Create the IdentificationCardType
        restIdentificationCardTypeMockMvc.perform(post("/api/identification-card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identificationCardType)))
            .andExpect(status().isCreated());

        // Validate the IdentificationCardType in the database
        List<IdentificationCardType> identificationCardTypeList = identificationCardTypeRepository.findAll();
        assertThat(identificationCardTypeList).hasSize(databaseSizeBeforeCreate + 1);
        IdentificationCardType testIdentificationCardType = identificationCardTypeList.get(identificationCardTypeList.size() - 1);
        assertThat(testIdentificationCardType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createIdentificationCardTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = identificationCardTypeRepository.findAll().size();

        // Create the IdentificationCardType with an existing ID
        identificationCardType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdentificationCardTypeMockMvc.perform(post("/api/identification-card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identificationCardType)))
            .andExpect(status().isBadRequest());

        // Validate the IdentificationCardType in the database
        List<IdentificationCardType> identificationCardTypeList = identificationCardTypeRepository.findAll();
        assertThat(identificationCardTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIdentificationCardTypes() throws Exception {
        // Initialize the database
        identificationCardTypeRepository.saveAndFlush(identificationCardType);

        // Get all the identificationCardTypeList
        restIdentificationCardTypeMockMvc.perform(get("/api/identification-card-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(identificationCardType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getIdentificationCardType() throws Exception {
        // Initialize the database
        identificationCardTypeRepository.saveAndFlush(identificationCardType);

        // Get the identificationCardType
        restIdentificationCardTypeMockMvc.perform(get("/api/identification-card-types/{id}", identificationCardType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(identificationCardType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIdentificationCardType() throws Exception {
        // Get the identificationCardType
        restIdentificationCardTypeMockMvc.perform(get("/api/identification-card-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdentificationCardType() throws Exception {
        // Initialize the database
        identificationCardTypeRepository.saveAndFlush(identificationCardType);

        int databaseSizeBeforeUpdate = identificationCardTypeRepository.findAll().size();

        // Update the identificationCardType
        IdentificationCardType updatedIdentificationCardType = identificationCardTypeRepository.findById(identificationCardType.getId()).get();
        // Disconnect from session so that the updates on updatedIdentificationCardType are not directly saved in db
        em.detach(updatedIdentificationCardType);
        updatedIdentificationCardType
            .name(UPDATED_NAME);

        restIdentificationCardTypeMockMvc.perform(put("/api/identification-card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIdentificationCardType)))
            .andExpect(status().isOk());

        // Validate the IdentificationCardType in the database
        List<IdentificationCardType> identificationCardTypeList = identificationCardTypeRepository.findAll();
        assertThat(identificationCardTypeList).hasSize(databaseSizeBeforeUpdate);
        IdentificationCardType testIdentificationCardType = identificationCardTypeList.get(identificationCardTypeList.size() - 1);
        assertThat(testIdentificationCardType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingIdentificationCardType() throws Exception {
        int databaseSizeBeforeUpdate = identificationCardTypeRepository.findAll().size();

        // Create the IdentificationCardType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentificationCardTypeMockMvc.perform(put("/api/identification-card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identificationCardType)))
            .andExpect(status().isBadRequest());

        // Validate the IdentificationCardType in the database
        List<IdentificationCardType> identificationCardTypeList = identificationCardTypeRepository.findAll();
        assertThat(identificationCardTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIdentificationCardType() throws Exception {
        // Initialize the database
        identificationCardTypeRepository.saveAndFlush(identificationCardType);

        int databaseSizeBeforeDelete = identificationCardTypeRepository.findAll().size();

        // Get the identificationCardType
        restIdentificationCardTypeMockMvc.perform(delete("/api/identification-card-types/{id}", identificationCardType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IdentificationCardType> identificationCardTypeList = identificationCardTypeRepository.findAll();
        assertThat(identificationCardTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentificationCardType.class);
        IdentificationCardType identificationCardType1 = new IdentificationCardType();
        identificationCardType1.setId(1L);
        IdentificationCardType identificationCardType2 = new IdentificationCardType();
        identificationCardType2.setId(identificationCardType1.getId());
        assertThat(identificationCardType1).isEqualTo(identificationCardType2);
        identificationCardType2.setId(2L);
        assertThat(identificationCardType1).isNotEqualTo(identificationCardType2);
        identificationCardType1.setId(null);
        assertThat(identificationCardType1).isNotEqualTo(identificationCardType2);
    }
}

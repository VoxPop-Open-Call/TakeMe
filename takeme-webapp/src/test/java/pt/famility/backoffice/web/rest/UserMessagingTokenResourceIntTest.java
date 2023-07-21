package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.FamilityBackofficeApp;

import pt.famility.backoffice.domain.UserMessagingToken;
import pt.famility.backoffice.repository.UserMessagingTokenRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.service.UserMessagingTokenService;
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

import pt.famility.backoffice.domain.enumeration.OriginType;
/**
 * Test class for the UserMessagingTokenResource REST controller.
 *
 * @see UserMessagingTokenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
public class UserMessagingTokenResourceIntTest {

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final OriginType DEFAULT_ORIGIN = OriginType.BROWSER;
    private static final OriginType UPDATED_ORIGIN = OriginType.NATIVE;

    @Autowired
    private UserMessagingTokenRepository userMessagingTokenRepository;

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

    private MockMvc restUserMessagingTokenMockMvc;

    private UserMessagingToken userMessagingToken;

    private UserMessagingTokenService userMessagingTokenService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserMessagingTokenResource userMessagingTokenResource =
                new UserMessagingTokenResource(userMessagingTokenRepository,userMessagingTokenService, accessValidator);
        this.restUserMessagingTokenMockMvc = MockMvcBuilders.standaloneSetup(userMessagingTokenResource)
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
    public static UserMessagingToken createEntity(EntityManager em) {
        UserMessagingToken userMessagingToken = new UserMessagingToken()
            .token(DEFAULT_TOKEN)
            .origin(DEFAULT_ORIGIN);
        return userMessagingToken;
    }

    @Before
    public void initTest() {
        userMessagingToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserMessagingToken() throws Exception {
        int databaseSizeBeforeCreate = userMessagingTokenRepository.findAll().size();

        // Create the UserMessagingToken
        restUserMessagingTokenMockMvc.perform(post("/api/user-messaging-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMessagingToken)))
            .andExpect(status().isCreated());

        // Validate the UserMessagingToken in the database
        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenRepository.findAll();
        assertThat(userMessagingTokenList).hasSize(databaseSizeBeforeCreate + 1);
        UserMessagingToken testUserMessagingToken = userMessagingTokenList.get(userMessagingTokenList.size() - 1);
        assertThat(testUserMessagingToken.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testUserMessagingToken.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
    }

    @Test
    @Transactional
    public void createUserMessagingTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userMessagingTokenRepository.findAll().size();

        // Create the UserMessagingToken with an existing ID
        userMessagingToken.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMessagingTokenMockMvc.perform(post("/api/user-messaging-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMessagingToken)))
            .andExpect(status().isBadRequest());

        // Validate the UserMessagingToken in the database
        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenRepository.findAll();
        assertThat(userMessagingTokenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMessagingTokenRepository.findAll().size();
        // set the field null
        userMessagingToken.setToken(null);

        // Create the UserMessagingToken, which fails.

        restUserMessagingTokenMockMvc.perform(post("/api/user-messaging-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMessagingToken)))
            .andExpect(status().isBadRequest());

        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenRepository.findAll();
        assertThat(userMessagingTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMessagingTokenRepository.findAll().size();
        // set the field null
        userMessagingToken.setUser(null);

        // Create the UserMessagingToken, which fails.

        restUserMessagingTokenMockMvc.perform(post("/api/user-messaging-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMessagingToken)))
            .andExpect(status().isBadRequest());

        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenRepository.findAll();
        assertThat(userMessagingTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserMessagingTokens() throws Exception {
        // Initialize the database
        userMessagingTokenRepository.saveAndFlush(userMessagingToken);

        // Get all the userMessagingTokenList
        restUserMessagingTokenMockMvc.perform(get("/api/user-messaging-tokens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMessagingToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())));
    }
    
    @Test
    @Transactional
    public void getUserMessagingToken() throws Exception {
        // Initialize the database
        userMessagingTokenRepository.saveAndFlush(userMessagingToken);

        // Get the userMessagingToken
        restUserMessagingTokenMockMvc.perform(get("/api/user-messaging-tokens/{id}", userMessagingToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userMessagingToken.getId().intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN.toString()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserMessagingToken() throws Exception {
        // Get the userMessagingToken
        restUserMessagingTokenMockMvc.perform(get("/api/user-messaging-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserMessagingToken() throws Exception {
        // Initialize the database
        userMessagingTokenRepository.saveAndFlush(userMessagingToken);

        int databaseSizeBeforeUpdate = userMessagingTokenRepository.findAll().size();

        // Update the userMessagingToken
        UserMessagingToken updatedUserMessagingToken = userMessagingTokenRepository.findById(userMessagingToken.getId()).get();
        // Disconnect from session so that the updates on updatedUserMessagingToken are not directly saved in db
        em.detach(updatedUserMessagingToken);
        updatedUserMessagingToken
            .token(UPDATED_TOKEN)
            .origin(UPDATED_ORIGIN);

        restUserMessagingTokenMockMvc.perform(put("/api/user-messaging-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserMessagingToken)))
            .andExpect(status().isOk());

        // Validate the UserMessagingToken in the database
        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenRepository.findAll();
        assertThat(userMessagingTokenList).hasSize(databaseSizeBeforeUpdate);
        UserMessagingToken testUserMessagingToken = userMessagingTokenList.get(userMessagingTokenList.size() - 1);
        assertThat(testUserMessagingToken.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testUserMessagingToken.getOrigin()).isEqualTo(UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    public void updateNonExistingUserMessagingToken() throws Exception {
        int databaseSizeBeforeUpdate = userMessagingTokenRepository.findAll().size();

        // Create the UserMessagingToken

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMessagingTokenMockMvc.perform(put("/api/user-messaging-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMessagingToken)))
            .andExpect(status().isBadRequest());

        // Validate the UserMessagingToken in the database
        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenRepository.findAll();
        assertThat(userMessagingTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserMessagingToken() throws Exception {
        // Initialize the database
        userMessagingTokenRepository.saveAndFlush(userMessagingToken);

        int databaseSizeBeforeDelete = userMessagingTokenRepository.findAll().size();

        // Get the userMessagingToken
        restUserMessagingTokenMockMvc.perform(delete("/api/user-messaging-tokens/{id}", userMessagingToken.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenRepository.findAll();
        assertThat(userMessagingTokenList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMessagingToken.class);
        UserMessagingToken userMessagingToken1 = new UserMessagingToken();
        userMessagingToken1.setId(1L);
        UserMessagingToken userMessagingToken2 = new UserMessagingToken();
        userMessagingToken2.setId(userMessagingToken1.getId());
        assertThat(userMessagingToken1).isEqualTo(userMessagingToken2);
        userMessagingToken2.setId(2L);
        assertThat(userMessagingToken1).isNotEqualTo(userMessagingToken2);
        userMessagingToken1.setId(null);
        assertThat(userMessagingToken1).isNotEqualTo(userMessagingToken2);
    }
}

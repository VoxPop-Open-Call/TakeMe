package pt.famility.backoffice.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.IntegrationTest;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.enumeration.LocationType;
import pt.famility.backoffice.repository.LocationRepository;
import pt.famility.backoffice.service.dto.LocationDTO;
import pt.famility.backoffice.service.mapper.LocationMapper;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_PORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PORT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FLOOR = "AAAAAAAAAA";
    private static final String UPDATED_FLOOR = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final LocationType DEFAULT_TYPE = LocationType.PRIVATE;
    private static final LocationType UPDATED_TYPE = LocationType.SCHOOL;

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_PLUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PLUS_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .designation(DEFAULT_DESIGNATION)
            .street(DEFAULT_STREET)
            .portNumber(DEFAULT_PORT_NUMBER)
            .floor(DEFAULT_FLOOR)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .type(DEFAULT_TYPE)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .plusCode(DEFAULT_PLUS_CODE);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .designation(UPDATED_DESIGNATION)
            .street(UPDATED_STREET)
            .portNumber(UPDATED_PORT_NUMBER)
            .floor(UPDATED_FLOOR)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .plusCode(UPDATED_PLUS_CODE);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();
        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testLocation.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testLocation.getPortNumber()).isEqualTo(DEFAULT_PORT_NUMBER);
        assertThat(testLocation.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testLocation.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testLocation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLocation.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testLocation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLocation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testLocation.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testLocation.getPlusCode()).isEqualTo(DEFAULT_PLUS_CODE);
    }

    @Test
    @Transactional
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        location.setId(1L);
        LocationDTO locationDTO = locationMapper.toDto(location);

        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setDesignation(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setStreet(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPortNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setPortNumber(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setPostalCode(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setCity(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setCountry(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setType(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLongitude(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLatitude(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setPlusCode(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].portNumber").value(hasItem(DEFAULT_PORT_NUMBER)))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].plusCode").value(hasItem(DEFAULT_PLUS_CODE)));
    }

    @Test
    @Transactional
    void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.portNumber").value(DEFAULT_PORT_NUMBER))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.plusCode").value(DEFAULT_PLUS_CODE));
    }

    @Test
    @Transactional
    void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        Long id = location.getId();

        defaultLocationShouldBeFound("id.equals=" + id);
        defaultLocationShouldNotBeFound("id.notEquals=" + id);

        defaultLocationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationsByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where designation equals to DEFAULT_DESIGNATION
        defaultLocationShouldBeFound("designation.equals=" + DEFAULT_DESIGNATION);

        // Get all the locationList where designation equals to UPDATED_DESIGNATION
        defaultLocationShouldNotBeFound("designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllLocationsByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where designation in DEFAULT_DESIGNATION or UPDATED_DESIGNATION
        defaultLocationShouldBeFound("designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION);

        // Get all the locationList where designation equals to UPDATED_DESIGNATION
        defaultLocationShouldNotBeFound("designation.in=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllLocationsByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where designation is not null
        defaultLocationShouldBeFound("designation.specified=true");

        // Get all the locationList where designation is null
        defaultLocationShouldNotBeFound("designation.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByDesignationContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where designation contains DEFAULT_DESIGNATION
        defaultLocationShouldBeFound("designation.contains=" + DEFAULT_DESIGNATION);

        // Get all the locationList where designation contains UPDATED_DESIGNATION
        defaultLocationShouldNotBeFound("designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllLocationsByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where designation does not contain DEFAULT_DESIGNATION
        defaultLocationShouldNotBeFound("designation.doesNotContain=" + DEFAULT_DESIGNATION);

        // Get all the locationList where designation does not contain UPDATED_DESIGNATION
        defaultLocationShouldBeFound("designation.doesNotContain=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where street equals to DEFAULT_STREET
        defaultLocationShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the locationList where street equals to UPDATED_STREET
        defaultLocationShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where street in DEFAULT_STREET or UPDATED_STREET
        defaultLocationShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the locationList where street equals to UPDATED_STREET
        defaultLocationShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where street is not null
        defaultLocationShouldBeFound("street.specified=true");

        // Get all the locationList where street is null
        defaultLocationShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByStreetContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where street contains DEFAULT_STREET
        defaultLocationShouldBeFound("street.contains=" + DEFAULT_STREET);

        // Get all the locationList where street contains UPDATED_STREET
        defaultLocationShouldNotBeFound("street.contains=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where street does not contain DEFAULT_STREET
        defaultLocationShouldNotBeFound("street.doesNotContain=" + DEFAULT_STREET);

        // Get all the locationList where street does not contain UPDATED_STREET
        defaultLocationShouldBeFound("street.doesNotContain=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllLocationsByPortNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where portNumber equals to DEFAULT_PORT_NUMBER
        defaultLocationShouldBeFound("portNumber.equals=" + DEFAULT_PORT_NUMBER);

        // Get all the locationList where portNumber equals to UPDATED_PORT_NUMBER
        defaultLocationShouldNotBeFound("portNumber.equals=" + UPDATED_PORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationsByPortNumberIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where portNumber in DEFAULT_PORT_NUMBER or UPDATED_PORT_NUMBER
        defaultLocationShouldBeFound("portNumber.in=" + DEFAULT_PORT_NUMBER + "," + UPDATED_PORT_NUMBER);

        // Get all the locationList where portNumber equals to UPDATED_PORT_NUMBER
        defaultLocationShouldNotBeFound("portNumber.in=" + UPDATED_PORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationsByPortNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where portNumber is not null
        defaultLocationShouldBeFound("portNumber.specified=true");

        // Get all the locationList where portNumber is null
        defaultLocationShouldNotBeFound("portNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByPortNumberContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where portNumber contains DEFAULT_PORT_NUMBER
        defaultLocationShouldBeFound("portNumber.contains=" + DEFAULT_PORT_NUMBER);

        // Get all the locationList where portNumber contains UPDATED_PORT_NUMBER
        defaultLocationShouldNotBeFound("portNumber.contains=" + UPDATED_PORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationsByPortNumberNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where portNumber does not contain DEFAULT_PORT_NUMBER
        defaultLocationShouldNotBeFound("portNumber.doesNotContain=" + DEFAULT_PORT_NUMBER);

        // Get all the locationList where portNumber does not contain UPDATED_PORT_NUMBER
        defaultLocationShouldBeFound("portNumber.doesNotContain=" + UPDATED_PORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationsByFloorIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where floor equals to DEFAULT_FLOOR
        defaultLocationShouldBeFound("floor.equals=" + DEFAULT_FLOOR);

        // Get all the locationList where floor equals to UPDATED_FLOOR
        defaultLocationShouldNotBeFound("floor.equals=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    void getAllLocationsByFloorIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where floor in DEFAULT_FLOOR or UPDATED_FLOOR
        defaultLocationShouldBeFound("floor.in=" + DEFAULT_FLOOR + "," + UPDATED_FLOOR);

        // Get all the locationList where floor equals to UPDATED_FLOOR
        defaultLocationShouldNotBeFound("floor.in=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    void getAllLocationsByFloorIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where floor is not null
        defaultLocationShouldBeFound("floor.specified=true");

        // Get all the locationList where floor is null
        defaultLocationShouldNotBeFound("floor.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByFloorContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where floor contains DEFAULT_FLOOR
        defaultLocationShouldBeFound("floor.contains=" + DEFAULT_FLOOR);

        // Get all the locationList where floor contains UPDATED_FLOOR
        defaultLocationShouldNotBeFound("floor.contains=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    void getAllLocationsByFloorNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where floor does not contain DEFAULT_FLOOR
        defaultLocationShouldNotBeFound("floor.doesNotContain=" + DEFAULT_FLOOR);

        // Get all the locationList where floor does not contain UPDATED_FLOOR
        defaultLocationShouldBeFound("floor.doesNotContain=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode is not null
        defaultLocationShouldBeFound("postalCode.specified=true");

        // Get all the locationList where postalCode is null
        defaultLocationShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode contains DEFAULT_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode contains UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city equals to DEFAULT_CITY
        defaultLocationShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the locationList where city equals to UPDATED_CITY
        defaultLocationShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city in DEFAULT_CITY or UPDATED_CITY
        defaultLocationShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the locationList where city equals to UPDATED_CITY
        defaultLocationShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city is not null
        defaultLocationShouldBeFound("city.specified=true");

        // Get all the locationList where city is null
        defaultLocationShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByCityContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city contains DEFAULT_CITY
        defaultLocationShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the locationList where city contains UPDATED_CITY
        defaultLocationShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city does not contain DEFAULT_CITY
        defaultLocationShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the locationList where city does not contain UPDATED_CITY
        defaultLocationShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country equals to DEFAULT_COUNTRY
        defaultLocationShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the locationList where country equals to UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllLocationsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultLocationShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the locationList where country equals to UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllLocationsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country is not null
        defaultLocationShouldBeFound("country.specified=true");

        // Get all the locationList where country is null
        defaultLocationShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByCountryContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country contains DEFAULT_COUNTRY
        defaultLocationShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the locationList where country contains UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllLocationsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country does not contain DEFAULT_COUNTRY
        defaultLocationShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the locationList where country does not contain UPDATED_COUNTRY
        defaultLocationShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllLocationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where type equals to DEFAULT_TYPE
        defaultLocationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the locationList where type equals to UPDATED_TYPE
        defaultLocationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultLocationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the locationList where type equals to UPDATED_TYPE
        defaultLocationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where type is not null
        defaultLocationShouldBeFound("type.specified=true");

        // Get all the locationList where type is null
        defaultLocationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude equals to DEFAULT_LONGITUDE
        defaultLocationShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude equals to UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultLocationShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the locationList where longitude equals to UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is not null
        defaultLocationShouldBeFound("longitude.specified=true");

        // Get all the locationList where longitude is null
        defaultLocationShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude contains DEFAULT_LONGITUDE
        defaultLocationShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude contains UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude does not contain DEFAULT_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude does not contain UPDATED_LONGITUDE
        defaultLocationShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude equals to DEFAULT_LATITUDE
        defaultLocationShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude equals to UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultLocationShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the locationList where latitude equals to UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is not null
        defaultLocationShouldBeFound("latitude.specified=true");

        // Get all the locationList where latitude is null
        defaultLocationShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude contains DEFAULT_LATITUDE
        defaultLocationShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude contains UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude does not contain DEFAULT_LATITUDE
        defaultLocationShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude does not contain UPDATED_LATITUDE
        defaultLocationShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByPlusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where plusCode equals to DEFAULT_PLUS_CODE
        defaultLocationShouldBeFound("plusCode.equals=" + DEFAULT_PLUS_CODE);

        // Get all the locationList where plusCode equals to UPDATED_PLUS_CODE
        defaultLocationShouldNotBeFound("plusCode.equals=" + UPDATED_PLUS_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPlusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where plusCode in DEFAULT_PLUS_CODE or UPDATED_PLUS_CODE
        defaultLocationShouldBeFound("plusCode.in=" + DEFAULT_PLUS_CODE + "," + UPDATED_PLUS_CODE);

        // Get all the locationList where plusCode equals to UPDATED_PLUS_CODE
        defaultLocationShouldNotBeFound("plusCode.in=" + UPDATED_PLUS_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPlusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where plusCode is not null
        defaultLocationShouldBeFound("plusCode.specified=true");

        // Get all the locationList where plusCode is null
        defaultLocationShouldNotBeFound("plusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByPlusCodeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where plusCode contains DEFAULT_PLUS_CODE
        defaultLocationShouldBeFound("plusCode.contains=" + DEFAULT_PLUS_CODE);

        // Get all the locationList where plusCode contains UPDATED_PLUS_CODE
        defaultLocationShouldNotBeFound("plusCode.contains=" + UPDATED_PLUS_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPlusCodeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where plusCode does not contain DEFAULT_PLUS_CODE
        defaultLocationShouldNotBeFound("plusCode.doesNotContain=" + DEFAULT_PLUS_CODE);

        // Get all the locationList where plusCode does not contain UPDATED_PLUS_CODE
        defaultLocationShouldBeFound("plusCode.doesNotContain=" + UPDATED_PLUS_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByTutorIsEqualToSomething() throws Exception {
        Tutor tutor;
        if (TestUtil.findAll(em, Tutor.class).isEmpty()) {
            locationRepository.saveAndFlush(location);
            tutor = TutorResourceIntTest.createEntity(em);
        } else {
            tutor = TestUtil.findAll(em, Tutor.class).get(0);
        }
        em.persist(tutor);
        em.flush();
        location.addTutor(tutor);
        locationRepository.saveAndFlush(location);
        Long tutorId = tutor.getId();

        // Get all the locationList where tutor equals to tutorId
        defaultLocationShouldBeFound("tutorId.equals=" + tutorId);

        // Get all the locationList where tutor equals to (tutorId + 1)
        defaultLocationShouldNotBeFound("tutorId.equals=" + (tutorId + 1));
    }

    @Test
    @Transactional
    void getAllLocationsByOrganizationIsEqualToSomething() throws Exception {
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            locationRepository.saveAndFlush(location);
            organization = OrganizationResourceIntTest.createEntity(em);
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        em.persist(organization);
        em.flush();
        locationRepository.saveAndFlush(location);
        Long organizationId = organization.getId();

        // Get all the locationList where organization equals to organizationId
        defaultLocationShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the locationList where organization equals to (organizationId + 1)
        defaultLocationShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].portNumber").value(hasItem(DEFAULT_PORT_NUMBER)))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].plusCode").value(hasItem(DEFAULT_PLUS_CODE)));

        // Check, that the count call also returns 1
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .designation(UPDATED_DESIGNATION)
            .street(UPDATED_STREET)
            .portNumber(UPDATED_PORT_NUMBER)
            .floor(UPDATED_FLOOR)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .plusCode(UPDATED_PLUS_CODE);
        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);

        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLocation.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testLocation.getPortNumber()).isEqualTo(UPDATED_PORT_NUMBER);
        assertThat(testLocation.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocation.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testLocation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocation.getPlusCode()).isEqualTo(UPDATED_PLUS_CODE);
    }

    @Test
    @Transactional
    void putNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .designation(UPDATED_DESIGNATION)
            .portNumber(UPDATED_PORT_NUMBER)
            .floor(UPDATED_FLOOR)
            .city(UPDATED_CITY)
            .type(UPDATED_TYPE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLocation.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testLocation.getPortNumber()).isEqualTo(UPDATED_PORT_NUMBER);
        assertThat(testLocation.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testLocation.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocation.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testLocation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocation.getPlusCode()).isEqualTo(DEFAULT_PLUS_CODE);
    }

    @Test
    @Transactional
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .designation(UPDATED_DESIGNATION)
            .street(UPDATED_STREET)
            .portNumber(UPDATED_PORT_NUMBER)
            .floor(UPDATED_FLOOR)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .plusCode(UPDATED_PLUS_CODE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLocation.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testLocation.getPortNumber()).isEqualTo(UPDATED_PORT_NUMBER);
        assertThat(testLocation.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocation.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testLocation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocation.getPlusCode()).isEqualTo(UPDATED_PLUS_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, location.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package pt.famility.backoffice.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.domain.Contact;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.repository.LocationRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.LocationService;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * * REST controller for managing {@link pt.famility.backoffice.domain.Location}.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "location";

    private final LocationRepository locationRepository;

    private final AccessValidator accessValidator;

    private final LocationService locationService;

    public LocationResource(
            LocationRepository locationRepository,
            LocationService locationService,
            AccessValidator accessValidator
    ) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /locations : Create a new location.
     *
     * @param location the location to create
     * @return the ResponseEntity with status 201 (Created) and with body the new location, or with status 400 (Bad Request) if the location has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/locations")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);
        if (location.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Location result = locationRepository.save(location);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    // TODO: CHANGE LOCATION PARAMETER TO DTO

    /**
     * PUT  /locations : Updates an existing location.
     *
     * @param location the location to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated location,
     * or with status 400 (Bad Request) if the location is not valid,
     * or with status 500 (Internal Server Error) if the location couldn't be updated
     */
    @PutMapping("/locations")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Location> updateLocation(@Valid @RequestBody Location location) {
        log.debug("REST request to update Location : {}", location);
        accessValidator.canDeleteOrUpdateLocation(location.getId());
        if (location.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        Location result = locationRepository.save(location);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, location.getId().toString())).body(result);
    }

    /**
     * GET  /locations : get all the locations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
     */
    @GetMapping("/locations")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public List<Location> getAllLocations() {
        log.debug("REST request to get all Locations");
        return locationRepository.findAll();
    }

    /**
     * GET  /locations/:id : get the "id" location.
     *
     * @param id the id of the location to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the location, or with status 404 (Not Found)
     */
    @GetMapping("/locations/{id}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        accessValidator.canAccessLocation(id);
        Optional<Location> location = locationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(location);
    }

    /**
     * DELETE  /locations/:id : delete the "id" location.
     *
     * @param id the id of the location to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/locations/{id}")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        accessValidator.canDeleteOrUpdateLocation(id);
        try {
            locationRepository.deleteById(id);
        } catch (DataIntegrityViolationException ce) {
            throw new BadRequestAlertException("This location is being used", ENTITY_NAME, "location-in-use");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/location/{locationId}/contacts")

    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<List<Contact>> getOrganizationPublicContactsByLocationId(@PathVariable Long locationId) {
        log.debug("REST request to get Contacts by locationId: {}", locationId);
        List<Contact> contacts = locationService.getOrganizationPublicContactsByLocationId(locationId);
        return ResponseEntity.ok(contacts);
    }
}

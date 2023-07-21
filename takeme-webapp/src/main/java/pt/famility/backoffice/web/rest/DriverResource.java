package pt.famility.backoffice.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.domain.Driver;
import pt.famility.backoffice.repository.DriverRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.DriverService;
import pt.famility.backoffice.service.ItineraryService;
import pt.famility.backoffice.service.dto.DriverDTO;
import pt.famility.backoffice.service.dto.ItineraryDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.NotFoundAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static pt.famility.backoffice.config.Constants.DAY_IN_SECONDS;

/**
 * REST controller for managing Driver.
 */
@RestController
@RequestMapping("/api")
public class DriverResource {

    private final Logger log = LoggerFactory.getLogger(DriverResource.class);

    private static final String ENTITY_NAME = "driver";

    private final DriverRepository driverRepository;

    private final ItineraryService itineraryService;

    private final DriverService driverService;

    private AccessValidator accessValidator;

    public DriverResource(DriverRepository driverRepository, ItineraryService itineraryService, DriverService driverService, AccessValidator accessValidator) {
        this.driverRepository = driverRepository;
        this.itineraryService = itineraryService;
        this.driverService = driverService;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /drivers : Create a new driver.
     *
     * @param driverDTO the driver to create
     * @return the ResponseEntity with status 201 (Created) and with body the new driverDTO, or with status 400 (Bad Request) if the driverDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/drivers")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<DriverDTO> createDriver(@Valid @RequestBody DriverDTO driverDTO) throws URISyntaxException {
        log.debug("REST request to save Driver : {}", driverDTO);
        accessValidator.canCreateDriver(driverDTO.getOrganization().getId());
        if (driverDTO.getId() != null) {
            throw new BadRequestAlertException("A new driver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DriverDTO result = driverService.save(driverDTO);
        return ResponseEntity.created(new URI("/api/drivers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * Get photo by Driver
     * @param id
     * @return
     */
    @GetMapping("/drivers/{id}/photo/{photoFileId}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<PhotoDTO> getPhoto(HttpServletResponse response, @PathVariable Long id, @PathVariable Long photoFileId) {
        log.debug("Driver photo: {}", id);
        accessValidator.canAccessDriverPhoto(id);
        response.setHeader("Cache-Control", "no-transform, public, max-age=31536000");
        response.setHeader("Pragma", "");
        return ResponseEntity.ok().body(driverService.getPhotoByDriverAndPhotoFileId(id, photoFileId));
    }

    /**
     * PUT  /drivers : Updates an existing driver.
     *
     * @param driverDTO the driver to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated driver,
     * or with status 400 (Bad Request) if the driver is not valid,
     * or with status 500 (Internal Server Error) if the driver couldn't be updated
     */
    @PutMapping("/drivers")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<DriverDTO> updateDriver(@Valid @RequestBody DriverDTO driverDTO) {
        log.debug("REST request to update Driver : {}", driverDTO);
        accessValidator.canUpdateDriver(driverDTO.getId());
        if (driverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DriverDTO result = driverService.save(driverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /drivers : get all the drivers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of drivers in body
     */
    @GetMapping("/drivers")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<Driver> getAllDrivers() {
        log.debug("REST request to get all Drivers");
        return driverRepository.findAll();
    }

    /**
     * GET  /drivers/:id : get the "id" driver.
     *
     * @param id the id of the driver to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the driver, or with status 404 (Not Found)
     */
    @GetMapping("/drivers/{id}")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Long id) {
        log.debug("REST request to get Driver : {}", id);
        accessValidator.canAccessDriver(id);
        return new ResponseEntity<>(driverService.findDriverById(id),HttpStatus.OK);
    }

    /**
     * DELETE  /drivers/:id : delete the "id" driver.
     *
     * @param id the id of the driver to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/drivers/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        log.debug("REST request to delete Driver : {}", id);
        driverRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET /drivers/:id/itineraries : get itineraries of the day for the driver
     *
     * @param id driver id
     * @return list of itineraries with status 200 (OK)
     */
    @GetMapping("/drivers/{id}/itineraries")

    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<List<ItineraryDTO>> getItineraries(@PathVariable Long id) {
        log.debug("REST request the Driver's itineraries : {}", id);
        accessValidator.canAccessItineraries(id);
        Instant minToday = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant maxToday = minToday.plusSeconds(DAY_IN_SECONDS);
        List<ItineraryDTO> itineraries = itineraryService.findAllByDriverIdAndScheduledTimeBetween(id, minToday, maxToday);
        return new ResponseEntity<>(itineraries, HttpStatus.OK);
    }

    /**
     * GET /drivers/:id/itinerary/current : get the first itinerary active for the driver
     *
     * @param id driver id
     * @return itinerary with status (200) ok, or 404 (NotFound)
     */
    @GetMapping("/drivers/{id}/itinerary/current")

    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<ItineraryDTO> getCurrentItinerary(@PathVariable Long id) {
        log.debug("REST request to get current itinerary of driver : {}", id);
        accessValidator.canAccessCurrentItinerary(id);
        Instant minToday = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant maxToday = minToday.plusSeconds(DAY_IN_SECONDS);
        ItineraryDTO itineraryDTO = itineraryService.findFirstByDriverIdAndItineraryStatusTypeAndScheduledTimeBetween(id, minToday, maxToday);
        if (itineraryDTO != null) {
            return new ResponseEntity<>(itineraryDTO, HttpStatus.OK);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @ExceptionHandler({NotFoundAlertException.class})
    void handleNotFoundAlertException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

}

package pt.famility.backoffice.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek;
import pt.famility.backoffice.repository.ServiceStopPointDaysOfWeekRepository;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek}.
 */
@RestController
@RequestMapping("/api")
public class ServiceStopPointDaysOfWeekResource {

    private final Logger log = LoggerFactory.getLogger(ServiceStopPointDaysOfWeekResource.class);

    private static final String ENTITY_NAME = "serviceStopPointDaysOfWeek";

    private final ServiceStopPointDaysOfWeekRepository serviceStopPointDaysOfWeekRepository;

    public ServiceStopPointDaysOfWeekResource(ServiceStopPointDaysOfWeekRepository serviceStopPointDaysOfWeekRepository) {
        this.serviceStopPointDaysOfWeekRepository = serviceStopPointDaysOfWeekRepository;
    }

    /**
     * POST  /service-stop-point-days-of-weeks : Create a new serviceStopPointDaysOfWeek.
     *
     * @param serviceStopPointDaysOfWeek the serviceStopPointDaysOfWeek to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceStopPointDaysOfWeek, or with status 400 (Bad Request) if the serviceStopPointDaysOfWeek has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-stop-point-days-of-weeks")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPointDaysOfWeek> createServiceStopPointDaysOfWeek(@RequestBody ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek) throws URISyntaxException {
        log.debug("REST request to save ServiceStopPointDaysOfWeek : {}", serviceStopPointDaysOfWeek);
        if (serviceStopPointDaysOfWeek.getId() != null) {
            throw new BadRequestAlertException("A new serviceStopPointDaysOfWeek cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceStopPointDaysOfWeek result = serviceStopPointDaysOfWeekRepository.save(serviceStopPointDaysOfWeek);
        return ResponseEntity.created(new URI("/api/service-stop-point-days-of-weeks/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /service-stop-point-days-of-weeks : Updates an existing serviceStopPointDaysOfWeek.
     *
     * @param serviceStopPointDaysOfWeek the serviceStopPointDaysOfWeek to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceStopPointDaysOfWeek,
     * or with status 400 (Bad Request) if the serviceStopPointDaysOfWeek is not valid,
     * or with status 500 (Internal Server Error) if the serviceStopPointDaysOfWeek couldn't be updated
     */
    @PutMapping("/service-stop-point-days-of-weeks")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPointDaysOfWeek> updateServiceStopPointDaysOfWeek(@RequestBody ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek) {
        log.debug("REST request to update ServiceStopPointDaysOfWeek : {}", serviceStopPointDaysOfWeek);
        if (serviceStopPointDaysOfWeek.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceStopPointDaysOfWeek result = serviceStopPointDaysOfWeekRepository.save(serviceStopPointDaysOfWeek);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceStopPointDaysOfWeek.getId().toString()))
                .body(result);
    }

    // TODO: ADD ACCESS VALIDATOR AND CREATE A SERVICE_STOP_POINT_DAYS_OF_WEEK_DTO AND SERVICE FILE

    /**
     * GET  /service-stop-point-days-of-weeks : get all the serviceStopPointDaysOfWeeks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serviceStopPointDaysOfWeeks in body
     */
    @GetMapping("/service-stop-point-days-of-weeks")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public List<ServiceStopPointDaysOfWeek> getAllServiceStopPointDaysOfWeeks() {
        log.debug("REST request to get all ServiceStopPointDaysOfWeeks");
        return serviceStopPointDaysOfWeekRepository.findAll();
    }

    /**
     * GET  /service-stop-point-days-of-weeks/:id : get the "id" serviceStopPointDaysOfWeek.
     *
     * @param id the id of the serviceStopPointDaysOfWeek to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceStopPointDaysOfWeek, or with status 404 (Not Found)
     */
    @GetMapping("/service-stop-point-days-of-weeks/{id}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPointDaysOfWeek> getServiceStopPointDaysOfWeek(@PathVariable Long id) {
        log.debug("REST request to get ServiceStopPointDaysOfWeek : {}", id);
        Optional<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeek = serviceStopPointDaysOfWeekRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceStopPointDaysOfWeek);
    }

    /**
     * DELETE  /service-stop-point-days-of-weeks/:id : delete the "id" serviceStopPointDaysOfWeek.
     *
     * @param id the id of the serviceStopPointDaysOfWeek to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-stop-point-days-of-weeks/{id}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteServiceStopPointDaysOfWeek(@PathVariable Long id) {
        log.debug("REST request to delete ServiceStopPointDaysOfWeek : {}", id);
        serviceStopPointDaysOfWeekRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

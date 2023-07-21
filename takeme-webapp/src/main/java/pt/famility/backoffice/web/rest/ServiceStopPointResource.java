package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.ServiceStopPoint;
import pt.famility.backoffice.repository.ServiceStopPointRepository;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ServiceStopPoint.
 */
@RestController
@RequestMapping("/api")
public class ServiceStopPointResource {

    private final Logger log = LoggerFactory.getLogger(ServiceStopPointResource.class);

    private static final String ENTITY_NAME = "serviceStopPoint";

    private final ServiceStopPointRepository serviceStopPointRepository;

    public ServiceStopPointResource(ServiceStopPointRepository serviceStopPointRepository) {
        this.serviceStopPointRepository = serviceStopPointRepository;
    }

    /**
     * POST  /service-stop-points : Create a new serviceStopPoint.
     *
     * @param serviceStopPoint the serviceStopPoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceStopPoint, or with status 400 (Bad Request) if the serviceStopPoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-stop-points")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPoint> createServiceStopPoint(@Valid @RequestBody ServiceStopPoint serviceStopPoint) throws URISyntaxException {
        log.debug("REST request to save ServiceStopPoint : {}", serviceStopPoint);
        if (serviceStopPoint.getId() != null) {
            throw new BadRequestAlertException("A new serviceStopPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceStopPoint result = serviceStopPointRepository.save(serviceStopPoint);
        return ResponseEntity.created(new URI("/api/service-stop-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-stop-points : Updates an existing serviceStopPoint.
     *
     * @param serviceStopPoint the serviceStopPoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceStopPoint,
     * or with status 400 (Bad Request) if the serviceStopPoint is not valid,
     * or with status 500 (Internal Server Error) if the serviceStopPoint couldn't be updated
     */
    @PutMapping("/service-stop-points")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPoint> updateServiceStopPoint(@Valid @RequestBody ServiceStopPoint serviceStopPoint) {
        log.debug("REST request to update ServiceStopPoint : {}", serviceStopPoint);
        if (serviceStopPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceStopPoint result = serviceStopPointRepository.save(serviceStopPoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceStopPoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-stop-points : get all the serviceStopPoints.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of serviceStopPoints in body
     */
    @GetMapping("/service-stop-points")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<ServiceStopPoint> getAllServiceStopPoints(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ServiceStopPoints");
        return serviceStopPointRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /service-stop-points/:id : get the "id" serviceStopPoint.
     *
     * @param id the id of the serviceStopPoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceStopPoint, or with status 404 (Not Found)
     */
    @GetMapping("/service-stop-points/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPoint> getServiceStopPoint(@PathVariable Long id) {
        log.debug("REST request to get ServiceStopPoint : {}", id);
        Optional<ServiceStopPoint> serviceStopPoint = serviceStopPointRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(serviceStopPoint);
    }

    /**
     * DELETE  /service-stop-points/:id : delete the "id" serviceStopPoint.
     *
     * @param id the id of the serviceStopPoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-stop-points/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteServiceStopPoint(@PathVariable Long id) {
        log.debug("REST request to delete ServiceStopPoint : {}", id);
        serviceStopPointRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

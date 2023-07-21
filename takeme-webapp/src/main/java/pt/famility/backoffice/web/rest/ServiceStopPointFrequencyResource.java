package pt.famility.backoffice.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
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
import pt.famility.backoffice.domain.ServiceStopPointFrequency;
import pt.famility.backoffice.repository.ServiceStopPointFrequencyRepository;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.ServiceStopPointFrequency}.
 */
@RestController
@RequestMapping("/api")
public class ServiceStopPointFrequencyResource {

    private final Logger log = LoggerFactory.getLogger(ServiceStopPointFrequencyResource.class);

    private static final String ENTITY_NAME = "serviceStopPointFrequency";

    private final ServiceStopPointFrequencyRepository serviceStopPointFrequencyRepository;

    public ServiceStopPointFrequencyResource(ServiceStopPointFrequencyRepository serviceStopPointFrequencyRepository) {
        this.serviceStopPointFrequencyRepository = serviceStopPointFrequencyRepository;
    }

    /**
     * POST  /service-stop-point-frequencies : Create a new serviceStopPointFrequency.
     *
     * @param serviceStopPointFrequency the serviceStopPointFrequency to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceStopPointFrequency, or with status 400 (Bad Request) if the serviceStopPointFrequency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-stop-point-frequencies")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPointFrequency> createServiceStopPointFrequency(@Valid @RequestBody ServiceStopPointFrequency serviceStopPointFrequency) throws URISyntaxException {
        log.debug("REST request to save ServiceStopPointFrequency : {}", serviceStopPointFrequency);
        if (serviceStopPointFrequency.getId() != null) {
            throw new BadRequestAlertException("A new serviceStopPointFrequency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceStopPointFrequency result = serviceStopPointFrequencyRepository.save(serviceStopPointFrequency);
        return ResponseEntity.created(new URI("/api/service-stop-point-frequencies/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /service-stop-point-frequencies : Updates an existing serviceStopPointFrequency.
     *
     * @param serviceStopPointFrequency the serviceStopPointFrequency to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceStopPointFrequency,
     * or with status 400 (Bad Request) if the serviceStopPointFrequency is not valid,
     * or with status 500 (Internal Server Error) if the serviceStopPointFrequency couldn't be updated
     */
    @PutMapping("/service-stop-point-frequencies")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPointFrequency> updateServiceStopPointFrequency(@Valid @RequestBody ServiceStopPointFrequency serviceStopPointFrequency) {
        log.debug("REST request to update ServiceStopPointFrequency : {}", serviceStopPointFrequency);
        if (serviceStopPointFrequency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceStopPointFrequency result = serviceStopPointFrequencyRepository.save(serviceStopPointFrequency);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceStopPointFrequency.getId().toString()))
                .body(result);
    }

    // TODO: ADD ACCESS VALIDATOR AND CREATE A SERVICE_STOP_POINT_FREQUENCY_DTO AND SERVICE FILE

    /**
     * GET  /service-stop-point-frequencies : get all the serviceStopPointFrequencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serviceStopPointFrequencies in body
     */
    @GetMapping("/service-stop-point-frequencies")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public List<ServiceStopPointFrequency> getAllServiceStopPointFrequencies() {
        log.debug("REST request to get all ServiceStopPointFrequencies");
        return serviceStopPointFrequencyRepository.findAll(Sort.by("id"));
    }

    /**
     * GET  /service-stop-point-frequencies/:id : get the "id" serviceStopPointFrequency.
     *
     * @param id the id of the serviceStopPointFrequency to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceStopPointFrequency, or with status 404 (Not Found)
     */
    @GetMapping("/service-stop-point-frequencies/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ServiceStopPointFrequency> getServiceStopPointFrequency(@PathVariable Long id) {
        log.debug("REST request to get ServiceStopPointFrequency : {}", id);
        Optional<ServiceStopPointFrequency> serviceStopPointFrequency = serviceStopPointFrequencyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceStopPointFrequency);
    }

    /**
     * DELETE  /service-stop-point-frequencies/:id : delete the "id" serviceStopPointFrequency.
     *
     * @param id the id of the serviceStopPointFrequency to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-stop-point-frequencies/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteServiceStopPointFrequency(@PathVariable Long id) {
        log.debug("REST request to delete ServiceStopPointFrequency : {}", id);
        serviceStopPointFrequencyRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

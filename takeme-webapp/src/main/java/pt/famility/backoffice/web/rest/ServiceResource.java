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
import pt.famility.backoffice.domain.Service;
import pt.famility.backoffice.repository.ServiceRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ServiceService;
import pt.famility.backoffice.service.dto.ServiceDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.Service}.
 */
@RestController
@RequestMapping("/api")
public class ServiceResource {

    private final Logger log = LoggerFactory.getLogger(ServiceResource.class);

    private static final String ENTITY_NAME = "service";

    private final ServiceRepository serviceRepository;

    private final ServiceService serviceService;

    private final AccessValidator accessValidator;

    public ServiceResource(
            ServiceRepository serviceRepository,
            ServiceService serviceService,
            AccessValidator accessValidator
    ) {
        this.serviceRepository = serviceRepository;
        this.serviceService = serviceService;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /services : Create a new service.
     *
     * @param serviceDTO the service to create
     * @return the ResponseEntity with status 201 (Created) and with body the new service, or with status 400 (Bad Request) if the service has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/services")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceDTO serviceDTO) throws URISyntaxException {
        log.debug("REST request to save Service : {}", serviceDTO);
        accessValidator.canCreateOrUpdateService(serviceDTO.getOrganization().getId());
        if (serviceDTO.getId() != null) throw new BadRequestAlertException("A new service cannot already have an ID", ENTITY_NAME, "idexists");
        ServiceDTO result = serviceService.getServiceDetail(serviceService.save(serviceDTO).getId());
        return ResponseEntity.created(new URI("/api/services/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT  /services : Updates an existing service.
     *
     * @param serviceDTO the service to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated service,
     * or with status 400 (Bad Request) if the service is not valid,
     * or with status 500 (Internal Server Error) if the service couldn't be updated
     */
    @PutMapping("/services")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ServiceDTO> updateService(@Valid @RequestBody ServiceDTO serviceDTO) {
        log.debug("REST request to update Service : {}", serviceDTO);
        accessValidator.canCreateOrUpdateService(serviceDTO.getOrganization().getId());
        if (serviceDTO.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        ServiceDTO result = serviceService.getServiceDetail(serviceService.save(serviceDTO).getId());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * GET  /services : get all the services.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of services in body
     */
    @GetMapping("/services")
    @Secured(AuthoritiesConstants.FAMILITY)
    public List<Service> getAllServices() {
        log.debug("REST request to get all Services");
        return serviceRepository.findAll();
    }

    /**
     * DELETE  /services/:id : delete the "id" service.
     *
     * @param id the id of the service to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/services/{id}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        log.debug("REST request to delete Service : {}", id);
        accessValidator.canAccessOrDeleteService(id);
        serviceRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code GET  /services/:id} : get the "id" service.
     *
     * @param id the id of the service to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceDTO.
     */
    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceDTO> getServiceDetail(@PathVariable Long id) {
        log.debug("REST request to get Service {}", id);
        ServiceDTO service = serviceService.getServiceDetail(id);
        return ResponseEntity.ok().body(service);
    }
}

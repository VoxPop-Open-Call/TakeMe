package pt.famility.backoffice.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.famility.backoffice.repository.ChildItinerarySubscriptionRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ChildItinerarySubscriptionQueryService;
import pt.famility.backoffice.service.ChildItinerarySubscriptionService;
import pt.famility.backoffice.service.ServiceService;
import pt.famility.backoffice.service.criteria.ChildItinerarySubscriptionCriteria;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.dto.ServiceDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static pt.famility.backoffice.security.SecurityUtils.hasCurrentUserAnyOfAuthorities;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.ChildItinerarySubscription}.
 */
@RestController
@RequestMapping("/api")
public class ChildItinerarySubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(ChildItinerarySubscriptionResource.class);

    private static final String ENTITY_NAME = "childItinerarySubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChildItinerarySubscriptionService childItinerarySubscriptionService;

    private final ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository;

    private final ChildItinerarySubscriptionQueryService childItinerarySubscriptionQueryService;

    private final ServiceService serviceService;

    private final AccessValidator accessValidator;

    public ChildItinerarySubscriptionResource(
            ChildItinerarySubscriptionService childItinerarySubscriptionService,
            ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository,
            ChildItinerarySubscriptionQueryService childItinerarySubscriptionQueryService,
            ServiceService serviceService,
            AccessValidator accessValidator
    ) {
        this.childItinerarySubscriptionService = childItinerarySubscriptionService;
        this.childItinerarySubscriptionRepository = childItinerarySubscriptionRepository;
        this.childItinerarySubscriptionQueryService = childItinerarySubscriptionQueryService;
        this.serviceService = serviceService;
        this.accessValidator = accessValidator;
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code POST  /child-itinerary-subscriptions} : Create a new childItinerarySubscription.
     *
     * @param childItinerarySubscriptionDTO the childItinerarySubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new childItinerarySubscriptionDTO, or with status {@code 400 (Bad Request)} if the childItinerarySubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/child-itinerary-subscriptions")
    public ResponseEntity<ChildItinerarySubscriptionDTO> createChildItinerarySubscription(@Valid @RequestBody ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save ChildItinerarySubscription : {}", childItinerarySubscriptionDTO);
        if (childItinerarySubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new childItinerarySubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChildItinerarySubscriptionDTO result = childItinerarySubscriptionService.save(childItinerarySubscriptionDTO);
        return ResponseEntity
                .created(new URI("/api/child-itinerary-subscriptions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /child-itinerary-subscriptions/:id} : Updates an existing childItinerarySubscription.
     *
     * @param id                            the id of the childItinerarySubscriptionDTO to save.
     * @param childItinerarySubscriptionDTO the childItinerarySubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated childItinerarySubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the childItinerarySubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the childItinerarySubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/child-itinerary-subscriptions/{id}")
    public ResponseEntity<ChildItinerarySubscriptionDTO> updateChildItinerarySubscription(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChildItinerarySubscription : {}, {}", id, childItinerarySubscriptionDTO);
        if (childItinerarySubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, childItinerarySubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!childItinerarySubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChildItinerarySubscriptionDTO result = childItinerarySubscriptionService.update(childItinerarySubscriptionDTO);
        return ResponseEntity
                .ok()
                .headers(
                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, childItinerarySubscriptionDTO.getId().toString())
                )
                .body(result);
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code PATCH  /child-itinerary-subscriptions/:id} : Partial updates given fields of an existing childItinerarySubscription, field will ignore if it is null
     *
     * @param id                            the id of the childItinerarySubscriptionDTO to save.
     * @param childItinerarySubscriptionDTO the childItinerarySubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated childItinerarySubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the childItinerarySubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the childItinerarySubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the childItinerarySubscriptionDTO couldn't be updated.
     */
    @PatchMapping(value = "/child-itinerary-subscriptions/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<ChildItinerarySubscriptionDTO> partialUpdateChildItinerarySubscription(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO) {
        log.debug("REST request to partial update ChildItinerarySubscription partially : {}, {}", id, childItinerarySubscriptionDTO);
        if (childItinerarySubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, childItinerarySubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!childItinerarySubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<ChildItinerarySubscriptionDTO> result = childItinerarySubscriptionService.partialUpdate(childItinerarySubscriptionDTO);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, childItinerarySubscriptionDTO.getId().toString()));
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code GET  /child-itinerary-subscriptions} : get all the childItinerarySubscriptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of childItinerarySubscriptions in body.
     */
    @GetMapping("/child-itinerary-subscriptions")
    public ResponseEntity<List<ChildItinerarySubscriptionDTO>> getAllChildItinerarySubscriptions(
            ChildItinerarySubscriptionCriteria criteria,
            @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ChildItinerarySubscriptions by criteria: {}", criteria);
        if (hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.BUS_COMPANY)) {
            accessValidator.canPerformOrganizationRelatedAction(criteria.getOrganizationId().getEquals());
        }
        Page<ChildItinerarySubscriptionDTO> page = childItinerarySubscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /child-itinerary-subscriptions/count} : count all the childItinerarySubscriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/child-itinerary-subscriptions/count")
    public ResponseEntity<Long> countChildItinerarySubscriptions(ChildItinerarySubscriptionCriteria criteria) {
        log.debug("REST request to count ChildItinerarySubscriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(childItinerarySubscriptionQueryService.countByCriteria(criteria));
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code GET  /child-itinerary-subscriptions/:id} : get the "id" childItinerarySubscription.
     *
     * @param id the id of the childItinerarySubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the childItinerarySubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/child-itinerary-subscriptions/{id}")
    public ResponseEntity<ChildItinerarySubscriptionDTO> getChildItinerarySubscription(@PathVariable Long id) {
        log.debug("REST request to get ChildItinerarySubscription {}", id);
        Optional<ChildItinerarySubscriptionDTO> subscription = childItinerarySubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscription);
    }

    /**
     * {@code DELETE  /child-itinerary-subscriptions/:id} : delete the "id" childItinerarySubscription.
     *
     * @param id the id of the childItinerarySubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/child-itinerary-subscriptions/{id}")
    public ResponseEntity<Void> deleteChildItinerarySubscription(@PathVariable Long id) {
        log.debug("REST request to delete ChildItinerarySubscription : {}", id);
        childItinerarySubscriptionService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * {@code DELETE  /child-itinerary-subscriptions/promoter-itinerary/:id} : delete the childItinerarySubscription by promoter-itinerary.
     *
     * @param id the id of the promoter-itinerary of the childItinerarySubscriptions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/child-itinerary-subscriptions/promoter-itinerary/{id}")
    public ResponseEntity<Void> deleteChildItinerarySubscriptionByPromoterItineraryId(@PathVariable Long id) {
        log.debug("REST request to delete ChildItinerarySubscription by promoter-itinerary-id : {}", id);
        childItinerarySubscriptionService.deleteByPromoterItineraryId(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code GET  /child-itinerary-subscriptions/:id/services} : get all the services of childItinerarySubscription "id".
     *
     * @param id the childItinerarySubscription's "id".
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of services in body.
     */
    @GetMapping("/child-itinerary-subscriptions/{id}/services")
    public ResponseEntity<List<ServiceDTO>> getServicesByChildItinerarySubscriptionId(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get all Services of ChildItinerarySubscription {}", id);
        Page<ServiceDTO> services = serviceService.getServicesBySubscriptionId(id, pageable);
        HttpHeaders headers = pt.famility.backoffice.web.rest.util.PaginationUtil.generatePaginationHttpHeaders(services, "/child-itinerary-subscriptions/" + id + "/services");
        return ResponseEntity.ok().headers(headers).body(services.getContent());
    }
}

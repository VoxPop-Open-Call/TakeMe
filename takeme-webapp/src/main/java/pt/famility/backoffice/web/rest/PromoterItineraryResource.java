package pt.famility.backoffice.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.famility.backoffice.config.Constants;
import pt.famility.backoffice.converter.ChildStopPointsServicesConverter;
import pt.famility.backoffice.domain.ChildItinerarySubscription;
import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.repository.PromoterItineraryRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ChildItinerarySubscriptionService;
import pt.famility.backoffice.service.PromoterItineraryQueryService;
import pt.famility.backoffice.service.PromoterItineraryService;
import pt.famility.backoffice.service.criteria.PromoterItineraryCriteria;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.dto.ChildStopPointsServicesDTO;
import pt.famility.backoffice.service.dto.PromoterItineraryDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.PromoterItinerary}.
 */
@RestController
@RequestMapping("/api")
public class PromoterItineraryResource {

    private final Logger log = LoggerFactory.getLogger(PromoterItineraryResource.class);

    private static final String ENTITY_NAME = "promoterItinerary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromoterItineraryService promoterItineraryService;

    private final PromoterItineraryRepository promoterItineraryRepository;

    private final PromoterItineraryQueryService promoterItineraryQueryService;

    private final AccessValidator accessValidator;

    private final ChildItinerarySubscriptionService childItinerarySubscriptionService;

    private final ChildStopPointsServicesConverter childStopPointsServicesConverter;

    public PromoterItineraryResource(
            PromoterItineraryService promoterItineraryService,
            PromoterItineraryRepository promoterItineraryRepository,
            PromoterItineraryQueryService promoterItineraryQueryService,
            AccessValidator accessValidator,
            ChildItinerarySubscriptionService childItinerarySubscriptionService,
            ChildStopPointsServicesConverter childStopPointsServicesConverter
    ) {
        this.promoterItineraryService = promoterItineraryService;
        this.promoterItineraryRepository = promoterItineraryRepository;
        this.promoterItineraryQueryService = promoterItineraryQueryService;
        this.accessValidator = accessValidator;
        this.childItinerarySubscriptionService = childItinerarySubscriptionService;
        this.childStopPointsServicesConverter = childStopPointsServicesConverter;
    }

    /**
     * {@code POST  /promoter-itineraries} : Create a new promoterItinerary.
     *
     * @param promoterItineraryDTO the promoterItineraryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promoterItineraryDTO, or with status {@code 400 (Bad Request)} if the promoterItinerary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promoter-itineraries")
    public ResponseEntity<PromoterItineraryDTO> createPromoterItinerary(@Valid @RequestBody PromoterItineraryDTO promoterItineraryDTO)
            throws URISyntaxException {
        log.debug("REST request to save PromoterItinerary : {}", promoterItineraryDTO);
        if (promoterItineraryDTO.getId() != null) {
            throw new BadRequestAlertException("A new promoterItinerary cannot already have an ID", ENTITY_NAME, "idexists");
        }

        PromoterItineraryDTO result = promoterItineraryService.save(promoterItineraryDTO);
        return ResponseEntity
                .created(new URI("/api/promoter-itineraries/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /promoter-itineraries/:id} : Updates an existing promoterItinerary.
     *
     * @param id                   the id of the promoterItineraryDTO to save.
     * @param promoterItineraryDTO the promoterItineraryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoterItineraryDTO,
     * or with status {@code 400 (Bad Request)} if the promoterItineraryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promoterItineraryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promoter-itineraries/{id}")
    public ResponseEntity<PromoterItineraryDTO> updatePromoterItinerary(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody PromoterItineraryDTO promoterItineraryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PromoterItinerary : {}, {}", id, promoterItineraryDTO);
        if (promoterItineraryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoterItineraryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promoterItineraryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PromoterItineraryDTO result = promoterItineraryService.update(promoterItineraryDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoterItineraryDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /promoter-itineraries/:id} : Partial updates given fields of an existing promoterItinerary, field will ignore if it is null
     *
     * @param id                   the id of the promoterItineraryDTO to save.
     * @param promoterItineraryDTO the promoterItineraryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoterItineraryDTO,
     * or with status {@code 400 (Bad Request)} if the promoterItineraryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promoterItineraryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promoterItineraryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promoter-itineraries/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<PromoterItineraryDTO> partialUpdatePromoterItinerary(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody PromoterItineraryDTO promoterItineraryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PromoterItinerary partially : {}, {}", id, promoterItineraryDTO);
        if (promoterItineraryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoterItineraryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promoterItineraryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromoterItineraryDTO> result = promoterItineraryService.partialUpdate(promoterItineraryDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoterItineraryDTO.getId().toString())
        );
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code GET  /promoter-itineraries} : get all the promoterItineraries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promoterItineraries in body.
     */
    @GetMapping("/promoter-itineraries")
    public ResponseEntity<List<PromoterItineraryDTO>> getAllPromoterItineraries(PromoterItineraryCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get PromoterItineraries by criteria: {}", criteria);
        Page<PromoterItineraryDTO> page = promoterItineraryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promoter-itineraries/count} : count all the promoterItineraries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/promoter-itineraries/count")
    public ResponseEntity<Long> countPromoterItineraries(PromoterItineraryCriteria criteria) {
        log.debug("REST request to count PromoterItineraries by criteria: {}", criteria);
        return ResponseEntity.ok().body(promoterItineraryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /promoter-itineraries/:id} : get the "id" promoterItinerary.
     *
     * @param id the id of the promoterItineraryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promoterItineraryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promoter-itineraries/{id}")
    public ResponseEntity<PromoterItineraryDTO> getPromoterItinerary(@PathVariable Long id) {
        log.debug("REST request to get PromoterItinerary : {}", id);
        Optional<PromoterItineraryDTO> promoterItineraryDTO = promoterItineraryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promoterItineraryDTO);
    }

    /**
     * {@code DELETE  /promoter-itineraries/:id} : delete the "id" promoterItinerary.
     *
     * @param id the id of the promoterItineraryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promoter-itineraries/{id}")
    public ResponseEntity<Void> deletePromoterItinerary(@PathVariable Long id) {
        log.debug("REST request to delete PromoterItinerary : {}", id);
        promoterItineraryService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * {@code GET  /promoter-itineraries/promoter-service/:id} : get the list of itineraries for "id" PromoterService.
     *
     * @param id the id of the PromoterService.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PromoterItineraryDTO list, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promoter-itineraries/promoter-service/{id}")
    public ResponseEntity<List<PromoterItineraryDTO>> getPromoterItinerariesByServiceId(@PathVariable Long id) {
        log.debug("REST request to get PromoterItineraries by PromoterServiceId : {}", id);
        List<PromoterItineraryDTO> promoterItineraries = promoterItineraryService.findPromoterItinerariesByPromoterServiceId(id);
        return ResponseEntity.ok().body(promoterItineraries);
    }

    /**
     * {@code GET  /promoter-itineraries/promoter-service/:id/active} : get the list of active itineraries for "id" PromoterService.
     *
     * @param id the id of the PromoterService.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PromoterItineraryDTO list, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promoter-itineraries/promoter-service/{id}/active")
    public ResponseEntity<List<PromoterItineraryDTO>> getActivePromoterItinerariesByServiceId(@PathVariable Long id) {
        log.debug("REST request to get active PromoterItineraries by PromoterServiceId : {}", id);
        List<PromoterItineraryDTO> promoterItineraries = promoterItineraryService.findActivePromoterItinerariesByPromoterServiceId(id);
        return ResponseEntity.ok().body(promoterItineraries);
    }

    /**
     * {@code GET  /promoter-itineraries/by-organization} : get the list of active itineraries for "id" Organization.
     *
     * @param id the id of the Organization.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PromoterItineraryDTO list, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promoter-itineraries/by-organization")
    public ResponseEntity<List<PromoterItineraryDTO>> getPromoterItinerariesByOrganizationId(@RequestParam Long id, Pageable pageable) {
        log.debug("REST request to get PromoterItineraries by OrganizationId : {}", id);
        Page<PromoterItineraryDTO> promoterItineraries = promoterItineraryService.findPromoterItinerariesByOrganizationId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), promoterItineraries);
        return ResponseEntity.ok().headers(headers).body(promoterItineraries.getContent());
    }

    // TODO: ADD ACCESS VALIDATOR

    /**
     * GET /promoter-itineraries/:id/service-stop-points : get the list of stop points in services associated with "id" PromoterItinerary.
     *
     * @param id the id of the PromoterItinerary.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ChildStopPointsServicesDTO list.
     */
    @GetMapping("/promoter-itineraries/{id}/service-stop-points")
    @Secured({AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<ChildStopPointsServicesDTO>> getPromoterItineraryServiceStopPoints(@PathVariable Long id) {
        log.debug("REST request to get stop points in services associated with PromoterItinerary : {}", id);
        List<ChildItinerarySubscription> childItinerarySubscriptions = childItinerarySubscriptionService.findAllByPromoterItineraryId(id);
        List<ChildStopPointsServicesDTO> servicesStopPoints = new ArrayList<>();
        childItinerarySubscriptions.forEach(childItinerarySubscription -> servicesStopPoints.add(childStopPointsServicesConverter.convert(childItinerarySubscription)));
        return ResponseEntity.ok().body(servicesStopPoints);
    }
}

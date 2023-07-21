package pt.famility.backoffice.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.famility.backoffice.repository.PromoterStopPointRepository;
import pt.famility.backoffice.service.PromoterStopPointQueryService;
import pt.famility.backoffice.service.PromoterStopPointService;
import pt.famility.backoffice.service.criteria.PromoterStopPointCriteria;
import pt.famility.backoffice.service.dto.PromoterStopPointDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.PromoterStopPoint}.
 */
@RestController
@RequestMapping("/api")
public class PromoterStopPointResource {

    private final Logger log = LoggerFactory.getLogger(PromoterStopPointResource.class);

    private static final String ENTITY_NAME = "promoterStopPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromoterStopPointService promoterStopPointService;

    private final PromoterStopPointRepository promoterStopPointRepository;

    private final PromoterStopPointQueryService promoterStopPointQueryService;

    public PromoterStopPointResource(
        PromoterStopPointService promoterStopPointService,
        PromoterStopPointRepository promoterStopPointRepository,
        PromoterStopPointQueryService promoterStopPointQueryService
    ) {
        this.promoterStopPointService = promoterStopPointService;
        this.promoterStopPointRepository = promoterStopPointRepository;
        this.promoterStopPointQueryService = promoterStopPointQueryService;
    }

    /**
     * {@code POST  /promoter-stop-points} : Create a new promoterStopPoint.
     *
     * @param promoterStopPointDTO the promoterStopPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promoterStopPointDTO, or with status {@code 400 (Bad Request)} if the promoterStopPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promoter-stop-points")
    public ResponseEntity<PromoterStopPointDTO> createPromoterStopPoint(@Valid @RequestBody PromoterStopPointDTO promoterStopPointDTO)
        throws URISyntaxException {
        log.debug("REST request to save PromoterStopPoint : {}", promoterStopPointDTO);
        if (promoterStopPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new promoterStopPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PromoterStopPointDTO result = promoterStopPointService.save(promoterStopPointDTO);
        return ResponseEntity
            .created(new URI("/api/promoter-stop-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promoter-stop-points/:id} : Updates an existing promoterStopPoint.
     *
     * @param id the id of the promoterStopPointDTO to save.
     * @param promoterStopPointDTO the promoterStopPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoterStopPointDTO,
     * or with status {@code 400 (Bad Request)} if the promoterStopPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promoterStopPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promoter-stop-points/{id}")
    public ResponseEntity<PromoterStopPointDTO> updatePromoterStopPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PromoterStopPointDTO promoterStopPointDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PromoterStopPoint : {}, {}", id, promoterStopPointDTO);
        if (promoterStopPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoterStopPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promoterStopPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PromoterStopPointDTO result = promoterStopPointService.update(promoterStopPointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoterStopPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /promoter-stop-points/:id} : Partial updates given fields of an existing promoterStopPoint, field will ignore if it is null
     *
     * @param id the id of the promoterStopPointDTO to save.
     * @param promoterStopPointDTO the promoterStopPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoterStopPointDTO,
     * or with status {@code 400 (Bad Request)} if the promoterStopPointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promoterStopPointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promoterStopPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promoter-stop-points/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PromoterStopPointDTO> partialUpdatePromoterStopPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PromoterStopPointDTO promoterStopPointDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PromoterStopPoint partially : {}, {}", id, promoterStopPointDTO);
        if (promoterStopPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoterStopPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promoterStopPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromoterStopPointDTO> result = promoterStopPointService.partialUpdate(promoterStopPointDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoterStopPointDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /promoter-stop-points} : get all the promoterStopPoints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promoterStopPoints in body.
     */
    @GetMapping("/promoter-stop-points")
    public ResponseEntity<List<PromoterStopPointDTO>> getAllPromoterStopPoints(
        PromoterStopPointCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PromoterStopPoints by criteria: {}", criteria);
        Page<PromoterStopPointDTO> page = promoterStopPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promoter-stop-points/count} : count all the promoterStopPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/promoter-stop-points/count")
    public ResponseEntity<Long> countPromoterStopPoints(PromoterStopPointCriteria criteria) {
        log.debug("REST request to count PromoterStopPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(promoterStopPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /promoter-stop-points/:id} : get the "id" promoterStopPoint.
     *
     * @param id the id of the promoterStopPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promoterStopPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promoter-stop-points/{id}")
    public ResponseEntity<PromoterStopPointDTO> getPromoterStopPoint(@PathVariable Long id) {
        log.debug("REST request to get PromoterStopPoint : {}", id);
        Optional<PromoterStopPointDTO> promoterStopPointDTO = promoterStopPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promoterStopPointDTO);
    }

    /**
     * {@code DELETE  /promoter-stop-points/:id} : delete the "id" promoterStopPoint.
     *
     * @param id the id of the promoterStopPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promoter-stop-points/{id}")
    public ResponseEntity<Void> deletePromoterStopPoint(@PathVariable Long id) {
        log.debug("REST request to delete PromoterStopPoint : {}", id);
        promoterStopPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     *
     */
    @GetMapping("/promoter-stop-points/promoter-itinerary/{id}")
    public ResponseEntity<List<PromoterStopPointDTO>> getPromoterStopPointByItineraryId(@PathVariable Long id) {
        log.debug("REST request to get PromoterStopPoints by PromoterItineraryId : {}", id);
        List<PromoterStopPointDTO> promoterStopPoint = promoterStopPointService.findPromoterStopPointByPromoterItineraryId(id);
        return ResponseEntity.ok().body(promoterStopPoint);
    }
}

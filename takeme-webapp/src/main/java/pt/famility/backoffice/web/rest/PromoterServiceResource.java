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
import pt.famility.backoffice.repository.PromoterServiceRepository;
import pt.famility.backoffice.service.PromoterServiceQueryService;
import pt.famility.backoffice.service.PromoterServiceService;
import pt.famility.backoffice.service.criteria.PromoterServiceCriteria;
import pt.famility.backoffice.service.dto.PromoterServiceDTO;
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

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.PromoterService}.
 */
@RestController
@RequestMapping("/api")
public class PromoterServiceResource {

    private final Logger log = LoggerFactory.getLogger(PromoterServiceResource.class);

    private static final String ENTITY_NAME = "promoterService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromoterServiceService promoterServiceService;

    private final PromoterServiceRepository promoterServiceRepository;

    private final PromoterServiceQueryService promoterServiceQueryService;

    public PromoterServiceResource(
            PromoterServiceService promoterServiceService,
            PromoterServiceRepository promoterServiceRepository,
            PromoterServiceQueryService promoterServiceQueryService
    ) {
        this.promoterServiceService = promoterServiceService;
        this.promoterServiceRepository = promoterServiceRepository;
        this.promoterServiceQueryService = promoterServiceQueryService;
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code POST  /promoter-services} : Create a new promoterService.
     *
     * @param promoterServiceDTO the promoterServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promoterServiceDTO, or with status {@code 400 (Bad Request)} if the promoterService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promoter-services")
    public ResponseEntity<PromoterServiceDTO> createPromoterService(@Valid @RequestBody PromoterServiceDTO promoterServiceDTO) throws URISyntaxException {
        log.debug("REST request to save PromoterService : {}", promoterServiceDTO);
        if (promoterServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new promoterService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PromoterServiceDTO result = promoterServiceService.save(promoterServiceDTO);
        return ResponseEntity.created(new URI("/api/promoter-services/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code PUT  /promoter-services/:id} : Updates an existing promoterService.
     *
     * @param id                 the id of the promoterServiceDTO to save.
     * @param promoterServiceDTO the promoterServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoterServiceDTO,
     * or with status {@code 400 (Bad Request)} if the promoterServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promoterServiceDTO couldn't be updated.
     */
    @PutMapping("/promoter-services/{id}")
    public ResponseEntity<PromoterServiceDTO> updatePromoterService(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody PromoterServiceDTO promoterServiceDTO) {
        log.debug("REST request to update PromoterService : {}, {}", id, promoterServiceDTO);
        if (promoterServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoterServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!promoterServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        PromoterServiceDTO result = promoterServiceService.update(promoterServiceDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoterServiceDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /promoter-services/:id} : Partial updates given fields of an existing promoterService, field will ignore if it is null
     *
     * @param id                 the id of the promoterServiceDTO to save.
     * @param promoterServiceDTO the promoterServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoterServiceDTO,
     * or with status {@code 400 (Bad Request)} if the promoterServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promoterServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promoterServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promoter-services/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<PromoterServiceDTO> partialUpdatePromoterService(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody PromoterServiceDTO promoterServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PromoterService partially : {}, {}", id, promoterServiceDTO);
        if (promoterServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoterServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promoterServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromoterServiceDTO> result = promoterServiceService.partialUpdate(promoterServiceDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoterServiceDTO.getId().toString())
        );
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code GET  /promoter-services} : get all the promoterServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promoterServices in body.
     */
    @GetMapping("/promoter-services")
    public ResponseEntity<List<PromoterServiceDTO>> getAllPromoterServices(PromoterServiceCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get PromoterServices by criteria: {}", criteria);
        Page<PromoterServiceDTO> page = promoterServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promoter-services/count} : count all the promoterServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/promoter-services/count")
    public ResponseEntity<Long> countPromoterServices(PromoterServiceCriteria criteria) {
        log.debug("REST request to count PromoterServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(promoterServiceQueryService.countByCriteria(criteria));
    }

    // TODO: ADD @SECURED AND ACCESS VALIDATOR

    /**
     * {@code GET  /promoter-services/:id} : get the "id" promoterService.
     *
     * @param id the id of the promoterServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promoterServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promoter-services/{id}")
    public ResponseEntity<PromoterServiceDTO> getPromoterService(@PathVariable Long id) {
        log.debug("REST request to get PromoterService : {}", id);
        Optional<PromoterServiceDTO> promoterServiceDTO = promoterServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promoterServiceDTO);
    }

    /**
     * {@code DELETE  /promoter-services/:id} : delete the "id" promoterService.
     *
     * @param id the id of the promoterServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promoter-services/{id}")
    public ResponseEntity<Void> deletePromoterService(@PathVariable Long id) {
        log.debug("REST request to delete PromoterService : {}", id);
        promoterServiceService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}

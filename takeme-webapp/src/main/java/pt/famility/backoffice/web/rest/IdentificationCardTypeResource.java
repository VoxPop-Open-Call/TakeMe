package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.IdentificationCardType;
import pt.famility.backoffice.repository.IdentificationCardTypeRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing IdentificationCardType.
 */
@RestController
@RequestMapping("/api")
public class IdentificationCardTypeResource {

    private final Logger log = LoggerFactory.getLogger(IdentificationCardTypeResource.class);

    private static final String ENTITY_NAME = "identificationCardType";

    private final IdentificationCardTypeRepository identificationCardTypeRepository;

    public IdentificationCardTypeResource(IdentificationCardTypeRepository identificationCardTypeRepository) {
        this.identificationCardTypeRepository = identificationCardTypeRepository;
    }

    /**
     * POST  /identification-card-types : Create a new identificationCardType.
     *
     * @param identificationCardType the identificationCardType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new identificationCardType, or with status 400 (Bad Request) if the identificationCardType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/identification-card-types")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<IdentificationCardType> createIdentificationCardType(@RequestBody IdentificationCardType identificationCardType) throws URISyntaxException {
        log.debug("REST request to save IdentificationCardType : {}", identificationCardType);
        if (identificationCardType.getId() != null) {
            throw new BadRequestAlertException("A new identificationCardType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IdentificationCardType result = identificationCardTypeRepository.save(identificationCardType);
        return ResponseEntity.created(new URI("/api/identification-card-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /identification-card-types : Updates an existing identificationCardType.
     *
     * @param identificationCardType the identificationCardType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated identificationCardType,
     * or with status 400 (Bad Request) if the identificationCardType is not valid,
     * or with status 500 (Internal Server Error) if the identificationCardType couldn't be updated
     */
    @PutMapping("/identification-card-types")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<IdentificationCardType> updateIdentificationCardType(@RequestBody IdentificationCardType identificationCardType) {
        log.debug("REST request to update IdentificationCardType : {}", identificationCardType);
        if (identificationCardType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IdentificationCardType result = identificationCardTypeRepository.save(identificationCardType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, identificationCardType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /identification-card-types : get all the identificationCardTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of identificationCardTypes in body
     */
    @GetMapping("/identification-card-types")

    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public List<IdentificationCardType> getAllIdentificationCardTypes() {
        log.debug("REST request to get all IdentificationCardTypes");
        return identificationCardTypeRepository.findAll();
    }

    /**
     * GET  /identification-card-types/:id : get the "id" identificationCardType.
     *
     * @param id the id of the identificationCardType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the identificationCardType, or with status 404 (Not Found)
     */
    @GetMapping("/identification-card-types/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<IdentificationCardType> getIdentificationCardType(@PathVariable Long id) {
        log.debug("REST request to get IdentificationCardType : {}", id);
        Optional<IdentificationCardType> identificationCardType = identificationCardTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(identificationCardType);
    }

    /**
     * DELETE  /identification-card-types/:id : delete the "id" identificationCardType.
     *
     * @param id the id of the identificationCardType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/identification-card-types/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteIdentificationCardType(@PathVariable Long id) {
        log.debug("REST request to delete IdentificationCardType : {}", id);
        identificationCardTypeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

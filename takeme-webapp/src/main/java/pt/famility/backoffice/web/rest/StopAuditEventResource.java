package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.StopAuditEvent;
import pt.famility.backoffice.repository.StopAuditEventRepository;
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
 * REST controller for managing StopAuditEvent.
 */
@RestController
@RequestMapping("/api")
public class StopAuditEventResource {

    private final Logger log = LoggerFactory.getLogger(StopAuditEventResource.class);

    private static final String ENTITY_NAME = "stopAuditEvent";

    private final StopAuditEventRepository stopAuditEventRepository;

    public StopAuditEventResource(StopAuditEventRepository stopAuditEventRepository) {
        this.stopAuditEventRepository = stopAuditEventRepository;
    }

    /**
     * POST  /stop-audit-events : Create a new stopAuditEvent.
     *
     * @param stopAuditEvent the stopAuditEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stopAuditEvent, or with status 400 (Bad Request) if the stopAuditEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stop-audit-events")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<StopAuditEvent> createStopAuditEvent(@Valid @RequestBody StopAuditEvent stopAuditEvent) throws URISyntaxException {
        log.debug("REST request to save StopAuditEvent : {}", stopAuditEvent);
        if (stopAuditEvent.getId() != null) {
            throw new BadRequestAlertException("A new stopAuditEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StopAuditEvent result = stopAuditEventRepository.save(stopAuditEvent);
        return ResponseEntity.created(new URI("/api/stop-audit-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stop-audit-events : Updates an existing stopAuditEvent.
     *
     * @param stopAuditEvent the stopAuditEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stopAuditEvent,
     * or with status 400 (Bad Request) if the stopAuditEvent is not valid,
     * or with status 500 (Internal Server Error) if the stopAuditEvent couldn't be updated
     */
    @PutMapping("/stop-audit-events")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<StopAuditEvent> updateStopAuditEvent(@Valid @RequestBody StopAuditEvent stopAuditEvent) {
        log.debug("REST request to update StopAuditEvent : {}", stopAuditEvent);
        if (stopAuditEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StopAuditEvent result = stopAuditEventRepository.save(stopAuditEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stopAuditEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stop-audit-events : get all the stopAuditEvents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stopAuditEvents in body
     */
    @GetMapping("/stop-audit-events")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<StopAuditEvent> getAllStopAuditEvents() {
        log.debug("REST request to get all StopAuditEvents");
        return stopAuditEventRepository.findAll();
    }

    /**
     * GET  /stop-audit-events/:id : get the "id" stopAuditEvent.
     *
     * @param id the id of the stopAuditEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stopAuditEvent, or with status 404 (Not Found)
     */
    @GetMapping("/stop-audit-events/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<StopAuditEvent> getStopAuditEvent(@PathVariable Long id) {
        log.debug("REST request to get StopAuditEvent : {}", id);
        Optional<StopAuditEvent> stopAuditEvent = stopAuditEventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stopAuditEvent);
    }

    /**
     * DELETE  /stop-audit-events/:id : delete the "id" stopAuditEvent.
     *
     * @param id the id of the stopAuditEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stop-audit-events/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteStopAuditEvent(@PathVariable Long id) {
        log.debug("REST request to delete StopAuditEvent : {}", id);
        stopAuditEventRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

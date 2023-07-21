package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.StopPoint;
import pt.famility.backoffice.repository.StopPointRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing StopPoint.
 */
@RestController
@RequestMapping("/api")
public class StopPointResource {

    private final Logger log = LoggerFactory.getLogger(StopPointResource.class);

    private static final String ENTITY_NAME = "stopPoint";

    private final StopPointRepository stopPointRepository;

    public StopPointResource(StopPointRepository stopPointRepository) {
        this.stopPointRepository = stopPointRepository;
    }

    /**
     * POST  /stop-points : Create a new stopPoint.
     *
     * @param stopPoint the stopPoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stopPoint, or with status 400 (Bad Request) if the stopPoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stop-points")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<StopPoint> createStopPoint(@Valid @RequestBody StopPoint stopPoint) throws URISyntaxException {
        log.debug("REST request to save StopPoint : {}", stopPoint);
        if (stopPoint.getId() != null) {
            throw new BadRequestAlertException("A new stopPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StopPoint result = stopPointRepository.save(stopPoint);
        return ResponseEntity.created(new URI("/api/stop-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stop-points : Updates an existing stopPoint.
     *
     * @param stopPoint the stopPoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stopPoint,
     * or with status 400 (Bad Request) if the stopPoint is not valid,
     * or with status 500 (Internal Server Error) if the stopPoint couldn't be updated
     */
    @PutMapping("/stop-points")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<StopPoint> updateStopPoint(@Valid @RequestBody StopPoint stopPoint) {
        log.debug("REST request to update StopPoint : {}", stopPoint);
        if (stopPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StopPoint result = stopPointRepository.save(stopPoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stopPoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stop-points : get all the stopPoints.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of stopPoints in body
     */
    @GetMapping("/stop-points")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<StopPoint> getAllStopPoints(@RequestParam(required = false) String filter) {
        if ("stopauditevent-is-null".equals(filter)) {
            log.debug("REST request to get all StopPoints where stopAuditEvent is null");
            return StreamSupport
                .stream(stopPointRepository.findAll().spliterator(), false)
                .filter(stopPoint -> stopPoint.getStopAuditEvents() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all StopPoints");
        return stopPointRepository.findAll();
    }

    /**
     * GET  /stop-points/:id : get the "id" stopPoint.
     *
     * @param id the id of the stopPoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stopPoint, or with status 404 (Not Found)
     */
    @GetMapping("/stop-points/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<StopPoint> getStopPoint(@PathVariable Long id) {
        log.debug("REST request to get StopPoint : {}", id);
        Optional<StopPoint> stopPoint = stopPointRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stopPoint);
    }

    /**
     * DELETE  /stop-points/:id : delete the "id" stopPoint.
     *
     * @param id the id of the stopPoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stop-points/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteStopPoint(@PathVariable Long id) {
        log.debug("REST request to delete StopPoint : {}", id);
        stopPointRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

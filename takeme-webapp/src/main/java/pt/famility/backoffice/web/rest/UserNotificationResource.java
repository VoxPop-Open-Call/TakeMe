package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.UserNotification;
import pt.famility.backoffice.repository.UserNotificationRepository;
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
 * REST controller for managing UserNotification.
 */
@RestController
@RequestMapping("/api")
public class UserNotificationResource {

    private final Logger log = LoggerFactory.getLogger(UserNotificationResource.class);

    private static final String ENTITY_NAME = "userNotification";

    private final UserNotificationRepository userNotificationRepository;

    public UserNotificationResource(UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    /**
     * POST  /user-notifications : Create a new userNotification.
     *
     * @param userNotification the userNotification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userNotification, or with status 400 (Bad Request) if the userNotification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-notifications")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserNotification> createUserNotification(@Valid @RequestBody UserNotification userNotification) throws URISyntaxException {
        log.debug("REST request to save UserNotification : {}", userNotification);
        if (userNotification.getId() != null) {
            throw new BadRequestAlertException("A new userNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserNotification result = userNotificationRepository.save(userNotification);
        return ResponseEntity.created(new URI("/api/user-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-notifications : Updates an existing userNotification.
     *
     * @param userNotification the userNotification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userNotification,
     * or with status 400 (Bad Request) if the userNotification is not valid,
     * or with status 500 (Internal Server Error) if the userNotification couldn't be updated
     */
    @PutMapping("/user-notifications")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserNotification> updateUserNotification(@Valid @RequestBody UserNotification userNotification) {
        log.debug("REST request to update UserNotification : {}", userNotification);
        if (userNotification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserNotification result = userNotificationRepository.save(userNotification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userNotification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-notifications : get all the userNotifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userNotifications in body
     */
    @GetMapping("/user-notifications")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<UserNotification> getAllUserNotifications() {
        log.debug("REST request to get all UserNotifications");
        return userNotificationRepository.findAll();
    }

    /**
     * GET  /user-notifications/:id : get the "id" userNotification.
     *
     * @param id the id of the userNotification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userNotification, or with status 404 (Not Found)
     */
    @GetMapping("/user-notifications/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserNotification> getUserNotification(@PathVariable Long id) {
        log.debug("REST request to get UserNotification : {}", id);
        Optional<UserNotification> userNotification = userNotificationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userNotification);
    }

    /**
     * DELETE  /user-notifications/:id : delete the "id" userNotification.
     *
     * @param id the id of the userNotification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-notifications/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteUserNotification(@PathVariable Long id) {
        log.debug("REST request to delete UserNotification : {}", id);
        userNotificationRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

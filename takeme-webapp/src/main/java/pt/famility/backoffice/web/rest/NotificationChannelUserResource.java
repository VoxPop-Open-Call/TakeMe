package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.NotificationChannelUser;
import pt.famility.backoffice.repository.NotificationChannelUserRepository;
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
 * REST controller for managing NotificationChannelUser.
 */
@RestController
@RequestMapping("/api")
public class NotificationChannelUserResource {

    private final Logger log = LoggerFactory.getLogger(NotificationChannelUserResource.class);

    private static final String ENTITY_NAME = "notificationChannelUser";

    private final NotificationChannelUserRepository notificationChannelUserRepository;

    public NotificationChannelUserResource(NotificationChannelUserRepository notificationChannelUserRepository) {
        this.notificationChannelUserRepository = notificationChannelUserRepository;
    }

    /**
     * POST  /notification-channel-users : Create a new notificationChannelUser.
     *
     * @param notificationChannelUser the notificationChannelUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationChannelUser, or with status 400 (Bad Request) if the notificationChannelUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notification-channel-users")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<NotificationChannelUser> createNotificationChannelUser(@Valid @RequestBody NotificationChannelUser notificationChannelUser) throws URISyntaxException {
        log.debug("REST request to save NotificationChannelUser : {}", notificationChannelUser);
        if (notificationChannelUser.getId() != null) {
            throw new BadRequestAlertException("A new notificationChannelUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationChannelUser result = notificationChannelUserRepository.save(notificationChannelUser);
        return ResponseEntity.created(new URI("/api/notification-channel-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-channel-users : Updates an existing notificationChannelUser.
     *
     * @param notificationChannelUser the notificationChannelUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationChannelUser,
     * or with status 400 (Bad Request) if the notificationChannelUser is not valid,
     * or with status 500 (Internal Server Error) if the notificationChannelUser couldn't be updated
     */
    @PutMapping("/notification-channel-users")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<NotificationChannelUser> updateNotificationChannelUser(@Valid @RequestBody NotificationChannelUser notificationChannelUser) {
        log.debug("REST request to update NotificationChannelUser : {}", notificationChannelUser);
        if (notificationChannelUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificationChannelUser result = notificationChannelUserRepository.save(notificationChannelUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationChannelUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-channel-users : get all the notificationChannelUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notificationChannelUsers in body
     */
    @GetMapping("/notification-channel-users")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<NotificationChannelUser> getAllNotificationChannelUsers() {
        log.debug("REST request to get all NotificationChannelUsers");
        return notificationChannelUserRepository.findAll();
    }

    /**
     * GET  /notification-channel-users/:id : get the "id" notificationChannelUser.
     *
     * @param id the id of the notificationChannelUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationChannelUser, or with status 404 (Not Found)
     */
    @GetMapping("/notification-channel-users/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<NotificationChannelUser> getNotificationChannelUser(@PathVariable Long id) {
        log.debug("REST request to get NotificationChannelUser : {}", id);
        Optional<NotificationChannelUser> notificationChannelUser = notificationChannelUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notificationChannelUser);
    }

    /**
     * DELETE  /notification-channel-users/:id : delete the "id" notificationChannelUser.
     *
     * @param id the id of the notificationChannelUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notification-channel-users/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteNotificationChannelUser(@PathVariable Long id) {
        log.debug("REST request to delete NotificationChannelUser : {}", id);
        notificationChannelUserRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

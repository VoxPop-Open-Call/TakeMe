package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.NotificationChannel;
import pt.famility.backoffice.repository.NotificationChannelRepository;
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
 * REST controller for managing NotificationChannel.
 */
@RestController
@RequestMapping("/api")
public class NotificationChannelResource {

    private final Logger log = LoggerFactory.getLogger(NotificationChannelResource.class);

    private static final String ENTITY_NAME = "notificationChannel";

    private final NotificationChannelRepository notificationChannelRepository;

    public NotificationChannelResource(NotificationChannelRepository notificationChannelRepository) {
        this.notificationChannelRepository = notificationChannelRepository;
    }

    /**
     * POST  /notification-channels : Create a new notificationChannel.
     *
     * @param notificationChannel the notificationChannel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationChannel, or with status 400 (Bad Request) if the notificationChannel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notification-channels")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<NotificationChannel> createNotificationChannel(@RequestBody NotificationChannel notificationChannel) throws URISyntaxException {
        log.debug("REST request to save NotificationChannel : {}", notificationChannel);
        if (notificationChannel.getId() != null) {
            throw new BadRequestAlertException("A new notificationChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationChannel result = notificationChannelRepository.save(notificationChannel);
        return ResponseEntity.created(new URI("/api/notification-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-channels : Updates an existing notificationChannel.
     *
     * @param notificationChannel the notificationChannel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationChannel,
     * or with status 400 (Bad Request) if the notificationChannel is not valid,
     * or with status 500 (Internal Server Error) if the notificationChannel couldn't be updated
     */
    @PutMapping("/notification-channels")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<NotificationChannel> updateNotificationChannel(@RequestBody NotificationChannel notificationChannel) {
        log.debug("REST request to update NotificationChannel : {}", notificationChannel);
        if (notificationChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificationChannel result = notificationChannelRepository.save(notificationChannel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationChannel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-channels : get all the notificationChannels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notificationChannels in body
     */
    @GetMapping("/notification-channels")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<NotificationChannel> getAllNotificationChannels() {
        log.debug("REST request to get all NotificationChannels");
        return notificationChannelRepository.findAll();
    }

    /**
     * GET  /notification-channels/:id : get the "id" notificationChannel.
     *
     * @param id the id of the notificationChannel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationChannel, or with status 404 (Not Found)
     */
    @GetMapping("/notification-channels/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<NotificationChannel> getNotificationChannel(@PathVariable Long id) {
        log.debug("REST request to get NotificationChannel : {}", id);
        Optional<NotificationChannel> notificationChannel = notificationChannelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notificationChannel);
    }

    /**
     * DELETE  /notification-channels/:id : delete the "id" notificationChannel.
     *
     * @param id the id of the notificationChannel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notification-channels/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteNotificationChannel(@PathVariable Long id) {
        log.debug("REST request to delete NotificationChannel : {}", id);
        notificationChannelRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

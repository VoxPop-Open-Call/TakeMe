package pt.famility.backoffice.web.rest;


import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.domain.UserMessagingToken;
import pt.famility.backoffice.repository.UserMessagingTokenRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.UserMessagingTokenService;
import pt.famility.backoffice.service.dto.UserMessagingTokenDTO;
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
 * REST controller for managing UserMessagingToken.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserMessagingTokenResource {

    private final Logger log = LoggerFactory.getLogger(UserMessagingTokenResource.class);

    private static final String ENTITY_NAME = "userMessagingToken";

    private final UserMessagingTokenRepository userMessagingTokenRepository;

    private final UserMessagingTokenService userMessagingTokenService;

    private final AccessValidator accessValidator;

    public UserMessagingTokenResource(UserMessagingTokenRepository userMessagingTokenRepository,
                                      UserMessagingTokenService userMessagingTokenService,
                                      AccessValidator accessValidator) {
        this.userMessagingTokenRepository = userMessagingTokenRepository;
        this.userMessagingTokenService = userMessagingTokenService;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /user-messaging-tokens : Create a new userMessagingToken.
     *
     * @param userMessagingTokenDTO the userMessagingToken to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userMessagingToken, or with status 400 (Bad Request) if the userMessagingToken has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-messaging-tokens")

    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<UserMessagingToken> createUserMessagingToken(@Valid @RequestBody UserMessagingTokenDTO userMessagingTokenDTO) throws URISyntaxException {
        log.debug("REST request to save UserMessagingToken : {}", userMessagingTokenDTO);
        accessValidator.canCreateUserMessagingToken(userMessagingTokenDTO.getUserId());
        UserMessagingToken result = userMessagingTokenService.save(userMessagingTokenDTO);
        return ResponseEntity.created(new URI("/api/user-messaging-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-messaging-tokens : Updates an existing userMessagingToken.
     *
     * @param userMessagingToken the userMessagingToken to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userMessagingToken,
     * or with status 400 (Bad Request) if the userMessagingToken is not valid,
     * or with status 500 (Internal Server Error) if the userMessagingToken couldn't be updated
     */
    @PutMapping("/user-messaging-tokens")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserMessagingToken> updateUserMessagingToken(@Valid @RequestBody UserMessagingToken userMessagingToken) {
        log.debug("REST request to update UserMessagingToken : {}", userMessagingToken);
        if (userMessagingToken.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserMessagingToken result = userMessagingTokenRepository.save(userMessagingToken);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userMessagingToken.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-messaging-tokens : get all the userMessagingTokens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userMessagingTokens in body
     */
    @GetMapping("/user-messaging-tokens")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<UserMessagingToken> getAllUserMessagingTokens() {
        log.debug("REST request to get all UserMessagingTokens");
        return userMessagingTokenRepository.findAll();
    }

    /**
     * GET  /user-messaging-tokens/:id : get the "id" userMessagingToken.
     *
     * @param id the id of the userMessagingToken to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userMessagingToken, or with status 404 (Not Found)
     */
    @GetMapping("/user-messaging-tokens/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserMessagingToken> getUserMessagingToken(@PathVariable Long id) {
        log.debug("REST request to get UserMessagingToken : {}", id);
        Optional<UserMessagingToken> userMessagingToken = userMessagingTokenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userMessagingToken);
    }

    /**
     * DELETE  /user-messaging-tokens/:id : delete the "id" userMessagingToken.
     *
     * @param id the id of the userMessagingToken to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-messaging-tokens/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteUserMessagingToken(@PathVariable Long id) {
        log.debug("REST request to delete UserMessagingToken : {}", id);
        userMessagingTokenRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

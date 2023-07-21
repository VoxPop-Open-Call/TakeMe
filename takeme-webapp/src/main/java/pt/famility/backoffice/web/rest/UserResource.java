package pt.famility.backoffice.web.rest;


import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
import pt.famility.backoffice.config.Constants;
import pt.famility.backoffice.converter.UserConverter;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.repository.UserRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.MailService;
import pt.famility.backoffice.service.UserService;
import pt.famility.backoffice.service.dto.UserDTO;
import pt.famility.backoffice.service.dto.UserPatchDTO;
import pt.famility.backoffice.service.dto.UserPatchProfileDTO;
import pt.famility.backoffice.service.dto.UserUpdateStatusDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.EmailAlreadyUsedException;
import pt.famility.backoffice.web.rest.errors.LoginAlreadyUsedException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import pt.famility.backoffice.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final UserConverter userConverter;

    private final AccessValidator accessValidator;

    public UserResource(
        UserService userService,
        UserRepository userRepository,
        MailService mailService,
        UserConverter userConverter,
        AccessValidator accessValidator
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userConverter = userConverter;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/users")

    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users")

    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<UserDTO>> getAllUsers(
        @RequestParam(required = false) Long organizationId,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) Boolean active,
        Pageable pageable) {
        log.debug("REST request to get users");
        accessValidator.canGetUsers(organizationId);
        Page<UserDTO> page = userService.findWithFilters(organizationId, firstName, lastName, active, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")

    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Secured(AuthoritiesConstants.FAMILITY)
    public List<String> getAuthorities() {
        log.debug("REST resquest to get user authorities.");
        return userService.getAuthorities();
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")

    @Transactional(readOnly = true)
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(userConverter::convert));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")

    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }

    @GetMapping("/users/info")
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<UserDTO> getUserInfo() {
        log.debug("REST request to get user info");
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PatchMapping("/users/{id}/update-status")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody UserUpdateStatusDTO userUpdateStatusDTO) {
        log.debug("REST request to update status of user: {}", id);
        accessValidator.canPerformUserAction(id);
        userService.updateUserStatus(id, userUpdateStatusDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{id}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> patchUser(@PathVariable Long id, @RequestBody UserPatchDTO userPatchDTO) {
        log.debug("REST request to patch user {}", id);
        accessValidator.canPerformUserAction(id);
        userService.patchUser(id, userPatchDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{id}/profile")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> patchUserProfile(@PathVariable Long id, @RequestBody UserPatchProfileDTO userPatchProfileDTO) {
        log.debug("Patching profile of user {}", id);
        userService.patchUserProfile(id, userPatchProfileDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{id}/language")
    @Secured({AuthoritiesConstants.TUTOR})
    public ResponseEntity<Void> changeUserLanguage(@PathVariable Long id, @RequestBody UserPatchDTO userPatchDTO) {
        log.debug("REST request to change language of user {}", id);
        userService.changeUserLanguage(id, userPatchDTO);
        return ResponseEntity.ok().build();
    }
}

package pt.famility.backoffice.web.rest;

import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.service.dto.TermsAndConditionsDTO;
import tech.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.repository.UserRepository;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.security.SecurityUtils;
import pt.famility.backoffice.service.MailService;
import pt.famility.backoffice.service.UserService;
import pt.famility.backoffice.service.dto.PasswordChangeDTO;
import pt.famility.backoffice.service.dto.UserDTO;
import pt.famility.backoffice.service.dto.firebase.FirebaseUserDTO;
import pt.famility.backoffice.service.dto.firebase.FirebaseUserMultipleTypeDTO;
import pt.famility.backoffice.service.dto.firebase.ResetPasswordDTO;
import pt.famility.backoffice.web.rest.errors.EmailAlreadyUsedException;
import pt.famility.backoffice.web.rest.errors.EmailNotFoundException;
import pt.famility.backoffice.web.rest.errors.InternalServerErrorException;
import pt.famility.backoffice.web.rest.errors.InvalidPasswordException;
import pt.famility.backoffice.web.rest.errors.LoginAlreadyUsedException;
import pt.famility.backoffice.web.rest.vm.KeyAndPasswordVM;
import pt.famility.backoffice.web.rest.vm.ManagedUserVM;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;


/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final ApplicationProperties applicationProperties;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService, ApplicationProperties applicationProperties) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /register : register the user.
     *
     * @param firebaseUserDTO the user information
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerAccount(@Valid @RequestBody FirebaseUserDTO firebaseUserDTO) {
        log.debug("REST request to register User : {}", firebaseUserDTO);
        if (!checkPasswordLength(firebaseUserDTO.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(firebaseUserDTO, false);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * POST  /account/create : register the user.
     *
     * @param firebaseUserMultipleTypeDTO the user information
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
     */
    @PostMapping("/account/create")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<User> createAccount(@Valid @RequestBody FirebaseUserMultipleTypeDTO firebaseUserMultipleTypeDTO) {
        log.debug("REST request to create user : {}", firebaseUserMultipleTypeDTO);
        if (!checkPasswordLength(firebaseUserMultipleTypeDTO.getFirebaseUser().getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.createUser(firebaseUserMultipleTypeDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * GET  /account/{uid} : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account/{uid}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<UserDTO> getAccount(@PathVariable String uid) {
        log.debug("REST request to get Account : {}", uid);
        return ResponseUtil.wrapOrNotFound(userService.loadUserByUID(uid).map(UserDTO::new));
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    @Secured(AuthoritiesConstants.ADMIN)
    public void activateAccount(@RequestParam(value = "key") String key) {
        log.debug("REST request to activate account");
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this activation key");
        }
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Secured(AuthoritiesConstants.ADMIN)
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Secured(AuthoritiesConstants.FAMILITY)
    public UserDTO getAccount() {
        log.debug("REST request to get account");
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
     */
    @PostMapping("/account")
    @Secured(AuthoritiesConstants.ADMIN)
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to save account");
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
    }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordChangeDTO current and new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    @Secured(AuthoritiesConstants.USER)
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        log.debug("REST request to change password");
        if (!checkPasswordLength(passwordChangeDTO.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
    }

    /**
     * POST   /account/reset-password : Send an email to reset the password of the user
     *
     * @param resetPasswordDTO the mail of the user
     * @throws EmailNotFoundException 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/account/reset-password")
    public void requestPasswordReset(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.debug("REST request to reset password of user with mail {}", resetPasswordDTO.getEmail());
        userService.resetFirebasePassword(resetPasswordDTO);
    }

    /**
     * POST   /account/reset-password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws RuntimeException 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset-password/finish")
    @Secured(AuthoritiesConstants.ADMIN)
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        log.debug("REST request to finish password reset");
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    @GetMapping("/terms-and-conditions")
    public ResponseEntity<TermsAndConditionsDTO> getTermsAndConditionsURL() {
        log.debug("REST request to get terms and conditions URL");
        TermsAndConditionsDTO termsAndConditions = new TermsAndConditionsDTO();
        termsAndConditions.setTermsAndConditionsURL(applicationProperties.getRegistration().getTermsAndConditionsURL());
        return ResponseEntity.ok().body(termsAndConditions);
    }
}

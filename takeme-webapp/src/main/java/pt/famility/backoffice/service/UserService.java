package pt.famility.backoffice.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.config.Constants;
import pt.famility.backoffice.config.firebase.FirebaseAuthenticationToken;
import pt.famility.backoffice.converter.FirebaseErrorConverter;
import pt.famility.backoffice.converter.UserConverter;
import pt.famility.backoffice.domain.Authority;
import pt.famility.backoffice.domain.Driver;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.domain.enumeration.UserType;
import pt.famility.backoffice.repository.AuthorityRepository;
import pt.famility.backoffice.repository.DriverRepository;
import pt.famility.backoffice.repository.TutorRepository;
import pt.famility.backoffice.repository.UserRepository;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.security.SecurityUtils;
import pt.famility.backoffice.service.dto.AuthenticationDTO;
import pt.famility.backoffice.service.dto.UserDTO;
import pt.famility.backoffice.service.dto.UserPatchDTO;
import pt.famility.backoffice.service.dto.UserPatchProfileDTO;
import pt.famility.backoffice.service.dto.UserUpdateStatusDTO;
import pt.famility.backoffice.service.dto.firebase.FirebaseUserDTO;
import pt.famility.backoffice.service.dto.firebase.FirebaseUserMultipleTypeDTO;
import pt.famility.backoffice.service.dto.firebase.ResetPasswordDTO;
import pt.famility.backoffice.service.util.RandomUtil;
import pt.famility.backoffice.web.rest.errors.EmailAlreadyUsedException;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;
import pt.famility.backoffice.web.rest.errors.InvalidPasswordException;
import pt.famility.backoffice.web.rest.errors.InvalidUIDException;
import pt.famility.backoffice.web.rest.errors.LoginAlreadyUsedException;
import pt.famility.backoffice.web.rest.errors.NotFoundAlertException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    public static final Long ID_ORGANIZATION_TUTOR = 2l;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private FirebaseService firebaseService;

    private UserConverter userConverter;

    private TutorRepository tutorRepository;

    private DriverRepository driverRepository;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        FirebaseService firebaseService,
        UserConverter userConverter,
        TutorRepository tutorRepository,
        DriverRepository driverRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.firebaseService = firebaseService;
        this.userConverter = userConverter;
        this.tutorRepository = tutorRepository;
        this.driverRepository = driverRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }


    /**
     * Register user in firebase and famility db
     * @param firebaseUserDTO
     * @return
     * @throws FirebaseAuthException
     */
    public User registerUser(FirebaseUserDTO firebaseUserDTO, boolean authoritiesApplied) {

        firebaseUserDTO.setLogin(firebaseUserDTO.getEmail());

        if (!authoritiesApplied) {
            firebaseUserDTO.setAuthorities(applyAuthoritiesByType(firebaseUserDTO.getType()));
        }

        UserRecord firebaseUser;
        try {
            firebaseUser = firebaseService.registerFirebaseUser(firebaseUserDTO);
        } catch (FirebaseAuthException e) {
            throw FirebaseErrorConverter.convert(e.getErrorCode());
        }
        firebaseUserDTO.setUid(firebaseUser.getUid());

        User familityUser = null;

        try {
            familityUser = registerFamilityUser(firebaseUserDTO);
            firebaseService.sendEmailVerification(firebaseUserDTO);
        } catch (LoginAlreadyUsedException | EmailAlreadyUsedException e) {
            log.error(e.getMessage());
            try {
                firebaseService.deleteFirebaseUser(firebaseUser.getUid());
            } catch (FirebaseAuthException e1) {
                throw new InvalidUIDException("registerUser");
            }
        }

        return familityUser;
    }

    /**
     * Create user with multiple profiles
     *
     * @param firebaseUserMultipleTypeDTO
     * @return
     * @throws FirebaseAuthException
     */
    public User createUser(FirebaseUserMultipleTypeDTO firebaseUserMultipleTypeDTO) {
        FirebaseUserDTO firebaseUserDTO = firebaseUserMultipleTypeDTO.getFirebaseUser();

        Set<String> authorities = new HashSet<>();
        for (UserType userType: firebaseUserMultipleTypeDTO.getTypes()) {
            authorities.addAll(applyAuthoritiesByType(userType));
        }

        firebaseUserDTO.setAuthorities(authorities);

        User user = registerUser(firebaseUserDTO, true);

        if (Optional.ofNullable(firebaseUserDTO.getDriverId()).isPresent()) {
            Driver driver = driverRepository.findById(firebaseUserDTO.getDriverId())
                .orElseThrow(() -> new NotFoundAlertException("Driver does not exist", "driver", "id"));

            driver.setUserId(user);

            driverRepository.save(driver);
        }

        return user;
    }

    private Set<String> applyAuthoritiesByType(UserType type) {
        Set<String> authorities = new HashSet<>();
        switch (type) {
            case TUTOR:
                authorities.add(AuthoritiesConstants.USER);
                authorities.add(AuthoritiesConstants.TUTOR);
                break;
            case FAMILITY:
                authorities.add(AuthoritiesConstants.USER);
                authorities.add(AuthoritiesConstants.FAMILITY_ADMIN);
                authorities.add(AuthoritiesConstants.FAMILITY);
                break;
            case BUS_COMPANY:
                authorities.add(AuthoritiesConstants.USER);
                authorities.add(AuthoritiesConstants.BUS_COMPANY);
                break;
            case BUS_DRIVER:
                authorities.add(AuthoritiesConstants.USER);
                authorities.add(AuthoritiesConstants.BUS_COMPANY_DRIVER);
                break;
        }

        return authorities;
    }



    public User registerFamilityUser(FirebaseUserDTO firebaseUserDTO) {
        userRepository.findOneByLogin(firebaseUserDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(firebaseUserDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(firebaseUserDTO.getPassword());
        newUser.setLogin(firebaseUserDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setFirstName(firebaseUserDTO.getFirstName());
        newUser.setLastName(firebaseUserDTO.getLastName());
        newUser.setEmail(firebaseUserDTO.getEmail().toLowerCase());
        newUser.setImageUrl(firebaseUserDTO.getImageUrl());
        if (firebaseUserDTO.getLangKey() != null) {
            newUser.setLangKey(firebaseUserDTO.getLangKey());
        } else {
            newUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        firebaseUserDTO.getAuthorities().forEach(role -> authorityRepository.findById(role).ifPresent(authorities::add));
        newUser.setAuthorities(authorities);
        newUser.setUid(firebaseUserDTO.getUid());
        newUser.setOrganizationId(UserType.TUTOR.equals(firebaseUserDTO.getType()) ? ID_ORGANIZATION_TUTOR : firebaseUserDTO.getOrganizationId());
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser){
        if (existingUser.getActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        user.setUid(userDTO.getUid()); //Created by Firebase
        user.setOrganizationId(userDTO.getOrganizationId());
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {

        //Verify if current password is Ok
        firebaseService.getLoginDTO(new AuthenticationDTO(getLoggedUser().getEmail(), currentClearTextPassword, true));

        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(getLoggedUser().getUid())
            .setPassword(newPassword);

        try {
            FirebaseAuth.getInstance().updateUser(request);
        } catch (FirebaseAuthException e) {
            throw new InvalidPasswordException();
        }

    }

    public void resetFirebasePassword(ResetPasswordDTO resetPasswordDTO) {
        firebaseService.resetPassword(resetPasswordDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

    @Transactional(readOnly = true)
    public Optional<User> loadUserByUID(String uid) {
        return userRepository.findOneWithAuthoritiesByUid(uid);
    }

    @Transactional(readOnly = true)
    public Optional<User> loadUserById(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> loadUserByEmail(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    public UserDTO getUserInfo() {
        return userConverter.convert(getLoggedUser());
    }

    private User getLoggedUser() {
        FirebaseAuthenticationToken authentication = (FirebaseAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public UserDTO convert(User user) {
        return userConverter.convert(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findWithFilters(Long organizationId, String firstName, String lastName, Boolean active, Pageable pageable) {

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("firstName", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase())
                .withMatcher("lastName", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase())
                .withIgnorePaths("createdDate", "lastModifiedDate");

        if (Optional.ofNullable(active).isPresent()) {
            user.setActivated(active);
        } else {
            matcher = matcher.withIgnorePaths("activated");
        }
        if (Optional.ofNullable(organizationId).isPresent()) {
            user.setOrganizationId(organizationId);
            user.setUid("");
            matcher = matcher.withMatcher("uid", ExampleMatcher.GenericPropertyMatcher::contains);
        }

        return userRepository.findAll(Example.of(user, matcher), pageable).map(userConverter::convert);
    }

    @Transactional
    public void updateUserStatus(Long id, UserUpdateStatusDTO userUpdateStatusDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidIDException("userId"));
        user.setActivated(userUpdateStatusDTO.isActivated());
        user.setStatusChangeDate(Instant.now());
        userRepository.save(user);

        tutorRepository.findByUserId(id).ifPresent(tutor -> {
            if (userUpdateStatusDTO.isActivated()) {
                tutor.setStatusType(StatusType.ACTIVE);
            } else {
                tutor.setStatusType(StatusType.INACTIVE);
            }
            tutorRepository.save(tutor);
        });

        Optional.ofNullable(user.getUid()).ifPresent(uid -> {
            try {
                firebaseService.disableUser(uid, !userUpdateStatusDTO.isActivated());
            } catch (FirebaseAuthException e) {
                throw FirebaseErrorConverter.convert(e.getErrorCode());
            }
        });
    }

    public void patchUser(Long id, UserPatchDTO userPatchDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidIDException("user not found"));

        Optional.ofNullable(userPatchDTO.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(userPatchDTO.getLastName()).ifPresent(user::setLastName);

        user = userRepository.save(user);

        Tutor tutor = tutorRepository.findByUserId(user.getId()).orElseThrow(() -> new InvalidIDException("tutor not found"));

        tutor.setName(user.getFirstName() + " " + user.getLastName());

        tutorRepository.save(tutor);
    }

    public void patchUserProfile(Long id, UserPatchProfileDTO userPatchProfileDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidIDException("user not found"));

        Set<String> authoritiesString = new HashSet<>();
        for (UserType userType: userPatchProfileDTO.getTypes()) {
            authoritiesString.addAll(applyAuthoritiesByType(userType));
        }

        Set<Authority> authorities = new HashSet<>();
        authoritiesString.forEach(role -> authorityRepository.findById(role).ifPresent(authorities::add));

        user.setAuthorities(authorities);

        user = userRepository.save(user);
        this.clearUserCaches(user);

        driverRepository.findByUserId_Id(user.getId()).ifPresent(
            oldDriver -> {
                oldDriver.setUserId(null);
                driverRepository.save(oldDriver);
            }
        );

        if (Optional.ofNullable(userPatchProfileDTO.getDriverId()).isPresent()) {
            Driver driver = driverRepository.findById(userPatchProfileDTO.getDriverId()).orElseThrow(() -> new InvalidIDException("Driver not found"));

            driver.setUserId(user);
            driverRepository.save(driver);
        }

    }

    @Transactional
    public void updateAllUsersByOrganization(Long organizationId, StatusType statusType) {
        List<User> users = userRepository.findAllByOrganizationId(organizationId);
        users.forEach(user -> {
            UserUpdateStatusDTO userUpdateStatusDTO = new UserUpdateStatusDTO();
            userUpdateStatusDTO.setActivated(StatusType.ACTIVE.equals(statusType));
            updateUserStatus(user.getId(), userUpdateStatusDTO);
        } );
    }

    public void updateNameFirebase(String uuid, String displayName) {
        try {
            firebaseService.updateName(uuid, displayName);
        } catch (FirebaseAuthException e) {
            throw FirebaseErrorConverter.convert(e.getErrorCode());
        }
    }

    public void changeUserLanguage(Long id, UserPatchDTO userPatchDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidIDException("user not found"));
        user.setLangKey(userPatchDTO.getLangKey());
        userRepository.save(user);
    }
}

package pt.famility.backoffice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.config.firebase.FirebaseTokenHolder;
import pt.famility.backoffice.service.dto.AuthenticationDTO;
import pt.famility.backoffice.service.dto.LoginDTO;
import pt.famility.backoffice.service.dto.firebase.FirebaseErrorDTO;
import pt.famility.backoffice.service.dto.firebase.FirebaseUserDTO;
import pt.famility.backoffice.service.dto.firebase.ResetPasswordDTO;
import pt.famility.backoffice.service.dto.firebase.SendEmailDTO;
import pt.famility.backoffice.service.shared.FirebaseParser;
import pt.famility.backoffice.web.rest.errors.EmailNotFoundException;
import pt.famility.backoffice.web.rest.errors.InternalServerErrorException;
import pt.famility.backoffice.web.rest.errors.InvalidPasswordException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.firebase.auth.UserRecord.CreateRequest;

@Service
public class FirebaseService {

    private final Logger log = LoggerFactory.getLogger(FirebaseService.class);

    private static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    private static final String MISSING_PASSWORD = "MISSING_PASSWORD";
    private static final String MISSING_EMAIL = "MISSING_EMAIL";
    private static final String EMAIL_NOT_FOUND = "EMAIL_NOT_FOUND";

    private static final String VERIFY_EMAIL = "VERIFY_EMAIL";

    private ApplicationProperties applicationProperties;

    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    public FirebaseService(ApplicationProperties applicationProperties, RestTemplateBuilder restTemplateBuilder) {
        this.applicationProperties = applicationProperties;
        this.restTemplateBuilder = restTemplateBuilder;
        restTemplate = this.restTemplateBuilder.build();
    }

    public FirebaseTokenHolder parseToken(String firebaseToken) throws InterruptedException, ExecutionException, FirebaseException {
        return new FirebaseParser().parseToken(firebaseToken);
    }

    /**
     * Register an user in Firebase
     *
     * @param firebaseUserDTO
     * @return
     * @throws FirebaseAuthException
     */
    public UserRecord registerFirebaseUser(FirebaseUserDTO firebaseUserDTO) throws FirebaseAuthException {
        CreateRequest request = new CreateRequest()
            .setEmail(firebaseUserDTO.getEmail())
            .setEmailVerified(false)
            .setPassword(firebaseUserDTO.getPassword())
            .setDisplayName(String.format("%s %s", firebaseUserDTO.getFirstName(), firebaseUserDTO.getLastName()))
            .setPhotoUrl("http://google.com") //TODO definir url
            .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        log.debug("Created Information for UserRecord: {}", userRecord.getEmail());

        return userRecord;
    }

    /**
     * Send email to account verification
     *
     * @param firebaseUserDTO
     */
    public void sendEmailVerification(FirebaseUserDTO firebaseUserDTO) {
        LoginDTO loginDTO = getLoginDTO(new AuthenticationDTO(firebaseUserDTO.getEmail(), firebaseUserDTO.getPassword(), true));
        SendEmailDTO sendEmailDTO = new SendEmailDTO(VERIFY_EMAIL, loginDTO.getIdToken());
        ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity(applicationProperties.getFirebase().getVerifyEmailUrl() + applicationProperties.getFirebase().getAppKey(), sendEmailDTO, Map.class);
        log.debug("Send verification email: {}", mapResponseEntity.getBody().get("email"));
    }

    /**
     * Delete an user in firebase
     *
     * @param uid
     * @throws FirebaseAuthException
     */
    public void deleteFirebaseUser(String uid) throws FirebaseAuthException {
        FirebaseAuth.getInstance().deleteUser(uid);
        log.debug("Firebase User deleted: {}", uid);
    }

    public void disableUser(String uid, boolean disabled) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid).setDisabled(disabled);
        FirebaseAuth.getInstance().updateUser(request);
    }

    public UserRecord getFirebaseUser(String uid) throws FirebaseAuthException {
        log.debug("Get firebase user: {}", uid);
        return FirebaseAuth.getInstance().getUser(uid);
    }

    /**
     * Get login information from Firebase
     *
     * @param authenticationDTO
     * @return
     * @throws InvalidPasswordException
     * @throws EmailNotFoundException
     * @throws InternalServerErrorException
     */
    public LoginDTO getLoginDTO(AuthenticationDTO authenticationDTO) throws InvalidPasswordException, EmailNotFoundException, InternalServerErrorException {

        log.debug("Get Firebase Token for: {}", authenticationDTO.getEmail());

        authenticationDTO.setReturnSecureToken(true);

        ResponseEntity<LoginDTO> mapResponseEntity = null;
        try {
            mapResponseEntity = restTemplate.postForEntity(applicationProperties.getFirebase().getTokenUrl() + applicationProperties.getFirebase().getAppKey(), authenticationDTO, LoginDTO.class);
        } catch (HttpClientErrorException e) {
            throwsExceptionByError(e);
        }
        return mapResponseEntity.getBody();

    }

    private void throwsExceptionByError(HttpClientErrorException e) throws InvalidPasswordException, EmailNotFoundException, InternalServerErrorException {
        ObjectMapper mapper = new ObjectMapper();
        FirebaseErrorDTO firebaseErrorDTO = null;
        try {
            firebaseErrorDTO = mapper.readValue(e.getResponseBodyAsString(), FirebaseErrorDTO.class);
        } catch (IOException e1) {
            log.error("Internal Server error: {}", e1.getMessage());
            throw new InternalServerErrorException(firebaseErrorDTO.getError().getMessage());
        }
        log.error("Bad Request: {}", firebaseErrorDTO.getError().getMessage());

        switch (firebaseErrorDTO.getError().getMessage()) {

            case MISSING_PASSWORD:
            case INVALID_PASSWORD:
                throw new InvalidPasswordException();

            case MISSING_EMAIL:
            case EMAIL_NOT_FOUND:
                throw new EmailNotFoundException();

            default:
                throw new InternalServerErrorException(firebaseErrorDTO.getError().getMessage());

        }
    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity(applicationProperties.getFirebase().getResetPasswordEmailUrl() + applicationProperties.getFirebase().getAppKey(), resetPasswordDTO, Map.class);
        log.debug("Send reset password email: {}", mapResponseEntity.getBody().get("email"));
    }

    public void updateName(String uid, String displaName) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid).setDisplayName(displaName);
        FirebaseAuth.getInstance().updateUser(request);
    }
}

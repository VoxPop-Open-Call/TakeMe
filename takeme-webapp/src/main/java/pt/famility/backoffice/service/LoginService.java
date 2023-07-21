package pt.famility.backoffice.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Service;
import pt.famility.backoffice.converter.FirebaseErrorConverter;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.repository.CustomAuditEventRepository;
import pt.famility.backoffice.service.dto.AuthenticationDTO;
import pt.famility.backoffice.service.dto.LoginDTO;
import pt.famility.backoffice.service.dto.UserDTO;
import pt.famility.backoffice.web.rest.errors.CustomParameterizedException;
import pt.famility.backoffice.web.rest.errors.InvalidEmailException;

import java.util.Optional;

@Service
public class LoginService {

    private final Logger log = LoggerFactory.getLogger(LoginService.class);

    public static final String USER_EMAIL_NOT_VERIFIED = "USER_EMAIL_NOT_VERIFIED";
    public static final String AUTHENTICATION_SUCCESS = "AUTHENTICATION_SUCCESS";

    private CustomAuditEventRepository customAuditEventRepository;

    private FirebaseService firebaseService;

    private UserService userService;

    public LoginService(CustomAuditEventRepository customAuditEventRepository, FirebaseService firebaseService, UserService userService) {
        this.customAuditEventRepository = customAuditEventRepository;
        this.firebaseService = firebaseService;
        this.userService = userService;
    }

    /**
     * Login in firebase and get user data in famility database
     * @param authenticationDTO
     * @return
      */
    public LoginDTO login(AuthenticationDTO authenticationDTO) {
        LoginDTO loginDTO =  firebaseService.getLoginDTO(authenticationDTO);
        UserRecord firebaseUser = null;
        try {
            firebaseUser = firebaseService.getFirebaseUser(loginDTO.getLocalId());
        } catch (FirebaseAuthException e) {
            throw FirebaseErrorConverter.convert(e.getErrorCode());
        }
        if (firebaseUser.isEmailVerified()) {
            Optional<User> optionalUser = userService.loadUserByEmail(authenticationDTO.getEmail());
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO(user);
            if (!user.getActivated()) {
                userDTO.setActivated(true);
                userService.updateUser(userDTO);
            }            
            loginDTO.setUser(userDTO);
            customAuditEventRepository.add(new AuditEvent(userDTO.getLogin(), AUTHENTICATION_SUCCESS));

        } else {
            throw new InvalidEmailException();
        }
        return loginDTO;
    }
}

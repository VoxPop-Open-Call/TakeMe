package pt.famility.backoffice.converter;


import org.zalando.problem.AbstractThrowableProblem;
import pt.famility.backoffice.web.rest.errors.CustomParameterizedException;
import pt.famility.backoffice.web.rest.errors.EmailAlreadyUsedException;
import pt.famility.backoffice.web.rest.errors.InvalidEmailException;
import pt.famility.backoffice.web.rest.errors.InvalidPasswordException;

/**
 * Firebase Admin SDK Error to Client SDK Error
 */
public class FirebaseErrorConverter {

    public static AbstractThrowableProblem convert(String adminSDKError) {
        switch (adminSDKError) {
            case "email-already-exists":
                return new EmailAlreadyUsedException();
            case "invalid-email":
               return new InvalidEmailException();
            case "invalid-password":
                return new InvalidPasswordException();
            default:
                return new CustomParameterizedException(adminSDKError);
        }
    }

}

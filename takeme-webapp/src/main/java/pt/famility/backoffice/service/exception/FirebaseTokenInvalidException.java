package pt.famility.backoffice.service.exception;

import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.ErrorConstants;

import java.net.URI;

public class FirebaseTokenInvalidException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public FirebaseTokenInvalidException() {
        super(URI.create(ErrorConstants.PROBLEM_BASE_URL + ErrorConstants.INVALID_TOKEN), "Invalid token", "firebase_filter", ErrorConstants.INVALID_TOKEN);
    }
}

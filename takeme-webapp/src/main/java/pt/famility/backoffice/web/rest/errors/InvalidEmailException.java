package pt.famility.backoffice.web.rest.errors;

import java.net.URI;

public class InvalidEmailException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidEmailException() {
        super(URI.create(ErrorConstants.PROBLEM_BASE_URL + ErrorConstants.INVALID_EMAIL), "Invalid Email", "userRegistration", ErrorConstants.INVALID_EMAIL);
    }
}

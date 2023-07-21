package pt.famility.backoffice.web.rest.errors;

import java.net.URI;

public class InvalidUIDException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidUIDException(String entityName) {
        super(URI.create(ErrorConstants.PROBLEM_BASE_URL + ErrorConstants.INVALID_UID), "Invalid UID", entityName, ErrorConstants.INVALID_UID);
    }
}

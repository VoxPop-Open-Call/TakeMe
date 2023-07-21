package pt.famility.backoffice.web.rest.errors;

import java.net.URI;

public class InvalidIDException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidIDException(String entityName) {
        super(URI.create(ErrorConstants.PROBLEM_BASE_URL + ErrorConstants.INVALID_ID), "Invalid ID", entityName, ErrorConstants.INVALID_ID);
    }
}

package pt.famility.backoffice.web.rest.errors;

import java.net.URI;

public class InvalidIdentificationCardTypeException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidIdentificationCardTypeException(String entityName) {
        super(URI.create(ErrorConstants.PROBLEM_BASE_URL + ErrorConstants.INVALID_IDENTIFICATION_CARD_TYPE), "Invalid Identification Card Type", entityName, ErrorConstants.INVALID_IDENTIFICATION_CARD_TYPE);
    }
}

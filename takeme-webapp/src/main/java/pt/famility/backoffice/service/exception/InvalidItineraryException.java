package pt.famility.backoffice.service.exception;

import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.ErrorConstants;

public class InvalidItineraryException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidItineraryException() {
        super(ErrorConstants.INVALID_ROUTE_TYPE, "Invalid route", "directions", ErrorConstants.INVALID_ROUTE);
    }
}

package pt.famility.backoffice.web.rest.errors;

public class EmailNotFoundException extends NotFoundAlertException {

    private static final long serialVersionUID = 1L;

    public EmailNotFoundException() {
        super(ErrorConstants.EMAIL_NOT_FOUND_TYPE, "Email address not registered", "userManagement", ErrorConstants.EMAIL_NOT_FOUND);
    }
}

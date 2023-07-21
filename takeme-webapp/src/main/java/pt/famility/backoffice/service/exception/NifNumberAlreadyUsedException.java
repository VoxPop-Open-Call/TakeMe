package pt.famility.backoffice.service.exception;

import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.ErrorConstants;

public class NifNumberAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NifNumberAlreadyUsedException() {
        super(ErrorConstants.NIF_ALREADY_USED_TYPE, "Nif already used", null, "nif-already-used");
    }

}

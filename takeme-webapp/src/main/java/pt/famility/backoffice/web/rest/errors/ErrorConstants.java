package pt.famility.backoffice.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String EMAIL_NOT_FOUND = "email-not-found";
    public static final String EMAIL_ALREADY_IN_USE = "auth/email-already-in-use";
    public static final String INVALID_EMAIL = "auth/invalid-email";
    public static final String OPERATION_NOT_ALLOWED = "auth/operation-not-allowed";
    public static final String WEAK_PASSWORD = "auth/weak-password";
    public static final String INVALID_PASSWORD = "invalid-password";
    public static final String INVALID_ID = "invalid-id";
    public static final String INVALID_UID = "invalid-uid";
    public static final String INVALID_TOKEN = "invalid-token";
    public static final String INVALID_IDENTIFICATION_CARD_TYPE = "invalid-identificationCardType";
    public static final String INVALID_ROUTE = "invalid-route";

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI PARAMETERIZED_TYPE = URI.create(PROBLEM_BASE_URL + "/parameterized");
    public static final URI ENTITY_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI EMAIL_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/" + EMAIL_NOT_FOUND);
    public static final URI INVALID_ROUTE_TYPE = URI.create(PROBLEM_BASE_URL + "/" + INVALID_ROUTE);
    public static final URI NIF_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/nif-already-used");

    private ErrorConstants() {
    }
}

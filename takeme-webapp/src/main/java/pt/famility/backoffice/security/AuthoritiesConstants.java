package pt.famility.backoffice.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String FAMILITY_ADMIN = "ROLE_FAMILITY_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String FAMILITY = "ROLE_FAMILITY";

    public static final String BUS_COMPANY = "ROLE_BUS_COMPANY";

    public static final String TUTOR = "ROLE_TUTOR";

    public static final String BUS_COMPANY_DRIVER = "ROLE_BUS_COMPANY_DRIVER";

    private AuthoritiesConstants() {
    }
}

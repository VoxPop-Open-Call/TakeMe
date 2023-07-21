package pt.famility.backoffice.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[+_.@A-Za-z0-9-]*$";

    // Time of day in seconds
    public static final int DAY_IN_SECONDS = 60 * 60 * 24;

    // Page and size default
    public static final int PAGE = 0;
    public static final int PAGE_SIZE = 20;

    public static final String SYSTEM = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    private Constants() {
    }
}

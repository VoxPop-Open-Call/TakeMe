package pt.famility.backoffice.service.problems;

public enum ProblemContextEnum {

    AUTHORIZATIONS_IMPORT_ERROR("entity/authorization-import-error", "Authorizations import error", "Message here");
    // Add new error messages here.

    private final String key;
    private final String title;
    private final String detail;

    ProblemContextEnum(String key, String title, String detail) {
        this.key = key;
        this.title = title;
        this.detail = detail;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

}

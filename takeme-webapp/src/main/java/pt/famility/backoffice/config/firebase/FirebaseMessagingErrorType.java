package pt.famility.backoffice.config.firebase;

import lombok.Getter;

public enum FirebaseMessagingErrorType {

    REGISTRATION_TOKEN_NOT_REGISTERED("registration-token-not-registered");

    /**
     * See https://firebase.google.com/docs/cloud-messaging/admin/errors
     */
    @Getter
    private String errorKey;

    FirebaseMessagingErrorType(String errorKey) {
        this.errorKey = errorKey;
    }
}

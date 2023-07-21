package pt.famility.backoffice.service.exception;

public class FirebaseTokenException extends RuntimeException {

    String message;

    public FirebaseTokenException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

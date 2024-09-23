package main.java.exception;

public class LaborValidationException extends RuntimeException {

    public LaborValidationException(String message) {
        super(message);
    }

    public LaborValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

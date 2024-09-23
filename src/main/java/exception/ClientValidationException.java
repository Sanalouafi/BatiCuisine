package main.java.exception;

public class ClientValidationException extends RuntimeException {

    public ClientValidationException(String message) {

        super(message);
    }

    public ClientValidationException(String message, Throwable cause) {

        super(message, cause);
    }
}

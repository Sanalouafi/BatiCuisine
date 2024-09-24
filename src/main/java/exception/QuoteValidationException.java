package main.java.exception;

public class QuoteValidationException extends RuntimeException {

    public QuoteValidationException(String message) {
        super(message);
    }

    public QuoteValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

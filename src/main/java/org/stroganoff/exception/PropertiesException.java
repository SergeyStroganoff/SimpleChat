package org.stroganoff.exception;

public class PropertiesException extends Exception {

    public PropertiesException(String message) {
        super(message);
    }

    public PropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}

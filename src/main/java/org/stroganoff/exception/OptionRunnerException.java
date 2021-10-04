package org.stroganoff.exception;

public class OptionRunnerException extends Exception {
    public OptionRunnerException(String message) {
        super(message);
    }

    public OptionRunnerException(String message, Throwable cause) {
        super(message, cause);
    }
}

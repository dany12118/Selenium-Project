package com.example.framework.exceptions;

/**
 * Exception thrown when wait conditions are not met or timeout occurs
 */
public class WaitException extends FrameworkException {

    public WaitException(String message) {
        super(message);
    }

    public WaitException(String message, Throwable cause) {
        super(message, cause);
    }

    public WaitException(Throwable cause) {
        super(cause);
    }
}

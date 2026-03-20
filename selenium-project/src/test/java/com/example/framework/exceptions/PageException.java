package com.example.framework.exceptions;

/**
 * Exception thrown when page object operations fail
 * Examples: Element not found, unable to interact with element
 */
public class PageException extends FrameworkException {

    public PageException(String message) {
        super(message);
    }

    public PageException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageException(Throwable cause) {
        super(cause);
    }
}

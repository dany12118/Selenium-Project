package com.example.framework.exceptions;

/**
 * Exception thrown when configuration/properties loading fails
 */
public class ConfigException extends FrameworkException {

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}

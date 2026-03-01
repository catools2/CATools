package org.catools.mcp.exceptions;

/**
 * Signals that attempt to convert provided value to json or backward failed.
 */
public class InvalidJsonFormatException extends RuntimeException {
    public InvalidJsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

package org.catools.mcp.exceptions;

/** Signals that attempt to convert provided value to json or backward failed. */
public class CInvalidJsonFormatException extends RuntimeException {
  public CInvalidJsonFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}

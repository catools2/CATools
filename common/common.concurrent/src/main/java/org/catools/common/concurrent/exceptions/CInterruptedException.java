package org.catools.common.concurrent.exceptions;

/**
 * Custom exception class for handling interrupted exceptions in concurrent operations.
 *
 * <p>This class extends {@link RuntimeException} and is used to wrap interrupted exceptions with a
 * custom message and cause.
 */
public class CInterruptedException extends RuntimeException {

  /**
   * Constructs a new {@code CInterruptedException} with the specified detail message and cause.
   *
   * @param message the detail message explaining the reason for the exception
   * @param cause the underlying cause of the exception
   */
  public CInterruptedException(String message, Throwable cause) {
    super(message, cause);
  }
}

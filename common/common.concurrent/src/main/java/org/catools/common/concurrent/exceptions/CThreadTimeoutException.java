package org.catools.common.concurrent.exceptions;

/**
 * Exception thrown when a thread execution exceeds the expected time limit and is terminated.
 *
 * <p>This class extends {@link RuntimeException} and is used to signal that a thread has timed out
 * while performing a job.
 */
public class CThreadTimeoutException extends RuntimeException {

  /**
   * Constructs a new {@code CThreadTimeoutException} with the specified description.
   *
   * @param description the detail message explaining the reason for the timeout
   */
  public CThreadTimeoutException(String description) {
    super(description);
  }
}

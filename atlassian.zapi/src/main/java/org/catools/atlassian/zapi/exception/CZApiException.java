package org.catools.atlassian.zapi.exception;

import org.catools.common.exception.CRuntimeException;

/**
 * Custom exception for ZAPI-related errors.
 *
 * <p>This exception is used to indicate issues encountered while interacting
 * with the ZAPI system. It extends {@link CRuntimeException} to provide additional
 * context about the error.</p>
 */
public class CZApiException extends CRuntimeException {

  /**
   * Constructs a new {@code CZApiException} with the specified detail message
   * and cause.
   *
   * @param message the detail message explaining the reason for the exception
   * @param t the cause of the exception (can be {@code null})
   */
  public CZApiException(String message, Throwable t) {
    super(message, t);
  }
}

package org.catools.atlassian.zapi.exception;

import org.catools.common.exception.CRuntimeException;

/**
 * Custom exception for ZAPI client errors.
 *
 * <p>This exception is thrown to indicate issues encountered while interacting
 * with the ZAPI client. It extends {@link CRuntimeException} to provide additional
 * context about the error.</p>
 */
public class CZApiClientException extends CRuntimeException {

  /**
   * Constructs a new {@code CZApiClientException} with the specified detail message
   * and cause.
   *
   * @param message the detail message explaining the reason for the exception
   * @param t the cause of the exception (can be {@code null})
   */
  public CZApiClientException(String message, Throwable t) {
    super(message, t);
  }
}

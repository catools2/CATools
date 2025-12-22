package org.catools.atlassian.scale.exception;

import org.catools.common.exception.CRuntimeException;

/** Exception class for Scale client related exceptions. */
public class CZScaleClientException extends CRuntimeException {

  public CZScaleClientException(String message, Throwable t) {
    super(message, t);
  }
}

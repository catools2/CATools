package org.catools.atlassian.scale.exception;

import org.catools.common.exception.CRuntimeException;

/**
 * Exception class for Scale client related exceptions.
 */
public class CZScaleException extends CRuntimeException {

  public CZScaleException(String message, Throwable t) {
    super(message, t);
  }
}

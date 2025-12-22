package org.catools.atlassian.jira.exception;

import org.catools.common.exception.CRuntimeException;

/** Exception class for Jira client related exceptions. */
public class CJiraClientException extends CRuntimeException {

  public CJiraClientException(String message, Throwable t) {
    super(message, t);
  }
}

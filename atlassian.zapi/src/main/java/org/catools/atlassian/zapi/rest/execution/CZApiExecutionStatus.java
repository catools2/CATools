package org.catools.atlassian.zapi.rest.execution;

import org.catools.common.collections.CSet;

/**
 * Enumeration representing the various statuses of a test execution in the ZAPI system.
 *
 * <p>This enum provides methods to determine if a status indicates a running, failed, skipped, or
 * passed state.
 */
public enum CZApiExecutionStatus {
  SUCCESS, // Indicates that the execution was successful.
  FAILURE, // Indicates that the execution failed.
  WIP, // Indicates that the execution is a work in progress.
  SKIP, // Indicates that the execution was skipped.
  IGNORED, // Indicates that the execution was ignored.
  DEFERRED, // Indicates that the execution was deferred.
  BLOCKED, // Indicates that the execution was blocked.
  AWAITING, // Indicates that the execution is awaiting further action.
  SUCCESS_PERCENTAGE_FAILURE, // Indicates a partial success with some percentage of failure.
  CREATED; // Indicates that the execution has been created but not yet started.

  /**
   * Checks if the current status represents a running state.
   *
   * @return {@code true} if the status is {@link #WIP}, otherwise {@code false}.
   */
  public boolean isRunning() {
    return new CSet<>(WIP).contains(this);
  }

  /**
   * Checks if the current status represents a failed state.
   *
   * @return {@code true} if the status is {@link #FAILURE}, otherwise {@code false}.
   */
  public boolean isFailed() {
    return new CSet<>(FAILURE).contains(this);
  }

  /**
   * Checks if the current status represents a skipped state.
   *
   * @return {@code true} if the status is one of {@link #SKIP}, {@link #IGNORED}, {@link
   *     #DEFERRED}, or {@link #AWAITING}, otherwise {@code false}.
   */
  public boolean isSkipped() {
    return new CSet<>(SKIP, IGNORED, DEFERRED, AWAITING).contains(this);
  }

  /**
   * Checks if the current status represents a passed state.
   *
   * @return {@code true} if the status is one of {@link #SUCCESS} or {@link
   *     #SUCCESS_PERCENTAGE_FAILURE}, otherwise {@code false}.
   */
  public boolean isPassed() {
    return new CSet<>(SUCCESS, SUCCESS_PERCENTAGE_FAILURE).contains(this);
  }
}

package org.catools.atlassian.scale.rest.cycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CSet;

/**
 * Represents the execution status of a test in the Scale system.
 *
 * <p>This enum defines various statuses that a test execution can have, such as "In Progress",
 * "Fail", "Blocked", "Pass", and "Not Executed". It also provides methods for JSON
 * serialization/deserialization and utility methods for working with the statuses.
 */
public enum CZScaleExecutionStatus {
  /** Status is unset or undefined. */
  UNSET(null),

  /** Test execution is currently in progress. */
  IN_PROGRESS("In Progress"),

  /** Test execution has failed. */
  FAIL("Fail"),

  /** Test execution is blocked and cannot proceed. */
  BLOCKED("Blocked"),

  /** Test execution has passed successfully. */
  PASS("Pass"),

  /** Test execution has not been executed. */
  NOT_EXECUTED("Not Executed");

  /** The name of the status as represented in the Scale system. */
  private final String scaleName;

  /**
   * Constructs a new execution status with the specified Scale name.
   *
   * @param scaleName the name of the status in the Scale system
   */
  CZScaleExecutionStatus(String scaleName) {
    this.scaleName = scaleName;
  }

  /**
   * Creates an execution status from its Scale name.
   *
   * <p>This method is used for deserializing JSON values into enum instances.
   *
   * @param value the Scale name of the status
   * @return the corresponding {@link CZScaleExecutionStatus}, or null if no match is found
   */
  @JsonCreator
  public static CZScaleExecutionStatus formScaleName(String value) {
    return CSet.of(values())
        .getFirstOrNull(s -> StringUtils.equalsIgnoreCase(s.getScaleName(), value));
  }

  /**
   * Retrieves the Scale name of the execution status.
   *
   * <p>This method is used for serializing the enum instance into a JSON value.
   *
   * @return the Scale name of the status
   */
  @JsonValue
  public String getScaleName() {
    return scaleName;
  }
}

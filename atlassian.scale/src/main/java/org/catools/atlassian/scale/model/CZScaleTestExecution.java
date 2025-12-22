package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.atlassian.scale.rest.cycle.CZScaleExecutionStatus;
import org.catools.atlassian.scale.utils.CustomDateDeserializer;
import org.catools.atlassian.scale.utils.CustomDateSerializer;
import org.catools.common.date.CDate;

/**
 * Represents a test execution in the Scale system.
 *
 * <p>This class models the details of a test execution, including its status, environment, assigned
 * user, execution dates, and related test case information. It supports JSON
 * serialization/deserialization and uses custom serializers/deserializers for date handling.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleTestExecution {

  /** The unique identifier of the test execution. */
  private Long id;

  /** The execution status of the test. */
  private CZScaleExecutionStatus status;

  /** The environment in which the test was executed. */
  private String environment;

  /** The user to whom the test execution is assigned. */
  private String assignedTo;

  /** The user who executed the test. */
  private String executedBy;

  /** The key of the user who executed the test. */
  private String userKey;

  /**
   * The actual start date and time of the test execution.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate actualStartDate;

  /**
   * The actual end date and time of the test execution.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate actualEndDate;

  /** The key of the test case associated with the test execution. */
  private String testCaseKey;

  /**
   * The date and time when the test execution occurred.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate executionDate;
}

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
import org.catools.common.collections.CHashMap;
import org.catools.common.date.CDate;

/**
 * Represents the execution details of a Scale plan.
 *
 * <p>This class models the execution of a test case within a Scale plan, including
 * information such as the test case key, execution status, environment, user details,
 * execution time, and custom fields. It supports JSON serialization/deserialization
 * and uses custom serializers/deserializers for date handling.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScalePlanExecution {

  /**
   * The key of the test case associated with this execution.
   */
  private String testCaseKey;

  /**
   * The execution status of the test case.
   */
  private CZScaleExecutionStatus status;

  /**
   * The environment in which the test case was executed.
   */
  private String environment;

  /**
   * A comment or note associated with the execution.
   */
  private String comment;

  /**
   * The key of the user who performed the execution.
   */
  private String userKey;

  /**
   * The time taken for the execution, in milliseconds.
   */
  private Long executionTime;

  /**
   * The date and time when the execution occurred.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.</p>
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate executionDate;

  /**
   * A map of custom fields associated with the execution.
   */
  private CHashMap<String, String> customFields;

  /**
   * The results of the scripts executed during the test case execution.
   */
  private CZScaleScriptResults scriptResults;
}
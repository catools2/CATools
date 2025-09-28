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
import org.catools.common.collections.CSet;

import java.util.Date;

/**
 * Represents a request to update the test result in the Scale system.
 *
 * <p>This class models the details required to update a test result, including
 * execution status, environment, comments, execution time, and custom fields.
 * It supports JSON serialization/deserialization and uses custom serializers/deserializers
 * for date handling.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleUpdateTestResultRequest {

  /**
   * The execution status of the test result.
   */
  private CZScaleExecutionStatus status;

  /**
   * The environment in which the test was executed.
   */
  private String environment;

  /**
   * A comment associated with the test result update.
   */
  private String comment;

  /**
   * The user to whom the test result is assigned.
   */
  private String assignedTo;

  /**
   * The user who executed the test.
   */
  private String executedBy;

  /**
   * The total execution time for the test, in seconds.
   */
  private Integer executionTime;

  /**
   * The date and time when the test was executed.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.</p>
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date executionDate;

  /**
   * The actual start date and time of the test execution.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.</p>
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date actualStartDate;

  /**
   * The actual end date and time of the test execution.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.</p>
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date actualEndDate;

  /**
   * A map of custom fields associated with the test result.
   *
   * <p>Keys represent field names, and values represent field values.</p>
   */
  private CHashMap<String, String> customFields;

  /**
   * A set of issue links associated with the test result.
   */
  private CSet<String> issueLinks;

  /**
   * The script results associated with the test result.
   */
  private CZScaleScriptResults scriptResults;
}
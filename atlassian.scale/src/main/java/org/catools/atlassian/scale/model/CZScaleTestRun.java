package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.atlassian.scale.utils.CustomDateDeserializer;
import org.catools.atlassian.scale.utils.CustomDateSerializer;
import org.catools.common.collections.CHashMap;
import org.catools.common.date.CDate;

/**
 * Represents a test run in the Scale system.
 *
 * <p>This class models the details of a test run, including its metadata, associated project and
 * issue information, planned dates, and execution details. It supports JSON
 * serialization/deserialization and uses custom serializers/deserializers for date handling.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleTestRun {

  /** The unique key of the test run. */
  private String key;

  /** The name of the test run. */
  private String name;

  /** The key of the issue associated with the test run. */
  private String issueKey;

  /** A description of the test run. */
  private String description;

  /** The version associated with the test run. */
  private String version;

  /** The key of the project associated with the test run. */
  private String projectKey;

  /** The owner of the test run. */
  private String owner;

  /** The user who last updated the test run. */
  private String updatedBy;

  /**
   * The date and time when the test run was last updated.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate updatedOn;

  /**
   * The date and time when the test run was created.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate createdOn;

  /** The user who created the test run. */
  private String createdBy;

  /** The folder in which the test run is organized. */
  private String folder;

  /** The status of the test run. */
  private String status;

  /**
   * The planned end date and time for the test run.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate plannedEndDate;

  /**
   * The planned start date and time for the test run.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate plannedStartDate;

  /** The number of issues associated with the test run. */
  private Integer issueCount;

  /** The total execution time for the test run, in seconds. */
  private Integer executionTime;

  /** The estimated time for the test run, in seconds. */
  private Integer estimatedTime;

  /** The number of test cases in the test run. */
  private Integer testCaseCount;

  /**
   * A summary of the execution results for the test run.
   *
   * <p>Maps execution statuses to their respective counts.
   */
  private CHashMap<String, Integer> executionSummary;

  /** The collection of test executions associated with the test run. */
  private CZScaleTestExecutions items;
}

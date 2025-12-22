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
 * Represents a test run within a Scale plan.
 *
 * <p>This class models the details of a test run, including its project, plan, issue, status,
 * owner, planned dates, and custom fields. It supports JSON serialization/deserialization and uses
 * custom serializers/deserializers for date handling.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScalePlanTestRun {

  /** The key of the project associated with the test run. */
  private String projectKey;

  /** The key of the test plan associated with the test run. */
  private String testPlanKey;

  /** The key of the issue associated with the test run. */
  private String issueKey;

  /** The name of the test run. */
  private String name;

  /** The status of the test run. */
  private String status;

  /** The iteration of the test run. */
  private String iteration;

  /** The version associated with the test run. */
  private String version;

  /** The folder where the test run is organized. */
  private String folder;

  /** The owner of the test run. */
  private String owner;

  /**
   * The planned start date of the test run.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate plannedStartDate;

  /**
   * The planned end date of the test run.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate plannedEndDate;

  /** A map of custom fields associated with the test run. */
  private CHashMap<String, String> customFields;

  /** The collection of executions associated with the test run. */
  private CZScalePlanExecutions items;
}

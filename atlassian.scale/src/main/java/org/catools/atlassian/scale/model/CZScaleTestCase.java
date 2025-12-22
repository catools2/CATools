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
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;

/**
 * Represents a test case in the Scale system.
 *
 * <p>This class models the details of a test case, including its metadata, status, associated
 * scripts, and custom fields. It supports JSON serialization/deserialization and uses custom
 * serializers/deserializers for date handling.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleTestCase {

  /** The owner of the test case. */
  private String owner;

  /** The user who last updated the test case. */
  private String updatedBy;

  /** The precondition for the test case. */
  private String precondition;

  /** The major version of the test case. */
  private int majorVersion;

  /** The estimated time to execute the test case, in seconds. */
  private int estimatedTime;

  /** The priority of the test case. */
  private String priority;

  /** The objective of the test case. */
  private String objective;

  /** The key of the project associated with the test case. */
  private String projectKey;

  /** The user who created the test case. */
  private String createdBy;

  /** Indicates whether this is the latest version of the test case. */
  private boolean latestVersion;

  /** The status of the last test result for the test case. */
  private String lastTestResultStatus;

  /** The name of the test case. */
  private String name;

  /** The unique identifier of the test case. */
  private String id;

  /** The key of the test case. */
  private String key;

  /** The status of the test case. */
  private String status;

  /** The component associated with the test case. */
  private String component;

  /** The folder where the test case is organized. */
  private String folder;

  /**
   * The date and time when the test case was last updated.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate updatedOn;

  /**
   * The date and time when the test case was created.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate createdOn;

  /** A list of labels associated with the test case. */
  private CList<String> labels;

  /** A list of issue links associated with the test case. */
  private CList<String> issueLinks;

  /** The test script associated with the test case. */
  private CZScaleTestScript testScript;

  /** A map of custom fields associated with the test case. */
  private CHashMap<String, String> customFields;

  /** The parameters associated with the test case. */
  private CZScaleTestCaseParameters parameters;

  /** The change history of the test case. */
  private CZScaleChangeHistories histories;
}

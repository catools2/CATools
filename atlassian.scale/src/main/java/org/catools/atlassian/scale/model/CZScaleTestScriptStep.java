package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a step in a test script in the Scale system.
 *
 * <p>This class models the details of an individual test script step, including its
 * identifier, index, description, expected result, test data, and associated test case key.
 * It also includes any attachments related to the step. The class supports JSON
 * serialization/deserialization and uses Lombok annotations to reduce boilerplate code.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleTestScriptStep {

  /**
   * The unique identifier of the test script step.
   */
  private int id;

  /**
   * The index of the step in the test script.
   */
  private int index;

  /**
   * The expected result of the step.
   */
  private String expectedResult;

  /**
   * A description of the step.
   */
  private String description;

  /**
   * The test data associated with the step.
   */
  private String testData;

  /**
   * The key of the test case associated with the step.
   */
  private String testCaseKey;

  /**
   * The attachments associated with the step.
   */
  private CZScaleAttachments attachments;
}
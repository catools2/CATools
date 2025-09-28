package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.atlassian.scale.rest.cycle.CZScaleExecutionStatus;

/**
 * Represents the result of a script execution in the Scale system.
 *
 * <p>This class models the details of a script execution result, including its index,
 * execution status, test data, expected result, description, and any associated comments.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleScriptResult {

  /**
   * The index of the script execution result.
   */
  private Integer index;

  /**
   * The execution status of the script.
   */
  private CZScaleExecutionStatus status;

  /**
   * The test data used during the script execution.
   */
  private String testData;

  /**
   * The expected result of the script execution.
   */
  private String expectedResult;

  /**
   * A description of the script execution result.
   */
  private String description;

  /**
   * A comment or note associated with the script execution result.
   */
  private String comment;
}
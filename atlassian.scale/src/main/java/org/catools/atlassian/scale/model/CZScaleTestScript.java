package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a test script in the Scale system.
 *
 * <p>This class models the details of a test script, including its unique identifier, type, and
 * associated steps. It supports JSON serialization/deserialization and uses Lombok annotations for
 * boilerplate code reduction.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleTestScript {

  /** The unique identifier of the test script. */
  private int id;

  /** The type of the test script. */
  private String type;

  /** The steps associated with the test script. */
  private CZScaleTestScriptSteps steps;
}

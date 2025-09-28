package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.common.collections.CList;

/**
 * Represents the parameters of a test case in the Scale system.
 *
 * <p>This class models the variables and entries associated with a test case.
 * It supports JSON serialization/deserialization and uses Lombok annotations
 * for boilerplate code reduction.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CZScaleTestCaseParameters {

  /**
   * A list of variables associated with the test case parameters.
   */
  public CList<Object> variables;

  /**
   * A list of entries associated with the test case parameters.
   */
  public CList<Object> entries;
}
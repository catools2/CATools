package org.catools.atlassian.zapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents the status of a ZAPI execution.
 *
 * <p>This class models the structure of an execution status in the ZAPI system, including its
 * unique identifier, name, and description.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CZApiExecutionStatus {

  /** Unique identifier for the execution status. */
  private Long id;

  /** Description of the execution status. */
  private String description;

  /** Name of the execution status. */
  private String name;
}

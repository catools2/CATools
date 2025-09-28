package org.catools.atlassian.zapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.common.date.CDate;

/**
 * Represents a ZAPI execution.
 *
 * <p>This class models the structure of an execution in the ZAPI system, including
 * its metadata such as execution status, associated cycle, project, and defect details.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CZApiExecution {

  /**
   * Unique identifier for the execution.
   */
  private Long id;

  /**
   * Order ID of the execution.
   */
  private Long orderId;

  /**
   * Status of the execution (e.g., passed, failed, etc.).
   */
  private String executionStatus;

  /**
   * Date and time when the execution was performed.
   */
  private CDate executedOn;

  /**
   * Username of the user who performed the execution.
   */
  private String executedByUserName;

  /**
   * Comment or note associated with the execution.
   */
  private String comment;

  /**
   * Identifier of the cycle to which the execution belongs.
   */
  private Long cycleId;

  /**
   * Name of the cycle to which the execution belongs.
   */
  private String cycleName;

  /**
   * Name of the version associated with the execution.
   */
  private String versionName;

  /**
   * Date and time when the execution was created.
   */
  private CDate createdOn;

  /**
   * Identifier of the issue associated with the execution.
   */
  private Long issueId;

  /**
   * Key of the issue associated with the execution.
   */
  private String issueKey;

  /**
   * Key of the project associated with the execution.
   */
  private String projectKey;

  /**
   * Name of the project associated with the execution.
   */
  private String projectName;

  /**
   * Defects associated with the execution.
   */
  private CZApiExecutionDefects executionDefects;

  /**
   * Count of defects associated with the execution.
   */
  private Long executionDefectCount;

  /**
   * Count of defects associated with the steps of the execution.
   */
  private Long stepDefectCount;

  /**
   * Total count of defects associated with the execution.
   */
  private Long totalDefectCount;
}

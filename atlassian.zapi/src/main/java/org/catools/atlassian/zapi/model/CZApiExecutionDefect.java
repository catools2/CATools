package org.catools.atlassian.zapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a defect associated with a ZAPI execution.
 *
 * <p>This class models the structure of a defect in the ZAPI system, including
 * its metadata such as defect ID, key, summary, status, and resolution details.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CZApiExecutionDefect {

  /**
   * Unique identifier for the defect.
   */
  private Long defectId;

  /**
   * Key of the defect (e.g., issue key in a tracking system).
   */
  private String defectKey;

  /**
   * Summary or title of the defect.
   */
  private String defectSummary;

  /**
   * Current status of the defect (e.g., open, resolved, etc.).
   */
  private String defectStatus;

  /**
   * Identifier for the resolution of the defect.
   */
  private String defectResolutionId;
}

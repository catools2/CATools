package org.catools.atlassian.zapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.common.date.CDate;

/**
 * Represents a ZAPI test cycle.
 *
 * <p>This class models the structure of a test cycle in the ZAPI system, including
 * its metadata such as project, version, environment, and scheduling details.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CZApiCycle {

  /**
   * Unique identifier for the test cycle.
   */
  private Long id;

  /**
   * Description of the test cycle.
   */
  private String description;

  /**
   * The project associated with the test cycle.
   */
  private CZApiProject project;

  /**
   * The version associated with the test cycle.
   */
  private CZApiVersion version;

  /**
   * The environment in which the test cycle is executed.
   */
  private String environment;

  /**
   * The end date of the test cycle.
   */
  private CDate endDate;

  /**
   * The build associated with the test cycle.
   */
  private String build;

  /**
   * The name of the test cycle.
   */
  private String name;

  /**
   * The user who last modified the test cycle.
   */
  private String modifiedBy;

  /**
   * The start date of the test cycle.
   */
  private CDate startDate;
}

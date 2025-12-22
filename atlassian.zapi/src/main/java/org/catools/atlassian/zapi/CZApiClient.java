package org.catools.atlassian.zapi;

import org.catools.atlassian.zapi.rest.cycle.CZApiCycleClient;
import org.catools.atlassian.zapi.rest.execution.CZApiExecutionClient;
import org.catools.atlassian.zapi.rest.util.CZApiExecutionStatusClient;
import org.catools.atlassian.zapi.rest.util.CZApiProjectClient;
import org.catools.atlassian.zapi.rest.util.CZApiVersionClient;
import org.catools.atlassian.zapi.rest.zql.CZApiSearchClient;

/**
 * Main client class for interacting with the ZAPI system.
 *
 * <p>This class provides static instances of various client components to interact with different
 * parts of the ZAPI REST API, such as execution statuses, cycles, searches, executions, projects,
 * and versions.
 */
public class CZApiClient {

  /** Client for retrieving execution statuses. */
  public static final CZApiExecutionStatusClient ExecutionStatus = new CZApiExecutionStatusClient();

  /** Client for managing test cycles. */
  public static final CZApiCycleClient Cycle = new CZApiCycleClient();

  /** Client for searching test executions using ZQL queries. */
  public static final CZApiSearchClient Search = new CZApiSearchClient();

  /** Client for managing test executions. */
  public static final CZApiExecutionClient Execution = new CZApiExecutionClient();

  /** Client for retrieving project information. */
  public static final CZApiProjectClient Project = new CZApiProjectClient();

  /** Client for retrieving version information for projects. */
  public static final CZApiVersionClient Version = new CZApiVersionClient();
}

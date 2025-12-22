package org.catools.atlassian.etl.zapi.translators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.zapi.model.*;
import org.catools.etl.tms.cache.CEtlCacheManager;
import org.catools.etl.tms.dao.CEtlBaseDao;
import org.catools.etl.tms.model.*;

import java.util.Objects;

/**
 * Translator class for converting ZAPI cycles and executions into ETL-compatible objects. Provides
 * methods to map cycle and execution data to ETL cycles and executions.
 */
@Slf4j
public class CEtlZApiTranslator {

  /**
   * Translates a ZAPI cycle into an ETL cycle.
   *
   * @param zProject The ZAPI project associated with the cycle.
   * @param zVersion The ZAPI version associated with the cycle.
   * @param cycle The ZAPI cycle to be translated.
   * @return The translated ETL cycle.
   */
  public static CEtlCycle translateCycle(
      CZApiProject zProject, CZApiVersion zVersion, CZApiCycle cycle) {
    Objects.requireNonNull(zProject);
    Objects.requireNonNull(zVersion);
    Objects.requireNonNull(cycle);

    // Find or create the ETL cycle
    CEtlCycle etlCycle = CEtlBaseDao.find(CEtlCycle.class, cycle.getId().toString());
    if (etlCycle == null) {
      etlCycle = new CEtlCycle();
      etlCycle.setId(cycle.getId().toString());
    }

    try {
      // Retrieve the associated ETL project and version
      CEtlProject project = CEtlCacheManager.readProject(new CEtlProject(zProject.getName()));
      CEtlVersion version =
          CEtlCacheManager.readVersion(new CEtlVersion(zVersion.getName(), project));

      // Set properties for the ETL cycle
      etlCycle.setVersion(version);
      etlCycle.setName(cycle.getName());
      etlCycle.setEndDate(cycle.getEndDate());
      etlCycle.setStartDate(cycle.getStartDate());

      return etlCycle;
    } catch (Throwable t) {
      log.error("Failed to translate cycle.", t);
      throw t;
    }
  }

  /**
   * Translates a ZAPI execution into an ETL execution.
   *
   * @param project The ZAPI project associated with the execution.
   * @param version The ZAPI version associated with the execution.
   * @param cycles The ZAPI cycles containing the execution.
   * @param execution The ZAPI execution to be translated.
   * @return The translated ETL execution.
   */
  public static CEtlExecution translateExecution(
      CZApiProject project, CZApiVersion version, CZApiCycles cycles, CZApiExecution execution) {
    Objects.requireNonNull(project);
    Objects.requireNonNull(version);
    Objects.requireNonNull(cycles);
    Objects.requireNonNull(execution);

    // Find or create the ETL execution
    CEtlExecution etlExecution =
        CEtlBaseDao.find(CEtlExecution.class, String.valueOf(execution.getId()));
    if (etlExecution == null) {
      etlExecution = new CEtlExecution();
      etlExecution.setId(String.valueOf(execution.getId()));
    }

    try {
      // Set properties for the ETL execution
      etlExecution.setItem(CEtlCacheManager.readItem(execution.getIssueKey()));
      etlExecution.setStatus(getStatus(execution.getExecutionStatus()));
      etlExecution.setExecutor(getExecutor(execution));
      etlExecution.setCycle(
          translateCycle(project, version, cycles.getById(execution.getCycleId())));
      etlExecution.setCreated(execution.getCreatedOn());
      etlExecution.setExecuted(execution.getExecutedOn());

      return etlExecution;
    } catch (Throwable t) {
      log.error("Failed to translate execution.", t);
      throw t;
    }
  }

  /**
   * Retrieves the executor of a test execution.
   *
   * @param execution The ZAPI execution.
   * @return The ETL user representing the executor.
   */
  private static CEtlUser getExecutor(CZApiExecution execution) {
    return StringUtils.isBlank(execution.getExecutedByUserName())
        ? CEtlUser.UNSET
        : CEtlCacheManager.readUser(new CEtlUser(execution.getExecutedByUserName()));
  }

  /**
   * Retrieves the status of a test execution.
   *
   * @param statusName The name of the execution status.
   * @return The corresponding ETL execution status.
   */
  private static CEtlExecutionStatus getStatus(String statusName) {
    return StringUtils.isBlank(statusName)
        ? CEtlExecutionStatus.UNSET
        : CEtlCacheManager.readExecutionStatus(new CEtlExecutionStatus(statusName.toUpperCase()));
  }
}

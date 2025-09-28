package org.catools.atlassian.etl.zapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.etl.zapi.translators.CEtlZApiTranslator;
import org.catools.atlassian.zapi.CZApiClient;
import org.catools.atlassian.zapi.model.*;
import org.catools.common.collections.CSet;
import org.catools.common.date.CDate;
import org.catools.etl.tms.dao.CEtlExecutionDao;
import org.catools.etl.tms.dao.CEtlLastSyncDao;
import org.catools.etl.tms.model.CEtlExecution;

import java.util.Date;

/**
 * Client class for synchronizing ZAPI data with the ETL system.
 * Provides methods to synchronize projects, versions, cycles, and executions.
 */
@Slf4j
public class CEtlZApiSyncClient {
  private static final String ZAPI = "ZAPI";
  private static final String UNSCHEDULED = "Unscheduled";

  /**
   * Synchronizes ZAPI data for the specified projects.
   *
   * @param projectNamesToSync A set of project names to synchronize.
   * @param parallelInputCount The number of parallel threads for input operations.
   * @param parallelOutputCount The number of parallel threads for output operations.
   */
  public static void syncZephyr(CSet<String> projectNamesToSync, int parallelInputCount, int parallelOutputCount) {
    CZApiProjects projects = CZApiClient.Project.getProjects();

    for (CZApiProject project : projects.getAll(p -> projectNamesToSync.contains(p.getName()))) {
      CZApiVersions projectVersions = CZApiClient.Version.getProjectVersions(project).getAllVersions();

      // Remove "Unscheduled" cycles from the versions
      projectVersions.removeIf(v -> StringUtils.containsIgnoreCase(UNSCHEDULED, v.getName()));

      // Determine the last synchronization time for the project
      Date projectSyncStartTime = CDate.now();
      Date projectLastSync = CEtlLastSyncDao.getProjectLastSync(ZAPI, project.getName());

      // If the project was previously synchronized, filter versions to only include updated ones
      if (projectLastSync != null) {
        CZApiExecutions executions =
            CZApiClient.Search.getExecutions(
                String.format("project='%s'", project.getName()),
                projectLastSync,
                parallelInputCount,
                parallelOutputCount,
                null);
        CSet<String> versionsToUpdate = executions.mapToSet(CZApiExecution::getVersionName);
        projectVersions.removeIf(v -> !versionsToUpdate.contains(v.getName()));
      }

      // Synchronize cycles and executions for each version
      for (CZApiVersion version : projectVersions) {
        CZApiCycles cycles =
            CZApiClient.Cycle.getAllCycle(
                new CZApiProject().setId(project.getId()).setName(project.getName()),
                new CZApiVersion().setId(version.getId()).setName(version.getName()));

        if (cycles != null && cycles.isNotEmpty()) {
          addExecutions(
              project,
              version,
              cycles,
              parallelInputCount,
              parallelOutputCount);
        }
      }
      // Update the last synchronization time for the project
      CEtlLastSyncDao.updateProjectLastSync(ZAPI, project.getName(), projectSyncStartTime);
    }
  }

  /**
   * Adds executions for the specified project, version, and cycles.
   *
   * @param project The ZAPI project.
   * @param version The ZAPI version.
   * @param cycles The ZAPI cycles.
   * @param parallelInputCount The number of parallel threads for input operations.
   * @param parallelOutputCount The number of parallel threads for output operations.
   */
  private static void addExecutions(
      CZApiProject project,
      CZApiVersion version,
      CZApiCycles cycles,
      int parallelInputCount,
      int parallelOutputCount) {
    // Determine the last synchronization time for the executions
    Date lastSync = CEtlLastSyncDao.getExecutionLastSync(ZAPI, project.getName(), version.getName());
    String zql = String.format("project=\"%s\" AND fixVersion = \"%s\"", project.getName(), version.getName());

    Date syncStartTime = CDate.now();
    CZApiClient.Search.getExecutions(
        zql,
        lastSync,
        parallelInputCount,
        parallelOutputCount,
        zApiExecutions -> {
          if (zApiExecutions != null && zApiExecutions.isNotEmpty()) {
            // Filter out executions that do not belong to the specified project or version
            zApiExecutions.removeIf(e -> !project.getName().equalsIgnoreCase(e.getProjectName())
                || !version.getName().equalsIgnoreCase(e.getVersionName()));

            if (zApiExecutions.isNotEmpty()) {
              // Translate and merge each execution into the ETL system
              for (CZApiExecution execution : zApiExecutions) {
                CEtlExecution translatedExecution = CEtlZApiTranslator.translateExecution(project, version, cycles, execution);
                CEtlExecutionDao.mergeExecution(translatedExecution);
              }
            }
          }
        });

    // Update the last synchronization time for the executions
    CEtlLastSyncDao.updateExecutionLastSync(ZAPI, project.getName(), version.getName(), syncStartTime);
  }
}
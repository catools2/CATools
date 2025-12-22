package org.catools.atlassian.etl.scale;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.atlassian.etl.scale.configs.CEtlZScaleConfigs;
import org.catools.atlassian.etl.scale.translators.CEtlZScaleTestCaseTranslator;
import org.catools.atlassian.scale.CZScaleClient;
import org.catools.atlassian.scale.model.CZScaleTestCase;
import org.catools.atlassian.scale.model.CZScaleTestExecution;
import org.catools.atlassian.scale.model.CZScaleTestRun;
import org.catools.atlassian.scale.model.CZScaleTestRuns;
import org.catools.common.collections.CSet;
import org.catools.common.concurrent.CParallelRunner;
import org.catools.common.date.CDate;
import org.catools.common.utils.CStringUtil;
import org.catools.etl.tms.cache.CEtlCacheManager;
import org.catools.etl.tms.dao.CEtlExecutionDao;
import org.catools.etl.tms.dao.CEtlItemDao;
import org.catools.etl.tms.dao.CEtlLastSyncDao;
import org.catools.etl.tms.model.*;

import java.util.Date;
import java.util.Stack;

import static org.catools.atlassian.etl.scale.helpers.CEtlZScaleSyncHelper.getProjectByName;
import static org.catools.atlassian.etl.scale.helpers.CEtlZScaleSyncHelper.getProjectVersions;
import static org.catools.atlassian.etl.scale.translators.CEtlZScaleTestRunTranslator.translateExecution;
import static org.catools.atlassian.etl.scale.translators.CEtlZScaleTestRunTranslator.translateTestRun;

/**
 * Client class for synchronizing ZScale data with the ETL system. Provides methods to synchronize
 * test cases, test runs, and their executions.
 */
@Slf4j
@UtilityClass
public class CEtlZScaleSyncClient {

  /**
   * Synchronizes ZScale data for a specific project.
   *
   * @param projectNameToSync The name of the project to synchronize.
   * @param parallelInputCount The number of parallel threads for input operations.
   * @param parallelOutputCount The number of parallel threads for output operations.
   * @throws Throwable If an error occurs during synchronization.
   */
  public static void syncScale(
      String projectNameToSync, int parallelInputCount, int parallelOutputCount) throws Throwable {
    BasicProject project = getProjectByName(projectNameToSync);
    CEtlProject etlProject = CEtlCacheManager.readProject(new CEtlProject(project.getName()));

    CEtlVersions versions = new CEtlVersions(getProjectVersions(project.getKey(), etlProject));

    updateTestCases(
        project.getKey(), etlProject, versions, parallelInputCount, parallelOutputCount);
    updateTestRuns(project.getKey(), versions, parallelOutputCount);
  }

  /**
   * Updates test runs for a specific project.
   *
   * @param projectKey The key of the project.
   * @param versions The versions associated with the project.
   * @param parallelOutputCount The number of parallel threads for output operations.
   * @throws Throwable If an error occurs during the update.
   */
  private static void updateTestRuns(
      String projectKey, CEtlVersions versions, int parallelOutputCount) throws Throwable {
    for (String activeFolder : CEtlZScaleConfigs.Scale.getSyncTestRunsFolders()) {
      Date projectSyncStartTime = CDate.now();
      CZScaleTestRuns testRunsToSync = getTestRunsToSync(projectKey, activeFolder);
      for (CZScaleTestRun scaleTestRun : testRunsToSync) {
        String testRunInfoKey = scaleTestRun.getKey();
        String runDbSyncKey = "SCALE_RUN_" + testRunInfoKey.toUpperCase();
        Date runLastSync = CEtlLastSyncDao.getProjectLastSync(runDbSyncKey, projectKey);

        log.info("Start sync {} run.", testRunInfoKey);
        CZScaleTestRun testRun = CZScaleClient.TestRuns.getTestRun(testRunInfoKey);

        if (testRun.getItems().isNotEmpty()) {
          CEtlVersion version = getVersionForTestRun(versions, testRun);
          updateTestRunExecutions(
              version, testRunInfoKey, runLastSync, testRun, parallelOutputCount);
        }

        CEtlLastSyncDao.updateProjectLastSync(runDbSyncKey, projectKey, projectSyncStartTime);
        log.info("Finish sync {} run.", testRunInfoKey);
      }
    }
  }

  /**
   * Retrieves the version associated with a test run.
   *
   * @param versions The available versions for the project.
   * @param testRun The test run to retrieve the version for.
   * @return The corresponding ETL version, or null if not found.
   */
  private static CEtlVersion getVersionForTestRun(CEtlVersions versions, CZScaleTestRun testRun) {
    if (testRun.getVersion() == null) {
      return null;
    }
    return versions.getFirst(
        v -> CStringUtil.equalsAnyIgnoreCase(v.getName(), testRun.getVersion()));
  }

  /**
   * Updates the executions for a specific test run.
   *
   * @param version The version associated with the test run.
   * @param testRunInfoKey The key of the test run.
   * @param runLastSync The last synchronization time for the test run.
   * @param testRun The test run to update.
   * @param parallelOutputCount The number of parallel threads for output operations.
   * @throws Throwable If an error occurs during the update.
   */
  private static void updateTestRunExecutions(
      CEtlVersion version,
      String testRunInfoKey,
      Date runLastSync,
      CZScaleTestRun testRun,
      int parallelOutputCount)
      throws Throwable {
    log.info(
        "Start updating {} run execution with {} items.",
        testRun.getKey(),
        testRun.getItems().size());
    CEtlCycle cycle = translateTestRun(version, testRun);
    Stack<CZScaleTestExecution> executionsToSync = new Stack<>();
    executionsToSync.addAll(testRun.getItems().getAll(item -> itemShouldSync(runLastSync, item)));

    log.info("{} items need to be updated for {} run.", executionsToSync.size(), testRun.getKey());
    new CParallelRunner<>(
            String.format("Update %s Test Run Executions", testRun.getKey()),
            parallelOutputCount,
            () -> {
              while (true) {
                CZScaleTestExecution testExecution;
                synchronized (executionsToSync) {
                  if (executionsToSync.isEmpty()) break;
                  testExecution = executionsToSync.pop();
                }
                CEtlExecution execution = translateExecution(testRun, cycle, testExecution);
                if (execution != null) CEtlExecutionDao.mergeExecution(execution);
              }
              return true;
            })
        .invokeAll();

    CSet<String> issueKeysFromScale =
        testRun.getItems().mapToSet(CZScaleTestExecution::getTestCaseKey);
    CSet<String> issueKeysFromDB = CEtlExecutionDao.getExecutionsByCycleId(testRunInfoKey);
    CSet<String> idsToDeleteFromCycle =
        issueKeysFromDB.getAll(issueKeysFromScale::notContains).toSet();

    if (idsToDeleteFromCycle.isNotEmpty()) {
      CEtlExecutionDao.deleteExecutions(testRunInfoKey, idsToDeleteFromCycle);
    }

    log.info(
        "Finish updating {} run execution with {} items.",
        testRun.getKey(),
        testRun.getItems().size());
  }

  /**
   * Updates test cases for a specific project.
   *
   * @param projectKey The key of the project.
   * @param project The ETL project associated with the test cases.
   * @param versions The versions associated with the project.
   * @param parallelInputCount The number of parallel threads for input operations.
   * @param parallelOutputCount The number of parallel threads for output operations.
   */
  private static void updateTestCases(
      String projectKey,
      CEtlProject project,
      CEtlVersions versions,
      int parallelInputCount,
      int parallelOutputCount) {
    Date projectSyncStartTime = CDate.now();
    Date projectLastSync = CEtlLastSyncDao.getProjectLastSync("SCALE_TEST_CYCLES", projectKey);
    for (String activeFolder : CEtlZScaleConfigs.Scale.getSyncTestCasesFolders()) {
      CZScaleClient.TestCases.getProjectTestCases(
          projectKey,
          activeFolder,
          "createdOn,updatedOn,key",
          parallelInputCount,
          parallelOutputCount,
          testCase -> {
            if (testCase != null && !testCaseIsSynced(projectLastSync, testCase)) {
              CZScaleTestCase testCaseItem = CZScaleClient.TestCases.getTestCase(testCase.getKey());
              if (testCaseItem != null)
                CEtlItemDao.mergeItem(
                    CEtlZScaleTestCaseTranslator.translateTestCase(
                        project, versions, testCaseItem));
            }
          });
    }

    CEtlLastSyncDao.updateProjectLastSync("SCALE_TEST_CYCLES", projectKey, projectSyncStartTime);
  }

  /**
   * Retrieves test runs to synchronize for a specific project and folder.
   *
   * @param projectKey The key of the project.
   * @param activeFolder The folder containing the test runs.
   * @return The test runs to synchronize.
   */
  private static CZScaleTestRuns getTestRunsToSync(String projectKey, String activeFolder) {
    return CZScaleClient.TestRuns.getAllTestRuns(
        projectKey, activeFolder, "createdOn,updatedOn,key");
  }

  /**
   * Determines if a test execution item should be synchronized.
   *
   * @param runLastSync The last synchronization time for the test run.
   * @param item The test execution item to check.
   * @return True if the item should be synchronized, false otherwise.
   */
  private static boolean itemShouldSync(Date runLastSync, CZScaleTestExecution item) {
    return item.getExecutionDate() == null
        || runLastSync == null
        || item.getExecutionDate().after(runLastSync);
  }

  /**
   * Determines if a test case is already synchronized.
   *
   * @param projectLastSync The last synchronization time for the project.
   * @param testcase The test case to check.
   * @return True if the test case is already synchronized, false otherwise.
   */
  private static boolean testCaseIsSynced(Date projectLastSync, CZScaleTestCase testcase) {
    if (projectLastSync == null) return false;
    return testcase.getUpdatedOn() != null
        ? testcase.getUpdatedOn().before(projectLastSync)
        : testcase.getCreatedOn().before(projectLastSync);
  }
}

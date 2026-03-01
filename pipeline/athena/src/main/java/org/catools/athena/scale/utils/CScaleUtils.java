package org.catools.athena.scale.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.atlassian.etl.scale.ScaleSyncClient;
import org.catools.athena.atlassian.etl.scale.model.ScalePlanExecution;
import org.catools.athena.atlassian.etl.scale.model.ScalePlanTestRun;
import org.catools.athena.atlassian.etl.scale.model.ScaleUpdateTestResultRequest;
import org.catools.athena.atlassian.etl.scale.rest.cycle.ScaleExecutionStatus;
import org.catools.athena.atlassian.etl.scale.rest.cycle.TestRunClient;
import org.catools.athena.model.core.ProjectDto;
import org.catools.athena.model.core.VersionDto;
import org.catools.athena.model.tms.TestCycleDto;
import org.catools.athena.rest.feign.core.cache.CoreCache;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;
import org.catools.athena.rest.feign.pipeline.configs.PipelineConfigs;
import org.catools.athena.rest.feign.tms.clients.TmsClient;
import org.catools.athena.scale.entities.CEtlTestExecution;
import org.catools.athena.scale.entities.CEtlTestExecutions;
import org.catools.athena.scale.entities.CTestCycleInfo;
import org.catools.athena.scale.helpers.CTestExecutionHelper;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CCollection;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.date.CDate;
import org.catools.common.testng.model.CExecutionStatus;
import org.catools.common.testng.utils.CTestClassUtil;
import org.catools.common.utils.CObjectUtil;
import org.catools.common.utils.CStringUtil;

@Slf4j
public class CScaleUtils {
  /**
   * Get Test Cycle by its Code
   *
   * @param cycleCode Test Cycle Code
   * @return TestCycleDto
   */
  public static TestCycleDto getCycleById(String cycleCode) {
    log.debug("Getting test cycle by code: {}", cycleCode);
    TestCycleDto result = TmsClient.findTestCycleByCode(cycleCode);
    log.debug("Found test cycle: {}", result != null ? result.getCode() : "null");
    return result;
  }

  /**
   * Get last Test Cycle by its name pattern for defined project and version
   *
   * @param cycleName Test Cycle Name Pattern
   * @return TestCycleDto
   */
  public static TestCycleDto getLastCycleForDefinedVersion(String cycleName) {
    log.debug(
        "Getting last test cycle by name pattern: {} for project: {} and version: {}",
        cycleName,
        CoreConfigs.getProjectCode(),
        CoreConfigs.getVersionCode());
    TestCycleDto result =
        TmsClient.findLastTestCycleByPattern(
            cycleName, CoreConfigs.getProjectCode(), CoreConfigs.getVersionCode());
    log.debug("Found test cycle: {}", result != null ? result.getCode() : "null");
    return result;
  }

  /**
   * Create Test Cycle with tests defined in cycleInfo
   *
   * @param cycleInfo Cycle Info
   */
  public static void createCycle(CTestCycleInfo cycleInfo) {
    log.info(
        "Creating test cycle with info: name={}, folderPath={}",
        cycleInfo.getTestCycleName(),
        cycleInfo.getTestCycleFolderPath());
    CScaleUtils.createRun(cycleInfo);
    log.info("Test cycle creation completed for: {}", cycleInfo.getTestCycleName());
  }

  /**
   * Update executions in a cycle with given status
   *
   * @param cycleId Cycle Id
   * @param issueIds Issue Ids
   * @param openDefects Open Defects
   * @param executionStatus Execution Status
   */
  public static void updateExecutions(
      String cycleId,
      CSet<String> issueIds,
      CSet<String> openDefects,
      CExecutionStatus executionStatus) {
    log.debug(
        "Updating executions for cycle: {}, issueIds count: {}, openDefects count: {}, status: {}",
        cycleId,
        issueIds != null ? issueIds.size() : 0,
        openDefects != null ? openDefects.size() : 0,
        executionStatus);

    if (issueIds == null || issueIds.isEmpty()) {
      log.warn("No issue IDs provided for cycle: {}, skipping execution update", cycleId);
      return;
    }

    log.debug("Issue IDs to update: {}", issueIds);
    CScaleUtils.updateExecutionStatuses(
        cycleId, issueIds, openDefects, toZScaleExecutionStatus(executionStatus));
    log.info("Completed updating {} executions for cycle: {}", issueIds.size(), cycleId);
  }

  /**
   * Create Test Run in Scale with tests defined in cycleInfo
   *
   * @param cycleInfo Cycle Info
   */
  public static void createRun(CTestCycleInfo cycleInfo) {
    log.info(
        "Creating test run with cycle info: name={}, folderPath={}, include={}, exclude={}",
        cycleInfo.getTestCycleName(),
        cycleInfo.getTestCycleFolderPath(),
        cycleInfo.getIncludeIssueKeys(),
        cycleInfo.getExcludeIssueKeys());

    CList<CTestClassUtil.TestClassInfo> availableTests =
        CObjectUtil.clone(CTestClassUtil.getClassNameMap(true));
    log.debug("Available tests count: {}", availableTests.size());

    // Apply test suite info to filter available tests based on cycle info
    CMap<String, CSet<CTestClassUtil.TestClassInfo>> runInfo =
        CTestExecutionHelper.processFilter(cycleInfo, availableTests);
    log.debug("Filtered run info contains {} groups", runInfo.size());

    // Flatten the map to get unique test classes to add to the cycle
    CSet<CTestClassUtil.TestClassInfo> testsToAdd =
        runInfo.values().stream().flatMap(CSet::stream).collect(Collectors.toCollection(CSet::new));
    log.info("Tests to add to cycle: {} unique tests", testsToAdd.size());

    CEtlTestExecutions testExecutions = new CEtlTestExecutions();
    for (CTestClassUtil.TestClassInfo testClass : testsToAdd) {
      CEtlTestExecution execution = new CEtlTestExecution();
      execution.setIssueKey(testClass.getTestId());
      testExecutions.add(execution);
      log.trace("Added test execution: {}", testClass.getTestId());
    }

    if (testExecutions.isEmpty()) {
      log.warn(
          "Skipping creating Cycle '{}', No test executions to add", cycleInfo.getTestCycleName());
      return;
    }

    log.info("Creating test run with {} executions", testExecutions.size());
    String testRunKey =
        createRun(cycleInfo.getTestCycleName(), cycleInfo.getTestCycleFolderPath(), testExecutions);
    log.info("Created test run with key: {}", testRunKey);

    log.debug("Syncing test runs for key: {}", testRunKey);
    ScaleSyncClient.syncTestRuns(CList.of(testRunKey), List.of());
    log.info("Test run sync completed for: {}", testRunKey);
  }

  /**
   * Update executions in a cycle with given status
   *
   * @param cycleId Cycle Id
   * @param testCaseKeys Test Case Keys
   * @param openDefects Open Defects
   * @param executionStatus Execution Status
   */
  public static void updateExecutionStatuses(
      String cycleId,
      CSet<String> testCaseKeys,
      CSet<String> openDefects,
      ScaleExecutionStatus executionStatus) {
    log.info(
        "Updating execution statuses for cycle: {}, testCaseKeys count: {}, status: {}",
        cycleId,
        testCaseKeys.size(),
        executionStatus.getScaleName());
    log.debug("Test case keys: {}", testCaseKeys);
    log.debug("Open defects: {}", openDefects);

    testCaseKeys.forEach(
        testCaseKey -> updateExecutionStatus(cycleId, testCaseKey, openDefects, executionStatus));
    log.info(
        "Completed updating execution statuses for {} test cases in cycle: {}",
        testCaseKeys.size(),
        cycleId);
  }

  /**
   * Check if issue keys exist in the project
   *
   * @param issueKeys Issue Keys
   * @return Valid Issue Keys as comma separated string
   */
  public static String checkIssueKeysAreExists(CCollection<String, Collection<String>> issueKeys) {
    log.debug("Checking if issue keys exist: {}", issueKeys);

    CSet<String> invalidKeys =
        issueKeys
            .getAll(
                s ->
                    CTestClassUtil.getClassNameMap(false)
                        .hasNot(i -> CStringUtil.equalsIgnoreCase(i.getTestId(), s)))
            .toSet();

    issueKeys.removeAll(invalidKeys);

    if (!invalidKeys.isEmpty()) {
      String message =
          "Could not find following tests in package therefore we cannot execute them.\n"
              + StringUtils.join(invalidKeys, ", ");
      log.warn(message);
      log.debug("Valid issue keys remaining: {}", issueKeys);
    } else {
      log.debug("All {} issue keys are valid", issueKeys.size());
    }

    String result = StringUtils.join(issueKeys, ",");
    log.debug("Returning valid issue keys: {}", result);
    return result;
  }

  private static void updateExecutionStatus(
      String cycleId,
      String testCaseKey,
      CSet<String> openDefects,
      ScaleExecutionStatus executionStatus) {
    log.debug(
        "Updating test case {} execution status to {} in cycle {}, openDefects: {}",
        testCaseKey,
        executionStatus.getScaleName(),
        cycleId,
        openDefects);

    ScaleUpdateTestResultRequest testResult = new ScaleUpdateTestResultRequest();

    testResult.setStatus(executionStatus);
    testResult.setExecutedBy(PipelineConfigs.getExecutorName());
    testResult.setActualStartDate(CDate.now());
    testResult.setActualEndDate(CDate.now());
    testResult.setExecutionDate(CDate.now());
    testResult.setIssueLinks(openDefects);

    log.trace(
        "Test result request: status={}, executedBy={}, issueLinks count={}",
        executionStatus.getScaleName(),
        PipelineConfigs.getExecutorName(),
        openDefects != null ? openDefects.size() : 0);

    TestRunClient.updateTestResult(cycleId, testCaseKey, testResult);
    log.debug("Successfully updated test result for {} in cycle {}", testCaseKey, cycleId);
  }

  private static String createRun(
      String testCycleName, String cycleFolder, CEtlTestExecutions executions) {
    log.info(
        "Creating test run: name={}, folder={}, executions count={}",
        testCycleName,
        cycleFolder,
        executions.size());

    ProjectDto project = CoreCache.readProject(CoreConfigs.getProject());
    VersionDto version = CoreCache.readVersion(CoreConfigs.getVersion());
    log.debug(
        "Using project: {} ({}), version: {} ({})",
        project.getName(),
        project.getCode(),
        version.getName(),
        version.getCode());

    ScalePlanTestRun planTestRun = new ScalePlanTestRun();
    planTestRun.setProjectKey(project.getCode());
    planTestRun.setName(testCycleName);
    planTestRun.setPlannedStartDate(CDate.now());
    planTestRun.setPlannedEndDate(CDate.now().addDays(7));
    planTestRun.setFolder(cycleFolder);
    planTestRun.setVersion(version.getName());
    planTestRun.setItems(new HashSet<>());

    log.info("Adding {} tests to cycle '{}'", executions.size(), testCycleName);
    log.debug("Test case keys: {}", executions.mapToSet(CEtlTestExecution::getIssueKey));

    if (executions.isNotEmpty()) {
      for (CEtlTestExecution exec : executions) {
        ScalePlanExecution execution = new ScalePlanExecution();
        execution.setTestCaseKey(exec.getIssueKey());
        log.trace(
            "Processing execution for test case: {}, status: {}",
            exec.getIssueKey(),
            exec.getStatus());

        if (StringUtils.isNotEmpty(exec.getStatus())) {
          CExecutionStatus status = CExecutionStatus.valueOf(exec.getStatus());
          log.trace("Mapped execution status: {} -> Scale status", status);

          if (status.isRunning()) {
            execution.setStatus(ScaleExecutionStatus.IN_PROGRESS);
          } else if (status.isFailed()) {
            execution.setStatus(ScaleExecutionStatus.FAIL);
          } else if (status.isBlocked()) {
            execution.setStatus(ScaleExecutionStatus.BLOCKED);
          } else if (status.isPassed()) {
            execution.setStatus(ScaleExecutionStatus.PASS);
          } else {
            execution.setStatus(ScaleExecutionStatus.NOT_EXECUTED);
          }
          log.trace("Set status to: {}", execution.getStatus());
        }
        planTestRun.getItems().add(execution);
      }
    }

    log.debug(
        "Calling TestRunClient to create test run with {} items", planTestRun.getItems().size());
    String testRunKey = TestRunClient.createTestRun(planTestRun);
    log.info(
        "Successfully created test run {}[{}] with {} tests",
        testCycleName,
        testRunKey,
        executions.size());
    return testRunKey;
  }

  private static ScaleExecutionStatus toZScaleExecutionStatus(CExecutionStatus executionStatus) {
    log.trace("Converting CExecutionStatus {} to ScaleExecutionStatus", executionStatus);

    if (executionStatus.isPassed()) {
      log.trace("Converted to PASS");
      return ScaleExecutionStatus.PASS;
    }

    if (executionStatus.isFailed()) {
      log.trace("Converted to FAIL");
      return ScaleExecutionStatus.FAIL;
    }

    if (executionStatus.isBlocked()) {
      log.trace("Converted to BLOCKED");
      return ScaleExecutionStatus.BLOCKED;
    }

    if (executionStatus.isRunning()) {
      log.trace("Converted to IN_PROGRESS");
      return ScaleExecutionStatus.IN_PROGRESS;
    }

    log.error("No implementation for execution status: {}", executionStatus);
    throw new NotImplementedException("No implementation for " + executionStatus);
  }
}

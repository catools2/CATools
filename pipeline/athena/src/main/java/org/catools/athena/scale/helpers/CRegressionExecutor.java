package org.catools.athena.scale.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.atlassian.etl.scale.ScaleSyncClient;
import org.catools.athena.model.tms.TestCycleDto;
import org.catools.athena.scale.entities.CTestExecutionInfo;
import org.catools.athena.scale.listeners.CScaleExecutionListener;
import org.catools.athena.scale.utils.CScaleUtils;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.testng.listeners.CTestNGListener;
import org.catools.common.testng.utils.CTestClassUtil;
import org.jspecify.annotations.NonNull;

/** Created by akeshmiri on 10/22/2017. */
@Slf4j
@UtilityClass
public class CRegressionExecutor {

  public void process(
      CTestExecutionInfo executionsInfo,
      Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper) {
    process(executionsInfo, testClassGroupMapper, true);
  }

  public int process(
      CTestExecutionInfo executionsInfo,
      Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper,
      boolean exitAfterExecution) {
    int finalExitCode = 0;

    // Sync results to Athena before to update test runs with the new execution info,
    // to make sure we have the latest results in Athena before executing new tests and updating
    // results again.
    if (executionsInfo.isShouldSyncJira()) {
      List<String> cycleIds = collectRelatedCycleIds(executionsInfo);
      ScaleSyncClient.syncTestRuns(cycleIds, List.of());
    }

    int exitCode = processTestExecutionInfo(executionsInfo, testClassGroupMapper);
    if (exitCode != 0) {
      finalExitCode = Math.min(finalExitCode, exitCode);
    }

    // Sync results back to Athena after execution to update test runs with the new execution
    // results.
    if (executionsInfo.isShouldSyncJira()) {
      List<String> cycleIds = collectRelatedCycleIds(executionsInfo);
      ScaleSyncClient.syncTestRuns(cycleIds, List.of());
    }

    if (exitAfterExecution) System.exit(finalExitCode);

    return finalExitCode;
  }

  private static @NonNull List<String> collectRelatedCycleIds(CTestExecutionInfo executionsInfo) {
    List<String> cycleIds = new ArrayList<>();

    if (executionsInfo.getUnExecutedFromCycleIds() != null) {
      cycleIds.addAll(executionsInfo.getUnExecutedFromCycleIds());
    }

    if (executionsInfo.getUnPassedFromCycleIds() != null) {
      cycleIds.addAll(executionsInfo.getUnPassedFromCycleIds());
    }

    if (executionsInfo.getUnExecutedFromLastCycleNameWithSqlLikePatterns() != null) {
      for (String unExecutedFromLastCycleWithNamePattern :
          executionsInfo.getUnExecutedFromLastCycleNameWithSqlLikePatterns()) {
        TestCycleDto lastCycle =
            CScaleUtils.getLastCycleForDefinedVersion(unExecutedFromLastCycleWithNamePattern);
        if (lastCycle != null) {
          cycleIds.add(lastCycle.getCode());
        }
      }
    }

    if (executionsInfo.getUnPassedFromLastCycleNameWithSqlLikePatterns() != null) {
      for (String unExecutedFromLastCycleWithNamePattern :
          executionsInfo.getUnPassedFromLastCycleNameWithSqlLikePatterns()) {
        TestCycleDto lastCycle =
            CScaleUtils.getLastCycleForDefinedVersion(unExecutedFromLastCycleWithNamePattern);
        if (lastCycle != null) {
          cycleIds.add(lastCycle.getCode());
        }
      }
    }
    return cycleIds;
  }

  private int processTestExecutionInfo(
      CTestExecutionInfo executionInfo,
      Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper) {
    int exitCode = 1;

    if (executionInfo == null) return exitCode;

    try {
      CList<CTestClassUtil.TestClassInfo> availableTests = CTestClassUtil.getClassNameMap(true);
      CMap<String, CSet<CTestClassUtil.TestClassInfo>> testSuiteInfo =
          CTestExecutionHelper.processFilter(executionInfo, availableTests);

      // Update Listener
      setListener(testSuiteInfo);

      CList<CTestClassUtil.TestClassInfo> testsToRun = new CList<>();
      testSuiteInfo.values().forEach(testsToRun::addAll);

      // Execute Tests
      exitCode = CTestExecutionHelper.executeTestsAndReturnResult(testsToRun, testClassGroupMapper);
    } catch (Exception ex) {
      log.error("Failed to process test execution info " + executionInfo, ex);
    }
    return exitCode;
  }

  private void setListener(CMap<String, CSet<CTestClassUtil.TestClassInfo>> testSuiteInfo) {
    CScaleExecutionListener.init(testSuiteInfo);
    CTestNGListener.addListeners(new CScaleExecutionListener());
  }
}

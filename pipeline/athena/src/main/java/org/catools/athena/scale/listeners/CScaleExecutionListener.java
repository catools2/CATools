package org.catools.athena.scale.listeners;

import static org.catools.athena.scale.configs.Constants.DEFAULT_CYCLE;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.scale.utils.CScaleUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.testng.listeners.CITestNGListener;
import org.catools.common.testng.model.CExecutionStatus;
import org.catools.common.testng.model.CTestResult;
import org.catools.common.testng.utils.CTestClassUtil;
import org.catools.common.utils.CRetry;
import org.jetbrains.annotations.NotNull;
import org.testng.ITestResult;

@Slf4j
public class CScaleExecutionListener implements CITestNGListener {
  private static CMap<String, CSet<CTestClassUtil.TestClassInfo>> testCyclesMap;

  public static void init(CMap<String, CSet<CTestClassUtil.TestClassInfo>> executionInfo) {
    testCyclesMap = executionInfo;
  }

  public static boolean initialized() {
    return testCyclesMap != null;
  }

  private static synchronized void addResult(ITestResult result) {
    if (initialized()) {
      CMap<String, CSet<String>> cycleTests = new CHashMap<>();

      CTestResult testResult = getCycleGroup(result, cycleTests);

      for (String cycleId : cycleTests.keySet()) {
        if (DEFAULT_CYCLE.equals(cycleId)) {
          continue;
        }
        try {
          CRetry.retry(
              integer -> {
                if (testResult.getStatus().isPassed()) {
                  CScaleUtils.updateExecutions(
                      cycleId, cycleTests.get(cycleId), CSet.of(), CExecutionStatus.SUCCESS);
                } else if (testResult.getStatus().isFailed()) {
                  CSet<String> openDefects =
                      Optional.of(testResult.getOpenDefectIds()).orElse(CList.of()).toSet();
                  if (openDefects.isNotEmpty()) {
                    CScaleUtils.updateExecutions(
                        cycleId, cycleTests.get(cycleId), openDefects, CExecutionStatus.FAILURE);
                  }
                }
                return true;
              },
              10,
              15);
        } catch (Throwable t) {
          log.error(
              "Failed to update {} execution status in cycle {}", cycleId, cycleTests.get(cycleId));
          log.error("Could not persist execution result in jira.", t);
        }
      }
    }
  }

  private static @NotNull CTestResult getCycleGroup(
      ITestResult result, CMap<String, CSet<String>> cycleTests) {
    CTestResult testResult = new CTestResult(result);
    CList<String> testIds = testResult.getTestIds();

    for (String cycle : testCyclesMap.keySet()) {
      for (String testId : testIds) {
        if (testCyclesMap.get(cycle).stream()
            .anyMatch(c -> StringUtils.equalsAnyIgnoreCase(c.getTestId(), testId))) {
          cycleTests.putIfAbsent(cycle, new CSet<>());
          cycleTests.get(cycle).add(testId);
        }
      }
    }

    return testResult;
  }

  @Override
  public int priority() {
    return 0;
  }

  @Override
  public synchronized void onTestSuccess(ITestResult result) {
    addResult(result);
  }

  @Override
  public void onTestFailure(ITestResult result) {
    addResult(result);
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    addResult(result);
  }
}

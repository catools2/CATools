package org.catools.athena.scale.helpers;

import static org.catools.athena.scale.configs.Constants.*;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.model.tms.TestCycleDto;
import org.catools.athena.model.tms.TestExecutionDto;
import org.catools.athena.rest.feign.core.client.CoreClient;
import org.catools.athena.rest.feign.tms.clients.TmsClient;
import org.catools.athena.scale.entities.CTestExecutionInfo;
import org.catools.athena.scale.utils.CScaleUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.date.CDate;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.io.CFile;
import org.catools.common.testng.CTestNGProcessor;
import org.catools.common.testng.utils.CTestClassUtil;
import org.catools.common.utils.CRegExUtil;
import org.catools.common.utils.CStringUtil;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class CTestExecutionHelper {

  /**
   * Apply filters in the order of Include Qualified Matches, Include Issue Keys, UnExecuted From
   * Cycle Ids, UnPassed From Cycle Ids, UnExecuted From Last Cycle Name With Sql Like Patterns,
   * UnPassed From Last Cycle Name With Sql Like Patterns, Exclude Qualified Matches, Exclude Issue
   * Keys
   *
   * @param filter
   * @param tests
   */
  public static CMap<String, CSet<CTestClassUtil.TestClassInfo>> processFilter(
      CTestExecutionInfo filter, CList<CTestClassUtil.TestClassInfo> tests) {
    ensureAllTestsHaveClassName(tests);
    CMap<String, CSet<CTestClassUtil.TestClassInfo>> testCycleMap = new CHashMap<>();

    // Initialize testCycleMap with default cycle to hold tests without specific cycle,
    // this is for better organization of tests and also to make sure the filters are applied in the
    // right order
    testCycleMap.put(DEFAULT_CYCLE, new CSet<>());

    applyInclusionFilters(filter, tests, testCycleMap);
    applyExclusionFilters(filter, tests, testCycleMap);

    if (filter.isTestShouldHaveId()) {
      for (String s : testCycleMap.keySet()) {
        ensureAllTestsHaveTestId(testCycleMap.get(s));
      }
    }
    int totalSize =
        testCycleMap.values().stream().reduce(0, (sum, set) -> sum + set.size(), Integer::sum);

    log.debug("Total Tests: {}", totalSize);
    return testCycleMap;
  }

  /**
   * Ensure all tests have valid class names, if not, throw exception with details
   *
   * @param testClassInfo
   */
  public static void ensureAllTestsHaveClassName(
      CList<CTestClassUtil.TestClassInfo> testClassInfo) {
    CList<CTestClassUtil.TestClassInfo> invalidTests =
        testClassInfo.getAll(ti -> StringUtils.isBlank(ti.getClassName()));

    if (invalidTests.isNotEmpty()) {
      throw new InvalidParameterException(
          "Failed to process with cycle creation, Some records have no valid class names:\n "
              + invalidTests.join("\n"));
    }
  }

  /**
   * Apply inclusion filters to add tests to testCycleMap, the filters include Include Qualified
   * Matches, Include Issue Keys, UnExecuted From Cycle Ids, UnPassed From Cycle Ids, UnExecuted
   * From Last Cycle Name With Sql Like Patterns, UnPassed From Last Cycle Name With Sql Like
   * Patterns
   *
   * @param filter
   * @param tests
   * @param testCycleMap
   */
  public static void applyInclusionFilters(
      CTestExecutionInfo filter,
      CList<CTestClassUtil.TestClassInfo> tests,
      CMap<String, CSet<CTestClassUtil.TestClassInfo>> testCycleMap) {
    if (filter.getUnExecutedFromCycleIds() != null) {
      for (String unExecutedFromCycleId : filter.getUnExecutedFromCycleIds()) {
        log.debug(
            "Applying UnExecuted From Cycle Id {}. Total Tests: {}",
            unExecutedFromCycleId,
            tests.size());
        TestCycleDto lastCycle = CScaleUtils.getCycleById(unExecutedFromCycleId);
        extractClasses(
            getUnExecutedTestByCycleId(lastCycle), testCycleMap, lastCycle.getCode(), tests);
      }
    }

    if (filter.getUnPassedFromCycleIds() != null) {
      for (String unPassedFromCycleId : filter.getUnPassedFromCycleIds()) {
        log.debug(
            "Applying UnPassed From Cycle Id {}. Total Tests: {}",
            unPassedFromCycleId,
            tests.size());
        TestCycleDto lastCycle = CScaleUtils.getCycleById(unPassedFromCycleId);
        extractClasses(
            getUnPassedTestByCycleId(lastCycle), testCycleMap, lastCycle.getCode(), tests);
      }
    }

    if (filter.getUnExecutedFromLastCycleNameWithSqlLikePatterns() != null) {
      for (String unExecutedFromLastCycleWithNamePattern :
          filter.getUnExecutedFromLastCycleNameWithSqlLikePatterns()) {
        log.debug(
            "Applying UnExecuted From Last Cycle With Name Pattern {}. Total Tests: {}",
            unExecutedFromLastCycleWithNamePattern,
            tests.size());
        TestCycleDto lastCycle =
            CScaleUtils.getLastCycleForDefinedVersion(unExecutedFromLastCycleWithNamePattern);
        CVerify.Bool.isTrue(
            lastCycle != null,
            "Cycle with name following pattern %s found.",
            unExecutedFromLastCycleWithNamePattern);
        extractClasses(
            getUnExecutedTestByCycleId(lastCycle), testCycleMap, lastCycle.getCode(), tests);
      }
    }

    if (filter.getUnPassedFromLastCycleNameWithSqlLikePatterns() != null) {
      for (String unPassedFromLastCycleWithNamePattern :
          filter.getUnPassedFromLastCycleNameWithSqlLikePatterns()) {
        log.debug(
            "Applying UnPassed From Last Cycle With Name Pattern {}. Total Tests: {}",
            unPassedFromLastCycleWithNamePattern,
            tests.size());
        TestCycleDto lastCycle =
            CScaleUtils.getLastCycleForDefinedVersion(unPassedFromLastCycleWithNamePattern);
        CVerify.Bool.isTrue(
            lastCycle != null,
            "Cycle with name following pattern %s found.",
            unPassedFromLastCycleWithNamePattern);
        extractClasses(
            getUnPassedTestByCycleId(lastCycle), testCycleMap, lastCycle.getCode(), tests);
      }
    }

    CSet<String> includeQualifiedMatches = filter.getIncludeQualifiedMatches();
    if (includeQualifiedMatches != null && includeQualifiedMatches.isNotEmpty()) {
      log.debug(
          "Applying Include Qualified Matches {}. Total Tests: {}",
          includeQualifiedMatches,
          tests.size());
      Set<String> ids =
          tests
              .getAll(
                  t -> includeQualifiedMatches.has(p -> CRegExUtil.isMatch(t.getClassName(), p)))
              .mapToSet(CTestClassUtil.TestClassInfo::getTestId);

      extractClasses(ids, testCycleMap, DEFAULT_CYCLE, tests);
    }

    CSet<String> includeIssueKeys = filter.getIncludeIssueKeys();
    if (includeIssueKeys != null && includeIssueKeys.isNotEmpty()) {
      log.debug("Applying Include Issue Keys {}. Total Tests: {}", includeIssueKeys, tests.size());
      CScaleUtils.checkIssueKeysAreExists(includeIssueKeys);
      Set<String> ids =
          tests
              .getAll(
                  t -> includeIssueKeys.has(p -> CStringUtil.equalsAnyIgnoreCase(t.getTestId(), p)))
              .mapToSet(CTestClassUtil.TestClassInfo::getTestId);
      extractClasses(ids, testCycleMap, DEFAULT_CYCLE, tests);
    }

    String query = filter.getIncludeQuery();
    if (CStringUtil.isNotBlank(query)) {
      log.debug("Applying Include Query {}. Total Tests: {}", query, tests.size());
      Optional<Set<Object>> queryResult = CoreClient.queryRecords(query);
      Set<String> ids =
          queryResult.orElse(CSet.of()).stream().map(String::valueOf).collect(Collectors.toSet());
      extractClasses(ids, testCycleMap, DEFAULT_CYCLE, tests);
    }
  }

  /**
   * Apply exclusion filters to remove tests from testCycleMap, the filters include Exclude
   * Qualified Matches, Exclude Issue Keys
   *
   * @param filter
   * @param tests
   * @param testCycleMap
   */
  public static void applyExclusionFilters(
      CTestExecutionInfo filter,
      CList<CTestClassUtil.TestClassInfo> tests,
      CMap<String, CSet<CTestClassUtil.TestClassInfo>> testCycleMap) {
    CSet<String> excludeQualifiedMatches = filter.getExcludeQualifiedMatches();
    if (excludeQualifiedMatches != null && excludeQualifiedMatches.isNotEmpty()) {
      log.debug(
          "Applying Exclude Qualified Matches {}. Total Tests: {}",
          excludeQualifiedMatches,
          tests.size());
      for (String cycle : testCycleMap.keySet()) {
        testCycleMap
            .get(cycle)
            .removeIf(
                t -> excludeQualifiedMatches.has(p -> CRegExUtil.isMatch(t.getClassName(), p)));
      }
    }

    CSet<String> excludeIssueKeys = filter.getExcludeIssueKeys();
    if (excludeIssueKeys != null && excludeIssueKeys.isNotEmpty()) {
      log.debug("Applying Exclude Issue Keys {}. Total Tests: {}", excludeIssueKeys, tests.size());
      CScaleUtils.checkIssueKeysAreExists(excludeIssueKeys);
      for (String cycle : testCycleMap.keySet()) {
        testCycleMap
            .get(cycle)
            .removeIf(
                t -> excludeIssueKeys.has(p -> CStringUtil.equalsIgnoreCase(t.getTestId(), p)));
      }
    }

    String query = filter.getExcludeQuery();
    if (CStringUtil.isNotBlank(query)) {
      log.debug("Applying Exclude Query {}. Total Tests: {}", query, tests.size());
      Optional<Set<Object>> queryResult = CoreClient.queryRecords(query);
      Set<String> ids =
          queryResult.orElse(CSet.of()).stream().map(String::valueOf).collect(Collectors.toSet());
      for (String cycle : testCycleMap.keySet()) {
        testCycleMap.get(cycle).removeIf(t -> ids.contains(t.getTestId()));
      }
    }
  }

  /**
   * Execute tests and return the result, if no test to execute, return 8 which is the code for "No
   * tests were found by TestNG"
   *
   * @param testsToExecute
   * @param testClassGroupMapper
   * @return
   */
  public static int executeTestsAndReturnResult(
      CList<CTestClassUtil.TestClassInfo> testsToExecute,
      Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper) {
    if (testsToExecute.isEmpty()) return 8;
    return CTestNGProcessor.processTestClasses(
        testsToExecute.mapToSet(CTestClassUtil.TestClassInfo::getClassName),
        CFile.fromOutput(CDate.now().toTimeStampForFileName()).getAbsolutePath(),
        testClassGroupMapper,
        null);
  }

  private static void extractClasses(
      Set<String> lastCycle,
      CMap<String, CSet<CTestClassUtil.TestClassInfo>> testCycleMap,
      String cycleId,
      CList<CTestClassUtil.TestClassInfo> testClassInfo) {
    Set<String> testIds = lastCycle;
    testCycleMap.putIfAbsent(cycleId, new CSet<>());
    testCycleMap.get(cycleId).addAll(getTestClasses(testClassInfo, testIds));
  }

  private static @NotNull Set<CTestClassUtil.TestClassInfo> getTestClasses(
      CList<CTestClassUtil.TestClassInfo> testClassInfo, Set<String> testIds) {
    return testClassInfo.stream()
        .filter(tc -> testIds.contains(tc.getTestId()))
        .collect(Collectors.toSet());
  }

  private static Set<String> getUnExecutedTestByCycleId(TestCycleDto cycle) {
    String unexecutedStatus = getStatusCode("NOT_EXECUTED");
    return cycle.getTestExecutions().stream()
        .filter(e -> unexecutedStatus.equalsIgnoreCase(e.getStatus()))
        .map(TestExecutionDto::getItem)
        .collect(Collectors.toSet());
  }

  private static Set<String> getUnPassedTestByCycleId(TestCycleDto cycle) {
    String pass = getStatusCode("PASS");
    return cycle.getTestExecutions().stream()
        .filter(e -> !pass.equalsIgnoreCase(e.getStatus()))
        .map(TestExecutionDto::getItem)
        .collect(Collectors.toSet());
  }

  private static String getStatusCode(String notExecutedStatus) {
    return TmsClient.searchStatus(notExecutedStatus)
        .orElseThrow(() -> new RuntimeException("Status " + notExecutedStatus + " not found"))
        .getCode();
  }

  private static void ensureAllTestsHaveTestId(CSet<CTestClassUtil.TestClassInfo> testClassInfo) {
    CList<CTestClassUtil.TestClassInfo> invalidTests =
        testClassInfo.getAll(ti -> StringUtils.isBlank(ti.getTestId()));

    if (invalidTests.isNotEmpty()) {
      throw new InvalidParameterException(
          "Failed to process with cycle creation, CTestId missed and should be added to some records:\n "
              + invalidTests.join("\n"));
    }
  }
}

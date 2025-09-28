package org.catools.common.testng.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.testng.ITestNGMethod;

import java.util.stream.Stream;

/**
 * A specialized collection that extends {@link CList} to manage a list of {@link CTestResult} objects.
 * This class provides convenient methods for querying, filtering, and organizing test results
 * from TestNG test executions.
 * 
 * <p>The class offers functionality to:
 * <ul>
 *   <li>Find test results by TestNG method or test ID</li>
 *   <li>Check test execution status</li>
 *   <li>Collect all issue IDs from test results</li>
 *   <li>Group test results by execution status</li>
 * </ul>
 * 
 * @see CTestResult
 * @see CExecutionStatus
 * @see CList
 */
public class CTestResults extends CList<CTestResult> {
  /**
   * Creates an empty CTestResults collection.
   * 
   * <p>Example:
   * <pre>
   * CTestResults results = new CTestResults();
   * // results.size() == 0
   * results.add(new CTestResult());
   * // results.size() == 1
   * </pre>
   */
  public CTestResults() {
  }

  /**
   * Creates a CTestResults collection initialized with the provided test results.
   * 
   * @param c varargs array of {@link CTestResult} objects to add to the collection
   * 
   * <p>Example:
   * <pre>
   * CTestResult result1 = new CTestResult();
   * CTestResult result2 = new CTestResult();
   * CTestResults results = new CTestResults(result1, result2);
   * // results.size() == 2
   * </pre>
   */
  public CTestResults(CTestResult... c) {
    super(c);
  }

  /**
   * Creates a CTestResults collection from a Stream of CTestResult objects.
   * 
   * @param stream a {@link Stream} of {@link CTestResult} objects
   * 
   * <p>Example:
   * <pre>
   * Stream&lt;CTestResult&gt; stream = Arrays.stream(testResultArray);
   * CTestResults results = new CTestResults(stream);
   * 
   * // Or from a filtered stream
   * CTestResults passedResults = new CTestResults(
   *     allResults.stream().filter(r -&gt; r.getStatus().isPassed())
   * );
   * </pre>
   */
  public CTestResults(Stream<CTestResult> stream) {
    super(stream);
  }

  /**
   * Creates a CTestResults collection from an Iterable of CTestResult objects.
   * 
   * @param iterable an {@link Iterable} collection of {@link CTestResult} objects
   * 
   * <p>Example:
   * <pre>
   * List&lt;CTestResult&gt; testList = Arrays.asList(result1, result2, result3);
   * CTestResults results = new CTestResults(testList);
   * 
   * // Or from another CTestResults
   * CTestResults copy = new CTestResults(originalResults);
   * </pre>
   */
  public CTestResults(Iterable<CTestResult> iterable) {
    super(iterable);
  }

  /**
   * Retrieves the test result for a specific TestNG method, or null if not found.
   * 
   * <p>This method searches for a test result that matches both the class name and method name
   * of the provided TestNG method.
   * 
   * @param testNGMethod the {@link ITestNGMethod} to search for
   * @return the matching {@link CTestResult} or null if no match is found
   * 
   * <p>Example:
   * <pre>
   * // Given a TestNG method from test execution
   * ITestNGMethod method = testResult.getMethod();
   * 
   * CTestResults results = new CTestResults(result1, result2, result3);
   * CTestResult found = results.getTestResultOrNull(method);
   * 
   * if (found != null) {
   *     System.out.println("Test status: " + found.getStatus());
   * } else {
   *     System.out.println("Test result not found");
   * }
   * </pre>
   */
  @JsonIgnore
  public CTestResult getTestResultOrNull(ITestNGMethod testNGMethod) {
    return getFirstOrNull(
        test ->
            test.getClassName().equals(testNGMethod.getTestClass().getName())
                && test.getMethodName().equals(testNGMethod.getMethodName()));
  }

  /**
   * Checks if a specific TestNG method has passed based on the test results in this collection.
   * 
   * <p>This method searches for a test result matching the provided method and checks if its
   * status indicates a passed state.
   * 
   * @param method the {@link ITestNGMethod} to check
   * @return true if the test method passed, false if it failed, was skipped, or not found
   * 
   * <p>Example:
   * <pre>
   * // During TestNG execution
   * CTestResults results = new CTestResults();
   * // ... populate results from test execution
   * 
   * ITestNGMethod testMethod = testResult.getMethod();
   * 
   * if (results.isPassed(testMethod)) {
   *     System.out.println("Test " + testMethod.getMethodName() + " passed!");
   * } else {
   *     System.out.println("Test " + testMethod.getMethodName() + " did not pass");
   * }
   * </pre>
   */
  @JsonIgnore
  public boolean isPassed(ITestNGMethod method) {
    CTestResult testResultByMethod =
        getFirstOrNull(
            test ->
                test.getClassName().equals(method.getTestClass().getName())
                    && test.getMethodName().equals(method.getMethodName()));
    return testResultByMethod != null && testResultByMethod.getStatus().isPassed();
  }

  /**
   * Retrieves a test result by its test ID, or null if not found.
   * 
   * <p>This method searches for a test result that contains the specified ID in its test IDs collection.
   * Test IDs are typically used to link test cases to external test management systems or issue trackers.
   * 
   * @param id the test ID to search for (e.g., "TEST-123", "JIRA-456")
   * @return the matching {@link CTestResult} or null if no test result contains this ID
   * 
   * <p>Example:
   * <pre>
   * CTestResults results = new CTestResults();
   * // ... populate results where some tests have IDs like "TEST-001", "TEST-002"
   * 
   * CTestResult testResult = results.getTestResultByIdOrNull("TEST-001");
   * if (testResult != null) {
   *     System.out.println("Found test: " + testResult.getMethodName());
   *     System.out.println("Status: " + testResult.getStatus());
   * } else {
   *     System.out.println("No test found with ID: TEST-001");
   * }
   * </pre>
   */
  @JsonIgnore
  public CTestResult getTestResultByIdOrNull(String id) {
    return getFirstOrNull(test -> test.getTestIds() != null && test.getTestIds().contains(id));
  }

  /**
   * Collects and returns all unique issue IDs from all test results in this collection.
   * 
   * <p>This method aggregates test IDs from all test results, which can be useful for 
   * reporting, tracking coverage, or integration with external systems like JIRA or other
   * test management tools.
   * 
   * @return a {@link CSet} containing all unique test/issue IDs found across all test results
   * 
   * <p>Example:
   * <pre>
   * CTestResults results = new CTestResults();
   * // Assume test results have various IDs:
   * // result1 has IDs: ["TEST-001", "JIRA-123"]
   * // result2 has IDs: ["TEST-002", "JIRA-123"] 
   * // result3 has IDs: ["TEST-003"]
   * 
   * CSet&lt;String&gt; allIds = results.getAllIssueIds();
   * // allIds contains: ["TEST-001", "TEST-002", "TEST-003", "JIRA-123"]
   * 
   * System.out.println("Total unique test IDs: " + allIds.size());
   * for (String id : allIds) {
   *     System.out.println("ID: " + id);
   * }
   * </pre>
   */
  public CSet<String> getAllIssueIds() {
    CSet<String> ids = new CSet<>();
    for (CTestResult result : this) {
      ids.addAll(result.getTestIds());
    }
    return ids;
  }

  /**
   * Groups all test results by their execution status and returns a map of status to results.
   * 
   * <p>This method organizes test results into separate collections based on their execution status
   * (SUCCESS, FAILURE, SKIP, etc.), which is useful for generating reports, statistics, or 
   * performing batch operations on tests with the same status.
   * 
   * @return a {@link CHashMap} where keys are {@link CExecutionStatus} values and values are
   *         {@link CTestResults} collections containing tests with that status
   * 
   * <p>Example:
   * <pre>
   * CTestResults allResults = new CTestResults();
   * // ... populate with test results having various statuses
   * 
   * CHashMap&lt;CExecutionStatus, CTestResults&gt; statusMap = allResults.getStatusMap();
   * 
   * CTestResults passedTests = statusMap.get(CExecutionStatus.SUCCESS);
   * CTestResults failedTests = statusMap.get(CExecutionStatus.FAILURE);
   * CTestResults skippedTests = statusMap.get(CExecutionStatus.SKIP);
   * 
   * System.out.println("Passed: " + (passedTests != null ? passedTests.size() : 0));
   * System.out.println("Failed: " + (failedTests != null ? failedTests.size() : 0));
   * System.out.println("Skipped: " + (skippedTests != null ? skippedTests.size() : 0));
   * 
   * // Process only failed tests
   * if (failedTests != null) {
   *     for (CTestResult failed : failedTests) {
   *         System.out.println("Failed test: " + failed.getMethodName());
   *     }
   * }
   * </pre>
   */
  public CHashMap<CExecutionStatus, CTestResults> getStatusMap() {
    CHashMap<CExecutionStatus, CTestResults> keys = new CHashMap<>();
    for (CTestResult result : this) {
      keys.putIfAbsent(result.getStatus(), new CTestResults());
      keys.get(result.getStatus()).add(result);
    }
    return keys;
  }
}

package org.catools.common.testng.listeners;

import static org.catools.common.testng.utils.CTestClassUtil.noRetryLeft;

import org.catools.common.collections.CList;
import org.catools.common.io.CFile;
import org.catools.common.testng.model.CTestResult;
import org.catools.common.testng.model.CTestResults;
import org.catools.common.testng.utils.CRetryAnalyzer;
import org.catools.common.utils.CJsonUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

/**
 * A TestNG listener that collects and manages test execution results. This listener tracks test
 * outcomes, handles retry logic, and exports results to JSON format.
 *
 * <p>The listener implements the {@link CITestNGListener} interface and provides functionality to:
 *
 * <ul>
 *   <li>Collect test results from all test methods
 *   <li>Handle retry analysis for failed tests
 *   <li>Export execution results to a JSON file
 *   <li>Skip already passed tests on subsequent runs
 * </ul>
 *
 * <p><strong>Usage Example:</strong>
 *
 * <pre>{@code
 * // In your TestNG configuration or programmatically
 * TestNG testNG = new TestNG();
 * testNG.addListener(new CExecutionResultListener());
 * testNG.run();
 *
 * // Or via XML configuration
 * <suite>
 *   <listeners>
 *     <listener class-name="org.catools.common.testng.listeners.CExecutionResultListener"/>
 *   </listeners>
 * </suite>
 * }</pre>
 *
 * @see CITestNGListener
 * @see CTestResults
 * @see CTestResult
 */
public class CExecutionResultListener implements CITestNGListener {
  private final CTestResults executionResults = new CTestResults();

  /**
   * Returns the priority of this listener in the execution order. Lower values indicate higher
   * priority.
   *
   * <p>This listener has a priority of 0, meaning it will execute before listeners with higher
   * priority values.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * CExecutionResultListener listener = new CExecutionResultListener();
   * int priority = listener.priority(); // Returns 0
   * }</pre>
   *
   * @return the priority value (0 for this listener)
   */
  @Override
  public int priority() {
    return 0;
  }

  /**
   * Called when a test context starts execution. This method performs initialization tasks
   * including:
   *
   * <ul>
   *   <li>Filtering out already passed tests to avoid re-execution
   *   <li>Resetting retry analyzers for tests that need to be retried
   * </ul>
   *
   * <p>The method optimizes test execution by skipping tests that have already passed in previous
   * runs, and ensures proper retry behavior for tests that have retry logic configured.
   *
   * <p><strong>Example scenario:</strong>
   *
   * <pre>{@code
   * // Given a test suite with methods A, B, C where:
   * // - Method A previously passed
   * // - Method B previously failed and has retry logic
   * // - Method C is new
   *
   * // onStart will:
   * // 1. Remove method A from execution (already passed)
   * // 2. Reset retry counter for method B
   * // 3. Keep method C for normal execution
   * }</pre>
   *
   * @param context the test context containing suite and method information
   */
  @Override
  public void onStart(ITestContext context) {
    CList<ITestNGMethod> allMethods = new CList<>(context.getSuite().getAllMethods());
    allMethods.removeIf(
        m -> {
          CTestResult testResult = executionResults.getTestResultOrNull(m);
          return testResult != null && testResult.getStatus().isPassed();
        });

    for (ITestNGMethod method : allMethods) {
      if (method == null) continue;

      CTestResult testResult = executionResults.getTestResultOrNull(method);

      if (testResult == null || testResult.getOrigin() == null) continue;

      IRetryAnalyzer retryAnalyzer = method.getRetryAnalyzer(testResult.getOrigin());
      if (retryAnalyzer instanceof CRetryAnalyzer analyzer) {
        analyzer.resetCount();
      }
    }
  }

  /**
   * Called when a test method completes successfully. This method immediately records the
   * successful test result to the collection.
   *
   * <p>The method is synchronized to ensure thread-safety when multiple tests are running
   * concurrently.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * // When a test like this passes:
   * @Test
   * public void testUserLogin() {
   *     // test implementation that passes
   * }
   *
   * // The listener will:
   * // 1. Create a CTestResult from the ITestResult
   * // 2. Add it to the executionResults collection
   * // 3. Mark it as successful
   * }</pre>
   *
   * @param result the test result containing execution details and outcome
   */
  @Override
  public synchronized void onTestSuccess(ITestResult result) {
    addResult(result);
  }

  /**
   * Called when a test method fails. This method only records the failure if no retries are
   * remaining for the test.
   *
   * <p>The method uses {@link
   * org.catools.common.testng.utils.CTestClassUtil#noRetryLeft(ITestResult)} to determine if the
   * test has exhausted all retry attempts before recording the failure.
   *
   * <p>The method is synchronized to ensure thread-safety when multiple tests are running
   * concurrently.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * // Given a test with retry analyzer configured for 3 attempts:
   * @Test(retryAnalyzer = CRetryAnalyzer.class)
   * public void testDatabaseConnection() {
   *     // test implementation that might fail
   * }
   *
   * // The listener behavior:
   * // Attempt 1 fails: onTestFailure called, but result not recorded (retries left)
   * // Attempt 2 fails: onTestFailure called, but result not recorded (retries left)
   * // Attempt 3 fails: onTestFailure called, result IS recorded (no retries left)
   * }</pre>
   *
   * @param result the test result containing failure details and retry information
   */
  @Override
  public synchronized void onTestFailure(ITestResult result) {
    if (noRetryLeft(result)) {
      addResult(result);
    }
  }

  /**
   * Called when a test method is skipped. This method only records the skip if no retries are
   * remaining for the test.
   *
   * <p>Tests can be skipped for various reasons including:
   *
   * <ul>
   *   <li>Failed dependencies (dependsOnMethods)
   *   <li>Failed groups (dependsOnGroups)
   *   <li>Configuration method failures
   *   <li>Explicit skip conditions
   * </ul>
   *
   * <p>The method is synchronized to ensure thread-safety when multiple tests are running
   * concurrently.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * // Test with dependency that might be skipped:
   * @Test
   * public void testLogin() {
   *     // login test that fails
   * }
   *
   * @Test(dependsOnMethods = "testLogin")
   * public void testUserProfile() {
   *     // this will be skipped if testLogin fails
   * }
   *
   * // When testLogin fails, testUserProfile is skipped
   * // The listener will record the skip only if no retries are configured
   * }</pre>
   *
   * @param result the test result containing skip details and dependency information
   */
  @Override
  public synchronized void onTestSkipped(ITestResult result) {
    if (noRetryLeft(result)) {
      addResult(result);
    }
  }

  /**
   * Called when a test fails but is within the success percentage threshold. This method only
   * records the result if no retries are remaining for the test.
   *
   * <p>This callback is triggered when a test method is configured with a {@code successPercentage}
   * attribute and the failure rate is within the acceptable threshold. For example, if a test is
   * configured to pass with 70% success rate and it fails but overall success is above 70%.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * // Test configured with success percentage:
   * @Test(invocationCount = 10, successPercentage = 70)
   * public void testNetworkConnection() {
   *     // test that might be flaky due to network conditions
   *     // If 7 out of 10 invocations pass, the test is considered successful
   * }
   *
   * // When individual invocations fail but overall success >= 70%:
   * // This method is called instead of onTestFailure
   * }</pre>
   *
   * @param result the test result containing failure details and success percentage information
   */
  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    if (noRetryLeft(result)) {
      addResult(result);
    }
  }

  /**
   * Called when the entire test execution completes. This method exports all collected test results
   * to a JSON file for persistence and later analysis.
   *
   * <p>The results are written to a file named "CTestResultCollection.json" in the output
   * directory. The JSON format allows for easy integration with reporting tools and test result
   * analysis systems.
   *
   * <p><strong>Example output structure:</strong>
   *
   * <pre>{@code
   * {
   *   "testResults": [
   *     {
   *       "methodName": "testUserLogin",
   *       "className": "com.example.UserTest",
   *       "status": "PASSED",
   *       "startTime": 1642678800000,
   *       "endTime": 1642678801000,
   *       "duration": 1000
   *     },
   *     {
   *       "methodName": "testDataValidation",
   *       "className": "com.example.DataTest",
   *       "status": "FAILED",
   *       "startTime": 1642678802000,
   *       "endTime": 1642678803000,
   *       "duration": 1000,
   *       "throwable": "AssertionError: Expected value was 10 but got 5"
   *     }
   *   ]
   * }
   * }</pre>
   *
   * <p>The generated file can be used for:
   *
   * <ul>
   *   <li>Test result reporting and dashboards
   *   <li>Trend analysis across test runs
   *   <li>Integration with CI/CD pipelines
   *   <li>Test result archiving and auditing
   * </ul>
   */
  @Override
  public void onExecutionFinish() {
    CJsonUtil.write(CFile.fromOutput("./CTestResultCollection.json"), executionResults);
  }

  private boolean addResult(ITestResult result) {
    return executionResults.add(new CTestResult(result));
  }
}

package org.catools.common.testng.utils;

import org.catools.common.testng.CTestNGConfigs;
import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

/**
 * Custom retry analyzer for TestNG tests that extends the built-in RetryAnalyzerCount. This class
 * provides functionality to automatically retry failed tests based on a configurable retry count
 * obtained from CTestNGConfigs.
 *
 * <p>The retry analyzer allows tests to be re-executed when they fail, which is useful for handling
 * flaky tests or transient failures in test environments.
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>{@code
 * @Test(retryAnalyzer = CRetryAnalyzer.class)
 * public void testWithRetry() {
 *     // Test implementation that may need retries
 *     someFlakeyOperation();
 * }
 * }</pre>
 *
 * @see org.testng.util.RetryAnalyzerCount
 * @see org.catools.common.testng.CTestNGConfigs
 * @author CATools
 */
public class CRetryAnalyzer extends RetryAnalyzerCount {
  /**
   * Constructs a new CRetryAnalyzer and initializes the retry count. The retry count is obtained
   * from the configuration and set automatically.
   *
   * <h3>Example:</h3>
   *
   * <pre>{@code
   * CRetryAnalyzer analyzer = new CRetryAnalyzer();
   * // The analyzer is now ready to use with the configured retry count
   * }</pre>
   */
  public CRetryAnalyzer() {
    resetCount();
  }

  /**
   * Resets the retry count to the value configured in CTestNGConfigs. This method fetches the retry
   * count from the configuration and sets it as the maximum number of retries allowed for this
   * analyzer.
   *
   * <h3>Example:</h3>
   *
   * <pre>{@code
   * CRetryAnalyzer analyzer = new CRetryAnalyzer();
   * analyzer.resetCount(); // Resets to configured retry count
   *
   * // If configuration has retry count = 3, analyzer will retry up to 3 times
   * }</pre>
   *
   * @see org.catools.common.testng.CTestNGConfigs#getTestRetryCount()
   */
  public void resetCount() {
    super.setCount(CTestNGConfigs.getTestRetryCount());
  }

  /**
   * Checks if there are any retry executions left for the current test. Returns true if the retry
   * count is greater than -1, indicating that more retries are available.
   *
   * <h3>Example:</h3>
   *
   * <pre>{@code
   * CRetryAnalyzer analyzer = new CRetryAnalyzer();
   *
   * // Initially, if retry count is 3
   * boolean hasRetries = analyzer.anyExecutionLeft(); // true
   *
   * // After all retries are exhausted
   * // hasRetries would be false
   * }</pre>
   *
   * @return true if there are retry executions remaining, false otherwise
   */
  public boolean anyExecutionLeft() {
    return super.getCount() > -1;
  }

  /**
   * Determines if the current execution is the last retry attempt. Returns true when the retry
   * count is less than 1, indicating that this is the final retry attempt.
   *
   * <h3>Example:</h3>
   *
   * <pre>{@code
   * CRetryAnalyzer analyzer = new CRetryAnalyzer();
   *
   * // During test execution, check if this is the last retry
   * if (analyzer.isLastRetry()) {
   *     // Perform cleanup or special handling for final attempt
   *     System.out.println("This is the last retry attempt");
   * }
   * }</pre>
   *
   * @return true if this is the last retry attempt, false otherwise
   */
  public boolean isLastRetry() {
    return super.getCount() < 1;
  }

  /**
   * Determines whether a failed test method should be retried. This implementation always returns
   * true, meaning all failed tests will be retried up to the configured retry count limit.
   *
   * <p>This method is called by TestNG framework when a test fails. The actual retry logic and
   * count management is handled by the parent RetryAnalyzerCount class.
   *
   * <h3>Example:</h3>
   *
   * <pre>{@code
   * // This method is called internally by TestNG
   * // When a test fails, TestNG calls this method to decide if retry is needed
   *
   * @Test(retryAnalyzer = CRetryAnalyzer.class)
   * public void flakeyTest() {
   *     // If this test fails, retryMethod() will be called
   *     // Since it returns true, the test will be retried
   *     performSomeOperation();
   * }
   * }</pre>
   *
   * @param iTestResult the result of the failed test execution
   * @return true to indicate the test should be retried, false otherwise
   * @see org.testng.util.RetryAnalyzerCount#retryMethod(ITestResult)
   */
  @Override
  public boolean retryMethod(ITestResult iTestResult) {
    return true;
  }
}

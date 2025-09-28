package org.catools.common.testng.listeners;

import lombok.Getter;
import org.catools.common.collections.CHashMap;
import org.catools.common.testng.model.CExecutionStatus;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.internal.IResultListener;

/**
 * A TestNG listener that tracks execution statistics for test methods during test suite execution.
 * 
 * <p>This listener implements both {@link ISuiteListener} and {@link IResultListener} to monitor
 * the lifecycle of test execution and maintain real-time statistics about test results. It tracks
 * the total number of tests and categorizes them by their execution status (passed, failed, 
 * skipped, running, waiting).</p>
 * 
 * <p>The listener maintains a thread-safe collection of method signatures mapped to their current
 * execution status, providing static getter methods to access current statistics at any point
 * during test execution.</p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>Real-time tracking of test execution statistics</li>
 *   <li>Thread-safe operations for concurrent test execution</li>
 *   <li>Automatic categorization of tests by execution status</li>
 *   <li>Support for dynamic test removal and statistics updates</li>
 * </ul>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Basic Usage in TestNG Configuration:</h4>
 * <pre>{@code
 * // In testng.xml
 * <suite name="TestSuite">
 *   <listeners>
 *     <listener class-name="org.catools.common.testng.listeners.CExecutionStatisticListener"/>
 *   </listeners>
 *   <test name="Test">
 *     <classes>
 *       <class name="com.example.MyTestClass"/>
 *     </classes>
 *   </test>
 * </suite>
 * }</pre>
 * 
 * <h4>Programmatic Usage:</h4>
 * <pre>{@code
 * // Add listener programmatically
 * TestNG testng = new TestNG();
 * testng.addListener(new CExecutionStatisticListener());
 * 
 * // Access statistics during or after test execution
 * int totalTests = CExecutionStatisticListener.getTotal();
 * int passedTests = CExecutionStatisticListener.getPassed();
 * int failedTests = CExecutionStatisticListener.getFailed();
 * int skippedTests = CExecutionStatisticListener.getSkipped();
 * int runningTests = CExecutionStatisticListener.getRunning();
 * int waitingTests = CExecutionStatisticListener.getWaiting();
 * 
 * System.out.println("Test Execution Summary:");
 * System.out.println("Total: " + totalTests);
 * System.out.println("Passed: " + passedTests);
 * System.out.println("Failed: " + failedTests);
 * System.out.println("Skipped: " + skippedTests);
 * System.out.println("Running: " + runningTests);
 * System.out.println("Waiting: " + waitingTests);
 * }</pre>
 * 
 * <h4>Real-time Monitoring:</h4>
 * <pre>{@code
 * // Monitor test progress in a separate thread
 * public class TestProgressMonitor extends Thread {
 *   @Override
 *   public void run() {
 *     while (CExecutionStatisticListener.getRunning() > 0 || 
 *            CExecutionStatisticListener.getWaiting() > 0) {
 *       int total = CExecutionStatisticListener.getTotal();
 *       int completed = CExecutionStatisticListener.getPassed() + 
 *                      CExecutionStatisticListener.getFailed() + 
 *                      CExecutionStatisticListener.getSkipped();
 *       double progress = total > 0 ? (double) completed / total * 100 : 0;
 *       
 *       System.out.printf("Progress: %.1f%% (%d/%d)\n", progress, completed, total);
 *       
 *       try {
 *         Thread.sleep(1000); // Update every second
 *       } catch (InterruptedException e) {
 *         break;
 *       }
 *     }
 *   }
 * }
 * 
 * // Start monitoring
 * new TestProgressMonitor().start();
 * }</pre>
 * 
 * <h4>Custom Test Management:</h4>
 * <pre>{@code
 * // Dynamically remove a test method from tracking
 * ITestNGMethod methodToRemove = // ... obtain method reference
 * CExecutionStatisticListener.removeTestMethod(methodToRemove);
 * 
 * // Check updated statistics
 * System.out.println("Updated total after removal: " + 
 *                    CExecutionStatisticListener.getTotal());
 * }</pre>
 * 
 * @author CATools Team
 * @since 1.0
 * @see CExecutionStatus
 * @see ISuiteListener
 * @see IResultListener
 */
public class CExecutionStatisticListener implements ISuiteListener, IResultListener {
  private static final CHashMap<String, CExecutionStatus> methodSignatures = new CHashMap<>();
  
  /**
   * The total number of test methods tracked by this listener.
   * 
   * <p>This count includes all test methods in all statuses: passed, failed, skipped, 
   * running, and waiting. The total is automatically updated whenever test methods
   * are added or removed from tracking.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * int totalTests = CExecutionStatisticListener.getTotal();
   * System.out.println("Total test methods: " + totalTests);
   * 
   * // Calculate completion percentage
   * int completed = CExecutionStatisticListener.getPassed() + 
   *                CExecutionStatisticListener.getFailed() + 
   *                CExecutionStatisticListener.getSkipped();
   * double completionRate = totalTests > 0 ? (double) completed / totalTests * 100 : 0;
   * System.out.printf("Completion: %.1f%%\n", completionRate);
   * }</pre>
   * 
   * @return the total number of test methods being tracked
   */
  @Getter
  private static int total;
  
  /**
   * The number of test methods that have passed successfully.
   * 
   * <p>This count includes tests with {@link CExecutionStatus#SUCCESS} and 
   * {@link CExecutionStatus#SUCCESS_PERCENTAGE_FAILURE} statuses. The count is 
   * automatically updated as tests complete.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * int passedTests = CExecutionStatisticListener.getPassed();
   * int totalTests = CExecutionStatisticListener.getTotal();
   * 
   * if (totalTests > 0) {
   *   double passRate = (double) passedTests / totalTests * 100;
   *   System.out.printf("Pass Rate: %.2f%% (%d/%d)\n", 
   *                     passRate, passedTests, totalTests);
   * }
   * 
   * // Check if all tests passed
   * if (passedTests == totalTests) {
   *   System.out.println("All tests passed successfully! 🎉");
   * }
   * }</pre>
   * 
   * @return the number of passed test methods
   */
  @Getter
  private static int passed;
  
  /**
   * The number of test methods that have failed.
   * 
   * <p>This count includes tests with {@link CExecutionStatus#FAILURE} status.
   * The count is automatically updated as tests complete with failures.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * int failedTests = CExecutionStatisticListener.getFailed();
   * 
   * if (failedTests > 0) {
   *   System.err.println("⚠️  " + failedTests + " test(s) failed!");
   *   
   *   // Calculate failure rate
   *   int totalTests = CExecutionStatisticListener.getTotal();
   *   double failureRate = (double) failedTests / totalTests * 100;
   *   System.err.printf("Failure Rate: %.2f%%\n", failureRate);
   *   
   *   // Trigger failure notifications
   *   if (failureRate > 10.0) {
   *     System.err.println("High failure rate detected - investigating required");
   *   }
   * } else {
   *   System.out.println("No test failures detected");
   * }
   * }</pre>
   * 
   * @return the number of failed test methods
   */
  @Getter
  private static int failed;
  
  /**
   * The number of test methods that have been skipped.
   * 
   * <p>This count includes tests with {@link CExecutionStatus#SKIP}, 
   * {@link CExecutionStatus#IGNORED}, and {@link CExecutionStatus#DEFERRED} statuses.
   * The count is automatically updated when tests are skipped for any reason.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * int skippedTests = CExecutionStatisticListener.getSkipped();
   * int totalTests = CExecutionStatisticListener.getTotal();
   * 
   * if (skippedTests > 0) {
   *   double skipRate = (double) skippedTests / totalTests * 100;
   *   System.out.printf("⏭️  %d test(s) skipped (%.1f%%)\n", 
   *                     skippedTests, skipRate);
   *   
   *   // Alert if too many tests are being skipped
   *   if (skipRate > 20.0) {
   *     System.out.println("Warning: High skip rate may indicate configuration issues");
   *   }
   * }
   * 
   * // Calculate effective test coverage
   * int executedTests = CExecutionStatisticListener.getPassed() + 
   *                    CExecutionStatisticListener.getFailed();
   * double coverage = totalTests > 0 ? (double) executedTests / totalTests * 100 : 0;
   * System.out.printf("Test Coverage: %.1f%% (%d executed, %d skipped)\n", 
   *                   coverage, executedTests, skippedTests);
   * }</pre>
   * 
   * @return the number of skipped test methods
   */
  @Getter
  private static int skipped;
  
  /**
   * The number of test methods that are currently running.
   * 
   * <p>This count includes tests with {@link CExecutionStatus#WIP} (Work In Progress) 
   * status. This provides real-time insight into active test execution.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * int runningTests = CExecutionStatisticListener.getRunning();
   * 
   * if (runningTests > 0) {
   *   System.out.println("🔄 " + runningTests + " test(s) currently running");
   *   
   *   // Monitor test execution in real-time
   *   while (runningTests > 0) {
   *     System.out.print(".");
   *     Thread.sleep(500);
   *     runningTests = CExecutionStatisticListener.getRunning();
   *   }
   *   System.out.println(" Done!");
   * }
   * 
   * // Check for concurrent execution capacity
   * int maxConcurrency = Runtime.getRuntime().availableProcessors();
   * if (runningTests >= maxConcurrency) {
   *   System.out.println("Maximum concurrency reached: " + runningTests);
   * }
   * }</pre>
   * 
   * @return the number of currently running test methods
   */
  @Getter
  private static int running;
  
  /**
   * The number of test methods that are waiting to be executed.
   * 
   * <p>This count represents tests that have been registered but haven't started 
   * execution yet. It's calculated as the difference between total tests and all 
   * other status categories (passed + failed + skipped + running).</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * int waitingTests = CExecutionStatisticListener.getWaiting();
   * int totalTests = CExecutionStatisticListener.getTotal();
   * 
   * if (waitingTests > 0) {
   *   System.out.println("⏳ " + waitingTests + " test(s) waiting to execute");
   *   
   *   // Estimate remaining execution time
   *   double avgTestTime = 2.5; // seconds per test (estimated)
   *   double estimatedTime = waitingTests * avgTestTime;
   *   System.out.printf("Estimated remaining time: %.1f seconds\n", estimatedTime);
   * }
   * 
   * // Show queue status
   * int processed = totalTests - waitingTests;
   * double progress = totalTests > 0 ? (double) processed / totalTests * 100 : 0;
   * System.out.printf("Queue Progress: %.1f%% (%d/%d processed)\n", 
   *                   progress, processed, totalTests);
   * 
   * // Check if test suite is complete
   * if (waitingTests == 0 && CExecutionStatisticListener.getRunning() == 0) {
   *   System.out.println("✅ All tests completed!");
   * }
   * }</pre>
   * 
   * @return the number of test methods waiting to be executed
   */
  @Getter
  private static int waiting;

  private static void updateVariables() {
    total = methodSignatures.size();
    passed = methodSignatures.getAllKeys(CExecutionStatus::isPassed).size();
    failed = methodSignatures.getAllKeys(CExecutionStatus::isFailed).size();
    skipped = methodSignatures.getAllKeys(CExecutionStatus::isSkipped).size();
    running = methodSignatures.getAllKeys(CExecutionStatus::isRunning).size();
    waiting = total - passed - failed - skipped - running;
  }

  private static synchronized void updateTestResult(ITestNGMethod method, CExecutionStatus status) {
    methodSignatures.put(method.getTestClass().getName() + method.getMethodName(), status);
    updateVariables();
  }

  /**
   * Removes a test method from tracking and updates all statistics accordingly.
   * 
   * <p>This method provides a way to dynamically remove test methods from the statistics
   * tracking, which can be useful for conditional test execution scenarios or when
   * tests need to be excluded from reporting after registration.</p>
   * 
   * <p>The method is thread-safe and will automatically recalculate all statistics
   * after the removal operation.</p>
   * 
   * <h3>Usage Examples:</h3>
   * 
   * <h4>Basic Method Removal:</h4>
   * <pre>{@code
   * // Remove a specific test method from tracking
   * ITestNGMethod methodToRemove = // ... obtain method reference
   * 
   * System.out.println("Before removal - Total: " + 
   *                    CExecutionStatisticListener.getTotal());
   * 
   * CExecutionStatisticListener.removeTestMethod(methodToRemove);
   * 
   * System.out.println("After removal - Total: " + 
   *                    CExecutionStatisticListener.getTotal());
   * }</pre>
   * 
   * <h4>Conditional Test Exclusion:</h4>
   * <pre>{@code
   * // Remove tests based on conditions during suite setup
   * public class ConditionalTestManager {
   *   public void excludeTestsBasedOnEnvironment(ISuite suite) {
   *     String environment = System.getProperty("test.environment", "dev");
   *     
   *     for (ITestNGMethod method : suite.getAllMethods()) {
   *       // Check for custom annotation or naming pattern
   *       if (shouldExcludeFromEnvironment(method, environment)) {
   *         System.out.println("Excluding test: " + method.getMethodName() + 
   *                           " for environment: " + environment);
   *         CExecutionStatisticListener.removeTestMethod(method);
   *       }
   *     }
   *   }
   *   
   *   private boolean shouldExcludeFromEnvironment(ITestNGMethod method, String env) {
   *     // Custom logic to determine if method should be excluded
   *     return method.getMethodName().contains("ProductionOnly") && 
   *            !"production".equals(env);
   *   }
   * }
   * }</pre>
   * 
   * <h4>Dynamic Test Suite Management:</h4>
   * <pre>{@code
   * // Remove failed tests from retry tracking
   * public void cleanupFailedTests(List<ITestNGMethod> failedMethods) {
   *   System.out.println("Cleaning up " + failedMethods.size() + " failed tests");
   *   
   *   for (ITestNGMethod method : failedMethods) {
   *     // Remove from statistics if not retrying
   *     if (!shouldRetry(method)) {
   *       CExecutionStatisticListener.removeTestMethod(method);
   *       System.out.println("Removed from tracking: " + 
   *                         method.getTestClass().getName() + "." + 
   *                         method.getMethodName());
   *     }
   *   }
   *   
   *   // Show updated statistics
   *   System.out.println("Updated statistics after cleanup:");
   *   System.out.println("Total: " + CExecutionStatisticListener.getTotal());
   *   System.out.println("Failed: " + CExecutionStatisticListener.getFailed());
   * }
   * }</pre>
   * 
   * @param method the TestNG method to remove from tracking. The method is identified
   *               by the combination of its test class name and method name.
   * 
   * @throws NullPointerException if the method parameter is null
   * 
   * @see #getTotal()
   * @see ITestNGMethod#getTestClass()
   * @see ITestNGMethod#getMethodName()
   */
  public static synchronized void removeTestMethod(ITestNGMethod method) {
    methodSignatures.remove(method.getTestClass().getName() + method.getMethodName());
    updateVariables();
  }

  /**
   * Called when a test suite starts execution.
   * 
   * <p>This method initializes the statistics tracking by clearing any previous data
   * and registering all test methods in the suite with {@link CExecutionStatus#CREATED}
   * status. This provides the foundation for tracking test execution progress.</p>
   * 
   * <p>The method processes all methods returned by {@link ISuite#getAllMethods()},
   * which includes all test methods across all test classes in the suite.</p>
   * 
   * <h3>Execution Flow:</h3>
   * <ol>
   *   <li>Clears any existing method tracking data</li>
   *   <li>Iterates through all methods in the suite</li>
   *   <li>Registers each method with CREATED status</li>
   *   <li>Updates all statistics counters</li>
   * </ol>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * // This method is automatically called by TestNG framework
   * // Manual invocation example for testing:
   * 
   * ISuite mockSuite = // ... create or mock suite
   * CExecutionStatisticListener listener = new CExecutionStatisticListener();
   * listener.onStart(mockSuite);
   * 
   * // Check initial statistics
   * System.out.println("Suite started with " + 
   *                    CExecutionStatisticListener.getTotal() + " tests");
   * System.out.println("All tests initially have CREATED status");
   * System.out.println("Waiting tests: " + 
   *                    CExecutionStatisticListener.getWaiting());
   * }</pre>
   * 
   * @param suite the TestNG suite that is starting execution
   * 
   * @see ISuite#getAllMethods()
   * @see CExecutionStatus#CREATED
   * @see ISuiteListener#onStart(ISuite)
   */
  @Override
  public void onStart(ISuite suite) {
    methodSignatures.clear();
    suite.getAllMethods().forEach(m -> updateTestResult(m, CExecutionStatus.CREATED));
  }

  /**
   * Called when a test method starts execution.
   * 
   * <p>This method updates the execution status of the test method to 
   * {@link CExecutionStatus#WIP} (Work In Progress), indicating that the test
   * is currently running. This allows for real-time monitoring of active tests.</p>
   * 
   * <p>The statistics are automatically recalculated to reflect the change,
   * incrementing the running counter and decrementing the waiting counter.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * // This method is automatically called by TestNG framework
   * // Example of monitoring test starts:
   * 
   * public class TestStartMonitor extends CExecutionStatisticListener {
   *   @Override
   *   public void onTestStart(ITestResult result) {
   *     super.onTestStart(result);
   *     
   *     String testName = result.getMethod().getMethodName();
   *     System.out.println("🚀 Starting test: " + testName);
   *     
   *     // Show current execution status
   *     int running = getRunning();
   *     int waiting = getWaiting();
   *     System.out.printf("Status: %d running, %d waiting\n", running, waiting);
   *     
   *     // Log start time for duration tracking
   *     long startTime = System.currentTimeMillis();
   *     result.setAttribute("startTime", startTime);
   *   }
   * }
   * }</pre>
   * 
   * @param result the test result containing information about the test that is starting
   * 
   * @see ITestResult#getMethod()
   * @see CExecutionStatus#WIP
   * @see IResultListener#onTestStart(ITestResult)
   */
  @Override
  public void onTestStart(ITestResult result) {
    updateTestResult(result.getMethod(), CExecutionStatus.WIP);
  }

  /**
   * Called when a test method executes successfully.
   * 
   * <p>This method updates the execution status of the test method to 
   * {@link CExecutionStatus#SUCCESS}, indicating that the test has passed
   * all assertions and completed without errors.</p>
   * 
   * <p>The statistics are automatically recalculated to reflect the successful
   * completion, incrementing the passed counter and decrementing the running counter.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * // This method is automatically called by TestNG framework
   * // Example of monitoring successful tests:
   * 
   * public class SuccessMonitor extends CExecutionStatisticListener {
   *   @Override
   *   public void onTestSuccess(ITestResult result) {
   *     super.onTestSuccess(result);
   *     
   *     String testName = result.getMethod().getMethodName();
   *     long duration = result.getEndMillis() - result.getStartMillis();
   *     
   *     System.out.println("✅ Test passed: " + testName + 
   *                       " (duration: " + duration + "ms)");
   *     
   *     // Show updated pass rate
   *     int passed = getPassed();
   *     int total = getTotal();
   *     double passRate = total > 0 ? (double) passed / total * 100 : 0;
   *     System.out.printf("Pass rate: %.1f%% (%d/%d)\n", 
   *                       passRate, passed, total);
   *     
   *     // Trigger success notifications if needed
   *     if (passed % 10 == 0) {
   *       System.out.println("🎉 Milestone: " + passed + " tests passed!");
   *     }
   *   }
   * }
   * }</pre>
   * 
   * @param result the test result containing information about the successful test
   * 
   * @see ITestResult#getMethod()
   * @see ITestResult#getStartMillis()
   * @see ITestResult#getEndMillis()
   * @see CExecutionStatus#SUCCESS
   * @see IResultListener#onTestSuccess(ITestResult)
   */
  @Override
  public void onTestSuccess(ITestResult result) {
    updateTestResult(result.getMethod(), CExecutionStatus.SUCCESS);
  }

  /**
   * Called when a test method fails during execution.
   * 
   * <p>This method updates the execution status of the test method to 
   * {@link CExecutionStatus#FAILURE}, indicating that the test has failed
   * due to assertion failures, exceptions, or other errors.</p>
   * 
   * <p>The statistics are automatically recalculated to reflect the failure,
   * incrementing the failed counter and decrementing the running counter.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * // This method is automatically called by TestNG framework
   * // Example of monitoring and handling test failures:
   * 
   * public class FailureMonitor extends CExecutionStatisticListener {
   *   @Override
   *   public void onTestFailure(ITestResult result) {
   *     super.onTestFailure(result);
   *     
   *     String testName = result.getMethod().getMethodName();
   *     Throwable exception = result.getThrowable();
   *     
   *     System.err.println("❌ Test failed: " + testName);
   *     if (exception != null) {
   *       System.err.println("Error: " + exception.getMessage());
   *     }
   *     
   *     // Show failure statistics
   *     int failed = getFailed();
   *     int total = getTotal();
   *     double failureRate = total > 0 ? (double) failed / total * 100 : 0;
   *     System.err.printf("Failure rate: %.1f%% (%d/%d)\n", 
   *                      failureRate, failed, total);
   *     
   *     // Take screenshots or collect debug info for failed tests
   *     if (result.getMethod().isTest()) {
   *       collectDebugInfo(result);
   *     }
   *     
   *     // Alert if failure rate is getting high
   *     if (failureRate > 15.0) {
   *       System.err.println("⚠️ High failure rate detected! Investigation needed.");
   *     }
   *   }
   *   
   *   private void collectDebugInfo(ITestResult result) {
   *     // Implement debug info collection logic
   *     System.out.println("Collecting debug info for: " + 
   *                       result.getMethod().getMethodName());
   *   }
   * }
   * }</pre>
   * 
   * @param result the test result containing information about the failed test,
   *               including exception details and execution context
   * 
   * @see ITestResult#getMethod()
   * @see ITestResult#getThrowable()
   * @see CExecutionStatus#FAILURE
   * @see IResultListener#onTestFailure(ITestResult)
   */
  @Override
  public void onTestFailure(ITestResult result) {
    updateTestResult(result.getMethod(), CExecutionStatus.FAILURE);
  }

  /**
   * Called when a test method is skipped during execution.
   * 
   * <p>This method updates the execution status of the test method to 
   * {@link CExecutionStatus#SKIP}, indicating that the test was not executed.
   * Tests can be skipped for various reasons including dependency failures,
   * conditional execution logic, or explicit skip annotations.</p>
   * 
   * <p>The statistics are automatically recalculated to reflect the skip,
   * incrementing the skipped counter and decrementing the waiting counter.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * // This method is automatically called by TestNG framework
   * // Example of monitoring and handling skipped tests:
   * 
   * public class SkipMonitor extends CExecutionStatisticListener {
   *   @Override
   *   public void onTestSkipped(ITestResult result) {
   *     super.onTestSkipped(result);
   *     
   *     String testName = result.getMethod().getMethodName();
   *     String skipReason = getSkipReason(result);
   *     
   *     System.out.println("⏭️ Test skipped: " + testName);
   *     if (skipReason != null) {
   *       System.out.println("Reason: " + skipReason);
   *     }
   *     
   *     // Track skip patterns
   *     int skipped = getSkipped();
   *     int total = getTotal();
   *     double skipRate = total > 0 ? (double) skipped / total * 100 : 0;
   *     System.out.printf("Skip rate: %.1f%% (%d/%d)\n", 
   *                      skipRate, skipped, total);
   *     
   *     // Analyze skip reasons for patterns
   *     analyzeSkipPattern(result);
   *     
   *     // Alert if too many tests are being skipped
   *     if (skipRate > 25.0) {
   *       System.out.println("⚠️ High skip rate detected! " +
   *                         "Check test configuration and dependencies.");
   *     }
   *   }
   *   
   *   private String getSkipReason(ITestResult result) {
   *     Throwable throwable = result.getThrowable();
   *     if (throwable != null) {
   *       return throwable.getMessage();
   *     }
   *     return "No specific reason provided";
   *   }
   *   
   *   private void analyzeSkipPattern(ITestResult result) {
   *     // Implement skip pattern analysis
   *     String className = result.getMethod().getTestClass().getName();
   *     System.out.println("Skip pattern analysis for class: " + className);
   *   }
   * }
   * }</pre>
   * 
   * @param result the test result containing information about the skipped test,
   *               including potential skip reasons and execution context
   * 
   * @see ITestResult#getMethod()
   * @see ITestResult#getThrowable()
   * @see CExecutionStatus#SKIP
   * @see IResultListener#onTestSkipped(ITestResult)
   */
  @Override
  public void onTestSkipped(ITestResult result) {
    updateTestResult(result.getMethod(), CExecutionStatus.SKIP);
  }

  /**
   * Called when a test method fails but the failure is within the acceptable success percentage.
   * 
   * <p>This method is invoked for tests that have a defined success percentage and the
   * failure falls within that acceptable range. Such tests are treated as successful
   * for reporting purposes, so the status is updated to {@link CExecutionStatus#SUCCESS}.</p>
   * 
   * <p>This feature is useful for tests that may have intermittent issues but are
   * generally reliable, allowing for some tolerance in test execution results.</p>
   * 
   * <h3>Usage Example:</h3>
   * <pre>{@code
   * // This method is automatically called by TestNG framework
   * // Example test method with success percentage:
   * 
   * public class SuccessPercentageTest {
   *   
   *   // This test will pass if it succeeds at least 80% of the time
   *   @Test(successPercentage = 80, invocationCount = 10)
   *   public void flakySuiteTest() {
   *     boolean success = Math.random() > 0.15; // ~85% success rate
   *     Assert.assertTrue(success, "Random test condition");
   *   }
   * }
   * 
   * // Example of monitoring success percentage results:
   * public class SuccessPercentageMonitor extends CExecutionStatisticListener {
   *   @Override
   *   public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
   *     super.onTestFailedButWithinSuccessPercentage(result);
   *     
   *     String testName = result.getMethod().getMethodName();
   *     int successPercentage = result.getMethod().getSuccessPercentage();
   *     
   *     System.out.println("📊 Test within success percentage: " + testName);
   *     System.out.println("Required success rate: " + successPercentage + "%");
   *     
   *     // Log for analysis of flaky tests
   *     System.out.println("Note: Test failed but met success percentage criteria");
   *     
   *     // Track tests that frequently use success percentage
   *     trackFlakyTest(result);
   *   }
   *   
   *   private void trackFlakyTest(ITestResult result) {
   *     // Implement flaky test tracking logic
   *     System.out.println("Tracking potentially flaky test: " + 
   *                       result.getMethod().getMethodName());
   *   }
   * }
   * }</pre>
   * 
   * @param result the test result containing information about the test that failed
   *               but is within the acceptable success percentage range
   * 
   * @see ITestResult#getMethod()
   * @see ITestNGMethod#getSuccessPercentage()
   * @see CExecutionStatus#SUCCESS
   * @see IResultListener#onTestFailedButWithinSuccessPercentage(ITestResult)
   */
  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    updateTestResult(result.getMethod(), CExecutionStatus.SUCCESS);
  }
}

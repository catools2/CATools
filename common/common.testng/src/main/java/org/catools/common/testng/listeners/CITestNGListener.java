package org.catools.common.testng.listeners;

import org.testng.IClassListener;
import org.testng.IConfigurationListener;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener;

/**
 * A comprehensive TestNG listener interface that combines multiple TestNG listener interfaces to
 * provide a unified way to listen to all test execution events in a continuous integration (CI)
 * environment.
 *
 * <p>This interface extends the following TestNG listener interfaces:
 *
 * <ul>
 *   <li>{@link IExecutionListener} - for execution start/finish events
 *   <li>{@link ISuiteListener} - for test suite events
 *   <li>{@link IClassListener} - for test class events
 *   <li>{@link IResultListener} - for test result events
 *   <li>{@link IConfigurationListener} - for configuration method events
 *   <li>{@link IInvokedMethodListener} - for method invocation events
 * </ul>
 *
 * <p>All methods provide default empty implementations, allowing implementers to override only the
 * methods they need for their specific use case.
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>{@code
 * public class MyTestListener implements CITestNGListener {
 *     @Override
 *     public int priority() {
 *         return 100; // Higher priority listener
 *     }
 *
 *     @Override
 *     public void onTestStart(ITestResult result) {
 *         System.out.println("Starting test: " + result.getMethod().getMethodName());
 *     }
 *
 *     @Override
 *     public void onTestSuccess(ITestResult result) {
 *         System.out.println("Test passed: " + result.getMethod().getMethodName());
 *     }
 * }
 * }</pre>
 *
 * <h3>Registration Example:</h3>
 *
 * <pre>{@code
 * // In testng.xml
 * <suite name="MySuite">
 *   <listeners>
 *     <listener class-name="com.example.MyTestListener"/>
 *   </listeners>
 *   <!-- test configuration -->
 * </suite>
 *
 * // Or programmatically
 * TestNG testng = new TestNG();
 * testng.addListener(new MyTestListener());
 * }</pre>
 *
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 */
public interface CITestNGListener
    extends IExecutionListener,
        ISuiteListener,
        IClassListener,
        IResultListener,
        IConfigurationListener,
        IInvokedMethodListener {
  /**
   * Returns the priority of this listener. Listeners with higher priority values are executed
   * before listeners with lower priority values.
   *
   * <p>This method is used to control the execution order when multiple listeners are registered.
   * The default TestNG behavior processes listeners in the order they were registered, but this
   * priority system allows for explicit ordering.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * public class HighPriorityListener implements CITestNGListener {
   *     @Override
   *     public int priority() {
   *         return 1000; // This listener will execute first
   *     }
   * }
   *
   * public class LowPriorityListener implements CITestNGListener {
   *     @Override
   *     public int priority() {
   *         return 10; // This listener will execute later
   *     }
   * }
   * }</pre>
   *
   * @return the priority value for this listener (higher values = higher priority)
   */
  int priority();

  /**
   * Invoked before the TestNG run starts. This is the very first method called when TestNG begins
   * execution, before any suites are processed.
   *
   * <p>This method is useful for performing global setup operations such as:
   *
   * <ul>
   *   <li>Initializing logging systems
   *   <li>Setting up database connections
   *   <li>Configuring test environments
   *   <li>Starting external services
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onExecutionStart() {
   *     System.out.println("=== TestNG Execution Started ===");
   *     Logger.getLogger("TestNG").info("Beginning test execution");
   *     // Initialize test environment
   *     TestEnvironment.setUp();
   * }
   * }</pre>
   *
   * @see #onExecutionFinish()
   */
  @Override
  default void onExecutionStart() {}

  /**
   * Invoked before a test suite starts execution. This method is called for each &lt;suite&gt; tag
   * in the TestNG XML configuration file.
   *
   * <p>Use this method to perform suite-level setup operations such as:
   *
   * <ul>
   *   <li>Reading suite-specific configuration
   *   <li>Setting up suite-level test data
   *   <li>Initializing suite-specific resources
   *   <li>Logging suite start information
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onStart(ISuite suite) {
   *     String suiteName = suite.getName();
   *     System.out.println("Starting suite: " + suiteName);
   *
   *     // Access suite parameters
   *     Map<String, String> parameters = suite.getXmlSuite().getParameters();
   *     String environment = parameters.get("environment");
   *
   *     // Setup suite-specific configuration
   *     SuiteConfiguration.initialize(suiteName, environment);
   * }
   * }</pre>
   *
   * @param suite the {@link ISuite} that is about to start
   * @see #onFinish(ISuite)
   */
  @Override
  default void onStart(ISuite suite) {}

  /**
   * Invoked before a configuration method (like @BeforeMethod, @BeforeClass, etc.) is executed.
   * This provides an opportunity to perform operations before any configuration method runs.
   *
   * <p>Configuration methods include:
   *
   * <ul>
   *   <li>@BeforeSuite, @AfterSuite
   *   <li>@BeforeTest, @AfterTest
   *   <li>@BeforeClass, @AfterClass
   *   <li>@BeforeMethod, @AfterMethod
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void beforeConfiguration(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     String className = result.getTestClass().getName();
   *
   *     System.out.println("About to run configuration method: " +
   *                       className + "." + methodName);
   *
   *     // Log configuration method details
   *     if (result.getMethod().isBeforeMethodConfiguration()) {
   *         System.out.println("This is a @BeforeMethod configuration");
   *     }
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the configuration method about to be
   *     executed
   * @see #onConfigurationSuccess(ITestResult)
   * @see #onConfigurationFailure(ITestResult)
   */
  @Override
  default void beforeConfiguration(ITestResult result) {}

  /**
   * Invoked when a configuration method completes successfully. This allows you to perform cleanup
   * or logging after successful configuration methods.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onConfigurationSuccess(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     long duration = result.getEndMillis() - result.getStartMillis();
   *
   *     System.out.println("Configuration method '" + methodName +
   *                       "' completed successfully in " + duration + "ms");
   *
   *     // Log successful configuration
   *     ConfigurationLogger.logSuccess(methodName, duration);
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the successful configuration method
   * @see #beforeConfiguration(ITestResult)
   * @see #onConfigurationFailure(ITestResult)
   */
  @Override
  default void onConfigurationSuccess(ITestResult result) {}

  /**
   * Invoked when a configuration method fails during execution. This allows you to handle
   * configuration failures gracefully and take appropriate actions.
   *
   * <p>Configuration failures can prevent tests from running properly, so this method is crucial
   * for debugging and error handling in CI environments.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onConfigurationFailure(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     Throwable exception = result.getThrowable();
   *
   *     System.err.println("Configuration method '" + methodName + "' failed!");
   *
   *     if (exception != null) {
   *         System.err.println("Error: " + exception.getMessage());
   *         exception.printStackTrace();
   *     }
   *
   *     // Take screenshot or collect logs for debugging
   *     FailureHandler.handleConfigurationFailure(result);
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the failed configuration method
   * @see #beforeConfiguration(ITestResult)
   * @see #onConfigurationSuccess(ITestResult)
   */
  @Override
  default void onConfigurationFailure(ITestResult result) {}

  /**
   * Invoked when a configuration method is skipped during execution. Configuration methods can be
   * skipped due to failed dependencies or conditional execution.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onConfigurationSkip(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     String skipReason = result.getSkipCausedBy() != null ?
   *                        result.getSkipCausedBy().toString() : "Unknown";
   *
   *     System.out.println("Configuration method '" + methodName +
   *                       "' was skipped. Reason: " + skipReason);
   *
   *     // Log skipped configuration for analysis
   *     ConfigurationLogger.logSkipped(methodName, skipReason);
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the skipped configuration method
   * @see #beforeConfiguration(ITestResult)
   * @see #onConfigurationSuccess(ITestResult)
   */
  @Override
  default void onConfigurationSkip(ITestResult result) {}

  /**
   * Invoked before a test context starts execution. A test context represents a &lt;test&gt; tag in
   * the TestNG XML configuration and can contain multiple test classes.
   *
   * <p>This method is called once per &lt;test&gt; tag, allowing you to perform test-level setup
   * operations that apply to all classes within that test context.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onStart(ITestContext context) {
   *     String testName = context.getName();
   *     String[] includedGroups = context.getIncludedGroups();
   *
   *     System.out.println("Starting test context: " + testName);
   *     System.out.println("Included groups: " + Arrays.toString(includedGroups));
   *
   *     // Initialize test context specific resources
   *     TestContextManager.initialize(context);
   *
   *     // Set up parallel execution parameters
   *     int threadCount = context.getSuite().getXmlSuite().getThreadCount();
   *     System.out.println("Thread count: " + threadCount);
   * }
   * }</pre>
   *
   * @param context the {@link ITestContext} that is about to start
   * @see #onFinish(ITestContext)
   */
  @Override
  default void onStart(ITestContext context) {}

  /**
   * Invoked before the first test method in a test class is executed. This method is called once
   * per test class, before any @BeforeClass methods are run.
   *
   * <p>Use this method to perform class-level setup operations such as:
   *
   * <ul>
   *   <li>Initializing class-specific resources
   *   <li>Setting up class-level test data
   *   <li>Configuring class-specific settings
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onBeforeClass(ITestClass testClass) {
   *     String className = testClass.getName();
   *     Class<?> realClass = testClass.getRealClass();
   *
   *     System.out.println("About to start test class: " + className);
   *
   *     // Check for class-level annotations
   *     if (realClass.isAnnotationPresent(Test.class)) {
   *         Test testAnnotation = realClass.getAnnotation(Test.class);
   *         String[] groups = testAnnotation.groups();
   *         System.out.println("Class belongs to groups: " + Arrays.toString(groups));
   *     }
   *
   *     // Initialize class-specific test environment
   *     ClassTestEnvironment.setUp(className);
   * }
   * }</pre>
   *
   * @param testClass the {@link ITestClass} that is about to start execution
   * @see #onAfterClass(ITestClass)
   */
  @Override
  default void onBeforeClass(ITestClass testClass) {}

  /**
   * Invoked when a test method is about to start execution. This is called for each
   * individual @Test method before it runs.
   *
   * <p>This method provides access to the test result object before the test runs, allowing you to
   * perform pre-test operations such as:
   *
   * <ul>
   *   <li>Logging test start information
   *   <li>Setting up test-specific data
   *   <li>Recording test start time
   *   <li>Preparing test environment
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onTestStart(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     String className = result.getTestClass().getName();
   *     String[] groups = result.getMethod().getGroups();
   *
   *     System.out.println("Starting test: " + className + "." + methodName);
   *     System.out.println("Test groups: " + Arrays.toString(groups));
   *
   *     // Log test parameters if any
   *     Object[] parameters = result.getParameters();
   *     if (parameters.length > 0) {
   *         System.out.println("Test parameters: " + Arrays.toString(parameters));
   *     }
   *
   *     // Record test start time for custom reporting
   *     TestMetrics.recordTestStart(methodName, System.currentTimeMillis());
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the test about to start
   * @see #onTestSuccess(ITestResult)
   * @see #onTestFailure(ITestResult)
   * @see #onTestSkipped(ITestResult)
   */
  @Override
  default void onTestStart(ITestResult result) {}

  /**
   * Invoked before any method (test, configuration, or factory) is invoked by TestNG. This provides
   * the most granular level of method invocation tracking.
   *
   * <p><strong>Note:</strong> This method is called for ALL methods, including configuration
   * methods (@BeforeMethod, @AfterMethod, etc.) and factory methods, not just test methods.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
   *     String methodName = method.getTestMethod().getMethodName();
   *     boolean isTestMethod = method.isTestMethod();
   *     boolean isConfigurationMethod = method.isConfigurationMethod();
   *
   *     if (isTestMethod) {
   *         System.out.println("About to invoke test method: " + methodName);
   *     } else if (isConfigurationMethod) {
   *         System.out.println("About to invoke configuration method: " + methodName);
   *     }
   *
   *     // Start method execution timer
   *     MethodTimer.start(methodName);
   * }
   * }</pre>
   *
   * @param method the {@link IInvokedMethod} about to be invoked
   * @param testResult the {@link ITestResult} associated with the method invocation
   * @see #afterInvocation(IInvokedMethod, ITestResult)
   * @see #beforeInvocation(IInvokedMethod, ITestResult, ITestContext)
   */
  @Override
  default void beforeInvocation(IInvokedMethod method, ITestResult testResult) {}

  /**
   * Invoked before any method (test, configuration, or factory) is invoked by TestNG. This
   * overloaded version provides additional context information.
   *
   * <p>This method provides the same functionality as {@link #beforeInvocation(IInvokedMethod,
   * ITestResult)} but includes the test context, which can be useful for accessing suite and
   * test-level information.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
   *     String methodName = method.getTestMethod().getMethodName();
   *     String testName = context.getName();
   *     String suiteName = context.getSuite().getName();
   *
   *     System.out.println("Invoking method: " + methodName +
   *                       " in test: " + testName +
   *                       " in suite: " + suiteName);
   *
   *     // Access context-specific parameters
   *     String environment = context.getCurrentXmlTest().getParameter("environment");
   *     if (environment != null) {
   *         System.out.println("Running in environment: " + environment);
   *     }
   * }
   * }</pre>
   *
   * @param method the {@link IInvokedMethod} about to be invoked
   * @param testResult the {@link ITestResult} associated with the method invocation
   * @param context the {@link ITestContext} providing additional context information
   * @see #afterInvocation(IInvokedMethod, ITestResult, ITestContext)
   * @see #beforeInvocation(IInvokedMethod, ITestResult)
   */
  @Override
  default void beforeInvocation(
      IInvokedMethod method, ITestResult testResult, ITestContext context) {}

  /**
   * Invoked after any method (test, configuration, or factory) has been invoked by TestNG. This
   * provides the most granular level of method completion tracking.
   *
   * <p>This method is called after ALL methods complete, regardless of their outcome (success,
   * failure, or skip). It's useful for cleanup operations and metrics collection.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
   *     String methodName = method.getTestMethod().getMethodName();
   *     long duration = testResult.getEndMillis() - testResult.getStartMillis();
   *
   *     System.out.println("Method '" + methodName + "' completed in " + duration + "ms");
   *
   *     // Record method execution metrics
   *     MethodTimer.stop(methodName, duration);
   *
   *     // Perform cleanup if method failed
   *     if (testResult.getStatus() == ITestResult.FAILURE && method.isTestMethod()) {
   *         TestCleanup.cleanupAfterFailure(testResult);
   *     }
   * }
   * }</pre>
   *
   * @param method the {@link IInvokedMethod} that was invoked
   * @param testResult the {@link ITestResult} with the result of the method invocation
   * @see #beforeInvocation(IInvokedMethod, ITestResult)
   * @see #afterInvocation(IInvokedMethod, ITestResult, ITestContext)
   */
  @Override
  default void afterInvocation(IInvokedMethod method, ITestResult testResult) {}

  /**
   * Invoked after any method (test, configuration, or factory) has been invoked by TestNG. This
   * overloaded version provides additional context information.
   *
   * <p>This method provides the same functionality as {@link #afterInvocation(IInvokedMethod,
   * ITestResult)} but includes the test context for accessing suite and test-level information.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
   *     String methodName = method.getTestMethod().getMethodName();
   *     long duration = testResult.getEndMillis() - testResult.getStartMillis();
   *     String testName = context.getName();
   *
   *     System.out.println("Method '" + methodName + "' in test '" + testName +
   *                       "' completed in " + duration + "ms");
   *
   *     // Update context-level statistics
   *     if (method.isTestMethod()) {
   *         TestStats testStats = TestContextManager.getStats(context);
   *         testStats.addMethodExecution(methodName, duration, testResult.getStatus());
   *     }
   * }
   * }</pre>
   *
   * @param method the {@link IInvokedMethod} that was invoked
   * @param testResult the {@link ITestResult} with the result of the method invocation
   * @param context the {@link ITestContext} providing additional context information
   * @see #beforeInvocation(IInvokedMethod, ITestResult, ITestContext)
   * @see #afterInvocation(IInvokedMethod, ITestResult)
   */
  @Override
  default void afterInvocation(
      IInvokedMethod method, ITestResult testResult, ITestContext context) {}

  /**
   * Invoked when a test method passes successfully. This method is called only for @Test methods
   * that complete without throwing any exceptions.
   *
   * <p>Use this method to perform post-success operations such as:
   *
   * <ul>
   *   <li>Logging successful test completion
   *   <li>Recording test metrics and statistics
   *   <li>Performing success-specific cleanup
   *   <li>Updating test result databases
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onTestSuccess(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     String className = result.getTestClass().getName();
   *     long duration = result.getEndMillis() - result.getStartMillis();
   *
   *     System.out.println("✓ Test PASSED: " + className + "." + methodName +
   *                       " (" + duration + "ms)");
   *
   *     // Record success metrics
   *     TestMetrics.recordSuccess(methodName, duration);
   *
   *     // Generate success report entry
   *     TestReporter.addSuccess(result);
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the successful test
   * @see #onTestStart(ITestResult)
   * @see #onTestFailure(ITestResult)
   * @see #onTestSkipped(ITestResult)
   */
  @Override
  default void onTestSuccess(ITestResult result) {}

  /**
   * Invoked when a test method fails during execution. This method is called for @Test methods that
   * throw exceptions or fail assertions.
   *
   * <p>This is one of the most important methods for debugging and error handling in CI
   * environments. Use this method to:
   *
   * <ul>
   *   <li>Log failure details and stack traces
   *   <li>Capture screenshots or other debugging artifacts
   *   <li>Send failure notifications
   *   <li>Record failure metrics
   *   <li>Perform failure-specific cleanup
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onTestFailure(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     String className = result.getTestClass().getName();
   *     Throwable exception = result.getThrowable();
   *
   *     System.err.println("✗ Test FAILED: " + className + "." + methodName);
   *
   *     if (exception != null) {
   *         System.err.println("Error: " + exception.getMessage());
   *         exception.printStackTrace();
   *     }
   *
   *     // Capture screenshot for web tests
   *     if (isWebTest(result)) {
   *         String screenshot = ScreenshotUtils.captureScreenshot(methodName);
   *         result.setAttribute("screenshot", screenshot);
   *     }
   *
   *     // Record failure metrics
   *     TestMetrics.recordFailure(methodName, exception);
   *
   *     // Send notification for critical failures
   *     if (isCriticalTest(result)) {
   *         NotificationService.sendFailureAlert(result);
   *     }
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the failed test
   * @see #onTestStart(ITestResult)
   * @see #onTestSuccess(ITestResult)
   * @see #onTestSkipped(ITestResult)
   */
  @Override
  default void onTestFailure(ITestResult result) {}

  /**
   * Invoked when a test method is skipped during execution. Tests can be skipped due to various
   * reasons such as failed dependencies, conditional execution, or explicit skip directives.
   *
   * <p>Common reasons for test skipping include:
   *
   * <ul>
   *   <li>Failed @BeforeMethod or @BeforeClass methods
   *   <li>Dependency failures (dependsOnMethods, dependsOnGroups)
   *   <li>Conditional skips using assumeTrue() or similar
   *   <li>Explicit throws of SkipException
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onTestSkipped(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     String className = result.getTestClass().getName();
   *     Throwable skipCause = result.getThrowable();
   *
   *     System.out.println("⊘ Test SKIPPED: " + className + "." + methodName);
   *
   *     // Log skip reason
   *     if (skipCause != null) {
   *         System.out.println("Skip reason: " + skipCause.getMessage());
   *     }
   *
   *     // Check if skipped due to dependency failure
   *     String[] dependencies = result.getMethod().getMethodsDependedUpon();
   *     if (dependencies.length > 0) {
   *         System.out.println("Depends on: " + Arrays.toString(dependencies));
   *     }
   *
   *     // Record skip metrics
   *     TestMetrics.recordSkip(methodName, skipCause);
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the skipped test
   * @see #onTestStart(ITestResult)
   * @see #onTestSuccess(ITestResult)
   * @see #onTestFailure(ITestResult)
   */
  @Override
  default void onTestSkipped(ITestResult result) {}

  /**
   * Invoked when a test method completes with a failure but within the acceptable success
   * percentage. This method is called for tests that have a success percentage defined and the
   * failure rate is still within the acceptable threshold.
   *
   * <p>This method is used in conjunction with the {@code successPercentage} attribute of the @Test
   * annotation, which allows tests to pass even if a certain percentage of executions fail (useful
   * for flaky tests or load testing scenarios).
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * // Test method with success percentage
   * @Test(invocationCount = 100, successPercentage = 90)
   * public void flakyTest() {
   *     // This test can fail up to 10% of the time and still be considered successful
   *     if (Math.random() < 0.1) {
   *         throw new RuntimeException("Random failure");
   *     }
   * }
   *
   * @Override
   * public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
   *     String methodName = result.getMethod().getMethodName();
   *     int successPercentage = result.getMethod().getSuccessPercentage();
   *
   *     System.out.println("⚠ Test failed but within success percentage: " + methodName);
   *     System.out.println("Required success percentage: " + successPercentage + "%");
   *
   *     // Log the failure but don't treat it as a critical issue
   *     TestMetrics.recordAcceptableFailure(methodName, result.getThrowable());
   * }
   * }</pre>
   *
   * @param result the {@link ITestResult} representing the failed test within success percentage
   * @see #onTestFailure(ITestResult)
   * @see #onTestSuccess(ITestResult)
   */
  @Override
  default void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

  /**
   * Invoked after all test methods in a test class have completed execution. This method is called
   * once per test class, after all @AfterClass methods have run.
   *
   * <p>Use this method to perform class-level cleanup operations such as:
   *
   * <ul>
   *   <li>Cleaning up class-specific resources
   *   <li>Generating class-level reports
   *   <li>Recording class execution statistics
   *   <li>Performing final class-level validations
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onAfterClass(ITestClass testClass) {
   *     String className = testClass.getName();
   *     ITestNGMethod[] testMethods = testClass.getTestMethods();
   *
   *     System.out.println("Completed test class: " + className);
   *     System.out.println("Total test methods: " + testMethods.length);
   *
   *     // Generate class-level report
   *     ClassTestReport report = TestReporter.generateClassReport(testClass);
   *     System.out.println("Class success rate: " + report.getSuccessRate() + "%");
   *
   *     // Cleanup class-specific resources
   *     ClassTestEnvironment.cleanup(className);
   *
   *     // Log class completion metrics
   *     TestMetrics.recordClassCompletion(className, testMethods.length);
   * }
   * }</pre>
   *
   * @param testClass the {@link ITestClass} that has completed execution
   * @see #onBeforeClass(ITestClass)
   */
  @Override
  default void onAfterClass(ITestClass testClass) {}

  /**
   * Invoked after a test context has finished execution. This method is called once per
   * &lt;test&gt; tag in the TestNG XML configuration, after all test classes within that context
   * have completed.
   *
   * <p>This method provides access to comprehensive test execution results and is ideal for
   * generating test context-level reports and performing final cleanup.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onFinish(ITestContext context) {
   *     String testName = context.getName();
   *
   *     // Get test execution results
   *     IResultMap passedTests = context.getPassedTests();
   *     IResultMap failedTests = context.getFailedTests();
   *     IResultMap skippedTests = context.getSkippedTests();
   *
   *     System.out.println("Test context '" + testName + "' completed:");
   *     System.out.println("  Passed: " + passedTests.size());
   *     System.out.println("  Failed: " + failedTests.size());
   *     System.out.println("  Skipped: " + skippedTests.size());
   *
   *     // Calculate execution time
   *     long duration = context.getEndDate().getTime() - context.getStartDate().getTime();
   *     System.out.println("  Duration: " + duration + "ms");
   *
   *     // Generate test context report
   *     TestContextReport report = TestReporter.generateContextReport(context);
   *     ReportManager.saveReport(report);
   *
   *     // Cleanup test context resources
   *     TestContextManager.cleanup(context);
   * }
   * }</pre>
   *
   * @param context the {@link ITestContext} that has finished execution
   * @see #onStart(ITestContext)
   */
  @Override
  default void onFinish(ITestContext context) {}

  /**
   * Invoked after a test suite has completed execution. This method is called once per
   * &lt;suite&gt; tag in the TestNG XML configuration, after all tests within that suite have
   * finished.
   *
   * <p>This is the ideal place to perform suite-level cleanup and generate comprehensive suite
   * reports with complete execution statistics.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onFinish(ISuite suite) {
   *     String suiteName = suite.getName();
   *     Map<String, ISuiteResult> results = suite.getResults();
   *
   *     System.out.println("Suite '" + suiteName + "' completed with " +
   *                       results.size() + " test(s)");
   *
   *     // Aggregate results from all tests in the suite
   *     int totalPassed = 0, totalFailed = 0, totalSkipped = 0;
   *
   *     for (ISuiteResult result : results.values()) {
   *         ITestContext context = result.getTestContext();
   *         totalPassed += context.getPassedTests().size();
   *         totalFailed += context.getFailedTests().size();
   *         totalSkipped += context.getSkippedTests().size();
   *     }
   *
   *     System.out.println("Suite totals - Passed: " + totalPassed +
   *                       ", Failed: " + totalFailed +
   *                       ", Skipped: " + totalSkipped);
   *
   *     // Generate comprehensive suite report
   *     SuiteReport suiteReport = TestReporter.generateSuiteReport(suite);
   *     ReportManager.saveSuiteReport(suiteReport);
   *
   *     // Cleanup suite-specific resources
   *     SuiteConfiguration.cleanup(suiteName);
   * }
   * }</pre>
   *
   * @param suite the {@link ISuite} that has finished execution
   * @see #onStart(ISuite)
   */
  @Override
  default void onFinish(ISuite suite) {}

  /**
   * Invoked when the entire TestNG execution is complete. This is the very last method called when
   * TestNG finishes execution, after all suites have been processed.
   *
   * <p>This method is perfect for performing final cleanup operations and generating overall
   * execution reports. Use this method for:
   *
   * <ul>
   *   <li>Final cleanup of global resources
   *   <li>Generating comprehensive execution reports
   *   <li>Sending execution completion notifications
   *   <li>Shutting down external services
   *   <li>Final metric collection and reporting
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * @Override
   * public void onExecutionFinish() {
   *     System.out.println("=== TestNG Execution Completed ===");
   *
   *     // Generate final execution report
   *     ExecutionReport finalReport = TestReporter.generateFinalReport();
   *     System.out.println("Total execution time: " + finalReport.getTotalDuration() + "ms");
   *     System.out.println("Overall success rate: " + finalReport.getSuccessRate() + "%");
   *
   *     // Send completion notification
   *     NotificationService.sendExecutionComplete(finalReport);
   *
   *     // Cleanup global resources
   *     TestEnvironment.shutdown();
   *
   *     // Save final metrics
   *     TestMetrics.saveToDatabase();
   *
   *     Logger.getLogger("TestNG").info("Test execution completed successfully");
   * }
   * }</pre>
   *
   * @see #onExecutionStart()
   */
  @Override
  default void onExecutionFinish() {}
}

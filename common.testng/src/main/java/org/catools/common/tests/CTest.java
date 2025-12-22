package org.catools.common.tests;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.catools.common.logger.CLoggerConfigs;
import org.catools.common.testng.model.CExecutionStatus;
import org.catools.common.testng.model.CTestResult;
import org.catools.common.testng.utils.CTestClassUtil;
import org.catools.common.utils.CStringUtil;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

/**
 * Base test class providing common functionality for TestNG tests. This class manages test
 * lifecycle, logging, state management, and metadata handling.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * public class MyTest extends CTest {
 *     @Test
 *     public void testExample() {
 *         addMetadata("testType", "integration");
 *         updateDataState("userId", "12345");
 *
 *         // Test logic here
 *
 *         String userId = getDataState("userId");
 *         assert userId.equals("12345");
 *     }
 *
 *     @Override
 *     protected void onSuccess() {
 *         getLogger().info("Test completed successfully!");
 *     }
 * }
 * }</pre>
 */
@Slf4j
public class CTest {
  static {
    AnsiConsole.systemInstall();
    System.setProperty("log4j2.Script.enableLanguages", "groovy");
    ThreadContext.put("LogFolder", CLoggerConfigs.getLogFolderPath());
  }

  /** Logger instance for this test class. Use this logger for test-specific logging operations. */
  public final Logger logger = LoggerFactory.getLogger(CTest.class);

  private static boolean FIRST_RUN_PREPARATION_CALLED = false;

  /** Current test execution status. */
  private CExecutionStatus testResult = CExecutionStatus.CREATED;

  /**
   * Test state data container for storing key-value pairs during test execution.
   *
   * @return the data state container
   */
  @Getter private final CTestStateData dataState = new CTestStateData();

  /**
   * Test metadata container for storing test-related metadata.
   *
   * @return the metadata container
   */
  @Getter private final CTestMetadata metadata = new CTestMetadata();

  /**
   * The name of this test class derived from the class name.
   *
   * @return the test name
   */
  @Getter private final String name = CTestClassUtil.getTestName(getClass());

  /**
   * Executes before the entire test suite runs. Ensures that first-run preparation is called only
   * once across all test classes.
   *
   * <p>Example usage in subclass:
   *
   * <pre>{@code
   * @Override
   * public void beforeSuite() {
   *     super.beforeSuite();
   *     // Additional suite-level setup
   * }
   * }</pre>
   */
  @BeforeSuite
  public void beforeSuite() {
    if (!FIRST_RUN_PREPARATION_CALLED) {
      onFirstRun();
      FIRST_RUN_PREPARATION_CALLED = true;
    }
  }

  /**
   * Executes before each test group runs.
   *
   * @param context the TestNG test context containing test information
   *     <p>Example usage in subclass:
   *     <pre>{@code
   * @Override
   * public void beforeTest(ITestContext context) {
   *     super.beforeTest(context);
   *     // Additional test-level setup
   *     getLogger().info("Starting test group: " + context.getName());
   * }
   *
   * }</pre>
   */
  @BeforeTest
  public void beforeTest(ITestContext context) {
    log.debug("BeforeTest Started for issue {} ", getContextName(context));
  }

  /**
   * Executes before each test class runs. Sets up logging context with log folder path and test
   * name.
   *
   * <p>Example usage in subclass:
   *
   * <pre>{@code
   * @Override
   * public void beforeClass() {
   *     super.beforeClass();
   *     // Additional class-level setup
   *     addMetadata("testClass", this.getClass().getSimpleName());
   * }
   * }</pre>
   */
  @BeforeClass
  public void beforeClass() {
    ThreadContext.put("LogFolder", CLoggerConfigs.getLogFolderPath());
    ThreadContext.put("TestName", name);
    log.debug("BeforeClass Started for class {} ", name);
  }

  /**
   * Executes before each test method runs. Sets up logging context for the specific test method.
   *
   * @param result the TestNG test result containing method information
   *     <p>Example usage in subclass:
   *     <pre>{@code
   * @Override
   * public void beforeMethod(ITestResult result) {
   *     super.beforeMethod(result);
   *     // Additional method-level setup
   *     updateDataState("startTime", System.currentTimeMillis());
   * }
   *
   * }</pre>
   */
  @BeforeMethod
  public void beforeMethod(ITestResult result) {
    ThreadContext.put("LogFolder", CLoggerConfigs.getLogFolderPath());
    ThreadContext.put("TestName", name);
    log.debug("BeforeMethod Started for class {}, method {}", name, getMethodName(result));
  }

  /**
   * Executes after each test method completes. Captures test result status and logs any exceptions
   * that occurred.
   *
   * @param result the TestNG test result containing execution information
   *     <p>Example usage in subclass:
   *     <pre>{@code
   * @Override
   * public void afterMethod(ITestResult result) {
   *     super.afterMethod(result);
   *     // Additional cleanup or reporting
   *     Long startTime = getDataState("startTime");
   *     if (startTime != null) {
   *         long duration = System.currentTimeMillis() - startTime;
   *         addMetadata("executionTime", String.valueOf(duration));
   *     }
   * }
   *
   * }</pre>
   */
  @AfterMethod
  public void afterMethod(ITestResult result) {
    if (result == null) return;

    log.debug("AfterMethod Started for class {}, method {} ", name, getMethodName(result));
    this.testResult = new CTestResult(result).getStatus();

    if (result.getThrowable() != null) {
      log.error("Test Failed With Exception:\n" + result.getThrowable());
    }
  }

  /**
   * Executes after each test class completes. Calls appropriate status-specific callback methods
   * based on test execution result.
   *
   * <p>Example usage in subclass:
   *
   * <pre>{@code
   * @Override
   * public void afterClass() {
   *     super.afterClass();
   *     // Additional class-level cleanup
   *     getLogger().info("Test class completed with status: " + testResult);
   * }
   * }</pre>
   */
  @AfterClass
  public void afterClass() {
    log.debug("AfterClass Started for class {}", name);
    switch (testResult) {
      case SUCCESS, SUCCESS_PERCENTAGE_FAILURE -> onSuccess();
      case SKIP -> onSkip();
      case FAILURE -> onFailure();
      case DEFERRED -> onDeferred();
      case BLOCKED -> onBlocked();
      case AWAITING -> onAwaiting();
    }
  }

  /**
   * Executes after each test group completes. Removes test name from thread context to clean up
   * logging context.
   *
   * @param context the TestNG test context containing test information
   *     <p>Example usage in subclass:
   *     <pre>{@code
   * @Override
   * public void afterTest(ITestContext context) {
   *     super.afterTest(context);
   *     // Additional test-level cleanup
   *     getLogger().info("Test group completed: " + context.getName());
   * }
   *
   * }</pre>
   */
  @AfterTest
  public void afterTest(ITestContext context) {
    ThreadContext.remove("TestName");
    log.debug("AfterTest Started for issue {} ", getContextName(context));
  }

  /**
   * Executes after the entire test suite completes. Override this method in subclasses to perform
   * suite-level cleanup.
   *
   * <p>Example usage in subclass:
   *
   * <pre>{@code
   * @Override
   * public void afterSuite() {
   *     super.afterSuite();
   *     // Suite-level cleanup
   *     getLogger().info("Test suite completed");
   * }
   * }</pre>
   */
  @AfterSuite
  public void afterSuite() {}

  /**
   * Updates the test data state with a key-value pair. This method provides a convenient way to
   * store test-specific data during execution.
   *
   * @param key the key to store the value under
   * @param value the value to store
   *     <p>Example usage:
   *     <pre>{@code
   * // Store user ID for later use in test
   * updateDataState("userId", "12345");
   *
   * // Store complex objects
   * updateDataState("userObject", new User("John", "Doe"));
   *
   * // Store test configuration
   * updateDataState("testConfig", Map.of("timeout", 30, "retries", 3));
   *
   * }</pre>
   */
  public void updateDataState(String key, Object value) {
    getDataState().updateDataState(key, value);
  }

  /**
   * Retrieves a value from the test data state by key.
   *
   * @param <T> the type of the value to retrieve
   * @param key the key to lookup
   * @return the value associated with the key, or null if not found
   *     <p>Example usage:
   *     <pre>{@code
   * // Retrieve previously stored user ID
   * String userId = getDataState("userId");
   *
   * // Retrieve complex objects with casting
   * User user = getDataState("userObject");
   *
   * // Retrieve with explicit type specification
   * Map<String, Object> config = this.<Map<String, Object>>getDataState("testConfig");
   *
   * // Safe retrieval with null check
   * String optionalValue = getDataState("optionalKey");
   * if (optionalValue != null) {
   *     // Use the value
   * }
   * }</pre>
   */
  public <T> T getDataState(String key) {
    return getDataState().getDataState(key);
  }

  /**
   * Adds metadata key-value pair if the key doesn't already exist. Metadata is typically used for
   * test reporting and categorization.
   *
   * @param key the metadata key
   * @param value the metadata value
   *     <p>Example usage:
   *     <pre>{@code
   * // Add test categorization metadata
   * addMetadata("testType", "integration");
   * addMetadata("priority", "high");
   * addMetadata("component", "user-service");
   *
   * // Add environment information
   * addMetadata("environment", "staging");
   * addMetadata("browser", "chrome");
   *
   * // Add test timing information
   * addMetadata("expectedDuration", "30s");
   *
   * }</pre>
   */
  public void addMetadata(String key, String value) {
    getMetadata().addIfNotExists(key, value);
  }

  /**
   * Called when test execution is in awaiting status. Override this method to implement custom
   * behavior for awaiting tests.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * @Override
   * protected void onAwaiting() {
   *     getLogger().warn("Test is awaiting external dependencies");
   *     addMetadata("awaitingReason", "External service unavailable");
   * }
   * }</pre>
   */
  protected void onAwaiting() {}

  /**
   * Called when test execution is blocked. Override this method to implement custom behavior for
   * blocked tests.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * @Override
   * protected void onBlocked() {
   *     getLogger().error("Test blocked due to prerequisite failure");
   *     addMetadata("blockReason", "Database connection failed");
   * }
   * }</pre>
   */
  protected void onBlocked() {}

  /**
   * Called when test execution is deferred. Override this method to implement custom behavior for
   * deferred tests.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * @Override
   * protected void onDeferred() {
   *     getLogger().info("Test deferred for later execution");
   *     addMetadata("deferReason", "Peak hours - rescheduled");
   * }
   * }</pre>
   */
  protected void onDeferred() {}

  /**
   * Called when test execution fails. Override this method to implement custom behavior for failed
   * tests.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * @Override
   * protected void onFailure() {
   *     getLogger().error("Test failed - capturing screenshots and logs");
   *     addMetadata("failureHandled", "true");
   *
   *     // Capture screenshot if UI test
   *     // captureScreenshot();
   *
   *     // Send notification
   *     // notifyFailure();
   * }
   * }</pre>
   */
  protected void onFailure() {}

  /**
   * Called when test execution is skipped. Override this method to implement custom behavior for
   * skipped tests.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * @Override
   * protected void onSkip() {
   *     getLogger().info("Test skipped - checking skip reason");
   *     addMetadata("skipHandled", "true");
   *
   *     // Log skip reason
   *     // logSkipReason();
   * }
   * }</pre>
   */
  protected void onSkip() {}

  /**
   * Called when test execution succeeds. Override this method to implement custom behavior for
   * successful tests.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * @Override
   * protected void onSuccess() {
   *     getLogger().info("Test completed successfully");
   *     addMetadata("successHandled", "true");
   *
   *     // Clean up test data
   *     // cleanupTestData();
   *
   *     // Update test metrics
   *     // updateSuccessMetrics();
   * }
   * }</pre>
   */
  protected void onSuccess() {}

  /**
   * Called during the first test suite execution for one-time initialization. Override this method
   * to implement custom first-run setup logic.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * @Override
   * protected void onFirstRun() {
   *     getLogger().info("Performing first-run initialization");
   *
   *     // Initialize global test resources
   *     // initializeDatabase();
   *     // setupTestEnvironment();
   *     // loadTestConfiguration();
   * }
   * }</pre>
   */
  protected void onFirstRun() {}

  private String getMethodName(ITestResult result) {
    if (result == null || result.getMethod() == null) {
      return CStringUtil.EMPTY;
    }
    return result.getMethod().getMethodName();
  }

  private String getSuiteName(ITestContext context) {
    if (context == null || context.getSuite() == null) {
      return CStringUtil.EMPTY;
    }
    return context.getSuite().getName();
  }

  private String getContextName(ITestContext context) {
    if (context == null || context.getCurrentXmlTest() == null) {
      return CStringUtil.EMPTY;
    }
    return context.getCurrentXmlTest().getName();
  }

  /**
   * Gets the logger instance for this test class. This provides access to the SLF4J logger for
   * test-specific logging.
   *
   * @return the logger instance
   *     <p>Example usage:
   *     <pre>{@code
   * // Log test progress
   * getLogger().info("Starting user authentication test");
   *
   * // Log debug information
   * getLogger().debug("User ID: {}, Session: {}", userId, sessionId);
   *
   * // Log warnings
   * getLogger().warn("Using fallback configuration");
   *
   * // Log errors with exception
   * try {
   *     // test code
   * } catch (Exception e) {
   *     getLogger().error("Test failed with error", e);
   *     throw e;
   * }
   * }</pre>
   */
  public Logger getLogger() {
    return log;
  }
}

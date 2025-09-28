package org.catools.pipeline.listeners;

import org.catools.common.date.CDate;
import org.catools.common.testng.listeners.CITestNGListener;
import org.catools.common.testng.model.CTestResult;
import org.catools.pipeline.dao.CPipelineDao;
import org.catools.pipeline.helpers.CPipelineHelper;
import org.catools.pipeline.model.CPipeline;
import org.testng.IInvokedMethod;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

/**
 * CPipelineListener is a TestNG listener that tracks test execution within a pipeline context.
 * This listener captures test results, execution timing, and pipeline metadata to provide
 * comprehensive test execution reporting and analytics.
 * 
 * <p>The listener automatically handles:</p>
 * <ul>
 *   <li>Pipeline initialization and tracking</li>
 *   <li>Test method timing (including setup and teardown methods)</li>
 *   <li>Test result collection and storage</li>
 *   <li>Pipeline completion tracking</li>
 * </ul>
 * 
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * // Add to testng.xml
 * <suite name="MyTestSuite">
 *   <listeners>
 *     <listener class-name="org.catools.pipeline.listeners.CPipelineListener"/>
 *   </listeners>
 *   <test name="MyTest">
 *     <classes>
 *       <class name="com.example.MyTestClass"/>
 *     </classes>
 *   </test>
 * </suite>
 * 
 * // Or programmatically
 * TestNG testng = new TestNG();
 * testng.addListener(new CPipelineListener());
 * testng.setTestClasses(new Class[]{MyTestClass.class});
 * testng.run();
 * }</pre>
 * 
 * <p><strong>Configuration:</strong></p>
 * <p>The pipeline context is automatically determined by the CPipelineHelper.
 * Ensure your pipeline configuration is properly set up before test execution.</p>
 * 
 * @author CATools Team
 * @version 1.0
 * @see CITestNGListener
 * @see CPipelineHelper
 * @see CPipelineDao
 * @since 1.0
 */
public class CPipelineListener implements CITestNGListener {
  /** The current pipeline instance being tracked */
  private static CPipeline pipeline;
  
  /** Start time of the current test method */
  private CDate testStartTime;
  
  /** Start time of the @BeforeClass method */
  private CDate beforeClassStartTime;
  
  /** Start time of the @BeforeMethod method */
  private CDate beforeMethodStartTime;
  
  /** End time of the @BeforeClass method */
  private CDate beforeClassEndTime;
  
  /** End time of the @BeforeMethod method */
  private CDate beforeMethodEndTime;

  /**
   * Returns the priority of this listener in the execution order.
   * Lower values indicate higher priority.
   * 
   * @return 0, indicating default priority
   */
  @Override
  public int priority() {
    return 0;
  }

  /**
   * Called when test execution starts. Initializes the pipeline context
   * and sets up tracking for the current test run.
   * 
   * <p>This method ensures that a pipeline instance is created and ready
   * to track test executions. It's called once at the beginning of the
   * entire test suite execution.</p>
   * 
   * <p><strong>Example Timeline:</strong></p>
   * <pre>
   * onExecutionStart() → [All test methods execute] → onExecutionFinish()
   * </pre>
   */
  @Override
  public void onExecutionStart() {
    buildPipeline();
  }

  /**
   * Called before each test method invocation to capture start timing.
   * This includes @BeforeClass, @BeforeMethod, and @Test methods.
   * 
   * <p>The method tracks different types of method invocations:</p>
   * <ul>
   *   <li>@BeforeClass methods - class-level setup</li>
   *   <li>@BeforeMethod methods - method-level setup</li>
   *   <li>@Test methods - actual test execution</li>
   * </ul>
   * 
   * <p><strong>Example execution flow:</strong></p>
   * <pre>
   * beforeInvocation(@BeforeClass) → afterInvocation(@BeforeClass)
   * beforeInvocation(@BeforeMethod) → afterInvocation(@BeforeMethod)  
   * beforeInvocation(@Test) → afterInvocation(@Test)
   * </pre>
   * 
   * @param method the invoked method wrapper
   * @param testResult the test result object
   * @param context the test context
   */
  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    setStartTime(testResult.getMethod());
  }

  /**
   * Called after each test method invocation to capture end timing.
   * This method records the completion time for @BeforeClass and @BeforeMethod methods.
   * 
   * <p>Note: End time for @Test methods is captured in the onTest* methods
   * to ensure accurate timing even when tests fail or are skipped.</p>
   * 
   * @param method the invoked method wrapper
   * @param testResult the test result object containing execution details
   * @param context the test context
   */
  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    setEndTime(testResult);
  }

  /**
   * Called when a test method passes successfully.
   * Records the successful test result with complete timing information.
   * 
   * <p>This method is thread-safe and can handle concurrent test execution.</p>
   * 
   * <p><strong>Example successful test flow:</strong></p>
   * <pre>
   * @Test
   * public void testLogin() {
   *   // Test passes
   *   assert user.login("valid", "credentials");
   * }
   * // → onTestSuccess() called → test result recorded
   * </pre>
   * 
   * @param result the test result containing execution details and outcome
   */
  @Override
  public synchronized void onTestSuccess(ITestResult result) {
    addResult(result);
  }

  /**
   * Called when a test method fails.
   * Records the failed test result with error details and timing information.
   * 
   * <p>This method captures both assertion failures and unexpected exceptions.</p>
   * 
   * <p><strong>Example failed test scenarios:</strong></p>
   * <pre>
   * @Test
   * public void testInvalidLogin() {
   *   // Assertion failure
   *   assert user.login("invalid", "credentials"); // fails
   * }
   * 
   * @Test  
   * public void testDatabaseConnection() {
   *   // Runtime exception
   *   database.connect(); // throws SQLException
   * }
   * // → onTestFailure() called → failure details recorded
   * </pre>
   * 
   * @param result the test result containing failure details and stack trace
   */
  @Override
  public synchronized void onTestFailure(ITestResult result) {
    addResult(result);
  }

  /**
   * Called when a test method is skipped.
   * Records the skipped test result with timing information.
   * 
   * <p>Tests can be skipped for various reasons:</p>
   * <ul>
   *   <li>Failed dependencies (dependsOnMethods)</li>
   *   <li>Conditional execution (@Test(enabled=false))</li>
   *   <li>Group exclusions</li>
   *   <li>Runtime conditions</li>
   * </ul>
   * 
   * <p><strong>Example skipped test scenarios:</strong></p>
   * <pre>
   * @Test
   * public void setupTest() {
   *   // This test fails
   * }
   * 
   * @Test(dependsOnMethods = "setupTest")
   * public void dependentTest() {
   *   // This gets skipped due to setupTest failure
   * }
   * // → onTestSkipped() called → skip reason recorded
   * </pre>
   * 
   * @param result the test result containing skip details and reason
   */
  @Override
  public synchronized void onTestSkipped(ITestResult result) {
    addResult(result);
  }

  /**
   * Called when a test fails but is within the success percentage threshold.
   * Records the test result even though it technically failed.
   * 
   * <p>This occurs when using TestNG's success percentage feature:</p>
   * <pre>
   * @Test(successPercentage = 80, invocationCount = 10)
   * public void flaky_test() {
   *   // Test may fail up to 20% of invocations and still be considered successful
   * }
   * </pre>
   * 
   * <p>If 8 out of 10 invocations pass, the test is considered successful
   * even though 2 invocations failed.</p>
   * 
   * @param result the test result with failure details but within success threshold
   */
  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    addResult(result);
  }

  /**
   * Called when test execution finishes. Updates the pipeline end timestamp
   * to mark the completion of the entire test suite.
   * 
   * <p>This method ensures that pipeline duration can be accurately calculated
   * by setting the end time when all tests have completed.</p>
   * 
   * <p><strong>Pipeline lifecycle:</strong></p>
   * <pre>
   * onExecutionStart() → pipeline.startTime set
   *   ↓
   * [All test execution]
   *   ↓  
   * onExecutionFinish() → pipeline.endTime set
   * </pre>
   */
  @Override
  public void onExecutionFinish() {
    CPipelineDao.updateEndDate(pipeline.getId(), CDate.now().getTimeStamp());
  }

  /**
   * Initializes the pipeline instance for tracking test executions.
   * This method is thread-safe and ensures only one pipeline is created per test run.
   * 
   * <p>The pipeline is obtained from CPipelineHelper, which handles the
   * configuration and setup based on the current environment context.</p>
   * 
   * <p><strong>Thread Safety:</strong> This method uses synchronization to prevent
   * multiple threads from creating duplicate pipeline instances during
   * parallel test execution.</p>
   */
  private static synchronized void buildPipeline() {
    if (pipeline == null) {
      pipeline = CPipelineHelper.getPipeline();
    }
  }

  /**
   * Records the start time for different types of test method invocations.
   * 
   * <p>This method differentiates between three types of methods:</p>
   * <ul>
   *   <li><strong>@BeforeClass</strong> - Class-level setup, executed once per test class</li>
   *   <li><strong>@BeforeMethod</strong> - Method-level setup, executed before each test method</li>
   *   <li><strong>@Test</strong> - Actual test method execution</li>
   * </ul>
   * 
   * <p><strong>Timing capture example:</strong></p>
   * <pre>
   * @BeforeClass
   * public void setupClass() { } // → beforeClassStartTime recorded
   * 
   * @BeforeMethod  
   * public void setupMethod() { } // → beforeMethodStartTime recorded
   * 
   * @Test
   * public void testFeature() { } // → testStartTime recorded
   * </pre>
   * 
   * @param method the TestNG method being invoked
   */
  private void setStartTime(ITestNGMethod method) {
    if (method.isBeforeClassConfiguration())
      beforeClassStartTime = CDate.now();
    else if (method.isBeforeMethodConfiguration())
      beforeMethodStartTime = CDate.now();
    else if (method.isTest())
      testStartTime = CDate.now();
  }

  /**
   * Records the end time for @BeforeClass and @BeforeMethod invocations.
   * 
   * <p>Note: End times for @Test methods are captured directly in the
   * onTest* callback methods to ensure accurate timing regardless of
   * test outcome (success, failure, or skip).</p>
   * 
   * <p><strong>End time capture:</strong></p>
   * <pre>
   * @BeforeClass setup completes → beforeClassEndTime recorded
   * @BeforeMethod setup completes → beforeMethodEndTime recorded  
   * @Test method completes → end time recorded in onTest* methods
   * </pre>
   * 
   * @param testResult the test result containing method information
   */
  private void setEndTime(ITestResult testResult) {
    ITestNGMethod method = testResult.getMethod();
    if (method.isBeforeClassConfiguration())
      beforeClassEndTime = CDate.now();
    else if (method.isBeforeMethodConfiguration())
      beforeMethodEndTime = CDate.now();
  }

  /**
   * Creates and stores a comprehensive test result record with complete timing information.
   * 
   * <p>This method builds a CTestResult object that includes:</p>
   * <ul>
   *   <li>Test method execution timing (start to end)</li>
   *   <li>@BeforeClass method timing (if applicable)</li>
   *   <li>@BeforeMethod method timing (if applicable)</li>
   *   <li>Test outcome and any error details</li>
   *   <li>Method metadata and parameters</li>
   * </ul>
   * 
   * <p><strong>Complete test execution timeline:</strong></p>
   * <pre>
   * beforeClassStartTime ──────▶ beforeClassEndTime
   *                               │
   * beforeMethodStartTime ─────▶ beforeMethodEndTime  
   *                               │
   * testStartTime ─────────────▶ testEndTime (CDate.now())
   * </pre>
   * 
   * <p>The assembled result is then added to the pipeline for tracking and reporting.</p>
   * 
   * @param testResult the TestNG result containing execution details and outcome
   */
  private void addResult(ITestResult testResult) {
    CTestResult result = new CTestResult(testResult, testStartTime, CDate.now());
    result.setBeforeClassStartTime(beforeClassStartTime);
    result.setBeforeClassEndTime(beforeClassEndTime);

    result.setBeforeMethodStartTime(beforeMethodStartTime);
    result.setBeforeMethodEndTime(beforeMethodEndTime);

    CPipelineHelper.addExecution(pipeline, result);
  }
}

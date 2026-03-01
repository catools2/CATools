package org.catools.common.testng.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.annotations.CAwaiting;
import org.catools.common.annotations.CDefects;
import org.catools.common.annotations.CDeferred;
import org.catools.common.annotations.CIgnored;
import org.catools.common.annotations.COpenDefects;
import org.catools.common.annotations.CRegression;
import org.catools.common.annotations.CSeverity;
import org.catools.common.annotations.CTestIds;
import org.catools.common.collections.CList;
import org.catools.common.config.CTestManagementConfigs;
import org.catools.common.date.CDate;
import org.catools.common.exception.CExceptionInfo;
import org.catools.common.tests.CTest;
import org.catools.common.tests.CTestMetadata;
import org.testng.ITestResult;
import org.testng.annotations.Test;

/**
 * Represents a test result with comprehensive execution information and metadata. This class wraps
 * TestNG's ITestResult and provides additional functionality for test management, including support
 * for custom annotations, timing information, and execution status tracking.
 *
 * <p>The CTestResult class captures detailed information about test execution including:
 *
 * <ul>
 *   <li>Timing information (start/end times for various phases)
 *   <li>Test metadata (IDs, defects, severity, regression depth)
 *   <li>Exception information if the test failed
 *   <li>Custom annotations and parameters
 * </ul>
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Basic Usage:</h4>
 *
 * <pre>{@code
 * // Create from TestNG result
 * ITestResult testngResult = // ... obtained from TestNG
 * CTestResult result = new CTestResult(testngResult);
 *
 * // Get basic information
 * String testName = result.getName();
 * CExecutionStatus status = result.getStatus();
 * long duration = result.getDuration();
 * }</pre>
 *
 * <h4>Creating with Custom Timing:</h4>
 *
 * <pre>{@code
 * Date testStart = new Date();
 * Date testEnd = new Date(testStart.getTime() + 5000); // 5 seconds later
 *
 * CTestResult result = new CTestResult(testngResult, testStart, testEnd);
 *
 * // Access timing information
 * CDate startTime = result.getTestStartTime();
 * CDate endTime = result.getTestEndTime();
 * }</pre>
 *
 * <h4>Accessing Test Metadata:</h4>
 *
 * <pre>{@code
 * CTestResult result = new CTestResult(testngResult);
 *
 * // Get test identifiers
 * CList<String> testIds = result.getTestIds();
 * CList<String> defectIds = result.getDefectIds();
 *
 * // Get execution details
 * String fullName = result.getFullName();
 * String testFullName = result.getTestFullName();
 *
 * // Check if configuration method
 * boolean isConfig = result.isConfigurationMethod();
 * }</pre>
 *
 * @author CA Tools Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class CTestResult implements Comparable<CTestResult> {
  /**
   * The project name retrieved from test management configuration. This is a static field shared
   * across all test results.
   *
   * @return the project name from configuration
   */
  @Getter private static final String project = CTestManagementConfigs.getProjectName();

  /**
   * The version name retrieved from test management configuration. This is a static field shared
   * across all test results.
   *
   * @return the version name from configuration
   */
  @Getter private static final String version = CTestManagementConfigs.getVersionName();

  @JsonIgnore private ITestResult origin;
  private int testExecutionId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate startTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate endTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate testStartTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate testEndTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate beforeClassStartTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate beforeMethodStartTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate beforeClassEndTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private CDate beforeMethodEndTime;

  private String packageName;
  private String className;
  private String methodName;
  private CExecutionStatus status;
  private Object[] annotations;
  private Object[] parameters;
  private CExceptionInfo exceptionInfo;
  private long duration;
  private String name;
  private String description;
  private String host;
  private CList<String> methodGroups = new CList<>();
  private CList<String> testIds = new CList<>();
  private CList<String> defectIds = new CList<>();
  private CList<String> openDefectIds = new CList<>();
  private CList<String> deferredId = new CList<>();
  private String awaiting = StringUtils.EMPTY;
  private boolean configurationMethod = true;
  private Integer severityLevel = null;
  private Integer regressionDepth = null;
  private CTestMetadata executionMetadata = null;

  /**
   * Creates a new CTestResult from a TestNG ITestResult. This constructor extracts all relevant
   * information from the TestNG result and populates the CTestResult fields accordingly.
   *
   * <p>The constructor automatically:
   *
   * <ul>
   *   <li>Extracts class and method information
   *   <li>Processes custom annotations (CTestIds, CDefects, etc.)
   *   <li>Determines execution status based on result and annotations
   *   <li>Calculates duration and timing information
   * </ul>
   *
   * <h4>Example:</h4>
   *
   * <pre>{@code
   * @Test
   * @CTestIds(ids = {"TC001", "TC002"})
   * @CDefects(ids = {"BUG-123"})
   * public void myTest() {
   *     // test implementation
   * }
   *
   * // In TestNG listener
   * public void onTestSuccess(ITestResult result) {
   *     CTestResult cResult = new CTestResult(result);
   *
   *     // Access extracted information
   *     assertEquals("myTest", cResult.getMethodName());
   *     assertEquals(2, cResult.getTestIds().size());
   *     assertEquals("BUG-123", cResult.getDefectIds().get(0));
   * }
   * }</pre>
   *
   * @param testResult the TestNG ITestResult to wrap
   * @throws NullPointerException if testResult is null
   */
  public CTestResult(ITestResult testResult) {
    this(testResult, null, null);
  }

  /**
   * Creates a new CTestResult from a TestNG ITestResult with custom timing information. This
   * constructor allows you to specify custom start and end times for the test execution, which can
   * be useful when you need to track timing information that differs from TestNG's default timing.
   *
   * <p>All the same processing as the single-parameter constructor is performed, plus the custom
   * timing information is stored.
   *
   * <h4>Example:</h4>
   *
   * <pre>{@code
   * // Custom timing scenario
   * Date customStart = new Date();
   * Thread.sleep(1000); // simulate some setup
   *
   * // Run test...
   * ITestResult testngResult = // ... from TestNG execution
   *
   * Date customEnd = new Date();
   * CTestResult result = new CTestResult(testngResult, customStart, customEnd);
   *
   * // The custom times are preserved
   * assertEquals(customStart.getTime(), result.getTestStartTime().getTime());
   * assertEquals(customEnd.getTime(), result.getTestEndTime().getTime());
   * }</pre>
   *
   * @param testResult the TestNG ITestResult to wrap
   * @param testStartTime custom start time for the test (can be null)
   * @param testEndTime custom end time for the test (can be null)
   * @throws NullPointerException if testResult is null
   */
  public CTestResult(ITestResult testResult, Date testStartTime, Date testEndTime) {
    this.origin = testResult;

    this.className = testResult.getMethod().getRealClass().getSimpleName();
    this.packageName = testResult.getMethod().getRealClass().getPackage().getName();

    if (testResult.getMethod().getGroups() != null) {
      this.methodGroups.addAll(List.of(testResult.getMethod().getGroups()));
    }

    if (testResult.getInstance() instanceof CTest) {
      executionMetadata = ((CTest) testResult.getInstance()).getMetadata();
    }

    if (testResult.getThrowable() != null)
      this.exceptionInfo = new CExceptionInfo(testResult.getThrowable());

    Method method = testResult.getMethod().getConstructorOrMethod().getMethod();
    this.methodName = method.getName();
    this.annotations = method.getAnnotations();
    this.parameters = testResult.getParameters();
    this.duration = testResult.getEndMillis() - testResult.getStartMillis();
    this.name = testResult.getName();
    this.host = testResult.getHost();
    this.description = testResult.getMethod().getDescription();

    this.startTime = new CDate(testResult.getStartMillis());
    this.endTime = new CDate(testResult.getEndMillis());

    this.testStartTime = testStartTime == null ? null : new CDate(testStartTime);
    this.testEndTime = testEndTime == null ? null : new CDate(testEndTime);

    boolean ignored = false;
    for (Object annotation : this.annotations) {
      if (annotation instanceof CTestIds a) {
        this.testIds.addAll(List.of(a.ids()));
      } else if (annotation instanceof CDeferred a) {
        this.deferredId.addAll(List.of(a.ids()));
      } else if (annotation instanceof CDefects a) {
        this.defectIds.addAll(List.of(a.ids()));
      } else if (annotation instanceof COpenDefects a) {
        this.openDefectIds.addAll(List.of(a.ids()));
      } else if (annotation instanceof CAwaiting a) {
        this.awaiting = a.cause();
      } else if (annotation instanceof CIgnored) {
        ignored = true;
      } else if (annotation instanceof CRegression a) {
        this.regressionDepth = a.depth();
      } else if (annotation instanceof CSeverity a) {
        this.severityLevel = a.level();
      } else if (annotation instanceof Test) {
        this.configurationMethod = false;
      }
    }

    if (testResult.getStatus() == ITestResult.SUCCESS) {
      this.status = CExecutionStatus.SUCCESS;
    } else if (!awaiting.isEmpty()) {
      this.status = CExecutionStatus.AWAITING;
    } else {
      if (ignored) {
        this.status = CExecutionStatus.IGNORED;
      } else if (!deferredId.isEmpty()) {
        this.status = CExecutionStatus.DEFERRED;
      } else if (testResult.getStatus() == ITestResult.FAILURE) {
        this.status = CExecutionStatus.FAILURE;
      } else {
        this.status = CExecutionStatus.SKIP;
      }
    }
    // set it at the end to make sure we have all values we need
    this.testExecutionId = hashCode();
  }

  /**
   * Returns the full name of the test in the format "ClassName.testName". This provides a concise
   * identifier for the test that includes both the class context and the specific test name.
   *
   * <h4>Example:</h4>
   *
   * <pre>{@code
   * // For a test method named "testLogin" in class "AuthenticationTest"
   * CTestResult result = new CTestResult(testngResult);
   * String fullName = result.getFullName();
   * // Returns: "AuthenticationTest.testLogin"
   *
   * // Useful for logging or reporting
   * logger.info("Executing test: " + result.getFullName());
   * }</pre>
   *
   * @return the full name in format "ClassName.testName"
   */
  public String getFullName() {
    return className + "." + name;
  }

  /**
   * Returns the complete test identifier including package, class, and method name. This format
   * follows the pattern "package.ClassName::methodName" and provides a unique identifier that can
   * be used for test selection or filtering.
   *
   * <h4>Example:</h4>
   *
   * <pre>{@code
   * // For method "testValidLogin" in class "com.example.auth.LoginTest"
   * CTestResult result = new CTestResult(testngResult);
   * String testFullName = result.getTestFullName();
   * // Returns: "com.example.auth.LoginTest::testValidLogin"
   *
   * // Useful for test selection in CI/CD
   * String testSelector = result.getTestFullName();
   * // Can be used with: mvn test -Dtest=com.example.auth.LoginTest::testValidLogin
   * }</pre>
   *
   * @return the complete test identifier in format "package.ClassName::methodName"
   */
  public String getTestFullName() {
    return getPackageName() + "." + getClassName() + "::" + getMethodName();
  }

  /**
   * Compares this CTestResult with another CTestResult for ordering. The comparison is based on the
   * string representation of the objects, providing a consistent ordering for collections of test
   * results.
   *
   * <h4>Example:</h4>
   *
   * <pre>{@code
   * List<CTestResult> results = Arrays.asList(
   *     new CTestResult(testResult1),
   *     new CTestResult(testResult2),
   *     new CTestResult(testResult3)
   * );
   *
   * // Sort results for consistent reporting
   * Collections.sort(results);
   *
   * // Or use in TreeSet for automatic sorting
   * Set<CTestResult> sortedResults = new TreeSet<>(results);
   * }</pre>
   *
   * @param o the CTestResult to compare with
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified object
   * @throws NullPointerException if the specified object is null
   */
  @Override
  public int compareTo(CTestResult o) {
    if (o == null) {
      return -1;
    }
    return toString().compareTo(o.toString());
  }
}

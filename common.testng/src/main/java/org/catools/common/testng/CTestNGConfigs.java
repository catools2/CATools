package org.catools.common.testng;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.testng.ITestNGListener;
import org.testng.xml.XmlSuite;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for managing and retrieving TestNG configuration settings.
 * 
 * <p>This class provides static methods to access various TestNG configuration parameters
 * from HOCON configuration files. All configuration values are retrieved using the
 * CHocon utility class.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Get test retry count
 * int retryCount = CTestNGConfigs.getTestRetryCount();
 * 
 * // Get test listeners
 * Set<ITestNGListener> listeners = CTestNGConfigs.getListeners();
 * 
 * // Get parallel execution mode
 * XmlSuite.ParallelMode mode = CTestNGConfigs.getTestLevelParallel();
 * }</pre>
 */
@UtilityClass
public class CTestNGConfigs {
  
  /**
   * Retrieves the base test class loader from configuration.
   * 
   * <p>This method loads the class specified in the configuration property
   * {@code catools.testng.base_test_class_loader} and returns its Class object.</p>
   * 
   * @return the Class object for the configured base test class loader
   * @throws RuntimeException if the specified class cannot be found
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.base_test_class_loader = "com.example.BaseTestClass"
   * Class<?> baseClass = CTestNGConfigs.getBaseClassLoader();
   * // Returns: Class object for com.example.BaseTestClass
   * }</pre>
   */
  public static Class<?> getBaseClassLoader() {
    try {
      return Class.forName(CHocon.asString(Configs.CATOOLS_TESTNG_BASE_TEST_CLASS_LOADER));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves the configured TestNG listeners.
   * 
   * <p>This method reads the listener class names from the configuration property
   * {@code catools.testng.listeners} and instantiates them. If a listener class
   * cannot be found or instantiated, a warning message is printed and the listener
   * is skipped.</p>
   * 
   * @return a Set of ITestNGListener instances configured for the test execution
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.listeners = ["com.example.TestListener1", "com.example.TestListener2"]
   * Set<ITestNGListener> listeners = CTestNGConfigs.getListeners();
   * // Returns: Set containing instances of TestListener1 and TestListener2
   * }</pre>
   */
  public static Set<ITestNGListener> getListeners() {
    final Set<ITestNGListener> listeners = new HashSet<>();
    if (CHocon.has(Configs.CATOOLS_TESTNG_LISTENERS)) {
      for (String listener : CHocon.asStrings(Configs.CATOOLS_TESTNG_LISTENERS)) {
        try {
          listeners.add((ITestNGListener) Class.forName(listener).getConstructor().newInstance());
        } catch (Exception e) {
          System.out.println(
              "Could not find CATOOLS_TESTNG_LISTENERS parameter " + listener + " in the class path.");
        }
      }
    }
    return listeners;
  }

  /**
   * Retrieves the severity level for test execution.
   * 
   * <p>The severity level determines which tests should be executed based on their
   * severity rating. Tests with severity levels higher than this value may be skipped.</p>
   * 
   * @return the configured severity level as an integer
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.run_severity_level = 3
   * int severityLevel = CTestNGConfigs.getSeverityLevel();
   * // Returns: 3
   * }</pre>
   */
  public static int getSeverityLevel() {
    return CHocon.asInteger(Configs.CATOOLS_TESTNG_RUN_SEVERITY_LEVEL);
  }

  /**
   * Retrieves the regression depth for test execution.
   * 
   * <p>The regression depth determines how many levels of regression tests should be
   * executed. Higher values indicate more comprehensive regression testing.</p>
   * 
   * @return the configured regression depth as an integer
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.run_regression_depth = 2
   * int regressionDepth = CTestNGConfigs.getRegressionDepth();
   * // Returns: 2
   * }</pre>
   */
  public static int getRegressionDepth() {
    return CHocon.asInteger(Configs.CATOOLS_TESTNG_RUN_REGRESSION_DEPTH);
  }

  /**
   * Retrieves the list of annotations that will cause a test to be ignored if ANY of them are present.
   * 
   * <p>If a test method or class is annotated with any of the annotations in this list,
   * the test will be skipped during execution.</p>
   * 
   * @return a CList containing the annotation class names to ignore tests for (ANY match)
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.ignore_test_with_any_annotation = ["@Deprecated", "@Flaky"]
   * CList<String> annotations = CTestNGConfigs.getAnnotationsToIgnoreTestIfAnyMatch();
   * // Returns: CList containing ["@Deprecated", "@Flaky"]
   * // A test with either @Deprecated OR @Flaky will be ignored
   * }</pre>
   */
  public static CList<String> getAnnotationsToIgnoreTestIfAnyMatch() {
    return CList.of(CHocon.asStrings(Configs.CATOOLS_TESTNG_IGNORE_TEST_WITH_ANY_ANNOTATION));
  }

  /**
   * Retrieves the list of annotations that will cause a test to be ignored if ALL of them are present.
   * 
   * <p>If a test method or class is annotated with all of the annotations in this list,
   * the test will be skipped during execution.</p>
   * 
   * @return a CList containing the annotation class names to ignore tests for (ALL match)
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.ignore_test_with_all_annotation = ["@Slow", "@Integration"]
   * CList<String> annotations = CTestNGConfigs.getAnnotationsToIgnoreTestIfAllMatch();
   * // Returns: CList containing ["@Slow", "@Integration"]
   * // A test will be ignored only if it has BOTH @Slow AND @Integration
   * }</pre>
   */
  public static CList<String> getAnnotationsToIgnoreTestIfAllMatch() {
    return CList.of(CHocon.asStrings(Configs.CATOOLS_TESTNG_IGNORE_TEST_WITH_ALL_ANNOTATION));
  }

  /**
   * Retrieves the list of annotations that must ALL be present for a test to be executed.
   * 
   * <p>Only tests that are annotated with all of the annotations in this list will be
   * executed. This provides a way to filter tests to run only specific subsets.</p>
   * 
   * @return a CList containing the annotation class names required for test execution (ALL match)
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.run_test_with_all_annotations = ["@Smoke", "@Critical"]
   * CList<String> annotations = CTestNGConfigs.getAnnotationsToRunTestIfAllMatch();
   * // Returns: CList containing ["@Smoke", "@Critical"]
   * // Only tests with BOTH @Smoke AND @Critical will be executed
   * }</pre>
   */
  public static CList<String> getAnnotationsToRunTestIfAllMatch() {
    return CList.of(CHocon.asStrings(Configs.CATOOLS_TESTNG_RUN_TEST_WITH_ALL_ANNOTATIONS));
  }

  /**
   * Retrieves the list of annotations where ANY one present will allow a test to be executed.
   * 
   * <p>Tests that are annotated with any of the annotations in this list will be
   * executed. This provides a way to run tests that have at least one of the specified annotations.</p>
   * 
   * @return a CList containing the annotation class names that trigger test execution (ANY match)
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.run_test_with_any_annotations = ["@Smoke", "@Regression"]
   * CList<String> annotations = CTestNGConfigs.getAnnotationsToRunTestIfAnyMatch();
   * // Returns: CList containing ["@Smoke", "@Regression"]
   * // Tests with either @Smoke OR @Regression will be executed
   * }</pre>
   */
  public static CList<String> getAnnotationsToRunTestIfAnyMatch() {
    return CList.of(CHocon.asStrings(Configs.CATOOLS_TESTNG_RUN_TEST_WITH_ANY_ANNOTATIONS));
  }

  /**
   * Retrieves the number of times a failed test should be retried.
   * 
   * <p>When a test fails, it can be automatically retried up to this many times
   * before being marked as failed. A value of 0 means no retries.</p>
   * 
   * @return the configured test retry count as an integer
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.test_retry_count = 2
   * int retryCount = CTestNGConfigs.getTestRetryCount();
   * // Returns: 2
   * // Failed tests will be retried up to 2 times
   * }</pre>
   */
  public static int getTestRetryCount() {
    return CHocon.asInteger(Configs.CATOOLS_TESTNG_TEST_RETRY_COUNT);
  }

  /**
   * Retrieves the set of test packages to be scanned for test classes.
   * 
   * <p>This method returns the package names that TestNG should scan for test classes.
   * Only classes in these packages (and their sub-packages) will be considered for test execution.</p>
   * 
   * @return a CSet containing the package names to scan for tests
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.test_packages = ["com.example.tests", "com.example.integration"]
   * CSet<String> packages = CTestNGConfigs.getTestPackages();
   * // Returns: CSet containing ["com.example.tests", "com.example.integration"]
   * }</pre>
   */
  public static CSet<String> getTestPackages() {
    return new CSet<>(CHocon.asStrings(Configs.CATOOLS_TESTNG_TEST_PACKAGES));
  }

  /**
   * Retrieves the parallel execution mode for test level parallelism.
   * 
   * <p>This determines how TestNG should execute tests in parallel at the test level.
   * Common values include METHODS, CLASSES, TESTS, or NONE.</p>
   * 
   * @return the configured parallel mode for test level execution
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.test_level.parallel_mode = "METHODS"
   * XmlSuite.ParallelMode mode = CTestNGConfigs.getTestLevelParallel();
   * // Returns: XmlSuite.ParallelMode.METHODS
   * }</pre>
   */
  public static XmlSuite.ParallelMode getTestLevelParallel() {
    return CHocon.asEnum(Configs.CATOOLS_TESTNG_TEST_LEVEL_PARALLEL_MODE, XmlSuite.ParallelMode.class);
  }

  /**
   * Retrieves the thread count for test level parallel execution.
   * 
   * <p>This specifies how many threads should be used when executing tests in parallel
   * at the test level. This works in conjunction with the test level parallel mode.</p>
   * 
   * @return the configured thread count for test level parallel execution
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.test_level.thread_count = 4
   * int threadCount = CTestNGConfigs.getTestLevelThreadCount();
   * // Returns: 4
   * // Up to 4 threads will be used for parallel test execution
   * }</pre>
   */
  public static int getTestLevelThreadCount() {
    return CHocon.asInteger(Configs.CATOOLS_TESTNG_TEST_LEVEL_THREAD_COUNT);
  }

  /**
   * Retrieves the parallel execution mode for suite level parallelism.
   * 
   * <p>This determines how TestNG should execute test suites in parallel at the suite level.
   * This is typically used when running multiple test suites concurrently.</p>
   * 
   * @return the configured parallel mode for suite level execution
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.suite_level.parallel_mode = "TESTS"
   * XmlSuite.ParallelMode mode = CTestNGConfigs.getSuiteLevelParallel();
   * // Returns: XmlSuite.ParallelMode.TESTS
   * }</pre>
   */
  public static XmlSuite.ParallelMode getSuiteLevelParallel() {
    return CHocon.asEnum(Configs.CATOOLS_TESTNG_SUITE_LEVEL_PARALLEL_MODE, XmlSuite.ParallelMode.class);
  }

  /**
   * Retrieves the thread count for suite level parallel execution.
   * 
   * <p>This specifies how many threads should be used when executing test suites in parallel
   * at the suite level. This works in conjunction with the suite level parallel mode.</p>
   * 
   * @return the configured thread count for suite level parallel execution
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.suite_level.thread_count = 2
   * int threadCount = CTestNGConfigs.getSuiteLevelThreadCount();
   * // Returns: 2
   * // Up to 2 threads will be used for parallel suite execution
   * }</pre>
   */
  public static int getSuiteLevelThreadCount() {
    return CHocon.asInteger(Configs.CATOOLS_TESTNG_SUITE_LEVEL_THREAD_COUNT);
  }

  /**
   * Determines whether to skip entire test classes that contain awaiting tests.
   * 
   * <p>When set to true, if any test method in a class is marked as "awaiting" (e.g., pending
   * implementation or resolution), the entire class will be skipped during test execution.</p>
   * 
   * @return true if classes with awaiting tests should be skipped, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.skip_class_with_awaiting_test = true
   * boolean skipClass = CTestNGConfigs.skipClassWithAwaitingTest();
   * // Returns: true
   * // Classes containing any awaiting tests will be completely skipped
   * }</pre>
   */
  public static boolean skipClassWithAwaitingTest() {
    return CHocon.asBoolean(Configs.CATOOLS_TESTNG_SKIP_CLASS_WITH_AWAITING_TEST);
  }

  /**
   * Determines whether to skip entire test classes that contain ignored tests.
   * 
   * <p>When set to true, if any test method in a class is marked as ignored (e.g., using
   * {@code @Ignore} annotation), the entire class will be skipped during test execution.</p>
   * 
   * @return true if classes with ignored tests should be skipped, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.skip_class_with_ignored_test = false
   * boolean skipClass = CTestNGConfigs.skipClassWithIgnoredTest();
   * // Returns: false
   * // Classes with ignored tests will still run their non-ignored test methods
   * }</pre>
   */
  public static boolean skipClassWithIgnoredTest() {
    return CHocon.asBoolean(Configs.CATOOLS_TESTNG_SKIP_CLASS_WITH_IGNORED_TEST);
  }

  /**
   * Retrieves the name for the TestNG result XML file.
   * 
   * <p>This specifies the filename that will be used for the TestNG results XML output.
   * The XML file contains detailed information about test execution results.</p>
   * 
   * @return the configured name for the TestNG result XML file
   * 
   * @example
   * <pre>{@code
   * // Configuration: catools.testng.result_xml_name = "testng-results.xml"
   * String resultName = CTestNGConfigs.getTestNgResultName();
   * // Returns: "testng-results.xml"
   * }</pre>
   */
  public static String getTestNgResultName() {
    return CHocon.asString(Configs.CATOOLS_TESTNG_RESULT_XML_NAME);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_TESTNG_TEST_PACKAGES("catools.testng.test_packages"),
    CATOOLS_TESTNG_TEST_LEVEL_PARALLEL_MODE("catools.testng.test_level.parallel_mode"),
    CATOOLS_TESTNG_TEST_LEVEL_THREAD_COUNT("catools.testng.test_level.thread_count"),
    CATOOLS_TESTNG_SUITE_LEVEL_PARALLEL_MODE("catools.testng.suite_level.parallel_mode"),
    CATOOLS_TESTNG_SUITE_LEVEL_THREAD_COUNT("catools.testng.suite_level.thread_count"),
    CATOOLS_TESTNG_LISTENERS("catools.testng.listeners"),
    CATOOLS_TESTNG_TEST_RETRY_COUNT("catools.testng.test_retry_count"),
    CATOOLS_TESTNG_BASE_TEST_CLASS_LOADER("catools.testng.base_test_class_loader"),
    CATOOLS_TESTNG_SKIP_CLASS_WITH_AWAITING_TEST("catools.testng.skip_class_with_awaiting_test"),
    CATOOLS_TESTNG_SKIP_CLASS_WITH_IGNORED_TEST("catools.testng.skip_class_with_ignored_test"),
    CATOOLS_TESTNG_RUN_SEVERITY_LEVEL("catools.testng.run_severity_level"),
    CATOOLS_TESTNG_RUN_REGRESSION_DEPTH("catools.testng.run_regression_depth"),
    CATOOLS_TESTNG_IGNORE_TEST_WITH_ANY_ANNOTATION("catools.testng.ignore_test_with_any_annotation"),
    CATOOLS_TESTNG_IGNORE_TEST_WITH_ALL_ANNOTATION("catools.testng.ignore_test_with_all_annotation"),
    CATOOLS_TESTNG_RUN_TEST_WITH_ALL_ANNOTATIONS("catools.testng.run_test_with_all_annotations"),
    CATOOLS_TESTNG_RUN_TEST_WITH_ANY_ANNOTATIONS("catools.testng.run_test_with_any_annotations"),
    CATOOLS_TESTNG_RESULT_XML_NAME("catools.testng.result_xml_name");

    private final String path;
  }
}

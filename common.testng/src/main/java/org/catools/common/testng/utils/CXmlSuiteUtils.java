package org.catools.common.testng.utils;

import lombok.experimental.UtilityClass;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.testng.CTestNGConfigs;
import org.catools.common.utils.CObjectUtil;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.function.Consumer;

/**
 * Utility class for creating and managing TestNG XML test suites and tests.
 * 
 * <p>This utility class provides methods to dynamically build TestNG XML test suites
 * and tests from various sources such as issue keys, test class names, and existing tests.
 * It integrates with the catools TestNG configuration system to apply parallel execution
 * settings and other test execution parameters.</p>
 * 
 * <p>The class supports:</p>
 * <ul>
 *   <li>Building tests from issue keys with optional filtering</li>
 *   <li>Creating test suites from collections of test classes</li>
 *   <li>Configuring parallel execution at suite and test levels</li>
 *   <li>Copying and modifying existing test suites</li>
 * </ul>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Build a test from issue keys
 * CSet<String> issueKeys = CSet.of("ISSUE-123", "ISSUE-456");
 * XmlTest test = CXmlSuiteUtils.buildTestForIssueKeys(issueKeys, "Regression Test", true);
 * 
 * // Build a test suite from multiple test groups
 * CHashMap<String, CSet<String>> testGroups = new CHashMap<>();
 * testGroups.put("Smoke Tests", CSet.of("com.example.SmokeTest1", "com.example.SmokeTest2"));
 * testGroups.put("Integration Tests", CSet.of("com.example.IntegrationTest1"));
 * 
 * XmlSuite suite = CXmlSuiteUtils.buildTestSuiteForClasses(
 *     testGroups, 
 *     "Full Regression Suite", 
 *     xmlSuite -> xmlSuite.setVerbose(2)
 * );
 * }</pre>
 * 
 * @since 1.0
 */
@UtilityClass
public class CXmlSuiteUtils {
  /**
   * Builds a TestNG XML test from a collection of issue keys.
   * 
   * <p>This method converts issue keys into their corresponding test class names
   * and creates an XML test configuration. The issue keys are resolved to test
   * classes using the {@link CTestClassUtil#getClassNameForIssueKeys} method.</p>
   * 
   * @param issueIds a set of issue identifiers (e.g., "JIRA-123", "ISSUE-456")
   * @param testName the name to assign to the created XML test
   * @param filterTestsWhichWillSkipInRun if true, excludes test classes that would
   *                                      be skipped during execution due to configuration
   *                                      filters (annotations, severity, etc.)
   * @return an XmlTest object configured with the test classes corresponding to the issue keys
   * 
   * @example
   * <pre>{@code
   * // Create a test for specific issues with filtering enabled
   * CSet<String> issueKeys = CSet.of("JIRA-1001", "JIRA-1002", "JIRA-1003");
   * XmlTest regressionTest = CXmlSuiteUtils.buildTestForIssueKeys(
   *     issueKeys, 
   *     "Critical Issues Regression", 
   *     true  // Filter out tests that would be skipped
   * );
   * 
   * // The resulting test will contain only the test classes that:
   * // 1. Are associated with the specified issue keys
   * // 2. Would actually execute (not filtered out by annotations/severity)
   * }</pre>
   * 
   * @see CTestClassUtil#getClassNameForIssueKeys(CSet, boolean)
   * @see #buildTestForClasses(CSet, String)
   */
  public static XmlTest buildTestForIssueKeys(CSet<String> issueIds, String testName, boolean filterTestsWhichWillSkipInRun) {
    CSet<String> classNameForIssueKeys = CTestClassUtil.getClassNameForIssueKeys(issueIds, filterTestsWhichWillSkipInRun);
    return buildTestForClasses(classNameForIssueKeys, testName);
  }

  /**
   * Builds a TestNG XML test suite from a map of test class collections.
   * 
   * <p>This method creates a complete test suite containing multiple tests, where each
   * test is built from a collection of test classes. The method automatically applies
   * parallel execution configuration from {@link CTestNGConfigs} and allows for
   * custom suite-level adjustments through the xmlSuiteAdjuster parameter.</p>
   * 
   * @param testClasses a map where keys are test names and values are sets of test class names.
   *                   Each map entry will become a separate XmlTest within the suite
   * @param suiteName the name to assign to the created XML test suite
   * @param xmlSuiteAdjuster optional consumer function to perform additional configuration
   *                        on the suite after it's created (can be null)
   * @return a complete XmlSuite object with all tests configured and parallel settings applied
   * 
   * @example
   * <pre>{@code
   * // Create a comprehensive test suite with multiple test groups
   * CHashMap<String, CSet<String>> testGroups = new CHashMap<>();
   * testGroups.put("Smoke Tests", CSet.of(
   *     "com.example.LoginTest", 
   *     "com.example.HomePageTest"
   * ));
   * testGroups.put("API Tests", CSet.of(
   *     "com.example.UserApiTest", 
   *     "com.example.ProductApiTest"
   * ));
   * testGroups.put("Integration Tests", CSet.of(
   *     "com.example.DatabaseIntegrationTest"
   * ));
   * 
   * // Build suite with custom configuration
   * XmlSuite suite = CXmlSuiteUtils.buildTestSuiteForClasses(
   *     testGroups,
   *     "Full Regression Suite",
   *     xmlSuite -> {
   *         xmlSuite.setVerbose(2);
   *         xmlSuite.setPreserveOrder(true);
   *         xmlSuite.addParameter("environment", "staging");
   *     }
   * );
   * 
   * // The resulting suite will have:
   * // - 3 separate tests ("Smoke Tests", "API Tests", "Integration Tests")
   * // - Parallel execution configured from CTestNGConfigs
   * // - Custom verbose level, preserve order, and environment parameter
   * }</pre>
   * 
   * @see #buildTestForClasses(CSet, String)
   * @see #buildTestSuiteForTests(CList, String, Consumer)
   * @see CTestNGConfigs#getSuiteLevelParallel()
   * @see CTestNGConfigs#getSuiteLevelThreadCount()
   */
  public static XmlSuite buildTestSuiteForClasses(CHashMap<String, CSet<String>> testClasses, String suiteName, Consumer<XmlSuite> xmlSuiteAdjuster) {
    CList<XmlTest> tests = testClasses.asSet().mapToList(e -> buildTestForClasses(e.getValue(), e.getKey()));
    return buildTestSuiteForTests(tests, suiteName, xmlSuiteAdjuster);
  }

  /**
   * Builds a TestNG XML test from a collection of test class names.
   * 
   * <p>This method creates an XmlTest object configured with the specified test classes.
   * The test classes are automatically sorted alphabetically to ensure consistent
   * test execution order across different runs.</p>
   * 
   * @param testClasses a set of fully qualified test class names (e.g., "com.example.MyTest")
   * @param testName the name to assign to the created XML test
   * @return an XmlTest object configured with the specified test classes, sorted alphabetically
   * 
   * @example
   * <pre>{@code
   * // Create a test with multiple test classes
   * CSet<String> testClasses = CSet.of(
   *     "com.example.integration.DatabaseTest",
   *     "com.example.integration.ApiTest", 
   *     "com.example.unit.UtilityTest"
   * );
   * 
   * XmlTest integrationTest = CXmlSuiteUtils.buildTestForClasses(
   *     testClasses, 
   *     "Integration Test Suite"
   * );
   * 
   * // The resulting test will contain the classes in alphabetical order:
   * // 1. com.example.integration.ApiTest
   * // 2. com.example.integration.DatabaseTest  
   * // 3. com.example.unit.UtilityTest
   * }</pre>
   * 
   * @see XmlTest
   * @see XmlClass
   */
  public static XmlTest buildTestForClasses(CSet<String> testClasses, String testName) {
    XmlTest xmlTest = new XmlTest();
    xmlTest.setName(testName);

    // Sort list
    CList<String> listForSorting = testClasses.toList();
    listForSorting.sort(String::compareTo);
    xmlTest.setXmlClasses(listForSorting.mapToList(XmlClass::new));
    return xmlTest;
  }

  /**
   * Builds a complete TestNG XML test suite from a collection of XML tests.
   * 
   * <p>This method creates a fully configured TestNG test suite with the following features:</p>
   * <ul>
   *   <li>Configures parallel execution settings from {@link CTestNGConfigs}</li>
   *   <li>Sets up thread counts for both suite and test levels</li>
   *   <li>Enables return values (required for some TestNG features)</li>
   *   <li>Applies custom configuration through the xmlSuiteAdjuster</li>
   *   <li>Properly associates all tests with the suite</li>
   * </ul>
   * 
   * <p>Parallel execution is configured at two levels:</p>
   * <ul>
   *   <li><strong>Suite Level:</strong> Controls how multiple tests within the suite run in parallel</li>
   *   <li><strong>Test Level:</strong> Controls how test methods within each test run in parallel</li>
   * </ul>
   * 
   * @param tests a list of XmlTest objects to include in the suite
   * @param suiteName the name to assign to the created XML test suite
   * @param xmlSuiteAdjuster optional consumer function to perform additional configuration
   *                        on the suite after basic setup is complete (can be null)
   * @return a complete XmlSuite object with all tests configured and settings applied
   * 
   * @example
   * <pre>{@code
   * // Create individual tests
   * XmlTest smokeTest = CXmlSuiteUtils.buildTestForClasses(
   *     CSet.of("com.example.SmokeTest"), "Smoke Tests"
   * );
   * XmlTest regressionTest = CXmlSuiteUtils.buildTestForClasses(
   *     CSet.of("com.example.RegressionTest"), "Regression Tests"
   * );
   * 
   * // Build a comprehensive suite
   * CList<XmlTest> allTests = CList.of(smokeTest, regressionTest);
   * XmlSuite completeSuite = CXmlSuiteUtils.buildTestSuiteForTests(
   *     allTests,
   *     "Nightly Build Suite",
   *     suite -> {
   *         // Add custom parameters
   *         suite.addParameter("build.version", "1.2.3");
   *         suite.addParameter("test.environment", "staging");
   *         
   *         // Configure additional settings
   *         suite.setVerbose(1);
   *         suite.setPreserveOrder(true);
   *         
   *         // Add suite-level listeners
   *         suite.addListener("com.example.CustomTestListener");
   *     }
   * );
   * 
   * // The resulting suite will have:
   * // - Parallel execution configured from CTestNGConfigs
   * // - Custom parameters and listeners
   * // - Both tests properly associated with the suite
   * // - Thread counts applied based on configuration
   * }</pre>
   * 
   * @see CTestNGConfigs#getSuiteLevelParallel()
   * @see CTestNGConfigs#getSuiteLevelThreadCount()
   * @see CTestNGConfigs#getTestLevelParallel()
   * @see CTestNGConfigs#getTestLevelThreadCount()
   * @see XmlSuite.ParallelMode
   */
  public static XmlSuite buildTestSuiteForTests(CList<XmlTest> tests, String suiteName, Consumer<XmlSuite> xmlSuiteAdjuster) {
    XmlSuite xmlSuite = new XmlSuite();
    xmlSuite.setName(suiteName);
    xmlSuite.setAllowReturnValues(true);
    if (!XmlSuite.ParallelMode.NONE.equals(CTestNGConfigs.getSuiteLevelParallel())) {
      xmlSuite.setParallel(CTestNGConfigs.getSuiteLevelParallel());
    }

    if (CTestNGConfigs.getSuiteLevelThreadCount() > 0) {
      xmlSuite.setThreadCount(CTestNGConfigs.getSuiteLevelThreadCount());
    }

    tests.forEach(test -> {
      if (!XmlSuite.ParallelMode.NONE.equals(CTestNGConfigs.getTestLevelParallel())) {
        test.setParallel(CTestNGConfigs.getTestLevelParallel());
      }

      if (CTestNGConfigs.getTestLevelThreadCount() > 0) {
        test.setThreadCount(CTestNGConfigs.getTestLevelThreadCount());
      }

      test.setSuite(xmlSuite);
      xmlSuite.addTest(test);
    });

    if (xmlSuiteAdjuster != null) {
      xmlSuiteAdjuster.accept(xmlSuite);
    }
    return xmlSuite;
  }

  /**
   * Creates a deep copy of an existing TestNG XML test suite with a modified name.
   * 
   * <p>This method performs a deep clone of the provided XmlSuite object and modifies
   * the suite name by appending the specified postfix. This is useful for creating
   * variations of existing test suites (e.g., for different environments, retry runs,
   * or parallel execution scenarios).</p>
   * 
   * <p>The copy includes all configuration from the original suite:</p>
   * <ul>
   *   <li>All tests and their associated test classes</li>
   *   <li>Parallel execution settings</li>
   *   <li>Thread counts and other execution parameters</li>
   *   <li>Suite-level parameters and listeners</li>
   *   <li>All other suite-level configuration</li>
   * </ul>
   * 
   * @param xmlSuite the original XmlSuite to copy
   * @param suiteNamePostfix the postfix to append to the original suite name
   * @return a new XmlSuite object that is an exact copy of the original with the modified name
   * 
   * @example
   * <pre>{@code
   * // Create an original test suite
   * XmlSuite originalSuite = CXmlSuiteUtils.buildTestSuiteForClasses(
   *     testClasses, 
   *     "Regression Suite", 
   *     suite -> suite.setVerbose(2)
   * );
   * 
   * // Create copies for different environments
   * XmlSuite stagingSuite = CXmlSuiteUtils.copy(originalSuite, " - Staging");
   * XmlSuite prodSuite = CXmlSuiteUtils.copy(originalSuite, " - Production");
   * XmlSuite retrySuite = CXmlSuiteUtils.copy(originalSuite, " - Retry");
   * 
   * // Results:
   * // - stagingSuite.getName() returns "Regression Suite - Staging"
   * // - prodSuite.getName() returns "Regression Suite - Production"  
   * // - retrySuite.getName() returns "Regression Suite - Retry"
   * // - All suites have identical configuration except for the name
   * }</pre>
   * 
   * @see CObjectUtil#clone(Object)
   * @see XmlSuite#setName(String)
   */
  public static XmlSuite copy(XmlSuite xmlSuite, String suiteNamePostfix) {
    XmlSuite suite = CObjectUtil.clone(xmlSuite);
    suite.setName(suite.getName() + suiteNamePostfix);
    return suite;
  }
}

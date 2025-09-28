package org.catools.common.testng;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.date.CDate;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.testng.utils.CTestClassUtil;
import org.catools.common.testng.utils.CXmlSuiteUtils;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.testng.xml.internal.Parser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A utility class for processing TestNG test suites, providing methods to build, configure,
 * and execute TestNG suites programmatically.
 * 
 * <p>This class supports various ways of running TestNG tests:
 * <ul>
 *   <li>Building test suites from class collections</li>
 *   <li>Processing XML suite files (local or from resources)</li>
 *   <li>Running tests by test IDs or class names</li>
 *   <li>Executing XML suites with custom configurations</li>
 * </ul>
 * 
 * @author CATools Team
 * @since 1.0
 */
@Slf4j
public class CTestNGProcessor {

  /**
   * Builds a TestNG XML suite file from a map of test classes grouped by test names.
   * 
   * <p>This method creates an XML suite file that can be used to run TestNG tests.
   * The test classes are organized into groups, and each group becomes a test within the suite.
   * 
   * @param testClassesMap a map where keys are test group names and values are sets of test class names
   * @param suiteName the name of the TestNG suite
   * @param filename the path where the XML suite file will be created
   * @param xmlSuiteAdjuster an optional consumer to customize the XmlSuite before writing to file
   * @return a CFile object representing the created XML suite file
   * 
   * @example
   * <pre>{@code
   * CHashMap<String, CSet<String>> testMap = new CHashMap<>();
   * testMap.put("LoginTests", CSet.of("com.example.LoginTest", "com.example.AuthTest"));
   * testMap.put("UserTests", CSet.of("com.example.UserTest"));
   * 
   * CFile suiteFile = CTestNGProcessor.buildTestSuite(
   *     testMap, 
   *     "MyTestSuite", 
   *     "output/suite.xml",
   *     suite -> suite.setParallel(XmlSuite.ParallelMode.METHODS)
   * );
   * }</pre>
   */
  public static CFile buildTestSuite(CHashMap<String, CSet<String>> testClassesMap, String suiteName, String filename, Consumer<XmlSuite> xmlSuiteAdjuster) {
    CFile file = new CFile(filename);
    file.getParentFile().mkdirs();
    file.write(CXmlSuiteUtils.buildTestSuiteForClasses(testClassesMap, suiteName, xmlSuiteAdjuster).toXml());
    return file;
  }

  /**
   * Builds a TestNG XML suite file from a collection of test classes, with optional grouping.
   * 
   * <p>This method provides a convenient way to create a TestNG suite from a simple set of test classes.
   * If no group mapper is provided, all classes are placed in a single test group named "Test".
   * 
   * @param testClasses a set of fully qualified test class names
   * @param filename the path where the XML suite file will be created
   * @param testClassGroupMapper an optional function to group test classes; if null, all classes go to "Test" group
   * @param xmlSuiteAdjuster an optional consumer to customize the XmlSuite before writing to file
   * @return a CFile object representing the created XML suite file
   * 
   * @example
   * <pre>{@code
   * CSet<String> testClasses = CSet.of(
   *     "com.example.LoginTest",
   *     "com.example.UserTest",
   *     "com.example.PaymentTest"
   * );
   * 
   * // Group tests by package
   * Function<CSet<String>, Map<String, CSet<String>>> grouper = classes -> {
   *     Map<String, CSet<String>> groups = new HashMap<>();
   *     classes.forEach(cls -> {
   *         String groupName = cls.contains("Login") ? "AuthTests" : "CoreTests";
   *         groups.computeIfAbsent(groupName, k -> new CSet<>()).add(cls);
   *     });
   *     return groups;
   * };
   * 
   * CFile suite = CTestNGProcessor.buildTestSuiteForClasses(
   *     testClasses,
   *     "output/grouped-suite.xml",
   *     grouper,
   *     xmlSuite -> xmlSuite.setThreadCount(3)
   * );
   * }</pre>
   */
  public static CFile buildTestSuiteForClasses(CSet<String> testClasses, String filename, Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, Consumer<XmlSuite> xmlSuiteAdjuster) {
    CHashMap<String, CSet<String>> map = new CHashMap<>();
    if (testClassGroupMapper == null) {
      map.put("Test", testClasses);
    } else {
      map.putAll(testClassGroupMapper.apply(testClasses));
    }
    return buildTestSuite(map, "Suite", filename, xmlSuiteAdjuster);
  }

  /**
   * Processes and executes a TestNG XML suite file from the local filesystem.
   * 
   * <p>This method loads a TestNG XML suite file from the local filesystem and executes it.
   * The file must exist and have a .xml extension.
   * 
   * @param suiteName the path to the XML suite file (must end with .xml)
   * @return the TestNG execution status code (0 for success, non-zero for failure)
   * @throws IllegalArgumentException if the file doesn't end with .xml or doesn't exist
   * 
   * @example
   * <pre>{@code
   * // Execute a local TestNG suite file
   * int result = CTestNGProcessor.processLocalXmlSuite("src/test/resources/smoke-tests.xml");
   * if (result == 0) {
   *     System.out.println("All tests passed!");
   * } else {
   *     System.out.println("Some tests failed with status: " + result);
   * }
   * }</pre>
   */
  public static int processLocalXmlSuite(String suiteName) {
    CVerify.Bool.isTrue(suiteName.toLowerCase().trim().endsWith(".xml"), "TestNG suite file name should end with xml.");
    CFile localXmlFile = new CFile(suiteName);
    CVerify.Bool.isTrue(localXmlFile.exists(), "Xml file exists. file: " + localXmlFile.getCanonicalPath());
    print("Running local xml file:" + localXmlFile.getCanonicalPath());
    return processFile(localXmlFile);
  }

  /**
   * Processes and executes a TestNG XML suite file from the classpath resources.
   * 
   * <p>This method loads a TestNG XML suite file from the classpath resources, copies it to
   * a temporary location, and executes it. The resource must exist and have a .xml extension.
   * 
   * @param suiteName the name/path of the XML suite resource (must end with .xml)
   * @return the TestNG execution status code (0 for success, non-zero for failure)
   * @throws IllegalArgumentException if the resource doesn't end with .xml or doesn't exist
   * 
   * @example
   * <pre>{@code
   * // Execute a TestNG suite from classpath resources
   * int result = CTestNGProcessor.processResourceXmlSuite("testng-suites/regression.xml");
   * 
   * // Can also be used with resources in JAR files
   * int result2 = CTestNGProcessor.processResourceXmlSuite("integration-tests.xml");
   * 
   * if (result == 0) {
   *     System.out.println("Regression tests completed successfully!");
   * }
   * }</pre>
   */
  public static int processResourceXmlSuite(String suiteName) {
    CVerify.Bool.isTrue(suiteName.toLowerCase().trim().endsWith(".xml"), "TestNG suite file name should end with xml.");
    String resourceContent = new CResource(suiteName, CTestNGConfigs.getBaseClassLoader()).getString();
    CVerify.String.isNotBlank(resourceContent, "Xml resource file exists and it is not empty. Resource Name: " + suiteName);
    CFile localXmlFile = CFile.fromTmp(suiteName);
    localXmlFile.write(resourceContent);
    CVerify.Bool.isTrue(localXmlFile.exists(), "Xml file copied to resource folder. file: " + localXmlFile);
    return processFile(localXmlFile);
  }

  /**
   * Processes and executes tests identified by their test IDs/issue keys.
   * 
   * <p>This method resolves test class names from the provided test IDs, creates a TestNG suite,
   * and executes it. Test IDs are typically issue keys or identifiers that map to specific test classes.
   * 
   * @param issueIds a set of test IDs or issue keys to execute
   * @param filename the path where the generated XML suite file will be created
   * @param testClassGroupMapper an optional function to group test classes; if null, all classes go to "Test" group
   * @param xmlSuiteAdjuster an optional consumer to customize the XmlSuite before execution
   * @param filterTestsWhichWillSkipInRun if true, filters out tests that would be skipped during execution
   * @return the TestNG execution status code (0 for success, non-zero for failure)
   * 
   * @example
   * <pre>{@code
   * CSet<String> testIds = CSet.of("PROJ-123", "PROJ-456", "PROJ-789");
   * 
   * int result = CTestNGProcessor.processTestByTestIds(
   *     testIds,
   *     "output/test-by-ids.xml",
   *     null, // Use default grouping
   *     suite -> {
   *         suite.setParallel(XmlSuite.ParallelMode.CLASSES);
   *         suite.setThreadCount(2);
   *     },
   *     true // Filter out tests that would skip
   * );
   * 
   * System.out.println("Test execution completed with status: " + result);
   * }</pre>
   */
  public static int processTestByTestIds(CSet<String> issueIds, String filename, Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, Consumer<XmlSuite> xmlSuiteAdjuster, boolean filterTestsWhichWillSkipInRun) {
    return processTestClasses(CTestClassUtil.getClassNameForIssueKeys(issueIds, filterTestsWhichWillSkipInRun), filename, testClassGroupMapper, xmlSuiteAdjuster);
  }

  /**
   * Processes and executes tests by their fully qualified class names.
   * 
   * <p>This method creates a TestNG suite from the provided test class names and executes it.
   * This is useful when you know exactly which test classes you want to run.
   * 
   * @param classNames a set of fully qualified test class names
   * @param filename the path where the generated XML suite file will be created
   * @param testClassGroupMapper an optional function to group test classes; if null, all classes go to "Test" group
   * @param xmlSuiteAdjuster an optional consumer to customize the XmlSuite before execution
   * @return the TestNG execution status code (0 for success, non-zero for failure)
   * 
   * @example
   * <pre>{@code
   * CSet<String> classNames = CSet.of(
   *     "com.example.tests.LoginTest",
   *     "com.example.tests.UserManagementTest",
   *     "com.example.tests.PaymentTest"
   * );
   * 
   * // Group tests by functionality
   * Function<CSet<String>, Map<String, CSet<String>>> grouper = classes -> {
   *     Map<String, CSet<String>> groups = new HashMap<>();
   *     classes.forEach(cls -> {
   *         if (cls.contains("Login")) {
   *             groups.computeIfAbsent("Authentication", k -> new CSet<>()).add(cls);
   *         } else if (cls.contains("Payment")) {
   *             groups.computeIfAbsent("Financial", k -> new CSet<>()).add(cls);
   *         } else {
   *             groups.computeIfAbsent("Core", k -> new CSet<>()).add(cls);
   *         }
   *     });
   *     return groups;
   * };
   * 
   * int result = CTestNGProcessor.processTestByClassNames(
   *     classNames,
   *     "output/class-based-suite.xml",
   *     grouper,
   *     suite -> suite.setVerbose(1)
   * );
   * }</pre>
   */
  public static int processTestByClassNames(CSet<String> classNames, String filename, Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, Consumer<XmlSuite> xmlSuiteAdjuster) {
    return processTestClasses(classNames, filename, testClassGroupMapper, xmlSuiteAdjuster);
  }

  /**
   * Processes and executes a collection of test classes by building and running a TestNG suite.
   * 
   * <p>This is a core method that builds a TestNG XML suite from test classes and immediately
   * executes it. It combines the suite building and execution steps into a single operation.
   * 
   * @param testClasses a set of fully qualified test class names
   * @param filename the path where the generated XML suite file will be created
   * @param testClassGroupMapper an optional function to group test classes; if null, all classes go to "Test" group
   * @param xmlSuiteAdjuster an optional consumer to customize the XmlSuite before execution
   * @return the TestNG execution status code (0 for success, non-zero for failure)
   * 
   * @example
   * <pre>{@code
   * CSet<String> testClasses = CSet.of(
   *     "com.myapp.SmokeTest",
   *     "com.myapp.RegressionTest"
   * );
   * 
   * int status = CTestNGProcessor.processTestClasses(
   *     testClasses,
   *     "temp/dynamic-suite.xml",
   *     null, // No custom grouping
   *     suite -> {
   *         suite.setParallel(XmlSuite.ParallelMode.METHODS);
   *         suite.setDataProviderThreadCount(3);
   *         suite.setTimeOut("300000"); // 5 minutes timeout
   *     }
   * );
   * 
   * if (status == 0) {
   *     System.out.println("All tests passed successfully!");
   * } else {
   *     System.out.println("Test execution failed with status: " + status);
   * }
   * }</pre>
   */
  public static int processTestClasses(CSet<String> testClasses, String filename, Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, Consumer<XmlSuite> xmlSuiteAdjuster) {
    return processFile(buildTestSuiteForClasses(testClasses, filename, testClassGroupMapper, xmlSuiteAdjuster));
  }

  /**
   * Executes a collection of pre-configured TestNG XML suites.
   * 
   * <p>This method takes already constructed XmlSuite objects and executes them using TestNG.
   * It applies any configured listeners from CTestNGConfigs and saves the suite configuration
   * to an output file for debugging purposes.
   * 
   * @param xmlSuites a collection of XmlSuite objects to execute
   * @return the TestNG execution status code (0 for success, non-zero for failure)
   * 
   * @example
   * <pre>{@code
   * // Create and configure XML suites programmatically
   * XmlSuite suite1 = new XmlSuite();
   * suite1.setName("SmokeTests");
   * suite1.setParallel(XmlSuite.ParallelMode.CLASSES);
   * 
   * XmlTest test1 = new XmlTest(suite1);
   * test1.setName("LoginTests");
   * test1.setXmlClasses(Arrays.asList(
   *     new XmlClass("com.example.LoginTest")
   * ));
   * 
   * XmlSuite suite2 = new XmlSuite();
   * suite2.setName("RegressionTests");
   * suite2.setParallel(XmlSuite.ParallelMode.METHODS);
   * 
   * XmlTest test2 = new XmlTest(suite2);
   * test2.setName("FullRegression");
   * test2.setXmlClasses(Arrays.asList(
   *     new XmlClass("com.example.RegressionTest")
   * ));
   * 
   * Collection<XmlSuite> suites = Arrays.asList(suite1, suite2);
   * int result = CTestNGProcessor.processXmlSuites(suites);
   * 
   * System.out.println("Multi-suite execution completed with status: " + result);
   * }</pre>
   */
  public static int processXmlSuites(Collection<XmlSuite> xmlSuites) {
    try {
      new CList<>(xmlSuites).forEach(x -> print("Processing Xml Suites \n" + x.toXml()));
      TestNG testNG = new TestNG();
      testNG.setXmlSuites((List<XmlSuite>) (xmlSuites));

      for (ITestNGListener listener : CTestNGConfigs.getListeners()) {
        testNG.addListener(listener);
      }

      CFile.fromOutput(CDate.now().toTimeStampForFileName() + ".xml").write(xmlSuites.toString());
      testNG.run();
      return testNG.getStatus();
    } catch (Throwable t) {
      log.error("Could Not Processing Xml Suites", t);
      return 1;
    }
  }

  private static void print(String x) {
    log.info(x);
    if (!log.isInfoEnabled()) {
      System.out.print(x);
    }
  }

  private static int processFile(CFile xmlFile) {
    print("Processing " + xmlFile);
    try {
      return processXmlSuites(new Parser(xmlFile.getCanonicalPath()).parse());
    } catch (Throwable t) {
      return 1;
    }
  }
}

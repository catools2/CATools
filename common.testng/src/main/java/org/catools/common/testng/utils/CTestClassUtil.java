package org.catools.common.testng.utils;

import com.google.common.reflect.ClassPath;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.annotations.CTestIds;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.testng.CTestNGConfigs;
import org.testng.IMethodInstance;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.internal.annotations.DisabledRetryAnalyzer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for TestNG test class analysis and manipulation.
 * 
 * <p>This utility class provides various methods to work with TestNG test classes,
 * including extracting issue keys, mapping test classes to issue IDs, analyzing test methods,
 * and handling retry logic for test execution.</p>
 * 
 * <p>The class supports filtering tests based on annotations and provides utilities
 * for test name generation and retry analysis.</p>
 * 
 * @see org.testng.IMethodInstance
 * @see org.testng.ITestResult
 * @see org.catools.common.annotations.CTestIds
 */
@Slf4j
@UtilityClass
public class CTestClassUtil {
  private static final CList<TestClassInfo> keyClasses = new CList<>();

  /**
   * Extracts issue keys from a list of TestNG method instances.
   * 
   * <p>This method analyzes the provided method instances and extracts issue keys
   * from test classes that have been annotated with {@link CTestIds}. It can optionally
   * filter out tests that would be skipped during execution.</p>
   * 
   * @param list                                the list of TestNG method instances to analyze
   * @param filterTestsWhichWillSkipInRun      {@code true} to exclude tests that will be skipped,
   *                                           {@code false} to include all tests
   * @return a {@link CList} of issue keys found in the test methods
   * 
   * @example
   * <pre>{@code
   * List<IMethodInstance> methods = Arrays.asList(methodInstance1, methodInstance2);
   * CList<String> issueKeys = CTestClassUtil.getIssueKeys(methods, true);
   * // Returns: ["JIRA-123", "JIRA-456"] 
   * }</pre>
   */
  public static CList<String> getIssueKeys(List<IMethodInstance> list, boolean filterTestsWhichWillSkipInRun) {
    CList<String> issueKeys = new CList<>();
    CList<TestClassInfo> classNameMap = CTestClassUtil.getClassNameMap(filterTestsWhichWillSkipInRun);
    list = CTestSuiteUtil.filterMethodInstanceToExecute(list);

    for (IMethodInstance method : list) {
      for (TestClassInfo testClassInfo : classNameMap) {
        if (testClassInfo.getClassName().equals(method.getMethod().getTestClass().getName())) {
          issueKeys.add(StringUtils.strip(testClassInfo.getTestId()));
        }
      }
    }
    return issueKeys;
  }

  /**
   * Retrieves class names associated with the specified issue IDs.
   * 
   * <p>This method takes a set of issue IDs and returns the corresponding test class names
   * that are annotated with those issue IDs using {@link CTestIds} annotation.</p>
   * 
   * @param issueIds                           the set of issue IDs to search for
   * @param filterTestsWhichWillSkipInRun     {@code true} to exclude tests that will be skipped,
   *                                          {@code false} to include all tests
   * @return a {@link CSet} of class names that match the provided issue IDs
   * 
   * @example
   * <pre>{@code
   * CSet<String> issueIds = CSet.of("JIRA-123", "JIRA-456");
   * CSet<String> classNames = CTestClassUtil.getClassNameForIssueKeys(issueIds, true);
   * // Returns: {"com.example.TestClass1", "com.example.TestClass2"}
   * }</pre>
   */
  public static CSet<String> getClassNameForIssueKeys(CSet<String> issueIds, boolean filterTestsWhichWillSkipInRun) {
    return getClassNameMap(filterTestsWhichWillSkipInRun)
        .getAll(k -> issueIds.contains(StringUtils.strip(k.getTestId())))
        .mapToSet(TestClassInfo::getClassName);
  }

  /**
   * Builds and returns a mapping of test classes to their associated information.
   * 
   * <p>This method scans the classpath for test classes within the configured test packages,
   * analyzes their TestNG annotations, and creates a mapping of test class information
   * including issue IDs and skip flags. The result is cached for subsequent calls.</p>
   * 
   * <p>The method uses reflection to examine test methods and their annotations,
   * particularly looking for {@link Test} and {@link CTestIds} annotations.</p>
   * 
   * @param filterTestsWhichWillSkipInRun {@code true} to exclude tests marked to be skipped,
   *                                      {@code false} to include all discovered tests
   * @return a {@link CList} of {@link TestClassInfo} objects containing test class metadata
   * 
   * @throws RuntimeException if there's an IOException during classpath scanning
   * 
   * @example
   * <pre>{@code
   * // Get all test classes including those that might be skipped
   * CList<TestClassInfo> allTests = CTestClassUtil.getClassNameMap(false);
   * 
   * // Get only test classes that will actually run (not skipped)
   * CList<TestClassInfo> runnableTests = CTestClassUtil.getClassNameMap(true);
   * 
   * for (TestClassInfo info : runnableTests) {
   *     System.out.println("Class: " + info.getClassName() + 
   *                       ", Test ID: " + info.getTestId() + 
   *                       ", Will Skip: " + info.isShouldSkipByAnnotation());
   * }
   * }</pre>
   */
  public static CList<TestClassInfo> getClassNameMap(boolean filterTestsWhichWillSkipInRun) {
    if (keyClasses.isEmpty()) {
      final ClassLoader loader = Thread.currentThread().getContextClassLoader();
      try {
        ClassPath classpath = ClassPath.from(loader);
        for (String testPackage : CTestNGConfigs.getTestPackages()) {
          for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive(testPackage)) {
            new CList<>(classInfo.load().getMethods()).forEach(m -> {
              CList<Annotation> annotations = CList.of(m.getAnnotations());
              if (annotations.has(a -> a instanceof Test)) {
                boolean shouldSkipByAnnotation = CTestSuiteUtil.shouldSkipByAnnotation(annotations);
                Annotation testIds = annotations.getFirstOrNull(a -> a instanceof CTestIds);
                if (testIds != null) {
                  Arrays.asList(((CTestIds) testIds).ids()).forEach(s -> {
                    s = s.trim();
                    keyClasses.add(new TestClassInfo(shouldSkipByAnnotation, s, classInfo.getName()));
                  });
                } else {
                  keyClasses.add(new TestClassInfo(shouldSkipByAnnotation, null, classInfo.getName()));
                }
              }
            });
          }
        }
      } catch (IOException e) {
        log.error("Fail to build class name map", e);
      }
      log.info(keyClasses.size() + " tests class found.");
    }
    return filterTestsWhichWillSkipInRun ? keyClasses.getAll(t -> !t.isShouldSkipByAnnotation()) : keyClasses;
  }

  /**
   * Generates a normalized test name from a test class.
   * 
   * <p>This method converts the fully qualified class name to a test name by replacing
   * all non-word characters (anything that is not a letter, digit, or underscore)
   * with underscores. This is useful for creating file names or identifiers that
   * are safe to use in various contexts.</p>
   * 
   * @param testClazz the test class to generate a name for
   * @return a normalized string representation of the class name with non-word characters replaced by underscores
   * 
   * @example
   * <pre>{@code
   * // Example with a typical test class
   * Class<?> testClass = com.example.test.UserServiceTest.class;
   * String testName = CTestClassUtil.getTestName(testClass);
   * // Returns: "com_example_test_UserServiceTest"
   * 
   * // Example with special characters in package name
   * Class<?> complexClass = com.example.test$inner.TestClass.class;
   * String complexName = CTestClassUtil.getTestName(complexClass);
   * // Returns: "com_example_test_inner_TestClass"
   * }</pre>
   */
  public static String getTestName(Class testClazz) {
    return testClazz.getName().replaceAll("\\W", "_");
  }

  /**
   * Determines if a test has no more retry attempts left.
   * 
   * <p>This method analyzes the test result to determine if the test can be retried again.
   * It handles different types of retry analyzers and provides appropriate logic for
   * determining retry status. The method specifically works with {@link CRetryAnalyzer}
   * but also handles other retry analyzer types.</p>
   * 
   * <p>The method returns {@code true} in the following cases:</p>
   * <ul>
   *   <li>The test method is null</li>
   *   <li>No retry analyzer is configured</li>
   *   <li>The retry analyzer is disabled ({@link DisabledRetryAnalyzer})</li>
   *   <li>The retry analyzer indicates this is the last retry attempt</li>
   * </ul>
   * 
   * @param result the TestNG test result to analyze
   * @return {@code true} if no more retry attempts are available, {@code false} if the test can be retried
   * 
   * @example
   * <pre>{@code
   * // In a TestNG listener
   * @Override
   * public void onTestFailure(ITestResult result) {
   *     if (CTestClassUtil.noRetryLeft(result)) {
   *         System.out.println("Test " + result.getMethod().getMethodName() + " has failed permanently");
   *         // Perform final failure actions (logging, cleanup, etc.)
   *     } else {
   *         System.out.println("Test " + result.getMethod().getMethodName() + " will be retried");
   *         // Prepare for retry (reset state, etc.)
   *     }
   * }
   * 
   * // Example with manual checking
   * ITestResult testResult = getTestResult(); // obtained from TestNG
   * if (CTestClassUtil.noRetryLeft(testResult)) {
   *     handleFinalFailure(testResult);
   * }
   * }</pre>
   */
  public static boolean noRetryLeft(ITestResult result) {
    if (result.getMethod() == null ||
        result.getMethod().getRetryAnalyzer(result) == null ||
        result.getMethod().getRetryAnalyzer(result) instanceof DisabledRetryAnalyzer) {
      return true;
    }

    if (result.getMethod().getRetryAnalyzer(result) instanceof CRetryAnalyzer) {
      return ((CRetryAnalyzer) result.getMethod().getRetryAnalyzer(result)).isLastRetry();
    }

    log.warn(
        "You should use CRetryAnalyzer for retry analyzer annotation. method {}",
        result.getMethod().getMethodName());
    return false;
  }

  /**
   * Data class containing information about a test class.
   * 
   * <p>This class encapsulates metadata about test classes discovered during classpath scanning,
   * including whether the test should be skipped based on annotations, the associated test ID
   * from {@link CTestIds} annotation, and the fully qualified class name.</p>
   * 
   * <p>Instances of this class are immutable and are created during the test discovery process.</p>
   * 
   * @example
   * <pre>{@code
   * // Creating a TestClassInfo instance (typically done internally)
   * TestClassInfo info = new TestClassInfo(false, "JIRA-123", "com.example.TestClass");
   * 
   * // Accessing the information
   * String className = info.getClassName();        // "com.example.TestClass"
   * String testId = info.getTestId();             // "JIRA-123"
   * boolean shouldSkip = info.isShouldSkipByAnnotation(); // false
   * 
   * // Using in filtering operations
   * CList<TestClassInfo> allTests = CTestClassUtil.getClassNameMap(false);
   * CList<TestClassInfo> nonSkippedTests = allTests.getAll(info -> !info.isShouldSkipByAnnotation());
   * }</pre>
   */
  @Data
  public static class TestClassInfo {
    private final boolean shouldSkipByAnnotation;
    private final String testId;
    private final String className;
  }
}

package org.catools.common.testng.listeners;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.testng.utils.CTestSuiteUtil;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.List;

/**
 * A TestNG method interceptor implementation that filters test methods before execution
 * in a Continuous Integration (CI) environment.
 * 
 * <p>This interceptor is responsible for analyzing and filtering the list of test methods
 * that TestNG intends to execute, allowing for selective test execution based on various
 * criteria defined in {@link CTestSuiteUtil#filterMethodInstanceToExecute(List)}.
 * 
 * <p><strong>Usage Examples:</strong>
 * 
 * <p>1. Register the interceptor in TestNG XML configuration:
 * <pre>{@code
 * <suite name="TestSuite">
 *   <listeners>
 *     <listener class-name="org.catools.common.testng.listeners.CIMethodInterceptor"/>
 *   </listeners>
 *   <!-- test definitions -->
 * </suite>
 * }</pre>
 * 
 * <p>2. Register programmatically in a test class:
 * <pre>{@code
 * @Listeners(CIMethodInterceptor.class)
 * public class MyTestClass {
 *     // test methods
 * }
 * }</pre>
 * 
 * <p>3. Register using TestNG API:
 * <pre>{@code
 * TestNG testng = new TestNG();
 * testng.addListener(new CIMethodInterceptor());
 * // configure and run tests
 * }</pre>
 * 
 * <p><strong>Benefits:</strong>
 * <ul>
 *   <li>Reduces test execution time by filtering out unnecessary tests</li>
 *   <li>Provides detailed logging of filtering operations</li>
 *   <li>Integrates seamlessly with existing TestNG test suites</li>
 *   <li>Supports CI/CD pipeline optimization</li>
 * </ul>
 * 
 * @see IMethodInterceptor
 * @see CTestSuiteUtil#filterMethodInstanceToExecute(List)
 * @since 1.0
 */
@Slf4j
public class CIMethodInterceptor implements IMethodInterceptor {

  /**
   * Intercepts and filters the list of test methods before TestNG executes them.
   * 
   * <p>This method is automatically called by TestNG during the test discovery phase,
   * allowing the interceptor to modify the list of methods that will be executed.
   * The filtering logic is delegated to {@link CTestSuiteUtil#filterMethodInstanceToExecute(List)}.
   * 
   * <p><strong>Processing Flow:</strong>
   * <ol>
   *   <li>Receives the original list of test methods from TestNG</li>
   *   <li>Checks if the list is not empty</li>
   *   <li>Records the original size for logging purposes</li>
   *   <li>Applies filtering logic via CTestSuiteUtil</li>
   *   <li>Logs the filtering results (methods before/after filtering)</li>
   *   <li>Returns the filtered list to TestNG for execution</li>
   * </ol>
   * 
   * <p><strong>Example Scenario:</strong>
   * <pre>{@code
   * // Original list contains 100 test methods
   * List<IMethodInstance> originalMethods = [...]; // 100 methods
   * 
   * // After interception and filtering
   * List<IMethodInstance> filteredMethods = interceptor.intercept(originalMethods, context);
   * // filteredMethods might contain 75 methods
   * 
   * // Log output would show:
   * // "75 out of 100 tests will be executed after applying filter."
   * }</pre>
   * 
   * <p><strong>Use Cases:</strong>
   * <ul>
   *   <li>Skip tests based on environment variables or system properties</li>
   *   <li>Filter tests by priority, groups, or custom annotations</li>
   *   <li>Implement test selection strategies for different CI environments</li>
   *   <li>Exclude flaky or environment-specific tests</li>
   * </ul>
   * 
   * @param list the original list of test method instances that TestNG intends to execute.
   *             This list contains all discovered test methods from the test classes.
   *             Must not be null, but can be empty.
   * @param iTestContext the TestNG test context containing information about the current
   *                     test run, including suite configuration, parameters, and attributes.
   *                     Provides access to test execution context and metadata.
   * @return a filtered list of test method instances that should actually be executed.
   *         The returned list will be a subset of or equal to the input list.
   *         Never returns null - returns empty list if no methods should be executed.
   * 
   * @see CTestSuiteUtil#filterMethodInstanceToExecute(List)
   * @see IMethodInstance
   * @see ITestContext
   * @since 1.0
   */
  @Override
  public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {
    if (!list.isEmpty()) {
      int sizeBeforeFilter = list.size();
      log.debug("Cleanup test method inventory.");
      list = CTestSuiteUtil.filterMethodInstanceToExecute(list);
      log.info(
          "{} out of {} tests will be executed after applying filter.",
          list.size(),
          sizeBeforeFilter);
    }
    return list;
  }
}

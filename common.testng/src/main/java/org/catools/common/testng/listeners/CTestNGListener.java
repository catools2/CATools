package org.catools.common.testng.listeners;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.Getter;
import org.catools.common.collections.CList;
import org.catools.common.testng.utils.CTestSuiteUtil;
import org.catools.common.tests.exception.CSkipAwaitingTestException;
import org.testng.IClassListener;
import org.testng.IConfigurationListener;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

/**
 * A composite TestNG listener that aggregates multiple TestNG listeners and provides centralized
 * listener management. This class implements multiple TestNG listener interfaces and delegates
 * method calls to registered listeners based on their type.
 *
 * <p>The CTestNGListener acts as a proxy that:
 *
 * <ul>
 *   <li>Manages a collection of registered TestNG listeners
 *   <li>Delegates method calls to appropriate listeners based on their implemented interfaces
 *   <li>Provides priority-based execution for CITestNGListener implementations
 *   <li>Supports test method interception and custom reporting
 * </ul>
 *
 * <p><strong>Usage Example:</strong>
 *
 * <pre>{@code
 * // Register listeners
 * CTestNGListener.addListeners(
 *     new MyCustomTestListener(),
 *     new MySuiteListener(),
 *     new MyReporter()
 * );
 *
 * // Configure TestNG to use this listener
 * TestNG testng = new TestNG();
 * testng.addListener(new CTestNGListener());
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0.0
 */
public final class CTestNGListener implements CITestNGListener, IMethodInterceptor, IReporter {
  /**
   * Thread-safe collection of registered TestNG listeners. This collection maintains all listeners
   * that have been added to this composite listener.
   *
   * @return immutable list of registered listeners
   */
  @Getter private static final CList<ITestNGListener> listeners = new CList<>();

  /**
   * Creates a new instance of CTestNGListener. The listener starts with an empty collection of
   * registered listeners.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * CTestNGListener listener = new CTestNGListener();
   * // Add this listener to TestNG configuration
   * testng.addListener(listener);
   * }</pre>
   */
  public CTestNGListener() {}

  /**
   * Adds one or more TestNG listeners to the collection of registered listeners. Duplicate
   * listeners are automatically filtered out to prevent double registration.
   *
   * <p>This method is thread-safe and can be called from multiple threads concurrently. Only unique
   * listeners (based on object identity) will be added to the collection.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * // Add a single listener
   * CTestNGListener.addListeners(new MyTestListener());
   *
   * // Add multiple listeners at once
   * CTestNGListener.addListeners(
   *     new MyTestListener(),
   *     new MySuiteListener(),
   *     new MyMethodInterceptor()
   * );
   *
   * // Adding the same listener twice won't create duplicates
   * MyTestListener listener = new MyTestListener();
   * CTestNGListener.addListeners(listener);
   * CTestNGListener.addListeners(listener); // This won't add a duplicate
   * }</pre>
   *
   * @param listeners varargs array of TestNG listeners to add
   * @throws NullPointerException if the listeners array or any listener is null
   */
  public static void addListeners(ITestNGListener... listeners) {
    getListeners().addAll(new CList<>(listeners).getAll(l -> getListeners().notContains(l)));
  }

  /**
   * Returns the priority of this listener for execution ordering. CITestNGListener implementations
   * with lower priority values are executed first.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * CTestNGListener listener = new CTestNGListener();
   * int priority = listener.priority(); // Returns 0
   * }</pre>
   *
   * @return priority value (0 = default priority)
   */
  @Override
  public int priority() {
    return 0;
  }

  /**
   * Called when TestNG execution starts. This is the first method called in the TestNG lifecycle.
   * Delegates to all registered listeners that implement IExecutionListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * // This method is automatically called by TestNG
   * // You can register a custom IExecutionListener to receive this event:
   *
   * public class MyExecutionListener implements IExecutionListener {
   *     @Override
   *     public void onExecutionStart() {
   *         System.out.println("Test execution starting...");
   *         // Initialize global resources, setup logging, etc.
   *     }
   * }
   *
   * CTestNGListener.addListeners(new MyExecutionListener());
   * }</pre>
   *
   * @see IExecutionListener#onExecutionStart()
   */
  @Override // 1- IExecutionListener
  public void onExecutionStart() {
    doIf(l -> l instanceof IExecutionListener, l -> ((IExecutionListener) l).onExecutionStart());
  }

  /**
   * Called when a test suite starts. Delegates to all registered listeners that implement
   * ISuiteListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MySuiteListener implements ISuiteListener {
   *     @Override
   *     public void onStart(ISuite suite) {
   *         System.out.println("Starting suite: " + suite.getName());
   *         // Initialize suite-level resources
   *     }
   * }
   *
   * CTestNGListener.addListeners(new MySuiteListener());
   * }</pre>
   *
   * @param suite the test suite that is starting
   * @see ISuiteListener#onStart(ISuite)
   */
  @Override // 2- ISuiteListener
  public void onStart(ISuite suite) {
    doIf(l -> l instanceof ISuiteListener, l -> ((ISuiteListener) l).onStart(suite));
  }

  /**
   * Called before a configuration method is invoked. Delegates to all registered listeners that
   * implement IConfigurationListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyConfigListener implements IConfigurationListener {
   *     @Override
   *     public void beforeConfiguration(ITestResult result) {
   *         System.out.println("Before config: " + result.getMethod().getMethodName());
   *         // Setup before @BeforeMethod, @BeforeClass, etc.
   *     }
   * }
   *
   * CTestNGListener.addListeners(new MyConfigListener());
   * }</pre>
   *
   * @param result the test result for the configuration method
   * @see IConfigurationListener#beforeConfiguration(ITestResult)
   */
  @Override // 3- IConfigurationListener2
  public void beforeConfiguration(ITestResult result) {
    doIf(
        l -> l instanceof IConfigurationListener,
        l -> ((IConfigurationListener) l).beforeConfiguration(result));
  }

  /**
   * Called when a configuration method succeeds. Delegates to all registered listeners that
   * implement IConfigurationListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyConfigListener implements IConfigurationListener {
   *     @Override
   *     public void onConfigurationSuccess(ITestResult result) {
   *         System.out.println("Config succeeded: " + result.getMethod().getMethodName());
   *         // Log successful setup/teardown
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the successful configuration method
   * @see IConfigurationListener#onConfigurationSuccess(ITestResult)
   */
  @Override // 4- IConfigurationListener
  public void onConfigurationSuccess(ITestResult result) {
    doIf(
        l -> l instanceof IConfigurationListener,
        l -> ((IConfigurationListener) l).onConfigurationSuccess(result));
  }

  /**
   * Called when a configuration method fails. Delegates to all registered listeners that implement
   * IConfigurationListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyConfigListener implements IConfigurationListener {
   *     @Override
   *     public void onConfigurationFailure(ITestResult result) {
   *         System.err.println("Config failed: " + result.getMethod().getMethodName());
   *         System.err.println("Error: " + result.getThrowable().getMessage());
   *         // Handle configuration failures
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the failed configuration method
   * @see IConfigurationListener#onConfigurationFailure(ITestResult)
   */
  @Override // 4- IConfigurationListener
  public void onConfigurationFailure(ITestResult result) {
    doIf(
        l -> l instanceof IConfigurationListener,
        l -> ((IConfigurationListener) l).onConfigurationFailure(result));
  }

  /**
   * Called when a configuration method is skipped. Delegates to all registered listeners that
   * implement IConfigurationListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyConfigListener implements IConfigurationListener {
   *     @Override
   *     public void onConfigurationSkip(ITestResult result) {
   *         System.out.println("Config skipped: " + result.getMethod().getMethodName());
   *         // Handle skipped configuration methods
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the skipped configuration method
   * @see IConfigurationListener#onConfigurationSkip(ITestResult)
   */
  @Override // 4- IConfigurationListener
  public void onConfigurationSkip(ITestResult result) {
    doIf(
        l -> l instanceof IConfigurationListener,
        l -> ((IConfigurationListener) l).onConfigurationSkip(result));
  }

  /**
   * Called when a test context starts (before any test methods in the context are run). Delegates
   * to all registered listeners that implement ITestListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyTestListener implements ITestListener {
   *     @Override
   *     public void onStart(ITestContext context) {
   *         System.out.println("Starting test context: " + context.getName());
   *         System.out.println("Test methods to run: " + context.getAllTestMethods().length);
   *         // Initialize test context resources
   *     }
   * }
   * }</pre>
   *
   * @param context the test context that is starting
   * @see ITestListener#onStart(ITestContext)
   */
  @Override // 5- ITestListener
  public void onStart(ITestContext context) {
    doIf(l -> l instanceof ITestListener, l -> ((ITestListener) l).onStart(context));
  }

  /**
   * Called before the first test method in a test class is run. Delegates to all registered
   * listeners that implement IClassListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyClassListener implements IClassListener {
   *     @Override
   *     public void onBeforeClass(ITestClass testClass) {
   *         System.out.println("Before class: " + testClass.getName());
   *         // Setup class-level resources
   *     }
   * }
   * }</pre>
   *
   * @param testClass the test class that is about to start
   * @see IClassListener#onBeforeClass(ITestClass)
   */
  @Override // 6- IClassListener
  public void onBeforeClass(ITestClass testClass) {
    doIf(l -> l instanceof IClassListener, l -> ((IClassListener) l).onBeforeClass(testClass));
  }

  /**
   * Called when a test method starts. Delegates to all registered listeners that implement
   * ITestListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyTestListener implements ITestListener {
   *     @Override
   *     public void onTestStart(ITestResult result) {
   *         System.out.println("Starting test: " + result.getMethod().getMethodName());
   *         // Start timing, initialize test data, etc.
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the starting test method
   * @see ITestListener#onTestStart(ITestResult)
   */
  @Override // 7- ITestListener
  public void onTestStart(ITestResult result) {
    doIf(l -> l instanceof ITestListener, l -> ((ITestListener) l).onTestStart(result));
  }

  /**
   * Called before any method (test, configuration, or other) is invoked. Delegates to all
   * registered listeners that implement IInvokedMethodListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyMethodListener implements IInvokedMethodListener {
   *     @Override
   *     public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
   *         System.out.println("Before invoking: " + method.getTestMethod().getMethodName());
   *         // Method-level setup for all invoked methods
   *     }
   * }
   * }</pre>
   *
   * @param method the method about to be invoked
   * @param testResult the test result associated with the method
   * @see IInvokedMethodListener#beforeInvocation(IInvokedMethod, ITestResult)
   */
  // 8- IInvokedMethodListener
  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    doIf(
        l -> l instanceof IInvokedMethodListener,
        l -> ((IInvokedMethodListener) l).beforeInvocation(method, testResult));
  }

  /**
   * Called before any method (test, configuration, or other) is invoked with context information.
   * This method also performs automatic test skipping based on annotation rules using
   * CTestSuiteUtil.
   *
   * <p>This method has special handling for test methods:
   *
   * <ul>
   *   <li>Checks if the test should be skipped using CTestSuiteUtil.shouldSkipForThisRun()
   *   <li>Throws CSkipAwaitingTestException if the test should be skipped
   *   <li>Delegates to registered IInvokedMethodListener implementations
   * </ul>
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyMethodListener implements IInvokedMethodListener {
   *     @Override
   *     public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
   *         if (method.isTestMethod()) {
   *             System.out.println("Test context: " + context.getName());
   *             System.out.println("Test method: " + method.getTestMethod().getMethodName());
   *         }
   *     }
   * }
   * }</pre>
   *
   * @param method the method about to be invoked
   * @param testResult the test result associated with the method
   * @param context the test context
   * @throws CSkipAwaitingTestException if the test method should be skipped based on annotation
   *     rules
   * @see IInvokedMethodListener#beforeInvocation(IInvokedMethod, ITestResult, ITestContext)
   * @see CTestSuiteUtil#shouldSkipForThisRun(ITestNGMethod)
   */
  // 8- IInvokedMethodListener
  @Override
  public void beforeInvocation(
      IInvokedMethod method, ITestResult testResult, ITestContext context) {
    if (method.isTestMethod()) {
      if (CTestSuiteUtil.shouldSkipForThisRun(method.getTestMethod())) {
        throw new CSkipAwaitingTestException("Skipping test by annotation rules!");
      }
    }
    doIf(
        l -> l instanceof IInvokedMethodListener,
        l -> ((IInvokedMethodListener) l).beforeInvocation(method, testResult, context));
  }

  /**
   * Called after any method (test, configuration, or other) has been invoked. Delegates to all
   * registered listeners that implement IInvokedMethodListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyMethodListener implements IInvokedMethodListener {
   *     @Override
   *     public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
   *         System.out.println("After invoking: " + method.getTestMethod().getMethodName());
   *         System.out.println("Status: " + testResult.getStatus());
   *         // Method-level cleanup
   *     }
   * }
   * }</pre>
   *
   * @param method the method that was invoked
   * @param testResult the test result associated with the method
   * @see IInvokedMethodListener#afterInvocation(IInvokedMethod, ITestResult)
   */
  // 9- IInvokedMethodListener
  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    doIf(
        l -> l instanceof IInvokedMethodListener,
        l -> ((IInvokedMethodListener) l).afterInvocation(method, testResult));
  }

  /**
   * Called after any method (test, configuration, or other) has been invoked with context
   * information. Delegates to all registered listeners that implement IInvokedMethodListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyMethodListener implements IInvokedMethodListener {
   *     @Override
   *     public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
   *         if (method.isTestMethod()) {
   *             System.out.println("Test completed in context: " + context.getName());
   *             System.out.println("Duration: " + (testResult.getEndMillis() - testResult.getStartMillis()) + "ms");
   *         }
   *     }
   * }
   * }</pre>
   *
   * @param method the method that was invoked
   * @param testResult the test result associated with the method
   * @param context the test context
   * @see IInvokedMethodListener#afterInvocation(IInvokedMethod, ITestResult, ITestContext)
   */
  // 9- IInvokedMethodListener
  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    doIf(
        l -> l instanceof IInvokedMethodListener,
        l -> ((IInvokedMethodListener) l).afterInvocation(method, testResult, context));
  }

  /**
   * Called when a test method succeeds. Delegates to all registered listeners that implement
   * ITestListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyTestListener implements ITestListener {
   *     @Override
   *     public void onTestSuccess(ITestResult result) {
   *         System.out.println("Test passed: " + result.getMethod().getMethodName());
   *         System.out.println("Duration: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
   *         // Log success, update metrics, etc.
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the successful test method
   * @see ITestListener#onTestSuccess(ITestResult)
   */
  @Override // 10- ITestListener
  public void onTestSuccess(ITestResult result) {
    doIf(l -> l instanceof ITestListener, l -> ((ITestListener) l).onTestSuccess(result));
  }

  /**
   * Called when a test method fails. Delegates to all registered listeners that implement
   * ITestListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyTestListener implements ITestListener {
   *     @Override
   *     public void onTestFailure(ITestResult result) {
   *         System.err.println("Test failed: " + result.getMethod().getMethodName());
   *         System.err.println("Error: " + result.getThrowable().getMessage());
   *         // Take screenshots, log failures, send notifications, etc.
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the failed test method
   * @see ITestListener#onTestFailure(ITestResult)
   */
  @Override // 11- ITestListener
  public void onTestFailure(ITestResult result) {
    doIf(l -> l instanceof ITestListener, l -> ((ITestListener) l).onTestFailure(result));
  }

  /**
   * Called when a test method is skipped. Delegates to all registered listeners that implement
   * ITestListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyTestListener implements ITestListener {
   *     @Override
   *     public void onTestSkipped(ITestResult result) {
   *         System.out.println("Test skipped: " + result.getMethod().getMethodName());
   *         if (result.getThrowable() != null) {
   *             System.out.println("Skip reason: " + result.getThrowable().getMessage());
   *         }
   *         // Log skipped tests, update skip counts, etc.
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the skipped test method
   * @see ITestListener#onTestSkipped(ITestResult)
   */
  @Override // 12- ITestListener
  public void onTestSkipped(ITestResult result) {
    doIf(l -> l instanceof ITestListener, l -> ((ITestListener) l).onTestSkipped(result));
  }

  /**
   * Called when a test method fails but is within the success percentage threshold. Delegates to
   * all registered listeners that implement ITestListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * // Configure a test with success percentage
   * @Test(successPercentage = 80, invocationCount = 10)
   * public void flakyTest() {
   *     // Test that might fail occasionally but should pass 80% of the time
   * }
   *
   * public class MyTestListener implements ITestListener {
   *     @Override
   *     public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
   *         System.out.println("Test failed but within success percentage: " +
   *                          result.getMethod().getMethodName());
   *         // Handle acceptable failures
   *     }
   * }
   * }</pre>
   *
   * @param result the test result for the test method that failed but is within success percentage
   * @see ITestListener#onTestFailedButWithinSuccessPercentage(ITestResult)
   */
  @Override // 13- ITestListener
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    doIf(
        l -> l instanceof ITestListener,
        l -> ((ITestListener) l).onTestFailedButWithinSuccessPercentage(result));
  }

  /**
   * Called after the last test method in a test class is run. Delegates to all registered listeners
   * that implement IClassListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyClassListener implements IClassListener {
   *     @Override
   *     public void onAfterClass(ITestClass testClass) {
   *         System.out.println("After class: " + testClass.getName());
   *         // Cleanup class-level resources
   *     }
   * }
   * }</pre>
   *
   * @param testClass the test class that has finished
   * @see IClassListener#onAfterClass(ITestClass)
   */
  @Override // 14- IClassListener
  public void onAfterClass(ITestClass testClass) {
    doIf(l -> l instanceof IClassListener, l -> ((IClassListener) l).onAfterClass(testClass));
  }

  /**
   * Called when a test context finishes (after all test methods in the context have been run).
   * Delegates to all registered listeners that implement ITestListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyTestListener implements ITestListener {
   *     @Override
   *     public void onFinish(ITestContext context) {
   *         System.out.println("Finished test context: " + context.getName());
   *         System.out.println("Passed tests: " + context.getPassedTests().size());
   *         System.out.println("Failed tests: " + context.getFailedTests().size());
   *         System.out.println("Skipped tests: " + context.getSkippedTests().size());
   *         // Generate summary reports, cleanup context resources
   *     }
   * }
   * }</pre>
   *
   * @param context the test context that has finished
   * @see ITestListener#onFinish(ITestContext)
   */
  @Override // 15- ITestListener
  public void onFinish(ITestContext context) {
    doIf(l -> l instanceof ITestListener, l -> ((ITestListener) l).onFinish(context));
  }

  /**
   * Called when a test suite finishes. Delegates to all registered listeners that implement
   * ISuiteListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MySuiteListener implements ISuiteListener {
   *     @Override
   *     public void onFinish(ISuite suite) {
   *         System.out.println("Finished suite: " + suite.getName());
   *         // Cleanup suite-level resources, generate suite reports
   *     }
   * }
   * }</pre>
   *
   * @param suite the test suite that has finished
   * @see ISuiteListener#onFinish(ISuite)
   */
  @Override // 16- ISuiteListener
  public void onFinish(ISuite suite) {
    doIf(l -> l instanceof ISuiteListener, l -> ((ISuiteListener) l).onFinish(suite));
  }

  /**
   * Called when TestNG execution finishes. This is the last method called in the TestNG lifecycle.
   * Delegates to all registered listeners that implement IExecutionListener.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyExecutionListener implements IExecutionListener {
   *     @Override
   *     public void onExecutionFinish() {
   *         System.out.println("Test execution finished");
   *         // Final cleanup, send summary reports, close resources
   *     }
   * }
   * }</pre>
   *
   * @see IExecutionListener#onExecutionFinish()
   */
  @Override // 17- IExecutionListener
  public void onExecutionFinish() {
    doIf(l -> l instanceof IExecutionListener, l -> ((IExecutionListener) l).onExecutionFinish());
  }

  /**
   * Intercepts test methods before they are executed, allowing for modification of the test
   * execution order or filtering of test methods. If registered listeners contain an
   * IMethodInterceptor, delegates to the first one found; otherwise returns the original list
   * unchanged.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyMethodInterceptor implements IMethodInterceptor {
   *     @Override
   *     public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
   *         // Sort methods by priority or filter based on conditions
   *         return methods.stream()
   *                      .sorted((m1, m2) -> Integer.compare(
   *                          m1.getMethod().getPriority(),
   *                          m2.getMethod().getPriority()))
   *                      .collect(Collectors.toList());
   *     }
   * }
   *
   * CTestNGListener.addListeners(new MyMethodInterceptor());
   * }</pre>
   *
   * @param list the list of method instances to be executed
   * @param iTestContext the test context
   * @return the potentially modified list of method instances
   * @see IMethodInterceptor#intercept(List, ITestContext)
   */
  @Override
  public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {
    CList<ITestNGListener> methodInterceptor =
        listeners.getAll(l -> l instanceof IMethodInterceptor);
    if (!methodInterceptor.isEmpty()) {
      return ((IMethodInterceptor) methodInterceptor.getFirst()).intercept(list, iTestContext);
    }
    return list;
  }

  /**
   * Generates test reports after all tests have completed. Delegates to all registered listeners
   * that implement IReporter.
   *
   * <p><strong>Example Usage:</strong>
   *
   * <pre>{@code
   * public class MyCustomReporter implements IReporter {
   *     @Override
   *     public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
   *         // Generate custom HTML reports
   *         for (ISuite suite : suites) {
   *             System.out.println("Generating report for suite: " + suite.getName());
   *             // Create custom report files in outputDirectory
   *         }
   *     }
   * }
   *
   * CTestNGListener.addListeners(new MyCustomReporter());
   * }</pre>
   *
   * @param xmlSuites the list of XML suites that were executed
   * @param suites the list of test suites that were executed
   * @param outputDirectory the directory where reports should be generated
   * @see IReporter#generateReport(List, List, String)
   */
  @Override
  public void generateReport(
      List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
    doIf(
        l -> l instanceof IReporter,
        l -> ((IReporter) l).generateReport(xmlSuites, suites, outputDirectory));
  }

  private <T extends ITestNGListener> void doIf(
      Predicate<ITestNGListener> predicate, Consumer<T> action) {
    CList<ITestNGListener> list = new CList<>(listeners.getAll(predicate));

    if (list.isEmpty()) {
      return;
    }

    // We are using some listener to do Suite manipulation so we give CITestNGListener higher
    // priority
    CList<ITestNGListener> cListeners = list.getAll(l -> l instanceof CITestNGListener);
    cListeners.sort(Comparator.comparingInt(f -> ((CITestNGListener) f).priority()));
    cListeners.forEach(l -> action.accept((T) l));

    list.getAll(l -> !(l instanceof CITestNGListener)).forEach(l -> action.accept((T) l));
  }
}

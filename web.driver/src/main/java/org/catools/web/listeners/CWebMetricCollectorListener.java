package org.catools.web.listeners;

import org.catools.common.date.CDate;
import org.catools.web.drivers.CDriverEngine;
import org.catools.web.entities.CWebPageInfo;
import org.catools.web.metrics.CWebActionMetric;
import org.catools.web.metrics.CWebMetric;
import org.catools.web.metrics.CWebPageLoadMetric;
import org.catools.web.metrics.CWebPageTransitionInfo;

/**
 * A comprehensive web performance metrics collector that implements {@link CDriverListener}
 * to automatically capture and aggregate performance data during web driver interactions.
 * 
 * <p>This listener provides thread-safe collection of web performance metrics by automatically
 * tracking driver actions, page transitions, and page load operations. It maintains separate
 * metric collections per thread using {@link ThreadLocal} storage, making it suitable for
 * concurrent test execution scenarios.</p>
 * 
 * <p>The listener automatically collects three main categories of performance data:</p>
 * <ul>
 *   <li><strong>Action Metrics</strong> - Performance timing for individual web actions (clicks, form fills, etc.)</li>
 *   <li><strong>Page Load Metrics</strong> - Timing data for page loading operations</li>
 *   <li><strong>Page Transition Metrics</strong> - Information about page navigations and state changes</li>
 * </ul>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>Thread-safe operation using ThreadLocal storage</li>
 *   <li>Automatic memory cleanup via shutdown hook</li>
 *   <li>Seamless integration with existing WebDriver test frameworks</li>
 *   <li>Zero-configuration setup - just register the listener</li>
 *   <li>Comprehensive performance data collection</li>
 * </ul>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Basic Setup and Usage:</h4>
 * <pre>{@code
 * // Create and register the listener
 * CWebMetricCollectorListener metricsListener = new CWebMetricCollectorListener();
 * 
 * // Register with driver provider (specific implementation may vary)
 * driverProvider.addListener(metricsListener);
 * 
 * // Perform your web automation test steps
 * driver.get("https://example.com");
 * driver.$(By.id("login")).click();
 * driver.$(By.name("username")).sendKeys("testuser");
 * 
 * // Retrieve collected metrics
 * CWebMetric collectedMetrics = metricsListener.getWebMetric();
 * 
 * // Analyze the results
 * System.out.println("Actions performed: " + collectedMetrics.getActionPerformances().size());
 * System.out.println("Pages loaded: " + collectedMetrics.getPageLoadMetrics().size());
 * }</pre>
 * 
 * <h4>Integration with JUnit Test Framework:</h4>
 * <pre>{@code
 * public class WebPerformanceTest {
 *     private CWebMetricCollectorListener metricsListener;
 *     private WebDriver driver;
 *     
 *     &#64;BeforeEach
 *     void setUp() {
 *         metricsListener = new CWebMetricCollectorListener();
 *         
 *         // Configure driver with metrics listener
 *         // (Implementation depends on your driver setup)
 *         driver = configureDriverWithListener(metricsListener);
 *     }
 *     
 *     &#64;Test
 *     void testLoginPerformance() {
 *         // Perform test actions
 *         driver.get("https://myapp.com/login");
 *         driver.$(By.id("username")).sendKeys("testuser");
 *         driver.$(By.id("password")).sendKeys("password");
 *         driver.$(By.id("submit")).click();
 *         
 *         // Assert performance metrics
 *         CWebMetric metrics = metricsListener.getWebMetric();
 *         
 *         // Verify page load performance
 *         for (CWebPageLoadMetric pageLoad : metrics.getPageLoadMetrics()) {
 *             long loadTimeMs = pageLoad.getDuration() / 1_000_000;
 *             assertThat(loadTimeMs).isLessThan(3000); // 3 second max
 *         }
 *         
 *         // Verify action performance
 *         for (CWebActionMetric action : metrics.getActionPerformances()) {
 *             long actionTimeMs = action.getDuration() / 1_000_000;
 *             assertThat(actionTimeMs).isLessThan(1000); // 1 second max per action
 *         }
 *     }
 * }
 * }</pre>
 * 
 * <h4>Detailed Performance Analysis:</h4>
 * <pre>{@code
 * CWebMetricCollectorListener listener = new CWebMetricCollectorListener();
 * 
 * // ... register listener and perform test actions ...
 * 
 * CWebMetric metrics = listener.getWebMetric();
 * 
 * // Analyze page load performance
 * System.out.println("=== Page Load Analysis ===");
 * metrics.getPageLoadMetrics().forEach(pageLoad -> {
 *     double loadTimeSeconds = pageLoad.getDuration() / 1_000_000_000.0;
 *     System.out.printf("Page: %s (URL: %s) - Load Time: %.2f seconds%n", 
 *         pageLoad.getTitle(), pageLoad.getUrl(), loadTimeSeconds);
 * });
 * 
 * // Analyze action performance
 * System.out.println("\n=== Action Performance Analysis ===");
 * metrics.getActionPerformances().forEach(action -> {
 *     double actionTimeMs = action.getDuration() / 1_000_000.0;
 *     System.out.printf("Action: %s - Duration: %.2f ms%n", 
 *         action.getName(), actionTimeMs);
 *     
 *     // Flag slow actions
 *     if (actionTimeMs > 500) {
 *         System.out.println("  ⚠️  WARNING: Slow action detected!");
 *     }
 * });
 * 
 * // Analyze page transitions
 * System.out.println("\n=== Page Transition Analysis ===");
 * metrics.getPagePerformances().forEach(transition -> {
 *     System.out.printf("Transition: %s%n", transition.getName());
 *     System.out.printf("  From: %s%n", transition.getUrlBeforeAction());
 *     System.out.printf("  To: %s%n", transition.getUrlAfterAction());
 * });
 * }</pre>
 * 
 * <h4>Multi-threaded Test Environment:</h4>
 * <pre>{@code
 * // The listener is thread-safe and maintains separate metrics per thread
 * CWebMetricCollectorListener sharedListener = new CWebMetricCollectorListener();
 * 
 * // Thread 1
 * CompletableFuture<CWebMetric> thread1Metrics = CompletableFuture.supplyAsync(() -> {
 *     WebDriver driver1 = createDriverWithListener(sharedListener);
 *     // Perform test actions...
 *     driver1.get("https://example.com/page1");
 *     return sharedListener.getWebMetric(); // Returns metrics for this thread only
 * });
 * 
 * // Thread 2  
 * CompletableFuture<CWebMetric> thread2Metrics = CompletableFuture.supplyAsync(() -> {
 *     WebDriver driver2 = createDriverWithListener(sharedListener);
 *     // Perform different test actions...
 *     driver2.get("https://example.com/page2");
 *     return sharedListener.getWebMetric(); // Returns metrics for this thread only
 * });
 * 
 * // Collect results from both threads
 * CWebMetric metrics1 = thread1Metrics.get();
 * CWebMetric metrics2 = thread2Metrics.get();
 * 
 * // Each thread has its own isolated metrics
 * System.out.println("Thread 1 actions: " + metrics1.getActionPerformances().size());
 * System.out.println("Thread 2 actions: " + metrics2.getActionPerformances().size());
 * }</pre>
 * 
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 * @see CDriverListener
 * @see CWebMetric
 * @see CWebPageInfo
 * @see CWebPageTransitionInfo
 * @see ThreadLocal
 */
public class CWebMetricCollectorListener implements CDriverListener {
  private final ThreadLocal<CWebMetric> pageMetricThreadLocal = ThreadLocal.withInitial(CWebMetric::new);

  /**
   * Constructs a new CWebMetricCollectorListener with automatic memory management.
   * 
   * <p>This constructor initializes the thread-local storage for metrics collection
   * and registers a shutdown hook to ensure proper cleanup of thread-local resources
   * when the JVM terminates. This prevents potential memory leaks in long-running
   * applications or test suites.</p>
   * 
   * <p>The shutdown hook automatically calls {@code ThreadLocal.remove()} to clean up
   * any thread-local data that may still be present when the application shuts down,
   * ensuring proper resource management even if metrics are not explicitly cleared.</p>
   * 
   * <h4>Memory Management Features:</h4>
   * <ul>
   *   <li>Automatic ThreadLocal cleanup via shutdown hook</li>
   *   <li>Prevention of memory leaks in long-running applications</li>
   *   <li>Safe for use in application servers and test frameworks</li>
   *   <li>No manual cleanup required in most scenarios</li>
   * </ul>
   * 
   * <h4>Usage Examples:</h4>
   * 
   * <h5>Basic Instantiation:</h5>
   * <pre>{@code
   * // Simple instantiation - cleanup is automatic
   * CWebMetricCollectorListener listener = new CWebMetricCollectorListener();
   * 
   * // Register with your driver setup
   * driverProvider.addListener(listener);
   * 
   * // Use normally - no explicit cleanup needed
   * // Shutdown hook will handle ThreadLocal cleanup
   * }</pre>
   * 
   * <h5>Integration with Dependency Injection:</h5>
   * <pre>{@code
   * &#64;Component
   * &#64;Scope("singleton")
   * public class TestMetricsConfig {
   *     
   *     &#64;Bean
   *     public CWebMetricCollectorListener metricsListener() {
   *         // Spring will manage the lifecycle
   *         return new CWebMetricCollectorListener();
   *     }
   * }
   * 
   * &#64;Service
   * public class WebTestService {
   *     &#64;Autowired
   *     private CWebMetricCollectorListener metricsListener;
   *     
   *     public void runPerformanceTest() {
   *         // Use the injected listener
   *         // Automatic cleanup via shutdown hook
   *     }
   * }
   * }</pre>
   * 
   * <h5>Manual Resource Management (Advanced):</h5>
   * <pre>{@code
   * public class CustomTestFramework {
   *     private CWebMetricCollectorListener listener;
   *     
   *     public void setUp() {
   *         listener = new CWebMetricCollectorListener();
   *         // Shutdown hook is automatically registered
   *     }
   *     
   *     public void tearDown() {
   *         // Optional: Get final metrics before cleanup
   *         CWebMetric finalMetrics = listener.getWebMetric();
   *         saveMetricsToReport(finalMetrics);
   *         
   *         // ThreadLocal will be cleaned up by shutdown hook
   *         // No manual cleanup needed unless explicitly required
   *     }
   * }
   * }</pre>
   * 
   * @since 1.0
   * @see ThreadLocal
   * @see Runtime#addShutdownHook(Thread)
   */
  public CWebMetricCollectorListener() {
    Runtime.getRuntime().addShutdownHook(new Thread(pageMetricThreadLocal::remove));
  }

  /**
   * Retrieves the comprehensive web performance metrics collected for the current thread.
   * 
   * <p>This method provides access to the thread-local {@link CWebMetric} instance that
   * has been automatically populated by the listener during web driver interactions.
   * Each thread maintains its own separate metrics collection, ensuring thread safety
   * in concurrent testing scenarios.</p>
   * 
   * <p>The returned {@link CWebMetric} object contains three categories of performance data:</p>
   * <ul>
   *   <li><strong>Action Performance</strong> - Timing and details for individual web actions</li>
   *   <li><strong>Page Load Metrics</strong> - Performance data for page loading operations</li>
   *   <li><strong>Page Transition Information</strong> - Details about page navigation events</li>
   * </ul>
   * 
   * <p>The metrics are automatically collected as the listener responds to WebDriver
   * events, requiring no manual intervention from the test code. This enables transparent
   * performance monitoring without modifying existing test logic.</p>
   * 
   * <h4>Thread Safety:</h4>
   * <p>This method is completely thread-safe. Each calling thread receives its own
   * isolated metrics collection, preventing data contamination in multi-threaded
   * test execution environments.</p>
   * 
   * <h4>Usage Examples:</h4>
   * 
   * <h5>Basic Metrics Retrieval:</h5>
   * <pre>{@code
   * CWebMetricCollectorListener listener = new CWebMetricCollectorListener();
   * 
   * // Register listener and perform web automation...
   * driver.get("https://example.com");
   * driver.$(By.id("submit")).click();
   * 
   * // Retrieve all collected metrics
   * CWebMetric metrics = listener.getWebMetric();
   * 
   * // Access different metric categories
   * System.out.println("Total actions: " + metrics.getActionPerformances().size());
   * System.out.println("Total page loads: " + metrics.getPageLoadMetrics().size());
   * System.out.println("Total transitions: " + metrics.getPagePerformances().size());
   * }</pre>
   * 
   * <h5>Performance Analysis and Reporting:</h5>
   * <pre>{@code
   * public void generatePerformanceReport() {
   *     CWebMetric metrics = metricsListener.getWebMetric();
   *     
   *     // Calculate average action time
   *     double avgActionTime = metrics.getActionPerformances().stream()
   *         .mapToLong(CWebActionMetric::getDuration)
   *         .average()
   *         .orElse(0.0) / 1_000_000; // Convert to milliseconds
   *     
   *     // Find slowest page load
   *     Optional<CWebPageLoadMetric> slowestLoad = metrics.getPageLoadMetrics().stream()
   *         .max(Comparator.comparingLong(CWebPageLoadMetric::getDuration));
   *     
   *     // Generate summary
   *     System.out.printf("Performance Summary:%n");
   *     System.out.printf("  Average action time: %.2f ms%n", avgActionTime);
   *     slowestLoad.ifPresent(load -> 
   *         System.out.printf("  Slowest page load: %s (%.2f ms)%n", 
   *             load.getTitle(), load.getDuration() / 1_000_000.0));
   * }
   * }</pre>
   * 
   * <h5>Test Assertion Integration:</h5>
   * <pre>{@code
   * &#64;Test
   * void assertPerformanceThresholds() {
   *     // Perform test actions...
   *     
   *     CWebMetric metrics = metricsListener.getWebMetric();
   *     
   *     // Assert all page loads are under 3 seconds
   *     metrics.getPageLoadMetrics().forEach(pageLoad -> {
   *         long loadTimeMs = pageLoad.getDuration() / 1_000_000;
   *         assertThat(loadTimeMs)
   *             .as("Page load time for: " + pageLoad.getTitle())
   *             .isLessThan(3000);
   *     });
   *     
   *     // Assert no action takes longer than 1 second
   *     metrics.getActionPerformances().forEach(action -> {
   *         long actionTimeMs = action.getDuration() / 1_000_000;
   *         assertThat(actionTimeMs)
   *             .as("Action time for: " + action.getName())
   *             .isLessThan(1000);
   *     });
   * }
   * }</pre>
   * 
   * <h5>Concurrent Testing Scenario:</h5>
   * <pre>{@code
   * // Each thread gets its own metrics - no interference
   * public class ConcurrentPerformanceTest {
   *     private static CWebMetricCollectorListener sharedListener = 
   *         new CWebMetricCollectorListener();
   *     
   *     &#64;Test
   *     &#64;Execution(ExecutionMode.CONCURRENT)
   *     void testThread1Performance() {
   *         // Thread 1 operations...
   *         driver1.get("https://example.com/path1");
   *         
   *         CWebMetric thread1Metrics = sharedListener.getWebMetric();
   *         // Contains only Thread 1's metrics
   *     }
   *     
   *     &#64;Test
   *     &#64;Execution(ExecutionMode.CONCURRENT) 
   *     void testThread2Performance() {
   *         // Thread 2 operations...
   *         driver2.get("https://example.com/path2");
   *         
   *         CWebMetric thread2Metrics = sharedListener.getWebMetric();
   *         // Contains only Thread 2's metrics - completely isolated
   *     }
   * }
   * }</pre>
   * 
   * <h5>Continuous Monitoring:</h5>
   * <pre>{@code
   * public void monitorLongRunningTest() {
   *     Timer timer = new Timer();
   *     timer.scheduleAtFixedRate(new TimerTask() {
   *         public void run() {
   *             CWebMetric currentMetrics = metricsListener.getWebMetric();
   *             
   *             // Log current performance statistics
   *             logger.info("Current metrics - Actions: {}, Page loads: {}, Transitions: {}",
   *                 currentMetrics.getActionPerformances().size(),
   *                 currentMetrics.getPageLoadMetrics().size(),
   *                 currentMetrics.getPagePerformances().size());
   *         }
   *     }, 0, 30000); // Every 30 seconds
   * }
   * }</pre>
   * 
   * @return A {@link CWebMetric} instance containing all performance metrics collected
   *         for the current thread. This includes action timings, page load data, and
   *         transition information. Never returns null - will return an empty metrics
   *         object if no data has been collected yet.
   * 
   * @since 1.0
   * @see CWebMetric
   * @see ThreadLocal
   * @see CWebActionMetric
   * @see CWebPageLoadMetric
   * @see CWebPageTransitionInfo
   */
  public CWebMetric getWebMetric() {
    return pageMetricThreadLocal.get();
  }

  /**
   * Automatically captures and records performance metrics after each web driver action.
   * 
   * <p>This callback method is invoked by the driver framework after every web interaction
   * (such as clicks, form fills, navigation, etc.) to automatically collect action-specific
   * performance data. It creates and stores a comprehensive action metric that includes
   * timing information, page state changes, and action metadata.</p>
   * 
   * <p>The method leverages the provided parameters to create a detailed performance record
   * without requiring any manual intervention from test code, enabling transparent and
   * comprehensive performance monitoring across all web driver interactions.</p>
   * 
   * <h4>Automatic Data Collection:</h4>
   * <p>This method automatically records:</p>
   * <ul>
   *   <li>Action execution timing (calculated from start time to current time)</li>
   *   <li>Page state before the action (URL, title, DOM state)</li>
   *   <li>Page state after the action (changes in URL, title, DOM state)</li>
   *   <li>Action name and metadata for later analysis</li>
   *   <li>Thread-specific metric storage for concurrent testing</li>
   * </ul>
   * 
   * <h4>Integration Points:</h4>
   * <p>This method is typically called by:</p>
   * <ul>
   *   <li>WebDriver wrapper implementations</li>
   *   <li>Test framework integration layers</li>
   *   <li>Driver proxy or interceptor mechanisms</li>
   *   <li>Event-driven automation frameworks</li>
   * </ul>
   * 
   * @param actionName A descriptive name identifying the specific action that was performed.
   *                   Examples: "Click Login Button", "Fill Username Field", "Submit Form",
   *                   "Navigate to Dashboard". This name appears in performance reports
   *                   and analysis output for easy identification.
   * 
   * @param engine The CDriverEngine instance that executed the action. Used for
   *                  context and potential additional data extraction if needed.
   * 
   * @param pageBeforeAction Complete page state information captured before the action 
   *                        was executed, including URL, title, DOM elements, and other
   *                        relevant page metadata. Used to establish the starting context.
   * 
   * @param pageAfterAction Complete page state information captured after the action
   *                       was completed, including any changes to URL, title, DOM elements,
   *                       or page content. Used to measure the impact of the action.
   * 
   * @param driverMetricInfo Detailed transition information containing performance metrics
   *                        and metadata about the driver operation. May include network
   *                        timing, rendering data, and other driver-level performance indicators.
   * 
   * @param startTime The precise timestamp when the action was initiated. Used to calculate
   *                 the total action duration with nanosecond precision for accurate
   *                 performance measurement.
   * 
   * @param durationInNano The total duration of the action execution in nanoseconds.
   *                      This parameter provides the exact timing measurement for the
   *                      operation, enabling precise performance analysis.
   * 
   * <h4>Usage Context Examples:</h4>
   * 
   * <h5>Typical Integration in Driver Wrapper:</h5>
   * <pre>{@code
   * public class EnhancedWebDriver extends CDriverEngine {
   *     private List<CDriverListener> listeners = new ArrayList<>();
   *     
   *     &#64;Override
   *     public void click(WebElement element) {
   *         // Capture state before action
   *         CWebPageInfo beforePage = new CWebPageInfo(this);
   *         CDate startTime = CDate.now();
   *         String actionName = "Click Element: " + getElementDescription(element);
   *         
   *         // Execute the actual action
   *         super.click(element);
   *         
   *         // Capture state after action
   *         CWebPageInfo afterPage = new CWebPageInfo(this);
   *         CWebPageTransitionInfo transitionInfo = createTransitionInfo();
   *         long duration = startTime.getDurationToNow().getNano();
   *         
   *         // Notify all listeners (including CWebMetricCollectorListener)
   *         listeners.forEach(listener -> 
   *             listener.afterAction(actionName, this, beforePage, afterPage, 
   *                                transitionInfo, startTime, duration));
   *     }
   * }
   * }</pre>
   * 
   * <h5>Framework Integration Example:</h5>
   * <pre>{@code
   * public class TestFrameworkIntegration {
   *     private CWebMetricCollectorListener metricsCollector;
   *     
   *     public void executeTrackedAction(String actionName, Runnable action) {
   *         // Prepare for action tracking
   *         CWebPageInfo beforeState = new CWebPageInfo(driver);
   *         CDate actionStart = CDate.now();
   *         
   *         try {
   *             // Execute the actual test action
   *             action.run();
   *             
   *             // Capture results and notify listener
   *             CWebPageInfo afterState = new CWebPageInfo(driver);
   *             CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *                 actionName, beforeState, afterState);
   *             long duration = actionStart.getDurationToNow().getNano();
   *             
   *             // This triggers the metrics collection
   *             metricsCollector.afterAction(actionName, (CDriverEngine) driver, 
   *                 beforeState, afterState, transition, actionStart, duration);
   *                 
   *         } catch (Exception e) {
   *             // Handle action failure while still collecting metrics
   *             CWebPageInfo errorState = new CWebPageInfo(driver);
   *             long duration = actionStart.getDurationToNow().getNano();
   *             
   *             metricsCollector.afterAction(actionName + " (FAILED)", 
   *                 (CDriverEngine) driver, beforeState, errorState, 
   *                 null, actionStart, duration);
   *             throw e;
   *         }
   *     }
   * }
   * }</pre>
   * 
   * <h5>Performance Analysis After Collection:</h5>
   * <pre>{@code
   * // After actions are automatically tracked, analyze the collected data
   * CWebMetric collectedMetrics = metricsCollector.getWebMetric();
   * 
   * // Find the slowest actions
   * List<CWebActionMetric> slowActions = collectedMetrics.getActionPerformances()
   *     .stream()
   *     .filter(action -> action.getDuration() > 1_000_000_000L) // > 1 second
   *     .sorted((a, b) -> Long.compare(b.getDuration(), a.getDuration()))
   *     .collect(Collectors.toList());
   * 
   * slowActions.forEach(action -> {
   *     System.out.printf("Slow action detected: %s took %.2f seconds%n",
   *         action.getName(), action.getDuration() / 1_000_000_000.0);
   * });
   * }</pre>
   * 
   * @since 1.0
   * @see CDriverListener#afterAction(String, CDriverEngine, CWebPageInfo, CWebPageInfo, CWebPageTransitionInfo, CDate, long)
   * @see CWebMetric#addActionMetric(String, CWebPageInfo, CWebPageInfo, CDate)
   * @see CWebPageInfo
   * @see CWebPageTransitionInfo
   */
  @Override
  public void afterAction(String actionName, CDriverEngine engine, CWebPageInfo pageBeforeAction, CWebPageInfo pageAfterAction, CWebPageTransitionInfo driverMetricInfo, CDate startTime, long durationInNano) {
    getWebMetric().addActionMetric(actionName, pageBeforeAction, pageAfterAction, startTime);
  }

  /**
   * Automatically captures page transition and loading performance metrics when page changes occur.
   * 
   * <p>This callback method is invoked by the driver framework whenever a page transition
   * is detected, such as navigation events, form submissions that change pages, or dynamic
   * content updates that alter the page state. It automatically records both page loading
   * performance and transition metadata for comprehensive page-level performance analysis.</p>
   * 
   * <p>The method performs two distinct types of metric collection:</p>
   * <ol>
   *   <li><strong>Page Load Metrics</strong> - Timing data for the page loading operation</li>
   *   <li><strong>Page Performance Data</strong> - Detailed transition information and metadata</li>
   * </ol>
   * 
   * <p>This dual approach provides both quantitative performance measurements (timing)
   * and qualitative information (what changed, how the page transitioned) for thorough
   * performance analysis and debugging.</p>
   * 
   * <h4>Automatic Metric Types Collected:</h4>
   * <ul>
   *   <li><strong>Page Load Timing</strong> - Complete loading duration with nanosecond precision</li>
   *   <li><strong>Page Metadata</strong> - Title, URL, and page state information</li>
   *   <li><strong>Transition Context</strong> - Details about what triggered the page change</li>
   *   <li><strong>Performance Indicators</strong> - Network, rendering, and processing metrics</li>
   * </ul>
   * 
   * <h4>Common Page Change Triggers:</h4>
   * <ul>
   *   <li>Direct navigation via driver.get() or driver.navigate()</li>
   *   <li>Form submissions that result in page redirects</li>
   *   <li>Link clicks that navigate to new pages</li>
   *   <li>JavaScript-triggered page changes or SPA route changes</li>
   *   <li>Browser back/forward navigation</li>
   *   <li>Page refreshes or reloads</li>
   * </ul>
   * 
   * @param engine The CDriverEngine instance where the page change occurred.
   *                  Used to extract current page information (title, URL) for
   *                  the page load metric recording.
   * 
   * @param pageTransitionInfo Comprehensive information about the page transition,
   *                          including before/after states, transition type, performance
   *                          metrics, and metadata about what caused the page change.
   *                          This object contains detailed context for analysis.
   * 
   * @param startTime The precise timestamp when the page transition was initiated.
   *                 Used to calculate the total page loading duration with
   *                 nanosecond precision for accurate performance measurement.
   * 
   * @param durationInNano The total duration of the page transition in nanoseconds.
   *                      Represents the complete time from initiation to completion
   *                      of the page change operation.
   * 
   * <h4>Usage Context Examples:</h4>
   * 
   * <h5>Navigation Event Handling:</h5>
   * <pre>{@code
   * public class PageNavigationHandler {
   *     private CWebMetricCollectorListener metricsListener;
   *     
   *     public void performNavigation(String url) {
   *         // Capture initial state
   *         CWebPageInfo beforePage = new CWebPageInfo(driver);
   *         CDate navigationStart = CDate.now();
   *         
   *         // Perform navigation
   *         driver.get(url);
   *         
   *         // Capture final state and create transition info
   *         CWebPageInfo afterPage = new CWebPageInfo(driver);
   *         CWebPageTransitionInfo transitionInfo = new CWebPageTransitionInfo(
   *             "Navigate to " + url, beforePage, afterPage);
   *         
   *         long duration = navigationStart.getDurationToNow().getNano();
   *         
   *         // This triggers the onPageChanged method
   *         metricsListener.onPageChanged((CDriverEngine) driver, 
   *             transitionInfo, navigationStart, duration);
   *     }
   * }
   * }</pre>
   * 
   * <h5>Form Submission with Page Change:</h5>
   * <pre>{@code
   * public void submitFormWithTracking() {
   *     CDate submitStart = CDate.now();
   *     CWebPageInfo beforeSubmit = new CWebPageInfo(driver);
   *     
   *     // Fill and submit form
   *     driver.$(By.name("username")).sendKeys("testuser");
   *     driver.$(By.name("password")).sendKeys("password");
   *     driver.$(By.id("login-form")).submit();
   *     
   *     // Wait for redirect/new page
   *     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
   *     wait.until(ExpectedConditions.urlContains("dashboard"));
   *     
   *     CWebPageInfo afterSubmit = new CWebPageInfo(driver);
   *     long submitDuration = submitStart.getDurationToNow().getNano();
   *     
   *     // Create transition info for login process
   *     CWebPageTransitionInfo loginTransition = new CWebPageTransitionInfo(
   *         "User Login Form Submission", beforeSubmit, afterSubmit);
   *     
   *     // Automatically records both load and transition metrics
   *     metricsListener.onPageChanged((CDriverEngine) driver, 
   *         loginTransition, submitStart, submitDuration);
   * }
   * }</pre>
   * 
   * <h5>SPA Route Change Detection:</h5>
   * <pre>{@code
   * public class SPANavigationTracker {
   *     public void trackRouteChange(String fromRoute, String toRoute) {
   *         CDate routeChangeStart = CDate.now();
   *         
   *         // Trigger SPA route change
   *         ((JavascriptExecutor) driver).executeScript(
   *             "window.history.pushState({}, '', arguments[0]);", toRoute);
   *         
   *         // Wait for route change completion (app-specific)
   *         wait.until(ExpectedConditions.urlContains(toRoute));
   *         
   *         // Create transition info for SPA navigation
   *         CWebPageInfo beforeRoute = new CWebPageInfo();
   *         beforeRoute.setUrl(driver.getCurrentUrl().replace(toRoute, fromRoute));
   *         CWebPageInfo afterRoute = new CWebPageInfo(driver);
   *         
   *         CWebPageTransitionInfo routeTransition = new CWebPageTransitionInfo(
   *             "SPA Route Change: " + fromRoute + " -> " + toRoute, 
   *             beforeRoute, afterRoute);
   *         
   *         long routeDuration = routeChangeStart.getDurationToNow().getNano();
   *         
   *         metricsListener.onPageChanged((CDriverEngine) driver, 
   *             routeTransition, routeChangeStart, routeDuration);
   *     }
   * }
   * }</pre>
   * 
   * <h5>Analysis of Collected Page Metrics:</h5>
   * <pre>{@code
   * // After page changes are automatically tracked
   * public void analyzePagePerformance() {
   *     CWebMetric metrics = metricsListener.getWebMetric();
   *     
   *     // Analyze page load performance
   *     System.out.println("=== Page Load Performance ===");
   *     metrics.getPageLoadMetrics().forEach(loadMetric -> {
   *         double loadTimeSeconds = loadMetric.getDuration() / 1_000_000_000.0;
   *         System.out.printf("Page: %s - Load Time: %.2f seconds%n",
   *             loadMetric.getTitle(), loadTimeSeconds);
   *             
   *         if (loadTimeSeconds > 3.0) {
   *             System.out.println("  ⚠️  WARNING: Slow page load detected!");
   *         }
   *     });
   *     
   *     // Analyze page transitions
   *     System.out.println("\n=== Page Transition Analysis ===");
   *     metrics.getPagePerformances().forEach(transition -> {
   *         System.out.printf("Transition: %s%n", transition.getName());
   *         System.out.printf("  From: %s%n", transition.getUrlBeforeAction());
   *         System.out.printf("  To: %s%n", transition.getUrlAfterAction());
   *         
   *         boolean isNavigation = !transition.getUrlBeforeAction()
   *             .equals(transition.getUrlAfterAction());
   *         System.out.printf("  Type: %s%n", 
   *             isNavigation ? "Navigation" : "State Change");
   *     });
   * }
   * }</pre>
   * 
   * <h5>Performance Thresholds and Alerts:</h5>
   * <pre>{@code
   * &#64;Test
   * void validatePageLoadPerformance() {
   *     // ... perform test actions that trigger page changes ...
   *     
   *     CWebMetric metrics = metricsListener.getWebMetric();
   *     
   *     // Validate page load performance thresholds
   *     metrics.getPageLoadMetrics().forEach(pageLoad -> {
   *         double loadTimeSeconds = pageLoad.getDuration() / 1_000_000_000.0;
   *         
   *         assertThat(loadTimeSeconds)
   *             .as("Page load time for: " + pageLoad.getUrl())
   *             .isLessThan(5.0); // 5 second maximum
   *             
   *         // Log performance warnings
   *         if (loadTimeSeconds > 2.0) {
   *             logger.warn("Slow page load detected: {} took {:.2f} seconds", 
   *                 pageLoad.getTitle(), loadTimeSeconds);
   *         }
   *     });
   * }
   * }</pre>
   * 
   * @since 1.0
   * @see CDriverListener#onPageChanged(CDriverEngine, CWebPageTransitionInfo, CDate, long)
   * @see CWebMetric#addPagePerformance(CWebPageTransitionInfo)
   * @see CWebPageTransitionInfo
   */
  @Override
  public void onPageChanged(CDriverEngine engine, CWebPageTransitionInfo pageTransitionInfo, CDate startTime, long durationInNano) {
    getWebMetric().addPageLoadMetric(engine, startTime);
    getWebMetric().addPagePerformance(pageTransitionInfo);
  }
}

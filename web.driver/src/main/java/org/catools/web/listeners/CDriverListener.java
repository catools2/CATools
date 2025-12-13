package org.catools.web.listeners;

import org.catools.common.date.CDate;
import org.catools.web.drivers.CDriverEngine;
import org.catools.web.drivers.CDriverEngineProvider;
import org.catools.web.entities.CWebPageInfo;
import org.catools.web.metrics.CWebPageTransitionInfo;

/**
 * Driver listener interface to manage event handling throughout the WebDriver lifecycle.
 * This interface provides hooks for monitoring and reacting to driver initialization,
 * actions, and page transitions.
 *
 * <p>Implementations can be used for various purposes such as:</p>
 * <ul>
 *   <li>Logging driver activities</li>
 *   <li>Performance monitoring and metrics collection</li>
 *   <li>Screenshot capture on actions</li>
 *   <li>Custom reporting and analytics</li>
 *   <li>Error handling and recovery</li>
 * </ul>
 *
 * <p><strong>Example implementation:</strong></p>
 * <pre>{@code
 * public class MyDriverListener implements CDriverListener {
 *     private final Logger logger = LoggerFactory.getLogger(MyDriverListener.class);
 *
 *     @Override
 *     public void beforeInit(Capabilities capabilities) {
 *         logger.info("Initializing driver with capabilities: {}", capabilities.getBrowserName());
 *     }
 *
 *     @Override
 *     public void afterAction(String actionName, RemoteWebDriver webDriver,
 *                           CWebPageInfo pageBeforeAction, CWebPageInfo pageAfterAction,
 *                           CWebPageTransitionInfo driverMetricInfo, CDate startTime,
 *                           long durationInNano) {
 *         logger.info("Action '{}' completed in {} ms", actionName, durationInNano / 1_000_000);
 *     }
 * }
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
public interface CDriverListener {
  /**
   * Triggers before WebDriver initialization.
   *
   * <p>This method is called before the WebDriver instance is created, providing
   * an opportunity to perform setup operations, validate capabilities, or log
   * initialization details.</p>
   *
   * <p><strong>Common use cases:</strong></p>
   * <ul>
   *   <li>Logging the browser and platform being used</li>
   *   <li>Validating required capabilities</li>
   *   <li>Setting up test environment prerequisites</li>
   *   <li>Initializing performance monitoring</li>
   * </ul>
   *
   * <p><strong>Example usage:</strong></p>
   * <pre>{@code
   * @Override
   * public void beforeInit(Capabilities capabilities) {
   *     String browserName = capabilities.getBrowserName();
   *     String browserVersion = capabilities.getBrowserVersion();
   *     String platformName = capabilities.getPlatformName().toString();
   *
   *     logger.info("Starting {} {} on {}", browserName, browserVersion, platformName);
   *
   *     // Validate required capabilities
   *     if (browserName == null || browserName.isEmpty()) {
   *         throw new IllegalArgumentException("Browser name must be specified");
   *     }
   * }
   * }</pre>
   *
   */
  default void beforeInit() {
  }

  /**
   * Triggers after WebDriver initialization is complete.
   *
   * <p>This method is called immediately after the WebDriver instance has been
   * successfully created and is ready for use. This is the perfect place to
   * perform post-initialization setup, configure the driver, or capture
   * initialization metrics.</p>
   *
   * <p><strong>Common use cases:</strong></p>
   * <ul>
   *   <li>Configuring timeouts and window size</li>
   *   <li>Taking initial screenshots</li>
   *   <li>Recording initialization completion time</li>
   *   <li>Setting up browser-specific configurations</li>
   *   <li>Initializing page object factories</li>
   * </ul>
   *
   * <p><strong>Example usage:</strong></p>
   * <pre>{@code
   * @Override
   * public void afterInit(CDriverProvider driverProvider, CDriverEngine engine) {
   *     // Configure driver timeouts
   *     engine.manage().timeouts()
   *         .implicitlyWait(Duration.ofSeconds(10))
   *         .pageLoadTimeout(Duration.ofSeconds(30));
   *
   *     // Set window size
   *     engine.manage().window().setSize(new Dimension(1920, 1080));
   *
   *     // Log successful initialization
   *     String sessionId = engine.getSessionId().toString();
   *     logger.info("Driver initialized successfully. Session ID: {}", sessionId);
   *
   *     // Capture initial screenshot
   *     screenshotCapture.takeScreenshot(engine, "driver_initialized");
   * }
   * }</pre>
   *
   * @param driverProvider the provider that created the WebDriver instance
   * @param engine         the newly created WebDriver instance
   */
  default void afterInit(CDriverEngineProvider driverProvider, CDriverEngine engine) {
  }

  /**
   * Triggers before each WebDriver interaction/action is performed.
   *
   * <p>This method is called immediately before any WebDriver action such as
   * clicking elements, entering text, navigating to pages, or retrieving element
   * properties. It provides an opportunity to prepare for the action, capture
   * pre-action state, or implement custom logic.</p>
   *
   * <p><strong>Common use cases:</strong></p>
   * <ul>
   *   <li>Logging the action being performed</li>
   *   <li>Taking screenshots before critical actions</li>
   *   <li>Implementing retry logic preparation</li>
   *   <li>Validating page state before action</li>
   *   <li>Performance monitoring setup</li>
   * </ul>
   *
   * <p><strong>Example usage:</strong></p>
   * <pre>{@code
   * @Override
   * public void beforeAction(RemoteWebDriver webDriver, CWebPageInfo currentPage, String actionName) {
   *     logger.debug("About to perform action: {} on page: {}", actionName, currentPage.getUrl());
   *
   *     // Take screenshot before critical actions
   *     if (isCriticalAction(actionName)) {
   *         String screenshotName = String.format("before_%s_%d", actionName, System.currentTimeMillis());
   *         screenshotCapture.takeScreenshot(webDriver, screenshotName);
   *     }
   *
   *     // Validate page is ready for interaction
   *     if (!isPageReady(webDriver)) {
   *         logger.warn("Page may not be ready for action: {}", actionName);
   *     }
   *
   *     // Start performance timer
   *     performanceMonitor.startTimer(actionName);
   * }
   *
   * private boolean isCriticalAction(String actionName) {
   *     return actionName.contains("click") || actionName.contains("submit") || actionName.contains("navigate");
   * }
   * }</pre>
   *
   * @param webDriver   the WebDriver instance performing the action
   * @param currentPage information about the current page state
   * @param actionName  the name/description of the action being performed
   */
  default void beforeAction(CDriverEngine webDriver, CWebPageInfo currentPage, String actionName) {
  }

  /**
   * Triggers after each WebDriver interaction/action is completed.
   *
   * <p>This method is called immediately after any WebDriver action has been
   * performed, providing comprehensive information about the action results,
   * page state changes, performance metrics, and timing data. This is the
   * primary hook for post-action processing and analysis.</p>
   *
   * <p><strong>Common use cases:</strong></p>
   * <ul>
   *   <li>Performance monitoring and metrics collection</li>
   *   <li>Error detection and logging</li>
   *   <li>Screenshot capture after actions</li>
   *   <li>Page state validation</li>
   *   <li>Custom reporting and analytics</li>
   *   <li>Action result verification</li>
   * </ul>
   *
   * <p><strong>Example usage:</strong></p>
   * <pre>{@code
   * @Override
   * public void afterAction(String actionName, CDriverEngine webDriver,
   *                        CWebPageInfo pageBeforeAction, CWebPageInfo pageAfterAction,
   *                        CWebPageTransitionInfo driverMetricInfo, CDate startTime, long durationInNano) {
   *
   *     // Convert nanoseconds to milliseconds for readability
   *     double durationMs = durationInNano / 1_000_000.0;
   *
   *     logger.info("Action '{}' completed in {:.2f} ms", actionName, durationMs);
   *
   *     // Log page transition if URL changed
   *     if (!pageBeforeAction.getUrl().equals(pageAfterAction.getUrl())) {
   *         logger.info("Page navigated from '{}' to '{}'",
   *                    pageBeforeAction.getUrl(), pageAfterAction.getUrl());
   *     }
   *
   *     // Performance monitoring
   *     if (durationMs > SLOW_ACTION_THRESHOLD) {
   *         logger.warn("Slow action detected: {} took {:.2f} ms", actionName, durationMs);
   *         screenshotCapture.takeScreenshot(webDriver, "slow_action_" + actionName);
   *     }
   *
   *     // Collect metrics
   *     metricsCollector.recordActionDuration(actionName, durationMs);
   *     metricsCollector.recordPageTransition(driverMetricInfo);
   *
   *     // Validate page state after critical actions
   *     if (isCriticalAction(actionName)) {
   *         validatePageState(webDriver, pageAfterAction);
   *     }
   * }
   * }</pre>
   *
   * @param actionName       the name/description of the action that was performed
   * @param webDriver        the WebDriver instance that performed the action
   * @param pageBeforeAction page information before the action was performed
   * @param pageAfterAction  page information after the action was completed
   * @param driverMetricInfo metrics and performance data about the page transition
   * @param startTime        the timestamp when the action started
   * @param durationInNano   the duration of the action in nanoseconds
   */
  default void afterAction(
      String actionName,
      CDriverEngine webDriver,
      CWebPageInfo pageBeforeAction,
      CWebPageInfo pageAfterAction,
      CWebPageTransitionInfo driverMetricInfo,
      CDate startTime,
      long durationInNano) {
  }

  /**
   * Triggers when a page transition/change is detected.
   *
   * <p>This method is called when the WebDriver detects that the current page
   * has changed, typically due to navigation, form submissions, or dynamic
   * content loading. It provides specific metrics about the page transition
   * and timing information.</p>
   *
   * <p><strong>Common use cases:</strong></p>
   * <ul>
   *   <li>Page load performance monitoring</li>
   *   <li>Navigation tracking and analytics</li>
   *   <li>Page transition logging</li>
   *   <li>Wait condition validation</li>
   *   <li>Page readiness verification</li>
   *   <li>Custom page load event handling</li>
   * </ul>
   *
   * <p><strong>Example usage:</strong></p>
   * <pre>{@code
   * @Override
   * public void onPageChanged(CDriverEngine webDriver, CWebPageTransitionInfo driverMetricInfo,
   *                          CDate startTime, long durationInNano) {
   *
   *     double durationMs = durationInNano / 1_000_000.0;
   *     String currentUrl = webDriver.getCurrentUrl();
   *     String pageTitle = webDriver.getTitle();
   *
   *     logger.info("Page changed to '{}' (title: '{}') in {:.2f} ms",
   *                currentUrl, pageTitle, durationMs);
   *
   *     // Monitor page load performance
   *     if (durationMs > PAGE_LOAD_TIMEOUT_MS) {
   *         logger.warn("Slow page load detected: {:.2f} ms for {}", durationMs, currentUrl);
   *         performanceMonitor.recordSlowPageLoad(currentUrl, durationMs);
   *     }
   *
   *     // Capture page metrics
   *     pageMetrics.recordLoadTime(currentUrl, durationMs);
   *     pageMetrics.recordTransitionInfo(driverMetricInfo);
   *
   *     // Wait for page to be fully loaded
   *     waitForPageReady(webDriver);
   *
   *     // Take screenshot of new page
   *     String screenshotName = "page_changed_" + sanitizeUrl(currentUrl);
   *     screenshotCapture.takeScreenshot(webDriver, screenshotName);
   * }
   *
   * private void waitForPageReady(WebDriver driver) {
   *     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   *     wait.until(webDriver -> ((JavascriptExecutor) webDriver)
   *         .executeScript("return document.readyState").equals("complete"));
   * }
   * }</pre>
   *
   * @param webDriver        the WebDriver instance where the page change occurred
   * @param driverMetricInfo metrics and performance data about the page transition
   * @param startTime        the timestamp when the page transition started
   * @param durationInNano   the duration of the page transition in nanoseconds
   */
  default void onPageChanged(
      CDriverEngine webDriver,
      CWebPageTransitionInfo driverMetricInfo,
      CDate startTime,
      long durationInNano) {
  }
}

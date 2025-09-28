package org.catools.web.metrics;

import lombok.Data;
import org.catools.common.date.CDate;
import org.catools.web.entities.CWebPageInfo;
import org.openqa.selenium.WebDriver;

/**
 * A comprehensive web metrics collection class that aggregates various types of web performance 
 * and action metrics during automated web testing sessions.
 * 
 * <p>This class serves as a central repository for collecting and managing three main categories 
 * of web metrics:</p>
 * <ul>
 *   <li><strong>Action Metrics</strong> - Performance data for individual web actions (clicks, form fills, etc.)</li>
 *   <li><strong>Page Transition Metrics</strong> - Information about page navigations and state changes</li>
 *   <li><strong>Page Load Metrics</strong> - Timing and performance data for page loading operations</li>
 * </ul>
 * 
 * <p>The class uses Lombok's {@code @Data} annotation to automatically generate getters, setters, 
 * equals, hashCode, and toString methods for clean and maintainable code.</p>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Basic Usage - Collecting Web Action Metrics:</h4>
 * <pre>{@code
 * // Initialize metrics collector
 * CWebMetric webMetrics = new CWebMetric();
 * 
 * // Capture page state before action
 * CWebPageInfo beforePage = new CWebPageInfo(driver);
 * CDate startTime = CDate.now();
 * 
 * // Perform web action (e.g., clicking a button)
 * driver.$(By.id("submit-btn")).click();
 * 
 * // Capture page state after action
 * CWebPageInfo afterPage = new CWebPageInfo(driver);
 * 
 * // Record the action metric
 * webMetrics.addActionMetric("Click Submit Button", beforePage, afterPage, startTime);
 * }</pre>
 * 
 * <h4>Recording Page Load Metrics:</h4>
 * <pre>{@code
 * CWebMetric webMetrics = new CWebMetric();
 * CDate loadStartTime = CDate.now();
 * 
 * // Navigate to a page
 * driver.get("https://example.com/login");
 * 
 * // Record page load metric
 * webMetrics.addPageLoadMetric(driver, loadStartTime);
 * 
 * // The metric will include page title, URL, load time, and timestamp
 * }</pre>
 * 
 * <h4>Complete Test Session Metrics Collection:</h4>
 * <pre>{@code
 * CWebMetric sessionMetrics = new CWebMetric();
 * 
 * // Record page load
 * CDate pageLoadStart = CDate.now();
 * driver.get("https://example.com");
 * sessionMetrics.addPageLoadMetric(driver, pageLoadStart);
 * 
 * // Record login action
 * CWebPageInfo beforeLogin = new CWebPageInfo(driver);
 * CDate loginStart = CDate.now();
 * driver.$(By.id("username")).sendKeys("user@example.com");
 * driver.$(By.id("password")).sendKeys("password");
 * driver.$(By.id("login-btn")).click();
 * CWebPageInfo afterLogin = new CWebPageInfo(driver);
 * sessionMetrics.addActionMetric("User Login", beforeLogin, afterLogin, loginStart);
 * 
 * // Record page transition
 * CWebPageTransitionInfo transitionInfo = new CWebPageTransitionInfo(
 *     "Login to Dashboard", beforeLogin, afterLogin);
 * sessionMetrics.addPagePerformance(transitionInfo);
 * 
 * // Access collected metrics
 * System.out.println("Total actions recorded: " + 
 *     sessionMetrics.getActionPerformances().size());
 * System.out.println("Total page loads recorded: " + 
 *     sessionMetrics.getPageLoadMetrics().size());
 * System.out.println("Total transitions recorded: " + 
 *     sessionMetrics.getPagePerformances().size());
 * }</pre>
 * 
 * <h4>Performance Analysis:</h4>
 * <pre>{@code
 * CWebMetric metrics = new CWebMetric();
 * 
 * // ... collect metrics during test execution ...
 * 
 * // Analyze action performance
 * for (CWebActionMetric action : metrics.getActionPerformances()) {
 *     System.out.println("Action: " + action.getName() + 
 *                       " took " + action.getDuration() + " nanoseconds");
 *     
 *     if (action.getDuration() > 5000000000L) { // 5 seconds in nanoseconds
 *         System.out.println("WARNING: Slow action detected!");
 *     }
 * }
 * 
 * // Analyze page load performance
 * for (CWebPageLoadMetric pageLoad : metrics.getPageLoadMetrics()) {
 *     System.out.println("Page: " + pageLoad.getTitle() + 
 *                       " loaded in " + pageLoad.getDuration() + " nanoseconds");
 * }
 * }</pre>
 * 
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 * @see CWebActionMetric
 * @see CWebPageTransitionInfo
 * @see CWebPageLoadMetric
 * @see CWebPageInfo
 */
@Data
public class CWebMetric {
  
  /**
   * Collection of individual web action performance metrics.
   * 
   * <p>This field stores metrics for discrete user actions such as clicking buttons,
   * filling form fields, selecting dropdown options, etc. Each action metric includes
   * timing information, page states before and after the action, and action metadata.</p>
   * 
   * <p>Example actions tracked:</p>
   * <ul>
   *   <li>Button clicks and form submissions</li>
   *   <li>Text input and field interactions</li>
   *   <li>Menu selections and navigation</li>
   *   <li>File uploads and downloads</li>
   * </ul>
   */
  private CWebActionMetrics actionPerformances = new CWebActionMetrics();
  
  /**
   * Collection of page transition and navigation performance information.
   * 
   * <p>This field stores information about page-to-page transitions, including URL changes,
   * page state modifications, and associated performance metrics. It captures comprehensive
   * data about how users move through the web application.</p>
   * 
   * <p>Typical transitions tracked:</p>
   * <ul>
   *   <li>Navigation between different pages</li>
   *   <li>Form submissions that change page state</li>
   *   <li>AJAX-based content updates</li>
   *   <li>Single-page application state changes</li>
   * </ul>
   */
  private CWebPageTransitionsInfo pagePerformances = new CWebPageTransitionsInfo();
  
  /**
   * Collection of page loading performance metrics.
   * 
   * <p>This field stores timing and performance data specifically related to page loading
   * operations. It captures how long pages take to load completely and provides insights
   * into application performance from a user experience perspective.</p>
   * 
   * <p>Metrics include:</p>
   * <ul>
   *   <li>Initial page load times</li>
   *   <li>Page refresh operations</li>
   *   <li>Resource loading performance</li>
   *   <li>DOM ready and complete load events</li>
   * </ul>
   */
  private CWebPageLoadMetrics pageLoadMetrics = new CWebPageLoadMetrics();

  /**
   * Records a web action metric with comprehensive performance and state information.
   * 
   * <p>This method captures detailed information about a specific web action, including 
   * the page state before and after the action, timing information, and calculates the 
   * duration automatically based on the provided start time.</p>
   * 
   * <p>The method automatically calculates the action duration by measuring the time 
   * elapsed from the provided start time to the current time when this method is called.</p>
   * 
   * @param name A descriptive name for the action being performed. Should be clear and 
   *             specific to help with later analysis. Examples: "Click Login Button", 
   *             "Fill Registration Form", "Select Payment Method"
   * @param pageBeforeAction The page state captured before the action was performed. 
   *                        Contains URL, title, and other page information at the starting point
   * @param pageAfterAction The page state captured after the action was completed. 
   *                       Contains URL, title, and other page information at the end point
   * @param startTime The timestamp when the action was initiated. Used to calculate 
   *                  the total action duration in nanoseconds
   * 
   * <h4>Usage Examples:</h4>
   * 
   * <h5>Recording a Simple Button Click:</h5>
   * <pre>{@code
   * // Capture initial state
   * CWebPageInfo beforeState = new CWebPageInfo(driver);
   * CDate actionStart = CDate.now();
   * 
   * // Perform the action
   * driver.$(By.id("save-button")).click();
   * 
   * // Capture final state
   * CWebPageInfo afterState = new CWebPageInfo(driver);
   * 
   * // Record the metric
   * webMetrics.addActionMetric("Save Form Data", beforeState, afterState, actionStart);
   * }</pre>
   * 
   * <h5>Recording a Complex Form Interaction:</h5>
   * <pre>{@code
   * CWebPageInfo beforeForm = new CWebPageInfo(driver);
   * CDate formStart = CDate.now();
   * 
   * // Fill multiple form fields
   * driver.$(By.name("firstName")).sendKeys("John");
   * driver.$(By.name("lastName")).sendKeys("Doe");
   * driver.$(By.name("email")).sendKeys("john.doe@example.com");
   * driver.$(By.id("submit")).click();
   * 
   * CWebPageInfo afterForm = new CWebPageInfo(driver);
   * webMetrics.addActionMetric("Complete User Registration", beforeForm, afterForm, formStart);
   * }</pre>
   * 
   * @since 1.0
   */
  public void addActionMetric(String name, CWebPageInfo pageBeforeAction, CWebPageInfo pageAfterAction, CDate startTime) {
    CWebActionMetric action = new CWebActionMetric()
        .setName(name)
        .setPageBeforeAction(pageBeforeAction)
        .setPageAfterAction(pageAfterAction)
        .setActionTime(startTime)
        .setDuration(startTime.getDurationToNow().getNano());
    actionPerformances.add(action);
  }

  /**
   * Adds a page transition performance record to the metrics collection.
   * 
   * <p>This method records information about page transitions, including navigation events,
   * state changes, and associated performance metrics. It's particularly useful for tracking
   * user journey flows and identifying performance bottlenecks during page transitions.</p>
   * 
   * <p>Page transitions can include:</p>
   * <ul>
   *   <li>Navigation from one page to another</li>
   *   <li>Form submissions that result in page changes</li>
   *   <li>AJAX operations that modify page content</li>
   *   <li>Single-page application route changes</li>
   * </ul>
   * 
   * @param pageTransitionInfo A comprehensive object containing information about the page 
   *                          transition, including before/after page states, action details,
   *                          performance metrics, and timing information
   * 
   * <h4>Usage Examples:</h4>
   * 
   * <h5>Recording a Simple Navigation:</h5>
   * <pre>{@code
   * // Capture current page state
   * CWebPageInfo currentPage = new CWebPageInfo(driver);
   * 
   * // Perform navigation
   * driver.$(By.linkText("About Us")).click();
   * 
   * // Capture new page state
   * CWebPageInfo newPage = new CWebPageInfo(driver);
   * 
   * // Create and record transition info
   * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *     "Navigate to About Page", currentPage, newPage);
   * webMetrics.addPagePerformance(transition);
   * }</pre>
   * 
   * <h5>Recording a Form Submission Transition:</h5>
   * <pre>{@code
   * CWebPageInfo loginPage = new CWebPageInfo(driver);
   * 
   * // Fill and submit login form
   * driver.$(By.name("username")).sendKeys("testuser");
   * driver.$(By.name("password")).sendKeys("password");
   * driver.$(By.id("login-submit")).click();
   * 
   * CWebPageInfo dashboardPage = new CWebPageInfo(driver);
   * 
   * // Record the login transition with timing
   * CWebPageTransitionInfo loginTransition = new CWebPageTransitionInfo(
   *     "User Login Process", loginPage, dashboardPage);
   * loginTransition.setActionTime(new Date());
   * 
   * webMetrics.addPagePerformance(loginTransition);
   * }</pre>
   * 
   * <h5>Accessing Transition Information:</h5>
   * <pre>{@code
   * // After adding transitions, you can analyze them
   * for (CWebPageTransitionInfo transition : webMetrics.getPagePerformances()) {
   *     System.out.println("Action: " + transition.getActionName());
   *     System.out.println("From: " + transition.getUrlBeforeAction());
   *     System.out.println("To: " + transition.getUrlAfterAction());
   *     
   *     // Check if it was a successful navigation
   *     if (!transition.getUrlBeforeAction().equals(transition.getUrlAfterAction())) {
   *         System.out.println("Navigation successful!");
   *     }
   * }
   * }</pre>
   * 
   * @since 1.0
   * @see CWebPageTransitionInfo
   * @see CWebPageInfo
   */
  public void addPagePerformance(CWebPageTransitionInfo pageTransitionInfo) {
    pagePerformances.add(pageTransitionInfo);
  }

  /**
   * Records a page load performance metric using the current WebDriver state.
   * 
   * <p>This method captures comprehensive page loading performance information by automatically
   * extracting the current page title and URL from the provided WebDriver instance. It calculates
   * the loading duration based on the provided start time and creates a standardized page load metric.</p>
   * 
   * <p>The method is particularly useful for measuring:</p>
   * <ul>
   *   <li>Initial page load times during navigation</li>
   *   <li>Page refresh performance</li>
   *   <li>Resource loading efficiency</li>
   *   <li>Overall user experience timing</li>
   * </ul>
   * 
   * <p>The recorded metric includes automatic calculation of load duration in nanoseconds,
   * providing high-precision timing information for performance analysis.</p>
   * 
   * @param driver The WebDriver instance from which to extract current page information 
   *               (title and URL). The driver should be in a stable state after page loading
   * @param startTime The timestamp when the page load operation was initiated. Used to 
   *                  calculate the total loading duration
   * 
   * <h4>Usage Examples:</h4>
   * 
   * <h5>Recording Initial Page Load:</h5>
   * <pre>{@code
   * CWebMetric webMetrics = new CWebMetric();
   * 
   * // Start timing before navigation
   * CDate loadStart = CDate.now();
   * 
   * // Navigate to page
   * driver.get("https://example.com/products");
   * 
   * // Wait for page to fully load (if needed)
   * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   * wait.until(webDriver -> ((JavascriptExecutor) webDriver)
   *     .executeScript("return document.readyState").equals("complete"));
   * 
   * // Record the page load metric
   * webMetrics.addPageLoadMetric(driver, loadStart);
   * }</pre>
   * 
   * <h5>Recording Page Refresh Performance:</h5>
   * <pre>{@code
   * // Record refresh performance
   * CDate refreshStart = CDate.now();
   * driver.navigate().refresh();
   * 
   * // Wait for refresh completion
   * new WebDriverWait(driver, Duration.ofSeconds(15))
   *     .until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
   * 
   * webMetrics.addPageLoadMetric(driver, refreshStart);
   * }</pre>
   * 
   * <h5>Performance Analysis After Collection:</h5>
   * <pre>{@code
   * // Analyze collected page load metrics
   * for (CWebPageLoadMetric loadMetric : webMetrics.getPageLoadMetrics()) {
   *     long durationMs = loadMetric.getDuration() / 1_000_000; // Convert to milliseconds
   *     
   *     System.out.println("Page: " + loadMetric.getTitle());
   *     System.out.println("URL: " + loadMetric.getUrl());
   *     System.out.println("Load time: " + durationMs + "ms");
   *     
   *     // Flag slow loading pages
   *     if (durationMs > 3000) {
   *         System.out.println("WARNING: Slow page load detected!");
   *     }
   * }
   * }</pre>
   * 
   * <h5>Integration with Test Framework:</h5>
   * <pre>{@code
   * &#64;Test
   * public void testPageLoadPerformance() {
   *     CWebMetric metrics = new CWebMetric();
   *     
   *     // Test multiple page loads
   *     String[] testPages = {
   *         "https://example.com/home",
   *         "https://example.com/about", 
   *         "https://example.com/contact"
   *     };
   *     
   *     for (String url : testPages) {
   *         CDate loadStart = CDate.now();
   *         driver.get(url);
   *         metrics.addPageLoadMetric(driver, loadStart);
   *     }
   *     
   *     // Assert performance thresholds
   *     for (CWebPageLoadMetric metric : metrics.getPageLoadMetrics()) {
   *         long loadTimeMs = metric.getDuration() / 1_000_000;
   *         Assert.assertTrue("Page load too slow: " + metric.getUrl(), 
   *                          loadTimeMs < 5000);
   *     }
   * }
   * }</pre>
   * 
   * @since 1.0
   * @see CWebPageLoadMetric
   * @see CDate
   * @see WebDriver
   */
  public void addPageLoadMetric(WebDriver driver, CDate startTime) {
    CWebPageLoadMetric pageLoad = new CWebPageLoadMetric()
        .setName("Page Load")
        .setTitle(driver.getTitle())
        .setUrl(driver.getCurrentUrl())
        .setActionTime(startTime)
        .setDuration(startTime.getDurationToNow().getNano());
    pageLoadMetrics.add(pageLoad);
  }
}

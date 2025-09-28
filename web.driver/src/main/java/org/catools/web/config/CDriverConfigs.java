package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Utility class for managing web driver configuration settings.
 * 
 * <p>This class provides static methods to retrieve configuration values for web driver behavior,
 * including ready state waiting, performance metrics collection, network metrics collection, 
 * and timeout settings. All configuration values are read from HOCON configuration files.</p>
 * 
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * // Check if ready state waiting is enabled before actions
 * if (CDriverConfigs.waitCompleteReadyStateBeforeEachAction()) {
 *     // Wait for page to be ready before performing action
 *     driver.waitForReadyState();
 * }
 * 
 * // Get configured timeout value
 * int timeout = CDriverConfigs.getTimeout();
 * driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
 * 
 * // Check if performance metrics collection is enabled
 * if (CDriverConfigs.isCollectPerformanceMetricsEnable()) {
 *     performanceCollector.startCollection();
 * }
 * }</pre>
 * 
 * @author CATools Team
 * @since 1.0
 */
@UtilityClass
public class CDriverConfigs {

  /**
   * Determines whether the web driver should wait for the complete ready state before each action.
   * 
   * <p>When enabled, the driver will wait for the document ready state to be "complete" 
   * before executing any web driver action. This helps ensure that the page is fully loaded 
   * and ready for interaction.</p>
   * 
   * <p><b>Configuration path:</b> {@code catools.web.driver.wait_ready_state_before_action}</p>
   * 
   * <p><b>Example usage:</b></p>
   * <pre>{@code
   * if (CDriverConfigs.waitCompleteReadyStateBeforeEachAction()) {
   *     // Wait for document.readyState === "complete" before clicking
   *     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   *     wait.until(webDriver -> ((JavascriptExecutor) webDriver)
   *         .executeScript("return document.readyState").equals("complete"));
   * }
   * element.click();
   * }</pre>
   * 
   * @return {@code true} if ready state waiting before actions is enabled, {@code false} otherwise
   */
  public static boolean waitCompleteReadyStateBeforeEachAction() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_DRIVER_WAIT_READY_STATE_BEFORE_ACTION);
  }

  /**
   * Determines whether the web driver should wait for the complete ready state after each action.
   * 
   * <p>When enabled, the driver will wait for the document ready state to be "complete" 
   * after executing any web driver action. This helps ensure that any asynchronous operations 
   * triggered by the action have completed before proceeding.</p>
   * 
   * <p><b>Configuration path:</b> {@code catools.web.driver.wait_ready_state_after_action}</p>
   * 
   * <p><b>Example usage:</b></p>
   * <pre>{@code
   * element.click();
   * if (CDriverConfigs.waitCompleteReadyStateAfterEachAction()) {
   *     // Wait for any AJAX calls or dynamic content loading to complete
   *     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   *     wait.until(webDriver -> ((JavascriptExecutor) webDriver)
   *         .executeScript("return document.readyState").equals("complete"));
   * }
   * }</pre>
   * 
   * @return {@code true} if ready state waiting after actions is enabled, {@code false} otherwise
   */
  public static boolean waitCompleteReadyStateAfterEachAction() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_DRIVER_WAIT_READY_STATE_AFTER_ACTION);
  }

  /**
   * Determines whether performance metrics collection is enabled for the web driver.
   * 
   * <p>When enabled, the driver will collect performance-related metrics such as page load times,
   * resource loading times, and other browser performance data. This is useful for performance 
   * testing and monitoring.</p>
   * 
   * <p><b>Configuration path:</b> {@code catools.web.driver.collect_performance_metrics}</p>
   * 
   * <p><b>Example usage:</b></p>
   * <pre>{@code
   * if (CDriverConfigs.isCollectPerformanceMetricsEnable()) {
   *     // Enable performance logging
   *     LoggingPreferences logPrefs = new LoggingPreferences();
   *     logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
   *     
   *     ChromeOptions options = new ChromeOptions();
   *     options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
   *     
   *     WebDriver driver = new ChromeDriver(options);
   *     
   *     // Collect performance metrics after navigation
   *     driver.get("https://example.com");
   *     LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
   *     for (LogEntry entry : logs) {
   *         System.out.println(entry.getMessage());
   *     }
   * }
   * }</pre>
   * 
   * @return {@code true} if performance metrics collection is enabled, {@code false} otherwise
   */
  public static boolean isCollectPerformanceMetricsEnable() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_DRIVER_COLLECT_PERFORMANCE_METRICS);
  }

  /**
   * Determines whether network metrics collection is enabled for the web driver.
   * 
   * <p>When enabled, the driver will collect network-related metrics such as HTTP requests,
   * response times, status codes, and other network activity data. This is valuable for 
   * API testing and network performance analysis.</p>
   * 
   * <p><b>Configuration path:</b> {@code catools.web.driver.collect_network_metrics}</p>
   * 
   * <p><b>Example usage:</b></p>
   * <pre>{@code
   * if (CDriverConfigs.isCollectNetworkMetricsEnable()) {
   *     // Enable network domain for Chrome DevTools
   *     ChromeDriver driver = new ChromeDriver();
   *     DevTools devTools = driver.getDevTools();
   *     devTools.createSession();
   *     devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
   *     
   *     // Listen for network events
   *     devTools.addListener(Network.responseReceived(), response -> {
   *         System.out.println("Response: " + response.getResponse().getUrl() + 
   *                           " - Status: " + response.getResponse().getStatus());
   *     });
   *     
   *     driver.get("https://example.com");
   * }
   * }</pre>
   * 
   * @return {@code true} if network metrics collection is enabled, {@code false} otherwise
   */
  public static boolean isCollectNetworkMetricsEnable() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_DRIVER_COLLECT_NETWORK_METRICS);
  }

  /**
   * Retrieves the configured timeout value for browser operations in seconds.
   * 
   * <p>This timeout value is used for various web driver operations such as implicit waits,
   * explicit waits, and page load timeouts. The value should be configured based on the 
   * application's expected response times and network conditions.</p>
   * 
   * <p><b>Configuration path:</b> {@code catools.web.driver.browser_timeout}</p>
   * 
   * <p><b>Example usage:</b></p>
   * <pre>{@code
   * int timeoutSeconds = CDriverConfigs.getTimeout();
   * 
   * // Set implicit wait timeout
   * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSeconds));
   * 
   * // Set page load timeout
   * driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeoutSeconds));
   * 
   * // Use in explicit waits
   * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
   * WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
   * 
   * // Use with fluent waits
   * Wait<WebDriver> fluentWait = new FluentWait<>(driver)
   *     .withTimeout(Duration.ofSeconds(timeoutSeconds))
   *     .pollingEvery(Duration.ofMillis(500));
   * }</pre>
   * 
   * @return the configured timeout value in seconds
   */
  public static int getTimeout() {
    return CHocon.asInteger(Configs.CATOOLS_WEB_DRIVER_BROWSER_TIMEOUT);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_DRIVER_WAIT_READY_STATE_BEFORE_ACTION("catools.web.driver.wait_ready_state_before_action"),
    CATOOLS_WEB_DRIVER_WAIT_READY_STATE_AFTER_ACTION("catools.web.driver.wait_ready_state_after_action"),
    CATOOLS_WEB_DRIVER_COLLECT_PERFORMANCE_METRICS("catools.web.driver.collect_performance_metrics"),
    CATOOLS_WEB_DRIVER_COLLECT_NETWORK_METRICS("catools.web.driver.collect_network_metrics"),
    CATOOLS_WEB_DRIVER_BROWSER_TIMEOUT("catools.web.driver.browser_timeout");

    private final String path;
  }
}

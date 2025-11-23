package org.catools.web.drivers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;
import org.catools.common.tests.CTest;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.config.CGridConfigs;
import org.catools.web.entities.CWebPageInfo;
import org.catools.web.listeners.CDriverOTelNetworkListener;
import org.catools.web.metrics.CWebPageTransitionInfo;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.*;
import org.openqa.selenium.devtools.v138.log.Log;
import org.openqa.selenium.devtools.v138.network.Network;
import org.openqa.selenium.devtools.v138.performance.Performance;
import org.openqa.selenium.devtools.v138.performance.model.Metric;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Optional;

/**
 * CDevTools provides Chrome DevTools Protocol (CDP) integration for WebDriver automation.
 * This class enables collection of network metrics, performance metrics, and browser logs
 * during web automation testing.
 *
 * <p>Features supported:</p>
 * <ul>
 *   <li>Network metrics collection using DevTools Protocol</li>
 *   <li>Performance metrics gathering</li>
 *   <li>OpenTelemetry integration for observability</li>
 *   <li>Support for both local Chrome and remote Grid with CDP</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * CDevTools devTools = new CDevTools();
 * CTest test = new CTest("MyTest");
 *
 * // Start recording metrics before navigation
 * devTools.startRecording(test, "Navigate to homepage", webDriver);
 *
 * // Perform navigation or actions
 * webDriver.get("https://example.com");
 *
 * // Stop recording and get metrics
 * CWebPageInfo previousPage = new CWebPageInfo("about:blank", "");
 * CWebPageInfo currentPage = new CWebPageInfo("https://example.com", "Example Site");
 * CWebPageTransitionInfo metrics = devTools.stopRecording(previousPage, currentPage);
 * </pre>
 *
 * @author CATools Team
 * @since 1.0
 */
@Slf4j
public class CDevTools {

  private DevTools devTools;
  private CDriverOTelNetworkListener listener;

  /**
   * Starts recording network and performance metrics using Chrome DevTools Protocol.
   *
   * <p>This method initializes a DevTools session and configures it based on the current
   * driver configuration settings. It enables or disables network and performance monitoring
   * based on {@link CDriverConfigs} settings.</p>
   *
   * <p>The method will:</p>
   * <ul>
   *   <li>Initialize DevTools connection if not already done</li>
   *   <li>Create a new DevTools session</li>
   *   <li>Configure network monitoring based on {@code CDriverConfigs.isCollectNetworkMetricsEnable()}</li>
   *   <li>Configure performance monitoring based on {@code CDriverConfigs.isCollectPerformanceMetricsEnable()}</li>
   *   <li>Attach an OpenTelemetry network listener for observability</li>
   * </ul>
   *
   * <p>Example usage:</p>
   * <pre>
   * CTest testInstance = new CTest("LoginTest");
   * CDevTools devTools = new CDevTools();
   * ChromeDriver driver = new ChromeDriver();
   *
   * // Start recording before performing actions
   * devTools.startRecording(testInstance, "User Login", driver);
   *
   * // Now perform your test actions
   * driver.get("https://example.com/login");
   * </pre>
   *
   * @param testInstance the test instance containing test metadata and context
   * @param actionName a descriptive name for the action being recorded (e.g., "Page Load", "User Login")
   * @param webDriver the RemoteWebDriver instance that supports DevTools (must be Chrome-based)
   *
   * @throws UnsupportedOperationException if the WebDriver doesn't support DevTools
   * @throws DevToolsException if there's an error creating the DevTools session
   *
   * @see CDriverConfigs#isCollectNetworkMetricsEnable()
   * @see CDriverConfigs#isCollectPerformanceMetricsEnable()
   * @see CDriverOTelNetworkListener
   */
  public void startRecording(CTest testInstance, String actionName, RemoteWebDriver webDriver) {
    init(webDriver);

    if (canNotRecord(webDriver)) return;

    devTools.createSession();
    devTools.send(Log.disable());

    if (CDriverConfigs.isCollectNetworkMetricsEnable())
      devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
    else
      devTools.send(Network.disable());

    if (CDriverConfigs.isCollectPerformanceMetricsEnable())
      devTools.send(Performance.enable(Optional.empty()));
    else
      devTools.send(Performance.disable());

    String title = webDriver.getTitle();
    String tracerName = StringUtils.isBlank(title) ? actionName : "%s on \"%s\"".formatted(actionName, title);
    listener = new CDriverOTelNetworkListener(testInstance.getName(), tracerName);
    listener.attach(devTools);
  }

  /**
   * Stops the recording session and collects all performance metrics gathered during the session.
   *
   * <p>This method is synchronized to ensure thread safety when multiple tests are running
   * concurrently. It performs the following operations:</p>
   * <ul>
   *   <li>Retrieves performance metrics from the DevTools session</li>
   *   <li>Detaches the OpenTelemetry network listener</li>
   *   <li>Creates a comprehensive transition info object with collected data</li>
   *   <li>Handles any DevTools exceptions gracefully</li>
   * </ul>
   *
   * <p>The returned {@link CWebPageTransitionInfo} contains:</p>
   * <ul>
   *   <li>Performance metrics (if collection was enabled)</li>
   *   <li>Network timing information</li>
   *   <li>Page transition details</li>
   *   <li>Timestamp of when recording stopped</li>
   * </ul>
   *
   * <p>Example usage:</p>
   * <pre>
   * // After starting recording and performing actions
   * CWebPageInfo previousPage = new CWebPageInfo("https://example.com", "Home Page");
   * CWebPageInfo currentPage = new CWebPageInfo("https://example.com/products", "Products Page");
   *
   * CWebPageTransitionInfo transitionInfo = devTools.stopRecording(previousPage, currentPage);
   *
   * // Access collected metrics
   * CList&lt;Metric&gt; performanceMetrics = transitionInfo.getPerformanceMetrics();
   * long navigationTime = transitionInfo.getTimestamp();
   * </pre>
   *
   * @param previousPage information about the page before the transition (source page)
   * @param currentPage information about the page after the transition (destination page)
   *
   * @return a {@link CWebPageTransitionInfo} object containing all collected metrics and transition data.
   *         If an error occurs, returns a transition info object with minimal data but no metrics.
   *
   * @throws DevToolsException if there's an error reading metrics (handled gracefully)
   *
   * @see CWebPageTransitionInfo
   * @see CWebPageInfo
   * @see org.openqa.selenium.devtools.v138.performance.model.Metric
   */
  public synchronized CWebPageTransitionInfo stopRecording(CWebPageInfo previousPage, CWebPageInfo currentPage) {
    try {
      CList<Metric> metricList = readPerformanceMetrics();
      listener.detach();

      return new CWebPageTransitionInfo("Page Transition", previousPage, currentPage, metricList, CDate.now());
    } catch (DevToolsException exception) {
      return new CWebPageTransitionInfo("Page Transition", previousPage, currentPage);
    }
  }

  private CList<Metric> readPerformanceMetrics() {
    if (devTools == null || !CDriverConfigs.isCollectPerformanceMetricsEnable())
      return CList.of();

    try {
      return new CList<>(devTools.send(Performance.getMetrics()));
    } finally {
      devTools.disconnectSession();
    }
  }

  private void init(RemoteWebDriver webDriver) {
    if (devTools != null) {
      return;
    }

    try {
      if (webDriver instanceof HasDevTools hasDevTools) {
        devTools = hasDevTools.getDevTools();
      } else if (webDriver.getCapabilities().asMap().containsKey("se:cdp")) {
        DevToolsProvider devToolsProvider = new DevToolsProvider();
        ChromeOptions other = new ChromeOptions();
        other.setCapability("se:cdp", CGridConfigs.getCdpURL(webDriver.getSessionId().toString()));
        Capabilities merged = webDriver.getCapabilities().merge(other);
        devTools = devToolsProvider.getImplementation(merged, new RemoteExecuteMethod(webDriver)).getDevTools();
      } else {
        throw new UnsupportedOperationException("DevTools support only with chromedriver or Grid with CDP enabled feature");
      }
      java.util.logging.Logger.getLogger(Connection.class.getName()).setLevel(java.util.logging.Level.OFF);
    } catch (Exception ex) {
      log.warn("Failed to init DevTools", ex);
    }
  }

  private boolean canNotRecord(RemoteWebDriver webDriver) {
    return devTools == null ||
        !(webDriver instanceof HasDevTools && webDriver.getCapabilities().asMap().containsKey("se:cdp"));
  }
}

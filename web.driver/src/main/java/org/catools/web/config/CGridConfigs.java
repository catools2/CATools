package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;

/**
 * Utility class for managing Selenium Grid configuration settings.
 * <p>
 * This class provides static methods to access various configuration parameters
 * for Selenium Grid setup, including remote driver settings, hub connection details,
 * and various grid-specific options.
 * </p>
 * 
 * <h3>Configuration Examples:</h3>
 * <pre>{@code
 * // Check if remote driver should be used
 * if (CGridConfigs.isUseRemoteDriver()) {
 *     URL hubUrl = CGridConfigs.getHubURL();
 *     // Initialize remote WebDriver with hub URL
 * }
 * 
 * // Get CDP URL for debugging
 * String sessionId = "abc123-def456";
 * String cdpUrl = CGridConfigs.getCdpURL(sessionId);
 * // Use CDP URL for Chrome DevTools Protocol connection
 * }</pre>
 * 
 * @author CATools Team
 * @since 1.0
 */
@Slf4j
@UtilityClass
public class CGridConfigs {

  /**
   * Determines whether remote WebDriver should be used instead of local WebDriver.
   * <p>
   * This setting controls whether tests should connect to a remote Selenium Grid
   * or use local browser drivers.
   * </p>
   * 
   * @return {@code true} if remote driver should be used, {@code false} for local driver
   * 
   * @example
   * <pre>{@code
   * if (CGridConfigs.isUseRemoteDriver()) {
   *     // Configure RemoteWebDriver
   *     WebDriver driver = new RemoteWebDriver(CGridConfigs.getHubURL(), capabilities);
   * } else {
   *     // Use local ChromeDriver, FirefoxDriver, etc.
   *     WebDriver driver = new ChromeDriver();
   * }
   * }</pre>
   */
  public static boolean isUseRemoteDriver() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_REMOTE_DRIVER);
  }

  /**
   * Determines whether to use TestContainers for browser automation.
   * <p>
   * TestContainers allows running browser instances in Docker containers,
   * providing isolated and reproducible test environments.
   * </p>
   * 
   * @return {@code true} if TestContainers should be used, {@code false} otherwise
   * 
   * @example
   * <pre>{@code
   * if (CGridConfigs.isUseTestContainer()) {
   *     // Set up TestContainers with Selenium
   *     BrowserWebDriverContainer<?> container = new BrowserWebDriverContainer<>()
   *         .withCapabilities(new ChromeOptions());
   *     container.start();
   * }
   * }</pre>
   */
  public static boolean isUseTestContainer() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_TEST_CONTAINER);
  }

  /**
   * Constructs the Chrome DevTools Protocol (CDP) WebSocket URL for a given session.
   * <p>
   * The CDP URL allows direct communication with Chrome DevTools for advanced
   * browser automation, network monitoring, and debugging capabilities.
   * The protocol scheme is automatically determined based on the grid hub schema
   * (https → wss, http → ws).
   * </p>
   * 
   * @param sessionId the WebDriver session ID to connect to
   * @return the CDP WebSocket URL if remote driver is enabled, {@code null} otherwise
   * 
   * @example
   * <pre>{@code
   * // After creating a RemoteWebDriver session
   * RemoteWebDriver driver = new RemoteWebDriver(hubUrl, capabilities);
   * String sessionId = driver.getSessionId().toString();
   * String cdpUrl = CGridConfigs.getCdpURL(sessionId);
   * 
   * // Use CDP URL with Chrome DevTools
   * if (cdpUrl != null) {
   *     // Example: "wss://grid.example.com:4444/session/abc123/se/cdp"
   *     ChromeDevToolsService devTools = new ChromeDevToolsService(cdpUrl);
   * }
   * }</pre>
   */
  public static String getCdpURL(String sessionId) {
    if (isUseRemoteDriver()) {
      String schema = "https".equals(getGridHubSchema()) ? "wss" : "ws";
      return "%s://%s:%s/session/%s/se/cdp".formatted(schema, getGridHubIP(), getGridHubPort(), sessionId);
    }
    return null;
  }

  /**
   * Constructs the Selenium Grid Hub URL for WebDriver connections.
   * <p>
   * This URL is used to initialize RemoteWebDriver instances that connect
   * to the Selenium Grid hub. The URL is constructed using the configured
   * schema, IP address, and port.
   * </p>
   * 
   * @return the complete Hub URL if remote driver is enabled, {@code null} otherwise
   * @throws RuntimeException if the constructed URL is malformed
   * 
   * @example
   * <pre>{@code
   * URL hubUrl = CGridConfigs.getHubURL();
   * if (hubUrl != null) {
   *     // Example URL: "http://selenium-hub:4444/wd/hub"
   *     ChromeOptions options = new ChromeOptions();
   *     WebDriver driver = new RemoteWebDriver(hubUrl, options);
   * }
   * }</pre>
   */
  public static URL getHubURL() {
    if (isUseRemoteDriver()) {
      try {
        return URI.create("%s://%s:%s/wd/hub".formatted(getGridHubSchema(), getGridHubIP(), getGridHubPort())).toURL();
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  /**
   * Retrieves the protocol schema for connecting to the Selenium Grid hub.
   * <p>
   * Common values are "http" for standard connections or "https" for
   * secure connections. This affects both the hub URL and CDP URL generation.
   * </p>
   * 
   * @return the protocol schema (e.g., "http", "https")
   * 
   * @example
   * <pre>{@code
   * String schema = CGridConfigs.getGridHubSchema();
   * // Typical values: "http" or "https"
   * 
   * // Used internally to construct URLs like:
   * // "https://grid.example.com:4444/wd/hub"
   * }</pre>
   */
  public static String getGridHubSchema() {
    return CHocon.asString(Configs.CATOOLS_WEB_GRID_HUB_SCHEMA);
  }

  /**
   * Retrieves the IP address or hostname of the Selenium Grid hub.
   * <p>
   * This can be a numeric IP address (e.g., "192.168.1.100") or a hostname
   * (e.g., "selenium-hub", "grid.example.com").
   * </p>
   * 
   * @return the IP address or hostname of the grid hub
   * 
   * @example
   * <pre>{@code
   * String hubIP = CGridConfigs.getGridHubIP();
   * // Examples:
   * // "localhost" - for local development
   * // "192.168.1.100" - specific IP address
   * // "selenium-hub" - Docker service name
   * // "grid.example.com" - external grid service
   * }</pre>
   */
  public static String getGridHubIP() {
    return CHocon.asString(Configs.CATOOLS_WEB_GRID_HUB_IP);
  }

  /**
   * Retrieves the port number for connecting to the Selenium Grid hub.
   * <p>
   * The default port for Selenium Grid hub is typically 4444, but this
   * can be customized based on your infrastructure setup.
   * </p>
   * 
   * @return the port number of the grid hub
   * 
   * @example
   * <pre>{@code
   * int hubPort = CGridConfigs.getGridHubPort();
   * // Common values:
   * // 4444 - standard Selenium Grid port
   * // 4443 - alternative port for SSL
   * // 8080 - custom port configuration
   * 
   * // Used to construct full URLs like:
   * // "http://selenium-hub:4444/wd/hub"
   * }</pre>
   */
  public static int getGridHubPort() {
    return CHocon.asInteger(Configs.CATOOLS_WEB_GRID_HUB_PORT);
  }

  /**
   * Determines whether hub folder mode is enabled for the Selenium Grid.
   * <p>
   * Hub folder mode affects how the grid organizes and manages browser
   * sessions and associated resources. When enabled, the hub may create
   * separate folders for different sessions or test runs.
   * </p>
   * 
   * @return {@code true} if hub folder mode is enabled, {@code false} otherwise
   * 
   * @example
   * <pre>{@code
   * if (CGridConfigs.isUseHubFolderModeIsOn()) {
   *     // Configure session with folder organization
   *     // Grid will manage files in organized folder structure
   *     log.info("Hub folder mode is enabled - sessions will be organized in folders");
   * }
   * }</pre>
   */
  public static boolean isUseHubFolderModeIsOn() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_HUB_FOLDERS);
  }

  /**
   * Determines whether local file detector should be enabled for remote WebDriver.
   * <p>
   * The local file detector allows RemoteWebDriver to handle file uploads
   * by automatically detecting local file paths and transferring files
   * to the remote browser session. This is essential for file upload
   * functionality in remote grid environments.
   * </p>
   * 
   * @return {@code true} if local file detector should be enabled, {@code false} otherwise
   * 
   * @example
   * <pre>{@code
   * RemoteWebDriver driver = new RemoteWebDriver(hubUrl, capabilities);
   * 
   * if (CGridConfigs.isUseLocalFileDetectorInOn()) {
   *     // Enable file upload capability for remote sessions
   *     driver.setFileDetector(new LocalFileDetector());
   *     
   *     // Now file uploads will work:
   *     WebElement uploadElement = driver.$(By.id("file-upload"));
   *     uploadElement.sendKeys("/path/to/local/file.txt");
   * }
   * }</pre>
   */
  public static boolean isUseLocalFileDetectorInOn() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_LOCAL_FILE_DETECTOR);
  }

  /**
   * Retrieves the logging level for Selenium Grid operations.
   * <p>
   * This controls the verbosity of logging output from WebDriver and
   * grid-related operations. The level affects both local and remote
   * driver logging behavior.
   * </p>
   * 
   * @return the configured {@link Level} for logging (e.g., INFO, DEBUG, WARNING)
   * 
   * @example
   * <pre>{@code
   * Level logLevel = CGridConfigs.getLogLevel();
   * 
   * // Apply to WebDriver logging
   * LoggingPreferences loggingPrefs = new LoggingPreferences();
   * loggingPrefs.enable(LogType.BROWSER, logLevel);
   * loggingPrefs.enable(LogType.DRIVER, logLevel);
   * 
   * ChromeOptions options = new ChromeOptions();
   * options.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs);
   * 
   * // Common levels:
   * // Level.INFO - standard information logging
   * // Level.DEBUG - detailed debugging information  
   * // Level.WARNING - only warnings and errors
   * // Level.SEVERE - only severe errors
   * }</pre>
   */
  public static Level getLogLevel() {
    return Level.parse(CHocon.asString(Configs.CATOOLS_WEB_GRID_LOG_LEVEL));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_GRID_HUB_IP("catools.web.grid.hub.ip"),
    CATOOLS_WEB_GRID_HUB_PORT("catools.web.grid.hub.port"),
    CATOOLS_WEB_GRID_HUB_SCHEMA("catools.web.grid.hub.schema"),
    CATOOLS_WEB_GRID_LOG_LEVEL("catools.web.grid.log_level"),
    CATOOLS_WEB_GRID_USE_REMOTE_DRIVER("catools.web.grid.use_remote_driver"),
    CATOOLS_WEB_GRID_USE_TEST_CONTAINER("catools.web.grid.use_test_container"),
    CATOOLS_WEB_GRID_USE_HUB_FOLDERS("catools.web.grid.use_hub_folders"),
    CATOOLS_WEB_GRID_USE_LOCAL_FILE_DETECTOR("catools.web.grid.use_local_file_detector");

    private final String path;
  }
}

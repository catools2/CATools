package org.catools.web.drivers.selenium;

import org.catools.web.drivers.CDriverEngine;
import org.catools.web.drivers.CDriverEngineProvider;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.enums.CBrowser;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.catools.web.config.CGridConfigs.isUseRemoteDriver;
import static org.catools.web.config.CGridConfigs.isUseTestContainer;

/**
 * Interface for providing WebDriver instances with various configuration options.
 * <p>
 * This interface defines the contract for creating WebDriver instances that can be configured
 * to run locally, remotely, or in test containers. It supports different browsers and provides
 * lifecycle hooks through listeners.
 * </p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Basic usage with Chrome provider
 * CDriverProvider chromeProvider = new ChromeDriverProvider();
 * RemoteWebDriver driver = chromeProvider.build(null);
 * 
 * // Usage with listeners
 * List<CDriverListener> listeners = Arrays.asList(
 *     new LoggingDriverListener(),
 *     new ScreenshotDriverListener()
 * );
 * RemoteWebDriver driver = chromeProvider.build(listeners);
 * 
 * // Get browser type
 * CBrowser browser = chromeProvider.getBrowser(); // Returns CBrowser.CHROME
 * 
 * // Get capabilities for debugging
 * Capabilities caps = chromeProvider.getCapabilities();
 * System.out.println("Browser name: " + caps.getBrowserName());
 * }</pre>
 */
public interface CSeleniumProvider extends CDriverEngineProvider {
  /**
   * Builds and initializes a WebDriver instance with optional listeners.
   * <p>
   * This method creates a WebDriver instance based on the current configuration:
   * <ul>
   *   <li>Remote driver if {@code isUseRemoteDriver()} returns true</li>
   *   <li>Test container if {@code isUseTestContainer()} returns true</li>
   *   <li>Local driver otherwise</li>
   * </ul>
   * </p>
   * 
   * <p>
   * The method also handles listener lifecycle callbacks:
   * <ul>
   *   <li>{@code beforeInit()} is called before driver creation</li>
   *   <li>{@code afterInit()} is called after driver creation</li>
   * </ul>
   * </p>
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Build driver without listeners
   * RemoteWebDriver driver = provider.build(null);
   * 
   * // Build driver with custom listeners
   * List<CDriverListener> listeners = Arrays.asList(
   *     new CDriverListener() {
   *         public void beforeInit(Capabilities capabilities) {
   *             System.out.println("Initializing driver with: " + capabilities.getBrowserName());
   *         }
   *         
   *         public void afterInit(CDriverProvider provider, RemoteWebDriver driver) {
   *             System.out.println("Driver initialized: " + driver.getSessionId());
   *         }
   *     }
   * );
   * RemoteWebDriver driver = provider.build(listeners);
   * 
   * // Build driver with logging listener
   * List<CDriverListener> loggingListeners = Collections.singletonList(
   *     new LoggingDriverListener()
   * );
   * RemoteWebDriver driver = provider.build(loggingListeners);
   * }</pre>
   * 
   * @return A fully initialized RemoteWebDriver instance ready for use
   * @throws RuntimeException if driver initialization fails
   */
  default CDriverEngine build() {
    RemoteWebDriver webDriver;
    if (isUseRemoteDriver())
      webDriver = buildRemoteWebDrier();
    else if (isUseTestContainer())
      webDriver = buildTestContainer();
    else
      webDriver = buildLocalDriver();

    return new CSeleniumEngine(webDriver);
  }

  /**
   * Builds a WebDriver instance configured to run in a test container environment.
   * <p>
   * This method creates a WebDriver that connects to a browser running inside a
   * Docker container or similar containerized environment. This is useful for
   * consistent testing environments and CI/CD pipelines.
   * </p>
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Create a test container driver
   * CDriverProvider provider = new ChromeDriverProvider();
   * RemoteWebDriver containerDriver = provider.buildTestContainer();
   * 
   * // Use the container driver for testing
   * containerDriver.get("https://example.com");
   * WebElement element = containerDriver.findElement(By.id("submit"));
   * element.click();
   * 
   * // Container drivers are automatically cleaned up
   * containerDriver.quit();
   * 
   * // Custom container configuration (implementation dependent)
   * // This would be handled by the specific implementation
   * }</pre>
   * 
   * @return RemoteWebDriver instance connected to a containerized browser
   * @throws RuntimeException if container setup or connection fails
   */
  RemoteWebDriver buildTestContainer();

  /**
   * Builds a local WebDriver instance that runs on the current machine.
   * <p>
   * This method creates a WebDriver that launches and controls a browser process
   * directly on the local machine. The browser executable must be available in
   * the system PATH or explicitly configured.
   * </p>
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Create a local Chrome driver
   * CDriverProvider chromeProvider = new ChromeDriverProvider();
   * RemoteWebDriver localDriver = chromeProvider.buildLocalDriver();
   * 
   * // Use local driver for development testing
   * localDriver.manage().window().maximize();
   * localDriver.get("http://localhost:3000");
   * 
   * // Local drivers support debugging
   * localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
   * 
   * // Clean up local driver
   * localDriver.quit();
   * 
   * // Firefox local driver example
   * CDriverProvider firefoxProvider = new FirefoxDriverProvider();
   * RemoteWebDriver firefoxLocal = firefoxProvider.buildLocalDriver();
   * firefoxLocal.get("https://mozilla.org");
   * }</pre>
   * 
   * @return RemoteWebDriver instance controlling a local browser process
   * @throws RuntimeException if local browser cannot be started or driver executable is missing
   */
  RemoteWebDriver buildLocalDriver();

  /**
   * Builds a remote WebDriver instance that connects to a Selenium Grid or remote browser service.
   * <p>
   * This method creates a WebDriver that connects to a remote Selenium server or grid,
   * allowing tests to run on different machines or cloud services. The remote endpoint
   * configuration is typically provided through system properties or configuration files.
   * </p>
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Create a remote driver (assumes grid URL is configured)
   * CDriverProvider provider = new ChromeDriverProvider();
   * RemoteWebDriver remoteDriver = provider.buildRemoteWebDrier();
   * 
   * // Remote drivers work like local ones
   * remoteDriver.get("https://example.com");
   * String title = remoteDriver.getTitle();
   * System.out.println("Page title: " + title);
   * 
   * // Remote driver with capabilities
   * Capabilities caps = provider.getCapabilities();
   * System.out.println("Running on: " + remoteDriver.getCapabilities().getPlatformName());
   * 
   * // Cross-browser testing on grid
   * CDriverProvider firefoxProvider = new FirefoxDriverProvider();
   * RemoteWebDriver firefoxRemote = firefoxProvider.buildRemoteWebDrier();
   * 
   * // Parallel execution (run in separate threads)
   * CompletableFuture<Void> chromeTest = CompletableFuture.runAsync(() -> {
   *     RemoteWebDriver chrome = new ChromeDriverProvider().buildRemoteWebDrier();
   *     chrome.get("https://example.com");
   *     chrome.quit();
   * });
   * 
   * remoteDriver.quit();
   * }</pre>
   * 
   * @return RemoteWebDriver instance connected to a remote Selenium server
   * @throws RuntimeException if remote connection fails or grid is unavailable
   */
  RemoteWebDriver buildRemoteWebDrier();

  /**
   * Gets the browser type that this provider supports.
   * <p>
   * This method returns the specific browser enum value that identifies which
   * browser this provider is designed to work with. This is useful for
   * determining provider capabilities and for driver selection logic.
   * </p>
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Get browser type from provider
   * CDriverProvider chromeProvider = new ChromeDriverProvider();
   * CBrowser browser = chromeProvider.getBrowser();
   * System.out.println("Provider browser: " + browser); // CBrowser.CHROME
   * 
   * // Use browser type for conditional logic
   * if (browser == CBrowser.CHROME) {
   *     System.out.println("Using Chrome-specific configuration");
   * } else if (browser == CBrowser.FIREFOX) {
   *     System.out.println("Using Firefox-specific configuration");
   * }
   * 
   * // Browser-specific test execution
   * switch (provider.getBrowser()) {
   *     case CHROME:
   *         // Chrome-specific test setup
   *         break;
   *     case FIREFOX:
   *         // Firefox-specific test setup  
   *         break;
   *     case EDGE:
   *         // Edge-specific test setup
   *         break;
   * }
   * 
   * // Filter providers by browser type
   * List<CDriverProvider> providers = getAllProviders();
   * List<CDriverProvider> chromeProviders = providers.stream()
   *     .filter(p -> p.getBrowser() == CBrowser.CHROME)
   *     .collect(Collectors.toList());
   * }</pre>
   * 
   * @return CBrowser enum value indicating the supported browser type
   */
  CBrowser getBrowser();
}

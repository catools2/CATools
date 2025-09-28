package org.catools.web.drivers.providers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.catools.web.config.CGridConfigs;
import org.catools.web.drivers.CDriverProvider;
import org.catools.web.drivers.config.CEdgeConfigs;
import org.catools.web.drivers.config.CWebDriverManagerConfigs;
import org.catools.web.enums.CBrowser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.Connection;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.Objects;
import java.util.logging.Level;

import static org.catools.web.config.CGridConfigs.getHubURL;

/**
 * Microsoft Edge WebDriver provider implementation that creates and configures Edge WebDriver instances.
 * 
 * <p>This class provides functionality to create Edge WebDriver instances in different modes:
 * <ul>
 * <li>Local Edge driver execution</li>
 * <li>Remote WebDriver execution via Selenium Grid</li>
 * <li>Test container execution for isolated testing</li>
 * </ul>
 * 
 * <p>The provider automatically configures Edge options based on system configurations and supports
 * both headless and headed execution modes.
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Basic usage with default configuration
 * CEdgeDriverProvider provider = new CEdgeDriverProvider();
 * RemoteWebDriver driver = provider.buildLocalDriver();
 * 
 * // Custom configuration with additional arguments
 * CEdgeDriverProvider provider = new CEdgeDriverProvider();
 * provider.addArguments(Arrays.asList("--disable-web-security", "--allow-running-insecure-content"));
 * provider.setPageLoadStrategy(PageLoadStrategy.EAGER);
 * RemoteWebDriver driver = provider.buildLocalDriver();
 * 
 * // Using with test containers for isolated testing
 * CEdgeDriverProvider provider = new CEdgeDriverProvider();
 * RemoteWebDriver driver = provider.buildTestContainer();
 * }</pre>
 * 
 * @author CA Tools Team
 * @version 1.0
 * @since 1.0
 */
public class CEdgeDriverProvider implements CDriverProvider {

  static {
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(CGridConfigs.getLogLevel());
    java.util.logging.Logger.getLogger(Connection.class.getPackage().getName()).setLevel(Level.SEVERE);
    System.setProperty("webdriver.http.factory", "jdk-http-client");
    if (CWebDriverManagerConfigs.isEnabled()) {
      WebDriverManager.edgedriver().setup();
    }
  }

  private EdgeOptions options = new EdgeOptions();

  /**
   * Constructs a new CEdgeDriverProvider with default Edge configurations.
   * 
   * <p>The constructor automatically applies the following configurations:
   * <ul>
   * <li>Sets the Edge binary path if configured via {@link CEdgeConfigs#getBinaryPath()}</li>
   * <li>Adds default arguments from {@link CEdgeConfigs#getDefaultArguments()}</li>
   * <li>Sets the page load strategy from {@link CEdgeConfigs#getPageLoadStrategy()}</li>
   * <li>Enables headless mode if configured via {@link CEdgeConfigs#isInHeadLessMode()}</li>
   * </ul>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * // Creates a provider with default system configurations
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * 
   * // The provider is now ready to create WebDriver instances
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   */
  public CEdgeDriverProvider() {
    if (StringUtils.isNotBlank(CEdgeConfigs.getBinaryPath())) {
      setBinary(CEdgeConfigs.getBinaryPath());
    }
    addArguments(CEdgeConfigs.getDefaultArguments());
    setPageLoadStrategy(CEdgeConfigs.getPageLoadStrategy());

    if (CEdgeConfigs.isInHeadLessMode()) {
      addArguments(CEdgeConfigs.getHeadLessArguments());
    }
  }

  /**
   * Returns the configured Edge capabilities with download functionality enabled.
   * 
   * <p>This method prepares and returns the {@link Capabilities} object that will be used
   * to configure the Edge WebDriver instance. The capabilities include all previously
   * configured options such as arguments, binary path, and page load strategy.
   * 
   * <p>The method automatically enables downloads by calling {@code setEnableDownloads(true)}
   * on the Edge options before returning the capabilities.
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * provider.addArguments(Arrays.asList("--disable-notifications"));
   * 
   * Capabilities caps = provider.getCapabilities();
   * // caps now contains all configured options including download enablement
   * }</pre>
   * 
   * @return the configured Edge capabilities with downloads enabled
   */
  @Override
  public Capabilities getCapabilities() {
    options.setEnableDownloads(true);
    return options;
  }

  /**
   * Creates and returns a Edge WebDriver instance running in a Docker test container.
   * 
   * <p>This method creates an isolated Edge WebDriver instance using Testcontainers with
   * the official Selenium Edge standalone image. The container is automatically started
   * and configured with the capabilities from {@link #getCapabilities()}.
   * 
   * <p>This approach is ideal for:
   * <ul>
   * <li>CI/CD environments where consistent browser versions are required</li>
   * <li>Parallel test execution with complete isolation</li>
   * <li>Testing without installing Edge browser locally</li>
   * <li>Ensuring reproducible test results across different environments</li>
   * </ul>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * 
   * // Creates a containerized Edge instance
   * RemoteWebDriver driver = provider.buildTestContainer();
   * 
   * try {
   *     driver.get("https://example.com");
   *     // Perform your tests...
   * } finally {
   *     driver.quit(); // This also stops the container
   * }
   * }</pre>
   * 
   * @return a RemoteWebDriver instance connected to a containerized Edge browser
   * @throws RuntimeException if the container fails to start or connect
   */
  @Override
  public RemoteWebDriver buildTestContainer() {
    BrowserWebDriverContainer<?> driverContainer = new BrowserWebDriverContainer<>("selenium/standalone-edge:latest");
    driverContainer.withCapabilities(getCapabilities());
    driverContainer.start();
    return new RemoteWebDriver(driverContainer.getSeleniumAddress(), getCapabilities());
  }

  /**
   * Creates and returns a local Edge WebDriver instance.
   * 
   * <p>This method creates a local Edge WebDriver instance using the configured options.
   * The Edge browser must be installed on the local machine, and the Edge driver binary
   * should be available in the system PATH or configured via WebDriverManager.
   * 
   * <p>The method calls {@link #getCapabilities()} to ensure all configurations are
   * applied before creating the driver instance.
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * provider.addArguments(Arrays.asList("--start-maximized", "--disable-infobars"));
   * 
   * // Creates a local Edge driver instance
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * 
   * try {
   *     driver.get("https://example.com");
   *     // Perform your tests...
   * } finally {
   *     driver.quit();
   * }
   * }</pre>
   * 
   * @return a local EdgeDriver instance configured with the specified options
   * @throws IllegalStateException if Edge browser is not installed or driver binary is not found
   * @throws WebDriverException if the driver fails to start
   */
  @Override
  public RemoteWebDriver buildLocalDriver() {
    getCapabilities();
    return new EdgeDriver(options);
  }

  /**
   * Creates and returns a remote Edge WebDriver instance connected to a Selenium Grid hub.
   * 
   * <p>This method creates a RemoteWebDriver instance that connects to a Selenium Grid hub
   * to execute tests on a remote Edge browser. The hub URL is obtained from the grid
   * configuration via {@link CGridConfigs#getHubURL()}.
   * 
   * <p>This approach is suitable for:
   * <ul>
   * <li>Distributed test execution across multiple machines</li>
   * <li>Cross-platform testing without local browser installation</li>
   * <li>Scaling test execution in CI/CD environments</li>
   * <li>Running tests on different browser versions simultaneously</li>
   * </ul>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * // Ensure grid hub URL is configured
   * // System.setProperty("grid.hub.url", "http://selenium-hub:4444/wd/hub");
   * 
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * provider.addArguments(Arrays.asList("--no-sandbox", "--disable-dev-shm-usage"));
   * 
   * // Creates a remote Edge driver instance
   * RemoteWebDriver driver = provider.buildRemoteWebDrier();
   * 
   * try {
   *     driver.get("https://example.com");
   *     // Perform your tests...
   * } finally {
   *     driver.quit();
   * }
   * }</pre>
   * 
   * @return a RemoteWebDriver instance connected to the configured Selenium Grid hub
   * @throws NullPointerException if the hub URL is not configured or is null
   * @throws WebDriverException if connection to the hub fails or no Edge nodes are available
   */
  @Override
  public RemoteWebDriver buildRemoteWebDrier() {
    return new RemoteWebDriver(Objects.requireNonNull(getHubURL()), getCapabilities());
  }

  /**
   * Returns the browser type that this provider handles.
   * 
   * <p>This method returns {@link CBrowser#EDGE} to identify that this provider
   * is specifically designed for Microsoft Edge browser instances.
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * CBrowser browserType = provider.getBrowser();
   * 
   * if (browserType == CBrowser.EDGE) {
   *     System.out.println("This provider handles Edge browsers");
   * }
   * }</pre>
   * 
   * @return {@link CBrowser#EDGE} indicating this provider handles Edge browsers
   */
  @Override
  public CBrowser getBrowser() {
    return CBrowser.EDGE;
  }

  /**
   * Sets the page load strategy for the Edge browser.
   * 
   * <p>The page load strategy determines when WebDriver considers a page to be loaded.
   * This affects the behavior of navigation methods like {@code driver.get()} and
   * {@code driver.navigate().to()}.
   * 
   * <p>Available strategies:
   * <ul>
   * <li>{@link PageLoadStrategy#NORMAL} - Waits for all resources to load (default)</li>
   * <li>{@link PageLoadStrategy#EAGER} - Waits for initial HTML document to be loaded</li>
   * <li>{@link PageLoadStrategy#NONE} - Does not wait for any loading to complete</li>
   * </ul>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * 
   * // Set eager loading for faster test execution
   * provider.setPageLoadStrategy(PageLoadStrategy.EAGER);
   * 
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * driver.get("https://example.com"); // Will not wait for all resources
   * }</pre>
   * 
   * @param pageLoadStrategy the page load strategy to use
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if pageLoadStrategy is null
   */
  public CEdgeDriverProvider setPageLoadStrategy(PageLoadStrategy pageLoadStrategy) {
    options.setPageLoadStrategy(pageLoadStrategy);
    return this;
  }

  /**
   * Adds command-line arguments to the Edge browser configuration.
   * 
   * <p>This method allows you to customize the Edge browser behavior by adding
   * command-line arguments. Arguments are added to the existing set of arguments
   * and will be applied when the WebDriver instance is created.
   * 
   * <p>Common Edge arguments include:
   * <ul>
   * <li>{@code --headless} - Run in headless mode (no GUI)</li>
   * <li>{@code --start-maximized} - Start with maximized window</li>
   * <li>{@code --disable-web-security} - Disable web security (for testing)</li>
   * <li>{@code --no-sandbox} - Disable sandbox (useful in containers)</li>
   * <li>{@code --disable-dev-shm-usage} - Disable /dev/shm usage (useful in containers)</li>
   * <li>{@code --remote-allow-origins=*} - Allow remote origins (for remote debugging)</li>
   * </ul>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * 
   * // Add multiple arguments for container-friendly execution
   * List<String> args = Arrays.asList(
   *     "--no-sandbox",
   *     "--disable-dev-shm-usage",
   *     "--disable-gpu",
   *     "--window-size=1920,1080"
   * );
   * provider.addArguments(args);
   * 
   * // Method chaining is supported
   * provider.addArguments(Arrays.asList("--disable-notifications"))
   *         .setPageLoadStrategy(PageLoadStrategy.EAGER);
   * 
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   * 
   * @param args an iterable collection of command-line arguments to add
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if args is null or contains null elements
   */
  public CEdgeDriverProvider addArguments(Iterable<String> args) {
    for (String arg : args) {
      options.addArguments(arg);
    }
    return this;
  }

  /**
   * Sets the path to the Edge browser binary executable.
   * 
   * <p>This method allows you to specify a custom path to the Edge browser binary
   * if it's not installed in the standard location or if you want to use a specific
   * version of Edge. This is particularly useful when:
   * <ul>
   * <li>Testing with different Edge versions</li>
   * <li>Using portable Edge installations</li>
   * <li>Working in environments where Edge is not in the standard location</li>
   * <li>Using Edge Dev, Beta, or Canary versions</li>
   * </ul>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * CEdgeDriverProvider provider = new CEdgeDriverProvider();
   * 
   * // Windows path example
   * provider.setBinary("C:\\Program Files (x86)\\Microsoft\\Edge Dev\\Application\\msedge.exe");
   * 
   * // macOS path example
   * provider.setBinary("/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge");
   * 
   * // Linux path example
   * provider.setBinary("/usr/bin/microsoft-edge");
   * 
   * // Method chaining is supported
   * provider.setBinary("/custom/path/to/edge")
   *         .addArguments(Arrays.asList("--start-maximized"));
   * 
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   * 
   * @param path the absolute path to the Edge browser binary
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if path is null or empty
   */
  public CEdgeDriverProvider setBinary(String path) {
    options.setBinary(path);
    return this;
  }
}

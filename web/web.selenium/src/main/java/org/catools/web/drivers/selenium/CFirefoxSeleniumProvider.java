package org.catools.web.drivers.selenium;

import static org.catools.web.config.CGridConfigs.getHubURL;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Objects;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.catools.web.config.CGridConfigs;
import org.catools.web.config.selenium.CFireFoxConfigs;
import org.catools.web.config.selenium.CWebDriverManagerConfigs;
import org.catools.web.enums.CBrowser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.devtools.Connection;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

/**
 * Firefox WebDriver provider implementation that creates and configures Firefox WebDriver
 * instances. This class supports various Firefox driver configurations including local, remote, and
 * container-based execution.
 *
 * <p>The provider automatically configures Firefox with sensible defaults and allows customization
 * through various configuration methods. It supports headless mode, custom binary paths, arguments,
 * and profile settings.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
 * provider.setBinary("/path/to/firefox")
 *         .addArguments(Arrays.asList("--disable-web-security", "--no-sandbox"))
 *         .setPageLoadStrategy(PageLoadStrategy.EAGER);
 *
 * RemoteWebDriver driver = provider.buildLocalDriver();
 * }</pre>
 *
 * @see CSeleniumProvider
 * @see FirefoxDriver
 * @see FirefoxOptions
 * @since 1.0
 */
public class CFirefoxSeleniumProvider implements CSeleniumProvider {
  static {
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(CGridConfigs.getLogLevel());
    java.util.logging.Logger.getLogger(Connection.class.getPackage().getName())
        .setLevel(Level.SEVERE);
    if (CWebDriverManagerConfigs.isEnabled()) {
      WebDriverManager.firefoxdriver().setup();
    }
  }

  private FirefoxOptions options = new FirefoxOptions();
  private FirefoxProfile profile = getFirefoxProfile(options);

  /**
   * Creates a new Firefox driver provider with default configuration.
   *
   * <p>Initializes the provider with the following default settings:
   *
   * <ul>
   *   <li>Custom binary path if configured
   *   <li>Default Firefox arguments from CFireFoxConfigs
   *   <li>Page load strategy from configuration
   *   <li>PDF opening in new tabs enabled
   *   <li>Headless mode arguments if headless mode is enabled
   * </ul>
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   * // Provider is now configured with defaults from configuration files
   * }</pre>
   */
  public CFirefoxSeleniumProvider() {
    if (StringUtils.isNotBlank(CFireFoxConfigs.getBinaryPath())) {
      setBinary(CFireFoxConfigs.getBinaryPath());
    }
    addArguments(CFireFoxConfigs.getDefaultArguments());
    setPageLoadStrategy(CFireFoxConfigs.getPageLoadStrategy());
    setOpenPdfInNewTab(true);

    if (CFireFoxConfigs.isInHeadLessMode()) {
      addArguments(CFireFoxConfigs.getHeadLessArguments());
    }
  }

  /**
   * Returns the configured Firefox capabilities for WebDriver initialization.
   *
   * <p>This method finalizes the Firefox options by enabling downloads and returns the complete
   * capabilities object that can be used to create WebDriver instances.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   * Capabilities caps = provider.getCapabilities();
   *
   * // Use capabilities with RemoteWebDriver
   * RemoteWebDriver driver = new RemoteWebDriver(hubUrl, caps);
   * }</pre>
   *
   * @return the configured Firefox capabilities with downloads enabled
   */
  public Capabilities getCapabilities() {
    options.setEnableDownloads(true);
    return options;
  }

  /**
   * Creates a Firefox WebDriver instance running in a Docker container using Testcontainers.
   *
   * <p>This method uses the selenium/standalone-firefox:latest Docker image to create an isolated
   * Firefox browser instance. The container is automatically started and configured with the
   * current capabilities.
   *
   * <p>Benefits of container-based testing:
   *
   * <ul>
   *   <li>Consistent browser environment across different machines
   *   <li>Isolation from host system configuration
   *   <li>Easy cleanup and resource management
   *   <li>Parallel test execution support
   * </ul>
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   * RemoteWebDriver driver = provider.buildTestContainer();
   *
   * // Use driver for testing
   * driver.get("https://example.com");
   *
   * // Container is automatically cleaned up when driver quits
   * driver.quit();
   * }</pre>
   *
   * @return a RemoteWebDriver instance connected to the Firefox container
   * @throws RuntimeException if the container fails to start
   */
  @Override
  public RemoteWebDriver buildTestContainer() {
    BrowserWebDriverContainer<?> driverContainer =
        new BrowserWebDriverContainer<>("selenium/standalone-firefox:latest");
    driverContainer.withCapabilities(getCapabilities());
    driverContainer.start();
    return new RemoteWebDriver(driverContainer.getSeleniumAddress(), getCapabilities());
  }

  /**
   * Creates a local Firefox WebDriver instance using the locally installed Firefox browser.
   *
   * <p>This method creates a Firefox driver that runs on the local machine using the Firefox
   * browser installed on the system. The driver is configured with all the options and profile
   * settings defined in this provider.
   *
   * <p>Prerequisites:
   *
   * <ul>
   *   <li>Firefox browser must be installed on the system
   *   <li>Firefox driver (geckodriver) must be available in PATH or configured via WebDriverManager
   *   <li>Sufficient system resources for browser execution
   * </ul>
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   * provider.setBinary("/usr/bin/firefox")
   *         .addArguments(Arrays.asList("--width=1920", "--height=1080"));
   *
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * driver.get("https://example.com");
   * driver.quit();
   * }</pre>
   *
   * @return a FirefoxDriver instance configured with the current options and profile
   * @throws org.openqa.selenium.WebDriverException if Firefox browser or driver is not found
   */
  @Override
  public RemoteWebDriver buildLocalDriver() {
    getCapabilities();
    return new FirefoxDriver(options.setProfile(profile));
  }

  /**
   * Creates a remote Firefox WebDriver instance connected to a Selenium Grid hub.
   *
   * <p>This method creates a RemoteWebDriver that connects to a Selenium Grid hub to execute tests
   * on remote Firefox browser instances. The hub URL is obtained from the grid configuration.
   *
   * <p>Use cases for remote execution:
   *
   * <ul>
   *   <li>Distributed testing across multiple machines
   *   <li>Testing on different operating systems
   *   <li>Scaling test execution
   *   <li>Cloud-based testing services
   * </ul>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Configure grid hub URL in CGridConfigs
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   * RemoteWebDriver driver = provider.buildRemoteWebDrier();
   *
   * // Driver now connects to remote Firefox instance
   * driver.get("https://example.com");
   * driver.quit();
   * }</pre>
   *
   * @return a RemoteWebDriver instance connected to the Selenium Grid hub
   * @throws NullPointerException if hub URL is not configured
   * @throws org.openqa.selenium.WebDriverException if connection to hub fails
   */
  @Override
  public RemoteWebDriver buildRemoteWebDrier() {
    return new RemoteWebDriver(Objects.requireNonNull(getHubURL()), getCapabilities());
  }

  /**
   * Returns the browser type supported by this provider.
   *
   * <p>This method always returns {@link CBrowser#FIREFOX} to identify that this provider creates
   * Firefox WebDriver instances.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   * CBrowser browser = provider.getBrowser();
   * assert browser == CBrowser.FIREFOX;
   * }</pre>
   *
   * @return CBrowser.FIREFOX indicating this provider creates Firefox drivers
   */
  @Override
  public CBrowser getBrowser() {
    return CBrowser.FIREFOX;
  }

  /**
   * Sets the path to the Firefox binary executable.
   *
   * <p>This method allows you to specify a custom Firefox binary location instead of using the
   * default system installation. This is useful when you have multiple Firefox versions installed
   * or when Firefox is installed in a non-standard location.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   *
   * // Use custom Firefox installation
   * provider.setBinary("/opt/firefox/firefox");
   *
   * // Or use Firefox Developer Edition
   * provider.setBinary("/Applications/Firefox Developer Edition.app/Contents/MacOS/firefox");
   *
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   *
   * @param path the absolute path to the Firefox binary executable
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if the path is null or points to a non-executable file
   */
  public CFirefoxSeleniumProvider setBinary(String path) {
    options.setBinary(path);
    return this;
  }

  /**
   * Adds command-line arguments to the Firefox browser instance.
   *
   * <p>This method allows you to pass additional command-line arguments to Firefox when it starts.
   * These arguments can control various browser behaviors, performance settings, security options,
   * and feature flags.
   *
   * <p>Common Firefox arguments:
   *
   * <ul>
   *   <li>{@code --headless} - Run Firefox in headless mode
   *   <li>{@code --width=1920} - Set browser window width
   *   <li>{@code --height=1080} - Set browser window height
   *   <li>{@code --disable-web-security} - Disable web security (for testing)
   *   <li>{@code --no-sandbox} - Disable sandbox mode
   *   <li>{@code --disable-dev-shm-usage} - Disable /dev/shm usage
   * </ul>
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   *
   * List<String> args = Arrays.asList(
   *     "--width=1920",
   *     "--height=1080",
   *     "--disable-web-security",
   *     "--no-sandbox"
   * );
   * provider.addArguments(args);
   *
   * // Or add individual arguments
   * provider.addArguments(Collections.singletonList("--disable-extensions"));
   * }</pre>
   *
   * @param args an iterable collection of command-line arguments to add
   * @return this provider instance for method chaining
   * @throws NullPointerException if args is null
   */
  public CFirefoxSeleniumProvider addArguments(Iterable<String> args) {
    for (String arg : args) {
      options.addArguments(arg);
    }
    return this;
  }

  /**
   * Configures whether PDF files should open in a new tab or be downloaded.
   *
   * <p>This method controls Firefox's handling of PDF files by setting the {@code pdfjs.disabled}
   * preference. When set to {@code true} (default), PDF files will open in a new tab using
   * Firefox's built-in PDF viewer. When set to {@code false}, PDF files will be downloaded instead.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   *
   * // PDF files will open in new tabs (default behavior)
   * provider.setOpenPdfInNewTab(true);
   *
   * // PDF files will be downloaded
   * provider.setOpenPdfInNewTab(false);
   *
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * driver.get("https://example.com/document.pdf");
   * }</pre>
   *
   * @param value {@code true} to open PDFs in new tabs, {@code false} to download them
   * @return this provider instance for method chaining
   */
  public CFirefoxSeleniumProvider setOpenPdfInNewTab(boolean value) {
    profile.setPreference("pdfjs.disabled", value);
    return this;
  }

  /**
   * Sets the page load strategy for the Firefox browser.
   *
   * <p>The page load strategy determines when WebDriver considers a page load to be complete. This
   * affects when control returns to your test code after calling {@code driver.get()} or {@code
   * driver.navigate().to()}.
   *
   * <p>Available strategies:
   *
   * <ul>
   *   <li>{@link PageLoadStrategy#NORMAL} - Wait for all resources to load (default)
   *   <li>{@link PageLoadStrategy#EAGER} - Wait for DOMContentLoaded event only
   *   <li>{@link PageLoadStrategy#NONE} - Return immediately after initial page load
   * </ul>
   *
   * <p>Example:
   *
   * <pre>{@code
   * CFireFoxDriverProvider provider = new CFireFoxDriverProvider();
   *
   * // Wait for all resources including images, stylesheets, etc.
   * provider.setPageLoadStrategy(PageLoadStrategy.NORMAL);
   *
   * // Wait only for DOM to be ready (faster for testing)
   * provider.setPageLoadStrategy(PageLoadStrategy.EAGER);
   *
   * // Return immediately (use with explicit waits)
   * provider.setPageLoadStrategy(PageLoadStrategy.NONE);
   *
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   *
   * @param pageLoadStrategy the page load strategy to use
   * @return this provider instance for method chaining
   * @throws NullPointerException if pageLoadStrategy is null
   */
  public CFirefoxSeleniumProvider setPageLoadStrategy(PageLoadStrategy pageLoadStrategy) {
    options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, pageLoadStrategy.toString());
    return this;
  }

  private FirefoxProfile getFirefoxProfile(FirefoxOptions options) {
    FirefoxProfile profile = new FirefoxProfile();
    profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
    profile.setPreference("startup.homepage_welcome_url.additional", "about:blank");

    options.setProfile(profile);

    profile.setAcceptUntrustedCertificates(true);
    profile.setAlwaysLoadNoFocusLib(true);

    options.setLogLevel(FirefoxDriverLogLevel.FATAL);
    return profile;
  }
}

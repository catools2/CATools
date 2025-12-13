package org.catools.web.drivers.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.interfaces.CMap;
import org.catools.web.config.CGridConfigs;
import org.catools.web.config.selenium.CChromeConfigs;
import org.catools.web.config.selenium.CWebDriverManagerConfigs;
import org.catools.web.enums.CBrowser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.devtools.Connection;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import static org.catools.web.config.CGridConfigs.getHubURL;

/**
 * Chrome WebDriver provider implementation that supports local, remote, and containerized Chrome browser instances.
 * 
 * <p>This provider offers comprehensive Chrome browser configuration options including headless mode,
 * mobile device emulation, plugin management, and custom Chrome binary paths. It supports three execution modes:
 * <ul>
 *   <li>Local driver execution</li>
 *   <li>Remote WebDriver grid execution</li>
 *   <li>Docker container-based execution using Testcontainers</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Basic Chrome driver with default configuration
 * CChromeDriverProvider provider = new CChromeDriverProvider();
 * RemoteWebDriver driver = provider.buildLocalDriver();
 * 
 * // Chrome driver with mobile emulation
 * CChromeDriverProvider mobileProvider = new CChromeDriverProvider()
 *     .setDeviceEmulation("iPhone 12")
 *     .setHeadless();
 * RemoteWebDriver mobileDriver = mobileProvider.buildLocalDriver();
 * 
 * // Chrome driver with custom binary and arguments
 * CChromeDriverProvider customProvider = new CChromeDriverProvider()
 *     .setBinary("/usr/bin/google-chrome-beta")
 *     .addArguments(Arrays.asList("--disable-web-security", "--user-data-dir=/tmp"));
 * RemoteWebDriver customDriver = customProvider.buildLocalDriver();
 * }</pre>
 * 
 * @author CATools Team
 * @since 1.0
 */
public class CChromeSeleniumProvider implements CSeleniumProvider {
  static {
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(CGridConfigs.getLogLevel());
    java.util.logging.Logger.getLogger(Connection.class.getPackage().getName()).setLevel(Level.SEVERE);
    System.setProperty("webdriver.http.factory", "jdk-http-client");
    if (CWebDriverManagerConfigs.isEnabled()) {
      WebDriverManager.chromedriver().setup();
    }
  }

  private CMap<String, Object> prefs = new CHashMap<>();
  private CList<CMap<String, Object>> plugins = new CList<>();
  private ChromeOptions options = new ChromeOptions();

  /**
   * Constructs a new Chrome driver provider with default configuration settings.
   * 
   * <p>The constructor automatically applies default configurations from {@link CChromeConfigs}
   * including binary path, arguments, plugins, page load strategy, and mobile emulation settings.
   * If headless mode is enabled in configuration, it will be automatically applied.
   * 
   * <p><strong>Default behavior:</strong>
   * <ul>
   *   <li>Sets custom Chrome binary path if configured</li>
   *   <li>Applies default Chrome arguments from configuration</li>
   *   <li>Enables/disables plugins based on configuration</li>
   *   <li>Sets page load strategy from configuration</li>
   *   <li>Enables PDF opening in new tabs</li>
   *   <li>Applies mobile device emulation if configured</li>
   *   <li>Enables headless mode if configured</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * // Create provider with default configuration
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Configuration is automatically loaded from CChromeConfigs
   * // No additional setup needed for basic usage
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   */
  public CChromeSeleniumProvider() {
    if (StringUtils.isNotBlank(CChromeConfigs.getBinaryPath())) {
      setBinary(CChromeConfigs.getBinaryPath());
    }
    addArguments(CChromeConfigs.getDefaultArguments());
    addPluginsToEnable(CChromeConfigs.getPluginsToEnable());
    addPluginsToDisable(CChromeConfigs.getPluginsToDisable());
    setPageLoadStrategy(CChromeConfigs.getPageLoadStrategy());
    setOpenPdfInNewTab(true);

    prefs.put("plugins.plugins_list", plugins);

    if (StringUtils.isNotBlank(CChromeConfigs.getChromeMobileEmulationDeviceName())) {
      setDeviceEmulation(CChromeConfigs.getChromeMobileEmulationDeviceName());
    }

    if (CChromeConfigs.isInHeadLessMode())
      setHeadless();
  }

  private static ChromeDriverService buildDefaultService() {
    return new ChromeDriverService.Builder()
        .withLogLevel(ChromiumDriverLogLevel.fromLevel(CGridConfigs.getLogLevel())).build();
  }

  /**
   * Retrieves the configured Chrome capabilities for WebDriver initialization.
   * 
   * <p>This method finalizes all configuration options and returns a {@link Capabilities} object
   * that includes all preferences, experimental options, and security settings. The capabilities
   * are automatically configured with:
   * <ul>
   *   <li>Custom preferences and plugin configurations</li>
   *   <li>Download enablement</li>
   *   <li>Insecure certificate acceptance</li>
   *   <li>Chrome DevTools Protocol (CDP) support</li>
   *   <li>BiDi protocol support for advanced automation</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider()
   *     .setHeadless()
   *     .addArguments(Arrays.asList("--disable-notifications"));
   * 
   * Capabilities caps = provider.getCapabilities();
   * 
   * // Use capabilities with RemoteWebDriver
   * RemoteWebDriver driver = new RemoteWebDriver(hubUrl, caps);
   * }</pre>
   * 
   * @return {@link Capabilities} object containing all configured Chrome options and preferences
   */
  public Capabilities getCapabilities() {
    setSystemProperties();
    options.setExperimentalOption("prefs", prefs);

    options.setEnableDownloads(true);
    options.setAcceptInsecureCerts(true);
    options.enableBiDi();
    options.setCapability("se:cdpEnabled", true);

    return options;
  }

  /**
   * Builds and returns a Chrome WebDriver instance running in a Docker container.
   *
   * <p>This method creates a containerized Chrome browser using Testcontainers with the latest
   * Selenium standalone Chrome image. The container is automatically started and configured
   * with the current provider's capabilities. This approach provides isolation and consistency
   * across different environments.
   *
   * <p><strong>Benefits of containerized execution:</strong>
   * <ul>
   *   <li>Environment isolation and consistency</li>
   *   <li>No need for local Chrome installation</li>
   *   <li>Automatic cleanup when tests complete</li>
   *   <li>Parallel execution support</li>
   * </ul>
   *
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider()
   *     .setHeadless()
   *     .addArguments(Arrays.asList("--no-sandbox"));
   *
   * // Creates Chrome in Docker container
   * RemoteWebDriver containerDriver = provider.buildTestContainer();
   *
   * try {
   *     containerDriver.get("https://example.com");
   *     // Perform test actions
   * } finally {
   *     containerDriver.quit(); // Container is automatically cleaned up
   * }
   * }</pre>
   *
   * @return {@link RemoteWebDriver} instance connected to containerized Chrome browser
   * @throws RuntimeException if container fails to start or connect
   */
  @Override
  public RemoteWebDriver buildTestContainer() {
    BrowserWebDriverContainer<?> driverContainer = new BrowserWebDriverContainer<>("selenium/standalone-chrome:latest");
    driverContainer.withCapabilities(getCapabilities());
    driverContainer.start();
    return new RemoteWebDriver(driverContainer.getSeleniumAddress(), getCapabilities());
  }

  /**
   * Builds and returns a local Chrome WebDriver instance.
   * 
   * <p>This method creates a Chrome WebDriver that connects directly to a locally installed
   * Chrome browser. The driver uses the configured Chrome binary path (if specified) or
   * the system's default Chrome installation. A Chrome driver service is automatically
   * configured with appropriate logging levels.
   * 
   * <p><strong>Prerequisites:</strong>
   * <ul>
   *   <li>Chrome browser installed on the local machine</li>
   *   <li>ChromeDriver executable available (handled by WebDriverManager if enabled)</li>
   *   <li>Sufficient system permissions to launch Chrome processes</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider()
   *     .setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
   *     .addArguments(Arrays.asList("--start-maximized"))
   *     .setPageLoadStrategy(PageLoadStrategy.EAGER);
   * 
   * // Creates local Chrome instance
   * RemoteWebDriver localDriver = provider.buildLocalDriver();
   * 
   * try {
   *     localDriver.get("https://example.com");
   *     // Perform test actions with local browser
   * } finally {
   *     localDriver.quit();
   * }
   * }</pre>
   * 
   * @return {@link RemoteWebDriver} instance connected to local Chrome browser
   * @throws org.openqa.selenium.WebDriverException if Chrome cannot be launched or ChromeDriver is not found
   */
  @Override
  public RemoteWebDriver buildLocalDriver() {
    getCapabilities();
    return new ChromeDriver(buildDefaultService(), options);
  }

  /**
   * Builds and returns a remote Chrome WebDriver instance connected to a Selenium Grid hub.
   * 
   * <p>This method creates a RemoteWebDriver that connects to a Selenium Grid hub URL
   * configured via {@link CGridConfigs}. The remote execution allows for distributed
   * testing across multiple machines and browsers, enabling scalable test execution.
   * 
   * <p><strong>Configuration requirements:</strong>
   * <ul>
   *   <li>Selenium Grid hub must be running and accessible</li>
   *   <li>Chrome nodes must be registered with the hub</li>
   *   <li>Hub URL must be properly configured in CGridConfigs</li>
   *   <li>Network connectivity between client and hub</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * // Ensure grid hub URL is configured
   * // Example: http://selenium-hub:4444/wd/hub
   * 
   * CChromeDriverProvider provider = new CChromeDriverProvider()
   *     .setHeadless()
   *     .addArguments(Arrays.asList("--disable-dev-shm-usage"));
   * 
   * // Connects to remote Selenium Grid
   * RemoteWebDriver gridDriver = provider.buildRemoteWebDrier();
   * 
   * try {
   *     gridDriver.get("https://example.com");
   *     // Test runs on remote Chrome node
   * } finally {
   *     gridDriver.quit();
   * }
   * }</pre>
   * 
   * @return {@link RemoteWebDriver} instance connected to remote Selenium Grid Chrome node
   * @throws NullPointerException if hub URL is not configured
   * @throws org.openqa.selenium.WebDriverException if connection to hub fails or no Chrome nodes available
   */
  @Override
  public RemoteWebDriver buildRemoteWebDrier() {
    return new RemoteWebDriver(Objects.requireNonNull(getHubURL()), getCapabilities());
  }

  /**
   * Returns the browser type handled by this provider.
   * 
   * <p>This method identifies the specific browser type that this provider manages,
   * which is always {@link CBrowser#CHROME} for this implementation. This information
   * is used by the framework for browser-specific configuration and routing.
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * CBrowser browserType = provider.getBrowser();
   * 
   * assert browserType == CBrowser.CHROME;
   * 
   * // Can be used for conditional logic
   * if (provider.getBrowser() == CBrowser.CHROME) {
   *     // Chrome-specific test logic
   * }
   * }</pre>
   * 
   * @return {@link CBrowser#CHROME} indicating this provider handles Chrome browser
   */
  @Override
  public CBrowser getBrowser() {
    return CBrowser.CHROME;
  }

  /**
   * Sets the path to a custom Chrome binary executable.
   * 
   * <p>This method allows specification of a custom Chrome browser binary instead of
   * using the system's default installation. This is useful for testing with different
   * Chrome versions, beta releases, or when Chrome is installed in a non-standard location.
   * 
   * <p><strong>Common use cases:</strong>
   * <ul>
   *   <li>Testing with Chrome Beta or Dev channels</li>
   *   <li>Using portable Chrome installations</li>
   *   <li>Custom Chrome builds with specific flags</li>
   *   <li>Docker containers with Chrome in non-standard paths</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Windows example
   * provider.setBinary("C:\\Program Files\\Google\\Chrome Beta\\chrome.exe");
   * 
   * // macOS example
   * provider.setBinary("/Applications/Google Chrome Beta.app/Contents/MacOS/Google Chrome Beta");
   * 
   * // Linux example
   * provider.setBinary("/usr/bin/google-chrome-beta");
   * 
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   * 
   * @param path the absolute path to the Chrome binary executable
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if the path is null or points to a non-executable file
   */
  public CChromeSeleniumProvider setBinary(String path) {
    options.setBinary(path);
    return this;
  }

  /**
   * Adds command-line arguments to the Chrome browser startup.
   * 
   * <p>This method allows adding custom Chrome command-line arguments to modify browser
   * behavior, enable experimental features, or configure security settings. Arguments
   * are applied in addition to any default arguments configured in {@link CChromeConfigs}.
   * 
   * <p><strong>Common arguments:</strong>
   * <ul>
   *   <li>{@code --disable-web-security} - Disables web security (for testing)</li>
   *   <li>{@code --no-sandbox} - Disables sandbox (required in some Docker environments)</li>
   *   <li>{@code --disable-dev-shm-usage} - Prevents /dev/shm issues in containers</li>
   *   <li>{@code --start-maximized} - Starts browser maximized</li>
   *   <li>{@code --disable-notifications} - Disables browser notifications</li>
   *   <li>{@code --disable-popup-blocking} - Disables popup blocking</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Add security and UI arguments
   * List<String> args = Arrays.asList(
   *     "--disable-web-security",
   *     "--disable-notifications",
   *     "--start-maximized",
   *     "--disable-popup-blocking"
   * );
   * provider.addArguments(args);
   * 
   * // Add container-friendly arguments
   * provider.addArguments(Arrays.asList("--no-sandbox", "--disable-dev-shm-usage"));
   * 
   * RemoteWebDriver driver = provider.buildLocalDriver();
   * }</pre>
   * 
   * @param args an iterable collection of Chrome command-line arguments (including leading dashes)
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if args is null or contains null arguments
   */
  public CChromeSeleniumProvider addArguments(Iterable<String> args) {
    for (String arg : args) {
      options.addArguments(arg);
    }
    return this;
  }

  /**
   * Enables headless mode for the Chrome browser.
   * 
   * <p>Headless mode runs Chrome without a visible user interface, which is ideal for
   * automated testing, server environments, and CI/CD pipelines. The browser operates
   * normally but without rendering to a display, resulting in faster execution and
   * lower resource consumption.
   * 
   * <p>This method applies the headless arguments configured in {@link CChromeConfigs#getHeadLessArguments()}.
   * 
   * <p><strong>Benefits of headless mode:</strong>
   * <ul>
   *   <li>Faster test execution (no UI rendering)</li>
   *   <li>Lower memory and CPU usage</li>
   *   <li>Suitable for server environments without displays</li>
   *   <li>Eliminates window focus issues</li>
   *   <li>Enables parallel test execution</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider()
   *     .setHeadless()
   *     .addArguments(Arrays.asList("--window-size=1920,1080"));
   * 
   * RemoteWebDriver headlessDriver = provider.buildLocalDriver();
   * 
   * // Browser runs invisibly in background
   * headlessDriver.get("https://example.com");
   * 
   * // Screenshots still work in headless mode
   * TakesScreenshot screenshot = (TakesScreenshot) headlessDriver;
   * byte[] image = screenshot.getScreenshotAs(OutputType.BYTES);
   * }</pre>
   * 
   * @return this provider instance for method chaining
   */
  public CChromeSeleniumProvider setHeadless() {
    addArguments(CChromeConfigs.getHeadLessArguments());
    return this;
  }

  /**
   * Configures whether PDF files should open in a new tab instead of downloading.
   * 
   * <p>This setting controls Chrome's behavior when encountering PDF files. When set to
   * {@code true}, PDFs will be displayed directly in the browser tab. When {@code false},
   * PDFs will be downloaded to the default download directory. This is particularly
   * useful for testing applications that need to display or interact with PDF content.
   * 
   * <p><strong>Use cases:</strong>
   * <ul>
   *   <li>Testing PDF viewer functionality</li>
   *   <li>Verifying PDF content without downloading</li>
   *   <li>Preventing unwanted PDF downloads during tests</li>
   *   <li>Enabling PDF interaction through WebDriver</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Enable PDF viewing in browser (default)
   * provider.setOpenPdfInNewTab(true);
   * RemoteWebDriver viewerDriver = provider.buildLocalDriver();
   * viewerDriver.get("https://example.com/document.pdf");
   * // PDF opens in browser tab for viewing/interaction
   * 
   * // Force PDF downloads instead
   * provider.setOpenPdfInNewTab(false);
   * RemoteWebDriver downloadDriver = provider.buildLocalDriver();
   * downloadDriver.get("https://example.com/document.pdf");
   * // PDF is downloaded to downloads folder
   * }</pre>
   * 
   * @param value {@code true} to open PDFs in new tabs, {@code false} to download them
   * @return this provider instance for method chaining
   */
  public CChromeSeleniumProvider setOpenPdfInNewTab(boolean value) {
    prefs.put("plugins.always_open_pdf_externally", value);
    return this;
  }

  /**
   * Sets the page load strategy for the Chrome browser.
   * 
   * <p>The page load strategy determines when WebDriver considers a page navigation to be complete.
   * Different strategies offer trade-offs between speed and completeness of page loading.
   * This setting affects the behavior of {@code driver.get()} and navigation commands.
   * 
   * <p><strong>Available strategies:</strong>
   * <ul>
   *   <li>{@link PageLoadStrategy#NORMAL} - Wait for all resources to load (default)</li>
   *   <li>{@link PageLoadStrategy#EAGER} - Wait for DOM to be ready, but not all resources</li>
   *   <li>{@link PageLoadStrategy#NONE} - Return immediately after initial navigation</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Fast loading - don't wait for images/stylesheets
   * provider.setPageLoadStrategy(PageLoadStrategy.EAGER);
   * RemoteWebDriver fastDriver = provider.buildLocalDriver();
   * fastDriver.get("https://heavy-site.com"); // Returns when DOM is ready
   * 
   * // Complete loading - wait for everything
   * provider.setPageLoadStrategy(PageLoadStrategy.NORMAL);
   * RemoteWebDriver completeDriver = provider.buildLocalDriver();
   * completeDriver.get("https://heavy-site.com"); // Returns when fully loaded
   * 
   * // Immediate return - manual wait required
   * provider.setPageLoadStrategy(PageLoadStrategy.NONE);
   * RemoteWebDriver immediateDriver = provider.buildLocalDriver();
   * immediateDriver.get("https://heavy-site.com"); // Returns immediately
   * // Need to use explicit waits for elements
   * }</pre>
   * 
   * @param pageLoadStrategy the desired page load strategy
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if pageLoadStrategy is null
   */
  public CChromeSeleniumProvider setPageLoadStrategy(PageLoadStrategy pageLoadStrategy) {
    options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, pageLoadStrategy.toString());
    return this;
  }

  /**
   * Enables mobile device emulation for the specified device.
   * 
   * <p>This method configures Chrome to emulate a mobile device, including viewport size,
   * user agent, touch events, and device pixel ratio. This enables testing mobile-responsive
   * websites and mobile-specific functionality without requiring actual mobile devices.
   * 
   * <p><strong>Supported device names include:</strong>
   * <ul>
   *   <li>iPhone 12, iPhone 12 Pro, iPhone SE</li>
   *   <li>Samsung Galaxy S21, Samsung Galaxy Note 20</li>
   *   <li>iPad, iPad Pro, iPad Mini</li>
   *   <li>Pixel 5, Pixel 4, Pixel 3</li>
   *   <li>Custom device configurations (see Chrome DevTools)</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Test iPhone 12 viewport
   * provider.setDeviceEmulation("iPhone 12");
   * RemoteWebDriver mobileDriver = provider.buildLocalDriver();
   * mobileDriver.get("https://mobile-site.com");
   * 
   * // Verify mobile layout
   * WebElement mobileMenu = mobileDriver.findElement(By.className("mobile-menu"));
   * assert mobileMenu.isDisplayed();
   * 
   * // Test tablet viewport
   * provider.setDeviceEmulation("iPad");
   * RemoteWebDriver tabletDriver = provider.buildLocalDriver();
   * tabletDriver.get("https://responsive-site.com");
   * 
   * // Test touch interactions
   * TouchActions touch = new TouchActions(mobileDriver);
   * touch.singleTap(element).perform();
   * }</pre>
   * 
   * @param deviceName the name of the device to emulate (must match Chrome's device list)
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if deviceName is null or not recognized by Chrome
   */
  public CChromeSeleniumProvider setDeviceEmulation(String deviceName) {
    Map<String, String> mobileEmulation = new HashMap<>();
    mobileEmulation.put("deviceName", deviceName);
    options.setExperimentalOption("mobileEmulation", mobileEmulation);
    return this;
  }

  /**
   * Disables the specified Chrome plugins/extensions.
   * 
   * <p>This method adds plugins to the disabled list, preventing them from loading
   * during browser startup. Disabling unnecessary plugins can improve browser performance,
   * reduce security risks, and eliminate potential interference with automated tests.
   * 
   * <p><strong>Common plugins to disable:</strong>
   * <ul>
   *   <li>Chrome PDF Viewer</li>
   *   <li>Chrome Flash Player (legacy)</li>
   *   <li>Chrome Default Apps</li>
   *   <li>Chrome Translate</li>
   *   <li>Chrome Password Manager</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Disable specific plugins for cleaner test environment
   * List<String> pluginsToDisable = Arrays.asList(
   *     "Chrome PDF Viewer",
   *     "Chrome Translate",
   *     "Chrome Password Manager"
   * );
   * provider.addPluginsToDisable(pluginsToDisable);
   * 
   * // Chain with other plugins
   * provider.addPluginsToDisable(Arrays.asList("Chrome Default Apps"));
   * 
   * RemoteWebDriver cleanDriver = provider.buildLocalDriver();
   * // Browser starts without specified plugins
   * }</pre>
   * 
   * @param pluginsToDisable an iterable collection of plugin names to disable
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if pluginsToDisable is null or contains null plugin names
   */
  public CChromeSeleniumProvider addPluginsToDisable(Iterable<String> pluginsToDisable) {
    new CList<>(pluginsToDisable).forEach(p -> addPlugin(false, p));
    return this;
  }

  /**
   * Enables the specified Chrome plugins/extensions.
   * 
   * <p>This method adds plugins to the enabled list, ensuring they are loaded and active
   * during browser startup. This is useful when you need specific plugin functionality
   * for your tests, or when overriding default disabled plugins from configuration.
   * 
   * <p><strong>Example use cases:</strong>
   * <ul>
   *   <li>Testing applications that require Flash (legacy)</li>
   *   <li>Enabling PDF viewer for PDF interaction tests</li>
   *   <li>Testing browser extension compatibility</li>
   *   <li>Overriding globally disabled plugins for specific tests</li>
   * </ul>
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Enable specific plugins needed for testing
   * List<String> pluginsToEnable = Arrays.asList(
   *     "Chrome PDF Viewer",
   *     "Chrome Media Router"
   * );
   * provider.addPluginsToEnable(pluginsToEnable);
   * 
   * // Test PDF functionality
   * RemoteWebDriver pdfDriver = provider.buildLocalDriver();
   * pdfDriver.get("https://example.com/document.pdf");
   * 
   * // PDF viewer plugin is active
   * WebElement pdfViewer = pdfDriver.findElement(By.id("pdf-viewer"));
   * assert pdfViewer.isDisplayed();
   * }</pre>
   * 
   * @param pluginsToEnable an iterable collection of plugin names to enable
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if pluginsToEnable is null or contains null plugin names
   */
  public CChromeSeleniumProvider addPluginsToEnable(Iterable<String> pluginsToEnable) {
    new CList<>(pluginsToEnable).forEach(p -> addPlugin(true, p));
    return this;
  }

  /**
   * Adds a single plugin to the enabled or disabled list.
   * 
   * <p>This method provides fine-grained control over individual plugin states.
   * It's particularly useful when you need to enable some plugins while disabling
   * others, or when building plugin configurations programmatically.
   * 
   * <p>The plugin configuration is stored internally and applied when the browser
   * capabilities are built. Each plugin entry includes an enabled flag and the
   * plugin name, allowing precise control over Chrome's plugin loading behavior.
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CChromeDriverProvider provider = new CChromeDriverProvider();
   * 
   * // Enable PDF viewer but disable Flash
   * provider.addPlugin(true, "Chrome PDF Viewer")
   *         .addPlugin(false, "Chrome Flash Player");
   * 
   * // Conditional plugin management
   * boolean needsPdf = testRequiresPdfViewer();
   * provider.addPlugin(needsPdf, "Chrome PDF Viewer");
   * 
   * // Build configuration with specific plugin states
   * for (String plugin : getRequiredPlugins()) {
   *     provider.addPlugin(true, plugin);
   * }
   * for (String plugin : getBlockedPlugins()) {
   *     provider.addPlugin(false, plugin);
   * }
   * 
   * RemoteWebDriver configuredDriver = provider.buildLocalDriver();
   * }</pre>
   * 
   * @param flag {@code true} to enable the plugin, {@code false} to disable it
   * @param pluginName the name of the plugin to configure
   * @return this provider instance for method chaining
   * @throws IllegalArgumentException if pluginName is null or empty
   */
  public CChromeSeleniumProvider addPlugin(boolean flag, String pluginName) {
    CMap<String, Object> plugin = new CHashMap<>();
    plugin.put("enabled", flag);
    plugin.put("name", pluginName);
    plugins.add(plugin);
    return this;
  }

  private void setSystemProperties() {
    System.setProperty("webdriver.chrome.silentOutput", "true");
  }
}

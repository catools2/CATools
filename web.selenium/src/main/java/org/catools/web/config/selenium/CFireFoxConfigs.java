package org.catools.web.config.selenium;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.openqa.selenium.PageLoadStrategy;

import java.util.List;

/**
 * Utility class for managing Firefox WebDriver configuration settings.
 * <p>
 * This class provides access to Firefox-specific configuration properties
 * including binary path, browser arguments, page load strategy, and headless mode settings.
 * All configuration values are retrieved from the HOCON configuration system.
 * </p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Get Firefox binary path
 * String binaryPath = CFireFoxConfigs.getBinaryPath();
 * 
 * // Check if headless mode is enabled
 * boolean isHeadless = CFireFoxConfigs.isInHeadLessMode();
 * 
 * // Get default browser arguments
 * List<String> defaultArgs = CFireFoxConfigs.getDefaultArguments();
 * 
 * // Get page load strategy
 * PageLoadStrategy strategy = CFireFoxConfigs.getPageLoadStrategy();
 * }</pre>
 * 
 * @author CATools
 * @since 1.0
 */
@UtilityClass
public class CFireFoxConfigs {

  /**
   * Retrieves the path to the Firefox binary executable.
   * <p>
   * This method returns the configured path to the Firefox browser executable.
   * If not specified in configuration, the system default Firefox installation will be used.
   * </p>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * String firefoxPath = CFireFoxConfigs.getBinaryPath();
   * System.out.println("Firefox binary path: " + firefoxPath);
   * // Output: Firefox binary path: /usr/bin/firefox
   * }</pre>
   * 
   * <h3>Configuration Key:</h3>
   * {@code catools.web.firefox.binary_path}
   * 
   * @return the path to the Firefox binary as a String, or empty string if not configured
   */
  public static String getBinaryPath() {
    return CHocon.asString(Configs.CATOOLS_WEB_FIREFOX_BINARY_PATH);
  }

  /**
   * Retrieves the list of default command-line arguments for Firefox.
   * <p>
   * These arguments are applied to Firefox when starting the browser instance.
   * Common arguments include preferences for privacy, security, and performance settings.
   * </p>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * List<String> defaultArgs = CFireFoxConfigs.getDefaultArguments();
   * defaultArgs.forEach(arg -> System.out.println("Argument: " + arg));
   * // Output might include:
   * // Argument: --disable-extensions
   * // Argument: --no-sandbox
   * // Argument: --disable-dev-shm-usage
   * }</pre>
   * 
   * <h3>Configuration Key:</h3>
   * {@code catools.web.firefox.default_arguments}
   * 
   * @return a List of String arguments to be passed to Firefox, or empty list if none configured
   */
  public static List<String> getDefaultArguments() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_FIREFOX_DEFAULT_ARGUMENTS);
  }

  /**
   * Retrieves the configured page load strategy for Firefox WebDriver.
   * <p>
   * The page load strategy determines how WebDriver waits for pages to load.
   * This affects the timing of when WebDriver considers a page "ready" for interaction.
   * </p>
   * 
   * <h3>Available Strategies:</h3>
   * <ul>
   *   <li>{@code NORMAL} - Wait for all resources to load (default)</li>
   *   <li>{@code EAGER} - Wait for DOM to be ready, but not all resources</li>
   *   <li>{@code NONE} - Return immediately after initial page load</li>
   * </ul>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * PageLoadStrategy strategy = CFireFoxConfigs.getPageLoadStrategy();
   * System.out.println("Page load strategy: " + strategy);
   * // Output: Page load strategy: NORMAL
   * 
   * // Usage with FirefoxOptions
   * FirefoxOptions options = new FirefoxOptions();
   * options.setPageLoadStrategy(strategy);
   * }</pre>
   * 
   * <h3>Configuration Key:</h3>
   * {@code catools.web.firefox.page_load_strategy}
   * 
   * @return the configured PageLoadStrategy enum value
   */
  public static PageLoadStrategy getPageLoadStrategy() {
    return CHocon.asEnum(Configs.CATOOLS_WEB_FIREFOX_PAGE_LOAD_STRATEGY, PageLoadStrategy.class);
  }

  /**
   * Checks whether Firefox should run in headless mode.
   * <p>
   * Headless mode runs Firefox without a graphical user interface,
   * which is useful for automated testing, CI/CD pipelines, and server environments
   * where no display is available.
   * </p>
   * 
   * <h3>Example:</h3>
   * <pre>{@code
   * boolean isHeadless = CFireFoxConfigs.isInHeadLessMode();
   * System.out.println("Headless mode enabled: " + isHeadless);
   * // Output: Headless mode enabled: true
   * 
   * // Usage with FirefoxOptions
   * FirefoxOptions options = new FirefoxOptions();
   * if (isHeadless) {
   *     options.setHeadless(true);
   * }
   * }</pre>
   * 
   * <h3>Configuration Key:</h3>
   * {@code catools.web.firefox.headless.enable}
   * 
   * @return true if Firefox should run in headless mode, false otherwise
   */
  public static boolean isInHeadLessMode() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_FIREFOX_HEADLESS_ENABLE);
  }

  /**
   * Retrieves the list of command-line arguments specifically for headless mode.
   * <p>
   * These arguments are applied to Firefox when running in headless mode,
   * in addition to the default arguments. They typically include optimizations
   * and configurations specific to headless operation.
   * </p>
   *
   * <h3>Example:</h3>
   * <pre>{@code
   * List<String> headlessArgs = CFireFoxConfigs.getHeadLessArguments();
   * headlessArgs.forEach(arg -> System.out.println("Headless argument: " + arg));
   * // Output might include:
   * // Headless argument: --window-size=1920,1080
   * // Headless argument: --disable-gpu
   * // Headless argument: --no-first-run
   *
   * // Usage with FirefoxOptions
   * FirefoxOptions options = new FirefoxOptions();
   * if (CFireFoxConfigs.isInHeadLessMode()) {
   *     options.setHeadless(true);
   *     headlessArgs.forEach(options::addArguments);
   * }
   * }</pre>
   *
   * <h3>Configuration Key:</h3>
   * {@code catools.web.firefox.headless.arguments}
   *
   * @return a List of String arguments for headless mode, or empty list if none configured
   */
  public static List<String> getHeadLessArguments() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_FIREFOX_HEADLESS_ARGUMENTS);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_FIREFOX_BINARY_PATH("catools.web.firefox.binary_path"),
    CATOOLS_WEB_FIREFOX_DEFAULT_ARGUMENTS("catools.web.firefox.default_arguments"),
    CATOOLS_WEB_FIREFOX_PAGE_LOAD_STRATEGY("catools.web.firefox.page_load_strategy"),
    CATOOLS_WEB_FIREFOX_HEADLESS_ENABLE("catools.web.firefox.headless.enable"),
    CATOOLS_WEB_FIREFOX_HEADLESS_ARGUMENTS("catools.web.firefox.headless.arguments");

    private final String path;
  }
}

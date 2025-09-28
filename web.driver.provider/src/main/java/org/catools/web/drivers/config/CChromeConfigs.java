package org.catools.web.drivers.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.openqa.selenium.PageLoadStrategy;

import java.util.List;

/**
 * Utility class for managing Chrome browser configuration settings.
 * <p>
 * This class provides static methods to retrieve various Chrome browser configuration
 * properties from the application configuration using HOCON format.
 * All configuration values are read from the application's configuration files
 * and can be overridden through system properties or environment variables.
 * </p>
 *
 * @since 1.0
 */
@UtilityClass
public class CChromeConfigs {

  /**
   * Retrieves the path to the Chrome binary executable.
   * <p>
   * This method returns the configured path to the Chrome browser executable.
   * If not specified in the configuration, Chrome will use its default installation path.
   * </p>
   *
   * @return the path to Chrome binary executable, or null if not configured
   * @example
   * <pre>{@code
   * String chromePath = CChromeConfigs.getBinaryPath();
   * // Example return: "/usr/bin/google-chrome" or "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"
   * }</pre>
   */
  public static String getBinaryPath() {
    return CHocon.asString(Configs.CATOOLS_WEB_CHROME_BINARY_PATH);
  }

  /**
   * Retrieves the default command-line arguments for Chrome browser.
   * <p>
   * These arguments are applied to every Chrome browser instance created.
   * Common arguments include disabling features, setting window sizes, or enabling/disabling specific Chrome features.
   * </p>
   *
   * @return a list of default Chrome command-line arguments
   * @example
   * <pre>{@code
   * List<String> args = CChromeConfigs.getDefaultArguments();
   * // Example return: ["--disable-web-security", "--disable-features=VizDisplayCompositor", "--no-sandbox"]
   * }</pre>
   */
  public static List<String> getDefaultArguments() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_CHROME_DEFAULT_ARGUMENTS);
  }

  /**
   * Retrieves the page load strategy for Chrome browser.
   * <p>
   * The page load strategy determines when WebDriver considers a page load complete.
   * This affects when control is returned to the test after a navigation command.
   * </p>
   *
   * @return the configured PageLoadStrategy enum value
   * @see PageLoadStrategy#NORMAL - waits for all resources to load (default)
   * @see PageLoadStrategy#EAGER - waits for DOM to be ready, doesn't wait for images/stylesheets
   * @see PageLoadStrategy#NONE - returns immediately after initial page load
   * @example
   * <pre>{@code
   * PageLoadStrategy strategy = CChromeConfigs.getPageLoadStrategy();
   * // Example return: PageLoadStrategy.EAGER
   * }</pre>
   */
  public static PageLoadStrategy getPageLoadStrategy() {
    return CHocon.asEnum(Configs.CATOOLS_WEB_CHROME_PAGE_LOAD_STRATEGY, PageLoadStrategy.class);
  }

  /**
   * Retrieves the list of Chrome plugins/extensions to disable.
   * <p>
   * This method returns plugin names or extension IDs that should be disabled
   * when Chrome browser starts. Disabling unnecessary plugins can improve
   * browser performance and stability during automated testing.
   * </p>
   *
   * @return a list of plugin names or extension IDs to disable
   * @example
   * <pre>{@code
   * List<String> pluginsToDisable = CChromeConfigs.getPluginsToDisable();
   * // Example return: ["Chrome PDF Viewer", "Adobe Flash Player", "abcdefghijklmnopqrstuvwxyz123456"]
   * }</pre>
   */
  public static List<String> getPluginsToDisable() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_CHROME_PLUGINS_TO_DISABLE);
  }

  /**
   * Retrieves the list of Chrome plugins/extensions to enable.
   * <p>
   * This method returns plugin names or extension IDs that should be explicitly enabled
   * when Chrome browser starts. This is useful for enabling specific extensions
   * required for testing scenarios.
   * </p>
   *
   * @return a list of plugin names or extension IDs to enable
   * @example
   * <pre>{@code
   * List<String> pluginsToEnable = CChromeConfigs.getPluginsToEnable();
   * // Example return: ["Test Extension", "gighmmpiobklfepjocnamgkkbiglidom"]
   * }</pre>
   */
  public static List<String> getPluginsToEnable() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_CHROME_PLUGINS_TO_ENABLE);
  }

  /**
   * Checks if Chrome browser should run in headless mode.
   * <p>
   * Headless mode runs the browser without a graphical user interface,
   * which is useful for automated testing in CI/CD environments or
   * server environments where no display is available.
   * </p>
   *
   * @return true if headless mode is enabled, false otherwise
   * @example
   * <pre>{@code
   * boolean isHeadless = CChromeConfigs.isInHeadLessMode();
   * // Example return: true (for CI environments) or false (for local development)
   * }</pre>
   */
  public static boolean isInHeadLessMode() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_CHROME_HEADLESS_ENABLE);
  }

  /**
   * Retrieves additional command-line arguments specific to headless mode.
   * <p>
   * These arguments are applied only when Chrome is running in headless mode.
   * They can include window size settings, disable GPU acceleration,
   * or other headless-specific optimizations.
   * </p>
   *
   * @return a list of headless-specific Chrome command-line arguments
   * @example
   * <pre>{@code
   * List<String> headlessArgs = CChromeConfigs.getHeadLessArguments();
   * // Example return: ["--window-size=1920,1080", "--disable-gpu", "--no-sandbox"]
   * }</pre>
   */
  public static List<String> getHeadLessArguments() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_CHROME_HEADLESS_ARGUMENTS);
  }

  /**
   * Retrieves the device name for Chrome mobile emulation.
   * <p>
   * This method returns the name of the mobile device to emulate when running Chrome.
   * Mobile emulation allows testing web applications as they would appear and behave
   * on mobile devices without requiring actual mobile hardware.
   * </p>
   *
   * @return the mobile device name for emulation, or null if not configured
   * @example
   * <pre>{@code
   * String deviceName = CChromeConfigs.getChromeMobileEmulationDeviceName();
   * // Example return: "iPhone 12 Pro", "Samsung Galaxy S21", "iPad Pro"
   * }</pre>
   */
  public static String getChromeMobileEmulationDeviceName() {
    return CHocon.asString(Configs.CATOOLS_WEB_CHROME_MOBILE_EMULATION_DEVICE_NAME);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_CHROME_BINARY_PATH("catools.web.chrome.binary_path"),
    CATOOLS_WEB_CHROME_DEFAULT_ARGUMENTS("catools.web.chrome.default_arguments"),
    CATOOLS_WEB_CHROME_PAGE_LOAD_STRATEGY("catools.web.chrome.page_load_strategy"),
    CATOOLS_WEB_CHROME_PLUGINS_TO_DISABLE("catools.web.chrome.plugins_to_disable"),
    CATOOLS_WEB_CHROME_PLUGINS_TO_ENABLE("catools.web.chrome.plugins_to_enable"),
    CATOOLS_WEB_CHROME_HEADLESS_ENABLE("catools.web.chrome.headless_enable"),
    CATOOLS_WEB_CHROME_HEADLESS_ARGUMENTS("catools.web.chrome.headless_arguments"),
    CATOOLS_WEB_CHROME_MOBILE_EMULATION_DEVICE_NAME("catools.web.chrome.mobile_emulation_device_name");

    private final String path;
  }
}

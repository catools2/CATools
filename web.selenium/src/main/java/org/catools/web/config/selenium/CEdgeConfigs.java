package org.catools.web.config.selenium;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.openqa.selenium.PageLoadStrategy;

/**
 * Utility class for managing Microsoft Edge WebDriver configuration settings.
 *
 * <p>This class provides methods to retrieve configuration values for Edge WebDriver from the
 * application's configuration files using the CHocon configuration system. All methods in this
 * class are static and the class cannot be instantiated.
 *
 * <p>Configuration values are read from properties with the prefix "catools.web.edge.*"
 *
 * @author CATools Team
 * @since 1.0
 */
@UtilityClass
public class CEdgeConfigs {

  /**
   * Retrieves the binary path for the Microsoft Edge browser executable.
   *
   * <p>This method returns the file system path to the Edge browser executable that will be used by
   * the WebDriver. If not configured, the system's default Edge installation will be used.
   *
   * @return the path to the Edge binary as a String, or null if not configured
   * @example
   *     <pre>{@code
   * // Get the configured Edge binary path
   * String edgePath = CEdgeConfigs.getBinaryPath();
   * if (edgePath != null) {
   *     System.out.println("Using Edge at: " + edgePath);
   * } else {
   *     System.out.println("Using system default Edge installation");
   * }
   * }</pre>
   *
   * @see org.openqa.selenium.edge.EdgeOptions#setBinary(String)
   */
  public static String getBinaryPath() {
    return CHocon.asString(Configs.CATOOLS_WEB_EDGE_BINARY_PATH);
  }

  /**
   * Retrieves the list of default command-line arguments for the Edge WebDriver.
   *
   * <p>These arguments are passed to the Edge browser instance when it starts. Common arguments
   * include options for window size, user agent, security settings, and performance optimizations.
   *
   * @return a List of String arguments to be passed to Edge browser, or an empty list if none
   *     configured
   * @example
   *     <pre>{@code
   * // Get and apply default arguments
   * List<String> defaultArgs = CEdgeConfigs.getDefaultArguments();
   * EdgeOptions options = new EdgeOptions();
   * for (String arg : defaultArgs) {
   *     options.addArguments(arg);
   * }
   *
   * // Example configuration might return:
   * // ["--disable-extensions", "--disable-gpu", "--no-sandbox"]
   * }</pre>
   *
   * @see org.openqa.selenium.edge.EdgeOptions#addArguments(String...)
   */
  public static List<String> getDefaultArguments() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_EDGE_DEFAULT_ARGUMENTS);
  }

  /**
   * Retrieves the page load strategy configuration for the Edge WebDriver.
   *
   * <p>The page load strategy determines when WebDriver considers a page load to be complete. This
   * affects how long WebDriver waits for pages to load before proceeding with test execution.
   *
   * @return the configured PageLoadStrategy enum value
   * @example
   *     <pre>{@code
   * // Get and apply page load strategy
   * PageLoadStrategy strategy = CEdgeConfigs.getPageLoadStrategy();
   * EdgeOptions options = new EdgeOptions();
   * options.setPageLoadStrategy(strategy);
   *
   * // Possible values:
   * // PageLoadStrategy.NORMAL - Wait for all resources to load (default)
   * // PageLoadStrategy.EAGER - Wait for DOM to be complete
   * // PageLoadStrategy.NONE - Return immediately after initial page content
   *
   * System.out.println("Using page load strategy: " + strategy);
   * }</pre>
   *
   * @see org.openqa.selenium.PageLoadStrategy
   * @see org.openqa.selenium.edge.EdgeOptions#setPageLoadStrategy(PageLoadStrategy)
   */
  public static PageLoadStrategy getPageLoadStrategy() {
    return CHocon.asEnum(Configs.CATOOLS_WEB_EDGE_PAGE_LOAD_STRATEGY, PageLoadStrategy.class);
  }

  /**
   * Determines whether Edge WebDriver should run in headless mode.
   *
   * <p>Headless mode runs the browser without a graphical user interface, which is useful for
   * automated testing in CI/CD environments or when running tests on servers without display
   * capabilities.
   *
   * @return true if headless mode is enabled, false otherwise
   * @example
   *     <pre>{@code
   * // Check if headless mode is enabled and configure accordingly
   * boolean headlessEnabled = CEdgeConfigs.isInHeadLessMode();
   * EdgeOptions options = new EdgeOptions();
   *
   * if (headlessEnabled) {
   *     options.addArguments("--headless");
   *     System.out.println("Running Edge in headless mode");
   * } else {
   *     System.out.println("Running Edge with GUI");
   * }
   *
   * WebDriver driver = new EdgeDriver(options);
   * }</pre>
   *
   * @see org.openqa.selenium.edge.EdgeOptions#addArguments(String...)
   */
  public static boolean isInHeadLessMode() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_EDGE_HEADLESS_ENABLE);
  }

  /**
   * Retrieves the list of command-line arguments specifically for headless mode.
   *
   * <p>These arguments are additional parameters that should be applied when running Edge in
   * headless mode. They typically include optimizations and configurations that are specific to
   * headless operation.
   *
   * @return a List of String arguments specific to headless mode, or an empty list if none
   *     configured
   * @example
   *     <pre>{@code
   * // Configure Edge with headless-specific arguments
   * if (CEdgeConfigs.isInHeadLessMode()) {
   *     List<String> headlessArgs = CEdgeConfigs.getHeadLessArguments();
   *     EdgeOptions options = new EdgeOptions();
   *
   *     // Add the main headless argument
   *     options.addArguments("--headless");
   *
   *     // Add headless-specific arguments
   *     for (String arg : headlessArgs) {
   *         options.addArguments(arg);
   *     }
   *
   *     // Example headless arguments might include:
   *     // ["--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage"]
   *
   *     WebDriver driver = new EdgeDriver(options);
   * }
   * }</pre>
   *
   * @see #isInHeadLessMode()
   * @see org.openqa.selenium.edge.EdgeOptions#addArguments(String...)
   */
  public static List<String> getHeadLessArguments() {
    return CHocon.asStrings(Configs.CATOOLS_WEB_EDGE_HEADLESS_ARGUMENTS);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_EDGE_BINARY_PATH("catools.web.edge.binary_path"),
    CATOOLS_WEB_EDGE_DEFAULT_ARGUMENTS("catools.web.edge.default_arguments"),
    CATOOLS_WEB_EDGE_PAGE_LOAD_STRATEGY("catools.web.edge.page_load_strategy"),
    CATOOLS_WEB_EDGE_HEADLESS_ENABLE("catools.web.edge.headless_enable"),
    CATOOLS_WEB_EDGE_HEADLESS_ARGUMENTS("catools.web.edge.headless_arguments");

    private final String path;
  }
}

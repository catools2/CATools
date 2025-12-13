package org.catools.web.config.selenium;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Utility class for managing Web Driver Manager configurations.
 * <p>
 * This class provides convenient access to configuration settings related to the Web Driver Manager
 * functionality through the HOCON configuration system. It serves as a centralized location for
 * retrieving configuration values that control the behavior of web driver management.
 * </p>
 * 
 * <h3>Configuration Structure</h3>
 * The configurations are read from HOCON configuration files with the following structure:
 * <pre>
 * catools {
 *   web {
 *     driver_manager {
 *       enabled = true
 *     }
 *   }
 * }
 * </pre>
 * 
 * <h3>Usage Examples</h3>
 * <pre>
 * // Check if Web Driver Manager is enabled
 * if (CWebDriverManagerConfigs.isEnabled()) {
 *     // Initialize web driver manager
 *     WebDriverManager.chromedriver().setup();
 * }
 * 
 * // Use in conditional logic
 * boolean shouldSetupDrivers = CWebDriverManagerConfigs.isEnabled();
 * if (shouldSetupDrivers) {
 *     // Setup multiple drivers
 *     WebDriverManager.chromedriver().setup();
 *     WebDriverManager.firefoxdriver().setup();
 * }
 * </pre>
 * 
 * @author CATools Team
 * @since 1.0
 * @see CHocon
 * @see CHoconPath
 */
@UtilityClass
public class CWebDriverManagerConfigs {
  
  /**
   * Determines whether the Web Driver Manager functionality is enabled.
   * <p>
   * This method reads the configuration value from the path {@code catools.web.driver_manager.enabled}
   * and returns it as a boolean. The Web Driver Manager is typically used to automatically download
   * and setup web driver binaries (like ChromeDriver, GeckoDriver, etc.) without manual intervention.
   * </p>
   * 
   * <h3>Configuration Path</h3>
   * The method reads from: {@code catools.web.driver_manager.enabled}
   * 
   * <h3>Default Behavior</h3>
   * If the configuration is not present or cannot be parsed as a boolean, the underlying
   * HOCON library will handle the default behavior according to its configuration rules.
   * 
   * <h3>Usage Examples</h3>
   * <pre>
   * // Basic usage
   * if (CWebDriverManagerConfigs.isEnabled()) {
   *     System.out.println("Web Driver Manager is enabled");
   * }
   * 
   * // Use in test setup
   * &#64;BeforeEach
   * void setUp() {
   *     if (CWebDriverManagerConfigs.isEnabled()) {
   *         WebDriverManager.chromedriver().setup();
   *         driver = new ChromeDriver();
   *     } else {
   *         // Use pre-installed driver or throw exception
   *         driver = new ChromeDriver(); // Assumes driver is in PATH
   *     }
   * }
   * 
   * // Conditional driver initialization
   * public WebDriver createWebDriver(String browserType) {
   *     if (CWebDriverManagerConfigs.isEnabled()) {
   *         switch (browserType.toLowerCase()) {
   *             case "chrome":
   *                 WebDriverManager.chromedriver().setup();
   *                 return new ChromeDriver();
   *             case "firefox":
   *                 WebDriverManager.firefoxdriver().setup();
   *                 return new FirefoxDriver();
   *             default:
   *                 throw new IllegalArgumentException("Unsupported browser: " + browserType);
   *         }
   *     }
   *     // Fallback to manual driver management
   *     return createWebDriverManually(browserType);
   * }
   * </pre>
   * 
   * @return {@code true} if the Web Driver Manager is enabled in the configuration,
   *         {@code false} otherwise
   * @throws com.typesafe.config.ConfigException if there's an error reading the configuration
   * @see CHocon#asBoolean(CHoconPath)
   * @since 1.0
   */
  public static boolean isEnabled() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_DRIVER_MANAGER_ENABLED);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_DRIVER_MANAGER_ENABLED("catools.web.driver_manager.enabled");

    private final String path;
  }
}

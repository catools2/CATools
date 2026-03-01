package org.catools.web.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Configuration management for Playwright settings. Uses HOCON for flexible, type-safe
 * configuration.
 *
 * <p>Configuration properties:
 *
 * <ul>
 *   <li>catools.playwright.headless - Whether to run browser in headless mode (default: false)
 *   <li>catools.playwright.browser - Browser type: CHROMIUM, FIREFOX, or WEBKIT (default: CHROMIUM)
 *   <li>catools.playwright.viewport.width - Viewport width in pixels (default: 1280)
 *   <li>catools.playwright.viewport.height - Viewport height in pixels (default: 720)
 *   <li>catools.playwright.timeout - Default timeout in milliseconds (default: 30000)
 *   <li>catools.playwright.slowmo - Slow down operations by specified milliseconds (default: 0)
 *   <li>catools.playwright.screenshots.enabled - Enable automatic screenshots (default: false)
 *   <li>catools.playwright.screenshots.path - Path to save screenshots (default: ./screenshots)
 *   <li>catools.playwright.tracing.enabled - Enable tracing (default: false)
 *   <li>catools.playwright.tracing.path - Path to save traces (default: ./traces)
 * </ul>
 *
 * <p>Example application.conf:
 *
 * <pre>{@code
 * catools {
 *   playwright {
 *     headless = true
 *     browser = "CHROMIUM"
 *     viewport {
 *       width = 1920
 *       height = 1080
 *     }
 *     timeout = 60000
 *     slowmo = 100
 *     screenshots {
 *       enabled = true
 *       path = "./test-screenshots"
 *     }
 *     tracing {
 *       enabled = true
 *       path = "./test-traces"
 *     }
 *   }
 * }
 * }</pre>
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Get configuration values
 * boolean headless = CPlaywrightConfigs.isHeadless();
 * String browser = CPlaywrightConfigs.getBrowser();
 * int viewportWidth = CPlaywrightConfigs.getViewportWidth();
 * int viewportHeight = CPlaywrightConfigs.getViewportHeight();
 * int timeout = CPlaywrightConfigs.getTimeout();
 *
 * // Check if features are enabled
 * if (CPlaywrightConfigs.isScreenshotsEnabled()) {
 *     String screenshotPath = CPlaywrightConfigs.getScreenshotsPath();
 *     // Take screenshot
 * }
 *
 * if (CPlaywrightConfigs.isTracingEnabled()) {
 *     String tracePath = CPlaywrightConfigs.getTracingPath();
 *     // Start tracing
 * }
 * }</pre>
 *
 * @author Alireza Keshmiri
 */
@UtilityClass
public class CPlaywrightConfigs {

  /**
   * Retrieves custom Playwright browser launch options from configuration.
   *
   * @return BrowserType.LaunchOptions if configured, otherwise null.
   */
  public static BrowserType.LaunchOptions getLaunchOptions() {
    return CHocon.has(Configs.CATOOLS_PLAYWRIGHT_LAUNCH_OPTIONS)
        ? CHocon.asModel(Configs.CATOOLS_PLAYWRIGHT_LAUNCH_OPTIONS, BrowserType.LaunchOptions.class)
        : null;
  }

  /**
   * Retrieves custom Playwright browser context options from configuration.
   *
   * @return Browser.NewContextOptions if configured, otherwise null.
   */
  public static Browser.NewContextOptions getNewContextOptions() {
    return CHocon.has(Configs.CATOOLS_PLAYWRIGHT_BROWSER_CONTEXT_OPTIONS)
        ? CHocon.asModel(
            Configs.CATOOLS_PLAYWRIGHT_BROWSER_CONTEXT_OPTIONS, Browser.NewContextOptions.class)
        : null;
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_PLAYWRIGHT_LAUNCH_OPTIONS("catools.playwright.launch_options"),
    CATOOLS_PLAYWRIGHT_BROWSER_CONTEXT_OPTIONS("catools.playwright.browser_launch_options");

    private final String path;
  }
}

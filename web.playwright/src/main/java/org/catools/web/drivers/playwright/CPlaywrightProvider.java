package org.catools.web.drivers.playwright;

import com.microsoft.playwright.*;
import org.catools.web.config.CPlaywrightConfigs;
import org.catools.web.drivers.CDriverEngine;
import org.catools.web.drivers.CDriverEngineProvider;
import org.catools.web.drivers.CPlaywrightEngine;

/**
 * Interface for providing Playwright browser instances with various configuration options.
 *
 * <p>This interface defines the contract for creating Playwright browser instances that can be
 * configured to run with different browsers (Chromium, Firefox, WebKit) in various modes (headed,
 * headless, remote).
 *
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Basic usage with Chromium
 * CPlaywrightProvider provider = new CChromiumPlaywrightProvider();
 * Page page = provider.build();
 *
 * // Get browser type
 * CBrowser browser = provider.getBrowser(); // Returns CBrowser.CHROMIUM
 * }</pre>
 *
 * @author Alireza Keshmiri
 */
public interface CPlaywrightProvider extends CDriverEngineProvider {

  /**
   * Builds and initializes a Playwright Page instance.
   *
   * @return A fully initialized Playwright Page instance ready for use
   * @throws RuntimeException if browser initialization fails
   */
  default CDriverEngine build() {
    return new CPlaywrightEngine(buildPage(), getPlaywright(), getBrowserInstance());
  }

  /**
   * Gets the browser type for this provider.
   *
   * @return Browser type enum
   */
  default BrowserType.LaunchOptions getLaunchOptions() {
    return CPlaywrightConfigs.getLaunchOptions();
  }

  /**
   * Builds a Playwright Page instance.
   *
   * @return Playwright Page instance
   */
  default Page buildPage() {
    Browser.NewContextOptions contextOptions = CPlaywrightConfigs.getNewContextOptions();
    BrowserContext context;

    if (contextOptions == null) {
      // Create context with minimal settings to avoid request/response tracking issues
      contextOptions = new Browser.NewContextOptions()
          .setIgnoreHTTPSErrors(true);
    }

    context = getBrowserInstance().newContext(contextOptions);
    return context.newPage();
  }

  /**
   * Gets the Playwright instance.
   *
   * @return Playwright instance
   */
  Playwright getPlaywright();

  /**
   * Gets the Browser instance.
   *
   * @return Browser instance
   */
  Browser getBrowserInstance();

}


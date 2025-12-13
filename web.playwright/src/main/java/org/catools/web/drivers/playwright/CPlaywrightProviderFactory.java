package org.catools.web.drivers.playwright;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.enums.CBrowser;

/**
 * Factory class for creating Playwright browser providers based on browser type.
 *
 * <p>This class provides a centralized way to instantiate the appropriate Playwright provider
 * based on the desired browser type (Chromium, Firefox, or WebKit).
 *
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Create a Chromium provider
 * CPlaywrightProvider chromiumProvider = CPlaywrightProviderFactory.create(CBrowser.CHROMIUM);
 * Page page = chromiumProvider.build(null);
 *
 * // Create a Firefox provider
 * CPlaywrightProvider firefoxProvider = CPlaywrightProviderFactory.create(CBrowser.FIREFOX);
 * Page firefoxPage = firefoxProvider.build(null);
 *
 * // Create a WebKit provider
 * CPlaywrightProvider webkitProvider = CPlaywrightProviderFactory.create(CBrowser.WEBKIT);
 * Page webkitPage = webkitProvider.build(null);
 *
 * // Create with custom configuration
 * CPlaywrightProvider customProvider = CPlaywrightProviderFactory.create(
 *     CBrowser.CHROMIUM,
 *     true,  // headless
 *     1920,  // viewport width
 *     1080   // viewport height
 * );
 * Page customPage = customProvider.build(null);
 * }</pre>
 *
 * @author Alireza Keshmiri
 */
@Slf4j
public class CPlaywrightProviderFactory {

  /**
   * Creates a Playwright provider instance for the specified browser type with default settings.
   *
   * @param browser The browser type to create a provider for
   * @return A CPlaywrightProvider instance configured for the specified browser
   * @throws IllegalArgumentException if the browser type is not supported for Playwright
   */
  public static CPlaywrightProvider create(CBrowser browser) {
    return create(browser, false, null, null);
  }

  /**
   * Creates a Playwright provider instance with custom configuration.
   *
   * @param browser        The browser type to create a provider for
   * @param headless       Whether to run the browser in headless mode
   * @param viewportWidth  The viewport width (can be null for default)
   * @param viewportHeight The viewport height (can be null for default)
   * @return A CPlaywrightProvider instance configured for the specified browser
   * @throws IllegalArgumentException if the browser type is not supported for Playwright
   */
  public static CPlaywrightProvider create(CBrowser browser, boolean headless,
                                           Integer viewportWidth, Integer viewportHeight) {
    log.debug("Creating Playwright provider for browser: {}, headless: {}", browser, headless);

    return switch (browser) {
      case CHROME, CHROMIUM -> new CChromiumPlaywrightProvider();
      case FIREFOX -> new CFirefoxPlaywrightProvider();
      case WEBKIT -> new CWebKitPlaywrightProvider();
      default -> {
        log.error("Browser type {} is not supported for Playwright. " +
            "Use CHROMIUM, FIREFOX, or WEBKIT.", browser);
        throw new IllegalArgumentException(
            "Unsupported browser for Playwright: " + browser +
                ". Use CHROMIUM, FIREFOX, or WEBKIT.");
      }
    };
  }

  /**
   * Creates a headless Playwright provider for the specified browser type.
   *
   * @param browser The browser type to create a provider for
   * @return A CPlaywrightProvider instance configured for headless mode
   */
  public static CPlaywrightProvider createHeadless(CBrowser browser) {
    return create(browser, true, null, null);
  }

  /**
   * Creates a Playwright provider with custom viewport dimensions.
   *
   * @param browser        The browser type to create a provider for
   * @param viewportWidth  The viewport width
   * @param viewportHeight The viewport height
   * @return A CPlaywrightProvider instance with custom viewport
   */
  public static CPlaywrightProvider createWithViewport(CBrowser browser,
                                                       int viewportWidth,
                                                       int viewportHeight) {
    return create(browser, false, viewportWidth, viewportHeight);
  }
}


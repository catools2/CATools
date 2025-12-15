package org.catools.web.drivers.selenium;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.enums.CBrowser;

/**
 * Factory for creating Selenium-based driver providers.
 *
 * <p>Produces the appropriate {@code CSeleniumProvider} implementation for the requested
 * browser. Keep the API small — use {@link #create(CBrowser)} for defaults and the
 * overloads for headless or custom options.</p>
 *
 * <p>Examples:</p>
 * <pre>{@code
 * CSeleniumProvider provider = CSeleniumProviderFactory.create(CBrowser.CHROME);
 * WebDriver driver = provider.build();
 * }</pre>
 *
 * @since 1.0
 */
@Slf4j
public class CSeleniumProviderFactory {

  /**
   * Create provider with default settings for the given browser.
   */
  public static CSeleniumProvider create(CBrowser browser) {
    log.debug("Creating Selenium provider for browser: {}, headless: {}", browser);
    return switch (browser) {
      case CHROME -> new CChromeSeleniumProvider();
      case CHROMIUM -> new CChromiumSeleniumProvider();
      case FIREFOX -> new CFirefoxSeleniumProvider();
      case EDGE -> new CEdgeSeleniumProvider();
      default -> throw new IllegalArgumentException("Unsupported browser for Selenium: " + browser);
    };
  }
}

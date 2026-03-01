package org.catools.web.enums;

/**
 * Enumeration representing different web browsers supported by the web driver framework. This enum
 * provides a type-safe way to specify and identify different browser types for web automation
 * testing.
 *
 * <p>The supported browsers include:
 *
 * <ul>
 *   <li>{@link #FIREFOX} - Mozilla Firefox browser
 *   <li>{@link #CHROME} - Google Chrome browser
 *   <li>{@link #EDGE} - Microsoft Edge browser
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Create a browser instance
 * CBrowser browser = CBrowser.CHROME;
 *
 * // Check browser type
 * if (browser.isChrome()) {
 *     System.out.println("Using Chrome browser");
 * }
 *
 * // Use in switch statement
 * switch (browser) {
 *     case FIREFOX:
 *         // Configure Firefox-specific settings
 *         break;
 *     case CHROME:
 *         // Configure Chrome-specific settings
 *         break;
 *     case EDGE:
 *         // Configure Edge-specific settings
 *         break;
 * }
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
public enum CBrowser {
  /** Mozilla Firefox browser */
  FIREFOX,

  /** Google Chrome browser */
  CHROME,

  /** Microsoft Edge browser */
  EDGE,

  /** Playwright Chromium browser (supports Chrome, Edge, and other Chromium-based browsers) */
  CHROMIUM,

  /** Playwright WebKit browser (Safari engine) */
  WEBKIT;

  /**
   * Checks if this browser instance represents Google Chrome.
   *
   * <p>This method provides a convenient way to test if the current browser enum value is Chrome
   * without using direct equality checks.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CBrowser browser = CBrowser.CHROME;
   * if (browser.isChrome()) {
   *     // Configure Chrome-specific Page options
   *     ChromeOptions options = new ChromeOptions();
   *     options.addArguments("--headless");
   * }
   *
   * // Use in conditional logic
   * String driverPath = browser.isChrome() ? "/path/to/chromedriver" : "/path/to/other/driver";
   * }</pre>
   *
   * @return {@code true} if this browser is Chrome, {@code false} otherwise
   */
  public boolean isChrome() {
    return CHROME.equals(this);
  }

  /**
   * Checks if this browser instance represents Mozilla Firefox.
   *
   * <p>This method provides a convenient way to test if the current browser enum value is Firefox
   * without using direct equality checks.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CBrowser browser = CBrowser.FIREFOX;
   * if (browser.isFirefox()) {
   *     // Configure Firefox-specific Page options
   *     FirefoxOptions options = new FirefoxOptions();
   *     options.setHeadless(true);
   * }
   *
   * // Use in test setup
   * if (browser.isFirefox()) {
   *     System.setProperty("page.gecko.driver", "/path/to/geckodriver");
   * }
   * }</pre>
   *
   * @return {@code true} if this browser is Firefox, {@code false} otherwise
   */
  public boolean isFirefox() {
    return FIREFOX.equals(this);
  }

  /**
   * Checks if this browser instance represents Microsoft Edge.
   *
   * <p>This method provides a convenient way to test if the current browser enum value is Edge
   * without using direct equality checks.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CBrowser browser = CBrowser.EDGE;
   * if (browser.isEdge()) {
   *     // Configure Edge-specific Page options
   *     EdgeOptions options = new EdgeOptions();
   *     options.addArguments("--inprivate");
   * }
   *
   * // Use in browser capability setup
   * DesiredCapabilities capabilities = new DesiredCapabilities();
   * if (browser.isEdge()) {
   *     capabilities.setBrowserName("MicrosoftEdge");
   * }
   * }</pre>
   *
   * @return {@code true} if this browser is Edge, {@code false} otherwise
   */
  public boolean isEdge() {
    return EDGE.equals(this);
  }
}

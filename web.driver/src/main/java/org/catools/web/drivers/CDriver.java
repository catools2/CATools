package org.catools.web.drivers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.types.CDynamicStringExtension;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CStringUtil;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.media.model.CScreenShot;
import org.catools.media.utils.CImageUtil;
import org.catools.web.controls.CWebElement;
import org.catools.web.enums.CBrowser;
import org.catools.web.selectors.CBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.function.Predicate;

/**
 * Main browser automation wrapper providing element interaction, navigation, and session management.
 * <p>
 * Implements {@link CDriverActions} and {@link CDriverNavigation} for complete browser automation.
 * Includes dynamic properties for title, URL, and screenshots with automatic waiting.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CDriver driver = new CDriver(session);
 * driver.open("https://example.com");
 * driver.$("username", "#user").type("test");
 * driver.$("login", "#submit").click();
 * }</pre>
 *
 * @author CATools Team
 * @see CDriverActions
 * @see CDriverNavigation
 * @see CWebElement
 * @since 1.0
 */
@Slf4j
public class CDriver implements CDriverActions, CDriverNavigation {

  /**
   * Driver session managing the underlying engine instance and lifecycle.
   */
  @Getter
  private final CDriverSession driverSession;

  /**
   * Logger instance for debugging and tracing operations.
   */
  @Getter
  private final Logger logger = LoggerFactory.getLogger(CDriver.class);

  /**
   * Creates a new CDriver instance with the specified driver session.
   * This is the primary constructor for creating CDriver instances and is used by Guice for dependency injection.
   *
   * @param driverSession the CDriverSession that manages the Page lifecycle
   * @example <pre>{@code
   * // Create driver with a new session
   * CDriverSession session = new CDriverSession(provider);
   * CDriver driver = new CDriver(session);
   *
   * // Start automation
   * driver.startSession()
   *       .open("https://example.com")
   *       .$(By.id("search")).type("selenium");
   * }</pre>
   */
  public CDriver(CDriverSession driverSession) {
    this.driverSession = driverSession;
  }

  /**
   * Dynamic property providing access to the current page title with automatic waiting capabilities.
   * This property implements intelligent waiting and retry mechanisms for title retrieval,
   * making it suitable for scenarios where page titles change dynamically or load asynchronously.
   *
   * <p>The Title property automatically waits for the title to become available and provides
   * verification methods for testing scenarios. It uses a default wait interval of 50ms
   * and timeout of {@link #DEFAULT_TIMEOUT} seconds.</p>
   *
   * @example <pre>{@code
   * // Get current page title
   * String title = driver.Title.get();
   *
   * // Wait until title contains specific text
   * driver.Title.waitUntil(title -> title.contains("Dashboard"));
   *
   * // Verify title matches expected value
   * driver.Title.verify().isEqualTo("Login Page");
   *
   * // Wait for title change after navigation
   * driver.click(By.linkText("Profile"));
   * driver.Title.waitUntil(title -> !title.equals("Home Page"));
   * }</pre>
   * @see CDynamicStringExtension
   */
  public final CDynamicStringExtension Title = new CDynamicStringExtension() {
    @Override
    public String _get() {
      return getTitle();
    }

    @Override
    public int getDefaultWaitIntervalInMilliSeconds() {
      return 50;
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return DEFAULT_TIMEOUT;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return " Page Title";
    }
  };

  /**
   * Dynamic property providing access to the current page URL with automatic waiting capabilities.
   * This property implements intelligent waiting and retry mechanisms for URL retrieval,
   * making it suitable for scenarios where URLs change due to redirects, navigation, or AJAX updates.
   *
   * <p>The Url property automatically waits for the URL to become available and provides
   * verification methods for testing scenarios. It uses a default wait interval of 50ms
   * and timeout of {@link #DEFAULT_TIMEOUT} seconds.</p>
   *
   * @example <pre>{@code
   * // Get current page URL
   * String currentUrl = driver.Url.get();
   *
   * // Wait until URL contains specific path
   * driver.Url.waitUntil(url -> url.contains("/dashboard"));
   *
   * // Verify URL matches expected pattern
   * driver.Url.verify().matches("https://example\\.com/.*");
   *
   * // Wait for redirect completion
   * driver.click(By.id("redirect-btn"));
   * driver.Url.waitUntil(url -> url.startsWith("https://target-site.com"));
   * }</pre>
   * @see CDynamicStringExtension
   */
  public final CDynamicStringExtension Url = new CDynamicStringExtension() {
    @Override
    public String _get() {
      return getUrl();
    }

    @Override
    public int getDefaultWaitIntervalInMilliSeconds() {
      return 50;
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return DEFAULT_TIMEOUT;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return " Page Url";
    }
  };

  /**
   * Dynamic property providing access to page screenshots with automatic waiting capabilities.
   * This property captures full-page screenshots using the Shutterbug library, with automatic
   * title embedding and timestamp generation for file naming.
   *
   * <p>The ScreenShot property automatically handles caret hiding, page stabilization,
   * and provides verification methods for visual testing scenarios. It uses a default
   * wait interval of 50ms and timeout of {@link #DEFAULT_TIMEOUT} seconds.</p>
   *
   * @example <pre>{@code
   * // Capture current page screenshot
   * BufferedImage screenshot = driver.ScreenShot.get();
   *
   * // Wait for page to stabilize before screenshot
   * driver.ScreenShot.waitUntil(img -> img != null);
   *
   * // Capture screenshot after specific action
   * driver.click(By.id("show-modal"));
   * BufferedImage modalScreenshot = driver.ScreenShot.get();
   *
   * // Use in visual regression testing
   * driver.open("https://example.com/login");
   * BufferedImage baseline = driver.ScreenShot.get();
   * // Compare with expected baseline image
   * }</pre>
   * @see CScreenShot
   * @see BufferedImage
   */
  public final CScreenShot ScreenShot = new CScreenShot() {
    @Override
    public boolean withWaiter() {
      return true;
    }

    @Override
    public BufferedImage _get() {
      return getScreenShot();
    }

    @Override
    public int getDefaultWaitIntervalInMilliSeconds() {
      return 50;
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return DEFAULT_TIMEOUT;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return " Page Screenshot";
    }
  };

  /**
   * Retrieves the browser type associated with the current driver session.
   * This method returns the browser enumeration value configured in the driver provider.
   *
   * @return the CBrowser enum representing the current browser type, or null if no session exists
   * @example <pre>{@code
   * // Get current browser type
   * CBrowser browser = driver.getBrowser();
   *
   * // Conditional logic based on browser
   * if (browser == CBrowser.CHROME) {
   *     // Chrome-specific operations
   *     driver.executeScript("console.log('Running on Chrome')");
   * } else if (browser == CBrowser.FIREFOX) {
   *     // Firefox-specific operations
   *     driver.executeScript("console.log('Running on Firefox')");
   * }
   *
   * // Browser capability checks
   * boolean supportsWebGL = (browser == CBrowser.CHROME || browser == CBrowser.FIREFOX);
   * }</pre>
   * @see CBrowser
   */
  public CBrowser getBrowser() {
    return driverSession == null ? null : driverSession.getDriverProvider().getBrowser();
  }

  /**
   * Starts a new browser session, terminating any existing session first.
   * This method provides a clean restart of the browser session, ensuring a fresh environment
   * for test execution or automation tasks.
   *
   * <p>The method first calls {@link #quit()} to properly clean up any existing session,
   * then starts a new session using the configured driver provider. This ensures that
   * any previous state, cookies, or cached data is cleared.</p>
   *
   * @return the current CDriver instance for method chaining
   * @example <pre>{@code
   * // Start fresh browser session
   * driver.startSession()
   *       .open("https://example.com")
   *       .$(By.id("username")).type("user123");
   *
   * // Restart session between test cases
   * driver.quit(); // Optional, as startSession() calls this automatically
   * driver.startSession()
   *       .deleteAllCookies()
   *       .open("https://example.com/fresh-start");
   *
   * // Chain multiple operations after session start
   * driver.startSession()
   *       .deleteAllCookies()
   *       .open("https://app.example.com")
   *       .$(By.id("login-form")).waitUntilVisible();
   * }</pre>
   * @see #quit()
   * @see CDriverSession#startSession()
   */
  public CDriver startSession() {
    quit();
    driverSession.startSession();
    return this;
  }

  /**
   * Terminates all active browser sessions and closes the Page.
   * This method performs comprehensive cleanup including alert dismissal, window closing,
   * and driver session reset to ensure proper resource cleanup.
   *
   * <p>The quit process includes the following steps:</p>
   * <ol>
   *   <li>Attempts to close any active alerts (with 1-second timeout)</li>
   *   <li>Closes the current browser window</li>
   *   <li>Quits the entire Page instance</li>
   *   <li>Resets the driver session state</li>
   * </ol>
   *
   * <p>All operations are performed with exception handling to ensure cleanup
   * continues even if individual steps fail, making this method safe to call
   * in any browser state.</p>
   *
   * @example <pre>{@code
   * // Proper cleanup after test completion
   * try {
   *     driver.open("https://example.com");
   *     // Perform test operations...
   * } finally {
   *     driver.quit(); // Ensures cleanup even if test fails
   * }
   *
   * // Cleanup between test methods
   * &#64;AfterEach
   * void cleanup() {
   *     driver.quit();
   * }
   *
   * // Restart browser session
   * driver.quit();
   * driver.startSession(); // Fresh browser instance
   *
   * // Safe to call multiple times
   * driver.quit(); // First call does cleanup
   * driver.quit(); // Subsequent calls are safe no-ops
   * }</pre>
   * @see #startSession()
   * @see CDriverSession#reset()
   */
  public void quit() {
    performActionOnEngine(
        "Quit",
        engine -> {
          if (engine != null) {
            try {
              try {
                engine.close();
              } catch (Exception e) {
                logger.trace("Failed to close page");
              }
              try {
                engine.close();
              } catch (Exception e) {
                logger.trace("Failed to quit page");
              }
            } catch (Throwable ex) {
              logger.trace("Failed to quit driver");
            } finally {
              driverSession.reset();
            }
          }
          return true;
        });
  }

  /**
   * Refreshes the current browser page using the browser's refresh functionality.
   * This method reloads the current page, which can be useful for updating dynamic content
   * or resetting page state during automation.
   *
   * @return the current CDriver instance for method chaining
   * @example <pre>{@code
   * // Simple page refresh
   * driver.refresh();
   *
   * // Refresh and continue with automation
   * driver.open("https://example.com/dynamic-content")
   *       .refresh()
   *       .$(By.id("updated-content")).waitUntilVisible();
   *
   * // Refresh in test scenarios
   * driver.$(By.id("data-input")).type("test data");
   * driver.refresh(); // Clear form and reload page
   * driver.$(By.id("data-input")).verify().isEmpty();
   * }</pre>
   * @see #refresh(Predicate)
   * @see #refresh(Predicate, int, int)
   */
  public final CDriver refresh() {
    return performActionOnEngine(
        "Refresh",
        engine -> {
          engine.refresh();
          return this;
        });
  }

  /**
   * Refreshes the current browser page with a post-condition check using default retry settings.
   * This method refreshes the page and then waits until the specified condition is met,
   * using 3 retry attempts with 1-second intervals.
   *
   * @param postCondition predicate to verify the refresh was successful and page is in expected state
   * @return the current CDriver instance for method chaining
   * @example <pre>{@code
   * // Refresh and wait for specific element to appear
   * driver.refresh(d -> d.$(By.id("refresh-indicator")).isDisplayed());
   *
   * // Refresh and wait for dynamic content to load
   * driver.refresh(d -> d.getTitle().contains("Updated"));
   *
   * // Refresh and wait for timestamp update
   * String oldTimestamp = driver.$(By.id("timestamp")).getText();
   * driver.refresh(d -> !d.$(By.id("timestamp")).getText().equals(oldTimestamp));
   * }</pre>
   * @see #refresh()
   * @see #refresh(Predicate, int, int)
   */
  public final CDriver refresh(Predicate<CDriver> postCondition) {
    return refresh(postCondition, 3, 1000);
  }

  /**
   * Refreshes the current browser page with custom retry settings and post-condition check.
   * This method provides full control over the refresh operation with configurable retry
   * attempts and intervals for post-condition validation.
   *
   * @param postCondition     predicate to verify the refresh was successful and page is in expected state
   * @param retryTimes        number of retry attempts if the post-condition is not met
   * @param intervalInSeconds interval between retries in seconds
   * @return the current CDriver instance for method chaining
   * @example <pre>{@code
   * // Refresh with custom retry settings for slow-loading content
   * driver.refresh(
   *     d -> d.$(By.className("dynamic-content")).isDisplayed(),
   *     5,  // 5 retry attempts
   *     2   // 2 seconds between retries
   * );
   *
   * // Refresh and wait for AJAX content with extended timeout
   * driver.refresh(
   *     d -> d.$(By.id("ajax-result")).getText().contains("Success"),
   *     10, // 10 retry attempts
   *     1   // 1 second intervals
   * );
   *
   * // Refresh with quick polling for fast updates
   * driver.refresh(
   *     d -> !d.$(By.id("loading-spinner")).isDisplayed(),
   *     20, // Many attempts
   *     500 // 500ms intervals for quick polling
   * );
   * }</pre>
   * @see #refresh()
   * @see #refresh(Predicate)
   * @see CRetry#retryIfNot(java.util.function.Function, Predicate, int, int)
   */
  public final CDriver refresh(Predicate<CDriver> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> refresh(), postCondition, retryTimes, intervalInSeconds);
    return this;
  }

  /**
   * Captures a full-page screenshot of the current browser window.
   * This method uses the Shutterbug library to create high-quality screenshots with
   * automatic caret hiding and page title embedding for enhanced screenshot quality.
   *
   * <p>The screenshot process includes:</p>
   * <ul>
   *   <li>Setting input carets to transparent to avoid visual artifacts</li>
   *   <li>Capturing the full page using Shutterbug</li>
   *   <li>Embedding the page title in the screenshot metadata</li>
   *   <li>Generating a timestamp-based name for identification</li>
   * </ul>
   *
   * @return BufferedImage containing the full-page screenshot, or null if capture fails
   * @example <pre>{@code
   * // Capture screenshot for visual verification
   * BufferedImage screenshot = driver.getScreenShot();
   * if (screenshot != null) {
   *     // Save screenshot to file
   *     ImageIO.write(screenshot, "PNG", new File("page-screenshot.png"));
   * }
   *
   * // Use in test reporting
   * driver.open("https://example.com/login");
   * BufferedImage loginPage = driver.getScreenShot();
   * testReport.addScreenshot("Login Page", loginPage);
   *
   * // Capture before and after screenshots for comparison
   * BufferedImage before = driver.getScreenShot();
   * driver.$(By.id("toggle-button")).click();
   * BufferedImage after = driver.getScreenShot();
   *
   * // Visual regression testing
   * driver.open("https://app.example.com/dashboard");
   * BufferedImage actual = driver.getScreenShot();
   * // Compare with baseline image...
   * }</pre>
   * @see BufferedImage
   */
  public BufferedImage getScreenShot() {
    return performActionOnEngine(
        "Get Screenshot",
        engine -> {
          if (engine == null) {
            return null;
          }

          setCaretColorForAllInputs("transparent");

          try {
            return CImageUtil.readImage(engine.screenshot());
          } catch (Exception e) {
            return null;
          }
        });
  }

  /**
   * Retrieves the current page title from the browser.
   * This method safely gets the page title, returning an empty string if no active session exists.
   *
   * @return the current page title, or empty string if no Page session is active
   * @example <pre>{@code
   * // Get page title for verification
   * String title = driver.getTitle();
   * assert title.contains("Login");
   *
   * // Use in conditional logic
   * if (driver.getTitle().equals("Dashboard")) {
   *     // Already on dashboard, proceed with test
   *     driver.$(By.id("user-menu")).click();
   * } else {
   *     // Navigate to dashboard first
   *     driver.$(By.linkText("Dashboard")).click();
   * }
   *
   * // Title-based page detection
   * String currentTitle = driver.getTitle();
   * switch (currentTitle) {
   *     case "Login Page":
   *         handleLoginPage();
   *         break;
   *     case "User Dashboard":
   *         handleDashboardPage();
   *         break;
   *     default:
   *         throw new IllegalStateException("Unknown page: " + currentTitle);
   * }
   *
   * // Wait for title change after navigation
   * String oldTitle = driver.getTitle();
   * driver.$(By.linkText("Profile")).click();
   * // Use with Title dynamic property for waiting
   * driver.Title.waitUntil(title -> !title.equals(oldTitle));
   * }</pre>
   * @see #Title
   */
  @CMcpTool(
      groups = {"web", "driver"},
      name = "driver_get_title",
      title = "Get Page Title",
      description = "Retrieves the current page title from the browser"
  )
  public String getTitle() {
    return performActionOnEngine(
        "Get Title",
        engine -> {
          return engine != null ? engine.title() : CStringUtil.EMPTY;
        });
  }

  /**
   * Retrieves the current page URL from the browser.
   * This method safely gets the current URL, returning an empty string if no active session exists.
   *
   * @return the current page URL, or empty string if no Page session is active
   * @example <pre>{@code
   * // Get current URL for verification
   * String currentUrl = driver.getUrl();
   * assert currentUrl.contains("example.com");
   *
   * // URL-based navigation logic
   * String url = driver.getUrl();
   * if (url.contains("/login")) {
   *     // On login page, perform login
   *     driver.$(By.id("username")).type("user123");
   * } else if (url.contains("/dashboard")) {
   *     // Already logged in, proceed with test
   *     driver.$(By.id("user-menu")).click();
   * }
   *
   * // Extract URL parameters
   * String currentUrl = driver.getUrl();
   * if (currentUrl.contains("?sessionId=")) {
   *     String sessionId = currentUrl.substring(currentUrl.indexOf("sessionId=") + 10);
   *     // Use sessionId for further operations
   * }
   *
   * // Verify redirect completion
   * driver.$(By.id("redirect-button")).click();
   * driver.Url.waitUntil(url -> url.startsWith("https://target-domain.com"));
   * String finalUrl = driver.getUrl();
   * }</pre>
   * @see #Url
   */
  @CMcpTool(
      groups = {"web", "driver"},
      name = "driver_get_url",
      title = "Get Current URL",
      description = "Retrieves the current page URL from the browser"
  )
  public String getUrl() {
    return performActionOnEngine(
        "Get URL",
        engine -> {
          return engine != null ? engine.url() : CStringUtil.EMPTY;
        });
  }

  /**
   * Checks if the browser session is currently active and responsive.
   * This method performs a comprehensive check by verifying both the driver session
   * state and the ability to retrieve the page title successfully.
   *
   * @return true if the session is active and the browser is responsive, false otherwise
   * @example <pre>{@code
   * // Check session before performing operations
   * if (driver.isActive()) {
   *     driver.$(By.id("search")).type("selenium");
   * } else {
   *     driver.startSession();
   *     driver.open("https://example.com");
   * }
   *
   * // Conditional session management
   * if (!driver.isActive()) {
   *     log.info("Session expired, restarting...");
   *     driver.startSession();
   * }
   *
   * // Health check in test setup
   * &#64;BeforeEach
   * void ensureActiveSession() {
   *     if (!driver.isActive()) {
   *         driver.startSession();
   *         driver.open(baseUrl);
   *     }
   * }
   *
   * // Robust error handling
   * try {
   *     if (driver.isActive()) {
   *         // Perform test operations
   *         driver.performComplexOperation();
   *     }
   * } catch (WebDriverException e) {
   *     // Handle session loss
   *     driver.startSession();
   * }
   * }</pre>
   * @see CDriverSession#isActive()
   * @see #getTitle()
   */
  public boolean isActive() {
    try {
      return driverSession.isActive() && !getTitle().isBlank();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout.
   * This method is the primary way to interact with web elements on the page,
   * providing a fluent interface for element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  public <DR extends CDriver> CWebElement<DR> $(CBy locator) {
    return $(locator, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout.
   * This method is the primary way to interact with web elements on the page,
   * providing a fluent interface for element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  public <DR extends CDriver> CWebElement<DR> $(String name, CBy locator) {
    return $(name, locator, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout.
   * This method allows you to specify a custom wait time for element operations,
   * useful for elements that may take longer to appear or become interactive.
   *
   * @param locator the CBy locator to find the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  public <DR extends CDriver> CWebElement<DR> $(CBy locator, int waitSec) {
    return $("Get Element", locator, waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout.
   * This method allows you to specify a custom wait time for element operations,
   * useful for elements that may take longer to appear or become interactive.
   *
   * @param locator the CBy locator to find the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  @SuppressWarnings("unchecked")
  public <DR extends CDriver> CWebElement<DR> $(String name, CBy locator, int waitSec) {
    return $(name, locator.getSelector(), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout.
   * This method is the primary way to interact with web elements on the page,
   * providing a fluent interface for element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  public <DR extends CDriver> CWebElement<DR> $(String locator) {
    return $(locator, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout.
   * This method is the primary way to interact with web elements on the page,
   * providing a fluent interface for element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  public <DR extends CDriver> CWebElement<DR> $(String name, String locator) {
    return $(name, locator, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout.
   * This method allows you to specify a custom wait time for element operations,
   * useful for elements that may take longer to appear or become interactive.
   *
   * @param locator the CBy locator to find the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  public <DR extends CDriver> CWebElement<DR> $(String locator, int waitSec) {
    return $("Get Element", locator, waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout.
   * This method allows you to specify a custom wait time for element operations,
   * useful for elements that may take longer to appear or become interactive.
   *
   * @param locator the CBy locator to find the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  @SuppressWarnings("unchecked")
  public <DR extends CDriver> CWebElement<DR> $(String name, String locator, int waitSec) {
    return new CWebElement<>(name, (DR) this, locator, waitSec);
  }

  /**
   * Retrieves the underlying Page session id for the current driver session.
   * This method safely gets the session id, returning null if no active session exists.
   *
   * @return the current page session id, or null if no Page session is active
   * @example <pre>{@code
   * // Get current session id for verification
   * String sessionId = driver.getSessionId();
   * assert sessionId != null;
   *
   * // Use in logging or debugging
   * log.info("Current session id: " + driver.getSessionId());
   *
   * // Conditional logic based on session id
   * if (driver.getSessionId().equals("expected-session-id")) {
   *     // Perform actions for specific session
   * }
   * }</pre>
   * @see #getTitle()
   * @see #getUrl()
   */
  @CMcpTool(
      groups = {"web", "driver"},
      name = "driver_get_session_id",
      title = "Get Session Id",
      description = "Retrieves the underlying Page session id for the current driver session"
  )
  public String getSessionId() {
    return performActionOnEngine(
        "Get Session Id",
        engine -> engine != null ? engine.getSessionId() : null
    );
  }
}


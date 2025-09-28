package org.catools.web.drivers;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.date.CDate;
import org.catools.common.enums.CPlatform;
import org.catools.common.extensions.types.CDynamicStringExtension;
import org.catools.common.utils.CRetry;
import org.catools.media.model.CScreenShot;
import org.catools.web.controls.CWebElement;
import org.catools.web.enums.CBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.function.Predicate;

/**
 * Main WebDriver wrapper class that provides comprehensive browser automation capabilities.
 * This class implements both {@link CDriverActions} and {@link CDriverNavigation} interfaces,
 * offering a complete set of web interaction, navigation, and utility methods.
 * 
 * <p>CDriver acts as the primary entry point for all browser automation tasks, providing
 * high-level methods for element interaction, page navigation, screenshot capture, and
 * browser session management. It includes dynamic properties for page title, URL, and
 * screenshot access with built-in wait mechanisms.</p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>Complete browser session lifecycle management</li>
 *   <li>Element finding and interaction methods</li>
 *   <li>Navigation capabilities (back/forward/refresh)</li>
 *   <li>Screenshot and page information capture</li>
 *   <li>Alert handling support</li>
 *   <li>Dynamic properties with automatic waiting</li>
 *   <li>Platform and browser detection</li>
 * </ul>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Basic browser automation
 * CDriver driver = new CDriver(session);
 * driver.open("https://example.com")
 *       .$(By.id("username")).type("user123")
 *       .$(By.id("password")).type("password")
 *       .$(By.id("login-btn")).click();
 * 
 * // Screenshot capture
 * BufferedImage screenshot = driver.ScreenShot.get();
 * 
 * // Dynamic property access
 * String pageTitle = driver.Title.get();
 * String currentUrl = driver.Url.get();
 * 
 * // Alert handling
 * driver.getAlert().acceptIfPresent();
 * }</pre>
 * 
 * @author CATools Team
 * @since 1.0
 * @see CDriverActions
 * @see CDriverNavigation
 * @see CWebElement
 */
@Slf4j
public class CDriver implements CDriverActions, CDriverNavigation {

  /**
   * The driver session managing the underlying WebDriver instance and its lifecycle.
   * This session handles browser creation, configuration, and cleanup operations.
   */
  @Getter
  private final CDriverSession driverSession;

  /**
   * Logger instance for this CDriver class, used for debugging and tracing operations.
   */
  @Getter
  private final Logger logger = LoggerFactory.getLogger(CDriver.class);

  /**
   * Creates a new CDriver instance by copying the session from another CDriver.
   * This constructor enables driver instance sharing while maintaining session integrity.
   * 
   * @param driver the source CDriver to copy the session from
   * 
   * @example
   * <pre>{@code
   * // Create a new driver instance sharing the same session
   * CDriver originalDriver = new CDriver(session);
   * CDriver sharedDriver = new CDriver(originalDriver);
   * 
   * // Both drivers operate on the same browser session
   * originalDriver.open("https://example.com");
   * String title = sharedDriver.getTitle(); // Same page title
   * }</pre>
   */
  public CDriver(CDriver driver) {
    this(driver.driverSession);
  }

  /**
   * Creates a new CDriver instance with the specified driver session.
   * This is the primary constructor for creating CDriver instances.
   * 
   * @param driverSession the CDriverSession that manages the WebDriver lifecycle
   * 
   * @example
   * <pre>{@code
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
   * @example
   * <pre>{@code
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
   * 
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
   * @example
   * <pre>{@code
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
   * 
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
   * @example
   * <pre>{@code
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
   * 
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
   * Creates and returns a web alert handler for the current driver instance.
   * This method provides type-safe alert handling with generic support for different driver types.
   * 
   * <p>The alert handler offers comprehensive methods for interacting with JavaScript alerts,
   * confirms, and prompts, including automatic waiting and conditional operations.</p>
   * 
   * @param <DR> the driver type for type-safe method chaining
   * @param <A> the alert type extending CWebAlert
   * @return a new CWebAlert instance configured for this driver
   * 
   * @example
   * <pre>{@code
   * // Handle simple alert
   * driver.getAlert().acceptIfPresent();
   * 
   * // Handle confirmation dialog
   * driver.click(By.id("delete-btn"));
   * driver.getAlert().accept(); // Confirm deletion
   * 
   * // Handle prompt with input
   * driver.click(By.id("prompt-btn"));
   * driver.getAlert().sendKeys("User input").accept();
   * 
   * // Conditional alert handling with timeout
   * boolean alertHandled = driver.getAlert().acceptIfPresent(true, 5);
   * 
   * // Get alert text before handling
   * String alertMessage = driver.getAlert().getText();
   * driver.getAlert().dismiss();
   * }</pre>
   * 
   * @see CWebAlert
   */
  @SuppressWarnings("unchecked")
  public <DR extends CDriver, A extends CWebAlert<DR>> A getAlert() {
    return (A) new CWebAlert<>(this);
  }

  /**
   * Retrieves the browser type associated with the current driver session.
   * This method returns the browser enumeration value configured in the driver provider.
   * 
   * @return the CBrowser enum representing the current browser type, or null if no session exists
   * 
   * @example
   * <pre>{@code
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
   * 
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
   * 
   * @example
   * <pre>{@code
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
   * 
   * @see #quit()
   * @see CDriverSession#startSession()
   */
  public CDriver startSession() {
    quit();
    driverSession.startSession();
    return this;
  }

  /**
   * Retrieves the unique session ID for the current WebDriver session.
   * This session ID is generated by the WebDriver server and can be used for
   * debugging, logging, or advanced driver operations.
   * 
   * <p>The session ID is particularly useful for:</p>
   * <ul>
   *   <li>Debugging WebDriver connections</li>
   *   <li>Logging and tracing browser sessions</li>
   *   <li>Advanced WebDriver server operations</li>
   *   <li>Session management in distributed testing environments</li>
   * </ul>
   * 
   * @return the SessionId for the current WebDriver session, or null if no active session
   * 
   * @example
   * <pre>{@code
   * // Get session ID for logging
   * SessionId sessionId = driver.getSessionId();
   * System.out.println("Current session: " + sessionId);
   * 
   * // Use in distributed testing
   * SessionId currentSession = driver.getSessionId();
   * // Store session ID for later reference or debugging
   * testContext.setSessionId(currentSession);
   * 
   * // Conditional logic based on session existence
   * if (driver.getSessionId() != null) {
   *     // Session is active, proceed with test
   *     driver.open("https://example.com");
   * } else {
   *     // No active session, start new one
   *     driver.startSession();
   * }
   * }</pre>
   * 
   * @see RemoteWebDriver#getSessionId()
   * @see SessionId
   */
  public SessionId getSessionId() {
    return performActionOnDriver("Copy File To Node", RemoteWebDriver::getSessionId);
  }

  /**
   * Terminates all active browser sessions and closes the WebDriver.
   * This method performs comprehensive cleanup including alert dismissal, window closing,
   * and driver session reset to ensure proper resource cleanup.
   * 
   * <p>The quit process includes the following steps:</p>
   * <ol>
   *   <li>Attempts to close any active alerts (with 1-second timeout)</li>
   *   <li>Closes the current browser window</li>
   *   <li>Quits the entire WebDriver instance</li>
   *   <li>Resets the driver session state</li>
   * </ol>
   * 
   * <p>All operations are performed with exception handling to ensure cleanup
   * continues even if individual steps fail, making this method safe to call
   * in any browser state.</p>
   * 
   * @example
   * <pre>{@code
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
   * 
   * @see #startSession()
   * @see CDriverSession#reset()
   */
  public void quit() {
    performActionOnDriver(
        "Quit",
        webDriver -> {
          if (webDriver != null) {
            try {
              try {
                getAlert().closeIfPresent(true, 1);
              } catch (Exception e) {
                logger.trace("Failed to close alert");
              }
              try {
                webDriver.close();
              } catch (Exception e) {
                logger.trace("Failed to close webdriver");
              }
              try {
                webDriver.quit();
              } catch (Exception e) {
                logger.trace("Failed to quit webdriver");
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
   * 
   * @example
   * <pre>{@code
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
   * 
   * @see #refresh(Predicate)
   * @see #refresh(Predicate, int, int)
   */
  public final CDriver refresh() {
    return performActionOnDriver(
        "Refresh",
        webDriver -> {
          webDriver.navigate().refresh();
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
   * 
   * @example
   * <pre>{@code
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
   * 
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
   * @param postCondition predicate to verify the refresh was successful and page is in expected state
   * @param retryTimes number of retry attempts if the post-condition is not met
   * @param intervalInSeconds interval between retries in seconds
   * @return the current CDriver instance for method chaining
   * 
   * @example
   * <pre>{@code
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
   * 
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
   * 
   * @example
   * <pre>{@code
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
   * 
   * @see BufferedImage
   * @see Shutterbug
   */
  public BufferedImage getScreenShot() {
    return performActionOnDriver(
        "Get Screenshot",
        webDriver -> {
          if (webDriver == null || webDriver.getSessionId() == null) {
            return null;
          }

          setCaretColorForAllInputs("transparent");

          try {
            return Shutterbug.shootPage(webDriver)
                .withTitle(getTitle())
                .withName(getTitle() + CDate.now().toTimeStampForFileName())
                .getImage();
          } catch (Exception e) {
            return null;
          }
        });
  }

  /**
   * Retrieves the current page title from the browser.
   * This method safely gets the page title, returning an empty string if no active session exists.
   * 
   * @return the current page title, or empty string if no WebDriver session is active
   * 
   * @example
   * <pre>{@code
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
   * 
   * @see #Title
   */
  public String getTitle() {
    return performActionOnDriver(
        "Get Title",
        webDriver -> {
          return webDriver != null ? webDriver.getTitle() : "";
        });
  }

  /**
   * Retrieves the current page URL from the browser.
   * This method safely gets the current URL, returning an empty string if no active session exists.
   * 
   * @return the current page URL, or empty string if no WebDriver session is active
   * 
   * @example
   * <pre>{@code
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
   * 
   * @see #Url
   */
  public String getUrl() {
    return performActionOnDriver(
        "Get URL",
        webDriver -> {
          return webDriver != null ? webDriver.getCurrentUrl() : "";
        });
  }

  /**
   * Checks if the browser session is currently active and responsive.
   * This method performs a comprehensive check by verifying both the driver session
   * state and the ability to retrieve the page title successfully.
   * 
   * @return true if the session is active and the browser is responsive, false otherwise
   * 
   * @example
   * <pre>{@code
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
   * 
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
   * Retrieves the platform name from the current WebDriver session capabilities.
   * This method extracts platform information from the WebDriver capabilities,
   * which is useful for cross-platform testing and platform-specific operations.
   * 
   * @return CPlatform enum representing the current platform, or null if unavailable
   * 
   * @example
   * <pre>{@code
   * // Get current platform for conditional testing
   * CPlatform platform = driver.getPlatform();
   * 
   * // Platform-specific test logic
   * if (platform == CPlatform.WINDOWS) {
   *     // Windows-specific keyboard shortcuts
   *     driver.sendKeys(Keys.CONTROL, "a"); // Ctrl+A
   * } else if (platform == CPlatform.MAC) {
   *     // macOS-specific keyboard shortcuts
   *     driver.sendKeys(Keys.COMMAND, "a"); // Cmd+A
   * }
   * 
   * // Platform capability validation
   * CPlatform currentPlatform = driver.getPlatform();
   * if (currentPlatform != CPlatform.MOBILE) {
   *     // Desktop-only operations
   *     driver.$(By.id("desktop-feature")).click();
   * }
   * 
   * // Logging platform information
   * CPlatform platform = driver.getPlatform();
   * log.info("Running tests on platform: " + platform);
   * }</pre>
   * 
   * @see CPlatform
   * @see org.openqa.selenium.Capabilities
   */
  public CPlatform getPlatform() {
    return performActionOnDriver("Get Platform name", webDriver ->
        CPlatform.fromName(webDriver.getCapabilities().getCapability("platformName").toString()));
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout.
   * This method is the primary way to interact with web elements on the page,
   * providing a fluent interface for element operations.
   * 
   * @param locator the By locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   * 
   * @example
   * <pre>{@code
   * // Basic element interaction
   * driver.$(By.id("username")).type("user123");
   * driver.$(By.id("password")).type("secret");
   * driver.$(By.id("login-btn")).click();
   * 
   * // Method chaining
   * driver.$(By.id("search-input"))
   *       .clear()
   *       .type("selenium webdriver")
   *       .pressEnter();
   * 
   * // Element verification
   * driver.$(By.id("error-message"))
   *       .verify().isVisible()
   *       .verify().textContains("Invalid credentials");
   * 
   * // Complex locators
   * driver.$(By.xpath("//button[@class='submit' and contains(text(), 'Save')]"))
   *       .waitUntilClickable()
   *       .click();
   * }</pre>
   * 
   * @see CWebElement
   * @see By
   */
  public CWebElement<?> $(By locator) {
    return new CWebElement<>("Get Element", this, locator);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout.
   * This method allows you to specify a custom wait time for element operations,
   * useful for elements that may take longer to appear or become interactive.
   * 
   * @param locator the By locator to find the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   * 
   * @example
   * <pre>{@code
   * // Element with extended timeout for slow-loading content
   * driver.$(By.id("dynamic-content"), 30)
   *       .waitUntilVisible()
   *       .verify().textContains("Loaded");
   * 
   * // Quick timeout for elements that should appear immediately
   * driver.$(By.id("instant-feedback"), 2)
   *       .verify().isDisplayed();
   * 
   * // AJAX content with custom wait time
   * driver.$(By.className("ajax-result"), 15)
   *       .waitUntil(el -> !el.getText().equals("Loading..."))
   *       .click();
   * 
   * // Form submission with longer timeout
   * driver.$(By.id("submit-button"), 45)
   *       .click()
   *       .waitUntil(btn -> btn.getAttribute("disabled") != null);
   * }</pre>
   * 
   * @see CWebElement
   * @see By
   */
  public CWebElement<?> $(By locator, int waitSec) {
    return new CWebElement<>("Get Element", this, locator, waitSec);
  }

  /**
   * Creates a CWebElement wrapper using XPath locator with default timeout.
   * This is a convenience method that automatically wraps the provided XPath string
   * in a By.xpath() locator for easier element selection.
   * 
   * @param xpath XPath expression to locate the target element
   * @return CWebElement wrapper for the XPath-located element
   * 
   * @example
   * <pre>{@code
   * // Simple XPath element selection
   * driver.$("//input[@name='email']").type("user@example.com");
   * 
   * // Complex XPath with text matching
   * driver.$("//button[contains(text(), 'Sign Up')]").click();
   * 
   * // XPath with multiple conditions
   * driver.$("//div[@class='form-group']//input[@type='password']")
   *       .type("secretpassword");
   * 
   * // XPath for table navigation
   * driver.$("//table[@id='data-table']//tr[2]//td[3]")
   *       .verify().textEquals("Expected Value");
   * 
   * // Dynamic XPath with variables (use String.format for dynamic values)
   * String userId = "12345";
   * driver.$(String.format("//user[@id='%s']//button[@action='delete']", userId))
   *       .click();
   * }</pre>
   * 
   * @see #$(By)
   * @see By#xpath(String)
   */
  public CWebElement<?> $(String xpath) {
    return $(By.xpath(xpath));
  }

  /**
   * Creates a CWebElement wrapper using XPath locator with custom timeout.
   * This convenience method combines XPath element location with custom timeout
   * configuration for elements that may require different wait times.
   * 
   * @param xpath XPath expression to locate the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper for the XPath-located element with custom timeout
   * 
   * @example
   * <pre>{@code
   * // Slow-loading dynamic content with XPath
   * driver.$("//div[@class='dynamic-content']//span[@id='result']", 30)
   *       .waitUntilVisible()
   *       .verify().textIsNotEmpty();
   * 
   * // Quick validation with shorter timeout
   * driver.$("//div[@class='validation-error']", 3)
   *       .verify().isVisible();
   * 
   * // Complex form element with extended wait
   * driver.$("//form[@name='checkout']//input[@data-field='credit-card']", 20)
   *       .waitUntilEnabled()
   *       .type("4111111111111111");
   * 
   * // AJAX table row with custom timeout
   * driver.$("//table[@id='results']//tr[contains(@class, 'new-row')]", 25)
   *       .waitUntilPresent()
   *       .$(By.tagName("button"))
   *       .click();
   * }</pre>
   * 
   * @see #$(By, int)
   * @see By#xpath(String)
   */
  public CWebElement<?> $(String xpath, int waitSec) {
    return $(By.xpath(xpath), waitSec);
  }

  /**
   * Creates a CWebElement wrapper using CSS selector locator with default timeout.
   * This is a convenience method that automatically wraps the provided CSS selector
   * in a By.cssSelector() locator for modern web element selection.
   * 
   * @param cssSelector CSS selector expression to locate the target element
   * @return CWebElement wrapper for the CSS selector-located element
   * 
   * @example
   * <pre>{@code
   * // Simple CSS selector usage
   * driver.$$("input[type='email']").type("user@example.com");
   * driver.$$("button.primary").click();
   * 
   * // Complex CSS selectors
   * driver.$$("form#login .form-group:nth-child(2) input")
   *       .type("password123");
   * 
   * // Class and attribute combinations
   * driver.$$(".nav-menu li[data-page='dashboard'] a")
   *       .click();
   * 
   * // CSS pseudo-selectors
   * driver.$$("table tr:first-child td:last-child")
   *       .verify().textContains("Total");
   * 
   * // Modern CSS features
   * driver.$$("div:has(> .error-icon) .error-message")
   *       .verify().isVisible();
   * }</pre>
   * 
   * @see #$(By)
   * @see By#cssSelector(String)
   */
  public CWebElement<?> $$(String cssSelector) {
    return $(By.cssSelector(cssSelector));
  }

  /**
   * Creates a CWebElement wrapper using CSS selector locator with custom timeout.
   * This convenience method combines CSS selector element location with custom timeout
   * configuration for elements that may require different wait times.
   * 
   * @param cssSelector CSS selector expression to locate the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper for the CSS selector-located element with custom timeout
   * 
   * @example
   * <pre>{@code
   * // Dynamic content with CSS selector and extended timeout
   * driver.$$(".loading-spinner", 30)
   *       .waitUntilNotVisible();
   * 
   * // Form validation with custom timeout
   * driver.$$(".form-errors .field-error", 5)
   *       .verify().isDisplayed();
   * 
   * // AJAX content loading
   * driver.$$("[data-testid='search-results'] .result-item", 20)
   *       .waitUntilPresent()
   *       .verify().countEquals(10);
   * 
   * // Interactive element with longer wait
   * driver.$$("button[aria-label='Save Changes']:not([disabled])", 15)
   *       .waitUntilClickable()
   *       .click();
   * 
   * // Complex nested selectors with timeout
   * driver.$$(".modal.active .modal-content form input[name='confirmation']", 10)
   *       .type("CONFIRM");
   * }</pre>
   * 
   * @see #$(By, int)
   * @see By#cssSelector(String)
   */
  public CWebElement<?> $$(String cssSelector, int waitSec) {
    return $(By.cssSelector(cssSelector), waitSec);
  }
}

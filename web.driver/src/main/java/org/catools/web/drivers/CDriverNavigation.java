package org.catools.web.drivers;

import org.catools.common.extensions.verify.CVerify;
import org.catools.common.utils.CRetry;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Interface providing navigation capabilities for web drivers, extending CDriverWaiter.
 * This interface offers methods for browser navigation (back/forward), window/frame switching,
 * and engine opening operations.
 *
 * <p>All navigation methods support method chaining and provide overloaded versions
 * with retry mechanisms and post-condition validation.</p>
 *
 * @author CATools Team
 * @since 1.0
 */
public interface CDriverNavigation extends CDriverWaiter {
  /**
   * Navigates the browser back to the previous engine in the browsing history.
   *
   * @param <T> the type of the implementing class for method chaining
   * @return the current instance for method chaining
   * @example <pre>
   * // Navigate back one engine
   * driver.back();
   *
   * // Method chaining example
   * driver.open("https://example.com")
   *       .back()
   *       .forward();
   * </pre>
   */
  default <T extends CDriverNavigation> T back() {
    return performActionOnEngine(
        "Back",
        engine -> {
          if (engine != null) engine.goBack();
          return (T) this;
        });
  }

  /**
   * Navigates the browser back to the previous engine with a post-condition check.
   * Uses default retry settings: 3 attempts with 1 second intervals.
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param postCondition predicate to verify the navigation was successful
   * @return the current instance for method chaining
   * @example <pre>
   * // Navigate back and wait until a specific element is visible
   * driver.back(nav -> nav.isElementPresent(By.id("login-form")));
   *
   * // Navigate back and verify URL contains expected text
   * driver.back(nav -> nav.getCurrentUrl().contains("login"));
   * </pre>
   */
  default <T extends CDriverNavigation> T back(Predicate<CDriverNavigation> postCondition) {
    return back(postCondition, 3, 1000);
  }

  /**
   * Navigates the browser back to the previous engine with custom retry settings and post-condition check.
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param postCondition     predicate to verify the navigation was successful
   * @param retryTimes        number of retry attempts
   * @param intervalInSeconds interval between retries in seconds
   * @return the current instance for method chaining
   * @example <pre>
   * // Navigate back with custom retry settings
   * driver.back(
   *     nav -> nav.getTitle().equals("Home Page"),
   *     5,  // 5 retry attempts
   *     2   // 2 seconds between retries
   * );
   *
   * // Navigate back and verify specific element state
   * driver.back(
   *     nav -> nav.isElementEnabled(By.id("submit-btn")),
   *     10,
   *     500  // 500ms intervals
   * );
   * </pre>
   */
  default <T extends CDriverNavigation> T back(
      Predicate<CDriverNavigation> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> back(), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Navigates the browser forward to the next engine in the browsing history.
   *
   * @param <T> the type of the implementing class for method chaining
   * @return the current instance for method chaining
   * @example <pre>
   * // Navigate forward one engine
   * driver.forward();
   *
   * // Method chaining example
   * driver.back()
   *       .forward()
   *       .refresh();
   * </pre>
   */
  default <T extends CDriverNavigation> T forward() {
    return performActionOnEngine(
        "Forward",
        engine -> {
          if (engine != null) engine.goForward();
          return (T) this;
        });
  }

  /**
   * Navigates the browser forward to the next engine with a post-condition check.
   * Uses default retry settings: 3 attempts with 1 second intervals.
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param postCondition predicate to verify the navigation was successful
   * @return the current instance for method chaining
   * @example <pre>
   * // Navigate forward and wait until engine title changes
   * driver.forward(nav -> nav.getTitle().contains("Dashboard"));
   *
   * // Navigate forward and verify specific content is loaded
   * driver.forward(nav -> nav.isElementVisible(By.className("content-area")));
   * </pre>
   */
  default <T extends CDriverNavigation> T forward(Predicate<CDriverNavigation> postCondition) {
    return forward(postCondition, 3, 1000);
  }

  /**
   * Navigates the browser forward to the next engine with custom retry settings and post-condition check.
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param postCondition     predicate to verify the navigation was successful
   * @param retryTimes        number of retry attempts
   * @param intervalInSeconds interval between retries in seconds
   * @return the current instance for method chaining
   * @example <pre>
   * // Navigate forward with custom retry settings
   * driver.forward(
   *     nav -> nav.getCurrentUrl().endsWith("/profile"),
   *     7,   // 7 retry attempts
   *     1500 // 1.5 seconds between retries
   * );
   *
   * // Navigate forward and wait for AJAX content to load
   * driver.forward(
   *     nav -> nav.isElementPresent(By.id("ajax-content")),
   *     15,
   *     1000
   * );
   * </pre>
   */
  default <T extends CDriverNavigation> T forward(
      Predicate<CDriverNavigation> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> forward(), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Switches to a browser window/tab with the specified title using regex matching.
   * If the current window already has the matching title, no switch occurs.
   *
   * @param title the title pattern to match (supports regex)
   * @example <pre>
   * // Switch to window with exact title
   * driver.switchToPage("User Profile");
   *
   * // Switch to window using regex pattern
   * driver.switchToPage(".*Settings.*");
   *
   * // Switch to window with title containing specific text
   * driver.switchToPage("Gmail.*");
   * </pre>
   */
  default void switchToPage(String title) {
    performActionOnEngine(
        "Switch To Page",
        engine -> {
          if (engine != null) engine.switchToPage(title);
          return true;
        });
  }

  /**
   * Switches to the first opened browser window/tab.
   * This is typically the original window that was opened when the session started.
   *
   * @example <pre>
   * // Open multiple windows and switch back to the first one
   * driver.open("https://example.com");
   * driver.openNewWindow("https://google.com");
   * driver.openNewWindow("https://github.com");
   * driver.switchToFirstPage(); // Back to example.com
   *
   * // Useful after popup handling
   * driver.handlePopup();
   * driver.switchToFirstPage(); // Return to main window
   * </pre>
   */
  default void switchToFirstPage() {
    performActionOnEngine(
        "Switch To First Page",
        engine -> {
          if (engine != null) engine.switchToPage(0);
          return true;
        });
  }

  /**
   * Switches to the most recently opened browser window/tab.
   * This is useful for handling new windows opened by JavaScript or link clicks.
   *
   * @example <pre>
   * // Handle new window opened by clicking a link
   * driver.click(By.linkText("Open in New Window"));
   * driver.switchToLastPage(); // Switch to the newly opened window
   *
   * // Multiple windows scenario
   * driver.open("https://example.com");
   * driver.openNewWindow("https://google.com");
   * driver.openNewWindow("https://github.com");
   * driver.switchToLastPage(); // Now on github.com
   * </pre>
   */
  default void switchToLastPage() {
    performActionOnEngine(
        "Switch To Last Page",
        engine -> {
          if (engine != null) engine.switchToLastPage();
          return true;
        });
  }

  /**
   * Switches to a different browser window/tab (not the current one).
   * If only one window is open, the method returns false and no switch occurs.
   *
   * @example <pre>
   * // Toggle between two windows
   * driver.open("https://example.com");
   * driver.openNewWindow("https://google.com");
   * driver.switchToNextPage(); // Switch to example.com if currently on google.com
   *
   * // Useful for alternating between windows in tests
   * driver.switchToNextPage();
   * driver.performAction();
   * driver.switchToNextPage(); // Back to original window
   * driver.verifyResult();
   * </pre>
   */
  default void switchToNextPage() {
    performActionOnEngine(
        "Switch To Next Page",
        engine -> {
          if (engine != null) engine.switchToNextPage();
          return true;
        });
  }

  /**
   * Switches to a frame using the frame's name or id attribute.
   * Uses the default timeout for frame availability.
   *
   * @param frameName the name or id of the target frame
   * @example <pre>
   * // Switch to frame by name
   * driver.switchToFrame("content-frame");
   *
   * // Switch to frame by id
   * driver.switchToFrame("main-content");
   *
   * // Perform actions within the frame
   * driver.switchToFrame("login-frame")
   *       .type(By.id("username"), "user123")
   *       .type(By.id("password"), "pass123");
   * </pre>
   */
  default void switchToFrame(String frameName) {
    switchToFrame(frameName, DEFAULT_TIMEOUT);
  }

  /**
   * Switches to a frame using the frame's name or id attribute with a custom timeout.
   *
   * @param frameName the name or id of the target frame
   * @param waitSec   the maximum time to wait for the frame to be available (in seconds)
   * @example <pre>
   * // Switch to frame with 10-second timeout
   * driver.switchToFrame("slow-loading-frame", 10);
   *
   * // Switch to frame with shorter timeout for quick operations
   * driver.switchToFrame("instant-frame", 2);
   *
   * // Handle dynamically created frames
   * driver.click(By.id("create-frame-btn"));
   * driver.switchToFrame("dynamic-frame", 15); // Wait up to 15 seconds
   * </pre>
   */
  default void switchToFrame(String frameName, int waitSec) {
    waitUntil("Switch To Frame", waitSec, engine -> {
      if (engine != null) engine.switchToFrame(frameName);
      return true;
    });
  }

  /**
   * Switches back to the default content (main document) from any frame context.
   * This is equivalent to switching out of all frames back to the top-level document.
   *
   * @example <pre>
   * // Switch to frame, perform actions, then return to main content
   * driver.switchToFrame("content-frame")
   *       .type(By.id("message"), "Hello World")
   *       .click(By.id("submit"))
   *       .switchToDefaultContent();
   *
   * // Navigate through nested frames and return to main
   * driver.switchToFrame(0)
   *       .switchToFrame("nested-frame")
   *       .performAction()
   *       .switchToDefaultContent(); // Back to main document
   *
   * // Essential for multi-frame workflows
   * driver.switchToFrame("frame1").getData()
   *       .switchToDefaultContent()
   *       .switchToFrame("frame2").processData();
   * </pre>
   */
  default void switchToDefaultContent() {
    performActionOnEngine(
        "Switch To Default Content",
        engine -> {
          engine.switchToDefaultContent();
          return true;
        });
  }

  /**
   * Opens a web engine at the specified URL without returning a engine object.
   * If no browser session is active, a new session will be started automatically.
   *
   * @param url the URL to navigate to (must not be blank)
   * @example <pre>
   * // Open a simple webpage
   * driver.open("https://example.com");
   *
   * // Open a specific engine with parameters
   * driver.open("https://example.com/search?q=selenium");
   *
   * // Open local file
   * driver.open("file:///path/to/local/engine.html");
   *
   * // Chain navigation operations
   * driver.open("https://example.com")
   *       .click(By.linkText("Login"))
   *       .type(By.id("username"), "user123");
   * </pre>
   */
  default void open(String url) {
    open(url, null);
  }

  /**
   * Opens a web engine at the specified URL and returns an instance of the expected engine object.
   * If no browser session is active, a new session will be started automatically.
   *
   * @param <P>          the type of the engine object to return
   * @param url          the URL to navigate to (must not be blank)
   * @param expectedPage supplier that creates and returns the expected engine object instance
   * @return the engine object instance created by the supplier, or null if supplier is null
   * @example <pre>
   * // Open engine and get engine object
   * LoginPage loginPage = driver.open("https://example.com/login", () -> new LoginPage(driver));
   *
   * // Chain with engine object methods
   * DashboardPage dashboard = driver.open("https://example.com/login", LoginPage::new)
   *                                 .login("user123", "password")
   *                                 .getDashboard();
   *
   * // Using method reference
   * HomePage homePage = driver.open("https://example.com", () -> new HomePage());
   *
   * // Lambda with initialization
   * ProductPage productPage = driver.open("https://shop.example.com/products", () -> {
   *     ProductPage engine = new ProductPage(driver);
   *     engine.waitForPageLoad();
   *     return engine;
   * });
   * </pre>
   */
  default <P> P open(String url, Supplier<P> expectedPage) {
    CVerify.String.isNotBlank(url, "URL is not blank.");
    if (!isSessionStarted()) getDriverSession().startSession();
    return performActionOnEngine(
        "Open",
        engine -> {
          engine.open(url);
          return expectedPage == null ? null : expectedPage.get();
        });
  }

  private boolean isSessionStarted() {
    return performActionOnEngine("Open", Objects::nonNull) != null;
  }
}

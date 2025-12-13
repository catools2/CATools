package org.catools.web.drivers;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.options.Cookie;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CSleeper;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;

import java.util.List;
import java.util.function.Predicate;

/**
 * CDriverActions provides a comprehensive set of web driver action methods for interacting with web elements.
 * This interface extends CDriverWaiter and offers a fluent API for performing various user actions such as
 * clicking, typing, scrolling, and managing cookies. All methods support method chaining for improved readability.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CDriverActions driver = new MyDriverImplementation();
 * driver.click("#button", 5)
 *       .sendKeys("#input", 3, "Hello World")
 *       .pressEnter()
 *       .scrollIntoView("#footer", true);
 * }</pre>
 *
 * @since 1.0
 */
@SuppressWarnings({"unchecked"})
public interface CDriverActions extends CDriverWaiter {

  /**
   * Presses the ENTER key on the current page.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEnter();
   * }</pre>
   *
   * @param <T> the type of the implementing class for method chaining
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEnter() {
    return performActionOnEngine(
        "Press ENTER",
        engine -> {
          engine.pressEnter();
          return (T) this;
        });
  }

  /**
   * Presses the ENTER key and waits for the specified number of seconds.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEnter(2); // Press ENTER and wait 2 seconds
   * }</pre>
   *
   * @param <T>                 the type of the implementing class for method chaining
   * @param waitAfterPressEnter number of seconds to wait after pressing ENTER
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEnter(int waitAfterPressEnter) {
    pressEnter();
    CSleeper.sleepTightInSeconds(waitAfterPressEnter);
    return (T) this;
  }

  /**
   * Presses the ENTER key and retries until the post-condition is met.
   * Uses default retry settings (3 retries, 1000ms interval).
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEnter(d -> d.isElementVisible(By.id("success-message")));
   * }</pre>
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param postCondition condition to check after pressing ENTER
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEnter(Predicate<CDriverActions> postCondition) {
    return pressEnter(postCondition, 3, 1000);
  }

  /**
   * Presses the ENTER key and retries until the post-condition is met with custom retry settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEnter(
   *     d -> d.isElementVisible(By.id("loading")),
   *     5,    // retry 5 times
   *     2000  // wait 2 seconds between retries
   * );
   * }</pre>
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param postCondition     condition to check after pressing ENTER
   * @param retryTimes        number of retry attempts
   * @param intervalInSeconds interval between retries in seconds
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEnter(
      Predicate<CDriverActions> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> pressEnter(), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Presses the ESCAPE key on the current engine.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEscape(); // Close modal or cancel operation
   * }</pre>
   *
   * @param <T> the type of the implementing class for method chaining
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEscape() {
    return performActionOnEngine(
        "Press Scape",
        engine -> {
          engine.pressEscape();
          return (T) this;
        });
  }

  /**
   * Presses the ESCAPE key and waits for the specified number of seconds.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEscape(1); // Press ESCAPE and wait 1 second
   * }</pre>
   *
   * @param <T>                  the type of the implementing class for method chaining
   * @param waitAfterPressEscape number of seconds to wait after pressing ESCAPE
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEscape(int waitAfterPressEscape) {
    pressEscape();
    CSleeper.sleepTightInSeconds(waitAfterPressEscape);
    return (T) this;
  }

  /**
   * Presses the ESCAPE key and retries until the post-condition is met.
   * Uses default retry settings (3 retries, 1000ms interval).
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEscape(d -> !d.isElementVisible(By.className("modal")));
   * }</pre>
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param postCondition condition to check after pressing ESCAPE
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEscape(Predicate<CDriverActions> postCondition) {
    return pressEscape(postCondition, 3, 1000);
  }

  /**
   * Presses the ESCAPE key and retries until the post-condition is met with custom retry settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressEscape(
   *     d -> !d.isElementVisible(By.className("modal")),
   *     5,    // retry 5 times
   *     500   // wait 500ms between retries
   * );
   * }</pre>
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param postCondition     condition to check after pressing ESCAPE
   * @param retryTimes        number of retry attempts
   * @param intervalInSeconds interval between retries in seconds
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressEscape(
      Predicate<CDriverActions> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> pressEscape(), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Presses the TAB key on the current engine to navigate to the next focusable element.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressTab(); // Move focus to next form field
   * }</pre>
   *
   * @param <T> the type of the implementing class for method chaining
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressTab() {
    return performActionOnEngine(
        "Press Tab",
        engine -> {
          engine.sendKeys("Tab");
          return (T) this;
        });
  }

  /**
   * Presses the TAB key and waits for the specified number of seconds.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressTab(1); // Press TAB and wait 1 second for focus change
   * }</pre>
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param waitAfterPressTab number of seconds to wait after pressing TAB
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressTab(int waitAfterPressTab) {
    pressTab();
    CSleeper.sleepTightInSeconds(waitAfterPressTab);
    return (T) this;
  }

  /**
   * Presses the TAB key and retries until the post-condition is met.
   * Uses default retry settings (3 retries, 1000ms interval).
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressTab(d -> d.isElementFocused(By.id("next-field")));
   * }</pre>
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param postCondition condition to check after pressing TAB
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressTab(Predicate<CDriverActions> postCondition) {
    return pressTab(postCondition, 3, 1000);
  }

  /**
   * Presses the TAB key and retries until the post-condition is met with custom retry settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressTab(
   *     d -> d.isElementFocused(By.id("submit-button")),
   *     3,   // retry 3 times
   *     500  // wait 500ms between retries
   * );
   * }</pre>
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param postCondition     condition to check after pressing TAB
   * @param retryTimes        number of retry attempts
   * @param intervalInSeconds interval between retries in seconds
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressTab(
      Predicate<CDriverActions> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> pressTab(), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Clicks an element after scrolling it into view. This is the standard click method
   * that handles most common clicking scenarios.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.click("xpath=//button[@type='submit']", 10);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator (Playwright selector) to find the element to click
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T click(String locator, int waitSec) {
    waitUntil(
        "Click",
        waitSec,
        engine -> {
          engine.click(locator);
          return true;
        });
    return (T) this;
  }

  /**
   * Clicks an element and retries until the post-condition is met.
   * Uses default retry settings (3 retries, 1000ms interval).
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.click(
   *     By.id("next-engine"),
   *     5,
   *     d -> d.isElementVisible(By.id("engine-2-content"))
   * );
   * }</pre>
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param locator       the locator to find the element to click
   * @param waitSec       maximum time in seconds to wait for the element
   * @param postCondition condition to check after clicking
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T click(
      String locator, int waitSec, Predicate<CDriverActions> postCondition) {
    return click(locator, waitSec, postCondition, 3, 1000);
  }

  /**
   * Clicks an element and retries until the post-condition is met with custom retry settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.click(
   *     By.id("submit-form"),
   *     5,
   *     d -> d.isElementVisible(By.id("success-message")),
   *     5,    // retry 5 times
   *     2000  // wait 2 seconds between retries
   * );
   * }</pre>
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param locator           the locator to find the element to click
   * @param waitSec           maximum time in seconds to wait for the element
   * @param postCondition     condition to check after clicking
   * @param retryTimes        number of retry attempts
   * @param intervalInSeconds interval between retries in seconds
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T click(
      String locator,
      int waitSec,
      Predicate<CDriverActions> postCondition,
      int retryTimes,
      int intervalInSeconds) {
    CRetry.retryIfNot(
        integer -> click(locator, waitSec), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Performs a mouse click on an element using the default timeout.
   * This method moves to the element and then performs a mouse click action.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.mouseClick(By.id("menu-item"));
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to click
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T mouseClick(String locator) {
    return mouseClick(locator, DEFAULT_TIMEOUT);
  }

  /**
   * Performs a mouse click on an element with a specified wait time.
   * This method moves to the element and then performs a mouse click action.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.mouseClick(By.className("dropdown-toggle"), 5);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to click
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T mouseClick(String locator, int waitSec) {
    return waitUntil(
        "Mouse Click",
        waitSec,
        engine -> {
          engine.mouseClick(locator);
          return (T) this;
        });
  }

  /**
   * Performs a mouse double-click on an element using the default timeout.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.mouseDoubleClick(By.id("file-item")); // Double-click to open file
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to double-click
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T mouseDoubleClick(String locator) {
    return mouseDoubleClick(locator, DEFAULT_TIMEOUT);
  }

  /**
   * Performs a mouse double-click on an element with a specified wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.mouseDoubleClick(By.xpath("//div[@class='selectable-item']"), 3);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to double-click
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T mouseDoubleClick(String locator, int waitSec) {
    return waitUntil(
        "Double Mouse Click",
        waitSec,
        engine -> {
          engine.mouseDoubleClick(locator);
          return (T) this;
        });
  }

  /**
   * Moves the mouse cursor to the center of an element using the default timeout.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.moveTo(By.id("hover-menu")); // Show hover menu
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to move to
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T moveTo(String locator) {
    return moveTo(locator, 0, 0, DEFAULT_TIMEOUT);
  }

  /**
   * Moves the mouse cursor to an element with specified offset from its center.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.moveTo(By.id("canvas"), 50, -20); // Move 50px right, 20px up from center
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to move to
   * @param xOffset horizontal offset from the element's center
   * @param yOffset vertical offset from the element's center
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T moveTo(String locator, int xOffset, int yOffset) {
    return moveTo(locator, xOffset, yOffset, DEFAULT_TIMEOUT);
  }

  /**
   * Moves the mouse cursor to the center of an element with a specified wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.moveTo(By.className("tooltip-trigger"), 5);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to move to
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T moveTo(String locator, int waitSec) {
    return moveTo(locator, 0, 0, waitSec);
  }

  /**
   * Moves the mouse cursor to an element with specified offset and wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.moveTo(By.id("slider"), 100, 0, 3); // Move to slider handle position
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to move to
   * @param xOffset horizontal offset from the element's center
   * @param yOffset vertical offset from the element's center
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T moveTo(String locator, int xOffset, int yOffset, int waitSec) {
    moveToElement(locator, xOffset, yOffset, waitSec);
    return (T) this;
  }

  /**
   * Scrolls an element into view using the default timeout.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollIntoView(By.id("footer"), true); // Scroll to footer
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to scroll into view
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollIntoView(String locator) {
    return scrollIntoView(locator, DEFAULT_TIMEOUT);
  }

  /**
   * Scrolls an element into view with a specified wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollIntoView(By.xpath("//section[@id='contact']"), false, 5);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to scroll into view
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollIntoView(String locator, int waitSec) {
    waitUntil(
        "Scroll Into View",
        waitSec,
        engine -> {
          engine.scrollIntoView(locator);
          return null;
        });
    return (T) this;
  }

  /**
   * Scrolls an element horizontally to the left by the specified amount.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollLeft(By.id("horizontal-scroll-container"), 100);
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the scrollable element
   * @param scrollSize the number of pixels to scroll left
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollLeft(String locator, int scrollSize) {
    return scrollLeft(locator, scrollSize, DEFAULT_TIMEOUT);
  }

  /**
   * Scrolls an element horizontally to the left by the specified amount with a wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollLeft(By.className("carousel"), 200, 3);
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the scrollable element
   * @param scrollSize the number of pixels to scroll left
   * @param waitSec    maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollLeft(String locator, int scrollSize, int waitSec) {
    executeScript(String.format("document.querySelector('%s').scrollLeft-=arguments[0];", locator), scrollSize);
    return (T) this;
  }

  /**
   * Scrolls an element horizontally to the right by the specified amount.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollRight(By.id("table-container"), 150);
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the scrollable element
   * @param scrollSize the number of pixels to scroll right
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollRight(String locator, int scrollSize) {
    return scrollRight(locator, scrollSize, DEFAULT_TIMEOUT);
  }

  /**
   * Scrolls an element horizontally to the right by the specified amount with a wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollRight(By.id("timeline"), 300, 2);
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the scrollable element
   * @param scrollSize the number of pixels to scroll right
   * @param waitSec    maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollRight(String locator, int scrollSize, int waitSec) {
    executeScript(String.format("document.querySelector('%s').scrollLeft+=arguments[0];", locator), scrollSize);
    return (T) this;
  }

  /**
   * Scrolls an element into view and then clicks it.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollIntoViewAndClick(By.id("submit-button"), true);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollIntoViewAndClick(
      String locator, int waitSec) {
    scrollIntoView(locator, waitSec);
    return click(locator, waitSec);
  }

  /**
   * Presses the key on the current page.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressKey();
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator of the element to send the key to
   * @param key     the key to press
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressKey(String locator, String key) {
    return performActionOnEngine(
        "Press ENTER",
        engine -> {
          engine.press(locator, key);
          return (T) this;
        });
  }

  /**
   * Presses the key and waits for the specified number of seconds.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressKey(2); // Press and wait 2 seconds
   * }</pre>
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param locator           the locator of the element to send the key to
   * @param key               the key to press
   * @param waitAfterPressKey of seconds to wait after pressing ENTER
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressKey(String locator, String key, int waitAfterPressKey) {
    pressKey(locator, key);
    CSleeper.sleepTightInSeconds(waitAfterPressKey);
    return (T) this;
  }

  /**
   * Presses the key and retries until the post-condition is met.
   * Uses default retry settings (3 retries, 1000ms interval).
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressKey(d -> d.isElementVisible(By.id("success-message")));
   * }</pre>
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param locator       the locator of the element to send the key to
   * @param key           the key to press
   * @param postCondition condition to check after pressing ENTER
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressKey(String locator, String key, Predicate<CDriverActions> postCondition) {
    return pressKey(locator, key, postCondition, 3, 1000);
  }

  /**
   * Presses the key and retries until the post-condition is met with custom retry settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.pressKey(
   *     d -> d.isElementVisible(By.id("loading")),
   *     5,    // retry 5 times
   *     2000  // wait 2 seconds between retries
   * );
   * }</pre>
   *
   * @param <T>               the type of the implementing class for method chaining
   * @param locator           the locator of the element to send the key to
   * @param key               the key to press
   * @param postCondition     condition to check after pressing ENTER
   * @param retryTimes        number of retry attempts
   * @param intervalInSeconds interval between retries in seconds
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T pressKey(String locator, String key,
                                                Predicate<CDriverActions> postCondition, int retryTimes, int intervalInSeconds) {
    CRetry.retryIfNot(integer -> pressKey(locator, key), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Sends keys to a specific element after waiting for it to be ready.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.sendKeys(By.id("username"), 5, "john.doe@example.com");
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the element
   * @param waitSec    maximum time in seconds to wait for the element
   * @param keysToSend the keys to send to the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T sendKeys(String locator, int waitSec, String keysToSend) {
    return waitUntil(
        "Send Keys",
        waitSec,
        engine -> {
          engine.sendKeys(locator, keysToSend);
          return (T) this;
        });
  }

  /**
   * Sends keys to the currently focused element.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.sendKeys("Hello", Keys.TAB, "World");
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param keysToSend the keys to send
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T sendKeys(String keysToSend) {
    return performActionOnEngine(
        "Send Keys",
        engine -> {
          engine.sendKeys(keysToSend);
          return (T) this;
        });
  }

  /**
   * Deletes all cookies from the current domain.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.deleteAllCookies(); // Clear all cookies for current domain
   * }</pre>
   *
   * @param <T> the type of the implementing class for method chaining
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T deleteAllCookies() {
    return performActionOnEngine(
        "Delete All Cookies",
        engine -> {
          engine.deleteAllCookies();
          return (T) this;
        });
  }

  /**
   * Retrieves a cookie by name from the current domain.
   *
   * <p>Example:</p>
   * <pre>{@code
   * Cookie sessionCookie = driver.getCookie("JSESSIONID");
   * if (sessionCookie != null) {
   *     System.out.println("Session value: " + sessionCookie.getValue());
   * }
   * }</pre>
   *
   * @param name the name of the cookie to retrieve
   * @return the Cookie object, or null if not found
   */
  default Cookie getCookie(String name) {
    return performActionOnEngine(
        "Get Cookie",
        engine -> engine.getCookie(name));
  }

  /**
   * Retrieves all cookies from the current domain.
   *
   * <p>Example:</p>
   * <pre>{@code
   * List<Cookie> allCookies = driver.getCookies();
   * allCookies.forEach(cookie ->
   *     System.out.println(cookie.getName() + "=" + cookie.getValue())
   * );
   * }</pre>
   *
   * @return a List of all cookies for the current domain
   */
  default List<Cookie> getCookies() {
    return performActionOnEngine("Get Cookies", CDriverEngine::getCookies);
  }

  /**
   * Adds a simple cookie with name and value to the current domain.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.addCookie("user_preference", "dark_theme");
   * }</pre>
   *
   * @param name  the name of the cookie
   * @param value the value of the cookie
   * @return the Cookie object that was added
   */
  default Cookie addCookie(String name, String value) {
    return addCookie(new Cookie(name, value));
  }

  /**
   * Adds a cookie object to the current domain.
   *
   * <p>Example:</p>
   * <pre>{@code
   * Cookie customCookie = new Cookie.Builder("session_id", "abc123")
   *     .domain(".example.com")
   *     .path("/")
   *     .isSecure(true)
   *     .build();
   * driver.addCookie(customCookie);
   * }</pre>
   *
   * @param cookie the Cookie object to add
   * @return the Cookie object that was added
   */
  default Cookie addCookie(Cookie cookie) {
    return performActionOnEngine(
        "Add Cookie",
        engine -> engine.addCookie(cookie));
  }

  /**
   * Sets the caret color for all input elements on the engine.
   * This is useful for improving visibility during automated testing.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.setCaretColorForAllInputs("red"); // Make cursor red in all inputs
   * }</pre>
   *
   * @param <T>   the type of the implementing class for method chaining
   * @param color the CSS color value for the caret
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T setCaretColorForAllInputs(String color) {
    return setStyleForAll("input", "caret-color", color);
  }

  /**
   * Sets a CSS style property for all elements matching the given selector.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.setStyleForAll("button", "border", "2px solid red");
   * driver.setStyleForAll(".highlight", "background-color", "yellow");
   * }</pre>
   *
   * @param <T>   the type of the implementing class for method chaining
   * @param xpath the CSS selector for elements to modify
   * @param style the CSS property name
   * @param value the CSS property value
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T setStyleForAll(String xpath, String style, String value) {
    executeScript(
        String.format(
            "document.querySelectorAll(\"%s\").forEach(function(a) { a.style[\"%s\"]=\"%s\"; });",
            xpath, style, value));
    return (T) this;
  }

  /**
   * Executes JavaScript code in the browser context.
   *
   * <p>Example:</p>
   * <pre>{@code
   * String title = driver.executeScript("return document.title;");
   * driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
   *
   * // With arguments
   * WebElement element = driver.getElement(By.id("myElement"));
   * driver.executeScript("arguments[0].style.backgroundColor = 'red';", element);
   * }</pre>
   *
   * @param <R>    the return type of the script execution
   * @param script the JavaScript code to execute
   * @param args   optional arguments to pass to the script
   * @return the result of the script execution
   */
  @CMcpTool(
      groups = {"web", "driver"},
      name = "driver_execute_script",
      title = "Execute JavaScript",
      description = "Executes JavaScript code in the browser context"
  )
  default <R> R executeScript(String script, Object... args) {
    return performActionOnEngine(
        "Execute Script",
        engine -> engine.executeScriptOnDriver(script, args));
  }

  /**
   * Executes JavaScript code on a specific element after waiting for it.
   *
   * <p>Example:</p>
   * <pre>{@code
   * String innerHTML = driver.executeScript(
   *     By.id("content"),
   *     5,
   *     "return arguments[0].innerHTML;"
   * );
   * }</pre>
   *
   * @param <R>     the return type of the script execution
   * @param locator the locator to find the element
   * @param waitSec maximum time in seconds to wait for the element
   * @param script  the JavaScript code to execute (element will be passed as arguments[0])
   * @return the result of the script execution
   */
  @CMcpTool(
      groups = {"web", "driver"},
      name = "driver_execute_script_on_element",
      title = "Execute JavaScript on Element",
      description = "Executes JavaScript code on a specific element after waiting for it"
  )
  default <R> R executeScript(
      @CMcpToolParam(name = "locator", description = "The locator to find the element")
      String locator,
      @CMcpToolParam(name = "waitSec", description = "Maximum time in seconds to wait for the element")
      int waitSec,
      @CMcpToolParam(name = "script", description = "The JavaScript code to execute (element will be passed as arguments[0])")
      String script) {
    return waitUntil("Execute Script", waitSec, engine -> engine.executeScript(locator, script));
  }

  /**
   * Executes asynchronous JavaScript code in the browser context.
   *
   * <p>Example:</p>
   * <pre>{@code
   * String result = driver.executeAsyncScript(
   *     "var callback = arguments[arguments.length - 1];" +
   *     "setTimeout(function() { callback('async result'); }, 1000);"
   * );
   * }</pre>
   *
   * @param <R>    the return type of the script execution
   * @param script the asynchronous JavaScript code to execute
   * @param args   optional arguments to pass to the script
   * @return the result of the async script execution
   */
  default <R> R executeAsyncScript(String script, Object... args) {
    return performActionOnEngine(
        "Execute Async Script",
        engine -> engine.executeAsyncScript(script, args));
  }

  /**
   * Executes asynchronous JavaScript code on a specific element after waiting for it.
   *
   * <p>Example:</p>
   * <pre>{@code
   * String result = driver.executeAsyncScript(
   *     By.id("async-element"),
   *     5,
   *     "var callback = arguments[arguments.length - 1];" +
   *     "var element = arguments[0];" +
   *     "// Perform async operation with element..." +
   *     "callback('done');"
   * );
   * }</pre>
   *
   * @param <R>     the return type of the script execution
   * @param locator the locator to find the element
   * @param waitSec maximum time in seconds to wait for the element
   * @param script  the asynchronous JavaScript code to execute
   * @return the result of the async script execution
   */
  default <R> R executeAsyncScript(String locator, int waitSec, String script, Object... args) {
    return waitUntil(
        "Execute Async Script",
        waitSec,
        engine -> engine.executeAsyncScript(locator, script, args));
  }

  /**
   * Scrolls the window by the specified offset.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollBy(0, 500);   // Scroll down 500 pixels
   * driver.scrollBy(-100, 0);  // Scroll left 100 pixels
   * }</pre>
   *
   * @param <T> the type of the implementing class for method chaining
   * @param x   horizontal scroll offset in pixels
   * @param y   vertical scroll offset in pixels
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollBy(int x, int y) {
    executeScript("window.scrollBy(" + x + "," + y + ")");
    return (T) this;
  }

  /**
   * Sets focus on an element using the default timeout.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.focus(By.id("username-field"));
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to focus
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T focus(final String locator) {
    return focus(locator, DEFAULT_TIMEOUT);
  }

  /**
   * Sets focus on an element with a specified wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.focus(By.name("email"), 5);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to focus
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T focus(final String locator, int waitSec) {
    waitUntil(
        "Focus",
        waitSec,
        engine -> {
          engine.focus(locator);
          return (T) this;
        });
    return (T) this;
  }

  /**
   * Sets focus on a specific WebElement.
   * Falls back to JavaScript focus if mouse movement fails.
   *
   * <p>Example:</p>
   * <pre>{@code
   * WebElement inputField = driver.getElement(By.id("search"));
   * driver.focus(inputField);
   * }</pre>
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param elementHandle the WebElement to focus
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T focus(final ElementHandle elementHandle) {
    try {
      return performActionOnEngine(
          "Focus",
          engine -> {
            elementHandle.focus();
            return (T) this;
          });
    } catch (Throwable t) {
      executeScript("arguments[0].focus();", elementHandle);
      return (T) this;
    }
  }

  /**
   * Performs a drag and drop operation by clicking and holding on an element,
   * then moving by the specified offset and releasing.
   *
   * <p>Example:</p>
   * <pre>{@code
   * // Drag a slider handle 100 pixels to the right
   * driver.dropTo(By.id("slider-handle"), 100, 0, 5);
   * }</pre>
   *
   * @param locator the locator to find the element to drag
   * @param xOffset horizontal offset to move in pixels
   * @param yOffset vertical offset to move in pixels
   * @param waitSec maximum time in seconds to wait for the element
   * @return the WebElement that was dragged
   */
  default <T extends CDriverActions> T dropTo(final String locator, int xOffset, int yOffset, int waitSec) {
    return waitUntil(
        "Drop To",
        waitSec,
        driver -> {
          driver.dropTo(locator, xOffset, yOffset);
          return null;
        });
  }

  /**
   * Performs a drag and drop operation from a specific point on an element to another point.
   *
   * <p>Example:</p>
   * <pre>{@code
   * // Drag from top-left corner of element (10, 10) and move 200 pixels right, 50 down
   * driver.dragAndDropTo(By.id("draggable"), 10, 10, 200, 50, 5);
   * }</pre>
   *
   * @param locator  the locator to find the element to drag
   * @param xOffset1 horizontal offset from element center for the start point
   * @param yOffset1 vertical offset from element center for the start point
   * @param xOffset2 horizontal offset to move from the start point
   * @param yOffset2 vertical offset to move from the start point
   * @param waitSec  maximum time in seconds to wait for the element
   * @return the WebElement that was dragged
   */
  default <T extends CDriverActions> T dragAndDropTo(final String locator, int xOffset1, int yOffset1, int xOffset2, int yOffset2, int waitSec) {
    return waitUntil(
        "Drag And Drop To",
        waitSec,
        driver -> {
          driver.dragAndDropTo(locator, xOffset1, yOffset1, xOffset2, yOffset2);
          return null;
        });
  }

  /**
   * Performs a drag and drop operation from a specific point on an element to another point.
   *
   * <p>Example:</p>
   * <pre>{@code
   * // Drag from top-left corner of element (10, 10) and move 200 pixels right, 50 down
   * driver.dragAndDropTo(By.id("draggable"), 10, 10, 200, 50, 5);
   * }</pre>
   *
   * @param source   the locator to find the source element to drag
   * @param target   the locator to find the target element to drag
   * @param xOffset1 horizontal offset from element center for the start point
   * @param yOffset1 vertical offset from element center for the start point
   * @param waitSec  maximum time in seconds to wait for the element
   * @return the WebElement that was dragged
   */
  default <T extends CDriverActions> T dragAndDropTo(final String source, final String target, int xOffset1, int yOffset1, int waitSec) {
    return waitUntil(
        "Drag And Drop To",
        waitSec,
        driver -> {
          driver.dragAndDropTo(source, target, xOffset1, yOffset1);
          return null;
        });
  }

  /**
   * Moves the mouse to an element with specified offset and waits for the element to be available.
   * This is a lower-level method used by other mouse operations.
   *
   * <p>Example:</p>
   * <pre>{@code
   * // Move to 20 pixels right and 10 pixels down from element center
   * WebElement hovered = driver.moveToElement(By.id("hover-target"), 20, 10, 5);
   * }</pre>
   *
   * @param locator the locator to find the element
   * @param xOffset horizontal offset from the element's center
   * @param yOffset vertical offset from the element's center
   * @param waitSec maximum time in seconds to wait for the element
   * @return the WebElement that was moved to
   */
  default <T extends CDriverActions> T moveToElement(final String locator, int xOffset, int yOffset, int waitSec) {
    return waitUntil(
        "Move To Element",
        waitSec,
        driver -> {
          driver.moveToElement(locator, xOffset, yOffset);
          return null;
        });
  }
}

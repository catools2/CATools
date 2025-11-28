package org.catools.web.drivers;

import org.catools.common.utils.CRetry;
import org.catools.common.utils.CSleeper;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.Set;
import java.util.function.Predicate;

/**
 * CDriverActions provides a comprehensive set of web driver action methods for interacting with web elements.
 * This interface extends CDriverWaiter and offers a fluent API for performing various user actions such as
 * clicking, typing, scrolling, and managing cookies. All methods support method chaining for improved readability.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CDriverActions driver = new MyDriverImplementation();
 * driver.click(By.id("button"), 5)
 *       .sendKeys(By.id("input"), 3, "Hello World")
 *       .pressEnter()
 *       .scrollIntoView(By.id("footer"), true);
 * }</pre>
 *
 * @since 1.0
 */
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
    return performActionOnDriver(
        "Press ENTER",
        webDriver -> {
          new Actions(webDriver).sendKeys(Keys.ENTER).perform();
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
   * Presses the ESCAPE key on the current page.
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
    return performActionOnDriver(
        "Press Scape",
        webDriver -> {
          new Actions(webDriver).sendKeys(Keys.ESCAPE).perform();
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
   * Presses the TAB key on the current page to navigate to the next focusable element.
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
    return performActionOnDriver(
        "Press Tab",
        webDriver -> {
          new Actions(webDriver).sendKeys(Keys.TAB).perform();
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
   * Clicks an element using JavaScript execution, bypassing standard click restrictions.
   * This method is useful for clicking elements that may be covered by other elements.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.clickJS(By.id("hidden-button"), 5);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to click
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T clickJS(By locator, int waitSec) {
    waitUntil(
        "Click",
        waitSec,
        webDriver -> {
          WebElement el = webDriver.findElement(locator);
          if (el == null) return el;
          JavascriptExecutor executor = (JavascriptExecutor) webDriver;
          executor.executeScript("arguments[0].click();", el);
          return el;
        });
    return (T) this;
  }

  /**
   * Clicks an element after scrolling it into view. This is the standard click method
   * that handles most common clicking scenarios.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.click(By.xpath("//button[@type='submit']"), 10);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to click
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T click(By locator, int waitSec) {
    waitUntil(
        "Click",
        waitSec,
        webDriver -> {
          WebElement el = webDriver.findElement(locator);
          if (el == null) return el;
          JavascriptExecutor executor = (JavascriptExecutor) webDriver;
          try {
            executor.executeScript("arguments[0].scrollIntoView(true);", el);
          } catch (Exception e) {
          }
          el.click();
          return el;
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
   *     By.id("next-page"),
   *     5,
   *     d -> d.isElementVisible(By.id("page-2-content"))
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
      By locator, int waitSec, Predicate<CDriverActions> postCondition) {
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
      By locator,
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
  default <T extends CDriverActions> T mouseClick(By locator) {
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
  default <T extends CDriverActions> T mouseClick(By locator, int waitSec) {
    return performActionOnDriver(
        "Mouse Click",
        webDriver -> {
          moveTo(locator, waitSec);
          new Actions(webDriver).click();
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
  default <T extends CDriverActions> T mouseDoubleClick(By locator) {
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
  default <T extends CDriverActions> T mouseDoubleClick(By locator, int waitSec) {
    return performActionOnDriver(
        "Double Mouse Click",
        webDriver -> {
          moveTo(locator, waitSec);
          new Actions(webDriver).doubleClick();
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
  default <T extends CDriverActions> T moveTo(By locator) {
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
  default <T extends CDriverActions> T moveTo(By locator, int xOffset, int yOffset) {
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
  default <T extends CDriverActions> T moveTo(By locator, int waitSec) {
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
  default <T extends CDriverActions> T moveTo(By locator, int xOffset, int yOffset, int waitSec) {
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
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the element to scroll into view
   * @param scrollDown true to scroll down to element, false to scroll up
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollIntoView(By locator, boolean scrollDown) {
    return scrollIntoView(locator, scrollDown, DEFAULT_TIMEOUT);
  }

  /**
   * Scrolls an element into view with a specified wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollIntoView(By.xpath("//section[@id='contact']"), false, 5);
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the element to scroll into view
   * @param scrollDown true to scroll down to element, false to scroll up
   * @param waitSec    maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollIntoView(By locator, boolean scrollDown, int waitSec) {
    executeScript("arguments[0].scrollIntoView(" + scrollDown + ");", getElement(locator, waitSec));
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
  default <T extends CDriverActions> T scrollLeft(By locator, int scrollSize) {
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
  default <T extends CDriverActions> T scrollLeft(By locator, int scrollSize, int waitSec) {
    executeScript("arguments[0].scrollLeft-=arguments[1];", getElement(locator, waitSec), scrollSize);
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
  default <T extends CDriverActions> T scrollRight(By locator, int scrollSize) {
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
  default <T extends CDriverActions> T scrollRight(By locator, int scrollSize, int waitSec) {
    executeScript("arguments[0].scrollLeft+=arguments[1];", getElement(locator, waitSec), scrollSize);
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
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the element
   * @param scrollDown true to scroll down to element, false to scroll up
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollIntoViewAndClick(By locator, boolean scrollDown) {
    return scrollIntoViewAndClick(locator, scrollDown, DEFAULT_TIMEOUT);
  }

  /**
   * Scrolls an element into view and then clicks it with a specified wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.scrollIntoViewAndClick(By.linkText("Terms of Service"), false, 5);
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param locator    the locator to find the element
   * @param scrollDown true to scroll down to element, false to scroll up
   * @param waitSec    maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T scrollIntoViewAndClick(
      By locator, boolean scrollDown, int waitSec) {
    scrollIntoView(locator, scrollDown, waitSec);
    return click(locator, waitSec);
  }

  /**
   * Clicks an invisible or hidden element using JavaScript execution.
   * This method is useful for clicking elements that are not visible but are enabled.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.clickInvisible(By.id("hidden-trigger"), 5);
   * }</pre>
   *
   * @param <T>     the type of the implementing class for method chaining
   * @param locator the locator to find the element to click
   * @param waitSec maximum time in seconds to wait for the element
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T clickInvisible(By locator, int waitSec) {
    waitUntil(
        "Click Invisible",
        waitSec,
        webDriver -> {
          WebElement el = webDriver.findElement(locator);
          if (el != null && el.isEnabled()) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", el);
            return el;
          }
          return null;
        });
    return (T) this;
  }

  /**
   * Clicks an invisible element and retries until the post-condition is met.
   * Uses default retry settings (3 retries, 1000ms interval).
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.clickInvisible(
   *     By.id("hidden-submit"),
   *     5,
   *     d -> d.isElementVisible(By.id("confirmation"))
   * );
   * }</pre>
   *
   * @param <T>           the type of the implementing class for method chaining
   * @param locator       the locator to find the element to click
   * @param waitSec       maximum time in seconds to wait for the element
   * @param postCondition condition to check after clicking
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T clickInvisible(
      By locator, int waitSec, Predicate<CDriverActions> postCondition) {
    return clickInvisible(locator, waitSec, postCondition, 3, 1000);
  }

  /**
   * Clicks an invisible element and retries until the post-condition is met with custom retry settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.clickInvisible(
   *     By.xpath("//input[@type='hidden'][@name='action']"),
   *     3,
   *     d -> d.getCurrentUrl().contains("success"),
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
  default <T extends CDriverActions> T clickInvisible(
      By locator,
      int waitSec,
      Predicate<CDriverActions> postCondition,
      int retryTimes,
      int intervalInSeconds) {
    CRetry.retryIfNot(
        integer -> clickInvisible(locator, waitSec), postCondition, retryTimes, intervalInSeconds);
    return (T) this;
  }

  /**
   * Sends keys to the currently focused element repeatedly.
   *
   * <p>Example:</p>
   * <pre>{@code
   * driver.sendKeys(3, Keys.ARROW_DOWN); // Press down arrow 3 times
   * }</pre>
   *
   * @param <T>        the type of the implementing class for method chaining
   * @param loopCount  number of times to repeat the key sequence
   * @param keysToSend the keys to send
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T sendKeys(int loopCount, CharSequence... keysToSend) {
    return performActionOnDriver(
        "Send Keys",
        webDriver -> {
          Actions actions = new Actions(webDriver);

          for (int i = 0; i < loopCount; ++i) {
            actions = actions.sendKeys(keysToSend);
          }

          actions.perform();
          return (T) this;
        });
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
  default <T extends CDriverActions> T sendKeys(
      By locator, int waitSec, CharSequence... keysToSend) {
    return waitUntil(
        "Send Keys",
        waitSec,
        webDriver -> {
          WebElement el = webDriver.findElement(locator);
          if (el == null || !el.isDisplayed() || !el.isEnabled()) {
            return null;
          }
          el.sendKeys(keysToSend);
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
  default <T extends CDriverActions> T sendKeys(CharSequence... keysToSend) {
    return performActionOnDriver(
        "Send Keys",
        webDriver -> {
          new Actions(webDriver).sendKeys(keysToSend).perform();
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
    return performActionOnDriver(
        "Delete All Cookies",
        webDriver -> {
          webDriver.manage().deleteAllCookies();
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
    return performActionOnDriver(
        "Get Cookie", webDriver -> webDriver.manage().getCookieNamed(name));
  }

  /**
   * Retrieves all cookies from the current domain.
   *
   * <p>Example:</p>
   * <pre>{@code
   * Set<Cookie> allCookies = driver.getCookies();
   * allCookies.forEach(cookie ->
   *     System.out.println(cookie.getName() + "=" + cookie.getValue())
   * );
   * }</pre>
   *
   * @return a Set of all cookies for the current domain
   */
  default Set<Cookie> getCookies() {
    return performActionOnDriver("Get Cookies", webDriver -> webDriver.manage().getCookies());
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
    return performActionOnDriver(
        "Add Cookie",
        webDriver -> {
          webDriver.manage().addCookie(cookie);
          return cookie;
        });
  }

  /**
   * Sets the caret color for all input elements on the page.
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
            "document.querySelectorAll(\"%s\").forEach(function(a) {\n"
                + "  a.style[\"%s\"]=\"%s\";\n"
                + "});",
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
  default <R> R executeScript(
      @CMcpToolParam(name = "script", description = "The JavaScript code to execute", required = true)
      String script,
      @CMcpToolParam(name = "args", description = "Optional arguments to pass to the script", required = false)
      Object... args) {
    return performActionOnDriver(
        "Execute Script",
        webDriver -> {
          return (R) ((JavascriptExecutor) webDriver).executeScript(script, args);
        });
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
      @CMcpToolParam(name = "locator", description = "The locator to find the element", required = true)
      By locator,
      @CMcpToolParam(name = "waitSec", description = "Maximum time in seconds to wait for the element", required = true)
      int waitSec,
      @CMcpToolParam(name = "script", description = "The JavaScript code to execute (element will be passed as arguments[0])", required = true)
      String script) {
    WebElement elem = waitUntil("Execute Script", waitSec, webDriver -> webDriver.findElement(locator));
    return executeScript(script, elem);
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
    return performActionOnDriver(
        "Execute Async Script",
        webDriver -> (R) ((JavascriptExecutor) webDriver).executeAsyncScript(script, args));
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
  default <R> R executeAsyncScript(By locator, int waitSec, String script) {
    return waitUntil(
        "Execute Async Script",
        waitSec,
        webDriver -> {
          WebElement el = webDriver.findElement(locator);
          if (el == null) {
            return null;
          }
          return (R) ((JavascriptExecutor) webDriver).executeAsyncScript(script, el);
        });
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
  default <T extends CDriverActions> T focus(final By locator) {
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
  default <T extends CDriverActions> T focus(final By locator, int waitSec) {
    waitUntil(
        "Focus",
        waitSec,
        webDriver -> {
          WebElement el = webDriver.findElement(locator);
          if (el == null) {
            return null;
          }
          focus(el);
          return el;
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
   * @param <T>        the type of the implementing class for method chaining
   * @param webElement the WebElement to focus
   * @return this instance for method chaining
   */
  default <T extends CDriverActions> T focus(final WebElement webElement) {
    try {
      return performActionOnDriver(
          "Focus",
          webDriver -> {
            new Actions(webDriver).moveToElement(webElement).perform();
            return (T) this;
          });
    } catch (Throwable t) {
      executeScript("arguments[0].focus();", webElement);
      return (T) this;
    }
  }

  /**
   * Retrieves a WebElement using the default timeout.
   *
   * <p>Example:</p>
   * <pre>{@code
   * WebElement button = driver.getElement(By.id("submit-btn"));
   * String buttonText = button.getText();
   * }</pre>
   *
   * @param locator the locator to find the element
   * @return the WebElement if found
   */
  default WebElement getElement(By locator) {
    return getElement(locator, DEFAULT_TIMEOUT);
  }

  /**
   * Retrieves a WebElement with a specified wait time.
   *
   * <p>Example:</p>
   * <pre>{@code
   * WebElement dynamicContent = driver.getElement(By.className("loaded-content"), 10);
   * if (dynamicContent.isDisplayed()) {
   *     System.out.println("Content loaded successfully");
   * }
   * }</pre>
   *
   * @param locator the locator to find the element
   * @param waitSec maximum time in seconds to wait for the element
   * @return the WebElement if found within the timeout
   */
  default WebElement getElement(By locator, int waitSec) {
    return waitUntil("Get Element", waitSec, null, webdriver -> webdriver.findElement(locator));
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
  default WebElement dropTo(final By locator, int xOffset, int yOffset, int waitSec) {
    return waitUntil(
        "Drop To",
        waitSec,
        driver -> {
          WebElement elem = driver.findElement(locator);
          if (elem != null) {
            try {
              new Actions(driver)
                  .moveToElement(elem)
                  .clickAndHold()
                  .moveByOffset(xOffset, yOffset)
                  .release()
                  .build()
                  .perform();
            } catch (Throwable t) {
            }
            return elem;
          }
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
  default WebElement dragAndDropTo(final By locator, int xOffset1, int yOffset1, int xOffset2, int yOffset2, int waitSec) {
    return waitUntil(
        "Drag And Drop To",
        waitSec,
        driver -> {
          WebElement elem = driver.findElement(locator);
          if (elem != null) {
            try {
              new Actions(driver)
                  .moveToElement(elem, xOffset1, yOffset1)
                  .clickAndHold()
                  .moveByOffset(xOffset2, yOffset2)
                  .release()
                  .build()
                  .perform();
            } catch (Throwable t) {
            }
            return elem;
          }
          return null;
        });
  }

  /**
   * Performs a drag and drop operation from one element to another element.
   *
   * <p>Example:</p>
   * <pre>{@code
   * // Drag from specific point on source element to target element
   * driver.dragAndDropTo(
   *     By.id("draggable-item"),
   *     By.id("drop-zone"),
   *     5, 5,  // Start from 5px right, 5px down from center of source
   *     3      // Wait up to 3 seconds
   * );
   * }</pre>
   *
   * @param fromLocator the locator to find the source element
   * @param toLocator   the locator to find the target element
   * @param xOffset1    horizontal offset from source element center
   * @param yOffset1    vertical offset from source element center
   * @param waitSec     maximum time in seconds to wait for elements
   * @return the source WebElement that was dragged
   */
  default WebElement dragAndDropTo(final By fromLocator, final By toLocator, int xOffset1, int yOffset1, int waitSec) {
    return waitUntil(
        "Drag And Drop To",
        waitSec,
        driver -> {
          WebElement elem = driver.findElement(fromLocator);
          if (elem != null) {
            new Actions(driver)
                .moveToElement(elem, xOffset1, yOffset1)
                .dragAndDrop(elem, driver.findElement(toLocator))
                .build()
                .perform();
          }
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
  default WebElement moveToElement(final By locator, int xOffset, int yOffset, int waitSec) {
    return waitUntil(
        "Move To Element",
        waitSec,
        driver -> {
          WebElement elem = driver.findElement(locator);
          if (elem != null) {
            try {
              new Actions(driver).moveToElement(elem).moveByOffset(xOffset, yOffset).perform();
            } catch (Throwable t) {
            }
            return elem;
          }
          return null;
        });
  }
}

package org.catools.web.drivers;

import org.catools.common.utils.CRetry;
import org.catools.web.config.CDriverConfigs;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Function;

/**
 * Interface providing advanced waiting capabilities for WebDriver operations.
 * This interface offers flexible methods for waiting until specific conditions are met,
 * with configurable timeouts, polling intervals, and exception handling.
 * 
 * <p>The waiter automatically handles various WebDriver exceptions and provides
 * hooks for pre/post action processing including document ready state checks.</p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>Flexible waiting with ExpectedConditions</li>
 *   <li>Automatic exception handling for common WebDriver issues</li>
 *   <li>Configurable timeouts and polling intervals</li>
 *   <li>Document ready state verification</li>
 *   <li>Default value support for failed waits</li>
 * </ul>
 * 
 * @author CATools Team
 * @since 1.0
 */
public interface CDriverWaiter {
  Logger logger = LoggerFactory.getLogger(CDriverWaiter.class);
  int DEFAULT_TIMEOUT = CDriverConfigs.getTimeout();

  /**
   * Gets the current driver session associated with this waiter.
   * 
   * @return the CDriverSession instance managing the WebDriver
   */
  CDriverSession getDriverSession();

  /**
   * Waits until the specified condition is satisfied using the default timeout.
   * This method blocks execution until the condition returns a non-null value or the timeout expires.
   * 
   * @param <C> the return type of the condition
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param condition the ExpectedCondition to wait for
   * @return the result returned by the condition when it succeeds
   * @throws TimeoutException if the condition is not satisfied within the default timeout
   * 
   * @example
   * <pre>{@code
   * // Wait for an element to be visible
   * WebElement element = waitUntil("Wait for login button", 
   *     ExpectedConditions.visibilityOfElementLocated(By.id("login-btn")));
   * 
   * // Wait for page title to contain text
   * Boolean titleMatch = waitUntil("Wait for page title", 
   *     ExpectedConditions.titleContains("Dashboard"));
   * }</pre>
   */
  default <C> C waitUntil(String actionName, ExpectedCondition<C> condition) {
    return waitUntil(actionName, DEFAULT_TIMEOUT, condition);
  }

  /**
   * Waits until the specified condition is satisfied with a custom timeout.
   * This method blocks execution until the condition returns a non-null value or the timeout expires.
   * 
   * @param <C> the return type of the condition
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param waitSec the maximum time to wait in seconds
   * @param condition the ExpectedCondition to wait for
   * @return the result returned by the condition when it succeeds
   * @throws TimeoutException if the condition is not satisfied within the specified timeout
   * 
   * @example
   * <pre>{@code
   * // Wait for an element to be clickable with custom timeout
   * WebElement button = waitUntil("Wait for submit button", 30, 
   *     ExpectedConditions.elementToBeClickable(By.id("submit")));
   * 
   * // Wait for text to appear with shorter timeout
   * WebElement message = waitUntil("Wait for success message", 5,
   *     ExpectedConditions.visibilityOfElementLocated(By.className("success")));
   * }</pre>
   */
  default <C> C waitUntil(String actionName, int waitSec, ExpectedCondition<C> condition) {
    onBeforeAction();

    C result = performActionOnDriver(actionName, webDriver -> getWebDriverWait(webDriver, waitSec).until(condition::apply));

    onAfterAction();
    return result;
  }

  /**
   * Waits until the specified condition is satisfied using the default timeout,
   * returning a default value if the condition fails.
   * This method provides a safe way to wait for conditions with graceful fallback.
   * 
   * @param <C> the return type of the condition
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param defaultTo the value to return if the condition times out
   * @param condition the ExpectedCondition to wait for
   * @return the result returned by the condition when it succeeds, or defaultTo if it times out
   * 
   * @example
   * <pre>{@code
   * // Try to find optional element, return null if not found
   * WebElement optionalElement = waitUntil("Look for optional banner", null,
   *     ExpectedConditions.presenceOfElementLocated(By.id("banner")));
   * 
   * // Wait for attribute value with fallback
   * String status = waitUntil("Check element status", "unknown",
   *     ExpectedConditions.attributeContains(element, "class", "active"));
   * }</pre>
   */
  default <C> C waitUntil(String actionName, C defaultTo, ExpectedCondition<C> condition) {
    return waitUntil(actionName, DEFAULT_TIMEOUT, defaultTo, condition);
  }

  /**
   * Waits until the specified condition is satisfied with a custom timeout,
   * returning a default value if the condition fails.
   * This method provides a safe way to wait for conditions with graceful fallback.
   * 
   * @param <C> the return type of the condition
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param waitSec the maximum time to wait in seconds
   * @param defaultTo the value to return if the condition times out
   * @param condition the ExpectedCondition to wait for
   * @return the result returned by the condition when it succeeds, or defaultTo if it times out
   * 
   * @example
   * <pre>{@code
   * // Wait for dynamic content with fallback
   * List<WebElement> items = waitUntil("Wait for search results", 15, 
   *     Collections.emptyList(),
   *     ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("result")));
   * 
   * // Check if element becomes stale with timeout
   * Boolean isStale = waitUntil("Check element staleness", 5, false,
   *     ExpectedConditions.stalenessOf(element));
   * }</pre>
   */
  default <C> C waitUntil(String actionName, int waitSec, C defaultTo, ExpectedCondition<C> condition) {
    try {
      onBeforeAction();

      C result = performActionOnDriver(actionName, webDriver -> getWebDriverWait(webDriver, waitSec).until(condition::apply));

      onAfterAction();
      return result;
    } catch (TimeoutException t) {
      return defaultTo;
    }
  }

  /**
   * Performs an action on the WebDriver instance managed by this session.
   * This method provides safe access to the underlying WebDriver with proper
   * session management and error handling.
   * 
   * @param <T> the return type of the action
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param consumer a function that takes a RemoteWebDriver and returns a result
   * @return the result of executing the consumer function
   * 
   * @example
   * <pre>{@code
   * // Execute JavaScript and return result
   * String userAgent = performActionOnDriver("Get user agent",
   *     driver -> (String) driver.executeScript("return navigator.userAgent;"));
   * 
   * // Get current window handle
   * String windowHandle = performActionOnDriver("Get window handle",
   *     driver -> driver.getWindowHandle());
   * 
   * // Take screenshot
   * byte[] screenshot = performActionOnDriver("Take screenshot",
   *     driver -> ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
   * }</pre>
   */
  default <T> T performActionOnDriver(String actionName, Function<RemoteWebDriver, T> consumer) {
    return getDriverSession().performActionOnDriver(actionName, consumer);
  }

  // Waiters
  
  /**
   * Waits for the document ready state to become "complete" using default timeout and polling interval.
   * This method is useful for ensuring that the page has fully loaded before proceeding with other actions.
   * Uses JavaScript to check document.readyState and retries until it equals "complete".
   * 
   * @return true if the document ready state becomes "complete" within the timeout, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Wait for page to fully load after navigation
   * driver.get("https://example.com");
   * boolean isReady = waitCompleteReadyState();
   * if (isReady) {
   *     // Page is fully loaded, safe to interact with elements
   *     WebElement button = driver.$(By.id("action-button"));
   *     button.click();
   * }
   * }</pre>
   */
  default boolean waitCompleteReadyState() {
    return waitCompleteReadyState(DEFAULT_TIMEOUT, 100);
  }

  /**
   * Waits for the document ready state to become "complete" with custom timeout and polling interval.
   * This method provides fine-grained control over the waiting behavior for document readiness.
   * 
   * @param waitSec the maximum time to wait in seconds
   * @param interval the polling interval in milliseconds between checks
   * @return true if the document ready state becomes "complete" within the timeout, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Wait for slow-loading page with longer timeout and less frequent polling
   * boolean isReady = waitCompleteReadyState(60, 500);
   * 
   * // Quick check with short timeout for fast pages
   * boolean isReadyQuick = waitCompleteReadyState(5, 50);
   * 
   * // Wait after AJAX operations that might change document state
   * submitForm();
   * boolean formProcessed = waitCompleteReadyState(30, 200);
   * }</pre>
   */
  default boolean waitCompleteReadyState(int waitSec, int interval) {
    return CRetry.retryIfFalse(idx -> (Boolean)
            performActionOnDriver("Wait Complete Ready State",
                webDriver -> webDriver.executeScript("return document.readyState === 'complete'")),
        waitSec,
        interval,
        () -> false);
  }

  private void onBeforeAction() {
    performActionOnDriver("After Action", driver -> {
      try {
        if (CDriverConfigs.waitCompleteReadyStateBeforeEachAction() && !alertPresent(driver)) {
          waitCompleteReadyState();
        }
      } catch (Throwable t) {
        logger.warn("Before action failed", t);
      }
      return true;
    });
  }

  private void onAfterAction() {
    performActionOnDriver("After Action", driver -> {
      try {
        if (CDriverConfigs.waitCompleteReadyStateAfterEachAction() && !alertPresent(driver)) {
          waitCompleteReadyState();
        }
      } catch (Throwable t) {
        logger.warn("After action failed", t);
      }
      return true;
    });
  }

  private boolean alertPresent(RemoteWebDriver webDriver) {
    try {
      return webDriver != null && webDriver.switchTo().alert() != null;
    } catch (Throwable t) {
      return false;
    }
  }

  // private
  private Wait<RemoteWebDriver> getWebDriverWait(RemoteWebDriver webDriver, int waitSec) {
    return new FluentWait<>(webDriver)
        .withTimeout(Duration.ofSeconds(waitSec))
        .pollingEvery(Duration.ofMillis(100))
        .ignoring(StaleElementReferenceException.class)
        .ignoring(InvalidElementStateException.class)
        .ignoring(NoSuchElementException.class)
        .ignoring(NoSuchSessionException.class)
        .ignoring(NoSuchWindowException.class)
        .ignoring(NoSuchFrameException.class)
        .ignoring(WebDriverException.class)
        .ignoring(TimeoutException.class)
        .ignoring(AssertionError.class);
  }
}

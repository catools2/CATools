package org.catools.web.drivers;

import org.catools.web.config.CDriverConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * Interface providing advanced waiting capabilities for CDriverEngine operations. This interface
 * offers flexible methods for waiting until specific conditions are met, with configurable
 * timeouts, polling intervals, and exception handling.
 *
 * <p>The waiter automatically handles various CDriverEngine exceptions and provides hooks for
 * pre/post action processing including document ready state checks.
 *
 * <h3>Key Features:</h3>
 *
 * <ul>
 *   <li>Flexible waiting with ExpectedConditions
 *   <li>Automatic exception handling for common CDriverEngine issues
 *   <li>Configurable timeouts and polling intervals
 *   <li>Document ready state verification
 *   <li>Default value support for failed waits
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
   * @return the CDriverSession instance managing the CDriverEngine
   */
  CDriverSession getDriverSession();

  /**
   * Waits until the specified condition is satisfied using the given timeout, returning a default
   * value if the condition times out.
   *
   * <p>Success rules: - If the consumer returns null -> not successful (keep waiting) - If the
   * consumer returns a Boolean -> only Boolean.TRUE is success - Otherwise any non-null return is
   * considered success
   *
   * @param <C> the return type of the condition
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param waitSec the maximum time to wait in seconds
   * @param defaultTo the value to return if the condition times out
   * @param consumer a function that takes a CDriverEngine and returns a result
   * @return the result returned by the consumer when it succeeds, or defaultTo if it times out
   */
  default <C> C waitUntil(
      String actionName, int waitSec, C defaultTo, Function<CDriverEngine<?>, C> consumer) {
    try {
      return performActionOnEngine(
          actionName,
          engine -> {
            final long timeoutAt =
                System.nanoTime() + TimeUnit.SECONDS.toNanos(Math.max(1, waitSec));
            final long pollMillis = 50L; // Reduced for 2x faster response
            Throwable lastError = null;

            while (true) {
              try {
                C res = consumer.apply(engine);
                // success rules
                if (res != null) {
                  if (res instanceof Boolean) {
                    if (Boolean.TRUE.equals(res)) return res;
                  } else {
                    return res;
                  }
                }
              } catch (Throwable t) {
                lastError = t;
                logger.debug(
                    "Ignored exception while waiting for {}: {}", actionName, t.getMessage());
              }

              if (System.nanoTime() > timeoutAt) {
                String msg = "Timed out waiting for action: " + actionName;
                if (lastError != null) msg += " (last error: " + lastError.getMessage() + ")";
                throw new RuntimeException(new TimeoutException(msg));
              }

              try {
                Thread.sleep(pollMillis);
              } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted for action: " + actionName, ie);
              }
            }
          });
    } catch (RuntimeException re) {
      if (re.getCause() instanceof TimeoutException) {
        return defaultTo;
      }
      throw re;
    }
  }

  /**
   * Waits until the specified condition is satisfied with a custom timeout, returning null if the
   * condition times out.
   *
   * <p>Delegates to the main overload with null as the default value.
   *
   * @param <C> the return type of the condition
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param waitSec the maximum time to wait in seconds
   * @param consumer a function that takes a CDriverEngine and returns a result
   * @return the result returned by the consumer when it succeeds, or null if it times out
   */
  default <C> C waitUntil(String actionName, int waitSec, Function<CDriverEngine<?>, C> consumer) {
    return waitUntil(actionName, waitSec, null, consumer);
  }

  /**
   * Performs an action on the CDriverEngine instance managed by this session. This method provides
   * safe access to the underlying CDriverEngine with proper session management and error handling.
   *
   * @param <T> the return type of the action
   * @param actionName a descriptive name for the action being performed (for logging)
   * @param consumer a function that takes a CDriverEngine and returns a result
   * @return the result of executing the consumer function
   * @implNote Example usage:
   *     <pre>{@code
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
  default <T> T performActionOnEngine(String actionName, Function<CDriverEngine<?>, T> consumer) {
    return getDriverSession().performActionOnEngine(actionName, consumer);
  }
}

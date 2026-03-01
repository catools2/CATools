package org.catools.web.controls;

import java.util.function.Function;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.pages.CWebComponent;

/**
 * Interface providing state checking and property retrieval methods for web elements. This
 * interface defines methods to check various states of web elements like visibility, enablement,
 * selection, and to retrieve element properties like text, attributes, and screenshots.
 *
 * @author CATools Team
 * @since 1.0
 */
public interface CWebElementWaiter extends CWebComponent {
  /** Default timeout value in seconds for web element operations. */
  int DEFAULT_TIMEOUT = CDriverConfigs.getTimeout();

  /**
   * Gets the wait timeout in seconds.
   *
   * @return the wait timeout in seconds
   */
  int getWaitSec();

  /**
   * Gets the locator used to find this element.
   *
   * @return the By locator
   */
  String getLocator();

  // Wait Methods

  /**
   * Waits until an element-based condition is satisfied and executes an action. This method
   * delegates to the driver engine's applyToElement for framework-specific handling.
   *
   * @param <C> the return type of the action
   * @param actionName descriptive name of the action for logging
   * @param waitSec maximum time to wait in seconds
   * @param action function to execute on the found element (Locator for Playwright, WebElement for
   *     Selenium)
   * @return the result of the action function, or null if element not found
   */
  default <C> C waitUntil(String actionName, int waitSec, Function<CElementEngine<?>, C> action) {
    return waitUntil(actionName, waitSec, null, action);
  }

  /**
   * Waits until an element-based condition is satisfied and executes an action with a default
   * return value. This method delegates to the driver engine's applyToElement for
   * framework-specific handling.
   *
   * @param <C> the return type of the action
   * @param actionName descriptive name of the action for logging
   * @param waitSec maximum time to wait in seconds
   * @param defaultTo default value to return if element not found or action fails
   * @param action function to execute on the found element (Locator for Playwright, WebElement for
   *     Selenium)
   * @return the result of the action function, or defaultTo if element not found
   */
  default <C> C waitUntil(
      String actionName, int waitSec, C defaultTo, Function<CElementEngine<?>, C> action) {
    return getElementEngine()
        .waitUntil(
            actionName,
            waitSec,
            defaultTo,
            engine -> {
              assert engine != null;
              try {
                return action.apply(engine);
              } catch (Throwable t) {
                return null;
              }
            });
  }
}

package org.catools.web.controls;

import static org.catools.web.drivers.CDriverWaiter.DEFAULT_TIMEOUT;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import org.catools.web.enums.CKeys;
import org.catools.web.selectors.CBy;
import org.catools.web.selectors.CByCssSelector;
import org.catools.web.selectors.CByXPath;

/**
 * Core engine interface for browser automation lifecycle management.
 *
 * <p>Provides methods for navigation, element interaction, script execution, and session
 * management.
 *
 * @author CATools Team
 * @since 1.0
 */
public interface CElementEngine<Context> {

  /**
   * Creates a CWebElement wrapper for the specified ID locator with custom timeout.
   *
   * @param name the element name
   * @param id the ID of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byId(String name, String id, int waitSec) {
    return new CWebElement(name, this, CBy.id(id), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified name locator with custom timeout.
   *
   * @param name the element name
   * @param elementName the name of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byName(String name, String elementName, int waitSec) {
    return new CWebElement(name, this, CBy.name(elementName), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified class name with custom timeout.
   *
   * @param name the element name
   * @param className the class name of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byClassName(String name, String className, int waitSec) {
    return new CWebElement(name, this, CBy.className(className), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified tag name with custom timeout.
   *
   * @param name the element name
   * @param tagName the tag name of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byTagName(String name, String tagName, int waitSec) {
    return new CWebElement(name, this, CBy.tagName(tagName), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified link text with custom timeout.
   *
   * @param name the element name
   * @param linkText the link text of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byLinkText(String name, String linkText, int waitSec) {
    return new CWebElement(name, this, CBy.linkText(linkText), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified partial link text with custom timeout.
   *
   * @param name the element name
   * @param partialLinkText the partial Link text of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byPartialLinkText(String name, String partialLinkText, int waitSec) {
    return new CWebElement(name, this, CBy.partialLinkText(partialLinkText), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified xpath with custom timeout.
   *
   * @param name the element name
   * @param xpath the xpath of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byXPath(String name, String xpath, int waitSec) {
    return new CWebElement(name, this, CBy.xpath(xpath), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified cssSelector locator with custom timeout.
   *
   * @param name the element name
   * @param cssSelector the cssSelector of the element to locate
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement byCssSelector(String name, String cssSelector, int waitSec) {
    return new CWebElement(name, this, CBy.cssSelector(cssSelector), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout. This method is
   * the primary way to interact with web elements on the page, providing a fluent interface for
   * element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  default CWebElement $(CBy locator) {
    return $("Get Element", locator);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout. This method is
   * the primary way to interact with web elements on the page, providing a fluent interface for
   * element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  default CWebElement $(String name, CBy locator) {
    return $(name, locator, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout. This method allows
   * you to specify a custom wait time for element operations, useful for elements that may take
   * longer to appear or become interactive.
   *
   * @param locator the CBy locator to find the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement $(String name, CBy locator, int waitSec) {
    return new CWebElement(name, this, locator, waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout. This method is
   * the primary way to interact with web elements on the page, providing a fluent interface for
   * element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  default CWebElement $(String locator) {
    return $("Get Element", locator);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout. This method is
   * the primary way to interact with web elements on the page, providing a fluent interface for
   * element operations.
   *
   * @param locator the CBy locator to find the target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  default CWebElement $(String name, String locator) {
    return $(name, locator, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout. This method allows
   * you to specify a custom wait time for element operations, useful for elements that may take
   * longer to appear or become interactive.
   *
   * @param locator the CBy locator to find the target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement $(String name, String locator, int waitSec) {
    return new CWebElement(name, this, locator, waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout. This method allows
   * you to specify a custom wait time for element operations, useful for elements that may take
   * longer to appear or become interactive.
   *
   * @param paths the paths to chain and find a target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement $$(String name, int waitSec, CByXPath... paths) {
    return $(name, CBy.chain(paths), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout. This method is
   * the primary way to interact with web elements on the page, providing a fluent interface for
   * element operations.
   *
   * @param paths the paths to chain and find a target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  default CWebElement $$(String name, CByXPath... paths) {
    return $(name, CBy.chain(paths), DEFAULT_TIMEOUT);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout. This method allows
   * you to specify a custom wait time for element operations, useful for elements that may take
   * longer to appear or become interactive.
   *
   * @param paths the paths to chain and find a target element
   * @param waitSec custom timeout in seconds for element operations
   * @return CWebElement wrapper with the specified timeout setting
   */
  default CWebElement $$(String name, int waitSec, CByCssSelector... paths) {
    return $(name, CBy.chain(paths), waitSec);
  }

  /**
   * Creates a CWebElement wrapper for the specified locator with default timeout. This method is
   * the primary way to interact with web elements on the page, providing a fluent interface for
   * element operations.
   *
   * @param paths the paths to chain and find a target element
   * @return CWebElement wrapper providing rich interaction capabilities
   */
  default CWebElement $$(String name, CByCssSelector... paths) {
    return $(name, CBy.chain(paths), DEFAULT_TIMEOUT);
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param keysToSend keys to send
   */
  boolean press(String locator, CKeys... keysToSend);

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param keysToSend keys to send
   */
  boolean press(String locator, long intervalInMilliSeconds, CKeys... keysToSend);

  /**
   * Retrieves the location of an element specified by the locator.
   *
   * @param locator element locator
   */
  Point getLocation(final String locator);

  /**
   * Move focus to the element identified by the locator.
   *
   * @param locator element selector or identifier
   */
  boolean focus(String locator);

  /**
   * Click the element located by the locator.
   *
   * @param locator element locator
   */
  boolean click(String locator);

  /**
   * Perform a mouse click on the element located by the locator.
   *
   * @param locator element locator
   */
  boolean mouseClick(String locator);

  /**
   * Perform a mouse double-click on the element located by the locator.
   *
   * @param locator element locator
   */
  boolean mouseDoubleClick(String locator);

  /**
   * Scroll the element into view.
   *
   * @param locator element locator
   */
  boolean scrollIntoView(String locator);

  /**
   * Scrolls an element horizontally to the left by the specified amount with a wait time.
   *
   * <p>Example:
   *
   * <pre>{@code
   * driver.scrollLeft(By.className("carousel"), 200, 3);
   * }</pre>
   *
   * @param locator the locator to find the scrollable element
   * @param scrollSize the number of pixels to scroll left
   * @return this instance for method chaining
   */
  default boolean scrollLeft(String locator, int scrollSize) {
    return executeScript(locator, "arguments[0].scrollLeft-=%s;".formatted(scrollSize));
  }

  /**
   * Scrolls an element horizontally to the right by the specified amount with a wait time.
   *
   * <p>Example:
   *
   * <pre>{@code
   * driver.scrollRight(By.id("timeline"), 300, 2);
   * }</pre>
   *
   * @param locator the locator to find the scrollable element
   * @param scrollSize the number of pixels to scroll right
   * @return this instance for method chaining
   */
  default boolean scrollRight(String locator, int scrollSize) {
    return executeScript(locator, "arguments[0].scrollLeft-+%s;".formatted(scrollSize));
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param keysToSend keys to send
   */
  boolean sendKeys(String locator, String keysToSend);

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param keysToSend keys to send
   */
  boolean sendKeys(String locator, String keysToSend, long intervalInMilliSeconds);

  /**
   * Drop an element to a specified offset.
   *
   * @param locator element locator
   * @param xOffset horizontal offset
   * @param yOffset vertical offset
   */
  boolean dropTo(String locator, int xOffset, int yOffset);

  /**
   * Move to an element with provided offsets.
   *
   * @param locator element locator
   * @param xOffset horizontal offset
   * @param yOffset vertical offset
   */
  boolean moveToElement(final String locator, int xOffset, int yOffset);

  /**
   * Drag source element and drop it to target with offsets.
   *
   * @param source source element locator
   * @param target target element locator
   */
  default boolean dragAndDropTo(final String source, final String target) {
    return dragAndDropTo(source, target, 0, 0);
  }

  /**
   * Drag source element and drop it to target with offsets.
   *
   * @param source source element locator
   * @param target target element locator
   * @param xOffset1 horizontal offset for source
   * @param yOffset1 vertical offset for source
   */
  boolean dragAndDropTo(final String source, final String target, int xOffset1, int yOffset1);

  /**
   * Drag an element from one point to another using offsets.
   *
   * @param locator element locator
   * @param xOffset1 first horizontal offset
   * @param yOffset1 first vertical offset
   * @param xOffset2 second horizontal offset
   * @param yOffset2 second vertical offset
   */
  boolean dragAndDropTo(
      final String locator, int xOffset1, int yOffset1, int xOffset2, int yOffset2);

  /**
   * Execute a script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script script to execute
   * @param <T> expected return type
   * @return script execution result
   */
  <T> T executeScript(String locator, String script);

  /**
   * Execute an asynchronous script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script script to execute
   * @param args optional script arguments
   * @param <T> expected return type
   * @return script execution result
   */
  <T> T executeAsyncScript(String locator, String script, Object... args);

  /**
   * Checks if an element matching the locator is present in the DOM.
   *
   * @param locator element locator string
   * @return true if element is present, false otherwise
   */
  boolean isElementPresent(String locator);

  /**
   * Checks if an element matching the locator is visible.
   *
   * @param locator element locator string
   * @return true if element is visible, false otherwise
   */
  boolean isElementVisible(String locator);

  /**
   * Checks if an element matching the locator is enabled.
   *
   * @param locator element locator string
   * @return true if element is enabled, false otherwise
   */
  boolean isElementEnabled(String locator);

  /**
   * Checks if an element matching the locator is selected/checked.
   *
   * @param locator element locator string
   * @return true if element is selected, false otherwise
   */
  boolean isElementSelected(String locator);

  /**
   * Gets the text content of the element matching the locator.
   *
   * @param locator element locator string
   * @return element text content
   */
  String getElementText(String locator);

  /**
   * Gets the value attribute of the element matching the locator.
   *
   * @param locator element locator string
   * @return element value attribute
   */
  String getElementValue(String locator);

  /**
   * Gets the inner HTML of the element matching the locator.
   *
   * @param locator element locator string
   * @return element inner HTML
   */
  String getElementInnerHtml(String locator);

  /**
   * Gets an attribute value of the element matching the locator.
   *
   * @param locator element locator string
   * @param attributeName the name of the attribute
   * @return attribute value or null if not found
   */
  String getElementAttribute(String locator, String attributeName);

  /**
   * Gets a CSS property value of the element matching the locator.
   *
   * @param locator element locator string
   * @param propertyName the name of the CSS property
   * @return CSS property value or null if not found
   */
  String getElementCssValue(String locator, String propertyName);

  /**
   * Gets the count of elements matching the locator.
   *
   * @param locator element locator string
   * @return number of matching elements
   */
  int getElementCount(String locator);

  /**
   * Clears the content of the element matching the locator.
   *
   * @param locator element locator string
   */
  boolean clearElement(String locator);

  /**
   * Fills the element with text (types text character by character).
   *
   * @param locator element locator string
   * @param text text to type
   */
  boolean setText(String locator, String text);

  /**
   * Press special key press to the element.
   *
   * @param locator element locator string
   * @param text key to press
   */
  boolean press(String locator, String text);

  /**
   * Takes a screenshot of the element matching the locator.
   *
   * @param locator element locator string
   * @return byte array representing the screenshot (PNG)
   */
  byte[] screenshotElement(String locator);

  /**
   * Returns all selected options for a select element.
   *
   * @param locator element locator string
   * @return list of selected Options
   */
  List<Options> getAllSelectedOptions(String locator);

  /**
   * Returns the first selected option for a select element.
   *
   * @param locator element locator string
   * @return first selected Option or null if none selected
   */
  Options getFirstSelectedOption(String locator);

  /**
   * Returns all available options from a select element.
   *
   * @param locator element locator string
   * @return list of available Options
   */
  List<Options> getOptions(String locator);

  /**
   * Selects an option by its visible text.
   *
   * @param locator element locator string
   * @param text visible text to select
   */
  boolean selectByVisibleText(String locator, String text);

  /**
   * Selects an option by matching its visible text against a pattern.
   *
   * @param locator element locator string
   * @param pattern regex pattern to match visible text
   */
  boolean selectByTextPattern(String locator, String pattern);

  /**
   * Selects an option by its value attribute.
   *
   * @param locator element locator string
   * @param value the value attribute to select
   */
  boolean selectByValue(String locator, String value);

  /**
   * Selects an option by its index.
   *
   * @param locator element locator string
   * @param index 0-based index to select
   */
  boolean selectByIndex(String locator, int index);

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
    try {
      final long timeoutAt = System.nanoTime() + TimeUnit.SECONDS.toNanos(Math.max(1, waitSec));
      final long pollMillis = 50L; // Reduced for 2x faster response
      Throwable lastError = null;

      while (true) {
        try {
          C res = action.apply(this);
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
    } catch (RuntimeException re) {
      if (re.getCause() instanceof TimeoutException) {
        return defaultTo;
      }
      throw re;
    }
  }

  <T> T performActionOnContext(String locator, Function<Context, T> action);

  Context getContext(String locator);

  /**
   * Represents an option element in a select control.
   *
   * @param text visible text of the option
   * @param value value attribute of the option
   */
  record Options(String text, String value) {
    /**
     * Returns a requested attribute value from the option.
     *
     * @param name attribute name (e.g. "value")
     * @return attribute value or null if attribute is not available
     */
    public String getAttribute(String name) {
      if ("value".equalsIgnoreCase(name)) return value;
      return null;
    }
  }
}

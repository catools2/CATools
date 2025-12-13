package org.catools.web.drivers;

import org.catools.web.controls.CWebElement;

import java.util.List;

/**
 * Core engine interface for browser automation lifecycle management.
 * <p>
 * Provides methods for navigation, element interaction, script execution, and session management.
 * </p>
 *
 * @author CATools Team
 * @since 1.0
 */
public interface CDriverEngine {

  /**
   * Creates a CWebElement wrapper for the specified locator with custom timeout.
   *
   * @param name    element name for logging and reporting
   * @param locator element locator string
   * @param waitSec custom timeout in seconds
   * @return CWebElement wrapper with specified timeout
   */
  default <DR extends CDriver> CWebElement<DR> $(String name, DR driver, String locator, int waitSec) {
    return new CWebElement<>(name, driver, locator, waitSec);
  }

  /**
   * Quits the driver engine session.
   *
   * @return true if quit was successful, false otherwise
   */
  boolean quit();

  /**
   * Checks if the driver engine is closed.
   *
   * @return true if the engine is closed, false otherwise
   */
  boolean isClosed();

  /**
   * Closes the driver engine and releases any associated resources.
   */
  void close();

  /**
   * Refreshes the current page.
   */
  void refresh();

  /**
   * Takes a screenshot of the current page.
   *
   * @return byte array representing the screenshot (PNG)
   */
  byte[] screenshot();

  /**
   * Gets the title of the current page.
   *
   * @return page title or empty string if not available
   */
  String title();

  /**
   * Gets the current url of the current page.
   *
   * @return current page URL
   */
  String url();

  /**
   * Gets the session ID of the current driver session.
   *
   * @return session identifier string
   */
  String getSessionId();

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend the keys to send
   */
  void sendKeys(String keysToSend);

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend the keys to send
   */
  void sendKeys(String keysToSend, long intervalInMilliSeconds);

  /**
   * Open the provided URL in the current session.
   *
   * @param url target URL to open
   */
  void open(String url);

  /**
   * Navigate back in the browser history.
   */
  void goBack();

  /**
   * Navigate forward in the browser history.
   */
  void goForward();

  /**
   * Switch to a page by its title.
   *
   * @param title page title to switch to
   */
  void switchToPage(String title);

  /**
   * Switch to a page by its index.
   *
   * @param index 0-based page index
   */
  void switchToPage(int index);

  /**
   * Switch to the last (most recently opened) page.
   */
  void switchToLastPage();

  /**
   * Switch to the next page in the list of open pages.
   */
  void switchToNextPage();

  /**
   * Switch the execution context to a frame identified by name.
   *
   * @param frameName frame name or id
   */
  void switchToFrame(String frameName);

  /**
   * Switch execution context back to the default content.
   */
  void switchToDefaultContent();

  /**
   * Press Enter key in the current context.
   */
  void pressEnter();

  /**
   * Press Escape key in the current context.
   */
  void pressEscape();

  /**
   * Delete all cookies for the current browsing context.
   */
  void deleteAllCookies();

  /**
   * Execute a synchronous JavaScript snippet in the page context.
   *
   * @param script script to execute
   * @param args   optional arguments passed to the script
   * @param <T>    expected return type
   * @return execution result cast to T
   */
  <T> T executeScriptOnDriver(String script, Object... args);

  /**
   * Execute an asynchronous JavaScript snippet in the page context.
   *
   * @param script async script to execute
   * @param args   optional arguments passed to the script
   * @param <T>    expected return type
   * @return execution result cast to T
   */
  <T> T executeAsyncScript(String script, Object... args);

  /**
   * Returns all cookies from the current session.
   *
   * @param <T> cookie representation type
   * @return list of cookies
   */
  <T> List<T> getCookies();

  /**
   * Retrieve a cookie by name.
   *
   * @param name cookie name
   * @param <T>  cookie representation type
   * @return cookie or null if not found
   */
  <T> T getCookie(String name);

  /**
   * Add a cookie to the current session.
   *
   * @param cookie cookie object
   * @param <T>    cookie representation type
   * @return added cookie
   */
  <T> T addCookie(T cookie);

  /**
   * Move focus to the element identified by the locator.
   *
   * @param locator element selector or identifier
   */
  void focus(String locator);

  /**
   * Click the element located by the locator.
   *
   * @param locator element locator
   */
  void click(String locator);

  /**
   * Perform a mouse click on the element located by the locator.
   *
   * @param locator element locator
   */
  void mouseClick(String locator);

  /**
   * Perform a mouse double-click on the element located by the locator.
   *
   * @param locator element locator
   */
  void mouseDoubleClick(String locator);

  /**
   * Scroll the element into view.
   *
   * @param locator element locator
   */
  void scrollIntoView(String locator);

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator    element locator
   * @param keysToSend keys to send
   */
  void sendKeys(String locator, String keysToSend);

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator    element locator
   * @param keysToSend keys to send
   */
  void sendKeys(String locator, String keysToSend, long intervalInMilliSeconds);

  /**
   * Drop an element to a specified offset.
   *
   * @param locator element locator
   * @param xOffset horizontal offset
   * @param yOffset vertical offset
   */
  void dropTo(String locator, int xOffset, int yOffset);

  /**
   * Move to an element with provided offsets.
   *
   * @param locator element locator
   * @param xOffset horizontal offset
   * @param yOffset vertical offset
   */
  void moveToElement(final String locator, int xOffset, int yOffset);

  /**
   * Drag source element and drop it to target with offsets.
   *
   * @param source   source element locator
   * @param target   target element locator
   * @param xOffset1 horizontal offset for source
   * @param yOffset1 vertical offset for source
   */
  void dragAndDropTo(final String source, final String target, int xOffset1, int yOffset1);

  /**
   * Drag an element from one point to another using offsets.
   *
   * @param locator  element locator
   * @param xOffset1 first horizontal offset
   * @param yOffset1 first vertical offset
   * @param xOffset2 second horizontal offset
   * @param yOffset2 second vertical offset
   */
  void dragAndDropTo(final String locator, int xOffset1, int yOffset1, int xOffset2, int yOffset2);

  /**
   * Execute a script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script  script to execute
   * @param <T>     expected return type
   * @return script execution result
   */
  <T> T executeScript(String locator, String script);

  /**
   * Execute an asynchronous script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script  script to execute
   * @param args    optional script arguments
   * @param <T>     expected return type
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
   * @param locator       element locator string
   * @param attributeName the name of the attribute
   * @return attribute value or null if not found
   */
  String getElementAttribute(String locator, String attributeName);

  /**
   * Gets a CSS property value of the element matching the locator.
   *
   * @param locator      element locator string
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
  void clearElement(String locator);

  /**
   * Fills the element with text (types text character by character).
   *
   * @param locator element locator string
   * @param text    text to type
   */
  void setText(String locator, String text);

  /**
   * Press special key press to the element.
   *
   * @param locator element locator string
   * @param text    key to press
   */
  void press(String locator, String text);

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
   * @param text    visible text to select
   */
  void selectByVisibleText(String locator, String text);

  /**
   * Selects an option by its value attribute.
   *
   * @param locator element locator string
   * @param value   the value attribute to select
   */
  void selectByValue(String locator, String value);

  /**
   * Selects an option by its index.
   *
   * @param locator element locator string
   * @param index   0-based index to select
   */
  void selectByIndex(String locator, int index);

  /**
   * Represents an option element in a select control.
   *
   * @param text  visible text of the option
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

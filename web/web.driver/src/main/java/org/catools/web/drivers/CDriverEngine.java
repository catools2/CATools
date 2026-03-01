package org.catools.web.drivers;

import java.util.List;
import org.catools.web.controls.CElementEngine;
import org.catools.web.controls.CWebFrame;
import org.catools.web.enums.CKeys;

/**
 * Core engine interface for browser automation lifecycle management.
 *
 * <p>Provides methods for navigation, element interaction, script execution, and session
 * management.
 *
 * @author CATools Team
 * @since 1.0
 */
public interface CDriverEngine<Context> extends CElementEngine<Context> {
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

  /** Closes the driver engine and releases any associated resources. */
  boolean close();

  /** Refreshes the current page. */
  boolean refresh();

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
  boolean press(CKeys... keysToSend);

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend the keys to send
   */
  boolean press(long intervalInMilliSeconds, CKeys... keysToSend);

  /**
   * Open the provided URL in the current session.
   *
   * @param url target URL to open
   */
  boolean open(String url);

  /** Navigate back in the browser history. */
  boolean goBack();

  /** Navigate forward in the browser history. */
  boolean goForward();

  /**
   * Switch to a page by its title.
   *
   * @param title page title to switch to
   */
  boolean switchToPage(String title);

  /**
   * Switch to a page by its index.
   *
   * @param index 0-based page index
   */
  boolean switchToPage(int index);

  /** Switch to the last (most recently opened) page. */
  boolean switchToLastPage();

  /** Switch to the next page in the list of open pages. */
  boolean switchToNextPage();

  /**
   * Switch the execution context to a frame identified by name.
   *
   * @param locator frame name or id
   */
  CWebFrame switchToFrame(String locator);

  /** Press Enter key in the current context. */
  boolean pressEnter();

  /** Press Escape key in the current context. */
  boolean pressEscape();

  /** Delete all cookies for the current browsing context. */
  boolean deleteAllCookies();

  /**
   * Execute a synchronous JavaScript snippet in the page context.
   *
   * @param script script to execute
   * @param args optional arguments passed to the script
   * @param <T> expected return type
   * @return execution result cast to T
   */
  <T> T executeScriptOnDriver(String script, Object... args);

  /**
   * Execute an asynchronous JavaScript snippet in the page context.
   *
   * @param script async script to execute
   * @param args optional arguments passed to the script
   * @param <T> expected return type
   * @return execution result cast to T
   */
  <T> T executeAsyncScript(String script, Object... args);

  /**
   * Returns all cookies from the current session.
   *
   * @return list of cookies
   */
  List<CDriverCookie> getCookies();

  /**
   * Retrieve a cookie by name.
   *
   * @param name cookie name
   * @return cookie or null if not found
   */
  CDriverCookie getCookie(String name);

  /**
   * Add a cookie to the current session.
   *
   * @param cookie cookie object
   * @return added cookie
   */
  CDriverCookie addCookie(CDriverCookie cookie);

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend the keys to send
   */
  boolean sendKeys(String keysToSend);

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend the keys to send
   */
  boolean sendKeys(String keysToSend, long intervalInMilliSeconds);

  /** Dismisses the currently displayed alert dialog. */
  boolean dismissAlert();

  /** Accepts the currently displayed alert dialog. */
  boolean acceptAlert();

  /**
   * Gets the text of the currently displayed alert dialog.
   *
   * @return alert text
   */
  String getAlertText();
}

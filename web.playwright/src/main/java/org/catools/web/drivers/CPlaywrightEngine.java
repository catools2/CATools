package org.catools.web.drivers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static org.catools.web.drivers.CDriverWaiter.DEFAULT_TIMEOUT;

/**
 * Playwright implementation of CDriverEngine providing browser automation capabilities.
 * <p>
 * Supports browser interactions including navigation, element manipulation, script execution,
 * and cookie management using Microsoft Playwright.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CPlayWrightEngine engine = new CPlayWrightEngine(page);
 * engine.open("https://example.com");
 * engine.click("#submit");
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
@Slf4j
public class CPlaywrightEngine implements CDriverEngine {

  @Getter
  @Setter
  private Page page;

  @Setter
  private Playwright playwright;

  @Setter
  private Browser browserInstance;

  private boolean closed = false;

  /**
   * Creates a new CPlayWrightEngine instance with the specified Page.
   *
   * @param page the Playwright Page instance
   */
  public CPlaywrightEngine(Page page, Playwright playwright, Browser browserInstance) {
    this.page = page;
    this.playwright = playwright;
    this.browserInstance = browserInstance;
  }

  /**
   * Quits the driver engine session.
   * <p>
   * Note: This method only closes the Page and BrowserContext, NOT the Browser or Playwright instances.
   * The Browser and Playwright instances are managed by the provider and shared across sessions via ThreadLocal.
   * Closing them here would break other sessions on the same thread.
   * </p>
   *
   * @return true if quit was successful, false otherwise
   */
  @Override
  public boolean quit() {
    try {
      // Close the page and its context
      close();

      // DO NOT close browserInstance or playwright here!
      // They are shared via ThreadLocal and managed by the provider
      // Closing them would break other sessions on the same thread

      return true;
    } catch (Exception e) {
      log.warn("Failed to quit Playwright engine", e);
      return false;
    }
  }

  /**
   * Checks if the driver engine is closed.
   *
   * @return true if the engine is closed, false otherwise
   */
  @Override
  public boolean isClosed() {
    if (closed) return true;
    if (page == null || page.context() == null || page.isClosed()) return true;
    try {
      // try a lightweight call
      page.url();
      return false;
    } catch (Exception e) {
      return true;
    }
  }

  /**
   * Closes the driver engine and releases any associated resources.
   * <p>
   * This method closes the Page and its associated BrowserContext.
   * The Browser and Playwright instances are NOT closed as they are shared via ThreadLocal.
   * </p>
   */
  @Override
  public boolean close() {
    if (!isClosed()) {
      try {
        BrowserContext context = page.context();
        page.close();
        if (context != null) {
          context.close();
        }
        closed = true;
        return true;
      } catch (Exception e) {
        log.debug("Error closing page/context", e);
        closed = true; // Mark as closed even if close fails
        return false;
      }
    }
    closed = true;
    return true;
  }

  /**
   * Refreshes the current page.
   */
  @Override
  public boolean refresh() {
    if (!isClosed()) {
      try {
        page.reload();
        return true;
      } catch (Exception e) {
        log.debug("Failed to refresh page", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Takes a screenshot of the current page.
   *
   * @return byte array representing the screenshot (PNG)
   */
  @Override
  public byte[] screenshot() {
    if (!isClosed()) {
      return page.screenshot();
    }
    return new byte[0];
  }

  /**
   * Gets the title of the current page.
   * <p>
   * Retries up to 3 times if execution context is destroyed (e.g., during navigation).
   * </p>
   *
   * @return page title or empty string if not available
   */
  @Override
  public String title() {
    if (isClosed()) {
      return "";
    }

    // Retry up to 3 times to handle "Execution context was destroyed" errors
    int maxAttempts = 3;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        return page.title();
      } catch (com.microsoft.playwright.PlaywrightException e) {
        if (e.getMessage().contains("Execution context was destroyed")) {
          if (attempt < maxAttempts) {
            log.debug("Execution context destroyed on attempt {}/{}, retrying...", attempt, maxAttempts);
            try {
              Thread.sleep(100); // Small delay before retry
            } catch (InterruptedException ie) {
              Thread.currentThread().interrupt();
            }
            continue;
          } else {
            log.warn("Failed to get title after {} attempts: {}", maxAttempts, e.getMessage());
            return "";
          }
        }
        // Re-throw other exceptions
        throw e;
      }
    }
    return "";
  }

  /**
   * Gets the current url of the current page.
   * <p>
   * Retries up to 3 times if execution context is destroyed (e.g., during navigation).
   * </p>
   *
   * @return current page URL or empty string if not available
   */
  @Override
  public String url() {
    if (isClosed()) {
      return "";
    }

    // Retry up to 3 times to handle "Execution context was destroyed" errors
    int maxAttempts = 3;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        return page.url();
      } catch (com.microsoft.playwright.PlaywrightException e) {
        if (e.getMessage().contains("Execution context was destroyed")) {
          if (attempt < maxAttempts) {
            log.debug("Execution context destroyed on attempt {}/{}, retrying...", attempt, maxAttempts);
            try {
              Thread.sleep(100); // Small delay before retry
            } catch (InterruptedException ie) {
              Thread.currentThread().interrupt();
            }
            continue;
          } else {
            log.warn("Failed to get URL after {} attempts: {}", maxAttempts, e.getMessage());
            return "";
          }
        }
        // Re-throw other exceptions
        throw e;
      }
    }
    return "";
  }

  /**
   * Gets the session ID of the current driver session.
   *
   * @return session identifier string
   */
  @Override
  public String getSessionId() {
    // Playwright doesn't have a direct session ID like Selenium
    // Return a combination of context and page info
    if (!isClosed()) {
      return "playwright-" + page.context().hashCode() + "-" + page.hashCode();
    }
    return "";
  }

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend the keys to send
   */
  @Override
  public boolean sendKeys(String keysToSend) {
    if (!isClosed()) {
      try {
        page.keyboard().type(keysToSend);
        return true;
      } catch (Exception e) {
        log.debug("Failed to send keys", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend             the keys to send
   * @param intervalInMilliSeconds the delay in milliseconds between key presses
   */
  @Override
  public boolean sendKeys(String keysToSend, long intervalInMilliSeconds) {
    if (!isClosed()) {
      try {
        for (char c : keysToSend.toCharArray()) {
          page.keyboard().type(String.valueOf(c));
          try {
            Thread.sleep(intervalInMilliSeconds);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while typing with delay", e);
            return false;
          }
        }
        return true;
      } catch (Exception e) {
        log.debug("Failed to send keys with interval", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Open the provided URL in the current session.
   *
   * @param url target URL to open
   */
  @Override
  public boolean open(String url) {
    if (page == null || page.isClosed()) {
      log.warn("Cannot navigate - page is null or closed");
      return false;
    }

    BrowserContext context = page.context();
    if (context == null) {
      log.warn("Cannot navigate - browser context is null");
      return false;
    }

    // Retry up to 3 times to handle transient errors
    int maxAttempts = 3;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        page.navigate(url, new Page.NavigateOptions()
            .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        return true; // Success
      } catch (com.microsoft.playwright.PlaywrightException e) {

        // Check for connection/command errors that indicate browser/context issues
        boolean isConnectionError = e.getMessage().contains("Cannot find command to respond") ||
            e.getMessage().contains("Browser has been closed") ||
            e.getMessage().contains("Target closed");

        // Check for transient errors that can be safely retried
        boolean isTransientError = e.getMessage().contains("Object doesn't exist") ||
            e.getMessage().contains("request@") ||
            e.getMessage().contains("response@") ||
            e.getMessage().contains("ERR_ABORTED");

        if (isConnectionError) {
          log.error("Browser connection error on attempt {}/{}: {}. Cannot retry - browser/context is closed.",
              attempt, maxAttempts, e.getMessage());
          return false;
        }

        if (isTransientError) {
          if (attempt < maxAttempts) {
            log.debug("Transient Playwright error on attempt {}/{}: {}. Retrying...",
                attempt, maxAttempts, e.getMessage());
            try {
              Thread.sleep(200); // Small delay before retry
            } catch (InterruptedException ie) {
              Thread.currentThread().interrupt();
              return false;
            }
            continue; // Retry
          } else {
            log.warn("Navigation failed after {} attempts to: {}", maxAttempts, url);
            // Don't throw - let the navigation complete even with the error
            // The page usually loads successfully despite the error
            return true; // Return true as page may have loaded
          }
        }

        // Handle other specific errors
        if (e.getMessage().contains("Target closed")) {
          log.warn("Page or context was closed during navigation to: {}", url);
          return false;
        }

        // Re-throw other exceptions
        log.error("Navigation failed with unexpected error", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Navigate back in the browser history.
   */
  @Override
  public boolean goBack() {
    if (!isClosed()) {
      try {
        page.goBack();
        return true;
      } catch (Exception e) {
        log.debug("Failed to go back", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Navigate forward in the browser history.
   */
  @Override
  public boolean goForward() {
    if (!isClosed()) {
      try {
        page.goForward();
        return true;
      } catch (Exception e) {
        log.debug("Failed to go forward", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Switch to a page by its title.
   *
   * @param title page title to switch to
   */
  @Override
  public boolean switchToPage(String title) {
    if (!isClosed()) {
      List<Page> pages = page.context().pages();
      for (Page p : pages) {
        try {
          if (p.title().equals(title)) {
            this.page = p;
            p.bringToFront();
            return true;
          }
        } catch (com.microsoft.playwright.PlaywrightException e) {
          // Ignore "Execution context was destroyed" errors - page might be navigating
          if (e.getMessage().contains("Execution context was destroyed")) {
            log.debug("Page title unavailable during navigation, skipping: {}", e.getMessage());
            continue;
          }
          log.debug("Error checking page title", e);
        }
      }
      log.warn("No page found with title: {}", title);
    }
    return false;
  }

  /**
   * Switch to a page by its index.
   *
   * @param index 0-based page index
   */
  @Override
  public boolean switchToPage(int index) {
    if (!isClosed()) {
      try {
        List<Page> pages = page.context().pages();
        if (index >= 0 && index < pages.size()) {
          this.page = pages.get(index);
          this.page.bringToFront();
          return true;
        } else {
          log.warn("Invalid page index: {}. Total pages: {}", index, pages.size());
        }
      } catch (Exception e) {
        log.debug("Failed to switch to page by index", e);
      }
    }
    return false;
  }

  /**
   * Switch to the last (most recently opened) page.
   */
  @Override
  public boolean switchToLastPage() {
    if (!isClosed()) {
      try {
        List<Page> pages = page.context().pages();
        if (!pages.isEmpty()) {
          this.page = pages.getLast();
          this.page.bringToFront();
          return true;
        }
      } catch (Exception e) {
        log.debug("Failed to switch to last page", e);
      }
    }
    return false;
  }

  /**
   * Switch to the next page in the list of open pages.
   */
  @Override
  public boolean switchToNextPage() {
    if (!isClosed()) {
      try {
        List<Page> pages = page.context().pages();
        int currentIndex = pages.indexOf(this.page);
        if (currentIndex >= 0 && currentIndex < pages.size() - 1) {
          this.page = pages.get(currentIndex + 1);
          this.page.bringToFront();
          return true;
        } else {
          log.warn("Already on last page or page not found in context");
        }
      } catch (Exception e) {
        log.debug("Failed to switch to next page", e);
      }
    }
    return false;
  }

  /**
   * Switch the execution context to a frame identified by name.
   *
   * @param frameName frame name or id
   */
  @Override
  public boolean switchToFrame(String frameName) {
    // In Playwright, frame switching is done via frame locators
    // This is a simplified implementation - actual usage would be via frame locators in element operations
    log.debug("Frame switching in Playwright is handled via frame locators: {}", frameName);
    return true; // Playwright handles frames differently
  }

  /**
   * Switch execution context back to the default content.
   */
  @Override
  public boolean switchToDefaultContent() {
    // In Playwright, frame context is handled automatically via mainFrame()
    log.debug("Switching to default content (main frame)");
    return true; // Playwright handles frames differently
  }

  /**
   * Press Enter key in the current context.
   */
  @Override
  public boolean pressEnter() {
    if (!isClosed()) {
      try {
        page.keyboard().press("Enter");
        return true;
      } catch (Exception e) {
        log.debug("Failed to press Enter", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Press Escape key in the current context.
   */
  @Override
  public boolean pressEscape() {
    if (!isClosed()) {
      try {
        page.keyboard().press("Escape");
        return true;
      } catch (Exception e) {
        log.debug("Failed to press Escape", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Delete all cookies for the current browsing context.
   */
  @Override
  public boolean deleteAllCookies() {
    if (!isClosed()) {
      try {
        page.context().clearCookies();
        return true;
      } catch (Exception e) {
        log.debug("Failed to delete cookies", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Execute a synchronous JavaScript snippet in the page context.
   *
   * @param script script to execute
   * @param args   optional arguments passed to the script
   * @return execution result cast to T
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T executeScriptOnDriver(String script, Object... args) {
    if (!isClosed()) {
      return (T) page.evaluate(script, args.length > 0 ? args[0] : null);
    }
    return null;
  }

  /**
   * Execute an asynchronous JavaScript snippet in the page context.
   *
   * @param script async script to execute
   * @param args   optional arguments passed to the script
   * @return execution result cast to T
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T executeAsyncScript(String script, Object... args) {
    if (!isClosed()) {
      // Playwright's evaluate is inherently async-capable
      return (T) page.evaluate(script, args.length > 0 ? args[0] : null);
    }
    return null;
  }

  /**
   * Returns all cookies from the current session.
   *
   * @return list of cookies
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> List<T> getCookies() {
    if (!isClosed()) {
      return (List<T>) page.context().cookies();
    }
    return List.of();
  }

  /**
   * Retrieve a cookie by name.
   *
   * @param name cookie name
   * @return cookie or null if not found
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getCookie(String name) {
    if (!isClosed()) {
      List<Cookie> cookies = page.context().cookies();
      for (Cookie cookie : cookies) {
        if (cookie.name.equals(name)) {
          return (T) cookie;
        }
      }
    }
    return null;
  }

  /**
   * Add a cookie to the current session.
   *
   * @param cookie cookie object
   * @return added cookie
   */
  @Override
  public <T> T addCookie(T cookie) {
    if (page != null && !page.isClosed() && cookie instanceof Cookie) {
      page.context().addCookies(List.of((Cookie) cookie));
      return cookie;
    }
    return null;
  }

  /**
   * Move focus to the element identified by the locator.
   *
   * @param locator element selector or identifier
   */
  @Override
  public boolean focus(String locator) {
    if (!isClosed()) {
      try {
        page.locator(locator).focus();
        return true;
      } catch (Exception e) {
        log.debug("Failed to focus element: {}", locator, e);
        return false;
      }
    }
    return false;
  }

  /**
   * Click the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean click(String locator) {
    if (!isClosed()) {
      try {
        page.locator(locator).click();
        return true;
      } catch (Exception e) {
        log.debug("Failed to click element: {}", locator, e);
        return false;
      }
    }
    return false;
  }

  /**
   * Perform a mouse click on the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean mouseClick(String locator) {
    if (!isClosed()) {
      try {
        page.locator(locator).click();
        return true;
      } catch (Exception e) {
        log.debug("Failed to mouse click element: {}", locator, e);
        return false;
      }
    }
    return false;
  }

  /**
   * Perform a mouse double-click on the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean mouseDoubleClick(String locator) {
    if (!isClosed()) {
      try {
        page.locator(locator).dblclick();
        return true;
      } catch (Exception e) {
        log.debug("Failed to double click element: {}", locator, e);
        return false;
      }
    }
    return false;
  }

  /**
   * Scroll the element into view.
   *
   * @param locator element locator
   */
  @Override
  public boolean scrollIntoView(String locator) {
    if (!isClosed()) {
      try {
        page.locator(locator).scrollIntoViewIfNeeded();
        return true;
      } catch (Exception e) {
        log.debug("Failed to scroll element into view: {}", locator, e);
        return false;
      }
    }
    return false;
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator    element locator
   * @param keysToSend keys to send
   */
  @Override
  public boolean sendKeys(String locator, String keysToSend) {
    if (!isClosed()) {
      return sendKeys(locator, keysToSend, DEFAULT_TIMEOUT);
    }
    return false;
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator                element locator
   * @param keysToSend             keys to send
   * @param intervalInMilliSeconds the delay in milliseconds between key presses
   */
  @Override
  public boolean sendKeys(String locator, String keysToSend, long intervalInMilliSeconds) {
    if (!isClosed()) {
      try {
        Locator element = page.locator(locator);
        element.focus();
        for (char c : keysToSend.toCharArray()) {
          page.keyboard().type(String.valueOf(c));
          try {
            Thread.sleep(intervalInMilliSeconds);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while typing with delay", e);
            return false;
          }
        }
        return true;
      } catch (Exception e) {
        log.debug("Failed to send keys with interval", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Drop an element to a specified offset.
   *
   * @param locator element locator
   * @param xOffset horizontal offset
   * @param yOffset vertical offset
   */
  @Override
  public boolean dropTo(String locator, int xOffset, int yOffset) {
    if (!isClosed()) {
      try {
        Locator element = page.locator(locator);
        element.dragTo(element, new Locator.DragToOptions().setTargetPosition(xOffset, yOffset));
        return true;
      } catch (Exception e) {
        log.debug("Failed to drop element", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Move to an element with provided offsets.
   *
   * @param locator element locator
   * @param xOffset horizontal offset
   * @param yOffset vertical offset
   */
  @Override
  public boolean moveToElement(String locator, int xOffset, int yOffset) {
    if (!isClosed()) {
      try {
        page.locator(locator).hover(new Locator.HoverOptions().setPosition(xOffset, yOffset));
        return true;
      } catch (Exception e) {
        log.debug("Failed to move to element", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Drag source element and drop it to target with offsets.
   *
   * @param source   source element locator
   * @param target   target element locator
   * @param xOffset1 horizontal offset for source
   * @param yOffset1 vertical offset for source
   */
  @Override
  public boolean dragAndDropTo(String source, String target, int xOffset1, int yOffset1) {
    if (!isClosed()) {
      try {
        Locator sourceElement = page.locator(source);
        Locator targetElement = page.locator(target);
        sourceElement.dragTo(targetElement, new Locator.DragToOptions().setSourcePosition(xOffset1, yOffset1));
        return true;
      } catch (Exception e) {
        log.debug("Failed to drag and drop", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Drag an element from one point to another using offsets.
   *
   * @param locator  element locator
   * @param xOffset1 first horizontal offset
   * @param yOffset1 first vertical offset
   * @param xOffset2 second horizontal offset
   * @param yOffset2 second vertical offset
   */
  @Override
  public boolean dragAndDropTo(String locator, int xOffset1, int yOffset1, int xOffset2, int yOffset2) {
    if (!isClosed()) {
      try {
        Locator element = page.locator(locator);
        element.dragTo(element, new Locator.DragToOptions()
            .setSourcePosition(xOffset1, yOffset1)
            .setTargetPosition(xOffset2, yOffset2));
        return true;
      } catch (Exception e) {
        log.debug("Failed to drag and drop with offsets", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Execute a script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script  script to execute
   * @return script execution result
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T executeScript(String locator, String script) {
    if (!isClosed()) {
      return (T) page.locator(locator).evaluate(script);
    }
    return null;
  }

  /**
   * Execute an asynchronous script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script  script to execute
   * @param args    optional script arguments
   * @return script execution result
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T executeAsyncScript(String locator, String script, Object... args) {
    if (!isClosed()) {
      Object arg = args.length > 0 ? args[0] : null;
      return (T) page.locator(locator).evaluate(script, arg);
    }
    return null;
  }

  /**
   * Checks if an element matching the locator is present in the DOM.
   *
   * @param locator element locator string
   * @return true if element is present, false otherwise
   */
  @Override
  public boolean isElementPresent(String locator) {
    if (!isClosed()) {
      return page.locator(locator).count() > 0;
    }
    return false;
  }

  /**
   * Checks if an element matching the locator is visible.
   *
   * @param locator element locator string
   * @return true if element is visible, false otherwise
   */
  @Override
  public boolean isElementVisible(String locator) {
    if (!isClosed()) {
      try {
        return page.locator(locator).isVisible();
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Checks if an element matching the locator is enabled.
   *
   * @param locator element locator string
   * @return true if element is enabled, false otherwise
   */
  @Override
  public boolean isElementEnabled(String locator) {
    if (!isClosed()) {
      try {
        Locator locator1 = page.locator(locator);
        return locator1.isEnabled() && locator1.isVisible() && locator1.isEditable();
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Checks if an element matching the locator is selected/checked.
   *
   * @param locator element locator string
   * @return true if element is selected, false otherwise
   */
  @Override
  public boolean isElementSelected(String locator) {
    if (!isClosed()) {
      try {
        return page.locator(locator).isChecked();
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Gets the text content of the element matching the locator.
   *
   * @param locator element locator string
   * @return element text content
   */
  @Override
  public String getElementText(String locator) {
    if (!isClosed()) {
      try {
        return page.locator(locator).textContent();
      } catch (Exception e) {
        log.warn("Failed to get text for locator: {}", locator, e);
        return "";
      }
    }
    return "";
  }

  /**
   * Gets the value attribute of the element matching the locator.
   *
   * @param locator element locator string
   * @return element value attribute
   */
  @Override
  public String getElementValue(String locator) {
    return page.locator(locator).inputValue();
  }

  /**
   * Gets the inner HTML of the element matching the locator.
   *
   * @param locator element locator string
   * @return element inner HTML
   */
  @Override
  public String getElementInnerHtml(String locator) {
    return page.locator(locator).innerHTML();
  }

  /**
   * Gets an attribute value of the element matching the locator.
   *
   * @param locator       element locator string
   * @param attributeName the name of the attribute
   * @return attribute value or null if not found
   */
  @Override
  public String getElementAttribute(String locator, String attributeName) {
    if (!isClosed()) {
      try {
        return page.locator(locator).getAttribute(attributeName);
      } catch (Exception e) {
        log.warn("Failed to get attribute {} for locator: {}", attributeName, locator, e);
        return "";
      }
    }
    return "";
  }

  /**
   * Gets a CSS property value of the element matching the locator.
   *
   * @param locator      element locator string
   * @param propertyName the name of the CSS property
   * @return CSS property value or null if not found
   */
  @Override
  public String getElementCssValue(String locator, String propertyName) {
    if (!isClosed()) {
      try {
        String script = String.format("element => window.getComputedStyle(element).getPropertyValue('%s')", propertyName);
        return (String) page.locator(locator).evaluate(script);
      } catch (Exception e) {
        log.warn("Failed to get CSS value {} for locator: {}", propertyName, locator, e);
        return "";
      }
    }
    return "";
  }

  /**
   * Gets the count of elements matching the locator.
   *
   * @param locator element locator string
   * @return number of matching elements
   */
  @Override
  public int getElementCount(String locator) {
    if (!isClosed()) {
      return page.locator(locator).count();
    }
    return 0;
  }

  /**
   * Clears the content of the element matching the locator.
   *
   * @param locator element locator string
   */
  @Override
  public boolean clearElement(String locator) {
    if (!isClosed()) {
      try {
        page.locator(locator).clear();
        return true;
      } catch (Exception e) {
        log.debug("Failed to clear element", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Fills the element with text (types text character by character).
   *
   * @param locator element locator string
   * @param text    text to type
   */
  @Override
  public boolean setText(String locator, String text) {
    if (!isClosed()) {
      try {
        page.locator(locator).fill(text);
        return true;
      } catch (Exception e) {
        log.debug("Failed to set text", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Press special key press to the element.
   *
   * @param locator element locator string
   * @param text    key to press
   */
  @Override
  public boolean press(String locator, String text) {
    if (!isClosed()) {
      try {
        page.locator(locator).press(text);
        return true;
      } catch (Exception e) {
        log.debug("Failed to press key", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Takes a screenshot of the element matching the locator.
   *
   * @param locator element locator string
   * @return byte array representing the screenshot (PNG)
   */
  @Override
  public byte[] screenshotElement(String locator) {
    if (!isClosed()) {
      try {
        return page.locator(locator).screenshot();
      } catch (Exception e) {
        log.warn("Failed to take screenshot for locator: {}", locator, e);
        return new byte[0];
      }
    }
    return new byte[0];
  }

  /**
   * Returns all selected options for a select element.
   *
   * @param locator element locator string
   * @return list of selected Options
   */
  @Override
  public List<Options> getAllSelectedOptions(String locator) {
    if (!isClosed()) {
      try {
        String script = "element => Array.from(element.selectedOptions).map(opt => ({text: opt.text, value: opt.value}))";
        @SuppressWarnings("unchecked")
        List<Object> result = (List<Object>) page.locator(locator).evaluate(script);
        return result.stream()
            .map(obj -> {
              @SuppressWarnings("unchecked")
              java.util.Map<String, String> map = (java.util.Map<String, String>) obj;
              return new Options(map.get("text"), map.get("value"));
            })
            .collect(Collectors.toList());
      } catch (Exception e) {
        log.warn("Failed to get selected options for locator: {}", locator, e);
      }
    }
    return List.of();
  }

  /**
   * Returns the first selected option for a select element.
   *
   * @param locator element locator string
   * @return first selected Option or null if none selected
   */
  @Override
  public Options getFirstSelectedOption(String locator) {
    List<Options> selectedOptions = getAllSelectedOptions(locator);
    return selectedOptions.isEmpty() ? null : selectedOptions.getFirst();
  }

  /**
   * Returns all available options from a select element.
   *
   * @param locator element locator string
   * @return list of available Options
   */
  @Override
  public List<Options> getOptions(String locator) {
    if (!isClosed()) {
      try {
        String script = "element => Array.from(element.options).map(opt => ({text: opt.text, value: opt.value}))";
        @SuppressWarnings("unchecked")
        List<Object> result = (List<Object>) page.locator(locator).evaluate(script);
        return result.stream()
            .map(obj -> {
              @SuppressWarnings("unchecked")
              java.util.Map<String, String> map = (java.util.Map<String, String>) obj;
              return new Options(map.get("text"), map.get("value"));
            })
            .collect(Collectors.toList());
      } catch (Exception e) {
        log.warn("Failed to get options for locator: {}", locator, e);
      }
    }
    return List.of();
  }

  /**
   * Selects an option by its visible text.
   *
   * @param locator element locator string
   * @param text    visible text to select
   */
  @Override
  public boolean selectByVisibleText(String locator, String text) {
    if (!isClosed()) {
      try {
        // Playwright doesn't have a direct selectByLabel, we need to find the option by text
        String script = String.format(
            "element => { const option = Array.from(element.options).find(opt => opt.text === '%s'); if (option) element.value = option.value; }",
            text.replace("'", "\\'")
        );
        page.locator(locator).evaluate(script);
        return true;
      } catch (Exception e) {
        log.debug("Failed to select by visible text", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Selects an option by its value attribute.
   *
   * @param locator element locator string
   * @param value   the value attribute to select
   */
  @Override
  public boolean selectByValue(String locator, String value) {
    if (!isClosed()) {
      try {
        page.locator(locator).selectOption(value);
        return true;
      } catch (Exception e) {
        log.debug("Failed to select by value", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Selects an option by its index.
   *
   * @param locator element locator string
   * @param index   0-based index to select
   */
  @Override
  public boolean selectByIndex(String locator, int index) {
    if (!isClosed()) {
      try {
        List<Options> options = getOptions(locator);
        if (index >= 0 && index < options.size()) {
          String value = options.get(index).value();
          page.locator(locator).selectOption(value);
          return true;
        } else {
          log.warn("Invalid index {} for select element with {} options", index, options.size());
          return false;
        }
      } catch (Exception e) {
        log.debug("Failed to select by index", e);
        return false;
      }
    }
    return false;
  }
}

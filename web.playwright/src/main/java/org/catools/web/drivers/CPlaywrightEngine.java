package org.catools.web.drivers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.SameSiteAttribute;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.date.CDate;
import org.catools.common.utils.CSleeper;
import org.catools.web.controls.CElementEngine;
import org.catools.web.controls.CWebFrame;
import org.catools.web.enums.CKeys;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Playwright implementation of CDriverEngine providing browser automation capabilities.
 *
 * <p>Supports browser interactions including navigation, element manipulation, script execution,
 * and cookie management using Microsoft Playwright.
 *
 * <p>Example usage:
 *
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
public class CPlaywrightEngine extends CPlaywrightElementEngine implements CDriverEngine<Locator> {

  private Page page;

  @Setter private Playwright playwright;

  @Setter private Browser browserInstance;

  private boolean closed = false;

  /**
   * Creates a new CPlayWrightEngine instance with the specified Page.
   *
   * @param page the Playwright Page instance
   */
  public CPlaywrightEngine(Page page, Playwright playwright, Browser browserInstance) {
    super(page);
    this.page = page;
    this.playwright = playwright;
    this.browserInstance = browserInstance;
  }

  /**
   * Quits the driver engine session.
   *
   * <p>Note: This method only closes the Page and BrowserContext, NOT the Browser or Playwright
   * instances. The Browser and Playwright instances are managed by the provider and shared across
   * sessions via ThreadLocal. Closing them here would break other sessions on the same thread.
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
   *
   * <p>This method closes the Page and its associated BrowserContext. The Browser and Playwright
   * instances are NOT closed as they are shared via ThreadLocal.
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

  /** Refreshes the current page. */
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
   *
   * <p>Retries up to 3 times if execution context is destroyed (e.g., during navigation).
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
            log.debug(
                "Execution context destroyed on attempt {}/{}, retrying...", attempt, maxAttempts);
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
   *
   * <p>Retries up to 3 times if execution context is destroyed (e.g., during navigation).
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
            log.debug(
                "Execution context destroyed on attempt {}/{}, retrying...", attempt, maxAttempts);
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
   * @param keysToSend the keys to send
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
   * Sends keys (CKey enum values) to the current focused element. Converts CKey enums to Playwright
   * key format.
   *
   * @param keys the keys to send (can be CKey or String)
   * @return true if successful, false otherwise
   */
  @Override
  public boolean press(CKeys... keys) {
    return press(0, keys);
  }

  /**
   * Sends keys to the current focused element.
   *
   * @param intervalInMilliSeconds
   * @param keysToSend the keys to send
   */
  @Override
  public boolean press(long intervalInMilliSeconds, CKeys... keysToSend) {
    if (isClosed()) return false;
    if (keysToSend == null || keysToSend.length == 0) return false;

    try {
      // if one key
      if (keysToSend.length == 1) {
        page.keyboard().press(keysToSend[0].getPlaywrightKey());
        return true;
      }

      // if multiple keys then press down all then release all
      for (CKeys cKeys : keysToSend) {
        page.keyboard().down(cKeys.getPlaywrightKey());
      }

      CSleeper.sleepTight(10);

      for (CKeys cKeys : keysToSend) {
        page.keyboard().up(cKeys.getPlaywrightKey());
      }

      return true;
    } catch (Exception e) {
      log.debug("Failed to send keys with CKey array", e);
      return false;
    }
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
        page.navigate(
            url,
            new Page.NavigateOptions()
                .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        return true; // Success
      } catch (com.microsoft.playwright.PlaywrightException e) {

        // Check for connection/command errors that indicate browser/context issues
        boolean isConnectionError =
            e.getMessage().contains("Cannot find command to respond")
                || e.getMessage().contains("Browser has been closed")
                || e.getMessage().contains("Target closed");

        // Check for transient errors that can be safely retried
        boolean isTransientError =
            e.getMessage().contains("Object doesn't exist")
                || e.getMessage().contains("request@")
                || e.getMessage().contains("response@")
                || e.getMessage().contains("ERR_ABORTED");

        if (isConnectionError) {
          log.error(
              "Browser connection error on attempt {}/{}: {}. Cannot retry - browser/context is closed.",
              attempt,
              maxAttempts,
              e.getMessage());
          return false;
        }

        if (isTransientError) {
          if (attempt < maxAttempts) {
            log.debug(
                "Transient Playwright error on attempt {}/{}: {}. Retrying...",
                attempt,
                maxAttempts,
                e.getMessage());
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

  /** Navigate back in the browser history. */
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

  /** Navigate forward in the browser history. */
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

  /** Switch to the last (most recently opened) page. */
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

  /** Switch to the next page in the list of open pages. */
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
   * @param frameLocator frame name or id
   */
  @Override
  public CWebFrame switchToFrame(String frameLocator) {
    if (isClosed()) return null;
    try {
      return new CWebFrame("Frame", this, frameLocator) {
        @Override
        public CElementEngine<?> getElementEngine() {
          return new CPlaywrightEngine(page, playwright, browserInstance) {
            @Override
            public Locator getContext(String locator) {
              return page.frameLocator(frameLocator).locator(locator);
            }
          };
        }
      };
    } catch (Exception e) {
      log.debug("switchToFrame failed for {}", frameLocator, e);
      return null;
    }
  }

  /** Press Enter key in the current context. */
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

  /** Press Escape key in the current context. */
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

  /** Delete all cookies for the current browsing context. */
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
   * @param args optional arguments passed to the script
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
   * @param args optional arguments passed to the script
   * @return execution result cast to T
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T executeAsyncScript(String script, Object... args) {
    if (!isClosed()) {
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
  public List<CDriverCookie> getCookies() {
    if (!isClosed()) {
      return page.context().cookies().stream()
          .map(CPlaywrightEngine::getCDriverCookie)
          .collect(Collectors.toList());
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
  public CDriverCookie getCookie(String name) {
    if (!isClosed()) {
      List<Cookie> cookies = page.context().cookies();
      for (Cookie cookie : cookies) {
        if (cookie.name.equals(name)) {
          return getCDriverCookie(cookie);
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
  public CDriverCookie addCookie(CDriverCookie cookie) {
    if (page != null && !page.isClosed()) {
      Cookie playwrightCookie =
          new Cookie(cookie.getName(), cookie.getValue())
              .setDomain(cookie.getDomain())
              .setPath(cookie.getPath())
              .setExpires(cookie.getExpiry() == null ? null : cookie.getExpiry().getTime())
              .setSecure(cookie.isSecure())
              .setHttpOnly(cookie.isHttpOnly())
              .setSameSite(
                  cookie.getSameSite() == null
                      ? null
                      : SameSiteAttribute.valueOf(cookie.getSameSite()));
      page.context().addCookies(List.of(playwrightCookie));
      return cookie;
    }
    return null;
  }

  /** Dismisses the currently displayed alert dialog. */
  @Override
  public boolean dismissAlert() {
    if (isClosed()) return false;
    try {
      page.onDialog(
          dialog -> {
            dialog.dismiss();
          });
      return true;
    } catch (Exception e) {
      log.debug("Failed to set up alert dismiss handler", e);
      return false;
    }
  }

  /** Accepts the currently displayed alert dialog. */
  @Override
  public boolean acceptAlert() {
    if (isClosed()) return false;
    try {
      page.onDialog(
          dialog -> {
            dialog.accept();
          });
      return true;
    } catch (Exception e) {
      log.debug("Failed to set up alert accept handler", e);
      return false;
    }
  }

  /**
   * Gets the text of the currently displayed alert dialog.
   *
   * @return alert text
   */
  @Override
  public String getAlertText() {
    if (isClosed()) return "";
    try {
      AtomicReference<String> alertText = new AtomicReference<>("");
      page.onDialog(
          dialog -> {
            alertText.set(dialog.message());
            dialog.accept();
          });
      return alertText.get();
    } catch (Exception e) {
      log.debug("Failed to get alert text", e);
      return "";
    }
  }

  private Locator findElement(String locator) {
    return page.locator(locator);
  }

  @NotNull private static CDriverCookie getCDriverCookie(Cookie cookie) {
    return new CDriverCookie(
        cookie.name,
        cookie.value,
        cookie.domain,
        cookie.path,
        cookie.expires == null ? null : new CDate(cookie.expires.longValue()),
        cookie.secure,
        cookie.httpOnly,
        cookie.sameSite == null ? null : cookie.sameSite.name());
  }
}

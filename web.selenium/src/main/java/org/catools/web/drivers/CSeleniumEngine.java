package org.catools.web.drivers;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.catools.web.controls.CElementEngine;
import org.catools.web.controls.CWebFrame;
import org.catools.web.enums.CKeys;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Selenium implementation of CDriverEngine providing browser automation capabilities.
 *
 * <p>Supports browser interactions including navigation, element manipulation, script execution,
 * and cookie management using Selenium WebDriver.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CSeleniumEngine engine = new CSeleniumEngine(driver);
 * engine.open("https://example.com");
 * engine.click("#submit");
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
@Slf4j
public class CSeleniumEngine extends CSeleniumElementEngine implements CDriverEngine<WebElement> {

  private final WebDriver driver;

  private boolean closed = false;

  public CSeleniumEngine(WebDriver driver) {
    super(driver);
    this.driver = driver;
  }

  /**
   * Quits the driver engine session.
   *
   * @return true if quit was successful, false otherwise
   */
  @Override
  public boolean quit() {
    try {
      close();
      if (driver != null) {
        try {
          driver.quit();
        } catch (Exception e) {
          log.debug("Driver quit threw", e);
        }
      }
      closed = true;
      return true;
    } catch (Exception e) {
      log.warn("Failed to quit Selenium engine", e);
      return false;
    }
  }

  /**
   * Checks if the driver engine is closed. Optimized to check cached flag first before making
   * expensive calls.
   *
   * @return true if the engine is closed, false otherwise
   */
  @Override
  public boolean isClosed() {
    // Fast path: check flag first
    if (closed) return true;
    if (driver == null) return true;

    // try a lightweight call
    try {
      driver.getWindowHandle();
      return false;
    } catch (Exception e) {
      closed = true; // Cache the result
      return true;
    }
  }

  /** Closes the driver engine and releases any associated resources. */
  @Override
  public boolean close() {
    try {
      if (driver != null) {
        driver.close();
      }
      closed = true;
      return true;
    } catch (Exception e) {
      log.debug("Failed to close driver", e);
      closed = true; // Mark as closed even if close fails
      return false;
    }
  }

  /** Refreshes the current page. */
  @Override
  public boolean refresh() {
    if (isClosed()) return false;
    try {
      driver.navigate().refresh();
      return true;
    } catch (Exception e) {
      log.debug("Failed to refresh page", e);
      return false;
    }
  }

  /**
   * Takes a screenshot of the current page.
   *
   * @return byte array representing the screenshot (PNG)
   */
  @Override
  public byte[] screenshot() {
    if (!isClosed()) {
      if (driver instanceof TakesScreenshot) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      }
    }
    return new byte[0];
  }

  /**
   * Gets the title of the current page.
   *
   * @return page title or empty string if not available
   */
  @Override
  public String title() {
    if (isClosed()) return "";
    return driver.getTitle();
  }

  /**
   * Gets the current url of the current page.
   *
   * @return current page URL
   */
  @Override
  public String url() {
    if (isClosed()) return "";
    return driver.getCurrentUrl();
  }

  /**
   * Gets the session ID of the current driver session.
   *
   * @return session identifier string
   */
  @Override
  public String getSessionId() {
    if (isClosed()) return "";
    if (driver instanceof RemoteWebDriver) {
      try {
        SessionId id = ((RemoteWebDriver) driver).getSessionId();
        return id != null ? id.toString() : "";
      } catch (Exception e) {
        log.debug("Failed to get session id", e);
      }
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
    return sendKeys(keysToSend, 0);
  }

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend the keys to send
   * @param intervalInMilliSeconds interval between keystrokes
   */
  @Override
  public boolean sendKeys(String keysToSend, long intervalInMilliSeconds) {
    if (isClosed()) return false;
    try {
      WebElement active = driver.switchTo().activeElement();
      if (active == null) return false;
      for (char c : keysToSend.toCharArray()) {
        active.sendKeys(String.valueOf(c));
        try {
          Thread.sleep(intervalInMilliSeconds);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      log.debug("sendKeys with interval failed", e);
      return false;
    }
  }

  /**
   * Sends keys (CKey enum values) to the current focused element. Converts CKey enums to Selenium
   * Keys format.
   *
   * @param keys the keys to send (can be CKey, String, or Selenium Keys)
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
    try {
      WebElement active = driver.switchTo().activeElement();
      for (CKeys c : keysToSend) {
        active.sendKeys(Keys.getKeyFromUnicode(c.toSeleniumKey()));
        try {
          Thread.sleep(intervalInMilliSeconds);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      log.debug("sendKeys with interval failed", e);
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
    if (isClosed()) return false;
    try {
      driver.get(url);
      return true;
    } catch (Exception e) {
      log.debug("Open url failed", e);
      return false;
    }
  }

  /** Navigate back in the browser history. */
  @Override
  public boolean goBack() {
    if (isClosed()) return false;
    try {
      driver.navigate().back();
      return true;
    } catch (Exception e) {
      log.debug("Navigate back failed", e);
      return false;
    }
  }

  /** Navigate forward in the browser history. */
  @Override
  public boolean goForward() {
    if (isClosed()) return false;
    try {
      driver.navigate().forward();
      return true;
    } catch (Exception e) {
      log.debug("Navigate forward failed", e);
      return false;
    }
  }

  /**
   * Switch to a page by its title.
   *
   * @param title page title to switch to
   */
  @Override
  public boolean switchToPage(String title) {
    if (isClosed()) return false;
    try {
      Set<String> handles = driver.getWindowHandles();
      for (String h : handles) {
        try {
          driver.switchTo().window(h);
          if (title.equals(driver.getTitle())) return true;
        } catch (Exception ignore) {
        }
      }
      log.warn("No page found with title: {}", title);
      return false;
    } catch (Exception e) {
      log.debug("switchToPage by title failed", e);
      return false;
    }
  }

  /**
   * Switch to a page by its index.
   *
   * @param index 0-based page index
   */
  @Override
  public boolean switchToPage(int index) {
    if (isClosed()) return false;
    try {
      List<String> handles = new ArrayList<>(driver.getWindowHandles());
      if (index >= 0 && index < handles.size()) {
        driver.switchTo().window(handles.get(index));
        return true;
      } else {
        log.warn("Invalid page index: {}. Total pages: {}", index, handles.size());
        return false;
      }
    } catch (Exception e) {
      log.debug("switchToPage by index failed", e);
      return false;
    }
  }

  /** Switch to the last (most recently opened) page. */
  @Override
  public boolean switchToLastPage() {
    if (isClosed()) return false;
    try {
      List<String> handles = new ArrayList<>(driver.getWindowHandles());
      if (!handles.isEmpty()) {
        driver.switchTo().window(handles.get(handles.size() - 1));
        return true;
      }
      return false;
    } catch (Exception e) {
      log.debug("switchToLastPage failed", e);
      return false;
    }
  }

  /** Switch to the next page in the list of open pages. */
  @Override
  public boolean switchToNextPage() {
    if (isClosed()) return false;
    try {
      List<String> handles = new ArrayList<>(driver.getWindowHandles());
      String current = driver.getWindowHandle();
      int idx = handles.indexOf(current);
      if (idx >= 0 && idx < handles.size() - 1) {
        driver.switchTo().window(handles.get(idx + 1));
        return true;
      } else {
        log.warn("Already on last page or page not found in context");
        return false;
      }
    } catch (Exception e) {
      log.debug("switchToNextPage failed", e);
      return false;
    }
  }

  /**
   * Switch the execution context to a frame identified by name.
   *
   * @param locator frame name or id
   */
  @Override
  public CWebFrame switchToFrame(String locator) {
    if (isClosed()) return null;
    try {
      return new CWebFrame(locator, this, locator) {
        @Override
        public CElementEngine<?> getElementEngine() {
          return new CSeleniumEngine(driver) {
            @Override
            public <T> T performActionOnContext(String locator, Function<WebElement, T> action) {
              try {
                driver.switchTo().frame(locator);
                return super.performActionOnContext(locator, action);
              } finally {
                driver.switchTo().defaultContent();
              }
            }
          };
        }
      };
    } catch (Exception e) {
      log.debug("switchToFrame failed for {}", locator, e);
      return null;
    }
  }

  /** Press Enter key in the current context. */
  @Override
  public boolean pressEnter() {
    if (isClosed()) return false;
    try {
      driver.switchTo().activeElement().sendKeys(Keys.ENTER);
      return true;
    } catch (Exception e) {
      log.debug("pressEnter failed", e);
      return false;
    }
  }

  /** Press Escape key in the current context. */
  @Override
  public boolean pressEscape() {
    if (isClosed()) return false;
    try {
      driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
      return true;
    } catch (Exception e) {
      log.debug("pressEscape failed", e);
      return false;
    }
  }

  /** Delete all cookies for the current browsing context. */
  @Override
  public boolean deleteAllCookies() {
    if (isClosed()) return false;
    try {
      driver.manage().deleteAllCookies();
      return true;
    } catch (Exception e) {
      log.debug("deleteAllCookies failed", e);
      return false;
    }
  }

  /**
   * Execute a synchronous JavaScript snippet in the page context.
   *
   * @param script script to execute
   * @param args optional arguments passed to the script
   * @return execution result cast to T
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T executeScriptOnDriver(String script, Object... args) {
    if (isClosed()) return null;
    try {
      if (driver instanceof JavascriptExecutor) {
        return (T) ((JavascriptExecutor) driver).executeScript(script, args);
      }
    } catch (Exception e) {
      log.debug("executeScript failed", e);
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
  @SuppressWarnings("unchecked")
  @Override
  public <T> T executeAsyncScript(String script, Object... args) {
    if (isClosed()) return null;
    try {
      if (driver instanceof JavascriptExecutor) {
        return (T) ((JavascriptExecutor) driver).executeAsyncScript(script, args);
      }
    } catch (Exception e) {
      log.debug("executeAsyncScript failed", e);
    }
    return null;
  }

  /**
   * Returns all cookies from the current session.
   *
   * @return list of cookies
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<CDriverCookie> getCookies() {
    if (isClosed()) return List.of();
    try {
      return driver.manage().getCookies().stream()
          .map(cookie -> getCDriverCookie(cookie))
          .collect(Collectors.toList());
    } catch (Exception e) {
      log.debug("getCookies failed", e);
      return List.of();
    }
  }

  /**
   * Retrieve a cookie by name.
   *
   * @param name cookie name
   * @return cookie or null if not found
   */
  @SuppressWarnings("unchecked")
  @Override
  public CDriverCookie getCookie(String name) {
    if (isClosed()) return null;
    try {
      org.openqa.selenium.Cookie c = driver.manage().getCookieNamed(name);
      return getCDriverCookie(c);
    } catch (Exception e) {
      log.debug("getCookie failed", e);
      return null;
    }
  }

  /**
   * Add a cookie to the current session.
   *
   * @param cookie cookie object
   * @return added cookie
   */
  @SuppressWarnings("unchecked")
  @Override
  public CDriverCookie addCookie(CDriverCookie cookie) {
    if (isClosed()) return null;
    try {
      driver
          .manage()
          .addCookie(
              new Cookie(
                  cookie.getName(),
                  cookie.getValue(),
                  cookie.getDomain(),
                  cookie.getPath(),
                  cookie.getExpiry(),
                  cookie.isSecure(),
                  cookie.isHttpOnly(),
                  cookie.getSameSite()));
      return cookie;
    } catch (Exception e) {
      log.debug("addCookie failed", e);
    }
    return null;
  }

  /** Dismisses the currently displayed alert dialog. */
  @Override
  public boolean dismissAlert() {
    if (isClosed()) return false;
    try {
      Alert alert = driver.switchTo().alert();
      alert.dismiss();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    } catch (Exception e) {
      log.debug("Failed to dismiss alert", e);
      return false;
    }
  }

  /** Accepts the currently displayed alert dialog. */
  @Override
  public boolean acceptAlert() {
    if (isClosed()) return false;
    try {
      Alert alert = driver.switchTo().alert();
      alert.accept();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    } catch (Exception e) {
      log.debug("Failed to dismiss alert", e);
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
    if (isClosed()) return Strings.EMPTY;
    try {
      Alert alert = driver.switchTo().alert();
      return alert.getText();
    } catch (NoAlertPresentException e) {
      log.debug("No alert present to dismiss");
      return Strings.EMPTY;
    } catch (Exception e) {
      log.debug("Failed to dismiss alert", e);
      return Strings.EMPTY;
    }
  }

  private static @NotNull CDriverCookie getCDriverCookie(Cookie c) {
    return new CDriverCookie(
        c.getName(),
        c.getValue(),
        c.getDomain(),
        c.getPath(),
        c.getExpiry(),
        c.isSecure(),
        c.isHttpOnly(),
        c.getSameSite());
  }
}

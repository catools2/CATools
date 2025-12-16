package org.catools.web.drivers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Selenium implementation of CDriverEngine providing browser automation capabilities.
 * <p>
 * Supports browser interactions including navigation, element manipulation, script execution,
 * and cookie management using Selenium WebDriver.
 * </p>
 *
 * <p>Example usage:</p>
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
public class CSeleniumEngine implements CDriverEngine {

  @Getter
  @Setter
  private WebDriver driver;

  private boolean closed = false;

  public CSeleniumEngine(WebDriver driver) {
    this.driver = driver;
  }

  // Helper: resolve By from a locator string (basic heuristic)
  private By toBy(String locator) {
    if (locator == null) return By.cssSelector("");
    locator = locator.trim();
    if (locator.startsWith("//") || locator.startsWith(".//")) {
      return By.xpath(locator);
    }
    // If locator looks like css or id/class, treat as CSS selector
    return By.cssSelector(locator);
  }

  private WebElement findElementSafe(String locator) {
    if (isClosed()) return null;
    try {
      return driver.findElement(toBy(locator));
    } catch (Exception e) {
      return null;
    }
  }

  private List<WebElement> findElementsSafe(String locator) {
    if (isClosed()) return List.of();
    try {
      return driver.findElements(toBy(locator));
    } catch (Exception e) {
      return List.of();
    }
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
   * Checks if the driver engine is closed.
   * Optimized to check cached flag first before making expensive calls.
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

  /**
   * Closes the driver engine and releases any associated resources.
   */
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

  /**
   * Refreshes the current page.
   */
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
    if (isClosed()) return false;
    try {
      WebElement active = driver.switchTo().activeElement();
      if (active != null) active.sendKeys(keysToSend);
      return true;
    } catch (Exception e) {
      log.debug("sendKeys failed", e);
      return false;
    }
  }

  /**
   * Sends keys to the current focused element.
   *
   * @param keysToSend             the keys to send
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

  /**
   * Navigate back in the browser history.
   */
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

  /**
   * Navigate forward in the browser history.
   */
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

  /**
   * Switch to the last (most recently opened) page.
   */
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

  /**
   * Switch to the next page in the list of open pages.
   */
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
   * @param frameName frame name or id
   */
  @Override
  public boolean switchToFrame(String frameName) {
    if (isClosed()) return false;
    try {
      driver.switchTo().frame(frameName);
      return true;
    } catch (Exception e) {
      log.debug("switchToFrame failed for {}", frameName, e);
      return false;
    }
  }

  /**
   * Switch execution context back to the default content.
   */
  @Override
  public boolean switchToDefaultContent() {
    if (isClosed()) return false;
    try {
      driver.switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      log.debug("switchToDefaultContent failed", e);
      return false;
    }
  }

  /**
   * Press Enter key in the current context.
   */
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

  /**
   * Press Escape key in the current context.
   */
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

  /**
   * Delete all cookies for the current browsing context.
   */
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
   * @param args   optional arguments passed to the script
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
   * @param args   optional arguments passed to the script
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
  public <T> List<T> getCookies() {
    if (isClosed()) return List.of();
    try {
      Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
      return (List<T>) new ArrayList<>(cookies);
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
  public <T> T getCookie(String name) {
    if (isClosed()) return null;
    try {
      org.openqa.selenium.Cookie c = driver.manage().getCookieNamed(name);
      return (T) c;
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
  public <T> T addCookie(T cookie) {
    if (isClosed()) return null;
    try {
      if (cookie instanceof org.openqa.selenium.Cookie) {
        driver.manage().addCookie((org.openqa.selenium.Cookie) cookie);
        return cookie;
      }
    } catch (Exception e) {
      log.debug("addCookie failed", e);
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
    if (isClosed()) return false;
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Actions(driver).moveToElement(el).perform();
      return true;
    } catch (Exception e) {
      log.debug("focus failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Click the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean click(String locator) {
    if (isClosed()) return false;
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      el.click();
      return true;
    } catch (Exception e) {
      try {
        // fallback: JS click
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        return true;
      } catch (Exception ex) {
        log.debug("click failed for {}", locator, ex);
        return false;
      }
    }
  }

  /**
   * Perform a mouse click on the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean mouseClick(String locator) {
    if (isClosed()) return false;
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Actions(driver).click(el).perform();
      return true;
    } catch (Exception e) {
      log.debug("mouseClick failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Perform a mouse double-click on the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean mouseDoubleClick(String locator) {
    if (isClosed()) return false;
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Actions(driver).doubleClick(el).perform();
      return true;
    } catch (Exception e) {
      log.debug("mouseDoubleClick failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Scroll the element into view.
   *
   * @param locator element locator
   */
  @Override
  public boolean scrollIntoView(String locator) {
    if (isClosed()) return false;
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
      return true;
    } catch (Exception e) {
      log.debug("scrollIntoView failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator    element locator
   * @param keysToSend keys to send
   */
  @Override
  public boolean sendKeys(String locator, String keysToSend) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      el.clear();
      el.sendKeys(keysToSend);
      return true;
    } catch (Exception e) {
      log.debug("sendKeys to locator failed {}", locator, e);
      return false;
    }
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator                element locator
   * @param keysToSend             keys to send
   * @param intervalInMilliSeconds interval between keystrokes
   */
  @Override
  public boolean sendKeys(String locator, String keysToSend, long intervalInMilliSeconds) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      el.click();
      for (char c : keysToSend.toCharArray()) {
        el.sendKeys(String.valueOf(c));
        try {
          Thread.sleep(intervalInMilliSeconds);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      log.debug("sendKeys with interval failed for {}", locator, e);
      return false;
    }
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
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Actions(driver).dragAndDropBy(el, xOffset, yOffset).perform();
      return true;
    } catch (Exception e) {
      log.debug("dropTo failed for {}", locator, e);
      return false;
    }
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
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Actions(driver).moveToElement(el, xOffset, yOffset).perform();
      return true;
    } catch (Exception e) {
      log.debug("moveToElement failed for {}", locator, e);
      return false;
    }
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
    WebElement src = findElementSafe(source);
    WebElement tgt = findElementSafe(target);
    if (src == null || tgt == null) return false;
    try {
      new Actions(driver).dragAndDrop(src, tgt).perform();
      return true;
    } catch (Exception e) {
      log.debug("dragAndDropTo failed", e);
      return false;
    }
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
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      // move by offsets using dragAndDropBy twice
      new Actions(driver).dragAndDropBy(el, xOffset2 - xOffset1, yOffset2 - yOffset1).perform();
      return true;
    } catch (Exception e) {
      log.debug("dragAndDropTo with offsets failed", e);
      return false;
    }
  }

  /**
   * Execute a script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script  script to execute
   * @return script execution result
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T executeScript(String locator, String script) {
    WebElement el = findElementSafe(locator);
    if (el == null) return null;
    try {
      if (driver instanceof JavascriptExecutor) {
        return (T) ((JavascriptExecutor) driver).executeScript(script, el);
      }
    } catch (Exception e) {
      log.debug("executeScript on element failed", e);
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
  @SuppressWarnings("unchecked")
  @Override
  public <T> T executeAsyncScript(String locator, String script, Object... args) {
    WebElement el = findElementSafe(locator);
    if (el == null) return null;
    try {
      if (driver instanceof JavascriptExecutor) {
        Object[] allArgs = new Object[args.length + 1];
        allArgs[0] = el;
        System.arraycopy(args, 0, allArgs, 1, args.length);
        return (T) ((JavascriptExecutor) driver).executeAsyncScript(script, allArgs);
      }
    } catch (Exception e) {
      log.debug("executeAsyncScript on element failed", e);
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
    return !findElementsSafe(locator).isEmpty();
  }

  /**
   * Checks if an element matching the locator is visible to the user.
   *
   * <p>This method performs comprehensive visibility checks including:
   * <ul>
   *   <li>Element exists in DOM</li>
   *   <li>Element is displayed (not display:none or visibility:hidden)</li>
   *   <li>Element has non-zero opacity</li>
   *   <li>Element has non-zero dimensions (width and height)</li>
   * </ul>
   *
   * @param locator element locator string
   * @return true if element is visible to the user, false otherwise
   */
  @Override
  public boolean isElementVisible(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;

    try {
      // If display and visible by CSS
      if (!el.isDisplayed()) {
        return false;
      }

      // If Element have non-zero dimensions
      org.openqa.selenium.Dimension size = el.getSize();
      if (size.getWidth() <= 0 || size.getHeight() <= 0) {
        return false;
      }

      // opacity must be greater than 0
      String opacity = el.getCssValue("opacity");
      if (opacity != null && !opacity.isEmpty()) {
        try {
          double opacityValue = Double.parseDouble(opacity);
          if (opacityValue <= 0) {
            return false;
          }
        } catch (NumberFormatException e) {
          // Opacity value is not a number, assume visible
        }
      }

      // must not be hidden via JavaScript
      Boolean isHidden = (Boolean) ((JavascriptExecutor) driver).executeScript(
          "var el = arguments[0];" +
          "var style = window.getComputedStyle(el);" +
          "if (style.display === 'none' || style.visibility === 'hidden') return true;" +
          "if (parseFloat(style.opacity) === 0) return true;" +
          "if (el.offsetWidth === 0 || el.offsetHeight === 0) return true;" +
          "if (el.hasAttribute('hidden')) return true;" +
          "if (el.getAttribute('aria-hidden') === 'true') return true;" +
          "return false;",
          el
      );

      if (Boolean.TRUE.equals(isHidden)) {
        return false;
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks if an element matching the locator is enabled.
   *
   * @param locator element locator string
   * @return true if element is enabled, false otherwise
   */
  @Override
  public boolean isElementEnabled(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      return el.isEnabled() && el.isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks if an element matching the locator is selected/checked.
   *
   * @param locator element locator string
   * @return true if element is selected, false otherwise
   */
  @Override
  public boolean isElementSelected(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      return el.isSelected();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Gets the text content of the element matching the locator.
   *
   * @param locator element locator string
   * @return element text content
   */
  @Override
  public String getElementText(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return "";
    try {
      return el.getText();
    } catch (Exception e) {
      log.debug("getElementText failed for {}", locator, e);
      return "";
    }
  }

  /**
   * Gets the value attribute of the element matching the locator.
   *
   * @param locator element locator string
   * @return element value attribute
   */
  @Override
  public String getElementValue(String locator) {
    return getElementAttribute(locator, "value");
  }

  /**
   * Gets the inner HTML of the element matching the locator.
   *
   * @param locator element locator string
   * @return element inner HTML
   */
  @Override
  public String getElementInnerHtml(String locator) {
    return executeScript(locator, "return arguments[0].innerHTML");
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
    WebElement el = findElementSafe(locator);
    if (el == null) return "";
    try {
      return el.getAttribute(attributeName);
    } catch (Exception e) {
      log.debug("getElementAttribute failed for {}", locator, e);
      return "";
    }
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
    WebElement el = findElementSafe(locator);
    if (el == null) return "";
    try {
      return el.getCssValue(propertyName);
    } catch (Exception e) {
      log.debug("getElementCssValue failed for {}", locator, e);
      return "";
    }
  }

  /**
   * Gets the count of elements matching the locator.
   *
   * @param locator element locator string
   * @return number of matching elements
   */
  @Override
  public int getElementCount(String locator) {
    return findElementsSafe(locator).size();
  }

  /**
   * Clears the content of the element matching the locator.
   * Uses multiple strategies to ensure the element is properly cleared:
   *
   * @param locator element locator string
   */
  @Override
  public boolean clearElement(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      // Fast path: Standard WebDriver clear (works for 90% of cases)
      el.clear();

      // Try keyboard-based clearing (works better for React/Angular apps)
      String os = System.getProperty("os.name").toLowerCase();
      Keys modifier = os.contains("mac") ? Keys.COMMAND : Keys.CONTROL;
      el.sendKeys(Keys.chord(modifier, "a"));
      el.sendKeys(Keys.DELETE);
      el.sendKeys(Keys.BACK_SPACE);

      // Verify if clear worked
      // Fallback for React/Angular/Vue: Use JavaScript
      ((JavascriptExecutor) driver).executeScript(
          "arguments[0].value = '';" +
              "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
              "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
          el
      );
      return true;
    } catch (Exception e) {
      log.debug("clearElement failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Fills the element with text (types text character by character).
   * Optimized for performance while maintaining framework compatibility.
   *
   * @param locator element locator string
   * @param text    text to type
   */
  @Override
  public boolean setText(String locator, String text) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      // Fast path: Standard clear + sendKeys
      el.click();
      el.clear();
      el.sendKeys(text);

      // Only trigger JavaScript events if clear didn't work properly
      // This handles React/Angular/Vue without the overhead for simpler pages
      String currentValue = el.getAttribute("value");
      if (currentValue == null || !currentValue.equals(text)) {
        // Fallback: Use JavaScript to set value and trigger events
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].focus();" +
                "arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            el, text
        );
      }
      return true;
    } catch (Exception e) {
      log.debug("setText failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Press special key press to the element.
   *
   * @param locator element locator string
   * @param text    key to press
   */
  @Override
  public boolean press(String locator, String text) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      el.sendKeys(Keys.valueOf(text.toUpperCase()));
      return true;
    } catch (Exception e) {
      log.debug("press failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Takes a screenshot of the element matching the locator.
   *
   * @param locator element locator string
   * @return byte array representing the screenshot (PNG)
   */
  @Override
  public byte[] screenshotElement(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return new byte[0];
    try {
      // WebElement.getScreenshotAs is available in Selenium 4
      return el.getScreenshotAs(OutputType.BYTES);
    } catch (Exception e) {
      log.warn("screenshotElement failed for {}", locator, e);
      return new byte[0];
    }
  }

  /**
   * Returns all selected options for a select element.
   *
   * @param locator element locator string
   * @return list of selected Options
   */
  @Override
  public List<Options> getAllSelectedOptions(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return List.of();
    try {
      Select select = new Select(el);
      List<WebElement> selected = select.getAllSelectedOptions();
      return selected.stream().map(o -> new Options(o.getText(), o.getAttribute("value"))).collect(Collectors.toList());
    } catch (Exception e) {
      log.debug("getAllSelectedOptions failed for {}", locator, e);
      return List.of();
    }
  }

  /**
   * Returns the first selected option for a select element.
   *
   * @param locator element locator string
   * @return first selected Option or null if none selected
   */
  @Override
  public Options getFirstSelectedOption(String locator) {
    List<Options> opts = getAllSelectedOptions(locator);
    return opts.isEmpty() ? null : opts.getFirst();
  }

  /**
   * Returns all available options from a select element.
   *
   * @param locator element locator string
   * @return list of available Options
   */
  @Override
  public List<Options> getOptions(String locator) {
    WebElement el = findElementSafe(locator);
    if (el == null) return List.of();
    try {
      Select select = new Select(el);
      return select.getOptions().stream().map(o -> new Options(o.getText(), o.getAttribute("value"))).collect(Collectors.toList());
    } catch (Exception e) {
      log.debug("getOptions failed for {}", locator, e);
      return List.of();
    }
  }

  /**
   * Selects an option by its visible text.
   *
   * @param locator element locator string
   * @param text    visible text to select
   */
  @Override
  public boolean selectByVisibleText(String locator, String text) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Select(el).selectByVisibleText(text);
      return true;
    } catch (Exception e) {
      log.debug("selectByVisibleText failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Selects an option by its value attribute.
   *
   * @param locator element locator string
   * @param value   the value attribute to select
   */
  @Override
  public boolean selectByValue(String locator, String value) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Select(el).selectByValue(value);
      return true;
    } catch (Exception e) {
      log.debug("selectByValue failed for {}", locator, e);
      return false;
    }
  }

  /**
   * Selects an option by its index.
   *
   * @param locator element locator string
   * @param index   0-based index to select
   */
  @Override
  public boolean selectByIndex(String locator, int index) {
    WebElement el = findElementSafe(locator);
    if (el == null) return false;
    try {
      new Select(el).selectByIndex(index);
      return true;
    } catch (Exception e) {
      log.debug("selectByIndex failed for {}", locator, e);
      return false;
    }
  }

}

package org.catools.web.drivers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.utils.CSleeper;
import org.catools.web.controls.CElementEngine;
import org.catools.web.enums.CKeys;

import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.catools.web.drivers.CDriverWaiter.DEFAULT_TIMEOUT;

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
public class CPlaywrightElementEngine implements CElementEngine<Locator> {

  private Page page;

  /**
   * Creates a new CPlayWrightEngine instance with the specified Page.
   *
   * @param page the Playwright Page instance
   */
  public CPlaywrightElementEngine(Page page) {
    this.page = page;
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param keysToSend keys to send
   */
  @Override
  public boolean press(String locator, CKeys... keysToSend) {
    return press(locator, 0, keysToSend);
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param intervalInMilliSeconds the delay in milliseconds between key presses
   * @param keysToSend keys to send
   */
  @Override
  public boolean press(String locator, long intervalInMilliSeconds, CKeys... keysToSend) {
    if (isClosed() || keysToSend == null || keysToSend.length == 0) return false;
    if (!focus(locator)) return false;

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
   * Retrieves the location of an element specified by the locator.
   *
   * @param locator element locator
   * @return Point containing x and y coordinates of the element, or null if element not found
   */
  @Override
  public Point getLocation(String locator) {
    if (!isClosed()) {
      try {
        return performActionOnContext(
            locator,
            loc -> {
              BoundingBox box = loc.boundingBox();
              if (box != null) {
                return new Point((int) box.x, (int) box.y);
              }
              return null;
            });
      } catch (Exception e) {
        log.debug("Failed to get location for element: {}", locator, e);
      }
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.focus();
              return true;
            });
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.click();
              return true;
            });
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.click();
              return true;
            });
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.dblclick();
              return true;
            });
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.scrollIntoViewIfNeeded();
              return true;
            });
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
   * @param locator element locator
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
   * @param locator element locator
   * @param keysToSend keys to send
   * @param intervalInMilliSeconds the delay in milliseconds between key presses
   */
  @Override
  public boolean sendKeys(String locator, String keysToSend, long intervalInMilliSeconds) {
    if (!isClosed()) {
      try {
        return performActionOnContext(
            locator,
            loc -> {
              loc.focus();
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
            });
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.dragTo(loc, new Locator.DragToOptions().setTargetPosition(xOffset, yOffset));
              return true;
            });
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.hover(new Locator.HoverOptions().setPosition(xOffset, yOffset));
              return true;
            });
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
   * @param source source element locator
   * @param target target element locator
   * @param xOffset1 horizontal offset for source
   * @param yOffset1 vertical offset for source
   */
  @Override
  public boolean dragAndDropTo(String source, String target, int xOffset1, int yOffset1) {
    if (!isClosed()) {
      try {
        return performActionOnContext(
            source,
            sourceElement -> {
              return performActionOnContext(
                  target,
                  targetElement -> {
                    sourceElement.dragTo(
                        targetElement,
                        new Locator.DragToOptions().setSourcePosition(xOffset1, yOffset1));
                    return true;
                  });
            });
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
   * @param locator element locator
   * @param xOffset1 first horizontal offset
   * @param yOffset1 first vertical offset
   * @param xOffset2 second horizontal offset
   * @param yOffset2 second vertical offset
   */
  @Override
  public boolean dragAndDropTo(
      String locator, int xOffset1, int yOffset1, int xOffset2, int yOffset2) {
    if (!isClosed()) {
      try {
        return performActionOnContext(
            locator,
            loc -> {
              loc.dragTo(
                  loc,
                  new Locator.DragToOptions()
                      .setSourcePosition(xOffset1, yOffset1)
                      .setTargetPosition(xOffset2, yOffset2));
              return true;
            });
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
   * @param script script to execute
   * @return script execution result
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T executeScript(String locator, String script) {
    if (!isClosed()) {
      return performActionOnContext(locator, loc -> (T) loc.evaluate(script));
    }
    return null;
  }

  /**
   * Execute an asynchronous script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script script to execute
   * @param args optional script arguments
   * @return script execution result
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T executeAsyncScript(String locator, String script, Object... args) {
    if (!isClosed()) {
      return performActionOnContext(
          locator,
          loc -> {
            Object arg = args.length > 0 ? args[0] : null;
            return (T) loc.evaluate(script, arg);
          });
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
      return performActionOnContext(locator, loc -> loc.count() > 0);
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
        return performActionOnContext(locator, Locator::isVisible);
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
        return performActionOnContext(
            locator, loc -> loc.isEnabled() && loc.isVisible() && loc.isEditable());
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
        return performActionOnContext(locator, Locator::isChecked);
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
        return performActionOnContext(locator, Locator::textContent);
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
    return performActionOnContext(locator, Locator::inputValue);
  }

  /**
   * Gets the inner HTML of the element matching the locator.
   *
   * @param locator element locator string
   * @return element inner HTML
   */
  @Override
  public String getElementInnerHtml(String locator) {
    return performActionOnContext(locator, Locator::innerHTML);
  }

  /**
   * Gets an attribute value of the element matching the locator.
   *
   * @param locator element locator string
   * @param attributeName the name of the attribute
   * @return attribute value or null if not found
   */
  @Override
  public String getElementAttribute(String locator, String attributeName) {
    if (!isClosed()) {
      try {
        return performActionOnContext(locator, loc -> loc.getAttribute(attributeName));
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
   * @param locator element locator string
   * @param propertyName the name of the CSS property
   * @return CSS property value or null if not found
   */
  @Override
  public String getElementCssValue(String locator, String propertyName) {
    if (!isClosed()) {
      try {
        String script =
            String.format(
                "element => window.getComputedStyle(element).getPropertyValue('%s')", propertyName);
        return executeScript(locator, script);
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
      return performActionOnContext(locator, Locator::count);
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
        return performActionOnContext(
            locator,
            loc -> {
              loc.clear();
              return true;
            });
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
   * @param text text to type
   */
  @Override
  public boolean setText(String locator, String text) {
    if (!isClosed()) {
      try {
        return performActionOnContext(
            locator,
            loc -> {
              loc.fill(text);
              return true;
            });
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
   * @param text key to press
   */
  @Override
  public boolean press(String locator, String text) {
    if (!isClosed()) {
      try {
        return performActionOnContext(
            locator,
            loc -> {
              loc.press(text);
              return true;
            });
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
        return performActionOnContext(locator, Locator::screenshot);
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
  @SuppressWarnings("unchecked")
  public List<Options> getAllSelectedOptions(String locator) {
    if (!isClosed()) {
      try {
        String script =
            "element => Array.from(element.selectedOptions).map(opt => ({text: opt.text, value: opt.value}))";
        List<Object> result = executeScript(locator, script);
        return result.stream()
            .map(
                obj -> {
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
        String script =
            "element => Array.from(element.options).map(opt => ({text: opt.text, value: opt.value}))";
        List<Object> result = executeScript(locator, script);
        return result.stream()
            .map(
                obj -> {
                  @SuppressWarnings("unchecked")
                  java.util.Map<String, String> map = (java.util.Map<String, String>) obj;
                  return new Options(map.get("text"), map.get("value"));
                })
            .toList();
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
   * @param text visible text to select
   */
  @Override
  public boolean selectByVisibleText(String locator, String text) {
    if (!isClosed()) {
      try {
        // Playwright doesn't have a direct selectByLabel, we need to find the option by text
        String script =
            String.format(
                "element => { const option = Array.from(element.options).find(opt => opt.text === '%s'); if (option) element.value = option.value; }",
                text.replace("'", "\\'"));
        executeScript(locator, script);
      } catch (Exception e) {
        log.debug("Failed to select by visible text", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Selects an option by matching its visible text against a pattern.
   *
   * @param locator element locator string
   * @param pattern regex pattern to match visible text
   */
  @Override
  public boolean selectByTextPattern(String locator, String pattern) {
    if (!isClosed()) {
      try {
        String script =
            String.format(
                "element => { const regex = new RegExp('%s'); const option = Array.from(element.options).find(opt => regex.test(opt.text)); if (option) element.value = option.value; }",
                pattern.replace("'", "\\'"));
        executeScript(locator, script);
      } catch (Exception e) {
        log.debug("Failed to select by text pattern", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Selects an option by its value attribute.
   *
   * @param locator element locator string
   * @param value the value attribute to select
   */
  @Override
  public boolean selectByValue(String locator, String value) {
    if (!isClosed()) {
      try {
        return performActionOnContext(
            locator,
            loc -> {
              loc.selectOption(value);
              return true;
            });
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
   * @param index 0-based index to select
   */
  @Override
  public boolean selectByIndex(String locator, int index) {
    if (!isClosed()) {
      try {
        List<Options> options = getOptions(locator);
        if (index >= 0 && index < options.size()) {
          String value = options.get(index).value();
          return performActionOnContext(
              locator,
              loc -> {
                loc.selectOption(value);
                return true;
              });
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

  @Override
  public <T> T performActionOnContext(String locator, Function<Locator, T> action) {
    if (isClosed()) return null;
    try {
      return action.apply(getContext(locator));
    } catch (Exception e) {
      return null;
    }
  }

  public Locator getContext(String locator) {
    return page.locator(locator);
  }

  public boolean isClosed() {
    if (page == null || page.context() == null || page.isClosed()) return true;
    try {
      // try a lightweight call
      page.url();
      return false;
    } catch (Exception e) {
      return true;
    }
  }
}

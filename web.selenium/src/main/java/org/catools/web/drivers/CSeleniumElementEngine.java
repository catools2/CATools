package org.catools.web.drivers;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.controls.CElementEngine;
import org.catools.web.enums.CKeys;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.awt.Point;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
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
public class CSeleniumElementEngine implements CElementEngine<WebElement> {

  private final WebDriver driver;

  public CSeleniumElementEngine(WebDriver driver) {
    this.driver = driver;
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
   * @param intervalInMilliSeconds
   * @param keysToSend keys to send
   */
  @Override
  public boolean press(String locator, long intervalInMilliSeconds, CKeys... keysToSend) {
    return performActionOnContext(
                locator,
                el -> {
                  try {
                    for (CKeys c : keysToSend) {
                      Keys seleniumKey = Keys.getKeyFromUnicode(c.toSeleniumKey());
                      Objects.requireNonNull(
                          seleniumKey, "Cannot convert CKey to Selenium Key " + c);
                      el.sendKeys(seleniumKey);
                      try {
                        Thread.sleep(intervalInMilliSeconds);
                      } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                      }
                    }
                    return true;
                  } catch (Exception e) {
                    log.debug("sendKeys failed", e);
                    return false;
                  }
                })
            != null
        && performActionOnContext(locator, el -> true);
  }

  /**
   * Retrieves the location of an element specified by the locator.
   *
   * @param locator element locator
   * @return Point containing x and y coordinates of the element, or null if element not found
   */
  @Override
  public Point getLocation(String locator) {
    return performActionOnContext(
        locator,
        el -> {
          try {
            org.openqa.selenium.Point seleniumPoint = el.getLocation();
            return new Point(seleniumPoint.getX(), seleniumPoint.getY());
          } catch (Exception e) {
            log.debug("getLocation failed for {}", locator, e);
            return null;
          }
        });
  }

  /**
   * Move focus to the element identified by the locator.
   *
   * @param locator element selector or identifier
   */
  @Override
  public boolean focus(String locator) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Actions(driver).moveToElement(el).perform();
                return true;
              } catch (Exception e) {
                log.debug("focus failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Click the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean click(String locator) {
    return performActionOnContext(
            locator,
            el -> {
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
            })
        == Boolean.TRUE;
  }

  /**
   * Perform a mouse click on the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean mouseClick(String locator) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Actions(driver).click(el).perform();
                return true;
              } catch (Exception e) {
                log.debug("mouseClick failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Perform a mouse double-click on the element located by the locator.
   *
   * @param locator element locator
   */
  @Override
  public boolean mouseDoubleClick(String locator) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Actions(driver).doubleClick(el).perform();
                return true;
              } catch (Exception e) {
                log.debug("mouseDoubleClick failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Scroll the element into view.
   *
   * @param locator element locator
   */
  @Override
  public boolean scrollIntoView(String locator) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", el);
                return true;
              } catch (Exception e) {
                log.debug("scrollIntoView failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param keysToSend keys to send
   */
  @Override
  public boolean sendKeys(String locator, String keysToSend) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                el.clear();
                el.sendKeys(keysToSend);
                return true;
              } catch (Exception e) {
                log.debug("sendKeys to locator failed {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Send keys to the element identified by locator.
   *
   * @param locator element locator
   * @param keysToSend keys to send
   * @param intervalInMilliSeconds interval between keystrokes
   */
  @Override
  public boolean sendKeys(String locator, String keysToSend, long intervalInMilliSeconds) {
    return performActionOnContext(
            locator,
            el -> {
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
            })
        == Boolean.TRUE;
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
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Actions(driver).dragAndDropBy(el, xOffset, yOffset).perform();
                return true;
              } catch (Exception e) {
                log.debug("dropTo failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
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
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Actions(driver).moveToElement(el, xOffset, yOffset).perform();
                return true;
              } catch (Exception e) {
                log.debug("moveToElement failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
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
    WebElement src = performActionOnContext(source, el -> el);
    WebElement tgt = performActionOnContext(target, el -> el);
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
   * @param locator element locator
   * @param xOffset1 first horizontal offset
   * @param yOffset1 first vertical offset
   * @param xOffset2 second horizontal offset
   * @param yOffset2 second vertical offset
   */
  @Override
  public boolean dragAndDropTo(
      String locator, int xOffset1, int yOffset1, int xOffset2, int yOffset2) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Actions(driver)
                    .dragAndDropBy(el, xOffset2 - xOffset1, yOffset2 - yOffset1)
                    .perform();
                return true;
              } catch (Exception e) {
                log.debug("dragAndDropTo with offsets failed", e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Execute a script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script script to execute
   * @return script execution result
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T executeScript(String locator, String script) {
    return performActionOnContext(
        locator,
        el -> {
          try {
            if (driver instanceof JavascriptExecutor) {
              return (T) ((JavascriptExecutor) driver).executeScript(script, el);
            }
          } catch (Exception e) {
            log.debug("executeScript on element failed", e);
          }
          return null;
        });
  }

  /**
   * Execute an asynchronous script in the context of an element specified by locator.
   *
   * @param locator element locator
   * @param script script to execute
   * @param args optional script arguments
   * @return script execution result
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T executeAsyncScript(String locator, String script, Object... args) {
    return performActionOnContext(
        locator,
        el -> {
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
        });
  }

  /**
   * Checks if an element matching the locator is present in the DOM.
   *
   * @param locator element locator string
   * @return true if element is present, false otherwise
   */
  @Override
  public boolean isElementPresent(String locator) {
    return !findElements(locator).isEmpty();
  }

  /**
   * Checks if an element matching the locator is visible to the user.
   *
   * <p>This method performs comprehensive visibility checks including:
   *
   * <ul>
   *   <li>Element exists in DOM
   *   <li>Element is displayed (not display:none or visibility:hidden)
   *   <li>Element has non-zero opacity
   *   <li>Element has non-zero dimensions (width and height)
   * </ul>
   *
   * @param locator element locator string
   * @return true if element is visible to the user, false otherwise
   */
  @Override
  public boolean isElementVisible(String locator) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                // If display and visible by CSS
                if (!el.isDisplayed()) {
                  return false;
                }

                // If Element have non-zero dimensions
                Dimension size = el.getSize();
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
                Boolean isHidden =
                    (Boolean)
                        ((JavascriptExecutor) driver)
                            .executeScript(
                                "var el = arguments[0];"
                                    + "var style = window.getComputedStyle(el);"
                                    + "if (style.display === 'none' || style.visibility === 'hidden') return true;"
                                    + "if (parseFloat(style.opacity) === 0) return true;"
                                    + "if (el.offsetWidth === 0 || el.offsetHeight === 0) return true;"
                                    + "if (el.hasAttribute('hidden')) return true;"
                                    + "if (el.getAttribute('aria-hidden') === 'true') return true;"
                                    + "return false;",
                                el);

                return !Boolean.TRUE.equals(isHidden);

              } catch (Exception e) {
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Checks if an element matching the locator is enabled.
   *
   * @param locator element locator string
   * @return true if element is enabled, false otherwise
   */
  @Override
  public boolean isElementEnabled(String locator) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                return el.isEnabled() && el.isDisplayed();
              } catch (Exception e) {
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Checks if an element matching the locator is selected/checked.
   *
   * @param locator element locator string
   * @return true if element is selected, false otherwise
   */
  @Override
  public boolean isElementSelected(String locator) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                return el.isSelected();
              } catch (Exception e) {
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Gets the text content of the element matching the locator.
   *
   * @param locator element locator string
   * @return element text content
   */
  @Override
  public String getElementText(String locator) {
    String result =
        performActionOnContext(
            locator,
            el -> {
              try {
                return el.getText();
              } catch (Exception e) {
                log.debug("getElementText failed for {}", locator, e);
                return "";
              }
            });
    return result != null ? result : "";
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
   * @param locator element locator string
   * @param attributeName the name of the attribute
   * @return attribute value or null if not found
   */
  @Override
  public String getElementAttribute(String locator, String attributeName) {
    String result =
        performActionOnContext(
            locator,
            el -> {
              try {
                return el.getAttribute(attributeName);
              } catch (Exception e) {
                log.debug("getElementAttribute failed for {}", locator, e);
                return "";
              }
            });
    return result != null ? result : "";
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
    String result =
        performActionOnContext(
            locator,
            el -> {
              try {
                return el.getCssValue(propertyName);
              } catch (Exception e) {
                log.debug("getElementCssValue failed for {}", locator, e);
                return "";
              }
            });
    return result != null ? result : "";
  }

  /**
   * Gets the count of elements matching the locator.
   *
   * @param locator element locator string
   * @return number of matching elements
   */
  @Override
  public int getElementCount(String locator) {
    return findElements(locator).size();
  }

  /**
   * Clears the content of the element matching the locator. Uses multiple strategies to ensure the
   * element is properly cleared:
   *
   * @param locator element locator string
   */
  @Override
  public boolean clearElement(String locator) {
    return performActionOnContext(
            locator,
            el -> {
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
                ((JavascriptExecutor) driver)
                    .executeScript(
                        "arguments[0].value = '';"
                            + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                            + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                        el);
                return true;
              } catch (Exception e) {
                log.debug("clearElement failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Fills the element with text (types text character by character). Optimized for performance
   * while maintaining framework compatibility.
   *
   * @param locator element locator string
   * @param text text to type
   */
  @Override
  public boolean setText(String locator, String text) {
    return performActionOnContext(
            locator,
            el -> {
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
                  ((JavascriptExecutor) driver)
                      .executeScript(
                          "arguments[0].focus();"
                              + "arguments[0].value = arguments[1];"
                              + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                              + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                          el,
                          text);
                }
                return true;
              } catch (Exception e) {
                log.debug("setText failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Press special key press to the element.
   *
   * @param locator element locator string
   * @param text key to press
   */
  @Override
  public boolean press(String locator, String text) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                el.sendKeys(Keys.valueOf(text.toUpperCase()));
                return true;
              } catch (Exception e) {
                log.debug("press failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Takes a screenshot of the element matching the locator.
   *
   * @param locator element locator string
   * @return byte array representing the screenshot (PNG)
   */
  @Override
  public byte[] screenshotElement(String locator) {
    byte[] result =
        performActionOnContext(
            locator,
            el -> {
              try {
                // WebElement.getScreenshotAs is available in Selenium 4
                return el.getScreenshotAs(OutputType.BYTES);
              } catch (Exception e) {
                log.warn("screenshotElement failed for {}", locator, e);
                return new byte[0];
              }
            });
    return result != null ? result : new byte[0];
  }

  /**
   * Returns all selected options for a select element.
   *
   * @param locator element locator string
   * @return list of selected Options
   */
  @Override
  public List<Options> getAllSelectedOptions(String locator) {
    List<Options> result =
        performActionOnContext(
            locator,
            el -> {
              try {
                Select select = new Select(el);
                List<WebElement> selected = select.getAllSelectedOptions();
                return selected.stream()
                    .map(o -> new Options(o.getText(), o.getAttribute("value")))
                    .collect(Collectors.toList());
              } catch (Exception e) {
                log.debug("getAllSelectedOptions failed for {}", locator, e);
                return List.<Options>of();
              }
            });
    return result != null ? result : List.of();
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
    List<Options> result =
        performActionOnContext(
            locator,
            el -> {
              try {
                Select select = new Select(el);
                return select.getOptions().stream()
                    .map(o -> new Options(o.getText(), o.getAttribute("value")))
                    .collect(Collectors.toList());
              } catch (Exception e) {
                log.debug("getOptions failed for {}", locator, e);
                return List.<Options>of();
              }
            });
    return result != null ? result : List.of();
  }

  /**
   * Selects an option by its visible text.
   *
   * @param locator element locator string
   * @param text visible text to select
   */
  @Override
  public boolean selectByVisibleText(String locator, String text) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Select(el).selectByVisibleText(text);
                return true;
              } catch (Exception e) {
                log.debug("selectByVisibleText failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Selects an option by matching its visible text against a pattern.
   *
   * @param locator element locator string
   * @param pattern regex pattern to match visible text
   */
  @Override
  public boolean selectByTextPattern(String locator, String pattern) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                Select select = new Select(el);
                for (WebElement option : select.getOptions()) {
                  String text = option.getText();
                  if (Pattern.matches(pattern, text)) {
                    select.selectByVisibleText(text);
                    return true;
                  }
                }
                return false;
              } catch (Exception e) {
                log.debug("selectByTextPattern failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Selects an option by its value attribute.
   *
   * @param locator element locator string
   * @param value the value attribute to select
   */
  @Override
  public boolean selectByValue(String locator, String value) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Select(el).selectByValue(value);
                return true;
              } catch (Exception e) {
                log.debug("selectByValue failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  /**
   * Selects an option by its index.
   *
   * @param locator element locator string
   * @param index 0-based index to select
   */
  @Override
  public boolean selectByIndex(String locator, int index) {
    return performActionOnContext(
            locator,
            el -> {
              try {
                new Select(el).selectByIndex(index);
                return true;
              } catch (Exception e) {
                log.debug("selectByIndex failed for {}", locator, e);
                return false;
              }
            })
        == Boolean.TRUE;
  }

  @Override
  public <T> T performActionOnContext(String locator, Function<WebElement, T> action) {
    if (isClosed()) return null;
    try {
      return action.apply(getContext(locator));
    } catch (Exception e) {
      return null;
    }
  }

  public WebElement getContext(String locator) {
    return driver.findElement(toBy(locator));
  }

  // Helper: resolve By from a locator string (basic heuristic)
  private By toBy(String locator) {
    if (locator == null) return By.cssSelector("");

    return locator.contains("//") ? By.xpath(locator) : By.cssSelector(locator);
  }

  private List<WebElement> findElements(String locator) {
    if (isClosed()) return List.of();
    try {
      return driver.findElements(toBy(locator));
    } catch (Exception e) {
      return List.of();
    }
  }

  private boolean isClosed() {
    // Fast path: check flag first
    if (driver == null) return true;

    // try a lightweight call
    try {
      driver.getWindowHandle();
      return false;
    } catch (Exception e) {
      return true;
    }
  }
}

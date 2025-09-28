package org.catools.web.controls;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.date.CDate;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.utils.CFileUtil;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CSleeper;
import org.catools.web.config.CBrowserConfigs;
import org.catools.web.config.CGridConfigs;
import org.catools.web.drivers.CDriver;
import org.catools.web.utils.CGridUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;

import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Interface providing action methods for web element interactions.
 * This interface extends CWebElementStates and provides comprehensive methods
 * for performing various actions on web elements such as clicking, typing,
 * moving, dragging, scrolling, and file operations.
 * 
 * @param <DR> the driver type extending CDriver
 * @author CA Tools Team
 */
public interface CWebElementActions<DR extends CDriver> extends CWebElementStates<DR> {

  // Actions
  
  /**
   * Moves the mouse cursor to the center of the element.
   * Uses default timeout for waiting until element is present.
   * 
   * @example
   * <pre>
   * // Move to a button element
   * button.moveTo();
   * </pre>
   */
  default void moveTo() {
    moveTo(0, 0);
  }

  /**
   * Moves the mouse cursor to the center of the element with specified wait time.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Move to element and wait up to 10 seconds for it to be present
   * element.moveTo(10);
   * </pre>
   */
  default void moveTo(int waitSec) {
    moveTo(0, 0, waitSec);
  }

  /**
   * Moves the mouse cursor to a specific offset from the element's center.
   * Uses default timeout for waiting until element is present.
   * 
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @example
   * <pre>
   * // Move 50 pixels right and 20 pixels down from element center
   * element.moveTo(50, 20);
   * </pre>
   */
  default void moveTo(int xOffset, int yOffset) {
    moveTo(xOffset, yOffset, getWaitSec());
  }

  /**
   * Moves the mouse cursor to a specific offset from the element's center with custom wait time.
   * 
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Move to offset position and wait up to 15 seconds
   * element.moveTo(30, -10, 15);
   * </pre>
   */
  default void moveTo(int xOffset, int yOffset, int waitSec) {
    isPresent(waitSec);
    getDriver().moveToElement(getLocator(), xOffset, yOffset, 0);
  }

  /**
   * Performs a drop action at a specific offset from the element's center.
   * Uses default timeout for waiting until element is present.
   * 
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @example
   * <pre>
   * // Drop at position 100 pixels right, 50 pixels down
   * draggableElement.dropTo(100, 50);
   * </pre>
   */
  default void dropTo(int xOffset, int yOffset) {
    dropTo(xOffset, yOffset, getWaitSec());
  }

  /**
   * Performs a drop action at a specific offset from the element's center with custom wait time.
   * 
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Drop at offset position and wait up to 8 seconds
   * draggableElement.dropTo(75, -25, 8);
   * </pre>
   */
  default void dropTo(int xOffset, int yOffset, int waitSec) {
    isPresent(waitSec);
    getDriver().dropTo(getLocator(), xOffset, yOffset, 0);
  }

  /**
   * Drags the element from its center to a target offset position.
   * Uses default timeout for waiting until element is present.
   * 
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @example
   * <pre>
   * // Drag element from center to position (200, 100)
   * slider.dragAndDropTo(200, 100);
   * </pre>
   */
  default void dragAndDropTo(int xOffset2, int yOffset2) {
    dragAndDropTo(0, 0, xOffset2, yOffset2);
  }

  /**
   * Drags the element from its center to a target offset position with custom wait time.
   * 
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Drag to target position and wait up to 12 seconds
   * draggableItem.dragAndDropTo(150, -50, 12);
   * </pre>
   */
  default void dragAndDropTo(int xOffset2, int yOffset2, int waitSec) {
    dragAndDropTo(0, 0, xOffset2, yOffset2, waitSec);
  }

  /**
   * Drags the element from a source offset to a target offset position.
   * Uses default timeout for waiting until element is present.
   * 
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @example
   * <pre>
   * // Drag from (10, 5) to (100, 75) relative to element center
   * resizableCorner.dragAndDropTo(10, 5, 100, 75);
   * </pre>
   */
  default void dragAndDropTo(int xOffset1, int yOffset1, int xOffset2, int yOffset2) {
    dragAndDropTo(xOffset1, yOffset1, xOffset2, yOffset2, getWaitSec());
  }

  /**
   * Drags the element from a source offset to a target offset position with custom wait time.
   * 
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Drag from source to target with 15 second timeout
   * dragHandle.dragAndDropTo(0, 0, 200, 150, 15);
   * </pre>
   */
  default void dragAndDropTo(int xOffset1, int yOffset1, int xOffset2, int yOffset2, int waitSec) {
    isPresent(waitSec);
    getDriver().dragAndDropTo(getLocator(), xOffset1, yOffset1, xOffset2, yOffset2, 0);
  }

  /**
   * Drags the element to another target element.
   * Uses default timeout for waiting until element is present.
   * 
   * @param target the By locator of the target element
   * @example
   * <pre>
   * // Drag item to a drop zone
   * draggableItem.dragAndDropTo(By.id("dropZone"));
   * </pre>
   */
  default void dragAndDropTo(By target) {
    dragAndDropTo(target, 0);
  }

  /**
   * Drags the element to another target element with custom wait time.
   * 
   * @param target the By locator of the target element
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Drag to target element with 20 second timeout
   * fileItem.dragAndDropTo(By.className("upload-area"), 20);
   * </pre>
   */
  default void dragAndDropTo(By target, int waitSec) {
    dragAndDropTo(target, 0, 0, waitSec);
  }

  /**
   * Drags the element from a specific offset to a target element.
   * Uses default timeout for waiting until element is present.
   * 
   * @param target the By locator of the target element
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @example
   * <pre>
   * // Drag from specific point on element to target
   * resizeHandle.dragAndDropTo(By.id("container"), 10, 10);
   * </pre>
   */
  default void dragAndDropTo(By target, int xOffset1, int yOffset1) {
    dragAndDropTo(target, xOffset1, yOffset1, DEFAULT_TIMEOUT);
  }

  /**
   * Drags the element from a specific offset to a target element with custom wait time.
   * 
   * @param target the By locator of the target element
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Drag from offset to target with timeout
   * menuItem.dragAndDropTo(By.className("menu-container"), 5, 0, 10);
   * </pre>
   */
  default void dragAndDropTo(By target, int xOffset1, int yOffset1, int waitSec) {
    isPresent(waitSec);
    getDriver().dragAndDropTo(getLocator(), target, xOffset1, yOffset1, 0);
  }

  /**
   * Executes JavaScript code on the element using default timeout.
   * 
   * @param <R> the return type of the script execution
   * @param script the JavaScript code to execute
   * @return the result of script execution
   * @example
   * <pre>
   * // Get element's inner HTML
   * String innerHTML = element.executeScript("return arguments[0].innerHTML;");
   * 
   * // Change element's background color
   * element.executeScript("arguments[0].style.backgroundColor = 'red';");
   * </pre>
   */
  default <R> R executeScript(String script) {
    return executeScript(script, getWaitSec());
  }

  /**
   * Executes JavaScript code on the element with custom timeout.
   * 
   * @param <R> the return type of the script execution
   * @param script the JavaScript code to execute
   * @param waitSec maximum time in seconds to wait for element to be present
   * @return the result of script execution
   * @example
   * <pre>
   * // Scroll element into view with custom timeout
   * element.executeScript("arguments[0].scrollIntoView(true);", 15);
   * 
   * // Get computed style property
   * String color = element.executeScript("return getComputedStyle(arguments[0]).color;", 5);
   * </pre>
   */
  default <R> R executeScript(String script, int waitSec) {
    return getDriver().executeScript(getLocator(), waitSec, script);
  }

  /**
   * Gets the location (coordinates) of the element on the page.
   * 
   * @return Point object containing x and y coordinates
   * @example
   * <pre>
   * // Get element position
   * Point location = button.getLocation();
   * System.out.println("Element is at: " + location.x + ", " + location.y);
   * </pre>
   */
  default Point getLocation() {
    org.openqa.selenium.Point p = waitUntil("Get Position", getWaitSec(), WebElement::getLocation);
    return new Point(p.x, p.y);
  }

  /**
   * Sends keys to the element (types text or special keys).
   * 
   * @param keys the keys to send (text or special keys like Keys.ENTER)
   * @example
   * <pre>
   * // Type text in input field
   * inputField.sendKeys("Hello World");
   * 
   * // Send special keys
   * inputField.sendKeys(Keys.CTRL, "a", Keys.DELETE);
   * 
   * // Send combination
   * inputField.sendKeys("test@example.com", Keys.TAB);
   * </pre>
   */
  default void sendKeys(CharSequence... keys) {
    getDriver().sendKeys(getLocator(), getWaitSec(), keys);
  }

  /**
   * Performs a mouse click on the element using default timeout.
   * This method first moves to the element, then performs the click.
   * 
   * @example
   * <pre>
   * // Click on a button
   * submitButton.mouseClick();
   * </pre>
   */
  default void mouseClick() {
    mouseClick(getWaitSec());
  }

  /**
   * Performs a mouse click on the element with custom timeout.
   * This method first moves to the element, then performs the click.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Click with custom timeout
   * dynamicButton.mouseClick(20);
   * </pre>
   */
  default void mouseClick(int waitSec) {
    moveTo(waitSec);
    getDriver().mouseClick(getLocator(), waitSec);
  }

  /**
   * Performs a mouse double-click on the element using default timeout.
   * This method first moves to the element, then performs the double-click.
   * 
   * @example
   * <pre>
   * // Double-click on a file icon
   * fileIcon.mouseDoubleClick();
   * </pre>
   */
  default void mouseDoubleClick() {
    mouseDoubleClick(getWaitSec());
  }

  /**
   * Performs a mouse double-click on the element with custom timeout.
   * This method first moves to the element, then performs the double-click.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Double-click with custom timeout
   * editableCell.mouseDoubleClick(15);
   * </pre>
   */
  default void mouseDoubleClick(int waitSec) {
    moveTo(waitSec);
    getDriver().mouseDoubleClick(getLocator(), waitSec);
  }

  /**
   * Scrolls the element into view within the browser window.
   * 
   * @param scrollDown true to scroll down to bring element into view, false to scroll up
   * @example
   * <pre>
   * // Scroll down to bring footer element into view
   * footerElement.scrollIntoView(true);
   * 
   * // Scroll up to bring header element into view
   * headerElement.scrollIntoView(false);
   * </pre>
   */
  default void scrollIntoView(boolean scrollDown) {
    scrollIntoView(scrollDown, getWaitSec());
  }

  /**
   * Scrolls the element into view within the browser window with custom timeout.
   * 
   * @param scrollDown true to scroll down to bring element into view, false to scroll up
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Scroll with custom timeout
   * lazyLoadedElement.scrollIntoView(true, 30);
   * </pre>
   */
  default void scrollIntoView(boolean scrollDown, int waitSec) {
    getDriver().scrollIntoView(getLocator(), scrollDown, waitSec);
  }

  /**
   * Scrolls the element horizontally to the left by default amount (900 pixels).
   * 
   * @example
   * <pre>
   * // Scroll a horizontal scrollable container to the left
   * horizontalScrollContainer.scrollLeft();
   * </pre>
   */
  default void scrollLeft() {
    scrollLeft(900, getWaitSec());
  }

  /**
   * Scrolls the element horizontally to the left by specified amount.
   * 
   * @param scrollSize amount in pixels to scroll left
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Scroll left by 500 pixels with 10 second timeout
   * carousel.scrollLeft(500, 10);
   * </pre>
   */
  default void scrollLeft(int scrollSize, int waitSec) {
    getDriver().scrollLeft(getLocator(), scrollSize, waitSec);
  }

  /**
   * Scrolls the element horizontally to the right by default amount (900 pixels).
   * 
   * @example
   * <pre>
   * // Scroll a horizontal scrollable container to the right
   * horizontalScrollContainer.scrollRight();
   * </pre>
   */
  default void scrollRight() {
    scrollRight(900, getWaitSec());
  }

  /**
   * Scrolls the element horizontally to the right by specified amount.
   * 
   * @param scrollSize amount in pixels to scroll right
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Scroll right by 300 pixels with 8 second timeout
   * imageGallery.scrollRight(300, 8);
   * </pre>
   */
  default void scrollRight(int scrollSize, int waitSec) {
    getDriver().scrollRight(getLocator(), scrollSize, waitSec);
  }

  /**
   * Sets a CSS style property on the element using default timeout.
   * 
   * @param style the CSS style property name
   * @param color the value to set for the style property
   * @example
   * <pre>
   * // Change background color
   * element.setStyle("backgroundColor", "red");
   * 
   * // Change border
   * element.setStyle("border", "2px solid blue");
   * </pre>
   */
  default void setStyle(String style, String color) {
    setStyle(style, color, getWaitSec());
  }

  /**
   * Sets a CSS style property on the element with custom timeout.
   * 
   * @param style the CSS style property name
   * @param color the value to set for the style property
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Change display property with custom timeout
   * hiddenElement.setStyle("display", "block", 15);
   * </pre>
   */
  default void setStyle(String style, String color, int waitSec) {
    isPresent(waitSec);
    executeScript(String.format("arguments[0][%s][%s]=%s;", Quotes.escape("style"), Quotes.escape(style), Quotes.escape(color)));
  }

  /**
   * Sets an attribute value on the element using JavaScript using default timeout.
   * 
   * @param attributeName the name of the attribute to set
   * @param value the value to set for the attribute
   * @example
   * <pre>
   * // Set custom data attribute
   * element.setAttribute("data-test-id", "submit-button");
   * 
   * // Change input type
   * inputElement.setAttribute("type", "password");
   * </pre>
   */
  default void setAttribute(String attributeName, String value) {
    setAttribute(attributeName, value, getWaitSec());
  }

  /**
   * Sets an attribute value on the element using JavaScript with custom timeout.
   * 
   * @param attributeName the name of the attribute to set
   * @param value the value to set for the attribute
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Set disabled attribute with timeout
   * dynamicButton.setAttribute("disabled", "true", 10);
   * </pre>
   */
  default void setAttribute(String attributeName, String value, int waitSec) {
    isPresent(waitSec);
    executeScript(String.format("arguments[0][%s]=%s;", Quotes.escape(attributeName), Quotes.escape(value)));
  }

  /**
   * Removes an attribute from the element using JavaScript and default timeout.
   * 
   * @param attributeName the name of the attribute to remove
   * @example
   * <pre>
   * // Remove disabled attribute to enable button
   * submitButton.removeAttribute("disabled");
   * 
   * // Remove custom data attribute
   * element.removeAttribute("data-temp");
   * </pre>
   */
  default void removeAttribute(String attributeName) {
    removeAttribute(attributeName, getWaitSec());
  }

  /**
   * Removes an attribute from the element using JavaScript with custom timeout.
   * 
   * @param attributeName the name of the attribute to remove
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Remove readonly attribute with timeout
   * inputField.removeAttribute("readonly", 12);
   * </pre>
   */
  default void removeAttribute(String attributeName, int waitSec) {
    isPresent(waitSec);
    executeScript(String.format("arguments[0].removeAttribute(%s);", Quotes.escape(attributeName)));
  }

  /**
   * Sets the selected state of the element (for checkboxes, radio buttons).
   * 
   * @param value true to select the element, false to deselect
   * @example
   * <pre>
   * // Select a checkbox
   * checkbox.set(true);
   * 
   * // Deselect a radio button
   * radioButton.set(false);
   * </pre>
   */
  default void set(boolean value) {
    set(value, DEFAULT_TIMEOUT);
  }

  /**
   * Sets the selected state of the element with custom timeout.
   * 
   * @param value true to select the element, false to deselect
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Select checkbox with custom timeout
   * dynamicCheckbox.set(true, 20);
   * </pre>
   */
  default void set(boolean value, int waitSec) {
    if (value)
      select(waitSec);
    else
      deselect(waitSec);
  }

  /**
   * Selects the element (for checkboxes, radio buttons) if not already selected.
   * Uses default timeout.
   * 
   * @example
   * <pre>
   * // Select a checkbox (only clicks if not already selected)
   * agreeCheckbox.select();
   * 
   * // Select a radio option
   * genderMaleRadio.select();
   * </pre>
   */
  default void select() {
    select(getWaitSec());
  }

  /**
   * Selects the element (for checkboxes, radio buttons) if not already selected.
   * Uses custom timeout.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Select with custom timeout
   * termsCheckbox.select(15);
   * </pre>
   */
  default void select(int waitSec) {
    waitUntil("Select", waitSec, webElement -> {
      if (!webElement.isSelected()) {
        _click(false, webElement);
      }
      return true;
    });
  }

  /**
   * Selects the element using JavaScript (for invisible elements) if not already selected.
   * Uses default timeout.
   * 
   * @example
   * <pre>
   * // Select a hidden checkbox
   * hiddenCheckbox.selectInvisible();
   * </pre>
   */
  default void selectInvisible() {
    selectInvisible(getWaitSec());
  }

  /**
   * Selects the element using JavaScript (for invisible elements) if not already selected.
   * Uses custom timeout.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Select invisible element with timeout
   * hiddenRadioButton.selectInvisible(10);
   * </pre>
   */
  default void selectInvisible(int waitSec) {
    waitUntil("Select", waitSec, webElement -> {
      if (!webElement.isSelected()) {
        _click(true, webElement);
      }
      return true;
    });
  }

  /**
   * Deselects the element (for checkboxes) if currently selected.
   * Uses default timeout.
   * 
   * @example
   * <pre>
   * // Deselect a checkbox (only clicks if currently selected)
   * newsletterCheckbox.deselect();
   * </pre>
   */
  default void deselect() {
    deselect(getWaitSec());
  }

  /**
   * Deselects the element (for checkboxes) if currently selected.
   * Uses custom timeout.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Deselect with custom timeout
   * optionalCheckbox.deselect(8);
   * </pre>
   */
  default void deselect(int waitSec) {
    waitUntil("Deselect", waitSec, webElement -> {
      if (webElement.isSelected()) {
        _click(false, webElement);
      }
      return true;
    });
  }

  /**
   * Deselects the element using JavaScript (for invisible elements) if currently selected.
   * Uses default timeout.
   * 
   * @example
   * <pre>
   * // Deselect a hidden checkbox
   * hiddenOptionCheckbox.deselectInvisible();
   * </pre>
   */
  default void deselectInvisible() {
    deselectInvisible(getWaitSec());
  }

  /**
   * Deselects the element using JavaScript (for invisible elements) if currently selected.
   * Uses custom timeout.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Deselect invisible element with timeout
   * hiddenFeatureCheckbox.deselectInvisible(12);
   * </pre>
   */
  default void deselectInvisible(int waitSec) {
    waitUntil("Deselect", waitSec, webElement -> {
      if (webElement.isSelected()) {
        _click(true, webElement);
      }
      return true;
    });
  }

  /**
   * Clicks on the element using default timeout.
   * Falls back to JavaScript click if normal click fails.
   * 
   * @example
   * <pre>
   * // Click a button
   * submitButton.click();
   * 
   * // Click a link
   * menuLink.click();
   * </pre>
   */
  default void click() {
    click(getWaitSec());
  }

  /**
   * Clicks on the element with custom timeout.
   * Falls back to JavaScript click if normal click fails.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Click with custom timeout
   * dynamicButton.click(20);
   * </pre>
   */
  default void click(int waitSec) {
    waitUntil("Click", waitSec, webElement -> {
      _click(false, webElement);
      return true;
    });
  }

  /**
   * Clicks on the element and waits for a post-condition to be satisfied.
   * Retries the click if the post-condition is not met.
   * 
   * @param <R> the return type of the post-condition function
   * @param postCondition function to verify after clicking
   * @return the result of the post-condition function
   * @example
   * <pre>
   * // Click and wait for page to load
   * String newUrl = nextPageButton.click(driver -> driver.getCurrentUrl());
   * 
   * // Click and wait for element to appear
   * Boolean elementVisible = submitButton.click(driver -> 
   *     driver.$(By.id("success-message")).isDisplayed());
   * </pre>
   */
  default <R> R click(com.google.common.base.Function<DR, R> postCondition) {
    return click(2, 2000, postCondition);
  }

  /**
   * Clicks on the element and waits for a post-condition with custom retry settings.
   * 
   * @param <R> the return type of the post-condition function
   * @param retryCount number of times to retry the click if post-condition fails
   * @param interval time in milliseconds between retries
   * @param postCondition function to verify after clicking
   * @return the result of the post-condition function
   * @example
   * <pre>
   * // Click with custom retry settings
   * Boolean success = unreliableButton.click(5, 1000, driver -> 
   *     driver.$(By.id("result")).getText().equals("Success"));
   * </pre>
   */
  default <R> R click(int retryCount, int interval, com.google.common.base.Function<DR, R> postCondition) {
    return CRetry.retry(idx -> {
      click();
      return postCondition.apply(getDriver());
    }, retryCount, interval);
  }

  /**
   * Clicks on the element using JavaScript (for invisible or overlapped elements).
   * Uses default timeout.
   * 
   * @example
   * <pre>
   * // Click an invisible or overlapped element
   * hiddenButton.clickInvisible();
   * 
   * // Click element that's behind an overlay
   * overlappedLink.clickInvisible();
   * </pre>
   */
  default void clickInvisible() {
    clickInvisible(getWaitSec());
  }

  /**
   * Clicks on the element using JavaScript with custom timeout.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Click invisible element with timeout
   * hiddenMenuOption.clickInvisible(15);
   * </pre>
   */
  default void clickInvisible(int waitSec) {
    getDriver().clickInvisible(getLocator(), waitSec);
  }

  /**
   * Clicks invisibly on the element and waits for a post-condition to be satisfied.
   * 
   * @param <R> the return type of the post-condition function
   * @param postCondition function to verify after clicking
   * @return the result of the post-condition function
   * @example
   * <pre>
   * // Click invisible element and verify result
   * String message = hiddenTrigger.clickInvisible(driver -> 
   *     driver.$(By.id("status")).getText());
   * </pre>
   */
  default <R> R clickInvisible(com.google.common.base.Function<DR, R> postCondition) {
    return clickInvisible(2, 2000, postCondition);
  }

  /**
   * Clicks invisibly on the element and waits for a post-condition with custom retry settings.
   * 
   * @param <R> the return type of the post-condition function
   * @param retryCount number of times to retry the click if post-condition fails
   * @param interval time in milliseconds between retries
   * @param postCondition function to verify after clicking
   * @return the result of the post-condition function
   * @example
   * <pre>
   * // Click invisible element with custom retry
   * Boolean loaded = hiddenRefreshButton.clickInvisible(3, 2000, driver -> 
   *     driver.$(By.id("content")).isDisplayed());
   * </pre>
   */
  default <R> R clickInvisible(int retryCount, int interval, com.google.common.base.Function<DR, R> postCondition) {
    return CRetry.retry(idx -> {
      clickInvisible();
      return postCondition.apply(getDriver());
    }, retryCount, interval);
  }

  /**
   * Opens the URL specified in the element's href attribute in the current tab.
   * Uses default timeout.
   * 
   * @example
   * <pre>
   * // Navigate to the link's URL
   * externalLink.openHref();
   * </pre>
   */
  default void openHref() {
    openHref(getWaitSec());
  }

  /**
   * Opens the URL specified in the element's href attribute with custom timeout.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Navigate to link with custom timeout
   * dynamicLink.openHref(10);
   * </pre>
   */
  default void openHref(int waitSec) {
    getDriver().open(getAttribute("href", waitSec));
  }

  /**
   * Opens the URL specified in the element's href attribute and waits for a post-condition.
   * 
   * @param <R> the return type of the post-condition function
   * @param postCondition function to verify after navigation
   * @return the result of the post-condition function
   * @example
   * <pre>
   * // Navigate and verify page title
   * String title = documentationLink.openHref(driver -> driver.getTitle());
   * </pre>
   */
  default <R> R openHref(com.google.common.base.Function<DR, R> postCondition) {
    return openHref(2, 2000, postCondition);
  }

  /**
   * Opens the URL specified in the element's href attribute and waits for a post-condition with retry.
   * 
   * @param <R> the return type of the post-condition function
   * @param retryCount number of times to retry if post-condition fails
   * @param interval time in milliseconds between retries
   * @param postCondition function to verify after navigation
   * @return the result of the post-condition function
   * @example
   * <pre>
   * // Navigate with retry logic
   * Boolean pageLoaded = slowLink.openHref(3, 5000, driver -> 
   *     driver.$(By.id("main-content")).isDisplayed());
   * </pre>
   */
  default <R> R openHref(int retryCount, int interval, com.google.common.base.Function<DR, R> postCondition) {
    return CRetry.retry(idx -> {
      openHref();
      return postCondition.apply(getDriver());
    }, retryCount, interval);
  }

  // Download File
  /**
   * Downloads a file by clicking the element and waits for the download to complete.
   * 
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download a PDF report
   * CFile report = downloadButton.downloadFile("report.pdf", "monthly-report.pdf");
   * System.out.println("File downloaded to: " + report.getAbsolutePath());
   * </pre>
   */
  default CFile downloadFile(String filename, String renameTo) {
    return downloadFile(getWaitSec(), filename, renameTo, false);
  }

  /**
   * Downloads a file with custom download wait time.
   * 
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download with extended wait time for large files
   * CFile largeFile = downloadLink.downloadFile(120, "large-dataset.zip", "dataset.zip");
   * </pre>
   */
  default CFile downloadFile(int downloadWait, String filename, String renameTo) {
    return downloadFile(downloadWait, filename, renameTo, false);
  }

  /**
   * Downloads a file with custom click and download wait times.
   * 
   * @param clickWait time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download with custom click and download timeouts
   * CFile document = slowButton.downloadFile(30, 60, "document.docx", "final-document.docx");
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, String filename, String renameTo) {
    return downloadFile(clickWait, downloadWait, filename, renameTo, false);
  }

  /**
   * Downloads a file and handles expected alert dialog.
   * 
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @param expectedAlert true if an alert dialog is expected after clicking
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download file that shows confirmation alert
   * CFile backup = exportButton.downloadFile(45, "backup.sql", "database-backup.sql", true);
   * </pre>
   */
  default CFile downloadFile(int downloadWait, String filename, String renameTo, boolean expectedAlert) {
    return downloadFile(getWaitSec(), downloadWait, filename, renameTo, expectedAlert);
  }

  /**
   * Downloads a file with all custom settings including alert handling.
   * 
   * @param clickWait time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @param expectedAlert true if an alert dialog is expected after clicking
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Full control over download process
   * CFile invoice = downloadInvoice.downloadFile(10, 90, "invoice.pdf", "invoice-2023.pdf", true);
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, String filename, String renameTo, boolean expectedAlert) {
    click(clickWait);
    if (expectedAlert) {
      getDriver().getAlert().accept();
    }
    CFile downloadFolder = CBrowserConfigs.getDownloadFolder(getDriver().getSessionId());
    CFile downloadedFile = new CFile(downloadFolder, filename);
    downloadedFile.verifyExists(downloadWait, 500, "File downloaded! file:" + downloadedFile);
    return downloadedFile.moveTo(CPathConfigs.fromTmp(renameTo));
  }

  /**
   * Downloads a file using pattern matching for the filename.
   * 
   * @param filename regex pattern to match the downloaded filename
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download file with dynamic name using pattern
   * Pattern reportPattern = Pattern.compile("report_\\d{8}\\.xlsx");
   * CFile dailyReport = generateReportButton.downloadFile(reportPattern, "daily-report.xlsx");
   * </pre>
   */
  default CFile downloadFile(Pattern filename, String renameTo) {
    return downloadFile(getWaitSec(), filename, renameTo, false);
  }

  /**
   * Downloads a file using pattern matching with custom download wait time.
   * 
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename regex pattern to match the downloaded filename
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download with pattern and extended wait
   * Pattern logPattern = Pattern.compile("application_\\d+\\.log");
   * CFile logFile = exportLogsButton.downloadFile(180, logPattern, "app-logs.log");
   * </pre>
   */
  default CFile downloadFile(int downloadWait, Pattern filename, String renameTo) {
    return downloadFile(downloadWait, filename, renameTo, false);
  }

  /**
   * Downloads a file using pattern matching with custom click and download wait times.
   * 
   * @param clickWait time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename regex pattern to match the downloaded filename
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download with pattern and custom timeouts
   * Pattern csvPattern = Pattern.compile("data_export_\\d{4}-\\d{2}-\\d{2}\\.csv");
   * CFile csvFile = slowExportButton.downloadFile(20, 120, csvPattern, "export.csv");
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, Pattern filename, String renameTo) {
    return downloadFile(clickWait, downloadWait, filename, renameTo, false);
  }

  /**
   * Downloads a file using pattern matching and handles expected alert.
   * 
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename regex pattern to match the downloaded filename
   * @param renameTo the new name for the downloaded file
   * @param expectedAlert true if an alert dialog is expected after clicking
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Download with pattern and alert handling
   * Pattern jsonPattern = Pattern.compile("config_backup_\\w+\\.json");
   * CFile config = backupButton.downloadFile(60, jsonPattern, "config-backup.json", true);
   * </pre>
   */
  default CFile downloadFile(int downloadWait, Pattern filename, String renameTo, boolean expectedAlert) {
    return downloadFile(getWaitSec(), downloadWait, filename, renameTo, expectedAlert);
  }

  /**
   * Downloads a file using pattern matching with full control over all settings.
   * 
   * @param clickWait time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename regex pattern to match the downloaded filename
   * @param renameTo the new name for the downloaded file
   * @param expectedAlert true if an alert dialog is expected after clicking
   * @return CFile object representing the downloaded and renamed file
   * @example
   * <pre>
   * // Full control download with pattern matching
   * Pattern xmlPattern = Pattern.compile("report_\\d{8}_\\d{6}\\.xml");
   * CFile xmlReport = complexDownloadButton.downloadFile(15, 300, xmlPattern, "final-report.xml", true);
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, Pattern filename, String renameTo, boolean expectedAlert) {
    click(clickWait);
    if (expectedAlert) {
      getDriver().getAlert().accept();
    }
    CFile downloadFolder = CBrowserConfigs.getDownloadFolder(getDriver().getSessionId());
    File downloadedFile = CRetry.retryIfFalse(idx -> new CList<>(downloadFolder.listFiles()).getFirstOrNull(file -> filename.matcher(file.getName()).matches()), downloadWait * 2, 500);
    CVerify.Bool.isTrue(downloadedFile.exists(), "File downloaded properly!");
    return new CFile(downloadedFile).moveTo(CPathConfigs.getTempChildFolder(renameTo));
  }

  // Upload

  /**
   * Uploads a file to a file input element.
   * Handles both local and remote (grid) execution contexts.
   * 
   * @param file the File object to upload
   * @example
   * <pre>
   * // Upload an image file
   * File imageFile = new File("/path/to/image.jpg");
   * fileInput.uploadFile(imageFile);
   * </pre>
   */
  default void uploadFile(File file) {
    uploadFile(CFileUtil.getCanonicalPath(file));
  }

  /**
   * Uploads a file to a file input element using file path.
   * Handles both local and remote (grid) execution contexts.
   * 
   * @param filePath the absolute path to the file to upload
   * @example
   * <pre>
   * // Upload a document using file path
   * fileInput.uploadFile("/Users/user/Documents/resume.pdf");
   * 
   * // Upload a CSV file
   * csvUpload.uploadFile("C:\\data\\employees.csv");
   * </pre>
   */
  default void uploadFile(String filePath) {
    if (!CGridConfigs.isUseLocalFileDetectorInOn()) {
      String fullFileName = getDriver().performActionOnDriver("Copy File To Node", webDriver -> CGridUtil.copyFileToNode(webDriver.getSessionId(), new File(filePath)));
      sendKeys(fullFileName);
    } else {
      sendKeys(filePath);
    }
    getDriver().pressTab();
  }

  /**
   * Uploads a resource file from the classpath to a file input element.
   * 
   * @param resourceName the name of the resource file in the classpath
   * @param clazz the class to use for loading the resource
   * @example
   * <pre>
   * // Upload a test data file from resources
   * fileInput.uploadResource("test-data.json", MyTestClass.class);
   * 
   * // Upload an image from resources
   * avatarUpload.uploadResource("default-avatar.png", getClass());
   * </pre>
   */
  default void uploadResource(String resourceName, Class clazz) {
    uploadResource(new CResource(resourceName, clazz));
  }

  /**
   * Uploads a resource file using a CResource object.
   * 
   * @param resource the CResource object representing the file to upload
   * @example
   * <pre>
   * // Upload using CResource object
   * CResource templateFile = new CResource("template.docx", MyClass.class);
   * fileInput.uploadResource(templateFile);
   * </pre>
   */
  default void uploadResource(CResource resource) {
    CFile filePath = resource.saveToFolder(CPathConfigs.getTempFolder());
    uploadFile(filePath.getCanonicalPath());
  }

  // Input

  /**
   * Sets text in an input field after formatting a Date object.
   * Clears existing content first using platform-appropriate key combination.
   * 
   * @param date the Date object to format and set
   * @param format the date format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy")
   * @example
   * <pre>
   * // Set current date in ISO format
   * Date today = new Date();
   * dateInput.setText(today, "yyyy-MM-dd");
   * 
   * // Set date in US format
   * birthdateInput.setText(birthDate, "MM/dd/yyyy");
   * </pre>
   */
  default void setText(Date date, String format) {
    setText(date, format, getWaitSec());
  }

  /**
   * Sets text in an input field after formatting a Date object with custom timeout.
   * 
   * @param date the Date object to format and set
   * @param format the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Set date with custom timeout
   * eventDateInput.setText(eventDate, "dd/MM/yyyy", 15);
   * </pre>
   */
  default void setText(Date date, String format, int waitSec) {
    setText(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Sets text in an input field using default timeout.
   * Clears existing content first using platform-appropriate key combination (Ctrl+A/Cmd+A + Delete).
   * 
   * @param text the text to enter in the field
   * @example
   * <pre>
   * // Enter username
   * usernameField.setText("john.doe@example.com");
   * 
   * // Enter search query
   * searchBox.setText("selenium automation");
   * 
   * // Clear field by setting empty text
   * commentField.setText("");
   * </pre>
   */
  default void setText(String text) {
    setText(text, getWaitSec());
  }

  /**
   * Sets text in an input field with custom timeout.
   * Clears existing content first using platform-appropriate key combination.
   * 
   * @param text the text to enter in the field
   * @param waitSec maximum time in seconds to wait for element to be present and interactable
   * @example
   * <pre>
   * // Set text with extended timeout for slow-loading fields
   * dynamicTextField.setText("Updated content", 20);
   * </pre>
   */
  default void setText(String text, int waitSec) {
    waitUntil("Set Text", waitSec, el -> {
      el.sendKeys(getClearKeys(), text);
      return true;
    });
  }

  /**
   * Sets text in an input field and presses Enter key.
   * Useful for search boxes and forms that submit on Enter.
   * 
   * @param text the text to enter before pressing Enter
   * @example
   * <pre>
   * // Search and submit with Enter key
   * searchBox.setTextAndEnter("automation testing");
   * 
   * // Login with Enter key
   * passwordField.setTextAndEnter("mySecretPassword");
   * </pre>
   */
  default void setTextAndEnter(String text) {
    setTextAndEnter(text, getWaitSec());
  }

  /**
   * Sets text in an input field and presses Enter key with custom timeout.
   * 
   * @param text the text to enter before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Search with custom timeout
   * slowSearchBox.setTextAndEnter("complex query", 15);
   * </pre>
   */
  default void setTextAndEnter(String text, int waitSec) {
    setTextAnd(text, Keys.ENTER, waitSec);
  }

  /**
   * Sets text in an input field and presses Tab key.
   * Useful for moving to the next field in forms.
   * 
   * @param text the text to enter before pressing Tab
   * @example
   * <pre>
   * // Fill field and move to next
   * firstNameField.setTextAndTab("John");
   * // Focus automatically moves to next field
   * 
   * // Fill form fields in sequence
   * emailField.setTextAndTab("john@example.com");
   * </pre>
   */
  default void setTextAndTab(String text) {
    setTextAndTab(text, getWaitSec());
  }

  /**
   * Sets text in an input field and presses Tab key with custom timeout.
   * 
   * @param text the text to enter before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Fill field and tab with timeout
   * slowLoadingField.setTextAndTab("data", 12);
   * </pre>
   */
  default void setTextAndTab(String text, int waitSec) {
    setTextAnd(text, Keys.TAB, waitSec);
  }

  /**
   * Sets text in an input field and presses a specified key.
   * 
   * @param text the text to enter
   * @param keys the key to press after entering text
   * @example
   * <pre>
   * // Set text and press Escape key
   * editField.setTextAnd("cancelled text", Keys.ESCAPE);
   * 
   * // Set text and press F1 for help
   * commandField.setTextAnd("help command", Keys.F1);
   * </pre>
   */
  default void setTextAnd(String text, Keys keys) {
    setTextAnd(text, keys, getWaitSec());
  }

  /**
   * Sets text in an input field and presses a specified key with custom timeout.
   * 
   * @param text the text to enter
   * @param keys the key to press after entering text
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Set text and press custom key with timeout
   * specialField.setTextAnd("special input", Keys.F5, 10);
   * </pre>
   */
  default void setTextAnd(String text, Keys keys, int waitSec) {
    waitUntil("Set Text And " + keys.name(), waitSec, el -> {
      el.sendKeys(getClearKeys(), text, keys);
      return true;
    });
  }

  /**
   * Sets the value attribute of an element using a formatted Date object.
   * Uses JavaScript to directly set the value attribute.
   * 
   * @param date the Date object to format and set
   * @param format the date format string
   * @example
   * <pre>
   * // Set date input value directly
   * Date appointmentDate = new Date();
   * dateInput.setValue(appointmentDate, "yyyy-MM-dd");
   * </pre>
   */
  default void setValue(Date date, String format) {
    setValue(date, format, getWaitSec());
  }

  /**
   * Sets the value attribute of an element using a formatted Date object with custom timeout.
   * 
   * @param date the Date object to format and set
   * @param format the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Set date value with timeout
   * startDateInput.setValue(startDate, "MM/dd/yyyy", 8);
   * </pre>
   */
  default void setValue(Date date, String format, int waitSec) {
    setValue(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Sets the value attribute of an element directly using JavaScript.
   * This method bypasses normal input events and directly sets the value.
   * 
   * @param text the text value to set
   * @example
   * <pre>
   * // Set hidden input value
   * hiddenTokenField.setValue("abc123token");
   * 
   * // Set readonly field value
   * readonlyField.setValue("system generated value");
   * 
   * // Set value without triggering change events
   * calculatedField.setValue("42.50");
   * </pre>
   */
  default void setValue(String text) {
    setValue(text, getWaitSec());
  }

  /**
   * Sets the value attribute of an element directly using JavaScript with custom timeout.
   * 
   * @param text the text value to set
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Set value with timeout for dynamically created fields
   * dynamicField.setValue("computed value", 12);
   * </pre>
   */
  default void setValue(String text, int waitSec) {
    setAttribute("value", text, waitSec);
  }

  /**
   * Sets the value attribute and presses Enter key.
   * Combines setValue with Enter key press for form submission.
   * 
   * @param text the text value to set before pressing Enter
   * @example
   * <pre>
   * // Set value and submit form
   * amountField.setValueAndEnter("150.00");
   * 
   * // Set search term and execute search
   * quickSearchField.setValueAndEnter("products");
   * </pre>
   */
  default void setValueAndEnter(String text) {
    setValueAndEnter(text, getWaitSec());
  }

  /**
   * Sets the value attribute and presses Enter key with custom timeout.
   * 
   * @param text the text value to set before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Set value and submit with timeout
   * complexFormField.setValueAndEnter("complex data", 15);
   * </pre>
   */
  default void setValueAndEnter(String text, int waitSec) {
    setValue(text, waitSec);
    sendKeys(Keys.ENTER);
  }

  /**
   * Sets the value attribute and presses Tab key.
   * Useful for setting values and moving to next field in forms.
   * 
   * @param text the text value to set before pressing Tab
   * @example
   * <pre>
   * // Set value and move to next field
   * priceField.setValueAndTab("29.99");
   * // Focus moves to next form field
   * </pre>
   */
  default void setValueAndTab(String text) {
    setValueAndTab(text, getWaitSec());
  }

  /**
   * Sets the value attribute and presses Tab key with custom timeout.
   * 
   * @param text the text value to set before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Set value and tab with timeout
   * asyncField.setValueAndTab("calculated value", 10);
   * </pre>
   */
  default void setValueAndTab(String text, int waitSec) {
    setValue(text, waitSec);
    sendKeys(Keys.TAB);
  }

  /**
   * Clears the content of an input field using platform-appropriate key combination.
   * Uses Ctrl+A+Delete on Windows/Linux or Cmd+A+Delete on Mac.
   * 
   * @example
   * <pre>
   * // Clear text field
   * messageField.clear();
   * 
   * // Clear search box
   * searchInput.clear();
   * </pre>
   */
  default void clear() {
    clear(getWaitSec());
  }

  /**
   * Clears the content of an input field with custom timeout.
   * 
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Clear field with timeout
   * slowLoadingInput.clear(15);
   * </pre>
   */
  default void clear(int waitSec) {
    waitUntil("Clear", waitSec, el -> {
      el.sendKeys(getClearKeys());
      return true;
    });
  }

  /**
   * Types text character by character after formatting a Date object.
   * Clears existing content first, then types each character individually.
   * 
   * @param date the Date object to format and type
   * @param format the date format string
   * @example
   * <pre>
   * // Type date slowly for date pickers that need gradual input
   * Date selectedDate = new Date();
   * datePicker.type(selectedDate, "MM/dd/yyyy");
   * </pre>
   */
  default void type(Date date, String format) {
    type(date, format, getWaitSec());
  }

  /**
   * Types text character by character after formatting a Date object with custom timeout.
   * 
   * @param date the Date object to format and type
   * @param format the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Type date with timeout
   * endDateInput.type(endDate, "yyyy-MM-dd", 12);
   * </pre>
   */
  default void type(Date date, String format, int waitSec) {
    type(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Types text character by character in an input field.
   * Clears existing content first, then types each character individually.
   * Useful for fields that need gradual input or have real-time validation.
   * 
   * @param text the text to type character by character
   * @example
   * <pre>
   * // Type text slowly for autocomplete fields
   * autocompleteField.type("JavaScript");
   * 
   * // Type for fields with character-by-character validation
   * phoneField.type("555-123-4567");
   * </pre>
   */
  default void type(String text) {
    type(text, getWaitSec());
  }

  /**
   * Types text character by character with custom timeout.
   * 
   * @param text the text to type character by character
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Type with custom timeout
   * slowField.type("sensitive data", 20);
   * </pre>
   */
  default void type(String text, int waitSec) {
    type(text, waitSec, 0L);
  }

  /**
   * Types text character by character with a delay between each character.
   * Useful for simulating human-like typing or for fields sensitive to typing speed.
   * 
   * @param text the text to type
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Type slowly with 100ms delay between characters
   * secureField.type("password123", 100);
   * 
   * // Type very slowly for sensitive fields
   * tokenField.type("abc123def456", 500);
   * </pre>
   */
  default void type(String text, long intervalInMilliSeconds) {
    type(text, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with custom timeout and delay between characters.
   * 
   * @param text the text to type
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Type with both custom timeout and character delay
   * specialInput.type("complex input", 15, 200);
   * </pre>
   */
  default void type(String text, int waitSec, long intervalInMilliSeconds) {
    waitUntil("Type", waitSec, el -> {
      el.sendKeys(getClearKeys());

      if (intervalInMilliSeconds < 10) {
        el.sendKeys(StringUtils.defaultString(text).split(""));
      } else {
        for (String c : StringUtils.defaultString(text).split("")) {
          el.sendKeys(c);
          CSleeper.sleepTight(intervalInMilliSeconds);
        }
      }
      return true;
    });
  }

  /**
   * Types text character by character and presses Tab key.
   * Combines typing with Tab key press for moving to next field.
   * 
   * @param text the text to type before pressing Tab
   * @example
   * <pre>
   * // Type and move to next field
   * accountField.typeAndTab("12345");
   * // Focus automatically moves to next form field
   * </pre>
   */
  default void typeAndTab(String text) {
    typeAndTab(text, getWaitSec());
  }

  /**
   * Types text character by character and presses Tab key with custom timeout.
   * 
   * @param text the text to type before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Type and tab with timeout
   * dynamicField.typeAndTab("data", 10);
   * </pre>
   */
  default void typeAndTab(String text, int waitSec) {
    typeAndTab(text, waitSec, 0L);
  }

  /**
   * Types text character by character with delay and presses Tab key.
   * 
   * @param text the text to type before pressing Tab
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Type slowly and move to next field
   * sensitiveField.typeAndTab("secure data", 150);
   * </pre>
   */
  default void typeAndTab(String text, long intervalInMilliSeconds) {
    typeAndTab(text, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with delay and presses Tab key with custom timeout.
   * 
   * @param text the text to type before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Type with all custom settings and tab
   * complexField.typeAndTab("complex input", 15, 100);
   * </pre>
   */
  default void typeAndTab(String text, int waitSec, long intervalInMilliSeconds) {
    typeAnd(text, Keys.TAB, waitSec, intervalInMilliSeconds);
  }

  /**
   * Types text character by character and presses Enter key.
   * Useful for form submission or search execution after typing.
   * 
   * @param text the text to type before pressing Enter
   * @example
   * <pre>
   * // Type search query and execute search
   * searchField.typeAndEnter("selenium automation");
   * 
   * // Type password and submit form
   * passwordField.typeAndEnter("mySecretPassword");
   * </pre>
   */
  default void typeAndEnter(String text) {
    typeAndEnter(text, getWaitSec());
  }

  /**
   * Types text character by character and presses Enter key with custom timeout.
   * 
   * @param text the text to type before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Type and submit with timeout
   * slowSearchField.typeAndEnter("complex query", 20);
   * </pre>
   */
  default void typeAndEnter(String text, int waitSec) {
    typeAndEnter(text, waitSec, 0L);
  }

  /**
   * Types text character by character with delay and presses Enter key.
   * 
   * @param text the text to type before pressing Enter
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Type slowly and submit
   * commandField.typeAndEnter("important command", 200);
   * </pre>
   */
  default void typeAndEnter(String text, long intervalInMilliSeconds) {
    typeAndEnter(text, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with delay and presses Enter key with custom timeout.
   * 
   * @param text the text to type before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Type with all custom settings and submit
   * advancedField.typeAndEnter("advanced input", 18, 75);
   * </pre>
   */
  default void typeAndEnter(String text, int waitSec, long intervalInMilliSeconds) {
    typeAnd(text, Keys.ENTER, waitSec, intervalInMilliSeconds);
  }

  /**
   * Types text character by character and presses a specified key.
   * 
   * @param text the text to type
   * @param keys the key to press after typing
   * @example
   * <pre>
   * // Type text and press Escape
   * editField.typeAnd("cancelled text", Keys.ESCAPE);
   * 
   * // Type text and press F1 for help
   * helpField.typeAnd("help topic", Keys.F1);
   * </pre>
   */
  default void typeAnd(String text, Keys keys) {
    typeAnd(text, keys, getWaitSec());
  }

  /**
   * Types text character by character and presses a specified key with custom timeout.
   * 
   * @param text the text to type
   * @param keys the key to press after typing
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   * <pre>
   * // Type and press custom key with timeout
   * specialField.typeAnd("special input", Keys.F5, 12);
   * </pre>
   */
  default void typeAnd(String text, Keys keys, int waitSec) {
    typeAnd(text, keys, waitSec, 0L);
  }

  /**
   * Types text character by character with delay and presses a specified key.
   * 
   * @param text the text to type
   * @param keys the key to press after typing
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Type slowly and press custom key
   * scriptField.typeAnd("console.log('test');", Keys.F12, 50);
   * </pre>
   */
  default void typeAnd(String text, Keys keys, long intervalInMilliSeconds) {
    typeAnd(text, keys, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with delay and presses a specified key with custom timeout.
   * This is the most comprehensive typing method with full control over all parameters.
   * 
   * @param text the text to type
   * @param keys the key to press after typing
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   * <pre>
   * // Full control typing with custom key
   * complexField.typeAnd("complex data entry", Keys.F10, 20, 100);
   * 
   * // Type code slowly and press compile key
   * codeEditor.typeAnd("function test() { return true; }", Keys.F9, 15, 25);
   * </pre>
   */
  default void typeAnd(String text, Keys keys, int waitSec, long intervalInMilliSeconds) {
    type(text, waitSec, intervalInMilliSeconds);
    getDriver().sendKeys(keys);
  }

  private void _click(boolean useJS, WebElement webElement) {
    if (useJS) {
      _clickWithJS(webElement);
    } else {
      try {
        webElement.click();
      } catch (WebDriverException t) {
        _clickWithJS(webElement);
      }
    }
  }

  private void _clickWithJS(WebElement webElement) {
    getDriver().executeScript("arguments[0].click();", webElement);
  }


  private String getClearKeys() {
    if (getDriver().getPlatform().isMac())
      return Keys.chord(Keys.COMMAND, "a", Keys.DELETE);

    return Keys.chord(Keys.CONTROL, "a", Keys.DELETE);
  }
}

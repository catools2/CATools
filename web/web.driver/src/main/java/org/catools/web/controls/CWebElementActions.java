package org.catools.web.controls;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.date.CDate;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.utils.CFileUtil;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CStringUtil;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;
import org.catools.media.utils.CImageUtil;
import org.catools.web.config.CBrowserConfigs;
import org.catools.web.config.CGridConfigs;
import org.catools.web.enums.CKeys;

/**
 * Interface providing action methods for web element interactions. This interface extends
 * CWebElementStates and provides comprehensive methods for performing various actions on web
 * elements such as clicking, typing, moving, dragging, scrolling, and file operations.
 *
 * @author CA Tools Team
 */
public interface CWebElementActions extends CWebElementStates {

  // Actions

  /**
   * Moves the mouse cursor to the center of the element. Uses default timeout for waiting until
   * element is present.
   *
   * @example
   *     <pre>
   * // Move to a button element
   * button.moveTo();
   * </pre>
   */
  default boolean moveTo() {
    return moveTo(0, 0);
  }

  /**
   * Moves the mouse cursor to the center of the element with specified wait time.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Move to element and wait up to 10 seconds for it to be present
   * element.moveTo(10);
   * </pre>
   */
  default boolean moveTo(int waitSec) {
    return moveTo(0, 0, waitSec);
  }

  /**
   * Moves the mouse cursor to a specific offset from the element's center. Uses default timeout for
   * waiting until element is present.
   *
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @example
   *     <pre>
   * // Move 50 pixels right and 20 pixels down from element center
   * element.moveTo(50, 20);
   * </pre>
   */
  default boolean moveTo(int xOffset, int yOffset) {
    return moveTo(xOffset, yOffset, getWaitSec());
  }

  /**
   * Moves the mouse cursor to a specific offset from the element's center with custom wait time.
   *
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Move to offset position and wait up to 15 seconds
   * element.moveTo(30, -10, 15);
   * </pre>
   */
  default boolean moveTo(int xOffset, int yOffset, int waitSec) {
    return waitUntil(
        "MoveTo",
        waitSec,
        false,
        engine -> isPresent(waitSec) && engine.moveToElement(getLocator(), xOffset, yOffset));
  }

  /**
   * Performs a drop action at a specific offset from the element's center. Uses default timeout for
   * waiting until element is present.
   *
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @example
   *     <pre>
   * // Drop at position 100 pixels right, 50 pixels down
   * draggableElement.dropTo(100, 50);
   * </pre>
   */
  default boolean dropTo(int xOffset, int yOffset) {
    return dropTo(xOffset, yOffset, getWaitSec());
  }

  /**
   * Performs a drop action at a specific offset from the element's center with custom wait time.
   *
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Drop at offset position and wait up to 8 seconds
   * draggableElement.dropTo(75, -25, 8);
   * </pre>
   */
  default boolean dropTo(int xOffset, int yOffset, int waitSec) {
    return waitUntil(
        "Drop To",
        waitSec,
        false,
        engine -> isPresent(waitSec) && engine.dropTo(getLocator(), xOffset, yOffset));
  }

  /**
   * Drags the element from its center to a target offset position. Uses default timeout for waiting
   * until element is present.
   *
   * @param xOffset1 target horizontal offset in pixels
   * @param yOffset1 target vertical offset in pixels
   * @example
   *     <pre>
   * // Drag element from center to position (200, 100)
   * slider.dragAndDropTo(200, 100);
   * </pre>
   */
  default boolean dragAndDropTo(int xOffset1, int yOffset1) {
    return dragAndDropTo(xOffset1, yOffset1, 0, 0);
  }

  /**
   * Drags the element from its center to a target offset position with custom wait time.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Drag to target position and wait up to 12 seconds
   * draggableItem.dragAndDropTo(150, -50, 12);
   * </pre>
   */
  default boolean dragAndDropTo(int xOffset1, int yOffset1, int waitSec) {
    return dragAndDropTo(xOffset1, yOffset1, 0, 0, waitSec);
  }

  /**
   * Drags the element from a source offset to a target offset position. Uses default timeout for
   * waiting until element is present.
   *
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @example
   *     <pre>
   * // Drag from (10, 5) to (100, 75) relative to element center
   * resizableCorner.dragAndDropTo(10, 5, 100, 75);
   * </pre>
   */
  default boolean dragAndDropTo(int xOffset1, int yOffset1, int xOffset2, int yOffset2) {
    return dragAndDropTo(xOffset1, yOffset1, xOffset2, yOffset2, getWaitSec());
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
   *     <pre>
   * // Drag from source to target with 15 second timeout
   * dragHandle.dragAndDropTo(0, 0, 200, 150, 15);
   * </pre>
   */
  default boolean dragAndDropTo(
      int xOffset1, int yOffset1, int xOffset2, int yOffset2, int waitSec) {
    return waitUntil(
        "Drag And Drop To",
        waitSec,
        false,
        engine ->
            isPresent(waitSec)
                && engine.dragAndDropTo(getLocator(), xOffset1, yOffset1, xOffset2, yOffset2));
  }

  /**
   * Drags the element to another target element. Uses default timeout for waiting until element is
   * present.
   *
   * @param target the String locator of the target element
   * @example
   *     <pre>
   * // Drag item to a drop zone
   * draggableItem.dragAndDropTo(String.id("dropZone"));
   * </pre>
   */
  default boolean dragAndDropTo(String target) {
    return dragAndDropTo(target, getWaitSec());
  }

  /**
   * Drags the element to another target element with custom wait time.
   *
   * @param target the String locator of the target element
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Drag to target element with 20 second timeout
   * fileItem.dragAndDropTo(String.className("upload-area"), 20);
   * </pre>
   */
  default boolean dragAndDropTo(String target, int waitSec) {
    return dragAndDropTo(target, 0, 0, waitSec);
  }

  /**
   * Drags the element from a specific offset to a target element. Uses default timeout for waiting
   * until element is present.
   *
   * @param target the String locator of the target element
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @example
   *     <pre>
   * // Drag from specific point on element to target
   * resizeHandle.dragAndDropTo(String.id("container"), 10, 10);
   * </pre>
   */
  default boolean dragAndDropTo(String target, int xOffset1, int yOffset1) {
    return dragAndDropTo(target, xOffset1, yOffset1, DEFAULT_TIMEOUT);
  }

  /**
   * Drags the element from a specific offset to a target element with custom wait time.
   *
   * @param target the String locator of the target element
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Drag from offset to target with timeout
   * menuItem.dragAndDropTo(String.className("menu-container"), 5, 0, 10);
   * </pre>
   */
  default boolean dragAndDropTo(String target, int xOffset1, int yOffset1, int waitSec) {
    return waitUntil(
        "Drag And Drop To",
        waitSec,
        false,
        engine ->
            isPresent(waitSec) && engine.dragAndDropTo(getLocator(), target, xOffset1, yOffset1));
  }

  /**
   * Executes JavaScript code on the element using default timeout.
   *
   * @param <R> the return type of the script execution
   * @param script the JavaScript code to execute
   * @return the result of script execution
   * @example
   *     <pre>
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
   *     <pre>
   * // Scroll element into view with custom timeout
   * element.executeScript("arguments[0].scrollIntoView(true);", 15);
   *
   * // Get computed style property
   * String color = element.executeScript("return getComputedStyle(arguments[0]).color;", 5);
   * </pre>
   */
  @SuppressWarnings("unchecked")
  default <R> R executeScript(String script, int waitSec) {
    return waitUntil(
        "Execute Script", waitSec, engine -> (R) engine.executeScript(getLocator(), script));
  }

  /**
   * Gets the location (coordinates) of the element on the page.
   *
   * @return Point object containing x and y coordinates
   * @example
   *     <pre>
   * // Get element position
   * Point location = button.getLocation();
   * System.out.println("Element is at: " + location.x + ", " + location.y);
   * </pre>
   */
  default Point getLocation(int waitSec) {
    return waitUntil("Get Location", waitSec, engine -> engine.getLocation(getLocator()));
  }

  /**
   * Sends keys to the element (types text or special keys).
   *
   * @param keys the keys to send (text or special keys like "Enter")
   * @example
   *     <pre>
   * // Type text in input field
   * inputField.sendKeys("Hello World");
   *
   * // Send special keys
   * inputField.sendKeys("Ctrl", "a", "Delete");
   *
   * // Send combination
   * inputField.sendKeys("test@example.com", "Tab");
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_send_keys",
      title = "Send Keys to Element",
      description = "Sends keys to the web element (types text or special keys)")
  default boolean sendKeys(
      @CMcpToolParam(name = "keys", description = "The keys to send to the element") String keys) {
    return sendKeys(keys, getWaitSec());
  }

  /**
   * Sends keys to the element (types text or special keys).
   *
   * @param keys the keys to send (text or special keys like "Enter")
   * @param waitSec maximum time in seconds to wait for element to be present
   */
  default boolean sendKeys(String keys, int waitSec) {
    return waitUntil("Send Keys", waitSec, false, engine -> engine.sendKeys(getLocator(), keys));
  }

  /**
   * Sends keys to the element (types text or special keys).
   *
   * @param keys the keys to send (text or special keys like "Enter")
   * @example
   *     <pre>
   * // Send special keys
   * inputField.press(CKeys.CONTROL, CKeys.A, CKeys.DELETE);
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_press_special_keys",
      title = "Press Special Keys to Element",
      description = "Press keys on the web element (Enter, Tab, etc.)")
  default boolean press(
      @CMcpToolParam(name = "keys", description = "The keys to send to the element")
          CKeys... keys) {
    return press(getWaitSec(), keys);
  }

  /**
   * Sends keys to the element (types text or special keys).
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param keys the keys to send (text or special keys like "Enter")
   */
  default boolean press(int waitSec, CKeys... keys) {
    return waitUntil("Press", waitSec, false, engine -> engine.press(getLocator(), keys));
  }

  /**
   * Presses the Enter key on the element.
   *
   * @example
   *     <pre>
   * // Press Enter key
   * inputField.pressEnter();
   * </pre>
   */
  default boolean pressEnter() {
    return press(CKeys.ENTER);
  }

  /**
   * Presses the Escape key on the element.
   *
   * @example
   *     <pre>
   * // Press Escape key
   * modalDialog.pressEscape();
   * </pre>
   */
  default boolean pressEscape() {
    return press(CKeys.ESCAPE);
  }

  /**
   * Presses the Tab key on the element.
   *
   * @example
   *     <pre>
   * // Press Tab key
   * inputField.pressTab();
   * </pre>
   */
  default boolean pressTab() {
    return press(CKeys.TAB);
  }

  /**
   * Performs a mouse click on the element using default timeout. This method first moves to the
   * element, then performs the click.
   *
   * @example
   *     <pre>
   * // Click on a button
   * submitButton.mouseClick();
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_mouse_click",
      title = "Mouse Click Element",
      description = "Performs a mouse click on the web element")
  default boolean mouseClick() {
    return mouseClick(getWaitSec());
  }

  /**
   * Performs a mouse click on the element with custom timeout. This method first moves to the
   * element, then performs the click.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Click with custom timeout
   * dynamicButton.mouseClick(20);
   * </pre>
   */
  default boolean mouseClick(int waitSec) {
    return waitUntil(
        "Mouse Click",
        waitSec,
        false,
        engine -> moveTo(waitSec) && engine.mouseClick(getLocator()));
  }

  /**
   * Performs a mouse double-click on the element using default timeout. This method first moves to
   * the element, then performs the double-click.
   *
   * @example
   *     <pre>
   * // Double-click on a file icon
   * fileIcon.mouseDoubleClick();
   * </pre>
   */
  default boolean mouseDoubleClick() {
    return mouseDoubleClick(getWaitSec());
  }

  /**
   * Performs a mouse double-click on the element with custom timeout. This method first moves to
   * the element, then performs the double-click.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Double-click with custom timeout
   * editableCell.mouseDoubleClick(15);
   * </pre>
   */
  default boolean mouseDoubleClick(int waitSec) {
    return waitUntil(
        "Mouse Double Click",
        waitSec,
        false,
        engine -> moveTo(waitSec) && engine.mouseDoubleClick(getLocator()));
  }

  /** Scrolls the element into view within the browser window. */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_scroll_into_view",
      title = "Scroll Element Into View",
      description = "Scrolls the element into view within the browser window")
  default boolean scrollIntoView() {
    return scrollIntoView(getWaitSec());
  }

  /**
   * Scrolls the element into view within the browser window with custom timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Scroll with custom timeout
   * lazyLoadedElement.scrollIntoView(true, 30);
   * </pre>
   */
  default boolean scrollIntoView(int waitSec) {
    return waitUntil("Scroll Into View", waitSec, engine -> engine.scrollIntoView(getLocator()));
  }

  /**
   * Scrolls the element horizontally to the left by default amount (900 pixels).
   *
   * @example
   *     <pre>
   * // Scroll a horizontal scrollable container to the left
   * horizontalScrollContainer.scrollLeft();
   * </pre>
   */
  default boolean scrollLeft() {
    return scrollLeft(900, getWaitSec());
  }

  /**
   * Scrolls the element horizontally to the left by specified amount.
   *
   * @param scrollSize amount in pixels to scroll left
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Scroll left by 500 pixels with 10 second timeout
   * carousel.scrollLeft(500, 10);
   * </pre>
   */
  default boolean scrollLeft(int scrollSize, int waitSec) {
    return waitUntil("Scroll Left", waitSec, engine -> engine.scrollLeft(getLocator(), scrollSize));
  }

  /**
   * Scrolls the element horizontally to the right by default amount (900 pixels).
   *
   * @example
   *     <pre>
   * // Scroll a horizontal scrollable container to the right
   * horizontalScrollContainer.scrollRight();
   * </pre>
   */
  default boolean scrollRight() {
    return scrollRight(900, getWaitSec());
  }

  /**
   * Scrolls the element horizontally to the right by specified amount.
   *
   * @param scrollSize amount in pixels to scroll right
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Scroll right by 300 pixels with 8 second timeout
   * imageGallery.scrollRight(300, 8);
   * </pre>
   */
  default boolean scrollRight(int scrollSize, int waitSec) {
    return waitUntil(
        "Scroll Right", waitSec, engine -> engine.scrollRight(getLocator(), scrollSize));
  }

  /**
   * Sets an attribute value on the element using JavaScript using default timeout.
   *
   * @param attributeName the name of the attribute to set
   * @param value the value to set for the attribute
   * @example
   *     <pre>
   * // Set custom data attribute
   * element.setAttribute("data-test-id", "submit-button");
   *
   * // Change input type
   * inputElement.setAttribute("type", "password");
   * </pre>
   */
  default boolean setAttribute(String attributeName, String value) {
    return setAttribute(attributeName, value, getWaitSec());
  }

  /**
   * Sets an attribute value on the element using JavaScript with custom timeout.
   *
   * @param attributeName the name of the attribute to set
   * @param value the value to set for the attribute
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Set disabled attribute with timeout
   * dynamicButton.setAttribute("disabled", "true", 10);
   * </pre>
   */
  default boolean setAttribute(String attributeName, String value, int waitSec) {
    return isPresent(waitSec)
        && executeScript("arguments[0][\"%s\"]=\"%s\";".formatted(attributeName, value)) != null;
  }

  /**
   * Removes an attribute from the element using JavaScript and default timeout.
   *
   * @param attributeName the name of the attribute to remove
   * @example
   *     <pre>
   * // Remove disabled attribute to enable button
   * submitButton.removeAttribute("disabled");
   *
   * // Remove custom data attribute
   * element.removeAttribute("data-temp");
   * </pre>
   */
  default boolean removeAttribute(String attributeName) {
    return removeAttribute(attributeName, getWaitSec());
  }

  /**
   * Removes an attribute from the element using JavaScript with custom timeout.
   *
   * @param attributeName the name of the attribute to remove
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Remove readonly attribute with timeout
   * inputField.removeAttribute("readonly", 12);
   * </pre>
   */
  default boolean removeAttribute(String attributeName, int waitSec) {
    if (!isPresent(waitSec)) return false;
    executeScript("arguments[0].removeAttribute(\"%s\");".formatted(attributeName));
    return true;
  }

  /**
   * Sets the selected state of the element (for checkboxes, radio buttons).
   *
   * @param value true to select the element, false to deselect
   * @example
   *     <pre>
   * // Select a checkbox
   * checkbox.set(true);
   *
   * // Deselect a radio button
   * radioButton.set(false);
   * </pre>
   */
  default boolean set(boolean value) {
    return set(value, DEFAULT_TIMEOUT);
  }

  /**
   * Sets the selected state of the element with custom timeout.
   *
   * @param value true to select the element, false to deselect
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Select checkbox with custom timeout
   * dynamicCheckbox.set(true, 20);
   * </pre>
   */
  default boolean set(boolean value, int waitSec) {
    if (value) return select(waitSec);
    else return deselect(waitSec);
  }

  /**
   * Selects the element (for checkboxes, radio buttons) if not already selected. Uses default
   * timeout.
   *
   * @example
   *     <pre>
   * // Select a checkbox (only clicks if not already selected)
   * agreeCheckbox.select();
   *
   * // Select a radio option
   * genderMaleRadio.select();
   * </pre>
   */
  default boolean select() {
    return select(getWaitSec());
  }

  /**
   * Selects the element (for checkboxes, radio buttons) if not already selected. Uses custom
   * timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Select with custom timeout
   * termsCheckbox.select(15);
   * </pre>
   */
  default boolean select(int waitSec) {
    return waitUntil(
        "Select",
        waitSec,
        engine -> {
          // Check if already selected, if not, click to select
          if (!engine.isElementSelected(getLocator())) {
            engine.click(getLocator());
          }
          return true;
        });
  }

  /**
   * Deselects the element (for checkboxes) if currently selected. Uses default timeout.
   *
   * @example
   *     <pre>
   * // Deselect a checkbox (only clicks if currently selected)
   * newsletterCheckbox.deselect();
   * </pre>
   */
  default boolean deselect() {
    return deselect(getWaitSec());
  }

  /**
   * Deselects the element (for checkboxes) if currently selected. Uses custom timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Deselect with custom timeout
   * optionalCheckbox.deselect(8);
   * </pre>
   */
  default boolean deselect(int waitSec) {
    return waitUntil(
        "Deselect",
        waitSec,
        engine -> {
          // Check if already selected, if so, click to deselect
          if (engine.isElementSelected(getLocator())) {
            engine.click(getLocator());
          }
          return true;
        });
  }

  /**
   * Clicks on the element using default timeout. Falls back to JavaScript click if normal click
   * fails.
   *
   * @example
   *     <pre>
   * // Click a button
   * submitButton.click();
   *
   * // Click a link
   * menuLink.click();
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_click",
      title = "Click Element",
      description = "Clicks on the web element with fallback to JavaScript click if needed")
  default boolean click() {
    return click(getWaitSec());
  }

  /**
   * Clicks on the element with custom timeout. Falls back to JavaScript click if normal click
   * fails.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Click with custom timeout
   * dynamicButton.click(20);
   * </pre>
   */
  default boolean click(int waitSec) {
    return waitUntil("Click", waitSec, engine -> engine.click(getLocator()));
  }

  /**
   * Clicks on the element and waits for a post-condition to be satisfied. Retries the click if the
   * post-condition is not met.
   *
   * @param <R> the return type of the post-condition function
   * @param postCondition function to verify after clicking
   * @return the result of the post-condition function
   * @example
   *     <pre>
   * // Click and wait for page to load
   * String newUrl = nextPageButton.click(driver -> driver.getCurrentUrl());
   *
   * // Click and wait for element to appear
   * Boolean elementVisible = submitButton.click(driver ->
   *     driver.$(String.id("success-message")).isDisplayed());
   * </pre>
   */
  default <R> R click(Function<CElementEngine<?>, R> postCondition) {
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
   *     <pre>
   * // Click with custom retry settings
   * Boolean success = unreliableButton.click(5, 1000, driver ->
   *     driver.$(String.id("result")).getText().equals("Success"));
   * </pre>
   */
  default <R> R click(int retryCount, int interval, Function<CElementEngine<?>, R> postCondition) {
    return CRetry.retry(
        idx -> {
          click();
          return postCondition.apply(getElementEngine());
        },
        retryCount,
        interval);
  }

  // Download File

  /**
   * Downloads a file and handles expected alert dialog.
   *
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   *     <pre>
   * // Download file that shows confirmation alert
   * CFile backup = exportButton.downloadFile("backup.sql", "database-backup.sql", true);
   * </pre>
   */
  default CFile downloadFile(String filename, String renameTo) {
    return downloadFile(getWaitSec(), DEFAULT_TIMEOUT, filename, renameTo);
  }

  /**
   * Downloads a file and handles expected alert dialog.
   *
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   *     <pre>
   * // Download file that shows confirmation alert
   * CFile backup = exportButton.downloadFile(45, "backup.sql", "database-backup.sql", true);
   * </pre>
   */
  default CFile downloadFile(int downloadWait, String filename, String renameTo) {
    return downloadFile(getWaitSec(), downloadWait, filename, renameTo);
  }

  /**
   * Downloads a file with all custom settings including alert handling.
   *
   * @param clickWait time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename the expected filename of the downloaded file
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   *     <pre>
   * // Full control over download process
   * CFile invoice = downloadInvoice.downloadFile(10, 90, "invoice.pdf", "invoice-2023.pdf", true);
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, String filename, String renameTo) {
    click(clickWait);
    CFile downloadFolder = CBrowserConfigs.getDownloadFolder();
    CFile downloadedFile = new CFile(downloadFolder, filename);
    downloadedFile.verifyExists(downloadWait, 500, "File downloaded! file:" + downloadedFile);
    return downloadedFile.moveTo(CPathConfigs.fromTmp(renameTo));
  }

  /**
   * Downloads a file using pattern matching and handles expected alert.
   *
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename regex pattern to match the downloaded filename
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   *     <pre>
   * // Download with pattern and alert handling
   * Pattern jsonPattern = Pattern.compile("config_backup_\\w+\\.json");
   * CFile config = backupButton.downloadFile(60, jsonPattern, "config-backup.json", true);
   * </pre>
   */
  default CFile downloadFile(int downloadWait, Pattern filename, String renameTo) {
    return downloadFile(getWaitSec(), downloadWait, filename, renameTo);
  }

  /**
   * Downloads a file using pattern matching with full control over all settings.
   *
   * @param clickWait time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename regex pattern to match the downloaded filename
   * @param renameTo the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example
   *     <pre>
   * // Full control download with pattern matching
   * Pattern xmlPattern = Pattern.compile("report_\\d{8}_\\d{6}\\.xml");
   * CFile xmlReport = complexDownloadButton.downloadFile(15, 300, xmlPattern, "final-report.xml", true);
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, Pattern filename, String renameTo) {
    click(clickWait);
    CFile downloadFolder = CBrowserConfigs.getDownloadFolder();
    File downloadedFile =
        CRetry.retryIfFalse(
            idx ->
                new CList<>(downloadFolder.listFiles())
                    .getFirstOrNull(file -> filename.matcher(file.getName()).matches()),
            downloadWait * 2,
            500);
    CVerify.Bool.isTrue(downloadedFile.exists(), "File downloaded properly!");
    return new CFile(downloadedFile).moveTo(CPathConfigs.getTempChildFolder(renameTo));
  }

  // Upload

  /**
   * Uploads a file to a file input element. Handles both local and remote (grid) execution
   * contexts.
   *
   * @param file the File object to upload
   * @example
   *     <pre>
   * // Upload an image file
   * File imageFile = new File("/path/to/image.jpg");
   * fileInput.uploadFile(imageFile);
   * </pre>
   */
  default boolean uploadFile(File file) {
    return uploadFile(CFileUtil.getCanonicalPath(file));
  }

  /**
   * Uploads a file to a file input element using file path. Handles both local and remote (grid)
   * execution contexts.
   *
   * @param filePath the absolute path to the file to upload
   * @example
   *     <pre>
   * // Upload a document using file path
   * fileInput.uploadFile("/Users/user/Documents/resume.pdf");
   *
   * // Upload a CSV file
   * csvUpload.uploadFile("C:\\data\\employees.csv");
   * </pre>
   */
  default boolean uploadFile(String filePath) {
    boolean result;
    if (!CGridConfigs.isUseLocalFileDetectorInOn()) {
      result = sendKeys(filePath);
    } else {
      result = sendKeys(filePath);
    }
    return result && pressTab();
  }

  /**
   * Uploads a resource file from the classpath to a file input element.
   *
   * @param resourceName the name of the resource file in the classpath
   * @param clazz the class to use for loading the resource
   * @example
   *     <pre>
   * // Upload a test data file from resources
   * fileInput.uploadResource("test-data.json", MyTestClass.class);
   *
   * // Upload an image from resources
   * avatarUpload.uploadResource("default-avatar.png", getClass());
   * </pre>
   */
  default boolean uploadResource(String resourceName, Class clazz) {
    return uploadResource(new CResource(resourceName, clazz));
  }

  /**
   * Uploads a resource file using a CResource object.
   *
   * @param resource the CResource object representing the file to upload
   * @example
   *     <pre>
   * // Upload using CResource object
   * CResource templateFile = new CResource("template.docx", MyClass.class);
   * fileInput.uploadResource(templateFile);
   * </pre>
   */
  default boolean uploadResource(CResource resource) {
    CFile filePath = resource.saveToFolder(CPathConfigs.getTempFolder());
    return uploadFile(filePath.getCanonicalPath());
  }

  // Input

  /**
   * Sets text in an input field after formatting a Date object. Clears existing content first using
   * platform-appropriate key combination.
   *
   * @param date the Date object to format and set
   * @param format the date format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy")
   * @example
   *     <pre>
   * // Set current date in ISO format
   * Date today = new Date();
   * dateInput.setText(today, "yyyy-MM-dd");
   *
   * // Set date in US format
   * birthdateInput.setText(birthDate, "MM/dd/yyyy");
   * </pre>
   */
  default boolean setText(Date date, String format) {
    return setText(date, format, getWaitSec());
  }

  /**
   * Sets text in an input field after formatting a Date object with custom timeout.
   *
   * @param date the Date object to format and set
   * @param format the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Set date with custom timeout
   * eventDateInput.setText(eventDate, "dd/MM/yyyy", 15);
   * </pre>
   */
  default boolean setText(Date date, String format, int waitSec) {
    return setText(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Sets text in an input field using default timeout. Clears existing content first using
   * platform-appropriate key combination (Ctrl+A/Cmd+A + Delete).
   *
   * @param text the text to enter in the field
   * @example
   *     <pre>
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
  default boolean setText(String text) {
    return setText(text, getWaitSec());
  }

  /**
   * Sets text in an input field with custom timeout. Clears existing content first using
   * platform-appropriate key combination.
   *
   * @param text the text to enter in the field
   * @param waitSec maximum time in seconds to wait for element to be present and interactable
   * @example
   *     <pre>
   * // Set text with extended timeout for slow-loading fields
   * dynamicTextField.setText("Updated content", 20);
   * </pre>
   */
  default boolean setText(String text, int waitSec) {
    return waitUntil("Set Text", waitSec, engine -> engine.setText(getLocator(), text));
  }

  /**
   * Sets text in an input field and presses Enter key. Useful for search boxes and forms that
   * submit on Enter.
   *
   * @param text the text to enter before pressing Enter
   * @example
   *     <pre>
   * // Search and submit with Enter key
   * searchBox.setTextAndEnter("automation testing");
   *
   * // Login with Enter key
   * passwordField.setTextAndEnter("mySecretPassword");
   * </pre>
   */
  default boolean setTextAndEnter(String text) {
    return setTextAndEnter(text, getWaitSec());
  }

  /**
   * Sets text in an input field and presses Enter key with custom timeout.
   *
   * @param text the text to enter before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Search with custom timeout
   * slowSearchBox.setTextAndEnter("complex query", 15);
   * </pre>
   */
  default boolean setTextAndEnter(String text, int waitSec) {
    return setTextAnd(text, "Enter", waitSec);
  }

  /**
   * Sets text in an input field and presses Tab key. Useful for moving to the next field in forms.
   *
   * @param text the text to enter before pressing Tab
   * @example
   *     <pre>
   * // Fill field and move to next
   * firstNameField.setTextAndTab("John");
   * // Focus automatically moves to next field
   *
   * // Fill form fields in sequence
   * emailField.setTextAndTab("john@example.com");
   * </pre>
   */
  default boolean setTextAndTab(String text) {
    return setTextAndTab(text, getWaitSec());
  }

  /**
   * Sets text in an input field and presses Tab key with custom timeout.
   *
   * @param text the text to enter before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Fill field and tab with timeout
   * slowLoadingField.setTextAndTab("data", 12);
   * </pre>
   */
  default boolean setTextAndTab(String text, int waitSec) {
    return setTextAnd(text, "Tab", waitSec);
  }

  /**
   * Sets text in an input field and presses a specified key.
   *
   * @param text the text to enter
   * @param keys the key to press after entering text
   * @example
   *     <pre>
   * // Set text and press Escape key
   * editField.setTextAnd("cancelled text", "Escape");
   *
   * // Set text and press F1 for help
   * commandField.setTextAnd("help command", "F1");
   * </pre>
   */
  default boolean setTextAnd(String text, String keys) {
    return setTextAnd(text, keys, getWaitSec());
  }

  /**
   * Sets text in an input field and presses a specified key with custom timeout.
   *
   * @param text the text to enter
   * @param key the key to press after entering text
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Set text and press custom key with timeout
   * specialField.setTextAnd("special input", "F5", 10);
   * </pre>
   */
  default boolean setTextAnd(String text, String key, int waitSec) {
    return waitUntil(
        "Set Text And " + key,
        waitSec,
        engine -> engine.setText(getLocator(), text) && engine.press(getLocator(), key));
  }

  /**
   * Sets the value attribute of an element using a formatted Date object. Uses JavaScript to
   * directly set the value attribute.
   *
   * @param date the Date object to format and set
   * @param format the date format string
   * @example
   *     <pre>
   * // Set date input value directly
   * Date appointmentDate = new Date();
   * dateInput.setValue(appointmentDate, "yyyy-MM-dd");
   * </pre>
   */
  default boolean setValue(Date date, String format) {
    return setValue(date, format, getWaitSec());
  }

  /**
   * Sets the value attribute of an element using a formatted Date object with custom timeout.
   *
   * @param date the Date object to format and set
   * @param format the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Set date value with timeout
   * startDateInput.setValue(startDate, "MM/dd/yyyy", 8);
   * </pre>
   */
  default boolean setValue(Date date, String format, int waitSec) {
    return setValue(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Sets the value attribute of an element directly using JavaScript. This method bypasses normal
   * input events and directly sets the value.
   *
   * @param text the text value to set
   * @example
   *     <pre>
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
  default boolean setValue(String text) {
    return setValue(text, getWaitSec());
  }

  /**
   * Sets the value attribute of an element directly using JavaScript with custom timeout.
   *
   * @param text the text value to set
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Set value with timeout for dynamically created fields
   * dynamicField.setValue("computed value", 12);
   * </pre>
   */
  default boolean setValue(String text, int waitSec) {
    return setAttribute("value", text, waitSec);
  }

  /**
   * Sets the value attribute and presses Enter key. Combines setValue with Enter key press for form
   * submission.
   *
   * @param text the text value to set before pressing Enter
   * @example
   *     <pre>
   * // Set value and submit form
   * amountField.setValueAndEnter("150.00");
   *
   * // Set search term and execute search
   * quickSearchField.setValueAndEnter("products");
   * </pre>
   */
  default boolean setValueAndEnter(String text) {
    return setValueAndEnter(text, getWaitSec());
  }

  /**
   * Sets the value attribute and presses Enter key with custom timeout.
   *
   * @param text the text value to set before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Set value and submit with timeout
   * complexFormField.setValueAndEnter("complex data", 15);
   * </pre>
   */
  default boolean setValueAndEnter(String text, int waitSec) {
    return setValue(text, waitSec) && press(CKeys.ENTER);
  }

  /**
   * Sets the value attribute and presses Tab key. Useful for setting values and moving to next
   * field in forms.
   *
   * @param text the text value to set before pressing Tab
   * @example
   *     <pre>
   * // Set value and move to next field
   * priceField.setValueAndTab("29.99");
   * // Focus moves to next form field
   * </pre>
   */
  default boolean setValueAndTab(String text) {
    return setValueAndTab(text, getWaitSec());
  }

  /**
   * Sets the value attribute and presses Tab key with custom timeout.
   *
   * @param text the text value to set before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Set value and tab with timeout
   * asyncField.setValueAndTab("calculated value", 10);
   * </pre>
   */
  default boolean setValueAndTab(String text, int waitSec) {
    return setValue(text, waitSec) && press(CKeys.TAB);
  }

  /**
   * Clears the content of an input field using platform-appropriate key combination. Uses
   * Ctrl+A+Delete on Windows/Linux or Cmd+A+Delete on Mac.
   *
   * @example
   *     <pre>
   * // Clear text field
   * messageField.clear();
   *
   * // Clear search box
   * searchInput.clear();
   * </pre>
   */
  default boolean clear() {
    return clear(getWaitSec());
  }

  /**
   * Clears the content of an input field with custom timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Clear field with timeout
   * slowLoadingInput.clear(15);
   * </pre>
   */
  default boolean clear(int waitSec) {
    return waitUntil("Clear", waitSec, engine -> engine.clearElement(getLocator()));
  }

  /**
   * Types text character by character after formatting a Date object. Clears existing content
   * first, then types each character individually.
   *
   * @param date the Date object to format and type
   * @param format the date format string
   * @example
   *     <pre>
   * // Type date slowly for date pickers that need gradual input
   * Date selectedDate = new Date();
   * datePicker.type(selectedDate, "MM/dd/yyyy");
   * </pre>
   */
  default boolean type(Date date, String format) {
    return type(date, format, getWaitSec());
  }

  /**
   * Types text character by character after formatting a Date object with custom timeout.
   *
   * @param date the Date object to format and type
   * @param format the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Type date with timeout
   * endDateInput.type(endDate, "yyyy-MM-dd", 12);
   * </pre>
   */
  default boolean type(Date date, String format, int waitSec) {
    return type(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Types text character by character in an input field. Clears existing content first, then types
   * each character individually. Useful for fields that need gradual input or have real-time
   * validation.
   *
   * @param text the text to type character by character
   * @example
   *     <pre>
   * // Type text slowly for autocomplete fields
   * autocompleteField.type("JavaScript");
   *
   * // Type for fields with character-by-character validation
   * phoneField.type("555-123-4567");
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_type",
      title = "Type Text in Element",
      description = "Types text character by character in the web element input field")
  default boolean type(
      @CMcpToolParam(name = "text", description = "The text to type") String text) {
    return type(text, getWaitSec());
  }

  /**
   * Types text character by character with custom timeout.
   *
   * @param text the text to type character by character
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Type with custom timeout
   * slowField.type("sensitive data", 20);
   * </pre>
   */
  default boolean type(String text, int waitSec) {
    return type(text, waitSec, 0L);
  }

  /**
   * Types text character by character with a delay between each character. Useful for simulating
   * human-like typing or for fields sensitive to typing speed.
   *
   * @param text the text to type
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Type slowly with 100ms delay between characters
   * secureField.type("password123", 100);
   *
   * // Type very slowly for sensitive fields
   * tokenField.type("abc123def456", 500);
   * </pre>
   */
  default boolean type(String text, long intervalInMilliSeconds) {
    return type(text, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with custom timeout and delay between characters.
   *
   * @param text the text to type
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Type with both custom timeout and character delay
   * specialInput.type("complex input", 15, 200);
   * </pre>
   */
  default boolean type(String text, int waitSec, long intervalInMilliSeconds) {
    return waitUntil(
        "Type", waitSec, engine -> engine.sendKeys(getLocator(), text, intervalInMilliSeconds));
  }

  /**
   * Types text character by character and presses Tab key. Combines typing with Tab key press for
   * moving to next field.
   *
   * @param text the text to type before pressing Tab
   * @example
   *     <pre>
   * // Type and move to next field
   * accountField.typeAndTab("12345");
   * // Focus automatically moves to next form field
   * </pre>
   */
  default boolean typeAndTab(String text) {
    return typeAndTab(text, getWaitSec());
  }

  /**
   * Types text character by character and presses Tab key with custom timeout.
   *
   * @param text the text to type before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Type and tab with timeout
   * dynamicField.typeAndTab("data", 10);
   * </pre>
   */
  default boolean typeAndTab(String text, int waitSec) {
    return typeAndTab(text, waitSec, 0L);
  }

  /**
   * Types text character by character with delay and presses Tab key.
   *
   * @param text the text to type before pressing Tab
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Type slowly and move to next field
   * sensitiveField.typeAndTab("secure data", 150);
   * </pre>
   */
  default boolean typeAndTab(String text, long intervalInMilliSeconds) {
    return typeAndTab(text, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with delay and presses Tab key with custom timeout.
   *
   * @param text the text to type before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Type with all custom settings and tab
   * complexField.typeAndTab("complex input", 15, 100);
   * </pre>
   */
  default boolean typeAndTab(String text, int waitSec, long intervalInMilliSeconds) {
    return typeAnd(text, "Tab", waitSec, intervalInMilliSeconds);
  }

  /**
   * Types text character by character and presses Enter key. Useful for form submission or search
   * execution after typing.
   *
   * @param text the text to type before pressing Enter
   * @example
   *     <pre>
   * // Type search query and execute search
   * searchField.typeAndEnter("selenium automation");
   *
   * // Type password and submit form
   * passwordField.typeAndEnter("mySecretPassword");
   * </pre>
   */
  default boolean typeAndEnter(String text) {
    return typeAndEnter(text, getWaitSec());
  }

  /**
   * Types text character by character and presses Enter key with custom timeout.
   *
   * @param text the text to type before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Type and submit with timeout
   * slowSearchField.typeAndEnter("complex query", 20);
   * </pre>
   */
  default boolean typeAndEnter(String text, int waitSec) {
    return typeAndEnter(text, waitSec, 0L);
  }

  /**
   * Types text character by character with delay and presses Enter key.
   *
   * @param text the text to type before pressing Enter
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Type slowly and submit
   * commandField.typeAndEnter("important command", 200);
   * </pre>
   */
  default boolean typeAndEnter(String text, long intervalInMilliSeconds) {
    return typeAndEnter(text, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with delay and presses Enter key with custom timeout.
   *
   * @param text the text to type before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Type with all custom settings and submit
   * advancedField.typeAndEnter("advanced input", 18, 75);
   * </pre>
   */
  default boolean typeAndEnter(String text, int waitSec, long intervalInMilliSeconds) {
    return typeAnd(text, "Enter", waitSec, intervalInMilliSeconds);
  }

  /**
   * Types text character by character and presses a specified key.
   *
   * @param text the text to type
   * @param keys the key to press after typing
   * @example
   *     <pre>
   * // Type text and press Escape
   * editField.typeAnd("cancelled text", "Escape");
   *
   * // Type text and press F1 for help
   * helpField.typeAnd("help topic", "F1");
   * </pre>
   */
  default boolean typeAnd(String text, String keys) {
    return typeAnd(text, keys, getWaitSec());
  }

  /**
   * Types text character by character and presses a specified key with custom timeout.
   *
   * @param text the text to type
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example
   *     <pre>
   * // Type and press custom key with timeout
   * specialField.typeAnd("special input", "F5", 12);
   * </pre>
   */
  default boolean typeAnd(String text, String key, int waitSec) {
    return typeAnd(text, key, waitSec, 0L);
  }

  /**
   * Types text character by character with delay and presses a specified key.
   *
   * @param text the text to type
   * @param keys the key to press after typing
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Type slowly and press custom key
   * scriptField.typeAnd("console.log('test');", "F12", 50);
   * </pre>
   */
  default boolean typeAnd(String text, String keys, long intervalInMilliSeconds) {
    return typeAnd(text, keys, getWaitSec(), intervalInMilliSeconds);
  }

  /**
   * Types text character by character with delay and presses a specified key with custom timeout.
   * This is the most comprehensive typing method with full control over all parameters.
   *
   * @param text the text to type
   * @param key the key to press after typing
   * @param waitSec maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example
   *     <pre>
   * // Full control typing with custom key
   * complexField.typeAnd("complex data entry", "F10", 20, 100);
   *
   * // Type code slowly and press compile key
   * codeEditor.typeAnd("function test() { return true; }", "F9", "15", "25");
   * </pre>
   */
  default boolean typeAnd(String text, String key, int waitSec, long intervalInMilliSeconds) {
    return waitUntil(
        "Type And",
        waitSec,
        engine ->
            engine.sendKeys(getLocator(), text, intervalInMilliSeconds)
                && engine.press(getLocator(), key));
  }

  /**
   * Gets the visible text content of the element using the default timeout.
   *
   * @return the text content of the element, or empty string if not found
   * @example
   *     <pre>
   * CWebElement title = driver.$(By.tagName("h1"));
   * String titleText = title.getText();
   * System.out.println("Page title: " + titleText);
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_get_text",
      title = "Get Element Text",
      description = "Gets the visible text content of the web element")
  default String getText() {
    return getText(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the visible text content of the element within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the text content of the element, or empty string if not found
   * @example
   *     <pre>
   * // Wait for dynamic content to load
   * CWebElement statusMessage = driver.$(By.id("status"));
   * String status = statusMessage.getText(10);
   *
   * if (status.contains("Success")) {
   *     System.out.println("Operation completed successfully");
   * }
   * </pre>
   */
  default String getText(int waitSec) {
    return waitUntil(
        "Get Text",
        waitSec,
        CStringUtil.EMPTY,
        engine -> {
          String text = engine.getElementText(getLocator());
          return text == null ? CStringUtil.EMPTY : text;
        });
  }

  /**
   * Parses the element's text content as a date using the specified format.
   *
   * @param dateFormat the date format pattern (e.g., "yyyy-MM-dd", "MM/dd/yyyy")
   * @return the parsed Date object, or null if text is blank or cannot be parsed
   * @example
   *     <pre>
   * CWebElement dateField = driver.$(By.id("created-date"));
   * Date createdDate = dateField.getDate("yyyy-MM-dd");
   *
   * if (createdDate != null) {
   *     System.out.println("Created: " + createdDate);
   * }
   * </pre>
   */
  default Date getDate(String dateFormat) {
    String text = getText();
    return StringUtils.isBlank(text) ? null : CDate.of(text, dateFormat);
  }

  /**
   * Parses the element's text content as a date using the specified format and timeout.
   *
   * @param dateFormat the date format pattern (e.g., "yyyy-MM-dd", "MM/dd/yyyy")
   * @param waitSec the maximum time to wait in seconds
   * @return the parsed Date object, or null if text is blank or cannot be parsed
   * @example
   *     <pre>
   * // Wait for timestamp to appear and parse it
   * CWebElement timestamp = driver.$(By.className("last-updated"));
   * Date lastUpdated = timestamp.getDate("MM/dd/yyyy HH:mm:ss", 5);
   *
   * if (lastUpdated != null && lastUpdated.after(yesterday)) {
   *     System.out.println("Content is recent");
   * }
   * </pre>
   */
  default Date getDate(String dateFormat, int waitSec) {
    String text = getText(waitSec);
    return StringUtils.isBlank(text) ? null : CDate.of(text, dateFormat);
  }

  /**
   * Gets the value attribute of the element using the default timeout. Commonly used for input
   * fields, textareas, and select elements.
   *
   * @return the value attribute of the element, or empty string if not found
   * @example
   *     <pre>
   * CWebElement emailInput = driver.$(By.id("email"));
   * String currentEmail = emailInput.getValue();
   * System.out.println("Current email: " + currentEmail);
   * </pre>
   */
  default String getValue() {
    return getValue(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the value attribute of the element within the specified timeout. Commonly used for input
   * fields, textareas, and select elements.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the value attribute of the element, or empty string if not found
   * @example
   *     <pre>
   * CWebElement priceField = driver.$(By.id("calculated-price"));
   * // Wait for price calculation to complete
   * String price = priceField.getValue(5);
   *
   * if (!price.isEmpty()) {
   *     System.out.println("Calculated price: $" + price);
   * }
   * </pre>
   */
  default String getValue(int waitSec) {
    return waitUntil(
        "Get Value",
        waitSec,
        CStringUtil.EMPTY,
        engine -> {
          String v = engine.getElementValue(getLocator());
          return v == null ? CStringUtil.EMPTY : v;
        });
  }

  /**
   * Gets the innerHTML content of the element using the default timeout. Returns the HTML content
   * inside the element.
   *
   * @return the innerHTML content of the element, or empty string if not found
   * @example
   *     <pre>
   * CWebElement contentDiv = driver.$(By.id("rich-content"));
   * String htmlContent = contentDiv.getInnerHTML();
   * System.out.println("Content HTML: " + htmlContent);
   * </pre>
   */
  default String getInnerHTML() {
    return getInnerHTML(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the innerHTML content of the element within the specified timeout. Returns the HTML
   * content inside the element using JavaScript execution.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the innerHTML content of the element, or empty string if not found
   * @example
   *     <pre>
   * // Wait for dynamic content to be injected
   * CWebElement dynamicContainer = driver.$(By.id("dynamic-content"));
   * String html = dynamicContainer.getInnerHTML(8);
   *
   * if (html.contains("&lt;table&gt;")) {
   *     System.out.println("Table content loaded successfully");
   * }
   * </pre>
   */
  default String getInnerHTML(int waitSec) {
    return waitUntil(
        "Get Inner HTML",
        waitSec,
        CStringUtil.EMPTY,
        engine -> {
          Object res = engine.getElementInnerHtml(getLocator());
          return res == null ? CStringUtil.EMPTY : res.toString();
        });
  }

  /**
   * Gets the computed CSS property value of the element using the default timeout.
   *
   * @param cssKey the CSS property name (e.g., "color", "font-size", "display")
   * @return the computed CSS value, or empty string if not found
   * @example
   *     <pre>
   * CWebElement errorMessage = driver.$(By.className("error"));
   * String color = errorMessage.getCss("color");
   *
   * if (color.contains("rgb(255, 0, 0)")) { // Red color
   *     System.out.println("Error message is displayed in red");
   * }
   * </pre>
   */
  default String getCss(final String cssKey) {
    return getCss(cssKey, DEFAULT_TIMEOUT);
  }

  /**
   * Gets the computed CSS property value of the element within the specified timeout.
   *
   * @param cssKey the CSS property name (e.g., "color", "font-size", "display")
   * @param waitSec the maximum time to wait in seconds
   * @return the computed CSS value, or empty string if not found
   * @example
   *     <pre>
   * CWebElement loadingSpinner = driver.$(By.className("spinner"));
   *
   * // Wait for spinner to appear and check its display property
   * String display = loadingSpinner.getCss("display", 3);
   * if ("block".equals(display)) {
   *     System.out.println("Loading spinner is visible");
   * }
   * </pre>
   */
  default String getCss(final String cssKey, int waitSec) {
    return waitUntil(
        "Get Css",
        waitSec,
        CStringUtil.EMPTY,
        engine -> {
          String res = engine.getElementCssValue(getLocator(), cssKey);
          return res == null ? CStringUtil.EMPTY : res;
        });
  }

  /**
   * Gets the value of the specified attribute of the element using the default timeout.
   *
   * @param attribute the attribute name (e.g., "id", "class", "href", "data-value")
   * @return the attribute value, or empty string if attribute doesn't exist
   * @example
   *     <pre>
   * CWebElement link = driver.$(By.tagName("a"));
   * String href = link.getAttribute("href");
   * String target = link.getAttribute("target");
   *
   * System.out.println("Link URL: " + href);
   * System.out.println("Target: " + target);
   * </pre>
   */
  default String getAttribute(final String attribute) {
    return getAttribute(attribute, DEFAULT_TIMEOUT);
  }

  /**
   * Gets the value of the specified attribute of the element within the specified timeout.
   *
   * @param attribute the attribute name (e.g., "id", "class", "href", "data-value")
   * @param waitSec the maximum time to wait in seconds
   * @return the attribute value, or empty string if attribute doesn't exist
   * @example
   *     <pre>
   * CWebElement statusIcon = driver.$(By.id("status-icon"));
   *
   * // Wait for status to be updated and check data attribute
   * String status = statusIcon.getAttribute("data-status", 5);
   *
   * switch (status) {
   *     case "success":
   *         System.out.println("Operation successful");
   *         break;
   *     case "error":
   *         System.out.println("Operation failed");
   *         break;
   * }
   * </pre>
   */
  default String getAttribute(final String attribute, int waitSec) {
    return waitUntil(
        "Get Attribute",
        waitSec,
        CStringUtil.EMPTY,
        engine -> {
          String v = engine.getElementAttribute(getLocator(), attribute);
          return v == null ? CStringUtil.EMPTY : v;
        });
  }

  /**
   * Gets the ARIA role of the element using the default timeout.
   *
   * @return the ARIA role value, or empty string if not defined
   * @example
   *     <pre>
   * CWebElement navigation = driver.$(By.tagName("nav"));
   * String role = navigation.getAriaRole();
   *
   * if ("navigation".equals(role)) {
   *     System.out.println("Element has proper navigation role");
   * }
   * </pre>
   */
  default String getAriaRole() {
    return getAriaRole(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the ARIA role of the element within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the ARIA role value, or empty string if not defined
   * @example
   *     <pre>
   * // Wait for dynamic content to load and check accessibility role
   * CWebElement dialog = driver.$(By.className("modal"));
   * String role = dialog.getAriaRole(3);
   *
   * if ("dialog".equals(role)) {
   *     System.out.println("Modal has proper dialog role for accessibility");
   * }
   * </pre>
   */
  default String getAriaRole(int waitSec) {
    return waitUntil(
        "Get AriaRole",
        waitSec,
        CStringUtil.EMPTY,
        engine -> {
          String v = engine.getElementAttribute(getLocator(), "role");
          return v == null ? CStringUtil.EMPTY : v;
        });
  }

  /**
   * Captures a screenshot of the element using the default timeout.
   *
   * @return BufferedImage of the element screenshot, or null if capture fails
   * @example
   *     <pre>
   * CWebElement chart = driver.$(By.id("sales-chart"));
   * BufferedImage screenshot = chart.getScreenShot();
   *
   * if (screenshot != null) {
   *     // Save screenshot to file
   *     ImageIO.write(screenshot, "PNG", new File("chart.png"));
   * }
   * </pre>
   */
  default BufferedImage getScreenShot() {
    return getScreenShot(DEFAULT_TIMEOUT);
  }

  /**
   * Captures a screenshot of the element within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return BufferedImage of the element screenshot, or null if capture fails
   * @example
   *     <pre>
   * // Wait for graph to render and capture screenshot
   * CWebElement graph = driver.$(By.className("data-visualization"));
   * BufferedImage image = graph.getScreenShot(10);
   *
   * if (image != null) {
   *     int width = image.getWidth();
   *     int height = image.getHeight();
   *     System.out.println("Screenshot captured: " + width + "x" + height);
   * }
   * </pre>
   */
  default BufferedImage getScreenShot(int waitSec) {
    return waitUntil(
        "Get ScreenShot",
        waitSec,
        null,
        engine -> {
          if (!engine.isElementPresent(getLocator())) return null;
          try {
            byte[] bytes = engine.screenshotElement(getLocator());
            return CImageUtil.readImageOrNull(bytes);
          } catch (Throwable t) {
            return null;
          }
        });
  }
}

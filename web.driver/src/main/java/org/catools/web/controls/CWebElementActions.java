package org.catools.web.controls;

import org.catools.common.collections.CList;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.date.CDate;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.utils.CFileUtil;
import org.catools.common.utils.CRetry;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;
import org.catools.web.config.CBrowserConfigs;
import org.catools.web.config.CGridConfigs;
import org.catools.web.drivers.CDriver;
import org.catools.web.utils.CGridUtil;

import java.io.File;
import java.util.Date;
import java.util.function.Function;
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
   * @example <pre>
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
   * @example <pre>
   * // Move to element and wait up to 10 seconds for it to be present
   * element.moveTo(10);
   * </pre>
   */
  default boolean moveTo(int waitSec) {
    return moveTo(0, 0, waitSec);
  }

  /**
   * Moves the mouse cursor to a specific offset from the element's center.
   * Uses default timeout for waiting until element is present.
   *
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @example <pre>
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
   * @example <pre>
   * // Move to offset position and wait up to 15 seconds
   * element.moveTo(30, -10, 15);
   * </pre>
   */
  default boolean moveTo(int xOffset, int yOffset, int waitSec) {
    return isPresent(waitSec) && getDriver().moveToElement(getLocator(), xOffset, yOffset, 0);
  }

  /**
   * Performs a drop action at a specific offset from the element's center.
   * Uses default timeout for waiting until element is present.
   *
   * @param xOffset horizontal offset in pixels from element center
   * @param yOffset vertical offset in pixels from element center
   * @example <pre>
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
   * @example <pre>
   * // Drop at offset position and wait up to 8 seconds
   * draggableElement.dropTo(75, -25, 8);
   * </pre>
   */
  default boolean dropTo(int xOffset, int yOffset, int waitSec) {
    return isPresent(waitSec) && getDriver().dropTo(getLocator(), xOffset, yOffset, 0);
  }

  /**
   * Drags the element from its center to a target offset position.
   * Uses default timeout for waiting until element is present.
   *
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @example <pre>
   * // Drag element from center to position (200, 100)
   * slider.dragAndDropTo(200, 100);
   * </pre>
   */
  default boolean dragAndDropTo(int xOffset2, int yOffset2) {
    return dragAndDropTo(0, 0, xOffset2, yOffset2);
  }

  /**
   * Drags the element from its center to a target offset position with custom wait time.
   *
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @param waitSec  maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Drag to target position and wait up to 12 seconds
   * draggableItem.dragAndDropTo(150, -50, 12);
   * </pre>
   */
  default boolean dragAndDropTo(int xOffset2, int yOffset2, int waitSec) {
    return dragAndDropTo(0, 0, xOffset2, yOffset2, waitSec);
  }

  /**
   * Drags the element from a source offset to a target offset position.
   * Uses default timeout for waiting until element is present.
   *
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @param xOffset2 target horizontal offset in pixels
   * @param yOffset2 target vertical offset in pixels
   * @example <pre>
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
   * @param waitSec  maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Drag from source to target with 15 second timeout
   * dragHandle.dragAndDropTo(0, 0, 200, 150, 15);
   * </pre>
   */
  default boolean dragAndDropTo(int xOffset1, int yOffset1, int xOffset2, int yOffset2, int waitSec) {
    return isPresent(waitSec) && getDriver().dragAndDropTo(getLocator(), xOffset1, yOffset1, xOffset2, yOffset2, 0);
  }

  /**
   * Drags the element to another target element.
   * Uses default timeout for waiting until element is present.
   *
   * @param target the String locator of the target element
   * @example <pre>
   * // Drag item to a drop zone
   * draggableItem.dragAndDropTo(String.id("dropZone"));
   * </pre>
   */
  default boolean dragAndDropTo(String target) {
    return dragAndDropTo(target, 0);
  }

  /**
   * Drags the element to another target element with custom wait time.
   *
   * @param target  the String locator of the target element
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Drag to target element with 20 second timeout
   * fileItem.dragAndDropTo(String.className("upload-area"), 20);
   * </pre>
   */
  default boolean dragAndDropTo(String target, int waitSec) {
    return dragAndDropTo(target, 0, 0, waitSec);
  }

  /**
   * Drags the element from a specific offset to a target element.
   * Uses default timeout for waiting until element is present.
   *
   * @param target   the String locator of the target element
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @example <pre>
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
   * @param target   the String locator of the target element
   * @param xOffset1 source horizontal offset in pixels from element center
   * @param yOffset1 source vertical offset in pixels from element center
   * @param waitSec  maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Drag from offset to target with timeout
   * menuItem.dragAndDropTo(String.className("menu-container"), 5, 0, 10);
   * </pre>
   */
  default boolean dragAndDropTo(String target, int xOffset1, int yOffset1, int waitSec) {
    return isPresent(waitSec) && getDriver().dragAndDropTo(getLocator(), target, xOffset1, yOffset1, 0);
  }

  /**
   * Executes JavaScript code on the element using default timeout.
   *
   * @param <R>    the return type of the script execution
   * @param script the JavaScript code to execute
   * @return the result of script execution
   * @example <pre>
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
   * @param <R>     the return type of the script execution
   * @param script  the JavaScript code to execute
   * @param waitSec maximum time in seconds to wait for element to be present
   * @return the result of script execution
   * @example <pre>
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
   * Sends keys to the element (types text or special keys).
   *
   * @param keys the keys to send (text or special keys like "Enter")
   * @example <pre>
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
      description = "Sends keys to the web element (types text or special keys)"
  )
  default boolean sendKeys(
      @CMcpToolParam(name = "keys", description = "The keys to send to the element") String keys) {
    return getDriver().sendKeys(getLocator(), getWaitSec(), keys);
  }

  /**
   * Press Special keys to the element (types text or special keys).
   *
   * @param key the keys to send special keys like "Enter"
   * @example <pre>
   * // Type text in input field
   * inputField.pressKey("Enter");
   *
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_press_key",
      title = "Press Special Key On Element",
      description = "Press special key on the web element (i.e. Enter, Tab, etc.)"
  )
  default boolean pressKey(
      @CMcpToolParam(name = "key", description = "The keys to send to the element") String key) {
    return getDriver().pressKey(getLocator(), key, getWaitSec());
  }

  /**
   * Performs a mouse click on the element using default timeout.
   * This method first moves to the element, then performs the click.
   *
   * @example <pre>
   * // Click on a button
   * submitButton.mouseClick();
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_mouse_click",
      title = "Mouse Click Element",
      description = "Performs a mouse click on the web element"
  )
  default boolean mouseClick() {
    return mouseClick(getWaitSec());
  }

  /**
   * Performs a mouse click on the element with custom timeout.
   * This method first moves to the element, then performs the click.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Click with custom timeout
   * dynamicButton.mouseClick(20);
   * </pre>
   */
  default boolean mouseClick(int waitSec) {
    return moveTo(waitSec) && getDriver().mouseClick(getLocator(), waitSec);
  }

  /**
   * Performs a mouse double-click on the element using default timeout.
   * This method first moves to the element, then performs the double-click.
   *
   * @example <pre>
   * // Double-click on a file icon
   * fileIcon.mouseDoubleClick();
   * </pre>
   */
  default boolean mouseDoubleClick() {
    return mouseDoubleClick(getWaitSec());
  }

  /**
   * Performs a mouse double-click on the element with custom timeout.
   * This method first moves to the element, then performs the double-click.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Double-click with custom timeout
   * editableCell.mouseDoubleClick(15);
   * </pre>
   */
  default boolean mouseDoubleClick(int waitSec) {
    return moveTo(waitSec) && getDriver().mouseDoubleClick(getLocator(), waitSec);
  }

  /**
   * Scrolls the element into view within the browser window.
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_scroll_into_view",
      title = "Scroll Element Into View",
      description = "Scrolls the element into view within the browser window"
  )
  default boolean scrollIntoView() {
    return scrollIntoView(getWaitSec());
  }

  /**
   * Scrolls the element into view within the browser window with custom timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Scroll with custom timeout
   * lazyLoadedElement.scrollIntoView(true, 30);
   * </pre>
   */
  default boolean scrollIntoView(int waitSec) {
    return getDriver().scrollIntoView(getLocator(), waitSec);
  }

  /**
   * Scrolls the element horizontally to the left by default amount (900 pixels).
   *
   * @example <pre>
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
   * @param waitSec    maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Scroll left by 500 pixels with 10 second timeout
   * carousel.scrollLeft(500, 10);
   * </pre>
   */
  default boolean scrollLeft(int scrollSize, int waitSec) {
    return getDriver().scrollLeft(getLocator(), scrollSize, waitSec);
  }

  /**
   * Scrolls the element horizontally to the right by default amount (900 pixels).
   *
   * @example <pre>
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
   * @param waitSec    maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Scroll right by 300 pixels with 8 second timeout
   * imageGallery.scrollRight(300, 8);
   * </pre>
   */
  default boolean scrollRight(int scrollSize, int waitSec) {
    return getDriver().scrollRight(getLocator(), scrollSize, waitSec);
  }

  /**
   * Sets an attribute value on the element using JavaScript using default timeout.
   *
   * @param attributeName the name of the attribute to set
   * @param value         the value to set for the attribute
   * @example <pre>
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
   * @param value         the value to set for the attribute
   * @param waitSec       maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Set disabled attribute with timeout
   * dynamicButton.setAttribute("disabled", "true", 10);
   * </pre>
   */
  default boolean setAttribute(String attributeName, String value, int waitSec) {
    return isPresent(waitSec) && executeScript("arguments[0][\"%s\"]=\"%s\";".formatted(attributeName, value)) != null;
  }

  /**
   * Removes an attribute from the element using JavaScript and default timeout.
   *
   * @param attributeName the name of the attribute to remove
   * @example <pre>
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
   * @param waitSec       maximum time in seconds to wait for element to be present
   * @example <pre>
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
   * @example <pre>
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
   * @param value   true to select the element, false to deselect
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Select checkbox with custom timeout
   * dynamicCheckbox.set(true, 20);
   * </pre>
   */
  default boolean set(boolean value, int waitSec) {
    if (value)
      return select(waitSec);
    else
      return deselect(waitSec);
  }

  /**
   * Selects the element (for checkboxes, radio buttons) if not already selected.
   * Uses default timeout.
   *
   * @example <pre>
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
   * Selects the element (for checkboxes, radio buttons) if not already selected.
   * Uses custom timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Select with custom timeout
   * termsCheckbox.select(15);
   * </pre>
   */
  default boolean select(int waitSec) {
    return getDriver()
        .waitUntil(
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
   * Deselects the element (for checkboxes) if currently selected.
   * Uses default timeout.
   *
   * @example <pre>
   * // Deselect a checkbox (only clicks if currently selected)
   * newsletterCheckbox.deselect();
   * </pre>
   */
  default boolean deselect() {
    return deselect(getWaitSec());
  }

  /**
   * Deselects the element (for checkboxes) if currently selected.
   * Uses custom timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Deselect with custom timeout
   * optionalCheckbox.deselect(8);
   * </pre>
   */
  default boolean deselect(int waitSec) {
    return getDriver()
        .waitUntil(
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
   * Clicks on the element using default timeout.
   * Falls back to JavaScript click if normal click fails.
   *
   * @example <pre>
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
      description = "Clicks on the web element with fallback to JavaScript click if needed"
  )
  default boolean click() {
    return click(getWaitSec());
  }

  /**
   * Clicks on the element with custom timeout.
   * Falls back to JavaScript click if normal click fails.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Click with custom timeout
   * dynamicButton.click(20);
   * </pre>
   */
  default boolean click(int waitSec) {
    return getDriver().waitUntil("Click", waitSec, engine -> engine.click(getLocator()));
  }

  /**
   * Clicks on the element and waits for a post-condition to be satisfied.
   * Retries the click if the post-condition is not met.
   *
   * @param <R>           the return type of the post-condition function
   * @param postCondition function to verify after clicking
   * @return the result of the post-condition function
   * @example <pre>
   * // Click and wait for page to load
   * String newUrl = nextPageButton.click(driver -> driver.getCurrentUrl());
   *
   * // Click and wait for element to appear
   * Boolean elementVisible = submitButton.click(driver ->
   *     driver.$(String.id("success-message")).isDisplayed());
   * </pre>
   */
  default <R> R click(com.google.common.base.Function<DR, R> postCondition) {
    return click(2, 2000, postCondition);
  }

  /**
   * Clicks on the element and waits for a post-condition with custom retry settings.
   *
   * @param <R>           the return type of the post-condition function
   * @param retryCount    number of times to retry the click if post-condition fails
   * @param interval      time in milliseconds between retries
   * @param postCondition function to verify after clicking
   * @return the result of the post-condition function
   * @example <pre>
   * // Click with custom retry settings
   * Boolean success = unreliableButton.click(5, 1000, driver ->
   *     driver.$(String.id("result")).getText().equals("Success"));
   * </pre>
   */
  default <R> R click(int retryCount, int interval, Function<DR, R> postCondition) {
    return CRetry.retry(idx -> {
      click();
      return postCondition.apply(getDriver());
    }, retryCount, interval);
  }


  /**
   * Opens the URL specified in the element's href attribute in the current tab.
   * Uses default timeout.
   *
   * @example <pre>
   * // Navigate to the link's URL
   * externalLink.openHref();
   * </pre>
   */
  default boolean openHref() {
    return openHref(getWaitSec());
  }

  /**
   * Opens the URL specified in the element's href attribute with custom timeout.
   *
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Navigate to link with custom timeout
   * dynamicLink.openHref(10);
   * </pre>
   */
  default boolean openHref(int waitSec) {
    getDriver().open(getAttribute("href", waitSec));
    return true;
  }

  /**
   * Opens the URL specified in the element's href attribute and waits for a post-condition.
   *
   * @param <R>           the return type of the post-condition function
   * @param postCondition function to verify after navigation
   * @return the result of the post-condition function
   * @example <pre>
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
   * @param <R>           the return type of the post-condition function
   * @param retryCount    number of times to retry if post-condition fails
   * @param interval      time in milliseconds between retries
   * @param postCondition function to verify after navigation
   * @return the result of the post-condition function
   * @example <pre>
   * // Navigate with retry logic
   * Boolean pageLoaded = slowLink.openHref(3, 5000, driver ->
   *     driver.$(String.id("main-content")).isDisplayed());
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
   * Downloads a file and handles expected alert dialog.
   *
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename     the expected filename of the downloaded file
   * @param renameTo     the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example <pre>
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
   * @param clickWait    time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename     the expected filename of the downloaded file
   * @param renameTo     the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example <pre>
   * // Full control over download process
   * CFile invoice = downloadInvoice.downloadFile(10, 90, "invoice.pdf", "invoice-2023.pdf", true);
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, String filename, String renameTo) {
    click(clickWait);
    CFile downloadFolder = CBrowserConfigs.getDownloadFolder(getDriver().getSessionId());
    CFile downloadedFile = new CFile(downloadFolder, filename);
    downloadedFile.verifyExists(downloadWait, 500, "File downloaded! file:" + downloadedFile);
    return downloadedFile.moveTo(CPathConfigs.fromTmp(renameTo));
  }

  /**
   * Downloads a file using pattern matching and handles expected alert.
   *
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename     regex pattern to match the downloaded filename
   * @param renameTo     the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example <pre>
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
   * @param clickWait    time in seconds to wait for element to be clickable
   * @param downloadWait time in seconds to wait for download to complete
   * @param filename     regex pattern to match the downloaded filename
   * @param renameTo     the new name for the downloaded file
   * @return CFile object representing the downloaded and renamed file
   * @example <pre>
   * // Full control download with pattern matching
   * Pattern xmlPattern = Pattern.compile("report_\\d{8}_\\d{6}\\.xml");
   * CFile xmlReport = complexDownloadButton.downloadFile(15, 300, xmlPattern, "final-report.xml", true);
   * </pre>
   */
  default CFile downloadFile(int clickWait, int downloadWait, Pattern filename, String renameTo) {
    click(clickWait);
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
   * @example <pre>
   * // Upload an image file
   * File imageFile = new File("/path/to/image.jpg");
   * fileInput.uploadFile(imageFile);
   * </pre>
   */
  default boolean uploadFile(File file) {
    return uploadFile(CFileUtil.getCanonicalPath(file));
  }

  /**
   * Uploads a file to a file input element using file path.
   * Handles both local and remote (grid) execution contexts.
   *
   * @param filePath the absolute path to the file to upload
   * @example <pre>
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
      String fullFileName = getDriver().performActionOnEngine("Copy File To Node", page -> CGridUtil.copyFileToNode(java.lang.Integer.toHexString(System.identityHashCode(page)), new File(filePath)));
      result = sendKeys(fullFileName);
    } else {
      result = sendKeys(filePath);
    }
    return result && getDriver().pressTab();
  }

  /**
   * Uploads a resource file from the classpath to a file input element.
   *
   * @param resourceName the name of the resource file in the classpath
   * @param clazz        the class to use for loading the resource
   * @example <pre>
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
   * @example <pre>
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
   * Sets text in an input field after formatting a Date object.
   * Clears existing content first using platform-appropriate key combination.
   *
   * @param date   the Date object to format and set
   * @param format the date format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy")
   * @example <pre>
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
   * @param date    the Date object to format and set
   * @param format  the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Set date with custom timeout
   * eventDateInput.setText(eventDate, "dd/MM/yyyy", 15);
   * </pre>
   */
  default boolean setText(Date date, String format, int waitSec) {
    return setText(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Sets text in an input field using default timeout.
   * Clears existing content first using platform-appropriate key combination (Ctrl+A/Cmd+A + Delete).
   *
   * @param text the text to enter in the field
   * @example <pre>
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
   * Sets text in an input field with custom timeout.
   * Clears existing content first using platform-appropriate key combination.
   *
   * @param text    the text to enter in the field
   * @param waitSec maximum time in seconds to wait for element to be present and interactable
   * @example <pre>
   * // Set text with extended timeout for slow-loading fields
   * dynamicTextField.setText("Updated content", 20);
   * </pre>
   */
  default boolean setText(String text, int waitSec) {
    return getDriver()
        .waitUntil(
            "Set Text",
            waitSec,
            engine -> engine.setText(getLocator(), text));
  }

  /**
   * Sets text in an input field and presses Enter key.
   * Useful for search boxes and forms that submit on Enter.
   *
   * @param text the text to enter before pressing Enter
   * @example <pre>
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
   * @param text    the text to enter before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Search with custom timeout
   * slowSearchBox.setTextAndEnter("complex query", 15);
   * </pre>
   */
  default boolean setTextAndEnter(String text, int waitSec) {
    return setTextAnd(text, "Enter", waitSec);
  }

  /**
   * Sets text in an input field and presses Tab key.
   * Useful for moving to the next field in forms.
   *
   * @param text the text to enter before pressing Tab
   * @example <pre>
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
   * @param text    the text to enter before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
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
   * @example <pre>
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
   * @param text    the text to enter
   * @param key     the key to press after entering text
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Set text and press custom key with timeout
   * specialField.setTextAnd("special input", "F5", 10);
   * </pre>
   */
  default boolean setTextAnd(String text, String key, int waitSec) {
    return getDriver()
        .waitUntil(
            "Set Text And " + key,
            waitSec,
            engine -> engine.setText(getLocator(), text) && engine.press(getLocator(), key));
  }

  /**
   * Sets the value attribute of an element using a formatted Date object.
   * Uses JavaScript to directly set the value attribute.
   *
   * @param date   the Date object to format and set
   * @param format the date format string
   * @example <pre>
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
   * @param date    the Date object to format and set
   * @param format  the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Set date value with timeout
   * startDateInput.setValue(startDate, "MM/dd/yyyy", 8);
   * </pre>
   */
  default boolean setValue(Date date, String format, int waitSec) {
    return setValue(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Sets the value attribute of an element directly using JavaScript.
   * This method bypasses normal input events and directly sets the value.
   *
   * @param text the text value to set
   * @example <pre>
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
   * @param text    the text value to set
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Set value with timeout for dynamically created fields
   * dynamicField.setValue("computed value", 12);
   * </pre>
   */
  default boolean setValue(String text, int waitSec) {
    return setAttribute("value", text, waitSec);
  }

  /**
   * Sets the value attribute and presses Enter key.
   * Combines setValue with Enter key press for form submission.
   *
   * @param text the text value to set before pressing Enter
   * @example <pre>
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
   * @param text    the text value to set before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Set value and submit with timeout
   * complexFormField.setValueAndEnter("complex data", 15);
   * </pre>
   */
  default boolean setValueAndEnter(String text, int waitSec) {
    return setValue(text, waitSec) && sendKeys("Enter");
  }

  /**
   * Sets the value attribute and presses Tab key.
   * Useful for setting values and moving to next field in forms.
   *
   * @param text the text value to set before pressing Tab
   * @example <pre>
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
   * @param text    the text value to set before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Set value and tab with timeout
   * asyncField.setValueAndTab("calculated value", 10);
   * </pre>
   */
  default boolean setValueAndTab(String text, int waitSec) {
    return setValue(text, waitSec) && sendKeys("Tab");
  }

  /**
   * Clears the content of an input field using platform-appropriate key combination.
   * Uses Ctrl+A+Delete on Windows/Linux or Cmd+A+Delete on Mac.
   *
   * @example <pre>
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
   * @example <pre>
   * // Clear field with timeout
   * slowLoadingInput.clear(15);
   * </pre>
   */
  default boolean clear(int waitSec) {
    return getDriver()
        .waitUntil(
            "Clear",
            waitSec,
            engine -> engine.clearElement(getLocator()));
  }

  /**
   * Types text character by character after formatting a Date object.
   * Clears existing content first, then types each character individually.
   *
   * @param date   the Date object to format and type
   * @param format the date format string
   * @example <pre>
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
   * @param date    the Date object to format and type
   * @param format  the date format string
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Type date with timeout
   * endDateInput.type(endDate, "yyyy-MM-dd", 12);
   * </pre>
   */
  default boolean type(Date date, String format, int waitSec) {
    return type(CDate.of(date).toFormat(format), waitSec);
  }

  /**
   * Types text character by character in an input field.
   * Clears existing content first, then types each character individually.
   * Useful for fields that need gradual input or have real-time validation.
   *
   * @param text the text to type character by character
   * @example <pre>
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
      description = "Types text character by character in the web element input field"
  )
  default boolean type(
      @CMcpToolParam(name = "text", description = "The text to type") String text) {
    return type(text, getWaitSec());
  }

  /**
   * Types text character by character with custom timeout.
   *
   * @param text    the text to type character by character
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
   * // Type with custom timeout
   * slowField.type("sensitive data", 20);
   * </pre>
   */
  default boolean type(String text, int waitSec) {
    return type(text, waitSec, 0L);
  }

  /**
   * Types text character by character with a delay between each character.
   * Useful for simulating human-like typing or for fields sensitive to typing speed.
   *
   * @param text                   the text to type
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
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
   * @param text                   the text to type
   * @param waitSec                maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
   * // Type with both custom timeout and character delay
   * specialInput.type("complex input", 15, 200);
   * </pre>
   */
  default boolean type(String text, int waitSec, long intervalInMilliSeconds) {
    return getDriver().waitUntil("Type", waitSec, engine -> engine.sendKeys(getLocator(), text, intervalInMilliSeconds));
  }

  /**
   * Types text character by character and presses Tab key.
   * Combines typing with Tab key press for moving to next field.
   *
   * @param text the text to type before pressing Tab
   * @example <pre>
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
   * @param text    the text to type before pressing Tab
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
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
   * @param text                   the text to type before pressing Tab
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
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
   * @param text                   the text to type before pressing Tab
   * @param waitSec                maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
   * // Type with all custom settings and tab
   * complexField.typeAndTab("complex input", 15, 100);
   * </pre>
   */
  default boolean typeAndTab(String text, int waitSec, long intervalInMilliSeconds) {
    return typeAnd(text, "Tab", waitSec, intervalInMilliSeconds);
  }

  /**
   * Types text character by character and presses Enter key.
   * Useful for form submission or search execution after typing.
   *
   * @param text the text to type before pressing Enter
   * @example <pre>
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
   * @param text    the text to type before pressing Enter
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
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
   * @param text                   the text to type before pressing Enter
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
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
   * @param text                   the text to type before pressing Enter
   * @param waitSec                maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
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
   * @example <pre>
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
   * @param text    the text to type
   * @param waitSec maximum time in seconds to wait for element to be present
   * @example <pre>
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
   * @param text                   the text to type
   * @param keys                   the key to press after typing
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
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
   * @param text                   the text to type
   * @param key                    the key to press after typing
   * @param waitSec                maximum time in seconds to wait for element to be present
   * @param intervalInMilliSeconds delay in milliseconds between each character
   * @example <pre>
   * // Full control typing with custom key
   * complexField.typeAnd("complex data entry", "F10", 20, 100);
   *
   * // Type code slowly and press compile key
   * codeEditor.typeAnd("function test() { return true; }", "F9", "15", "25");
   * </pre>
   */
  default boolean typeAnd(String text, String key, int waitSec, long intervalInMilliSeconds) {
    return getDriver().waitUntil("Type And", waitSec, engine ->
        engine.sendKeys(getLocator(), text, intervalInMilliSeconds) && engine.press(getLocator(), key));
  }
}

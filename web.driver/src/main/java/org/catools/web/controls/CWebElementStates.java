package org.catools.web.controls;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.date.CDate;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.media.utils.CImageUtil;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.drivers.CDriver;
import org.openqa.selenium.*;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

/**
 * Interface providing state checking and property retrieval methods for web elements.
 * This interface defines methods to check various states of web elements like visibility,
 * enablement, selection, and to retrieve element properties like text, attributes, and screenshots.
 *
 * @param <DR> the driver type extending CDriver
 * @author CATools Team
 * @since 1.0
 */
public interface CWebElementStates<DR extends CDriver> {
  /**
   * Default timeout value in seconds for web element operations.
   */
  int DEFAULT_TIMEOUT = CDriverConfigs.getTimeout();

  /**
   * Gets the web driver instance.
   *
   * @return the driver instance
   */
  DR getDriver();

  /**
   * Gets the wait timeout in seconds.
   *
   * @return the wait timeout in seconds
   */
  int getWaitSec();

  /**
   * Gets the locator used to find this element.
   *
   * @return the By locator
   */
  By getLocator();

  // Getters

  /**
   * Gets the offset top value of the element using the default timeout.
   *
   * @return the offset top value as Integer, or null if element not found
   * @example <pre>
   * CWebElement button = driver.$(By.id("submit-btn"));
   * Integer offset = button.getOffset();
   * System.out.println("Button offset: " + offset);
   * </pre>
   */
  default Integer getOffset() {
    return getOffset(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the offset top value of the element within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the offset top value as Integer, or null if element not found
   * @example <pre>
   * CWebElement header = driver.$(By.className("page-header"));
   * Integer offset = header.getOffset(5); // Wait up to 5 seconds
   * if (offset != null && offset > 100) {
   *     // Element is positioned more than 100px from top
   * }
   * </pre>
   */
  default Integer getOffset(int waitSec) {
    return waitUntil(
        "Get Offset", waitSec, null, el -> Integer.valueOf(el.getAttribute("offsetTop")));
  }

  /**
   * Checks if the element is stale (no longer attached to the DOM) using the default timeout.
   *
   * @return true if the element is stale, false otherwise
   * @example <pre>
   * CWebElement dynamicElement = driver.$(By.id("dynamic-content"));
   * // After some DOM manipulation...
   * if (dynamicElement.isStaleness()) {
   *     // Element is no longer valid, need to re-find it
   *     dynamicElement = driver.$(By.id("dynamic-content"));
   * }
   * </pre>
   */
  default boolean isStaleness() {
    return isStaleness(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is stale (no longer attached to the DOM) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is stale, false otherwise
   * @example <pre>
   * CWebElement popup = driver.$(By.className("modal"));
   * // Click close button that removes the popup
   * popup.findElement(By.className("close")).click();
   *
   * // Wait up to 3 seconds for popup to become stale
   * if (popup.isStaleness(3)) {
   *     System.out.println("Popup successfully closed");
   * }
   * </pre>
   */
  default boolean isStaleness(int waitSec) {
    return waitUntil(
        "Is Staleness",
        waitSec,
        false,
        el -> {
          try {
            return !el.isEnabled();
          } catch (StaleElementReferenceException | NullPointerException var3) {
            return true;
          }
        });
  }

  /**
   * Checks if the element is not stale (still attached to the DOM) using the default timeout.
   *
   * @return true if the element is not stale, false otherwise
   * @example <pre>
   * CWebElement persistentElement = driver.$(By.id("main-content"));
   * if (persistentElement.isNotStaleness()) {
   *     // Element is still valid, safe to interact with
   *     persistentElement.click();
   * }
   * </pre>
   */
  default boolean isNotStaleness() {
    return isNotStaleness(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is not stale (still attached to the DOM) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not stale, false otherwise
   * @example <pre>
   * CWebElement form = driver.$(By.id("user-form"));
   * // Submit form and wait for it to remain valid
   * form.findElement(By.type("submit")).click();
   *
   * if (form.isNotStaleness(2)) {
   *     // Form is still present, might show validation errors
   *     List&lt;CWebElement&gt; errors = form.findElements(By.className("error"));
   * }
   * </pre>
   */
  default boolean isNotStaleness(int waitSec) {
    return isEnabled(waitSec);
  }

  /**
   * Checks if the element is present in the DOM using the default timeout.
   *
   * @return true if the element is present, false otherwise
   * @example <pre>
   * CWebElement errorMessage = driver.$(By.id("error-msg"));
   * if (errorMessage.isPresent()) {
   *     String message = errorMessage.getText();
   *     System.out.println("Error: " + message);
   * }
   * </pre>
   */
  @CMcpTool(
      name = "element_is_present",
      title = "Check if Element is Present",
      description = "Checks if the web element is present in the DOM"
  )
  default boolean isPresent() {
    return isPresent(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is present in the DOM within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is present, false otherwise
   * @example <pre>
   * // Wait up to 10 seconds for loading spinner to appear
   * CWebElement spinner = driver.$(By.className("loading-spinner"));
   * if (spinner.isPresent(10)) {
   *     // Spinner appeared, now wait for it to disappear
   *     spinner.waitUntilNotVisible();
   * }
   * </pre>
   */
  default boolean isPresent(int waitSec) {
    return waitUntil("Is Present", waitSec, false, Objects::nonNull);
  }

  /**
   * Checks if the element is not present in the DOM using the default timeout.
   *
   * @return true if the element is not present, false otherwise
   * @example <pre>
   * CWebElement temporaryAlert = driver.$(By.id("temp-alert"));
   * // Click dismiss button
   * temporaryAlert.findElement(By.className("dismiss")).click();
   *
   * if (temporaryAlert.isNotPresent()) {
   *     System.out.println("Alert successfully dismissed");
   * }
   * </pre>
   */
  default boolean isNotPresent() {
    return isNotPresent(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is not present in the DOM within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not present, false otherwise
   * @example <pre>
   * CWebElement modal = driver.$(By.className("modal-dialog"));
   * modal.findElement(By.className("close-btn")).click();
   *
   * // Wait up to 5 seconds for modal to be removed from DOM
   * if (modal.isNotPresent(5)) {
   *     // Modal is completely gone, continue with next step
   *     driver.$(By.id("next-button")).click();
   * }
   * </pre>
   */
  default boolean isNotPresent(int waitSec) {
    return waitUntil(
        "Is Not Present",
        waitSec,
        false,
        webDriver -> {
          try {
            return webDriver.findElement(getLocator()) == null;
          } catch (NoSuchElementException | NoSuchFrameException | NoSuchWindowException e) {
            return true;
          }
        });
  }

  /**
   * Checks if the element is visible (displayed) using the default timeout.
   *
   * @return true if the element is visible, false otherwise
   * @example <pre>
   * CWebElement successMessage = driver.$(By.id("success-msg"));
   * if (successMessage.isVisible()) {
   *     System.out.println("Success message is displayed");
   * }
   * </pre>
   */
  @CMcpTool(
      name = "element_is_visible",
      title = "Check if Element is Visible",
      description = "Checks if the web element is visible (displayed)"
  )
  default boolean isVisible() {
    return isVisible(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is visible (displayed) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is visible, false otherwise
   * @example <pre>
   * // Click button that shows a tooltip
   * driver.$(By.id("help-btn")).click();
   *
   * CWebElement tooltip = driver.$(By.className("tooltip"));
   * if (tooltip.isVisible(3)) {
   *     String helpText = tooltip.getText();
   *     System.out.println("Help: " + helpText);
   * }
   * </pre>
   */
  default boolean isVisible(int waitSec) {
    return waitUntil("Is Visible", waitSec, false, WebElement::isDisplayed);
  }

  /**
   * Checks if the element is not visible (hidden) using the default timeout.
   *
   * @return true if the element is not visible, false otherwise
   * @example <pre>
   * CWebElement loadingSpinner = driver.$(By.className("spinner"));
   * if (loadingSpinner.isNotVisible()) {
   *     // Loading is complete, proceed with next action
   *     driver.$(By.id("submit")).click();
   * }
   * </pre>
   */
  default boolean isNotVisible() {
    return isNotVisible(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is not visible (hidden) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not visible, false otherwise
   * @example <pre>
   * CWebElement progressBar = driver.$(By.id("progress"));
   *
   * // Start a process and wait up to 30 seconds for progress bar to disappear
   * driver.$(By.id("start-process")).click();
   * if (progressBar.isNotVisible(30)) {
   *     System.out.println("Process completed successfully");
   * }
   * </pre>
   */
  default boolean isNotVisible(int waitSec) {
    return waitUntil(
        "Is Not Visible",
        waitSec,
        false,
        webDriver -> {
          try {
            WebElement el = webDriver.findElement(getLocator());
            return !el.isDisplayed();
          } catch (NoSuchElementException | NoSuchFrameException | NoSuchWindowException e) {
            return true;
          }
        });
  }

  /**
   * Checks if the element is enabled and interactable using the default timeout.
   * This method checks not only the element's enabled state but also verifies it's not
   * readonly, disabled, or contained within disabled ancestor elements.
   *
   * @return true if the element is enabled and interactable, false otherwise
   * @example <pre>
   * CWebElement submitButton = driver.$(By.id("submit-btn"));
   * if (submitButton.isEnabled()) {
   *     submitButton.click();
   * } else {
   *     System.out.println("Submit button is disabled");
   * }
   * </pre>
   */
  default boolean isEnabled() {
    return isEnabled(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is enabled and interactable within the specified timeout.
   * This method performs comprehensive checks including:
   * - Element is enabled
   * - Element is not readonly
   * - Element is not disabled
   * - Element is not contained within disabled/readonly/hidden ancestor elements
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is enabled and interactable, false otherwise
   * @example <pre>
   * CWebElement inputField = driver.$(By.id("email"));
   *
   * // Wait up to 5 seconds for field to become enabled after form initialization
   * if (inputField.isEnabled(5)) {
   *     inputField.type("user@example.com");
   * } else {
   *     System.out.println("Email field is not interactable");
   * }
   * </pre>
   */
  default boolean isEnabled(int waitSec) {
    return waitUntil(
        "Is Enable",
        waitSec,
        false,
        el -> {
          return el != null
              && el.isEnabled()
              && StringUtils.isBlank(el.getAttribute("readonly"))
              && StringUtils.isBlank(el.getAttribute("disabled"))
              && el.findElements(
                  By.xpath(
                      "./ancestor::*[contains(@class, 'disabled') or contains(@class, 'readonly') or contains(@class, 'hidden')]"))
              .isEmpty();
        });
  }

  /**
   * Checks if the element is not enabled (disabled or not interactable) using the default timeout.
   *
   * @return true if the element is not enabled, false otherwise
   * @example <pre>
   * CWebElement saveButton = driver.$(By.id("save-btn"));
   * if (saveButton.isNotEnabled()) {
   *     // Fill required fields first
   *     driver.$(By.id("required-field")).type("value");
   *     // Now save button should become enabled
   * }
   * </pre>
   */
  default boolean isNotEnabled() {
    return isNotEnabled(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is not enabled (disabled or not interactable) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not enabled, false otherwise
   * @example <pre>
   * CWebElement submitBtn = driver.$(By.id("submit"));
   *
   * // Clear a required field and wait for submit button to become disabled
   * driver.$(By.id("required-name")).clear();
   * if (submitBtn.isNotEnabled(2)) {
   *     System.out.println("Form validation working correctly");
   * }
   * </pre>
   */
  default boolean isNotEnabled(int waitSec) {
    return waitUntil(
        "Is Not Enabled",
        waitSec,
        false,
        el -> {
          return el != null
              && !(el.isEnabled()
              && StringUtils.isBlank(el.getAttribute("readonly"))
              && StringUtils.isBlank(el.getAttribute("disabled"))
              && el.findElements(By.xpath("./ancestor::*[contains(@class, 'disabled') or contains(@class, 'readonly') or contains(@class, 'hidden')]"))
              .isEmpty());
        });
  }

  /**
   * Checks if the element is selected (for checkboxes, radio buttons, options) using the default timeout.
   *
   * @return true if the element is selected, false otherwise
   * @example <pre>
   * CWebElement checkbox = driver.$(By.id("terms-checkbox"));
   * if (!checkbox.isSelected()) {
   *     checkbox.click(); // Select the checkbox
   * }
   * </pre>
   */
  default boolean isSelected() {
    return isSelected(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is selected (for checkboxes, radio buttons, options) within the specified timeout.
   * This method first ensures the element is visible before checking selection state.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is selected, false otherwise
   * @example <pre>
   * CWebElement radioButton = driver.$(By.id("option-premium"));
   * radioButton.click();
   *
   * // Wait up to 2 seconds for radio button to be selected
   * if (radioButton.isSelected(2)) {
   *     System.out.println("Premium option selected");
   * }
   * </pre>
   */
  default boolean isSelected(int waitSec) {
    return isVisible(waitSec) && waitUntil("Is Selected", 1, false, WebElement::isSelected);
  }

  /**
   * Checks if the element is not selected (for checkboxes, radio buttons, options) using the default timeout.
   *
   * @return true if the element is not selected, false otherwise
   * @example <pre>
   * CWebElement optionalCheckbox = driver.$(By.id("newsletter-subscribe"));
   * if (optionalCheckbox.isNotSelected()) {
   *     System.out.println("User did not opt-in for newsletter");
   * }
   * </pre>
   */
  default boolean isNotSelected() {
    return isNotSelected(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is not selected (for checkboxes, radio buttons, options) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not selected, false otherwise
   * @example <pre>
   * CWebElement defaultRadio = driver.$(By.id("option-basic"));
   * // Select a different option
   * driver.$(By.id("option-premium")).click();
   *
   * // Wait for the default option to become unselected
   * if (defaultRadio.isNotSelected(2)) {
   *     System.out.println("Successfully changed from basic to premium");
   * }
   * </pre>
   */
  default boolean isNotSelected(int waitSec) {
    return waitUntil(
        "Is Not Selected",
        waitSec,
        false,
        webDriver -> {
          try {
            WebElement el = webDriver.findElement(getLocator());
            return el != null && !el.isSelected();
          } catch (NoSuchElementException | NoSuchFrameException | NoSuchWindowException e) {
            return true;
          }
        });
  }

  /**
   * Checks if the element is clickable (enabled and can be interacted with) using the default timeout.
   *
   * @return true if the element is clickable, false otherwise
   * @example <pre>
   * CWebElement loginButton = driver.$(By.id("login-btn"));
   * if (loginButton.isClickable()) {
   *     loginButton.click();
   * } else {
   *     System.out.println("Please fill in required fields first");
   * }
   * </pre>
   */
  default boolean isClickable() {
    return isClickable(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is clickable (enabled and can be interacted with) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is clickable, false otherwise
   * @example <pre>
   * // Fill form and wait for submit button to become clickable
   * driver.$(By.id("username")).type("testuser");
   * driver.$(By.id("password")).type("password123");
   *
   * CWebElement submitBtn = driver.$(By.id("submit"));
   * if (submitBtn.isClickable(3)) {
   *     submitBtn.click();
   * }
   * </pre>
   */
  default boolean isClickable(int waitSec) {
    return isEnabled(waitSec);
  }

  /**
   * Checks if the element is not clickable (disabled or cannot be interacted with) using the default timeout.
   *
   * @return true if the element is not clickable, false otherwise
   * @example <pre>
   * CWebElement nextButton = driver.$(By.id("next-btn"));
   * if (nextButton.isNotClickable()) {
   *     // Complete current step first
   *     driver.$(By.id("required-step")).complete();
   * }
   * </pre>
   */
  default boolean isNotClickable() {
    return isNotClickable(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is not clickable (disabled or cannot be interacted with) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not clickable, false otherwise
   * @example <pre>
   * CWebElement deleteBtn = driver.$(By.id("delete-btn"));
   *
   * // Start deletion process - button should become disabled
   * deleteBtn.click();
   * if (deleteBtn.isNotClickable(2)) {
   *     System.out.println("Delete button properly disabled during operation");
   * }
   * </pre>
   */
  default boolean isNotClickable(int waitSec) {
    return isNotEnabled(waitSec);
  }

  /**
   * Gets the visible text content of the element using the default timeout.
   *
   * @return the text content of the element, or empty string if not found
   * @example <pre>
   * CWebElement title = driver.$(By.tagName("h1"));
   * String titleText = title.getText();
   * System.out.println("Page title: " + titleText);
   * </pre>
   */
  @CMcpTool(
      name = "element_get_text",
      title = "Get Element Text",
      description = "Gets the visible text content of the web element"
  )
  default String getText() {
    return getText(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the visible text content of the element within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the text content of the element, or empty string if not found
   * @example <pre>
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
    return waitUntil("Get Text", waitSec, "", WebElement::getText);
  }

  /**
   * Parses the element's text content as a date using the specified format.
   *
   * @param dateFormat the date format pattern (e.g., "yyyy-MM-dd", "MM/dd/yyyy")
   * @return the parsed Date object, or null if text is blank or cannot be parsed
   * @example <pre>
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
   * @param waitSec    the maximum time to wait in seconds
   * @return the parsed Date object, or null if text is blank or cannot be parsed
   * @example <pre>
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
   * Gets the value attribute of the element using the default timeout.
   * Commonly used for input fields, textareas, and select elements.
   *
   * @return the value attribute of the element, or empty string if not found
   * @example <pre>
   * CWebElement emailInput = driver.$(By.id("email"));
   * String currentEmail = emailInput.getValue();
   * System.out.println("Current email: " + currentEmail);
   * </pre>
   */
  default String getValue() {
    return getValue(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the value attribute of the element within the specified timeout.
   * Commonly used for input fields, textareas, and select elements.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the value attribute of the element, or empty string if not found
   * @example <pre>
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
    return waitUntil("Get Value", waitSec, "", el -> el.getAttribute("value"));
  }

  /**
   * Gets the innerHTML content of the element using the default timeout.
   * Returns the HTML content inside the element.
   *
   * @return the innerHTML content of the element, or empty string if not found
   * @example <pre>
   * CWebElement contentDiv = driver.$(By.id("rich-content"));
   * String htmlContent = contentDiv.getInnerHTML();
   * System.out.println("Content HTML: " + htmlContent);
   * </pre>
   */
  default String getInnerHTML() {
    return getInnerHTML(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the innerHTML content of the element within the specified timeout.
   * Returns the HTML content inside the element using JavaScript execution.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the innerHTML content of the element, or empty string if not found
   * @example <pre>
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
        "",
        el -> getDriver().executeScript("return arguments[0].innerHTML", el));
  }

  /**
   * Gets the tag name of the element using the default timeout.
   *
   * @return the tag name of the element (e.g., "div", "input", "button"), or empty string if not found
   * @example <pre>
   * CWebElement element = driver.$(By.id("my-element"));
   * String tagName = element.getTagName();
   *
   * if ("input".equals(tagName)) {
   *     System.out.println("Element is an input field");
   * }
   * </pre>
   */
  default String getTagName() {
    return getTagName(DEFAULT_TIMEOUT);
  }

  /**
   * Gets the tag name of the element within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return the tag name of the element (e.g., "div", "input", "button"), or empty string if not found
   * @example <pre>
   * // Wait for dynamically created element and check its type
   * CWebElement newElement = driver.$(By.className("dynamic-element"));
   * String tag = newElement.getTagName(3);
   *
   * switch (tag.toLowerCase()) {
   *     case "button":
   *         System.out.println("Dynamic button created");
   *         break;
   *     case "input":
   *         System.out.println("Dynamic input field created");
   *         break;
   * }
   * </pre>
   */
  default String getTagName(int waitSec) {
    return waitUntil("Get Tag", waitSec, "", WebElement::getTagName);
  }

  /**
   * Gets the computed CSS property value of the element using the default timeout.
   *
   * @param cssKey the CSS property name (e.g., "color", "font-size", "display")
   * @return the computed CSS value, or empty string if not found
   * @example <pre>
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
   * @param cssKey  the CSS property name (e.g., "color", "font-size", "display")
   * @param waitSec the maximum time to wait in seconds
   * @return the computed CSS value, or empty string if not found
   * @example <pre>
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
    return waitUntil("Get Css", waitSec, "", el -> el.getCssValue(cssKey));
  }

  /**
   * Gets the value of the specified attribute of the element using the default timeout.
   *
   * @param attribute the attribute name (e.g., "id", "class", "href", "data-value")
   * @return the attribute value, or empty string if attribute doesn't exist
   * @example <pre>
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
   * @param waitSec   the maximum time to wait in seconds
   * @return the attribute value, or empty string if attribute doesn't exist
   * @example <pre>
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
    return waitUntil("Get Attribute", waitSec, "", el -> el.getAttribute(attribute));
  }

  /**
   * Gets the ARIA role of the element using the default timeout.
   *
   * @return the ARIA role value, or empty string if not defined
   * @example <pre>
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
   * @example <pre>
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
    return waitUntil("Get AriaRole", waitSec, "", WebElement::getAriaRole);
  }

  /**
   * Captures a screenshot of the element using the default timeout.
   *
   * @return BufferedImage of the element screenshot, or null if capture fails
   * @example <pre>
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
   * @example <pre>
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
        el -> CImageUtil.readImageOrNull(el.getScreenshotAs(OutputType.BYTES)));
  }

  /**
   * Gets the platform information from the driver session.
   *
   * @return the Platform object containing platform details
   * @example <pre>
   * CWebElement element = driver.$(By.id("platform-specific"));
   * Platform platform = element.getPlatform();
   *
   * if (platform.is(Platform.WINDOWS)) {
   *     // Handle Windows-specific behavior
   * } else if (platform.is(Platform.MAC)) {
   *     // Handle Mac-specific behavior
   * }
   * </pre>
   */
  default Platform getPlatform() {
    return getDriver().getDriverSession().getPlatform();
  }

  // Protected utility methods

  /**
   * Utility method to wait for a condition on the web element and execute an action.
   * This method finds the element using the locator and applies the given function.
   *
   * @param <C>        the return type of the action function
   * @param actionName descriptive name of the action for logging
   * @param waitSec    maximum time to wait in seconds
   * @param action     function to execute on the found element
   * @return the result of the action function, or null if element not found
   */
  default <C> C waitUntil(String actionName, int waitSec, Function<WebElement, C> action) {
    return getDriver()
        .waitUntil(
            actionName,
            waitSec,
            webDriver -> {
              assert webDriver != null;
              WebElement element = webDriver.findElement(getLocator());
              return action.apply(element);
            });
  }

  /**
   * Utility method to wait for a condition on the web element and execute an action with a default return value.
   * This method finds the element using the locator and applies the given function.
   *
   * @param <C>        the return type of the action function
   * @param actionName descriptive name of the action for logging
   * @param waitSec    maximum time to wait in seconds
   * @param defaultTo  default value to return if element not found or action fails
   * @param action     function to execute on the found element
   * @return the result of the action function, or defaultTo if element not found
   */
  default <C> C waitUntil(String actionName, int waitSec, C defaultTo, Function<WebElement, C> action) {
    return getDriver()
        .waitUntil(
            actionName,
            waitSec,
            defaultTo,
            webDriver -> {
              assert webDriver != null;
              WebElement element = webDriver.findElement(getLocator());
              return action.apply(element);
            });
  }
}

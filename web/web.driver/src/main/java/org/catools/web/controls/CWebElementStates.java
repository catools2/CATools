package org.catools.web.controls;

import org.apache.commons.lang3.StringUtils;
import org.catools.mcp.annotation.CMcpTool;

/**
 * Interface providing state checking and property retrieval methods for web elements. This
 * interface defines methods to check various states of web elements like visibility, enablement,
 * selection, and to retrieve element properties like text, attributes, and screenshots.
 *
 * @author CATools Team
 * @since 1.0
 */
public interface CWebElementStates extends CWebElementWaiter {

  // Getters

  /**
   * Checks if the element is stale (no longer attached to the DOM) using the default timeout.
   *
   * @return true if the element is stale, false otherwise
   * @example
   *     <pre>
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
   * @example
   *     <pre>
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
        "Is Staleness", waitSec, false, engine -> engine.getElementCount(getLocator()) == 0);
  }

  /**
   * Checks if the element is not stale (still attached to the DOM) using the default timeout.
   *
   * @return true if the element is not stale, false otherwise
   * @example
   *     <pre>
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
   * @example
   *     <pre>
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
   * @example
   *     <pre>
   * CWebElement errorMessage = driver.$(By.id("error-msg"));
   * if (errorMessage.isPresent()) {
   *     String message = errorMessage.getText();
   *     System.out.println("Error: " + message);
   * }
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_is_present",
      title = "Check if Element is Present",
      description = "Checks if the web element is present in the DOM")
  default boolean isPresent() {
    return isPresent(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is present in the DOM within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is present, false otherwise
   * @example
   *     <pre>
   * // Wait up to 10 seconds for loading spinner to appear
   * CWebElement spinner = driver.$(By.className("loading-spinner"));
   * if (spinner.isPresent(10)) {
   *     // Spinner appeared, now wait for it to disappear
   *     spinner.waitUntilNotVisible();
   * }
   * </pre>
   */
  default boolean isPresent(int waitSec) {
    return waitUntil(
        "Is Present", waitSec, false, engine -> engine.getElementCount(getLocator()) > 0);
  }

  /**
   * Checks if the element is not present in the DOM using the default timeout.
   *
   * @return true if the element is not present, false otherwise
   * @example
   *     <pre>
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
   * @example
   *     <pre>
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
        "Is Not Present", waitSec, false, engine -> engine.getElementCount(getLocator()) == 0);
  }

  /**
   * Checks if the element is visible (displayed) using the default timeout.
   *
   * @return true if the element is visible, false otherwise
   * @example
   *     <pre>
   * CWebElement successMessage = driver.$(By.id("success-msg"));
   * if (successMessage.isVisible()) {
   *     System.out.println("Success message is displayed");
   * }
   * </pre>
   */
  @CMcpTool(
      groups = {"web", "web_element"},
      name = "element_is_visible",
      title = "Check if Element is Visible",
      description = "Checks if the web element is visible (displayed)")
  default boolean isVisible() {
    return isVisible(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the element is visible (displayed) within the specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is visible, false otherwise
   * @example
   *     <pre>
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
    return waitUntil("Is Visible", waitSec, false, engine -> engine.isElementVisible(getLocator()));
  }

  /**
   * Checks if the element is not visible (hidden) using the default timeout.
   *
   * @return true if the element is not visible, false otherwise
   * @example
   *     <pre>
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
   * @example
   *     <pre>
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
        "Is Not Visible", waitSec, false, engine -> !engine.isElementVisible(getLocator()));
  }

  /**
   * Checks if the element is enabled and interactable using the default timeout. This method checks
   * not only the element's enabled state but also verifies it's not readonly, disabled, or
   * contained within disabled ancestor elements.
   *
   * @return true if the element is enabled and interactable, false otherwise
   * @example
   *     <pre>
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
   * Checks if the element is enabled and interactable within the specified timeout. This method
   * performs comprehensive checks including: - Element is enabled - Element is not readonly -
   * Element is not disabled - Element is not contained within disabled/readonly/hidden ancestor
   * elements
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is enabled and interactable, false otherwise
   * @example
   *     <pre>
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
        engine -> {
          if (engine.getElementCount(getLocator()) == 0) return false;
          boolean enabled = engine.isElementEnabled(getLocator());
          String ro = engine.getElementAttribute(getLocator(), "readonly");
          String dis = engine.getElementAttribute(getLocator(), "disabled");
          return enabled && StringUtils.isBlank(ro) && StringUtils.isBlank(dis);
        });
  }

  /**
   * Checks if the element is not enabled (disabled or not interactable) using the default timeout.
   *
   * @return true if the element is not enabled, false otherwise
   * @example
   *     <pre>
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
   * Checks if the element is not enabled (disabled or not interactable) within the specified
   * timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not enabled, false otherwise
   * @example
   *     <pre>
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
    return waitUntil("Is Not Enabled", waitSec, false, engine -> !isEnabled(waitSec));
  }

  /**
   * Checks if the element is selected (for checkboxes, radio buttons, options) using the default
   * timeout.
   *
   * @return true if the element is selected, false otherwise
   * @example
   *     <pre>
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
   * Checks if the element is selected (for checkboxes, radio buttons, options) within the specified
   * timeout. This method first ensures the element is visible before checking selection state.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is selected, false otherwise
   * @example
   *     <pre>
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
    return isVisible(waitSec)
        && waitUntil(
            "Is Selected",
            1,
            false,
            engine ->
                engine.getElementCount(getLocator()) > 0 && engine.isElementSelected(getLocator()));
  }

  /**
   * Checks if the element is not selected (for checkboxes, radio buttons, options) using the
   * default timeout.
   *
   * @return true if the element is not selected, false otherwise
   * @example
   *     <pre>
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
   * Checks if the element is not selected (for checkboxes, radio buttons, options) within the
   * specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not selected, false otherwise
   * @example
   *     <pre>
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
        engine ->
            engine.getElementCount(getLocator()) == 0 || !engine.isElementSelected(getLocator()));
  }

  /**
   * Checks if the element is clickable (enabled and can be interacted with) using the default
   * timeout.
   *
   * @return true if the element is clickable, false otherwise
   * @example
   *     <pre>
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
   * Checks if the element is clickable (enabled and can be interacted with) within the specified
   * timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is clickable, false otherwise
   * @example
   *     <pre>
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
   * Checks if the element is not clickable (disabled or cannot be interacted with) using the
   * default timeout.
   *
   * @return true if the element is not clickable, false otherwise
   * @example
   *     <pre>
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
   * Checks if the element is not clickable (disabled or cannot be interacted with) within the
   * specified timeout.
   *
   * @param waitSec the maximum time to wait in seconds
   * @return true if the element is not clickable, false otherwise
   * @example
   *     <pre>
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
}

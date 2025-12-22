package org.catools.web.drivers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.catools.common.extensions.types.CDynamicBooleanExtension;
import org.catools.common.extensions.types.CDynamicStringExtension;

import static org.catools.web.drivers.CDriver.DEFAULT_TIMEOUT;

/**
 * A comprehensive wrapper for handling web browser alerts in Selenium WebDriver. This class
 * provides fluent API methods for interacting with JavaScript alerts, confirms, and prompts.
 *
 * <p>Usage examples:
 *
 * <pre>
 * // Basic alert handling
 * CWebAlert alert = new CWebAlert(driver);
 * alert.verifyIsPresent().accept();
 *
 * // Check alert message and close
 * alert.verifyMessage("Are you sure?").close(true);
 *
 * // Conditional alert handling
 * if (alert.isPresent()) {
 *     alert.accept();
 * }
 * </pre>
 *
 * @param <DR> the driver type extending CDriver
 * @author catools
 * @since 1.0
 */
@Slf4j
public class CWebAlert<DR extends CDriver> {

  /** The WebDriver instance used for alert operations. */
  @Getter protected final DR driver;

  /**
   * Dynamic extension for retrieving and verifying alert message text. Automatically waits for
   * alert presence and retrieves the alert text.
   *
   * <p>Usage examples:
   *
   * <pre>
   * // Get alert message
   * String message = alert.Message.get();
   *
   * // Verify alert message with wait
   * alert.Message.verifyEquals("Expected message", 10);
   *
   * // Wait for specific message
   * alert.Message.waitEquals("Loading complete...", 30);
   * </pre>
   */
  public final CDynamicStringExtension Message =
      new CDynamicStringExtension() {
        @Override
        public String _get() {
          return getText(0);
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return DEFAULT_TIMEOUT;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return "Alert Text";
        }
      };

  /**
   * Dynamic extension for checking and verifying alert presence. Provides methods to wait for alert
   * to appear or disappear.
   *
   * <p>Usage examples:
   *
   * <pre>
   * // Check if alert is present
   * boolean isPresent = alert.Present.get();
   *
   * // Wait for alert to appear
   * alert.Present.waitIsTrue(10);
   *
   * // Verify alert is present
   * alert.Present.verifyIsTrue("Alert should be visible");
   *
   * // Wait for alert to disappear
   * alert.Present.waitIsFalse(5);
   * </pre>
   */
  public final CDynamicBooleanExtension Present =
      new CDynamicBooleanExtension() {
        @Override
        public Boolean _get() {
          return isPresent();
        }

        @Override
        public int getDefaultWaitInSeconds() {
          return DEFAULT_TIMEOUT;
        }

        @Override
        public String getVerifyMessagePrefix() {
          return "Alert Presence";
        }
      };

  /**
   * Constructs a new CWebAlert instance with the specified driver.
   *
   * @param driver the WebDriver instance to use for alert operations
   *     <p>Usage example:
   *     <pre>
   *                                                                                     CDriver driver = new CDriver();
   *                                                                                     CWebAlert alert = new CWebAlert(driver);
   *                                                                                     </pre>
   */
  public CWebAlert(DR driver) {
    this.driver = driver;
  }

  /**
   * Checks if an alert is currently present without waiting.
   *
   * @return true if an alert is present, false otherwise
   *     <p>Usage example:
   *     <pre>
   * if (alert.isPresent()) {
   *     System.out.println("Alert is visible");
   *     alert.accept();
   * }
   * </pre>
   */
  // Getter
  public boolean isPresent() {
    return isPresent(0);
  }

  /**
   * Checks if an alert is present within the specified timeout period.
   *
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return true if an alert becomes present within the timeout, false otherwise
   *     <p>Usage examples:
   *     <pre>
   * // Wait up to 5 seconds for alert
   * if (alert.isPresent(5)) {
   *     alert.accept();
   * }
   *
   * // Check immediately without waiting
   * boolean present = alert.isPresent(0);
   * </pre>
   */
  public final boolean isPresent(int waitSec) {
    return driver.waitUntil(
        "Is Present",
        waitSec,
        false,
        driver -> {
          try {
            return driver.getAlertText() != null;
          } catch (Exception var3) {
            return true;
          }
        });
  }

  /**
   * Checks if no alert is currently present without waiting.
   *
   * @return true if no alert is present, false if an alert exists
   *     <p>Usage example:
   *     <pre>
   * if (alert.isNotPresent()) {
   *     System.out.println("No alert is showing");
   *     // Proceed with other actions
   * }
   * </pre>
   */
  public boolean isNotPresent() {
    return isNotPresent(0);
  }

  /**
   * Waits for an alert to disappear within the specified timeout period.
   *
   * @param waitSec maximum time to wait for alert absence in seconds
   * @return true if no alert is present within the timeout, false if alert persists
   *     <p>Usage examples:
   *     <pre>
   * // Wait up to 10 seconds for alert to disappear
   * if (alert.isNotPresent(10)) {
   *     System.out.println("Alert has been dismissed");
   * }
   *
   * // Check immediately
   * boolean gone = alert.isNotPresent(0);
   * </pre>
   */
  public final boolean isNotPresent(int waitSec) {
    return driver.waitUntil(
        "Is Not Present",
        waitSec,
        false,
        driver -> {
          try {
            return driver.getAlertText() == null;
          } catch (Exception var3) {
            return true;
          }
        });
  }

  public String getText() {
    return getText(DEFAULT_TIMEOUT);
  }

  public String getText(int waitSec) {
    return driver.waitUntil(
        "Is Not Present",
        waitSec,
        driver -> {
          try {
            return driver.getAlertText();
          } catch (Exception var3) {
            return Strings.EMPTY;
          }
        });
  }

  /**
   * Closes an alert if present, accepting it by default with the default timeout. Does nothing if
   * no alert is present.
   *
   * @return this alert instance for fluent chaining
   *     <p>Usage example:
   *     <pre>
   * // Close alert if present (accept by default)
   * alert.closeIfPresent()
   *      .verifyIsNotPresent();
   * </pre>
   */
  // Method
  public boolean closeIfPresent() {
    return closeIfPresent(true, DEFAULT_TIMEOUT);
  }

  /**
   * Closes an alert if present with the specified accept/cancel behavior and default timeout. Does
   * nothing if no alert is present.
   *
   * @param accept true to accept the alert, false to cancel/dismiss it
   * @return this alert instance for fluent chaining
   *     <p>Usage examples:
   *     <pre>
   * // Accept alert if present
   * alert.closeIfPresent(true);
   *
   * // Cancel/dismiss alert if present
   * alert.closeIfPresent(false);
   * </pre>
   */
  public boolean closeIfPresent(boolean accept) {
    return closeIfPresent(accept, DEFAULT_TIMEOUT);
  }

  /**
   * Closes an alert if present within the specified timeout, accepting it by default. Does nothing
   * if no alert appears within the timeout.
   *
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   *     <p>Usage example:
   *     <pre>
   * // Wait up to 15 seconds for alert and accept it
   * alert.closeIfPresent(15)
   *      .verifyIsNotPresent();
   * </pre>
   */
  public boolean closeIfPresent(int waitSec) {
    return closeIfPresent(true, waitSec);
  }

  /**
   * Closes an alert if present within the specified timeout with the given accept/cancel behavior.
   * Does nothing if no alert appears within the timeout.
   *
   * @param accept true to accept the alert, false to cancel/dismiss it
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   *     <p>Usage examples:
   *     <pre>
   * // Wait 5 seconds and accept if present
   * alert.closeIfPresent(true, 5);
   *
   * // Wait 10 seconds and cancel if present
   * alert.closeIfPresent(false, 10);
   * </pre>
   */
  public boolean closeIfPresent(boolean accept, int waitSec) {
    if (Present.waitIsTrue(waitSec)) {
      return close(accept, 0);
    }
    return false;
  }

  /**
   * Closes an alert by accepting it with the default timeout. Throws an exception if no alert is
   * present.
   *
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert is present
   *     <p>Usage example:
   *     <pre>
   *                                                                                                                                                       // Close alert (accept) - will fail if no alert
   *                                                                                                                                                       alert.verifyIsPresent()
   *                                                                                                                                                            .close()
   *                                                                                                                                                            .verifyIsNotPresent();
   *
   *     </pre>
   */
  public boolean close() {
    return close(true, DEFAULT_TIMEOUT);
  }

  /**
   * Closes an alert with the specified accept/cancel behavior and default timeout. Throws an
   * exception if no alert is present.
   *
   * @param accept true to accept the alert, false to cancel/dismiss it
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert is present
   *     <p>Usage examples:
   *     <pre>
   *                                                                                                                                                       // Accept the alert
   *                                                                                                                                                       alert.close(true);
   *
   *                                                                                                                                                       // Cancel/dismiss the alert
   *                                                                                                                                                       alert.close(false);
   *
   *     </pre>
   */
  public boolean close(boolean accept) {
    return close(accept, DEFAULT_TIMEOUT);
  }

  /**
   * Closes an alert by accepting it within the specified timeout. Waits for alert to appear if not
   * immediately present.
   *
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert appears within timeout
   *     <p>Usage example:
   *     <pre>
   *                                                                                                                                                       // Wait up to 8 seconds for alert then accept
   *                                                                                                                                                       alert.close(8)
   *                                                                                                                                                            .verifyIsNotPresent();
   *
   *     </pre>
   */
  public boolean close(int waitSec) {
    return close(true, waitSec);
  }

  /**
   * Closes an alert with the specified behavior within the given timeout. Waits for alert to appear
   * if not immediately present.
   *
   * @param accept true to accept the alert, false to cancel/dismiss it
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert appears within timeout
   *     <p>Usage examples:
   *     <pre>
   *                                                                                                                                                       // Wait 12 seconds then accept
   *                                                                                                                                                       alert.close(true, 12);
   *
   *                                                                                                                                                       // Wait 5 seconds then cancel
   *                                                                                                                                                       alert.close(false, 5);
   *
   *     </pre>
   */
  public boolean close(boolean accept, int waitSec) {
    verifyIsPresent(waitSec);
    return accept ? accept(0) : cancel(0);
  }

  /**
   * Accepts an alert using the default timeout. Equivalent to clicking "OK" or "Yes" on the alert
   * dialog.
   *
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert is present
   *     <p>Usage example:
   *     <pre>
   *                                                                                                                                                       // Handle confirmation alert
   *                                                                                                                                                       alert.verifyMessage("Are you sure you want to delete?")
   *                                                                                                                                                            .accept()
   *                                                                                                                                                            .verifyIsNotPresent();
   *
   *     </pre>
   */
  public boolean accept() {
    return accept(DEFAULT_TIMEOUT);
  }

  /**
   * Accepts an alert within the specified timeout. Waits for alert to appear if not immediately
   * present.
   *
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert appears within timeout
   *     <p>Usage example:
   *     <pre>
   *                                                                                                      // Wait up to 15 seconds for alert then accept
   *                                                                                                      alert.accept(15);
   *
   *     </pre>
   */
  public boolean accept(int waitSec) {
    return getDriver().waitUntil("Accept Alert", waitSec, CDriverEngine::acceptAlert);
  }

  /**
   * Cancels/dismisses an alert using the default timeout. Equivalent to clicking "Cancel" or "No"
   * on the alert dialog.
   *
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert is present
   *     <p>Usage example:
   *     <pre>
   *                                                                                                      // Wait up to 15 seconds for alert then accept
   *                                                                                                      alert.cancel(15);
   *
   *     </pre>
   */
  public boolean cancel() {
    return cancel(DEFAULT_TIMEOUT);
  }

  /**
   * Cancels/dismisses an alert within the specified timeout. Waits for alert to appear if not
   * immediately present.
   *
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   * @throws RuntimeException if no alert appears within timeout
   *     <p>Usage example:
   *     <pre>
   *                                                                                                                                                       // Wait up to 10 seconds for alert then cancel
   *                                                                                                                                                       alert.cancel(10);
   *
   *     </pre>
   */
  public boolean cancel(int waitSec) {
    return getDriver().waitUntil("Dismiss Alert", waitSec, CDriverEngine::dismissAlert);
  }

  /**
   * Verifies that an alert is present using the default timeout. Throws an exception if no alert
   * appears within the timeout.
   *
   * @return this alert instance for fluent chaining
   * @throws AssertionError if no alert is present within timeout
   *     <p>Usage example:
   *     <pre>
   *                                                                                                                                           // Verify alert appears after clicking delete button
   *                                                                                                                                           clickDeleteButton();
   *                                                                                                                                           alert.verifyIsPresent()
   *                                                                                                                                                .verifyMessage("Confirm deletion")
   *                                                                                                                                                .accept();
   *
   *     </pre>
   */
  public void verifyIsPresent() {
    verifyIsPresent(DEFAULT_TIMEOUT);
  }

  /**
   * Verifies that an alert is present within the specified timeout. Throws an exception if no alert
   * appears within the timeout.
   *
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   * @throws AssertionError if no alert is present within timeout
   *     <p>Usage examples:
   *     <pre>
   *                                                                                                                                           // Verify alert appears within 20 seconds
   *                                                                                                                                           alert.verifyIsPresent(20);
   *
   *                                                                                                                                           // Chain verification with message check
   *                                                                                                                                           alert.verifyIsPresent(5)
   *                                                                                                                                                .verifyMessage("Operation completed");
   *
   *     </pre>
   */
  public void verifyIsPresent(int waitSec) {
    Present.verifyIsTrue(waitSec, "Verify that alert is present");
  }

  /**
   * Verifies that an alert has the expected message using the default timeout. First verifies alert
   * presence, then checks the message content.
   *
   * @param message the expected alert message text
   * @return this alert instance for fluent chaining
   * @throws AssertionError if alert is not present or message doesn't match
   *     <p>Usage examples:
   *     <pre>
   *                                                                                                                                           // Verify specific alert message
   *                                                                                                                                           alert.verifyMessage("File saved successfully");
   *
   *                                                                                                                                           // Chain with alert acceptance
   *                                                                                                                                           alert.verifyMessage("Are you sure?")
   *                                                                                                                                                .accept();
   *
   *     </pre>
   */
  public void verifyMessage(String message) {
    verifyMessage(message, DEFAULT_TIMEOUT);
  }

  /**
   * Verifies that an alert has the expected message within the specified timeout. First waits for
   * alert presence, then verifies the message content.
   *
   * @param message the expected alert message text
   * @param waitSec maximum time to wait for alert presence in seconds
   * @return this alert instance for fluent chaining
   * @throws AssertionError if alert is not present within timeout or message doesn't match
   *     <p>Usage examples:
   *     <pre>
   *                                                                                                                                           // Wait up to 25 seconds for specific message
   *                                                                                                                                           alert.verifyMessage("Processing complete", 25);
   *
   *                                                                                                                                           // Verify error message with custom timeout
   *                                                                                                                                           alert.verifyMessage("Invalid input", 3)
   *                                                                                                                                                .accept();
   *
   *     </pre>
   */
  public void verifyMessage(String message, int waitSec) {
    verifyIsPresent(waitSec);
    Message.verifyEquals(message, 0, "Verify that alert has a right message");
  }
}

package org.catools.web.exceptions;

import org.catools.web.drivers.CDriver;

/**
 * Exception thrown when a page is expected to be open but is not found or accessible. This
 * exception provides detailed information about the current page state including the page title and
 * URL at the time the exception was thrown.
 *
 * <p>This exception is typically thrown when:
 *
 * <ul>
 *   <li>A page fails to load completely
 *   <li>Navigation to a page was unsuccessful
 *   <li>A page is not in the expected state for further operations
 *   <li>Page elements are not available due to page loading issues
 * </ul>
 *
 * @author CA Tools Framework
 * @see RuntimeException
 * @see CDriver
 * @since 1.0
 */
public class CPageNotOpenedException extends RuntimeException {

  /**
   * Constructs a new CPageNotOpenedException with detailed information about the current page
   * state. The exception message includes the current page title and URL from the provided driver.
   *
   * <p><strong>Example usage:</strong>
   *
   * <pre>{@code
   * try {
   *     // Attempt to navigate to a page
   *     driver.navigateTo("https://example.com/page");
   *     // Verify page is loaded
   *     if (!isPageLoaded(driver)) {
   *         throw new CPageNotOpenedException(driver,
   *             new IllegalStateException("Page failed to load"));
   *     }
   * } catch (CPageNotOpenedException e) {
   *     // Handle the exception - message will contain current page details
   *     logger.error("Page not opened: " + e.getMessage(), e);
   * }
   * }</pre>
   *
   * <p><strong>Another example with timeout scenarios:</strong>
   *
   * <pre>{@code
   * public void waitForPageToOpen(CDriver driver, String expectedTitle) {
   *     try {
   *         // Wait for page with timeout
   *         WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), Duration.ofSeconds(10));
   *         wait.until(ExpectedConditions.titleContains(expectedTitle));
   *     } catch (TimeoutException e) {
   *         // Throw custom exception with current page context
   *         throw new CPageNotOpenedException(driver, e);
   *     }
   * }
   * }</pre>
   *
   * @param driver the CDriver instance containing current page information (title and URL)
   * @param cause the underlying cause of the exception (can be null)
   * @throws NullPointerException if driver is null and getTitle() or getUrl() is called
   */
  public CPageNotOpenedException(CDriver driver, Throwable cause) {
    super(
        "Current Page Title: '" + driver.getTitle() + "' Current Url:'" + driver.getUrl() + "'",
        cause);
  }
}

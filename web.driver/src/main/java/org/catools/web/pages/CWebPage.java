package org.catools.web.pages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.catools.common.extensions.verify.CVerify;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CWebAlert;
import org.catools.web.exceptions.CPageNotOpenedException;
import org.catools.web.factory.CWebElementFactory;

import static org.catools.web.drivers.CDriverWaiter.DEFAULT_TIMEOUT;

/**
 * Abstract base class for web page representations in the CATools framework.
 * This class provides common functionality for web page interactions, verification,
 * and navigation. All page objects should extend this class to inherit standardized
 * page handling capabilities.
 *
 * <p>The class supports automatic page verification upon instantiation, with options
 * for retry mechanisms and refresh functionality if the initial page load fails.</p>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * public class LoginPage extends CWebPage<CDriver> {
 *     public LoginPage(CDriver driver) {
 *         super(driver, "Login - MyApp");
 *     }
 *     
 *     // Page-specific methods...
 * }
 * 
 * // Usage
 * CDriver driver = new CDriver();
 * LoginPage loginPage = new LoginPage(driver);
 * loginPage.verifyDisplayed();
 * }</pre>
 *
 * @param <DR> the type of driver extending CDriver
 * @author CATools Team
 * @see CWebComponent
 * @see CDriver
 */
public abstract class CWebPage<DR extends CDriver> implements CWebComponent<DR> {

  /**
   * The web driver instance used for browser interactions.
   * This field is final and cannot be modified after instantiation.
   */
  @Getter
  @Setter(AccessLevel.NONE)
  protected final DR driver;
  
  /**
   * Pattern used to match the page title for verification purposes.
   * This pattern is used with regular expressions for flexible title matching.
   */
  protected String titlePattern;

  /**
   * Constructs a new web page with default timeout settings.
   * This constructor uses the default timeout for page verification.
   *
   * @param driver the web driver instance to use for page interactions
   * @param titlePattern the regex pattern to match against the page title
   * 
   * @throws CPageNotOpenedException if the page is not displayed within the default timeout
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * public class HomePage extends CWebPage<CDriver> {
   *     public HomePage(CDriver driver) {
   *         super(driver, "Home.*Dashboard");
   *     }
   * }
   * }</pre>
   */
  public CWebPage(DR driver, String titlePattern) {
    this(driver, titlePattern, DEFAULT_TIMEOUT);
  }

  /**
   * Constructs a new web page with custom timeout settings.
   *
   * @param driver the web driver instance to use for page interactions
   * @param titlePattern the regex pattern to match against the page title
   * @param waitSecs the maximum time in seconds to wait for page verification
   * 
   * @throws CPageNotOpenedException if the page is not displayed within the specified timeout
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Wait up to 30 seconds for a slow-loading page
   * HomePage homePage = new HomePage(driver, "Home.*", 30);
   * }</pre>
   */
  public CWebPage(DR driver, String titlePattern, int waitSecs) {
    this(driver, titlePattern, waitSecs, false);
  }

  /**
   * Constructs a new web page with refresh retry capability.
   *
   * @param driver the web driver instance to use for page interactions
   * @param titlePattern the regex pattern to match against the page title
   * @param waitSecs the maximum time in seconds to wait for initial page verification
   * @param tryRefreshIfNotDisplayed if true, attempts to refresh the page if initial verification fails
   * 
   * @throws CPageNotOpenedException if the page is not displayed even after refresh attempt
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Try refreshing if the page doesn't load initially
   * ProductPage productPage = new ProductPage(driver, "Product.*Details", 15, true);
   * }</pre>
   */
  public CWebPage(DR driver, String titlePattern, int waitSecs, boolean tryRefreshIfNotDisplayed) {
    this(driver, titlePattern, waitSecs, tryRefreshIfNotDisplayed, waitSecs);
  }

  /**
   * Constructs a new web page with full customization of retry behavior.
   * This is the most comprehensive constructor that allows fine-tuning of both
   * initial verification timeout and post-refresh verification timeout.
   *
   * @param driver the web driver instance to use for page interactions
   * @param titlePattern the regex pattern to match against the page title
   * @param waitSecs the maximum time in seconds to wait for initial page verification
   * @param tryRefreshIfNotDisplayed if true, attempts to refresh the page if initial verification fails
   * @param waitSecsAfterRefresh the maximum time in seconds to wait for verification after refresh
   * 
   * @throws CPageNotOpenedException if the page is not displayed even after refresh attempt
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Wait 10 seconds initially, then refresh and wait 20 seconds
   * CheckoutPage checkout = new CheckoutPage(driver, "Checkout.*", 10, true, 20);
   * }</pre>
   */
  public CWebPage(DR driver, String titlePattern, int waitSecs, boolean tryRefreshIfNotDisplayed, int waitSecsAfterRefresh) {
    this.driver = driver;
    CWebElementFactory.initElements(this);
    this.titlePattern = titlePattern;
    try {
      driver.waitCompleteReadyState();
      verifyDisplayed(waitSecs);
    } catch (Throwable t) {
      if (!tryRefreshIfNotDisplayed) {
        throw new CPageNotOpenedException(driver, t);
      }
      try {
        driver.refresh();
        verifyDisplayed(waitSecsAfterRefresh);
      } catch (Throwable e) {
        throw new CPageNotOpenedException(driver, e);
      }
    }
  }

  /**
   * Checks if the current page is displayed using the default timeout.
   * This method performs a non-blocking check to determine if the page title
   * matches the expected pattern.
   *
   * @return true if the page is currently displayed and title matches the pattern, false otherwise
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * if (loginPage.isDisplayed()) {
   *     System.out.println("Login page is ready for interaction");
   * } else {
   *     System.out.println("Login page is not available");
   * }
   * }</pre>
   */
  public boolean isDisplayed() {
    return isDisplayed(DEFAULT_TIMEOUT);
  }

  /**
   * Checks if the current page is displayed within the specified timeout.
   * This method switches to the page context and waits for the title to match
   * the expected pattern.
   *
   * @param waitSecs the maximum time in seconds to wait for the page to be displayed
   * @return true if the page is displayed within the timeout, false otherwise
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Check if page loads within 5 seconds
   * if (dashboardPage.isDisplayed(5)) {
   *     dashboardPage.clickMenuItem("Reports");
   * } else {
   *     // Handle timeout scenario
   *     driver.refresh();
   * }
   * }</pre>
   */
  public boolean isDisplayed(int waitSecs) {
    driver.switchToPage(this.titlePattern);
    return driver.Title.waitMatches("^" + titlePattern + "$", waitSecs);
  }

  /**
   * Verifies that the current page is displayed using the default timeout.
   * This method throws an assertion error if the page is not displayed,
   * making it suitable for test scenarios where page presence is mandatory.
   *
   * @param <T> the specific page type being verified
   * @return this page instance for method chaining
   * @throws AssertionError if the page is not displayed within the default timeout
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Verify page is displayed and continue with test
   * loginPage.verifyDisplayed()
   *         .enterUsername("testuser")
   *         .enterPassword("password")
   *         .clickLogin();
   * }</pre>
   */
  public <T extends CWebPage<DR>> T verifyDisplayed() {
    CVerify.Bool.isTrue(isDisplayed(DEFAULT_TIMEOUT), String.format("Verify %s page is displayed", getClass().getSimpleName()));
    return (T) this;
  }

  /**
   * Verifies that the current page is displayed within the specified timeout.
   * This method throws an assertion error if the page is not displayed,
   * with a custom timeout for slower loading pages.
   *
   * @param <T> the specific page type being verified
   * @param waitSecs the maximum time in seconds to wait for verification
   * @return this page instance for method chaining
   * @throws AssertionError if the page is not displayed within the specified timeout
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Verify slow-loading report page with extended timeout
   * reportPage.verifyDisplayed(60)
   *          .selectDateRange("Last Month")
   *          .generateReport();
   * }</pre>
   */
  public <T extends CWebPage<DR>> T verifyDisplayed(int waitSecs) {
    CVerify.Bool.isTrue(isDisplayed(waitSecs), String.format("Verify %s page is displayed", getClass().getSimpleName()));
    return (T) this;
  }

  /**
   * Creates and returns a web alert handler for this page.
   * This method provides access to browser alert, confirm, and prompt dialogs
   * that may appear during page interactions.
   *
   * @return a CWebAlert instance for handling browser dialogs
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Handle a confirmation dialog
   * myPage.clickDeleteButton();
   * CWebAlert<CDriver> alert = myPage.getAlert();
   * if (alert.isPresent()) {
   *     alert.accept(); // Click OK/Yes
   * }
   * 
   * // Handle a prompt dialog
   * myPage.clickRenameButton();
   * alert = myPage.getAlert();
   * alert.sendKeys("New Name").accept();
   * }</pre>
   */
  public CWebAlert<DR> getAlert() {
    return new CWebAlert<>(driver);
  }

  /**
   * Refreshes the current page by reloading it in the browser.
   * This method triggers a browser refresh, which will reload all page content
   * and reset any dynamic state.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Refresh page to see latest data
   * dashboardPage.refresh();
   * dashboardPage.verifyDisplayed(); // Verify page loaded correctly
   * 
   * // Refresh after network error
   * try {
   *     orderPage.submitOrder();
   * } catch (NetworkException e) {
   *     orderPage.refresh();
   *     orderPage.retrySubmitOrder();
   * }
   * }</pre>
   */
  public void refresh() {
    driver.refresh();
  }

  /**
   * Returns the web driver instance associated with this page.
   * This method provides direct access to the underlying driver for
   * advanced operations not covered by the page object methods.
   *
   * @return the web driver instance used by this page
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Access driver for advanced operations
   * CDriver driver = myPage.getDriver();
   * driver.executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
   * 
   * // Switch to a different window/tab
   * String currentWindow = driver.getWindowHandle();
   * myPage.clickOpenNewTab();
   * driver.switchTo().window(driver.getWindowHandles().iterator().next());
   * }</pre>
   */
  public DR getDriver() {
    return driver;
  }
}

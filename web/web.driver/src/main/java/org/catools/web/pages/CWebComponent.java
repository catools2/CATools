package org.catools.web.pages;

import org.catools.web.controls.CElementEngine;
import org.catools.web.drivers.CDriver;

/**
 * Base interface for web components that provides access to the underlying driver. This interface
 * establishes a contract for components that need to interact with web drivers in the CA Tools web
 * automation framework.
 *
 * <p>The interface uses generics to ensure type safety when working with specific driver
 * implementations that extend {@link CDriver}.
 *
 * <h2>Example Usage:</h2>
 *
 * <pre>{@code
 * public class LoginPage implements CWebComponent {
 *     private ChromeDriver driver;
 *
 *     public LoginPage(ChromeDriver driver) {
 *         this.driver = driver;
 *     }
 *
 *     @Override
 *     public ChromeDriver getDriver() {
 *         return driver;
 *     }
 *
 *     public void login(String username, String password) {
 *         getDriver().findElement(By.id("username")).sendKeys(username);
 *         getDriver().findElement(By.id("password")).sendKeys(password);
 *         getDriver().findElement(By.id("loginButton")).click();
 *     }
 * }
 *
 * // Usage example
 * ChromeDriver chromeDriver = new ChromeDriver();
 * LoginPage loginPage = new LoginPage(chromeDriver);
 * loginPage.login("testuser", "password123");
 * }</pre>
 *
 * <h2>Another Example with Page Object Pattern:</h2>
 *
 * <pre>{@code
 * public abstract class BasePage implements CWebComponent {
 *     protected Page driver;
 *
 *     public BasePage(Page driver) {
 *         this.driver = driver;
 *     }
 *
 *     @Override
 *     public Page getDriver() {
 *         return driver;
 *     }
 *
 *     protected void waitForElement(By locator) {
 *         new WebDriverWait(getDriver(), Duration.ofSeconds(10))
 *             .until(ExpectedConditions.presenceOfElementLocated(locator));
 *     }
 * }
 *
 * public class SearchPage extends BasePage {
 *     private static final By SEARCH_INPUT = By.id("search");
 *     private static final By SEARCH_BUTTON = By.id("searchBtn");
 *
 *     public SearchPage(Page driver) {
 *         super(driver);
 *     }
 *
 *     public void performSearch(String query) {
 *         waitForElement(SEARCH_INPUT);
 *         getDriver().findElement(SEARCH_INPUT).sendKeys(query);
 *         getDriver().findElement(SEARCH_BUTTON).click();
 *     }
 * }
 * }</pre>
 *
 * @author CA Tools Team
 * @see CElementEngine
 * @since 1.0
 */
public interface CWebComponent {

  /**
   * Gets the underlying web driver associated with this component.
   *
   * @return the web driver instance
   */
  CElementEngine<?> getElementEngine();
}

package org.catools.web.pages;

import org.catools.web.drivers.CDriver;

/**
 * Base interface for web components that provides access to the underlying driver.
 * This interface establishes a contract for components that need to interact with
 * web drivers in the CA Tools web automation framework.
 * 
 * <p>The interface uses generics to ensure type safety when working with specific
 * driver implementations that extend {@link CDriver}.
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * public class LoginPage implements CWebComponent<ChromeDriver> {
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
 * <pre>{@code
 * public abstract class BasePage implements CWebComponent<Page> {
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
 * @param <DR> the type of driver that extends {@link CDriver}
 * 
 * @see CDriver
 * @since 1.0
 * @author CA Tools Team
 */
public interface CWebComponent<DR extends CDriver> {
    
    /**
     * Retrieves the driver instance associated with this web component.
     * 
     * <p>This method provides access to the underlying driver that can be used
     * to perform web automation operations such as finding elements, navigating
     * pages, and interacting with web elements.
     * 
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * public class NavigationComponent implements CWebComponent<Page> {
     *     private Page driver;
     *     
     *     public NavigationComponent(Page driver) {
     *         this.driver = driver;
     *     }
     *     
     *     @Override
     *     public Page getDriver() {
     *         return driver;
     *     }
     *     
     *     public void navigateToHome() {
     *         getDriver().get("https://example.com/home");
     *     }
     *     
     *     public String getCurrentUrl() {
     *         return getDriver().getCurrentUrl();
     *     }
     *     
     *     public void refreshPage() {
     *         getDriver().navigate().refresh();
     *     }
     * }
     * 
     * // Usage
     * Page page = new ChromeDriver();
     * NavigationComponent nav = new NavigationComponent(page);
     * nav.navigateToHome();
     * System.out.println("Current URL: " + nav.getCurrentUrl());
     * }</pre>
     * 
     * <h3>Advanced Example with Element Interaction:</h3>
     * <pre>{@code
     * public class FormComponent implements CWebComponent<Page> {
     *     private Page driver;
     *     
     *     public FormComponent(Page driver) {
     *         this.driver = driver;
     *     }
     *     
     *     @Override
     *     public Page getDriver() {
     *         return driver;
     *     }
     *     
     *     public void fillForm(Map<String, String> formData) {
     *         WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
     *         
     *         for (Map.Entry<String, String> entry : formData.entrySet()) {
     *             WebElement element = wait.until(
     *                 ExpectedConditions.elementToBeClickable(By.name(entry.getKey()))
     *             );
     *             element.clear();
     *             element.sendKeys(entry.getValue());
     *         }
     *     }
     *     
     *     public void submitForm() {
     *         getDriver().findElement(By.cssSelector("button[type='submit']")).click();
     *     }
     * }
     * }</pre>
     * 
     * @return the driver instance of type {@code DR} that extends {@link CDriver}
     * @throws IllegalStateException if the driver has not been properly initialized
     * 
     * @see CDriver
     * @see org.openqa.selenium.Page
     */
    DR getDriver();
}

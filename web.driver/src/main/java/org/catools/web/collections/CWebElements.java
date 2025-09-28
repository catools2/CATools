package org.catools.web.collections;

import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.openqa.selenium.By;

/**
 * A specialized collection of web elements that provides indexed access to web components
 * found by XPath locators. This class extends {@link CWebList} to offer convenient methods
 * for interacting with multiple similar web elements on a page.
 * 
 * <p>CWebElements is particularly useful when dealing with collections of similar elements
 * such as table rows, list items, form fields, buttons, or any other repeating web components.
 * It uses XPath locators to find elements and provides various methods for iteration,
 * testing, and manipulation of the collection.
 * 
 * <p>The class supports configurable wait times for element loading, which is essential
 * for dynamic web applications where elements may load asynchronously.
 * 
 * <p><strong>Common usage examples:</strong>
 * <pre>{@code
 * // Create a collection of table rows
 * CWebElements<ChromeDriver> tableRows = new CWebElements<>(
 *     "Product Table Rows", 
 *     driver, 
 *     "//table[@id='products']//tr[position()>1]"
 * );
 * 
 * // Create a collection of navigation menu items
 * CWebElements<FirefoxDriver> menuItems = new CWebElements<>(
 *     "Navigation Menu Items",
 *     driver,
 *     "//nav[@class='main-menu']//li//a",
 *     10  // Wait up to 10 seconds for elements to load
 * );
 * 
 * // Create a collection with different wait times for first and subsequent elements
 * CWebElements<EdgeDriver> productCards = new CWebElements<>(
 *     "Product Cards",
 *     driver,
 *     "//div[@class='product-card']",
 *     15,  // Wait 15 seconds for first element
 *     5    // Wait 5 seconds for other elements
 * );
 * }</pre>
 * 
 * <p><strong>Element access patterns:</strong>
 * <pre>{@code
 * // Access specific elements by index
 * CWebElement<Driver> firstElement = elements.getRecord(0);
 * CWebElement<Driver> thirdElement = elements.getRecord(2);
 * 
 * // Safe element access with existence check
 * if (elements.hasRecord(5)) {
 *     CWebElement<Driver> sixthElement = elements.getRecord(5);
 *     sixthElement.click();
 * }
 * 
 * // Iterate through all elements
 * for (CWebElement<Driver> element : elements) {
 *     if (element.isDisplayed()) {
 *         element.click();
 *     }
 * }
 * }</pre>
 * 
 * <p><strong>Collection operations:</strong>
 * <pre>{@code
 * // Get count of elements
 * int totalElements = elements.count();
 * 
 * // Get all element texts
 * CList<String> allTexts = elements.getTexts();
 * 
 * // Test all elements meet a condition
 * boolean allVisible = elements.testAll(element -> element.isDisplayed());
 * 
 * // Test if any element meets a condition
 * boolean hasEnabledElement = elements.testAny(element -> element.isEnabled());
 * 
 * // Perform action on elements matching condition
 * elements.onFirstMatch(
 *     element -> element.getText().contains("Buy Now"),
 *     element -> element.click()
 * );
 * }</pre>
 * 
 * @param <DR> the type of web driver (must extend {@link CDriver})
 * 
 * @see CWebList
 * @see CWebElement
 * @see CDriver
 * @see CWebIterable
 */
public class CWebElements<DR extends CDriver> extends CWebList<CWebElement<DR>> {
  /**
   * Creates a new collection of web elements using the specified XPath locator with default wait time.
   * 
   * <p>This constructor uses the default timeout for waiting for elements to appear. The first element
   * and subsequent elements will use the same default wait time defined by the driver configuration.
   * 
   * <p>Each element in the collection will be created as a {@link CWebElement} with a name that includes
   * the base name and the element's index (e.g., "Button List[0]", "Button List[1]", etc.).
   * 
   * <p><strong>Example usage:</strong>
   * <pre>{@code
   * // Create a collection of all buttons on the page
   * CWebElements<ChromeDriver> buttons = new CWebElements<>(
   *     "All Buttons", 
   *     chromeDriver, 
   *     "//button"
   * );
   * 
   * // Create a collection of table cells in the first row
   * CWebElements<FirefoxDriver> firstRowCells = new CWebElements<>(
   *     "First Row Cells",
   *     firefoxDriver,
   *     "//table//tr[1]//td"
   * );
   * 
   * // Create a collection of form input fields
   * CWebElements<EdgeDriver> inputFields = new CWebElements<>(
   *     "Form Inputs",
   *     edgeDriver,
   *     "//form//input[@type='text' or @type='email' or @type='password']"
   * );
   * }</pre>
   * 
   * @param name a descriptive name for this collection of elements, used for logging and debugging
   * @param driver the web driver instance to use for element interactions
   * @param xpathLocator the XPath expression to locate elements in the collection
   * 
   * @throws IllegalArgumentException if name is null or empty
   * @throws IllegalArgumentException if driver is null
   * @throws IllegalArgumentException if xpathLocator is null or empty
   */
  public CWebElements(String name, DR driver, String xpathLocator) {
    super(
        name,
        xpathLocator,
        (idx, xpath) -> new CWebElement<>(name + "[" + idx + "]", driver, By.xpath(xpath)));
  }

  /**
   * Creates a new collection of web elements using the specified XPath locator with custom wait time.
   * 
   * <p>This constructor allows you to specify a custom wait time for all elements in the collection.
   * Both the first element and subsequent elements will use the same wait time. This is useful when
   * you know that elements in this collection typically take longer or shorter to load than the
   * default timeout.
   * 
   * <p>Each element in the collection will be created as a {@link CWebElement} with a name that includes
   * the base name and the element's index (e.g., "Menu Items[0]", "Menu Items[1]", etc.).
   * 
   * <p><strong>Example usage:</strong>
   * <pre>{@code
   * // Create a collection of slow-loading images with 15-second wait
   * CWebElements<ChromeDriver> images = new CWebElements<>(
   *     "Product Images", 
   *     chromeDriver, 
   *     "//div[@class='gallery']//img",
   *     15  // Wait up to 15 seconds for images to load
   * );
   * 
   * // Create a collection of fast-loading cached elements with 2-second wait
   * CWebElements<FirefoxDriver> cachedItems = new CWebElements<>(
   *     "Cached Menu Items",
   *     firefoxDriver,
   *     "//nav[@class='cached-menu']//a",
   *     2  // Only wait 2 seconds since items are cached
   * );
   * 
   * // Create a collection of dynamically loaded search results
   * CWebElements<EdgeDriver> searchResults = new CWebElements<>(
   *     "Search Results",
   *     edgeDriver,
   *     "//div[@class='search-results']//div[@class='result-item']",
   *     20  // Wait up to 20 seconds for search to complete
   * );
   * }</pre>
   * 
   * @param name a descriptive name for this collection of elements, used for logging and debugging
   * @param driver the web driver instance to use for element interactions
   * @param xpathLocator the XPath expression to locate elements in the collection
   * @param waitSecs the number of seconds to wait for elements to appear (must be positive)
   * 
   * @throws IllegalArgumentException if name is null or empty
   * @throws IllegalArgumentException if driver is null
   * @throws IllegalArgumentException if xpathLocator is null or empty
   * @throws IllegalArgumentException if waitSecs is less than or equal to 0
   */
  public CWebElements(String name, DR driver, String xpathLocator, int waitSecs) {
    super(
        name,
        xpathLocator,
        waitSecs,
        (idx, xpath) -> new CWebElement<>(name + "[" + idx + "]", driver, By.xpath(xpath)));
  }

  /**
   * Creates a new collection of web elements using the specified XPath locator with different
   * wait times for the first element and subsequent elements.
   * 
   * <p>This constructor provides fine-grained control over wait times, allowing you to specify
   * different timeouts for the first element versus other elements. This is particularly useful
   * in scenarios where the first element might take longer to load (e.g., triggering a lazy-loaded
   * list), while subsequent elements appear more quickly.
   * 
   * <p>Each element in the collection will be created as a {@link CWebElement} with a name that includes
   * the base name and the element's index (e.g., "Data Rows[0]", "Data Rows[1]", etc.).
   * 
   * <p><strong>Common use cases:</strong>
   * <ul>
   *   <li><strong>Lazy-loaded lists:</strong> First element triggers loading, others appear quickly</li>
   *   <li><strong>Progressive loading:</strong> First element loads slowly, others are cached</li>
   *   <li><strong>API-driven content:</strong> First element waits for API call, others use cached data</li>
   *   <li><strong>Dynamic tables:</strong> First row triggers table population, other rows appear fast</li>
   * </ul>
   * 
   * <p><strong>Example usage:</strong>
   * <pre>{@code
   * // Lazy-loaded product list - first product triggers loading, others appear quickly
   * CWebElements<ChromeDriver> products = new CWebElements<>(
   *     "Product List", 
   *     chromeDriver, 
   *     "//div[@class='product-grid']//div[@class='product-card']",
   *     30,  // Wait 30 seconds for first product (triggers lazy loading)
   *     3    // Wait only 3 seconds for subsequent products
   * );
   * 
   * // Dynamic search results - first result waits for search, others appear quickly  
   * CWebElements<FirefoxDriver> searchResults = new CWebElements<>(
   *     "Search Results",
   *     firefoxDriver,
   *     "//div[@id='search-results']//div[@class='result']",
   *     25,  // Wait 25 seconds for search to complete and first result
   *     2    // Wait 2 seconds for additional results
   * );
   * 
   * // Infinite scroll content - first item loads content, others are immediate
   * CWebElements<EdgeDriver> scrollItems = new CWebElements<>(
   *     "Scroll Content Items",
   *     edgeDriver,
   *     "//div[@class='infinite-scroll']//div[@class='content-item']",
   *     15,  // Wait 15 seconds for initial content load
   *     1    // Wait 1 second for subsequent items (already loaded)
   * );
   * }</pre>
   * 
   * @param name a descriptive name for this collection of elements, used for logging and debugging
   * @param driver the web driver instance to use for element interactions
   * @param xpathLocator the XPath expression to locate elements in the collection
   * @param waitForFirstElementInSecond the number of seconds to wait for the first element to appear
   * @param waitForOtherElementInSecond the number of seconds to wait for subsequent elements to appear
   * 
   * @throws IllegalArgumentException if name is null or empty
   * @throws IllegalArgumentException if driver is null
   * @throws IllegalArgumentException if xpathLocator is null or empty
   * @throws IllegalArgumentException if waitForFirstElementInSecond is less than or equal to 0
   * @throws IllegalArgumentException if waitForOtherElementInSecond is less than or equal to 0
   */
  public CWebElements(
      String name,
      DR driver,
      String xpathLocator,
      int waitForFirstElementInSecond,
      int waitForOtherElementInSecond) {
    super(
        name,
        xpathLocator,
        waitForFirstElementInSecond,
        waitForOtherElementInSecond,
        (idx, xpath) -> new CWebElement<>(name + "[" + idx + "]", driver, By.xpath(xpath)));
  }
}

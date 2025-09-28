package org.catools.web.collections;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CList;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.catools.web.drivers.CDriver.DEFAULT_TIMEOUT;

/**
 * A web collection class that represents a list of web elements, providing convenient methods
 * for iterating, filtering, and performing operations on multiple similar web elements.
 * 
 * <p>This class is designed to work with dynamic lists of web elements that may appear
 * and disappear during test execution. It provides robust waiting mechanisms and
 * various utility methods for common list operations.
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Create a list of table rows
 * CWebList<CWebElement> tableRows = new CWebList<>(
 *     "Table Rows", 
 *     "//table//tr", 
 *     (index, xpath) -> new CWebElement("Row " + index, xpath)
 * );
 * 
 * // Iterate through all elements
 * tableRows.forEach(row -> {
 *     System.out.println("Row text: " + row.Text._get());
 * });
 * 
 * // Get all text values
 * CList<String> rowTexts = tableRows.getTexts();
 * 
 * // Find and click first matching element
 * tableRows.onFirstMatch(
 *     row -> row.Text._get().contains("Active"),
 *     row -> row.click()
 * );
 * 
 * // Count elements
 * int totalRows = tableRows.count();
 * }</pre>
 * 
 * @param <E> the type of web element this list contains, must extend CWebElement
 * @author CATools
 */
@Getter
@Slf4j
public class CWebList<E extends CWebElement<? extends CDriver>> implements CWebIterable<E> {
  protected final String name;
  protected final String locator;
  protected final BiFunction<Integer, String, E> controlBuilder;
  protected final int waitForFirstElementInSecond;
  protected final int waitForOtherElementInSecond;

  /**
   * Creates a new CWebList with default timeout settings.
   * 
   * @param name a descriptive name for this list (used in logging and error messages)
   * @param xpathLocator the XPath expression to locate elements in the list
   * @param controlBuilder a function that creates web elements given an index and XPath
   * 
   * @example
   * <pre>{@code
   * CWebList<CWebButton> buttons = new CWebList<>(
   *     "Action Buttons",
   *     "//div[@class='actions']//button",
   *     (index, xpath) -> new CWebButton("Button " + index, xpath)
   * );
   * }</pre>
   */
  public CWebList(String name, String xpathLocator, BiFunction<Integer, String, E> controlBuilder) {
    this(name, xpathLocator, DEFAULT_TIMEOUT, controlBuilder);
  }

  /**
   * Creates a new CWebList with custom timeout for all elements.
   * 
   * @param name a descriptive name for this list
   * @param xpathLocator the XPath expression to locate elements
   * @param waitSecs timeout in seconds to wait for each element
   * @param controlBuilder a function that creates web elements
   * 
   * @example
   * <pre>{@code
   * CWebList<CWebElement> slowLoadingItems = new CWebList<>(
   *     "Slow Items",
   *     "//div[@class='slow-loading']//item",
   *     10, // wait up to 10 seconds for each element
   *     (index, xpath) -> new CWebElement("Item " + index, xpath)
   * );
   * }</pre>
   */
  public CWebList(
      String name,
      String xpathLocator,
      int waitSecs,
      BiFunction<Integer, String, E> controlBuilder) {
    this(name, xpathLocator, waitSecs, 0, controlBuilder);
  }

  /**
   * Creates a new CWebList with different timeouts for first and subsequent elements.
   * 
   * @param name a descriptive name for this list
   * @param xpathLocator the XPath expression to locate elements
   * @param waitForFirstElementInSecond timeout for the first element
   * @param waitForOtherElementInSecond timeout for subsequent elements
   * @param controlBuilder a function that creates web elements
   * 
   * @example
   * <pre>{@code
   * CWebList<CWebElement> dynamicList = new CWebList<>(
   *     "Search Results",
   *     "//div[@class='results']//item",
   *     15, // wait longer for first result to appear
   *     2,  // subsequent results should appear quickly
   *     (index, xpath) -> new CWebElement("Result " + index, xpath)
   * );
   * }</pre>
   */
  public CWebList(
      String name,
      String xpathLocator,
      int waitForFirstElementInSecond,
      int waitForOtherElementInSecond,
      BiFunction<Integer, String, E> controlBuilder) {
    super();
    this.name = name;
    this.locator = xpathLocator;
    this.controlBuilder = controlBuilder;
    this.waitForFirstElementInSecond = Math.max(1, waitForFirstElementInSecond);
    this.waitForOtherElementInSecond = Math.max(1, waitForOtherElementInSecond);
  }

  /**
   * Executes a function for each element in the list with custom timeout for the first element.
   * Uses the default timeout for subsequent elements.
   * 
   * @param function the action to perform on each element
   * @param firstWaitSecs timeout in seconds for the first element
   * 
   * @example
   * <pre>{@code
   * menuItems.forEach(item -> {
   *     item.click();
   *     // wait for submenu to appear
   *     Thread.sleep(500);
   * }, 5); // wait up to 5 seconds for first menu item
   * }</pre>
   */
  public void forEach(Consumer<E> function, int firstWaitSecs) {
    forEach(function, firstWaitSecs, this.waitForOtherElementInSecond);
  }

  /**
   * Executes a function for each element in the list with custom timeouts.
   * 
   * @param function the action to perform on each element
   * @param firstWaitSecs timeout in seconds for the first element
   * @param waitSecs timeout in seconds for subsequent elements
   * 
   * @example
   * <pre>{@code
   * // Process search results with different wait times
   * searchResults.forEach(result -> {
   *     String title = result.findElement(".//h3").getText();
   *     String url = result.findElement(".//a").getAttribute("href");
   *     System.out.println(title + " -> " + url);
   * }, 10, 2); // 10 seconds for first result, 2 seconds for others
   * }</pre>
   */
  public void forEach(Consumer<E> function, int firstWaitSecs, int waitSecs) {
    int idx = 0;
    while (true) {
      E control = getRecord(idx++);
      if (control == null || !control.Present.waitIsTrue(idx == 0 ? firstWaitSecs : waitSecs)) {
        break;
      }
      function.accept(control);
    }
  }

  /**
   * Performs an action on the first element that matches the given condition.
   * Uses default timeouts.
   * 
   * @param condition the predicate to test each element against
   * @param action the action to perform on the matching element
   * 
   * @example
   * <pre>{@code
   * // Click the first button containing "Submit"
   * buttons.onFirstMatch(
   *     btn -> btn.Text._get().contains("Submit"),
   *     btn -> btn.click()
   * );
   * }</pre>
   */
  public void onFirstMatch(Predicate<E> condition, Consumer<E> action) {
    onMatch(condition, action, true);
  }

  /**
   * Performs an action on the first element that matches the given condition.
   * 
   * @param condition the predicate to test each element against
   * @param action the action to perform on the matching element
   * @param firstWaitSecs timeout for the first element
   * 
   * @example
   * <pre>{@code
   * // Find and click first enabled checkbox with longer wait
   * checkboxes.onFirstMatch(
   *     cb -> cb.Enabled._get() && !cb.Selected._get(),
   *     cb -> cb.click(),
   *     8 // wait up to 8 seconds for first checkbox
   * );
   * }</pre>
   */
  public void onFirstMatch(Predicate<E> condition, Consumer<E> action, int firstWaitSecs) {
    onMatch(condition, action, true, firstWaitSecs);
  }

  /**
   * Performs an action on the first element that matches the given condition with custom timeouts.
   * 
   * @param condition the predicate to test each element against
   * @param action the action to perform on the matching element
   * @param firstWaitSecs timeout for the first element
   * @param waitSecs timeout for subsequent elements
   * 
   * @example
   * <pre>{@code
   * // Select first available option in a dropdown list
   * dropdownOptions.onFirstMatch(
   *     opt -> opt.Displayed._get() && opt.Enabled._get(),
   *     opt -> opt.click(),
   *     5, // wait 5 seconds for first option
   *     1  // wait 1 second for subsequent options
   * );
   * }</pre>
   */
  public void onFirstMatch(
      Predicate<E> condition, Consumer<E> action, int firstWaitSecs, int waitSecs) {
    onMatch(condition, action, true, firstWaitSecs, waitSecs);
  }

  /**
   * Performs an action on elements that match the given condition.
   * 
   * @param condition the predicate to test each element against
   * @param action the action to perform on matching elements
   * @param stopAfterFirstMatch if true, stops after finding the first match
   * 
   * @example
   * <pre>{@code
   * // Log all error messages (process all matches)
   * errorMessages.onMatch(
   *     msg -> msg.CssClass._get().contains("error"),
   *     msg -> log.error("Found error: " + msg.Text._get()),
   *     false // process all matches
   * );
   * 
   * // Click first "Delete" button (stop after first match)
   * actionButtons.onMatch(
   *     btn -> btn.Text._get().equals("Delete"),
   *     btn -> btn.click(),
   *     true // stop after first match
   * );
   * }</pre>
   */
  public void onMatch(Predicate<E> condition, Consumer<E> action, boolean stopAfterFirstMatch) {
    onMatch(condition, action, stopAfterFirstMatch, DEFAULT_TIMEOUT);
  }

  /**
   * Performs an action on elements that match the given condition with custom timeout for first element.
   * 
   * @param condition the predicate to test each element against
   * @param action the action to perform on matching elements
   * @param stopAfterFirstMatch if true, stops after finding the first match
   * @param firstWaitSecs timeout for the first element
   * 
   * @example
   * <pre>{@code
   * // Find and validate all visible form fields
   * formFields.onMatch(
   *     field -> field.Displayed._get(),
   *     field -> validateField(field),
   *     false, // check all matches
   *     10     // wait longer for first field
   * );
   * }</pre>
   */
  public void onMatch(
      Predicate<E> condition, Consumer<E> action, boolean stopAfterFirstMatch, int firstWaitSecs) {
    onMatch(condition, action, stopAfterFirstMatch, firstWaitSecs, 1);
  }

  /**
   * Performs an action on elements that match the given condition with custom timeouts.
   * 
   * @param condition the predicate to test each element against
   * @param action the action to perform on matching elements
   * @param stopAfterFirstMatch if true, stops after finding the first match
   * @param firstWaitSecs timeout for the first element
   * @param waitSecs timeout for subsequent elements
   * 
   * @example
   * <pre>{@code
   * // Collect all product prices above $50
   * List<Double> expensiveProducts = new ArrayList<>();
   * productPrices.onMatch(
   *     price -> {
   *         String priceText = price.Text._get().replace("$", "");
   *         return Double.parseDouble(priceText) > 50.0;
   *     },
   *     price -> {
   *         String priceText = price.Text._get().replace("$", "");
   *         expensiveProducts.add(Double.parseDouble(priceText));
   *     },
   *     false, // collect all matches
   *     8,     // wait 8 seconds for first price
   *     2      // wait 2 seconds for subsequent prices
   * );
   * }</pre>
   */
  public void onMatch(
      Predicate<E> condition,
      Consumer<E> action,
      boolean stopAfterFirstMatch,
      int firstWaitSecs,
      int waitSecs) {
    int idx = 0;
    while (true) {
      E control = getRecord(idx++);
      if (control == null
          || (!control.Enabled.waitIsTrue(idx == 0 ? firstWaitSecs : waitSecs)
          && !control.Present.waitIsTrue(1))) {
        break;
      }
      if (condition.test(control)) {
        action.accept(control);
        if (stopAfterFirstMatch) {
          break;
        }
      }
    }
  }

  /**
   * Returns all elements in the list using default timeout.
   * 
   * @return a CList containing all found elements
   * 
   * @example
   * <pre>{@code
   * CList<E> allItems = itemList.getElements();
   * System.out.println("Found " + allItems.size() + " items");
   * 
   * // Process each element
   * for (E item : allItems) {
   *     processItem(item);
   * }
   * }</pre>
   */
  public CList<E> getElements() {
    return getElements(DEFAULT_TIMEOUT);
  }

  /**
   * Returns all elements in the list with custom timeout for the first element.
   * 
   * @param firstWaitSecs timeout for the first element
   * @return a CList containing all found elements
   * 
   * @example
   * <pre>{@code
   * // Get notification messages with longer wait for first message
   * CList<E> notifications = notificationList.getElements(15);
   * if (!notifications.isEmpty()) {
   *     System.out.println("Latest notification: " + notifications.get(0).Text._get());
   * }
   * }</pre>
   */
  public CList<E> getElements(int firstWaitSecs) {
    return getElements(firstWaitSecs, 1);
  }

  /**
   * Returns all elements in the list with custom timeouts.
   * 
   * @param firstWaitSecs timeout for the first element
   * @param waitSecs timeout for subsequent elements
   * @return a CList containing all found elements
   * 
   * @example
   * <pre>{@code
   * // Get search results with different wait times
   * CList<E> results = searchResultList.getElements(10, 2);
   * 
   * // Filter and process results
   * results.stream()
   *     .filter(result -> result.Displayed._get())
   *     .forEach(result -> analyzeResult(result));
   * }</pre>
   */
  public CList<E> getElements(int firstWaitSecs, int waitSecs) {
    CList<E> output = new CList<>();
    forEach(output::add, firstWaitSecs, waitSecs);
    return output;
  }

  /**
   * Returns the text content of all elements in the list using default timeout.
   * 
   * @return a CList containing the text of all found elements
   * 
   * @example
   * <pre>{@code
   * CList<String> menuLabels = navigationMenu.getTexts();
   * System.out.println("Available menu options: " + String.join(", ", menuLabels));
   * 
   * // Check if specific text is present
   * boolean hasSettings = menuLabels.contains("Settings");
   * }</pre>
   */
  public CList<String> getTexts() {
    return getTexts(DEFAULT_TIMEOUT);
  }

  /**
   * Returns the text content of all elements with custom timeout for the first element.
   * 
   * @param firstWaitSecs timeout for the first element
   * @return a CList containing the text of all found elements
   * 
   * @example
   * <pre>{@code
   * // Get error messages with longer wait for first error
   * CList<String> errorTexts = errorMessageList.getTexts(8);
   * if (!errorTexts.isEmpty()) {
   *     System.err.println("Validation errors found:");
   *     errorTexts.forEach(error -> System.err.println("- " + error));
   * }
   * }</pre>
   */
  public CList<String> getTexts(int firstWaitSecs) {
    return getTexts(firstWaitSecs, 1);
  }

  /**
   * Returns the text content of all elements with custom timeouts.
   * 
   * @param firstWaitSecs timeout for the first element
   * @param waitSecs timeout for subsequent elements
   * @return a CList containing the text of all found elements
   * 
   * @example
   * <pre>{@code
   * // Get product names from a dynamically loaded list
   * CList<String> productNames = productList.getTexts(12, 3);
   * 
   * // Find products matching criteria
   * List<String> saleItems = productNames.stream()
   *     .filter(name -> name.contains("SALE"))
   *     .collect(Collectors.toList());
   * }</pre>
   */
  public CList<String> getTexts(int firstWaitSecs, int waitSecs) {
    CList<String> output = new CList<>();
    forEach(t -> output.add(t.Text._get()), firstWaitSecs, waitSecs);
    return output;
  }

  /**
   * Tests if ALL elements in the list satisfy the given predicate using default timeout.
   * 
   * @param predicate the condition to test each element against
   * @return true if all elements satisfy the predicate, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Check if all buttons are enabled
   * boolean allButtonsEnabled = buttonList.testAll(btn -> btn.Enabled._get());
   * 
   * // Validate all form fields are filled
   * boolean allFieldsFilled = formFields.testAll(field -> {
   *     String value = field.Value._get();
   *     return value != null && !value.trim().isEmpty();
   * });
   * }</pre>
   */
  public boolean testAll(Predicate<E> predicate) {
    return testAll(predicate, DEFAULT_TIMEOUT);
  }

  /**
   * Tests if ALL elements in the list satisfy the given predicate with custom timeout for first element.
   * 
   * @param predicate the condition to test each element against
   * @param firstWaitSecs timeout for the first element
   * @return true if all elements satisfy the predicate, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Check if all images are loaded (wait longer for first image)
   * boolean allImagesLoaded = imageList.testAll(
   *     img -> img.Present._get() && !img.getAttribute("src").isEmpty(),
   *     15 // wait up to 15 seconds for first image to load
   * );
   * }</pre>
   */
  public boolean testAll(Predicate<E> predicate, int firstWaitSecs) {
    return testAll(predicate, firstWaitSecs, 1);
  }

  /**
   * Tests if ALL elements in the list satisfy the given predicate with custom timeouts.
   * 
   * @param predicate the condition to test each element against
   * @param firstWaitSecs timeout for the first element
   * @param waitSecs timeout for subsequent elements
   * @return true if all elements satisfy the predicate, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Validate all table rows have required data
   * boolean allRowsValid = tableRows.testAll(
   *     row -> {
   *         String id = row.findElement(".//td[1]").getText();
   *         String name = row.findElement(".//td[2]").getText();
   *         return !id.isEmpty() && !name.isEmpty();
   *     },
   *     10, // wait 10 seconds for first row
   *     2   // wait 2 seconds for subsequent rows
   * );
   * }</pre>
   */
  public boolean testAll(Predicate<E> predicate, int firstWaitSecs, int waitSecs) {
    Map<String, Boolean> output = new HashMap<>();
    forEach(
        t -> {
          output.put(t.getName(), predicate.test(t));
        },
        firstWaitSecs,
        waitSecs);
    return output.keySet().stream().allMatch(output::get);
  }

  /**
   * Tests if ANY element in the list satisfies the given predicate using default timeout.
   * 
   * @param predicate the condition to test each element against
   * @return true if at least one element satisfies the predicate, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Check if any notification is an error
   * boolean hasError = notificationList.testAny(
   *     notification -> notification.CssClass._get().contains("error")
   * );
   * 
   * // Check if any product is on sale
   * boolean hasDiscountedItems = productList.testAny(
   *     product -> product.Text._get().contains("SALE")
   * );
   * }</pre>
   */
  public boolean testAny(Predicate<E> predicate) {
    return testAny(predicate, DEFAULT_TIMEOUT);
  }

  /**
   * Tests if ANY element in the list satisfies the given predicate with custom timeout for first element.
   * 
   * @param predicate the condition to test each element against
   * @param firstWaitSecs timeout for the first element
   * @return true if at least one element satisfies the predicate, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Check if any alert message appeared (wait longer for first alert)
   * boolean hasAlert = alertList.testAny(
   *     alert -> alert.Displayed._get(),
   *     12 // wait up to 12 seconds for first alert
   * );
   * }</pre>
   */
  public boolean testAny(Predicate<E> predicate, int firstWaitSecs) {
    return testAny(predicate, firstWaitSecs, 1);
  }

  /**
   * Tests if ANY element in the list satisfies the given predicate with custom timeouts.
   * 
   * @param predicate the condition to test each element against
   * @param firstWaitSecs timeout for the first element
   * @param waitSecs timeout for subsequent elements
   * @return true if at least one element satisfies the predicate, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Check if any validation error exists in the form
   * boolean hasValidationError = formFieldList.testAny(
   *     field -> {
   *         String errorClass = field.CssClass._get();
   *         return errorClass.contains("error") || errorClass.contains("invalid");
   *     },
   *     8, // wait 8 seconds for first field to be validated
   *     1  // wait 1 second for subsequent field validations
   * );
   * }</pre>
   */
  public boolean testAny(Predicate<E> predicate, int firstWaitSecs, int waitSecs) {
    Map<String, Boolean> output = new HashMap<>();
    onFirstMatch(predicate, t -> output.put(t.getName(), true), firstWaitSecs, waitSecs);
    return output.keySet().stream().filter(output::get).count() == 1;
  }

  /**
   * Returns the number of elements in the list using default timeout.
   * 
   * @return the count of elements found
   * 
   * @example
   * <pre>{@code
   * int totalItems = shoppingCartItems.count();
   * System.out.println("Shopping cart contains " + totalItems + " items");
   * 
   * // Use count for validation
   * if (totalItems == 0) {
   *     System.out.println("Cart is empty");
   * } else if (totalItems > 10) {
   *     System.out.println("Cart is getting full!");
   * }
   * }</pre>
   */
  public int count() {
    return getElements(DEFAULT_TIMEOUT).size();
  }

  /**
   * Returns the number of elements in the list with custom timeout for the first element.
   * 
   * @param firstWaitSecs timeout for the first element
   * @return the count of elements found
   * 
   * @example
   * <pre>{@code
   * // Count search results (wait longer for first result)
   * int resultCount = searchResults.count(15);
   * 
   * if (resultCount == 0) {
   *     System.out.println("No results found for the search query");
   * } else {
   *     System.out.println("Found " + resultCount + " search results");
   * }
   * }</pre>
   */
  public int count(int firstWaitSecs) {
    return getElements(firstWaitSecs).size();
  }

  /**
   * Returns the number of elements in the list with custom timeouts.
   * 
   * @param firstWaitSecs timeout for the first element
   * @param waitSecs timeout for subsequent elements
   * @return the count of elements found
   * 
   * @example
   * <pre>{@code
   * // Count table rows with different wait times for performance
   * int rowCount = dataTableRows.count(10, 1);
   * 
   * // Validate expected number of rows
   * Assert.assertTrue("Expected at least 5 rows, but found " + rowCount, 
   *                   rowCount >= 5);
   * 
   * // Use count for pagination logic
   * if (rowCount == ITEMS_PER_PAGE) {
   *     // More pages might be available
   *     checkForNextPage();
   * }
   * }</pre>
   */
  public int count(int firstWaitSecs, int waitSecs) {
    return getElements(firstWaitSecs, waitSecs).size();
  }

  /**
   * Checks if an element exists at the specified index in the list.
   * 
   * @param idx the zero-based index to check
   * @return true if an element exists at the given index, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Check if there are at least 3 items in the list
   * if (itemList.hasRecord(2)) { // index 2 = 3rd item
   *     System.out.println("List has at least 3 items");
   *     E thirdItem = itemList.getRecord(2);
   *     // work with third item
   * }
   * 
   * // Safe access pattern
   * for (int i = 0; itemList.hasRecord(i); i++) {
   *     E item = itemList.getRecord(i);
   *     processItem(item);
   * }
   * }</pre>
   */
  @Override
  public boolean hasRecord(int idx) {
    if (idx == 0) {
      return getRecord(idx).Present.waitIsTrue(waitForFirstElementInSecond, 100);
    }
    return getRecord(idx).Present.waitIsTrue(waitForOtherElementInSecond, 100);
  }

  /**
   * Returns a prefix used in verification messages for this list.
   * 
   * @return the name of this list used in error and verification messages
   * 
   * @example
   * <pre>{@code
   * // When assertions fail, they will include this prefix in the message
   * // For example: "Product List: Expected 5 elements but found 3"
   * String prefix = productList.getVerifyMessagePrefix();
   * System.out.println("Verification prefix: " + prefix);
   * }</pre>
   */
  @Override
  public String getVerifyMessagePrefix() {
    return name;
  }

  /**
   * Compares this list with another iterable collection for equality.
   * 
   * @param expected the expected collection to compare against
   * @return true if the collections are equal, false otherwise
   * 
   * @example
   * <pre>{@code
   * // Compare with expected list of texts
   * List<String> expectedTexts = Arrays.asList("Home", "About", "Contact");
   * CWebList<CWebElement> menuItems = new CWebList<>(...);
   * 
   * // This will compare the actual menu item texts with expected texts
   * boolean isEqual = menuItems.isEqual(expectedTexts);
   * 
   * // Can also compare with another CWebList
   * CWebList<CWebElement> otherList = new CWebList<>(...);
   * boolean listsMatch = menuItems.isEqual(otherList);
   * }</pre>
   */
  @Override
  public boolean isEqual(Iterable<E> expected) {
    return equals(expected);
  }

  /**
   * Returns the web element at the specified index.
   * 
   * @param idx the zero-based index of the element to retrieve
   * @return the web element at the specified index, or null if not found
   * 
   * @example
   * <pre>{@code
   * // Get the first element (index 0)
   * E firstItem = itemList.getRecord(0);
   * if (firstItem != null && firstItem.Present._get()) {
   *     firstItem.click();
   * }
   * 
   * // Get a specific item by index
   * E fifthItem = itemList.getRecord(4); // 5th item (0-based index)
   * 
   * // Safe navigation pattern
   * E item = itemList.getRecord(index);
   * if (item != null && item.Present.waitIsTrue(5)) {
   *     String itemText = item.Text._get();
   *     processItemText(itemText);
   * }
   * }</pre>
   */
  @Override
  public E getRecord(int idx) {
    return controlBuilder.apply(idx, String.format("(%s)[%s]", locator, idx + 1));
  }
}

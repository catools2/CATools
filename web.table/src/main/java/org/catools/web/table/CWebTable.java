package org.catools.web.table;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.functions.CMemoize;
import org.catools.web.collections.CWebIterable;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.catools.web.factory.CWebElementFactory;
import org.catools.web.pages.CWebComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Quotes;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * CWebTable is an abstract base class for web table components that provides comprehensive
 * functionality for interacting with HTML tables in web applications.
 * 
 * <p>This class extends CWebElement and implements CWebComponent and CWebIterable interfaces,
 * providing a rich set of methods for searching, filtering, and manipulating table data.
 * It supports both simple and complex search criteria, predicate-based filtering, and
 * various retrieval patterns.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Dynamic header mapping and column indexing</li>
 *   <li>Flexible search criteria with multiple column support</li>
 *   <li>Predicate-based filtering for complex queries</li>
 *   <li>Various retrieval patterns (first, any, all, etc.)</li>
 *   <li>Thread-safe search criteria management</li>
 *   <li>Configurable XPath expressions for different table structures</li>
 * </ul>
 * 
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * // Create a custom table implementation
 * public class ProductTable extends CWebTable<ChromeDriver, ProductRow> {
 *     public ProductTable(ChromeDriver driver) {
 *         super("Product Table", driver, "//table[@id='products']");
 *     }
 *     
 *     @Override
 *     public ProductRow getRecord(int idx) {
 *         return new ProductRow(driver, getRowXpath(idx));
 *     }
 * }
 * 
 * // Use the table
 * ProductTable table = new ProductTable(driver);
 * 
 * // Find first product with name "iPhone"
 * ProductRow product = table.getFirst("Name", "iPhone");
 * 
 * // Find all products in "Electronics" category
 * CList<ProductRow> electronics = table.getAll("Category", "Electronics");
 * 
 * // Find products with complex criteria
 * Map<String, String> criteria = ImmutableMap.of(
 *     "Category", "Electronics",
 *     "Price", "999"
 * );
 * CList<ProductRow> expensiveElectronics = table.getAll(criteria);
 * }</pre>
 * 
 * @param <DR> the driver type that extends CDriver
 * @param <R> the row type that extends CWebTableRow
 * 
 * @author CATools Team
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class CWebTable<DR extends CDriver, R extends CWebTableRow<DR, ?>>
    extends CWebElement<DR> implements CWebComponent<DR>, CWebIterable<R> {

  protected String searchCriteriaXpathFormat = "[%d][contains(.,%s)]/ancestor::tr[1]";
  protected String tHeadXpath = "/thead";
  protected String headerRowXpath = "/tr";
  protected String headerCellXpath = "/th";
  protected String tBodyXpath = "/tbody";
  protected String rowXpath = "/tr";
  protected String cellXpath = "/td";
  private String baseXpath;
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private CMemoize<CWebTableHeaderInfo<DR>> memoizeHeadersMap = new CMemoize<>(this::readHeaders);

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private final ThreadLocal<CMap<String, String>> searchCriteria = ThreadLocal.withInitial(CHashMap::new);

  /**
   * Constructs a CWebTable with the default timeout.
   * 
   * @param name the name identifier for this table
   * @param driver the web driver instance
   * @param baseXpath the base XPath expression to locate the table element
   * 
   * @example
   * <pre>{@code
   * CWebTable<ChromeDriver, UserRow> userTable = new UserTableImpl(
   *     "Users Table", 
   *     chromeDriver, 
   *     "//table[@id='users']"
   * );
   * }</pre>
   */
  public CWebTable(String name, DR driver, String baseXpath) {
    this(name, driver, baseXpath, CDriver.DEFAULT_TIMEOUT);
  }

  /**
   * Constructs a CWebTable with a custom timeout.
   * 
   * @param name the name identifier for this table
   * @param driver the web driver instance
   * @param baseXpath the base XPath expression to locate the table element
   * @param waitSec the timeout in seconds to wait for elements
   * 
   * @example
   * <pre>{@code
   * CWebTable<ChromeDriver, ProductRow> productTable = new ProductTableImpl(
   *     "Products Table", 
   *     chromeDriver, 
   *     "//div[@class='data-table']//table",
   *     10  // 10 second timeout
   * );
   * }</pre>
   */
  public CWebTable(String name, DR driver, String baseXpath, int waitSec) {
    super(name, driver, By.xpath(baseXpath), waitSec);
    this.baseXpath = baseXpath;
    CWebElementFactory.initElements(this);

    Runtime.getRuntime().addShutdownHook(new Thread(searchCriteria::remove));
  }

  /**
   * Abstract method to get a table row record at the specified index.
   * Implementations must provide logic to create and return the appropriate row type.
   * 
   * @param idx the zero-based index of the row to retrieve
   * @return the row record at the specified index
   * 
   * @example
   * <pre>{@code
   * @Override
   * public UserRow getRecord(int idx) {
   *     return new UserRow(driver, getRowXpath(idx));
   * }
   * }</pre>
   */
  @Override
  public abstract R getRecord(int idx);

  /**
   * Checks if a record exists at the specified index by verifying row visibility.
   * 
   * @param idx the zero-based index of the row to check
   * @return true if the record exists and is visible, false otherwise
   * 
   * @example
   * <pre>{@code
   * if (table.hasRecord(0)) {
   *     UserRow firstUser = table.getRecord(0);
   *     // Process first user
   * }
   * }</pre>
   */
  @Override
  public boolean hasRecord(int idx) {
    return isDataAvailable() && driver.$(getRowXpath(idx)).isVisible(0);
  }

  /**
   * Retrieves all rows that match the specified header-value pair.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @return a list of all matching rows
   * 
   * @example
   * <pre>{@code
   * // Get all users with status "Active"
   * CList<UserRow> activeUsers = table.getAll("Status", "Active");
   * 
   * // Get all products with category "Electronics"
   * CList<ProductRow> electronics = table.getAll("Category", "Electronics");
   * }</pre>
   */
  public CList<R> getAll(String header, String value) {
    return getAll(ImmutableMap.of(header, value));
  }

  /**
   * Retrieves all rows that match the specified search criteria map.
   * Multiple criteria are combined with AND logic.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @return a list of all matching rows
   * 
   * @example
   * <pre>{@code
   * // Get all active premium users
   * Map<String, String> criteria = ImmutableMap.of(
   *     "Status", "Active",
   *     "Plan", "Premium"
   * );
   * CList<UserRow> premiumUsers = table.getAll(criteria);
   * }</pre>
   */
  public CList<R> getAll(Map<String, String> searchCriteria) {
    return performActionOnTable(searchCriteria, this::getAll);
  }

  /**
   * Retrieves all rows that match the header-value pair and satisfy the predicate.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate additional filtering condition
   * @return a list of all matching rows that satisfy the predicate
   * 
   * @example
   * <pre>{@code
   * // Get all active users whose names start with "John"
   * CList<UserRow> johnUsers = table.getAll("Status", "Active", 
   *     user -> user.getName().startsWith("John"));
   * }</pre>
   */
  public CList<R> getAll(String header, String value, Predicate<R> predicate) {
    return getAll(ImmutableMap.of(header, value), predicate);
  }

  /**
   * Retrieves all rows that match the search criteria and satisfy the predicate.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate additional filtering condition
   * @return a list of all matching rows that satisfy the predicate
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of(
   *     "Department", "Engineering",
   *     "Location", "San Francisco"
   * );
   * 
   * // Get all engineers in SF with salary > 100k
   * CList<EmployeeRow> highPaidEngineers = table.getAll(criteria, 
   *     emp -> emp.getSalary() > 100000);
   * }</pre>
   */
  public CList<R> getAll(Map<String, String> searchCriteria, Predicate<R> predicate) {
    return performActionOnTable(searchCriteria, () -> getAll(predicate));
  }

  /**
   * Retrieves any (random) row that matches the specified header-value pair.
   * Useful when you need any matching record rather than a specific one.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @return any matching row, or null if none found
   * 
   * @example
   * <pre>{@code
   * // Get any available product for testing
   * ProductRow anyProduct = table.getAny("Status", "Available");
   * }</pre>
   */
  public R getAny(String header, String value) {
    return getAny(ImmutableMap.of(header, value));
  }

  /**
   * Retrieves any (random) row that matches the specified search criteria.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @return any matching row, or null if none found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of(
   *     "Status", "Active",
   *     "Type", "Premium"
   * );
   * UserRow anyPremiumUser = table.getAny(criteria);
   * }</pre>
   */
  public R getAny(Map<String, String> searchCriteria) {
    return performActionOnTable(searchCriteria, this::getRandom);
  }

  /**
   * Retrieves the first row that matches the specified header-value pair.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @return the first matching row
   * @throws RuntimeException if no matching row is found
   * 
   * @example
   * <pre>{@code
   * // Get the first user with email "john@example.com"
   * UserRow john = table.getFirst("Email", "john@example.com");
   * }</pre>
   */
  public R getFirst(String header, String value) {
    return getFirst(ImmutableMap.of(header, value));
  }

  /**
   * Retrieves the first row that matches the specified search criteria.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @return the first matching row
   * @throws RuntimeException if no matching row is found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of(
   *     "Status", "Active",
   *     "Role", "Admin"
   * );
   * UserRow firstAdmin = table.getFirst(criteria);
   * }</pre>
   */
  public R getFirst(Map<String, String> searchCriteria) {
    return performActionOnTable(searchCriteria, this::getFirst);
  }

  /**
   * Retrieves the first row that matches the header-value pair and satisfies the predicate.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate additional filtering condition
   * @return the first matching row that satisfies the predicate
   * @throws RuntimeException if no matching row is found
   * 
   * @example
   * <pre>{@code
   * // Get first active user whose name contains "Smith"
   * UserRow smith = table.getFirst("Status", "Active", 
   *     user -> user.getName().contains("Smith"));
   * }</pre>
   */
  public R getFirst(String header, String value, Predicate<R> predicate) {
    return getFirst(ImmutableMap.of(header, value), predicate);
  }

  /**
   * Retrieves the first row that matches the search criteria and satisfies the predicate.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate additional filtering condition
   * @return the first matching row that satisfies the predicate
   * @throws RuntimeException if no matching row is found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of(
   *     "Department", "Sales",
   *     "Location", "New York"
   * );
   * 
   * EmployeeRow topSalesRep = table.getFirst(criteria, 
   *     emp -> emp.getSalesTarget() > 50000);
   * }</pre>
   */
  public R getFirst(Map<String, String> searchCriteria, Predicate<R> predicate) {
    return performActionOnTable(searchCriteria, () -> getFirst(predicate));
  }

  /**
   * Retrieves the first matching row or returns the provided alternative if none found.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param other the alternative row to return if no match is found
   * @return the first matching row, or the alternative if none found
   * 
   * @example
   * <pre>{@code
   * UserRow defaultUser = new UserRow("Default User");
   * UserRow user = table.getFirstOrElse("Email", "test@example.com", defaultUser);
   * }</pre>
   */
  public R getFirstOrElse(String header, String value, R other) {
    return getFirstOrElse(ImmutableMap.of(header, value), other);
  }

  /**
   * Retrieves the first matching row or returns the provided alternative if none found.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param other the alternative row to return if no match is found
   * @return the first matching row, or the alternative if none found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Role", "SuperAdmin");
   * UserRow defaultAdmin = new UserRow("Default Admin");
   * UserRow admin = table.getFirstOrElse(criteria, defaultAdmin);
   * }</pre>
   */
  public R getFirstOrElse(Map<String, String> searchCriteria, R other) {
    return performActionOnTable(searchCriteria, () -> getFirstOrElse(other));
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or returns the alternative.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate additional filtering condition
   * @param other the alternative row to return if no match is found
   * @return the first matching row that satisfies the predicate, or the alternative
   * 
   * @example
   * <pre>{@code
   * UserRow guestUser = new UserRow("Guest");
   * UserRow premiumUser = table.getFirstOrElse("Status", "Active", 
   *     user -> user.isPremium(), guestUser);
   * }</pre>
   */
  public R getFirstOrElse(String header, String value, Predicate<R> predicate, R other) {
    return getFirstOrElse(ImmutableMap.of(header, value), predicate, other);
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or returns the alternative.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate additional filtering condition
   * @param other the alternative row to return if no match is found
   * @return the first matching row that satisfies the predicate, or the alternative
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Department", "IT");
   * EmployeeRow defaultIT = new EmployeeRow("Default IT");
   * 
   * EmployeeRow seniorIT = table.getFirstOrElse(criteria, 
   *     emp -> emp.getExperience() > 5, defaultIT);
   * }</pre>
   */
  public R getFirstOrElse(Map<String, String> searchCriteria, Predicate<R> predicate, R other) {
    return performActionOnTable(searchCriteria, () -> getFirstOrElse(predicate, other));
  }

  /**
   * Retrieves the first matching row or gets an alternative using the provided supplier.
   * The supplier is only called if no match is found, providing lazy evaluation.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param other the supplier to provide an alternative row if no match is found
   * @return the first matching row, or the result of the supplier if none found
   * 
   * @example
   * <pre>{@code
   * UserRow user = table.getFirstOrElseGet("Email", "test@example.com", 
   *     () -> createDefaultUser());
   * }</pre>
   */
  public R getFirstOrElseGet(String header, String value, Supplier<R> other) {
    return getFirstOrElseGet(ImmutableMap.of(header, value), other);
  }

  /**
   * Retrieves the first matching row or gets an alternative using the provided supplier.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param other the supplier to provide an alternative row if no match is found
   * @return the first matching row, or the result of the supplier if none found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Role", "Manager");
   * UserRow manager = table.getFirstOrElseGet(criteria, 
   *     () -> createTemporaryManager());
   * }</pre>
   */
  public R getFirstOrElseGet(Map<String, String> searchCriteria, Supplier<R> other) {
    return performActionOnTable(searchCriteria, () -> getFirstOrElseGet(other));
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or gets an alternative
   * using the provided supplier.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate additional filtering condition
   * @param other the supplier to provide an alternative row if no match is found
   * @return the first matching row that satisfies the predicate, or the result of the supplier
   * 
   * @example
   * <pre>{@code
   * UserRow activeUser = table.getFirstOrElseGet("Status", "Active", 
   *     user -> user.getLastLogin().isAfter(yesterday),
   *     () -> findBackupUser());
   * }</pre>
   */
  public R getFirstOrElseGet(
      String header, String value, Predicate<R> predicate, Supplier<R> other) {
    return getFirstOrElseGet(ImmutableMap.of(header, value), predicate, other);
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or gets an alternative
   * using the provided supplier.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate additional filtering condition
   * @param other the supplier to provide an alternative row if no match is found
   * @return the first matching row that satisfies the predicate, or the result of the supplier
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Department", "Sales");
   * EmployeeRow topSales = table.getFirstOrElseGet(criteria,
   *     emp -> emp.getSalesAmount() > 100000,
   *     () -> findAverageSalesEmployee());
   * }</pre>
   */
  public R getFirstOrElseGet(
      Map<String, String> searchCriteria, Predicate<R> predicate, Supplier<R> other) {
    return performActionOnTable(searchCriteria, () -> getFirstOrElseGet(predicate, other));
  }

  /**
   * Retrieves the first matching row or returns null if none found.
   * Provides a null-safe way to search for rows without throwing exceptions.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @return the first matching row, or null if none found
   * 
   * @example
   * <pre>{@code
   * UserRow user = table.getFirstOrNull("Email", "unknown@example.com");
   * if (user != null) {
   *     // Process user
   * } else {
   *     // Handle case where user not found
   * }
   * }</pre>
   */
  public R getFirstOrNull(String header, String value) {
    return getFirstOrNull(ImmutableMap.of(header, value));
  }

  /**
   * Retrieves the first matching row or returns null if none found.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @return the first matching row, or null if none found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of(
   *     "Status", "Inactive",
   *     "Type", "Trial"
   * );
   * UserRow inactiveTrialUser = table.getFirstOrNull(criteria);
   * }</pre>
   */
  public R getFirstOrNull(Map<String, String> searchCriteria) {
    return performActionOnTable(searchCriteria, this::getFirstOrNull);
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or returns null.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate additional filtering condition
   * @return the first matching row that satisfies the predicate, or null if none found
   * 
   * @example
   * <pre>{@code
   * UserRow recentUser = table.getFirstOrNull("Status", "Active", 
   *     user -> user.getCreatedDate().isAfter(lastWeek));
   * }</pre>
   */
  public R getFirstOrNull(String header, String value, Predicate<R> predicate) {
    return getFirstOrNull(ImmutableMap.of(header, value), predicate);
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or returns null.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate additional filtering condition
   * @return the first matching row that satisfies the predicate, or null if none found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Role", "Manager");
   * EmployeeRow youngManager = table.getFirstOrNull(criteria, 
   *     emp -> emp.getAge() < 35);
   * }</pre>
   */
  public R getFirstOrNull(Map<String, String> searchCriteria, Predicate<R> predicate) {
    return performActionOnTable(searchCriteria, () -> getFirstOrNull(predicate));
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or any matching row
   * if no row satisfies the predicate. This is useful when you prefer specific criteria
   * but will accept any match as fallback.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate preferred filtering condition
   * @return the first matching row that satisfies the predicate, or any matching row as fallback
   * 
   * @example
   * <pre>{@code
   * // Prefer active users, but accept any user with matching email
   * UserRow user = table.getFirstOrAny("Email", "test@example.com", 
   *     u -> u.getStatus().equals("Active"));
   * }</pre>
   */
  public R getFirstOrAny(String header, String value, Predicate<R> predicate) {
    return getFirstOrAny(ImmutableMap.of(header, value), predicate);
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or any matching row
   * if no row satisfies the predicate.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate preferred filtering condition
   * @return the first matching row that satisfies the predicate, or any matching row as fallback
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Department", "Engineering");
   * // Prefer senior engineers, but accept any engineer
   * EmployeeRow engineer = table.getFirstOrAny(criteria, 
   *     emp -> emp.getLevel().equals("Senior"));
   * }</pre>
   */
  public R getFirstOrAny(Map<String, String> searchCriteria, Predicate<R> predicate) {
    return performActionOnTable(searchCriteria, () -> getFirstOrAny(predicate));
  }

  /**
   * Retrieves the first matching row or throws the specified exception if none found.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param e the exception to throw if no match is found
   * @return the first matching row
   * @throws RuntimeException the provided exception if no match is found
   * 
   * @example
   * <pre>{@code
   * UserRow admin = table.getFirstOrThrow("Role", "Admin", 
   *     new IllegalStateException("No admin user found"));
   * }</pre>
   */
  public R getFirstOrThrow(String header, String value, RuntimeException e) {
    return getFirstOrThrow(ImmutableMap.of(header, value), e);
  }

  /**
   * Retrieves the first matching row or throws the specified exception if none found.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param e the exception to throw if no match is found
   * @return the first matching row
   * @throws RuntimeException the provided exception if no match is found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Status", "SuperUser");
   * UserRow superUser = table.getFirstOrThrow(criteria, 
   *     new SecurityException("SuperUser not found"));
   * }</pre>
   */
  public R getFirstOrThrow(Map<String, String> searchCriteria, RuntimeException e) {
    return performActionOnTable(searchCriteria, () -> getFirstOrThrow(e));
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or throws an exception
   * generated by the supplier if none found.
   * 
   * @param <X> the exception type
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate additional filtering condition
   * @param exceptionSupplier supplier that generates the exception to throw
   * @return the first matching row that satisfies the predicate
   * @throws X the generated exception if no match is found
   * 
   * @example
   * <pre>{@code
   * UserRow premiumUser = table.getFirstOrThrow("Status", "Active", 
   *     user -> user.isPremium(),
   *     () -> new BusinessException("No premium active users found"));
   * }</pre>
   */
  public <X extends RuntimeException> R getFirstOrThrow(
      String header,
      String value,
      Predicate<R> predicate,
      Supplier<? extends X> exceptionSupplier) {
    return getFirstOrThrow(ImmutableMap.of(header, value), predicate, exceptionSupplier);
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or throws an exception
   * generated by the supplier if none found.
   * 
   * @param <X> the exception type
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate additional filtering condition
   * @param exceptionSupplier supplier that generates the exception to throw
   * @return the first matching row that satisfies the predicate
   * @throws X the generated exception if no match is found
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Department", "Finance");
   * EmployeeRow seniorFinance = table.getFirstOrThrow(criteria,
   *     emp -> emp.getExperience() > 10,
   *     () -> new HRException("No senior finance employees available"));
   * }</pre>
   */
  public <X extends RuntimeException> R getFirstOrThrow(
      Map<String, String> searchCriteria,
      Predicate<R> predicate,
      Supplier<? extends X> exceptionSupplier) {
    return performActionOnTable(searchCriteria, () -> getFirstOrThrow(predicate, exceptionSupplier));
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or gets an alternative
   * using the provided supplier. This is an overloaded method providing flexibility 
   * in parameter ordering.
   * 
   * @param header the column header name to search in
   * @param value the value to search for in the specified column
   * @param predicate additional filtering condition
   * @param other the supplier to provide an alternative row if no match is found
   * @return the first matching row that satisfies the predicate, or the result of the supplier
   * 
   * @example
   * <pre>{@code
   * UserRow recentActiveUser = table.getFirstOrElse("Status", "Active", 
   *     user -> user.getLastLogin().isAfter(yesterday),
   *     () -> getDefaultUser());
   * }</pre>
   */
  public R getFirstOrElse(String header, String value, Predicate<R> predicate, Supplier<R> other) {
    return getFirstOrElse(ImmutableMap.of(header, value), predicate, other);
  }

  /**
   * Retrieves the first matching row that satisfies the predicate, or gets an alternative
   * using the provided supplier.
   * 
   * @param searchCriteria a map of column headers to values for filtering
   * @param predicate additional filtering condition
   * @param other the supplier to provide an alternative row if no match is found
   * @return the first matching row that satisfies the predicate, or the result of the supplier
   * 
   * @example
   * <pre>{@code
   * Map<String, String> criteria = ImmutableMap.of("Team", "DevOps");
   * EmployeeRow leadDevOps = table.getFirstOrElse(criteria,
   *     emp -> emp.getRole().equals("Lead"),
   *     () -> assignTempLead());
   * }</pre>
   */
  public R getFirstOrElse(
      Map<String, String> searchCriteria, Predicate<R> predicate, Supplier<R> other) {
    return performActionOnTable(searchCriteria, () -> getFirstOrElse(predicate, other));
  }

  /**
   * Retrieves a header element by its column name.
   * 
   * @param headerName the name of the header column
   * @return the header web element
   * @throws RuntimeException if the header is not found
   * 
   * @example
   * <pre>{@code
   * CWebElement<DR> nameHeader = table.getHeader("Full Name");
   * if (nameHeader.isVisible()) {
   *     nameHeader.click(); // Sort by name
   * }
   * }</pre>
   */
  public CWebElement<DR> getHeader(String headerName) {
    return getHeader(getHeaderIndex(headerName));
  }

  /**
   * Retrieves a header element by its column index (1-based).
   * 
   * @param idx the 1-based index of the header column
   * @return the header web element
   * 
   * @example
   * <pre>{@code
   * // Get the first header column
   * CWebElement<DR> firstHeader = table.getHeader(1);
   * firstHeader.click(); // Sort by first column
   * }</pre>
   */
  public CWebElement<DR> getHeader(int idx) {
    return new CWebElement<>(
        "Header " + idx,
        driver,
        By.xpath(
            String.format(
                "(%s)[%d]", baseXpath + tHeadXpath + headerRowXpath + headerCellXpath, idx)));
  }

  /**
   * Gets the 1-based index of a header column by its name.
   * 
   * @param header the header column name
   * @return the 1-based index of the header column
   * @throws RuntimeException if the header is not found
   * 
   * @example
   * <pre>{@code
   * Integer emailIndex = table.getHeaderIndex("Email Address");
   * System.out.println("Email column is at position: " + emailIndex);
   * }</pre>
   */
  public Integer getHeaderIndex(String header) {
    return memoizeHeadersMap.get().getHeaderIndex(header);
  }

  /**
   * Gets a map of header indices to header names for all columns.
   * This uses cached results for performance.
   * 
   * @return a map where keys are 1-based column indices and values are header names
   * 
   * @example
   * <pre>{@code
   * CMap<Integer, String> headers = table.getHeadersMap();
   * headers.forEach((index, name) -> 
   *     System.out.println("Column " + index + ": " + name));
   * }</pre>
   */
  public CMap<Integer, String> getHeadersMap() {
    return getHeadersMap(false);
  }

  /**
   * Gets a map of header indices to header names, with option to reset cache.
   * 
   * @param reset if true, clears the cached header information and re-reads from DOM
   * @return a map where keys are 1-based column indices and values are header names
   * 
   * @example
   * <pre>{@code
   * // Force refresh of header information
   * CMap<Integer, String> freshHeaders = table.getHeadersMap(true);
   * 
   * // Use cached headers (faster)
   * CMap<Integer, String> cachedHeaders = table.getHeadersMap(false);
   * }</pre>
   */
  public CMap<Integer, String> getHeadersMap(boolean reset) {
    if (reset) {
      memoizeHeadersMap.reset();
    }
    return memoizeHeadersMap.get().getHeadersMap();
  }

  /**
   * Gets a map of visible header indices to header names, with option to reset cache.
   * Only includes headers that are currently visible in the DOM.
   * 
   * @param reset if true, clears the cached header information and re-reads from DOM
   * @return a map where keys are 1-based column indices and values are visible header names
   * 
   * @example
   * <pre>{@code
   * // Get only visible headers (useful for responsive tables)
   * CMap<Integer, String> visibleHeaders = table.getVisibleHeadersMap(true);
   * System.out.println("Visible columns: " + visibleHeaders.size());
   * }</pre>
   */
  public CMap<Integer, String> getVisibleHeadersMap(boolean reset) {
    if (reset) {
      memoizeHeadersMap.reset();
    }
    return memoizeHeadersMap.get().getVisibleHeadersMap();
  }

  /**
   * Checks if the table contains any data rows.
   * This method verifies that at least one data row exists in the table body.
   * 
   * @return true if data is available in the table, false otherwise
   * 
   * @example
   * <pre>{@code
   * if (table.isDataAvailable()) {
   *     CList<UserRow> users = table.getAll();
   *     processUsers(users);
   * } else {
   *     System.out.println("No data found in table");
   * }
   * }</pre>
   */
  public boolean isDataAvailable() {
    if (CDriverConfigs.waitCompleteReadyStateBeforeEachAction()) {
      driver.waitCompleteReadyState();
    }
    return driver.getElement(
        By.xpath(String.format("(%s)[1]", baseXpath + tBodyXpath + rowXpath)), waitSec)
        != null;
  }

  /**
   * Generates the XPath for a specific table row based on current search criteria and index.
   * This method constructs a dynamic XPath that incorporates any active search criteria
   * to locate the exact row needed.
   * 
   * @param idx the zero-based index of the row within the filtered results
   * @return the complete XPath string to locate the specified row
   * 
   * @example
   * <pre>{@code
   * // With search criteria set for "Status" = "Active"
   * String rowXpath = table.getRowXpath(0); // Gets first active user row
   * 
   * // Without search criteria
   * String rowXpath = table.getRowXpath(2); // Gets third row in table
   * }</pre>
   */
  public String getRowXpath(int idx) {
    StringBuilder searchXpath = new StringBuilder(StringUtils.EMPTY);
    String rowCellLocatorByIndexAndText = cellXpath + searchCriteriaXpathFormat;
    if (searchCriteria.get() != null && searchCriteria.get().isNotEmpty()) {
      for (Map.Entry<String, String> entry : searchCriteria.get().entrySet()) {
        searchXpath.append(String.format(
            rowCellLocatorByIndexAndText,
            getHeadersMap().getFirstKeyByValue(entry.getKey()),
            Quotes.escape(entry.getValue())));
      }
    }
    return String.format("(%s)[%s]", baseXpath + tBodyXpath + rowXpath + searchXpath, idx + 1);
  }

  protected CWebTableHeaderInfo<DR> readHeaders() {
    return new CWebTableHeaderInfo<>(driver, baseXpath + tHeadXpath + headerRowXpath + headerCellXpath);
  }

  protected <O> O performActionOnTable(Map<String, String> criteria, Supplier<O> supplier) {
    setSearchCriteria(criteria);
    O o = supplier.get();
    clearSearchCriteria();
    return o;
  }

  protected void setSearchCriteria(Map<String, String> searchCriteria) {
    clearSearchCriteria();
    logger.debug("Set Search Criteria to " + searchCriteria);
    this.searchCriteria.get().putAll(searchCriteria);
  }

  protected void clearSearchCriteria() {
    logger.debug("Clear Search Criteria");
    this.searchCriteria.get().clear();
  }
}

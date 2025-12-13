package org.catools.web.table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.utils.CSleeper;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.catools.web.selectors.CBy;

import javax.ws.rs.NotSupportedException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Abstract class for handling multi-page web tables with pagination controls.
 * This class extends {@link CWebTable} to provide functionality for navigating
 * through paginated table data using first, previous, next, and last page controls.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create a custom multi-page table implementation
 * public class MyMultiPageTable extends CWebMultiPageTable<MyDriver, MyTableRow> {
 *     public MyMultiPageTable(MyDriver driver) {
 *         super("My Table",
 *               driver,
 *               "//table[@id='data-table']",
 *               CBy.id("first-page"),
 *               CBy.id("prev-page"),
 *               CBy.id("next-page"),
 *               CBy.id("last-page"));
 *     }
 *
 *     @Override
 *     public String getCurrentPageNumber() {
 *         return driver.$(CBy.id("current-page")).getText();
 *     }
 *
 *     @Override
 *     protected MyTableRow createRow(int index) {
 *         return new MyTableRow(driver, baseXpath + "/tbody/tr[" + (index + 1) + "]");
 *     }
 * }
 *
 * // Usage
 * MyMultiPageTable table = new MyMultiPageTable(driver);
 *
 * // Iterate through all pages automatically
 * for (MyTableRow row : table) {
 *     System.out.println(row.getText());
 * }
 *
 * // Get total count across all pages
 * int totalRecords = table.getTotalRecordCount();
 * }</pre>
 *
 * @param <DR> the driver type extending {@link CDriver}
 * @param <R>  the table row type extending {@link CWebTableRow}
 * @author CATools Team
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class CWebMultiPageTable<DR extends CDriver, R extends CWebTableRow<DR, ?>>
    extends CWebTable<DR, R> {
  private final CWebElement<DR> firstLink;
  private final CWebElement<DR> previousLink;
  private final CWebElement<DR> nextLink;
  private final CWebElement<DR> lastLink;
  private final int maxNumberOfPageToIterate;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private final ThreadLocal<Boolean> singlePageMode = ThreadLocal.withInitial(() -> false);

  /**
   * Constructs a multi-page table with default timeout settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(
   *     "Users Table",
   *     driver,
   *     "//table[@class='users']",
   *     CBy.xpath("//a[@class='first']"),
   *     CBy.xpath("//a[@class='prev']"),
   *     CBy.xpath("//a[@class='next']"),
   *     CBy.xpath("//a[@class='last']")
   * );
   * }</pre>
   *
   * @param name            the name of the table for logging purposes
   * @param driver          the web driver instance
   * @param baseXpath       the base XPath to locate the table
   * @param firstLocator    locator for the "first page" button
   * @param previousLocator locator for the "previous page" button
   * @param nextLocator     locator for the "next page" button
   * @param lastLocator     locator for the "last page" button
   */
  public CWebMultiPageTable(
      String name,
      DR driver,
      String baseXpath,
      CBy firstLocator,
      CBy previousLocator,
      CBy nextLocator,
      CBy lastLocator) {
    this(
        name,
        driver,
        baseXpath,
        firstLocator,
        previousLocator,
        nextLocator,
        lastLocator,
        DEFAULT_TIMEOUT);
  }

  /**
   * Constructs a multi-page table with custom timeout settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(
   *     "Users Table",
   *     driver,
   *     "//table[@class='users']",
   *     CBy.xpath("//a[@class='first']"),
   *     CBy.xpath("//a[@class='prev']"),
   *     CBy.xpath("//a[@class='next']"),
   *     CBy.xpath("//a[@class='last']"),
   *     30  // 30 seconds timeout
   * );
   * }</pre>
   *
   * @param name            the name of the table for logging purposes
   * @param driver          the web driver instance
   * @param baseXpath       the base XPath to locate the table
   * @param firstLocator    locator for the "first page" button
   * @param previousLocator locator for the "previous page" button
   * @param nextLocator     locator for the "next page" button
   * @param lastLocator     locator for the "last page" button
   * @param waitSec         timeout in seconds for element operations
   */
  public CWebMultiPageTable(
      String name,
      DR driver,
      String baseXpath,
      CBy firstLocator,
      CBy previousLocator,
      CBy nextLocator,
      CBy lastLocator,
      int waitSec) {
    this(
        name,
        driver,
        baseXpath,
        firstLocator,
        previousLocator,
        nextLocator,
        lastLocator,
        waitSec,
        100);
  }

  /**
   * Constructs a multi-page table with custom timeout and maximum page iteration settings.
   *
   * <p>Example:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(
   *     "Users Table",
   *     driver,
   *     "//table[@class='users']",
   *     CBy.xpath("//a[@class='first']"),
   *     CBy.xpath("//a[@class='prev']"),
   *     CBy.xpath("//a[@class='next']"),
   *     CBy.xpath("//a[@class='last']"),
   *     30,  // 30 seconds timeout
   *     50   // maximum 50 pages to iterate
   * );
   * }</pre>
   *
   * @param name                     the name of the table for logging purposes
   * @param driver                   the web driver instance
   * @param baseXpath                the base XPath to locate the table
   * @param firstLocator             locator for the "first page" button
   * @param previousLocator          locator for the "previous page" button
   * @param nextLocator              locator for the "next page" button
   * @param lastLocator              locator for the "last page" button
   * @param waitSec                  timeout in seconds for element operations
   * @param maxNumberOfPageToIterate maximum number of pages to iterate through
   */
  public CWebMultiPageTable(
      String name,
      DR driver,
      String baseXpath,
      CBy firstLocator,
      CBy previousLocator,
      CBy nextLocator,
      CBy lastLocator,
      int waitSec,
      int maxNumberOfPageToIterate) {
    super(name, driver, baseXpath, waitSec);
    this.firstLink = new CWebElement<>("First", driver, firstLocator);
    this.previousLink = new CWebElement<>("Previous", driver, previousLocator);
    this.nextLink = new CWebElement<>("Next", driver, nextLocator);
    this.lastLink = new CWebElement<>("Last", driver, lastLocator);
    this.maxNumberOfPageToIterate = maxNumberOfPageToIterate;

    Runtime.getRuntime().addShutdownHook(new Thread(singlePageMode::remove));
  }

  /**
   * Gets the current page number as a string.
   * This method must be implemented by subclasses to provide the current page number
   * from the specific pagination control used by the table.
   *
   * <p>Example implementation:</p>
   * <pre>{@code
   * @Override
   * public String getCurrentPageNumber() {
   *     return driver.$(CBy.xpath("//span[@class='current-page']")).getText();
   * }
   * }</pre>
   *
   * @return the current page number as a string, or empty string if not available
   */
  public abstract String getCurrentPageNumber();

  /**
   * Gets the total number of records across all pages of the table.
   * This method navigates through all pages to count the total records.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   * int totalCount = table.getTotalRecordCount();
   * System.out.println("Total records: " + totalCount);
   * }</pre>
   *
   * @return the total number of records across all pages
   */
  public int getTotalRecordCount() {
    return performActionOnTable(new HashMap<>(), () -> (int) getAll().stream().count());
  }

  /**
   * Gets the number of records on the current page only.
   * This method does not navigate to other pages.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   * table.gotoFirstPage();
   * int currentPageCount = table.getCurrentPageRecordCount();
   * System.out.println("Records on current page: " + currentPageCount);
   * }</pre>
   *
   * @return the number of records on the current page
   */
  public int getCurrentPageRecordCount() {
    return performActionOnCurrentPage(new HashMap<>(), () -> (int) getAll().stream().count());
  }

  /**
   * Navigates to the first page of the table.
   * First attempts to click the "first page" button if available and clickable.
   * If not available, it will repeatedly click "previous page" until reaching the first page.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   * if (table.gotoFirstPage()) {
   *     System.out.println("Successfully navigated to first page");
   * } else {
   *     System.out.println("Could not navigate to first page");
   * }
   * }</pre>
   *
   * @return true if successfully navigated to the first page, false otherwise
   */
  public boolean gotoFirstPage() {
    logger.trace("Go to first page.");
    if (firstLink.isClickable(0)) {
      firstLink.click();
      return true;
    } else {
      int counter = maxNumberOfPageToIterate;
      while (gotoPreviousPage() && counter-- > 0) ;
      return counter < maxNumberOfPageToIterate;
    }
  }

  /**
   * Navigates to the previous page of the table.
   * Checks if the "previous page" button is present and enabled before attempting to click it.
   * Validates the navigation by comparing page numbers before and after the click.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   * while (table.gotoPreviousPage()) {
   *     System.out.println("Moved to previous page: " + table.getCurrentPageNumber());
   * }
   * System.out.println("Reached first page or no more previous pages available");
   * }</pre>
   *
   * @return true if successfully navigated to the previous page, false if already on first page or navigation failed
   */
  public boolean gotoPreviousPage() {
    if (previousLink.Present.isFalse() || previousLink.Enabled.isFalse()) {
      return false;
    }
    String currentPageNumber = getCurrentPageNumber();
    logger.trace("Go to previous page from page {}.", currentPageNumber);
    previousLink.click();
    if (StringUtils.isNotBlank(currentPageNumber)) {
      isDataAvailable();
      if (currentPageNumber.equals(getCurrentPageNumber())) {
        return false;
      }
    }
    CSleeper.sleepTightInSeconds(1);
    return true;
  }

  /**
   * Navigates to the next page of the table.
   * Checks if the "next page" button is present and enabled before attempting to click it.
   * Validates the navigation by comparing page numbers before and after the click.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   * while (table.gotoNextPage()) {
   *     System.out.println("Moved to next page: " + table.getCurrentPageNumber());
   *     // Process current page data
   *     for (MyTableRow row : table.getCurrentPageRecords()) {
   *         // Process each row
   *     }
   * }
   * System.out.println("Reached last page or no more next pages available");
   * }</pre>
   *
   * @return true if successfully navigated to the next page, false if already on last page or navigation failed
   */
  public boolean gotoNextPage() {
    if (nextLink.Present.isFalse() || nextLink.Enabled.isFalse()) {
      return false;
    }
    String currentPageNumber = getCurrentPageNumber();
    if (StringUtils.isBlank(currentPageNumber))
      logger.trace("Go to next page from current page.");
    else
      logger.trace("Go to next page from page {}.", currentPageNumber);
    nextLink.click();
    return StringUtils.isBlank(currentPageNumber)
        || !currentPageNumber.equals(getCurrentPageNumber());
  }

  /**
   * Navigates to the last page of the table.
   * First attempts to click the "last page" button if available and clickable.
   * If not available, it will repeatedly click "next page" until reaching the last page.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   * if (table.gotoLastPage()) {
   *     System.out.println("Successfully navigated to last page");
   *     System.out.println("Current page: " + table.getCurrentPageNumber());
   * } else {
   *     System.out.println("Could not navigate to last page");
   * }
   * }</pre>
   *
   * @return true if successfully navigated to the last page, false otherwise
   */
  public boolean gotoLastPage() {
    logger.trace("Go to last page.");
    if (lastLink.isClickable(0)) {
      lastLink.click();
      return true;
    } else {
      int counter = maxNumberOfPageToIterate;
      while (gotoNextPage() && counter-- > 0) ;
      return counter < maxNumberOfPageToIterate;
    }
  }

  /**
   * Returns an iterator that automatically handles pagination.
   * The iterator will navigate through all pages and return all rows across all pages.
   * If in single page mode, it behaves like the parent class iterator (current page only).
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   *
   * // Iterate through all rows across all pages
   * for (MyTableRow row : table) {
   *     System.out.println("Row data: " + row.getText());
   *     if (row.containsText("target")) {
   *         System.out.println("Found target row on page: " + table.getCurrentPageNumber());
   *         break;
   *     }
   * }
   *
   * // Alternative using iterator directly
   * Iterator<MyTableRow> iterator = table.iterator();
   * while (iterator.hasNext()) {
   *     MyTableRow row = iterator.next();
   *     // Process row
   * }
   * }</pre>
   *
   * @return an iterator that handles pagination automatically
   */
  @Override
  public Iterator<R> iterator() {
    if (singlePageMode.get())
      return super.iterator();
    return iterateWithPagination();
  }

  /**
   * Performs an action on the current page only without navigating to other pages.
   * This method temporarily enables single page mode to ensure the action is performed
   * only on the current page's data.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * CWebMultiPageTable table = new MyMultiPageTable(driver);
   * table.gotoFirstPage();
   *
   * // Get count of records on current page only
   * Map<String, String> criteria = new HashMap<>();
   * criteria.put("status", "active");
   *
   * int currentPageActiveCount = table.performActionOnCurrentPage(criteria, () -> {
   *     return (int) table.getAll().stream()
   *         .filter(row -> row.getColumnValue("status").equals("active"))
   *         .count();
   * });
   *
   * System.out.println("Active records on current page: " + currentPageActiveCount);
   * }</pre>
   *
   * @param <O>      the return type of the supplier
   * @param criteria search criteria for filtering (can be empty)
   * @param supplier the action to perform on the current page
   * @return the result of the supplier action
   */
  protected synchronized <O> O performActionOnCurrentPage(Map<String, String> criteria, Supplier<O> supplier) {
    singlePageMode.set(true);
    O o = super.performActionOnTable(criteria, supplier);
    singlePageMode.set(false);
    return o;
  }

  private Iterator<R> iterateWithPagination() {
    gotoFirstPage();
    return new Iterator<>() {
      int counter = maxNumberOfPageToIterate;
      int cursor = 0;
      R record = null;

      @Override
      public boolean hasNext() {
        record = null;
        while (counter > 0) {
          // Read the record
          boolean recordPresent = hasRecord(cursor) && (record = getRecord(cursor)) != null && record.Present.isTrue();

          //if record available then we are good
          if (recordPresent) break;

          // if not record available, and we cannot move to the next page then set the record to null to end the iteration
          if (!gotoNextPage()) {
            record = null;
            break;
          }
          // go to next page if no record found;
          counter--;
          cursor = 0;
        }
        return record != null;
      }

      @SuppressWarnings("unchecked")
      @Override
      public R next() {
        if (record == null || record.Present.isFalse())
          throw new NoSuchElementException();

        cursor++;
        return record;
      }

      @Override
      public void remove() {
        throw new NotSupportedException();
      }
    };
  }
}

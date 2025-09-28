package org.catools.web.table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.utils.CStringUtil;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.catools.web.factory.CWebElementFactory;
import org.catools.web.pages.CWebComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.function.Function;

/**
 * Abstract base class representing a row in a web table with fluent interface support.
 * This class provides functionality to interact with table cells within a specific row,
 * supporting both simple text retrieval and complex control interactions.
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create a concrete implementation of CWebTableRow
 * public class OrderTableRow extends CWebTableRow<CWebDriver, OrderTable> {
 *     public OrderTableRow(String name, CWebDriver driver, int idx, OrderTable parentTable) {
 *         super(name, driver, idx, parentTable);
 *     }
 * }
 * 
 * // Usage in test
 * OrderTable orderTable = new OrderTable("orders", driver);
 * OrderTableRow firstRow = orderTable.getRow(0);
 * String orderNumber = firstRow.getCell("Order Number").getText();
 * String customerName = firstRow.getCell("Customer").getText();
 * }</pre>
 * 
 * @param <DR> the driver type extending CDriver
 * @param <P> the parent table type extending CWebTable
 * 
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 */
@Getter
@Accessors(chain = true)
public abstract class CWebTableRow<DR extends CDriver, P extends CWebTable<DR, ?>>
    extends CWebElement<DR> implements CWebComponent<DR> {
  /** The parent table instance that contains this row */
  protected final P parentTable;
  
  /** XPath template for locating cells within this row */
  @Setter
  private String cellXpath;
  
  /** Zero-based index of this row within the parent table */
  private int rowIndex;

  /**
   * Constructs a new CWebTableRow with default timeout.
   * 
   * @param name the name identifier for this table row
   * @param driver the web driver instance
   * @param idx the zero-based row index within the parent table
   * @param parentTable the parent table containing this row
   * 
   * @example
   * <pre>{@code
   * OrderTableRow row = new OrderTableRow("order-row-1", driver, 0, orderTable);
   * }</pre>
   */
  public CWebTableRow(String name, DR driver, int idx, P parentTable) {
    this(name, driver, idx, parentTable, DEFAULT_TIMEOUT);
  }

  /**
   * Constructs a new CWebTableRow with specified timeout.
   * 
   * @param name the name identifier for this table row
   * @param driver the web driver instance
   * @param rowIndex the zero-based row index within the parent table
   * @param parentTable the parent table containing this row
   * @param waitSec the timeout in seconds for element operations
   * 
   * @example
   * <pre>{@code
   * OrderTableRow row = new OrderTableRow("order-row-1", driver, 0, orderTable, 30);
   * }</pre>
   */
  public CWebTableRow(String name, DR driver, int rowIndex, P parentTable, int waitSec) {
    super(name, driver, By.xpath(parentTable.getRowXpath(rowIndex)), waitSec);
    this.rowIndex = rowIndex;
    this.parentTable = parentTable;
    this.cellXpath = parentTable.getCellXpath() + "[%d]";
    CWebElementFactory.initElements(this);
  }

  /**
   * Reads all cells in this row and returns their content and metadata.
   * This method iterates through all columns defined in the parent table's header map
   * and extracts the text content and visibility status of each cell.
   * 
   * @return a list of CWebTableCell objects containing cell data including index, 
   *         header name, text content, and visibility status
   * 
   * @example
   * <pre>{@code
   * // Read all cells from the first row
   * CWebTableRow row = table.getRow(0);
   * CList<CWebTableCell> cells = row.readRowCells();
   * 
   * // Process each cell
   * for (CWebTableCell cell : cells) {
   *     System.out.println("Header: " + cell.getHeader());
   *     System.out.println("Value: " + cell.getValue());
   *     System.out.println("Visible: " + cell.isVisible());
   * }
   * }</pre>
   */
  public CList<CWebTableCell> readRowCells() {
    CList<CWebTableCell> cellValues = new CList<>();
    CMap<Integer, String> headersMap = parentTable.getHeadersMap();
    for (Integer idx : headersMap.keySet()) {
      String header = headersMap.get(idx);
      CWebElement<DR> element = getCell(header);
      cellValues.add(new CWebTableCell(idx, header, element.getText(0), element.Visible.isTrue()));
    }
    return cellValues;
  }

  /**
   * Creates a CWebElement that points to a specific cell within this row.
   * This method allows access to cells with duplicate header names by using an index,
   * and supports child locators for accessing nested DOM elements within the cell.
   *
   * @param header the column header name to locate the cell
   * @param index the zero-based index of the header (used when multiple columns have the same name)
   * @param childLocator additional XPath locator for accessing child elements within the cell
   * @return a CWebElement instance pointing to the specified cell
   * 
   * @throws IllegalArgumentException if the header is not found or index is out of bounds
   * 
   * @example
   * <pre>{@code
   * // Get a cell with a specific header and access a button inside it
   * CWebElement actionCell = row.getCell("Actions", 0, "//button[@class='edit-btn']");
   * actionCell.click();
   * 
   * // Get the second column with "Status" header (if there are multiple Status columns)
   * CWebElement statusCell = row.getCell("Status", 1, "");
   * String statusText = statusCell.getText();
   * }</pre>
   */
  protected CWebElement<DR> getCell(String header, int index, String childLocator) {
    return new CWebElement<>(header, driver, getCellLocator(header, index, childLocator));
  }

  /**
   * Creates a CWebElement that points to the first cell with the specified header name.
   * This is a convenience method equivalent to calling getCell(header, 0).
   *
   * @param header the column header name to locate the cell
   * @return a CWebElement instance pointing to the first matching cell
   * 
   * @throws IllegalArgumentException if the header is not found
   * 
   * @example
   * <pre>{@code
   * // Get the cell under "Customer Name" column
   * CWebElement customerCell = row.getCell("Customer Name");
   * String customerName = customerCell.getText();
   * 
   * // Check if a cell is visible
   * CWebElement priceCell = row.getCell("Price");
   * if (priceCell.isVisible()) {
   *     String price = priceCell.getText();
   * }
   * }</pre>
   */
  protected CWebElement<DR> getCell(String header) {
    return getCell(header, 0);
  }

  /**
   * Creates a CWebElement that points to a specific cell with the given header and index.
   * This method is useful when there are multiple columns with the same header name.
   *
   * @param header the column header name to locate the cell
   * @param index the zero-based index of the header (used when multiple columns have the same name)
   * @return a CWebElement instance pointing to the specified cell
   * 
   * @throws IllegalArgumentException if the header is not found or index is out of bounds
   * 
   * @example
   * <pre>{@code
   * // Get the first "Date" column
   * CWebElement startDate = row.getCell("Date", 0);
   * 
   * // Get the second "Date" column (e.g., end date)
   * CWebElement endDate = row.getCell("Date", 1);
   * 
   * // Compare dates
   * String startDateText = startDate.getText();
   * String endDateText = endDate.getText();
   * }</pre>
   */
  protected CWebElement<DR> getCell(String header, int index) {
    return getCell(header, index, "");
  }

  /**
   * Creates a custom control instance using a control builder function for the first cell
   * with the specified header. This method enables creation of specialized web controls
   * (like buttons, dropdowns, checkboxes) from table cells.
   *
   * @param <C> the type of control to be created
   * @param header the column header name to locate the cell
   * @param controlBuilder function that creates a control instance from a By locator
   * @return the created control instance
   * 
   * @throws IllegalArgumentException if the header is not found
   * 
   * @example
   * <pre>{@code
   * // Create a button control from an "Actions" cell
   * CWebButton editButton = row.getCell("Actions", 
   *     locator -> new CWebButton("edit-btn", driver, locator));
   * editButton.click();
   * 
   * // Create a checkbox control from a "Select" cell
   * CWebCheckbox selectBox = row.getCell("Select", 
   *     locator -> new CWebCheckbox("select-row", driver, locator));
   * selectBox.check();
   * }</pre>
   */
  protected <C> C getCell(String header, Function<By, C> controlBuilder) {
    return getCell(header, 0, "", controlBuilder);
  }

  /**
   * Creates a custom control instance using a control builder function for a specific cell
   * identified by header and index. This method is useful when there are multiple columns
   * with the same header name and you need to create specialized controls.
   *
   * @param <C> the type of control to be created
   * @param header the column header name to locate the cell
   * @param index the zero-based index of the header (used when multiple columns have the same name)
   * @param controlBuilder function that creates a control instance from a By locator
   * @return the created control instance
   * 
   * @throws IllegalArgumentException if the header is not found or index is out of bounds
   * 
   * @example
   * <pre>{@code
   * // Create a dropdown from the second "Category" column
   * CWebDropDown categoryDropdown = row.getCell("Category", 1, 
   *     locator -> new CWebDropDown("category-select", driver, locator));
   * categoryDropdown.selectByText("Electronics");
   * 
   * // Create a link from the first "Details" column
   * CWebLink detailsLink = row.getCell("Details", 0,
   *     locator -> new CWebLink("details-link", driver, locator));
   * detailsLink.click();
   * }</pre>
   */
  protected <C> C getCell(String header, int index, Function<By, C> controlBuilder) {
    return getCell(header, index, "", controlBuilder);
  }

  /**
   * Creates a custom control instance using a control builder function for the first cell
   * with the specified header, including a child locator for accessing nested elements.
   * This method combines header-based cell location with child element access.
   *
   * @param <C> the type of control to be created
   * @param header the column header name to locate the cell
   * @param childLocator additional XPath locator for accessing child elements within the cell
   * @param controlBuilder function that creates a control instance from a By locator
   * @return the created control instance
   * 
   * @throws IllegalArgumentException if the header is not found
   * 
   * @example
   * <pre>{@code
   * // Create a button control from a nested element within an "Actions" cell
   * CWebButton deleteButton = row.getCell("Actions", "//button[@data-action='delete']",
   *     locator -> new CWebButton("delete-btn", driver, locator));
   * deleteButton.click();
   * 
   * // Create an input field from within a "Quantity" cell
   * CWebTextBox quantityInput = row.getCell("Quantity", "//input[@type='number']",
   *     locator -> new CWebTextBox("quantity-input", driver, locator));
   * quantityInput.setText("5");
   * }</pre>
   */
  protected <C> C getCell(String header, String childLocator, Function<By, C> controlBuilder) {
    return getCell(header, 0, childLocator, controlBuilder);
  }

  /**
   * Creates a custom control instance using a control builder function for a specific cell.
   * This is the most comprehensive method that allows specification of header, index, child locator,
   * and control builder, providing maximum flexibility for complex table interactions.
   *
   * @param <C> the type of control to be created
   * @param header the column header name to locate the cell
   * @param index the zero-based index of the header (used when multiple columns have the same name)
   * @param childLocator additional XPath locator for accessing child elements within the cell
   * @param controlBuilder function that creates a control instance from a By locator
   * @return the created control instance
   * 
   * @throws IllegalArgumentException if the header is not found or index is out of bounds
   * 
   * @example
   * <pre>{@code
   * // Create a complex control from a specific cell with nested element
   * CWebDatePicker datePicker = row.getCell("Due Date", 1, "//input[@type='date']",
   *     locator -> new CWebDatePicker("due-date", driver, locator));
   * datePicker.setDate("2024-12-31");
   * 
   * // Create a file upload control from within a cell
   * CWebFileUpload fileUpload = row.getCell("Attachment", 0, "//input[@type='file']",
   *     locator -> new CWebFileUpload("file-upload", driver, locator));
   * fileUpload.uploadFile("/path/to/document.pdf");
   * }</pre>
   */
  protected <C> C getCell(String header, int index, String childLocator, Function<By, C> controlBuilder) {
    return controlBuilder.apply(getCellLocator(header, index, childLocator));
  }

  /**
   * Constructs a By locator for a specific cell within this table row.
   * This method handles the complex logic of mapping header names to column indices,
   * supporting duplicate header names and child element access within cells.
   * 
   * <p>The method performs the following operations:</p>
   * <ul>
   *   <li>Searches for all columns matching the specified header name</li>
   *   <li>Validates that the requested index exists</li>
   *   <li>Constructs an XPath locator combining row and cell positioning</li>
   *   <li>Appends child locator if provided</li>
   * </ul>
   *
   * @param header the column header name to locate
   * @param index the zero-based index for duplicate header names
   * @param childLocator additional XPath for nested elements (can be empty)
   * @return a By locator that can be used to find the specific cell element
   * 
   * @throws IllegalArgumentException if the header is not found in the table or 
   *         if the index is greater than the number of matching headers
   * 
   * @example
   * <pre>{@code
   * // Get locator for a simple cell
   * By cellLocator = row.getCellLocator("Product Name", 0, "");
   * 
   * // Get locator for a nested button within a cell
   * By buttonLocator = row.getCellLocator("Actions", 0, "//button[@class='edit']");
   * 
   * // Get locator for second column with same header
   * By secondStatusLocator = row.getCellLocator("Status", 1, "");
   * }</pre>
   */
  protected By getCellLocator(String header, int index, String childLocator) {
    CHashMap<Integer, String> allMatches = parentTable.getHeadersMap().getAll((k, v) -> StringUtils.equals(header, v));

    if (allMatches.isEmpty() || allMatches.size() < index) {
      // We send invalid locator in case if header does not exist
      throw new IllegalArgumentException("Header not found, header:'" + header + "', index:" + index);
    }

    Object headerIndex = allMatches.keySet().stream().sorted().toList().get(index);

    String cellLocator = String.format(cellXpath + childLocator, headerIndex);
    cellLocator = CStringUtil.removeStart(cellLocator, ".");
    return new ByChained(getLocator(), By.xpath("." + cellLocator));
  }
}

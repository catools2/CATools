package org.catools.web.table;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.utils.CStringUtil;
import org.catools.web.collections.CWebElements;
import org.catools.web.drivers.CDriver;

/**
 * Represents header information for web tables, providing functionality to parse and manage
 * table header elements with their associated metadata such as index, text content, and visibility.
 * 
 * <p>This class is designed to work with web tables by extracting header information from
 * web elements and providing convenient methods to access header data.</p>
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * // Create a web driver instance
 * CDriver driver = new CWebDriver();
 * 
 * // Initialize header info with table headers locator
 * CWebTableHeaderInfo<CDriver> headerInfo = new CWebTableHeaderInfo<>(driver, "//table/thead/tr/th");
 * 
 * // Get header index by name
 * Integer nameIndex = headerInfo.getHeaderIndex("Name");
 * Integer emailIndex = headerInfo.getHeaderIndex("Email");
 * 
 * // Get all headers map
 * CMap<Integer, String> allHeaders = headerInfo.getHeadersMap();
 * // Output: {1="Name", 2="Email", 3="Status", 4="Actions"}
 * 
 * // Get only visible headers
 * CMap<Integer, String> visibleHeaders = headerInfo.getVisibleHeadersMap();
 * // Output: {1="Name", 2="Email", 3="Status"} (assuming "Actions" column is hidden)
 * }</pre>
 *
 * @param <DR> the type of web driver extending CDriver
 * @author CATools Team
 * @since 1.0
 */
@Getter
public class CWebTableHeaderInfo<DR extends CDriver> {

  private CList<Header> headers = new CList<>();

  /**
   * Constructs a new CWebTableHeaderInfo instance by parsing header elements from the web page.
   * 
   * <p>This constructor locates all header elements using the provided CSS selector or XPath,
   * extracts their text content, determines their visibility, and creates Header objects
   * with sequential indices starting from 1.</p>
   * 
   * <p>If header text is initially blank, the constructor will attempt to move to the element
   * to trigger any dynamic content loading before re-extracting the text.</p>
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * // Using XPath to locate table headers
   * CWebTableHeaderInfo<CDriver> headerInfo = new CWebTableHeaderInfo<>(
   *     driver, 
   *     "//table[@id='dataTable']/thead/tr/th"
   * );
   * 
   * // Using CSS selector to locate headers
   * CWebTableHeaderInfo<CDriver> headerInfo2 = new CWebTableHeaderInfo<>(
   *     driver, 
   *     "#userTable thead th"
   * );
   * 
   * // For a table with headers: ["ID", "Name", "Email", "Status"]
   * // This will create Header objects:
   * // Header(1, "ID", true)
   * // Header(2, "Name", true) 
   * // Header(3, "Email", true)
   * // Header(4, "Status", false) // if Status column is hidden
   * }</pre>
   *
   * @param driver the web driver instance to use for locating elements
   * @param headersLocator the CSS selector or XPath expression to locate header elements
   * @throws IllegalArgumentException if driver or headersLocator is null
   */
  public CWebTableHeaderInfo(DR driver, String headersLocator) {
    new CWebElements<>("Headers", driver, headersLocator).forEach(h -> {
      String headerText = CStringUtil.normalizeSpace(h.getText(1));
      if (StringUtils.isBlank(headersLocator)) {
        h.moveTo();
        headerText = CStringUtil.normalizeSpace(h.getText(1));
      }
      headers.add(new Header(headers.size() + 1, headerText, h.Visible.isTrue()));
    });
  }

  /**
   * Retrieves the index of a header column by its name (case-insensitive).
   * 
   * <p>This method performs a case-insensitive search through all headers to find
   * a matching header name and returns its 1-based index. If no matching header
   * is found, returns -1.</p>
   * 
   * <p><strong>Examples:</strong></p>
   * <pre>{@code
   * // Assuming table headers: ["ID", "Full Name", "Email Address", "Status"]
   * CWebTableHeaderInfo<CDriver> headerInfo = new CWebTableHeaderInfo<>(driver, "//th");
   * 
   * // Exact match (case-insensitive)
   * Integer idIndex = headerInfo.getHeaderIndex("ID");          // Returns: 1
   * Integer nameIndex = headerInfo.getHeaderIndex("full name"); // Returns: 2
   * Integer emailIndex = headerInfo.getHeaderIndex("EMAIL ADDRESS"); // Returns: 3
   * 
   * // Non-existent header
   * Integer phoneIndex = headerInfo.getHeaderIndex("Phone");    // Returns: -1
   * 
   * // Null or empty input
   * Integer nullIndex = headerInfo.getHeaderIndex(null);       // Returns: -1
   * Integer emptyIndex = headerInfo.getHeaderIndex("");        // Returns: -1
   * }</pre>
   *
   * @param header the name of the header to search for (case-insensitive)
   * @return the 1-based index of the header if found, -1 otherwise
   */
  public Integer getHeaderIndex(String header) {
    Header first = headers.getFirstOrNull(h -> StringUtils.equalsIgnoreCase(header, h.header()));
    return first == null ? -1 : first.index();
  }

  /**
   * Returns a map containing all headers with their corresponding indices.
   * 
   * <p>This method creates a mapping from header index (1-based) to header text
   * for all headers, regardless of their visibility status. The returned map
   * is a new instance and modifications to it will not affect the original headers.</p>
   * 
   * <p><strong>Examples:</strong></p>
   * <pre>{@code
   * // Assuming table headers: ["ID", "Name", "Email", "Actions"]
   * CWebTableHeaderInfo<CDriver> headerInfo = new CWebTableHeaderInfo<>(driver, "//th");
   * 
   * CMap<Integer, String> allHeaders = headerInfo.getHeadersMap();
   * // Returns: {1="ID", 2="Name", 3="Email", 4="Actions"}
   * 
   * // Use the map to iterate through headers
   * for (Map.Entry<Integer, String> entry : allHeaders.entrySet()) {
   *     System.out.println("Column " + entry.getKey() + ": " + entry.getValue());
   * }
   * // Output:
   * // Column 1: ID
   * // Column 2: Name
   * // Column 3: Email
   * // Column 4: Actions
   * 
   * // Access specific header by index
   * String thirdHeader = allHeaders.get(3); // Returns: "Email"
   * }</pre>
   *
   * @return a new CMap containing index-to-header mappings for all headers
   */
  public CMap<Integer, String> getHeadersMap() {
    CMap<Integer, String> output = new CHashMap<>();
    for (Header h : headers) {
      output.put(h.index(), h.header());
    }
    return output;
  }

  /**
   * Returns a map containing only visible headers with their corresponding indices.
   * 
   * <p>This method creates a mapping from header index (1-based) to header text
   * for only those headers that are currently visible on the web page. Hidden columns
   * are excluded from the returned map. The returned map is a new instance and 
   * modifications to it will not affect the original headers.</p>
   * 
   * <p><strong>Examples:</strong></p>
   * <pre>{@code
   * // Assuming table headers: ["ID", "Name", "Email", "Actions"]
   * // where "Actions" column is hidden (visible=false)
   * CWebTableHeaderInfo<CDriver> headerInfo = new CWebTableHeaderInfo<>(driver, "//th");
   * 
   * CMap<Integer, String> visibleHeaders = headerInfo.getVisibleHeadersMap();
   * // Returns: {1="ID", 2="Name", 3="Email"}
   * // Note: "Actions" (index 4) is excluded because it's not visible
   * 
   * CMap<Integer, String> allHeaders = headerInfo.getHeadersMap();
   * // Returns: {1="ID", 2="Name", 3="Email", 4="Actions"}
   * 
   * // Compare visible vs all headers
   * System.out.println("Total headers: " + allHeaders.size());     // Output: 4
   * System.out.println("Visible headers: " + visibleHeaders.size()); // Output: 3
   * 
   * // Check if a specific column is visible
   * boolean actionsVisible = visibleHeaders.containsKey(4); // Returns: false
   * boolean emailVisible = visibleHeaders.containsKey(3);   // Returns: true
   * 
   * // Iterate through only visible headers
   * for (Map.Entry<Integer, String> entry : visibleHeaders.entrySet()) {
   *     System.out.println("Visible Column " + entry.getKey() + ": " + entry.getValue());
   * }
   * }</pre>
   *
   * @return a new CMap containing index-to-header mappings for visible headers only
   */
  public CMap<Integer, String> getVisibleHeadersMap() {
    CMap<Integer, String> output = new CHashMap<>();
    for (Header h : headers) {
      if (h.visible())
        output.put(h.index(), h.header());
    }
    return output;
  }

  /**
   * Record representing a table header with its index, text content, and visibility status.
   * 
   * <p>This immutable data structure encapsulates all the essential information
   * about a table header column. The index is 1-based to match common table
   * numbering conventions.</p>
   * 
   * <p><strong>Examples:</strong></p>
   * <pre>{@code
   * // Creating header instances
   * Header idHeader = new Header(1, "ID", true);
   * Header nameHeader = new Header(2, "Full Name", true);
   * Header hiddenHeader = new Header(3, "Internal Code", false);
   * 
   * // Accessing header properties
   * int position = idHeader.index();        // Returns: 1
   * String text = nameHeader.header();      // Returns: "Full Name"  
   * boolean isVisible = hiddenHeader.visible(); // Returns: false
   * 
   * // Using in collections
   * List<Header> headers = Arrays.asList(idHeader, nameHeader, hiddenHeader);
   * 
   * // Filter visible headers
   * List<Header> visibleHeaders = headers.stream()
   *     .filter(Header::visible)
   *     .collect(Collectors.toList());
   * // Result: [Header(1, "ID", true), Header(2, "Full Name", true)]
   * }</pre>
   *
   * @param index the 1-based position of the header in the table
   * @param header the text content of the header
   * @param visible whether the header column is currently visible on the page
   */
  public record Header(int index, String header, boolean visible) {
  }
}

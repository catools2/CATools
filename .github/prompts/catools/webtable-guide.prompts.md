# CWebTable Guide

**Version:** 2.0.0  
**Last Updated:** November 28, 2025  
**Framework:** CATools Web Automation  
**Java Version:** 21

---

## Table of Contents

1. [Introduction](#introduction)
2. [CWebTable Overview](#cwebtable-overview)
3. [Creating CWebTable Implementations](#creating-cwebtable-implementations)
4. [Header Mapping and Column Access](#header-mapping-and-column-access)
5. [Search and Filter Operations](#search-and-filter-operations)
6. [Business Language Methods](#business-language-methods)
7. [Advanced Usage Patterns](#advanced-usage-patterns)
8. [Best Practices](#best-practices)
9. [Common Pitfalls](#common-pitfalls)
10. [Examples](#examples)

---

## Introduction

`CWebTable` is the abstract base class for implementing table components in CATools web automation framework. It provides comprehensive functionality for interacting with HTML tables, including searching, filtering, header mapping, and row retrieval with business-oriented methods.

**This guide complements the page object patterns documented in `webpage-guide.prompts.md`.**

### Key Features

- **Dynamic Header Mapping** - Automatically maps column names to indices
- **Flexible Search** - Search by column name and value
- **Predicate Filtering** - Complex queries using Java predicates
- **Row Retrieval Patterns** - getFirst(), getAny(), getAll(), etc.
- **XPath Customization** - Configurable XPath for different table structures
- **Business Language** - Methods use business terminology
- **Extension Framework Integration** - Supports verification, wait, and state methods
- **Thread-Safe Search** - Thread-local search criteria management

### Table Hierarchy

**CWebTable contains:**
- ✅ **Rows** (CWebTableRow) - Individual table row interactions
- ✅ **Business Methods** - Query and filter operations
- ✅ **Column Mapping** - Header name to index mapping
- ✅ **Search Criteria** - Complex multi-column searches

**CWebTable does NOT contain:**
- ❌ Individual cell locators (put in CWebTableRow)
- ❌ User interaction forms (put in CWebForm)
- ❌ Row-specific business logic (put in CWebTableRow)

---

## CWebTable Overview

### Class Signature

```java
public abstract class CWebTable<DR extends CDriver, R extends CWebTableRow<DR, ?>>
    extends CWebElement<DR> implements CWebComponent<DR>, CWebIterable<R>
```

### Type Parameters

- `<DR>` - The driver type extending `CDriver`
- `<R>` - The row type extending `CWebTableRow<DR, ?>`

### Implemented Interfaces

- `CWebComponent<DR>` - Provides driver access
- `CWebIterable<R>` - Enables iteration over rows

### Core Components

1. **Base XPath** - XPath expression to locate the table element
2. **Header Info** - Memoized header mapping (column name → index)
3. **Search Criteria** - Thread-local storage for search parameters
4. **XPath Templates** - Customizable XPath for headers, rows, cells
5. **Element Factory** - Automatic initialization via `CWebElementFactory`

### Abstract Method

```java
/**
 * Get a table row at the specified index.
 * Must be implemented by concrete table classes.
 */
public abstract R getRecord(int idx);
```

---

## Creating CWebTable Implementations

### Method 1: Basic Table Implementation

```java
package org.catools.web.tables;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTable;

/**
 * Product table for displaying product information.
 * Business methods for querying products.
 */
@Slf4j
public class CProductTable extends CWebTable<CDriver, CProductRow> {

    /**
     * Constructs product table with default timeout.
     */
    public CProductTable(CDriver driver) {
        super("Product Table", driver, "//table[@id='products']");
        log.info("Product table initialized");
    }

    /**
     * Get product row at index.
     * Required implementation of abstract method.
     */
    @Override
    public CProductRow getRecord(int idx) {
        return new CProductRow("Product Row " + idx, getDriver(), idx, this);
    }

    // Business language methods

    /**
     * Find product by name.
     */
    public CProductRow findByName(String productName) {
        log.info("Finding product: {}", productName);
        return getFirst("Product Name", productName);
    }

    /**
     * Get all products in category.
     */
    public CList<CProductRow> getByCategory(String category) {
        log.info("Getting products in category: {}", category);
        return getAll("Category", category);
    }

    /**
     * Find products in stock.
     */
    public CList<CProductRow> getInStockProducts() {
        log.info("Getting in-stock products");
        return getAll(row -> row.isInStock());
    }

    /**
     * Find products in price range.
     */
    public CList<CProductRow> getByPriceRange(double min, double max) {
        log.info("Getting products in price range: {} - {}", min, max);
        return getAll(row -> {
            double price = Double.parseDouble(row.getPrice().replace("$", ""));
            return price >= min && price <= max;
        });
    }

    /**
     * Check if product exists.
     */
    public boolean hasProduct(String productName) {
        return findByName(productName) != null;
    }

    /**
     * Get product count.
     */
    public int getProductCount() {
        return size();
    }
}
```

### Method 2: Table with Custom XPath Configuration

```java
package org.catools.web.tables;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTable;

/**
 * Order table with custom XPath configuration for non-standard table structure.
 */
@Slf4j
public class COrderTable extends CWebTable<CDriver, COrderRow> {

    public COrderTable(CDriver driver) {
        super("Order Table", driver, "//div[@class='data-table']//table", 15);
        
        // Customize XPath for non-standard table structure
        setTHeadXpath("/div[@class='table-header']");
        setHeaderRowXpath("/div[@class='header-row']");
        setHeaderCellXpath("/span[@class='header-cell']");
        setTBodyXpath("/div[@class='table-body']");
        setRowXpath("/div[@class='data-row']");
        setCellXpath("/span[@class='data-cell']");
        
        log.info("Order table initialized with custom XPath");
    }

    @Override
    public COrderRow getRecord(int idx) {
        return new COrderRow("Order Row " + idx, getDriver(), idx, this);
    }

    // Business methods

    /**
     * Find order by order number.
     */
    public COrderRow findByOrderNumber(String orderNumber) {
        log.info("Finding order: {}", orderNumber);
        return getFirst("Order #", orderNumber);
    }

    /**
     * Get orders for customer.
     */
    public CList<COrderRow> getByCustomer(String customerName) {
        log.info("Getting orders for customer: {}", customerName);
        return getAll("Customer", customerName);
    }

    /**
     * Get orders by status.
     */
    public CList<COrderRow> getByStatus(String status) {
        log.info("Getting orders with status: {}", status);
        return getAll("Status", status);
    }

    /**
     * Get pending orders.
     */
    public CList<COrderRow> getPendingOrders() {
        log.info("Getting pending orders");
        return getAll("Status", "Pending");
    }

    /**
     * Get completed orders.
     */
    public CList<COrderRow> getCompletedOrders() {
        log.info("Getting completed orders");
        return getAll("Status", "Completed");
    }

    /**
     * Find orders above amount.
     */
    public CList<COrderRow> getOrdersAboveAmount(double minAmount) {
        log.info("Getting orders above amount: {}", minAmount);
        return getAll(row -> {
            double total = Double.parseDouble(row.getTotalAmount().replace("$", ""));
            return total >= minAmount;
        });
    }
}
```

### Method 3: Table with Complex Search Criteria

```java
package org.catools.web.tables;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTable;
import org.catools.common.collections.CList;

import java.util.Map;

/**
 * User table with complex multi-column search capabilities.
 */
@Slf4j
public class CUserTable extends CWebTable<CDriver, CUserRow> {

    public CUserTable(CDriver driver) {
        super("User Table", driver, "//table[@id='users']");
        log.info("User table initialized");
    }

    @Override
    public CUserRow getRecord(int idx) {
        return new CUserRow("User Row " + idx, getDriver(), idx, this);
    }

    // Business methods with complex search

    /**
     * Find user by username.
     */
    public CUserRow findByUsername(String username) {
        log.info("Finding user: {}", username);
        return getFirst("Username", username);
    }

    /**
     * Find user by email.
     */
    public CUserRow findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        return getFirst("Email", email);
    }

    /**
     * Get users by role.
     */
    public CList<CUserRow> getByRole(String role) {
        log.info("Getting users with role: {}", role);
        return getAll("Role", role);
    }

    /**
     * Get users by status.
     */
    public CList<CUserRow> getByStatus(String status) {
        log.info("Getting users with status: {}", status);
        return getAll("Status", status);
    }

    /**
     * Get active users.
     */
    public CList<CUserRow> getActiveUsers() {
        log.info("Getting active users");
        return getAll("Status", "Active");
    }

    /**
     * Get admin users.
     */
    public CList<CUserRow> getAdminUsers() {
        log.info("Getting admin users");
        return getAll("Role", "Admin");
    }

    /**
     * Find users matching multiple criteria.
     */
    public CList<CUserRow> findByMultipleCriteria(String role, String status) {
        log.info("Finding users with role: {} and status: {}", role, status);
        
        Map<String, String> criteria = ImmutableMap.of(
            "Role", role,
            "Status", status
        );
        
        return getAll(criteria);
    }

    /**
     * Find active admins.
     */
    public CList<CUserRow> getActiveAdmins() {
        log.info("Getting active admin users");
        return findByMultipleCriteria("Admin", "Active");
    }

    /**
     * Find users by email domain.
     */
    public CList<CUserRow> getByEmailDomain(String domain) {
        log.info("Getting users with email domain: {}", domain);
        return getAll(row -> row.getEmail().endsWith("@" + domain));
    }

    /**
     * Check if user exists.
     */
    public boolean userExists(String username) {
        return findByUsername(username) != null;
    }

    /**
     * Get user count.
     */
    public int getUserCount() {
        return size();
    }
}
```

---

## Header Mapping and Column Access

### Automatic Header Mapping

CWebTable automatically reads table headers and creates a mapping of column names to indices.

```java
// Table automatically reads headers on first access
public CProductTable(CDriver driver) {
    super("Product Table", driver, "//table[@id='products']");
    // Headers mapped: "Product Name" → 0, "Price" → 1, "Stock" → 2, etc.
}

// Access by column name
CProductRow product = productTable.getFirst("Product Name", "iPhone");

// Get column index
Integer priceColumnIndex = productTable.getHeadersMap().getKey("Price");
```

### Custom Header Configuration

```java
// Customize header XPath for non-standard tables
public CCustomTable(CDriver driver) {
    super("Custom Table", driver, "//div[@class='custom-table']");
    
    // Override default header paths
    setTHeadXpath("/div[@class='header-section']");
    setHeaderRowXpath("/div[@class='header-row']");
    setHeaderCellXpath("/div[@class='header-col']");
}
```

### Accessing Columns

```java
// By column name (recommended)
CList<CProductRow> electronics = productTable.getAll("Category", "Electronics");

// By multiple columns
Map<String, String> criteria = ImmutableMap.of(
    "Category", "Electronics",
    "Stock", "In Stock"
);
CList<CProductRow> available = productTable.getAll(criteria);

// Get header mapping
CMap<Integer, String> headers = productTable.getHeadersMap();
for (Map.Entry<Integer, String> entry : headers.entrySet()) {
    log.info("Column {}: {}", entry.getKey(), entry.getValue());
}
```

---

## Search and Filter Operations

### Simple Search

```java
// Find first row matching criteria
CProductRow product = productTable.getFirst("Name", "Laptop");

// Find any row matching criteria
CProductRow anyProduct = productTable.getAny("Category", "Electronics");

// Find all rows matching criteria
CList<CProductRow> allLaptops = productTable.getAll("Category", "Laptops");
```

### Multi-Column Search

```java
// Search by multiple columns
Map<String, String> searchCriteria = ImmutableMap.of(
    "Category", "Electronics",
    "Price", "999",
    "Stock", "In Stock"
);

CList<CProductRow> results = productTable.getAll(searchCriteria);
```

### Predicate-Based Filtering

```java
// Filter with custom predicate
CList<CProductRow> expensiveProducts = productTable.getAll(row -> {
    double price = Double.parseDouble(row.getPrice().replace("$", ""));
    return price > 1000.0;
});

// Complex business logic filter
CList<CProductRow> premiumInStock = productTable.getAll(row ->
    row.isPremium() && row.isInStock() && row.getRating() >= 4.5
);

// Filter with multiple conditions
CList<CUserRow> eligibleUsers = userTable.getAll(row -> {
    boolean isActive = row.isActive();
    boolean isVerified = row.isVerified();
    int loginCount = row.getLoginCount();
    return isActive && isVerified && loginCount > 10;
});
```

### Retrieval Patterns

```java
// Get first match
CProductRow first = productTable.getFirst("Category", "Electronics");

// Get any match (optimized for existence check)
CProductRow any = productTable.getAny("Category", "Electronics");

// Get all matches
CList<CProductRow> all = productTable.getAll("Category", "Electronics");

// Get by index
CProductRow product = productTable.getRecord(0);  // First row
CProductRow lastProduct = productTable.getRecord(productTable.size() - 1);  // Last row

// Iterate all rows
for (CProductRow product : productTable) {
    log.info("Product: {}", product.getName());
}

// Check if exists
boolean hasElectronics = productTable.getFirst("Category", "Electronics") != null;
```

---

## Business Language Methods

### Principle: Domain-Specific Operations

```java
// ✅ CORRECT - Business language methods
public class COrderTable extends CWebTable<CDriver, COrderRow> {
    
    /**
     * Find order by order number.
     * Business method - describes what we're finding in business terms.
     */
    public COrderRow findOrderByNumber(String orderNumber) {
        return getFirst("Order #", orderNumber);
    }
    
    /**
     * Get all orders for a customer.
     */
    public CList<COrderRow> getCustomerOrders(String customerName) {
        return getAll("Customer", customerName);
    }
    
    /**
     * Get recent orders (last 7 days).
     */
    public CList<COrderRow> getRecentOrders() {
        return getAll(row -> row.isWithinDays(7));
    }
    
    /**
     * Get high-value orders.
     */
    public CList<COrderRow> getHighValueOrders(double threshold) {
        return getAll(row -> row.getTotalAsDouble() >= threshold);
    }
    
    /**
     * Check if customer has pending orders.
     */
    public boolean hasPendingOrders(String customerName) {
        CList<COrderRow> orders = getCustomerOrders(customerName);
        return orders.stream().anyMatch(COrderRow::isPending);
    }
}

// ❌ WRONG - Technical method names
public class CBadTable extends CWebTable<CDriver, CBadRow> {
    
    public CBadRow getRowByColumn(int col, String val) {
        return getFirst(String.valueOf(col), val);
    }
    
    public CList<CBadRow> searchTable(String criteria) {
        return getAll("column", criteria);
    }
}
```

### Business Method Examples

```java
@Slf4j
public class CInvoiceTable extends CWebTable<CDriver, CInvoiceRow> {

    public CInvoiceTable(CDriver driver) {
        super("Invoice Table", driver, "//table[@id='invoices']");
    }

    @Override
    public CInvoiceRow getRecord(int idx) {
        return new CInvoiceRow("Invoice Row " + idx, getDriver(), idx, this);
    }

    // Business language methods

    /**
     * Find invoice by invoice number.
     */
    public CInvoiceRow findInvoice(String invoiceNumber) {
        log.info("Finding invoice: {}", invoiceNumber);
        return getFirst("Invoice #", invoiceNumber);
    }

    /**
     * Get all unpaid invoices.
     */
    public CList<CInvoiceRow> getUnpaidInvoices() {
        log.info("Getting unpaid invoices");
        return getAll("Status", "Unpaid");
    }

    /**
     * Get overdue invoices.
     */
    public CList<CInvoiceRow> getOverdueInvoices() {
        log.info("Getting overdue invoices");
        return getAll(CInvoiceRow::isOverdue);
    }

    /**
     * Get invoices for customer.
     */
    public CList<CInvoiceRow> getCustomerInvoices(String customerName) {
        log.info("Getting invoices for customer: {}", customerName);
        return getAll("Customer", customerName);
    }

    /**
     * Get invoices in date range.
     */
    public CList<CInvoiceRow> getInvoicesInDateRange(String startDate, String endDate) {
        log.info("Getting invoices from {} to {}", startDate, endDate);
        return getAll(row -> row.isDateBetween(startDate, endDate));
    }

    /**
     * Get total amount of unpaid invoices.
     */
    public double getTotalUnpaidAmount() {
        log.info("Calculating total unpaid amount");
        return getUnpaidInvoices().stream()
            .mapToDouble(CInvoiceRow::getAmountAsDouble)
            .sum();
    }

    /**
     * Check if customer has overdue invoices.
     */
    public boolean hasOverdueInvoices(String customerName) {
        return getCustomerInvoices(customerName).stream()
            .anyMatch(CInvoiceRow::isOverdue);
    }
}
```

---

## Advanced Usage Patterns

### Pattern 1: Table with Pagination

```java
@Slf4j
public class CPaginatedTable extends CWebTable<CDriver, CProductRow> {

    private final CWebElement<CDriver> nextPageButton;
    private final CWebElement<CDriver> previousPageButton;
    private final CWebElement<CDriver> pageInfo;

    public CPaginatedTable(CDriver driver) {
        super("Paginated Table", driver, "//table[@id='products']");
        
        this.nextPageButton = driver.findElementById("next-page", 5);
        this.previousPageButton = driver.findElementById("prev-page", 5);
        this.pageInfo = driver.findElementById("page-info", 5);
        
        log.info("Paginated table initialized");
    }

    @Override
    public CProductRow getRecord(int idx) {
        return new CProductRow("Product Row " + idx, getDriver(), idx, this);
    }

    /**
     * Go to next page.
     */
    public void nextPage() {
        log.info("Going to next page");
        if (nextPageButton.isEnabled()) {
            nextPageButton.click();
            waitForPageLoad();
        }
    }

    /**
     * Go to previous page.
     */
    public void previousPage() {
        log.info("Going to previous page");
        if (previousPageButton.isEnabled()) {
            previousPageButton.click();
            waitForPageLoad();
        }
    }

    /**
     * Get current page number.
     */
    public int getCurrentPage() {
        String pageText = pageInfo.getText();
        // Parse "Page 2 of 10" → 2
        return Integer.parseInt(pageText.split(" ")[1]);
    }

    /**
     * Get total pages.
     */
    public int getTotalPages() {
        String pageText = pageInfo.getText();
        // Parse "Page 2 of 10" → 10
        return Integer.parseInt(pageText.split(" ")[3]);
    }

    /**
     * Search across all pages.
     */
    public CProductRow findAcrossPages(String productName) {
        log.info("Searching across all pages for: {}", productName);
        
        int totalPages = getTotalPages();
        for (int page = 1; page <= totalPages; page++) {
            log.info("Searching page {} of {}", page, totalPages);
            
            CProductRow product = getFirst("Name", productName);
            if (product != null) {
                return product;
            }
            
            if (page < totalPages) {
                nextPage();
            }
        }
        
        return null;
    }

    /**
     * Wait for page to load.
     */
    private void waitForPageLoad() {
        // Wait for loading indicator to appear and disappear
        CWebElement<CDriver> loading = getDriver().findElementByClassName("loading", 2);
        if (loading.isPresent()) {
            loading.Visible.waitUntilTrue(5);
            loading.Visible.waitUntilFalse(30);
        }
    }
}
```

### Pattern 2: Table with Dynamic Filtering

```java
@Slf4j
public class CFilterableTable extends CWebTable<CDriver, CDataRow> {

    private final CWebElement<CDriver> filterInput;
    private final CWebElement<CDriver> filterButton;
    private final CWebElement<CDriver> clearFilterButton;

    public CFilterableTable(CDriver driver) {
        super("Filterable Table", driver, "//table[@id='data-table']");
        
        this.filterInput = driver.findElementById("table-filter", 5);
        this.filterButton = driver.findElementById("apply-filter", 5);
        this.clearFilterButton = driver.findElementById("clear-filter", 5);
    }

    @Override
    public CDataRow getRecord(int idx) {
        return new CDataRow("Data Row " + idx, getDriver(), idx, this);
    }

    /**
     * Apply table filter.
     */
    public void applyFilter(String filterText) {
        log.info("Applying filter: {}", filterText);
        filterInput.clear();
        filterInput.type(filterText);
        filterButton.click();
        waitForFilterComplete();
    }

    /**
     * Clear table filter.
     */
    public void clearFilter() {
        log.info("Clearing filter");
        clearFilterButton.click();
        waitForFilterComplete();
    }

    /**
     * Get filtered row count.
     */
    public int getFilteredRowCount() {
        return size();
    }

    /**
     * Wait for filter to complete.
     */
    private void waitForFilterComplete() {
        CWebElement<CDriver> loading = getDriver().findElementByClassName("table-loading", 2);
        if (loading.isPresent()) {
            loading.Visible.waitUntilFalse(10);
        }
    }
}
```

### Pattern 3: Table with Sorting

```java
@Slf4j
public class CSortableTable extends CWebTable<CDriver, CProductRow> {

    public CSortableTable(CDriver driver) {
        super("Sortable Table", driver, "//table[@id='products']");
    }

    @Override
    public CProductRow getRecord(int idx) {
        return new CProductRow("Product Row " + idx, getDriver(), idx, this);
    }

    /**
     * Sort by column name.
     */
    public void sortByColumn(String columnName) {
        log.info("Sorting by column: {}", columnName);
        
        // Find header cell and click to sort
        String headerXpath = String.format("%s%s%s/th[text()='%s']", 
            getBaseXpath(), getTHeadXpath(), getHeaderRowXpath(), columnName);
        
        CWebElement<CDriver> headerCell = getDriver().findElementByXPath(headerXpath, 5);
        headerCell.click();
        
        waitForSortComplete();
    }

    /**
     * Sort ascending.
     */
    public void sortAscending(String columnName) {
        log.info("Sorting {} ascending", columnName);
        
        sortByColumn(columnName);
        
        // Click again if currently descending
        if (isSortedDescending(columnName)) {
            sortByColumn(columnName);
        }
    }

    /**
     * Sort descending.
     */
    public void sortDescending(String columnName) {
        log.info("Sorting {} descending", columnName);
        
        sortByColumn(columnName);
        
        // Click again if currently ascending
        if (isSortedAscending(columnName)) {
            sortByColumn(columnName);
        }
    }

    /**
     * Check if sorted ascending.
     */
    private boolean isSortedAscending(String columnName) {
        String headerXpath = String.format("%s%s%s/th[text()='%s']", 
            getBaseXpath(), getTHeadXpath(), getHeaderRowXpath(), columnName);
        
        CWebElement<CDriver> headerCell = getDriver().findElementByXPath(headerXpath, 5);
        return headerCell.getAttribute("class").contains("sort-asc");
    }

    /**
     * Check if sorted descending.
     */
    private boolean isSortedDescending(String columnName) {
        String headerXpath = String.format("%s%s%s/th[text()='%s']", 
            getBaseXpath(), getTHeadXpath(), getHeaderRowXpath(), columnName);
        
        CWebElement<CDriver> headerCell = getDriver().findElementByXPath(headerXpath, 5);
        return headerCell.getAttribute("class").contains("sort-desc");
    }

    /**
     * Wait for sort to complete.
     */
    private void waitForSortComplete() {
        // Wait briefly for sort animation
        getDriver().waitSeconds(1);
    }
}
```

---

## Best Practices

### 1. Use Business Language for Methods

```java
// ✅ CORRECT - Business language
public COrderRow findOrderByNumber(String orderNumber) {
    return getFirst("Order #", orderNumber);
}

public CList<COrderRow> getPendingOrders() {
    return getAll("Status", "Pending");
}

public boolean hasHighValueOrders(double threshold) {
    return getAll(row -> row.getTotalAsDouble() > threshold).size() > 0;
}

// ❌ WRONG - Technical language
public COrderRow getRow(String col, String val) {
    return getFirst(col, val);
}

public CList<COrderRow> filterTable(String status) {
    return getAll("Status", status);
}
```

### 2. Implement getRecord() Method

```java
// ✅ CORRECT - Always implement getRecord()
@Override
public CProductRow getRecord(int idx) {
    return new CProductRow("Product Row " + idx, getDriver(), idx, this);
}

// ❌ WRONG - Don't leave abstract
// Must implement getRecord() in concrete table class
```

### 3. Use Predicates for Complex Filtering

```java
// ✅ CORRECT - Predicate for complex logic
public CList<CProductRow> getPremiumInStockProducts() {
    return getAll(row -> 
        row.isPremium() && 
        row.isInStock() && 
        row.getRating() >= 4.5
    );
}

// ❌ AVOID - Multiple separate queries
public CList<CProductRow> getPremiumInStockProducts() {
    CList<CProductRow> premium = getAll("Type", "Premium");
    CList<CProductRow> result = new CList<>();
    for (CProductRow row : premium) {
        if (row.isInStock() && row.getRating() >= 4.5) {
            result.add(row);
        }
    }
    return result;
}
```

### 4. Provide Existence Check Methods

```java
// ✅ CORRECT - Convenient existence checks
public boolean hasProduct(String productName) {
    return getFirst("Name", productName) != null;
}

public boolean hasOrdersForCustomer(String customerName) {
    return !getAll("Customer", customerName).isEmpty();
}

// Usage in tests
if (productTable.hasProduct("iPhone")) {
    // Product exists
}
```

### 5. Use Descriptive Constructor Parameters

```java
// ✅ CORRECT - Descriptive name and clear XPath
public CProductTable(CDriver driver) {
    super("Product Catalog Table", driver, "//table[@id='product-catalog']");
}

// ❌ AVOID - Generic name, unclear XPath
public CProductTable(CDriver driver) {
    super("Table", driver, "//table[1]");
}
```

### 6. Customize XPath for Non-Standard Tables

```java
// ✅ CORRECT - Override XPath for custom table structure
public CCustomTable(CDriver driver) {
    super("Custom Table", driver, "//div[@class='data-grid']");
    
    setTHeadXpath("/div[@class='grid-header']");
    setHeaderRowXpath("/div[@class='header-row']");
    setHeaderCellXpath("/div[@class='header-cell']");
    setTBodyXpath("/div[@class='grid-body']");
    setRowXpath("/div[@class='data-row']");
    setCellXpath("/div[@class='data-cell']");
}
```

### 7. Provide Aggregate Methods

```java
// ✅ CORRECT - Business aggregate methods
public int getProductCount() {
    return size();
}

public double getTotalInventoryValue() {
    return getAll().stream()
        .mapToDouble(row -> row.getPrice() * row.getQuantity())
        .sum();
}

public int getInStockCount() {
    return getAll(CProductRow::isInStock).size();
}
```

---

## Common Pitfalls

### Pitfall 1: Not Implementing getRecord()

```java
// ❌ WRONG - Missing getRecord() implementation
public class CBadTable extends CWebTable<CDriver, CProductRow> {
    public CBadTable(CDriver driver) {
        super("Table", driver, "//table");
    }
    // Forgot to implement getRecord()!
}

// ✅ CORRECT - Always implement
public class CGoodTable extends CWebTable<CDriver, CProductRow> {
    public CGoodTable(CDriver driver) {
        super("Table", driver, "//table");
    }
    
    @Override
    public CProductRow getRecord(int idx) {
        return new CProductRow("Row " + idx, getDriver(), idx, this);
    }
}
```

### Pitfall 2: Using Technical Method Names

```java
// ❌ WRONG - Technical names
public COrderRow getByColumn(String col, String val) {
    return getFirst(col, val);
}

// ✅ CORRECT - Business names
public COrderRow findOrderByNumber(String orderNumber) {
    return getFirst("Order #", orderNumber);
}
```

### Pitfall 3: Exposing Row Manipulation in Table

```java
// ❌ WRONG - Row manipulation in table
public void clickEditButtonInRow(String productName) {
    CProductRow row = getFirst("Name", productName);
    getDriver().findElementByXPath(row.getXpath() + "//button[@class='edit']", 5).click();
}

// ✅ CORRECT - Row handles its own interactions
public void editProduct(String productName) {
    CProductRow row = getFirst("Name", productName);
    row.clickEditButton();  // Row method
}
```

### Pitfall 4: Not Using Header Names

```java
// ❌ WRONG - Using column indices
public CProductRow findProduct(String name) {
    return getFirst("0", name);  // What is column 0?
}

// ✅ CORRECT - Use header names
public CProductRow findProduct(String name) {
    return getFirst("Product Name", name);  // Clear and maintainable
}
```

### Pitfall 5: Ignoring Null Returns

```java
// ❌ RISKY - Not checking for null
public void deleteProduct(String productName) {
    CProductRow product = getFirst("Name", productName);
    product.clickDeleteButton();  // NullPointerException if not found!
}

// ✅ CORRECT - Check for null
public void deleteProduct(String productName) {
    CProductRow product = getFirst("Name", productName);
    if (product != null) {
        product.clickDeleteButton();
    } else {
        log.warn("Product not found: {}", productName);
    }
}
```

### Pitfall 6: Inefficient Multiple Queries

```java
// ❌ INEFFICIENT - Multiple queries
public CList<CProductRow> getExpensiveLaptops() {
    CList<CProductRow> laptops = getAll("Category", "Laptop");
    CList<CProductRow> expensive = new CList<>();
    for (CProductRow laptop : laptops) {
        if (laptop.getPriceAsDouble() > 1000) {
            expensive.add(laptop);
        }
    }
    return expensive;
}

// ✅ EFFICIENT - Single predicate query
public CList<CProductRow> getExpensiveLaptops() {
    return getAll(row -> 
        "Laptop".equals(row.getCategory()) && 
        row.getPriceAsDouble() > 1000
    );
}
```

---

## Examples

### Example 1: Product Catalog Table

```java
package org.catools.web.tables;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTable;
import org.catools.common.collections.CList;

/**
 * Product catalog table with business methods for querying products.
 */
@Slf4j
public class CProductTable extends CWebTable<CDriver, CProductRow> {

    public CProductTable(CDriver driver) {
        super("Product Catalog Table", driver, "//table[@id='product-catalog']", 15);
        log.info("Product table initialized");
    }

    @Override
    public CProductRow getRecord(int idx) {
        return new CProductRow("Product Row " + idx, getDriver(), idx, this);
    }

    // Business methods

    /**
     * Find product by name.
     */
    public CProductRow findProductByName(String productName) {
        log.info("Finding product: {}", productName);
        return getFirst("Product Name", productName);
    }

    /**
     * Find product by SKU.
     */
    public CProductRow findProductBySKU(String sku) {
        log.info("Finding product by SKU: {}", sku);
        return getFirst("SKU", sku);
    }

    /**
     * Get all products in category.
     */
    public CList<CProductRow> getProductsByCategory(String category) {
        log.info("Getting products in category: {}", category);
        return getAll("Category", category);
    }

    /**
     * Get in-stock products.
     */
    public CList<CProductRow> getInStockProducts() {
        log.info("Getting in-stock products");
        return getAll("Stock Status", "In Stock");
    }

    /**
     * Get out-of-stock products.
     */
    public CList<CProductRow> getOutOfStockProducts() {
        log.info("Getting out-of-stock products");
        return getAll("Stock Status", "Out of Stock");
    }

    /**
     * Get products in price range.
     */
    public CList<CProductRow> getProductsInPriceRange(double minPrice, double maxPrice) {
        log.info("Getting products in price range: ${} - ${}", minPrice, maxPrice);
        return getAll(row -> {
            double price = row.getPriceAsDouble();
            return price >= minPrice && price <= maxPrice;
        });
    }

    /**
     * Get premium products (price > $500).
     */
    public CList<CProductRow> getPremiumProducts() {
        log.info("Getting premium products");
        return getProductsInPriceRange(500.0, Double.MAX_VALUE);
    }

    /**
     * Get highly rated products (rating >= 4.0).
     */
    public CList<CProductRow> getHighlyRatedProducts() {
        log.info("Getting highly rated products");
        return getAll(row -> row.getRating() >= 4.0);
    }

    /**
     * Get featured products.
     */
    public CList<CProductRow> getFeaturedProducts() {
        log.info("Getting featured products");
        return getAll(CProductRow::isFeatured);
    }

    /**
     * Get new arrivals (last 30 days).
     */
    public CList<CProductRow> getNewArrivals() {
        log.info("Getting new arrivals");
        return getAll(row -> row.isNewArrival(30));
    }

    /**
     * Check if product exists.
     */
    public boolean hasProduct(String productName) {
        return findProductByName(productName) != null;
    }

    /**
     * Check if product is in stock.
     */
    public boolean isProductInStock(String productName) {
        CProductRow product = findProductByName(productName);
        return product != null && product.isInStock();
    }

    /**
     * Get product count.
     */
    public int getProductCount() {
        return size();
    }

    /**
     * Get total inventory value.
     */
    public double getTotalInventoryValue() {
        return getAll().stream()
            .mapToDouble(row -> row.getPriceAsDouble() * row.getStockQuantity())
            .sum();
    }

    /**
     * Get average product price.
     */
    public double getAveragePrice() {
        return getAll().stream()
            .mapToDouble(CProductRow::getPriceAsDouble)
            .average()
            .orElse(0.0);
    }
}
```

### Example 2: Order Management Table

```java
package org.catools.web.tables;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTable;
import org.catools.common.collections.CList;

import java.time.LocalDate;
import java.util.Map;

/**
 * Order management table with complex search and filtering.
 */
@Slf4j
public class COrderTable extends CWebTable<CDriver, COrderRow> {

    public COrderTable(CDriver driver) {
        super("Order Management Table", driver, "//table[@id='orders']", 15);
        log.info("Order table initialized");
    }

    @Override
    public COrderRow getRecord(int idx) {
        return new COrderRow("Order Row " + idx, getDriver(), idx, this);
    }

    // Business methods

    /**
     * Find order by order number.
     */
    public COrderRow findOrderByNumber(String orderNumber) {
        log.info("Finding order: {}", orderNumber);
        return getFirst("Order #", orderNumber);
    }

    /**
     * Get all orders for customer.
     */
    public CList<COrderRow> getCustomerOrders(String customerName) {
        log.info("Getting orders for customer: {}", customerName);
        return getAll("Customer", customerName);
    }

    /**
     * Get orders by status.
     */
    public CList<COrderRow> getOrdersByStatus(String status) {
        log.info("Getting orders with status: {}", status);
        return getAll("Status", status);
    }

    /**
     * Get pending orders.
     */
    public CList<COrderRow> getPendingOrders() {
        log.info("Getting pending orders");
        return getOrdersByStatus("Pending");
    }

    /**
     * Get completed orders.
     */
    public CList<COrderRow> getCompletedOrders() {
        log.info("Getting completed orders");
        return getOrdersByStatus("Completed");
    }

    /**
     * Get cancelled orders.
     */
    public CList<COrderRow> getCancelledOrders() {
        log.info("Getting cancelled orders");
        return getOrdersByStatus("Cancelled");
    }

    /**
     * Get orders above amount.
     */
    public CList<COrderRow> getOrdersAboveAmount(double minAmount) {
        log.info("Getting orders above ${}", minAmount);
        return getAll(row -> row.getTotalAsDouble() >= minAmount);
    }

    /**
     * Get high-value orders ($1000+).
     */
    public CList<COrderRow> getHighValueOrders() {
        log.info("Getting high-value orders");
        return getOrdersAboveAmount(1000.0);
    }

    /**
     * Get orders in date range.
     */
    public CList<COrderRow> getOrdersInDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Getting orders from {} to {}", startDate, endDate);
        return getAll(row -> row.isOrderDateBetween(startDate, endDate));
    }

    /**
     * Get recent orders (last N days).
     */
    public CList<COrderRow> getRecentOrders(int days) {
        log.info("Getting orders from last {} days", days);
        LocalDate startDate = LocalDate.now().minusDays(days);
        LocalDate endDate = LocalDate.now();
        return getOrdersInDateRange(startDate, endDate);
    }

    /**
     * Get orders matching multiple criteria.
     */
    public CList<COrderRow> findOrders(String status, String customer) {
        log.info("Finding orders - Status: {}, Customer: {}", status, customer);
        
        Map<String, String> criteria = ImmutableMap.of(
            "Status", status,
            "Customer", customer
        );
        
        return getAll(criteria);
    }

    /**
     * Get customer's pending orders.
     */
    public CList<COrderRow> getCustomerPendingOrders(String customerName) {
        log.info("Getting pending orders for: {}", customerName);
        return findOrders("Pending", customerName);
    }

    /**
     * Check if order exists.
     */
    public boolean orderExists(String orderNumber) {
        return findOrderByNumber(orderNumber) != null;
    }

    /**
     * Check if customer has orders.
     */
    public boolean customerHasOrders(String customerName) {
        return !getCustomerOrders(customerName).isEmpty();
    }

    /**
     * Get order count.
     */
    public int getOrderCount() {
        return size();
    }

    /**
     * Get total order value.
     */
    public double getTotalOrderValue() {
        return getAll().stream()
            .mapToDouble(COrderRow::getTotalAsDouble)
            .sum();
    }

    /**
     * Get average order value.
     */
    public double getAverageOrderValue() {
        return getAll().stream()
            .mapToDouble(COrderRow::getTotalAsDouble)
            .average()
            .orElse(0.0);
    }
}
```

---

## Summary

`CWebTable` provides powerful table interaction capabilities with:

- **Automatic Header Mapping** - Column names to indices
- **Flexible Search** - By column name, multiple criteria, or predicates
- **Business Language Methods** - Domain-specific query operations
- **Row Retrieval Patterns** - getFirst(), getAny(), getAll(), etc.
- **Extension Framework** - All elements support verification, wait, and state methods
- **XPath Customization** - Support for non-standard table structures
- **Thread-Safe** - Thread-local search criteria management

**Critical Rules:**
1. **Always implement getRecord()** - Required abstract method
2. **Use business language** - Methods describe domain operations
3. **Use predicates for complex filtering** - More efficient than multiple queries
4. **Check for null returns** - Search methods may return null
5. **Provide existence check methods** - Convenient for tests
6. **Customize XPath for non-standard tables** - Override default paths
7. **Row interactions in CWebTableRow** - Not in table

**For More Information:**

- See `webpage-guide.prompts.md` for CWebPage (contains tables)
- See `webtablerow-guide.prompts.md` for CWebTableRow (row interactions)
- See `webform-guide.prompts.md` for CWebForm (user interactions)
- See `webelement-classes-guide.prompts.md` for CWebElement properties

**Version History:**

- **2.0.0** (Nov 28, 2025) - Initial comprehensive guide


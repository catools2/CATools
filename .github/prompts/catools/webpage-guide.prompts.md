# CWebPage Guide

**Version:** 2.0.0  
**Last Updated:** November 28, 2025  
**Framework:** CATools Web Automation  
**Java Version:** 21

---

## Table of Contents

1. [Introduction](#introduction)
2. [CWebPage Overview](#cwebpage-overview)
3. [Creating CWebPage Implementations](#creating-cwebpage-implementations)
4. [Page Components Organization](#page-components-organization)
5. [Business Language Methods](#business-language-methods)
6. [Page Verification](#page-verification)
7. [Advanced Usage Patterns](#advanced-usage-patterns)
8. [Best Practices](#best-practices)
9. [Common Pitfalls](#common-pitfalls)
10. [Examples](#examples)

---

## Introduction

`CWebPage` is the abstract base class for implementing page objects in CATools web automation framework. It provides automatic page verification, element initialization, and a structured approach to organizing forms, tables, and page-specific components.

**This guide complements the MCP automation patterns documented in `catools-web-mcp.agents.md`.**

### Key Features

- **Automatic Page Verification** - Verifies page loaded correctly via title pattern matching
- **Element Initialization** - Automatic initialization via `CWebElementFactory`
- **Component Organization** - Contains forms (CWebForm) and tables (CWebTable)
- **Business Language** - Methods use business terminology, not technical implementation details
- **Retry Mechanism** - Optional page refresh if initial load fails
- **Extension Framework Integration** - All elements support verification, wait, and state methods
- **Type Safety** - Generic typing `<DR extends CDriver>` for driver instance

### Page Object Pattern

**CWebPage contains:**
- ✅ **Forms** (CWebForm) - User interactions with input fields
- ✅ **Tables** (CWebTable) - Displaying and querying data
- ✅ **Business Methods** - Actions described in business language
- ✅ **Verification Methods** - Page state validation

**CWebPage does NOT contain:**
- ❌ Direct locators for individual elements (put in CWebForm instead)
- ❌ Technical implementation details exposed to tests
- ❌ Row-level interactions (put in CWebTableRow instead)

---

## CWebPage Overview

### Class Signature

```java
public abstract class CWebPage<DR extends CDriver> implements CWebComponent<DR>
```

### Type Parameters

- `<DR>` - The driver type extending `CDriver`

### Implemented Interfaces

- `CWebComponent<DR>` - Provides driver access and component contract

### Core Components

1. **Driver Reference** - Immutable `DR driver` field for browser automation
2. **Title Pattern** - Regex pattern for page title verification
3. **Element Factory** - Automatic initialization via `CWebElementFactory.initElements(this)`
4. **Forms** - Encapsulated user interaction components
5. **Tables** - Data display and query components

### Lifecycle

```java
// 1. Constructor is called with driver and title pattern
public MyPage(CDriver driver) {
    super(driver, "Expected Page Title", 10);
    // 2. Driver is stored
    // 3. CWebElementFactory.initElements(this) is called automatically
    // 4. Title verification performed
    // 5. All forms and tables are initialized
}

// 6. Page is ready to use
page.login("user", "pass");
page.searchProducts("laptop");
```

### Constructor Options

```java
// 1. Default timeout (10 seconds)
public CWebPage(DR driver, String titlePattern)

// 2. Custom timeout
public CWebPage(DR driver, String titlePattern, int waitSecs)

// 3. With refresh retry
public CWebPage(DR driver, String titlePattern, int waitSecs, boolean tryRefreshIfNotDisplayed)

// 4. Full customization
public CWebPage(DR driver, String titlePattern, int waitSecs, boolean tryRefreshIfNotDisplayed, int waitSecsAfterRefresh)
```

---

## Creating CWebPage Implementations

### Method 1: Basic Page with Forms

```java
package org.catools.web.pages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.forms.CLoginForm;
import org.catools.web.forms.CSearchForm;

/**
 * Login page for application authentication.
 * Contains login form and provides business-level login methods.
 */
@Slf4j
@Getter
public class CLoginPage extends CWebPage<CDriver> {

    // Forms - encapsulate user interactions
    private final CLoginForm loginForm;
    private final CSearchForm searchForm;

    /**
     * Constructs login page and verifies page loaded correctly.
     */
    public CLoginPage(CDriver driver) {
        super(driver, "Login.*MyApp", 10);
        
        // Initialize forms after page verification
        this.loginForm = new CLoginForm(driver);
        this.searchForm = new CSearchForm(driver);
        
        log.info("Login page initialized");
    }

    // Business language methods

    /**
     * Perform login with credentials.
     * Business method - describes WHAT, not HOW.
     */
    public void loginAsUser(String username, String password) {
        log.info("Logging in as user: {}", username);
        loginForm.login(username, password);
    }

    /**
     * Perform login and verify success.
     */
    public CDashboardPage loginAndNavigateToDashboard(String username, String password) {
        loginAsUser(username, password);
        return new CDashboardPage(getDriver());
    }

    /**
     * Check if login failed.
     */
    public boolean isLoginFailed() {
        return loginForm.hasError();
    }

    /**
     * Get login error message.
     */
    public String getLoginError() {
        return loginForm.getErrorMessage();
    }

    /**
     * Search for content from login page.
     */
    public CSearchResultsPage searchFor(String query) {
        log.info("Searching for: {}", query);
        searchForm.search(query);
        return new CSearchResultsPage(getDriver());
    }
}
```

### Method 2: Page with Tables

```java
package org.catools.web.pages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.forms.CProductFilterForm;
import org.catools.web.tables.CProductTable;
import org.catools.web.tables.CProductRow;
import org.catools.common.collections.CList;

/**
 * Product catalog page displaying products in a table.
 * Contains filter form and product table with business methods.
 */
@Slf4j
@Getter
public class CProductCatalogPage extends CWebPage<CDriver> {

    private final CProductFilterForm filterForm;
    private final CProductTable productTable;

    public CProductCatalogPage(CDriver driver) {
        super(driver, "Products.*Catalog", 15);
        
        this.filterForm = new CProductFilterForm(driver);
        this.productTable = new CProductTable(driver);
        
        log.info("Product catalog page initialized");
    }

    // Business language methods

    /**
     * Filter products by category.
     */
    public void filterByCategory(String category) {
        log.info("Filtering products by category: {}", category);
        filterForm.selectCategory(category);
        filterForm.applyFilters();
    }

    /**
     * Filter products by price range.
     */
    public void filterByPriceRange(String minPrice, String maxPrice) {
        log.info("Filtering products by price range: {} - {}", minPrice, maxPrice);
        filterForm.setPriceRange(minPrice, maxPrice);
        filterForm.applyFilters();
    }

    /**
     * Find product by name.
     * Business method - returns what the business needs.
     */
    public CProductRow findProductByName(String productName) {
        log.info("Finding product: {}", productName);
        return productTable.getFirst("Product Name", productName);
    }

    /**
     * Get all products in a category.
     */
    public CList<CProductRow> getProductsInCategory(String category) {
        log.info("Getting all products in category: {}", category);
        return productTable.getAll("Category", category);
    }

    /**
     * Check if product is available.
     */
    public boolean isProductAvailable(String productName) {
        CProductRow product = findProductByName(productName);
        return product != null && product.isInStock();
    }

    /**
     * Get product price by name.
     */
    public String getProductPrice(String productName) {
        CProductRow product = findProductByName(productName);
        return product != null ? product.getPrice() : "";
    }

    /**
     * Add product to cart by name.
     */
    public void addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        CProductRow product = findProductByName(productName);
        if (product != null) {
            product.clickAddToCart();
        }
    }

    /**
     * Get total product count.
     */
    public int getProductCount() {
        return productTable.size();
    }

    /**
     * Clear all filters.
     */
    public void clearFilters() {
        log.info("Clearing all filters");
        filterForm.clearFilters();
    }
}
```

### Method 3: Page with Forms and Tables

```java
package org.catools.web.pages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.forms.COrderFilterForm;
import org.catools.web.forms.COrderSearchForm;
import org.catools.web.tables.COrderTable;
import org.catools.web.tables.COrderRow;
import org.catools.common.collections.CList;

/**
 * Order management page with search, filters, and order table.
 * Demonstrates page with both forms and tables.
 */
@Slf4j
@Getter
public class COrderManagementPage extends CWebPage<CDriver> {

    // Forms for user interaction
    private final COrderSearchForm searchForm;
    private final COrderFilterForm filterForm;
    
    // Table for data display
    private final COrderTable orderTable;

    public COrderManagementPage(CDriver driver) {
        super(driver, "Order Management.*Dashboard", 15, true);
        
        this.searchForm = new COrderSearchForm(driver);
        this.filterForm = new COrderFilterForm(driver);
        this.orderTable = new COrderTable(driver);
        
        log.info("Order management page initialized");
    }

    // Business language methods

    /**
     * Search for order by order number.
     */
    public COrderRow searchOrderByNumber(String orderNumber) {
        log.info("Searching for order: {}", orderNumber);
        searchForm.searchByOrderNumber(orderNumber);
        return orderTable.getFirst("Order #", orderNumber);
    }

    /**
     * Search for orders by customer name.
     */
    public CList<COrderRow> searchOrdersByCustomer(String customerName) {
        log.info("Searching orders for customer: {}", customerName);
        searchForm.searchByCustomerName(customerName);
        return orderTable.getAll("Customer", customerName);
    }

    /**
     * Filter orders by status.
     */
    public void filterOrdersByStatus(String status) {
        log.info("Filtering orders by status: {}", status);
        filterForm.selectStatus(status);
        filterForm.applyFilters();
    }

    /**
     * Filter orders by date range.
     */
    public void filterOrdersByDateRange(String startDate, String endDate) {
        log.info("Filtering orders by date range: {} to {}", startDate, endDate);
        filterForm.setDateRange(startDate, endDate);
        filterForm.applyFilters();
    }

    /**
     * Get all pending orders.
     */
    public CList<COrderRow> getPendingOrders() {
        log.info("Getting all pending orders");
        filterOrdersByStatus("Pending");
        return orderTable.getAll();
    }

    /**
     * Get order total amount.
     */
    public String getOrderTotal(String orderNumber) {
        COrderRow order = searchOrderByNumber(orderNumber);
        return order != null ? order.getTotalAmount() : "";
    }

    /**
     * Cancel order by order number.
     */
    public void cancelOrder(String orderNumber) {
        log.info("Canceling order: {}", orderNumber);
        COrderRow order = searchOrderByNumber(orderNumber);
        if (order != null) {
            order.clickCancelButton();
        }
    }

    /**
     * View order details.
     */
    public COrderDetailsPage viewOrderDetails(String orderNumber) {
        log.info("Viewing details for order: {}", orderNumber);
        COrderRow order = searchOrderByNumber(orderNumber);
        if (order != null) {
            order.clickViewDetailsLink();
        }
        return new COrderDetailsPage(getDriver());
    }

    /**
     * Export filtered orders.
     */
    public void exportOrders() {
        log.info("Exporting orders");
        filterForm.clickExportButton();
    }

    /**
     * Get order count.
     */
    public int getOrderCount() {
        return orderTable.size();
    }
}
```

---

## Page Components Organization

### Proper Page Structure

```java
/**
 * Proper page organization following CATools patterns.
 */
@Slf4j
@Getter
public class CProperlyOrganizedPage extends CWebPage<CDriver> {

    // 1. Forms - encapsulate user interactions
    private final CLoginForm loginForm;
    private final CSearchForm searchForm;
    private final CFilterForm filterForm;

    // 2. Tables - encapsulate data display and queries
    private final CUserTable userTable;
    private final COrderTable orderTable;

    // 3. Constructor - initializes page and components
    public CProperlyOrganizedPage(CDriver driver) {
        super(driver, "Page Title Pattern", 10);
        
        this.loginForm = new CLoginForm(driver);
        this.searchForm = new CSearchForm(driver);
        this.filterForm = new CFilterForm(driver);
        this.userTable = new CUserTable(driver);
        this.orderTable = new COrderTable(driver);
    }

    // 4. Business language methods - describe WHAT, not HOW
    public void loginAsAdmin(String username, String password) {
        loginForm.login(username, password);
    }

    public CUserRow findUserByEmail(String email) {
        return userTable.getFirst("Email", email);
    }

    public void filterOrdersByStatus(String status) {
        filterForm.selectStatus(status);
        filterForm.applyFilters();
    }

    // 5. Verification methods - check page state
    public boolean isUserTableDisplayed() {
        return userTable.isVisible();
    }

    public void verifyPageLoaded() {
        verifyDisplayed();
        userTable.Visible.verifyTrue("User table should be visible");
    }
}
```

### What NOT to Put in CWebPage

```java
/**
 * WRONG - Don't expose individual element locators in page.
 */
public class CBadPage extends CWebPage<CDriver> {
    
    // ❌ WRONG - Individual element locators belong in CWebForm
    @CFindBy(id = "username")
    private CWebElement<CDriver> usernameField;
    
    @CFindBy(id = "password")
    private CWebElement<CDriver> passwordField;
    
    // ❌ WRONG - Technical method names, not business language
    public void typeUsername(String text) {
        usernameField.type(text);
    }
}

/**
 * CORRECT - Use forms to encapsulate element locators.
 */
public class CGoodPage extends CWebPage<CDriver> {
    
    // ✅ CORRECT - Form encapsulates elements
    private final CLoginForm loginForm;
    
    public CGoodPage(CDriver driver) {
        super(driver, "Title", 10);
        this.loginForm = new CLoginForm(driver);
    }
    
    // ✅ CORRECT - Business language method
    public void loginAsUser(String username, String password) {
        loginForm.login(username, password);
    }
}
```

---

## Business Language Methods

### Principle: Describe WHAT, Not HOW

```java
// ✅ CORRECT - Business language (WHAT)
public void placeOrder(String product, int quantity) {
    searchProduct(product);
    addToCart(quantity);
    proceedToCheckout();
    confirmOrder();
}

public boolean isProductInStock(String productName) {
    return productTable.getFirst("Name", productName).isAvailable();
}

public String getCustomerOrderTotal(String customerName) {
    return orderTable.getFirst("Customer", customerName).getTotal();
}

// ❌ WRONG - Technical implementation details (HOW)
public void clickSubmitButton() {
    submitButton.click();
}

public void typeIntoSearchBox(String text) {
    searchBox.type(text);
}

public String getTextFromElement() {
    return element.getText();
}
```

### Business Language Examples

```java
@Slf4j
@Getter
public class CShoppingCartPage extends CWebPage<CDriver> {

    private final CCartItemsTable cartItemsTable;
    private final CCheckoutForm checkoutForm;

    public CShoppingCartPage(CDriver driver) {
        super(driver, "Shopping Cart", 10);
        this.cartItemsTable = new CCartItemsTable(driver);
        this.checkoutForm = new CCheckoutForm(driver);
    }

    // Business language methods

    /**
     * Update product quantity in cart.
     */
    public void updateProductQuantity(String productName, int newQuantity) {
        log.info("Updating quantity for {} to {}", productName, newQuantity);
        CCartItemRow item = cartItemsTable.getFirst("Product", productName);
        if (item != null) {
            item.setQuantity(newQuantity);
        }
    }

    /**
     * Remove product from cart.
     */
    public void removeProduct(String productName) {
        log.info("Removing product from cart: {}", productName);
        CCartItemRow item = cartItemsTable.getFirst("Product", productName);
        if (item != null) {
            item.clickRemoveButton();
        }
    }

    /**
     * Apply discount code.
     */
    public void applyDiscountCode(String code) {
        log.info("Applying discount code: {}", code);
        checkoutForm.enterDiscountCode(code);
        checkoutForm.applyDiscount();
    }

    /**
     * Proceed to checkout.
     */
    public CCheckoutPage proceedToCheckout() {
        log.info("Proceeding to checkout");
        checkoutForm.clickCheckoutButton();
        return new CCheckoutPage(getDriver());
    }

    /**
     * Get cart total amount.
     */
    public String getCartTotal() {
        return checkoutForm.getTotalAmount();
    }

    /**
     * Get number of items in cart.
     */
    public int getCartItemCount() {
        return cartItemsTable.size();
    }

    /**
     * Check if cart is empty.
     */
    public boolean isCartEmpty() {
        return cartItemsTable.size() == 0;
    }

    /**
     * Verify product is in cart.
     */
    public boolean isProductInCart(String productName) {
        return cartItemsTable.getFirst("Product", productName) != null;
    }
}
```

---

## Page Verification

### Built-in Verification

```java
// Automatic verification on construction
public CLoginPage(CDriver driver) {
    super(driver, "Login.*", 10);  // Verifies title matches pattern
}

// Manual verification
public void verifyPageDisplayed() {
    verifyDisplayed();  // Checks title pattern
}

// Custom verification
public void verifyPageReady() {
    verifyDisplayed();
    loginForm.getUsernameField().Visible.verifyTrue("Username field visible");
    loginForm.getPasswordField().Visible.verifyTrue("Password field visible");
}
```

### Custom Verification Methods

```java
@Slf4j
@Getter
public class CDashboardPage extends CWebPage<CDriver> {

    private final CUserTable userTable;
    private final CStatsTable statsTable;

    public CDashboardPage(CDriver driver) {
        super(driver, "Dashboard.*", 15);
        this.userTable = new CUserTable(driver);
        this.statsTable = new CStatsTable(driver);
    }

    /**
     * Verify dashboard loaded completely.
     */
    public void verifyDashboardLoaded() {
        log.info("Verifying dashboard loaded");
        
        verifyDisplayed();
        
        userTable.Visible.verifyTrue("User table should be visible");
        statsTable.Visible.verifyTrue("Stats table should be visible");
        
        getDriver().Title.verifyContains("Dashboard", "Title should contain Dashboard");
        getDriver().Url.verifyContains("/dashboard", "URL should contain /dashboard");
    }

    /**
     * Verify user has access to admin features.
     */
    public void verifyAdminAccess() {
        log.info("Verifying admin access");
        // Business-level verification
        verifyDashboardLoaded();
        // Add admin-specific checks
    }

    /**
     * Wait for data to load.
     */
    public void waitForDataLoad() {
        log.info("Waiting for data to load");
        statsTable.Visible.waitUntilTrue(30);
        userTable.Visible.waitUntilTrue(30);
    }
}
```

---

## Advanced Usage Patterns

### Pattern 1: Page with Lazy Initialization

```java
@Slf4j
public class CLazyInitPage extends CWebPage<CDriver> {

    private CSearchForm searchForm;
    private CResultsTable resultsTable;

    public CLazyInitPage(CDriver driver) {
        super(driver, "Search Results", 10);
    }

    /**
     * Lazy init search form.
     */
    private CSearchForm getSearchForm() {
        if (searchForm == null) {
            searchForm = new CSearchForm(getDriver());
        }
        return searchForm;
    }

    /**
     * Lazy init results table.
     */
    private CResultsTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = new CResultsTable(getDriver());
        }
        return resultsTable;
    }

    // Business methods use lazy initialization
    public void searchFor(String query) {
        getSearchForm().search(query);
    }

    public int getResultCount() {
        return getResultsTable().size();
    }
}
```

### Pattern 2: Page with Navigation

```java
@Slf4j
@Getter
public class CNavigablePage extends CWebPage<CDriver> {

    private final CNavigationMenu navigationMenu;

    public CNavigablePage(CDriver driver) {
        super(driver, "Main Page", 10);
        this.navigationMenu = new CNavigationMenu(driver);
    }

    /**
     * Navigate to dashboard.
     */
    public CDashboardPage navigateToDashboard() {
        log.info("Navigating to dashboard");
        navigationMenu.clickDashboard();
        return new CDashboardPage(getDriver());
    }

    /**
     * Navigate to orders.
     */
    public COrdersPage navigateToOrders() {
        log.info("Navigating to orders");
        navigationMenu.clickOrders();
        return new COrdersPage(getDriver());
    }

    /**
     * Navigate to products.
     */
    public CProductsPage navigateToProducts() {
        log.info("Navigating to products");
        navigationMenu.clickProducts();
        return new CProductsPage(getDriver());
    }

    /**
     * Logout.
     */
    public CLoginPage logout() {
        log.info("Logging out");
        navigationMenu.clickLogout();
        return new CLoginPage(getDriver());
    }
}
```

### Pattern 3: Page with Modal Dialogs

```java
@Slf4j
@Getter
public class CPageWithModals extends CWebPage<CDriver> {

    private final CUserTable userTable;

    public CPageWithModals(CDriver driver) {
        super(driver, "Users", 10);
        this.userTable = new CUserTable(driver);
    }

    /**
     * Delete user (opens confirmation modal).
     */
    public void deleteUser(String username) {
        log.info("Deleting user: {}", username);
        
        CUserRow user = userTable.getFirst("Username", username);
        if (user != null) {
            user.clickDeleteButton();
            
            // Wait for modal
            CConfirmationModal modal = new CConfirmationModal(getDriver());
            modal.confirm();
        }
    }

    /**
     * Edit user (opens edit modal).
     */
    public void editUser(String username, String newEmail) {
        log.info("Editing user: {}", username);
        
        CUserRow user = userTable.getFirst("Username", username);
        if (user != null) {
            user.clickEditButton();
            
            // Interact with edit modal
            CEditUserModal modal = new CEditUserModal(getDriver());
            modal.setEmail(newEmail);
            modal.save();
        }
    }
}
```

### Pattern 4: Page with Async Content

```java
@Slf4j
@Getter
public class CAsyncContentPage extends CWebPage<CDriver> {

    private final CDataTable dataTable;

    public CAsyncContentPage(CDriver driver) {
        super(driver, "Data Dashboard", 15);
        this.dataTable = new CDataTable(driver);
    }

    /**
     * Refresh data and wait for reload.
     */
    public void refreshData() {
        log.info("Refreshing data");
        
        // Click refresh button
        getDriver().findElementById("refresh-btn", 5).click();
        
        // Wait for loading indicator
        CWebElement<CDriver> loadingSpinner = getDriver().findElementByClassName("loading", 5);
        loadingSpinner.Visible.waitUntilTrue(5);
        
        // Wait for loading to complete
        loadingSpinner.Visible.waitUntilFalse(30);
        
        // Verify data loaded
        dataTable.Visible.verifyTrue("Data table should be visible after refresh");
    }

    /**
     * Wait for async data load.
     */
    public void waitForDataLoad() {
        log.info("Waiting for data to load");
        
        dataTable.Visible.waitUntilTrue(30);
        
        // Wait for at least one row
        getDriver().findElementByXPath(dataTable.getRowXpath(0), 30)
            .Visible.waitUntilTrue(30);
    }
}
```

---

## Best Practices

### 1. Use Business Language

```java
// ✅ CORRECT - Business language
public void placeOrderForProduct(String productName, int quantity) {
    searchProduct(productName);
    addToCart(quantity);
    checkout();
}

public String getCustomerOrderStatus(String orderNumber) {
    return orderTable.getFirst("Order #", orderNumber).getStatus();
}

// ❌ WRONG - Technical language
public void clickSearchButton() {
    searchButton.click();
}

public String getTextFromStatusColumn() {
    return statusElement.getText();
}
```

### 2. Encapsulate Components in Forms and Tables

```java
// ✅ CORRECT - Components encapsulated
public class CGoodPage extends CWebPage<CDriver> {
    private final CLoginForm loginForm;  // Form has all locators
    private final CUserTable userTable;  // Table has all locators
    
    public void loginAsUser(String user, String pass) {
        loginForm.login(user, pass);  // Form handles interaction
    }
}

// ❌ WRONG - Direct element locators in page
public class CBadPage extends CWebPage<CDriver> {
    @CFindBy(id = "username")
    private CWebElement<CDriver> usernameField;  // Should be in form!
    
    public void typeUsername(String user) {
        usernameField.type(user);
    }
}
```

### 3. Initialize Components in Constructor

```java
// ✅ CORRECT - Initialize in constructor
public CMyPage(CDriver driver) {
    super(driver, "Title", 10);
    this.loginForm = new CLoginForm(driver);
    this.userTable = new CUserTable(driver);
}

// ❌ WRONG - Initialize outside constructor
public CMyPage(CDriver driver) {
    super(driver, "Title", 10);
}

public void someMethod() {
    this.loginForm = new CLoginForm(getDriver());  // Don't do this!
}
```

### 4. Return New Page Objects for Navigation

```java
// ✅ CORRECT - Return new page object
public CDashboardPage login(String user, String pass) {
    loginForm.login(user, pass);
    return new CDashboardPage(getDriver());
}

// ❌ AVOID - void return for navigation
public void login(String user, String pass) {
    loginForm.login(user, pass);
    // Caller doesn't know what page to expect
}
```

### 5. Provide Verification Methods

```java
// ✅ CORRECT - Verification methods
public void verifyPageLoaded() {
    verifyDisplayed();
    userTable.Visible.verifyTrue("User table should be visible");
    getDriver().Url.verifyContains("/users", "Should be on users page");
}

public boolean isUserDisplayed(String username) {
    return userTable.getFirst("Username", username) != null;
}
```

### 6. Use Appropriate Title Patterns

```java
// ✅ CORRECT - Flexible regex pattern
public CLoginPage(CDriver driver) {
    super(driver, "Login.*MyApp", 10);  // Matches "Login - MyApp", "Login Page - MyApp", etc.
}

// ✅ CORRECT - Exact match when needed
public CDashboardPage(CDriver driver) {
    super(driver, "Dashboard", 10);
}

// ❌ AVOID - Too specific
public CMyPage(CDriver driver) {
    super(driver, "Welcome to MyApp - Login Page - Version 1.0", 10);  // Too brittle
}
```

### 7. Handle Refresh Retry Appropriately

```java
// ✅ CORRECT - Use refresh for flaky pages
public CUnstablePage(CDriver driver) {
    super(driver, "Title", 10, true, 20);  // Try refresh if initial load fails
}

// ✅ CORRECT - No refresh for stable pages
public CStablePage(CDriver driver) {
    super(driver, "Title", 10);  // No refresh needed
}
```

---

## Common Pitfalls

### Pitfall 1: Exposing Element Locators in Page

```java
// ❌ WRONG - Element locators in page
public class CBadPage extends CWebPage<CDriver> {
    @CFindBy(id = "username")
    private CWebElement<CDriver> usernameField;
    
    @CFindBy(id = "password")
    private CWebElement<CDriver> passwordField;
}

// ✅ CORRECT - Use form to encapsulate locators
public class CGoodPage extends CWebPage<CDriver> {
    private final CLoginForm loginForm;  // Form contains locators
    
    public CGoodPage(CDriver driver) {
        super(driver, "Title", 10);
        this.loginForm = new CLoginForm(driver);
    }
}
```

### Pitfall 2: Using Technical Method Names

```java
// ❌ WRONG - Technical names
public void clickSubmitButton() {
    submitButton.click();
}

public void typeInSearchBox(String text) {
    searchBox.type(text);
}

// ✅ CORRECT - Business language
public void submitOrder() {
    checkoutForm.submit();
}

public void searchForProduct(String productName) {
    searchForm.search(productName);
}
```

### Pitfall 3: Not Initializing Components

```java
// ❌ WRONG - Components not initialized
public class CBadPage extends CWebPage<CDriver> {
    private CLoginForm loginForm;  // Null!
    
    public CBadPage(CDriver driver) {
        super(driver, "Title", 10);
        // Forgot to initialize!
    }
    
    public void login(String user, String pass) {
        loginForm.login(user, pass);  // NullPointerException!
    }
}

// ✅ CORRECT - Initialize in constructor
public class CGoodPage extends CWebPage<CDriver> {
    private final CLoginForm loginForm;
    
    public CGoodPage(CDriver driver) {
        super(driver, "Title", 10);
        this.loginForm = new CLoginForm(driver);  // Initialized
    }
}
```

### Pitfall 4: Mixing Row Operations in Page

```java
// ❌ WRONG - Row operations in page
public void clickEditButtonInRow(String username) {
    CUserRow row = userTable.getFirst("Username", username);
    // Don't manipulate row elements here
    getDriver().findElementById("edit-" + username, 5).click();
}

// ✅ CORRECT - Row operations in CWebTableRow
public void editUser(String username) {
    CUserRow row = userTable.getFirst("Username", username);
    row.clickEditButton();  // Row handles its own interactions
}
```

### Pitfall 5: Not Using Title Verification

```java
// ❌ RISKY - No title verification
public CMyPage(CDriver driver) {
    super(driver, "", 10);  // Empty title pattern - no verification!
}

// ✅ CORRECT - Always verify page loaded
public CMyPage(CDriver driver) {
    super(driver, "Expected Page Title", 10);  // Verifies correct page
}
```

### Pitfall 6: Exposing Forms/Tables as Public Mutable

```java
// ❌ WRONG - Public mutable fields
public class CBadPage extends CWebPage<CDriver> {
    public CLoginForm loginForm;  // Public and mutable!
}

// ✅ CORRECT - Private final with getter
@Getter
public class CGoodPage extends CWebPage<CDriver> {
    private final CLoginForm loginForm;  // Private and immutable
}
```

---

## Examples

### Example 1: E-Commerce Product Page

```java
package org.catools.web.pages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.forms.CProductSearchForm;
import org.catools.web.forms.CProductFilterForm;
import org.catools.web.tables.CProductTable;
import org.catools.web.tables.CProductRow;
import org.catools.common.collections.CList;

/**
 * E-commerce product listing page.
 * Demonstrates page with search, filters, and product table.
 */
@Slf4j
@Getter
public class CProductListingPage extends CWebPage<CDriver> {

    private final CProductSearchForm searchForm;
    private final CProductFilterForm filterForm;
    private final CProductTable productTable;

    public CProductListingPage(CDriver driver) {
        super(driver, "Products.*Store", 15);
        
        this.searchForm = new CProductSearchForm(driver);
        this.filterForm = new CProductFilterForm(driver);
        this.productTable = new CProductTable(driver);
        
        log.info("Product listing page initialized");
    }

    // Business language methods

    /**
     * Search for products by keyword.
     */
    public void searchProducts(String keyword) {
        log.info("Searching for products: {}", keyword);
        searchForm.search(keyword);
    }

    /**
     * Filter products by category.
     */
    public void filterByCategory(String category) {
        log.info("Filtering by category: {}", category);
        filterForm.selectCategory(category);
        filterForm.applyFilters();
    }

    /**
     * Filter products by price range.
     */
    public void filterByPriceRange(String min, String max) {
        log.info("Filtering by price: {} - {}", min, max);
        filterForm.setPriceRange(min, max);
        filterForm.applyFilters();
    }

    /**
     * Sort products by criterion.
     */
    public void sortBy(String criterion) {
        log.info("Sorting by: {}", criterion);
        filterForm.selectSortOption(criterion);
    }

    /**
     * Find product by name.
     */
    public CProductRow findProduct(String productName) {
        log.info("Finding product: {}", productName);
        return productTable.getFirst("Name", productName);
    }

    /**
     * Get all products in price range.
     */
    public CList<CProductRow> getProductsInPriceRange(String category, String minPrice, String maxPrice) {
        log.info("Getting products in category {} with price range {} - {}", category, minPrice, maxPrice);
        filterByCategory(category);
        filterByPriceRange(minPrice, maxPrice);
        return productTable.getAll();
    }

    /**
     * Add product to cart.
     */
    public void addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        CProductRow product = findProduct(productName);
        if (product != null) {
            product.clickAddToCartButton();
        }
    }

    /**
     * View product details.
     */
    public CProductDetailsPage viewProductDetails(String productName) {
        log.info("Viewing details for product: {}", productName);
        CProductRow product = findProduct(productName);
        if (product != null) {
            product.clickProductNameLink();
        }
        return new CProductDetailsPage(getDriver());
    }

    /**
     * Check if product is in stock.
     */
    public boolean isProductInStock(String productName) {
        CProductRow product = findProduct(productName);
        return product != null && product.isInStock();
    }

    /**
     * Get product price.
     */
    public String getProductPrice(String productName) {
        CProductRow product = findProduct(productName);
        return product != null ? product.getPrice() : "0.00";
    }

    /**
     * Get total product count.
     */
    public int getProductCount() {
        return productTable.size();
    }

    /**
     * Clear all filters.
     */
    public void clearAllFilters() {
        log.info("Clearing all filters");
        filterForm.clearFilters();
    }

    /**
     * Verify page loaded with products.
     */
    public void verifyPageLoaded() {
        verifyDisplayed();
        productTable.Visible.verifyTrue("Product table should be visible");
        searchForm.getSearchInput().Visible.verifyTrue("Search form should be visible");
    }
}
```

### Example 2: Admin User Management Page

```java
package org.catools.web.pages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.forms.CUserSearchForm;
import org.catools.web.forms.CUserFilterForm;
import org.catools.web.tables.CUserTable;
import org.catools.web.tables.CUserRow;
import org.catools.common.collections.CList;

/**
 * Admin page for user management.
 * Demonstrates page with user operations and table interactions.
 */
@Slf4j
@Getter
public class CUserManagementPage extends CWebPage<CDriver> {

    private final CUserSearchForm searchForm;
    private final CUserFilterForm filterForm;
    private final CUserTable userTable;

    public CUserManagementPage(CDriver driver) {
        super(driver, "User Management.*Admin", 15, true);
        
        this.searchForm = new CUserSearchForm(driver);
        this.filterForm = new CUserFilterForm(driver);
        this.userTable = new CUserTable(driver);
        
        log.info("User management page initialized");
    }

    // Business language methods

    /**
     * Search for user by username.
     */
    public CUserRow searchUser(String username) {
        log.info("Searching for user: {}", username);
        searchForm.searchByUsername(username);
        return userTable.getFirst("Username", username);
    }

    /**
     * Search users by email domain.
     */
    public CList<CUserRow> searchUsersByDomain(String domain) {
        log.info("Searching users by email domain: {}", domain);
        searchForm.searchByEmail("@" + domain);
        return userTable.getAll();
    }

    /**
     * Filter users by role.
     */
    public void filterUsersByRole(String role) {
        log.info("Filtering users by role: {}", role);
        filterForm.selectRole(role);
        filterForm.applyFilters();
    }

    /**
     * Filter users by status.
     */
    public void filterUsersByStatus(String status) {
        log.info("Filtering users by status: {}", status);
        filterForm.selectStatus(status);
        filterForm.applyFilters();
    }

    /**
     * Get all active users.
     */
    public CList<CUserRow> getActiveUsers() {
        log.info("Getting all active users");
        filterUsersByStatus("Active");
        return userTable.getAll();
    }

    /**
     * Get all admin users.
     */
    public CList<CUserRow> getAdminUsers() {
        log.info("Getting all admin users");
        filterUsersByRole("Admin");
        return userTable.getAll();
    }

    /**
     * Activate user account.
     */
    public void activateUser(String username) {
        log.info("Activating user: {}", username);
        CUserRow user = searchUser(username);
        if (user != null) {
            user.clickActivateButton();
        }
    }

    /**
     * Deactivate user account.
     */
    public void deactivateUser(String username) {
        log.info("Deactivating user: {}", username);
        CUserRow user = searchUser(username);
        if (user != null) {
            user.clickDeactivateButton();
        }
    }

    /**
     * Delete user account.
     */
    public void deleteUser(String username) {
        log.info("Deleting user: {}", username);
        CUserRow user = searchUser(username);
        if (user != null) {
            user.clickDeleteButton();
            // Confirm deletion in modal
            confirmDeletion();
        }
    }

    /**
     * Edit user details.
     */
    public CUserEditPage editUser(String username) {
        log.info("Editing user: {}", username);
        CUserRow user = searchUser(username);
        if (user != null) {
            user.clickEditButton();
        }
        return new CUserEditPage(getDriver());
    }

    /**
     * View user details.
     */
    public CUserDetailsPage viewUserDetails(String username) {
        log.info("Viewing details for user: {}", username);
        CUserRow user = searchUser(username);
        if (user != null) {
            user.clickViewLink();
        }
        return new CUserDetailsPage(getDriver());
    }

    /**
     * Check if user exists.
     */
    public boolean userExists(String username) {
        return searchUser(username) != null;
    }

    /**
     * Check if user is active.
     */
    public boolean isUserActive(String username) {
        CUserRow user = searchUser(username);
        return user != null && user.isActive();
    }

    /**
     * Get user role.
     */
    public String getUserRole(String username) {
        CUserRow user = searchUser(username);
        return user != null ? user.getRole() : "";
    }

    /**
     * Get user count.
     */
    public int getUserCount() {
        return userTable.size();
    }

    /**
     * Export users to CSV.
     */
    public void exportUsers() {
        log.info("Exporting users");
        filterForm.clickExportButton();
    }

    /**
     * Confirm deletion modal.
     */
    private void confirmDeletion() {
        CConfirmationModal modal = new CConfirmationModal(getDriver());
        modal.confirm();
    }

    /**
     * Verify page loaded.
     */
    public void verifyPageLoaded() {
        verifyDisplayed();
        userTable.Visible.verifyTrue("User table should be visible");
        getDriver().Url.verifyContains("/admin/users", "Should be on user management page");
    }
}
```

---

## Summary

`CWebPage` provides a structured approach to page object implementation with:

- **Automatic Page Verification** - Title pattern matching on construction
- **Component Organization** - Contains forms (user interactions) and tables (data display)
- **Business Language Methods** - Methods describe WHAT, not HOW
- **Element Initialization** - Automatic via `CWebElementFactory`
- **Proper Encapsulation** - Locators live in forms/tables, not in pages
- **Extension Framework Integration** - All components support verification, wait, and state methods
- **Navigation Support** - Methods return new page objects for navigation
- **Type Safety** - Generic typing for driver instance

**Critical Rules:**
1. **Use business language** - Methods describe intent, not implementation
2. **Encapsulate locators in forms/tables** - Never expose element locators in page
3. **Initialize components in constructor** - Forms and tables created after super() call
4. **Return page objects for navigation** - Help tests track page state
5. **Verify page loaded** - Use title pattern matching
6. **Provide verification methods** - Allow tests to verify page state

**For More Information:**

- See `webform-guide.prompts.md` for CWebForm (user interactions)
- See `webtable-guide.prompts.md` for CWebTable (data display)
- See `webtablerow-guide.prompts.md` for CWebTableRow (row interactions)
- See `webelement-classes-guide.prompts.md` for CWebElement properties
- See `extension-classes-guide.prompts.md` for extension framework details

**Version History:**

- **2.0.0** (Nov 28, 2025) - Initial comprehensive guide


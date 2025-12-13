# CWebTableRow Guide

**Version:** 2.0.0  
**Last Updated:** November 28, 2025  
**Framework:** CATools Web Automation  
**Java Version:** 21

---

## Table of Contents

1. [Introduction](#introduction)
2. [CWebTableRow Overview](#cwebtablerow-overview)
3. [Creating CWebTableRow Implementations](#creating-cwebtablerow-implementations)
4. [Cell Access and Data Extraction](#cell-access-and-data-extraction)
5. [User Interaction Methods](#user-interaction-methods)
6. [Advanced Usage Patterns](#advanced-usage-patterns)
7. [Best Practices](#best-practices)
8. [Common Pitfalls](#common-pitfalls)
9. [Examples](#examples)

---

## Introduction

`CWebTableRow` is the abstract base class for implementing table row components in CATools web automation framework. It provides functionality to interact with cells within a specific row, supporting both data extraction and user interactions (clicks, edits, etc.).

**This guide complements the table patterns documented in `webtable-guide.prompts.md`.**

### Key Features

- **Cell Access by Column Name** - Access cells using header names
- **Data Extraction** - Read cell text and attributes
- **User Interactions** - Click buttons, links, checkboxes within row
- **Nested Element Support** - Access child elements within cells
- **Element Initialization** - Automatic via `CWebElementFactory`
- **Extension Framework Integration** - All elements support verification, wait, and state methods
- **Type Safety** - Generic typing for driver and parent table

### Row Hierarchy

**CWebTableRow contains:**
- ✅ **Cell Locators** - Element locators for specific cells
- ✅ **Action Buttons/Links** - Interactive elements within the row
- ✅ **User Interaction Methods** - Click, edit, delete, select actions
- ✅ **Data Extraction Methods** - Get cell values

**CWebTableRow does NOT contain:**
- ❌ Table-level operations (put in CWebTable)
- ❌ Form input fields (put in CWebForm)
- ❌ Complex business logic (put in CWebPage)

---

## CWebTableRow Overview

### Class Signature

```java
public abstract class CWebTableRow<DR extends CDriver, P extends CWebTable<DR, ?>>
    extends CWebElement<DR> implements CWebComponent<DR>
```

### Type Parameters

- `<DR>` - The driver type extending `CDriver`
- `<P>` - The parent table type extending `CWebTable<DR, ?>`

### Implemented Interfaces

- `CWebComponent<DR>` - Provides driver access and component contract

### Core Components

1. **Parent Table** - Reference to the parent CWebTable
2. **Row Index** - Zero-based index within the table
3. **Cell XPath** - XPath template for locating cells
4. **Element Factory** - Automatic initialization via `CWebElementFactory`

### Constructor Pattern

```java
public CWebTableRow(String name, DR driver, int rowIndex, P parentTable, int waitSec) {
    super(name, driver, By.xpath(parentTable.getRowXpath(rowIndex)), waitSec);
    this.rowIndex = rowIndex;
    this.parentTable = parentTable;
    this.cellXpath = parentTable.getCellXpath() + "[%d]";
    CWebElementFactory.initElements(this);
}
```

---

## Creating CWebTableRow Implementations

### Method 1: Basic Row with Data Methods

```java
package org.catools.web.tables;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTableRow;
import org.openqa.selenium.By;

/**
 * Product table row with data extraction methods.
 */
@Slf4j
@Getter
public class CProductRow extends CWebTableRow<CDriver, CProductTable> {

    public CProductRow(String name, CDriver driver, int idx, CProductTable parentTable) {
        super(name, driver, idx, parentTable, DEFAULT_TIMEOUT);
        log.debug("Product row {} initialized", idx);
    }

    // Data extraction methods (business language)

    /**
     * Get product name from cell.
     */
    public String getProductName() {
        return getCell("Product Name").getText();
    }

    /**
     * Get product SKU.
     */
    public String getSKU() {
        return getCell("SKU").getText();
    }

    /**
     * Get product category.
     */
    public String getCategory() {
        return getCell("Category").getText();
    }

    /**
     * Get product price.
     */
    public String getPrice() {
        return getCell("Price").getText();
    }

    /**
     * Get price as double.
     */
    public double getPriceAsDouble() {
        String priceText = getPrice().replace("$", "").replace(",", "");
        return Double.parseDouble(priceText);
    }

    /**
     * Get stock status.
     */
    public String getStockStatus() {
        return getCell("Stock").getText();
    }

    /**
     * Check if product is in stock.
     */
    public boolean isInStock() {
        return "In Stock".equals(getStockStatus());
    }

    /**
     * Get product rating.
     */
    public double getRating() {
        String ratingText = getCell("Rating").getText();
        return Double.parseDouble(ratingText);
    }
}
```

### Method 2: Row with Action Methods

```java
package org.catools.web.tables;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTableRow;
import org.openqa.selenium.By;

/**
 * Product row with user interaction methods.
 */
@Slf4j
@Getter
public class CProductRow extends CWebTableRow<CDriver, CProductTable> {

    public CProductRow(String name, CDriver driver, int idx, CProductTable parentTable) {
        super(name, driver, idx, parentTable, DEFAULT_TIMEOUT);
    }

    // Data extraction
    public String getProductName() {
        return getCell("Product Name").getText();
    }

    public String getPrice() {
        return getCell("Price").getText();
    }

    public boolean isInStock() {
        return "In Stock".equals(getCell("Stock").getText());
    }

    // User interaction methods

    /**
     * Click "Add to Cart" button in actions column.
     */
    public void clickAddToCart() {
        log.info("Adding product to cart: {}", getProductName());
        
        CWebElement<CDriver> addToCartBtn = getCell("Actions", 0, "//button[@class='add-to-cart']");
        addToCartBtn.Clickable.verifyTrue("Add to cart button should be clickable");
        addToCartBtn.click();
    }

    /**
     * Click product name link to view details.
     */
    public void clickProductNameLink() {
        log.info("Viewing product details: {}", getProductName());
        
        CWebElement<CDriver> nameLink = getCell("Product Name", 0, "//a");
        nameLink.Clickable.verifyTrue("Product name link should be clickable");
        nameLink.click();
    }

    /**
     * Click "Quick View" button.
     */
    public void clickQuickView() {
        log.info("Opening quick view for: {}", getProductName());
        
        CWebElement<CDriver> quickViewBtn = getCell("Actions", 0, "//button[@class='quick-view']");
        quickViewBtn.click();
    }

    /**
     * Add product to wishlist.
     */
    public void addToWishlist() {
        log.info("Adding to wishlist: {}", getProductName());
        
        CWebElement<CDriver> wishlistBtn = getCell("Actions", 0, "//button[@class='wishlist']");
        wishlistBtn.click();
    }

    /**
     * Select product (checkbox).
     */
    public void selectProduct() {
        log.info("Selecting product: {}", getProductName());
        
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        checkbox.Selected.verifyTrue("Product should be selected");
    }

    /**
     * Deselect product (checkbox).
     */
    public void deselectProduct() {
        log.info("Deselecting product: {}", getProductName());
        
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        if (checkbox.isSelected()) {
            checkbox.click();
        }
        checkbox.Selected.verifyFalse("Product should not be selected");
    }
}
```

### Method 3: Row with Complex Cell Access

```java
package org.catools.web.tables;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTableRow;
import org.openqa.selenium.By;

/**
 * Order row with complex cell access patterns.
 */
@Slf4j
@Getter
public class COrderRow extends CWebTableRow<CDriver, COrderTable> {

    public COrderRow(String name, CDriver driver, int idx, COrderTable parentTable) {
        super(name, driver, idx, parentTable, DEFAULT_TIMEOUT);
    }

    // Data extraction with complex cell access

    /**
     * Get order number.
     */
    public String getOrderNumber() {
        return getCell("Order #").getText();
    }

    /**
     * Get customer name.
     */
    public String getCustomer() {
        return getCell("Customer").getText();
    }

    /**
     * Get order date.
     */
    public String getOrderDate() {
        return getCell("Date").getText();
    }

    /**
     * Get order status.
     */
    public String getStatus() {
        return getCell("Status").getText();
    }

    /**
     * Get total amount.
     */
    public String getTotalAmount() {
        return getCell("Total").getText();
    }

    /**
     * Get total as double.
     */
    public double getTotalAsDouble() {
        String totalText = getTotalAmount().replace("$", "").replace(",", "");
        return Double.parseDouble(totalText);
    }

    /**
     * Check if order is pending.
     */
    public boolean isPending() {
        return "Pending".equals(getStatus());
    }

    /**
     * Check if order is completed.
     */
    public boolean isCompleted() {
        return "Completed".equals(getStatus());
    }

    // User interactions

    /**
     * View order details.
     */
    public void clickViewDetails() {
        log.info("Viewing details for order: {}", getOrderNumber());
        
        CWebElement<CDriver> viewLink = getCell("Actions", 0, "//a[text()='View']");
        viewLink.click();
    }

    /**
     * Cancel order.
     */
    public void clickCancel() {
        log.info("Canceling order: {}", getOrderNumber());
        
        CWebElement<CDriver> cancelBtn = getCell("Actions", 0, "//button[text()='Cancel']");
        cancelBtn.Clickable.verifyTrue("Cancel button should be clickable");
        cancelBtn.click();
    }

    /**
     * Download invoice.
     */
    public void downloadInvoice() {
        log.info("Downloading invoice for order: {}", getOrderNumber());
        
        CWebElement<CDriver> downloadLink = getCell("Actions", 0, "//a[@class='download-invoice']");
        downloadLink.click();
    }

    /**
     * Track shipment.
     */
    public void trackShipment() {
        log.info("Tracking shipment for order: {}", getOrderNumber());
        
        CWebElement<CDriver> trackLink = getCell("Actions", 0, "//a[text()='Track']");
        trackLink.click();
    }

    /**
     * Get status badge color.
     */
    public String getStatusBadgeColor() {
        CWebElement<CDriver> statusCell = getCell("Status");
        CWebElement<CDriver> badge = statusCell.findElement(By.className("badge"));
        return badge.Css("background-color").get();
    }
}
```

---

## Cell Access and Data Extraction

### Basic Cell Access

```java
// Get cell by column name
CWebElement<CDriver> nameCell = row.getCell("Name");
String name = nameCell.getText();

// Get cell with duplicate header (by index)
CWebElement<CDriver> priceCell = row.getCell("Price", 0);
String price = priceCell.getText();

// Get child element within cell
CWebElement<CDriver> button = row.getCell("Actions", 0, "//button[@class='edit']");
button.click();
```

### Data Extraction Patterns

```java
public class CProductRow extends CWebTableRow<CDriver, CProductTable> {

    // Simple text extraction
    public String getProductName() {
        return getCell("Product Name").getText();
    }

    // With data transformation
    public double getPrice() {
        String priceText = getCell("Price").getText();
        return Double.parseDouble(priceText.replace("$", ""));
    }

    // With conditional logic
    public boolean isAvailable() {
        String status = getCell("Availability").getText();
        return "Available".equalsIgnoreCase(status);
    }

    // Extract from nested element
    public String getProductImageUrl() {
        CWebElement<CDriver> imageCell = getCell("Image");
        CWebElement<CDriver> image = imageCell.findElement(By.tagName("img"));
        return image.getAttribute("src");
    }

    // Extract attribute
    public String getProductId() {
        CWebElement<CDriver> nameCell = getCell("Product Name");
        return nameCell.getAttribute("data-product-id");
    }

    // Extract CSS property
    public String getStatusColor() {
        CWebElement<CDriver> statusCell = getCell("Status");
        return statusCell.Css("color").get();
    }
}
```

### Reading All Row Cells

```java
// Read all cells in row
CList<CWebTableCell> allCells = row.readRowCells();

for (CWebTableCell cell : allCells) {
    log.info("Column: {}, Value: {}, Visible: {}", 
        cell.getHeader(), 
        cell.getValue(), 
        cell.isVisible()
    );
}
```

---

## User Interaction Methods

### Click Actions

```java
public class CUserRow extends CWebTableRow<CDriver, CUserTable> {

    /**
     * Click edit button.
     */
    public void clickEditButton() {
        log.info("Editing user: {}", getUsername());
        
        CWebElement<CDriver> editBtn = getCell("Actions", 0, "//button[@class='edit']");
        editBtn.Clickable.verifyTrue("Edit button should be clickable");
        editBtn.click();
    }

    /**
     * Click delete button.
     */
    public void clickDeleteButton() {
        log.info("Deleting user: {}", getUsername());
        
        CWebElement<CDriver> deleteBtn = getCell("Actions", 0, "//button[@class='delete']");
        deleteBtn.Clickable.verifyTrue("Delete button should be clickable");
        deleteBtn.click();
    }

    /**
     * Click username link.
     */
    public void clickUsernameLink() {
        log.info("Clicking username: {}", getUsername());
        
        CWebElement<CDriver> usernameLink = getCell("Username", 0, "//a");
        usernameLink.click();
    }

    /**
     * Activate user.
     */
    public void clickActivateButton() {
        log.info("Activating user: {}", getUsername());
        
        CWebElement<CDriver> activateBtn = getCell("Actions", 0, "//button[text()='Activate']");
        activateBtn.click();
    }

    /**
     * Deactivate user.
     */
    public void clickDeactivateButton() {
        log.info("Deactivating user: {}", getUsername());
        
        CWebElement<CDriver> deactivateBtn = getCell("Actions", 0, "//button[text()='Deactivate']");
        deactivateBtn.click();
    }
}
```

### Checkbox/Selection Actions

```java
public class CProductRow extends CWebTableRow<CDriver, CProductTable> {

    /**
     * Select row checkbox.
     */
    public void select() {
        log.info("Selecting row for product: {}", getProductName());
        
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        checkbox.Selected.verifyTrue("Row should be selected");
    }

    /**
     * Deselect row checkbox.
     */
    public void deselect() {
        log.info("Deselecting row for product: {}", getProductName());
        
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        if (checkbox.isSelected()) {
            checkbox.click();
        }
        checkbox.Selected.verifyFalse("Row should not be selected");
    }

    /**
     * Check if row is selected.
     */
    public boolean isSelected() {
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        return checkbox.isSelected();
    }

    /**
     * Toggle selection.
     */
    public void toggleSelection() {
        log.info("Toggling selection for: {}", getProductName());
        
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        checkbox.click();
    }
}
```

### Dropdown/Select Actions

```java
public class COrderRow extends CWebTableRow<CDriver, COrderTable> {

    /**
     * Change order status via dropdown.
     */
    public void changeStatus(String newStatus) {
        log.info("Changing status to: {}", newStatus);
        
        CWebElement<CDriver> statusDropdown = getCell("Status", 0, "//select");
        statusDropdown.selectByVisibleText(newStatus);
    }

    /**
     * Set priority.
     */
    public void setPriority(String priority) {
        log.info("Setting priority to: {}", priority);
        
        CWebElement<CDriver> prioritySelect = getCell("Priority", 0, "//select");
        prioritySelect.selectByVisibleText(priority);
    }
}
```

### Input Field Actions

```java
public class CInventoryRow extends CWebTableRow<CDriver, CInventoryTable> {

    /**
     * Update quantity in row.
     */
    public void updateQuantity(int newQuantity) {
        log.info("Updating quantity to: {}", newQuantity);
        
        CWebElement<CDriver> quantityInput = getCell("Quantity", 0, "//input[@type='number']");
        quantityInput.clear();
        quantityInput.type(String.valueOf(newQuantity));
        
        // Blur to trigger update
        quantityInput.sendKeys(Keys.TAB);
    }

    /**
     * Update price.
     */
    public void updatePrice(double newPrice) {
        log.info("Updating price to: ${}", newPrice);
        
        CWebElement<CDriver> priceInput = getCell("Price", 0, "//input[@type='text']");
        priceInput.clear();
        priceInput.type(String.format("%.2f", newPrice));
        priceInput.sendKeys(Keys.TAB);
    }
}
```

---

## Advanced Usage Patterns

### Pattern 1: Row with Modal Dialogs

```java
@Slf4j
public class CUserRow extends CWebTableRow<CDriver, CUserTable> {

    public CUserRow(String name, CDriver driver, int idx, CUserTable parentTable) {
        super(name, driver, idx, parentTable);
    }

    public String getUsername() {
        return getCell("Username").getText();
    }

    /**
     * Delete user (handles confirmation modal).
     */
    public void deleteUser() {
        log.info("Deleting user: {}", getUsername());
        
        // Click delete button
        CWebElement<CDriver> deleteBtn = getCell("Actions", 0, "//button[@class='delete']");
        deleteBtn.click();
        
        // Wait for confirmation modal
        CWebElement<CDriver> modal = getDriver().findElementByClassName("confirmation-modal", 5);
        modal.Visible.waitUntilTrue(5);
        
        // Confirm deletion
        CWebElement<CDriver> confirmBtn = modal.findElement(By.id("confirm-delete"));
        confirmBtn.click();
        
        // Wait for modal to close
        modal.Visible.waitUntilFalse(10);
    }

    /**
     * Edit user (opens edit modal).
     */
    public void editUser(String newEmail) {
        log.info("Editing user: {}", getUsername());
        
        // Click edit button
        clickEditButton();
        
        // Wait for edit modal
        CWebElement<CDriver> modal = getDriver().findElementByClassName("edit-modal", 5);
        modal.Visible.waitUntilTrue(5);
        
        // Update email
        CWebElement<CDriver> emailInput = modal.findElement(By.id("email"));
        emailInput.clear();
        emailInput.type(newEmail);
        
        // Save
        CWebElement<CDriver> saveBtn = modal.findElement(By.id("save"));
        saveBtn.click();
        
        // Wait for modal to close
        modal.Visible.waitUntilFalse(10);
    }

    private void clickEditButton() {
        CWebElement<CDriver> editBtn = getCell("Actions", 0, "//button[@class='edit']");
        editBtn.click();
    }
}
```

### Pattern 2: Row with Inline Editing

```java
@Slf4j
public class CProductRow extends CWebTableRow<CDriver, CProductTable> {

    public CProductRow(String name, CDriver driver, int idx, CProductTable parentTable) {
        super(name, driver, idx, parentTable);
    }

    public String getProductName() {
        return getCell("Product Name").getText();
    }

    /**
     * Enable inline editing mode.
     */
    public void enableEditMode() {
        log.info("Enabling edit mode for: {}", getProductName());
        
        CWebElement<CDriver> editBtn = getCell("Actions", 0, "//button[@class='edit-inline']");
        editBtn.click();
        
        // Wait for input fields to appear
        CWebElement<CDriver> priceInput = getCell("Price", 0, "//input");
        priceInput.Visible.waitUntilTrue(5);
    }

    /**
     * Update price in inline edit mode.
     */
    public void updatePriceInline(double newPrice) {
        log.info("Updating price to: ${}", newPrice);
        
        enableEditMode();
        
        CWebElement<CDriver> priceInput = getCell("Price", 0, "//input");
        priceInput.clear();
        priceInput.type(String.format("%.2f", newPrice));
        
        saveInlineChanges();
    }

    /**
     * Save inline changes.
     */
    public void saveInlineChanges() {
        log.info("Saving changes");
        
        CWebElement<CDriver> saveBtn = getCell("Actions", 0, "//button[@class='save']");
        saveBtn.click();
        
        // Wait for save to complete
        CWebElement<CDriver> priceCell = getCell("Price");
        priceCell.Visible.waitUntilTrue(5);
    }

    /**
     * Cancel inline editing.
     */
    public void cancelInlineEdit() {
        log.info("Canceling inline edit");
        
        CWebElement<CDriver> cancelBtn = getCell("Actions", 0, "//button[@class='cancel']");
        cancelBtn.click();
    }
}
```

### Pattern 3: Row with Expandable Details

```java
@Slf4j
public class COrderRow extends CWebTableRow<CDriver, COrderTable> {

    public COrderRow(String name, CDriver driver, int idx, COrderTable parentTable) {
        super(name, driver, idx, parentTable);
    }

    public String getOrderNumber() {
        return getCell("Order #").getText();
    }

    /**
     * Expand row to show order details.
     */
    public void expandDetails() {
        log.info("Expanding details for order: {}", getOrderNumber());
        
        CWebElement<CDriver> expandBtn = getCell("", 0, "//button[@class='expand']");
        if (!isExpanded()) {
            expandBtn.click();
            
            // Wait for details row to appear
            String detailsRowXpath = getXpath() + "/following-sibling::tr[@class='details-row'][1]";
            CWebElement<CDriver> detailsRow = getDriver().findElementByXPath(detailsRowXpath, 5);
            detailsRow.Visible.waitUntilTrue(5);
        }
    }

    /**
     * Collapse row details.
     */
    public void collapseDetails() {
        log.info("Collapsing details for order: {}", getOrderNumber());
        
        CWebElement<CDriver> expandBtn = getCell("", 0, "//button[@class='expand']");
        if (isExpanded()) {
            expandBtn.click();
        }
    }

    /**
     * Check if row is expanded.
     */
    public boolean isExpanded() {
        CWebElement<CDriver> expandBtn = getCell("", 0, "//button[@class='expand']");
        return expandBtn.getAttribute("class").contains("expanded");
    }

    /**
     * Get order items from expanded details.
     */
    public CList<String> getOrderItems() {
        expandDetails();
        
        String detailsRowXpath = getXpath() + "/following-sibling::tr[@class='details-row'][1]";
        CWebElement<CDriver> detailsRow = getDriver().findElementByXPath(detailsRowXpath, 5);
        
        CList<CWebElement<CDriver>> items = detailsRow.findElements(By.className("order-item"));
        
        CList<String> itemNames = new CList<>();
        for (CWebElement<CDriver> item : items) {
            itemNames.add(item.getText());
        }
        
        return itemNames;
    }
}
```

### Pattern 4: Row with Status Indicators

```java
@Slf4j
public class CTaskRow extends CWebTableRow<CDriver, CTaskTable> {

    public CTaskRow(String name, CDriver driver, int idx, CTaskTable parentTable) {
        super(name, driver, idx, parentTable);
    }

    public String getTaskName() {
        return getCell("Task").getText();
    }

    /**
     * Get status badge text.
     */
    public String getStatus() {
        CWebElement<CDriver> statusCell = getCell("Status");
        CWebElement<CDriver> badge = statusCell.findElement(By.className("badge"));
        return badge.getText();
    }

    /**
     * Get status badge color.
     */
    public String getStatusColor() {
        CWebElement<CDriver> statusCell = getCell("Status");
        CWebElement<CDriver> badge = statusCell.findElement(By.className("badge"));
        return badge.Css("background-color").get();
    }

    /**
     * Check if task is completed.
     */
    public boolean isCompleted() {
        return "Completed".equals(getStatus());
    }

    /**
     * Check if task is in progress.
     */
    public boolean isInProgress() {
        return "In Progress".equals(getStatus());
    }

    /**
     * Check if task is pending.
     */
    public boolean isPending() {
        return "Pending".equals(getStatus());
    }

    /**
     * Get priority indicator.
     */
    public String getPriority() {
        CWebElement<CDriver> priorityCell = getCell("Priority");
        CWebElement<CDriver> indicator = priorityCell.findElement(By.className("priority-indicator"));
        return indicator.getAttribute("data-priority");
    }

    /**
     * Check if task is high priority.
     */
    public boolean isHighPriority() {
        return "high".equals(getPriority());
    }
}
```

---

## Best Practices

### 1. Use Business Language for Methods

```java
// ✅ CORRECT - Business language
public String getCustomerName() {
    return getCell("Customer").getText();
}

public void cancelOrder() {
    clickCancelButton();
}

public boolean isOrderComplete() {
    return "Completed".equals(getStatus());
}

// ❌ WRONG - Technical language
public String getCellText(int col) {
    return getCell(col).getText();
}

public void clickButton() {
    button.click();
}
```

### 2. Encapsulate Cell Access

```java
// ✅ CORRECT - Encapsulated cell access
public String getProductName() {
    return getCell("Product Name").getText();
}

public void clickEditButton() {
    CWebElement<CDriver> editBtn = getCell("Actions", 0, "//button[@class='edit']");
    editBtn.click();
}

// ❌ WRONG - Exposing cells publicly
public CWebElement<CDriver> nameCell;  // Don't expose cells!
```

### 3. Provide Data Transformation Methods

```java
// ✅ CORRECT - Transform data for business use
public double getPriceAsDouble() {
    String priceText = getCell("Price").getText();
    return Double.parseDouble(priceText.replace("$", "").replace(",", ""));
}

public LocalDate getOrderDateAsLocalDate() {
    String dateText = getCell("Date").getText();
    return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
}

// Instead of forcing tests to do transformation
// ❌ AVOID
String priceText = row.getPrice();
double price = Double.parseDouble(priceText.replace("$", ""));
```

### 4. Verify Element State Before Action

```java
// ✅ CORRECT - Verify before action
public void clickDeleteButton() {
    CWebElement<CDriver> deleteBtn = getCell("Actions", 0, "//button[@class='delete']");
    deleteBtn.Visible.verifyTrue("Delete button should be visible");
    deleteBtn.Enabled.verifyTrue("Delete button should be enabled");
    deleteBtn.click();
}

// ❌ RISKY - No verification
public void clickDeleteButton() {
    CWebElement<CDriver> deleteBtn = getCell("Actions", 0, "//button[@class='delete']");
    deleteBtn.click();  // May fail if not ready
}
```

### 5. Provide Boolean Check Methods

```java
// ✅ CORRECT - Convenient boolean methods
public boolean isInStock() {
    return "In Stock".equals(getStockStatus());
}

public boolean isActive() {
    return "Active".equals(getStatus());
}

public boolean isSelected() {
    CWebElement<CDriver> checkbox = getCell("Select", 0, "//input");
    return checkbox.isSelected();
}

// Tests can use easily
if (row.isInStock() && row.isActive()) {
    row.clickAddToCart();
}
```

### 6. Handle Null and Empty Values

```java
// ✅ CORRECT - Handle edge cases
public String getDescription() {
    CWebElement<CDriver> descCell = getCell("Description");
    String text = descCell.getText();
    return text != null ? text : "";
}

public double getDiscount() {
    String discountText = getCell("Discount").getText();
    if (discountText == null || discountText.isEmpty() || "-".equals(discountText)) {
        return 0.0;
    }
    return Double.parseDouble(discountText.replace("%", ""));
}
```

### 7. Use Descriptive Log Messages

```java
// ✅ CORRECT - Descriptive logging
public void clickEditButton() {
    log.info("Editing product: {} (SKU: {})", getProductName(), getSKU());
    
    CWebElement<CDriver> editBtn = getCell("Actions", 0, "//button[@class='edit']");
    editBtn.click();
}

// ❌ AVOID - Generic logging
public void clickEditButton() {
    log.info("Clicking button");
    button.click();
}
```

---

## Common Pitfalls

### Pitfall 1: Exposing Cells as Public Fields

```java
// ❌ WRONG - Public cell exposure
public class CBadRow extends CWebTableRow<CDriver, CTable> {
    public CWebElement<CDriver> nameCell;
    public CWebElement<CDriver> priceCell;
}

// ✅ CORRECT - Encapsulated access
public class CGoodRow extends CWebTableRow<CDriver, CTable> {
    public String getName() {
        return getCell("Name").getText();
    }
    
    public String getPrice() {
        return getCell("Price").getText();
    }
}
```

### Pitfall 2: Not Handling Missing Elements

```java
// ❌ RISKY - Assumes element exists
public void clickEditButton() {
    CWebElement<CDriver> editBtn = getCell("Actions", 0, "//button[@class='edit']");
    editBtn.click();  // May fail if button doesn't exist
}

// ✅ CORRECT - Check existence
public void clickEditButton() {
    CWebElement<CDriver> actionsCell = getCell("Actions");
    CWebElement<CDriver> editBtn = actionsCell.findElement(By.className("edit"));
    
    if (editBtn.isPresent() && editBtn.isVisible()) {
        editBtn.click();
    } else {
        log.warn("Edit button not available for row");
    }
}
```

### Pitfall 3: Using Column Indices Instead of Names

```java
// ❌ WRONG - Using column index
public String getProductName() {
    return getCell(0).getText();  // What is column 0?
}

// ✅ CORRECT - Use header names
public String getProductName() {
    return getCell("Product Name").getText();  // Clear and maintainable
}
```

### Pitfall 4: Complex Logic in Row Class

```java
// ❌ WRONG - Complex business logic in row
public class CBadRow extends CWebTableRow<CDriver, CTable> {
    public void processOrder() {
        // Complex multi-step workflow
        clickEditButton();
        updateStatus("Processing");
        clickSaveButton();
        sendEmailNotification();
        updateInventory();
        // This belongs in page or service layer!
    }
}

// ✅ CORRECT - Simple row interactions
public class CGoodRow extends CWebTableRow<CDriver, CTable> {
    public void clickEditButton() {
        // Simple interaction
    }
    
    public void updateStatus(String status) {
        // Simple interaction
    }
}
```

### Pitfall 5: Not Using Extension Framework

```java
// ❌ WRONG - Extract then assert
public void verifyProductName(String expectedName) {
    String actualName = getCell("Name").getText();
    assertThat(actualName).isEqualTo(expectedName);
}

// ✅ CORRECT - Use extension framework
public void verifyProductName(String expectedName) {
    getCell("Name").Text.verifyEquals(expectedName, "Product name should match");
}
```

### Pitfall 6: Ignoring Row Index

```java
// ❌ WRONG - Hardcoded row identification
public void clickButton() {
    getDriver().findElementByXPath("//table//tr[5]//button", 5).click();
}

// ✅ CORRECT - Use row's own XPath
public void clickButton() {
    CWebElement<CDriver> button = getCell("Actions", 0, "//button");
    button.click();
}
```

---

## Examples

### Example 1: Complete Product Row

```java
package org.catools.web.tables;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTableRow;
import org.openqa.selenium.By;

/**
 * Product table row with data extraction and user interactions.
 */
@Slf4j
@Getter
public class CProductRow extends CWebTableRow<CDriver, CProductTable> {

    public CProductRow(String name, CDriver driver, int idx, CProductTable parentTable) {
        super(name, driver, idx, parentTable, DEFAULT_TIMEOUT);
        log.debug("Product row {} initialized", idx);
    }

    // Data extraction methods

    /**
     * Get product name.
     */
    public String getProductName() {
        return getCell("Product Name").getText();
    }

    /**
     * Get product SKU.
     */
    public String getSKU() {
        return getCell("SKU").getText();
    }

    /**
     * Get product category.
     */
    public String getCategory() {
        return getCell("Category").getText();
    }

    /**
     * Get product price.
     */
    public String getPrice() {
        return getCell("Price").getText();
    }

    /**
     * Get price as double.
     */
    public double getPriceAsDouble() {
        String priceText = getPrice().replace("$", "").replace(",", "");
        return Double.parseDouble(priceText);
    }

    /**
     * Get stock quantity.
     */
    public int getStockQuantity() {
        String stockText = getCell("Stock").getText();
        return Integer.parseInt(stockText);
    }

    /**
     * Get product rating.
     */
    public double getRating() {
        String ratingText = getCell("Rating").getText();
        return Double.parseDouble(ratingText);
    }

    /**
     * Check if product is in stock.
     */
    public boolean isInStock() {
        return getStockQuantity() > 0;
    }

    /**
     * Check if product is featured.
     */
    public boolean isFeatured() {
        CWebElement<CDriver> nameCell = getCell("Product Name");
        return nameCell.getAttribute("class").contains("featured");
    }

    /**
     * Check if product is on sale.
     */
    public boolean isOnSale() {
        CWebElement<CDriver> priceCell = getCell("Price");
        CWebElement<CDriver> saleTag = priceCell.findElement(By.className("sale-tag"));
        return saleTag.isPresent() && saleTag.isVisible();
    }

    // User interaction methods

    /**
     * Click "Add to Cart" button.
     */
    public void clickAddToCartButton() {
        log.info("Adding to cart: {}", getProductName());
        
        CWebElement<CDriver> addToCartBtn = getCell("Actions", 0, "//button[@class='add-to-cart']");
        addToCartBtn.Clickable.verifyTrue("Add to cart button should be clickable");
        addToCartBtn.click();
    }

    /**
     * Click product name to view details.
     */
    public void clickProductNameLink() {
        log.info("Viewing details: {}", getProductName());
        
        CWebElement<CDriver> nameLink = getCell("Product Name", 0, "//a");
        nameLink.Clickable.verifyTrue("Product name link should be clickable");
        nameLink.click();
    }

    /**
     * Click "Quick View" button.
     */
    public void clickQuickViewButton() {
        log.info("Opening quick view: {}", getProductName());
        
        CWebElement<CDriver> quickViewBtn = getCell("Actions", 0, "//button[@class='quick-view']");
        quickViewBtn.click();
    }

    /**
     * Add to wishlist.
     */
    public void addToWishlist() {
        log.info("Adding to wishlist: {}", getProductName());
        
        CWebElement<CDriver> wishlistBtn = getCell("Actions", 0, "//button[@class='wishlist']");
        wishlistBtn.click();
    }

    /**
     * Select product (checkbox).
     */
    public void selectProduct() {
        log.info("Selecting product: {}", getProductName());
        
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        checkbox.Selected.verifyTrue("Product should be selected");
    }

    /**
     * Deselect product.
     */
    public void deselectProduct() {
        log.info("Deselecting product: {}", getProductName());
        
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        if (checkbox.isSelected()) {
            checkbox.click();
        }
        checkbox.Selected.verifyFalse("Product should not be selected");
    }

    /**
     * Check if product is selected.
     */
    public boolean isProductSelected() {
        CWebElement<CDriver> checkbox = getCell("Select", 0, "//input[@type='checkbox']");
        return checkbox.isSelected();
    }

    /**
     * Get product image URL.
     */
    public String getImageUrl() {
        CWebElement<CDriver> imageCell = getCell("Image");
        CWebElement<CDriver> image = imageCell.findElement(By.tagName("img"));
        return image.getAttribute("src");
    }

    /**
     * Verify product data.
     */
    public void verifyProductData(String expectedName, String expectedCategory, double expectedPrice) {
        getCell("Product Name").Text.verifyEquals(expectedName, "Product name should match");
        getCell("Category").Text.verifyEquals(expectedCategory, "Category should match");
        
        double actualPrice = getPriceAsDouble();
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }
}
```

### Example 2: Complete Order Row

```java
package org.catools.web.tables;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CSeleniumEngine;
import org.catools.web.table.CWebTableRow;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Order table row with comprehensive order operations.
 */
@Slf4j
@Getter
public class COrderRow extends CWebTableRow<CDriver, COrderTable> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public COrderRow(String name, CDriver driver, int idx, COrderTable parentTable) {
        super(name, driver, idx, parentTable, DEFAULT_TIMEOUT);
        log.debug("Order row {} initialized", idx);
    }

    // Data extraction

    /**
     * Get order number.
     */
    public String getOrderNumber() {
        return getCell("Order #").getText();
    }

    /**
     * Get customer name.
     */
    public String getCustomer() {
        return getCell("Customer").getText();
    }

    /**
     * Get order date.
     */
    public String getOrderDate() {
        return getCell("Date").getText();
    }

    /**
     * Get order date as LocalDate.
     */
    public LocalDate getOrderDateAsLocalDate() {
        String dateText = getOrderDate();
        return LocalDate.parse(dateText, DATE_FORMATTER);
    }

    /**
     * Get order status.
     */
    public String getStatus() {
        return getCell("Status").getText();
    }

    /**
     * Get total amount.
     */
    public String getTotalAmount() {
        return getCell("Total").getText();
    }

    /**
     * Get total as double.
     */
    public double getTotalAsDouble() {
        String totalText = getTotalAmount().replace("$", "").replace(",", "");
        return Double.parseDouble(totalText);
    }

    /**
     * Get payment method.
     */
    public String getPaymentMethod() {
        return getCell("Payment").getText();
    }

    /**
     * Get shipping status.
     */
    public String getShippingStatus() {
        CWebElement<CDriver> statusCell = getCell("Shipping");
        return statusCell.getText();
    }

    // Status checks

    /**
     * Check if order is pending.
     */
    public boolean isPending() {
        return "Pending".equals(getStatus());
    }

    /**
     * Check if order is processing.
     */
    public boolean isProcessing() {
        return "Processing".equals(getStatus());
    }

    /**
     * Check if order is completed.
     */
    public boolean isCompleted() {
        return "Completed".equals(getStatus());
    }

    /**
     * Check if order is cancelled.
     */
    public boolean isCancelled() {
        return "Cancelled".equals(getStatus());
    }

    /**
     * Check if order is within date range.
     */
    public boolean isOrderDateBetween(LocalDate startDate, LocalDate endDate) {
        LocalDate orderDate = getOrderDateAsLocalDate();
        return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
    }

    /**
     * Check if order is recent (within N days).
     */
    public boolean isRecent(int days) {
        LocalDate orderDate = getOrderDateAsLocalDate();
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return orderDate.isAfter(cutoffDate);
    }

    // User interactions

    /**
     * View order details.
     */
    public void clickViewDetailsLink() {
        log.info("Viewing details for order: {}", getOrderNumber());
        
        CWebElement<CDriver> viewLink = getCell("Actions", 0, "//a[text()='View']");
        viewLink.Clickable.verifyTrue("View link should be clickable");
        viewLink.click();
    }

    /**
     * Cancel order.
     */
    public void clickCancelButton() {
        log.info("Canceling order: {}", getOrderNumber());
        
        CWebElement<CDriver> cancelBtn = getCell("Actions", 0, "//button[text()='Cancel']");
        cancelBtn.Clickable.verifyTrue("Cancel button should be clickable");
        cancelBtn.click();
    }

    /**
     * Track shipment.
     */
    public void clickTrackShipmentLink() {
        log.info("Tracking shipment for order: {}", getOrderNumber());
        
        CWebElement<CDriver> trackLink = getCell("Actions", 0, "//a[@class='track-shipment']");
        trackLink.click();
    }

    /**
     * Download invoice.
     */
    public void downloadInvoice() {
        log.info("Downloading invoice for order: {}", getOrderNumber());
        
        CWebElement<CDriver> downloadLink = getCell("Actions", 0, "//a[@class='download-invoice']");
        downloadLink.click();
    }

    /**
     * Reorder.
     */
    public void clickReorderButton() {
        log.info("Reordering: {}", getOrderNumber());
        
        CWebElement<CDriver> reorderBtn = getCell("Actions", 0, "//button[text()='Reorder']");
        reorderBtn.click();
    }

    /**
     * Print order.
     */
    public void printOrder() {
        log.info("Printing order: {}", getOrderNumber());
        
        CWebElement<CDriver> printBtn = getCell("Actions", 0, "//button[@class='print']");
        printBtn.click();
    }

    /**
     * Get status badge CSS class.
     */
    public String getStatusBadgeClass() {
        CWebElement<CDriver> statusCell = getCell("Status");
        CWebElement<CDriver> badge = statusCell.findElement(By.className("badge"));
        return badge.getAttribute("class");
    }

    /**
     * Verify order details.
     */
    public void verifyOrderDetails(String expectedCustomer, String expectedStatus, double expectedTotal) {
        getCell("Customer").Text.verifyEquals(expectedCustomer, "Customer should match");
        getCell("Status").Text.verifyEquals(expectedStatus, "Status should match");
        
        double actualTotal = getTotalAsDouble();
        assertThat(actualTotal).isEqualTo(expectedTotal);
    }
}
```

---

## Summary

`CWebTableRow` provides focused row-level interaction capabilities with:

- **Cell Access by Column Name** - Use header names for maintainability
- **Data Extraction Methods** - Transform data for business use
- **User Interaction Methods** - Click, select, edit within row
- **Nested Element Support** - Access buttons, links, inputs within cells
- **Extension Framework** - All elements support verification, wait, and state methods
- **Type Safety** - Generic typing for driver and parent table

**Critical Rules:**
1. **Use business language** - Methods describe domain operations
2. **Encapsulate cell access** - Don't expose cells as public fields
3. **Provide data transformation** - Convert strings to appropriate types
4. **Verify before action** - Check element state before interaction
5. **Handle null/missing elements** - Defensive programming
6. **Use extension framework** - Never extract-then-assert
7. **Keep it simple** - Complex business logic belongs in CWebPage

**For More Information:**

- See `webtable-guide.prompts.md` for CWebTable (parent table)
- See `webpage-guide.prompts.md` for CWebPage (contains tables)
- See `webform-guide.prompts.md` for CWebForm (user interactions in forms)
- See `webelement-classes-guide.prompts.md` for CWebElement properties

**Version History:**

- **2.0.0** (Nov 28, 2025) - Initial comprehensive guide


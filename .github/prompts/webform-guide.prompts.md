# CWebForm Classes Guide

**Version:** 2.0.0  
**Last Updated:** November 28, 2025  
**Framework:** CATools Web Automation  
**Java Version:** 21

---

## Table of Contents

1. [Introduction](#introduction)
2. [CWebForm Overview](#cwebform-overview)
3. [Creating CWebForm Implementations](#creating-cwebform-implementations)
4. [Element Initialization with Annotations](#element-initialization-with-annotations)
5. [CWebForm vs CWebElement](#cwebform-vs-cwebelement)
6. [Advanced Usage Patterns](#advanced-usage-patterns)
7. [Best Practices](#best-practices)
8. [Common Pitfalls](#common-pitfalls)
9. [Examples](#examples)

---

## Introduction

`CWebForm` is the base class for implementing form-based page objects in CATools web automation framework. It provides automatic element initialization, structured form management, and seamless integration with the CATools extension framework.

**This guide complements the MCP automation patterns documented in `catools-web-mcp.agents.md`.**

### Key Features

- **Automatic Element Initialization** - Elements are initialized automatically via `CWebElementFactory`
- **Annotation-Based Locators** - Use `@CFindBy`, `@CFindBys`, or Selenium's `@FindBy` for element declaration
- **Type Safety** - Generic typing `<DR extends CDriver>` for driver instance
- **Encapsulation** - Centralizes all form-related locators in a single class
- **Reusability** - Form objects can be reused across multiple tests
- **Extension Framework Integration** - All initialized elements support extension properties
- **Immutable Driver** - Thread-safe driver reference

### When to Use CWebForm

**Use CWebForm for:**
- ✅ Login forms, registration forms, search forms
- ✅ Contact forms, checkout forms, profile update forms
- ✅ Any page component with multiple input fields
- ✅ Grouped elements that represent a logical form unit
- ✅ Elements that are NOT part of tables or rows
- ✅ Navigation menus
- ✅ Read-only content sections

**Don't Use CWebForm for:**
- ❌ Table rows and table data (use table-specific classes)
- ❌ Single standalone elements (use `CWebElement` directly)

---

## CWebForm Overview

### Class Signature

```java
public abstract class CWebForm<DR extends CDriver> implements CWebComponent<DR> {}
```

### Type Parameters

- `<DR>` - The driver type extending `CDriver`

### Implemented Interfaces

- `CWebComponent<DR>` - Provides driver access and component contract

### Core Components

1. **Driver Reference** - Immutable `DR driver` field for browser automation
2. **Element Factory** - Automatic initialization via `CWebElementFactory.initElements(this)`
3. **Annotation Support** - `@CFindBy`, `@CFindBys`, `@FindBy` for element declaration
4. **Extension Framework** - All initialized elements support verification, wait, and state methods

### Lifecycle

```java
// 1. Constructor is called with driver
public MyForm(CDriver driver) {
    super(driver);  // 2. Driver is stored
                    // 3. CWebElementFactory.initElements(this) is called automatically
                    // 4. All @CFindBy/@FindBy fields are initialized
}

// 5. Form is ready to use immediately
form.fillUsername("testuser");
form.submit();
```

---

## Creating CWebForm Implementations

### Method 1: Basic Form with @CFindBy

```java
package org.catools.web.forms;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.annotation.CFindBy;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;

/**
 * Login form implementation for example.com authentication.
 */
@Slf4j
public class CLoginForm extends CWebForm<CDriver> {

    @CFindBy(id = "username", name = "Username Field", waitInSeconds = 10)
    private CWebElement<CDriver> usernameField;

    @CFindBy(id = "password", name = "Password Field", waitInSeconds = 10)
    private CWebElement<CDriver> passwordField;

    @CFindBy(css = "button[type='submit']", name = "Submit Button", waitInSeconds = 5)
    private CWebElement<CDriver> submitButton;

    @CFindBy(id = "error-message", name = "Error Message", waitInSeconds = 5)
    private CWebElement<CDriver> errorMessage;

    /**
     * Constructs login form with driver instance.
     * Elements are automatically initialized after super() call.
     */
    public CLoginForm(CDriver driver) {
        super(driver);
        log.info("Login form initialized");
    }

    /**
     * Perform login with username and password.
     */
    public void login(String username, String password) {
        log.info("Logging in as: {}", username);
        
        usernameField.Visible.verifyTrue("Username field should be visible");
        usernameField.type(username);
        
        passwordField.Visible.verifyTrue("Password field should be visible");
        passwordField.type(password);
        
        submitButton.Clickable.verifyTrue("Submit button should be clickable");
        submitButton.click();
    }

    /**
     * Check if error message is displayed.
     */
    public boolean hasError() {
        return errorMessage.isPresent() && errorMessage.isVisible();
    }

    /**
     * Get error message text.
     */
    public String getErrorMessage() {
        if (hasError()) {
            return errorMessage.getText();
        }
        return "";
    }
}
```

### Method 2: Form with Selenium @FindBy

```java
package org.catools.web.forms;

import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.openqa.selenium.support.FindBy;

/**
 * Registration form using Selenium @FindBy annotation.
 */
public class CRegistrationForm extends CWebForm<CDriver> {

    @FindBy(name = "email")
    private CWebElement<CDriver> emailField;

    @FindBy(name = "confirmEmail")
    private CWebElement<CDriver> confirmEmailField;

    @FindBy(name = "firstName")
    private CWebElement<CDriver> firstNameField;

    @FindBy(name = "lastName")
    private CWebElement<CDriver> lastNameField;

    @FindBy(css = "input[type='checkbox'][name='terms']")
    private CWebElement<CDriver> termsCheckbox;

    @FindBy(id = "register-button")
    private CWebElement<CDriver> registerButton;

    public CRegistrationForm(CDriver driver) {
        super(driver);
    }

    /**
     * Fill registration form with user data.
     */
    public void fillForm(String email, String firstName, String lastName) {
        firstNameField.type(firstName);
        lastNameField.type(lastName);
        emailField.type(email);
        confirmEmailField.type(email);
    }

    /**
     * Accept terms and conditions.
     */
    public void acceptTerms() {
        if (!termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }
        termsCheckbox.Selected.verifyTrue("Terms checkbox should be selected");
    }

    /**
     * Submit registration form.
     */
    public void submit() {
        registerButton.Clickable.verifyTrue("Register button should be clickable");
        registerButton.click();
    }
}
```

### Method 3: Form with Multiple Elements (@CFindBys)

```java
package org.catools.web.forms;

import org.catools.web.annotation.CFindBy;
import org.catools.web.annotation.CFindBys;
import org.catools.web.collections.CWebElements;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;

/**
 * Product search form with filters.
 */
public class CProductSearchForm extends CWebForm<CDriver> {

    @CFindBy(id = "search-query", name = "Search Input")
    private CWebElement<CDriver> searchInput;

    @CFindBy(id = "search-button", name = "Search Button")
    private CWebElement<CDriver> searchButton;

    @CFindBys(
        xpath = "//input[@type='checkbox'][@name='category']",
        name = "Category Checkboxes",
        waitForFirstElementInSecond = 5,
        waitForOtherElementInSecond = 2
    )
    private CWebElements<CDriver> categoryCheckboxes;

    @CFindBy(id = "price-min", name = "Min Price Input")
    private CWebElement<CDriver> minPriceInput;

    @CFindBy(id = "price-max", name = "Max Price Input")
    private CWebElement<CDriver> maxPriceInput;

    @CFindBy(id = "apply-filters", name = "Apply Filters Button")
    private CWebElement<CDriver> applyFiltersButton;

    public CProductSearchForm(CDriver driver) {
        super(driver);
    }

    /**
     * Search for products by keyword.
     */
    public void search(String query) {
        searchInput.Visible.verifyTrue("Search input should be visible");
        searchInput.type(query);
        searchButton.click();
    }

    /**
     * Select category filter by index.
     */
    public void selectCategory(int index) {
        if (index < categoryCheckboxes.size()) {
            CWebElement<CDriver> checkbox = categoryCheckboxes.get(index);
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
    }

    /**
     * Set price range filter.
     */
    public void setPriceRange(String min, String max) {
        minPriceInput.type(min);
        maxPriceInput.type(max);
    }

    /**
     * Apply all selected filters.
     */
    public void applyFilters() {
        applyFiltersButton.click();
    }
}
```

---

## Element Initialization with Annotations

### @CFindBy Annotation (CATools Custom)

Most powerful and recommended annotation for CWebForm elements.

**Attributes:**

| Attribute | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `id` | String | No | "" | Element ID attribute |
| `name` | String | No | "" | Element name (descriptive) or name attribute |
| `css` | String | No | "" | CSS selector |
| `xpath` | String | No | "" | XPath expression |
| `className` | String | No | "" | CSS class name |
| `linkText` | String | No | "" | Link text |
| `partialLinkText` | String | No | "" | Partial link text |
| `tagName` | String | No | "" | HTML tag name |
| `waitInSeconds` | int | No | 10 | Wait timeout in seconds |

**Usage Examples:**

```java
// By ID (preferred)
@CFindBy(id = "email", name = "Email Input", waitInSeconds = 10)
private CWebElement<CDriver> emailField;

// By name attribute
@CFindBy(name = "username", waitInSeconds = 5)
private CWebElement<CDriver> usernameField;

// By CSS selector
@CFindBy(css = "button.btn-primary", name = "Submit Button")
private CWebElement<CDriver> submitButton;

// By XPath
@CFindBy(xpath = "//input[@data-test='password']", name = "Password Field")
private CWebElement<CDriver> passwordField;

// By class name
@CFindBy(className = "error-message", name = "Error Message", waitInSeconds = 5)
private CWebElement<CDriver> errorMsg;

// By link text
@CFindBy(linkText = "Forgot Password?", name = "Forgot Password Link")
private CWebElement<CDriver> forgotPasswordLink;
```

### @CFindBys Annotation (CATools Custom - for Collections)

Used for multiple elements of the same type.

**Attributes:**

| Attribute | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `xpath` | String | Yes | - | XPath expression for elements |
| `name` | String | No | "" | Descriptive name for collection |
| `waitForFirstElementInSecond` | int | No | 10 | Timeout for first element |
| `waitForOtherElementInSecond` | int | No | 2 | Timeout for subsequent elements |

**Usage Examples:**

```java
// Collection of checkboxes
@CFindBys(
    xpath = "//input[@type='checkbox']",
    name = "All Checkboxes",
    waitForFirstElementInSecond = 5,
    waitForOtherElementInSecond = 2
)
private CWebElements<CDriver> checkboxes;

// Collection of buttons
@CFindBys(
    xpath = "//button[contains(@class, 'action-btn')]",
    name = "Action Buttons"
)
private CWebElements<CDriver> actionButtons;

// Collection of form fields
@CFindBys(
    xpath = "//form[@id='contact']//input",
    name = "Contact Form Inputs",
    waitForFirstElementInSecond = 10
)
private CWebElements<CDriver> formInputs;
```

### @FindBy Annotation (Selenium Standard)

Standard Selenium annotation - simpler but less powerful.

**Usage Examples:**

```java
// By ID
@FindBy(id = "submit")
private CWebElement<CDriver> submitButton;

// By name
@FindBy(name = "email")
private CWebElement<CDriver> emailInput;

// By CSS
@FindBy(css = ".btn-primary")
private CWebElement<CDriver> primaryButton;

// By XPath
@FindBy(xpath = "//input[@type='password']")
private CWebElement<CDriver> passwordField;

// By class name
@FindBy(className = "error-msg")
private CWebElement<CDriver> errorMessage;

// By link text
@FindBy(linkText = "Sign Up")
private CWebElement<CDriver> signUpLink;

// By partial link text
@FindBy(partialLinkText = "Forgot")
private CWebElement<CDriver> forgotLink;

// By tag name
@FindBy(tagName = "button")
private CWebElement<CDriver> button;
```

### Annotation Priority and Best Practices

**Recommendation:**
1. ✅ **Use @CFindBy** - Most powerful, includes wait timeout and descriptive name
2. ⚠️ **Use @FindBy** - For simple cases when timeout customization not needed
3. ✅ **Use @CFindBys** - For element collections

**Locator Strategy Priority:**
1. **ID** - Fastest and most stable: `@CFindBy(id = "element-id")`
2. **Name** - Fast and stable: `@CFindBy(name = "element-name")`
3. **CSS** - Flexible and performant: `@CFindBy(css = ".class-name")`
4. **XPath** - Use when necessary: `@CFindBy(xpath = "//div[@id='parent']//input")`

---

## CWebForm vs CWebElement

### When to Use CWebForm

```java
// ✅ CORRECT - Use CWebForm for grouped elements representing a form
public class CLoginForm extends CWebForm<CDriver> {
    @CFindBy(id = "username")
    private CWebElement<CDriver> usernameField;
    
    @CFindBy(id = "password")
    private CWebElement<CDriver> passwordField;
    
    @CFindBy(id = "submit")
    private CWebElement<CDriver> submitButton;
    
    public void login(String user, String pass) {
        usernameField.type(user);
        passwordField.type(pass);
        submitButton.click();
    }
}

// Usage in test
CLoginForm loginForm = new CLoginForm(getDriver());
loginForm.login("testuser", "password123");
```

### When to Use CWebElement Directly

```java
// ✅ CORRECT - Use CWebElement for standalone elements
CWebElement<CDriver> searchButton = getDriver().findElementById("search", 10);
searchButton.Clickable.verifyTrue("Search button should be clickable");
searchButton.click();

// ✅ CORRECT - Use CWebElement for dynamic elements
CWebElement<CDriver> dynamicElement = getDriver().findElementByXPath(
    "//div[@data-id='" + dynamicId + "']", 10
);
dynamicElement.Visible.verifyTrue("Dynamic element should be visible");
```

### Comparison Table

| Aspect | CWebForm | CWebElement |
|--------|----------|-------------|
| **Use Case** | Grouped form elements | Single standalone elements |
| **Initialization** | Automatic via factory | Manual via driver.findElement* |
| **Reusability** | High - reuse across tests | Medium - find each time |
| **Encapsulation** | Excellent - all locators in one place | N/A |
| **Type Safety** | Generic `<DR extends CDriver>` | Generic `<DR extends CDriver>` |
| **Extension Properties** | Yes (all initialized elements) | Yes |
| **Dynamic Locators** | Limited (static annotations) | Excellent (runtime construction) |
| **Best For** | Login, registration, search forms | Buttons, links, dynamic content |

---

## Advanced Usage Patterns

### Pattern 1: Form with Validation Methods

```java
public class CContactForm extends CWebForm<CDriver> {

    @CFindBy(id = "name", name = "Name Field")
    private CWebElement<CDriver> nameField;

    @CFindBy(id = "email", name = "Email Field")
    private CWebElement<CDriver> emailField;

    @CFindBy(id = "message", name = "Message Field")
    private CWebElement<CDriver> messageField;

    @CFindBy(id = "name-error", name = "Name Error")
    private CWebElement<CDriver> nameError;

    @CFindBy(id = "email-error", name = "Email Error")
    private CWebElement<CDriver> emailError;

    public CContactForm(CDriver driver) {
        super(driver);
    }

    /**
     * Fill contact form with data.
     */
    public void fillForm(String name, String email, String message) {
        nameField.type(name);
        emailField.type(email);
        messageField.type(message);
    }

    /**
     * Verify form validation errors.
     */
    public void verifyValidationErrors() {
        nameError.Visible.verifyTrue("Name error should be visible");
        emailError.Visible.verifyTrue("Email error should be visible");
    }

    /**
     * Check if form is valid (no errors visible).
     */
    public boolean isValid() {
        return !nameError.isVisible() && !emailError.isVisible();
    }

    /**
     * Get all validation error messages.
     */
    public String getAllErrors() {
        StringBuilder errors = new StringBuilder();
        if (nameError.isVisible()) {
            errors.append("Name: ").append(nameError.getText()).append("; ");
        }
        if (emailError.isVisible()) {
            errors.append("Email: ").append(emailError.getText()).append("; ");
        }
        return errors.toString();
    }
}
```

### Pattern 2: Form with Fluent API

```java
public class CCheckoutForm extends CWebForm<CDriver> {

    @CFindBy(id = "billing-name")
    private CWebElement<CDriver> billingName;

    @CFindBy(id = "billing-address")
    private CWebElement<CDriver> billingAddress;

    @CFindBy(id = "card-number")
    private CWebElement<CDriver> cardNumber;

    @CFindBy(id = "cvv")
    private CWebElement<CDriver> cvv;

    @CFindBy(id = "place-order")
    private CWebElement<CDriver> placeOrderButton;

    public CCheckoutForm(CDriver driver) {
        super(driver);
    }

    /**
     * Set billing name (fluent).
     */
    public CCheckoutForm withBillingName(String name) {
        billingName.type(name);
        return this;
    }

    /**
     * Set billing address (fluent).
     */
    public CCheckoutForm withBillingAddress(String address) {
        billingAddress.type(address);
        return this;
    }

    /**
     * Set card number (fluent).
     */
    public CCheckoutForm withCardNumber(String card) {
        cardNumber.type(card);
        return this;
    }

    /**
     * Set CVV (fluent).
     */
    public CCheckoutForm withCVV(String cvvCode) {
        cvv.type(cvvCode);
        return this;
    }

    /**
     * Submit checkout form.
     */
    public void submit() {
        placeOrderButton.Clickable.verifyTrue("Place order button should be clickable");
        placeOrderButton.click();
    }
}

// Usage with fluent API
CCheckoutForm checkout = new CCheckoutForm(getDriver());
checkout
    .withBillingName("John Doe")
    .withBillingAddress("123 Main St")
    .withCardNumber("4111111111111111")
    .withCVV("123")
    .submit();
```

### Pattern 3: Form with Conditional Logic

```java
public class CAdvancedSearchForm extends CWebForm<CDriver> {

    @CFindBy(id = "search-query")
    private CWebElement<CDriver> searchQuery;

    @CFindBy(id = "advanced-options-toggle")
    private CWebElement<CDriver> advancedToggle;

    @CFindBy(id = "date-from")
    private CWebElement<CDriver> dateFrom;

    @CFindBy(id = "date-to")
    private CWebElement<CDriver> dateTo;

    @CFindBy(id = "category-select")
    private CWebElement<CDriver> categorySelect;

    public CAdvancedSearchForm(CDriver driver) {
        super(driver);
    }

    /**
     * Perform basic search.
     */
    public void basicSearch(String query) {
        searchQuery.type(query);
        searchQuery.sendKeys(Keys.ENTER);
    }

    /**
     * Perform advanced search with optional filters.
     */
    public void advancedSearch(String query, String from, String to, String category) {
        // Expand advanced options if collapsed
        if (!dateFrom.isVisible()) {
            advancedToggle.click();
            dateFrom.Visible.waitUntilTrue(5);
        }

        searchQuery.type(query);

        if (from != null && !from.isEmpty()) {
            dateFrom.type(from);
        }

        if (to != null && !to.isEmpty()) {
            dateTo.type(to);
        }

        if (category != null && !category.isEmpty()) {
            categorySelect.selectByVisibleText(category);
        }

        searchQuery.sendKeys(Keys.ENTER);
    }
}
```

### Pattern 4: Form with Multiple Sections

```java
public class CUserProfileForm extends CWebForm<CDriver> {

    // Personal Info Section
    @CFindBy(id = "first-name", name = "First Name")
    private CWebElement<CDriver> firstName;

    @CFindBy(id = "last-name", name = "Last Name")
    private CWebElement<CDriver> lastName;

    @CFindBy(id = "dob", name = "Date of Birth")
    private CWebElement<CDriver> dateOfBirth;

    // Contact Info Section
    @CFindBy(id = "email", name = "Email")
    private CWebElement<CDriver> email;

    @CFindBy(id = "phone", name = "Phone")
    private CWebElement<CDriver> phone;

    // Address Section
    @CFindBy(id = "street", name = "Street")
    private CWebElement<CDriver> street;

    @CFindBy(id = "city", name = "City")
    private CWebElement<CDriver> city;

    @CFindBy(id = "state", name = "State")
    private CWebElement<CDriver> state;

    @CFindBy(id = "zip", name = "ZIP Code")
    private CWebElement<CDriver> zip;

    // Actions
    @CFindBy(id = "save-button", name = "Save Button")
    private CWebElement<CDriver> saveButton;

    public CUserProfileForm(CDriver driver) {
        super(driver);
    }

    /**
     * Fill personal information section.
     */
    public void fillPersonalInfo(String first, String last, String dob) {
        firstName.type(first);
        lastName.type(last);
        dateOfBirth.type(dob);
    }

    /**
     * Fill contact information section.
     */
    public void fillContactInfo(String emailAddr, String phoneNum) {
        email.type(emailAddr);
        phone.type(phoneNum);
    }

    /**
     * Fill address section.
     */
    public void fillAddress(String streetAddr, String cityName, String stateName, String zipCode) {
        street.type(streetAddr);
        city.type(cityName);
        state.type(stateName);
        zip.type(zipCode);
    }

    /**
     * Save profile changes.
     */
    public void save() {
        saveButton.Clickable.verifyTrue("Save button should be clickable");
        saveButton.click();
    }

    /**
     * Fill complete profile form.
     */
    public void fillCompleteProfile(
        String first, String last, String dob,
        String emailAddr, String phoneNum,
        String streetAddr, String cityName, String stateName, String zipCode
    ) {
        fillPersonalInfo(first, last, dob);
        fillContactInfo(emailAddr, phoneNum);
        fillAddress(streetAddr, cityName, stateName, zipCode);
    }
}
```

---

## Best Practices

### 1. Use Descriptive Names

```java
// ✅ CORRECT - Descriptive form name and element names
public class CProductReviewForm extends CWebForm<CDriver> {
    @CFindBy(id = "rating", name = "Product Rating Stars")
    private CWebElement<CDriver> ratingStars;
    
    @CFindBy(id = "review-title", name = "Review Title Input")
    private CWebElement<CDriver> reviewTitle;
    
    @CFindBy(id = "review-text", name = "Review Text Area")
    private CWebElement<CDriver> reviewText;
}

// ❌ AVOID - Generic names
public class Form1 extends CWebForm<CDriver> {
    @CFindBy(id = "rating")
    private CWebElement<CDriver> el1;
    
    @CFindBy(id = "review-title")
    private CWebElement<CDriver> el2;
}
```

### 2. Prefer ID Locators

```java
// ✅ CORRECT - Use ID (fastest and most stable)
@CFindBy(id = "username", name = "Username Field")
private CWebElement<CDriver> usernameField;

// ⚠️ ACCEPTABLE - Use CSS when ID not available
@CFindBy(css = "input[name='username']", name = "Username Field")
private CWebElement<CDriver> usernameField;

// ❌ AVOID - Fragile XPath
@CFindBy(xpath = "//div[1]/div[2]/form/input[1]", name = "Username Field")
private CWebElement<CDriver> usernameField;
```

### 3. Set Appropriate Wait Times

```java
// ✅ CORRECT - Different timeouts based on expected behavior
@CFindBy(id = "logo", name = "Site Logo", waitInSeconds = 5)
private CWebElement<CDriver> logo;  // Fast loading element

@CFindBy(id = "async-data", name = "Async Data Section", waitInSeconds = 30)
private CWebElement<CDriver> asyncData;  // Slow loading element

// ❌ AVOID - Same long timeout for everything
@CFindBy(id = "logo", name = "Site Logo", waitInSeconds = 60)
private CWebElement<CDriver> logo;  // Unnecessarily long
```

### 4. Use Extension Framework for Verification

```java
// ✅ CORRECT - Use extension framework verification
public void submitForm() {
    submitButton.Visible.verifyTrue("Submit button should be visible");
    submitButton.Enabled.verifyTrue("Submit button should be enabled");
    submitButton.click();
}

// ❌ WRONG - Extract then assert
public void submitForm() {
    boolean isVisible = submitButton.isVisible();
    assertThat(isVisible).isTrue();  // Don't do this!
    submitButton.click();
}
```

### 5. Encapsulate Form Logic

```java
// ✅ CORRECT - Encapsulate all form interactions
public class CLoginForm extends CWebForm<CDriver> {
    // ...fields...
    
    public void login(String user, String pass) {
        usernameField.type(user);
        passwordField.type(pass);
        submitButton.click();
    }
    
    public void loginAndVerify(String user, String pass) {
        login(user, pass);
        getDriver().Url.waitUntilContains("/dashboard", 10);
        getDriver().Title.verifyContains("Dashboard", "Should navigate to dashboard");
    }
}

// ❌ AVOID - Exposing internal elements
public class CLoginForm extends CWebForm<CDriver> {
    public CWebElement<CDriver> usernameField;  // Don't expose fields
    public CWebElement<CDriver> passwordField;
}
```

### 6. Provide Validation Methods

```java
// ✅ CORRECT - Provide validation helper methods
public class CRegistrationForm extends CWebForm<CDriver> {
    @CFindBy(id = "email-error")
    private CWebElement<CDriver> emailError;
    
    @CFindBy(id = "password-error")
    private CWebElement<CDriver> passwordError;
    
    public boolean hasErrors() {
        return emailError.isVisible() || passwordError.isVisible();
    }
    
    public void verifyNoErrors() {
        emailError.Visible.verifyFalse("Email error should not be visible");
        passwordError.Visible.verifyFalse("Password error should not be visible");
    }
}
```

### 7. Use Constructor for Initialization Only

```java
// ✅ CORRECT - Constructor only initializes
public CMyForm(CDriver driver) {
    super(driver);
    log.info("Form initialized");
}

// ❌ AVOID - Don't perform actions in constructor
public CMyForm(CDriver driver) {
    super(driver);
    usernameField.type("default");  // Don't do this!
    submitButton.click();           // Don't do this!
}
```

---

## Common Pitfalls

### Pitfall 1: Accessing Elements Before Initialization

```java
// ❌ WRONG - Accessing field before super() call
public CLoginForm(CDriver driver) {
    usernameField.type("test");  // NullPointerException! Elements not initialized yet
    super(driver);
}

// ✅ CORRECT - Access elements after super() call
public CLoginForm(CDriver driver) {
    super(driver);  // Elements initialized here
    log.info("Form ready, elements initialized");
}
```

### Pitfall 2: Using Wrong Annotation for Collections

```java
// ❌ WRONG - Using @CFindBy for collection
@CFindBy(xpath = "//input[@type='checkbox']")
private CWebElements<CDriver> checkboxes;  // Won't work!

// ✅ CORRECT - Use @CFindBys for collections
@CFindBys(xpath = "//input[@type='checkbox']")
private CWebElements<CDriver> checkboxes;
```

### Pitfall 3: Exposing Internal Elements

```java
// ❌ WRONG - Public fields expose implementation
public class CLoginForm extends CWebForm<CDriver> {
    public CWebElement<CDriver> usernameField;  // Exposed!
    public CWebElement<CDriver> passwordField;  // Exposed!
}

// ✅ CORRECT - Private fields with public methods
public class CLoginForm extends CWebForm<CDriver> {
    @CFindBy(id = "username")
    private CWebElement<CDriver> usernameField;
    
    @CFindBy(id = "password")
    private CWebElement<CDriver> passwordField;
    
    public void login(String user, String pass) {
        usernameField.type(user);
        passwordField.type(pass);
    }
}
```

### Pitfall 4: Mixing Form and Table Concerns

```java
// ❌ WRONG - Using CWebForm for table data
public class CDataTableForm extends CWebForm<CDriver> {
    @CFindBys(xpath = "//table//tr")
    private CWebElements<CDriver> tableRows;  // Should use table classes!
}

// ✅ CORRECT - Use CWebForm for forms only
public class CFilterForm extends CWebForm<CDriver> {
    @CFindBy(id = "search")
    private CWebElement<CDriver> searchInput;
    
    @CFindBy(id = "apply")
    private CWebElement<CDriver> applyButton;
}
```

### Pitfall 5: Not Handling Dynamic Content

```java
// ❌ RISKY - Assuming element is immediately visible
public void fillForm() {
    usernameField.type("test");  // May fail if form loads asynchronously
}

// ✅ CORRECT - Wait for element to be ready
public void fillForm() {
    usernameField.Visible.waitUntilTrue(10);
    usernameField.type("test");
}
```

### Pitfall 6: Insufficient Wait Times

```java
// ❌ WRONG - Too short wait for slow element
@CFindBy(id = "async-content", waitInSeconds = 1)
private CWebElement<CDriver> asyncContent;  // May timeout!

// ✅ CORRECT - Appropriate wait time
@CFindBy(id = "async-content", name = "Async Content", waitInSeconds = 30)
private CWebElement<CDriver> asyncContent;
```

---

## Examples

### Example 1: Complete Login Form

```java
package org.catools.web.forms;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.annotation.CFindBy;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;

/**
 * Login form for application authentication.
 * Handles username/password login and remember me functionality.
 */
@Slf4j
public class CLoginForm extends CWebForm<CDriver> {

    @CFindBy(id = "username", name = "Username Input", waitInSeconds = 10)
    private CWebElement<CDriver> usernameInput;

    @CFindBy(id = "password", name = "Password Input", waitInSeconds = 10)
    private CWebElement<CDriver> passwordInput;

    @CFindBy(id = "remember-me", name = "Remember Me Checkbox")
    private CWebElement<CDriver> rememberMeCheckbox;

    @CFindBy(css = "button[type='submit']", name = "Login Button")
    private CWebElement<CDriver> loginButton;

    @CFindBy(id = "error-message", name = "Error Message", waitInSeconds = 5)
    private CWebElement<CDriver> errorMessage;

    @CFindBy(linkText = "Forgot Password?", name = "Forgot Password Link")
    private CWebElement<CDriver> forgotPasswordLink;

    /**
     * Initialize login form.
     */
    public CLoginForm(CDriver driver) {
        super(driver);
        log.info("Login form initialized");
    }

    /**
     * Perform login with credentials.
     */
    public void login(String username, String password) {
        log.info("Logging in as: {}", username);
        
        usernameInput.Visible.verifyTrue("Username field should be visible");
        usernameInput.type(username);
        usernameInput.Value.verifyEquals(username, "Username should be entered");
        
        passwordInput.Visible.verifyTrue("Password field should be visible");
        passwordInput.Attribute("type").verifyEquals("password", "Should be password field");
        passwordInput.type(password);
        
        loginButton.Clickable.verifyTrue("Login button should be clickable");
        loginButton.click();
    }

    /**
     * Login with remember me option.
     */
    public void loginWithRememberMe(String username, String password) {
        setRememberMe(true);
        login(username, password);
    }

    /**
     * Set remember me checkbox state.
     */
    public void setRememberMe(boolean remember) {
        if (remember && !rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        } else if (!remember && rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
    }

    /**
     * Check if error message is displayed.
     */
    public boolean hasError() {
        return errorMessage.isPresent() && errorMessage.isVisible();
    }

    /**
     * Get error message text.
     */
    public String getErrorMessage() {
        if (hasError()) {
            return errorMessage.getText();
        }
        return "";
    }

    /**
     * Verify error message is displayed.
     */
    public void verifyError(String expectedMessage) {
        errorMessage.Visible.verifyTrue("Error message should be visible");
        errorMessage.Text.verifyContains(expectedMessage, "Error message should match");
    }

    /**
     * Click forgot password link.
     */
    public void clickForgotPassword() {
        forgotPasswordLink.Clickable.verifyTrue("Forgot password link should be clickable");
        forgotPasswordLink.click();
    }
}
```

### Example 2: Multi-Step Registration Form

```java
package org.catools.web.forms;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.annotation.CFindBy;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;

/**
 * Registration form with multiple steps.
 */
@Slf4j
public class CRegistrationForm extends CWebForm<CDriver> {

    // Step 1: Basic Info
    @CFindBy(id = "email", name = "Email Input")
    private CWebElement<CDriver> emailInput;

    @CFindBy(id = "password", name = "Password Input")
    private CWebElement<CDriver> passwordInput;

    @CFindBy(id = "confirm-password", name = "Confirm Password Input")
    private CWebElement<CDriver> confirmPasswordInput;

    @CFindBy(id = "next-step-1", name = "Next Button Step 1")
    private CWebElement<CDriver> nextStep1Button;

    // Step 2: Personal Info
    @CFindBy(id = "first-name", name = "First Name Input")
    private CWebElement<CDriver> firstNameInput;

    @CFindBy(id = "last-name", name = "Last Name Input")
    private CWebElement<CDriver> lastNameInput;

    @CFindBy(id = "phone", name = "Phone Input")
    private CWebElement<CDriver> phoneInput;

    @CFindBy(id = "next-step-2", name = "Next Button Step 2")
    private CWebElement<CDriver> nextStep2Button;

    // Step 3: Terms and Submit
    @CFindBy(id = "terms-checkbox", name = "Terms Checkbox")
    private CWebElement<CDriver> termsCheckbox;

    @CFindBy(id = "submit-registration", name = "Submit Button")
    private CWebElement<CDriver> submitButton;

    // Validation errors
    @CFindBy(id = "email-error", name = "Email Error")
    private CWebElement<CDriver> emailError;

    @CFindBy(id = "password-error", name = "Password Error")
    private CWebElement<CDriver> passwordError;

    public CRegistrationForm(CDriver driver) {
        super(driver);
        log.info("Registration form initialized");
    }

    /**
     * Fill step 1: Account details.
     */
    public void fillStep1(String email, String password, String confirmPassword) {
        log.info("Filling registration step 1");
        
        emailInput.Visible.verifyTrue("Email field should be visible");
        emailInput.type(email);
        
        passwordInput.type(password);
        confirmPasswordInput.type(confirmPassword);
        
        nextStep1Button.Clickable.verifyTrue("Next button should be clickable");
        nextStep1Button.click();
    }

    /**
     * Fill step 2: Personal information.
     */
    public void fillStep2(String firstName, String lastName, String phone) {
        log.info("Filling registration step 2");
        
        firstNameInput.Visible.waitUntilTrue(10);
        firstNameInput.type(firstName);
        
        lastNameInput.type(lastName);
        phoneInput.type(phone);
        
        nextStep2Button.click();
    }

    /**
     * Complete step 3: Accept terms and submit.
     */
    public void completeStep3() {
        log.info("Completing registration step 3");
        
        termsCheckbox.Visible.waitUntilTrue(10);
        if (!termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }
        
        termsCheckbox.Selected.verifyTrue("Terms should be accepted");
        
        submitButton.Clickable.verifyTrue("Submit button should be clickable");
        submitButton.click();
    }

    /**
     * Complete full registration process.
     */
    public void completeRegistration(
        String email, String password, String confirmPassword,
        String firstName, String lastName, String phone
    ) {
        fillStep1(email, password, confirmPassword);
        fillStep2(firstName, lastName, phone);
        completeStep3();
    }

    /**
     * Verify validation errors.
     */
    public void verifyValidationErrors() {
        emailError.Visible.verifyTrue("Email error should be visible");
        passwordError.Visible.verifyTrue("Password error should be visible");
    }
}
```

### Example 3: Search Form with Filters

```java
package org.catools.web.forms;

import lombok.extern.slf4j.Slf4j;
import org.catools.web.annotation.CFindBy;
import org.catools.web.annotation.CFindBys;
import org.catools.web.collections.CWebElements;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;

/**
 * Product search form with advanced filters.
 */
@Slf4j
public class CProductSearchForm extends CWebForm<CDriver> {

    @CFindBy(id = "search-input", name = "Search Query Input")
    private CWebElement<CDriver> searchInput;

    @CFindBy(id = "search-button", name = "Search Button")
    private CWebElement<CDriver> searchButton;

    @CFindBy(id = "filter-toggle", name = "Filter Toggle Button")
    private CWebElement<CDriver> filterToggle;

    @CFindBys(
        xpath = "//input[@type='checkbox'][@name='category']",
        name = "Category Filters",
        waitForFirstElementInSecond = 5
    )
    private CWebElements<CDriver> categoryFilters;

    @CFindBy(id = "price-min", name = "Min Price Input")
    private CWebElement<CDriver> minPriceInput;

    @CFindBy(id = "price-max", name = "Max Price Input")
    private CWebElement<CDriver> maxPriceInput;

    @CFindBy(id = "sort-select", name = "Sort Dropdown")
    private CWebElement<CDriver> sortDropdown;

    @CFindBy(id = "apply-filters", name = "Apply Filters Button")
    private CWebElement<CDriver> applyFiltersButton;

    @CFindBy(id = "clear-filters", name = "Clear Filters Button")
    private CWebElement<CDriver> clearFiltersButton;

    public CProductSearchForm(CDriver driver) {
        super(driver);
        log.info("Product search form initialized");
    }

    /**
     * Perform basic search.
     */
    public void search(String query) {
        log.info("Searching for: {}", query);
        
        searchInput.Visible.verifyTrue("Search input should be visible");
        searchInput.type(query);
        searchButton.click();
    }

    /**
     * Expand filter options.
     */
    public void expandFilters() {
        if (!minPriceInput.isVisible()) {
            filterToggle.click();
            minPriceInput.Visible.waitUntilTrue(5);
        }
    }

    /**
     * Select category filter by name.
     */
    public void selectCategory(String categoryName) {
        expandFilters();
        
        for (int i = 0; i < categoryFilters.size(); i++) {
            CWebElement<CDriver> checkbox = categoryFilters.get(i);
            String label = checkbox.getAttribute("data-label");
            
            if (categoryName.equals(label) && !checkbox.isSelected()) {
                checkbox.click();
                checkbox.Selected.verifyTrue("Category should be selected");
                break;
            }
        }
    }

    /**
     * Set price range filter.
     */
    public void setPriceRange(String min, String max) {
        expandFilters();
        
        if (min != null && !min.isEmpty()) {
            minPriceInput.clear();
            minPriceInput.type(min);
        }
        
        if (max != null && !max.isEmpty()) {
            maxPriceInput.clear();
            maxPriceInput.type(max);
        }
    }

    /**
     * Set sort order.
     */
    public void setSortOrder(String sortOption) {
        sortDropdown.selectByVisibleText(sortOption);
    }

    /**
     * Apply all filters.
     */
    public void applyFilters() {
        applyFiltersButton.Clickable.verifyTrue("Apply filters button should be clickable");
        applyFiltersButton.click();
    }

    /**
     * Clear all filters.
     */
    public void clearFilters() {
        clearFiltersButton.click();
    }

    /**
     * Perform advanced search with filters.
     */
    public void advancedSearch(
        String query,
        String category,
        String minPrice,
        String maxPrice,
        String sortBy
    ) {
        search(query);
        
        if (category != null) {
            selectCategory(category);
        }
        
        if (minPrice != null || maxPrice != null) {
            setPriceRange(minPrice, maxPrice);
        }
        
        if (sortBy != null) {
            setSortOrder(sortBy);
        }
        
        applyFilters();
    }
}
```

---

## Summary

`CWebForm` provides a powerful, structured approach to web form automation with:

- **Automatic Initialization** - Elements initialized via `CWebElementFactory` on construction
- **Annotation-Based Locators** - `@CFindBy`, `@CFindBys`, `@FindBy` for clean element declaration
- **Encapsulation** - All form locators in one place for better maintainability
- **Extension Framework Integration** - All elements support verification, wait, and state methods
- **Reusability** - Form objects can be reused across multiple tests
- **Type Safety** - Generic typing for driver instance
- **Best Practices** - Prefer ID locators, use descriptive names, encapsulate logic

**Critical Rules:**
1. **Use CWebForm for grouped elements** - Login forms, registration forms, search forms
2. **DON'T use for tables** - Use table-specific classes for table data
3. **Always call super(driver) first** - Elements initialized after super() call
4. **Use @CFindBy for best control** - Includes wait timeout and descriptive name
5. **Encapsulate form logic** - Provide public methods, keep fields private
6. **Verify before action** - Use extension framework `.verify*()` methods

**For More Information:**

- See `webelement-classes-guide.prompts.md` for CWebElement properties and methods
- See `extension-classes-guide.prompts.md` for extension framework details
- See `catools-web-mcp.agents.md` for MCP automation patterns

**Version History:**

- **2.0.0** (Nov 28, 2025) - Initial comprehensive guide


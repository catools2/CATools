# CWebElement Classes Guide

**Version:** 2.0.0  
**Last Updated:** November 28, 2025  
**Framework:** CATools Web Automation  
**Java Version:** 21

---

## Table of Contents

1. [Introduction](#introduction)
2. [CWebElement Overview](#cwebelement-overview)
3. [Creating CWebElement Instances](#creating-cwebelement-instances)
4. [Element Properties](#element-properties)
5. [Element State Verification](#element-state-verification)
6. [Element Actions](#element-actions)
7. [Extension Framework Integration](#extension-framework-integration)
8. [Advanced Usage Patterns](#advanced-usage-patterns)
9. [Best Practices](#best-practices)
10. [Common Pitfalls](#common-pitfalls)
11. [Examples](#examples)

---

## Introduction

`CWebElement` is the core abstraction for web elements in the CATools framework. It provides a fluent, chainable API for finding, verifying, and interacting with web elements while leveraging the CATools extension framework for advanced waiting, verification, and state management.

**This guide complements the MCP automation patterns documented in `catools-web-mcp.agents.md`.**

### Key Features

- **Fluent API** - Chainable method calls for readable test code
- **Extension Framework** - Built-in support for `CDynamicStringExtension` and `CDynamicBooleanExtension` (see `extension-classes-guide.md`)
- **Automatic Waiting** - Configurable wait mechanisms for element state with polling and custom intervals
- **Soft Assertions** - Verification methods that accumulate failures for comprehensive test reporting
- **Type Safety** - Generic typing for driver instance (`<DR extends CDriver>`)
- **Locator Strategy** - Support for all Selenium locator types (id, name, xpath, css, etc.)
- **MCP Integration** - Works seamlessly with CATools Model Context Protocol tools for web automation

### Extension Framework Integration

CWebElement uses CATools extension framework classes for powerful, declarative property access:

- **`CDynamicBooleanExtension`** - For boolean state properties (Visible, Enabled, Present, etc.)
- **`CDynamicStringExtension`** - For string content properties (Text, Value, Attribute, etc.)
- **`CDynamicNumberExtension<Integer>`** - For numeric properties (Offset)

Each extension property provides three method groups:
1. **Verification Group** - Soft assertions (`verifyTrue()`, `verifyEquals()`, `verifyContains()`, etc.)
2. **Wait Group** - Polling with timeout (`waitUntilTrue()`, `waitUntilEquals()`, `waitUntilContains()`, etc.)
3. **State Group** - Immediate evaluation (`.get()` returns current value)

**For complete extension framework documentation, see: `extension-classes-guide.md`**

---

## CWebElement Overview

### Class Signature

```java
public class CWebElement<DR extends CDriver> implements CWebElementActions<DR>, CWebElementStates<DR>
```

### Type Parameters

- `<DR>` - The driver type extending `CDriver`

### Implemented Interfaces

- `CWebElementActions<DR>` - Provides action methods (click, type, submit, etc.)
- `CWebElementStates<DR>` - Provides state checking and property retrieval methods

### Core Components

1. **Driver Reference** - The `CDriver` instance managing the browser session
2. **Locator** - The `By` locator used to find the element
3. **Wait Timeout** - Configurable timeout for element operations (default from config)
4. **Element Name** - Descriptive name for logging and debugging

---

## Creating CWebElement Instances

### Method 1: Using CDriver Factory Methods (Recommended)

```java
// Find by ID
CWebElement<CDriver> searchBox = driver.findElementById("search-input", 10);

// Find by name
CWebElement<CDriver> username = driver.findElementByName("username", 5);

// Find by XPath
CWebElement<CDriver> submitBtn = driver.findElementByXPath("//button[@type='submit']", 10);

// Find by CSS selector
CWebElement<CDriver> header = driver.findElementByCssSelector(".main-header", 5);

// Find by class name
CWebElement<CDriver> modal = driver.findElementByClassName("modal-dialog", 10);

// Find by link text
CWebElement<CDriver> loginLink = driver.findElementByLinkText("Login", 5);

// Find by partial link text
CWebElement<CDriver> helpLink = driver.findElementByPartialLinkText("Help", 5);

// Find by tag name
CWebElement<CDriver> firstButton = driver.findElementByTagName("button", 5);
```

### Method 2: Direct Constructor (Advanced)

```java
// Create element with explicit locator
By locator = By.id("submit-button");
CWebElement<CDriver> element = new CWebElement<>("Submit Button", driver, locator, 10);
```

### Method 3: Nested Element Finding

```java
// Find parent element
CWebElement<CDriver> form = driver.findElementById("login-form", 10);

// Find child element within parent
CWebElement<CDriver> passwordField = form.findElement(By.name("password"));
```

---

## Element Properties

`CWebElement` provides extensive properties through the extension framework and simple getter methods.

### Extension Framework Properties (Advanced)

These properties use `CDynamicStringExtension` or `CDynamicBooleanExtension` and provide advanced verification, waiting, and state evaluation capabilities.

#### Boolean State Properties

| Property | Type | Description | Example |
|----------|------|-------------|---------|
| `Visible` | `CDynamicBooleanExtension` | Element is displayed and visible | `element.Visible.verifyTrue()` |
| `Present` | `CDynamicBooleanExtension` | Element exists in DOM | `element.Present.waitUntilTrue()` |
| `Enabled` | `CDynamicBooleanExtension` | Element is enabled and interactable | `element.Enabled.verifyTrue()` |
| `Selected` | `CDynamicBooleanExtension` | Element is selected (checkbox/radio/option) | `element.Selected.get()` |
| `Clickable` | `CDynamicBooleanExtension` | Element is clickable (visible + enabled) | `element.Clickable.verifyTrue()` |
| `Staleness` | `CDynamicBooleanExtension` | Element is stale (detached from DOM) | `element.Staleness.waitUntilTrue()` |

#### String Content Properties

| Property | Type | Description | Example |
|----------|------|-------------|---------|
| `Text` | `CDynamicStringExtension` | Inner text content of element | `element.Text.verifyEquals("Submit")` |
| `Value` | `CDynamicStringExtension` | Value attribute (form inputs) | `element.Value.verifyContains("test")` |
| `InnerHTML` | `CDynamicStringExtension` | HTML content within element | `element.InnerHTML.get()` |
| `TagName` | `CDynamicStringExtension` | HTML tag name | `element.TagName.verifyEquals("button")` |
| `AriaRole` | `CDynamicStringExtension` | ARIA role for accessibility | `element.AriaRole.verifyEquals("button")` |

#### Dynamic Properties (Methods)

| Property | Type | Description | Example |
|----------|------|-------------|---------|
| `Css(key)` | `CDynamicStringExtension` | CSS property value | `element.Css("color").verifyEquals("red")` |
| `Attribute(name)` | `CDynamicStringExtension` | HTML attribute value | `element.Attribute("href").verifyContains("/login")` |

#### Special Properties

| Property | Type | Description | Example |
|----------|------|-------------|---------|
| `Offset` | `CDynamicNumberExtension<Integer>` | Element offset position | `element.Offset.get()` |
| `ScreenShot` | `CScreenShot` | Element screenshot | `element.ScreenShot.get()` |

### Simple Getter Methods (Direct Access)

For quick property retrieval without extension framework overhead:

```java
// Boolean state methods
boolean isVisible = element.isVisible();
boolean isPresent = element.isPresent();
boolean isEnabled = element.isEnabled();
boolean isSelected = element.isSelected();
boolean isClickable = element.isClickable();
boolean isStale = element.isStaleness();

// String content methods
String text = element.getText();
String value = element.getValue();
String innerHTML = element.getInnerHTML();
String tagName = element.getTagName();
String ariaRole = element.getAriaRole();

// Attribute methods
String href = element.getAttribute("href");
String cssColor = element.getCss("color");

// Special properties
Integer offset = element.getOffset();
BufferedImage screenshot = element.getScreenShot();
```

---

## Element State Verification

### Using Extension Framework (Recommended for Complex Scenarios)

The extension framework provides three groups of methods:

#### 1. Verification Group (Soft Assertions)

```java
CWebElement<CDriver> button = driver.findElementById("submit", 10);

// Boolean state verification
button.Visible.verifyTrue("Submit button should be visible");
button.Enabled.verifyTrue("Submit button should be enabled");
button.Clickable.verifyTrue("Submit button should be clickable");

// String content verification
button.Text.verifyEquals("Submit", "Button text should be 'Submit'");
button.Text.verifyContains("Sub", "Button text should contain 'Sub'");
button.Text.verifyNotEmpty("Button text should not be empty");

// CSS and attribute verification
button.Css("background-color").verifyContains("blue", "Button should be blue");
button.Attribute("type").verifyEquals("submit", "Button type should be submit");
```

#### 2. Wait Group (Polling with Timeout)

```java
CWebElement<CDriver> loadingSpinner = driver.findElementById("spinner", 5);

// Wait for element to become visible (returns boolean)
boolean appeared = loadingSpinner.Visible.waitUntilTrue(10);

// Wait for element to disappear
boolean disappeared = loadingSpinner.Visible.waitUntilFalse(15);

// Wait for text to change
CWebElement<CDriver> status = driver.findElementById("status", 5);
boolean loaded = status.Text.waitUntilEquals("Complete", 20);

// Wait for text to contain specific value
boolean ready = status.Text.waitUntilContains("Ready", 10);

// Wait with custom interval
boolean visible = button.Visible.waitUntilTrue(20, 500); // 20s timeout, 500ms interval
```

#### 3. State Group (Immediate Evaluation)

```java
// Get current state immediately (no waiting)
boolean isVisible = button.Visible.get();
String buttonText = button.Text.get();
String cssColor = button.Css("color").get();
```

### Using Simple Methods (Recommended for Quick Checks)

```java
CWebElement<CDriver> element = driver.findElementById("my-element", 10);

// Quick boolean checks
if (element.isVisible()) {
    element.click();
}

if (element.isPresent() && element.isEnabled()) {
    element.type("text");
}

// Quick value extraction
String text = element.getText();
String value = element.getValue();

// Then use AssertJ for assertions
assertThat(text).isEqualTo("Expected Text");
assertThat(value).contains("partial");
```

---

## Element Actions

### Click Actions

```java
CWebElement<CDriver> button = driver.findElementById("submit-btn", 10);

// Standard click
button.click();

// Mouse click (uses Actions class)
button.mouseClick();

// Scroll into view then click
button.scrollIntoView(true); // true = scroll down, false = scroll up
button.click();
```

### Text Input Actions

```java
CWebElement<CDriver> inputField = driver.findElementById("email", 10);

// Type text (character by character)
inputField.type("user@example.com");

// Send keys (including special keys)
inputField.sendKeys("username");
inputField.sendKeys(Keys.TAB);

// Clear then type
inputField.clear();
inputField.type("new@example.com");
```

### Form Actions

```java
CWebElement<CDriver> form = driver.findElementById("login-form", 10);

// Submit form
form.submit();

// Select from dropdown
CWebElement<CDriver> dropdown = driver.findElementById("country", 10);
dropdown.selectByValue("US");
dropdown.selectByVisibleText("United States");
dropdown.selectByIndex(0);
```

### Special Actions

```java
// Execute JavaScript on element
Object result = driver.executeScriptOnElement(
    element.getLocator(), 
    10, 
    "arguments[0].style.border='2px solid red'"
);

// Scroll element into view
element.scrollIntoView(true);

// Take screenshot of element
BufferedImage screenshot = element.getScreenShot();
```

---

## Extension Framework Integration

### Understanding Extension Classes

`CWebElement` uses CATools extension framework classes for advanced capabilities:

- **`CDynamicBooleanExtension`** - For boolean properties (Visible, Enabled, etc.)
- **`CDynamicStringExtension`** - For string properties (Text, Value, etc.)
- **`CDynamicNumberExtension<Integer>`** - For numeric properties (Offset)

### Extension Methods Available

Each extension property provides:

#### Verification Methods (Soft Assertions)

```java
// Boolean extensions
element.Visible.verifyTrue(message);
element.Visible.verifyFalse(message);

// String extensions
element.Text.verifyEquals(expected, message);
element.Text.verifyNotEquals(expected, message);
element.Text.verifyContains(substring, message);
element.Text.verifyNotContains(substring, message);
element.Text.verifyStartsWith(prefix, message);
element.Text.verifyEndsWith(suffix, message);
element.Text.verifyMatches(pattern, message);
element.Text.verifyEmpty(message);
element.Text.verifyNotEmpty(message);
```

#### Wait Methods (Polling)

```java
// Boolean extensions
boolean result = element.Visible.waitUntilTrue(timeoutSeconds);
boolean result = element.Visible.waitUntilFalse(timeoutSeconds);
boolean result = element.Visible.waitUntilTrue(timeoutSeconds, intervalMillis);

// String extensions
boolean result = element.Text.waitUntilEquals(expected, timeoutSeconds);
boolean result = element.Text.waitUntilNotEquals(expected, timeoutSeconds);
boolean result = element.Text.waitUntilContains(substring, timeoutSeconds);
boolean result = element.Text.waitUntilNotContains(substring, timeoutSeconds);
```

#### State Methods (Immediate)

```java
// Get current value immediately
boolean isVisible = element.Visible.get();
String text = element.Text.get();
Integer offset = element.Offset.get();
```

### When to Use Extension Framework vs Simple Methods

**Use Extension Framework Verification Methods When:**
- You need soft assertions (accumulate failures for comprehensive test reporting)
- You need custom waiting with polling and configurable intervals
- You want fluent, chainable verification with descriptive error messages
- You need complex wait conditions (text changes, state transitions)
- **You're verifying any property value** (text, visibility, enabled state, etc.)

**Use Simple Getter Methods When:**
- You need quick value extraction for **non-assertion purposes** (logging, calculations, conditional logic)
- You want immediate value extraction without waiting
- **Never use for assertions** - always use extension framework `.verify*()` methods instead

**Critical: Extension Framework for ALL Assertions**
```java
// ✅ CORRECT - Extension framework verification (ALWAYS for assertions)
element.Text.verifyEquals("Expected", "Element text should match");
element.Visible.verifyTrue("Element should be visible");
driver.Title.verifyContains("Dashboard", "Should be on dashboard");

// ✅ ACCEPTABLE - Simple getter for non-assertion logic
String currentText = element.getText();
log.info("Current element text: {}", currentText);
if (currentText.isEmpty()) {
    // Take some action
}

// ❌ WRONG - Simple getter followed by assertion (NEVER!)
String text = element.getText();
assertThat(text).isEqualTo("Expected");  // Should use element.Text.verifyEquals()

// ❌ WRONG - Extension .get() followed by assertion (ALSO WRONG!)
String titleText = driver.Title.get();
assertThat(titleText).contains("Dashboard");  // Should use driver.Title.verifyContains()
```

---

## Advanced Usage Patterns

### Pattern 1: Multi-Step Element Verification

```java
@Test(description = "Comprehensive element verification")
public void testElementVerification() {
    CWebElement<CDriver> button = driver.findElementById("submit-btn", 10);
    
    // Verify all states using extension framework (soft assertions)
    button.Present.verifyTrue("Button should be present in DOM");
    button.Visible.verifyTrue("Button should be visible");
    button.Enabled.verifyTrue("Button should be enabled");
    button.Clickable.verifyTrue("Button should be clickable");
    button.Text.verifyEquals("Submit", "Button text should be 'Submit'");
    button.Attribute("type").verifyEquals("submit", "Button type should be submit");
    
    // All verifications above are soft assertions - failures are accumulated
    // Hard assertion will be triggered at end of test if any soft assertion failed
}
```

### Pattern 2: Wait for Dynamic Content

```java
@Test(description = "Wait for dynamic element state changes")
public void testDynamicContent() {
    // Click button that triggers async operation
    getDriver().findElementById("load-data", 5).click();
    
    // Wait for loading spinner to appear
    CWebElement<CDriver> spinner = getDriver().findElementById("spinner", 5);
    spinner.Visible.waitUntilTrue(5);
    
    // Wait for spinner to disappear
    spinner.Visible.waitUntilFalse(30);
    
    // Wait for success message to appear with specific text
    CWebElement<CDriver> message = getDriver().findElementById("status-message", 5);
    message.Text.waitUntilContains("Success", 10);
    message.Visible.verifyTrue("Success message should be visible");
}
```

### Pattern 3: Conditional Element Interaction

```java
@Test(description = "Conditional element handling")
public void testConditionalInteraction() {
    CWebElement<CDriver> popup = getDriver().findElementByClassName("cookie-banner", 5);
    
    // Check if popup exists and is visible before interacting
    if (popup.isPresent() && popup.isVisible()) {
        CWebElement<CDriver> acceptBtn = popup.findElement(By.id("accept-cookies"));
        acceptBtn.click();
        
        // Wait for popup to disappear
        popup.Visible.waitUntilFalse(5);
    }
}
```

### Pattern 4: Element State Polling

```java
@Test(description = "Poll element until condition met")
public void testElementPolling() {
    CWebElement<CDriver> progressBar = getDriver().findElementById("progress", 10);
    
    // Wait for progress to complete (text = "100%")
    boolean completed = progressBar.Text.waitUntilEquals("100%", 60);
    assertThat(completed).isTrue();
    
    // Verify final state
    progressBar.Attribute("aria-valuenow").verifyEquals("100", "Progress should be 100");
}
```

### Pattern 5: Attribute-Based Verification

```java
@Test(description = "Verify element attributes")
public void testElementAttributes() {
    CWebElement<CDriver> input = getDriver().findElementById("email-input", 10);
    
    // Verify multiple attributes
    input.Attribute("type").verifyEquals("email", "Input type should be email");
    input.Attribute("required").verifyNotEmpty("Input should be required");
    input.Attribute("placeholder").verifyContains("email", "Placeholder should mention email");
    input.AriaRole.verifyEquals("textbox", "ARIA role should be textbox");
    
    // CSS property verification
    input.Css("border-color").verifyContains("rgb", "Border should have color");
}
```

### Pattern 6: Nested Element Finding

```java
@Test(description = "Find nested elements within parent")
public void testNestedElements() {
    // Find parent container
    CWebElement<CDriver> form = getDriver().findElementById("registration-form", 10);
    form.Visible.verifyTrue("Form should be visible");
    
    // Find child elements within parent
    CWebElement<CDriver> firstNameInput = form.findElement(By.name("firstName"));
    CWebElement<CDriver> lastNameInput = form.findElement(By.name("lastName"));
    CWebElement<CDriver> submitButton = form.findElement(By.cssSelector("button[type='submit']"));
    
    // Interact with nested elements
    firstNameInput.type("John");
    lastNameInput.type("Doe");
    submitButton.click();
}
```

### Pattern 7: Screenshot Capture on State

```java
@Test(description = "Capture screenshots at specific states")
public void testScreenshotCapture() {
    CWebElement<CDriver> modal = getDriver().findElementByClassName("modal", 10);
    
    // Wait for modal to appear
    modal.Visible.waitUntilTrue(10);
    
    // Capture screenshot of modal when visible
    BufferedImage modalImage = modal.ScreenShot.get();
    
    // Save screenshot to file (using CATools utilities)
    CImageUtil.saveImage(modalImage, "modal-screenshot.png");
    
    // Verify modal content
    modal.Text.verifyContains("Confirmation", "Modal should show confirmation");
}
```

---

## Best Practices

### 1. ALWAYS Use Extension Framework Verification (NEVER Extract-Then-Assert)

```java
// ✅ CORRECT - Extension framework with soft assertions
button.Visible.verifyTrue("Button should be visible");
button.Enabled.verifyTrue("Button should be enabled");
button.Text.verifyEquals("Submit", "Button text should be Submit");

// ✅ CORRECT - Driver extension properties
driver.Title.verifyContains("Dashboard", "Should be on dashboard");
driver.Url.verifyContains("/dashboard", "URL should reflect dashboard");

// ❌ WRONG - Extract with getText() then assert (NEVER DO THIS!)
String text = button.getText();
assertThat(text).isEqualTo("Submit");

// ❌ WRONG - Extract from extension property then assert (ALSO NEVER!)
String titleText = driver.Title.get();
assertThat(titleText).contains("Dashboard");

// ❌ WRONG - Extract from element property then assert (AVOID!)
String buttonValue = button.Text.get();
assertThat(buttonValue).isEqualTo("Submit");
```

**Critical Rule:** NEVER extract property values into variables and then use AssertJ. ALWAYS use the extension framework's `.verify*()` methods directly.

**Exception:** Only extract values when you need them for multiple checks or non-assertion logic (e.g., logging, calculations).

### 2. Use Appropriate Wait Times

```java
// ✅ GOOD - Reasonable wait times based on expected behavior
CWebElement<CDriver> fastElement = driver.findElementById("cached-content", 5);
CWebElement<CDriver> slowElement = driver.findElementById("async-data", 30);

// ❌ AVOID - Excessive wait times for all elements
CWebElement<CDriver> element = driver.findElementById("simple-button", 60); // Too long
```

### 3. Use Descriptive Element Names

```java
// ✅ GOOD - Clear, descriptive names
CWebElement<CDriver> loginButton = driver.findElementById("login-btn", 10);
CWebElement<CDriver> emailInput = driver.findElementByName("email", 10);

// ❌ AVOID - Generic or unclear names
CWebElement<CDriver> btn = driver.findElementById("login-btn", 10);
CWebElement<CDriver> el1 = driver.findElementByName("email", 10);
```

### 4. Verify Before Action

```java
// ✅ GOOD - Verify element state before interaction
CWebElement<CDriver> button = getDriver().findElementById("submit", 10);
button.Visible.verifyTrue("Button should be visible");
button.Enabled.verifyTrue("Button should be enabled");
button.click();

// ❌ RISKY - Click without verification
button.click(); // May fail if element not ready
```

### 5. Use Appropriate Locator Strategy

```java
// ✅ GOOD - Prefer ID and name locators (faster, more stable)
CWebElement<CDriver> element1 = getDriver().findElementById("user-id", 10);
CWebElement<CDriver> element2 = getDriver().findElementByName("username", 10);

// ⚠️ ACCEPTABLE - XPath/CSS when necessary
CWebElement<CDriver> element3 = getDriver().findElementByXPath("//button[@data-test='submit']", 10);

// ❌ AVOID - Overly complex, fragile locators
CWebElement<CDriver> element4 = getDriver().findElementByXPath(
    "//div[1]/div[2]/span[3]/button[@class='btn btn-primary']", 10
);
```

### 6. Leverage Chainable API

```java
// ✅ GOOD - Fluent chaining for readability
getDriver().findElementById("username", 10)
    .type("testuser");

getDriver().findElementById("password", 10)
    .type("password123");
```

### 7. Handle Stale Elements Properly

```java
// ✅ GOOD - Re-find element after page change
CWebElement<CDriver> button = getDriver().findElementById("submit", 10);
button.click();

// Wait for page to change
getDriver().Url.waitUntilContains("/success", 10);

// Re-find element if needed on new page
CWebElement<CDriver> successMessage = getDriver().findElementById("message", 10);
successMessage.Visible.verifyTrue("Success message should appear");

// ❌ AVOID - Reusing element reference after page navigation
// button.click(); // Will fail with StaleElementReferenceException
```

---

## Common Pitfalls

### Pitfall 1: Extracting Values Before Assertions (CRITICAL - Most Common Mistake)

```java
// ❌ WRONG - Extract-then-assert pattern (DO NOT DO THIS!)
String buttonText = button.getText();
assertThat(buttonText).isEqualTo("Submit");

// ❌ ALSO WRONG - Extract from extension property then assert
String pageTitle = driver.Title.get();
assertThat(pageTitle).contains("Google");

// ❌ ALSO WRONG - Extract from element property then assert
String gmailText = gmailLink.Text.get();
assertThat(gmailText).isEqualTo("Gmail");

// ✅ CORRECT - Use extension framework verification directly
button.Text.verifyEquals("Submit", "Button text should be Submit");

// ✅ CORRECT - Use driver extension property verification
driver.Title.verifyContains("Google", "Page title should contain 'Google'");

// ✅ CORRECT - Use element extension property verification
gmailLink.Text.verifyEquals("Gmail", "Gmail link text should match");
```

**Why This Is Critical:**
1. **Extract-then-assert pattern bypasses extension framework** - No automatic waiting/retry
2. **Produces flaky tests** - Value extracted at single point in time, no polling
3. **Prevents soft assertion collection** - Hard assertions fail immediately
4. **Worse error messages** - Generic AssertJ error vs contextual CATools error
5. **Less maintainable code** - More verbose, harder to read intent

**Agent Scan Pattern:**
Before emitting Java code, scan for regex: `String\s+\w+\s*=\s*.*\.get(Text|Value|Title|Url)\(\);` followed by `assertThat\(` - replace with extension framework verification.

**For MCP automation patterns, see: `catools-web-mcp.agents.md`**

### Pitfall 2: Not Waiting for Element State

```java
// ❌ WRONG - Immediate action without wait
getDriver().findElementById("async-button", 5).click();

// ✅ CORRECT - Wait for element to be clickable
CWebElement<CDriver> asyncButton = getDriver().findElementById("async-button", 5);
asyncButton.Clickable.waitUntilTrue(10);
asyncButton.click();
```

### Pitfall 3: Ignoring Element Hierarchy

```java
// ❌ WRONG - Finding child with global locator (may find wrong element)
CWebElement<CDriver> submitBtn = getDriver().findElementByTagName("button", 10);

// ✅ CORRECT - Find within parent context
CWebElement<CDriver> form = getDriver().findElementById("login-form", 10);
CWebElement<CDriver> submitBtn = form.findElement(By.tagName("button"));
```

### Pitfall 4: Excessive Wait Times

```java
// ❌ WRONG - Default 60s wait for simple elements
CWebElement<CDriver> logo = getDriver().findElementById("site-logo", 60);

// ✅ CORRECT - Use appropriate timeout
CWebElement<CDriver> logo = getDriver().findElementById("site-logo", 5);
```

### Pitfall 5: Not Checking Element Presence

```java
// ❌ RISKY - Assuming element exists
CWebElement<CDriver> modal = getDriver().findElementByClassName("modal", 5);
modal.click(); // May fail if modal doesn't exist

// ✅ CORRECT - Check presence first
CWebElement<CDriver> modal = getDriver().findElementByClassName("modal", 5);
if (modal.isPresent() && modal.isVisible()) {
    modal.findElement(By.className("close-btn")).click();
}
```

### Pitfall 6: Reusing Stale Element References

```java
// ❌ WRONG - Element becomes stale after page reload
CWebElement<CDriver> button = getDriver().findElementById("submit", 10);
button.click();
getDriver().navigate().refresh();
button.click(); // StaleElementReferenceException!

// ✅ CORRECT - Re-find after page change
CWebElement<CDriver> button = getDriver().findElementById("submit", 10);
button.click();
getDriver().navigate().refresh();
CWebElement<CDriver> newButton = getDriver().findElementById("submit", 10);
newButton.click();
```

---

## Examples

### Example 1: Login Form Automation

```java
@Test(description = "Login with username and password")
public void testLogin() {
    log.info("Starting login test");
    
    // Navigate to login page using CWebTest method
    open("https://example.com/login");
    getDriver().Title.verifyContains("Login", "Should be on login page");
    
    // Find and verify username field
    CWebElement<CDriver> usernameField = getDriver().findElementById("username", 10);
    usernameField.Visible.verifyTrue("Username field should be visible");
    usernameField.Enabled.verifyTrue("Username field should be enabled");
    
    // Type username
    usernameField.type("testuser@example.com");
    usernameField.Value.verifyEquals("testuser@example.com", "Username should be entered");
    
    // Find and verify password field
    CWebElement<CDriver> passwordField = getDriver().findElementById("password", 10);
    passwordField.Attribute("type").verifyEquals("password", "Should be password field");
    
    // Type password
    passwordField.type("SecurePassword123");
    
    // Find and click login button
    CWebElement<CDriver> loginButton = getDriver().findElementById("login-btn", 10);
    loginButton.Text.verifyEquals("Login", "Button text should be 'Login'");
    loginButton.Clickable.verifyTrue("Login button should be clickable");
    loginButton.click();
    
    // Wait for navigation using extension framework
    getDriver().Url.waitUntilContains("/dashboard", 15);
    
    // Verify successful login using extension framework
    CWebElement<CDriver> welcomeMessage = getDriver().findElementById("welcome-msg", 10);
    welcomeMessage.Visible.verifyTrue("Welcome message should appear");
    welcomeMessage.Text.verifyContains("Welcome", "Should show welcome text");
    
    log.info("Login test completed successfully");
}
```

### Example 2: Form Validation Testing

```java
@Test(description = "Test form validation messages")
public void testFormValidation() {
    open("https://example.com/register");
    
    // Find submit button and click without filling form
    CWebElement<CDriver> submitBtn = getDriver().findElementById("submit", 10);
    submitBtn.click();
    
    // Verify validation messages appear
    CWebElement<CDriver> emailError = getDriver().findElementById("email-error", 10);
    emailError.Visible.waitUntilTrue(5);
    emailError.Text.verifyContains("required", "Should show required error");
    emailError.Css("color").verifyContains("red", "Error should be red");
    
    // Fill email with invalid format
    CWebElement<CDriver> emailField = getDriver().findElementById("email", 10);
    emailField.type("invalid-email");
    submitBtn.click();
    
    // Verify format validation
    emailError.Text.verifyContains("valid email", "Should show format error");
    
    // Fix email and verify error disappears
    emailField.clear();
    emailField.type("valid@example.com");
    emailError.Visible.waitUntilFalse(5);
}
```

### Example 3: Dynamic Content Handling

```java
@Test(description = "Handle dynamically loaded content")
public void testDynamicContent() {
    open("https://example.com/products");
    
    // Click "Load More" button
    CWebElement<CDriver> loadMoreBtn = getDriver().findElementById("load-more", 10);
    loadMoreBtn.Clickable.verifyTrue("Load more button should be clickable");
    loadMoreBtn.click();
    
    // Wait for loading spinner
    CWebElement<CDriver> spinner = getDriver().findElementByClassName("loading-spinner", 5);
    spinner.Visible.waitUntilTrue(5);
    
    // Wait for spinner to disappear
    spinner.Visible.waitUntilFalse(30);
    
    // Verify new products loaded - use extension framework for verification
    CWebElement<CDriver> productList = getDriver().findElementById("product-list", 10);
    productList.Visible.verifyTrue("Product list should be visible");
    
    // Execute script to count products (for non-assertion logic)
    Object productCount = getDriver().executeScript(
        "return document.querySelectorAll('.product-item').length"
    );
    log.info("Loaded {} products", productCount);
    assertThat((Long) productCount).isGreaterThan(10L);
    
    // Verify first new product using extension framework
    CWebElement<CDriver> firstProduct = getDriver().findElementByClassName("product-item", 5);
    firstProduct.Visible.verifyTrue("Product should be visible");
    firstProduct.Text.verifyNotEmpty("Product should have text");
}
```

### Example 4: Modal Dialog Interaction

```java
@Test(description = "Interact with modal dialogs")
public void testModalDialog() {
    open("https://example.com/settings");
    
    // Click button to open modal
    getDriver().findElementById("delete-account", 10).click();
    
    // Wait for modal to appear
    CWebElement<CDriver> modal = getDriver().findElementByClassName("confirmation-modal", 10);
    modal.Visible.waitUntilTrue(5);
    modal.Attribute("role").verifyEquals("dialog", "Should be a dialog");
    
    // Verify modal content
    CWebElement<CDriver> modalTitle = modal.findElement(By.className("modal-title"));
    modalTitle.Text.verifyContains("Confirm", "Should show confirmation title");
    
    // Verify buttons
    CWebElement<CDriver> cancelBtn = modal.findElement(By.id("cancel-btn"));
    CWebElement<CDriver> confirmBtn = modal.findElement(By.id("confirm-btn"));
    
    cancelBtn.Visible.verifyTrue("Cancel button should be visible");
    confirmBtn.Visible.verifyTrue("Confirm button should be visible");
    confirmBtn.Css("background-color").verifyContains("red", "Confirm should be red");
    
    // Click cancel
    cancelBtn.click();
    
    // Verify modal closed
    modal.Visible.waitUntilFalse(5);
}
```

### Example 5: Table Data Verification

```java
@Test(description = "Verify table data and structure")
public void testTableData() {
    open("https://example.com/users");
    
    // Find table
    CWebElement<CDriver> table = getDriver().findElementById("users-table", 10);
    table.Visible.verifyTrue("Table should be visible");
    table.TagName.verifyEquals("table", "Should be a table element");
    
    // Verify table headers
    CWebElement<CDriver> headerRow = table.findElement(By.tagName("thead"));
    String headerText = headerRow.getText();
    assertThat(headerText).contains("Name", "Email", "Role");
    
    // Find first data row
    CWebElement<CDriver> firstRow = getDriver().findElementByXPath("//table[@id='users-table']/tbody/tr[1]", 10);
    firstRow.Visible.verifyTrue("First row should be visible");
    
    // Verify first row cells
    CWebElement<CDriver> nameCell = firstRow.findElement(By.xpath("./td[1]"));
    CWebElement<CDriver> emailCell = firstRow.findElement(By.xpath("./td[2]"));
    CWebElement<CDriver> roleCell = firstRow.findElement(By.xpath("./td[3]"));
    
    nameCell.Text.verifyNotEmpty("Name should not be empty");
    emailCell.Text.verifyContains("@", "Email should contain @");
    roleCell.Text.verifyMatches("Admin|User|Guest", "Role should be valid");
    
    // Count rows (for non-assertion logic)
    Object rowCount = getDriver().executeScript(
        "return document.querySelectorAll('#users-table tbody tr').length"
    );
    log.info("Table has {} rows", rowCount);
    assertThat((Long) rowCount).isGreaterThan(0L);
}
```

---

## Summary

`CWebElement` provides a comprehensive, fluent API for web element automation with:

- **Multiple creation patterns** - Factory methods, constructors, nested finding
- **Rich property access** - Extension framework and simple getters
- **Advanced verification** - Soft assertions, waiting, state evaluation
- **Action methods** - Click, type, submit, scroll, screenshot
- **Best practices** - **ALWAYS use extension framework for assertions**, appropriate waits, descriptive names
- **Error avoidance** - Handle stale elements, verify before action, use proper locators
- **MCP Integration** - Works seamlessly with Model Context Protocol tools (see `catools-web-mcp.agents.md`)

**Critical Rules:**
1. **NEVER extract property values then assert** - Always use extension framework `.verify*()` methods
2. **Use extension framework for ALL assertions** - `element.Text.verifyEquals()`, not `assertThat(element.getText())`
3. **Use simple getters only for non-assertion logic** - Logging, calculations, conditional checks

By following the patterns and practices in this guide, you can create reliable, maintainable, and readable web automation tests using CATools framework.

---

**For More Information:**

- See `extension-classes-guide.md` for detailed extension framework documentation
- See `catools-web-mcp.agents.md` for MCP automation patterns and anti-patterns
- See CATools JavaDoc for complete API reference

**Version History:**

- **2.0.0** (Nov 28, 2025) - Initial comprehensive guide with MCP integration patterns


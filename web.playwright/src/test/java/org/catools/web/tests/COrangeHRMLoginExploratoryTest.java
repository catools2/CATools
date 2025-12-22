package org.catools.web.tests;

import org.catools.common.extensions.verify.CVerify;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverSession;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * OrangeHRM Login Exploratory Test - CATools Web Style
 *
 * <p>This test class demonstrates comprehensive login functionality testing including invalid
 * credentials, error messages, and successful authentication.
 *
 * <p>Generated from MCP exploratory testing session on December 14, 2025
 */
public class COrangeHRMLoginExploratoryTest extends CPlaywrightTest<CDriver> {

  private static final String ORANGEHRM_URL = "https://opensource-demo.orangehrmlive.com/";
  private static final String USERNAME_FIELD = "username";
  private static final String PASSWORD_FIELD = "password";
  private static final String VALID_USERNAME = "Admin";
  private static final String VALID_PASSWORD = "admin123";

  /**
   * Test 1: Verify login page loads and elements are present Tests that all login form elements are
   * visible and accessible
   */
  @Test(description = "Verify login page loads with all required elements")
  public void testLoginPageElementsPresent() {
    // Step 1: Navigate to OrangeHRM login page
    open(ORANGEHRM_URL);

    // Step 2: Find and verify username field
    var usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']");
    CVerify.Object.isNotNull(usernameField, "Username field should be present");
    usernameField.Visible.verifyIsTrue("Username field should be visible");

    // Step 3: Find and verify password field
    var passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']");
    CVerify.Object.isNotNull(passwordField, "Password field should be present");
    passwordField.Visible.verifyIsTrue("Password field should be visible");

    // Step 4: Find and verify login button
    var loginButton = getDriver().$("//button[@type='submit']", 10);
    CVerify.Object.isNotNull(loginButton, "Login button should be present");
    loginButton.Visible.verifyIsTrue("Login button should be visible");
    loginButton.Text.verifyContains("Login", "Login button text should contain 'Login'");
  }

  /**
   * Test 2: Verify invalid credentials show error message Tests that entering wrong credentials
   * displays appropriate error
   */
  @Test(description = "Verify invalid credentials display error message")
  public void testInvalidCredentialsShowError() {
    open(ORANGEHRM_URL);

    // Step 2: Enter invalid username
    var usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']", 5);
    usernameField.setText("invalidUser");

    // Step 3: Enter invalid password
    var passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']", 5);
    passwordField.setText("wrongPassword123");

    // Step 4: Click login button
    var loginButton = getDriver().$("//button[@type='submit']", 5);
    loginButton.click();

    // Step 5: Verify error message appears
    var errorAlert = getDriver().$(".oxd-alert", 10);
    CVerify.Object.isNotNull(errorAlert, "Error alert should be present");
    errorAlert.Visible.verifyIsTrue("Error alert should be visible");
    errorAlert.Text.verifyEquals(
        "Invalid credentials", "Error message should indicate invalid credentials");
  }

  /**
   * Test 3: Verify successful login with valid credentials Tests that valid credentials allow
   * access to the system
   */
  @Test(description = "Verify successful login with valid credentials")
  public void testSuccessfulLoginWithValidCredentials() {
    // Step 1: Navigate to login page
    open(ORANGEHRM_URL);

    // Step 2: Enter valid username
    var usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']", 5);
    usernameField.setText(VALID_USERNAME);

    // Step 3: Enter valid password
    var passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']", 5);
    passwordField.setText(VALID_PASSWORD);

    // Step 4: Click login button
    var loginButton = getDriver().$("//button[@type='submit']", 5);
    loginButton.click();

    // Step 5: Verify successful login by checking user dropdown
    var userDropdown = getDriver().$(".oxd-userdropdown", 10);
    CVerify.Object.isNotNull(userDropdown, "User dropdown should be present after login");
    userDropdown.Visible.verifyIsTrue("User dropdown should be visible after successful login");
  }

  /**
   * Test 4: Verify credentials hint is displayed on login page Tests that the page provides helpful
   * credential information
   */
  @Test(description = "Verify login page displays credential hints")
  public void testCredentialHintsDisplayed() {
    // Step 1: Navigate to login page
    open(ORANGEHRM_URL);

    // Step 2: Find login slot information section
    var loginSlot = getDriver().$(".orangehrm-login-slot", 5);
    CVerify.Object.isNotNull(loginSlot, "Login info section should be present");

    // Step 3: Verify credential hints are visible
    loginSlot.Text.verifyContains("Username : Admin", "Should display username hint");
    loginSlot.Text.verifyContains("Password : admin123", "Should display password hint");
  }

  /**
   * Test 5: End-to-end login scenario with navigation Complete workflow from login page to
   * dashboard
   */
  @Test(description = "Verify complete end-to-end login workflow")
  public void testEndToEndLoginScenario() {
    // Step 1: Navigate to OrangeHRM
    open(ORANGEHRM_URL);

    // Step 2: Verify we're on login page
    getDriver().Url.verifyContains("orangehrmlive.com", "Should be on OrangeHRM domain");

    // Step 3: Find and fill username
    var usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']", 5);
    CVerify.Object.isNotNull(usernameField, "Username field should be present");
    usernameField.setText(VALID_USERNAME);

    // Step 4: Verify username was entered
    usernameField.Value.verifyEquals(VALID_USERNAME, "Username should be set correctly");

    // Step 5: Find and fill password
    var passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']", 5);
    CVerify.Object.isNotNull(passwordField, "Password field should be present");
    passwordField.setText(VALID_PASSWORD);

    // Step 6: Verify password was entered (value will be hidden)
    passwordField.Value.verifyEquals(VALID_PASSWORD, "Password should be set correctly");

    // Step 7: Submit login form
    var loginButton = getDriver().$("//button[@type='submit']", 5);
    loginButton.click();

    // Step 8: Verify successful navigation to dashboard
    var userDropdown = getDriver().$(".oxd-userdropdown", 10);
    userDropdown.Visible.verifyIsTrue("Should navigate to dashboard after successful login");
  }

  /**
   * Test 6: Verify login button properties and interactions Tests login button state and
   * clickability
   */
  @Test(description = "Verify login button is clickable and has correct properties")
  public void testLoginButtonProperties() {
    // Step 1: Navigate to login page
    open(ORANGEHRM_URL);

    // Step 2: Find login button
    var loginButton = getDriver().$("//button[@type='submit']", 10);
    CVerify.Object.isNotNull(loginButton, "Login button should be present");

    // Step 3: Verify button is visible
    loginButton.Visible.verifyIsTrue("Login button should be visible");

    // Step 4: Verify button is present in DOM
    loginButton.Present.verifyIsTrue("Login button should be present in DOM");

    // Step 5: Verify button text
    loginButton.Text.verifyContains("Login", "Button should contain 'Login' text");
  }

  /**
   * Test 7: Verify multiple invalid login attempts Tests that multiple failed attempts continue to
   * show error
   */
  @Test(description = "Verify multiple invalid login attempts show consistent errors")
  public void testMultipleInvalidLoginAttempts() {
    // Step 1: Navigate to login page
    open(ORANGEHRM_URL);

    // Attempt 1: First invalid login
    var usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']", 5);
    usernameField.setText("wronguser1");

    var passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']", 5);
    passwordField.setText("wrongpass1");

    var loginButton = getDriver().$("//button[@type='submit']", 5);
    loginButton.click();

    // Verify first error
    var errorAlert = getDriver().$(".oxd-alert", 10);
    errorAlert.Visible.verifyIsTrue("First error should be visible");
    errorAlert.Text.verifyEquals("Invalid credentials", "Should show invalid credentials message");

    // Attempt 2: Second invalid login (fields should still be on page)
    usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']", 5);
    usernameField.setText("wronguser2");

    passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']", 5);
    passwordField.setText("wrongpass2");

    loginButton = getDriver().$("//button[@type='submit']", 5);
    loginButton.click();

    // Verify second error
    errorAlert = getDriver().$(".oxd-alert", 10);
    errorAlert.Visible.verifyIsTrue("Second error should be visible");
    errorAlert.Text.verifyEquals(
        "Invalid credentials", "Should show invalid credentials message again");
  }

  /** Test 8: Verify field clearing functionality Tests that fields can be cleared and refilled */
  @Test(description = "Verify login fields can be cleared and refilled")
  public void testFieldClearingFunctionality() {
    // Step 1: Navigate to login page
    open(ORANGEHRM_URL);

    // Step 2: Enter initial values
    var usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']", 5);
    usernameField.setText("initialUser");
    usernameField.Value.verifyEquals("initialUser", "Initial username should be set");

    // Step 3: Clear and re-enter username
    usernameField.clear();
    usernameField.Value.verifyIsBlank("Username should be empty after clear");

    usernameField.setText(VALID_USERNAME);
    usernameField.Value.verifyEquals(VALID_USERNAME, "Username should be updated");

    // Step 4: Do the same for password
    var passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']", 5);
    passwordField.setText("wrongpassword");
    passwordField.clear();
    passwordField.Value.verifyIsBlank("Password should be empty after clear");

    passwordField.setText(VALID_PASSWORD);
    passwordField.Value.verifyEquals(VALID_PASSWORD, "Password should be updated");
  }

  /**
   * Test 9: Verify login form field attributes Tests that form fields have correct HTML attributes
   */
  @Test(description = "Verify login form fields have correct attributes")
  public void testLoginFormFieldAttributes() {
    // Step 1: Navigate to login page
    open(ORANGEHRM_URL);

    // Step 2: Check username field attributes
    var usernameField = getDriver().$(USERNAME_FIELD, "[name='" + USERNAME_FIELD + "']", 5);
    CVerify.Object.isNotNull(usernameField, "Username field should be present");
    usernameField.Present.verifyIsTrue("Username field should be in DOM");
    usernameField.Visible.verifyIsTrue("Username field should be visible");

    // Step 3: Check password field attributes
    var passwordField = getDriver().$(PASSWORD_FIELD, "[name='" + PASSWORD_FIELD + "']", 5);
    CVerify.Object.isNotNull(passwordField, "Password field should be present");
    passwordField.Present.verifyIsTrue("Password field should be in DOM");
    passwordField.Visible.verifyIsTrue("Password field should be visible");

    // Step 4: Verify both fields are enabled
    usernameField.Enabled.verifyIsTrue("Username field should be enabled");
    passwordField.Enabled.verifyIsTrue("Password field should be enabled");
  }

  /**
   * Test 10: Verify page title and branding Tests that the login page displays correct branding
   * information
   */
  @Test(description = "Verify login page displays OrangeHRM branding")
  public void testLoginPageBrandingAndTitle() {
    // Step 1: Navigate to login page
    open(ORANGEHRM_URL);

    // Step 2: Verify URL contains OrangeHRM
    getDriver().Url.verifyContains("orangehrmlive", "URL should contain orangehrmlive domain");

    // Step 3: Find and verify branding information in login slot
    var loginSlot = getDriver().$(".orangehrm-login-slot", 5);
    CVerify.Object.isNotNull(loginSlot, "Login information section should be present");

    // Step 4: Verify OrangeHRM branding text
    loginSlot.Text.verifyContains("OrangeHRM", "Page should contain OrangeHRM branding");
    loginSlot.Text.verifyContains("2005 - 2025", "Page should contain copyright year range");
  }

  @Override
  protected CDriver buildDriver(CDriverSession session) {
    return new CDriver(session);
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod(ITestResult result) {
    try {
      // Take screenshot if test failed
      takeScreenShotIfFail(result);
    } finally {
      // Always quit the driver after each test method to prevent browser leaks
      quit();
    }
  }
}

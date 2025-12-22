package org.catools.web.forms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.drivers.CDriver;
import org.catools.web.factory.CWebElementFactory;
import org.catools.web.pages.CWebComponent;

/**
 * Abstract base class for web form automation that provides a foundation for implementing
 * form-specific page objects in web automation frameworks. This class extends the capabilities of
 * {@link CWebComponent} by automatically initializing web elements using the {@link
 * CWebElementFactory}, making it ideal for form-based user interactions.
 *
 * <p>CWebForm serves as a structured approach to handling web forms by encapsulating form-specific
 * logic and element management. It automatically handles element initialization through the factory
 * pattern, reducing boilerplate code and ensuring consistent element management across form
 * implementations.
 *
 * <h3>Key Features:</h3>
 *
 * <ul>
 *   <li>Automatic web element initialization via {@link CWebElementFactory}
 *   <li>Type-safe driver integration with generic constraints
 *   <li>Built-in logging support through SLF4J
 *   <li>Consistent form component architecture
 *   <li>Immutable driver reference for thread safety
 * </ul>
 *
 * <h3>Usage Pattern:</h3>
 *
 * <p>Extend this class to create specific form implementations. The factory will automatically
 * initialize any fields annotated with web element annotations (such as @FindBy).
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>{@code
 * // Create a login form implementation
 * public class LoginForm extends CWebForm<CDriver> {
 *     @FindBy(id = "username")
 *     private CWebElement usernameField;
 *
 *     @FindBy(id = "password")
 *     private CWebElement passwordField;
 *
 *     @FindBy(css = "button[type='submit']")
 *     private CWebElement submitButton;
 *
 *     public LoginForm(CDriver driver) {
 *         super(driver);
 *     }
 *
 *     public void login(String username, String password) {
 *         usernameField.type(username);
 *         passwordField.type(password);
 *         submitButton.click();
 *     }
 * }
 *
 * // Using the form in tests
 * CDriver driver = new CDriver(session);
 * driver.open("https://example.com/login");
 *
 * LoginForm loginForm = new LoginForm(driver);
 * loginForm.login("testuser", "password123");
 *
 * // Create a registration form with validation
 * public class RegistrationForm extends CWebForm<CDriver> {
 *     @FindBy(name = "email")
 *     private CWebElement emailField;
 *
 *     @FindBy(name = "confirmEmail")
 *     private CWebElement confirmEmailField;
 *
 *     public RegistrationForm(CDriver driver) {
 *         super(driver);
 *     }
 *
 *     public boolean isEmailValid() {
 *         return emailField.getAttribute("class").contains("valid");
 *     }
 *
 *     public void fillEmailFields(String email) {
 *         emailField.type(email);
 *         confirmEmailField.type(email);
 *     }
 * }
 * }</pre>
 *
 * @param <DR> the type of driver extending {@link CDriver} that will be used for form automation
 * @author CATools Team
 * @see CWebComponent
 * @see CWebElementFactory
 * @see CDriver
 * @since 1.0
 */
@Slf4j
public abstract class CWebForm<DR extends CDriver> implements CWebComponent {

  /**
   * The driver instance used for web automation within this form. This field is immutable after
   * construction to ensure thread safety and prevent accidental driver reassignment during form
   * operations.
   *
   * <p>The driver reference is automatically set during construction and provides access to all
   * browser automation capabilities needed for form interactions.
   */
  @Getter
  @Setter(AccessLevel.NONE)
  protected final DR driver;

  /**
   * Constructs a new CWebForm instance with the specified driver and automatically initializes all
   * web elements using the {@link CWebElementFactory}.
   *
   * <p>This constructor performs the following operations:
   *
   * <ol>
   *   <li>Stores the provided driver instance immutably
   *   <li>Invokes {@link CWebElementFactory#initElements(CWebComponent)} to initialize all fields
   *       annotated with web element locator annotations
   * </ol>
   *
   * <p>Element initialization happens automatically, so any fields in the extending class that are
   * annotated with @FindBy, @FindBys, or similar annotations will be properly initialized and ready
   * for use immediately after construction.
   *
   * @param driver the driver instance to use for form automation operations, must not be null
   * @throws IllegalArgumentException if the driver parameter is null
   * @throws RuntimeException if element initialization fails due to invalid locator annotations or
   *     other factory-related issues
   * @example
   *     <pre>{@code
   * // Basic form construction
   * public class ContactForm extends CWebForm<CDriver> {
   *     @FindBy(id = "name")
   *     private CWebElement nameField;  // Automatically initialized
   *
   *     @FindBy(id = "email")
   *     private CWebElement emailField; // Automatically initialized
   *
   *     public ContactForm(CDriver driver) {
   *         super(driver); // Elements are ready to use after this call
   *     }
   *
   *     public void fillForm(String name, String email) {
   *         // Elements are already initialized and ready to use
   *         nameField.type(name);
   *         emailField.type(email);
   *     }
   * }
   *
   * // Using with different driver types
   * CDriver basicDriver = new CDriver(session);
   * ContactForm form1 = new ContactForm(basicDriver);
   *
   * // With custom driver extension
   * MyCustomDriver customDriver = new MyCustomDriver(session);
   * ContactForm form2 = new ContactForm(customDriver);
   *
   * // Form is immediately ready for use
   * form1.fillForm("John Doe", "john@example.com");
   * }</pre>
   *
   * @see CWebElementFactory#initElements(CWebComponent)
   * @see CWebComponent#getElementEngine()
   */
  public CWebForm(DR driver) {
    this.driver = driver;
    CWebElementFactory.initElements(this);
  }
}

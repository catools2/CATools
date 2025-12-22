package org.catools.web.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.catools.common.utils.CStringUtil;

/**
 * Custom annotation that extends Selenium's functionality with additional features for enhanced web
 * element location and management.
 *
 * <p>This annotation provides enhanced capabilities over standard Selenium FindBy including:
 *
 * <ul>
 *   <li>Custom element naming for better identification and reporting
 *   <li>Configurable wait timeouts for element availability
 *   <li>Integration with custom web driver factory patterns
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * public class LoginPage {
 *
 *     @CFindBy(
 *         findBy = @FindBy(id = "username"),
 *         name = "Username Input Field",
 *         waitInSeconds = 10
 *     )
 *     private WebElement usernameField;
 *
 *     @CFindBy(
 *         findBy = @FindBy(xpath = "//button[@type='submit']"),
 *         name = "Login Button"
 *     )
 *     private WebElement loginButton;
 *
 *     @CFindBy(findBy = @FindBy(className = "error-message"))
 *     private WebElement errorMessage;
 * }
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface CFindBy {

  /**
   * The locator string to use for Playwright. Prefer CSS selectors or XPath (XPath should start
   * with 'xpath=' to be distinguished, e.g. "xpath=//div[@id='x']"). If empty, the factory will
   * fall back to using the field name as id or name.
   *
   * <p>Examples: - CSS: "div.content > button.submit" - XPath: "//form//input[@name='email']"
   */
  String locator() default CStringUtil.EMPTY;

  /**
   * Optional custom name for the web element used for identification and reporting purposes. When
   * specified, this name can be used in logs, error messages, and test reports to provide more
   * meaningful descriptions of elements.
   *
   * <p>If not provided (empty string), the element will be identified by its locator strategy.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * @CFindBy(
   *     findBy = @FindBy(id = "user-email"),
   *     name = "User Email Input"
   * )
   *
   * @CFindBy(
   *     findBy = @FindBy(xpath = "//button[contains(text(), 'Save')]"),
   *     name = "Save Changes Button"
   * )
   * }</pre>
   *
   * @return the custom name for the element, or empty string if not specified
   */
  String name() default CStringUtil.EMPTY;

  /**
   * Optional wait timeout in seconds for the element to become available.
   *
   * @return the wait timeout in seconds, or -1 to use default timeout
   */
  int waitInSeconds() default -1;
}

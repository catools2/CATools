package org.catools.web.factory;

import org.openqa.selenium.support.FindBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation that extends Selenium's {@link FindBy} functionality with additional features
 * for enhanced web element location and management.
 * 
 * <p>This annotation provides enhanced capabilities over standard Selenium FindBy including:
 * <ul>
 *   <li>Custom element naming for better identification and reporting</li>
 *   <li>Configurable wait timeouts for element availability</li>
 *   <li>Integration with custom web driver factory patterns</li>
 * </ul>
 * 
 * <p>Example usage:
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
 * @see FindBy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface CFindBy {
  
  /**
   * The standard Selenium {@link FindBy} annotation that defines how to locate the web element.
   * This parameter is required and specifies the locator strategy (id, name, xpath, css, etc.).
   * 
   * <p>Examples:
   * <pre>{@code
   * // By ID
   * @CFindBy(findBy = @FindBy(id = "submit-btn"))
   * 
   * // By XPath
   * @CFindBy(findBy = @FindBy(xpath = "//div[@class='container']/input"))
   * 
   * // By CSS Selector
   * @CFindBy(findBy = @FindBy(css = "input[type='email']"))
   * 
   * // By Class Name
   * @CFindBy(findBy = @FindBy(className = "form-control"))
   * 
   * // By Name attribute
   * @CFindBy(findBy = @FindBy(name = "password"))
   * }</pre>
   * 
   * @return the FindBy annotation containing the element locator information
   */
  FindBy findBy();

  /**
   * Optional custom name for the web element used for identification and reporting purposes.
   * When specified, this name can be used in logs, error messages, and test reports
   * to provide more meaningful descriptions of elements.
   * 
   * <p>If not provided (empty string), the element will be identified by its locator strategy.
   * 
   * <p>Examples:
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
  String name() default "";

  /**
   * Optional wait timeout in seconds for the element to become available.
   * Specifies how long the driver should wait for the element to be present/visible
   * before timing out.
   * 
   * <p>Values:
   * <ul>
   *   <li><b>-1</b> (default): Use the system default wait timeout</li>
   *   <li><b>0</b>: No explicit wait, immediate check</li>
   *   <li><b>&gt; 0</b>: Wait for specified number of seconds</li>
   * </ul>
   * 
   * <p>Examples:
   * <pre>{@code
   * // Wait up to 15 seconds for a slow-loading element
   * @CFindBy(
   *     findBy = @FindBy(id = "async-content"),
   *     waitInSeconds = 15
   * )
   * 
   * // No wait for elements that should be immediately available
   * @CFindBy(
   *     findBy = @FindBy(tagName = "body"),
   *     waitInSeconds = 0
   * )
   * 
   * // Use system default wait time
   * @CFindBy(findBy = @FindBy(id = "standard-element"))
   * }</pre>
   * 
   * @return the wait timeout in seconds, or -1 to use default timeout
   */
  int waitInSeconds() default -1;
}

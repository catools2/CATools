package org.catools.web.factory;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.web.collections.CWebElements;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriverWaiter;
import org.catools.web.pages.CWebComponent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * A factory class for initializing and decorating web element fields in CWebComponent instances.
 * This factory provides functionality to automatically initialize CWebElement and CWebElements
 * fields with appropriate locators and configurations based on annotations.
 *
 * <p>The factory supports the following annotations:
 *
 * <ul>
 *   <li>{@link CFindBy} - For single CWebElement fields
 *   <li>{@link CFindBys} - For CWebElements collections
 * </ul>
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * public class LoginPage extends CWebComponent {
 *     @CFindBy(name = "username")
 *     private CWebElement usernameField;
 *
 *     @CFindBys(xpath = "//input[@type='button']")
 *     private CWebElements<LoginPage> buttons;
 *
 *     public LoginPage(Page driver) {
 *         super(driver);
 *         CWebElementFactory.initElements(this); // Initialize all annotated fields
 *     }
 * }
 * }</pre>
 *
 * @see CWebComponent
 * @see CWebElement
 * @see CWebElements
 * @see CFindBy
 * @see CFindBys
 */
public class CWebElementFactory {
  /**
   * Initializes all annotated web element fields in the given CWebComponent instance. This method
   * traverses the component's class hierarchy and decorates fields annotated with {@link CFindBy},
   * {@link CFindBys}.
   *
   * <p>The method processes fields as follows:
   *
   * <ul>
   *   <li>CWebElement fields are initialized with appropriate By locators and timeout settings
   *   <li>CWebElements fields are initialized with XPath locators and wait configurations
   *   <li>Fields without proper annotations are skipped
   *   <li>Invalid annotation combinations throw IllegalArgumentException
   * </ul>
   *
   * <p>Supported field types:
   *
   * <ul>
   *   <li>{@code CWebElement} - Single web element
   *   <li>{@code CWebElements} - Collection of web elements
   * </ul>
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * public class ShoppingCartPage extends CWebComponent {
   *     @CFindBy(id = "checkout-button", name = "Checkout Button", waitInSeconds = 10)
   *     private CWebElement checkoutButton;
   *
   *     @CFindBy(xpath = "//input[@name='quantity']")
   *     private CWebElement quantityInput;
   *
   *     @CFindBys(xpath = "//tr[contains(@class, 'cart-item')]",
   *               name = "Cart Items",
   *               waitForFirstElementInSecond = 5,
   *               waitForOtherElementInSecond = 2)
   *     private CWebElements<ShoppingCartPage> cartItems;
   *
   *     @FindBy(className = "price-total")
   *     private CWebElement totalPrice;
   *
   *     public ShoppingCartPage(Page driver) {
   *         super(driver);
   *         CWebElementFactory.initElements(this); // Initializes all annotated fields
   *     }
   * }
   * }</pre>
   *
   * <p>Default behavior for CWebElement without explicit locator:
   *
   * <pre>{@code
   * // This field will use ByIdOrName with field name "submitButton"
   * private CWebElement submitButton;
   * }</pre>
   *
   * @param component The CWebComponent instance whose fields should be initialized. Must not be
   *     null and should contain fields annotated with supported web element annotations.
   * @throws RuntimeException if field access fails during initialization
   * @throws IllegalArgumentException if invalid annotation combinations are detected:
   *     <ul>
   *       <li>Using both Selenium and CATools annotations on the same field
   *       <li>Using multiple CATools annotations on the same field
   *       <li>Using CFindBys on CWebElement fields (should use CFindBy or FindBy)
   *       <li>Using CFindBy/FindBy on CWebElements fields (should use CFindBys)
   *       <li>CFindBys annotation without xpath specified
   *     </ul>
   *
   * @see CFindBy
   * @see CFindBys
   * @see CWebElement
   * @see CWebElements
   * @see CWebComponent
   */
  public static void initElements(CWebComponent component) {
    Class<?> proxyIn = component.getClass();
    while (proxyIn != Object.class) {
      decorateFields(component, proxyIn);
      proxyIn = proxyIn.getSuperclass();
    }
  }

  private static void decorateFields(CWebComponent component, Class<?> proxyIn) {
    Field[] fields = proxyIn.getDeclaredFields();
    for (Field field : fields) {
      Object value = decorateField(component, field);
      if (value != null) {
        try {
          field.setAccessible(true);
          field.set(component, value);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private static Object decorateField(CWebComponent component, Field field) {
    if (!isDecoratable(field)) {
      return null;
    }

    assertValidAnnotations(field);

    if (CWebElement.class.isAssignableFrom(field.getType())) {
      return buildWebElement(component, field);
    } else if (CWebElements.class.isAssignableFrom(field.getType())) {
      return buildWebElements(component, field);
    } else {
      return null;
    }
  }

  private static boolean isDecoratable(Field field) {
    return field.getType().equals(CWebElement.class) || field.getType().equals(CWebElements.class);
  }

  private static CWebElement buildWebElement(CWebComponent component, Field field) {
    CFindBy cFindBYInfo = field.getAnnotation(CFindBy.class);

    String name = field.getName();
    int timeout = CDriverWaiter.DEFAULT_TIMEOUT;

    String locator;
    if (cFindBYInfo != null && StringUtils.isNotBlank(cFindBYInfo.locator())) {
      locator = cFindBYInfo.locator();
      if (StringUtils.isNotBlank(cFindBYInfo.name())) name = cFindBYInfo.name();
      if (cFindBYInfo.waitInSeconds() > 0) timeout = cFindBYInfo.waitInSeconds();
    } else {
      // fallback to id or name
      locator = "#" + field.getName();
    }

    return new CWebElement(name, component.getElementEngine(), locator, timeout);
  }

  private static CWebElements buildWebElements(CWebComponent component, Field field) {
    CFindBys elementInfo = field.getAnnotation(CFindBys.class);
    if (StringUtils.isBlank(elementInfo.xpath()))
      throw new IllegalArgumentException("CWebElements requires xpath (String) to find element.");

    String name = StringUtils.defaultString(elementInfo.name(), field.getName());

    if (elementInfo.waitForFirstElementInSecond() < 0)
      return new CWebElements(name, component.getElementEngine(), elementInfo.xpath());

    if (elementInfo.waitForOtherElementInSecond() < 0)
      return new CWebElements(
          name,
          component.getElementEngine(),
          elementInfo.xpath(),
          elementInfo.waitForFirstElementInSecond());

    return new CWebElements(
        name,
        component.getElementEngine(),
        elementInfo.xpath(),
        elementInfo.waitForFirstElementInSecond(),
        elementInfo.waitForOtherElementInSecond());
  }

  private static void assertValidAnnotations(Field field) {
    CList<Annotation> seleniumAnnotatoins = CList.of();

    if (seleniumAnnotatoins.getAll(Objects::nonNull).isNotEmpty()) {
      throw new IllegalArgumentException(
          "You should only use CFindBys or CFindBy or FindBy annotation! (Attention to first Letter 'C')");
    }

    CFindBys cFindBys = field.getAnnotation(CFindBys.class);
    CList<Annotation> catoolsAnnotations = CList.of(cFindBys, field.getAnnotation(CFindBy.class));

    if (catoolsAnnotations.getAll(Objects::nonNull).size() > 1) {
      throw new IllegalArgumentException(
          "You should use only one of CFindBys or CFindBy or FindBy annotation!");
    }

    if (CWebElements.class.isAssignableFrom(field.getType()) && cFindBys == null) {
      throw new IllegalArgumentException("You should use CFindBys for CWebElements elements!");
    }

    if (CWebElement.class.isAssignableFrom(field.getType()) && cFindBys != null) {
      throw new IllegalArgumentException(
          "You should use CFindBy or FindBy for CWebElement elements!");
    }
  }
}

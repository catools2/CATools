package org.catools.web.controls;

import lombok.Getter;
import lombok.Setter;
import org.catools.common.extensions.types.CDynamicBooleanExtension;
import org.catools.common.extensions.types.CDynamicNumberExtension;
import org.catools.common.extensions.types.CDynamicStringExtension;
import org.catools.media.model.CScreenShot;
import org.catools.web.drivers.CDriver;
import org.catools.web.selectors.CBy;

import java.awt.image.BufferedImage;

/**
 * A web element wrapper that provides enhanced functionality for interacting with web elements
 * in automated testing scenarios. This class extends basic Selenium WebElement functionality
 * with additional features like dynamic property extensions, enhanced waiting mechanisms,
 * and comprehensive action methods.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create a web element
 * CWebElement<CDriver> loginButton = new CWebElement<>("Login Button", driver, By.id("login-btn"));
 *
 * // Use property extensions for verification
 * loginButton.Visible.verifyTrue("Login button should be visible");
 * loginButton.Text.verifyEquals("Login", "Button text should be 'Login'");
 *
 * // Perform actions
 * loginButton.click();
 * }</pre>
 *
 * @param <DR> the driver type that extends CDriver
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 */
public class CWebElement<DR extends CDriver> implements CWebElementActions<DR> {

  /**
   * The web driver instance used for interacting with the web element.
   */
  @Getter
  @Setter
  protected DR driver;

  /**
   * The default wait timeout in seconds for this element's operations.
   */
  @Getter
  @Setter
  protected int waitSec;

  /**
   * The descriptive name of this web element for logging and reporting purposes.
   */
  @Getter
  @Setter
  protected String name;

  /**
   * The locator strategy used to find this element on the web page.
   */
  @Getter
  @Setter
  protected String locator;

  /**
   * Default constructor for dependency injection frameworks.
   */
  public CWebElement() {
    // for DI frameworks
  }

  /**
   * Creates a new CWebElement with the default timeout.
   *
   * @param name    the descriptive name for this element
   * @param driver  the web driver instance
   * @param locator the locator strategy to find the element
   *
   *                <p>Example:</p>
   *                <pre>{@code
   *                                              CWebElement<CDriver> submitBtn = new CWebElement<>("Submit Button", driver, By.id("submit"));
   *                                              }</pre>
   */
  public CWebElement(String name, DR driver, String locator) {
    this(name, driver, locator, CDriver.DEFAULT_TIMEOUT);
  }

  /**
   * Creates a new CWebElement with the default timeout.
   *
   * @param name    the descriptive name for this element
   * @param driver  the web driver instance
   * @param locator the locator strategy to find the element
   *
   *  <p>Example:</p>
   *  <pre>{@code
   *  CWebElement<CDriver> cancelLink = new CWebElement<>("Cancel Link", driver, By.linkText("Cancel"));
   *  }</pre>
   */
  public CWebElement(String name, DR driver, CBy locator) {
    this(name, driver, locator.getSelector(), CDriver.DEFAULT_TIMEOUT);
  }

  /**
   * Creates a new CWebElement with a custom timeout.
   *
   * @param name    the descriptive name for this element
   * @param driver  the web driver instance
   * @param locator the locator strategy to find the element
   * @param waitSec the custom timeout in seconds for this element's operations
   *
   * <p>Example:</p>
   * <pre>{@code
   * // Create element with 15-second timeout
   * CWebElement<CDriver> dynamicElement = new CWexwbElement<>("Dynamic Element", driver, By.xpath("//div[@class='dynamic']"), 15);
   * }</pre>
   */
  public CWebElement(String name, DR driver, CBy locator, int waitSec) {
    this(name, driver, locator.getSelector(), waitSec);
  }

  /**
   * Creates a new CWebElement with a custom timeout.
   *
   * @param name    the descriptive name for this element
   * @param driver  the web driver instance
   * @param locator the locator strategy to find the element
   * @param waitSec the custom timeout in seconds for this element's operations
   *
   * <p>Example:</p>
   * <pre>{@code
   * // Create element with 10-second timeout
   * CWebElement<CDriver> slowElement = new CWebElement<>("Slow Element", driver, By.className("slow"), 10);
   * }</pre>
   */
  public CWebElement(String name, DR driver, String locator, int waitSec) {
    super();
    this.name = name;
    this.driver = driver;
    this.locator = locator;
    this.waitSec = waitSec;
  }

  // Control Extension
  /**
   * Dynamic extension for retrieving the element's offset position.
   * Provides verification and wait capabilities for the element's offsetTop value.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify offset is greater than 100
   * element.Offset.verifyGreaterThan(100, "Element should be positioned below 100px");
   *
   * // Wait until offset changes
   * element.Offset.waitUntilNotEquals(0, 5, "Wait for element to move from top");
   * }</pre>
   */
  public final CDynamicNumberExtension<Integer> Offset = new CDynamicNumberExtension<>() {
    @Override
    public Integer _get() {
      return getOffset(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Offset";
    }
  };

  /**
   * Dynamic extension for checking element staleness.
   * Provides verification and wait capabilities for detecting stale element references.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify element is not stale
   * element.Staleness.verifyFalse("Element should not be stale");
   *
   * // Wait until element becomes stale (after page refresh)
   * element.Staleness.waitUntilTrue(10, "Wait for element to become stale");
   * }</pre>
   */
  public final CDynamicBooleanExtension Staleness = new CDynamicBooleanExtension() {
    @Override
    public Boolean _get() {
      return isStaleness(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Staleness";
    }
  };

  /**
   * Dynamic extension for checking element presence in the DOM.
   * Provides verification and wait capabilities for element existence.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify element is present
   * element.Present.verifyTrue("Element should be present in DOM");
   *
   * // Wait until element appears
   * element.Present.waitUntilTrue(15, "Wait for element to appear");
   * }</pre>
   */
  public final CDynamicBooleanExtension Present = new CDynamicBooleanExtension() {
    @Override
    public Boolean _get() {
      return isPresent(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Presence";
    }
  };

  /**
   * Dynamic extension for checking element visibility.
   * Provides verification and wait capabilities for element display state.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify element is visible
   * element.Visible.verifyTrue("Element should be visible to user");
   *
   * // Wait until element becomes visible
   * element.Visible.waitUntilTrue(10, "Wait for element to become visible");
   *
   * // Wait until element is hidden
   * element.Visible.waitUntilFalse(5, "Wait for element to be hidden");
   * }</pre>
   */
  public final CDynamicBooleanExtension Visible = new CDynamicBooleanExtension() {
    @Override
    public Boolean _get() {
      return isVisible(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Visibility";
    }
  };

  /**
   * Dynamic extension for checking element enabled state.
   * Provides verification and wait capabilities for element interactability.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify element is enabled
   * element.Enabled.verifyTrue("Element should be enabled for interaction");
   *
   * // Wait until element becomes enabled
   * element.Enabled.waitUntilTrue(8, "Wait for element to be enabled");
   * }</pre>
   */
  public final CDynamicBooleanExtension Enabled = new CDynamicBooleanExtension() {
    @Override
    public Boolean _get() {
      return isEnabled(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Enable State";
    }
  };

  /**
   * Dynamic extension for retrieving element text content.
   * Provides verification and wait capabilities for the element's visible text.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify text content
   * element.Text.verifyEquals("Expected Text", "Text should match expected value");
   *
   * // Verify text contains substring
   * element.Text.verifyContains("Success", "Text should contain success message");
   *
   * // Wait until text changes
   * element.Text.waitUntilNotEquals("Loading...", 10, "Wait for loading to complete");
   * }</pre>
   */
  public final CDynamicStringExtension Text = new CDynamicStringExtension() {
    @Override
    public String _get() {
      return getText(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Text";
    }
  };

  /**
   * Dynamic extension for retrieving element value attribute.
   * Provides verification and wait capabilities for form element values.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify input value
   * inputField.Value.verifyEquals("expected@email.com", "Email field should have correct value");
   *
   * // Wait until value is not empty
   * inputField.Value.waitUntilNotEquals("", 5, "Wait for value to be entered");
   * }</pre>
   */
  public final CDynamicStringExtension Value = new CDynamicStringExtension() {
    @Override
    public String _get() {
      return getValue(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Value";
    }
  };

  /**
   * Dynamic extension for retrieving element innerHTML content.
   * Provides verification and wait capabilities for the element's inner HTML.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify HTML contains specific tags
   * element.InnerHTML.verifyContains("<span>", "HTML should contain span element");
   *
   * // Wait until HTML content loads
   * element.InnerHTML.waitUntilNotEquals("", 10, "Wait for content to load");
   * }</pre>
   */
  public final CDynamicStringExtension InnerHTML = new CDynamicStringExtension() {
    @Override
    public String _get() {
      return getInnerHTML(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Inner HTML";
    }
  };

  /**
   * Creates a dynamic extension for retrieving CSS property values.
   * Provides verification and wait capabilities for specific CSS properties.
   *
   * @param cssKey the CSS property name to retrieve
   * @return a CDynamicStringExtension for the specified CSS property
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify font size
   * element.Css("font-size").verifyEquals("16px", "Font size should be 16px");
   *
   * // Verify display property
   * element.Css("display").verifyEquals("block", "Element should be displayed as block");
   *
   * // Wait until opacity changes
   * element.Css("opacity").waitUntilNotEquals("0", 5, "Wait for element to become opaque");
   * }</pre>
   */
  public final CDynamicStringExtension Css(final String cssKey) {
    return new CDynamicStringExtension() {
      @Override
      public String _get() {
        return getCss(cssKey, 0);
      }

      @Override
      public int getDefaultWaitInSeconds() {
        return waitSec;
      }

      @Override
      public String getVerifyMessagePrefix() {
        return name + " " + cssKey + " CSS value";
      }
    };
  }

  /**
   * Creates a dynamic extension for retrieving HTML attribute values.
   * Provides verification and wait capabilities for specific element attributes.
   *
   * @param attribute the attribute name to retrieve
   * @return a CDynamicStringExtension for the specified attribute
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify class attribute contains specific class
   * element.Attribute("class").verifyContains("active", "Element should have active class");
   *
   * // Verify id attribute
   * element.Attribute("id").verifyEquals("submit-btn", "Element should have correct ID");
   *
   * // Wait until data attribute is set
   * element.Attribute("data-loaded").waitUntilEquals("true", 10, "Wait for data to load");
   * }</pre>
   */
  public final CDynamicStringExtension Attribute(final String attribute) {
    return new CDynamicStringExtension() {
      @Override
      public String _get() {
        return getAttribute(attribute, 0);
      }

      @Override
      public int getDefaultWaitInSeconds() {
        return waitSec;
      }

      @Override
      public String getVerifyMessagePrefix() {
        return name + " " + attribute + " Attribute value";
      }
    };
  }

  /**
   * Creates a dynamic extension for retrieving the element's ARIA role.
   * Provides verification and wait capabilities for accessibility role attributes.
   *
   * @return a CDynamicStringExtension for the ARIA role
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify button role
   * buttonElement.AriaRole().verifyEquals("button", "Element should have button role");
   *
   * // Verify dialog role
   * modalElement.AriaRole().verifyEquals("dialog", "Modal should have dialog role");
   * }</pre>
   */
  public final CDynamicStringExtension AriaRole() {
    return new CDynamicStringExtension() {
      @Override
      public String _get() {
        return getAriaRole(0);
      }

      @Override
      public int getDefaultWaitInSeconds() {
        return waitSec;
      }

      @Override
      public String getVerifyMessagePrefix() {
        return name + " AriaRole value";
      }
    };
  }

  /**
   * Screenshot capability for capturing images of this web element.
   * Provides functionality to take screenshots and perform image-based verifications.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify screenshot is not null
   * element.ScreenShot.verifyIsNotNull("Screenshot should be captured successfully");
   *
   * // Save screenshot for visual comparison
   * CImageUtil.saveImage(element.ScreenShot.get(), "element-screenshot.png");
   * }</pre>
   */
  public final CScreenShot ScreenShot = new CScreenShot() {
    @Override
    public BufferedImage _get() {
      return getScreenShot(0);
    }

    @Override
    public int getDefaultWaitIntervalInMilliSeconds() {
      return 50;
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return CDriver.DEFAULT_TIMEOUT;
    }

    @Override
    public boolean withWaiter() {
      return true;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Screen Shot";
    }
  };

  /**
   * Dynamic extension for checking element selection state.
   * Provides verification and wait capabilities for form element selection (checkboxes, radio buttons).
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify checkbox is selected
   * checkbox.Selected.verifyTrue("Checkbox should be selected");
   *
   * // Wait until radio button is selected
   * radioButton.Selected.waitUntilTrue(5, "Wait for radio button selection");
   *
   * // Verify option is not selected
   * optionElement.Selected.verifyFalse("Option should not be selected initially");
   * }</pre>
   */
  public final CDynamicBooleanExtension Selected = new CDynamicBooleanExtension() {
    @Override
    public Boolean _get() {
      return isSelected(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Selected State";
    }
  };

  /**
   * Dynamic extension for checking element clickability.
   * Provides verification and wait capabilities for element interaction readiness.
   *
   * <p>Example usage:</p>
   * <pre>{@code
   * // Verify element is clickable
   * element.Clickable.verifyTrue("Element should be clickable");
   *
   * // Wait until button becomes clickable
   * submitButton.Clickable.waitUntilTrue(10, "Wait for submit button to be clickable");
   *
   * // Verify element is not clickable (disabled state)
   * disabledButton.Clickable.verifyFalse("Disabled button should not be clickable");
   * }</pre>
   */
  public final CDynamicBooleanExtension Clickable = new CDynamicBooleanExtension() {
    @Override
    public Boolean _get() {
      return isClickable(0);
    }

    @Override
    public int getDefaultWaitInSeconds() {
      return waitSec;
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Clickable";
    }
  };
}

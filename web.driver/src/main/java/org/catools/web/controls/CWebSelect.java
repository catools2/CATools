package org.catools.web.controls;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.extensions.types.CDynamicStringExtension;
import org.catools.common.extensions.types.interfaces.CDynamicCollectionExtension;
import org.catools.common.extensions.verify.CVerify;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverEngine;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CWebSelect is a specialized web control for handling HTML select elements (dropdowns).
 * It extends CWebElement and provides comprehensive functionality for interacting with
 * select elements including selecting options by text, value, index, or pattern matching.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Multiple selection methods (by text, value, index, pattern)</li>
 *   <li>Retrieval of selected options and all available options</li>
 *   <li>Dynamic extensions for property-based access</li>
 *   <li>Built-in waiting mechanisms for element stability</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create a select element
 * CWebSelect<CDriver> countrySelect = new CWebSelect<>("Country", driver, By.id("country-select"));
 *
 * // Select by visible text
 * countrySelect.selectByText("United States");
 *
 * // Select by value attribute
 * countrySelect.selectByValue("US");
 *
 * // Get selected text
 * String selectedText = countrySelect.getSelectedText();
 *
 * // Get all available options
 * CList<String> allOptions = countrySelect.getTexts();
 * }</pre>
 *
 * @param <DR> the driver type that extends CDriver
 * @author CATools
 * @version 1.0
 */
@Slf4j
public class CWebSelect<DR extends CDriver> extends CWebElement<DR> {

  /**
   * Constructs a CWebSelect with the specified name, driver, and locator.
   * Uses the default wait time configured in the parent class.
   *
   * @param name    the name identifier for this select element
   * @param driver  the web driver instance
   * @param locator the By locator to find the select element
   * @example <pre>{@code
   * CWebSelect<CDriver> select = new CWebSelect<>("Status", driver, By.id("status-dropdown"));
   * }</pre>
   */
  public CWebSelect(String name, DR driver, String locator) {
    super(name, driver, locator);
  }

  /**
   * Constructs a CWebSelect with the specified name, driver, locator, and custom wait time.
   *
   * @param name    the name identifier for this select element
   * @param driver  the web driver instance
   * @param locator the By locator to find the select element
   * @param waitSec the wait time in seconds for element operations
   * @example <pre>{@code
   * // Create select with 10 second wait time
   * CWebSelect<CDriver> select = new CWebSelect<>("Status", driver, By.id("status-dropdown"), 10);
   * }</pre>
   */
  public CWebSelect(String name, DR driver, String locator, int waitSec) {
    super(name, driver, locator, waitSec);
  }

  // Extensions

  /**
   * Dynamic extension for accessing the currently selected option's text.
   * Provides verification capabilities and automatic retrieval of the selected text.
   *
   * @example <pre>{@code
   * // Verify selected text
   * countrySelect.SelectedText.shouldBe("United States");
   *
   * // Get selected text value
   * String selectedText = countrySelect.SelectedText.get();
   * }</pre>
   */
  public CDynamicStringExtension SelectedText = new CDynamicStringExtension() {
    @Override
    public String _get() {
      return getSelectedText();
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Selected Text";
    }
  };

  /**
   * Dynamic extension for accessing the currently selected option's value attribute.
   * Provides verification capabilities and automatic retrieval of the selected value.
   *
   * @example <pre>{@code
   * // Verify selected value
   * countrySelect.SelectedValue.shouldBe("US");
   *
   * // Get selected value
   * String selectedValue = countrySelect.SelectedValue.get();
   * }</pre>
   */
  public CDynamicStringExtension SelectedValue = new CDynamicStringExtension() {
    @Override
    public String _get() {
      return getSelectedValue();
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Selected Value";
    }
  };

  /**
   * Dynamic extension for accessing all available option values in the select element.
   * Provides verification capabilities and automatic retrieval of all option values.
   *
   * @example <pre>{@code
   * // Verify that certain values are available
   * countrySelect.Values.shouldContain("US", "CA", "UK");
   *
   * // Get all values
   * Collection<String> allValues = countrySelect.Values.get();
   * }</pre>
   */
  public CDynamicCollectionExtension<String> Values = new CDynamicCollectionExtension<>() {
    @Override
    public Collection<String> _get() {
      return getValues();
    }

    @Override
    public int hashCode() {
      return _get().hashCode();
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Values";
    }
  };

  /**
   * Dynamic extension for accessing all available option texts in the select element.
   * Provides verification capabilities and automatic retrieval of all option texts.
   *
   * @example <pre>{@code
   * // Verify that certain texts are available
   * countrySelect.Texts.shouldContain("United States", "Canada", "United Kingdom");
   *
   * // Get all texts
   * Collection<String> allTexts = countrySelect.Texts.get();
   * }</pre>
   */
  public CDynamicCollectionExtension<String> Texts = new CDynamicCollectionExtension<>() {
    @Override
    public Collection<String> _get() {
      return getTexts();
    }

    @Override
    public int hashCode() {
      return _get().hashCode();
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name + " Texts";
    }
  };

  /**
   * Dynamic extension for accessing all currently selected option texts (for multi-select elements).
   * Provides verification capabilities and automatic retrieval of selected option texts.
   *
   * @example <pre>{@code
   * // For multi-select elements
   * multiSelect.SelectedTexts.shouldContain("Option 1", "Option 3");
   *
   * // Get all selected texts
   * Collection<String> selectedTexts = multiSelect.SelectedTexts.get();
   * }</pre>
   */
  public CDynamicCollectionExtension<String> SelectedTexts = new CDynamicCollectionExtension<>() {
    @Override
    public Collection<String> _get() {
      return getSelectedTexts();
    }

    @Override
    public int hashCode() {
      return _get().hashCode();
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name;
    }
  };

  /**
   * Dynamic extension for accessing all currently selected option values (for multi-select elements).
   * Provides verification capabilities and automatic retrieval of selected option values.
   *
   * @example <pre>{@code
   * // For multi-select elements
   * multiSelect.SelectedValues.shouldContain("val1", "val3");
   *
   * // Get all selected values
   * Collection<String> selectedValues = multiSelect.SelectedValues.get();
   * }</pre>
   */
  public CDynamicCollectionExtension<String> SelectedValues = new CDynamicCollectionExtension<>() {
    @Override
    public Collection<String> _get() {
      return getSelectedValues();
    }

    @Override
    public int hashCode() {
      return _get().hashCode();
    }

    @Override
    public String getVerifyMessagePrefix() {
      return name;
    }
  };

  // Getters

  /**
   * Gets all currently selected option texts using the default wait time.
   * For single-select elements, returns a list with one item.
   * For multi-select elements, returns all selected options.
   *
   * @return CList of selected option texts
   * @example <pre>{@code
   * CList<String> selectedTexts = countrySelect.getSelectedTexts();
   * System.out.println("Selected: " + selectedTexts.get(0));
   * }</pre>
   */
  public CList<String> getSelectedTexts() {
    return getSelectedTexts(0);
  }

  /**
   * Gets all currently selected option texts with a custom wait time.
   *
   * @param waitSec maximum time to wait for the element in seconds
   * @return CList of selected option texts
   * @example <pre>{@code
   * // Wait up to 5 seconds for the element
   * CList<String> selectedTexts = countrySelect.getSelectedTexts(5);
   * }</pre>
   */
  public CList<String> getSelectedTexts(int waitSec) {
    return waitUntil("Get Selected Texts", waitSec, engine ->
        new CList<>(engine.getAllSelectedOptions(getLocator()))
            .mapToList(e -> e.text().trim()));
  }

  /**
   * Gets all currently selected option values using the default wait time.
   *
   * @return CList of selected option values
   * @example <pre>{@code
   * CList<String> selectedValues = countrySelect.getSelectedValues();
   * System.out.println("Selected value: " + selectedValues.get(0));
   * }</pre>
   */
  public CList<String> getSelectedValues() {
    return getSelectedValues(0);
  }

  /**
   * Gets all currently selected option values with a custom wait time.
   *
   * @param waitSec maximum time to wait for the element in seconds
   * @return CList of selected option values
   * @example <pre>{@code
   * CList<String> selectedValues = countrySelect.getSelectedValues(5);
   * }</pre>
   */
  public CList<String> getSelectedValues(int waitSec) {
    return waitUntil("Get Selected Values", waitSec, engine ->
        new CList<>(engine.getAllSelectedOptions(getLocator()))
            .mapToList(e -> e.getAttribute("value").trim()));
  }

  // Getter

  /**
   * Gets the text of the first selected option using the default wait time.
   *
   * @return the text of the first selected option
   * @example <pre>{@code
   * String selectedText = countrySelect.getSelectedText();
   * System.out.println("Selected country: " + selectedText);
   * }</pre>
   */
  public String getSelectedText() {
    return getSelectedText(0);
  }

  /**
   * Gets the text of the first selected option with a custom wait time.
   *
   * @param waitSecs maximum time to wait for the element in seconds
   * @return the text of the first selected option
   * @example <pre>{@code
   * String selectedText = countrySelect.getSelectedText(10);
   * }</pre>
   */
  public String getSelectedText(int waitSecs) {
    return waitUntil("Get Selected Text", waitSecs, engine ->
        engine.getFirstSelectedOption(getLocator()).text().trim());
  }

  /**
   * Gets the value of the first selected option using the default wait time.
   *
   * @return the value attribute of the first selected option
   * @example <pre>{@code
   * String selectedValue = countrySelect.getSelectedValue();
   * System.out.println("Selected country code: " + selectedValue);
   * }</pre>
   */
  public String getSelectedValue() {
    return getSelectedValue(0);
  }

  /**
   * Gets the value of the first selected option with a custom wait time.
   *
   * @param waitSecs maximum time to wait for the element in seconds
   * @return the value attribute of the first selected option
   * @example <pre>{@code
   * String selectedValue = countrySelect.getSelectedValue(5);
   * }</pre>
   */
  public String getSelectedValue(int waitSecs) {
    return waitUntil("Get Selected Value", waitSecs, engine ->
        engine.getFirstSelectedOption(getLocator()).getAttribute("value").trim());
  }

  /**
   * Gets a map of all option texts to their corresponding values using the default wait time.
   *
   * @return CMap where keys are option texts and values are option values
   * @example <pre>{@code
   * CMap<String, String> textToValue = countrySelect.getTextValue();
   * String usValue = textToValue.get("United States"); // Returns "US"
   * }</pre>
   */
  public CMap<String, String> getTextValue() {
    return getTextValue(0);
  }

  /**
   * Gets a map of all option texts to their corresponding values with a custom wait time.
   *
   * @param waitSecs maximum time to wait for the element in seconds
   * @return CMap where keys are option texts and values are option values
   * @example <pre>{@code
   * CMap<String, String> textToValue = countrySelect.getTextValue(10);
   * }</pre>
   */
  public CMap<String, String> getTextValue(int waitSecs) {
    return waitUntil("Get Text/Value", waitSecs, engine ->
        new CList<>(engine.getOptions(getLocator()))
            .toMap(e -> e.text().trim(), e -> e.getAttribute("value").trim()));
  }

  /**
   * Gets a map of all option values to their corresponding texts using the default wait time.
   *
   * @return CMap where keys are option values and values are option texts
   * @example <pre>{@code
   * CMap<String, String> valueToText = countrySelect.getValueText();
   * String usText = valueToText.get("US"); // Returns "United States"
   * }</pre>
   */
  public CMap<String, String> getValueText() {
    return getValueText(0);
  }

  /**
   * Gets a map of all option values to their corresponding texts with a custom wait time.
   *
   * @param waitSecs maximum time to wait for the element in seconds
   * @return CMap where keys are option values and values are option texts
   * @example <pre>{@code
   * CMap<String, String> valueToText = countrySelect.getValueText(5);
   * }</pre>
   */
  public CMap<String, String> getValueText(int waitSecs) {
    return waitUntil("Get Value/Text", waitSecs, engine ->
        new CList<>(engine.getOptions(getLocator()))
            .toMap(e -> e.getAttribute("value").trim(), e -> e.text().trim()));
  }

  /**
   * Gets all option texts in the select element using the default wait time.
   *
   * @return CList of all option texts
   * @example <pre>{@code
   * CList<String> allTexts = countrySelect.getTexts();
   * System.out.println("Available countries: " + allTexts);
   * }</pre>
   */
  public CList<String> getTexts() {
    return getTexts(0);
  }

  /**
   * Gets all option texts in the select element with a custom wait time.
   *
   * @param waitSecs maximum time to wait for the element in seconds
   * @return CList of all option texts
   * @example <pre>{@code
   * CList<String> allTexts = countrySelect.getTexts(10);
   * }</pre>
   */
  public CList<String> getTexts(int waitSecs) {
    return waitUntil("Get Text", waitSecs, engine ->
        new CList<>(engine.getOptions(getLocator())).mapToList(e -> e.text().trim()));
  }

  /**
   * Gets all option values in the select element using the default wait time.
   *
   * @return CList of all option values
   * @example <pre>{@code
   * CList<String> allValues = countrySelect.getValues();
   * System.out.println("Available country codes: " + allValues);
   * }</pre>
   */
  public CList<String> getValues() {
    return getValues(0);
  }

  /**
   * Gets all option values in the select element with a custom wait time.
   *
   * @param waitSecs maximum time to wait for the element in seconds
   * @return CList of all option values
   * @example <pre>{@code
   * CList<String> allValues = countrySelect.getValues(5);
   * }</pre>
   */
  public CList<String> getValues(int waitSecs) {
    return waitUntil("Get Values", waitSecs, engine -> new CList<>(engine.getOptions(getLocator())).mapToList(e -> e.getAttribute("value").trim()));
  }

  // Actions

  /**
   * Selects the first option in the select element using the default wait time.
   * This is equivalent to selectByIndex(0).
   *
   * @example <pre>{@code
   * // Select the first option
   * statusSelect.selectFirstItem();
   * }</pre>
   */
  public void selectFirstItem() {
    selectFirstItem(waitSec);
  }

  /**
   * Selects the first option in the select element with a custom wait time.
   *
   * @param waitSec maximum time to wait for the element in seconds
   * @example <pre>{@code
   * // Select the first option with 10 second wait
   * statusSelect.selectFirstItem(10);
   * }</pre>
   */
  public void selectFirstItem(int waitSec) {
    selectByIndex(0, waitSec);
  }

  /**
   * Selects an option by partial text match using the default wait time.
   * Searches for options containing the specified pattern anywhere in their text.
   *
   * @param pattern the text pattern to search for within option texts
   * @example <pre>{@code
   * // Select option containing "United" (will match "United States")
   * countrySelect.selectByPartialText("United");
   *
   * // Select option containing "Can" (will match "Canada")
   * countrySelect.selectByPartialText("Can");
   * }</pre>
   */
  public void selectByPartialText(String pattern) {
    selectByPartialText(pattern, DEFAULT_TIMEOUT);
  }

  /**
   * Selects an option by partial text match with a custom wait time.
   *
   * @param pattern the text pattern to search for within option texts
   * @param waitSec maximum time to wait for the element in seconds
   * @example <pre>{@code
   * countrySelect.selectByPartialText("United", 5);
   * }</pre>
   */
  public void selectByPartialText(String pattern, int waitSec) {
    selectByTextPattern("^.*" + pattern + ".*$", waitSec);
  }

  /**
   * Selects an option by its visible text using the default wait time.
   * The text must match exactly (case-sensitive).
   *
   * @param value the exact visible text of the option to select
   * @example <pre>{@code
   * // Select by exact text match
   * countrySelect.selectByText("United States");
   *
   * // Select empty option (if exists)
   * countrySelect.selectByText("");
   * }</pre>
   */
  public void selectByText(String value) {
    selectByText(value, waitSec);
  }

  /**
   * Selects an option by its visible text with a custom wait time.
   *
   * @param value   the exact visible text of the option to select
   * @param waitSec maximum time to wait for the element in seconds
   * @example <pre>{@code
   * countrySelect.selectByText("United States", 10);
   * }</pre>
   */
  public void selectByText(String value, int waitSec) {
    waitUntil("Select By Text", waitSec, engine -> {
      engine.selectByVisibleText(getLocator(), StringUtils.defaultString(value));
      return true;
    });
  }

  /**
   * Selects an option by its value attribute using the default wait time.
   *
   * @param value the value attribute of the option to select
   * @example <pre>{@code
   * // Select by value attribute
   * countrySelect.selectByValue("US");
   *
   * // Select option with empty value
   * countrySelect.selectByValue("");
   * }</pre>
   */
  public void selectByValue(String value) {
    selectByValue(value, waitSec);
  }

  /**
   * Selects an option by its value attribute with a custom wait time.
   *
   * @param value   the value attribute of the option to select
   * @param waitSec maximum time to wait for the element in seconds
   * @example <pre>{@code
   * countrySelect.selectByValue("US", 5);
   * }</pre>
   */
  public void selectByValue(String value, int waitSec) {
    waitUntil("Select By Value", waitSec, engine -> {
      engine.selectByValue(getLocator(), StringUtils.defaultString(value));
      return true;
    });
  }

  /**
   * Selects an option by its index position using the default wait time.
   * Index starts from 0 for the first option.
   *
   * @param i the zero-based index of the option to select
   * @example <pre>{@code
   * // Select the first option (index 0)
   * statusSelect.selectByIndex(0);
   *
   * // Select the third option (index 2)
   * statusSelect.selectByIndex(2);
   * }</pre>
   */
  public void selectByIndex(int i) {
    selectByIndex(i, waitSec);
  }

  /**
   * Selects an option by its index position with a custom wait time.
   *
   * @param i       the zero-based index of the option to select
   * @param waitSec maximum time to wait for the element in seconds
   * @example <pre>{@code
   * statusSelect.selectByIndex(1, 10);
   * }</pre>
   */
  public void selectByIndex(int i, int waitSec) {
    waitUntil("Select By Index", waitSec, engine -> {
      engine.selectByIndex(getLocator(), i);
      return true;
    });
  }

  /**
   * Selects an option using a regular expression pattern to match option text using the default wait time.
   * The pattern is applied against the trimmed text of each option.
   *
   * @param pattern the regular expression pattern to match against option texts
   * @example <pre>{@code
   * // Select option that starts with "United"
   * countrySelect.selectByTextPattern("^United.*");
   *
   * // Select option that ends with "States"
   * countrySelect.selectByTextPattern(".*States$");
   *
   * // Select option containing digits
   * countrySelect.selectByTextPattern(".*\\d+.*");
   * }</pre>
   */
  public void selectByTextPattern(String pattern) {
    selectByTextPattern(pattern, waitSec);
  }

  /**
   * Selects an option using a regular expression pattern to match option text with a custom wait time.
   *
   * @param pattern the regular expression pattern to match against option texts
   * @param waitSec maximum time to wait for the element in seconds
   * @example <pre>{@code
   * countrySelect.selectByTextPattern("^United.*", 5);
   * }</pre>
   */
  public void selectByTextPattern(String pattern, int waitSec) {
    waitUntil("Select By Text Pattern", waitSec, engine -> {
      List<CDriverEngine.Options> options = engine.getOptions(getLocator());
      int index = -1;
      for (int i = 0; i < options.size(); i++) {
        String text = StringUtils.strip(options.get(i).text(), "\\n");
        if (Pattern.matches(pattern, text.trim())) {
          index = i;
          break;
        }
      }
      CVerify.Int.greaterOrEqual(index, 0, pattern + " found in options.");
      engine.selectByIndex(getLocator(), index);
      return true;
    });
  }

  /**
   * Sets the selection state based on a boolean value using the default wait time.
   * If true, selects the element; if false, deselects it.
   * This method is useful for conditional selection logic.
   *
   * @param state true to select, false to deselect
   * @example <pre>{@code
   * boolean shouldSelectPremium = user.isPremiumUser();
   * premiumSelect.set(shouldSelectPremium);
   *
   * // Conditional selection
   * countrySelect.set(country.equals("US"));
   * }</pre>
   */
  public boolean set(boolean state) {
    return set(state, waitSec);
  }

  /**
   * Sets the selection state based on a boolean value with a custom wait time.
   *
   * @param state   true to select, false to deselect
   * @param waitSec maximum time to wait for the element in seconds
   * @example <pre>{@code
   * premiumSelect.set(user.isPremiumUser(), 10);
   * }</pre>
   */
  public boolean set(boolean state, int waitSec) {
    if (state) {
      return select(waitSec);
    } else {
      return deselect(waitSec);
    }
  }
}

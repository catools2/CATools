package org.catools.web.controls;

import lombok.extern.slf4j.Slf4j;

/**
 * CWebSelect is a specialized web control for handling HTML select elements (dropdowns). It extends
 * CWebElement and provides comprehensive functionality for interacting with select elements
 * including selecting options by text, value, index, or pattern matching.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Multiple selection methods (by text, value, index, pattern)
 *   <li>Retrieval of selected options and all available options
 *   <li>Dynamic extensions for property-based access
 *   <li>Built-in waiting mechanisms for element stability
 * </ul>
 *
 * <p>Example usage:
 *
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
 * @author CATools
 * @version 1.0
 */
@Slf4j
public abstract class CWebFrame extends CWebElement {

  public CWebFrame(String name, CElementEngine<?> elementEngine, String locator) {
    super(name, elementEngine, locator);
  }
}

package org.catools.common.logger.converters;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.catools.common.testng.listeners.CExecutionStatisticListener;

/**
 * Log4j pattern converter that displays the total count of tests tracked by the execution statistics listener.
 * This converter can be used in Log4j pattern layouts to show real-time test execution statistics.
 * 
 * <p>The converter is registered with the key "ETT" and retrieves the total test count from 
 * {@link CExecutionStatisticListener#getTotal()}, which includes all tests regardless of their 
 * execution status (passed, failed, skipped, running, or waiting).
 * 
 * <p>The total count represents the complete number of test methods discovered and tracked 
 * during test suite execution, providing visibility into the overall test scope.
 * 
 * <h3>Configuration Examples:</h3>
 * <pre>{@code
 * <!-- Basic usage in log4j2.xml -->
 * <PatternLayout pattern="%d{HH:mm:ss} [%ETT] %msg%n"/>
 * 
 * <!-- With custom format for zero-padded display -->
 * <PatternLayout pattern="%d{HH:mm:ss} [%ETT{%03d}] %msg%n"/>
 * 
 * <!-- With descriptive format -->
 * <PatternLayout pattern="%d{HH:mm:ss} [Total: %ETT{%d}] %msg%n"/>
 * }</pre>
 * 
 * <h3>Sample Output:</h3>
 * <pre>{@code
 * 14:30:25 [127] Test suite execution started
 * 14:30:26 [127] Running test method: loginTest
 * 14:30:27 [127] Test method completed successfully
 * 
 * // With zero-padded format (%03d)
 * 14:30:25 [127] Test suite execution started
 * 
 * // With descriptive format
 * 14:30:25 [Total: 127] Test suite execution started
 * }</pre>
 * 
 * @author CA Tools Team
 * @see CExecutionStatisticListener#getTotal()
 * @see CBaseExecutionStatisticConverter
 * @since 1.0
 */
@Plugin(name = "CTotalTestCountConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"ETT"})
public class CTotalTestCountConverter extends CBaseExecutionStatisticConverter {

  /**
   * Constructs an instance of CTotalTestCountConverter with the specified string format.
   * 
   * <p>This constructor initializes the converter to display the total test count using
   * the provided format string. The converter will retrieve real-time test count data
   * from {@link CExecutionStatisticListener#getTotal()}.
   * 
   * @param stringFormat the format string for displaying the total test count. 
   *                     If null or blank, defaults to "%d". Common formats include:
   *                     <ul>
   *                     <li>"%d" - simple integer display (e.g., "127")</li>
   *                     <li>"%03d" - zero-padded 3-digit display (e.g., "127")</li>
   *                     <li>"Total: %d" - descriptive format (e.g., "Total: 127")</li>
   *                     </ul>
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Default formatting
   * CTotalTestCountConverter converter1 = new CTotalTestCountConverter(null);
   * // Output: "127"
   * 
   * // Zero-padded formatting
   * CTotalTestCountConverter converter2 = new CTotalTestCountConverter("%03d");
   * // Output: "127"
   * 
   * // Descriptive formatting
   * CTotalTestCountConverter converter3 = new CTotalTestCountConverter("Total: %d");
   * // Output: "Total: 127"
   * }</pre>
   * 
   * @see CExecutionStatisticListener#getTotal()
   */
  protected CTotalTestCountConverter(final String stringFormat) {
    super("Total Tests", "ETT", stringFormat, () -> CExecutionStatisticListener.getTotal());
  }

  /**
   * Creates a new instance of CTotalTestCountConverter with the provided configuration options.
   * 
   * <p>This factory method is used by the Log4j framework to instantiate the converter
   * when it encounters the "ETT" pattern key in a layout configuration. The method
   * validates the provided options and creates a converter instance with the appropriate
   * string format.
   * 
   * @param options configuration options from the Log4j pattern layout. Can be null, 
   *                empty, or contain at most one string format option. If provided,
   *                the first element should be a valid format string (e.g., "%d", "%03d", "Total: %d")
   * @return a new CTotalTestCountConverter instance configured with the specified format
   * 
   * @throws AssertionError if more than one option is provided (when assertions are enabled)
   * 
   * <h3>Configuration Examples:</h3>
   * <pre>{@code
   * <!-- No options - uses default format "%d" -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETT] %msg%n"/>
   * 
   * <!-- With custom format option -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETT{%03d}] %msg%n"/>
   * 
   * <!-- With descriptive format option -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETT{Total: %d}] %msg%n"/>
   * }</pre>
   * 
   * <h3>Usage in Code:</h3>
   * <pre>{@code
   * // Called automatically by Log4j framework during configuration parsing
   * 
   * // Programmatic usage (if needed)
   * CTotalTestCountConverter converter1 = CTotalTestCountConverter.newInstance(null);
   * CTotalTestCountConverter converter2 = CTotalTestCountConverter.newInstance(new String[]{"%03d"});
   * CTotalTestCountConverter converter3 = CTotalTestCountConverter.newInstance(new String[]{"Tests: %d"});
   * }</pre>
   * 
   * @see #validateAndGetOption(String[])
   */
  public static CTotalTestCountConverter newInstance(final String[] options) {
    return new CTotalTestCountConverter(validateAndGetOption(options));
  }
}

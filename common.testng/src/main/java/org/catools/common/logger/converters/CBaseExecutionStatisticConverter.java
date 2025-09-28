package org.catools.common.logger.converters;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.util.function.Supplier;

/**
 * Abstract base class for execution statistic converters used in Log4j pattern layouts.
 * This class provides common functionality for converting execution statistics to formatted strings
 * in log output. Subclasses should implement specific statistic collection logic.
 *
 * @author CA Tools Team
 * @since 1.0
 */
public abstract class CBaseExecutionStatisticConverter extends LogEventPatternConverter {
  private final Supplier<Integer> supplier;
  private final String stringFormat;

  /**
   * Constructs a new execution statistic converter with the specified parameters.
   * 
   * <p>This constructor initializes the converter with a name, style, format string,
   * and a supplier function that provides the statistic values. The string format
   * defaults to "%d" if a blank or null format is provided.
   * 
   * @param name the name of the converter used in pattern layouts
   * @param style the style identifier for the converter
   * @param stringFormat the format string for displaying the statistic (e.g., "%03d", "Count: %d").
   *                     If null or blank, defaults to "%d"
   * @param supplier a function that supplies the integer statistic value when called
   * 
   * @throws NullPointerException if supplier is null
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Basic usage with default format
   * new MyConverter("testCount", "testCount", null, () -> getTestCount());
   * 
   * // With custom format for zero-padded display
   * new MyConverter("testCount", "testCount", "%03d", () -> getTestCount());
   * 
   * // With descriptive format
   * new MyConverter("testCount", "testCount", "Tests: %d", () -> getTestCount());
   * }</pre>
   */
  protected CBaseExecutionStatisticConverter(
      final String name,
      final String style,
      final String stringFormat,
      final Supplier<Integer> supplier) {
    super(name, style);
    this.stringFormat = StringUtils.defaultIfBlank(stringFormat, "%d");
    this.supplier = supplier;
  }

  /**
   * Validates the provided options array and extracts the first option if available.
   * This utility method is commonly used in converter constructors to handle
   * optional configuration parameters from Log4j pattern layouts.
   * 
   * <p>The method ensures that at most one option is provided and returns it,
   * or null if no options are specified.
   * 
   * @param options the options array from the Log4j pattern configuration.
   *                Can be null, empty, or contain at most one element
   * @return the first option string if present, null otherwise
   * 
   * @throws AssertionError if more than one option is provided (when assertions are enabled)
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // No options provided
   * String result1 = validateAndGetOption(null);        // returns null
   * String result2 = validateAndGetOption(new String[0]); // returns null
   * 
   * // One option provided
   * String result3 = validateAndGetOption(new String[]{"%03d"}); // returns "%03d"
   * 
   * // Usage in converter constructor
   * public class MyConverter extends CBaseExecutionStatisticConverter {
   *     public MyConverter(String[] options) {
   *         super("myConverter", "myConverter", 
   *               validateAndGetOption(options), 
   *               () -> getMyStatistic());
   *     }
   * }
   * }</pre>
   */
  public static String validateAndGetOption(final String[] options) {
    assert options.length < 2;
    return options == null || options.length == 0 ? null : options[0];
  }

  /**
   * Formats the execution statistic and appends it to the log output.
   * This method is called by the Log4j framework during log event processing
   * to convert the statistic value into its string representation.
   * 
   * <p>The method retrieves the current statistic value from the supplier function,
   * formats it according to the configured string format, and appends the result
   * to the provided StringBuilder.
   * 
   * @param event the log event being processed (not used in this implementation)
   * @param toAppendTo the StringBuilder to append the formatted statistic to
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // If supplier returns 42 and stringFormat is "%d"
   * // Result: "42" is appended to toAppendTo
   * 
   * // If supplier returns 5 and stringFormat is "%03d"
   * // Result: "005" is appended to toAppendTo
   * 
   * // If supplier returns 123 and stringFormat is "Count: %d"
   * // Result: "Count: 123" is appended to toAppendTo
   * 
   * // Usage in log pattern: "%d{HH:mm:ss} [%myConverter] %msg%n"
   * // Example output: "14:30:25 [42] Test execution completed"
   * }</pre>
   */
  @Override
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(String.format(stringFormat, supplier.get()));
  }
}

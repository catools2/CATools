package org.catools.common.logger.converters;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.catools.common.testng.listeners.CExecutionStatisticListener;

/**
 * A Log4j pattern converter that provides the total count of failed tests during test execution.
 * This converter integrates with the {@link CExecutionStatisticListener} to dynamically retrieve
 * the current number of failed tests and format it according to the specified pattern.
 *
 * <p>The converter is registered with the key "ETF" and can be used in Log4j patterns to display
 * real-time failed test statistics in log messages.
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Basic Usage in Log4j Configuration:</h4>
 *
 * <pre>{@code
 * <PatternLayout pattern="Failed Tests: %ETF - %msg%n"/>
 * }</pre>
 *
 * Output: {@code Failed Tests: 3 - Test execution completed}
 *
 * <h4>Custom Formatting:</h4>
 *
 * <pre>{@code
 * <PatternLayout pattern="Failed: %ETF{%03d} - %msg%n"/>
 * }</pre>
 *
 * Output: {@code Failed: 003 - Test execution completed}
 *
 * <h4>Integration with Other Converters:</h4>
 *
 * <pre>{@code
 * <PatternLayout pattern="[%d{HH:mm:ss}] Failed: %ETF/%ETT Total - %msg%n"/>
 * }</pre>
 *
 * Output: {@code [14:30:45] Failed: 3/10 Total - Test execution completed}
 *
 * <h4>Programmatic Usage:</h4>
 *
 * <pre>{@code
 * // The converter automatically retrieves the failed count from CExecutionStatisticListener
 * Logger logger = LoggerFactory.getLogger(MyTestClass.class);
 * logger.info("Current test status"); // Will show current failed count
 * }</pre>
 *
 * @see CExecutionStatisticListener#getFailed()
 * @see CBaseExecutionStatisticConverter
 * @since 1.0
 */
@Plugin(name = "CTotalFailedTestCountConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"ETF"})
public class CTotalFailedTestCountConverter extends CBaseExecutionStatisticConverter {

  /**
   * Constructs an instance of CTotalFailedTestCountConverter.
   *
   * @param stringFormat format to be used. defaults to "%d"
   */
  protected CTotalFailedTestCountConverter(final String stringFormat) {
    super("Total Failed", "ETF", stringFormat, () -> CExecutionStatisticListener.getFailed());
  }

  /**
   * Creates a new instance of the CTotalFailedTestCountConverter with the specified options. This
   * factory method is called by the Log4j framework when the converter is instantiated from
   * configuration.
   *
   * <p>The method accepts formatting options to customize how the failed test count is displayed.
   * If no options are provided, the default format "%d" is used.
   *
   * <h3>Usage Examples:</h3>
   *
   * <h4>Default Format:</h4>
   *
   * <pre>{@code
   * // In log4j2.xml:
   * <PatternLayout pattern="%ETF"/>
   * // Result: "5" (if 5 tests have failed)
   * }</pre>
   *
   * <h4>Zero-Padded Format:</h4>
   *
   * <pre>{@code
   * // In log4j2.xml:
   * <PatternLayout pattern="%ETF{%04d}"/>
   * // Result: "0005" (if 5 tests have failed)
   * }</pre>
   *
   * <h4>Hexadecimal Format:</h4>
   *
   * <pre>{@code
   * // In log4j2.xml:
   * <PatternLayout pattern="%ETF{%X}"/>
   * // Result: "A" (if 10 tests have failed)
   * }</pre>
   *
   * <h4>Programmatic Creation:</h4>
   *
   * <pre>{@code
   * // Create with default formatting
   * CTotalFailedTestCountConverter converter1 =
   *     CTotalFailedTestCountConverter.newInstance(null);
   *
   * // Create with custom formatting
   * CTotalFailedTestCountConverter converter2 =
   *     CTotalFailedTestCountConverter.newInstance(new String[]{"%03d"});
   * }</pre>
   *
   * @param options an array of formatting options where the first element (if present) specifies
   *     the format string to use for displaying the count. Must follow Java's {@link
   *     String#format(String, Object...)} syntax. Can be {@code null} or empty to use default
   *     formatting.
   * @return a new instance of CTotalFailedTestCountConverter configured with the specified options
   * @throws AssertionError if more than one option is provided (inherited from parent validation)
   * @see String#format(String, Object...)
   * @see CBaseExecutionStatisticConverter#validateAndGetOption(String[])
   */
  public static CTotalFailedTestCountConverter newInstance(final String[] options) {
    return new CTotalFailedTestCountConverter(validateAndGetOption(options));
  }
}

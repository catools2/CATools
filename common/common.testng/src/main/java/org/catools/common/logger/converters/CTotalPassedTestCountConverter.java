package org.catools.common.logger.converters;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.catools.common.testng.listeners.CExecutionStatisticListener;

/**
 * A Log4j pattern converter that formats the total count of passed tests in log messages. This
 * converter integrates with the TestNG execution statistics to provide real-time information about
 * test execution progress.
 *
 * <p>The converter is registered with the key "ETP" (Execution Total Passed) and can be used in
 * Log4j configuration patterns to display the current count of passed tests.
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Basic Usage in log4j2.xml:</h4>
 *
 * <pre>{@code
 * <Console name="Console" target="SYSTEM_OUT">
 *   <PatternLayout pattern="[%d] [%ETP passed] %msg%n"/>
 * </Console>
 * }</pre>
 *
 * <h4>With Custom Format:</h4>
 *
 * <pre>{@code
 * <Console name="Console" target="SYSTEM_OUT">
 *   <PatternLayout pattern="[%d] [%ETP{Passed: %03d} tests] %msg%n"/>
 * </Console>
 * }</pre>
 *
 * <h4>Sample Output:</h4>
 *
 * <pre>
 * [2023-12-07 10:15:30] [5 passed] Starting test execution
 * [2023-12-07 10:15:32] [Passed: 007 tests] Test completed successfully
 * </pre>
 *
 * <p><strong>Note:</strong> This converter requires the {@link CExecutionStatisticListener} to be
 * registered as a TestNG listener to track test execution statistics.
 *
 * @see CBaseExecutionStatisticConverter
 * @see CExecutionStatisticListener
 * @since 1.0
 */
@Plugin(name = "CTotalPassedTestCountConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"ETP"})
public class CTotalPassedTestCountConverter extends CBaseExecutionStatisticConverter {

  /**
   * Constructs an instance of CTotalPassedTestCountConverter.
   *
   * <p>This constructor initializes the converter with a custom string format for displaying the
   * passed test count. The format follows standard Java printf-style formatting conventions.
   *
   * <h4>Format Examples:</h4>
   *
   * <ul>
   *   <li>{@code "%d"} - Simple integer format (default): "5"
   *   <li>{@code "%03d"} - Zero-padded 3-digit format: "005"
   *   <li>{@code "Passed: %d"} - With prefix text: "Passed: 5"
   *   <li>{@code "%d/%d"} - Though only one value available: "5" (second %d ignored)
   * </ul>
   *
   * @param stringFormat format to be used for displaying the count. Defaults to "%d" if null or
   *     blank. Must be a valid printf-style format string.
   * @see String#format(String, Object...)
   * @see CExecutionStatisticListener#getPassed()
   */
  protected CTotalPassedTestCountConverter(final String stringFormat) {
    super("Total Pass", "ETP", stringFormat, () -> CExecutionStatisticListener.getPassed());
  }

  /**
   * Creates a new instance of CTotalPassedTestCountConverter with the specified options.
   *
   * <p>This factory method is called by the Log4j framework when parsing configuration files. It
   * validates the provided options and creates a converter instance with the appropriate string
   * format.
   *
   * <h4>Usage in Configuration:</h4>
   *
   * <pre>{@code
   * <!-- Simple usage with default format -->
   * <PatternLayout pattern="%ETP passed tests: %msg%n"/>
   *
   * <!-- With custom format -->
   * <PatternLayout pattern="%ETP{%03d} tests completed: %msg%n"/>
   *
   * <!-- With text format -->
   * <PatternLayout pattern="%ETP{Passed: %d tests} - %msg%n"/>
   * }</pre>
   *
   * <h4>Configuration Examples and Output:</h4>
   *
   * <table border="1">
   *   <tr><th>Configuration</th><th>Sample Output</th></tr>
   *   <tr><td>{@code %ETP}</td><td>5</td></tr>
   *   <tr><td>{@code %ETP{%03d}}</td><td>005</td></tr>
   *   <tr><td>{@code %ETP{Passed: %d}}</td><td>Passed: 5</td></tr>
   *   <tr><td>{@code %ETP{%d/%d total}}</td><td>5/%d total (only first format used)</td></tr>
   * </table>
   *
   * @param options array of configuration options. Expected to contain at most one element
   *     representing the string format. If null or empty, default format "%d" is used. If more than
   *     one option is provided, only the first is used.
   * @return a new CTotalPassedTestCountConverter instance configured with the specified format
   * @throws AssertionError if more than one option is provided (in debug mode)
   * @see #CTotalPassedTestCountConverter(String)
   * @see CBaseExecutionStatisticConverter#validateAndGetOption(String[])
   */
  public static CTotalPassedTestCountConverter newInstance(final String[] options) {
    return new CTotalPassedTestCountConverter(validateAndGetOption(options));
  }
}

package org.catools.common.logger.converters;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.catools.common.testng.listeners.CExecutionStatisticListener;

/**
 * Log4j2 pattern converter that provides the count of waiting (not yet started) tests in TestNG
 * execution statistics. This converter integrates with the CExecutionStatisticListener to retrieve
 * real-time counts of tests that are queued for execution but haven't started yet.
 *
 * <p>Waiting tests are calculated as: total tests - passed - failed - skipped - running tests. This
 * gives you visibility into how many tests are still pending execution in your test suite.
 *
 * <p>The converter is registered as a Log4j2 plugin and can be used in log pattern layouts with the
 * converter key "ETW" (Execution Tests Waiting).
 *
 * <h3>Log4j2 Configuration Examples:</h3>
 *
 * <pre>{@code
 * <!-- Basic usage with default formatting -->
 * <PatternLayout pattern="%d{HH:mm:ss} [%ETW] %msg%n"/>
 * <!-- Output: "14:30:25 [15] Test execution in progress"
 *
 * <!-- Custom formatting with zero-padding -->
 * <PatternLayout pattern="%d{HH:mm:ss} [%ETW{%03d}] %msg%n"/>
 * <!-- Output: "14:30:25 [015] Test execution in progress"
 *
 * <!-- Descriptive formatting -->
 * <PatternLayout pattern="%d{HH:mm:ss} [Waiting: %ETW{%d}] %msg%n"/>
 * <!-- Output: "14:30:25 [Waiting: 15] Test execution in progress"
 * }</pre>
 *
 * <h3>Usage Scenarios:</h3>
 *
 * <ul>
 *   <li>Monitoring test execution progress in real-time logs
 *   <li>Tracking test queue depth during parallel execution
 *   <li>Debugging test execution bottlenecks
 *   <li>Providing visibility into remaining test workload
 * </ul>
 *
 * <h3>Integration with TestNG:</h3>
 *
 * <pre>{@code
 * // TestNG configuration to enable the listener
 * <suite name="TestSuite">
 *   <listeners>
 *     <listener class-name="org.catools.common.testng.listeners.CExecutionStatisticListener"/>
 *   </listeners>
 *   <test name="TestGroup">
 *     <!-- Your test classes here -->
 *   </test>
 * </suite>
 * }</pre>
 *
 * @author CA Tools Team
 * @see CBaseExecutionStatisticConverter
 * @see CExecutionStatisticListener#getWaiting()
 * @since 1.0
 */
@Plugin(name = "CTotalWaitingTestCountConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"ETW"})
public class CTotalWaitingTestCountConverter extends CBaseExecutionStatisticConverter {

  /**
   * Constructs an instance of CTotalWaitingTestCountConverter with the specified string format.
   *
   * <p>This constructor initializes the converter to track waiting test counts using the
   * CExecutionStatisticListener. The converter will display the count of tests that are queued for
   * execution but haven't started yet.
   *
   * <p>The string format parameter allows customization of how the waiting test count is displayed
   * in log output. If null or blank, it defaults to "%d".
   *
   * @param stringFormat the format string for displaying the waiting test count. Common formats
   *     include:
   *     <ul>
   *       <li>"%d" - simple integer display (default)
   *       <li>"%03d" - zero-padded 3-digit display
   *       <li>"Waiting: %d" - descriptive format
   *       <li>"%,d" - comma-separated for large numbers
   *     </ul>
   *     <h3>Examples:</h3>
   *     <pre>{@code
   * // Default formatting
   * new CTotalWaitingTestCountConverter(null);
   * // If 15 tests are waiting, outputs: "15"
   *
   * // Zero-padded formatting
   * new CTotalWaitingTestCountConverter("%03d");
   * // If 15 tests are waiting, outputs: "015"
   *
   * // Descriptive formatting
   * new CTotalWaitingTestCountConverter("Pending: %d");
   * // If 15 tests are waiting, outputs: "Pending: 15"
   *
   * }</pre>
   */
  protected CTotalWaitingTestCountConverter(final String stringFormat) {
    super("Total Waiting", "ETW", stringFormat, () -> CExecutionStatisticListener.getWaiting());
  }

  /**
   * Factory method to create a new instance of CTotalWaitingTestCountConverter.
   *
   * <p>This method is called by the Log4j2 framework when the converter is used in pattern layouts.
   * It validates the provided options and creates a new converter instance with the appropriate
   * string format.
   *
   * <p>The options array should contain at most one element, which specifies the string format for
   * displaying the waiting test count. If no options are provided or the array is empty, the
   * default format "%d" will be used.
   *
   * @param options configuration options from the Log4j2 pattern layout. Expected to contain at
   *     most one string format option. Examples:
   *     <ul>
   *       <li>null or empty array - uses default "%d" format
   *       <li>new String[]{"%03d"} - uses zero-padded format
   *       <li>new String[]{"Waiting: %d"} - uses descriptive format
   *     </ul>
   *
   * @return a new CTotalWaitingTestCountConverter instance configured with the specified options
   * @throws AssertionError if more than one option is provided (when assertions are enabled)
   *     <h3>Log4j2 Usage Examples:</h3>
   *     <pre>{@code
   * <!-- No options - default format -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETW] %msg%n"/>
   *
   * <!-- With format option -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETW{%03d}] %msg%n"/>
   *
   * <!-- With descriptive format -->
   * <PatternLayout pattern="%d{HH:mm:ss} %ETW{Waiting: %d tests} %msg%n"/>
   *
   * }</pre>
   */
  public static CTotalWaitingTestCountConverter newInstance(final String[] options) {
    return new CTotalWaitingTestCountConverter(validateAndGetOption(options));
  }
}

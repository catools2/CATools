package org.catools.common.logger.converters;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.catools.common.testng.listeners.CExecutionStatisticListener;

/**
 * A Log4j pattern converter that displays the total count of currently running tests.
 * This converter integrates with the TestNG execution framework to provide real-time
 * statistics about test execution status in log output.
 * 
 * <p>The converter tracks tests that are currently in the RUNNING state as monitored
 * by {@link CExecutionStatisticListener}. This is useful for monitoring concurrent
 * test execution and understanding how many tests are actively running at any given moment.
 * 
 * <p>The converter is registered with Log4j using the key "ETR" (Execution Tests Running)
 * and can be used in pattern layouts to display running test counts in log messages.
 * 
 * <h3>Usage in log4j2.xml Configuration:</h3>
 * <pre>{@code
 * <Configuration>
 *   <Appenders>
 *     <Console name="Console">
 *       <!-- Basic usage - shows running count as plain number -->
 *       <PatternLayout pattern="%d{HH:mm:ss} [Running: %ETR] %msg%n"/>
 *       
 *       <!-- With zero-padded format -->
 *       <PatternLayout pattern="%d{HH:mm:ss} [%ETR{%03d}] %msg%n"/>
 *       
 *       <!-- With custom descriptive format -->
 *       <PatternLayout pattern="%d{HH:mm:ss} [Active: %ETR{%d tests}] %msg%n"/>
 *     </Console>
 *   </Appenders>
 * </Configuration>
 * }</pre>
 * 
 * <h3>Example Log Output:</h3>
 * <pre>{@code
 * // Basic format
 * 14:30:25 [Running: 3] Starting test suite execution
 * 14:30:26 [Running: 5] Additional tests started
 * 14:30:27 [Running: 2] Some tests completed
 * 
 * // Zero-padded format
 * 14:30:25 [003] Test execution in progress
 * 14:30:26 [005] Peak concurrent execution
 * 14:30:27 [002] Winding down execution
 * 
 * // Descriptive format
 * 14:30:25 [Active: 3 tests] Test suite started
 * 14:30:26 [Active: 5 tests] Maximum concurrency reached
 * 14:30:27 [Active: 2 tests] Tests completing
 * }</pre>
 * 
 * <h3>Integration with TestNG:</h3>
 * <p>This converter relies on {@link CExecutionStatisticListener} being properly
 * registered as a TestNG listener. The listener tracks test method execution states
 * and provides the running count through its static {@code getRunning()} method.
 * 
 * <h3>Thread Safety:</h3>
 * <p>This converter is thread-safe as it delegates to the thread-safe
 * {@link CExecutionStatisticListener#getRunning()} method for retrieving
 * the current running test count.
 * 
 * @author CA Tools Team  
 * @since 1.0
 * @see CBaseExecutionStatisticConverter
 * @see CExecutionStatisticListener
 * @see org.catools.common.testng.model.CExecutionStatus
 */
@Plugin(name = "CTotalRunningTestCountConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"ETR"})
public class CTotalRunningTestCountConverter extends CBaseExecutionStatisticConverter {

  /**
   * Constructs an instance of CTotalRunningTestCountConverter with the specified format.
   * 
   * <p>This constructor initializes the converter to track and display the count of
   * currently running tests. The running count is obtained from the execution
   * statistic listener which monitors TestNG test execution states.
   * 
   * <p>The string format parameter allows customization of how the running count
   * is displayed in log output. If null or blank, defaults to "%d" for plain
   * numeric display.
   *
   * @param stringFormat the format string for displaying the running test count.
   *                     Examples: "%d" (plain number), "%03d" (zero-padded to 3 digits),
   *                     "Running: %d" (with descriptive text). If null or blank, defaults to "%d"
   * 
   * <h3>Examples:</h3>
   * <pre>{@code
   * // Plain numeric format (default)
   * new CTotalRunningTestCountConverter(null);     // displays: "3"
   * new CTotalRunningTestCountConverter("%d");     // displays: "3"
   * 
   * // Zero-padded format
   * new CTotalRunningTestCountConverter("%03d");   // displays: "003"
   * new CTotalRunningTestCountConverter("%05d");   // displays: "00003"
   * 
   * // Descriptive formats
   * new CTotalRunningTestCountConverter("Active: %d");        // displays: "Active: 3"
   * new CTotalRunningTestCountConverter("%d running");        // displays: "3 running"
   * new CTotalRunningTestCountConverter("[%d tests active]"); // displays: "[3 tests active]"
   * }</pre>
   */
  protected CTotalRunningTestCountConverter(final String stringFormat) {
    super(
        "Total Running", "ETR", stringFormat, () -> CExecutionStatisticListener.getRunning());
  }

  /**
   * Creates a new instance of CTotalRunningTestCountConverter with configuration options.
   * 
   * <p>This factory method is called by the Log4j framework during pattern layout
   * initialization. It processes the configuration options from the pattern layout
   * and creates a properly configured converter instance.
   * 
   * <p>This method validates that at most one option is provided (the format string)
   * and delegates to {@link CBaseExecutionStatisticConverter#validateAndGetOption(String[])}
   * for option processing.
   * 
   * @param options configuration options from the Log4j pattern layout.
   *                Should contain at most one element representing the format string.
   *                Can be null, empty array, or single-element array with format string
   * @return a new configured CTotalRunningTestCountConverter instance
   * 
   * @throws AssertionError if more than one option is provided (when assertions are enabled)
   * 
   * <h3>Usage Examples in log4j2.xml:</h3>
   * <pre>{@code
   * <!-- No options - uses default "%d" format -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETR] %msg%n"/>
   * 
   * <!-- With format option for zero-padding -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETR{%03d}] %msg%n"/>
   * 
   * <!-- With descriptive format option -->
   * <PatternLayout pattern="%d{HH:mm:ss} [%ETR{Running: %d}] %msg%n"/>
   * 
   * <!-- Complex format with brackets -->
   * <PatternLayout pattern="%d{HH:mm:ss} %ETR{[%d active]} %msg%n"/>
   * }</pre>
   * 
   * <h3>Corresponding Method Calls:</h3>
   * <pre>{@code
   * // For pattern "%ETR"
   * CTotalRunningTestCountConverter.newInstance(null);
   * // or
   * CTotalRunningTestCountConverter.newInstance(new String[0]);
   * 
   * // For pattern "%ETR{%03d}"
   * CTotalRunningTestCountConverter.newInstance(new String[]{"%03d"});
   * 
   * // For pattern "%ETR{Running: %d}"
   * CTotalRunningTestCountConverter.newInstance(new String[]{"Running: %d"});
   * }</pre>
   */
  public static CTotalRunningTestCountConverter newInstance(final String[] options) {
    return new CTotalRunningTestCountConverter(validateAndGetOption(options));
  }
}

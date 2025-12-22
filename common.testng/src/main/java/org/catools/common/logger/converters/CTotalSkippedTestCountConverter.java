package org.catools.common.logger.converters;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.catools.common.testng.listeners.CExecutionStatisticListener;

/**
 * A Log4j2 pattern converter that provides the total count of skipped tests during TestNG
 * execution.
 *
 * <p>This converter is used to display the number of tests that have been skipped in log messages.
 * It integrates with the {@link CExecutionStatisticListener} to retrieve real-time statistics about
 * test execution status.
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Basic Configuration in log4j2.xml:</h4>
 *
 * <pre>{@code
 * <Configuration>
 *   <Appenders>
 *     <Console name="Console" target="SYSTEM_OUT">
 *       <PatternLayout pattern="[%d{HH:mm:ss}] Skipped: %ETS - %msg%n"/>
 *     </Console>
 *   </Appenders>
 *   <Loggers>
 *     <Root level="info">
 *       <AppenderRef ref="Console"/>
 *     </Root>
 *   </Loggers>
 * </Configuration>
 * }</pre>
 *
 * <h4>With Custom Formatting:</h4>
 *
 * <pre>{@code
 * <PatternLayout pattern="Tests: [Skipped: %ETS{%03d}] - %msg%n"/>
 * }</pre>
 *
 * <h4>Combined with Other Statistics:</h4>
 *
 * <pre>{@code
 * <PatternLayout pattern="[%d] Total: %ETC | Passed: %ETP | Failed: %ETF | Skipped: %ETS - %msg%n"/>
 * }</pre>
 *
 * <h4>Programmatic Usage:</h4>
 *
 * <pre>{@code
 * // The converter is automatically registered via the @Plugin annotation
 * // and can be used in any Log4j2 pattern layout with the %ETS key
 *
 * Logger logger = LogManager.getLogger(MyTestClass.class);
 * logger.info("Current test execution status"); // Will show skipped count
 * }</pre>
 *
 * @see CExecutionStatisticListener
 * @see CBaseExecutionStatisticConverter
 * @since 1.0
 */
@Plugin(name = "CTotalSkippedTestCountConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"ETS"})
public class CTotalSkippedTestCountConverter extends CBaseExecutionStatisticConverter {

  /**
   * Constructs an instance of CTotalSkippedTestCountConverter.
   *
   * <p>This constructor initializes the converter with a custom string format for displaying the
   * skipped test count. The converter will retrieve the skipped test count from the {@link
   * CExecutionStatisticListener}.
   *
   * <h4>String Format Examples:</h4>
   *
   * <ul>
   *   <li>{@code "%d"} - Default format, displays as integer (e.g., "5")
   *   <li>{@code "%03d"} - Zero-padded 3-digit format (e.g., "005")
   *   <li>{@code "%,d"} - Comma-separated format (e.g., "1,234")
   *   <li>{@code "Skipped: %d"} - Custom prefix format (e.g., "Skipped: 5")
   * </ul>
   *
   * @param stringFormat format to be used for displaying the skipped count. If null or blank,
   *     defaults to "%d"
   * @see String#format(String, Object...)
   * @see CExecutionStatisticListener#getSkipped()
   */
  protected CTotalSkippedTestCountConverter(final String stringFormat) {
    super("Total Skipped", "ETS", stringFormat, () -> CExecutionStatisticListener.getSkipped());
  }

  /**
   * Creates a new instance of CTotalSkippedTestCountConverter from the provided options.
   *
   * <p>This static factory method is used by Log4j2 to instantiate the converter when it encounters
   * the %ETS pattern in a layout configuration. The method validates the provided options and
   * creates a converter with the appropriate string format.
   *
   * <h4>Usage Examples:</h4>
   *
   * <pre>{@code
   * // In log4j2.xml configuration:
   * <PatternLayout pattern="%ETS"/>           // Uses default format
   * <PatternLayout pattern="%ETS{%03d}"/>     // Uses zero-padded format
   * <PatternLayout pattern="%ETS{%,d}"/>      // Uses comma-separated format
   * }</pre>
   *
   * <h4>Expected Behavior:</h4>
   *
   * <ul>
   *   <li>If options is null or empty: uses default format "%d"
   *   <li>If options contains one element: uses that element as format string
   *   <li>If options contains more than one element: assertion failure (development only)
   * </ul>
   *
   * @param options array of configuration options where the first element (if present) is used as
   *     the string format for displaying the skipped count. May be null or empty for default
   *     formatting.
   * @return a new CTotalSkippedTestCountConverter instance configured with the specified options
   * @throws AssertionError if options array contains more than one element (in development builds)
   * @see #CTotalSkippedTestCountConverter(String)
   * @see CBaseExecutionStatisticConverter#validateAndGetOption(String[])
   */
  public static CTotalSkippedTestCountConverter newInstance(final String[] options) {
    return new CTotalSkippedTestCountConverter(validateAndGetOption(options));
  }
}

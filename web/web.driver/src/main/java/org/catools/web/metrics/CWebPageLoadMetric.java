package org.catools.web.metrics;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A metric class for capturing web page load performance data. This class provides a structured way
 * to store and track page load metrics including timing information, page details, and navigation
 * context.
 *
 * <p>The class uses Lombok annotations to generate getter/setter methods and supports method
 * chaining for fluent API usage.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>{@code
 * // Create a new page load metric
 * CWebPageLoadMetric metric = new CWebPageLoadMetric()
 *     .setName("HomePage Load")
 *     .setTitle("Welcome - My Application")
 *     .setUrl("https://example.com/home")
 *     .setActionTime(new Date())
 *     .setDuration(1250);
 *
 * // Access metric data
 * System.out.println("Page " + metric.getName() + " loaded in " + metric.getDuration() + "ms");
 *
 * // Chain multiple metrics for reporting
 * List<CWebPageLoadMetric> metrics = Arrays.asList(
 *     new CWebPageLoadMetric().setName("Login").setDuration(800),
 *     new CWebPageLoadMetric().setName("Dashboard").setDuration(1200)
 * );
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class CWebPageLoadMetric {

  /**
   * The name or identifier of the page load metric. This is typically a human-readable identifier
   * for the specific page or action being measured.
   *
   * <p><strong>Examples:</strong>
   *
   * <ul>
   *   <li>"HomePage Load"
   *   <li>"User Login"
   *   <li>"Product Search Results"
   *   <li>"Checkout Process"
   * </ul>
   *
   * @return the name of the metric
   * @see #setName(String)
   */
  private String name;

  /**
   * The title of the web page as it appears in the browser tab or title bar. This is usually
   * extracted from the HTML &lt;title&gt; element.
   *
   * <p><strong>Examples:</strong>
   *
   * <ul>
   *   <li>"Welcome to MyApp - Home"
   *   <li>"User Dashboard | MyApp"
   *   <li>"Search Results for 'laptop' - Shopping"
   *   <li>"Login - Secure Access"
   * </ul>
   *
   * @return the page title
   * @see #setTitle(String)
   */
  private String title;

  /**
   * The complete URL of the web page being measured. This should include the protocol, domain,
   * path, and any query parameters.
   *
   * <p><strong>Examples:</strong>
   *
   * <ul>
   *   <li>"https://example.com/"
   *   <li>"https://shop.example.com/products?category=electronics"
   *   <li>"https://app.example.com/dashboard?user=123"
   *   <li>"https://secure.example.com/login"
   * </ul>
   *
   * @return the page URL
   * @see #setUrl(String)
   */
  private String url;

  /**
   * The timestamp when the page load action was initiated or completed. This provides temporal
   * context for when the measurement was taken.
   *
   * <p><strong>Usage Examples:</strong>
   *
   * <pre>{@code
   * // Set to current time when measurement starts
   * metric.setActionTime(new Date());
   *
   * // Set to specific timestamp for historical data
   * Calendar cal = Calendar.getInstance();
   * cal.set(2023, Calendar.DECEMBER, 1, 14, 30, 0);
   * metric.setActionTime(cal.getTime());
   *
   * // Convert from milliseconds timestamp
   * long timestamp = System.currentTimeMillis();
   * metric.setActionTime(new Date(timestamp));
   * }</pre>
   *
   * @return the action timestamp
   * @see #setActionTime(Date)
   */
  private Date actionTime;

  /**
   * The duration of the page load in milliseconds. This represents the total time taken from the
   * start of navigation to when the page is considered fully loaded.
   *
   * <p><strong>Typical Values:</strong>
   *
   * <ul>
   *   <li>Fast load: 0-1000ms
   *   <li>Average load: 1000-3000ms
   *   <li>Slow load: 3000ms+
   * </ul>
   *
   * <p><strong>Usage Examples:</strong>
   *
   * <pre>{@code
   * // Measure page load time
   * long startTime = System.currentTimeMillis();
   * // ... perform page navigation ...
   * long endTime = System.currentTimeMillis();
   * metric.setDuration(endTime - startTime);
   *
   * // Set predefined duration for testing
   * metric.setDuration(1500); // 1.5 seconds
   *
   * // Performance analysis
   * if (metric.getDuration() > 3000) {
   *     System.out.println("Slow page load detected: " + metric.getDuration() + "ms");
   * }
   * }</pre>
   *
   * @return the load duration in milliseconds
   * @see #setDuration(long)
   */
  private long duration;
}

package org.catools.web.metrics;

import lombok.Data;
import org.catools.common.collections.CList;

import java.util.Date;

/**
 * A specialized collection for managing web page load metrics that extends {@link CList}
 * functionality. This class provides a convenient way to store, manipulate, and analyze multiple
 * {@link CWebPageLoadMetric} instances.
 *
 * <p>The class inherits all the enhanced collection operations from {@link CList}, including
 * filtering, searching, partitioning, and conversion operations specifically tailored for web page
 * load metrics.
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Creating and Adding Metrics:</h4>
 *
 * <pre>{@code
 * CWebPageLoadMetrics metrics = new CWebPageLoadMetrics();
 *
 * // Add individual metrics
 * CWebPageLoadMetric homePageMetric = new CWebPageLoadMetric()
 *     .setName("home_page")
 *     .setTitle("Home - Example Site")
 *     .setUrl("https://example.com")
 *     .setActionTime(new Date())
 *     .setDuration(1500);
 * metrics.add(homePageMetric);
 *
 * // Add multiple metrics at once
 * CWebPageLoadMetric loginMetric = new CWebPageLoadMetric()
 *     .setName("login_page")
 *     .setTitle("Login - Example Site")
 *     .setUrl("https://example.com/login")
 *     .setActionTime(new Date())
 *     .setDuration(800);
 * metrics.addAll(List.of(homePageMetric, loginMetric));
 * }</pre>
 *
 * <h4>Filtering and Searching:</h4>
 *
 * <pre>{@code
 * // Find metrics with load time greater than 1000ms
 * CWebPageLoadMetrics slowPages = metrics.getAll(metric -> metric.getDuration() > 1000);
 *
 * // Get first metric matching criteria
 * CWebPageLoadMetric firstSlowPage = metrics.getFirstOrNull(metric -> metric.getDuration() > 2000);
 *
 * // Check if any page took longer than 3 seconds
 * boolean hasVerySlowPages = metrics.stream().anyMatch(metric -> metric.getDuration() > 3000);
 * }</pre>
 *
 * <h4>Statistical Operations:</h4>
 *
 * <pre>{@code
 * // Calculate average load time
 * double avgLoadTime = metrics.stream()
 *     .mapToLong(CWebPageLoadMetric::getDuration)
 *     .average()
 *     .orElse(0.0);
 *
 * // Find maximum load time
 * long maxLoadTime = metrics.stream()
 *     .mapToLong(CWebPageLoadMetric::getDuration)
 *     .max()
 *     .orElse(0);
 * }</pre>
 *
 * <h4>Grouping and Analysis:</h4>
 *
 * <pre>{@code
 * // Convert to map grouped by URL
 * CMap<String, CWebPageLoadMetric> metricsByUrl = metrics.toMap(
 *     CWebPageLoadMetric::getUrl,
 *     Function.identity()
 * );
 *
 * // Partition metrics into batches for processing
 * CList<CList<CWebPageLoadMetric>> batches = metrics.partition(10);
 * }</pre>
 *
 * <h4>Conditional Operations:</h4>
 *
 * <pre>{@code
 * // Add metric only if it meets criteria
 * CWebPageLoadMetric conditionalMetric = new CWebPageLoadMetric()
 *     .setDuration(500);
 * metrics.addIf(metric -> metric.getDuration() < 1000, conditionalMetric);
 *
 * // Remove slow loading pages
 * metrics.removeIf(metric -> metric.getDuration() > 5000);
 * }</pre>
 *
 * @author CATools Team
 * @see CWebPageLoadMetric
 * @see CList
 * @since 1.0
 */
@Data
public class CWebPageLoadMetrics extends CList<CWebPageLoadMetric> {

  /**
   * Creates an empty collection of web page load metrics.
   *
   * @since 1.0
   */
  public CWebPageLoadMetrics() {
    super();
  }

  /**
   * Creates a collection initialized with the provided metrics.
   *
   * @param metrics the initial metrics to add to the collection
   * @example
   *     <pre>{@code
   * CWebPageLoadMetric metric1 = new CWebPageLoadMetric().setName("page1").setDuration(1000);
   * CWebPageLoadMetric metric2 = new CWebPageLoadMetric().setName("page2").setDuration(1500);
   * CWebPageLoadMetrics collection = new CWebPageLoadMetrics(metric1, metric2);
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetrics(CWebPageLoadMetric... metrics) {
    super(metrics);
  }

  /**
   * Creates a collection from an iterable of metrics.
   *
   * @param metrics the iterable containing metrics to add
   * @example
   *     <pre>{@code
   * List<CWebPageLoadMetric> metricsList = Arrays.asList(metric1, metric2);
   * CWebPageLoadMetrics collection = new CWebPageLoadMetrics(metricsList);
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetrics(Iterable<CWebPageLoadMetric> metrics) {
    super(metrics);
  }

  /**
   * Finds all metrics for pages that loaded within the specified duration threshold.
   *
   * @param maxDurationMs the maximum load duration in milliseconds
   * @return a new collection containing metrics that meet the criteria
   * @example
   *     <pre>{@code
   * // Get all pages that loaded in under 2 seconds
   * CWebPageLoadMetrics fastPages = metrics.getFastLoadingPages(2000);
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetrics getFastLoadingPages(long maxDurationMs) {
    return new CWebPageLoadMetrics(getAll(metric -> metric.getDuration() <= maxDurationMs));
  }

  /**
   * Finds all metrics for pages that took longer than the specified duration to load.
   *
   * @param minDurationMs the minimum load duration in milliseconds
   * @return a new collection containing metrics that meet the criteria
   * @example
   *     <pre>{@code
   * // Get all pages that took more than 3 seconds to load
   * CWebPageLoadMetrics slowPages = metrics.getSlowLoadingPages(3000);
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetrics getSlowLoadingPages(long minDurationMs) {
    return new CWebPageLoadMetrics(getAll(metric -> metric.getDuration() > minDurationMs));
  }

  /**
   * Finds all metrics for pages accessed within a specific time range.
   *
   * @param startTime the start of the time range (inclusive)
   * @param endTime the end of the time range (inclusive)
   * @return a new collection containing metrics within the time range
   * @example
   *     <pre>{@code
   * Date startTime = new Date(System.currentTimeMillis() - 3600000); // 1 hour ago
   * Date endTime = new Date();
   * CWebPageLoadMetrics recentMetrics = metrics.getMetricsInTimeRange(startTime, endTime);
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetrics getMetricsInTimeRange(Date startTime, Date endTime) {
    return new CWebPageLoadMetrics(
        getAll(
            metric -> {
              Date actionTime = metric.getActionTime();
              return actionTime != null
                  && !actionTime.before(startTime)
                  && !actionTime.after(endTime);
            }));
  }

  /**
   * Finds all metrics for pages with URLs matching the specified pattern.
   *
   * @param urlPattern the URL pattern to match (can contain wildcards)
   * @return a new collection containing metrics with matching URLs
   * @example
   *     <pre>{@code
   * // Get all metrics for pages in the admin section
   * CWebPageLoadMetrics adminPages = metrics.getMetricsByUrlPattern("/admin/*");
   *
   * // Get all HTTPS pages
   * CWebPageLoadMetrics httpsPages = metrics.getMetricsByUrlPattern("https://*");
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetrics getMetricsByUrlPattern(String urlPattern) {
    String regex = urlPattern.replace("*", ".*");
    return new CWebPageLoadMetrics(
        getAll(metric -> metric.getUrl() != null && metric.getUrl().matches(regex)));
  }

  /**
   * Calculates the average load duration across all metrics in the collection.
   *
   * @return the average load duration in milliseconds, or 0.0 if the collection is empty
   * @example
   *     <pre>{@code
   * double avgLoadTime = metrics.getAverageLoadDuration();
   * System.out.println("Average page load time: " + avgLoadTime + "ms");
   * }</pre>
   *
   * @since 1.0
   */
  public double getAverageLoadDuration() {
    return stream().mapToLong(CWebPageLoadMetric::getDuration).average().orElse(0.0);
  }

  /**
   * Finds the metric with the longest load duration.
   *
   * @return the metric with the maximum load duration, or null if the collection is empty
   * @example
   *     <pre>{@code
   * CWebPageLoadMetric slowestPage = metrics.getSlowestLoadingPage();
   * if (slowestPage != null) {
   *     System.out.println("Slowest page: " + slowestPage.getUrl() +
   *                       " (" + slowestPage.getDuration() + "ms)");
   * }
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetric getSlowestLoadingPage() {
    return stream().max((m1, m2) -> Long.compare(m1.getDuration(), m2.getDuration())).orElse(null);
  }

  /**
   * Finds the metric with the shortest load duration.
   *
   * @return the metric with the minimum load duration, or null if the collection is empty
   * @example
   *     <pre>{@code
   * CWebPageLoadMetric fastestPage = metrics.getFastestLoadingPage();
   * if (fastestPage != null) {
   *     System.out.println("Fastest page: " + fastestPage.getUrl() +
   *                       " (" + fastestPage.getDuration() + "ms)");
   * }
   * }</pre>
   *
   * @since 1.0
   */
  public CWebPageLoadMetric getFastestLoadingPage() {
    return stream().min((m1, m2) -> Long.compare(m1.getDuration(), m2.getDuration())).orElse(null);
  }

  /**
   * Checks if any metric in the collection represents a page that loaded slower than the threshold.
   *
   * @param thresholdMs the duration threshold in milliseconds
   * @return true if any page loaded slower than the threshold, false otherwise
   * @example
   *     <pre>{@code
   * if (metrics.hasSlowLoadingPages(3000)) {
   *     System.out.println("Warning: Some pages took more than 3 seconds to load!");
   * }
   * }</pre>
   *
   * @since 1.0
   */
  public boolean hasSlowLoadingPages(long thresholdMs) {
    return stream().anyMatch(metric -> metric.getDuration() > thresholdMs);
  }

  /**
   * Checks if all metrics in the collection represent pages that loaded within the threshold.
   *
   * @param thresholdMs the duration threshold in milliseconds
   * @return true if all pages loaded within the threshold, false otherwise
   * @example
   *     <pre>{@code
   * if (metrics.allLoadedWithinThreshold(2000)) {
   *     System.out.println("Great! All pages loaded within 2 seconds.");
   * }
   * }</pre>
   *
   * @since 1.0
   */
  public boolean allLoadedWithinThreshold(long thresholdMs) {
    return stream().allMatch(metric -> metric.getDuration() <= thresholdMs);
  }
}

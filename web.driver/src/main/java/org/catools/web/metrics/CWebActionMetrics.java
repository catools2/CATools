package org.catools.web.metrics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.catools.common.collections.CList;

/**
 * A specialized collection for managing and storing web action metrics during automated testing.
 * This class extends {@link CList} to provide a type-safe collection of {@link CWebActionMetric}
 * objects with additional functionality for web automation performance tracking and analysis.
 *
 * <p>The class inherits all the functionality from {@link CList} and {@link
 * org.catools.common.collections.interfaces.CCollection}, providing a rich set of methods for
 * collection manipulation, filtering, and transformation operations specifically tailored for web
 * action metrics.
 *
 * <p>This collection is particularly useful for:
 *
 * <ul>
 *   <li>Tracking performance metrics of individual web actions
 *   <li>Analyzing page transitions and their timing
 *   <li>Collecting and reporting on web automation test execution data
 *   <li>Performance monitoring and optimization of web automation scripts
 * </ul>
 *
 * <h3>Key Features:</h3>
 *
 * <ul>
 *   <li><strong>Type Safety:</strong> Ensures only {@link CWebActionMetric} objects are stored
 *   <li><strong>Enhanced Collection Operations:</strong> Inherits advanced filtering, partitioning,
 *       and transformation methods
 *   <li><strong>Fluent API:</strong> Supports method chaining for easy metric collection building
 *   <li><strong>Performance Analysis:</strong> Built-in methods for metric analysis and reporting
 * </ul>
 *
 * <h3>Usage Examples:</h3>
 *
 * <h4>Creating and populating a metrics collection:</h4>
 *
 * <pre>{@code
 * CWebActionMetrics metrics = new CWebActionMetrics();
 *
 * // Add individual metrics
 * CWebActionMetric clickMetric = new CWebActionMetric()
 *     .setName("Click Login Button")
 *     .setActionTime(new Date())
 *     .setDuration(250L);
 * metrics.add(clickMetric);
 *
 * // Add multiple metrics
 * CWebActionMetric fillFormMetric = new CWebActionMetric()
 *     .setName("Fill Registration Form")
 *     .setDuration(1200L);
 * CWebActionMetric submitMetric = new CWebActionMetric()
 *     .setName("Submit Form")
 *     .setDuration(800L);
 *
 * metrics.addAll(Arrays.asList(fillFormMetric, submitMetric));
 * }</pre>
 *
 * <h4>Filtering and analyzing metrics:</h4>
 *
 * <pre>{@code
 * CWebActionMetrics metrics = new CWebActionMetrics();
 * // ... populate with metrics ...
 *
 * // Filter slow actions (> 1 second)
 * CWebActionMetrics slowActions = metrics.stream()
 *     .filter(metric -> metric.getDuration() > 1000)
 *     .collect(CList::new, CList::add, CList::addAll);
 *
 * // Find actions by name pattern
 * CWebActionMetrics clickActions = metrics.stream()
 *     .filter(metric -> metric.getName().toLowerCase().contains("click"))
 *     .collect(CList::new, CList::add, CList::addAll);
 *
 * // Get average action duration
 * double averageDuration = metrics.stream()
 *     .mapToLong(CWebActionMetric::getDuration)
 *     .average()
 *     .orElse(0.0);
 *
 * System.out.println("Average action duration: " + averageDuration + "ms");
 * }</pre>
 *
 * <h4>Partitioning metrics for batch processing:</h4>
 *
 * <pre>{@code
 * CWebActionMetrics allMetrics = new CWebActionMetrics();
 * // ... populate with many metrics ...
 *
 * // Process metrics in batches of 10
 * CList<CList<CWebActionMetric>> batches = allMetrics.partition(10);
 * for (CList<CWebActionMetric> batch : batches) {
 *     processBatch(batch);
 * }
 * }</pre>
 *
 * <h4>Converting to different data structures:</h4>
 *
 * <pre>{@code
 * CWebActionMetrics metrics = new CWebActionMetrics();
 * // ... populate with metrics ...
 *
 * // Convert to map for quick lookup by action name
 * CMap<String, CWebActionMetric> metricsByName = metrics.toMap(
 *     CWebActionMetric::getName,
 *     metric -> metric
 * );
 *
 * // Convert to map of action names to durations
 * CMap<String, Long> durationsByName = metrics.toMap(
 *     CWebActionMetric::getName,
 *     CWebActionMetric::getDuration
 * );
 * }</pre>
 *
 * <h4>Working with empty collections and random access:</h4>
 *
 * <pre>{@code
 * CWebActionMetrics metrics = new CWebActionMetrics();
 *
 * if (metrics.isEmpty()) {
 *     System.out.println("No metrics collected yet");
 * }
 *
 * // Add some metrics...
 * metrics.add(new CWebActionMetric().setName("Test Action").setDuration(500L));
 *
 * // Get a random metric for sampling
 * CWebActionMetric randomMetric = metrics.getAny();
 * if (randomMetric != null) {
 *     System.out.println("Random metric: " + randomMetric.getName());
 * }
 *
 * // Get and remove a random metric
 * CWebActionMetric sampledMetric = metrics.getAnyAndRemove();
 * }</pre>
 *
 * <h4>Integration with web automation frameworks:</h4>
 *
 * <pre>{@code
 * public class WebTestRunner {
 *     private CWebActionMetrics testMetrics = new CWebActionMetrics();
 *
 *     public void performAction(String actionName, Runnable action) {
 *         Date startTime = new Date();
 *         CWebPageInfo beforePage = capturePageInfo();
 *
 *         // Perform the actual web action
 *         action.run();
 *
 *         CWebPageInfo afterPage = capturePageInfo();
 *         long duration = System.currentTimeMillis() - startTime.getTime();
 *
 *         // Record the metric
 *         CWebActionMetric metric = new CWebActionMetric()
 *             .setName(actionName)
 *             .setPageBeforeAction(beforePage)
 *             .setPageAfterAction(afterPage)
 *             .setActionTime(startTime)
 *             .setDuration(duration);
 *
 *         testMetrics.add(metric);
 *     }
 *
 *     public CWebActionMetrics getTestMetrics() {
 *         return testMetrics;
 *     }
 * }
 * }</pre>
 *
 * @author CATools
 * @version 1.0
 * @see CWebActionMetric
 * @see CList
 * @see org.catools.common.collections.interfaces.CCollection
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CWebActionMetrics extends CList<CWebActionMetric> {}

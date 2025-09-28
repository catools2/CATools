package org.catools.web.metrics;

import lombok.Data;
import org.catools.common.collections.CList;

/**
 * A specialized collection class that manages a list of web page transition information objects.
 * This class extends {@link CList} to provide a type-safe collection for storing and managing
 * {@link CWebPageTransitionInfo} objects that represent transitions between web pages along with
 * associated performance metrics and timing data.
 * 
 * <p>This class is particularly useful for web automation testing scenarios where you need to
 * track multiple page transitions, collect performance metrics, and analyze user navigation
 * patterns across different pages in a web application.</p>
 * 
 * <p>The class leverages Lombok's {@code @Data} annotation to automatically generate getter/setter
 * methods, equals(), hashCode(), and toString() methods.</p>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Basic Usage - Creating and Adding Transitions:</h4>
 * <pre>{@code
 * // Create a new transitions collection
 * CWebPageTransitionsInfo transitions = new CWebPageTransitionsInfo();
 * 
 * // Create page info objects
 * CWebPageInfo homePage = new CWebPageInfo("Home", "https://example.com/home");
 * CWebPageInfo loginPage = new CWebPageInfo("Login", "https://example.com/login");
 * 
 * // Create and add a transition
 * CWebPageTransitionInfo loginTransition = new CWebPageTransitionInfo(
 *     "Navigate to Login", homePage, loginPage);
 * transitions.add(loginTransition);
 * 
 * // Check if any transitions were recorded
 * if (!transitions.isEmpty()) {
 *     System.out.println("Recorded " + transitions.size() + " transitions");
 * }
 * }</pre>
 * 
 * <h4>Creating Transitions with Performance Metrics:</h4>
 * <pre>{@code
 * // Create transitions collection
 * CWebPageTransitionsInfo performanceTransitions = new CWebPageTransitionsInfo();
 * 
 * // Sample metrics (in real scenario, these would come from browser DevTools)
 * CList<Metric> metrics = new CList<>();
 * // metrics would be populated with actual performance data
 * 
 * // Create transition with metrics and timing
 * Date transitionTime = new Date();
 * CWebPageTransitionInfo timedTransition = new CWebPageTransitionInfo(
 *     "Form Submission", currentPage, resultPage, metrics, transitionTime);
 * 
 * performanceTransitions.add(timedTransition);
 * }</pre>
 * 
 * <h4>Analyzing Transitions:</h4>
 * <pre>{@code
 * CWebPageTransitionsInfo userJourney = new CWebPageTransitionsInfo();
 * // ... populate with transitions
 * 
 * // Find transitions to specific pages
 * List<CWebPageTransitionInfo> loginTransitions = userJourney.stream()
 *     .filter(t -> t.getUrlAfterAction().contains("/login"))
 *     .toList();
 * 
 * // Get all unique page titles visited
 * Set<String> visitedPages = userJourney.stream()
 *     .map(CWebPageTransitionInfo::getTitleAfterAction)
 *     .filter(Objects::nonNull)
 *     .collect(Collectors.toSet());
 * 
 * // Calculate total transitions
 * System.out.println("Total page transitions: " + userJourney.size());
 * System.out.println("Unique pages visited: " + visitedPages.size());
 * }</pre>
 * 
 * <h4>Working with Transition Metrics:</h4>
 * <pre>{@code
 * CWebPageTransitionsInfo metricsAnalysis = new CWebPageTransitionsInfo();
 * // ... populate with transitions containing metrics
 * 
 * // Find transitions with performance issues (e.g., slow load times)
 * metricsAnalysis.stream()
 *     .filter(t -> {
 *         Number loadTime = t.getMetrics().get("loadEventEnd");
 *         return loadTime != null && loadTime.doubleValue() > 2000; // > 2 seconds
 *     })
 *     .forEach(t -> System.out.println("Slow transition: " + t.getActionName()));
 * 
 * // Calculate average transition time
 * OptionalDouble avgTime = metricsAnalysis.stream()
 *     .filter(t -> t.getActionTime() != null)
 *     .mapToLong(t -> t.getActionTime().getTime())
 *     .average();
 * }</pre>
 * 
 * <h4>Bulk Operations:</h4>
 * <pre>{@code
 * // Create from existing collection
 * List<CWebPageTransitionInfo> existingTransitions = Arrays.asList(
 *     transition1, transition2, transition3);
 * CWebPageTransitionsInfo bulkTransitions = new CWebPageTransitionsInfo();
 * bulkTransitions.addAll(existingTransitions);
 * 
 * // Or use CList factory methods inherited from parent
 * CWebPageTransitionsInfo factoryCreated = CList.of(transition1, transition2);
 * 
 * // Filter and create new collection
 * CWebPageTransitionsInfo errorTransitions = new CWebPageTransitionsInfo();
 * bulkTransitions.stream()
 *     .filter(t -> t.getUrlAfterAction().contains("/error"))
 *     .forEach(errorTransitions::add);
 * }</pre>
 * 
 * @author CA Tools Team
 * @since 1.0
 * @see CWebPageTransitionInfo
 * @see CList
 * @see org.catools.web.entities.CWebPageInfo
 */
@Data
public class CWebPageTransitionsInfo extends CList<CWebPageTransitionInfo> {
}

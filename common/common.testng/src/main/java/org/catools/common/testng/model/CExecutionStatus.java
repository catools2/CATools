package org.catools.common.testng.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.collections.CSet;

/**
 * Enumeration representing different execution statuses for test cases or test runs. Each status
 * has an associated priority ID that indicates its importance level.
 *
 * <p>This enum provides utility methods to categorize statuses into logical groups such as running,
 * failed, skipped, etc., which helps in test result analysis and reporting.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>{@code
 * // Check if a test is currently running
 * CExecutionStatus status = CExecutionStatus.WIP;
 * if (status.isRunning()) {
 *     System.out.println("Test is in progress");
 * }
 *
 * // Check if a test has passed
 * CExecutionStatus testResult = CExecutionStatus.SUCCESS;
 * if (testResult.isPassed()) {
 *     System.out.println("Test passed successfully");
 * }
 *
 * // Get the priority ID of a status
 * int priorityId = CExecutionStatus.FAILURE.getId();
 * System.out.println("Failure priority: " + priorityId); // Output: 40
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
@AllArgsConstructor
public enum CExecutionStatus {
  /** Test execution completed successfully with all assertions passing (Priority: 50) */
  SUCCESS(50),

  /** Test execution failed due to assertion failures or exceptions (Priority: 40) */
  FAILURE(40),

  /** Test is currently in progress or work in progress (Priority: 20) */
  WIP(20),

  /** Test was skipped during execution (Priority: 10) */
  SKIP(10),

  /** Test was ignored and not executed (Priority: 4) */
  IGNORED(4),

  /** Test execution was deferred to a later time (Priority: 3) */
  DEFERRED(3),

  /** Test execution is blocked by external dependencies (Priority: 2) */
  BLOCKED(2),

  /** Test is awaiting execution or resources (Priority: 1) */
  AWAITING(1),

  /** Test passed but with some percentage of failures in sub-components (Priority: 51) */
  SUCCESS_PERCENTAGE_FAILURE(51),

  /** Test has been created but not yet executed (Priority: 0) */
  CREATED(0);

  @Getter private int id;

  /**
   * Determines if the execution status represents a currently running test.
   *
   * <p>A test is considered running if it has the status {@link #WIP} (Work In Progress).
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * CExecutionStatus status = CExecutionStatus.WIP;
   * if (status.isRunning()) {
   *     System.out.println("Test is currently executing");
   *     // Take appropriate action for running tests
   * }
   *
   * CExecutionStatus completedStatus = CExecutionStatus.SUCCESS;
   * if (!completedStatus.isRunning()) {
   *     System.out.println("Test has completed");
   * }
   * }</pre>
   *
   * @return {@code true} if the status is {@link #WIP}, {@code false} otherwise
   */
  public boolean isRunning() {
    return new CSet<>(WIP).contains(this);
  }

  /**
   * Determines if the execution status represents a failed test.
   *
   * <p>A test is considered failed if it has the status {@link #FAILURE}.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * CExecutionStatus status = CExecutionStatus.FAILURE;
   * if (status.isFailed()) {
   *     System.out.println("Test failed - investigating issues");
   *     // Log failure details, send notifications, etc.
   * }
   *
   * // Check multiple statuses
   * List<CExecutionStatus> testResults = Arrays.asList(
   *     CExecutionStatus.SUCCESS,
   *     CExecutionStatus.FAILURE,
   *     CExecutionStatus.SKIP
   * );
   * long failedCount = testResults.stream()
   *     .filter(CExecutionStatus::isFailed)
   *     .count();
   * System.out.println("Failed tests: " + failedCount);
   * }</pre>
   *
   * @return {@code true} if the status is {@link #FAILURE}, {@code false} otherwise
   */
  public boolean isFailed() {
    return new CSet<>(FAILURE).contains(this);
  }

  /**
   * Determines if the execution status represents a skipped test.
   *
   * <p>A test is considered skipped if it has one of the following statuses:
   *
   * <ul>
   *   <li>{@link #SKIP} - Test was explicitly skipped during execution
   *   <li>{@link #IGNORED} - Test was marked to be ignored
   *   <li>{@link #DEFERRED} - Test execution was postponed
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * CExecutionStatus skipStatus = CExecutionStatus.SKIP;
   * CExecutionStatus ignoredStatus = CExecutionStatus.IGNORED;
   * CExecutionStatus deferredStatus = CExecutionStatus.DEFERRED;
   *
   * if (skipStatus.isSkipped()) {
   *     System.out.println("Test was skipped");
   * }
   *
   * // Generate report for skipped tests
   * List<CExecutionStatus> allStatuses = Arrays.asList(values());
   * List<CExecutionStatus> skippedStatuses = allStatuses.stream()
   *     .filter(CExecutionStatus::isSkipped)
   *     .collect(Collectors.toList());
   *
   * System.out.println("Skipped status types: " + skippedStatuses);
   * // Output: [SKIP, IGNORED, DEFERRED]
   * }</pre>
   *
   * @return {@code true} if the status is one of {@link #SKIP}, {@link #IGNORED}, or {@link
   *     #DEFERRED}, {@code false} otherwise
   */
  public boolean isSkipped() {
    return new CSet<>(SKIP, IGNORED, DEFERRED).contains(this);
  }

  /**
   * Determines if the execution status represents a test that is awaiting execution.
   *
   * <p>A test is considered awaiting if it has the status {@link #AWAITING}, indicating it is
   * queued for execution but has not yet started.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * CExecutionStatus status = CExecutionStatus.AWAITING;
   * if (status.isAwaiting()) {
   *     System.out.println("Test is in the execution queue");
   *     // Display in pending tests section of UI
   *     // Estimate execution time, etc.
   * }
   *
   * // Monitor test queue
   * List<CExecutionStatus> testQueue = getTestStatuses();
   * long awaitingCount = testQueue.stream()
   *     .filter(CExecutionStatus::isAwaiting)
   *     .count();
   * System.out.println("Tests awaiting execution: " + awaitingCount);
   * }</pre>
   *
   * @return {@code true} if the status is {@link #AWAITING}, {@code false} otherwise
   */
  public boolean isAwaiting() {
    return new CSet<>(AWAITING).contains(this);
  }

  /**
   * Determines if the execution status represents a blocked test.
   *
   * <p>A test is considered blocked if it has the status {@link #BLOCKED}, indicating that external
   * dependencies or conditions prevent the test from executing.
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * CExecutionStatus status = CExecutionStatus.BLOCKED;
   * if (status.isBlocked()) {
   *     System.out.println("Test is blocked by external dependencies");
   *     // Check database connectivity, service availability, etc.
   *     // Send alerts to operations team
   * }
   *
   * // Identify blocked tests for resolution
   * Map<String, CExecutionStatus> testResults = getTestResults();
   * List<String> blockedTests = testResults.entrySet().stream()
   *     .filter(entry -> entry.getValue().isBlocked())
   *     .map(Map.Entry::getKey)
   *     .collect(Collectors.toList());
   *
   * if (!blockedTests.isEmpty()) {
   *     System.out.println("Blocked tests requiring attention: " + blockedTests);
   * }
   * }</pre>
   *
   * @return {@code true} if the status is {@link #BLOCKED}, {@code false} otherwise
   */
  public boolean isBlocked() {
    return new CSet<>(BLOCKED).contains(this);
  }

  /**
   * Determines if the execution status represents a passed test.
   *
   * <p>A test is considered passed if it has one of the following statuses:
   *
   * <ul>
   *   <li>{@link #SUCCESS} - Test completed successfully with all assertions passing
   *   <li>{@link #SUCCESS_PERCENTAGE_FAILURE} - Test passed overall but with some percentage of
   *       sub-component failures
   * </ul>
   *
   * <h3>Usage Example:</h3>
   *
   * <pre>{@code
   * CExecutionStatus successStatus = CExecutionStatus.SUCCESS;
   * CExecutionStatus partialSuccessStatus = CExecutionStatus.SUCCESS_PERCENTAGE_FAILURE;
   *
   * if (successStatus.isPassed()) {
   *     System.out.println("Test passed completely");
   * }
   *
   * if (partialSuccessStatus.isPassed()) {
   *     System.out.println("Test passed with some sub-component issues");
   * }
   *
   * // Calculate pass rate
   * List<CExecutionStatus> testResults = getTestResults();
   * long passedCount = testResults.stream()
   *     .filter(CExecutionStatus::isPassed)
   *     .count();
   * double passRate = (double) passedCount / testResults.size() * 100;
   * System.out.printf("Pass rate: %.2f%%\n", passRate);
   *
   * // Generate success report
   * Map<Boolean, List<CExecutionStatus>> partitioned = testResults.stream()
   *     .collect(Collectors.partitioningBy(CExecutionStatus::isPassed));
   * System.out.println("Passed: " + partitioned.get(true).size());
   * System.out.println("Failed: " + partitioned.get(false).size());
   * }</pre>
   *
   * @return {@code true} if the status is {@link #SUCCESS} or {@link #SUCCESS_PERCENTAGE_FAILURE},
   *     {@code false} otherwise
   */
  public boolean isPassed() {
    return new CSet<>(SUCCESS, SUCCESS_PERCENTAGE_FAILURE).contains(this);
  }
}

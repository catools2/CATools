package org.catools.common.tests.exception;

import org.catools.common.exception.CRuntimeException;

/**
 * Signals that a test class contains an issue awaiting some action and due to the test configuration
 * it should be skipped. This exception is typically thrown when a test encounters a condition that
 * requires manual intervention or external resolution before the test can proceed.
 * 
 * <p>This exception extends {@link CRuntimeException} and is used in test scenarios where:
 * <ul>
 *   <li>A test depends on an external service that is temporarily unavailable</li>
 *   <li>A known issue exists that is awaiting a fix</li>
 *   <li>Environmental conditions prevent the test from running reliably</li>
 * </ul>
 * 
 * <p><strong>Example usage:</strong>
 * <pre>{@code
 * @Test
 * public void testDatabaseConnection() {
 *     if (!isDatabaseAvailable()) {
 *         throw new CSkipAwaitingTestException("Database connection unavailable - awaiting infrastructure fix");
 *     }
 *     // Test logic here
 * }
 * 
 * @Test
 * public void testFeatureX() {
 *     if (hasKnownIssue("TICKET-123")) {
 *         throw new CSkipAwaitingTestException("Feature X has known issue TICKET-123 - awaiting development fix");
 *     }
 *     // Test logic here
 * }
 * }</pre>
 * 
 * @see CRuntimeException
 * @since 1.0
 */
public class CSkipAwaitingTestException extends CRuntimeException {

  /**
   * Constructs a new CSkipAwaitingTestException with the specified description.
   * 
   * <p>The description should clearly indicate why the test is being skipped
   * and what action is being awaited to resolve the issue.
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * // Good description - specific and actionable
   * throw new CSkipAwaitingTestException("API endpoint /users unavailable - awaiting service deployment");
   * 
   * // Another good example
   * throw new CSkipAwaitingTestException("Test requires SSL certificate renewal - awaiting ops team action");
   * }</pre>
   * 
   * @param description a detailed message describing the reason for skipping the test
   *                   and what action is being awaited
   */
  public CSkipAwaitingTestException(String description) {
    super(description);
  }
}

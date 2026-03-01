package org.catools.common.annotations;

import static java.lang.annotation.ElementType.METHOD;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The {@code CAwaiting} annotation is used to mark test methods that are currently failing and
 * awaiting action from someone or need investigation. This annotation helps identify tests that
 * require attention but are not necessarily due to known defects.
 *
 * <p>This annotation is particularly useful for:
 *
 * <ul>
 *   <li>Tests that fail intermittently and need investigation
 *   <li>Tests waiting for external resources or environment setup
 *   <li>Tests that fail due to unclear requirements or specifications
 *   <li>Tests that need refactoring or redesign
 * </ul>
 *
 * <p><strong>Usage Guidelines:</strong>
 *
 * <ul>
 *   <li>Use the {@code CORE_SKIP_CLASS_WITH_AWAITING_TEST} flag to avoid running tests with {@code
 *       CAwaiting} annotation in CI/CD pipelines
 *   <li>If test fails due to a known defect, use {@link COpenDefects} annotation instead
 *   <li>When a defect is fixed, replace {@link COpenDefects} with {@link CDefects} to maintain
 *       visibility on all related defects for faster root cause identification
 *   <li>Always provide a clear and descriptive cause in the {@code cause()} parameter
 * </ul>
 *
 * <p><strong>Examples:</strong>
 *
 * <pre>{@code
 * // Example 1: Test waiting for environment setup
 * @Test
 * @CAwaiting(cause = "Test database not configured in staging environment")
 * public void testUserRegistration() {
 *     // Test implementation that depends on database
 * }
 *
 * // Example 2: Intermittent failure requiring investigation
 * @Test
 * @CAwaiting(cause = "Intermittent timeout - needs investigation of network latency")
 * public void testApiResponseTime() {
 *     // Test that occasionally fails due to timing issues
 * }
 *
 * // Example 3: Unclear requirements
 * @Test
 * @CAwaiting(cause = "Business rules for edge case validation not defined")
 * public void testEdgeCaseValidation() {
 *     // Test waiting for clarification on business requirements
 * }
 *
 * // Example 4: External dependency issue
 * @Test
 * @CAwaiting(cause = "Third-party service API changes - awaiting documentation update")
 * public void testExternalServiceIntegration() {
 *     // Test affected by external API changes
 * }
 * }</pre>
 *
 * <p><strong>Migration Path:</strong>
 *
 * <pre>{@code
 * // When issue is identified as a defect:
 * @Test
 * @COpenDefects(defects = {"DEF-12345"})
 * public void testSomeFeature() { ... }
 *
 * // After defect is fixed:
 * @Test
 * @CDefects(defects = {"DEF-12345"})
 * public void testSomeFeature() { ... }
 * }</pre>
 *
 * @since 1.0
 * @see CDeferred for tests that are deferred to future releases
 * @see CIgnored for tests that are intentionally ignored
 * @see CDefects for tests related to resolved defects
 * @see COpenDefects for tests related to open defects
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CAwaiting {
  /**
   * Specifies the reason why the test is awaiting action or investigation. This should provide
   * clear and actionable information about what needs to be done or what issue needs to be
   * resolved.
   *
   * <p>The cause should be descriptive enough for other team members to understand the context and
   * take appropriate action without additional investigation.
   *
   * <p><strong>Good Examples:</strong>
   *
   * <ul>
   *   <li>{@code "Database migration script missing for test data setup"}
   *   <li>{@code "API endpoint returns 500 error - server logs show connection timeout"}
   *   <li>{@code "Business requirements unclear for multi-currency calculations"}
   *   <li>{@code "External mock service not available in test environment"}
   *   <li>{@code "Performance threshold needs to be defined by product team"}
   * </ul>
   *
   * <p><strong>Avoid Vague Descriptions:</strong>
   *
   * <ul>
   *   <li>{@code "Broken"} - Too vague, doesn't explain what's broken
   *   <li>{@code "Fails sometimes"} - Doesn't provide actionable information
   *   <li>{@code "TODO"} - Doesn't explain what needs to be done
   * </ul>
   *
   * @return a descriptive string explaining why the test is awaiting action
   * @example
   *     <pre>{@code
   * @CAwaiting(cause = "Login API returns inconsistent response format - needs backend team review")
   * }</pre>
   */
  @JsonProperty("cause")
  String cause();
}

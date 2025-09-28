package org.catools.common.annotations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * This annotation {@link CDefects} is used to link test methods to related defects that have been
 * fixed or no longer reproduce. It provides traceability between test cases and resolved issues
 * from bug tracking systems.
 *
 * <p>This annotation is typically used in test automation frameworks to:
 * <ul>
 *   <li>Document historical defects that were fixed and verify they don't regress</li>
 *   <li>Maintain traceability between test cases and resolved bug reports</li>
 *   <li>Provide context for regression testing scenarios</li>
 *   <li>Track defect resolution validation in test reports</li>
 * </ul>
 *
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use this annotation when defects have been resolved and tests should pass</li>
 *   <li>If test fails due to known defect, use {@link COpenDefects} annotation instead</li>
 *   <li>If defect is deferred, use {@link CDeferred} annotation instead</li>
 *   <li>Include specific defect IDs from your bug tracking system (e.g., JIRA, GitHub Issues)</li>
 * </ul>
 *
 * <p><strong>Examples:</strong>
 * <pre>{@code
 * // Single fixed defect - test should now pass
 * @CDefects(ids = {"BUG-1234"})
 * @Test
 * public void testUserLoginWithValidCredentials() {
 *     // This test validates that BUG-1234 is fixed
 *     // Test should pass consistently
 * }
 *
 * // Multiple fixed defects covered by one test
 * @CDefects(ids = {"BUG-1234", "BUG-5678"})
 * @Test
 * public void testUserAccountManagement() {
 *     // This test validates fixes for multiple related defects
 *     // All listed defects should be resolved
 * }
 *
 * // Regression test for critical fixed defect
 * @CDefects(ids = {"CRITICAL-999"})
 * @Test
 * public void testPaymentProcessingRegression() {
 *     // Ensures critical payment bug doesn't regress
 *     // Test failure would indicate regression
 * }
 *
 * // Using with different bug tracking systems
 * @CDefects(ids = {"PROJ-123", "GitHub-456", "ServiceNow-789"})
 * @Test
 * public void testCrossSystemIntegration() {
 *     // Test covering defects from multiple tracking systems
 * }
 * }</pre>
 *
 * <p><strong>Migration Examples:</strong>
 * <pre>{@code
 * // Before: Test was failing due to open defect
 * @COpenDefects(ids = {"BUG-1234"})
 * @Test
 * public void testFeature() { ... }
 *
 * // After: Defect is fixed, update annotation
 * @CDefects(ids = {"BUG-1234"})
 * @Test
 * public void testFeature() { ... }
 * }</pre>
 *
 * @see COpenDefects for current failing defects
 * @see CDeferred for postponed defects
 * @see CIgnored for ignored test cases
 * @see CAwaiting for tests waiting on external dependencies
 * @since 1.0
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CDefects {
  @JsonProperty("ids") String[] ids();
}

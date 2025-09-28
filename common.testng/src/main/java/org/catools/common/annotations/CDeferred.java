package org.catools.common.annotations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Annotation to mark test methods that are associated with defects that have been deferred 
 * to a future release or version. This annotation helps maintain traceability between test
 * cases and known issues that have been postponed for resolution.
 *
 * <p>It is not a common case when we want to execute failing test cases but in large projects
 * it happens that teams lose the trace of defects and it is not clear if the defect is fixed
 * or if it still reproduces and what is the impact. In poorly managed processes you might have
 * to execute failing tests to see if defects are gone or not.
 *
 * <p>This annotation {@link CDeferred} is used to link tests with related defects which have
 * not been fixed and deferred for a version or more. In case the test scenario passes we mark
 * it at the end as passed but if it fails then we mark it as deferred and put the related id
 * in the report.
 *
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>If test fails due to known defect then we should use {@link COpenDefects} annotation instead</li>
 *   <li>If deferred defect is fixed then we should move it to {@link CDefects} annotation and remove this from test</li>
 *   <li>Use this annotation when defects are confirmed but postponed to future releases</li>
 *   <li>Always provide meaningful defect IDs that can be traced in your defect tracking system</li>
 * </ul>
 *
 * <p><strong>Examples:</strong>
 * <pre>{@code
 * // Single deferred defect
 * @CDeferred(ids = {"DEF-1234"})
 * @Test
 * public void testUserLoginWithSpecialCharacters() {
 *     // Test implementation that may fail due to deferred defect DEF-1234
 *     // This test will be marked as deferred if it fails
 * }
 *
 * // Multiple deferred defects
 * @CDeferred(ids = {"DEF-2345", "DEF-2346"})
 * @Test
 * public void testComplexWorkflow() {
 *     // Test implementation that may fail due to multiple deferred defects
 *     // Both defect IDs will be reported if test fails
 * }
 *
 * // Integration with other annotations
 * @CDeferred(ids = {"DEF-3456"})
 * @Test(groups = {"regression"})
 * public void testReportGeneration() {
 *     // Test that belongs to regression group but has deferred defect
 * }
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 * @see CDefects For tests with fixed defects
 * @see CIgnored For tests that should be ignored
 * @see CAwaiting For tests waiting for external dependencies
 * @see COpenDefects For tests with known open defects
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CDeferred {
  /**
   * Array of defect IDs that have been deferred and are associated with this test.
   * 
   * <p>These IDs should correspond to defects in your defect tracking system
   * (e.g., JIRA, Bugzilla, etc.) that have been identified but postponed for
   * resolution to a future release or version.
   * 
   * <p><strong>Examples:</strong>
   * <ul>
   *   <li>{@code ids = {"DEF-1234"}} - Single deferred defect</li>
   *   <li>{@code ids = {"BUG-5678", "ISSUE-9012"}} - Multiple deferred defects</li>
   *   <li>{@code ids = {"JIRA-ABC-123"}} - JIRA ticket reference</li>
   * </ul>
   *
   * @return array of defect identifiers that are deferred
   */
  @JsonProperty("ids") String[] ids();
}

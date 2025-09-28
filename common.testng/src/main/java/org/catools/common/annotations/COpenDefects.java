package org.catools.common.annotations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * This annotation {@link COpenDefects} is used to link test methods to related open defects.
 * It helps track which tests are expected to fail due to known issues that are still being worked on.
 *
 * <p>This annotation is typically used in test automation frameworks to:
 * <ul>
 *   <li>Document known failures in test cases</li>
 *   <li>Track defect IDs that are preventing tests from passing</li>
 *   <li>Provide traceability between test failures and bug tracking systems</li>
 * </ul>
 *
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>If defect is fixed, remove {@link COpenDefects} and add the fixed defect ID
 *       to {@code @CDefects} annotation instead</li>
 *   <li>If defect is deferred for short term, change {@link COpenDefects} to {@link CDeferred}</li>
 *   <li>Use specific defect IDs from your bug tracking system (e.g., JIRA, GitHub Issues)</li>
 * </ul>
 *
 * <p><strong>Examples:</strong>
 * <pre>{@code
 * // Single open defect
 * @COpenDefects(ids = {"BUG-1234"})
 * @Test
 * public void testUserLoginWithInvalidCredentials() {
 *     // Test that is currently failing due to BUG-1234
 * }
 *
 * // Multiple open defects affecting the same test
 * @COpenDefects(ids = {"BUG-1234", "BUG-5678"})
 * @Test
 * public void testComplexUserWorkflow() {
 *     // Test affected by multiple known issues
 * }
 *
 * // Using with different bug tracking systems
 * @COpenDefects(ids = {"PROJ-123", "GitHub-456"})
 * @Test
 * public void testFeatureIntegration() {
 *     // Test with defects from different tracking systems
 * }
 * }</pre>
 *
 * @see CDeferred for short-term deferred defects
 * @see CAwaiting for tests waiting on external dependencies
 * @since 1.0
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface COpenDefects {
  /**
   * Specifies the array of defect identifiers that are currently open and affecting this test.
   * 
   * <p>The defect IDs should correspond to entries in your bug tracking system and should be
   * specific enough to uniquely identify the defect. Common formats include:
   * <ul>
   *   <li>JIRA ticket IDs (e.g., "PROJ-1234", "BUG-5678")</li>
   *   <li>GitHub issue numbers (e.g., "GH-123", "Issue-456")</li>
   *   <li>Azure DevOps work item IDs (e.g., "ADO-789")</li>
   *   <li>Custom internal defect tracking IDs</li>
   * </ul>
   *
   * <p><strong>Best Practices:</strong>
   * <ul>
   *   <li>Use consistent naming conventions across your project</li>
   *   <li>Include the tracking system prefix for clarity</li>
   *   <li>Keep IDs up-to-date as defects are resolved</li>
   *   <li>Remove this annotation when all referenced defects are fixed</li>
   * </ul>
   *
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * // Single defect ID
   * @COpenDefects(ids = {"JIRA-1234"})
   * 
   * // Multiple defect IDs
   * @COpenDefects(ids = {"BUG-001", "BUG-002", "ENHANCEMENT-123"})
   * 
   * // Mixed tracking systems
   * @COpenDefects(ids = {"JIRA-1234", "GitHub-567", "Internal-DEF-890"})
   * }</pre>
   *
   * @return an array of strings representing the open defect identifiers
   */
  @JsonProperty("ids") String[] ids();
}

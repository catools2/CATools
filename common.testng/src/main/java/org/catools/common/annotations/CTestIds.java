package org.catools.common.annotations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Annotation to mark test methods with unique identifiers for traceability and test management.
 * 
 * <p>The {@code CTestIds} annotation is used to associate one or more unique identifiers with test methods,
 * enabling better test traceability, reporting, and execution control. These identifiers are typically
 * external reference numbers such as Jira ticket numbers, test case IDs, or requirement numbers.
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><b>Traceability:</b> Links test methods to external requirements or defect tracking systems</li>
 *   <li><b>Test Suite Generation:</b> Used to generate TestNG test suites for specific scenarios</li>
 *   <li><b>Command Line Execution:</b> Enables selective test execution via command line parameters</li>
 *   <li><b>Reporting:</b> Provides clear test identification in test reports and logs</li>
 *   <li><b>Multiple IDs:</b> Supports multiple identifiers per test method</li>
 * </ul>
 *
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Single Test ID:</h4>
 * <pre>{@code
 * @Test
 * @CTestIds(ids = {"JIRA-12345"})
 * public void testUserLogin() {
 *     // Test implementation
 * }
 * }</pre>
 * 
 * <h4>Multiple Test IDs:</h4>
 * <pre>{@code
 * @Test
 * @CTestIds(ids = {"JIRA-12345", "REQ-001", "TC-LOGIN-01"})
 * public void testComplexUserAuthentication() {
 *     // Test implementation covering multiple requirements
 * }
 * }</pre>
 * 
 * <h4>Integration with Other Annotations:</h4>
 * <pre>{@code
 * @Test
 * @CTestIds(ids = {"JIRA-54321"})
 * @CDeferred(reason = "Waiting for API fix")
 * public void testPaymentProcessing() {
 *     // Deferred test with traceability
 * }
 * 
 * @Test
 * @CTestIds(ids = {"JIRA-98765", "BUG-456"})
 * @COpenDefects(defects = {"DEF-001"})
 * public void testKnownIssue() {
 *     // Test with known defects and traceability
 * }
 * }</pre>
 * 
 * <h4>Parameterized Tests:</h4>
 * <pre>{@code
 * @Test(dataProvider = "loginData")
 * @CTestIds(ids = {"JIRA-11111", "PARAM-LOGIN-01"})
 * public void testParameterizedLogin(String username, String password) {
 *     // Parameterized test with consistent ID across all parameter sets
 * }
 * }</pre>
 *
 * <h3>Best Practices:</h3>
 * <ul>
 *   <li><b>Unique IDs:</b> Ensure each test has unique identifiers to avoid conflicts</li>
 *   <li><b>Consistent Naming:</b> Use consistent naming conventions (e.g., "JIRA-12345", "REQ-001")</li>
 *   <li><b>Meaningful IDs:</b> Use descriptive identifiers that relate to business requirements</li>
 *   <li><b>Documentation:</b> Document the meaning and source of ID formats in your team</li>
 *   <li><b>Version Control:</b> Track ID changes in version control for audit trails</li>
 * </ul>
 *
 * <h3>Integration Notes:</h3>
 * <p>This annotation is processed at runtime and can be used by:</p>
 * <ul>
 *   <li>Test execution frameworks to filter and select specific tests</li>
 *   <li>Reporting tools to correlate test results with requirements</li>
 *   <li>CI/CD pipelines to execute targeted test subsets</li>
 *   <li>Test management tools for traceability matrices</li>
 * </ul>
 *
 * @since 1.0
 * @see CDeferred For marking deferred tests
 * @see CAwaiting For tests awaiting external dependencies
 * @see COpenDefects For tests with known defects
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CTestIds {
  /**
   * Returns the array of test identifiers associated with the annotated test method.
   * 
   * <p>Each identifier should be unique within the test suite to ensure proper traceability
   * and avoid conflicts during test execution and reporting.
   * 
   * <h4>Examples of valid identifier formats:</h4>
   * <ul>
   *   <li><code>"JIRA-12345"</code> - Jira ticket number</li>
   *   <li><code>"REQ-001"</code> - Requirement identifier</li>
   *   <li><code>"TC-LOGIN-01"</code> - Test case identifier</li>
   *   <li><code>"BUG-456"</code> - Bug report number</li>
   *   <li><code>"STORY-789"</code> - User story identifier</li>
   * </ul>
   *
   * @return array of test identifiers, must not be null or empty
   * @since 1.0
   */
  @JsonProperty("ids") String[] ids();
}

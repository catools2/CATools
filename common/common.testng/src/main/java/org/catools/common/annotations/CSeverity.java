package org.catools.common.annotations;

import static java.lang.annotation.ElementType.METHOD;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.catools.common.testng.listeners.CIMethodInterceptor;

/**
 * The {@code CSeverity} annotation is used to mark test methods with a severity level, which
 * determines whether the test should be executed based on the configured {@code
 * TESTNG_RUN_SEVERITY_LEVEL} environment variable or system property.
 *
 * <p>This annotation enables selective test execution based on severity levels, allowing teams to
 * run only critical tests in time-constrained scenarios or include all tests when time permits.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>{@code
 * // Critical test - always runs when severity filtering is enabled
 * @Test
 * @CSeverity(level = 1)
 * public void testCriticalUserLogin() {
 *     // Critical functionality test
 * }
 *
 * // Medium priority test - runs when TESTNG_RUN_SEVERITY_LEVEL >= 2
 * @Test
 * @CSeverity(level = 2)
 * public void testUserProfileUpdate() {
 *     // Important but not critical test
 * }
 *
 * // Low priority test - runs when TESTNG_RUN_SEVERITY_LEVEL >= 3
 * @Test
 * @CSeverity(level = 3)
 * public void testOptionalFeature() {
 *     // Nice-to-have functionality test
 * }
 * }</pre>
 *
 * <h3>Severity Levels Guidelines:</h3>
 *
 * <ul>
 *   <li><strong>Level 1:</strong> Critical tests (core functionality, blocking issues)
 *   <li><strong>Level 2:</strong> Important tests (major features, significant workflows)
 *   <li><strong>Level 3:</strong> Medium tests (supporting features, edge cases)
 *   <li><strong>Level 4+:</strong> Low priority tests (optional features, minor edge cases)
 * </ul>
 *
 * <h3>Configuration:</h3>
 *
 * <p>Set the severity threshold using environment variable or system property:
 *
 * <pre>{@code
 * # Run only critical tests (level 1)
 * export TESTNG_RUN_SEVERITY_LEVEL=1
 *
 * # Run critical and important tests (levels 1-2)
 * export TESTNG_RUN_SEVERITY_LEVEL=2
 *
 * # Run all tests with severity levels 1-3
 * export TESTNG_RUN_SEVERITY_LEVEL=3
 * }</pre>
 *
 * <h3>Integration with Other Annotations:</h3>
 *
 * <pre>{@code
 * // Test with both severity and regression annotations
 * @Test
 * @CSeverity(level = 2)
 * @CRegression(depth = 1)
 * public void testImportantRegressionScenario() {
 *     // This test runs when both severity and regression conditions are met
 * }
 * }</pre>
 *
 * <h3>Important Notes:</h3>
 *
 * <ul>
 *   <li>You must include {@link CIMethodInterceptor} in your TestNG listeners for this annotation
 *       to work
 *   <li>Tests without {@code @CSeverity} will be ignored when {@code TESTNG_RUN_SEVERITY_LEVEL > 0}
 *   <li>Tests with severity level ≤ {@code TESTNG_RUN_SEVERITY_LEVEL} will be included in execution
 *   <li>When combined with {@link CRegression}, both severity and regression conditions must be
 *       satisfied
 *   <li>Lower numeric values indicate higher severity (level 1 = highest severity)
 * </ul>
 *
 * @since 1.0
 * @see CIMethodInterceptor
 * @see CRegression
 * @see CDeferred
 * @see CAwaiting
 * @see COpenDefects
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CSeverity {

  /**
   * Specifies the severity level for the test method.
   *
   * <p>The severity level determines when this test will be executed based on the {@code
   * TESTNG_RUN_SEVERITY_LEVEL} configuration. Tests with severity level less than or equal to the
   * configured threshold will be included in the test run.
   *
   * <p><strong>Severity Level Guidelines:</strong>
   *
   * <ul>
   *   <li><strong>1:</strong> Critical/Blocker - Core functionality that must always work
   *   <li><strong>2:</strong> Major - Important features and primary user workflows
   *   <li><strong>3:</strong> Normal - Standard features and common scenarios
   *   <li><strong>4:</strong> Minor - Edge cases and less critical functionality
   *   <li><strong>5+:</strong> Trivial - Optional features and rare edge cases
   * </ul>
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * @CSeverity(level = 1)  // Will run when TESTNG_RUN_SEVERITY_LEVEL >= 1
   * @CSeverity(level = 3)  // Will run when TESTNG_RUN_SEVERITY_LEVEL >= 3
   * }</pre>
   *
   * @return the severity level as a positive integer (lower numbers = higher severity)
   */
  @JsonProperty("level")
  int level();
}

package org.catools.common.annotations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.catools.common.testng.listeners.CIMethodInterceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * This annotation {@link CRegression} is used to mark test methods with a regression depth level
 * that determines whether the test should be executed based on the value of the 
 * {@code TESTNG_RUN_REGRESSION_DEPTH} environment variable.
 *
 * <p>The regression depth system allows for selective test execution based on the thoroughness
 * of testing required. Lower depth values represent more critical, frequently run tests, while
 * higher depth values represent more comprehensive regression tests that may be run less frequently.
 *
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Critical test - always runs (depth 1)
 * @Test
 * @CRegression(depth = 1)
 * public void testCriticalUserLogin() {
 *     // Test implementation
 * }
 * 
 * // Standard regression test (depth 2)
 * @Test
 * @CRegression(depth = 2) 
 * public void testUserProfileUpdate() {
 *     // Test implementation
 * }
 * 
 * // Extended regression test (depth 3)
 * @Test
 * @CRegression(depth = 3)
 * public void testComplexWorkflowIntegration() {
 *     // Test implementation
 * }
 * }</pre>
 *
 * <h3>Environment Configuration:</h3>
 * <p>Set the {@code TESTNG_RUN_REGRESSION_DEPTH} environment variable to control which tests run:
 * <ul>
 *   <li>{@code TESTNG_RUN_REGRESSION_DEPTH=0} - No tests with @CRegression will run</li>
 *   <li>{@code TESTNG_RUN_REGRESSION_DEPTH=1} - Only tests with depth ≤ 1 will run</li>
 *   <li>{@code TESTNG_RUN_REGRESSION_DEPTH=2} - Tests with depth ≤ 2 will run</li>
 *   <li>{@code TESTNG_RUN_REGRESSION_DEPTH=3} - Tests with depth ≤ 3 will run</li>
 * </ul>
 *
 * <h3>Integration with CI/CD:</h3>
 * <pre>{@code
 * // Example CI configuration
 * // For smoke tests: TESTNG_RUN_REGRESSION_DEPTH=1  
 * // For nightly builds: TESTNG_RUN_REGRESSION_DEPTH=2
 * // For full regression: TESTNG_RUN_REGRESSION_DEPTH=3
 * }</pre>
 *
 * <p><strong>Important Notes:</strong>
 * <ul>
 *   <li>You must include {@link CIMethodInterceptor} in your TestNG listeners for this annotation to work.</li>
 *   <li>Any tests without {@link CRegression} will be ignored if {@code TESTNG_RUN_REGRESSION_DEPTH} is greater than zero.</li>
 *   <li>Any test with {@link CRegression} depth value less than or equal to {@code TESTNG_RUN_REGRESSION_DEPTH} will be included in the execution suite.</li>
 *   <li>The depth value must be a positive integer.</li>
 * </ul>
 *
 * @since 1.0
 * @see CIMethodInterceptor
 * @see CDeferred
 * @see CAwaiting
 * @see COpenDefects
 * @see CSeverity
 * @see CIgnored
 * @see CDefects
 * @see CTestIds
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CRegression {
  /**
   * Specifies the regression depth level for the annotated test method.
   * 
   * <p>The depth value determines the priority and frequency of test execution:
   * <ul>
   *   <li><strong>Depth 1:</strong> Critical tests - run in smoke tests and all builds</li>
   *   <li><strong>Depth 2:</strong> Standard regression tests - run in nightly builds</li>
   *   <li><strong>Depth 3+:</strong> Extended regression tests - run in full regression cycles</li>
   * </ul>
   * 
   * <p>Tests will only execute if their depth value is less than or equal to the
   * {@code TESTNG_RUN_REGRESSION_DEPTH} environment variable value.
   * 
   * @return the regression depth level as a positive integer
   * @since 1.0
   */
  @JsonProperty("depth") int depth();
}

package org.catools.common.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Annotation to mark test methods that should be temporarily ignored during test execution.
 * 
 * <p>It happens when you want to remove test from your execution but in large projects it happens that
 * you disable test and then forgot about it. To avoid such situation we have {@link CIgnored}.
 *
 * <h3>Usage Guidelines:</h3>
 * <ul>
 *   <li>We have a flag CORE_SKIP_CLASS_WITH_IGNORED_TEST to avoid running tests with {@link
 *       CIgnored} annotation.
 *   <li>If test fails due to known defect then we should use {@link COpenDefects} annotation
 *       instead.
 *   <li>If deferred defect is fixed then we should move it to {@link CDefects} annotation and remove this
 *       from test.
 * </ul>
 *
 * <h3>Examples:</h3>
 * <pre>{@code
 * public class UserServiceTest {
 *     
 *     @Test
 *     @CIgnored
 *     public void testUserCreation() {
 *         // This test is temporarily ignored - perhaps due to infrastructure issues
 *         // or pending code changes that would make this test fail
 *         UserService service = new UserService();
 *         User user = service.createUser("john.doe", "john@example.com");
 *         assertNotNull(user);
 *     }
 *     
 *     @Test
 *     @CIgnored
 *     public void testUserDeletion() {
 *         // Temporarily ignored while refactoring the deletion logic
 *         UserService service = new UserService();
 *         boolean result = service.deleteUser("john.doe");
 *         assertTrue(result);
 *     }
 * }
 * }</pre>
 *
 * <h3>Best Practices:</h3>
 * <ul>
 *   <li>Always add a comment explaining why the test is ignored</li>
 *   <li>Include a timeline or condition for when the test should be re-enabled</li>
 *   <li>Consider using more specific annotations like {@link COpenDefects} for known issues</li>
 *   <li>Regularly review ignored tests to avoid accumulation of disabled tests</li>
 * </ul>
 *
 * @see CDeferred
 * @see CDefects
 * @see CAwaiting
 * @see COpenDefects
 * @since 1.0
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CIgnored {
}

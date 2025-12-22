package org.catools.common.testng.utils;

import java.lang.annotation.Annotation;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.catools.common.annotations.CAwaiting;
import org.catools.common.annotations.CIgnored;
import org.catools.common.annotations.CRegression;
import org.catools.common.annotations.CSeverity;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.testng.CTestNGConfigs;
import org.catools.common.testng.listeners.CExecutionStatisticListener;
import org.testng.IMethodInstance;
import org.testng.ITestNGMethod;

/**
 * Utility class for managing TestNG test suite execution and filtering. This class provides methods
 * to filter test methods based on various criteria such as annotations, severity levels, regression
 * depth, and configuration rules.
 *
 * <p>The utility supports filtering tests based on:
 *
 * <ul>
 *   <li>Custom annotations (@CSeverity, @CRegression, @CAwaiting, @CIgnored)
 *   <li>Severity levels and regression depths
 *   <li>Configuration-based annotation matching rules
 * </ul>
 *
 * @author CATools Team
 * @since 1.0
 */
@UtilityClass
public class CTestSuiteUtil {

  /**
   * Filters a list of TestNG method instances to determine which ones should be executed based on
   * various filtering criteria such as annotations, severity levels, and configuration rules.
   *
   * <p>This method removes test methods that should be skipped according to the current test
   * execution configuration and also updates the execution statistics listener by removing filtered
   * methods from tracking.
   *
   * <p><strong>Example usage:</strong>
   *
   * <pre>{@code
   * List<IMethodInstance> allMethods = getTestMethods();
   * List<IMethodInstance> methodsToExecute = CTestSuiteUtil.filterMethodInstanceToExecute(allMethods);
   *
   * // Only methods that pass the filtering criteria will be in methodsToExecute
   * for (IMethodInstance method : methodsToExecute) {
   *     // Execute the test method
   *     executeTest(method);
   * }
   * }</pre>
   *
   * @param list the original list of method instances to filter
   * @return a filtered list containing only the method instances that should be executed
   */
  public static List<IMethodInstance> filterMethodInstanceToExecute(List<IMethodInstance> list) {
    CList<IMethodInstance> output = new CList<>(list);
    if (!output.isEmpty()) {
      output.removeIf(
          method -> {
            boolean result =
                method.getMethod().isTest() && shouldSkipForThisRun(method.getMethod());
            if (result) {
              CExecutionStatisticListener.removeTestMethod(method.getMethod());
            }
            return result;
          });
    }
    return output;
  }

  /**
   * Determines whether a specific TestNG method should be skipped for the current test run based on
   * its annotations and the current test configuration.
   *
   * <p>This method extracts all annotations from the given method and evaluates them against the
   * filtering rules to determine if the method should be executed or skipped.
   *
   * <p><strong>Example usage:</strong>
   *
   * <pre>{@code
   * @Test
   * @CSeverity(level = 3)
   * @CRegression(depth = 2)
   * public void testHighSeverityMethod() {
   *     // Test implementation
   * }
   *
   * // In your test runner
   * ITestNGMethod method = getTestMethod("testHighSeverityMethod");
   * boolean shouldSkip = CTestSuiteUtil.shouldSkipForThisRun(method);
   *
   * if (shouldSkip) {
   *     System.out.println("Skipping test due to filtering rules");
   * } else {
   *     executeTest(method);
   * }
   * }</pre>
   *
   * @param method the TestNG method to evaluate for skipping
   * @return {@code true} if the method should be skipped, {@code false} if it should be executed
   */
  public static boolean shouldSkipForThisRun(ITestNGMethod method) {
    return shouldSkipByAnnotation(getAnnotations(method));
  }

  /**
   * Evaluates a collection of annotations to determine if a test method should be skipped based on
   * annotation-specific filtering rules.
   *
   * <p>This method applies two main categories of filtering rules:
   *
   * <ul>
   *   <li><strong>Configuration rules:</strong> Based on annotation matching patterns configured in
   *       CTestNGConfigs
   *   <li><strong>Regression and severity rules:</strong> Based on @CRegression depth
   *       and @CSeverity level annotations
   * </ul>
   *
   * <p><strong>Example usage:</strong>
   *
   * <pre>{@code
   * // Collect annotations from a test method
   * CList<Annotation> annotations = new CList<>();
   * annotations.add(new CSeverity() { public int level() { return 2; } });
   * annotations.add(new CRegression() { public int depth() { return 1; } });
   *
   * // Check if method should be skipped based on these annotations
   * boolean shouldSkip = CTestSuiteUtil.shouldSkipByAnnotation(annotations);
   *
   * if (shouldSkip) {
   *     System.out.println("Method will be skipped due to annotation rules");
   * }
   *
   * // Example with @CAwaiting annotation
   * CList<Annotation> awaitingAnnotations = new CList<>();
   * awaitingAnnotations.add(new CAwaiting() { });
   * boolean skipAwaiting = CTestSuiteUtil.shouldSkipByAnnotation(awaitingAnnotations);
   * // Returns true if CTestNGConfigs.skipClassWithAwaitingTest() is enabled
   * }</pre>
   *
   * @param annotations the list of annotations to evaluate against filtering rules
   * @return {@code true} if the test should be skipped based on the annotations, {@code false}
   *     otherwise
   */
  public static boolean shouldSkipByAnnotation(CList<Annotation> annotations) {
    return shouldSkipByAnnotationsConfigRules(annotations)
        || shouldSkipByRegressionAndSeverityRules(annotations);
  }

  private static boolean shouldSkipByRegressionAndSeverityRules(CList<Annotation> annotations) {
    int severityLevel = CTestNGConfigs.getSeverityLevel();
    int regressionDepth = CTestNGConfigs.getRegressionDepth();

    // We should not skip any test if there is no severity level or regression depth defined
    if (severityLevel == -1 && regressionDepth == -1) {
      return false;
    }

    // If we have both severity level abd regression depth then we should have both match otherwise
    // we should skip
    if (severityLevel > -1 && regressionDepth > -1) {
      return shouldSkipBySeverityLevel(annotations) || shouldSkipByRegressionLevel(annotations);
    } else if (severityLevel > -1) {
      return shouldSkipBySeverityLevel(annotations);
    } else if (regressionDepth > -1) {
      return shouldSkipByRegressionLevel(annotations);
    }

    return false;
  }

  private static boolean shouldSkipByAnnotationsConfigRules(CList<Annotation> annotations) {
    if (shouldBeSkippedByAwaitingAnnotation(annotations)
        || shouldBeSkippedByIgnoredAnnotation(annotations)) {
      return true;
    }

    CList<String> annotationsToIgnoreTestIfAllMatch =
        CTestNGConfigs.getAnnotationsToIgnoreTestIfAllMatch();
    CList<String> annotationsToIgnoreTestIfAnyMatch =
        CTestNGConfigs.getAnnotationsToIgnoreTestIfAnyMatch();
    CList<String> annotationsToRunTestIfAllMatch =
        CTestNGConfigs.getAnnotationsToRunTestIfAllMatch();
    CList<String> annotationsToRunTestIfAnyMatch =
        CTestNGConfigs.getAnnotationsToRunTestIfAnyMatch();

    // If no configuration set to handle label then we do not skip by label
    if (annotationsToIgnoreTestIfAllMatch.isEmpty()
        && annotationsToIgnoreTestIfAnyMatch.isEmpty()
        && annotationsToRunTestIfAllMatch.isEmpty()
        && annotationsToRunTestIfAnyMatch.isEmpty()) {
      return false;
    }

    CSet<String> annotationNames = annotations.mapToSet(a -> a.annotationType().getSimpleName());

    // If any configuration set to handle label and test does not hve a label then we skip it
    if (annotationNames.isEmpty()) {
      return true;
    }

    if (annotationsToIgnoreTestIfAllMatch.isNotEmpty()
        && annotationNames.containsAll(annotationsToIgnoreTestIfAllMatch)) {
      return true;
    }

    if (annotationsToIgnoreTestIfAnyMatch.isNotEmpty()
        && annotationNames.containsAny(annotationsToIgnoreTestIfAnyMatch)) {
      return true;
    }

    if (annotationsToRunTestIfAnyMatch.isNotEmpty()
        && annotationNames.containsAny(annotationsToRunTestIfAnyMatch)) {
      return false;
    }

    if (annotationsToRunTestIfAllMatch.isNotEmpty()) {
      return annotationNames.notContainsAll(annotationsToRunTestIfAllMatch);
    }

    return annotationsToRunTestIfAnyMatch.isNotEmpty();
  }

  private static boolean shouldSkipBySeverityLevel(CList<Annotation> annotations) {
    return annotations.hasNot(a -> a instanceof CSeverity)
        || annotations.has(
            a ->
                a instanceof CSeverity
                    && (((CSeverity) a).level() > CTestNGConfigs.getSeverityLevel()));
  }

  private static boolean shouldSkipByRegressionLevel(CList<Annotation> annotations) {
    return annotations.hasNot(a -> a instanceof CRegression)
        || annotations.has(
            a ->
                a instanceof CRegression
                    && (((CRegression) a).depth() > CTestNGConfigs.getRegressionDepth()));
  }

  private static boolean shouldBeSkippedByAwaitingAnnotation(CList<Annotation> annotations) {
    return CTestNGConfigs.skipClassWithAwaitingTest()
        && annotations.has(an -> an instanceof CAwaiting);
  }

  private static boolean shouldBeSkippedByIgnoredAnnotation(CList<Annotation> annotations) {
    return CTestNGConfigs.skipClassWithIgnoredTest()
        && annotations.has(an -> an instanceof CIgnored);
  }

  private static CList<Annotation> getAnnotations(ITestNGMethod method) {
    if (!hasAnnotation(method)) {
      return new CList<>();
    }
    return new CList<>(method.getConstructorOrMethod().getMethod().getAnnotations());
  }

  private static boolean hasAnnotation(ITestNGMethod method) {
    return method != null
        && method.getConstructorOrMethod() != null
        && method.getConstructorOrMethod().getMethod() != null
        && method.getConstructorOrMethod().getMethod().getAnnotations() != null;
  }
}

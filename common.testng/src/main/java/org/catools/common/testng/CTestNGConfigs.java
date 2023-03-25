package org.catools.common.testng;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CConfigUtil;
import org.catools.common.utils.CStringUtil;
import org.testng.ITestNGListener;
import org.testng.xml.XmlSuite;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CTestNGConfigs {
  private static final AtomicInteger SUITE_RUN_COUNTER = new AtomicInteger(1);

  public static Class getBaseClassLoader() {
    try {
      return Class.forName(CHocon.get(Configs.TESTNG_BASE_TEST_CLASS_LOADER).asString());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static Set<ITestNGListener> getListeners() {
    final Set<ITestNGListener> listeners = new HashSet<>();
    String testNgListeners = CHocon.get(Configs.TESTNG_LISTENERS).asString(null);
    if (testNgListeners != null) {
      for (String listener : CHocon.get(Configs.TESTNG_LISTENERS).asStrings()) {
        try {
          listeners.add((ITestNGListener) Class.forName(listener).getConstructor().newInstance());
        } catch (Throwable t) {
          System.out.println(
              "Could not convert Configs.TESTNG_LISTENERS parameter " + listener + " to classes.");
        }
      }
    }
    return listeners;
  }

  public static int getSeverityLevel() {
    return CHocon.get(Configs.TESTNG_RUN_SEVERITY_LEVEL).asInteger(-1);
  }

  public static int getRegressionDepth() {
    return CHocon.get(Configs.TESTNG_RUN_REGRESSION_DEPTH).asInteger(-1);
  }

  public static CList<String> getAnnotationsToIgnoreTestIfAnyMatch() {
    return CList.of(
        CHocon.get(Configs.TESTNG_IGNORE_TEST_WITH_ANY_ANNOTATION).asStrings(CList.of()));
  }

  public static CList<String> getAnnotationsToIgnoreTestIfAllMatch() {
    return CList.of(
        CHocon.get(Configs.TESTNG_IGNORE_TEST_WITH_ALL_ANNOTATION).asStrings(CList.of()));
  }

  public static CList<String> getAnnotationsToRunTestIfAllMatch() {
    return CList.of(
        CHocon.get(Configs.TESTNG_RUN_TEST_WITH_ALL_ANNOTATIONS).asStrings(CList.of()));
  }

  public static CList<String> getAnnotationsToRunTestIfAnyMatch() {
    return CList.of(CHocon.get(Configs.TESTNG_RUN_TEST_WITH_ANY_ANNOTATIONS).asStrings(CList.of()));
  }

  public static int getTestRetryCount() {
    return CHocon.get(Configs.TESTNG_TEST_RETRY_COUNT).asInteger(0);
  }

  public static int getSuiteRetryCount() {
    return CHocon.get(Configs.TESTNG_SUITE_RETRY_COUNT).asInteger(0);
  }

  public static int getSuiteRunCounter() {
    return SUITE_RUN_COUNTER.get();
  }

  public static CSet<String> getTestPackages() {
    return new CSet<>(CHocon.get(Configs.TESTNG_TEST_PACKAGES).asStrings());
  }

  public static XmlSuite.ParallelMode getTestLevelParallel() {
    return XmlSuite.ParallelMode.valueOf(CHocon.get(Configs.TESTNG_TEST_PARALLEL_LEVEL_PARALLEL_MODE).asString(XmlSuite.ParallelMode.CLASSES.name()));
  }

  public static int getTestLevelThreadCount() {
    return CHocon.get(Configs.TESTNG_TEST_PARALLEL_LEVEL_PARALLEL_THREAD_COUNT).asInteger(1);
  }

  public static XmlSuite.ParallelMode getSuiteLevelParallel() {
    return XmlSuite.ParallelMode.valueOf(CHocon.get(Configs.TESTNG_SUITE_PARALLEL_LEVEL_PARALLEL_MODE).asString(XmlSuite.ParallelMode.TESTS.name()));
  }

  public static int getSuiteLevelThreadCount() {
    return CHocon.get(Configs.TESTNG_SUITE_PARALLEL_LEVEL_PARALLEL_THREAD_COUNT).asInteger(1);
  }

  public static Integer incrementSuiteRun() {
    int suiteRun = SUITE_RUN_COUNTER.incrementAndGet();
    CConfigUtil.setRunName(isLastSuiteRun() ? CStringUtil.EMPTY : "run_" + suiteRun);
    return suiteRun;
  }

  public static boolean isFirstSuiteRun() {
    return getSuiteRunCounter() == 1;
  }

  public static boolean isLastSuiteRun() {
    return getSuiteRunCounter() >= getSuiteRetryCount() + 1;
  }

  public static boolean skipClassWithAwaitingTest() {
    return CHocon.get(Configs.TESTNG_SKIP_CLASS_WITH_AWAITING_TEST).asBoolean(false);
  }

  public static boolean skipClassWithIgnoredTest() {
    return CHocon.get(Configs.TESTNG_SKIP_CLASS_WITH_IGNORED_TEST).asBoolean(false);
  }

  public static String getTestNgResultName() {
    return CHocon.get(Configs.TESTNG_RESULT_XML_NAME).asString("cat-testng-results.xml");
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    TESTNG_TEST_PACKAGES("catools.testng.test_packages"),
    TESTNG_TEST_PARALLEL_LEVEL_PARALLEL_MODE("catools.testng.parallel.test_level.parallel_mode"),
    TESTNG_TEST_PARALLEL_LEVEL_PARALLEL_THREAD_COUNT("catools.testng.parallel.test_level.thread_count"),
    TESTNG_SUITE_PARALLEL_LEVEL_PARALLEL_MODE("catools.testng.parallel.suite_level.parallel_mode"),
    TESTNG_SUITE_PARALLEL_LEVEL_PARALLEL_THREAD_COUNT("catools.testng.parallel.suite_level.thread_count"),
    TESTNG_LISTENERS("catools.testng.listeners.general"),
    TESTNG_TEST_RETRY_COUNT("catools.testng.test_retry_count"),
    TESTNG_SUITE_RETRY_COUNT("catools.testng.suite_retry_count"),
    TESTNG_BASE_TEST_CLASS_LOADER("catools.testng.base_test_class_loader"),
    TESTNG_SKIP_CLASS_WITH_AWAITING_TEST("catools.testng.skip_class_with_awaiting_test"),
    TESTNG_SKIP_CLASS_WITH_IGNORED_TEST("catools.testng.skip_class_with_ignored_test"),
    TESTNG_RUN_SEVERITY_LEVEL("catools.testng.run_severity_level"),
    TESTNG_RUN_REGRESSION_DEPTH("catools.testng.run_regression_depth"),
    TESTNG_IGNORE_TEST_WITH_ANY_ANNOTATION("catools.testng.ignore_test_with_any_annotation"),
    TESTNG_IGNORE_TEST_WITH_ALL_ANNOTATION("catools.testng.ignore_test_with_all_annotation"),
    TESTNG_RUN_TEST_WITH_ALL_ANNOTATIONS("catools.testng.run_test_with_all_annotations"),
    TESTNG_RUN_TEST_WITH_ANY_ANNOTATIONS("catools.testng.run_test_with_any_annotations"),
    TESTNG_RESULT_XML_NAME("catools.testng.result_xml_name");

    private final String path;
  }
}

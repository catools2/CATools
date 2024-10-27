package org.catools.common.testng;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.date.CDate;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.testng.utils.CTestClassUtil;
import org.catools.common.testng.utils.CXmlSuiteUtils;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.testng.xml.internal.Parser;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class CTestNGProcessor {

  public static CFile buildTestSuite(CHashMap<String, CSet<String>> testClassesMap, String suiteName, String filename, @Nullable Consumer<XmlSuite> xmlSuiteAdjuster) {
    CFile file = new CFile(filename);
    file.getParentFile().mkdirs();
    file.write(CXmlSuiteUtils.buildTestSuiteForClasses(testClassesMap, suiteName, xmlSuiteAdjuster).toXml());
    return file;
  }

  public static CFile buildTestSuiteForClasses(CSet<String> testClasses, String filename, @Nullable Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, Consumer<XmlSuite> xmlSuiteAdjuster) {
    CHashMap<String, CSet<String>> map = new CHashMap<>();
    if (testClassGroupMapper == null) {
      map.put("Test", testClasses);
    } else {
      map.putAll(testClassGroupMapper.apply(testClasses));
    }
    return buildTestSuite(map, "Suite", filename, xmlSuiteAdjuster);
  }

  public static int processLocalXmlSuite(String suiteName) {
    CVerify.Bool.isTrue(suiteName.toLowerCase().trim().endsWith(".xml"), "TestNG suite file name should end with xml.");
    CFile localXmlFile = new CFile(suiteName);
    CVerify.Bool.isTrue(localXmlFile.exists(), "Xml file exists. file: " + localXmlFile.getCanonicalPath());
    print("Running local xml file:" + localXmlFile.getCanonicalPath());
    return processFile(localXmlFile);
  }

  public static int processResourceXmlSuite(String suiteName) {
    CVerify.Bool.isTrue(suiteName.toLowerCase().trim().endsWith(".xml"), "TestNG suite file name should end with xml.");
    String resourceContent = new CResource(suiteName, CTestNGConfigs.getBaseClassLoader()).getString();
    CVerify.String.isNotBlank(resourceContent, "Xml resource file exists and it is not empty. Resource Name: " + suiteName);
    CFile localXmlFile = CFile.fromTmp(suiteName);
    localXmlFile.write(resourceContent);
    CVerify.Bool.isTrue(localXmlFile.exists(), "Xml file copied to resource folder. file: " + localXmlFile);
    return processFile(localXmlFile);
  }

  public static int processTestByTestIds(CSet<String> issueIds, String filename, Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, Consumer<XmlSuite> xmlSuiteAdjuster, boolean filterTestsWhichWillSkipInRun) {
    return processTestClasses(CTestClassUtil.getClassNameForIssueKeys(issueIds, filterTestsWhichWillSkipInRun), filename, testClassGroupMapper, xmlSuiteAdjuster);
  }

  public static int processTestByClassNames(CSet<String> classNames, String filename, Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, Consumer<XmlSuite> xmlSuiteAdjuster) {
    return processTestClasses(classNames, filename, testClassGroupMapper, xmlSuiteAdjuster);
  }

  public static int processTestClasses(CSet<String> testClasses, String filename, @Nullable Function<CSet<String>, Map<String, CSet<String>>> testClassGroupMapper, @Nullable Consumer<XmlSuite> xmlSuiteAdjuster) {
    return processFile(buildTestSuiteForClasses(testClasses, filename, testClassGroupMapper, xmlSuiteAdjuster));
  }

  public static int processXmlSuites(Collection<XmlSuite> xmlSuites) {
    try {
      new CList<>(xmlSuites).forEach(x -> print("Processing Xml Suites \n" + x.toXml()));
      TestNG testNG = new TestNG();
      testNG.setXmlSuites((List<XmlSuite>) (xmlSuites));

      for (ITestNGListener listener : CTestNGConfigs.getListeners()) {
        testNG.addListener(listener);
      }

      CFile.fromOutput(CDate.now().toTimeStampForFileName() + ".xml").write(xmlSuites.toString());
      testNG.run();
      return testNG.getStatus();
    } catch (Throwable t) {
      log.error("Could Not Processing Xml Suites", t);
      return 1;
    }
  }

  private static void print(String x) {
    log.info(x);
    if (!log.isInfoEnabled()) {
      System.out.print(x);
    }
  }

  private static int processFile(CFile xmlFile) {
    print("Processing " + xmlFile);
    try {
      return processXmlSuites(new Parser(xmlFile.getCanonicalPath()).parse());
    } catch (Throwable t) {
      return 1;
    }
  }
}

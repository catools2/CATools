package org.catools.reportportal.tests.package1;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Level;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.io.CResource;
import org.catools.common.testng.utils.CRetryAnalyzer;
import org.catools.common.tests.CTest;
import org.catools.reportportal.utils.CReportPortalUtil;
import org.testng.annotations.Test;

/**
 * We need this class to run them and verify manually report portal to ensure that graph display
 * right information
 */
public class CRPTestClass1 extends CTest {
  private static AtomicInteger instanceValue = new AtomicInteger();

  @Test(retryAnalyzer = CRetryAnalyzer.class)
  public void testPassOnSecondTestRun() {
    assert instanceValue.getAndIncrement() > 1;
  }

  @Test(retryAnalyzer = CRetryAnalyzer.class)
  public void test1() {
    CReportPortalUtil.sendToReportPortal(
        Level.INFO,
        "Add Mickey",
        new CResource("mickey.png", CRPTestClass3.class)
            .saveToFolder(CPathConfigs.getTempFolder()));
  }

  @Test(retryAnalyzer = CRetryAnalyzer.class)
  public void test2() {
    CReportPortalUtil.sendToReportPortal(
        Level.WARN,
        "Add Minnie",
        new CResource("minnie.png", CRPTestClass3.class)
            .saveToFolder(CPathConfigs.getTempFolder()));
  }

  @Test(retryAnalyzer = CRetryAnalyzer.class)
  public void test3() {
    logger.error("test3");
  }

  @Test(retryAnalyzer = CRetryAnalyzer.class)
  public void test4() {
    logger.debug("test4");
  }
}

package org.catools.common.tests.utils;

import org.catools.common.tests.CBaseUnitTest;
import org.catools.common.tests.CTestRetryAnalyzer;
import org.catools.common.utils.CSystemUtil;
import org.testng.annotations.Test;

public class CSystemUtilTest extends CBaseUnitTest {

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testGetPlatform() {
    verify.String.isNotBlank(CSystemUtil.getPlatform().name(), "Method works as designed.");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testGetUserName() {
    verify.String.isNotBlank(CSystemUtil.getUserName(), "Method works as designed.");
  }

  @Test(retryAnalyzer = CTestRetryAnalyzer.class)
  public void testGetHostIP() {
    verify.String.isNotBlank(CSystemUtil.getHostIP(), "Method works as designed.");
  }
}

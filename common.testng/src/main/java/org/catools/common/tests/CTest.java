package org.catools.common.tests;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.catools.common.config.CCoreConfigs;
import org.catools.common.config.CTestManagementConfigs;
import org.catools.common.extensions.verify.CVerificationInfo;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.CVerify;
import org.catools.common.logger.CLoggerConfigs;
import org.catools.common.testng.model.CExecutionStatus;
import org.catools.common.testng.model.CTestResult;
import org.catools.common.testng.utils.CTestClassUtil;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

@Slf4j
public class CTest implements CVerificationQueue {
  static {
    AnsiConsole.systemInstall();
    ThreadContext.put("LogFolder", CLoggerConfigs.getLogFolderPath());
  }

  protected static final Logger logger = log;

  private static boolean FIRST_RUN_PREPARATION_CALLED = false;

  private CExecutionStatus testResult = CExecutionStatus.CREATED;
  private final CTestData dataState = new CTestData();
  private final String name = CTestClassUtil.getTestName(getClass());
  public final CVerify verify = new CVerify();

  private final String projectName;
  private final String versionName;

  public CTest() {
    projectName = CTestManagementConfigs.getProjectName();
    versionName = CTestManagementConfigs.getVersionName();
  }

  public String getName() {
    return name;
  }

  public CTestData getDataState() {
    return dataState;
  }

  @BeforeSuite
  public void beforeSuite(ITestContext context) {
    log.debug("BeforeSuite Started for suite {} ", getSuiteName(context));
    if (!FIRST_RUN_PREPARATION_CALLED) {
      onFirstRun();
      FIRST_RUN_PREPARATION_CALLED = true;
    }
  }

  @BeforeTest
  public void beforeTest(ITestContext context) {
    log.debug("BeforeTest Started for issue {} ", getContextName(context));
  }

  @BeforeClass
  public void beforeClass() {
    ThreadContext.put("LogFolder", CLoggerConfigs.getLogFolderPath());
    ThreadContext.put("TestName", name);
    log.debug("BeforeClass Started for class {} ", name);
  }

  @BeforeMethod
  public void beforeMethod(ITestResult result) {
    ThreadContext.put("LogFolder", CLoggerConfigs.getLogFolderPath());
    ThreadContext.put("TestName", name);
    log.debug("BeforeMethod Started for class {}, method {}", name, getMethodName(result));
  }

  @AfterMethod
  public void afterMethod(ITestResult result) {
    log.debug("AfterMethod Started for class {}, method {} ", name, getMethodName(result));
    if (CCoreConfigs.isDataSetupModeOn()) {
      if (this.testResult == CExecutionStatus.CREATED
          && (result == null || result.getStatus() != ITestResult.SUCCESS)) {
        testResult = CExecutionStatus.SUCCESS;
        Reporter.getCurrentTestResult().setStatus(ITestResult.SKIP);
      }
    } else {
      this.testResult = new CTestResult(projectName, versionName, result).getStatus();
    }

    if (result != null && result.getThrowable() != null) {
      log.error("Test Failed With Exception:\n" + result.getThrowable());
    }
  }

  @AfterClass
  public void afterClass() {
    log.debug("AfterClass Started for class {}", name);
    switch (testResult) {
      case SUCCESS:
      case SUCCESS_PERCENTAGE_FAILURE:
        onSuccess();
        break;
      case SKIP:
        onSkip();
        break;
      case FAILURE:
        onFailure();
        break;
      case DEFERRED:
        onDeferred();
        break;
      case BLOCKED:
        onBlocked();
        break;
      case AWAITING:
        onAwaiting();
        break;
    }
  }

  @AfterTest
  public void afterTest(ITestContext context) {
    ThreadContext.remove("TestName");
    log.debug("AfterTest Started for issue {} ", getContextName(context));
  }

  @AfterSuite
  public void afterSuite(ITestContext context) {
    log.debug("AfterSuite Started for {} suite.", getSuiteName(context));
  }

  public void updateDataState(String key, Object value) {
    getDataState().updateDataState(key, value);
  }

  public <T> T getDataState(String key) {
    return getDataState().getDataState(key);
  }

  protected void onAwaiting() {
  }

  protected void onBlocked() {
  }

  protected void onDeferred() {
  }

  protected void onFailure() {
  }

  protected void onSkip() {
  }

  protected void onSuccess() {
  }

  protected void onFirstRun() {
  }

  private String getMethodName(ITestResult result) {
    if (result == null || result.getMethod() == null) {
      return "";
    }
    return result.getMethod().getMethodName();
  }

  private String getSuiteName(ITestContext context) {
    if (context == null || context.getSuite() == null) {
      return "";
    }
    return context.getSuite().getName();
  }

  private String getContextName(ITestContext context) {
    if (context == null || context.getCurrentXmlTest() == null) {
      return "";
    }
    return context.getCurrentXmlTest().getName();
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public CVerify queue(CVerificationInfo verificationInfo) {
    return verify.queue(verificationInfo);
  }
}

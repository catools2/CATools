package org.catools.web.listeners;

import org.catools.web.tests.CWebTest;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class CScreenshotOnFailureListener implements IInvokedMethodListener {

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    if (testResult.getStatus() == ITestResult.FAILURE
        && testResult.getInstance() instanceof CWebTest<?> testInstance) {
      if (testInstance.isCurrentSessionActive()) {
        testInstance.takeScreenShotIfFail(testResult);
      }
    }
  }
}

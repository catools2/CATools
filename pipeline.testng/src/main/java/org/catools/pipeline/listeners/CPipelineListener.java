package org.catools.pipeline.listeners;

import org.catools.common.date.CDate;
import org.catools.common.testng.listeners.CTestNGListener;
import org.catools.common.testng.model.CTestResult;
import org.catools.pipeline.dao.CPipelineDao;
import org.catools.pipeline.helpers.CPipelineHelper;
import org.catools.pipeline.model.CPipeline;
import org.testng.ITestResult;

import static org.catools.common.testng.utils.CTestClassUtil.noRetryLeft;

public class CPipelineListener extends CTestNGListener {
  private static CPipeline pipeline;

  @Override
  public int priority() {
    return 0;
  }

  @Override
  public void onExecutionStart() {
    buildPipeline();
  }

  @Override
  public synchronized void onTestSuccess(ITestResult result) {
    addResult(result);
  }

  @Override
  public synchronized void onTestFailure(ITestResult result) {
    if (noRetryLeft(result, true)) {
      addResult(result);
    }
  }

  @Override
  public synchronized void onTestSkipped(ITestResult result) {
    if (noRetryLeft(result, true)) {
      addResult(result);
    }
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    if (noRetryLeft(result, true)) {
      addResult(result);
    }
  }

  @Override
  public void onExecutionFinish() {
    CPipelineDao.updateEndDate(pipeline.getId(), CDate.now().getTimeStamp());
  }

  private void addResult(ITestResult result) {
    CPipelineHelper.addExecution(pipeline, new CTestResult(null, null, result));
  }

  private static synchronized void buildPipeline() {
    if (pipeline == null) {
      pipeline = CPipelineHelper.getPipeline();
    }
  }
}

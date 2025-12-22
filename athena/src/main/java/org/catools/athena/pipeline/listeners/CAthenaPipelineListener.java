package org.catools.athena.pipeline.listeners;

import lombok.extern.slf4j.Slf4j;
import org.catools.athena.pipeline.configs.CAthenaPipelineConfigs;
import org.catools.athena.pipeline.helpers.CPipelineHelper;
import org.catools.athena.pipeline.model.PipelineDto;
import org.catools.athena.rest.feign.pipeline.utils.PipelineUtils;
import org.testng.*;
import org.testng.internal.IResultListener;

import java.time.Instant;

@Slf4j
public class CAthenaPipelineListener
    implements IExecutionListener,
        ISuiteListener,
        IClassListener,
        IResultListener,
        IConfigurationListener,
        IInvokedMethodListener {
  private PipelineDto pipeline;
  private Instant beforeClassStartTime;
  private Instant beforeMethodStartTime;
  private Instant beforeClassEndTime;
  private Instant beforeMethodEndTime;

  @Override
  public void onExecutionStart() {
    if (!CAthenaPipelineConfigs.isEnabled()) {
      return;
    }
    pipeline = CPipelineHelper.getPipeline();
  }

  @Override
  public void beforeInvocation(
      IInvokedMethod method, ITestResult testResult, ITestContext context) {
    if (!CAthenaPipelineConfigs.isEnabled()) {
      return;
    }

    setStartTime(testResult.getMethod());
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    if (!CAthenaPipelineConfigs.isEnabled()) {
      return;
    }

    setEndTime(testResult);
  }

  @Override
  public synchronized void onTestSuccess(ITestResult result) {
    addResult(result);
  }

  @Override
  public synchronized void onTestFailure(ITestResult result) {
    addResult(result);
  }

  @Override
  public synchronized void onTestSkipped(ITestResult result) {
    addResult(result);
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    addResult(result);
  }

  @Override
  public void onExecutionFinish() {
    if (!CAthenaPipelineConfigs.isEnabled()) {
      return;
    }

    PipelineUtils.getPipelineClient().updatePipelineEndDate(pipeline.getId(), Instant.now());
  }

  protected void addResult(ITestResult testResult) {
    if (!CAthenaPipelineConfigs.isEnabled()) {
      return;
    }

    CPipelineHelper.addResult(
        testResult,
        pipeline,
        beforeClassStartTime,
        beforeMethodStartTime,
        beforeClassEndTime,
        beforeMethodEndTime);
  }

  protected void setStartTime(ITestNGMethod method) {
    if (method.isBeforeClassConfiguration()) {
      beforeClassStartTime = Instant.now();
    } else if (method.isBeforeMethodConfiguration()) {
      beforeMethodStartTime = Instant.now();
    }
  }

  protected void setEndTime(ITestResult testResult) {
    ITestNGMethod method = testResult.getMethod();
    if (method.isBeforeClassConfiguration()) {
      beforeClassEndTime = Instant.now();
    } else if (method.isBeforeMethodConfiguration()) {
      beforeMethodEndTime = Instant.now();
    }
  }
}

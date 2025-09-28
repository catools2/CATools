package org.catools.pipeline.helpers;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.annotations.*;
import org.catools.common.date.CDate;
import org.catools.common.testng.model.CTestResult;
import org.catools.common.utils.CJsonUtil;
import org.catools.pipeline.configs.CPipelineConfigs;
import org.catools.pipeline.configs.CPipelineTestNGConfigs;
import org.catools.pipeline.dao.CPipelineBaseDao;
import org.catools.pipeline.dao.CPipelineDao;
import org.catools.pipeline.model.*;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.catools.pipeline.cache.CPipelineCacheManager.*;

/**
 * Helper class for managing pipeline operations and test execution tracking.
 * This class provides utility methods for creating, retrieving, and managing pipelines,
 * as well as adding test execution results to pipelines.
 *
 * <p>The CPipelineHelper class is responsible for:
 * <ul>
 *   <li>Creating and retrieving pipeline instances based on configuration</li>
 *   <li>Adding test execution results to pipelines with associated metadata</li>
 *   <li>Managing pipeline execution metadata and annotations</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * // Get or create a pipeline
 * CPipeline pipeline = CPipelineHelper.getPipeline();
 * 
 * // Add a test execution result to the pipeline
 * CTestResult testResult = new CTestResult();
 * testResult.setStatus(TestStatus.PASSED);
 * testResult.setClassName("com.example.MyTest");
 * testResult.setMethodName("testMethod");
 * 
 * CPipelineHelper.addExecution(pipeline, testResult);
 * }</pre>
 *
 * @see CPipeline
 * @see CTestResult
 * @see CPipelineExecution
 * @since 1.0
 */
public class CPipelineHelper {

  /**
   * Retrieves an existing pipeline or creates a new one based on configuration settings.
   * 
   * <p>This method handles pipeline retrieval logic based on the following conditions:
   * <ul>
   *   <li>If pipeline functionality is disabled, returns null</li>
   *   <li>If configured to always create new pipelines, creates a new pipeline</li>
   *   <li>Otherwise, attempts to retrieve the last pipeline by name</li>
   *   <li>If no existing pipeline is found and creation is allowed, creates a new one</li>
   * </ul>
   *
   * @return the retrieved or newly created pipeline, or null if pipeline functionality is disabled
   * @throws NullPointerException if no pipeline with the configured name is found and 
   *                              automatic creation is disabled
   * 
   * @example
   * <pre>{@code
   * // Basic usage - get or create pipeline
   * CPipeline pipeline = CPipelineHelper.getPipeline();
   * if (pipeline != null) {
   *     System.out.println("Pipeline ID: " + pipeline.getId());
   *     System.out.println("Pipeline Name: " + pipeline.getName());
   * }
   * 
   * // Configuration example (set via environment variables or config):
   * // CATOOLS_PIPELINE_LISTENER_ENABLED=true
   * // CATOOLS_PIPELINE_NAME=my-test-pipeline
   * // CATOOLS_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST=true
   * }</pre>
   * 
   * @see CPipelineTestNGConfigs#isEnabled()
   * @see CPipelineTestNGConfigs#always_create_new_pipeline()
   * @see CPipelineTestNGConfigs#createPipelineIfNotExist()
   */
  public static CPipeline getPipeline() {
    if (!CPipelineTestNGConfigs.isEnabled()) return null;

    if (CPipelineTestNGConfigs.always_create_new_pipeline()) {
      return buildPipeline();
    }

    CPipeline lastByName = CPipelineDao.getLastByName(CPipelineConfigs.getPipelineName());
    if (!CPipelineTestNGConfigs.createPipelineIfNotExist()) {
      Objects.requireNonNull(lastByName, "No pipeline with given name found, set CATOOLS_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST environment variable if you want to create one.");
    }
    return lastByName != null ? lastByName : buildPipeline();
  }

  /**
   * Adds a test execution result to the specified pipeline with empty metadata list.
   * This is a convenience method that calls {@link #addExecution(CPipeline, CTestResult, List)}
   * with an empty metadata list.
   *
   * @param pipeline   the pipeline to add the execution to
   * @param testResult the test result containing execution details
   * 
   * @example
   * <pre>{@code
   * CPipeline pipeline = CPipelineHelper.getPipeline();
   * 
   * CTestResult result = new CTestResult();
   * result.setStatus(TestStatus.PASSED);
   * result.setClassName("com.example.tests.LoginTest");
   * result.setMethodName("testValidLogin");
   * result.setStartTime(Instant.now().minusSeconds(30));
   * result.setEndTime(Instant.now());
   * 
   * // Add execution with default metadata extraction
   * CPipelineHelper.addExecution(pipeline, result);
   * }</pre>
   * 
   * @see #addExecution(CPipeline, CTestResult, List)
   */
  public static void addExecution(CPipeline pipeline, CTestResult testResult) {
    addExecution(pipeline, testResult, new ArrayList<>());
  }

  /**
   * Adds a test execution result to the specified pipeline along with additional execution metadata.
   * 
   * <p>This method processes the test result and extracts various types of metadata including:
   * <ul>
   *   <li>Test IDs, defect IDs, and deferred IDs</li>
   *   <li>Method groups and test parameters</li>
   *   <li>Awaiting status and regression depth</li>
   *   <li>Severity level and host information</li>
   *   <li>Custom annotations and execution metadata</li>
   *   <li>Exception information if the test failed</li>
   * </ul>
   *
   * @param pipeline           the pipeline to add the execution to
   * @param testResult         the test result containing execution details
   * @param executionMetaData  additional metadata to be associated with the execution
   * 
   * @example
   * <pre>{@code
   * CPipeline pipeline = CPipelineHelper.getPipeline();
   * 
   * // Create test result with comprehensive information
   * CTestResult result = new CTestResult();
   * result.setStatus(TestStatus.FAILED);
   * result.setClassName("com.example.tests.PaymentTest");
   * result.setMethodName("testCreditCardPayment");
   * result.setStartTime(Instant.now().minusSeconds(45));
   * result.setEndTime(Instant.now());
   * result.setHost("test-server-01");
   * result.setSeverityLevel(SeverityLevel.HIGH);
   * result.setRegressionDepth(RegressionDepth.FULL);
   * 
   * // Add test IDs and defect information
   * result.setTestIds(Arrays.asList("TC-001", "TC-002"));
   * result.setDefectIds(Arrays.asList("BUG-123"));
   * result.setOpenDefectIds(Arrays.asList("BUG-456"));
   * 
   * // Add exception information for failed test
   * ExceptionInfo exceptionInfo = new ExceptionInfo();
   * exceptionInfo.setType("AssertionError");
   * exceptionInfo.setMessage("Expected payment to succeed but got error");
   * exceptionInfo.setStackTrace("at com.example.PaymentTest.testCreditCardPayment...");
   * result.setExceptionInfo(exceptionInfo);
   * 
   * // Add custom execution metadata
   * List<CPipelineExecutionMetaData> customMetadata = new ArrayList<>();
   * customMetadata.add(new CPipelineExecutionMetaData("Browser", "Chrome 118"));
   * customMetadata.add(new CPipelineExecutionMetaData("Environment", "staging"));
   * 
   * CPipelineHelper.addExecution(pipeline, result, customMetadata);
   * }</pre>
   * 
   * <p>Another example with parameters and annotations:
   * <pre>{@code
   * // Test with parameters and annotations
   * CTestResult parameterizedResult = new CTestResult();
   * parameterizedResult.setStatus(TestStatus.PASSED);
   * parameterizedResult.setClassName("com.example.tests.DataDrivenTest");
   * parameterizedResult.setMethodName("testWithMultipleDataSets");
   * parameterizedResult.setParameters(new Object[]{"user1", "password123", true});
   * parameterizedResult.setMethodGroups(Arrays.asList("smoke", "regression"));
   * 
   * // Custom annotations will be automatically processed
   * List<Annotation> annotations = new ArrayList<>();
   * // annotations would be populated by TestNG framework
   * parameterizedResult.setAnnotations(annotations);
   * 
   * CPipelineHelper.addExecution(pipeline, parameterizedResult, new ArrayList<>());
   * }</pre>
   * 
   * @see CPipelineExecution
   * @see CPipelineExecutionMetaData
   * @see CTestResult
   */
  public static void addExecution(CPipeline pipeline, CTestResult testResult, List<CPipelineExecutionMetaData> executionMetaData) {
    if (!CPipelineTestNGConfigs.isEnabled()) return;

    CPipelineExecution execution = new CPipelineExecution();

    if (testResult.getTestIds() != null)
      testResult.getTestIds().forEach(t -> executionMetaData.add(readExecutionMetaData("Test ID", t)));

    if (testResult.getOpenDefectIds() != null)
      testResult.getOpenDefectIds().forEach(t -> executionMetaData.add(readExecutionMetaData("Open Defect ID", t)));

    if (testResult.getDefectIds() != null)
      testResult.getDefectIds().forEach(t -> executionMetaData.add(readExecutionMetaData("Defect ID", t)));

    if (testResult.getDeferredId() != null)
      testResult.getDeferredId().forEach(t -> executionMetaData.add(readExecutionMetaData("Deferred ID", t)));

    if (testResult.getMethodGroups() != null)
      testResult.getMethodGroups().forEach(t -> executionMetaData.add(readExecutionMetaData("Method Group", t)));

    if (testResult.getParameters() != null && testResult.getParameters().length > 0)
      executionMetaData.add(readExecutionMetaData("Parameters", CJsonUtil.toString(testResult.getParameters())));

    if (StringUtils.isNotBlank(testResult.getAwaiting()))
      executionMetaData.add(readExecutionMetaData("Awaiting", testResult.getAwaiting()));

    if (testResult.getRegressionDepth() != null)
      executionMetaData.add(readExecutionMetaData("Regression Depth", testResult.getRegressionDepth().toString()));

    if (testResult.getSeverityLevel() != null)
      executionMetaData.add(readExecutionMetaData("Severity Level", testResult.getSeverityLevel().toString()));

    if (StringUtils.isNotBlank(testResult.getHost()))
      executionMetaData.add(readExecutionMetaData("Host", testResult.getHost()));

    if (testResult.getAnnotations() != null) {
      for (Object annotation : testResult.getAnnotations()) {
        if (skipAlreadyAddedAnnotations(annotation)) continue;
        Annotation annot = (Annotation) annotation;
        Class<? extends Annotation> annotationType = annot.annotationType();
        executionMetaData.add(readExecutionMetaData(
            annotationType.getSimpleName(),
            annot.toString().replace("@" + annotationType.getName(), "")
        ));
      }
    }

    if (testResult.getExceptionInfo() != null)
      execution.setException(new CPipelineExecutionException(
          testResult.getExceptionInfo().getType(),
          testResult.getExceptionInfo().getMessage(),
          testResult.getExceptionInfo().getStackTrace()));

    if (testResult.getExecutionMetadata() != null)
      testResult.getExecutionMetadata().forEach(m -> executionMetaData.add(readExecutionMetaData(m.getName(), m.getValue())));

    execution.setPipeline(pipeline)
        .setStatus(new CPipelineStatus(testResult.getStatus().getId(), testResult.getStatus().name()))
        .setExecutor(getExecutor())
        .setStartTime(testResult.getStartTime())
        .setEndTime(testResult.getEndTime())
        .setTestStartTime(testResult.getTestStartTime())
        .setTestEndTime(testResult.getTestEndTime())
        .setBeforeClassStartTime(testResult.getBeforeClassStartTime())
        .setBeforeClassEndTime(testResult.getBeforeClassEndTime())
        .setBeforeMethodStartTime(testResult.getBeforeMethodStartTime())
        .setBeforeMethodEndTime(testResult.getBeforeMethodEndTime())
        .setPackageName(testResult.getPackageName())
        .setClassName(testResult.getClassName())
        .setMethodName(testResult.getMethodName())
        .setMetadata(executionMetaData);

    CPipelineBaseDao.merge(execution);
  }

  /**
   * Determines whether certain annotations should be skipped during metadata extraction.
   * This method filters out annotations that are already processed elsewhere to avoid duplication.
   * 
   * <p>The following annotations are skipped:
   * <ul>
   *   <li>{@code @Test} - TestNG's basic test annotation</li>
   *   <li>{@code @CRegression} - Custom regression annotation</li>
   *   <li>{@code @CSeverity} - Custom severity annotation</li>
   *   <li>{@code @CDefects} - Custom defects annotation</li>
   *   <li>{@code @CAwaiting} - Custom awaiting annotation</li>
   *   <li>{@code @COpenDefects} - Custom open defects annotation</li>
   *   <li>{@code @CDeferred} - Custom deferred annotation</li>
   *   <li>{@code @CTestIds} - Custom test IDs annotation</li>
   *   <li>{@code @CIgnored} - Custom ignored annotation</li>
   * </ul>
   *
   * @param annotation the annotation to check
   * @return true if the annotation should be skipped, false otherwise
   * 
   * @example
   * <pre>{@code
   * // This method is used internally during annotation processing
   * for (Object annotation : testResult.getAnnotations()) {
   *     if (skipAlreadyAddedAnnotations(annotation)) {
   *         continue; // Skip processing this annotation
   *     }
   *     // Process custom annotations that aren't already handled
   *     processCustomAnnotation(annotation);
   * }
   * }</pre>
   */
  private static boolean skipAlreadyAddedAnnotations(Object annotation) {
    return annotation instanceof Test ||
        annotation instanceof CRegression ||
        annotation instanceof CSeverity ||
        annotation instanceof CDefects ||
        annotation instanceof CAwaiting ||
        annotation instanceof COpenDefects ||
        annotation instanceof CDeferred ||
        annotation instanceof CTestIds ||
        annotation instanceof CIgnored;
  }

  /**
   * Creates and builds a new pipeline instance based on current configuration settings.
   * This method handles the complete pipeline creation process including project setup
   * and metadata association.
   * 
   * <p>The pipeline creation process includes:
   * <ul>
   *   <li>Merging the project configuration into the database</li>
   *   <li>Processing and converting pipeline metadata</li>
   *   <li>Creating a new pipeline with all configuration properties</li>
   *   <li>Persisting the pipeline to the database</li>
   * </ul>
   *
   * @return a newly created and persisted pipeline instance
   * 
   * @example
   * <pre>{@code
   * // This method is typically called internally, but here's how it works:
   * 
   * // Configuration would be set via environment variables or config files:
   * // CATOOLS_PIPELINE_TYPE=CI/CD
   * // CATOOLS_PIPELINE_NAME=nightly-regression
   * // CATOOLS_PIPELINE_DESCRIPTION=Nightly regression test suite
   * // CATOOLS_PIPELINE_NUMBER=build-123
   * // CATOOLS_ENVIRONMENT=staging
   * 
   * // The method creates a pipeline like this:
   * CPipeline newPipeline = buildPipeline();
   * 
   * System.out.println("Created pipeline: " + newPipeline.getName());
   * System.out.println("Pipeline type: " + newPipeline.getType());
   * System.out.println("Environment: " + newPipeline.getEnvironment());
   * System.out.println("Start date: " + newPipeline.getStartDate());
   * }</pre>
   * 
   * <p>Example of metadata processing:
   * <pre>{@code
   * // If pipeline metadata is configured like:
   * // CATOOLS_PIPELINE_METADATA=version:1.2.3,branch:main,commit:abc123
   * 
   * // The method will create metadata entries:
   * // - Name: "version", Value: "1.2.3"
   * // - Name: "branch", Value: "main" 
   * // - Name: "commit", Value: "abc123"
   * }</pre>
   * 
   * @see CPipelineConfigs
   * @see CPipelineMetaData
   * @see CPipelineBaseDao#merge(Object)
   */
  private static CPipeline buildPipeline() {
    CPipelineBaseDao.merge(CPipelineConfigs.getProject());

    List<CPipelineMetaData> metaData = CPipelineConfigs
        .getPipelineMetaData()
        .stream()
        .map(md -> readMetaData(md.getName(), md.getValue()))
        .toList();

    CPipeline pipeline = new CPipeline()
        .setType(CPipelineConfigs.getPipelineType())
        .setName(CPipelineConfigs.getPipelineName())
        .setDescription(CPipelineConfigs.getPipelineDescription())
        .setNumber(CPipelineConfigs.getPipelineNumber())
        .setStartDate(CDate.now())
        .setEnvironment(CPipelineConfigs.getEnvironment())
        .setProject(CPipelineConfigs.getProject())
        .setMetadata(metaData);

    return CPipelineBaseDao.merge(pipeline);
  }
}

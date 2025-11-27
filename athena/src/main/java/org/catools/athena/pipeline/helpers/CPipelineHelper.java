package org.catools.athena.pipeline.helpers;

import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.pipeline.configs.CAthenaPipelineConfigs;
import org.catools.athena.pipeline.model.PipelineDto;
import org.catools.athena.pipeline.model.PipelineExecutionDto;
import org.catools.athena.rest.feign.common.utils.JsonUtils;
import org.catools.athena.rest.feign.pipeline.configs.PipelineConfigs;
import org.catools.athena.rest.feign.pipeline.helpers.PipelineHelper;
import org.catools.common.annotations.*;
import org.catools.common.testng.model.CTestResult;
import org.catools.common.utils.CJsonUtil;
import org.catools.common.utils.CStringUtil;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Slf4j
public class CPipelineHelper {

    public static PipelineDto getPipeline() {
        if (CAthenaPipelineConfigs.alwaysCreateNewPipeline()) {
            return PipelineHelper.buildPipeline();
        }

        PipelineDto lastByName = PipelineHelper.getPipeline();

        if (!CAthenaPipelineConfigs.createPipelineIfNotExist()) {
            Objects.requireNonNull(lastByName,
                    "No pipeline with given name found, set CATOOLS_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST environment variable if you want to create one.");
        }

        return lastByName != null ? lastByName : PipelineHelper.buildPipeline();
    }

    public static void addResult(ITestResult testResult,
                                 PipelineDto pipeline,
                                 Instant beforeClassStartTime,
                                 Instant beforeMethodStartTime,
                                 Instant beforeClassEndTime,
                                 Instant beforeMethodEndTime) {

        PipelineExecutionDto executionDto = buildExecutionDto(testResult,
                pipeline,
                beforeClassStartTime,
                beforeMethodStartTime,
                beforeClassEndTime,
                beforeMethodEndTime);

        PipelineHelper.addExecution(executionDto);
    }

    public static PipelineExecutionDto buildExecutionDto(ITestResult testResult,
                                                         PipelineDto pipeline,
                                                         Instant beforeClassStartTime,
                                                         Instant beforeMethodStartTime,
                                                         Instant beforeClassEndTime,
                                                         Instant beforeMethodEndTime) {
        String packageName = testResult.getMethod().getRealClass().getPackage().getName();
        String className = testResult.getMethod().getRealClass().getSimpleName();
        Method method = testResult.getMethod().getConstructorOrMethod().getMethod();

        String methodName = method.getName();
        Object[] parameters = testResult.getParameters();

        PipelineExecutionDto executionDto = new PipelineExecutionDto();

        if (parameters != null && parameters.length > 0) {
            executionDto.setParameters(JsonUtils.writeValueAsString(parameters));
        }

        executionDto.setPackageName(packageName);
        executionDto.setClassName(className);
        executionDto.setMethodName(methodName);
        executionDto.setStartTime(beforeClassStartTime);
        executionDto.setEndTime(Instant.now());
        executionDto.setTestStartTime(testResult.getEndMillis() < 0 ? null : new Date(testResult.getStartMillis()).toInstant());
        executionDto.setTestEndTime(testResult.getEndMillis() < 0 ? null : new Date(testResult.getEndMillis()).toInstant());
        executionDto.setBeforeClassStartTime(beforeClassStartTime);
        executionDto.setBeforeClassEndTime(beforeClassEndTime);
        executionDto.setBeforeMethodStartTime(beforeMethodStartTime);
        executionDto.setBeforeMethodEndTime(beforeMethodEndTime);
        executionDto.setStatus(getStatus(testResult));
        executionDto.setExecutor(PipelineConfigs.getExecutorName());
        executionDto.setPipelineId(pipeline.getId());
        executionDto.setMetadata(CPipelineHelper.getExecutionMetadata(testResult, beforeClassStartTime, Instant.now()));

        return executionDto;
    }

    private static String getStatus(ITestResult testResult) {
        switch (testResult.getStatus()) {
            case ITestResult.CREATED:
                return "CREATED";
            case ITestResult.SUCCESS:
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                return "SUCCESS";
            case ITestResult.FAILURE:
                return "FAILURE";
            case ITestResult.SKIP:
                return "SKIP";
            case ITestResult.STARTED:
                return "STARTED";
            default:
                return "NOT_SET";
        }
    }

    public static Set<MetadataDto> getExecutionMetadata(ITestResult testNgResult, Instant testStartTime, Instant testEndTime) {
        CTestResult testResult = new CTestResult(testNgResult, Date.from(testStartTime), Date.from(testEndTime));
        Set<MetadataDto> executionMetaData = new HashSet<>();

        if (testResult.getTestIds() != null && testResult.getTestIds().isNotEmpty()) {
            testResult.getTestIds().forEach(t -> executionMetaData.add(new MetadataDto("Test ID", t)));
        }

        if (testResult.getOpenDefectIds() != null && testResult.getOpenDefectIds().isNotEmpty()) {
            testResult.getOpenDefectIds().forEach(t -> executionMetaData.add(new MetadataDto("Open Defect ID", t)));
        }

        if (testResult.getDefectIds() != null && testResult.getDefectIds().isNotEmpty()) {
            testResult.getDefectIds().forEach(t -> executionMetaData.add(new MetadataDto("Defect ID", t)));
        }

        if (testResult.getDeferredId() != null && testResult.getDeferredId().isNotEmpty()) {
            testResult.getDeferredId().forEach(t -> executionMetaData.add(new MetadataDto("Deferred ID", t)));
        }

        if (testResult.getMethodGroups() != null && testResult.getMethodGroups().isNotEmpty()) {
            testResult.getMethodGroups().forEach(t -> executionMetaData.add(new MetadataDto("Method Group", t)));
        }

        if (testResult.getParameters() != null && testResult.getParameters().length > 0) {
            executionMetaData.add(new MetadataDto("Parameters", CJsonUtil.toString(testResult.getParameters())));
        }

        if (CStringUtil.isNotBlank(testResult.getAwaiting())) {
            executionMetaData.add(new MetadataDto("Awaiting", testResult.getAwaiting()));
        }

        if (testResult.getAnnotations() != null) {
            for (Object annotation : testResult.getAnnotations()) {
                if (isStandardAnnotations(annotation)) {
                    continue;
                }
                Annotation annot = (Annotation) annotation;
                Class<? extends Annotation> annotationType = annot.annotationType();
                executionMetaData.add(new MetadataDto(annotationType.getSimpleName(), annot.toString().replace("@" + annotationType.getName(), "")));
            }
        }

        return executionMetaData;
    }

    private static boolean isStandardAnnotations(Object annotation) {
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
}

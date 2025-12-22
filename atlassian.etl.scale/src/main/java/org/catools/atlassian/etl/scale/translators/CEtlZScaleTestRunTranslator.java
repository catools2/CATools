package org.catools.atlassian.etl.scale.translators;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.etl.scale.helpers.CEtlZScaleSyncHelper;
import org.catools.atlassian.scale.CZScaleClient;
import org.catools.atlassian.scale.model.CZScaleTestCase;
import org.catools.atlassian.scale.model.CZScaleTestExecution;
import org.catools.atlassian.scale.model.CZScaleTestRun;
import org.catools.atlassian.scale.rest.cycle.CZScaleExecutionStatus;
import org.catools.common.utils.CStringUtil;
import org.catools.etl.tms.cache.CEtlCacheManager;
import org.catools.etl.tms.dao.CEtlBaseDao;
import org.catools.etl.tms.model.*;

import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * Translator class for converting ZScale test runs and executions into ETL-compatible objects.
 * Provides methods to map test run and execution data to ETL cycles and executions.
 */
@Slf4j
@UtilityClass
public class CEtlZScaleTestRunTranslator {

  /**
   * Translates a ZScale test run into an ETL cycle.
   *
   * @param version The ETL version associated with the test run.
   * @param testRun The ZScale test run to be translated.
   * @return The translated ETL cycle.
   */
  public static CEtlCycle translateTestRun(CEtlVersion version, CZScaleTestRun testRun) {
    Objects.requireNonNull(testRun);

    try {
      String folder = CStringUtil.EMPTY;

      // Ensure the folder path ends with a slash
      if (StringUtils.isNotBlank(testRun.getFolder())) {
        folder = testRun.getFolder().trim();
        if (!folder.endsWith("/")) {
          folder += "/";
        }
      }

      // Find or create the ETL cycle
      CEtlCycle etlCycle = CEtlBaseDao.find(CEtlCycle.class, testRun.getKey());
      if (etlCycle == null) {
        etlCycle = new CEtlCycle();
        etlCycle.setId(testRun.getKey());
      }

      // Set properties for the ETL cycle
      etlCycle.setVersion(version);
      etlCycle.setName(folder + testRun.getName());
      etlCycle.setEndDate(testRun.getPlannedEndDate());
      etlCycle.setStartDate(testRun.getPlannedStartDate());

      return etlCycle;
    } catch (Exception e) {
      log.error("Failed to translate test run {} to cycle.", testRun, e);
      throw e;
    }
  }

  /**
   * Translates a ZScale test execution into an ETL execution.
   *
   * @param testRun The ZScale test run associated with the execution.
   * @param cycle The ETL cycle associated with the execution.
   * @param execution The ZScale test execution to be translated.
   * @return The translated ETL execution.
   */
  public static CEtlExecution translateExecution(
      CZScaleTestRun testRun, CEtlCycle cycle, CZScaleTestExecution execution) {
    Objects.requireNonNull(execution);

    // Find or create the ETL execution
    CEtlExecution etlExecution =
        CEtlBaseDao.find(CEtlExecution.class, String.valueOf(execution.getId()));
    if (etlExecution == null) {
      etlExecution = new CEtlExecution();
      etlExecution.setId(String.valueOf(execution.getId()));
    }

    try {
      // Retrieve or create the associated ETL item
      try {
        etlExecution.setItem(CEtlCacheManager.readItem(execution.getTestCaseKey()));
      } catch (InvalidParameterException e) {
        CZScaleTestCase testcase = CZScaleClient.TestCases.getTestCase(execution.getTestCaseKey());
        if (testcase == null) {
          log.error("Failed to read testcase {} after 5 retry", execution.getTestCaseKey());
          return null;
        }
        etlExecution.setItem(CEtlZScaleSyncHelper.addItem(testcase));
      }

      // Set properties for the ETL execution
      etlExecution.setStatus(getStatus(execution.getStatus()));
      etlExecution.setExecutor(getExecutor(execution));
      etlExecution.setCycle(cycle);
      etlExecution.setCreated(testRun.getCreatedOn());
      etlExecution.setExecuted(execution.getExecutionDate());

      return etlExecution;
    } catch (Exception e) {
      log.error("Failed to translate execution {}.", execution, e);
      throw e;
    }
  }

  /**
   * Retrieves the executor of a test execution.
   *
   * @param execution The ZScale test execution.
   * @return The ETL user representing the executor.
   */
  private static CEtlUser getExecutor(CZScaleTestExecution execution) {
    return StringUtils.isBlank(execution.getExecutedBy())
        ? CEtlUser.UNSET
        : CEtlCacheManager.readUser(new CEtlUser(execution.getExecutedBy()));
  }

  /**
   * Retrieves the status of a test execution.
   *
   * @param status The ZScale execution status.
   * @return The corresponding ETL execution status.
   */
  private static CEtlExecutionStatus getStatus(CZScaleExecutionStatus status) {
    return status == null || StringUtils.isBlank(status.getScaleName())
        ? CEtlExecutionStatus.UNSET
        : CEtlCacheManager.readExecutionStatus(new CEtlExecutionStatus(status.getScaleName()));
  }
}

package org.catools.atlassian.etl.scale.translators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.scale.model.CZScaleChangeHistory;
import org.catools.atlassian.scale.model.CZScaleChangeHistoryItem;
import org.catools.atlassian.scale.model.CZScaleTestCase;
import org.catools.common.collections.CList;
import org.catools.etl.tms.cache.CEtlCacheManager;
import org.catools.etl.tms.dao.CEtlBaseDao;
import org.catools.etl.tms.model.*;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * Translator class for converting ZScale test cases into ETL items.
 * Provides methods to map test case data to ETL-compatible objects.
 */
@Slf4j
public class CEtlZScaleTestCaseTranslator {
  private static final String TEST_CASE_NAME = "Test";

  /**
   * Translates a ZScale test case into an ETL item.
   *
   * @param project The ETL project associated with the test case.
   * @param versions The versions associated with the project.
   * @param testCase The ZScale test case to be translated.
   * @return The translated ETL item.
   */
  public static CEtlItem translateTestCase(CEtlProject project, CEtlVersions versions, CZScaleTestCase testCase) {
    Objects.requireNonNull(project);
    Objects.requireNonNull(versions);
    Objects.requireNonNull(testCase);

    try {
      CEtlItem item = CEtlBaseDao.find(CEtlItem.class, String.valueOf(testCase.getKey()));
      if (item == null) {
        item = new CEtlItem();
        item.setId(String.valueOf(testCase.getKey()));
      }

      item.setName(testCase.getName());
      item.setCreated(testCase.getCreatedOn());
      item.setUpdated(testCase.getUpdatedOn());
      item.setProject(project);
      item.setStatus(getStatus(testCase.getStatus()));
      item.setPriority(getPriority(testCase.getPriority()));
      item.setType(getItemType());
      item.setVersions(getIssueVersions(versions, testCase));

      item.getMetadata().clear();
      addIssueMetaData(testCase, item);

      item.getStatusTransitions().clear();
      addStatusTransition(testCase, item);

      return item;
    } catch (Throwable t) {
      log.error("Failed to translate testcase {} to item.", testCase, t);
      throw t;
    }
  }

  /**
   * Retrieves the item type for a test case.
   *
   * @return The ETL item type for test cases.
   */
  private static CEtlItemType getItemType() {
    return CEtlCacheManager.readType(new CEtlItemType(TEST_CASE_NAME));
  }

  /**
   * Adds status transition information to an ETL item.
   *
   * @param issue The ZScale test case containing status history.
   * @param item The ETL item to which status transitions will be added.
   */
  private static void addStatusTransition(CZScaleTestCase issue, CEtlItem item) {
    if (issue.getHistories() != null) {
      CList<CEtlItemStatusTransition> statusTransitions = readStatusTransitionInfoFromResponse(issue, item);

      if (statusTransitions.isNotEmpty()) {
        // Set from status to previous status for each transition
        setFromStatusToPreviousStatus(item, statusTransitions);
      }
    }
  }

  /**
   * Sets the "from" status for each status transition based on the previous status.
   *
   * @param item The ETL item to which transitions belong.
   * @param statusTransitions The list of status transitions.
   */
  private static void setFromStatusToPreviousStatus(CEtlItem item, CList<CEtlItemStatusTransition> statusTransitions) {
    statusTransitions.sort(Comparator.comparing(CEtlItemStatusTransition::getOccurred));
    statusTransitions.get(0).setFrom(new CEtlStatus("Open"));
    for (int i = 1; i < statusTransitions.size(); i++) {
      statusTransitions.get(i).setFrom(statusTransitions.get(i - 1).getTo());
    }

    item.setStatusTransitions(statusTransitions.toSet());
  }

  /**
   * Reads status transition information from a ZScale test case.
   *
   * @param issue The ZScale test case containing change history.
   * @param item The ETL item to which transitions will be added.
   * @return A list of status transitions.
   */
  private static CList<CEtlItemStatusTransition> readStatusTransitionInfoFromResponse(CZScaleTestCase issue, CEtlItem item) {
    CList<CEtlItemStatusTransition> statusTransitions = new CList<>();
    for (CZScaleChangeHistory changelog : issue.getHistories()) {
      if (changelog.getChangeHistoryItems() == null) continue;

      for (CZScaleChangeHistoryItem statusChangelog : changelog.getChangeHistoryItems()) {
        if (!StringUtils.equalsIgnoreCase(statusChangelog.getFieldName(), "status")) continue;

        Date occurred = changelog.getHistoryDate();
        CEtlStatus to = getStatus(statusChangelog.getNewValue());
        statusTransitions.add(new CEtlItemStatusTransition(occurred, null, to, item));
      }
    }
    return statusTransitions;
  }

  /**
   * Adds metadata from a ZScale test case to an ETL item.
   *
   * @param testCase The ZScale test case containing metadata.
   * @param item The ETL item to which metadata will be added.
   */
  private static void addIssueMetaData(CZScaleTestCase testCase, CEtlItem item) {
    if (StringUtils.isNotEmpty(testCase.getComponent())) {
      item.addItemMetaData(getMetaData("Component", testCase.getComponent()));
    }

    if (StringUtils.isNotEmpty(testCase.getLastTestResultStatus())) {
      item.addItemMetaData(getMetaData("LastTestResultStatus", testCase.getLastTestResultStatus()));
    }

    if (StringUtils.isNotEmpty(testCase.getFolder())) {
      item.addItemMetaData(getMetaData("Folder", testCase.getFolder()));
    }

    if (StringUtils.isNotEmpty(testCase.getOwner())) {
      item.addItemMetaData(getMetaData("Owner", testCase.getOwner()));
    }

    if (StringUtils.isNotEmpty(testCase.getCreatedBy())) {
      item.addItemMetaData(getMetaData("CreatedBy", testCase.getCreatedBy()));
    }

    if (testCase.getLabels() != null) {
      for (String label : testCase.getLabels()) {
        item.addItemMetaData(getMetaData("Label", label));
      }
    }

    if (testCase.getIssueLinks() != null) {
      for (String issueLink : testCase.getIssueLinks()) {
        item.addItemMetaData(getMetaData("IssueLink", issueLink));
      }
    }

    if (testCase.getCustomFields() != null) {
      testCase.getCustomFields()
          .forEach((k, v) -> item.addItemMetaData(getMetaData(k, v)));
    }
  }

  /**
   * Retrieves metadata for a given name and value.
   *
   * @param name The name of the metadata.
   * @param value The value of the metadata.
   * @return The ETL item metadata.
   */
  private static CEtlItemMetaData getMetaData(String name, String value) {
    return CEtlCacheManager.readMetaData(new CEtlItemMetaData(name, value));
  }

  /**
   * Retrieves the status for a given status name.
   *
   * @param statusName The name of the status.
   * @return The corresponding ETL status.
   */
  private static CEtlStatus getStatus(String statusName) {
    return StringUtils.isBlank(statusName) ?
        CEtlStatus.UNSET :
        CEtlCacheManager.readStatus(new CEtlStatus(statusName.toUpperCase()));
  }

  /**
   * Retrieves the priority for a given priority name.
   *
   * @param priorityName The name of the priority.
   * @return The corresponding ETL priority.
   */
  private static CEtlPriority getPriority(String priorityName) {
    return StringUtils.isBlank(priorityName) ?
        CEtlPriority.UNSET :
        CEtlCacheManager.readPriority(new CEtlPriority(priorityName.toUpperCase()));
  }

  /**
   * Retrieves the versions associated with a test case.
   *
   * @param versions The available versions for the project.
   * @param testCase The ZScale test case containing version information.
   * @return The ETL versions associated with the test case.
   */
  private static CEtlVersions getIssueVersions(CEtlVersions versions, CZScaleTestCase testCase) {
    if (testCase == null || testCase.getCustomFields() == null || testCase.getCustomFields().isEmpty()) {
      return new CEtlVersions();
    }

    CList<String> potentialVersions = testCase
        .getCustomFields()
        .getAll((k, v) -> k.toLowerCase().contains("version")).values();

    CList<CEtlVersion> versionsToAdd = versions.getAll(v -> potentialVersions.contains(v.getName()));

    CEtlVersions etlVersions = new CEtlVersions();
    for (CEtlVersion version : versionsToAdd) {
      etlVersions.add(CEtlCacheManager.readVersion(version));
    }

    return etlVersions;
  }
}
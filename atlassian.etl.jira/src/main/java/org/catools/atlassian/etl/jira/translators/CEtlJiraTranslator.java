package org.catools.atlassian.etl.jira.translators;

import com.atlassian.jira.rest.client.api.domain.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.etl.jira.configs.CEtlJiraConfigs;
import org.catools.atlassian.etl.jira.translators.parsers.CEtlJiraParser;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.etl.tms.cache.CEtlCacheManager;
import org.catools.etl.tms.dao.CEtlBaseDao;
import org.catools.etl.tms.model.*;
import org.codehaus.jettison.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Translator class for converting Jira issues into {@link CEtlItem} objects.
 *
 * <p>Example:
 *
 * <pre>{@code
 * Issue issue = ...; // fetched from Jira REST client
 * CEtlItem item = CEtlJiraTranslator.translateIssue(issue);
 * }</pre>
 */
@Slf4j
@UtilityClass
public class CEtlJiraTranslator {

  /**
   * Translates a set of Jira issues into a {@link CEtlItems} collection.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CSet<Issue> issues = ...;
   * CEtlItems items = CEtlJiraTranslator.translateIssues(issues);
   * }</pre>
   *
   * @param issues the set of Jira issues to be translated
   * @return a CEtlItems collection containing the translated items
   */
  public static CEtlItems translateIssues(CSet<Issue> issues) {
    return new CEtlItems(issues.mapToSet(CEtlJiraTranslator::translateIssue));
  }

  /**
   * Translates a single Jira issue into a {@link CEtlItem} object.
   *
   * <p>Example:
   *
   * <pre>{@code
   * Issue issue = ...;
   * CEtlItem item = CEtlJiraTranslator.translateIssue(issue);
   * System.out.println(item.getId());
   * }</pre>
   *
   * @param issue the Jira issue to be translated
   * @return the translated CEtlItem object
   */
  public static CEtlItem translateIssue(Issue issue) {
    Objects.requireNonNull(issue);

    CEtlItem item = CEtlBaseDao.find(CEtlItem.class, String.valueOf(issue.getKey()));
    if (item == null) {
      item = new CEtlItem();
      item.setId(String.valueOf(issue.getKey()));
    }

    try {
      CEtlProject project = getProject(issue);
      item.setProject(project);
      item.setVersions(getIssueVersions(issue, project));
      item.setStatus(getStatus(issue));
      item.setPriority(getPriority(issue));
      item.setType(getItemType(issue));
      item.setName(StringUtils.substring(issue.getSummary(), 0, 1000));
      item.setCreated(issue.getCreationDate().toDate());
      item.setUpdated(issue.getUpdateDate() == null ? null : issue.getUpdateDate().toDate());

      item.getMetadata().clear();
      addIssueMetaData(issue, item);

      item.getStatusTransitions().clear();
      addStatusTransition(issue, item);

      return item;
    } catch (Throwable t) {
      log.error("Failed to translate issue {} to item.", issue, t);
      throw t;
    }
  }

  /**
   * Adds status transitions to the {@link CEtlItem} based on the changelog of the Jira issue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * Issue issue = ...; CEtlItem item = new CEtlItem();
   * CEtlJiraTranslator.addStatusTransition(issue, item);
   * }</pre>
   *
   * @param issue the Jira issue containing the changelog
   * @param item the CEtlItem to which status transitions will be added
   */
  private static void addStatusTransition(Issue issue, CEtlItem item) {
    if (issue.getChangelog() != null) {
      for (ChangelogGroup changelog : issue.getChangelog()) {
        if (changelog == null || changelog.getAuthor() == null) {
          continue;
        }

        CList<ChangelogItem> transitions =
            new CSet<>(changelog.getItems())
                .getAll(f -> f != null && StringUtils.equalsIgnoreCase(f.getField(), "status"));

        for (ChangelogItem statusChangelog : transitions) {
          if (statusChangelog == null) continue;

          Date occurred = changelog.getCreated() == null ? null : changelog.getCreated().toDate();
          CEtlStatus from = getStatus(statusChangelog.getFromString());
          CEtlStatus to = getStatus(statusChangelog.getToString());
          item.addStatusTransition(new CEtlItemStatusTransition(occurred, from, to, item));
        }
      }
    }
  }

  /**
   * Adds metadata to the {@link CEtlItem} based on various fields of the Jira issue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * Issue issue = ...; CEtlItem item = new CEtlItem();
   * CEtlJiraTranslator.addIssueMetaData(issue, item);
   * }</pre>
   *
   * @param issue the Jira issue containing the fields
   * @param item the CEtlItem to which metadata will be added
   */
  private static void addIssueMetaData(Issue issue, CEtlItem item) {
    for (BasicComponent component : issue.getComponents()) {
      item.addItemMetaData(getMetaData("Component", component.getName()));
    }

    if (issue.getAssignee() != null
        && StringUtils.isNotBlank(issue.getAssignee().getEmailAddress())) {
      item.addItemMetaData(getMetaData("Assignee", issue.getAssignee().getEmailAddress()));
    }

    if (issue.getLabels() != null) {
      for (String label : issue.getLabels()) {
        item.addItemMetaData(getMetaData("Label", label));
      }
    }

    if (issue.getFields() != null) {
      List<String> fieldsToRead = CEtlJiraConfigs.JiraSync.getFieldsToRead();
      CList<IssueField> noneNull =
          new CSet<>(issue.getFields()).getAll(CEtlJiraTranslator::valueIsNotNull);
      for (IssueField field : noneNull) {
        CHashMap<String, String> fieldsToSync = CEtlJiraParser.parserJiraField(field);

        if (!fieldsToRead.isEmpty()) {
          fieldsToSync = fieldsToSync.getAll((k, v) -> fieldsToRead.contains(k));
        }

        fieldsToSync.forEach((key, val) -> item.addItemMetaData(getMetaData(key, val)));
      }
    }
  }

  /**
   * Translates a Jira {@link Version} into a {@link CEtlVersion} object.
   *
   * <p>Example:
   *
   * <pre>{@code
   * Version v = ...;
   * CEtlVersion cv = CEtlJiraTranslator.translateVersion(project, v);
   * }</pre>
   *
   * @param project the CEtlProject associated with the version
   * @param version the Jira version to be translated
   * @return the translated CEtlVersion object
   */
  public static CEtlVersion translateVersion(CEtlProject project, Version version) {
    return version == null || StringUtils.isBlank(version.getName())
        ? CEtlVersion.UNSET
        : CEtlCacheManager.readVersion(new CEtlVersion(version.getName(), project));
  }

  /**
   * Extracts and translates the fix and affected versions from a Jira issue into a {@link
   * CEtlVersions} collection.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CEtlVersions versions = CEtlJiraTranslator.getIssueVersions(issue, project);
   * }</pre>
   *
   * @param issue the Jira issue containing the versions
   * @param project the CEtlProject associated with the versions
   * @return a CEtlVersions collection containing the translated versions
   */
  private static CEtlVersions getIssueVersions(Issue issue, CEtlProject project) {
    CEtlVersions versions = new CEtlVersions();

    if (issue.getFixVersions() != null) {
      for (Version fixVersion : issue.getFixVersions()) {
        versions.add(CEtlCacheManager.readVersion(translateVersion(project, fixVersion)));
      }
    }

    if (issue.getAffectedVersions() != null) {
      for (Version fixVersion : issue.getAffectedVersions()) {
        versions.add(CEtlCacheManager.readVersion(translateVersion(project, fixVersion)));
      }
    }

    return versions;
  }

  /**
   * Retrieves or creates a {@link CEtlItemMetaData} object based on the provided name and value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CEtlItemMetaData meta = CEtlJiraTranslator.getMetaData("Priority", "High");
   * }</pre>
   *
   * @param name the name of the metadata
   * @param value the value of the metadata
   * @return the CEtlItemMetaData object
   */
  private static CEtlItemMetaData getMetaData(String name, String value) {
    return CEtlCacheManager.readMetaData(new CEtlItemMetaData(name, value));
  }

  /**
   * Retrieves or creates a {@link CEtlProject} object based on the project information from a Jira
   * issue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CEtlProject project = CEtlJiraTranslator.getProject(issue);
   * }</pre>
   *
   * @param issue the Jira issue containing the project information
   * @return the CEtlProject object
   */
  private static CEtlProject getProject(Issue issue) {
    return issue.getProject() == null || StringUtils.isBlank(issue.getProject().getName())
        ? CEtlProject.UNSET
        : CEtlCacheManager.readProject(new CEtlProject(issue.getProject().getName()));
  }

  /**
   * Retrieves or creates a {@link CEtlItemType} object based on the issue type from a Jira issue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CEtlItemType type = CEtlJiraTranslator.getItemType(issue);
   * }</pre>
   *
   * @param issue the Jira issue containing the issue type information
   * @return the CEtlItemType object
   */
  private static CEtlItemType getItemType(Issue issue) {
    return issue.getIssueType() == null || StringUtils.isBlank(issue.getIssueType().getName())
        ? CEtlItemType.UNSET
        : CEtlCacheManager.readType(new CEtlItemType(issue.getIssueType().getName()));
  }

  /**
   * Retrieves or creates a {@link CEtlPriority} object based on the priority from a Jira issue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CEtlPriority priority = CEtlJiraTranslator.getPriority(issue);
   * }</pre>
   *
   * @param issue the Jira issue containing the priority information
   * @return the CEtlPriority object
   */
  private static CEtlPriority getPriority(Issue issue) {
    return issue.getPriority() == null || StringUtils.isBlank(issue.getPriority().getName())
        ? CEtlPriority.UNSET
        : CEtlCacheManager.readPriority(
            new CEtlPriority(issue.getPriority().getName().toUpperCase()));
  }

  /**
   * Retrieves or creates a {@link CEtlStatus} object based on the provided status name.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CEtlStatus status = CEtlJiraTranslator.getStatus("IN PROGRESS");
   * }</pre>
   *
   * @param statusName the name of the status
   * @return the CEtlStatus object
   */
  private static CEtlStatus getStatus(String statusName) {
    return StringUtils.isBlank(statusName)
        ? CEtlStatus.UNSET
        : CEtlCacheManager.readStatus(new CEtlStatus(statusName.toUpperCase()));
  }

  /**
   * Retrieves or creates a {@link CEtlStatus} object based on the status from a Jira issue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CEtlStatus status = CEtlJiraTranslator.getStatus(issue);
   * }</pre>
   *
   * @param issue the Jira issue containing the status information
   * @return the CEtlStatus object
   */
  private static CEtlStatus getStatus(Issue issue) {
    return issue.getStatus() == null || StringUtils.isBlank(issue.getStatus().getName())
        ? CEtlStatus.UNSET
        : CEtlCacheManager.readStatus(new CEtlStatus(issue.getStatus().getName().toUpperCase()));
  }

  /**
   * Checks if the value of an {@link IssueField} is not null or a JSON null representation.
   *
   * <p>Example:
   *
   * <pre>{@code
   * boolean ok = CEtlJiraTranslator.valueIsNotNull(issueField);
   * }</pre>
   *
   * @param f the IssueField to be checked
   * @return true if the value is not null, false otherwise
   */
  private static boolean valueIsNotNull(IssueField f) {
    return f.getValue() != null
        && f.getValue() != JSONObject.EXPLICIT_NULL
        && f.getValue() != JSONObject.NULL;
  }
}

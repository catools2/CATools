package org.catools.atlassian.etl.jira;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import lombok.experimental.UtilityClass;
import org.catools.atlassian.etl.jira.translators.CEtlJiraTranslator;
import org.catools.atlassian.jira.client.CJiraClient;
import org.catools.common.collections.CSet;
import org.catools.common.date.CDate;
import org.catools.etl.tms.dao.CEtlItemDao;
import org.catools.etl.tms.dao.CEtlLastSyncDao;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for synchronizing JIRA data into the ETL system.
 * Provides methods to sync projects and issues based on specified criteria.
 */
@UtilityClass
public class CEtlJiraSyncClient {
  private static final String JIRA = "JIRA";

  /**
   * Synchronizes JIRA projects and their issues based on the provided project names and issue types.
   *
   * @param projectNamesToSync  A set of project names to be synchronized.
   * @param issueTypes          A list of issue types to be synchronized for each project.
   * @param parallelInputCount  The number of parallel input threads to use during synchronization.
   * @param parallelOutputCount The number of parallel output threads to use during synchronization.
   * @throws NullPointerException if any of the parameters are null.
   */
  public static void syncJira(
      CSet<String> projectNamesToSync,
      List<String> issueTypes,
      int parallelInputCount,
      int parallelOutputCount) {
    Objects.requireNonNull(projectNamesToSync);
    Objects.requireNonNull(issueTypes);

    CSet<BasicProject> projects = CJiraClient.getProjects();
    for (BasicProject project : projects.getAll(p -> projectNamesToSync.contains(p.getName()))) {
      Date syncStartTime = CDate.now();
      for (String issueType : issueTypes) {
        addItems(project, issueType, parallelInputCount, parallelOutputCount);
      }
      CEtlLastSyncDao.updateProjectLastSync(JIRA, project.getName(), syncStartTime);
    }
  }

  /**
   * Adds items from a specific JIRA project and issue type to the ETL system.
   *
   * @param project             The JIRA project from which to add items.
   * @param issueType           The type of issues to be added from the project.
   * @param parallelInputCount  The number of parallel input threads to use during the addition.
   * @param parallelOutputCount The number of parallel output threads to use during the addition.
   * @throws NullPointerException if any of the parameters are null.
   */
  public static void addItems(
      BasicProject project,
      String issueType,
      int parallelInputCount,
      int parallelOutputCount) {
    Objects.requireNonNull(project);
    Objects.requireNonNull(issueType);

    Date lastSync = CEtlLastSyncDao.getItemsLastSync(JIRA, project.getName(), issueType);

    Date syncStartTime = CDate.now();
    CJiraClient.search(
        project.getKey(),
        issueType,
        lastSync,
        parallelInputCount,
        parallelOutputCount,
        issues -> {
          if (issues != null && issues.isNotEmpty())
            CEtlItemDao.mergeItems(CEtlJiraTranslator.translateIssues(issues));
        });

    CEtlLastSyncDao.updateItemsLastSync(JIRA, project.getName(), issueType, syncStartTime);
  }
}

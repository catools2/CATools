package org.catools.atlassian.etl.scale.helpers;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.etl.jira.translators.CEtlJiraTranslator;
import org.catools.atlassian.etl.scale.translators.CEtlZScaleTestCaseTranslator;
import org.catools.atlassian.jira.client.CJiraClient;
import org.catools.atlassian.scale.model.CZScaleTestCase;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CMap;
import org.catools.etl.tms.cache.CEtlCacheManager;
import org.catools.etl.tms.dao.CEtlItemDao;
import org.catools.etl.tms.model.CEtlItem;
import org.catools.etl.tms.model.CEtlProject;
import org.catools.etl.tms.model.CEtlVersion;
import org.catools.etl.tms.model.CEtlVersions;

import java.util.Objects;

/**
 * Utility class for synchronizing ZScale test cases with the ETL system.
 * Provides methods to manage projects, versions, and test cases.
 */
@UtilityClass
@Slf4j
public class CEtlZScaleSyncHelper {
  private static final CSet<BasicProject> PROJECTS = new CSet<>();
  private static final CMap<String, CList<CEtlVersion>> PROJECT_VERSION = new CHashMap<>();

  /**
   * Adds a ZScale test case to the ETL system.
   *
   * @param testcase The ZScale test case to be added.
   * @return The corresponding ETL item after being added to the cache.
   */
  public static CEtlItem addItem(CZScaleTestCase testcase) {
    CEtlProject etlProject = getProject(testcase);
    CEtlVersions versions = new CEtlVersions(getProjectVersions(testcase.getProjectKey(), etlProject));
    CEtlItemDao.mergeItem(CEtlZScaleTestCaseTranslator.translateTestCase(etlProject, versions, testcase));
    return CEtlCacheManager.readItem(testcase.getKey());
  }

  /**
   * Retrieves the ETL project corresponding to the given ZScale test case.
   *
   * @param testcase The ZScale test case.
   * @return The corresponding ETL project, or `CEtlProject.UNSET` if not found.
   */
  private static CEtlProject getProject(CZScaleTestCase testcase) {
    BasicProject project = getProjectByKey(testcase.getProjectKey());
    return project == null || StringUtils.isBlank(project.getName()) ?
        CEtlProject.UNSET :
        new CEtlProject(project.getName());
  }

  /**
   * Retrieves a project by its name.
   *
   * @param projectName The name of the project.
   * @return The `BasicProject` with the given name, or `null` if not found.
   */
  public static BasicProject getProjectByName(String projectName) {
    return getProjects().getFirst(p -> Objects.equals(p.getName(), projectName));
  }

  /**
   * Retrieves a project by its key.
   *
   * @param projectKey The key of the project.
   * @return The `BasicProject` with the given key, or `null` if not found.
   */
  public static BasicProject getProjectByKey(String projectKey) {
    return getProjects().getFirst(p -> p.getKey().equals(projectKey));
  }

  /**
   * Retrieves the versions of a project by its key.
   * If the versions are not cached, they are fetched from the Jira client.
   *
   * @param projectKey The key of the project.
   * @param project The ETL project.
   * @return A list of `CEtlVersion` objects for the project.
   */
  public static CList<CEtlVersion> getProjectVersions(String projectKey, CEtlProject project) {
    if (!PROJECT_VERSION.keySet().contains(projectKey)) {
      CList<CEtlVersion> cEtlVersions = CJiraClient.getProjectVersions(projectKey).mapToList(v -> CEtlJiraTranslator.translateVersion(project, v));
      PROJECT_VERSION.put(projectKey, cEtlVersions);
    }
    return PROJECT_VERSION.get(projectKey);
  }

  /**
   * Retrieves all projects from the Jira client.
   * If the projects are not cached, they are fetched and cached.
   *
   * @return A set of `BasicProject` objects.
   */
  private static CSet<BasicProject> getProjects() {
    if (PROJECTS.isEmpty()) {
      PROJECTS.addAll(CJiraClient.getProjects());
    }
    return PROJECTS;
  }
}
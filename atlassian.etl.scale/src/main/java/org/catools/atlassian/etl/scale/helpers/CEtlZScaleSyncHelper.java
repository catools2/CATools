package org.catools.atlassian.etl.scale.helpers;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import lombok.experimental.UtilityClass;
import org.catools.atlassian.etl.jira.translators.CEtlJiraTranslator;
import org.catools.atlassian.etl.scale.translators.CEtlZScaleTestCaseTranslator;
import org.catools.atlassian.jira.client.CJiraClient;
import org.catools.atlassian.scale.CZScaleClient;
import org.catools.atlassian.scale.model.CZScaleTestCase;
import org.catools.common.collections.CList;
import org.catools.common.utils.CStringUtil;
import org.catools.tms.etl.cache.CEtlCacheManager;
import org.catools.tms.etl.dao.CEtlItemDao;
import org.catools.tms.etl.model.CEtlItem;
import org.catools.tms.etl.model.CEtlProject;
import org.catools.tms.etl.model.CEtlVersion;
import org.catools.tms.etl.model.CEtlVersions;

import java.util.Objects;

@UtilityClass
public class CEtlZScaleSyncHelper {

  public static CEtlItem addItem(String testCaseKey) {
    CZScaleTestCase testcase = CZScaleClient.TestCases.getTestCase(testCaseKey);
    CEtlProject etlProject = getProject(testcase);
    CEtlVersions versions = new CEtlVersions(getProjectVersions(testcase.getProjectKey(), etlProject));
    CEtlItemDao.mergeItem(CEtlZScaleTestCaseTranslator.translateTestCase(etlProject, versions, testcase));
    return CEtlCacheManager.readItem(testCaseKey);
  }

  private static CEtlProject getProject(CZScaleTestCase testcase) {
    BasicProject project = getProjectByKey(testcase.getProjectKey());
    return project == null || CStringUtil.isBlank(project.getName()) ?
        CEtlProject.UNSET :
        new CEtlProject(project.getName());
  }

  public static BasicProject getProjectByName(String projectName) {
    return CJiraClient.getProjects().getFirst(p -> Objects.equals(p.getName(), projectName));
  }

  public static BasicProject getProjectByKey(String projectKey) {
    return CJiraClient.getProjects().getFirst(p -> p.getKey().equals(projectKey));
  }

  public static CList<CEtlVersion> getProjectVersions(String projectKey, CEtlProject project) {
    return CJiraClient.getProjectVersions(projectKey).mapToList(v -> CEtlJiraTranslator.translateVersion(project, v));
  }
}

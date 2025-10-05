package org.catools.reportportal.service;

import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.testng.TestMethodType;
import com.epam.reportportal.testng.TestNGService;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.config.CTestManagementConfigs;
import org.catools.common.testng.model.CTestResult;
import org.catools.reportportal.configs.CRPConfigs;
import org.testng.ITestResult;

/**
 * 99% copied from com.epam.reportportal.testng.TestNGService We need to have it separated with
 * large Duplicated code so we could build extensions we need
 */
public class CReportPortalService extends TestNGService {

  public CReportPortalService(@Nonnull ReportPortal reportPortal) {
    super(reportPortal);
  }

  @Override
  protected StartTestItemRQ buildStartStepRq(final @Nonnull ITestResult testResult, final @Nonnull TestMethodType type) {
    StartTestItemRQ rq = super.buildStartStepRq(testResult, type);

    CTestResult result = new CTestResult(testResult);

    if (result.getTestIds() != null)
      rq.setTestCaseId(String.join("", result.getTestIds()));

    rq.setName(getMethodName(testResult));
    if (rq.getAttributes() == null) {
      rq.setAttributes(CRPConfigs.getAttributes());
    } else {
      rq.getAttributes().addAll(CRPConfigs.getAttributes());
    }
    rq.getAttributes().addAll(CRPConfigs.getAttributes());
    rq.setDescription(createStepDescription(testResult));

    return rq;
  }

  /**
   * Extension point to customize test step description
   *
   * @param testResult TestNG's testResult context
   * @return Test/Step Description being sent to ReportPortal
   */
  protected String createStepDescription(ITestResult testResult) {
    StringBuilder stringBuffer = new StringBuilder();
    stringBuffer.append(super.createStepDescription(testResult));

    if (!stringBuffer.isEmpty())
      stringBuffer.append("\n");

    stringBuffer.append(getTestInfoForReport(testResult));
    return stringBuffer.toString();
  }

  public static String getTestInfoForReport(ITestResult testResult) {
    CTestResult result = new CTestResult(testResult);
    StringBuilder stringBuffer = new StringBuilder();

    stringBuffer.append("Package: ").append(result.getPackageName()).append("\n");

    if (result.getTestIds().isNotEmpty()) {
      if (StringUtils.isBlank(CTestManagementConfigs.getUrlToTest())) {
        stringBuffer.append("Tests: ")
            .append(result.getTestIds().join(", "))
            .append("\n");
      } else {
        stringBuffer.append("Tests: ")
            .append(result.getTestIds().mapToSet(CReportPortalService::getJiraUrl).join(", "))
            .append("\n");
      }
    }

    if (result.getDefectIds().isNotEmpty()) {
      if (StringUtils.isBlank(CTestManagementConfigs.getUrlToDefect())) {
        stringBuffer.append("Defects: ")
            .append(result.getDefectIds().join(", "))
            .append("\n");
      } else {
        stringBuffer.append("Defects: ")
            .append(result.getDefectIds().mapToSet(CReportPortalService::getDefectLink).join(", "))
            .append("\n");
      }
    }

    if (result.getOpenDefectIds().isNotEmpty()) {
      if (StringUtils.isBlank(CTestManagementConfigs.getUrlToDefect())) {
        stringBuffer.append("Open Defects: ")
            .append(result.getOpenDefectIds().join(", "))
            .append("\n");
      } else {
        stringBuffer.append("Open Defects: ")
            .append(result.getOpenDefectIds().mapToSet(CReportPortalService::getDefectLink).join(", "))
            .append("\n");
      }
    }

    if (StringUtils.isNoneBlank(result.getAwaiting())) {
      stringBuffer.append("Awaiting: ").append(result.getAwaiting()).append("\n");
    }

    if (StringUtils.isNoneBlank(CTestResult.getVersion())) {
      stringBuffer.append("Version: ").append(CTestResult.getVersion()).append("\n");
    }

    if (result.getSeverityLevel() != null) {
      stringBuffer.append("Severity Level: ").append(result.getSeverityLevel()).append("\n");
    }

    if (result.getRegressionDepth() != null) {
      stringBuffer.append("Regression Depth: ").append(result.getRegressionDepth()).append("\n");
    }

    return stringBuffer.toString();
  }

  private static String getJiraUrl(String i) {
    return String.format("[%s](%s)", i, CTestManagementConfigs.getUrlToTest(i));
  }

  private static String getDefectLink(String i) {
    return String.format("[%s](%s)", i, CTestManagementConfigs.getUrlToDefect(i));
  }

  // Originally this method returned getName() but I had to change it to getQualifiedName().
  // It seems that if you have a method with same name in other point then it was in RP
  // disrespecting the Package and Class Name
  private String getMethodName(ITestResult testResult) {
    String name = "";
    if (CRPConfigs.addPackageNameToMethodDescription()) {
      name += testResult.getTestClass().getRealClass().getPackageName();
    }

    if (CRPConfigs.addClassNameToMethodDescription()) {
      if (StringUtils.isNotBlank(name)) {
        name += "\n";
      }
      name += testResult.getTestClass().getRealClass().getSimpleName();
    }
    return String.format("%s::%s", name, testResult.getMethod().getMethodName());
  }
}

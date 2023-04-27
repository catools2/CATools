package org.catools.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CStringUtil;

public class CTestManagementConfigs {
  public static String getProjectName() {
    return CHocon.get(Configs.TMS_PROJECT_NAME).asString("");
  }

  public static String getVersionName() {
    return CHocon.get(Configs.TMS_VERSION_NAME).asString("");
  }

  public static String getUrlToTest() {
    return CHocon.get(Configs.TMS_URL_FORMAT_TO_TEST).asString("");
  }

  public static String getUrlToTest(String testKey) {
    String string = getUrlToTest();
    return CStringUtil.isBlank(string) ? CStringUtil.EMPTY : CStringUtil.format(string, testKey);
  }

  public static String getUrlToDefect() {
    return CHocon.get(Configs.TMS_URL_FORMAT_TO_DEFECT).asString("");
  }

  public static String getUrlToDefect(String testKey) {
    String string = getUrlToDefect();
    return CStringUtil.isBlank(string) ? CStringUtil.EMPTY : CStringUtil.format(string, testKey);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    TMS_URL_FORMAT_TO_DEFECT("catools.tms.defect_url_format"),
    TMS_URL_FORMAT_TO_TEST("catools.tms.test_url_format"),
    TMS_PROJECT_NAME("catools.tms.project_name"),
    TMS_VERSION_NAME("catools.tms.version_name");

    private final String path;
  }
}

package org.catools.extentreport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CExtentReportConfigs {

  public static String getExtentReportFileName() {
    return CHocon.get(Configs.EXTENT_REPORT_FILE_NAME).asString("ExtentReport");
  }

  public static String getExtentReportName() {
    return CHocon.get(Configs.EXTENT_REPORT_NAME).asString("Extent Report");
  }

  public static boolean isEnable() {
    return CHocon.get(Configs.EXTENT_REPORT_ENABLE).asBoolean(true);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    EXTENT_REPORT_ENABLE("catools.extent_report.enable"),
    EXTENT_REPORT_NAME("catools.extent_report.name"),
    EXTENT_REPORT_FILE_NAME("catools.extent_report.file_name");

    private final String path;
  }
}

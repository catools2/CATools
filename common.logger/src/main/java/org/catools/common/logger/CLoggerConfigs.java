package org.catools.common.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CAnsiUtil;
import org.catools.common.utils.CFileUtil;

import static org.catools.common.configs.CPathConfigs.getOutputChildFolder;

public class CLoggerConfigs {
  public static boolean logColoredOutput() {
    return isOutputSupportAnsi() && CHocon.get(Configs.LOGGER_LOG_COLORED).asBoolean(true);
  }

  public static boolean isOutputSupportAnsi() {
    return CAnsiUtil.isConsoleAvailable() || CAnsiUtil.isSystemOutSupportAnsi() || isXterm();
  }

  public static boolean isXterm() {
    String term = System.getenv("TERM");
    return term != null && term.toLowerCase().startsWith("xterm");
  }

  public static String getLogFolderPath() {
    return CFileUtil.getCanonicalPath(getOutputChildFolder("log"));
  }

  public static void setMaskSensitiveData(boolean flag) {
    System.setProperty(Configs.LOGGER_MASK_SENSITIVE_DATA.name(), Boolean.toString(flag));
  }

  public static boolean maskSensitiveData() {
    return CHocon.get(Configs.LOGGER_MASK_SENSITIVE_DATA).asBoolean(true);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    LOGGER_MASK_SENSITIVE_DATA("catools.logger.mask_sensitive_data"),
    LOGGER_LOG_COLORED("catools.logger.log_colored");

    private final String path;
  }
}

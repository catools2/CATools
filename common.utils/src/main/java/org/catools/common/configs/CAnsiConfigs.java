package org.catools.common.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CAnsiUtil;

/**
 * Utility class for managing ANSI configurations used in the application.
 * Provides methods to check if ANSI output and color printing are enabled.
 */

@UtilityClass
public class CAnsiConfigs {
  /**
   * If the console output should use ANSI.
   * You can control this behaviour by setting CATOOLS_CONSOLE_PRINT_IN_ANSI_ENABLE value to true/false
   *
   * @return true if output should use ANSI, otherwise it returns false
   */
  public static boolean isPrintInAnsiEnable() {
    return CHocon.asBoolean(Configs.CATOOLS_CONSOLE_PRINT_IN_ANSI_ENABLE);
  }

  /**
   * If the console output should print in color.
   * It returns the logical AND result of {@link CAnsiConfigs#isPrintInAnsiEnable()} and {@link CAnsiUtil#isOutputSupportAnsi()}
   *
   * @return true if output should print in color, otherwise it returns false
   */
  public static boolean isPrintInColorAvailable() {
    return CAnsiUtil.isOutputSupportAnsi() && isPrintInAnsiEnable();
  }

  /**
   * Enum representing configuration keys used in the `CAnsiConfigs` class.
   * Each enum constant corresponds to a specific configuration path.
   */
  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    /**
     * Configuration key for enabling ANSI output in the console.
     * Path: `catools.console.print_in_ansi_enable`
     */
    CATOOLS_CONSOLE_PRINT_IN_ANSI_ENABLE("catools.console.print_in_ansi_enable");

    private final String path;
  }
}

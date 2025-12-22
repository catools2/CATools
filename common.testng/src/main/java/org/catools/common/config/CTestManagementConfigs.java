package org.catools.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CStringUtil;

/**
 * Utility class providing configuration access for Test Management System (TMS) settings. This
 * class retrieves configuration values from HOCON configuration files for TMS integration.
 *
 * <p>Configuration properties are accessed through predefined paths:
 *
 * <ul>
 *   <li>catools.tms.project_name - The name of the project in TMS
 *   <li>catools.tms.version_name - The version name for the project
 *   <li>catools.tms.url_format_to_test - URL format template for linking to tests
 *   <li>catools.tms.url_format_to_defect - URL format template for linking to defects
 * </ul>
 *
 * <p>Example configuration file (application.conf):
 *
 * <pre>
 * catools {
 *   tms {
 *     project_name = "MyProject"
 *     version_name = "v1.2.3"
 *     url_format_to_test = "https://tms.example.com/test/{0}"
 *     url_format_to_defect = "https://tms.example.com/defect/{0}"
 *   }
 * }
 * </pre>
 *
 * @author CA Tools Team
 * @since 1.0
 */
@UtilityClass
public class CTestManagementConfigs {
  /**
   * Retrieves the project name from the TMS configuration.
   *
   * <p>This method reads the project name from the configuration property {@code
   * catools.tms.project_name}. The project name is typically used to identify the project within
   * the Test Management System.
   *
   * <p>Example usage:
   *
   * <pre>
   * String projectName = CTestManagementConfigs.getProjectName();
   * // Returns: "MyProject" (if configured as such)
   * </pre>
   *
   * @return the project name as configured, or null if not configured
   */
  public static String getProjectName() {
    return CHocon.asString(Configs.CATOOLS_TMS_PROJECT_NAME);
  }

  /**
   * Retrieves the version name from the TMS configuration.
   *
   * <p>This method reads the version name from the configuration property {@code
   * catools.tms.version_name}. The version name is used to identify the specific version or release
   * of the project being tested.
   *
   * <p>Example usage:
   *
   * <pre>
   * String versionName = CTestManagementConfigs.getVersionName();
   * // Returns: "v1.2.3" (if configured as such)
   * </pre>
   *
   * @return the version name as configured, or null if not configured
   */
  public static String getVersionName() {
    return CHocon.asString(Configs.CATOOLS_TMS_VERSION_NAME);
  }

  /**
   * Retrieves the URL format template for linking to tests in the TMS.
   *
   * <p>This method reads the URL format from the configuration property {@code
   * catools.tms.url_format_to_test}. The returned string is a template that can be used with {@link
   * #getUrlToTest(String)} to generate specific test URLs by substituting placeholders.
   *
   * <p>Example usage:
   *
   * <pre>
   * String urlFormat = CTestManagementConfigs.getUrlToTest();
   * // Returns: "https://tms.example.com/test/{0}" (if configured as such)
   * </pre>
   *
   * @return the URL format template as configured, or null if not configured
   * @see #getUrlToTest(String)
   */
  public static String getUrlToTest() {
    return CHocon.asString(Configs.CATOOLS_TMS_URL_FORMAT_TO_TEST);
  }

  /**
   * Generates a formatted URL for linking to a specific test in the TMS.
   *
   * <p>This method takes the URL format template from {@link #getUrlToTest()} and substitutes the
   * provided test key into the placeholder positions. The formatting is performed using {@link
   * CStringUtil#format(String, Object...)}.
   *
   * <p>If the URL format is not configured or is blank, an empty string is returned.
   *
   * <p>Example usage:
   *
   * <pre>
   * // Assuming URL format is "https://tms.example.com/test/{0}"
   * String testUrl = CTestManagementConfigs.getUrlToTest("TEST-123");
   * // Returns: "https://tms.example.com/test/TEST-123"
   *
   * // If URL format is not configured
   * String emptyUrl = CTestManagementConfigs.getUrlToTest("TEST-456");
   * // Returns: "" (empty string)
   * </pre>
   *
   * @param testKey the test identifier to be substituted in the URL template
   * @return the formatted URL for the specific test, or empty string if URL format is not
   *     configured
   * @see #getUrlToTest()
   */
  public static String getUrlToTest(String testKey) {
    String string = getUrlToTest();
    return StringUtils.isBlank(string) ? StringUtils.EMPTY : CStringUtil.format(string, testKey);
  }

  /**
   * Retrieves the URL format template for linking to defects in the TMS.
   *
   * <p>This method reads the URL format from the configuration property {@code
   * catools.tms.url_format_to_defect}. The returned string is a template that can be used with
   * {@link #getUrlToDefect(String)} to generate specific defect URLs by substituting placeholders.
   *
   * <p>Example usage:
   *
   * <pre>
   * String urlFormat = CTestManagementConfigs.getUrlToDefect();
   * // Returns: "https://tms.example.com/defect/{0}" (if configured as such)
   * </pre>
   *
   * @return the URL format template as configured, or null if not configured
   * @see #getUrlToDefect(String)
   */
  public static String getUrlToDefect() {
    return CHocon.asString(Configs.CATOOLS_TMS_URL_FORMAT_TO_DEFECT);
  }

  /**
   * Generates a formatted URL for linking to a specific defect in the TMS.
   *
   * <p>This method takes the URL format template from {@link #getUrlToDefect()} and substitutes the
   * provided defect key into the placeholder positions. The formatting is performed using {@link
   * CStringUtil#format(String, Object...)}.
   *
   * <p>If the URL format is not configured or is blank, an empty string is returned.
   *
   * <p>Example usage:
   *
   * <pre>
   * // Assuming URL format is "https://tms.example.com/defect/{0}"
   * String defectUrl = CTestManagementConfigs.getUrlToDefect("BUG-789");
   * // Returns: "https://tms.example.com/defect/BUG-789"
   *
   * // If URL format is not configured
   * String emptyUrl = CTestManagementConfigs.getUrlToDefect("BUG-456");
   * // Returns: "" (empty string)
   * </pre>
   *
   * @param testKey the defect identifier to be substituted in the URL template
   * @return the formatted URL for the specific defect, or empty string if URL format is not
   *     configured
   * @see #getUrlToDefect()
   */
  public static String getUrlToDefect(String testKey) {
    String string = getUrlToDefect();
    return StringUtils.isBlank(string) ? StringUtils.EMPTY : CStringUtil.format(string, testKey);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_TMS_URL_FORMAT_TO_DEFECT("catools.tms.url_format_to_defect"),
    CATOOLS_TMS_URL_FORMAT_TO_TEST("catools.tms.url_format_to_test"),
    CATOOLS_TMS_PROJECT_NAME("catools.tms.project_name"),
    CATOOLS_TMS_VERSION_NAME("catools.tms.version_name");

    private final String path;
  }
}

package org.catools.atlassian.etl.scale.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

import java.util.List;

/**
 * Utility class for managing ZScale ETL configurations.
 * Provides methods to retrieve various configuration settings related to ZScale synchronization.
 */
@UtilityClass
public class CEtlZScaleConfigs {

  /**
   * Utility class for managing ZScale synchronization configurations.
   * Provides methods to retrieve folders that need to be synchronized.
   */
  @UtilityClass
  public static class Scale {

    /**
     * Retrieves the list of test case folders to be synchronized.
     * The folders can be configured via the `catools.atlassian.etl.scale.test_case_folders_to_sync` configuration key.
     *
     * @return A list of test case folder names to be synchronized.
     */
    public static List<String> getSyncTestCasesFolders() {
      return CHocon.asStrings(Configs.CATOOLS_ATLASSIAN_ETL_SCALE_TEST_CASE_FOLDERS_TO_SYNC);
    }

    /**
     * Retrieves the list of test run folders to be synchronized.
     * The folders can be configured via the `catools.atlassian.etl.scale.test_run_folders_to_sync` configuration key.
     *
     * @return A list of test run folder names to be synchronized.
     */
    public static List<String> getSyncTestRunsFolders() {
      return CHocon.asStrings(Configs.CATOOLS_ATLASSIAN_ETL_SCALE_TEST_RUN_FOLDERS_TO_SYNC);
    }

    /**
     * Enum representing configuration keys used in the `Scale` class.
     * Each enum constant corresponds to a specific configuration path.
     */
    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      /**
       * Configuration key for the list of test case folders to synchronize.
       * Path: `catools.atlassian.etl.scale.test_case_folders_to_sync`
       */
      CATOOLS_ATLASSIAN_ETL_SCALE_TEST_CASE_FOLDERS_TO_SYNC("catools.atlassian.etl.scale.test_case_folders_to_sync"),
      /**
       * Configuration key for the list of test run folders to synchronize.
       * Path: `catools.atlassian.etl.scale.test_run_folders_to_sync`
       */
      CATOOLS_ATLASSIAN_ETL_SCALE_TEST_RUN_FOLDERS_TO_SYNC("catools.atlassian.etl.scale.test_run_folders_to_sync");

      private final String path;
    }
  }
}

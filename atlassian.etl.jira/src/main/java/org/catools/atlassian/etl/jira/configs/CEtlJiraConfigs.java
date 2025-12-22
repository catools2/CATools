package org.catools.atlassian.etl.jira.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

import java.util.List;

/**
 * Utility class for managing JIRA ETL configurations. Provides methods to retrieve various
 * configuration settings related to JIRA synchronization.
 */
@UtilityClass
public class CEtlJiraConfigs {

  /**
   * Utility class for managing JIRA synchronization configurations. Provides methods to retrieve
   * fields that need to be read during synchronization.
   */
  @UtilityClass
  public static class JiraSync {
    /**
     * Retrieves the list of JIRA fields to be synchronized. The fields can be configured via the
     * `catools.atlassian.etl.jira
     *
     * @return A list of JIRA field names to be synchronized.
     */
    public static List<String> getFieldsToRead() {
      return CHocon.asStrings(Configs.CATOOLS_ATLASSIAN_ETL_JIRA_FIELDS_TO_SYNC);
    }

    /**
     * Enum representing configuration keys used in the `JiraSync` class. Each enum constant
     * corresponds to a specific configuration path.
     */
    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      /**
       * Configuration key for the list of JIRA fields to synchronize. Path:
       * `catools.atlassian.etl.jira.fields_to_sync`
       */
      CATOOLS_ATLASSIAN_ETL_JIRA_FIELDS_TO_SYNC("catools.atlassian.etl.jira.fields_to_sync");
      private final String path;
    }
  }
}

package org.catools.atlassian.jira.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

import java.net.URI;
import java.util.List;

/**
 * Configuration utility class for Jira integration.
 * Provides methods to retrieve Jira-related configuration values from the application's configuration files.
 */
@UtilityClass
public class CJiraConfigs {

  /**
   * Nested utility class for Jira-specific configurations.
   * Contains methods to retrieve various Jira configuration properties.
   */
  @UtilityClass
  public static class Jira {

    /**
     * Retrieves the Jira home URI from the configuration.
     *
     * @return The Jira home URI, or null if not configured.
     */
    public static URI getHomeUri() {
      try {
        String string = CHocon.asString(Configs.CATOOLS_ATLASSIAN_JIRA_HOME);
        if (StringUtils.isBlank(string)) {
          return null;
        }
        return new URI(string);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }

    /**
     * Retrieves the Jira username from the configuration.
     *
     * @return The Jira username.
     */
    public static String getUserName() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_JIRA_USERNAME);
    }

    /**
     * Retrieves the Jira password from the configuration.
     *
     * @return The Jira password.
     */
    public static String getPassword() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_JIRA_PASSWORD);
    }

    /**
     * Retrieves the Jira project key from the configuration.
     *
     * @return The Jira project key.
     */
    public static String getProjectKey() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_JIRA_PROJECT_KEY);
    }

    /**
     * Retrieves the Jira version name from the configuration.
     *
     * @return The Jira version name.
     */
    public static String getVersionName() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_JIRA_VERSION_NAME);
    }

    /**
     * Retrieves the list of date formats used in Jira from the configuration.
     *
     * @return A list of date formats.
     */
    public static List<String> getDateFormats() {
      return CHocon.asStrings(Configs.CATOOLS_ATLASSIAN_JIRA_DATE_FORMAT);
    }

    /**
     * Retrieves the search buffer size for Jira queries from the configuration.
     *
     * @return The search buffer size.
     */
    public static int getSearchBufferSize() {
      return CHocon.asInteger(Configs.CATOOLS_ATLASSIAN_JIRA_SEARCH_BUFFER_SIZE);
    }

    /**
     * Retrieves the delay between API calls to Jira in milliseconds from the configuration.
     *
     * @return The delay in milliseconds.
     */
    public static int getDelayBetweenCallsInMilliseconds() {
      return CHocon.asInteger(Configs.CATOOLS_ATLASSIAN_JIRA_DELAY_BETWEEN_CALLS_IN_MILLI);
    }

    /**
     * Enum representing the configuration keys for Jira.
     * Each enum value corresponds to a specific configuration path.
     */
    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      /**
       * Configuration for Jira home URI.
       * Path: `catools.atlassian.jira.home`
       */
      CATOOLS_ATLASSIAN_JIRA_HOME("catools.atlassian.jira.home"),
      /**
       * Configuration for Jira username.
       * Path: `catools.atlassian.jira.username`
       */
      CATOOLS_ATLASSIAN_JIRA_USERNAME("catools.atlassian.jira.username"),
      /**
       * Configuration for Jira password.
       * Path: `catools.atlassian.jira.password`
       */
      CATOOLS_ATLASSIAN_JIRA_PASSWORD("catools.atlassian.jira.password"),
      /**
       * Configuration for Jira date formats.
       * Path: `catools.atlassian.jira.date_format`
       */
      CATOOLS_ATLASSIAN_JIRA_DATE_FORMAT("catools.atlassian.jira.date_format"),
      /**
       * Configuration for Jira project key.
       * Path: `catools.atlassian.jira.project_key`
       */
      CATOOLS_ATLASSIAN_JIRA_PROJECT_KEY("catools.atlassian.jira.project_key"),
      /**
       * Configuration for Jira version name.
       * Path: `catools.atlassian.jira.version_name`
       */
      CATOOLS_ATLASSIAN_JIRA_VERSION_NAME("catools.atlassian.jira.version_name"),
      /**
       * Configuration for delay between API calls to Jira in milliseconds.
       * Path: `catools.atlassian.jira.delay_between_calls_in_millisecond`
       */
      CATOOLS_ATLASSIAN_JIRA_DELAY_BETWEEN_CALLS_IN_MILLI("catools.atlassian.jira.delay_between_calls_in_millisecond"),
      /**
       * Configuration for search buffer size for Jira queries.
       * Path: `catools.atlassian.jira.search_buffer_size`
       */
      CATOOLS_ATLASSIAN_JIRA_SEARCH_BUFFER_SIZE("catools.atlassian.jira.search_buffer_size");

      private final String path; // The configuration path for the property.
    }
  }
}
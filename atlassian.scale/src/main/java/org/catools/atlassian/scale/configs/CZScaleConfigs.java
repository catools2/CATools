package org.catools.atlassian.scale.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Utility holder for Scale configuration accessors.
 *
 * <p>This class exposes a nested {@code Scale} utility with typed accessors to retrieve
 * Scale-related configuration properties via the {@link CHocon} helper.
 *
 * <p>All methods delegate to {@link CHocon} using well-known configuration paths defined in the
 * {@link Scale.Configs} enum.
 */
@UtilityClass
public class CZScaleConfigs {

  /**
   * Scale-specific configuration accessors.
   *
   * <p>All methods in this utility return values from the application's configuration store. Values
   * are obtained using {@link CHocon} and the paths defined in {@link Configs}.
   */
  @UtilityClass
  public static class Scale {

    /**
     * Returns the configured Scale base URI (home).
     *
     * @return the Scale service base URI as a string, or {@code null} if not configured
     */
    public static String getHomeUri() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_SCALE_HOME);
    }

    /**
     * Returns the configured Scale ATM endpoint URI.
     *
     * @return the Scale ATM endpoint URI as a string, or {@code null} if not configured
     */
    public static String getAtmUri() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_SCALE_ATM);
    }

    /**
     * Returns the configured Scale tests endpoint URI.
     *
     * @return the Scale tests endpoint URI as a string, or {@code null} if not configured
     */
    public static String getTestsUri() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_SCALE_TESTS);
    }

    /**
     * Returns the configured active folder path used by Scale.
     *
     * @return the active folder path as a string, or {@code null} if not configured
     */
    public static String getActiveFolder() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_SCALE_ACTIVE_FOLDER);
    }

    /**
     * Returns the configured username for authenticating with Scale.
     *
     * @return the Scale username as a string, or {@code null} if not configured
     */
    public static String getUserName() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_SCALE_USERNAME);
    }

    /**
     * Returns the configured password for authenticating with Scale.
     *
     * @return the Scale password as a string, or {@code null} if not configured
     */
    public static String getPassword() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_SCALE_PASSWORD);
    }

    /**
     * Returns the list of accepted date formats for Scale data parsing.
     *
     * @return a {@link CList} of date format patterns, never {@code null} (may be empty)
     */
    public static CList<String> getDateFormats() {
      return CList.of(CHocon.asStrings(Configs.CATOOLS_ATLASSIAN_SCALE_DATE_FORMAT));
    }

    /**
     * Returns the configured search buffer size (pagination size) used for Scale queries.
     *
     * @return the search buffer size as an integer
     */
    public static int getSearchBufferSize() {
      return CHocon.asInteger(Configs.CATOOLS_ATLASSIAN_SCALE_SEARCH_BUFFER_SIZE);
    }

    /**
     * Returns the configured delay (in milliseconds) between API calls to Scale.
     *
     * @return the delay between calls in milliseconds as an integer
     */
    public static int getDelayBetweenCallsInMilliseconds() {
      return CHocon.asInteger(Configs.CATOOLS_ATLASSIAN_SCALE_DELAY_BETWEEN_CALLS_IN_MILLI);
    }

    /**
     * Enum of configuration paths used by {@link Scale} accessors.
     *
     * <p>Each constant represents a configuration key path that maps to a property consumed by the
     * Scale integration. The {@link #path} value is returned from the enum's {@link CHoconPath}
     * implementation (provided via Lombok {@code @Getter}).
     */
    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      /**
       * Configuration path for the Scale service base URL. Property key: {@code
       * catools.atlassian.scale.home}
       */
      CATOOLS_ATLASSIAN_SCALE_HOME("catools.atlassian.scale.home"),

      /**
       * Configuration path for the Scale ATM endpoint URL. Property key: {@code
       * catools.atlassian.scale.atm}
       */
      CATOOLS_ATLASSIAN_SCALE_ATM("catools.atlassian.scale.atm"),

      /**
       * Configuration path for the Scale tests endpoint URL. Property key: {@code
       * catools.atlassian.scale.tests}
       */
      CATOOLS_ATLASSIAN_SCALE_TESTS("catools.atlassian.scale.tests"),

      /**
       * Configuration path for the Scale username used for authentication. Property key: {@code
       * catools.atlassian.scale.username}
       */
      CATOOLS_ATLASSIAN_SCALE_USERNAME("catools.atlassian.scale.username"),

      /**
       * Configuration path for the Scale password used for authentication. Property key: {@code
       * catools.atlassian.scale.password}
       */
      CATOOLS_ATLASSIAN_SCALE_PASSWORD("catools.atlassian.scale.password"),

      /**
       * Configuration path for the list of accepted date formats in Scale. Property key: {@code
       * catools.atlassian.scale.date_format}
       */
      CATOOLS_ATLASSIAN_SCALE_DATE_FORMAT("catools.atlassian.scale.date_format"),

      /**
       * Configuration path for the active folder used by Scale. Property key: {@code
       * catools.atlassian.scale.active_folder}
       */
      CATOOLS_ATLASSIAN_SCALE_ACTIVE_FOLDER("catools.atlassian.scale.active_folder"),

      /**
       * Configuration path for the delay between API calls to Scale in milliseconds. Property key:
       * {@code catools.atlassian.scale.delay_between_calls_in_millisecond}
       */
      CATOOLS_ATLASSIAN_SCALE_DELAY_BETWEEN_CALLS_IN_MILLI(
          "catools.atlassian.scale.delay_between_calls_in_millisecond"),

      /**
       * Configuration path for the search buffer size (pagination) for Scale queries. Property key:
       * {@code catools.atlassian.scale.search_buffer_size}
       */
      CATOOLS_ATLASSIAN_SCALE_SEARCH_BUFFER_SIZE("catools.atlassian.scale.search_buffer_size");

      private final String path;
    }
  }
}

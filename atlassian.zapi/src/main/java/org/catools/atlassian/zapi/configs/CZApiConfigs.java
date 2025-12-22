package org.catools.atlassian.zapi.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Utility holder for ZAPI-related configuration accessors.
 *
 * <p>Provides typed access to configuration values used by the ZAPI integration, including
 * endpoints, credentials, date formats and runtime parameters.
 */
@UtilityClass
public class CZApiConfigs {

  /**
   * ZAPI-specific configuration utilities.
   *
   * <p>This nested utility class exposes methods to read various ZAPI configuration values from the
   * application's HOCON configuration via {@link CHocon}.
   */
  @UtilityClass
  public static class ZApi {
    /**
     * Runtime cache for the status mapping loaded from configuration.
     *
     * <p>Populated on first call to {@link #getStatusMap()} to avoid repeated parsing.
     */
    public static final CHashMap<String, String> statusMap = new CHashMap<>();

    /**
     * Returns the base URI for the ZAPI service.
     *
     * @return configured ZAPI base URI as a string
     */
    public static String getZApiUri() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_ZAPI_HOME);
    }

    /**
     * Returns a mapping of status keys to values as defined in configuration.
     *
     * <p>The mapping is parsed lazily and cached in {@link #statusMap}. Expected configuration
     * entries are in the form "KEY:VALUE".
     *
     * @return a mutable map containing status mappings
     */
    public static CHashMap<String, String> getStatusMap() {
      if (!statusMap.isEmpty()) {
        return statusMap;
      }

      for (String[] strings :
          CList.of(CHocon.asStrings(Configs.CATOOLS_ATLASSIAN_ZAPI_STATUS_MAP))
              .mapToSet(s -> s.split(":"))) {
        statusMap.put(strings[0].trim(), strings[1].trim());
      }

      // Ensure the map is populated to prevent runtime lookups from failing later.
      assert statusMap.isNotEmpty();

      return statusMap;
    }

    /**
     * Returns the username for ZAPI authentication.
     *
     * @return configured username
     */
    public static String getUserName() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_ZAPI_USERNAME);
    }

    /**
     * Returns the password for ZAPI authentication.
     *
     * @return configured password
     */
    public static String getPassword() {
      return CHocon.asString(Configs.CATOOLS_ATLASSIAN_ZAPI_PASSWORD);
    }

    /**
     * Returns the list of accepted date formats for ZAPI payloads.
     *
     * @return list of date format patterns
     */
    public static CList<String> getDateFormats() {
      return CList.of(CHocon.asStrings(Configs.CATOOLS_ATLASSIAN_ZAPI_DATE_FORMAT));
    }

    /**
     * Returns the configured page/search buffer size used for paging API requests.
     *
     * @return search buffer size
     */
    public static int getSearchBufferSize() {
      return CHocon.asInteger(Configs.CATOOLS_ATLASSIAN_ZAPI_SEARCH_BUFFER_SIZE);
    }

    /**
     * Returns the configured delay (in milliseconds) to wait between API calls.
     *
     * @return delay between calls in milliseconds
     */
    public static int getDelayBetweenCallsInMilliseconds() {
      return CHocon.asInteger(Configs.CATOOLS_ATLASSIAN_ZAPI_DELAY_BETWEEN_CALLS_IN_MILLISECOND);
    }

    /**
     * Enum of configuration paths used by the ZAPI integration.
     *
     * <p>Each enum constant wraps the HOCON configuration path string. Use the enum when calling
     * {@link CHocon} to avoid hard-coded strings through the codebase.
     */
    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      /** Base URL of the ZAPI service. Path: catools.atlassian.zapi.home */
      CATOOLS_ATLASSIAN_ZAPI_HOME("catools.atlassian.zapi.home"),

      /** Username for ZAPI authentication. Path: catools.atlassian.zapi.username */
      CATOOLS_ATLASSIAN_ZAPI_USERNAME("catools.atlassian.zapi.username"),

      /** Password for ZAPI authentication. Path: catools.atlassian.zapi.password */
      CATOOLS_ATLASSIAN_ZAPI_PASSWORD("catools.atlassian.zapi.password"),

      /**
       * Status mapping entries (KEY:VALUE) used by the integration. Path:
       * catools.atlassian.zapi.status_map
       */
      CATOOLS_ATLASSIAN_ZAPI_STATUS_MAP("catools.atlassian.zapi.status_map"),

      /**
       * Date format patterns accepted/produced by ZAPI. Path: catools.atlassian.zapi.date_format
       */
      CATOOLS_ATLASSIAN_ZAPI_DATE_FORMAT("catools.atlassian.zapi.date_format"),

      /**
       * Millisecond delay between consecutive ZAPI calls. Path:
       * catools.atlassian.zapi.delay_between_calls_in_millisecond
       */
      CATOOLS_ATLASSIAN_ZAPI_DELAY_BETWEEN_CALLS_IN_MILLISECOND(
          "catools.atlassian.zapi.delay_between_calls_in_millisecond"),

      /**
       * Paging/search buffer size for ZAPI requests. Path:
       * catools.atlassian.zapi.search_buffer_size
       */
      CATOOLS_ATLASSIAN_ZAPI_SEARCH_BUFFER_SIZE("catools.atlassian.zapi.search_buffer_size");

      private final String path;
    }
  }
}

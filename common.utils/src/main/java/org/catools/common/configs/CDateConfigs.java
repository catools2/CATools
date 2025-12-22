package org.catools.common.configs;

import java.util.TimeZone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Utility class for managing date-related configurations. Provides methods to retrieve default
 * timezone settings for the application.
 */
@UtilityClass
public class CDateConfigs {

  /**
   * Retrieves the default timezone for date-related operations. The default timezone can be
   * controlled by setting the configuration value `CATOOLS_DATE_TIME_ZONE` to a valid {@link
   * TimeZone} ID.
   *
   * @return The default {@link TimeZone} for the application. If the configuration value is not set
   *     or is invalid, the system's default timezone is returned.
   */
  public static TimeZone getDefaultTimeZone() {
    String val = CHocon.asString(Configs.CATOOLS_DATE_TIME_ZONE);
    return StringUtils.isBlank(val) ? TimeZone.getDefault() : TimeZone.getTimeZone(val);
  }

  /**
   * Enum representing configuration keys used in the `CDateConfigs` class. Each enum constant
   * corresponds to a specific configuration path.
   */
  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    /** Configuration key for the default timezone. Path: `catools.date.time_zone` */
    CATOOLS_DATE_TIME_ZONE("catools.date.time_zone");

    private final String path;
  }
}

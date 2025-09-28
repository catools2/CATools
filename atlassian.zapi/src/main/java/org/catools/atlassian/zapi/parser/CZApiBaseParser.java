package org.catools.atlassian.zapi.parser;

import io.restassured.path.json.JsonPath;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.zapi.configs.CZApiConfigs;
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;
import org.json.JSONObject;

/**
 * Utility class for parsing data in the ZAPI system.
 *
 * <p>This class provides methods to parse boolean and date values from JSON objects
 * or JSONPath expressions. It uses predefined date formats from the ZAPI configuration.</p>
 */
@UtilityClass
public class CZApiBaseParser {

  /**
   * A list of date formats used for parsing date values.
   */
  private static final CList<String> dateFormats = CZApiConfigs.ZApi.getDateFormats();

  /**
   * Parses a boolean value from a JSONPath expression.
   *
   * @param json the JSONPath object containing the data
   * @param createdDate the key to retrieve the boolean value
   * @return {@code false} if the value is blank, otherwise the parsed boolean value
   */
  public static Boolean getBoolean(JsonPath json, String createdDate) {
    String val = json.getString(createdDate);
    if (StringUtils.isBlank(val)) {
      return false;
    }
    return Boolean.valueOf(val);
  }

  /**
   * Parses a date value from a JSONPath expression.
   *
   * @param json the JSONPath object containing the data
   * @param createdDate the key to retrieve the date value
   * @return the parsed {@link CDate} object, or {@code null} if the value is blank
   */
  public static CDate getDate(JsonPath json, String createdDate) {
    String val = json.getString(createdDate);
    if (StringUtils.isBlank(val)) {
      return null;
    }
    return CDate.valueOf(val, dateFormats.toArray(new String[dateFormats.size()]));
  }

  /**
   * Parses a date value from a {@link JSONObject}.
   *
   * @param json the {@link JSONObject} containing the data
   * @param createdDate the key to retrieve the date value
   * @return the parsed {@link CDate} object, or {@code null} if the value is blank
   */
  public static CDate getDate(JSONObject json, String createdDate) {
    String val = json.optString(createdDate);
    if (StringUtils.isBlank(val)) {
      return null;
    }
    return CDate.valueOf(val, dateFormats.toArray(new String[dateFormats.size()]));
  }
}

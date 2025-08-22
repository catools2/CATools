package org.catools.atlassian.zapi.parser;

import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.catools.atlassian.zapi.configs.CZApiConfigs;
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;
import org.json.JSONObject;

public class CZApiBaseParser {
  private static final CList<String> dateFormats = CZApiConfigs.ZApi.getDateFormats();

  protected static Boolean getBoolean(JsonPath json, String createdDate) {
    String val = json.getString(createdDate);
    if (StringUtils.isBlank(val)) {
      return false;
    }
    return Boolean.valueOf(val);
  }

  protected static CDate getDate(JsonPath json, String createdDate) {
    String val = json.getString(createdDate);
    if (StringUtils.isBlank(val)) {
      return null;
    }
    return CDate.valueOf(val, dateFormats.toArray(new String[dateFormats.size()]));
  }

  protected static CDate getDate(JSONObject json, String createdDate) {
    String val = json.optString(createdDate);
    if (StringUtils.isBlank(val)) {
      return null;
    }
    return CDate.valueOf(val, dateFormats.toArray(new String[dateFormats.size()]));
  }
}

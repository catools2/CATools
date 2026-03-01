package org.catools.mcp.context;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.utils.CJsonUtil;

public record CMcpStorageRecord(String clazz, String key, Object value) {

  public static CMcpStorageRecord of(JsonNode schema, String key, Object value) {
    String baseClass =
        schema != null && schema.has("$class") ? schema.get("$class").asText() : null;
    return new CMcpStorageRecord(baseClass, key, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T getPojo() {
    // If not value then return null
    if (value == null) {
      return null;
    }
    // If not class defined then return value as is
    if (StringUtils.isBlank(clazz)) {
      return (T) value;
    }
    try {
      Class<?> entityType = Class.forName(clazz);
      return (T) CJsonUtil.read(value.toString(), entityType);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}

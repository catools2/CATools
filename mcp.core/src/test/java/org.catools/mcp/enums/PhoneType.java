package org.catools.mcp.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum PhoneType {
  OFFICE("0"),
  MOBILE("1"),
  UNKNOWN("");

  private static final Map<String, PhoneType> DB_VALUE_MAP = new HashMap<>();
  private final String dbValue;

  static {
    for (PhoneType phoneType : values()) {
      DB_VALUE_MAP.put(phoneType.dbValue, phoneType);
    }
  }

  PhoneType(String dbValue) {
    this.dbValue = dbValue;
  }

  public static PhoneType fromDbValue(String dbValue) {
    return dbValue == null ? UNKNOWN : DB_VALUE_MAP.getOrDefault(dbValue, UNKNOWN);
  }
}

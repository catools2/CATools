package org.catools.mcp.enums;

import java.util.Arrays;

public enum StatusCode {
  ACTIVE("101", "ACTIVE"),
  AWAIT_CONFIRM("112", "AWAIT_CONFIRM"),
  CONFIRMED("114", "CONFIRMED"),
  DUPLICATED("116", "DUPLICATED"),
  PENDING_AUTHENTICATION("117", "PENDING_AUTH"),
  DEACTIVATED("103", "INACTIVE"),
  DELETED("102", "DELETED"),
  LOCKED("104", "LOCKED"),
  PENDING("110", "PENDING"),
  QUEUED("111", "QUEUED"),
  SUSPENDED("106", "SUSPENDED"),
  TERMINATED("105", "TERMINATED"),
  UNSET("0", "UNSET"),
  INACTIVE("0", "INACTIVE");

  private final String code;
  private final String name;

  StatusCode(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public static StatusCode fromCode(String code) {
    return Arrays.stream(StatusCode.values())
        .filter(record -> code.equalsIgnoreCase(record.getCode()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Unknown Status: " + code));
  }

  public String getCode() {
    return this.code;
  }

  public String getName() {
    return this.name;
  }
}

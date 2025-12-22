package org.catools.web.drivers;

import java.util.Date;
import lombok.Data;

@Data
public class CDriverCookie {
  private String name;
  private String value;
  private String path;
  private String domain;
  private Date expiry;
  private boolean isSecure;
  private boolean isHttpOnly;
  private String sameSite;

  public CDriverCookie(String name, String value, String path, Date expiry) {
    this(name, value, (String) null, path, expiry);
  }

  public CDriverCookie(String name, String value, String domain, String path, Date expiry) {
    this(name, value, domain, path, expiry, false);
  }

  public CDriverCookie(
      String name, String value, String domain, String path, Date expiry, boolean isSecure) {
    this(name, value, domain, path, expiry, isSecure, false);
  }

  public CDriverCookie(
      String name,
      String value,
      String domain,
      String path,
      Date expiry,
      boolean isSecure,
      boolean isHttpOnly) {
    this(name, value, domain, path, expiry, isSecure, isHttpOnly, (String) null);
  }

  public CDriverCookie(
      String name,
      String value,
      String domain,
      String path,
      Date expiry,
      boolean isSecure,
      boolean isHttpOnly,
      String sameSite) {
    this.name = name;
    this.value = value;
    this.path = path != null && !path.isEmpty() ? path : "/";
    this.domain = stripPort(domain);
    this.isSecure = isSecure;
    this.isHttpOnly = isHttpOnly;
    if (expiry != null) {
      this.expiry = new Date(expiry.getTime() / 1000L * 1000L);
    } else {
      this.expiry = null;
    }

    this.sameSite = sameSite;
  }

  public CDriverCookie(String name, String value) {
    this(name, value, "/", (Date) null);
  }

  public CDriverCookie(String name, String value, String path) {
    this(name, value, path, (Date) null);
  }

  private static String stripPort(String domain) {
    return domain == null ? null : domain.split(":")[0];
  }
}

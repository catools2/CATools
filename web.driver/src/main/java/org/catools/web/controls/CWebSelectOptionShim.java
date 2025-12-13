package org.catools.web.controls;

public class CWebSelectOptionShim {
  private final String text;
  private final String value;

  public CWebSelectOptionShim() {
    this.text = "";
    this.value = "";
  }

  public String getText() {
    return text;
  }

  public String getAttribute(String name) {
    if ("value".equalsIgnoreCase(name)) return value;
    return null;
  }
}

package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CByName extends CByXPath {

  public CByName(String name) {
    super(toSelector(name));
  }

  private static String toSelector(String name) {
    Objects.requireNonNull(name, "name must not be null");
    return String.format("*[name='%s']", name.replace("'", "\\'"));
  }
}

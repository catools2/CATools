package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CByTagName extends CByXPath {

  public CByTagName(String tagName) {
    super(toSelector(tagName));
  }

  private static String toSelector(String tagName) {
    Objects.requireNonNull(tagName, "tagName must not be null");
    String trimmed = tagName.trim();
    if (trimmed.isEmpty()) throw new IllegalArgumentException("Tag name must not be blank");
    return trimmed;
  }
}

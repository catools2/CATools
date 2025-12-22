package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CByClassName extends CByXPath {

  public CByClassName(String className) {
    super(toSelector(className));
  }

  private static String toSelector(String className) {
    Objects.requireNonNull(className, "className must not be null");
    String trimmed = className.trim();
    if (trimmed.isEmpty()) throw new IllegalArgumentException("Class name must not be blank");
    if (trimmed.contains(" ")) {
      String q = CWebSelectorHelper.escape(trimmed);
      return "//*[contains(concat(' ', normalize-space(@class), ' '), concat(' ', %s, ' '))]"
          .formatted(q);
    }
    return "//*[contains(@class, '%s')]".formatted(trimmed);
  }
}

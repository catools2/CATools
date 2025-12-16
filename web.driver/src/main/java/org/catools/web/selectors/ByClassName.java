package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ByClassName extends CBy {
  private final String selector;

  public ByClassName(String className) {
    Objects.requireNonNull(className, "className must not be null");
    String trimmed = className.trim();
    if (trimmed.isEmpty()) throw new IllegalArgumentException("Class name must not be blank");
    if (trimmed.contains(" ")) {
      String q = CWebSelectorHelper.escape(trimmed);
      this.selector = "//*[contains(concat(' ', normalize-space(@class), ' '), concat(' ', %s, ' '))]".formatted(q);
    } else {
      this.selector = ".%s".formatted(trimmed);
    }
  }
}


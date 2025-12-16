package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ByTagName extends CBy {
  private final String selector;

  public ByTagName(String tagName) {
    Objects.requireNonNull(tagName, "tagName must not be null");
    String trimmed = tagName.trim();
    if (trimmed.isEmpty()) throw new IllegalArgumentException("Tag name must not be blank");
    this.selector = trimmed;
  }
}


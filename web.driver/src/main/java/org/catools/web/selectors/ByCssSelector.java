package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ByCssSelector extends CBy {
  private final String selector;

  public ByCssSelector(String cssSelector) {
    Objects.requireNonNull(cssSelector, "cssSelector must not be null");
    this.selector = cssSelector;
  }
}


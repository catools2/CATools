package org.catools.web.selectors;

import java.util.Objects;
import lombok.Getter;

@Getter
public class CByCssSelector extends CBy {
  private final String selector;

  public CByCssSelector(String cssSelector) {
    Objects.requireNonNull(cssSelector, "cssSelector must not be null");
    this.selector = cssSelector;
  }
}

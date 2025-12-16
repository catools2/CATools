package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ByXPath extends CBy {
  private final String selector;

  public ByXPath(String xpath) {
    Objects.requireNonNull(xpath, "xpathExpression must not be null");
    this.selector = xpath;
  }
}


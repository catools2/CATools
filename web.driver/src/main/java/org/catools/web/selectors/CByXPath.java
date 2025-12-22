package org.catools.web.selectors;

import java.util.Objects;
import lombok.Getter;

@Getter
public class CByXPath extends CBy {
  private final String selector;

  public CByXPath(String xpath) {
    Objects.requireNonNull(xpath, "xpathExpression must not be null");
    this.selector = xpath;
  }
}

package org.catools.web.selectors;

import java.util.Objects;
import lombok.Getter;

@Getter
public class CByPartialLinkText extends CByXPath {

  public CByPartialLinkText(String partialLinkText) {
    super(toSelector(partialLinkText));
  }

  private static String toSelector(String partialLinkText) {
    Objects.requireNonNull(partialLinkText, "partialLinkText must not be null");
    String q = CWebSelectorHelper.escape(partialLinkText.trim());
    return "//a[contains(normalize-space(string(.)), %s)]".formatted(q);
  }
}

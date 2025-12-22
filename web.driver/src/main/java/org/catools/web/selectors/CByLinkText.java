package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CByLinkText extends CByXPath {

  public CByLinkText(String linkText) {
    super(toSelector(linkText));
  }

  private static String toSelector(String linkText) {
    Objects.requireNonNull(linkText, "linkText must not be null");
    String q = CWebSelectorHelper.escape(linkText.trim());
    return "//a[normalize-space(string(.)) = %s]".formatted(q);
  }
}

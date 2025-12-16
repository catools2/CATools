package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ByPartialLinkText extends CBy {
  private final String selector;

  public ByPartialLinkText(String partialLinkText) {
    Objects.requireNonNull(partialLinkText, "partialLinkText must not be null");
    String q = CWebSelectorHelper.escape(partialLinkText.trim());
    this.selector = "//a[contains(normalize-space(string(.)), %s)]".formatted(q);
  }
}


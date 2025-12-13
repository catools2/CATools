package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ByLinkText extends CBy {
  private final String selector;

  public ByLinkText(String linkText) {
    Objects.requireNonNull(linkText, "linkText must not be null");
    String q = CWebSelectorHelper.escape(linkText.trim());
    this.selector = "//a[normalize-space(string(.)) = %s]".formatted(q);
  }
}


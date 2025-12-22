package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CById extends CByXPath {

  public CById(String id) {
    super(toSelector(id));
  }

  private static String toSelector(String id) {
    Objects.requireNonNull(id, "id must not be null");
    return "//*[@id='%s']".formatted(id);
  }
}

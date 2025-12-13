package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ById extends CBy {
  private final String selector;

  public ById(String id) {
    Objects.requireNonNull(id, "id must not be null");
    this.selector = "#%s".formatted(id);
  }
}


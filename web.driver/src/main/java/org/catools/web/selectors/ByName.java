package org.catools.web.selectors;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ByName extends CBy {
  private final String selector;

  public ByName(String name) {
    Objects.requireNonNull(name, "name must not be null");
    this.selector = String.format("*[name='%s']", name.replace("'", "\\'"));
  }
}


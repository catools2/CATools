package org.catools.common.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD})
public @interface CMCPConfigHint {
  @JsonProperty("prompt") String prompt();
  @JsonProperty("description") String description();
  @JsonProperty("example") String example();
  @JsonProperty("path") String path();
  @JsonProperty("values") String[] values();
}


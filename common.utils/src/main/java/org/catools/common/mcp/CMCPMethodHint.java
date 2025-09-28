package org.catools.common.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD, TYPE})
public @interface CMCPMethodHint {
  @JsonProperty("prompt") String prompt();
  @JsonProperty("description") String description();
  @JsonProperty("example") String example();
}


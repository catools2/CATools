package org.catools.atlassian.zapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a project in the ZAPI system.
 *
 * <p>This class models the structure of a project, including its unique identifier
 * and name. It uses Jackson annotations for JSON serialization and deserialization.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CZApiProject {

  /**
   * Unique identifier for the project.
   *
   * <p>Mapped to the JSON property "value".</p>
   */
  @JsonProperty("value")
  private Long id;

  /**
   * Name of the project.
   *
   * <p>Mapped to the JSON property "label".</p>
   */
  @JsonProperty("label")
  private String name;
}

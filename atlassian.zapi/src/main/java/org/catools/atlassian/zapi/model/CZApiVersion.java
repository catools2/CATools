package org.catools.atlassian.zapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a version in the ZAPI system.
 *
 * <p>This class models the structure of a version, including its unique identifier and name. It
 * uses Jackson annotations for JSON serialization and deserialization.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CZApiVersion {

  /**
   * Unique identifier for the version.
   *
   * <p>Mapped to the JSON property "value".
   */
  @JsonProperty("value")
  private Long id;

  /**
   * Name of the version.
   *
   * <p>Mapped to the JSON property "label".
   */
  @JsonProperty("label")
  private String name;

  /**
   * Constructs a version with the specified ID and name.
   *
   * @param id the unique identifier of the version
   * @param name the name of the version
   */
  public CZApiVersion(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}

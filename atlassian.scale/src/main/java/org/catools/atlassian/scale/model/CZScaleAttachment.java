package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.atlassian.scale.utils.CustomDateDeserializer;
import org.catools.atlassian.scale.utils.CustomDateSerializer;
import org.catools.common.date.CDate;

/**
 * Represents an attachment in the Scale system.
 *
 * <p>This class is used to model the metadata of an attachment, including its file name, size,
 * associated project, and creation details. It supports JSON serialization/deserialization and uses
 * custom serializers/deserializers for date handling.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleAttachment {

  /** The name of the file associated with the attachment. */
  private String fileName;

  /** The size of the file in bytes. */
  private int fileSize;

  /** The display name of the attachment. */
  private String name;

  /** The unique identifier of the attachment. */
  private int id;

  /** The identifier of the project to which the attachment belongs. */
  private int projectId;

  /**
   * The date and time when the attachment was created.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate createdOn;

  /** The key of the user who created the attachment. */
  private String userKey;
}

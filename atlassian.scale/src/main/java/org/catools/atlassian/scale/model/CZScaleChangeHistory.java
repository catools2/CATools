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
 * Represents a change history entry in the Scale system.
 *
 * <p>This class models the details of a change history, including its identifier,
 * source, type, user information, and associated change history items. It supports
 * JSON serialization/deserialization and uses custom serializers/deserializers
 * for date handling.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleChangeHistory {

  /**
   * The unique identifier of the change history entry.
   */
  private Integer id;

  /**
   * The source of the change (e.g., system or user).
   */
  private String source;

  /**
   * The identifier of the source associated with the change.
   */
  private Integer sourceId;

  /**
   * The type of the change (e.g., update, delete).
   */
  private String type;

  /**
   * The key of the user who made the change.
   */
  private String userKey;

  /**
   * The date and time when the change occurred.
   *
   * <p>Uses custom serializers/deserializers for JSON processing.</p>
   */
  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private CDate historyDate;

  /**
   * The collection of items associated with this change history.
   */
  private CZScaleChangeHistoryItems changeHistoryItems;
}
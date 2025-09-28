package org.catools.atlassian.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an individual change history item in the Scale system.
 *
 * <p>This class models the details of a single change within a change history,
 * including the field name that was changed, its new value, and a unique identifier.</p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CZScaleChangeHistoryItem {

  /**
   * The unique identifier of the change history item.
   */
  private Integer id;

  /**
   * The name of the field that was changed.
   */
  private String fieldName;

  /**
   * The new value of the field after the change.
   */
  private String newValue;
}
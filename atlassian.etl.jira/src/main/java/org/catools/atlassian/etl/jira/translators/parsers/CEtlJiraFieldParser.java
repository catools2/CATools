package org.catools.atlassian.etl.jira.translators.parsers;

import com.google.common.collect.ImmutableSet;
import org.catools.common.collections.CHashMap;

/**
 * Parser for Jira fields to convert them into name value pairs. Different implementations can
 * handle different field types.
 */
public interface CEtlJiraFieldParser {
  int rank();

  /**
   * Checks if the given value is considered a default or placeholder value.
   *
   * @param value the value to check.
   * @return true if the value is a default value, false otherwise.
   */
  default boolean isDefaultValue(String value) {
    return ImmutableSet.of("0.0", "-1", "{}", "[]", "None", "N/A", String.valueOf(Long.MAX_VALUE))
        .contains(value);
  }

  /**
   * Determines if this parser is suitable for the given field.
   *
   * @return true if this parser can handle the field, false otherwise.
   */
  boolean isRightParser();

  /**
   * Extracts name-value pairs from the field.
   *
   * @return a map of name-value pairs extracted from the field.
   */
  CHashMap<String, String> getNameValuePairs();
}

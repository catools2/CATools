package org.catools.atlassian.etl.jira.translators.parsers;

import com.atlassian.jira.rest.client.api.domain.IssueField;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.utils.CJsonUtil;
import org.codehaus.jettison.json.JSONArray;

import java.util.LinkedHashMap;

/**
 * Parser for custom field options in Jira issues.
 * It extracts the relevant name-value pairs from the field's JSON array value.
 * Handles cases where the field value contains "customFieldOption".
 */
public class CEtlJiraCustomFieldOptionParser implements CEtlJiraFieldParser {
  private final IssueField field;

  /**
   * Constructor to initialize the parser with the given issue field.
   * @param field the Jira issue field to be parsed.
   */
  public CEtlJiraCustomFieldOptionParser(IssueField field) {
    this.field = field;
  }

  /**
   * Rank of the parser. Lower values indicate higher priority.
   * @return 0, indicating highest priority.
   */
  @Override
  public int rank() {
    return 0;
  }

  /**
   * Determines if this parser is suitable for the given field.
   * It checks if the field value is a JSON array and contains "customFieldOption".
   * @return true if this parser can handle the field, false otherwise.
   */
  @Override
  public boolean isRightParser() {
    return field.getValue() instanceof JSONArray
        && StringUtils.containsIgnoreCase(field.getValue().toString(), "customFieldOption");
  }

  /**
   * Extracts name-value pairs from the field's JSON array value.
   * It removes unnecessary keys like "self" and "id" and handles default values.
   * @return a map of name-value pairs extracted from the field.
   */
  @Override
  public CHashMap<String, String> getNameValuePairs() {
    CHashMap<String, String> output = new CHashMap<>();
    try {
      CList<LinkedHashMap<String, String>> parse = CJsonUtil.read(field.getValue().toString(), CList.class);
      for (LinkedHashMap<String, String> map : parse) {
        map.remove("self");
        map.remove("id");
        if (map.size() == 1) {
          String value = new CList<>(map.values()).get(0);
          if (!isDefaultValue(value)) {
            output.put(field.getName(), value);
          }
        } else if (!map.isEmpty()) {
          output.put(field.getName(), new CList<>(map.values()).get(0));
        } else {
          field.getName();
        }
      }
      return output;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

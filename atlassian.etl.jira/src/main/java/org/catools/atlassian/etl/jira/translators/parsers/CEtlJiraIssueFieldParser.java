package org.catools.atlassian.etl.jira.translators.parsers;

import com.atlassian.jira.rest.client.api.domain.IssueField;
import org.catools.common.collections.CHashMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 * Parser for standard Jira issue fields. It extracts the name-value pair from the field if the
 * value is neither a JSON object nor a JSON array.
 */
public class CEtlJiraIssueFieldParser implements CEtlJiraFieldParser {
  private final IssueField field;

  /**
   * Constructor to initialize the parser with the given issue field.
   *
   * @param field the Jira issue field to be parsed.
   */
  public CEtlJiraIssueFieldParser(IssueField field) {
    this.field = field;
  }

  /**
   * Rank of the parser. Lower values indicate higher priority.
   *
   * @return 10, indicating medium priority.
   */
  @Override
  public int rank() {
    return 10;
  }

  /**
   * Determines if this parser is suitable for the given field. It checks if the field value is
   * neither a JSON object nor a JSON array.
   *
   * @return true if this parser can handle the field, false otherwise.
   */
  @Override
  public boolean isRightParser() {
    return !(field.getValue() instanceof JSONObject || field.getValue() instanceof JSONArray);
  }

  /**
   * Extracts the name-value pair from the field.
   *
   * @return a map containing the field name and its string value.
   */
  @Override
  public CHashMap<String, String> getNameValuePairs() {
    CHashMap<String, String> output = new CHashMap<>();
    try {
      output.put(field.getName(), field.getValue().toString());
      return output;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

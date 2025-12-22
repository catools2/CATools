package org.catools.atlassian.etl.jira.translators.parsers;

import com.atlassian.jira.rest.client.api.domain.IssueField;
import org.catools.common.collections.CHashMap;
import org.codehaus.jettison.json.JSONObject;

/**
 * Parser for Jira fields with JSON object values. It extracts the relevant name-value pair based on
 * a specified attribute from the JSON object.
 */
public class CEtlJiraJsonFieldParser implements CEtlJiraFieldParser {
  private final IssueField field;
  private final String valueAttribute;

  /**
   * Constructor to initialize the parser with the given issue field and value attribute.
   *
   * @param field the Jira issue field to be parsed.
   * @param valueAttribute the attribute in the JSON object whose value will be extracted.
   */
  public CEtlJiraJsonFieldParser(IssueField field, String valueAttribute) {
    this.field = field;
    this.valueAttribute = valueAttribute;
  }

  /**
   * Rank of the parser. Lower values indicate higher priority.
   *
   * @return 100, indicating lower priority.
   */
  @Override
  public int rank() {
    return 100;
  }

  /**
   * Determines if this parser is suitable for the given field. It checks if the field value is a
   * JSON object containing the specified attribute.
   *
   * @return true if this parser can handle the field, false otherwise.
   */
  @Override
  public boolean isRightParser() {
    return field.getValue() instanceof JSONObject jsonobject
        && jsonobject.has(valueAttribute)
        && jsonobject.opt(valueAttribute) instanceof String;
  }

  /**
   * Extracts the name-value pair from the JSON object in the field.
   *
   * @return a map containing the field name and the value of the specified attribute from the JSON
   *     object.
   */
  @Override
  public CHashMap<String, String> getNameValuePairs() {
    CHashMap<String, String> output = new CHashMap<>();
    try {
      output.put(field.getName(), ((JSONObject) field.getValue()).getString(valueAttribute));
      return output;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

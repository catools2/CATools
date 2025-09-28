package org.catools.atlassian.etl.jira.translators.parsers;

import com.atlassian.jira.rest.client.api.domain.IssueField;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.utils.CJsonUtil;
import org.codehaus.jettison.json.JSONArray;

import java.util.LinkedHashMap;

/**
 * Parser for custom field options in Jira issues that contain array-based option values.
 * 
 * <p>This parser specializes in extracting name-value pairs from Jira custom fields that store
 * their values as JSON arrays containing "customFieldOption" objects. It handles the parsing
 * of complex custom field structures by removing unnecessary metadata fields and extracting
 * the meaningful option values.</p>
 * 
 * <p>The parser is designed to handle custom fields such as:
 * <ul>
 *   <li>Single-select dropdown fields</li>
 *   <li>Multi-select dropdown fields</li>
 *   <li>Radio button fields</li>
 *   <li>Checkbox fields</li>
 * </ul>
 * </p>
 * 
 * <p>Example JSON structure this parser handles:
 * <pre>{@code
 * [
 *   {
 *     "self": "https://jira.example.com/rest/api/2/customFieldOption/123",
 *     "id": "123",
 *     "value": "High Priority"
 *   },
 *   {
 *     "self": "https://jira.example.com/rest/api/2/customFieldOption/124", 
 *     "id": "124",
 *     "value": "Medium Priority"
 *   }
 * ]
 * }</pre>
 * </p>
 * 
 * <p>Usage example:
 * <pre>{@code
 * IssueField customField = issue.getFieldByName("Priority Level");
 * CEtlJiraCustomFieldOptionParser parser = new CEtlJiraCustomFieldOptionParser(customField);
 * 
 * if (parser.isRightParser()) {
 *     CHashMap<String, String> nameValuePairs = parser.getNameValuePairs();
 *     // Result: {"Priority Level" -> "High Priority"}
 * }
 * }</pre>
 * </p>
 * 
 * @see CEtlJiraFieldParser
 * @see IssueField
 */
public class CEtlJiraCustomFieldOptionParser implements CEtlJiraFieldParser {
  private final IssueField field;

  /**
   * Constructs a new CEtlJiraCustomFieldOptionParser for the specified Jira issue field.
   * 
   * <p>The parser will analyze the field to determine if it contains custom field options
   * and extract meaningful values while filtering out metadata.</p>
   * 
   * <p>Example usage:
   * <pre>{@code
   * IssueField priorityField = issue.getFieldByName("Custom Priority");
   * CEtlJiraCustomFieldOptionParser parser = new CEtlJiraCustomFieldOptionParser(priorityField);
   * }</pre>
   * </p>
   * 
   * @param field the Jira issue field containing custom field options to be parsed.
   *              Must not be null and should contain a JSONArray value for proper parsing.
   */
  public CEtlJiraCustomFieldOptionParser(IssueField field) {
    this.field = field;
  }

  /**
   * Returns the rank (priority) of this parser in the parsing chain.
   * 
   * <p>Lower values indicate higher priority. This parser has the highest priority (rank 0)
   * for custom field option parsing, ensuring it's evaluated before other field parsers
   * when dealing with JSONArray values containing "customFieldOption".</p>
   * 
   * <p>Example ranking in parser chain:
   * <pre>{@code
   * CEtlJiraCustomFieldOptionParser -> rank 0 (highest priority)
   * CEtlJiraJsonFieldParser         -> rank 1
   * CEtlJiraIssueFieldParser        -> rank 2 (lowest priority)
   * }</pre>
   * </p>
   * 
   * @return 0, indicating this parser has the highest priority for custom field options
   */
  @Override
  public int rank() {
    return 0;
  }

  /**
   * Determines if this parser is suitable for handling the current field.
   * 
   * <p>This parser is appropriate when the field value meets two criteria:
   * <ol>
   *   <li>The value is a JSONArray instance</li>
   *   <li>The array content contains "customFieldOption" (case-insensitive)</li>
   * </ol>
   * </p>
   * 
   * <p>Example scenarios where this returns true:
   * <pre>{@code
   * // Single option field
   * [{"self": "...", "id": "123", "value": "High Priority", "customFieldOption": {...}}]
   * 
   * // Multiple options field  
   * [
   *   {"self": "...", "id": "123", "value": "Bug", "customFieldOption": {...}},
   *   {"self": "...", "id": "124", "value": "Feature", "customFieldOption": {...}}
   * ]
   * }</pre>
   * </p>
   * 
   * <p>Example scenarios where this returns false:
   * <pre>{@code
   * // Simple string value
   * "Simple text value"
   * 
   * // JSONArray without customFieldOption
   * [{"name": "John", "email": "john@example.com"}]
   * 
   * // Non-array JSON object
   * {"key": "value", "number": 123}
   * }</pre>
   * </p>
   * 
   * @return true if this parser can handle the field (JSONArray containing "customFieldOption"), 
   *         false otherwise
   */
  @Override
  public boolean isRightParser() {
    return field.getValue() instanceof JSONArray
        && StringUtils.containsIgnoreCase(field.getValue().toString(), "customFieldOption");
  }

  /**
   * Extracts meaningful name-value pairs from the field's JSON array value.
   * 
   * <p>This method processes the JSON array by:</p>
   * <ol>
   *   <li>Parsing the JSONArray into a list of LinkedHashMap objects</li>
   *   <li>Removing metadata fields ("self" and "id") that are not meaningful for reporting</li>
   *   <li>Extracting the actual option values</li>
   *   <li>Filtering out default/placeholder values using {@link #isDefaultValue(String)}</li>
   *   <li>Mapping the field name to the extracted value(s)</li>
   * </ol>
   * 
   * <p>Processing examples:</p>
   * 
   * <p>Single option field:
   * <pre>{@code
   * Input JSON:
   * [
   *   {
   *     "self": "https://jira.example.com/rest/api/2/customFieldOption/123",
   *     "id": "123", 
   *     "value": "High Priority"
   *   }
   * ]
   * 
   * Result: {"Priority Level" -> "High Priority"}
   * }</pre>
   * </p>
   * 
   * <p>Multiple options field:
   * <pre>{@code
   * Input JSON:
   * [
   *   {
   *     "self": "https://jira.example.com/rest/api/2/customFieldOption/123",
   *     "id": "123",
   *     "value": "Bug"
   *   },
   *   {
   *     "self": "https://jira.example.com/rest/api/2/customFieldOption/124", 
   *     "id": "124",
   *     "value": "Feature Request"
   *   }
   * ]
   * 
   * Result: {"Issue Type" -> "Bug"} (first non-default value is selected)
   * }</pre>
   * </p>
   * 
   * <p>Field with default values (filtered out):
   * <pre>{@code
   * Input JSON:
   * [
   *   {
   *     "self": "https://jira.example.com/rest/api/2/customFieldOption/999",
   *     "id": "999",
   *     "value": "None"  // This is a default value and will be filtered out
   *   }
   * ]
   * 
   * Result: {} (empty map, no meaningful values)
   * }</pre>
   * </p>
   * 
   * @return a CHashMap containing the field name as key and the extracted option value as value.
   *         Returns empty map if no meaningful values are found or if all values are defaults.
   * @throws RuntimeException if JSON parsing fails or if the field value cannot be processed
   * 
   * @see CEtlJiraFieldParser#isDefaultValue(String) for list of filtered default values
   * @see CJsonUtil#read(String, Class) for JSON parsing implementation
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

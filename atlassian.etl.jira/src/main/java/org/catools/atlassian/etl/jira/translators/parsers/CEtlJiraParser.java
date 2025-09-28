package org.catools.atlassian.etl.jira.translators.parsers;

import com.atlassian.jira.rest.client.api.domain.IssueField;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Utility class for parsing Jira issue fields using various field parsers.
 * It maintains a list of parsers and applies the most suitable one to each field.
 * Fields that cannot be parsed are logged and skipped.
 */
@Slf4j
@UtilityClass
public class CEtlJiraParser {
  private static final CList<IssueField> skippedFields = new CList<>();
  private static final CList<Function<IssueField, CEtlJiraFieldParser>> fieldParsers = new CList<>();

  static {
    fieldParsers.add(CEtlJiraCustomFieldOptionParser::new);
    fieldParsers.add(field -> new CEtlJiraJsonFieldParser(field, "name"));
    fieldParsers.add(field -> new CEtlJiraJsonFieldParser(field, "value"));
    fieldParsers.add(CEtlJiraIssueFieldParser::new);
  }

  /**
   * Adds a new field parser to the list of available parsers.
   * @param parserFunction a function that takes an IssueField and returns a CEtlJiraFieldParser
   */
  public static void addFieldParser(Function<IssueField, CEtlJiraFieldParser> parserFunction) {
    fieldParsers.add(parserFunction);
  }

  /**
   * Parses a given Jira issue field using the most suitable parser from the list.
   * If no suitable parser is found, the field is logged and skipped.
   * @param field the Jira issue field to be parsed
   * @return a map of name-value pairs extracted from the field, or an empty map if skipped
   */
  public static CHashMap<String, String> parserJiraField(IssueField field) {
    CList<Function<IssueField, CEtlJiraFieldParser>> list =
        fieldParsers.getAll(p -> p.apply(field).isRightParser());
    // we skip any fields which does not have parser or if it is jira plug in at this point
    // in future we should implement parser for all fields
    if (list.isEmpty()
        || (field.getValue() != null
        && field.getValue().toString().contains("com.atlassian.jira.plugin"))) {
      skippedFields.add(field);
      log.trace(
          "Could not find parser for field {} with value {}.\n record will be skipped",
          field.getName(),
          field.getValue());
      return new CHashMap<>();
    }

    list.sort(Comparator.comparingInt(o -> o.apply(field).rank()));
    Function<IssueField, CEtlJiraFieldParser> bestMatch = list.getFirstOrNull();
    return bestMatch.apply(field).getNameValuePairs();
  }
}

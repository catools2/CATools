package org.catools.atlassian.etl.jira.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

import java.util.List;

public class CEtlJiraConfigs {
  private static final List<String> DEFAULT_FIELDS_TO_READ = List.of("Affected Version",
      "Affected Version/s",
      "Assignee",
      "Automatable",
      "Automation Test",
      "Component",
      "Component Version",
      "CreatedBy",
      "Creator",
      "Environment",
      "Functional Area (PX)",
      "IssueLink",
      "Label",
      "Owner",
      "PMX Scrum Team",
      "PX Subcomponent",
      "Regression Execution Type",
      "Regression Test",
      "Rejections",
      "Reporter",
      "Resolved",
      "Risk Score",
      "Story Points",
      "Team");


  public static class JiraSync {
    public static List<String> getFieldsToRead() {
      return CHocon.get(Configs.JIRA_FIELDS_TO_SYNC).asStrings(DEFAULT_FIELDS_TO_READ);
    }

    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      JIRA_FIELDS_TO_SYNC("catools.atlassian.etl.jira.fields_to_sync");
      private final String path;
    }
  }
}

package org.catools.atlassian.jira.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CStringUtil;

import java.net.URI;
import java.util.List;

public class CJiraConfigs {
  public static class Jira {
    public static URI getHomeUri() {
      try {
        String string = CHocon.get(Configs.JIRA_HOME).asString();
        if (CStringUtil.isBlank(string)) {
          return null;
        }
        return new URI(string);
      } catch (Throwable t) {
        throw new RuntimeException(t);
      }
    }

    public static List<String> getFieldsToRead() {
      return CHocon.get(Configs.JIRA_FIELDS).asStrings();
    }

    public static String getUserName() {
      return CHocon.get(Configs.JIRA_USERNAME).asString();
    }

    public static String getPassword() {
      return CHocon.get(Configs.JIRA_PASSWORD).asString();
    }

    public static String getProjectKey() {
      return CHocon.get(Configs.JIRA_PROJECT_KEY).asString();
    }

    public static String getVersionName() {
      return CHocon.get(Configs.JIRA_VERSION_NAME).asString();
    }

    public static List<String> getDateFormats() {
      return CHocon.get(Configs.JIRA_DATE_FORMAT).asStrings();
    }

    public static int getSearchBufferSize() {
      return CHocon.get(Configs.JIRA_SEARCH_BUFFER_SIZE).asInteger();
    }

    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      JIRA_HOME("catools.atlassian.jira.home"),
      JIRA_FIELDS("catools.atlassian.jira.fields"),
      JIRA_USERNAME("catools.atlassian.jira.username"),
      JIRA_PASSWORD("catools.atlassian.jira.password"),
      JIRA_DATE_FORMAT("catools.atlassian.jira.date_format"),
      JIRA_PROJECT_KEY("catools.atlassian.jira.project_key"),
      JIRA_VERSION_NAME("catools.atlassian.jira.version_name"),
      JIRA_SEARCH_BUFFER_SIZE("catools.atlassian.jira.search_buffer_size");

      private final String path;
    }
  }
}

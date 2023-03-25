package org.catools.atlassian.scale.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CZScaleConfigs {
  public static class Scale {
    public static String getHomeUri() {
      return CHocon.get(Configs.SCALE_HOME).asString();
    }

    public static String getActiveFolder() {
      return CHocon.get(Configs.SCALE_ACTIVE_FOLDER).asString("/");
    }

    public static String getUserName() {
      return CHocon.get(Configs.SCALE_USERNAME).asString();
    }

    public static String getPassword() {
      return CHocon.get(Configs.SCALE_PASSWORD).asString();
    }

    public static CList<String> getDateFormats() {
      return CList.of(
          CHocon.get(Configs.SCALE_DATE_FORMAT).asStrings(CList.of("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
      );
    }

    public static int getSearchBufferSize() {
      return CHocon.get(Configs.SCALE_SEARCH_BUFFER_SIZE).asInteger(400);
    }

    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      SCALE_HOME("catools.atlassian.scale.home"),
      SCALE_USERNAME("catools.atlassian.scale.username"),
      SCALE_PASSWORD("catools.atlassian.scale.password"),
      SCALE_DATE_FORMAT("catools.atlassian.scale.date_format"),
      SCALE_ACTIVE_FOLDER("catools.atlassian.scale.active_folder"),
      SCALE_SEARCH_BUFFER_SIZE("catools.atlassian.scale.search_buffer_size");

      private final String path;
    }
  }
}

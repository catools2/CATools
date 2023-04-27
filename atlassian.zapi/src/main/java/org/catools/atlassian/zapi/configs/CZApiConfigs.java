package org.catools.atlassian.zapi.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

@UtilityClass
public class CZApiConfigs {
  public static class ZApi {
    public static final CHashMap<String, String> statusMap = new CHashMap<>();

    public static String getZApiUri() {
      return CHocon.get(Configs.ZAPI_HOME).asString();
    }

    public static CHashMap<String, String> getStatusMap() {
      if (!statusMap.isEmpty()) {
        return statusMap;
      }
      for (String[] strings :
          CList.of(CHocon.get(Configs.ZAPI_STATUS_MAP).asStrings())
              .mapToSet(s -> s.split(":"))) {
        statusMap.put(strings[0].trim(), strings[1].trim());
      }

      // we need to ensure at the beginning that we map everything to avoid issues after execution
      assert statusMap.isNotEmpty();

      return statusMap;
    }

    public static String getUserName() {
      return CHocon.get(Configs.ZAPI_USERNAME).asString();
    }

    public static String getPassword() {
      return CHocon.get(Configs.ZAPI_PASSWORD).asString();
    }

    public static String getCycleName() {
      return CHocon.get(Configs.ZAPI_CYCLE_NAME).asString();
    }

    public static CList<String> getDateFormats() {
      return CList.of(CHocon.get(Configs.ZAPI_DATE_FORMAT).asStrings());
    }

    public static int getSearchBufferSize() {
      return CHocon.get(Configs.ZAPI_SEARCH_BUFFER_SIZE).asInteger();
    }

    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      ZAPI_HOME("catools.atlassian.zapi.home"),
      ZAPI_USERNAME("catools.atlassian.zapi.username"),
      ZAPI_PASSWORD("catools.atlassian.zapi.password"),
      ZAPI_CYCLE_NAME("catools.atlassian.zapi.cycle_name"),
      ZAPI_STATUS_MAP("catools.atlassian.zapi.status_map"),
      ZAPI_DATE_FORMAT("catools.atlassian.zapi.date_format"),
      ZAPI_SEARCH_BUFFER_SIZE("catools.atlassian.zapi.search_buffer_size");

      private final String path;
    }
  }
}

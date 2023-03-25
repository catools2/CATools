package org.catools.web.drivers.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.openqa.selenium.PageLoadStrategy;

public class CEdgeConfigs {


  public static String getDriverPath() {
    return CHocon.get(CEdgeConfigs.Configs.WEB_EDGE_DRIVER_PATH).asString("");
  }

  public static String getBinaryPath() {
    return CHocon.get(CEdgeConfigs.Configs.WEB_EDGE_BINARY_PATH).asString("");
  }

  public static CList<String> getDefaultArguments() {
    return CList.of(
        CHocon.get(CEdgeConfigs.Configs.WEB_EDGE_DEFAULT_ARGUMENTS).asStrings(CList.of()));
  }

  public static PageLoadStrategy getPageLoadStrategy() {
    return PageLoadStrategy.valueOf(
        CHocon.get(
            CEdgeConfigs.Configs.WEB_EDGE_PAGE_LOAD_STRATEGY).asString(PageLoadStrategy.NORMAL.name()));
  }

  public static boolean isInHeadLessMode() {
    return CHocon.get(CEdgeConfigs.Configs.WEB_EDGE_HEADLESS_ENABLE).asBoolean(false);
  }

  public static CList<String> getHeadLessArguments() {
    return CList.of(
        CHocon.get(CEdgeConfigs.Configs.WEB_EDGE_HEADLESS_ARGUMENTS).asStrings(CList.of()));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_EDGE_DRIVER_PATH("catools.web.edge.driver_path"),
    WEB_EDGE_BINARY_PATH("catools.web.edge.binary_path"),
    WEB_EDGE_DEFAULT_ARGUMENTS("catools.web.edge.default_arguments"),
    WEB_EDGE_PAGE_LOAD_STRATEGY("catools.web.edge.page_load_strategy"),
    WEB_EDGE_HEADLESS_ENABLE("catools.web.edge.headless_enable"),
    WEB_EDGE_HEADLESS_ARGUMENTS("catools.web.edge.headless_arguments");

    private final String path;
  }
}

package org.catools.web.drivers.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.openqa.selenium.PageLoadStrategy;

public class CFireFoxConfigs {

  public static String getDriverPath() {
    return CHocon.get(Configs.WEB_FIREFOX_DRIVER_PATH).asString("");
  }

  public static String getBinaryPath() {
    return CHocon.get(Configs.WEB_FIREFOX_BINARY_PATH).asString("");
  }

  public static CList<String> getDefaultArguments() {
    return CList.of(
        CHocon.get(Configs.WEB_FIREFOX_DEFAULT_ARGUMENTS).asStrings(CList.of()));
  }

  public static PageLoadStrategy getPageLoadStrategy() {
    return PageLoadStrategy.valueOf(
        CHocon.get(
            Configs.WEB_FIREFOX_PAGE_LOAD_STRATEGY).asString(PageLoadStrategy.NORMAL.name()));
  }

  public static boolean isInHeadLessMode() {
    return CHocon.get(Configs.WEB_FIREFOX_HEADLESS_ENABLE).asBoolean(false);
  }

  public static CList<String> getHeadLessArguments() {
    return CList.of(
        CHocon.get(Configs.WEB_FIREFOX_HEADLESS_ARGUMENTS).asStrings(CList.of()));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_FIREFOX_DRIVER_PATH("catools.web.firefox.driver_path"),
    WEB_FIREFOX_BINARY_PATH("catools.web.firefox.binary_path"),
    WEB_FIREFOX_DEFAULT_ARGUMENTS("catools.web.firefox.default_arguments"),
    WEB_FIREFOX_PAGE_LOAD_STRATEGY("catools.web.firefox.page_load_strategy"),
    WEB_FIREFOX_HEADLESS_ENABLE("catools.web.firefox.headless.enable"),
    WEB_FIREFOX_HEADLESS_ARGUMENTS("catools.web.firefox.headless.arguments");

    private final String path;
  }
}

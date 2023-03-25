package org.catools.web.drivers.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.openqa.selenium.PageLoadStrategy;

public class CChromeConfigs {

  public static String getDriverPath() {
    return CHocon.get(Configs.WEB_CHROME_DRIVER_PATH).asString("");
  }

  public static String getBinaryPath() {
    return CHocon.get(Configs.WEB_CHROME_BINARY_PATH).asString("");
  }

  public static CList<String> getDefaultArguments() {
    CList<String> defaultIfNotSet =
        CList.of(
            "--disable-extensions",
            "--disable-gpu",
            "--disable-plugins",
            "--disable-infobars",
            "--log-level=3",
            "--silent",
            "--disable-dev-shm-usage",
            "--force-device-scale-factor=1");
    return CList.of(
        CHocon.get(Configs.WEB_CHROME_DEFAULT_ARGUMENTS).asStrings(defaultIfNotSet));
  }

  public static PageLoadStrategy getPageLoadStrategy() {
    return PageLoadStrategy.valueOf(
        CHocon.get(
            Configs.WEB_CHROME_PAGE_LOAD_STRATEGY).asString(PageLoadStrategy.NORMAL.name()));
  }

  public static CList<String> getPluginsToDisable() {
    return CList.of(CHocon.get(Configs.WEB_CHROME_PLUGINS_TO_DISABLE)
        .asStrings(CList.of("Chrome PDF Viewer", "Chrome Automation Extension")));
  }

  public static CList<String> getPluginsToEnable() {
    return CList.of(CHocon.get(Configs.WEB_CHROME_PLUGINS_TO_ENABLE).asStrings(CList.of()));
  }

  public static boolean isInHeadLessMode() {
    return CHocon.get(Configs.WEB_CHROME_HEADLESS_ENABLE).asBoolean(false);
  }

  public static CList<String> getHeadLessArguments() {
    CList<String> defaultIfNotSet =
        CList.of(
            "--no-online",
            "--disable-gpu",
            "--disable-plugins",
            "--no-sandbox",
            "--disable-setuid-sandbox",
            "--disable-dev-shm-usage");
    return CList.of(
        CHocon.get(Configs.WEB_CHROME_HEADLESS_ARGUMENTS).asStrings(defaultIfNotSet));
  }

  public static String getChromeMobileEmulationDeviceName() {
    return CHocon.get(Configs.WEB_CHROME_MOBILE_EMULATION_DEVICE_NAME).asString("");
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_CHROME_DRIVER_PATH("catools.web.chrome.driver_path"),
    WEB_CHROME_BINARY_PATH("catools.web.chrome.binary_path"),
    WEB_CHROME_DEFAULT_ARGUMENTS("catools.web.chrome.default_arguments"),
    WEB_CHROME_PAGE_LOAD_STRATEGY("catools.web.chrome.page_load_strategy"),
    WEB_CHROME_PLUGINS_TO_DISABLE("catools.web.chrome.plugins_to_disable"),
    WEB_CHROME_PLUGINS_TO_ENABLE("catools.web.chrome.plugins_to_enable"),
    WEB_CHROME_HEADLESS_ENABLE("catools.web.chrome.headless_enable"),
    WEB_CHROME_HEADLESS_ARGUMENTS("catools.web.chrome.headless_arguments"),
    WEB_CHROME_MOBILE_EMULATION_DEVICE_NAME("catools.web.chrome.mobile_emulation_device_name");

    private final String path;
  }
}

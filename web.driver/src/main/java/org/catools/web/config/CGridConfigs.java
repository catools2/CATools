package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

@Slf4j
public class CGridConfigs {

  public static boolean isUseRemoteDriver() {
    return CHocon.get(Configs.WEB_GRID_USE_REMOTE_DRIVER).asBoolean(false);
  }

  public static URL getHubURL() {
    String hubUrl = CHocon.get(Configs.WEB_GRID_HUB_URL).asString(null);
    try {
      if (StringUtils.isNotBlank(hubUrl)) {
        return new URL(hubUrl);
      }

      if (!StringUtils.isBlank(getGridHubIP())) {
        return new URL(String.format("http://%s:%s/wd/hub", getGridHubIP(), getGridHubPort()));
      }
    } catch (MalformedURLException e) {
      log.error("Failed to get hub url", e);
    }
    return null;
  }

  public static int getGridHubPort() {
    return CHocon.get(Configs.WEB_GRID_HUB_PORT).asInteger(4444);
  }

  public static String getGridHubIP() {
    return CHocon.get(Configs.WEB_GRID_HUB_IP).asString("localhost");
  }

  public static boolean isUseHubFolderModeIsOn() {
    return CHocon.get(Configs.WEB_GRID_USE_HUB_FOLDERS).asBoolean(true);
  }

  public static boolean isUseLocalFileDetectorInOn() {
    return CHocon.get(Configs.WEB_GRID_USE_LOCAL_FILE_DETECTOR).asBoolean(true);
  }

  public static Level getLogLevel() {
    return Level.parse(CHocon.get(Configs.WEB_GRID_LOG_LEVEL).asString("OFF"));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_GRID_HUB_URL("catools.web.grid.hub.url"),
    WEB_GRID_HUB_IP("catools.web.grid.hub.ip"),
    WEB_GRID_HUB_PORT("catools.web.grid.hub.port"),
    WEB_GRID_LOG_LEVEL("catools.web.grid.log_level"),
    WEB_GRID_USE_REMOTE_DRIVER("catools.web.grid.use_remote_driver"),
    WEB_GRID_USE_HUB_FOLDERS("catools.web.grid.use_hub_folders"),
    WEB_GRID_USE_LOCAL_FILE_DETECTOR("catools.web.grid.use_local_file_detector");

    private final String path;
  }
}

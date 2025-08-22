package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;

@Slf4j
@UtilityClass
public class CGridConfigs {

  public static boolean isUseRemoteDriver() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_REMOTE_DRIVER);
  }

  public static boolean isUseTestContainer() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_TEST_CONTAINER);
  }

  public static String getCdpURL(String sessionId) {
    if (isUseRemoteDriver()) {
      String schema = "https".equals(getGridHubSchema()) ? "wss" : "ws";
      return "%s://%s:%s/session/%s/se/cdp".formatted(schema, getGridHubIP(), getGridHubPort(), sessionId);
    }
    return null;
  }

  public static URL getHubURL() {
    if (isUseRemoteDriver()) {
      try {
        return URI.create("%s://%s:%s/wd/hub".formatted(getGridHubSchema(), getGridHubIP(), getGridHubPort())).toURL();
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  public static String getGridHubSchema() {
    return CHocon.asString(Configs.CATOOLS_WEB_GRID_HUB_SCHEMA);
  }

  public static String getGridHubIP() {
    return CHocon.asString(Configs.CATOOLS_WEB_GRID_HUB_IP);
  }

  public static int getGridHubPort() {
    return CHocon.asInteger(Configs.CATOOLS_WEB_GRID_HUB_PORT);
  }

  public static boolean isUseHubFolderModeIsOn() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_HUB_FOLDERS);
  }

  public static boolean isUseLocalFileDetectorInOn() {
    return CHocon.asBoolean(Configs.CATOOLS_WEB_GRID_USE_LOCAL_FILE_DETECTOR);
  }

  public static Level getLogLevel() {
    return Level.parse(CHocon.asString(Configs.CATOOLS_WEB_GRID_LOG_LEVEL));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_GRID_HUB_IP("catools.web.grid.hub.ip"),
    CATOOLS_WEB_GRID_HUB_PORT("catools.web.grid.hub.port"),
    CATOOLS_WEB_GRID_HUB_SCHEMA("catools.web.grid.hub.schema"),
    CATOOLS_WEB_GRID_LOG_LEVEL("catools.web.grid.log_level"),
    CATOOLS_WEB_GRID_USE_REMOTE_DRIVER("catools.web.grid.use_remote_driver"),
    CATOOLS_WEB_GRID_USE_TEST_CONTAINER("catools.web.grid.use_test_container"),
    CATOOLS_WEB_GRID_USE_HUB_FOLDERS("catools.web.grid.use_hub_folders"),
    CATOOLS_WEB_GRID_USE_LOCAL_FILE_DETECTOR("catools.web.grid.use_local_file_detector");

    private final String path;
  }
}

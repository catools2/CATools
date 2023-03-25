package org.catools.web.drivers.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CWebDriverManagerConfigs {
  public static boolean isEnabled() {
    return CHocon.get(Configs.WEB_DRIVER_MANAGER_ENABLED).asBoolean(true);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_DRIVER_MANAGER_ENABLED("catools.web.web_driver_manager_enabled");

    private final String path;
  }
}

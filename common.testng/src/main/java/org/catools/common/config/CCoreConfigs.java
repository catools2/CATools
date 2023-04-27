package org.catools.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CCoreConfigs {
  public static boolean isCleanupModeOn() {
    return CHocon.get(Configs.CORE_CLEANUP_MODE).asBoolean(false);
  }

  public static boolean isDataSetupModeOn() {
    return CHocon.get(Configs.CORE_DATA_SETUP_ENABLE).asBoolean(false);
  }

  public static boolean isLocalStorageEnable() {
    return CHocon.get(Configs.CORE_LOCAL_STORAGE_ENABLE).asBoolean(true);
  }

  public static boolean isProductionModeOn() {
    return CHocon.get(Configs.CORE_PRODUCTION_MODE).asBoolean(false);
  }

  public static boolean isReleaseModeOn() {
    return CHocon.get(Configs.CORE_RELEASE_MODE).asBoolean(false);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CORE_DATA_SETUP_ENABLE("catools.core.data_setup_enable"),
    CORE_CLEANUP_MODE("catools.core.cleanup_mode_enable"),
    CORE_LOCAL_STORAGE_ENABLE("catools.core.local_storage_enable"),
    CORE_RELEASE_MODE("catools.core.release_mode_enable"),
    CORE_PRODUCTION_MODE("catools.core.production_mode_enable");

    private final String path;
  }
}

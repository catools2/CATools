package org.catools.common.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.hocon.model.CHoconConfig;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.hocon.utils.CHoconUtils;
import org.catools.common.utils.CConsoleUtil;
import org.catools.common.utils.CFileUtil;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A class to work safe with Type Safe Configuration
 */
@Slf4j
public class CHocon {
  private static Config CONFIG;

  /**
   * Load configuration and set them in System.properties
   */
  public static void reload() {
    ConfigFactory.invalidateCaches();
    CONFIG = useLocalConfig() ? ConfigFactory.load("local.conf") : ConfigFactory.load();
    getUserDefinedSettings()
        .forEach(
            entry -> {
              String key = entry.getKey();
              String propName = CHoconUtils.pathToEnvVariableName(key);
              if (System.getProperty(propName) == null) {
                System.setProperty(propName, CONFIG.getValue(key).unwrapped().toString());
              }
            });
  }

  public static Stream<Map.Entry<String, ConfigValue>> getUserDefinedSettings() {
    return CONFIG
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue().origin().resource() != null);
  }

  private static void cleaUp() {
    if (StringUtils.isNotBlank(CPathConfigs.getOutputPath())) {
      CConsoleUtil.println("cleanup %s", CFileUtil.getCanonicalPath(CPathConfigs.getOutputRoot()));
      if (!CPathConfigs.getOutputRoot().delete()) {
        log.warn("Failed to delete {}.", CPathConfigs.getOutputRoot());
      }
    }
  }

  public static Config getConfig() {
    if (CONFIG == null) {
      try {
        reload();
        cleaUp();
      } catch (Throwable t) {
        log.error("CHocon", t);
        throw t;
      }
    }
    return CONFIG;
  }

  public static CHoconConfig get(String path) {
    return new CHoconConfig(getConfig(), path);
  }

  public static <T extends CHoconPath> CHoconConfig get(T config) {
    return new CHoconConfig(getConfig(), config.getPath());
  }

  public static boolean has(String path) {
    return get(path).isDefined();
  }

  public static <T extends CHoconPath> boolean has(T config) {
    return get(config).isDefined();
  }

  private static boolean useLocalConfig() {
    URL resource = CHocon.class.getClassLoader().getResource("local.conf");
    return resource != null && new File(resource.getFile()).exists();
  }
}

package org.catools.common.extensions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CTypeExtensionConfigs {
  public static int getDefaultWaitInSeconds() {
    return CHocon.get(Configs.EXTENSION_DEFAULT_WAIT_IN_SECONDS).asInteger(5);
  }

  public static int getDefaultWaitIntervalInMilliSeconds() {
    return CHocon.get(
        Configs.EXTENSION_DEFAULT_WAIT_INTERVAL_IN_MILLIS).asInteger(10);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    EXTENSION_DEFAULT_WAIT_INTERVAL_IN_MILLIS("catools.extensions.default_wait_interval_in_millis"),
    EXTENSION_DEFAULT_WAIT_IN_SECONDS("catools.extensions.default_wait_in_seconds");

    private final String path;
  }
}

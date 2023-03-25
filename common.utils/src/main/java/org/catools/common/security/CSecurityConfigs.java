package org.catools.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CSecurityConfigs {

  public static boolean maskSensitiveData() {
    return CHocon.get(Configs.LOGGER_MASK_SENSITIVE_DATA).asBoolean(true);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    LOGGER_MASK_SENSITIVE_DATA("catools.logger.mask_sensitive_data");

    private final String path;
  }
}

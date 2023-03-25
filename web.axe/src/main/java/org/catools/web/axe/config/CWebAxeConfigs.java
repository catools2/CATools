package org.catools.web.axe.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CWebAxeConfigs {
  public static boolean isAxeAnalyserEnable() {
    return CHocon.get(Configs.WEB_AXE_ANALYSER_ENABLE).asBoolean(false);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_AXE_ANALYSER_ENABLE("catools.web.axe_analyser_enable");

    private final String path;
  }
}

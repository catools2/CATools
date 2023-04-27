package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CDriverConfigs {

  public static boolean waitCompleteReadyStateBeforeEachAction() {
    return CHocon.get(
        Configs.WEB_DRIVER_WAIT_COMPLETE_READY_STATE_BEFORE_EACH_ACTION).asBoolean(false);
  }

  public static boolean useJS() {
    return CHocon.get(Configs.WEB_DRIVER_USE_JAVASCRIPT).asBoolean(false);
  }

  public static int getTimeout() {
    return CHocon.get(Configs.WEB_BROWSER_TIMEOUT).asInteger(15);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_DRIVER_WAIT_COMPLETE_READY_STATE_BEFORE_EACH_ACTION("catools.web_driver.wait_complete_ready_state_before_each_action"),
    WEB_DRIVER_USE_JAVASCRIPT("catools.web_driver.use_javascript"),
    WEB_BROWSER_TIMEOUT("catools.web_driver.browser_timeout");

    private final String path;
  }
}

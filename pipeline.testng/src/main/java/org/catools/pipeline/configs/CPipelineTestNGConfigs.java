package org.catools.pipeline.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CPipelineTestNGConfigs {
  public static boolean isEnabled() {
    return CHocon.get(Configs.PIPELINE_LISTENER_ENABLED).asBoolean(true);
  }

  public static boolean alwaysCreateNewPipeline() {
    return CHocon.get(Configs.PIPELINE_LISTENER_ALWAYS_CREATE_NEW).asBoolean(false);
  }

  public static boolean createPipelineIfNotExist() {
    return CHocon.get(Configs.PIPELINE_LISTENER_ALWAYS_CREATE_IF_NOT_EXIST).asBoolean(true);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    PIPELINE_LISTENER_ENABLED("catools.pipeline.listener.enabled"),
    PIPELINE_LISTENER_ALWAYS_CREATE_NEW("catools.pipeline.listener.always_create_new_pipeline"),
    PIPELINE_LISTENER_ALWAYS_CREATE_IF_NOT_EXIST("catools.pipeline.listener.create_if_not_exist");

    private final String path;
  }
}

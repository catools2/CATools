package org.catools.athena.pipeline.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CAthenaPipelineConfigs {
  public static boolean isEnabled() {
    return CHocon.asBoolean(Configs.CATOOLS_ATHENA_PIPELINE_LISTENER_ENABLED);
  }

  public static boolean alwaysCreateNewPipeline() {
    return CHocon.asBoolean(Configs.CATOOLS_ATHENA_PIPELINE_LISTENER_ALWAYS_CREATE_NEW_PIPELINE);
  }

  public static boolean createPipelineIfNotExist() {
    return CHocon.asBoolean(Configs.CATOOLS_ATHENA_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_ATHENA_PIPELINE_LISTENER_ENABLED("catools.athena.pipeline.listener.enabled"),
    CATOOLS_ATHENA_PIPELINE_LISTENER_ALWAYS_CREATE_NEW_PIPELINE(
        "catools.athena.pipeline.listener.always_create_new_pipeline"),
    CATOOLS_ATHENA_PIPELINE_LISTENER_CREATE_IF_NOT_EXIST(
        "catools.athena.pipeline.listener.create_if_not_exist");

    private final String path;
  }
}

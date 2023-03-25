package org.catools.pipeline.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CSystemUtil;
import org.catools.pipeline.model.CPipelineEnvironment;
import org.catools.pipeline.model.CPipelineMetaData;
import org.catools.pipeline.model.CPipelineProject;
import org.catools.pipeline.model.CPipelineUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CPipelineConfigs {

  public static String getPipelineName() {
    return CHocon.get(Configs.PIPELINE_NAME).asString("Local");
  }

  public static String getPipelineDescription() {
    return CHocon.get(Configs.PIPELINE_DESCRIPTION).asString();
  }

  public static List<CPipelineMetaData> getPipelineMetaData() {
    List<CPipelineMetaData> metaData = new ArrayList<>();
    for (Object object : CHocon.get(Configs.PIPELINE_METADATA).asObjects(new ArrayList<>())) {
      Map<String, String> map = (Map<String, String>) object;
      metaData.add(new CPipelineMetaData(map.get("name"), map.get("value")));
    }
    return metaData;
  }

  public static CPipelineEnvironment getEnvironment() {
    return new CPipelineEnvironment(getEnvironmentCode(), getEnvironmentName());
  }

  public static String getEnvironmentCode() {
    return CHocon.get(Configs.PIPELINE_ENVIRONMENT_CODE).asString();
  }

  public static String getEnvironmentName() {
    return CHocon.get(Configs.PIPELINE_ENVIRONMENT_NAME).asString();
  }

  public static CPipelineProject getProject() {
    return new CPipelineProject(getProjectCode(), getProjectName());
  }

  public static String getProjectCode() {
    return CHocon.get(Configs.PIPELINE_PROJECT_CODE).asString();
  }

  public static String getProjectName() {
    return CHocon.get(Configs.PIPELINE_PROJECT_NAME).asString();
  }

  public static CPipelineUser getExecutor() {
    return new CPipelineUser(getExecutorName());
  }

  public static String getExecutorName() {
    return CHocon.get(Configs.PIPELINE_EXECUTOR_NAME).asString(CSystemUtil.getUserName());
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    PIPELINE_NAME("catools.pipeline.name"),
    PIPELINE_DESCRIPTION("catools.pipeline.description"),
    PIPELINE_METADATA("catools.pipeline.metadata"),
    PIPELINE_ENVIRONMENT_CODE("catools.pipeline.environment.code"),
    PIPELINE_ENVIRONMENT_NAME("catools.pipeline.environment.name"),
    PIPELINE_PROJECT_CODE("catools.pipeline.project.code"),
    PIPELINE_PROJECT_NAME("catools.pipeline.project.name"),
    PIPELINE_EXECUTOR_NAME("catools.pipeline.executor.name");

    private final String path;
  }
}

package org.catools.etl.git.configs;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

@UtilityClass
public class CGitConfigs {
  public static final String GIT_SCHEMA = "git";

  public static List<CGitConfigRepo> getRepositories() {
    return CHocon.asList(Configs.CATOOLS_GIT_REPOS, CGitConfigRepo.class);
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_GIT_REPOS("catools.git.repos");

    private final String path;
  }
}

package org.catools.atlassian.etl.scale.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.catools.common.collections.CList;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

public class CEtlZScaleConfigs {
  public static class Scale {
    public static CList<String> getSyncTestCasesFolders() {
      return CList.of(CHocon.get(Configs.SCALE_SYNC_TEST_CASE_FOLDERS).asStrings(CList.of("")));
    }

    public static CList<String> getSyncTestCycleFolders() {
      return CList.of(CHocon.get(Configs.SCALE_SYNC_TEST_CYCLE_FOLDERS).asStrings(CList.of("/")));
    }

    @Getter
    @AllArgsConstructor
    private enum Configs implements CHoconPath {
      SCALE_SYNC_TEST_CASE_FOLDERS("catools.atlassian.etl.scale.test_case_folders_to_sync"), SCALE_SYNC_TEST_CYCLE_FOLDERS("catools.atlassian.etl.scale.test_cycle_folders_to_sync");

      private final String path;
    }
  }
}

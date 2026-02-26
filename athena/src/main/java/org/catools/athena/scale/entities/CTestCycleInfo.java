package org.catools.athena.scale.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CTestCycleInfo extends CTestExecutionInfo {
  private String testCycleName;
  private String testCycleFolderPath;
  private String testCycleDescription;
}

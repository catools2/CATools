package org.catools.k8s.model;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CKubeContainer {
  private CKubeContainerStateInfo terminatedState = new CKubeContainerStateInfo();
  private CKubeContainerStateInfo waitingState = new CKubeContainerStateInfo();
  private Boolean ready;
  private Date startedAt;
  private String image;
  private String imageId;
  private String name;
  private Integer restartCount;
  private Boolean started;
}

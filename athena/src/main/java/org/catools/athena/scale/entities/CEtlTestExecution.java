package org.catools.athena.scale.entities;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CEtlTestExecution {
  private Long id;
  private String issueKey;
  private String status;
  private String packageName;
  private String className;
  private String methodName;
  private String executedBy;
  private String pipeline;
  private String jobName;
  private String jobNumber;
  private String version;
  private String project;
  private String configs;
  private String appVersion;
  private Integer regressionDepth;
  private Integer severityLevel;
  private String openDefects;
  private String defects;
  private String ignored;
  private String awaiting;
  private String deferred;
  private String domain;
  private String region;
  private String phase;
  private String stage;
  private String datacenter;
  private String browser;
  private Date startTime;
  private Date endTime;
}

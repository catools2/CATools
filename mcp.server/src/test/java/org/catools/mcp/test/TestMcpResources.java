package org.catools.mcp.test;

import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpResource;
import org.catools.mcp.exception.CMcpServerException;

@Slf4j
public class TestMcpResources {

  @CMcpResource(
      groups = "test",
      uri = "test://resource1",
      name = "resource1_name",
      title = "resource1_title",
      description = "resource1_description")
  public String resource1() {
    log.debug("calling resource1");
    return "resource1_content";
  }

  @CMcpResource(
      uri = "test://resource12",
      name = "resource12_name",
      title = "resource12_title",
      description = "resource12_description")
  public void resourceWithoutGroupShouldSkip() {
    throw new CMcpServerException("This resource should be skipped because it has no group");
  }
}

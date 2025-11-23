package org.catools.mcp.test;

import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpResource;

@Slf4j
public class TestMcpResources {

  @CMcpResource(
      uri = "test://resource1",
      name = "resource1_name",
      title = "resource1_title",
      description = "resource1_description")
  public String resource1() {
    log.debug("calling resource1");
    return "resource1_content";
  }
}

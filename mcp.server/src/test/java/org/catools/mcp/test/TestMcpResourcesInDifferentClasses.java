package org.catools.mcp.test;

import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpResource;

@Slf4j
public class TestMcpResourcesInDifferentClasses {

  @CMcpResource(
      uri = "test://resource2",
      name = "resource2_name",
      title = "resource2_title",
      description = "resource2_description")
  public String resource2() {
    log.debug("calling resource2");
    return "resource2_content";
  }
}

package org.catools.mcp.test;

import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpPrompt;

@Slf4j
public class TestMcpPromptsInDifferentClasses {

  @CMcpPrompt(groups = "test")
  public void promptWithVoidReturn() {
    log.debug("calling promptWithVoidReturn");
  }

  @CMcpPrompt(groups = "test")
  public String promptWithReturnNull() {
    log.debug("calling promptWithReturnNull");
    return null;
  }
}

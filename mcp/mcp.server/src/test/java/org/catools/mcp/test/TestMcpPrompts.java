package org.catools.mcp.test;

import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.annotation.CMcpPrompt;
import org.catools.mcp.annotation.CMcpPromptParam;
import org.catools.mcp.exception.CMcpServerException;

@Slf4j
public class TestMcpPrompts {

  @CMcpPrompt(groups = "test", title = "title", description = "description")
  public String promptWithDefaultName() {
    log.debug("calling promptWithDefaultName");
    return "promptWithDefaultName is called";
  }

  @CMcpPrompt(groups = "test", name = "promptWithDefaultTitle", description = "description")
  public String promptWithDefaultTitle() {
    log.debug("calling promptWithDefaultTitle");
    return "promptWithDefaultTitle is called";
  }

  @CMcpPrompt(groups = "test", name = "promptWithDefaultDescription", title = "title")
  public String promptWithDefaultDescription() {
    log.debug("calling promptWithDefaultDescription");
    return "promptWithDefaultDescription is called";
  }

  @CMcpPrompt(groups = "test")
  public String promptWithAllDefault() {
    log.debug("calling promptWithAllDefault");
    return "promptWithAllDefault is called";
  }

  @CMcpPrompt(groups = "test")
  public String promptWithOptionalParam(
      @CMcpPromptParam(name = "param", description = "param") String param) {

    log.debug("calling promptWithOptionalParam with param: {}", param);
    return "promptWithOptionalParam is called with param: " + param;
  }

  @CMcpPrompt(groups = "test")
  public String promptWithRequiredParam(
      @CMcpPromptParam(name = "param", description = "param") String param) {

    log.debug("calling promptWithRequiredParam with param: {}", param);
    return "promptWithRequiredParam is called with param: " + param;
  }

  @CMcpPrompt(groups = "test")
  public String promptWithMultiParams(
      @CMcpPromptParam(name = "param1", description = "param1") String param1,
      @CMcpPromptParam(name = "param2", description = "param2") String param2) {

    log.debug("calling promptWithMultiParams with params: {}, {}", param1, param2);
    return String.format("promptWithMultiParams is called with params: %s, %s", param1, param2);
  }

  @CMcpPrompt(groups = "test")
  public String promptWithMixedParams(
      @CMcpPromptParam(name = "mcpParam", description = "mcpParam") String mcpParam,
      String nonMcpParam) {

    log.debug("calling promptWithMixedParams with params: {}, {}", mcpParam, nonMcpParam);
    return String.format(
        "promptWithMixedParams is called with params: %s, %s", mcpParam, nonMcpParam);
  }

  @CMcpPrompt
  public void promptWithoutGroupShouldSkip() {
    throw new CMcpServerException("This prompt should be skipped because it has no group");
  }
}

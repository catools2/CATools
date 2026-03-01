package org.catools.mcp.test;

import org.catools.mcp.annotation.McpPrompt;
import org.catools.mcp.annotation.McpPromptParam;
import org.catools.mcp.exception.McpServerException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMcpPrompts {

    @McpPrompt(groups = {"test", "test1"}, title = "title", description = "description")
    public String promptWithDefaultName() {
        log.debug("calling promptWithDefaultName");
        return "promptWithDefaultName is called";
    }

    @McpPrompt(groups = {"test", "test1"}, name = "promptWithDefaultTitle", description = "description")
    public String promptWithDefaultTitle() {
        log.debug("calling promptWithDefaultTitle");
        return "promptWithDefaultTitle is called";
    }

    @McpPrompt(groups = {"test", "test1"}, name = "promptWithDefaultDescription", title = "title")
    public String promptWithDefaultDescription() {
        log.debug("calling promptWithDefaultDescription");
        return "promptWithDefaultDescription is called";
    }

    @McpPrompt(groups = {"test", "test2"})
    public String promptWithAllDefault() {
        log.debug("calling promptWithAllDefault");
        return "promptWithAllDefault is called";
    }

    @McpPrompt(groups = {"test", "test3"})
    public String promptWithOptionalParam(
            @McpPromptParam(name = "param", description = "param") String param) {

        log.debug("calling promptWithOptionalParam with param: {}", param);
        return "promptWithOptionalParam is called with param: " + param;
    }

    @McpPrompt(groups = "test")
    public String promptWithRequiredParam(
            @McpPromptParam(name = "param", description = "param") String param) {

        log.debug("calling promptWithRequiredParam with param: {}", param);
        return "promptWithRequiredParam is called with param: " + param;
    }

    @McpPrompt(groups = "test")
    public String promptWithMultiParams(
            @McpPromptParam(name = "param1", description = "param1") String param1,
            @McpPromptParam(name = "param2", description = "param2") String param2) {

        log.debug("calling promptWithMultiParams with params: {}, {}", param1, param2);
        return String.format("promptWithMultiParams is called with params: %s, %s", param1, param2);
    }

    @McpPrompt(groups = "test")
    public String promptWithMixedParams(
            @McpPromptParam(name = "mcpParam", description = "mcpParam") String mcpParam,
            String nonMcpParam) {

        log.debug("calling promptWithMixedParams with params: {}, {}", mcpParam, nonMcpParam);
        return String.format(
                "promptWithMixedParams is called with params: %s, %s", mcpParam, nonMcpParam);
    }

    @McpPrompt
    public void promptWithoutGroupShouldSkip() {
        throw new McpServerException("This prompt should be skipped because it has no group");
    }
}

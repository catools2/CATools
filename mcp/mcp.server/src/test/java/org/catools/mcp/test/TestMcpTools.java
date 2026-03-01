package org.catools.mcp.test;

import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.annotation.McpToolParam;
import org.catools.mcp.exception.McpServerException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMcpTools {

    @McpTool(groups = {"test", "test2"}, title = "title", description = "description")
    public String toolWithDefaultName() {
        log.debug("calling toolWithDefaultName");
        return "toolWithDefaultName is called";
    }

    @McpTool(groups = {"test", "test3"}, name = "toolWithDefaultTitle", description = "description")
    public String toolWithDefaultTitle() {
        log.debug("calling toolWithDefaultTitle");
        return "toolWithDefaultTitle is called";
    }

    @McpTool(groups = "test", name = "toolWithDefaultDescription", title = "title")
    public String toolWithDefaultDescription() {
        log.debug("calling toolWithDefaultDescription");
        return "toolWithDefaultDescription is called";
    }

    @McpTool(groups = "test")
    public String toolWithAllDefault() {
        log.debug("calling toolWithAllDefault");
        return "toolWithAllDefault is called";
    }

    @McpTool(groups = "test")
    public String toolWithOptionalParam(
            @McpToolParam(name = "param", description = "param") String param) {

        log.debug("calling toolWithOptionalParam with param: {}", param);
        return "toolWithOptionalParam is called with optional param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithRequiredParam(
            @McpToolParam(name = "param", description = "param") String param) {

        log.debug("calling toolWithRequiredParam with param: {}", param);
        return "toolWithRequiredParam is called with required param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithMultiParams(
            @McpToolParam(name = "param1", description = "param1") String param1,
            @McpToolParam(name = "param2", description = "param2") String param2) {

        log.debug("calling toolWithMultiParams with params: {}, {}", param1, param2);
        return String.format("toolWithMultiParams is called with params: %s, %s", param1, param2);
    }

    @McpTool(groups = "test")
    public String toolWithMixedParams(
            @McpToolParam(name = "mcpParam", description = "mcpParam") String mcpParam,
            String nonMcpParam) {

        log.debug("calling toolWithMixedParams with params: {}, {}", mcpParam, nonMcpParam);
        return String.format(
                "toolWithMixedParams is called with params: %s, %s", mcpParam, nonMcpParam);
    }

    @McpTool
    public void toolWithoutGroupShouldSkip() {
        throw new McpServerException("This tool should be skipped because it has no group");
    }
}

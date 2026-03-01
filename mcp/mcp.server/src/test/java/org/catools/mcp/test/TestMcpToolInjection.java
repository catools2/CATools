package org.catools.mcp.test;

import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.annotation.McpToolParam;

public class TestMcpToolInjection {

    @McpTool(groups = "test")
    public String injectedUser(@McpToolParam(name = "user") User user) {
        return "injected:" + user.name();
    }

    @McpTool(groups = "test")
    public String fallbackUser(@McpToolParam(name = "user") User user) {
        return "fallback:" + user.name();
    }

    public record User(String name) {
    }
}


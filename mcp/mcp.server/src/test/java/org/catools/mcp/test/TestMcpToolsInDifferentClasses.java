package org.catools.mcp.test;

import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.annotation.McpToolParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMcpToolsInDifferentClasses {

    @McpTool(groups = "test")
    public void toolWithVoidReturn() {
        log.debug("calling toolWithVoidReturn");
    }

    @McpTool(groups = "test")
    public String toolWithReturnNull() {
        log.debug("calling toolWithReturnNull");
        return null;
    }

    @McpTool(groups = "test")
    public String toolWithIntParam(@McpToolParam(name = "param") int param) {
        log.debug("calling toolWithIntParam with param: {}", param);
        return "toolWithIntParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithIntegerParam(@McpToolParam(name = "param") Integer param) {
        log.debug("calling toolWithIntegerParam with param: {}", param);
        return "toolWithIntegerParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithLongParam(@McpToolParam(name = "param") long param) {
        log.debug("calling toolWithLongParam with param: {}", param);
        return "toolWithLongParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithLongClassParam(@McpToolParam(name = "param") Long param) {
        log.debug("calling toolWithLongClassParam with param: {}", param);
        return "toolWithLongClassParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithFloatParam(@McpToolParam(name = "param") float param) {
        log.debug("calling toolWithFloatParam with param: {}", param);
        return "toolWithFloatParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithFloatClassParam(@McpToolParam(name = "param") Float param) {
        log.debug("calling toolWithFloatClassParam with param: {}", param);
        return "toolWithFloatClassParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithDoubleParam(@McpToolParam(name = "param") double param) {
        log.debug("calling toolWithDoubleParam with param: {}", param);
        return "toolWithDoubleParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithDoubleClassParam(@McpToolParam(name = "param") Double param) {
        log.debug("calling toolWithDoubleClassParam with param: {}", param);
        return "toolWithDoubleClassParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithNumberParam(@McpToolParam(name = "param") Number param) {
        log.debug("calling toolWithNumberParam with param: {}", param);
        return "toolWithNumberParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithBooleanParam(@McpToolParam(name = "param") boolean param) {
        log.debug("calling toolWithBooleanParam with param: {}", param);
        return "toolWithBooleanParam is called with param: " + param;
    }

    @McpTool(groups = "test")
    public String toolWithBooleanClassParam(@McpToolParam(name = "param") Boolean param) {
        log.debug("calling toolWithBooleanClassParam with param: {}", param);
        return "toolWithBooleanClassParam is called with param: " + param;
    }
}

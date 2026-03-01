package org.catools.mcp.test;

import org.catools.mcp.annotation.McpPrompt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMcpPromptsInDifferentClasses {

    @McpPrompt(groups = {"test", "test2"})
    public void promptWithVoidReturn() {
        log.debug("calling promptWithVoidReturn");
    }

    @McpPrompt(groups = {"test", "test1"})
    public String promptWithReturnNull() {
        log.debug("calling promptWithReturnNull");
        return null;
    }
}

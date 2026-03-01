package org.catools.mcp.test;

import org.catools.mcp.annotation.McpResource;
import org.catools.mcp.exception.McpServerException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMcpResources {

    @McpResource(
            groups = "test",
            uri = "test://resource1",
            name = "resource1_name",
            title = "resource1_title",
            description = "resource1_description")
    public String resource1() {
        log.debug("calling resource1");
        return "resource1_content";
    }

    @McpResource(
            uri = "test://resource12",
            name = "resource12_name",
            title = "resource12_title",
            description = "resource12_description")
    public void resourceWithoutGroupShouldSkip() {
        throw new McpServerException("This resource should be skipped because it has no group");
    }
}

package org.catools.mcp.example;

import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.annotation.McpToolParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Calc {

    @McpTool(
            groups = "calc",
            name = "add_2_integers",
            title = "Add Two Integers",
            description = "Adds two integers and returns the result"
    )
    public int add(
            @McpToolParam(name = "num1", description = "First Number") int num1,
            @McpToolParam(name = "num2", description = "Second Number") int num2) {
        return num1 + num2;
    }
}


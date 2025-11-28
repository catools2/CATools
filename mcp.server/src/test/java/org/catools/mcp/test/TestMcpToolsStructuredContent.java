package org.catools.mcp.test;

import org.catools.mcp.annotation.CMcpJsonSchemaProperty;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.server.CMcpStructuredContent;

public class TestMcpToolsStructuredContent {

  public record TestStructuredContentC(
      @CMcpJsonSchemaProperty(description = "test int", required = true) int testInt,
      @CMcpJsonSchemaProperty(description = "test integer") Integer testInteger,
      @CMcpJsonSchemaProperty(description = "test long", required = true) long testLong,
      @CMcpJsonSchemaProperty(description = "test long class") Long testLongClass,
      @CMcpJsonSchemaProperty(description = "test float", required = true) float testFloat,
      @CMcpJsonSchemaProperty(description = "test float class") Float testFloatClass,
      @CMcpJsonSchemaProperty(description = "test double") double testDouble,
      @CMcpJsonSchemaProperty(description = "test double class") Double testDoubleClass)
      implements CMcpStructuredContent {

    @Override
    public String asTextContent() {
      return String.format(
          "testInt: %d, testInteger: %d, testLong: %d, testLongClass: %d, testFloat: %f, testFloatClass: %f, testDouble: %f, testDoubleClass: %f",
          testInt,
          testInteger,
          testLong,
          testLongClass,
          testFloat,
          testFloatClass,
          testDouble,
          testDoubleClass);
    }
  }

  @CMcpTool(groups = "test")
  public TestStructuredContentC toolWithReturnStructuredContent() {
    return new TestStructuredContentC(1, 2, 3L, 4L, 5.0F, 6.0F, 7.0, 8.0);
  }
}

package org.catools.mcp.enums;

/**
 * This enum represents the mode of MCP (Model Context Protocol) server.
 *
 * <p>It can be either {@link #STDIO}, {@link #SSE}, or {@link #STREAMABLE}.
 */
public enum CServerMode {
  /** The MCP server runs in {@code STDIO} mode. */
  STDIO,

  /** The MCP server runs in http {@code SSE} mode. */
  SSE,

  /** The MCP server runs in {@code STREAMABLE} http mode. */
  STREAMABLE
}

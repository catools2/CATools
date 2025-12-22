package org.catools.mcp.server;

/** This interface is used to define the contract for an MCP server. */
public interface CMcpServer<S extends CMcpServerInfo> {
  /**
   * Returns the sync specification for the MCP server.
   *
   * @param serverInfo the server info
   * @return the sync specification for the MCP server
   */
  io.modelcontextprotocol.server.McpServer.SyncSpecification<?> sync(S serverInfo);
}

package org.catools.mcp.server;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;

/**
 * This class is used to create a new instance of {@link CBaseMcpStreamableServer} based on the
 * specified {@link CMcpStreamableServerInfo} in Streamable HTTP mode.
 */
public class CBaseMcpStreamableServer extends CBaseMcpServer<CMcpStreamableServerInfo> {
  /**
   * Returns the sync specification for the MCP server in Streamable HTTP mode.
   *
   * <p>This method returns the sync specification for the MCP server in Streamable HTTP mode. The
   * sync specification is used to start the MCP server in Streamable HTTP mode.
   *
   * @param info the server info
   * @return the sync specification for the MCP server in Streamable HTTP mode
   */
  @Override
  public McpServer.SyncSpecification<?> sync(CMcpStreamableServerInfo info) {
    HttpServletStreamableServerTransportProvider transportProvider =
        HttpServletStreamableServerTransportProvider.builder()
            .jsonMapper(McpJsonMapper.getDefault())
            .mcpEndpoint(info.mcpEndpoint())
            .disallowDelete(info.disallowDelete())
            .contextExtractor(info.contextExtractor())
            .keepAliveInterval(info.keepAliveInterval())
            .build();
    CEmbeddedJettyServer httpserver = new CEmbeddedJettyServer();
    httpserver.use(transportProvider).bind(info.port()).start();
    return McpServer.sync(transportProvider);
  }
}

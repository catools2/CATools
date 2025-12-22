package org.catools.mcp.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to create a new instance of {@link CBaseMcpSseServer} based on the specified
 * {@link CMcpSseServerInfo} in HTTP SSE mode.
 */
@Slf4j
public class CBaseMcpSseServer extends CBaseMcpServer<CMcpSseServerInfo> {

  /**
   * Returns the sync specification for the MCP server in HTTP SSE mode.
   *
   * <p>This method returns the sync specification for the MCP server in SSE mode. The sync
   * specification is used to start the MCP server in HTTP SSE mode.
   *
   * @param info the server info
   * @return the sync specification for the MCP server in SSE mode
   */
  @Override
  public McpServer.SyncSpecification<?> sync(CMcpSseServerInfo info) {
    log.warn("HTTP SSE mode has been deprecated, recommend to use Stream HTTP server instead.");
    HttpServletSseServerTransportProvider transportProvider =
        HttpServletSseServerTransportProvider.builder()
            .baseUrl(info.baseUrl())
            .sseEndpoint(info.sseEndpoint())
            .messageEndpoint(info.messageEndpoint())
            .build();
    CEmbeddedJettyServer httpserver = new CEmbeddedJettyServer();
    httpserver.use(transportProvider).bind(info.port()).start();
    return McpServer.sync(transportProvider);
  }
}

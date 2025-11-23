package org.catools.mcp.server.configurable;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.configuration.CMcpServerConfiguration;
import org.catools.mcp.configuration.CMcpServerSSE;
import org.catools.mcp.server.CEmbeddedJettyServer;

/**
 * This class is used to create a new instance of {@link CConfigurableMcpSseServer} based on the
 * specified {@link CMcpServerConfiguration} in HTTP SSE mode.
 */
@Slf4j
public class CConfigurableMcpSseServer implements CConfigurableMcpServer {

  @Getter
  private final CMcpServerConfiguration configuration;

  /**
   * Creates a new instance of {@link CConfigurableMcpSseServer} with the specified configuration.
   *
   * @param configuration the configuration for the MCP server
   */
  public CConfigurableMcpSseServer(CMcpServerConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Returns the sync specification for the MCP server in HTTP SSE mode.
   *
   * <p>This method returns the sync specification for the MCP server in HTTP SSE mode. The sync
   * specification is used to start the MCP server in HTTP SSE mode.
   *
   * @return the sync specification for the MCP server in HTTP SSE mode
   */
  @Override
  public McpServer.SyncSpecification<?> sync() {
    log.warn("HTTP SSE mode has been deprecated, recommend to use Stream HTTP server instead.");
    CMcpServerSSE sse = configuration.sse();
    HttpServletSseServerTransportProvider transportProvider =
        HttpServletSseServerTransportProvider.builder()
            .baseUrl(sse.baseUrl())
            .sseEndpoint(sse.endpoint())
            .messageEndpoint(sse.messageEndpoint())
            .build();
    CEmbeddedJettyServer httpserver = new CEmbeddedJettyServer();
    httpserver.use(transportProvider).bind(sse.port()).start();
    return McpServer.sync(transportProvider);
  }
}

package org.catools.mcp.server.configurable;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import java.time.Duration;
import lombok.Getter;
import org.catools.mcp.configuration.CMcpServerConfiguration;
import org.catools.mcp.configuration.CMcpServerStreamable;
import org.catools.mcp.server.CEmbeddedJettyServer;

/**
 * This class is used to create a new instance of {@link CConfigurableMcpStreamableServer} based on
 * the specified {@link CMcpServerConfiguration} in Streamable HTTP mode.
 */
public class CConfigurableMcpStreamableServer implements CConfigurableMcpServer {
  @Getter private final CMcpServerConfiguration configuration;

  /**
   * Creates a new instance of {@link CConfigurableMcpStreamableServer} with the specified
   * configuration.
   *
   * @param configuration the configuration for the MCP server
   */
  public CConfigurableMcpStreamableServer(CMcpServerConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Returns the sync specification for the MCP server in Streamable HTTP mode.
   *
   * <p>This method returns the sync specification for the MCP server in Streamable HTTP mode. The
   * sync specification is used to start the MCP server in Streamable HTTP mode.
   *
   * @return the sync specification for the MCP server in Streamable HTTP mode
   */
  @Override
  public McpServer.SyncSpecification<?> sync() {
    CMcpServerStreamable streamable = configuration.streamable();
    HttpServletStreamableServerTransportProvider transportProvider =
        HttpServletStreamableServerTransportProvider.builder()
            .jsonMapper(McpJsonMapper.getDefault())
            .mcpEndpoint(streamable.mcpEndpoint())
            .disallowDelete(streamable.disallowDelete())
            .keepAliveInterval(Duration.ofMillis(streamable.keepAliveInterval()))
            .build();
    CEmbeddedJettyServer httpserver = new CEmbeddedJettyServer();
    httpserver.use(transportProvider).bind(streamable.port()).start();
    return McpServer.sync(transportProvider);
  }
}

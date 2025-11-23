package org.catools.mcp.server.configurable;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import lombok.Getter;
import org.catools.mcp.configuration.CMcpServerConfiguration;

/**
 * This class is used to create a new instance of {@link CConfigurableMcpStdioServer} based on the
 * specified {@link CMcpServerConfiguration} in STDIO mode.
 */
public class CConfigurableMcpStdioServer implements CConfigurableMcpServer {

  @Getter
  private final CMcpServerConfiguration configuration;

  /**
   * Creates a new instance of {@link CConfigurableMcpStdioServer} with the specified configuration.
   *
   * @param configuration the configuration for the MCP server
   */
  public CConfigurableMcpStdioServer(CMcpServerConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Returns the sync specification for the MCP server in STDIO mode.
   *
   * <p>This method returns the sync specification for the MCP server in STDIO mode. The sync
   * specification is used to start the MCP server in STDIO mode.
   *
   * @return the sync specification for the MCP server in STDIO mode
   */
  @Override
  public McpServer.SyncSpecification<?> sync() {
    return McpServer.sync(new StdioServerTransportProvider(McpJsonMapper.getDefault()));
  }
}

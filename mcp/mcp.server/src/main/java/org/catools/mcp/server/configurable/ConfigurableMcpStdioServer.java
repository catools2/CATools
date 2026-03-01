package org.catools.mcp.server.configurable;

import org.catools.mcp.configuration.McpServerConfiguration;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import lombok.Getter;

/**
 * This class is used to create a new instance of {@link ConfigurableMcpStdioServer} based on the
 * specified {@link McpServerConfiguration} in STDIO mode.
 */
public class ConfigurableMcpStdioServer implements ConfigurableMcpServer {

    @Getter
    private final McpServerConfiguration configuration;

    /**
     * Creates a new instance of {@link ConfigurableMcpStdioServer} with the specified configuration.
     *
     * @param configuration the configuration for the MCP server
     */
    public ConfigurableMcpStdioServer(McpServerConfiguration configuration) {
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

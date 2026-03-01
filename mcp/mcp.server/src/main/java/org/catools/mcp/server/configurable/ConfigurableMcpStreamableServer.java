package org.catools.mcp.server.configurable;

import org.catools.mcp.configuration.McpServerConfiguration;
import org.catools.mcp.configuration.McpServerStreamable;
import org.catools.mcp.server.EmbeddedJettyServer;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import lombok.Getter;

import java.time.Duration;

/**
 * This class is used to create a new instance of {@link ConfigurableMcpStreamableServer} based on
 * the specified {@link McpServerConfiguration} in Streamable HTTP mode.
 */
public class ConfigurableMcpStreamableServer implements ConfigurableMcpServer {
    @Getter
    private final McpServerConfiguration configuration;

    /**
     * Creates a new instance of {@link ConfigurableMcpStreamableServer} with the specified
     * configuration.
     *
     * @param configuration the configuration for the MCP server
     */
    public ConfigurableMcpStreamableServer(McpServerConfiguration configuration) {
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
        McpServerStreamable streamable = configuration.streamable();
        HttpServletStreamableServerTransportProvider transportProvider =
                HttpServletStreamableServerTransportProvider.builder()
                        .jsonMapper(McpJsonMapper.getDefault())
                        .mcpEndpoint(streamable.mcpEndpoint())
                        .disallowDelete(streamable.disallowDelete())
                        .keepAliveInterval(Duration.ofMillis(streamable.keepAliveInterval()))
                        .build();
        EmbeddedJettyServer httpserver = new EmbeddedJettyServer();
        httpserver.use(transportProvider).bind(streamable.port()).start();
        return McpServer.sync(transportProvider);
    }
}

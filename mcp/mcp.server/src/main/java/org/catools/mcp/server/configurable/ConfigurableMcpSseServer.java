package org.catools.mcp.server.configurable;

import org.catools.mcp.configuration.McpServerConfiguration;
import org.catools.mcp.configuration.McpServerSSE;
import org.catools.mcp.server.EmbeddedJettyServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to create a new instance of {@link ConfigurableMcpSseServer} based on the
 * specified {@link McpServerConfiguration} in HTTP SSE mode.
 */
@Slf4j
public class ConfigurableMcpSseServer implements ConfigurableMcpServer {

    @Getter
    private final McpServerConfiguration configuration;

    /**
     * Creates a new instance of {@link ConfigurableMcpSseServer} with the specified configuration.
     *
     * @param configuration the configuration for the MCP server
     */
    public ConfigurableMcpSseServer(McpServerConfiguration configuration) {
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
        McpServerSSE sse = configuration.sse();
        HttpServletSseServerTransportProvider transportProvider =
                HttpServletSseServerTransportProvider.builder()
                        .baseUrl(sse.baseUrl())
                        .sseEndpoint(sse.endpoint())
                        .messageEndpoint(sse.messageEndpoint())
                        .build();
        EmbeddedJettyServer httpserver = new EmbeddedJettyServer();
        httpserver.use(transportProvider).bind(sse.port()).start();
        return McpServer.sync(transportProvider);
    }
}

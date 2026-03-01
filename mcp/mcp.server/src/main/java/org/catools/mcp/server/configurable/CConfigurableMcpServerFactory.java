package org.catools.mcp.server.configurable;

import org.catools.mcp.configuration.McpServerConfiguration;

/**
 * This factory class is used to create instances of {@link ConfigurableMcpServer}
 * implementations based on the specified {@link McpServerConfiguration}.
 */
public final class CConfigurableMcpServerFactory {

    /**
     * Factory class should not be instantiated
     */
    private CConfigurableMcpServerFactory() {
        throw new UnsupportedOperationException("Factory class should not be instantiated");
    }

    /**
     * Creates a new instance of {@link ConfigurableMcpServer} implementations based on the
     * specified {@link McpServerConfiguration}.
     *
     * @param config the configuration to use for the server
     * @return a new instance of {@link ConfigurableMcpServer}
     */
    public static ConfigurableMcpServer getServer(McpServerConfiguration config) {
        return switch (config.mode()) {
            case STDIO -> new ConfigurableMcpStdioServer(config);
            case SSE -> new ConfigurableMcpSseServer(config);
            case STREAMABLE -> new ConfigurableMcpStreamableServer(config);
        };
    }
}

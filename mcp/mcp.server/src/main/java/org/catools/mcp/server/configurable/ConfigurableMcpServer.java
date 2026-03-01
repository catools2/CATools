package org.catools.mcp.server.configurable;

import org.catools.mcp.configuration.McpServerCapabilities;
import org.catools.mcp.configuration.McpServerChangeNotification;
import org.catools.mcp.configuration.McpServerConfiguration;
import org.catools.mcp.configuration.YamlConfigurationLoader;
import org.catools.mcp.server.component.McpServerComponentRegister;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

import java.time.Duration;

/**
 * This interface represents a configurable MCP (Model Context Protocol) server.
 *
 * <p>A configurable MCP server can use the {@link YamlConfigurationLoader} to load its
 * configuration from a YAML file.
 */
public interface ConfigurableMcpServer {
    /**
     * Returns the sync specification for the MCP server.
     *
     * @return the sync specification for the MCP server
     */
    McpServer.SyncSpecification<?> sync();


    /**
     * The configuration for the MCP server.
     */
    McpServerConfiguration getConfiguration();

    /**
     * Starts the MCP server.
     *
     * <p>This method starts the MCP server using the sync specification provided by {@link #sync()}.
     */
    default void startServer() {
        McpServerConfiguration configuration = getConfiguration();
        McpSyncServer server =
                sync()
                        .serverInfo(configuration.name(), configuration.version())
                        .capabilities(serverCapabilities())
                        .instructions(configuration.instructions())
                        .requestTimeout(Duration.ofMillis(configuration.requestTimeout()))
                        .build();
        McpServerComponentRegister.of(server).registerComponents(configuration.groups());
    }

    /**
     * Returns the server capabilities for the MCP server.
     *
     * @return the server capabilities for the MCP server
     */
    private McpSchema.ServerCapabilities serverCapabilities() {
        McpServerConfiguration configuration = getConfiguration();
        McpSchema.ServerCapabilities.Builder capabilities = McpSchema.ServerCapabilities.builder();
        McpServerCapabilities capabilitiesConfig = configuration.capabilities();
        McpServerChangeNotification serverChangeNotification = configuration.changeNotification();
        if (capabilitiesConfig.resource()) {
            capabilities.resources(true, serverChangeNotification.resource());
        }
        if (capabilitiesConfig.prompt()) {
            capabilities.prompts(serverChangeNotification.prompt());
        }
        if (capabilitiesConfig.tool()) {
            capabilities.tools(serverChangeNotification.tool());
        }
        return capabilities.build();
    }
}

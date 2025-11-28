package org.catools.mcp.server.configurable;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import org.catools.mcp.configuration.CMcpServerCapabilities;
import org.catools.mcp.configuration.CMcpServerChangeNotification;
import org.catools.mcp.configuration.CMcpServerConfiguration;
import org.catools.mcp.configuration.CYamlConfigurationLoader;
import org.catools.mcp.server.component.CMcpServerComponentRegister;

import java.time.Duration;

/**
 * This interface represents a configurable MCP (Model Context Protocol) server.
 *
 * <p>A configurable MCP server can use the {@link CYamlConfigurationLoader} to load its
 * configuration from a YAML file.
 */
public interface CConfigurableMcpServer {
  /**
   * Returns the sync specification for the MCP server.
   *
   * @return the sync specification for the MCP server
   */
  McpServer.SyncSpecification<?> sync();


  /**
   * The configuration for the MCP server.
   */
  CMcpServerConfiguration getConfiguration();

  /**
   * Starts the MCP server.
   *
   * <p>This method starts the MCP server using the sync specification provided by {@link #sync()}.
   */
  default void startServer() {
    CMcpServerConfiguration configuration = getConfiguration();
    McpSyncServer server =
        sync()
            .serverInfo(configuration.name(), configuration.version())
            .capabilities(serverCapabilities())
            .instructions(configuration.instructions())
            .requestTimeout(Duration.ofMillis(configuration.requestTimeout()))
            .build();
    CMcpServerComponentRegister.of(server).registerComponents(configuration.groups());
  }

  /**
   * Returns the server capabilities for the MCP server.
   *
   * @return the server capabilities for the MCP server
   */
  private McpSchema.ServerCapabilities serverCapabilities() {
    CMcpServerConfiguration configuration = getConfiguration();
    McpSchema.ServerCapabilities.Builder capabilities = McpSchema.ServerCapabilities.builder();
    CMcpServerCapabilities capabilitiesConfig = configuration.capabilities();
    CMcpServerChangeNotification serverChangeNotification = configuration.changeNotification();
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

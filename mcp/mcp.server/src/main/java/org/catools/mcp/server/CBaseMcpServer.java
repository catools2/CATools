package org.catools.mcp.server;

import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import org.catools.mcp.configuration.CMcpServerCapabilities;
import org.catools.mcp.configuration.CMcpServerChangeNotification;
import org.catools.mcp.server.component.CMcpServerComponentRegister;

/** This abstract class is used to provide a base implementation for an MCP server. */
public abstract class CBaseMcpServer<S extends CMcpServerInfo> implements CMcpServer<S> {

  /**
   * Starts the MCP server with the specified server info.
   *
   * @param serverInfo the server info
   */
  public void start(S serverInfo) {
    McpSyncServer server =
        sync(serverInfo)
            .serverInfo(serverInfo.name(), serverInfo.version())
            .capabilities(serverCapabilities(serverInfo))
            .instructions(serverInfo.instructions())
            .requestTimeout(serverInfo.requestTimeout())
            .build();
    CMcpServerComponentRegister.of(server).registerComponents(serverInfo.groups());
  }

  /**
   * Returns the server capabilities for the MCP server.
   *
   * @param serverInfo the server info
   * @return the server capabilities for the MCP server
   */
  private McpSchema.ServerCapabilities serverCapabilities(S serverInfo) {
    McpSchema.ServerCapabilities.Builder capabilities = McpSchema.ServerCapabilities.builder();
    CMcpServerCapabilities capabilitiesConfig = serverInfo.capabilities();
    CMcpServerChangeNotification serverChangeNotification = serverInfo.changeNotification();
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

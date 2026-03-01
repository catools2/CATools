package org.catools.mcp.server;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;

/**
 * This class is used to create a new instance of {@link BaseMcpStdioServer} based on the specified
 * {@link McpServerInfo} in STDIO mode.
 */
public class BaseMcpStdioServer extends BaseMcpServer<McpServerInfo> {
    /**
     * Returns the sync specification for the MCP server in STDIO mode.
     *
     * <p>This method returns the sync specification for the MCP server in STDIO mode. The sync
     * specification is used to start the MCP server in STDIO mode.
     *
     * @param serverInfo the server info
     * @return the sync specification for the MCP server in STDIO mode
     */
    @Override
    public McpServer.SyncSpecification<?> sync(McpServerInfo serverInfo) {
        return McpServer.sync(new StdioServerTransportProvider(McpJsonMapper.getDefault()));
    }
}

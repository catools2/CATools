package org.catools.mcp.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This record represents the streamable http server configuration for an MCP (Model Context
 * Protocol) server.
 *
 * <p>It contains properties such as the MCP endpoint, disallow delete flag, keep-alive interval,
 * and port.
 */
public record CMcpServerStreamable(
    @JsonProperty("mcp-endpoint") String mcpEndpoint,
    @JsonProperty("disallow-delete") boolean disallowDelete,
    @JsonProperty("keep-alive-interval") long keepAliveInterval,
    @JsonProperty("port") int port) {}

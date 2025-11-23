package org.catools.mcp.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This record represents the Server-Sent Events (SSE) configuration for an MCP (Model Context
 * Protocol) server.
 *
 * <p>It contains properties such as the message endpoint, endpoint, base URL, and port.
 */
public record CMcpServerSSE(
        @JsonProperty("message-endpoint") String messageEndpoint,
        @JsonProperty("endpoint") String endpoint,
        @JsonProperty("base-url") String baseUrl,
        @JsonProperty("port") int port) {
}

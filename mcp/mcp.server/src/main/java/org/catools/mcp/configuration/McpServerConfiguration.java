package org.catools.mcp.configuration;

import org.catools.mcp.enums.ServerMode;
import org.catools.mcp.enums.ServerType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * This record represents the configuration of an MCP (Model Context Protocol) server.
 *
 * <p>It contains various properties such as enabled status, server mode, name, version, type,
 * instructions, request timeout, capabilities, change notification, SSE (Server-Sent Events), and
 * streamable configuration.
 */
public record McpServerConfiguration(
        @JsonProperty("enabled") boolean enabled,
        @JsonProperty("mode") ServerMode mode,
        @JsonProperty("name") String name,
        @JsonProperty("groups") Set<String> groups,
        @JsonProperty("version") String version,
        @JsonProperty("type") ServerType type,
        @JsonProperty("instructions") String instructions,
        @JsonProperty("request-timeout") long requestTimeout,
        @JsonProperty("capabilities") McpServerCapabilities capabilities,
        @JsonProperty("change-notification") McpServerChangeNotification changeNotification,
        @JsonProperty("sse") McpServerSSE sse,
        @JsonProperty("streamable") McpServerStreamable streamable) {
}

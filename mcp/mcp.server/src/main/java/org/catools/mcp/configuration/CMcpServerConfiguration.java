package org.catools.mcp.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import org.catools.mcp.enums.CServerMode;
import org.catools.mcp.enums.CServerType;

/**
 * This record represents the configuration of an MCP (Model Context Protocol) server.
 *
 * <p>It contains various properties such as enabled status, server mode, name, version, type,
 * instructions, request timeout, capabilities, change notification, SSE (Server-Sent Events), and
 * streamable configuration.
 */
public record CMcpServerConfiguration(
    @JsonProperty("enabled") boolean enabled,
    @JsonProperty("mode") CServerMode mode,
    @JsonProperty("name") String name,
    @JsonProperty("groups") Set<String> groups,
    @JsonProperty("version") String version,
    @JsonProperty("type") CServerType type,
    @JsonProperty("instructions") String instructions,
    @JsonProperty("request-timeout") long requestTimeout,
    @JsonProperty("capabilities") CMcpServerCapabilities capabilities,
    @JsonProperty("change-notification") CMcpServerChangeNotification changeNotification,
    @JsonProperty("sse") CMcpServerSSE sse,
    @JsonProperty("streamable") CMcpServerStreamable streamable) {}

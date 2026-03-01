package org.catools.mcp.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This record represents the capabilities of an MCP (Model Context Protocol) server.
 *
 * <p>It contains boolean flags indicating whether the server supports resource, prompt, and tool
 * capabilities.
 */
public record CMcpServerCapabilities(
    @JsonProperty("resource") boolean resource,
    @JsonProperty("prompt") boolean prompt,
    @JsonProperty("tool") boolean tool) {

  /**
   * Creates a new instance of {@code McpServerCapabilities} with default values.
   *
   * <p>By default, all capabilities are set to {@code true}.
   */
  public CMcpServerCapabilities() {
    this(true, true, true);
  }
}

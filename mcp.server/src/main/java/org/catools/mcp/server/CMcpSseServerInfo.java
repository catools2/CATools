package org.catools.mcp.server;

import org.catools.common.utils.CStringUtil;

/**
 * This class is used to define the information of the MCP server with HTTP SSE support.
 *
 * @see CMcpServerInfo
 */
public class CMcpSseServerInfo extends CMcpServerInfo {

  /**
   * The base URL of the MCP server.
   */
  private final String baseUrl;

  /**
   * The endpoint of the MCP server to send messages.
   */
  private final String messageEndpoint;

  /**
   * The endpoint of the MCP server to receive SSE events.
   */
  private final String sseEndpoint;

  /**
   * The port of the MCP HTTP server.
   */
  private final int port;

  /**
   * Constructs a new {@code McpSseServerInfo} instance with the specified builder.
   *
   * @param builder the builder to construct the instance
   */
  private CMcpSseServerInfo(Builder builder) {
    super(builder);
    this.baseUrl = builder.baseUrl;
    this.messageEndpoint = builder.messageEndpoint;
    this.sseEndpoint = builder.sseEndpoint;
    this.port = builder.port;
  }

  /**
   * Returns a new builder instance to construct a {@code McpSseServerInfo} instance.
   *
   * @return a new builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Returns the base URL of the MCP server.
   *
   * @return the base URL of the MCP server
   */
  public String baseUrl() {
    return baseUrl;
  }

  /**
   * Returns the endpoint of the MCP server to send messages.
   *
   * @return the endpoint of the MCP server to send messages
   */
  public String messageEndpoint() {
    return messageEndpoint;
  }

  /**
   * Returns the endpoint of the MCP server to receive SSE events.
   *
   * @return the endpoint of the MCP server to receive SSE events
   */
  public String sseEndpoint() {
    return sseEndpoint;
  }

  /**
   * Returns the port of the MCP HTTP server.
   *
   * @return the port of the MCP HTTP server
   */
  public int port() {
    return port;
  }

  /**
   * The builder class for {@code McpSseServerInfo}.
   */
  public static class Builder extends CMcpServerInfo.Builder<Builder> {

    /**
     * The base URL of the MCP server. Default value is {@code ""}.
     */
    private String baseUrl = CStringUtil.EMPTY;

    /**
     * The endpoint of the MCP server to send messages. Default value is {@code "/mcp/message"}.
     */
    private String messageEndpoint = "/mcp/message";

    /**
     * The endpoint of the MCP server to receive SSE events. Default value is {@code "/sse"}.
     */
    private String sseEndpoint = "/sse";

    /**
     * The port of the MCP HTTP server. Default value is {@code 8080}.
     */
    private int port = 8080;

    /**
     * Returns the self reference of the builder, which is used to chain the method calls.
     *
     * @return the self reference of the builder
     */
    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Builds a new {@code McpSseServerInfo} instance with the specified builder.
     *
     * @return a new {@code McpSseServerInfo} instance
     */
    @Override
    public CMcpSseServerInfo build() {
      return new CMcpSseServerInfo(this);
    }

    /**
     * Sets the base URL of the MCP server.
     *
     * @param baseUrl the base URL of the MCP server
     * @return the self reference of the builder
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return self();
    }

    /**
     * Sets the endpoint of the MCP server to send messages.
     *
     * @param messageEndpoint the endpoint of the MCP server to send messages
     * @return the self reference of the builder
     */
    public Builder messageEndpoint(String messageEndpoint) {
      this.messageEndpoint = messageEndpoint;
      return self();
    }

    /**
     * Sets the endpoint of the MCP server to receive SSE events.
     *
     * @param sseEndpoint the endpoint of the MCP server to receive SSE events
     * @return the self reference of the builder
     */
    public Builder sseEndpoint(String sseEndpoint) {
      this.sseEndpoint = sseEndpoint;
      return self();
    }

    /**
     * Sets the port of the MCP HTTP server.
     *
     * @param port the port of the MCP HTTP server
     * @return the self reference of the builder
     */
    public Builder port(int port) {
      this.port = port;
      return self();
    }
  }
}

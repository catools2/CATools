package org.catools.mcp.exception;

/**
 * Exception thrown when MCP (Model Context Protocol) server encounters configuration-related errors.
 * This typically indicates issues with server setup, initialization parameters, or invalid configuration values.
 *
 * <p>Extends the base {@link McpServerException} to provide specific handling for configuration failures.
 */
public class McpServerConfigurationException extends McpServerException {
    /**
     * Constructs a new configuration exception with the specified detail message.
     *
     * @param message descriptive message explaining the configuration error
     */
    public McpServerConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new configuration exception with the specified detail message and underlying cause.
     *
     * @param message descriptive message explaining the configuration error
     * @param cause   the underlying exception that caused this configuration error
     */
    public McpServerConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

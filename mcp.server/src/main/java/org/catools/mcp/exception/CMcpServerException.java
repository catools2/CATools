package org.catools.mcp.exception;

/**
 * Runtime exception indicating an error condition within the MCP (Model Context Protocol) server.
 * This serves as the base exception type for all MCP server-related errors.
 *
 * <p>Thrown when general server operations encounter unexpected conditions that prevent normal
 * execution flow.
 */
public class CMcpServerException extends RuntimeException {

  /**
   * Constructs a new MCP server exception with the specified detail message.
   *
   * @param message descriptive message explaining the error condition
   */
  public CMcpServerException(String message) {
    super(message);
  }

  /**
   * Constructs a new MCP server exception with the specified detail message and underlying cause.
   *
   * @param message descriptive message explaining the error condition
   * @param cause the underlying exception that caused this error
   */
  public CMcpServerException(String message, Throwable cause) {
    super(message, cause);
  }
}

package org.catools.mcp.exception;

/**
 * Exception thrown when JSON serialization or deserialization operations fail within the MCP
 * server. This indicates issues with JSON data parsing, validation, or transformation during server
 * operations.
 *
 * <p>Extends the base {@link CMcpServerException} to provide specific handling for JSON processing
 * failures.
 */
public class CMcpServerJsonProcessingException extends CMcpServerException {
  /**
   * Constructs a new JSON processing exception with the specified detail message.
   *
   * @param message descriptive message explaining the JSON processing error
   */
  public CMcpServerJsonProcessingException(String message) {
    super(message);
  }

  /**
   * Constructs a new JSON processing exception with the specified detail message and underlying
   * cause.
   *
   * @param message descriptive message explaining the JSON processing error
   * @param cause the underlying exception that caused this JSON processing error
   */
  public CMcpServerJsonProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}

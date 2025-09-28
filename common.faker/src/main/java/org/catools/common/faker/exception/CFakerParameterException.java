package org.catools.common.faker.exception;

/**
 * Exception thrown when invalid parameters are provided to CFaker operations.
 * <p>
 * This runtime exception is used to indicate that the parameters passed to a CFaker
 * method are invalid, missing, or outside acceptable ranges. It extends RuntimeException
 * to provide unchecked exception handling for parameter validation errors.
 * </p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Throwing with a simple description
 * if (minValue > maxValue) {
 *     throw new CFakerParameterException("Minimum value cannot be greater than maximum value");
 * }
 * 
 * // Throwing with formatted message
 * if (count < 0) {
 *     throw new CFakerParameterException(
 *         String.format("Count parameter must be non-negative, but was: %d", count)
 *     );
 * }
 * 
 * // Catching and handling the exception
 * try {
 *     faker.generateRandomString(-5); // Invalid negative length
 * } catch (CFakerParameterException e) {
 *     logger.error("Invalid parameter provided: " + e.getMessage());
 *     // Handle the error appropriately
 * }
 * }</pre>
 *
 * @author CFaker Team
 * @since 1.0
 * @see RuntimeException
 */
public class CFakerParameterException extends RuntimeException {

  /**
   * Constructs a new CFakerParameterException with the specified detail message.
   * <p>
   * This constructor is used when a parameter validation error occurs and you want
   * to provide a descriptive message about what went wrong.
   * </p>
   * 
   * <h3>Usage Examples:</h3>
   * <pre>{@code
   * // Validate string parameter is not null or empty
   * if (pattern == null || pattern.trim().isEmpty()) {
   *     throw new CFakerParameterException("Pattern parameter cannot be null or empty");
   * }
   * 
   * // Validate numeric range
   * if (percentage < 0 || percentage > 100) {
   *     throw new CFakerParameterException(
   *         "Percentage must be between 0 and 100, got: " + percentage
   *     );
   * }
   * 
   * // Validate array parameter
   * if (options == null || options.length == 0) {
   *     throw new CFakerParameterException("Options array must contain at least one element");
   * }
   * }</pre>
   *
   * @param description the detail message explaining why the parameter is invalid.
   *                   The detail message is saved for later retrieval by the
   *                   {@link #getMessage()} method.
   * @see #getMessage()
   */
  public CFakerParameterException(String description) {
    super(description);
  }

  /**
   * Constructs a new CFakerParameterException with the specified detail message and cause.
   * <p>
   * This constructor is useful when a parameter validation error is detected while
   * handling another exception, allowing you to wrap the original exception while
   * providing context about the parameter validation failure.
   * </p>
   * 
   * <h3>Usage Examples:</h3>
   * <pre>{@code
   * // Wrapping a parsing exception
   * try {
   *     int value = Integer.parseInt(userInput);
   *     if (value < 0) {
   *         throw new CFakerParameterException("Value must be positive: " + value);
   *     }
   * } catch (NumberFormatException e) {
   *     throw new CFakerParameterException(
   *         "Invalid numeric parameter: '" + userInput + "'", e
   *     );
   * }
   * 
   * // Wrapping a reflection exception during parameter validation
   * try {
   *     Class<?> clazz = Class.forName(className);
   *     // Additional validation...
   * } catch (ClassNotFoundException e) {
   *     throw new CFakerParameterException(
   *         "Invalid class name parameter: " + className, e
   *     );
   * }
   * 
   * // Wrapping an I/O exception during configuration loading
   * try {
   *     Properties config = loadConfiguration(configPath);
   *     validateConfig(config);
   * } catch (IOException e) {
   *     throw new CFakerParameterException(
   *         "Cannot load configuration from: " + configPath, e
   *     );
   * }
   * }</pre>
   *
   * @param message the detail message explaining why the parameter is invalid.
   *               The detail message is saved for later retrieval by the
   *               {@link #getMessage()} method.
   * @param cause the cause (which is saved for later retrieval by the
   *             {@link #getCause()} method). A null value is permitted,
   *             and indicates that the cause is nonexistent or unknown.
   * @see #getMessage()
   * @see #getCause()
   */
  public CFakerParameterException(String message, Throwable cause) {
    super(message, cause);
  }
}

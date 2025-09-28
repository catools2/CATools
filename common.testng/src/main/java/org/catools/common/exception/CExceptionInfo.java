package org.catools.common.exception;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CSet;

import java.util.Objects;

/**
 * A data class that captures and stores exception information including type, message, and stack trace.
 * This class provides a convenient way to represent exception details in a structured format
 * that can be used for logging, serialization, or comparison purposes.
 * 
 * <p>The class supports fluent API pattern for setting properties and provides utility methods
 * for retrieving formatted exception information.</p>
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * // Creating from an exception
 * try {
 *     // Some operation that might throw
 *     throw new RuntimeException("Something went wrong");
 * } catch (Exception e) {
 *     CExceptionInfo info = new CExceptionInfo(e);
 *     System.out.println(info.getAllInfo());
 * }
 * 
 * // Creating manually
 * CExceptionInfo info = new CExceptionInfo()
 *     .setType("java.lang.RuntimeException")
 *     .setMessage("Custom error message")
 *     .setStackTrace("at com.example.MyClass.method(MyClass.java:42)");
 * }</pre>
 * 
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 */
public class CExceptionInfo {
  private String type = StringUtils.EMPTY;
  private String message = StringUtils.EMPTY;
  private String stackTrace = StringUtils.EMPTY;

  /**
   * Default constructor that creates an empty CExceptionInfo instance.
   * All fields are initialized to empty strings.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info = new CExceptionInfo();
   * // info.getType() returns ""
   * // info.getMessage() returns ""
   * // info.getStackTrace() returns ""
   * }</pre>
   */
  public CExceptionInfo() {
  }

  /**
   * Constructor that creates a CExceptionInfo instance from a Throwable.
   * Extracts the exception type, message, and stack trace from the provided throwable.
   * 
   * <p>If the throwable is null, all fields remain as empty strings.</p>
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * try {
   *     Integer.parseInt("not-a-number");
   * } catch (NumberFormatException e) {
   *     CExceptionInfo info = new CExceptionInfo(e);
   *     System.out.println("Type: " + info.getType()); // java.lang.NumberFormatException
   *     System.out.println("Message: " + info.getMessage()); // For input string: "not-a-number"
   * }
   * 
   * // With null throwable
   * CExceptionInfo info = new CExceptionInfo(null);
   * // All fields remain empty strings
   * }</pre>
   * 
   * @param t the throwable to extract information from, can be null
   */
  public CExceptionInfo(Throwable t) {
    if (t != null) {
      type = t.getClass().getName();
      message = t.getLocalizedMessage();
      stackTrace = CExceptionUtil.getStackTrace(t);
    }
  }

  /**
   * Compares this CExceptionInfo with another object for equality.
   * Two CExceptionInfo objects are considered equal if they have the same type, message, and stack trace.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info1 = new CExceptionInfo()
   *     .setType("RuntimeException")
   *     .setMessage("Error occurred");
   * 
   * CExceptionInfo info2 = new CExceptionInfo()
   *     .setType("RuntimeException")
   *     .setMessage("Error occurred");
   * 
   * System.out.println(info1.equals(info2)); // true
   * 
   * info2.setMessage("Different message");
   * System.out.println(info1.equals(info2)); // false
   * }</pre>
   * 
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CExceptionInfo that = (CExceptionInfo) o;
    return Objects.equals(type, that.type)
        && Objects.equals(message, that.message)
        && Objects.equals(stackTrace, that.stackTrace);
  }

  /**
   * Returns all non-blank exception information (type, message, stack trace) joined by newlines.
   * Only includes fields that are not blank, providing a clean formatted output.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info = new CExceptionInfo()
   *     .setType("java.lang.RuntimeException")
   *     .setMessage("Something went wrong")
   *     .setStackTrace("at com.example.MyClass.method(MyClass.java:42)");
   * 
   * System.out.println(info.getAllInfo());
   * // Output:
   * // java.lang.RuntimeException
   * // Something went wrong
   * // at com.example.MyClass.method(MyClass.java:42)
   * 
   * // With empty message
   * CExceptionInfo info2 = new CExceptionInfo()
   *     .setType("java.lang.RuntimeException")
   *     .setStackTrace("at com.example.MyClass.method(MyClass.java:42)");
   * 
   * System.out.println(info2.getAllInfo());
   * // Output:
   * // java.lang.RuntimeException
   * // at com.example.MyClass.method(MyClass.java:42)
   * }</pre>
   * 
   * @return a formatted string containing all non-blank exception information
   */
  public String getAllInfo() {
    return new CSet<>(type, message, stackTrace).getAll(StringUtils::isNotBlank).join("\n");
  }

  /**
   * Gets the exception message.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info = new CExceptionInfo()
   *     .setMessage("File not found");
   * 
   * String message = info.getMessage(); // "File not found"
   * }</pre>
   * 
   * @return the exception message, never null (empty string if not set)
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the exception message using fluent API pattern.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info = new CExceptionInfo()
   *     .setMessage("Connection timeout")
   *     .setType("java.net.SocketTimeoutException");
   * 
   * // Method chaining
   * CExceptionInfo info2 = new CExceptionInfo()
   *     .setMessage("Invalid input")
   *     .setStackTrace("at com.example.validate(Main.java:15)");
   * }</pre>
   * 
   * @param message the exception message to set
   * @return this CExceptionInfo instance for method chaining
   */
  public CExceptionInfo setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * Gets the exception stack trace.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * try {
   *     throw new RuntimeException("Test exception");
   * } catch (Exception e) {
   *     CExceptionInfo info = new CExceptionInfo(e);
   *     String stackTrace = info.getStackTrace();
   *     // Contains full stack trace like:
   *     // java.lang.RuntimeException: Test exception
   *     //     at com.example.MyClass.method(MyClass.java:10)
   *     //     at com.example.MyClass.main(MyClass.java:5)
   * }
   * }</pre>
   * 
   * @return the exception stack trace, never null (empty string if not set)
   */
  public String getStackTrace() {
    return stackTrace;
  }

  /**
   * Sets the exception stack trace using fluent API pattern.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * String customStackTrace = "java.lang.RuntimeException: Custom error\n" +
   *                          "    at com.example.MyClass.method(MyClass.java:42)\n" +
   *                          "    at com.example.MyClass.main(MyClass.java:10)";
   * 
   * CExceptionInfo info = new CExceptionInfo()
   *     .setType("java.lang.RuntimeException")
   *     .setMessage("Custom error")
   *     .setStackTrace(customStackTrace);
   * 
   * // Method chaining
   * CExceptionInfo info2 = new CExceptionInfo()
   *     .setStackTrace("Brief stack trace")
   *     .setMessage("Error message");
   * }</pre>
   * 
   * @param stackTrace the exception stack trace to set
   * @return this CExceptionInfo instance for method chaining
   */
  public CExceptionInfo setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
    return this;
  }

  /**
   * Gets the exception type (class name).
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * try {
   *     Integer.parseInt("not-a-number");
   * } catch (NumberFormatException e) {
   *     CExceptionInfo info = new CExceptionInfo(e);
   *     String type = info.getType(); // "java.lang.NumberFormatException"
   * }
   * 
   * // Manual setting
   * CExceptionInfo info = new CExceptionInfo()
   *     .setType("com.example.CustomException");
   * String type = info.getType(); // "com.example.CustomException"
   * }</pre>
   * 
   * @return the exception type (class name), never null (empty string if not set)
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the exception type (class name) using fluent API pattern.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info = new CExceptionInfo()
   *     .setType("java.io.FileNotFoundException")
   *     .setMessage("Config file not found");
   * 
   * // Method chaining with all properties
   * CExceptionInfo info2 = new CExceptionInfo()
   *     .setType("java.lang.IllegalArgumentException")
   *     .setMessage("Invalid parameter value")
   *     .setStackTrace("at com.example.validate(Utils.java:25)");
   * }</pre>
   * 
   * @param type the exception type (class name) to set
   * @return this CExceptionInfo instance for method chaining
   */
  public CExceptionInfo setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Returns the hash code for this CExceptionInfo instance.
   * The hash code is computed based on the type, message, and stack trace fields.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info1 = new CExceptionInfo()
   *     .setType("RuntimeException")
   *     .setMessage("Error");
   * 
   * CExceptionInfo info2 = new CExceptionInfo()
   *     .setType("RuntimeException")
   *     .setMessage("Error");
   * 
   * // Equal objects have the same hash code
   * System.out.println(info1.hashCode() == info2.hashCode()); // true
   * }</pre>
   * 
   * @return the hash code for this object
   */
  @Override
  public int hashCode() {
    return Objects.hash(type, message, stackTrace);
  }

  /**
   * Returns a string representation of this CExceptionInfo instance.
   * Includes all three fields (type, message, stackTrace) in a structured format.
   * 
   * <p><strong>Example:</strong></p>
   * <pre>{@code
   * CExceptionInfo info = new CExceptionInfo()
   *     .setType("java.lang.RuntimeException")
   *     .setMessage("Something went wrong")
   *     .setStackTrace("at com.example.MyClass.method(MyClass.java:42)");
   * 
   * System.out.println(info.toString());
   * // Output:
   * // CExceptionInfo{type='java.lang.RuntimeException', message='Something went wrong', 
   * //                stackTrace='at com.example.MyClass.method(MyClass.java:42)'}
   * }</pre>
   * 
   * @return a string representation of this object
   */
  @Override
  public String toString() {
    return "CExceptionInfo{"
        + "type='"
        + type
        + '\''
        + ", message='"
        + message
        + '\''
        + ", stackTrace='"
        + stackTrace
        + '\''
        + '}';
  }
}

package org.catools.common.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CList;

/**
 * Utility class for handling exceptions and stack traces. This class provides convenient methods
 * for extracting and formatting exception information, including stack traces and error messages.
 *
 * <p>All methods in this class are static and the class cannot be instantiated.
 *
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 */
@UtilityClass
@Slf4j
public class CExceptionUtil {
  /**
   * Retrieves the complete exception message combined with its stack trace.
   *
   * <p>This method returns a formatted string containing both the exception's toString()
   * representation and its full stack trace separated by two newlines. If the provided throwable is
   * null, an empty string is returned.
   *
   * @param t the throwable to extract message and stack trace from
   * @return a string containing the exception message and stack trace, or an empty string if the
   *     throwable is null
   * @example
   *     <pre>{@code
   * try {
   *   int result = 10 / 0;
   * } catch (ArithmeticException e) {
   *   String fullError = CExceptionUtil.getMessageAndStackTrace(e);
   *   // Output format:
   *   // java.lang.ArithmeticException: / by zero
   *   //
   *   // java.lang.ArithmeticException: / by zero
   *   //     at com.example.MyClass.divide(MyClass.java:15)
   *   //     at com.example.MyClass.main(MyClass.java:10)
   *   System.out.println(fullError);
   * }
   *
   * // Null handling
   * String result = CExceptionUtil.getMessageAndStackTrace(null);
   * // result = ""
   * }</pre>
   */
  public static String getMessageAndStackTrace(Throwable t) {
    if (t == null) {
      return StringUtils.EMPTY;
    }
    return t + "\n\n" + getStackTrace(t);
  }

  /**
   * Extracts and formats the stack trace from a throwable as a string.
   *
   * <p>This method converts the stack trace elements of the given throwable into a single string,
   * with each stack trace element separated by the system's line separator. If the provided
   * throwable is null, an empty string is returned.
   *
   * @param t the throwable to extract the stack trace from
   * @return a formatted string representation of the stack trace, or an empty string if the
   *     throwable is null
   * @example
   *     <pre>{@code
   * try {
   *   throw new IllegalArgumentException("Invalid input");
   * } catch (IllegalArgumentException e) {
   *   String stackTrace = CExceptionUtil.getStackTrace(e);
   *   // Output format:
   *   // java.lang.IllegalArgumentException: Invalid input
   *   //     at com.example.MyClass.validateInput(MyClass.java:25)
   *   //     at com.example.MyClass.processData(MyClass.java:15)
   *   //     at com.example.MyClass.main(MyClass.java:10)
   *   System.out.println(stackTrace);
   * }
   *
   * // Null handling
   * String result = CExceptionUtil.getStackTrace(null);
   * // result = ""
   * }</pre>
   */
  public static String getStackTrace(Throwable t) {
    return t == null
        ? StringUtils.EMPTY
        : new CList<>(t.getStackTrace()).join(System.lineSeparator());
  }

  /**
   * Prints the current thread's stack trace to the trace log level.
   *
   * <p>This method captures the current stack trace of the executing thread and logs it at the
   * TRACE level using the configured logger. This is useful for debugging purposes to understand
   * the execution path that led to a particular point in the code.
   *
   * <p><strong>Note:</strong> The stack trace will only be visible if the logging level is set to
   * TRACE or a more verbose level.
   *
   * @example
   *     <pre>{@code
   * public void complexMethod() {
   *   // Some complex logic here
   *   if (someCondition) {
   *     // Print stack trace for debugging
   *     CExceptionUtil.printCurrentStackTrace();
   *     // This will log something like:
   *     // TRACE - java.lang.Thread.getStackTrace(Thread.java:1559)
   *     //         org.catools.common.exception.CExceptionUtil.getCurrentStackTrace(CExceptionUtil.java:XX)
   *     //         org.catools.common.exception.CExceptionUtil.printCurrentStackTrace(CExceptionUtil.java:XX)
   *     //         com.example.MyClass.complexMethod(MyClass.java:XX)
   *     //         com.example.MyClass.main(MyClass.java:XX)
   *   }
   * }
   *
   * // Usage in a test or debugging scenario
   * public void debugWorkflow() {
   *   step1();
   *   CExceptionUtil.printCurrentStackTrace(); // See how we got here
   *   step2();
   * }
   * }</pre>
   */
  public static void printCurrentStackTrace() {
    log.trace(getCurrentStackTrace());
  }

  private static String getCurrentStackTrace() {
    return new CList<>(Thread.currentThread().getStackTrace()).join(System.lineSeparator());
  }
}

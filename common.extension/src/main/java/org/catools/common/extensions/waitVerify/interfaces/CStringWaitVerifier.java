package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.extensions.states.interfaces.CStringState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CStringVerifier;
import org.catools.common.utils.CStringUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * CStringVerifier is an interface for String verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 *
 * <p>Please Note that we should extend manually {@link
 * org.catools.common.extensions.verify.CStringVerification} for each new added verification here
 */
public interface CStringWaitVerifier
    extends CStringVerifier, CObjectWaitVerifier<String, CStringState> {

  default CStringState _toState(Object e) {
    return () -> (String) e;
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the int size of new String, negative treated as zero
   * @param padStr            the String to pad the new String with, must not be null or empty
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyCenterPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyCenterPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the int size of new String, negative treated as zero
   * @param padStr                 the String to pad the new String with, must not be null or empty
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyCenterPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).centerPadEquals(size, padStr, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the int size of new String, negative treated as zero
   * @param padStr            the String to pad the new String with, must not be null or empty
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyCenterPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds) {
    verifyCenterPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the int size of new String, negative treated as zero
   * @param padStr                 the String to pad the new String with, must not be null or empty
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyCenterPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyCenterPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Value Center Pad With '%s' And The Length Of '%d' Equals To Expected Value",
            padStr, size));
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the int size of new String, negative treated as zero
   * @param padStr            the String to pad the new String with, must not be null or empty
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyCenterPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyCenterPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the int size of new String, negative treated as zero
   * @param padStr                 the String to pad the new String with, must not be null or empty
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyCenterPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).centerPadNotEquals(size, padStr, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the int size of new String, negative treated as zero
   * @param padStr            the String to pad the new String with, must not be null or empty
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyCenterPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds) {
    verifyCenterPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the int size of new String, negative treated as zero
   * @param padStr                 the String to pad the new String with, must not be null or empty
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyCenterPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyCenterPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Value Center Pad With '%s' And The Length Of '%d' Is Not Equal To Expected Value",
            padStr, size));
  }

  /**
   * Verify if result of {@link CStringUtil#compare(String, String)} equals to the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stringToCompare   the string value to compare against
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyCompare(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyCompare(
        verificationQueue,
        stringToCompare,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#compare(String, String)} equals to the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stringToCompare        the string value to compare against
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyCompare(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        stringToCompare,
        true,
        (a, b) -> _toState(a).compare(b, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#compare(String, String)} equals to the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stringToCompare   the string value to compare against
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyCompare(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds) {
    verifyCompare(
        verificationQueue,
        stringToCompare,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#compare(String, String)} equals to the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stringToCompare        the string value to compare against
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyCompare(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyCompare(
        verificationQueue,
        stringToCompare,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Result Of Comparison With The Expected Value Is '%d'", expected));
  }

  /**
   * Verify if result of {@link CStringUtil#compareIgnoreCase(String, String)} equals to the
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stringToCompare   the string value to compare against
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyCompareIgnoreCase(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyCompareIgnoreCase(
        verificationQueue,
        stringToCompare,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#compareIgnoreCase(String, String)} equals to the
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stringToCompare        the string value to compare against
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyCompareIgnoreCase(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        stringToCompare,
        true,
        (a, b) -> _toState(a).compareIgnoreCase(b, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#compareIgnoreCase(String, String)} equals to the
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stringToCompare   the string value to compare against
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyCompareIgnoreCase(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds) {
    verifyCompareIgnoreCase(
        verificationQueue,
        stringToCompare,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#compareIgnoreCase(String, String)} equals to the
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stringToCompare        the string value to compare against
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyCompareIgnoreCase(
      CVerificationQueue verificationQueue,
      String stringToCompare,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyCompareIgnoreCase(
        verificationQueue,
        stringToCompare,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Result Of Comparison (Ignoring Case) With The Expected Value Is '%d'", expected));
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).contains(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyContains(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is true,
   * ignoring case.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyContainsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is true,
   * ignoring case.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).containsIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is true,
   * ignoring case.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyContainsIgnoreCase(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyContainsIgnoreCase(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is true,
   * ignoring case.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContainsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContainsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains The Expected Value Ignoring Case Sensitivity"));
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWith(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEndsWith(
        verificationQueue,
        suffix,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWith(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        suffix,
        false,
        (a, b) -> _toState(a).endsWith(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEndsWith(
      CVerificationQueue verificationQueue, String suffix, final int waitInSeconds) {
    verifyEndsWith(
        verificationQueue, suffix, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEndsWith(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEndsWith(
        verificationQueue,
        suffix,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Ends With The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequences to find, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWithAny(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEndsWithAny(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequences to find, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWithAny(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        searchInputs,
        false,
        (a, b) -> _toState(a).endsWithAny(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequences to find, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEndsWithAny(
      CVerificationQueue verificationQueue, List<String> searchInputs, final int waitInSeconds) {
    verifyEndsWithAny(
        verificationQueue, searchInputs, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequences to find, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEndsWithAny(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEndsWithAny(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Ends With Any Value From The Expected Values"));
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEndsWithIgnoreCase(
        verificationQueue,
        suffix,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        suffix,
        false,
        (a, b) -> _toState(a).endsWithIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEndsWithIgnoreCase(
      CVerificationQueue verificationQueue, String suffix, final int waitInSeconds) {
    verifyEndsWithIgnoreCase(
        verificationQueue, suffix, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEndsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEndsWithIgnoreCase(
        verificationQueue,
        suffix,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Ends With The Expected Value Ignoring Case Sensitivity"));
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequences to find, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWithNone(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEndsWithNone(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequences to find, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEndsWithNone(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        searchInputs,
        false,
        (a, b) -> _toState(a).endsWithNone(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequences to find, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEndsWithNone(
      CVerificationQueue verificationQueue, List<String> searchInputs, final int waitInSeconds) {
    verifyEndsWithNone(
        verificationQueue, searchInputs, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequences to find, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEndsWithNone(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEndsWithNone(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Ends With None Of Value From The Expected Values"));
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)}
   * equals to the expected value, ignoring case.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsAnyIgnoreCase(
      CVerificationQueue verificationQueue,
      List<String> expectedList,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsAnyIgnoreCase(
        verificationQueue,
        expectedList,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)}
   * equals to the expected value, ignoring case.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsAnyIgnoreCase(
      CVerificationQueue verificationQueue,
      List<String> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedList,
        false,
        (a, b) -> _toState(a).equalsAnyIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)}
   * equals to the expected value, ignoring case.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsAnyIgnoreCase(
      CVerificationQueue verificationQueue, List<String> expectedList, final int waitInSeconds) {
    verifyEqualsAnyIgnoreCase(
        verificationQueue, expectedList, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)}
   * equals to the expected value, ignoring case.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsAnyIgnoreCase(
      CVerificationQueue verificationQueue,
      List<String> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsAnyIgnoreCase(
        verificationQueue,
        expectedList,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Value Ends With Any Value From The Expected Values Ignoring Case Sensitivity"));
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value equals the
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value equals the
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).equalsIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value equals the
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsIgnoreCase(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyEqualsIgnoreCase(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value equals the
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Equals The Expected Values Ignoring Case Sensitivity"));
  }

  /**
   * Verify if value is equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsIgnoreWhiteSpaces(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if value is equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).equalsIgnoreWhiteSpaces(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if value is equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyEqualsIgnoreWhiteSpaces(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if value is equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsIgnoreWhiteSpaces(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Equals The Expected Values Ignoring White Spaces"));
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * false, ignoring case.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsNoneIgnoreCase(
      CVerificationQueue verificationQueue,
      List<String> expectedList,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsNoneIgnoreCase(
        verificationQueue,
        expectedList,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * false, ignoring case.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsNoneIgnoreCase(
      CVerificationQueue verificationQueue,
      List<String> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedList,
        false,
        (a, b) -> _toState(a).equalsNoneIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * false, ignoring case.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsNoneIgnoreCase(
      CVerificationQueue verificationQueue, List<String> expectedList, final int waitInSeconds) {
    verifyEqualsNoneIgnoreCase(
        verificationQueue, expectedList, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * false, ignoring case.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsNoneIgnoreCase(
      CVerificationQueue verificationQueue,
      List<String> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsNoneIgnoreCase(
        verificationQueue,
        expectedList,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Value Ends With None Of Value From The Expected Values Ignoring Case Sensitivity"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isAlpha(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsAlpha(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsAlpha(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsAlpha(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Alpha Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlphaSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsAlphaSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlphaSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isAlphaSpace(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsAlphaSpace(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsAlphaSpace(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsAlphaSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsAlphaSpace(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Alpha Or Space Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isAlphanumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsAlphanumeric(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsAlphanumeric(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsAlphanumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Alpha-Numeric Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlphanumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsAlphanumericSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAlphanumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isAlphanumericSpace(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsAlphanumericSpace(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsAlphanumericSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsAlphanumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsAlphanumericSpace(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Alpha-Numeric Or Space Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAsciiPrintable(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsAsciiPrintable(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsAsciiPrintable(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isAsciiPrintable(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsAsciiPrintable(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsAsciiPrintable(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsAsciiPrintable(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsAsciiPrintable(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Ascii Printable Characters"));
  }

  /**
   * Verify if String value is blank (Null or Empty)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlank(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsBlank(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if String value is blank (Null or Empty)
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlank(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        "<Blank>",
        false,
        (a, b) -> _toState(a).isBlank(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value is blank (Null or Empty)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsBlank(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsBlank(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if String value is blank (Null or Empty)
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsBlank(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsBlank(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Blank"));
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsBlankOrAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isBlankOrAlpha(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsBlankOrAlpha(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsBlankOrAlpha(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsBlankOrAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsBlankOrAlpha(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Alpha Characters Or It Is Blank"));
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsBlankOrAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isBlankOrAlphanumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsBlankOrAlphanumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsBlankOrAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsBlankOrAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsBlankOrAlphanumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Alpha-Numeric Characters Or It Is Blank"));
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsBlankOrNotAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isBlankOrNotAlpha(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsBlankOrNotAlpha(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsBlankOrNotAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsBlankOrNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsBlankOrNotAlpha(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Blank Or Not Contains Only Alpha-Numeric Characters"));
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsBlankOrNotAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isBlankOrNotAlphanumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsBlankOrNotAlphanumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsBlankOrNotAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsBlankOrNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsBlankOrNotAlphanumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Blank Or Not Contains Only Alpha-Numeric Characters"));
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsBlankOrNotNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isBlankOrNotNumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsBlankOrNotNumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsBlankOrNotNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsBlankOrNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsBlankOrNotNumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Blank Or Not Contains Only Numeric Characters"));
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsBlankOrNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsBlankOrNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isBlankOrNumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsBlankOrNumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsBlankOrNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsBlankOrNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsBlankOrNumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Blank Or Not Contains Only Numeric Characters"));
  }

  /**
   * Verify if String value is empty
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmpty(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if String value is empty
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        "<Empty>",
        false,
        (a, b) -> _toState(a).isEmpty(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value is empty
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmpty(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmpty(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if String value is empty
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmpty(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Empty"));
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmptyOrAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isEmptyOrAlpha(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmptyOrAlpha(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmptyOrAlpha(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmptyOrAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmptyOrAlpha(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Empty Or Contains Only Alpha Characters"));
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmptyOrAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isEmptyOrAlphanumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmptyOrAlphanumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmptyOrAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmptyOrAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmptyOrAlphanumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Empty Or Contains Only Alpha-Numeric Characters"));
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmptyOrNotAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isEmptyOrNotAlpha(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmptyOrNotAlpha(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmptyOrNotAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmptyOrNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmptyOrNotAlpha(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Empty Or Not Contains Only Alpha Characters"));
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmptyOrNotAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isEmptyOrNotAlphanumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmptyOrNotAlphanumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmptyOrNotAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmptyOrNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmptyOrNotAlphanumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Empty Or Not Contains Only Alpha-Numeric Characters"));
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmptyOrNotNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isEmptyOrNotNumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmptyOrNotNumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmptyOrNotNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmptyOrNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmptyOrNotNumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Empty Or Not Contains Only Numeric Characters"));
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmptyOrNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmptyOrNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isEmptyOrNumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmptyOrNumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmptyOrNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if string is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmptyOrNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmptyOrNumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Empty Or Contains Only Numeric Characters"));
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param pattern           regular expression pattern
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue,
      final Pattern pattern,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param pattern                regular expression pattern
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue,
      final Pattern pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        pattern,
        false,
        (a, b) -> _toState(a).matches(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value match any of provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param patterns          regular expression patterns
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyMatchesAny(
      CVerificationQueue verificationQueue,
      final List<Pattern> patterns,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyMatchesAny(
        verificationQueue,
        patterns,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if String value match any of provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param patterns               regular expression patterns
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyMatchesAny(
      CVerificationQueue verificationQueue,
      final List<Pattern> patterns,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        patterns,
        false,
        (a, b) -> _toState(a).matchAny(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value match NONE of provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param patterns          regular expression patterns
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyMatchesNone(
      CVerificationQueue verificationQueue,
      final List<Pattern> patterns,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyMatchesNone(
        verificationQueue,
        patterns,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if String value match NONE of provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param patterns               regular expression patterns
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyMatchesNone(
      CVerificationQueue verificationQueue,
      final List<Pattern> patterns,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        patterns,
        false,
        (a, b) -> _toState(a).matchNone(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param pattern           regular expression pattern
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue,
      final String pattern,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param pattern                regular expression pattern
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue,
      final String pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        pattern,
        false,
        (a, b) -> _toState(a).matches(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param pattern           regular expression pattern
   * @param waitInSeconds     maximum wait time
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue, final Pattern pattern, final int waitInSeconds) {
    verifyMatches(
        verificationQueue, pattern, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param pattern           regular expression pattern
   * @param waitInSeconds     maximum wait time
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue, final String pattern, final int waitInSeconds) {
    verifyMatches(
        verificationQueue, pattern, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param pattern                regular expression pattern
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue,
      final Pattern pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Matches The Provided Pattern"));
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param pattern                regular expression pattern
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyMatches(
      CVerificationQueue verificationQueue,
      final String pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Matches The Provided Pattern"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotAlpha(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNotAlpha(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotAlpha(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotAlpha(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotAlpha(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotAlpha(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Not Contains Only Alpha Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlphaSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotAlphaSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlphaSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNotAlphaSpace(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotAlphaSpace(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotAlphaSpace(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotAlphaSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotAlphaSpace(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Not Contains Only Alpha Characters Or Space"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNotAlphanumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotAlphanumeric(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotAlphanumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotAlphanumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotAlphanumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Not Contains Only Alpha-Numeric Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlphanumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotAlphanumericSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAlphanumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNotAlphanumericSpace(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotAlphanumericSpace(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotAlphanumericSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotAlphanumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotAlphanumericSpace(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Not Contains Only Alpha-Numeric Characters Or Space"));
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAsciiPrintable(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotAsciiPrintable(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotAsciiPrintable(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNotAsciiPrintable(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotAsciiPrintable(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotAsciiPrintable(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotAsciiPrintable(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotAsciiPrintable(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Not Contains Only Ascii Printable Characters"));
  }

  /**
   * Verify if String value is not blank (Null or Empty)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotBlank(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotBlank(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if String value is not blank (Null or Empty)
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotBlank(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        "<Not Blank>",
        false,
        (a, b) -> _toState(a).isNotBlank(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value is not blank (Null or Empty)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotBlank(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotBlank(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if String value is not blank (Null or Empty)
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotBlank(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotBlank(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Not Blank"));
  }

  /**
   * Verify String value is not empty
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotEmpty(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify String value is not empty
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        "<Not Empty>",
        false,
        (a, b) -> _toState(a).isNotEmpty(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify String value is not empty
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotEmpty(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotEmpty(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify String value is not empty
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotEmpty(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Not Empty"));
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param pattern           regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue,
      final Pattern pattern,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param pattern                regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue,
      final Pattern pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        pattern,
        false,
        (a, b) -> _toState(a).notMatches(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param pattern           regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue,
      final String pattern,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param pattern                regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue,
      final String pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        pattern,
        false,
        (a, b) -> _toState(a).notMatches(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param pattern           regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue, final Pattern pattern, final int waitInSeconds) {
    verifyNotMatches(
        verificationQueue, pattern, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param pattern                regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue,
      final Pattern pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Not Match The Expected Pattern"));
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param pattern           regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue, final String pattern, final int waitInSeconds) {
    verifyNotMatches(
        verificationQueue, pattern, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param pattern                regular expression pattern
   */
  default void verifyNotMatches(
      CVerificationQueue verificationQueue,
      final String pattern,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotMatches(
        verificationQueue,
        pattern,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Is Not Match The Expected Pattern"));
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNotNumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotNumeric(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotNumeric(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotNumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Not Contains Only Numeric Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotNumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotNumericSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotNumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNotNumericSpace(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotNumericSpace(
      CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotNumericSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotNumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotNumericSpace(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Not Contains Only Numeric Or Space Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNumeric(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNumeric(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNumeric(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNumeric(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNumeric(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNumeric(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Numeric Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNumericSpace(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> _toState(a).isNumericSpace(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNumericSpace(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNumericSpace(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is true.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNumericSpace(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNumericSpace(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Value Contains Only Numeric Or Space Characters"));
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLeftPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).leftPadEquals(size, padStr, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLeftPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds) {
    verifyLeftPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLeftPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLeftPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Equals To The Actual Value Left Pad With '%s' And The Length Of '%d'",
            padStr, size));
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLeftPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).leftPadNotEquals(size, padStr, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLeftPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds) {
    verifyLeftPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLeftPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLeftPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Not Equals To The Actual Value Left Pad With '%s' And The Length Of '%d'",
            padStr, size));
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLeftValueEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).leftValueEquals(len, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLeftValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds) {
    verifyLeftValueEquals(
        verificationQueue, len, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLeftValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLeftValueEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Expected Value Equals To The Left '%d' Character Of Actual Value", len));
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLeftValueNotEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLeftValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).leftValueNotEquals(len, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLeftValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds) {
    verifyLeftValueNotEquals(
        verificationQueue, len, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLeftValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLeftValueNotEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Not Equals To The Left '%d' Character Of Actual Value", len));
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLengthEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLengthEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLengthEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).lengthEquals(expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLengthEquals(
      CVerificationQueue verificationQueue, int expected, final int waitInSeconds) {
    verifyLengthEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLengthEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLengthEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Expected Value Length Equals To The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLengthNotEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLengthNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLengthNotEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).lengthNotEquals(expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLengthNotEquals(
      CVerificationQueue verificationQueue, int expected, final int waitInSeconds) {
    verifyLengthNotEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLengthNotEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLengthNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Expected Value Length Not Equals To The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param pos               the position to start from, negative treated as zero
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyMidValueEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyMidValueEquals(
        verificationQueue,
        pos,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param pos                    the position to start from, negative treated as zero
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyMidValueEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).midValueEquals(pos, len, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param pos               the position to start from, negative treated as zero
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   */
  default void verifyMidValueEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds) {
    verifyMidValueEquals(
        verificationQueue,
        pos,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param pos                    the position to start from, negative treated as zero
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyMidValueEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyMidValueEquals(
        verificationQueue,
        pos,
        len,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Equals To The Characters Of Actual Value From Position '%d' For '%d' Length",
            pos, len));
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param pos               the position to start from, negative treated as zero
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyMidValueNotEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyMidValueNotEquals(
        verificationQueue,
        pos,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param pos                    the position to start from, negative treated as zero
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyMidValueNotEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).midValueNotEquals(pos, len, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param pos               the position to start from, negative treated as zero
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   */
  default void verifyMidValueNotEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds) {
    verifyMidValueNotEquals(
        verificationQueue,
        pos,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param pos                    the position to start from, negative treated as zero
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyMidValueNotEquals(
      CVerificationQueue verificationQueue,
      int pos,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyMidValueNotEquals(
        verificationQueue,
        pos,
        len,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Not Equals To The Characters Of Actual Value From Position '%d' For '%d' Length",
            pos, len));
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).notContains(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyNotContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Not Contains The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContainsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotContainsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContainsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).notContainsIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotContainsIgnoreCase(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyNotContainsIgnoreCase(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotContainsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotContainsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Not Contains The Expected Value Ignoring Case Sensitivity"));
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEndsWith(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEndsWith(
        verificationQueue,
        suffix,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEndsWith(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        suffix,
        false,
        (a, b) -> _toState(a).notEndsWith(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotEndsWith(
      CVerificationQueue verificationQueue, String suffix, final int waitInSeconds) {
    verifyNotEndsWith(
        verificationQueue, suffix, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEndsWith(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotEndsWith(
        verificationQueue,
        suffix,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Not Ends With The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEndsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEndsWithIgnoreCase(
        verificationQueue,
        suffix,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEndsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        suffix,
        false,
        (a, b) -> _toState(a).notEndsWithIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param suffix            the suffix to find, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotEndsWithIgnoreCase(
      CVerificationQueue verificationQueue, String suffix, final int waitInSeconds) {
    verifyNotEndsWithIgnoreCase(
        verificationQueue, suffix, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param suffix                 the suffix to find, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEndsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      String suffix,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotEndsWithIgnoreCase(
        verificationQueue,
        suffix,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Not Ends With The Expected Value Ignoring Case Sensitivity"));
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value NOT equals the
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEqualsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value NOT equals the
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).notEqualsIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value NOT equals the
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotEqualsIgnoreCase(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyNotEqualsIgnoreCase(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#equalsIgnoreCase(CharSequence, CharSequence)} value NOT equals the
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEqualsIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotEqualsIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Not Equals To The Expected Value Ignoring Case Sensitivity"));
  }

  /**
   * Verify if value is not equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEqualsIgnoreWhiteSpaces(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if value is not equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).notEqualsIgnoreWhiteSpaces(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if value is not equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyNotEqualsIgnoreWhiteSpaces(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if value is not equal to expected after removing all WhiteSpaces from both.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEqualsIgnoreWhiteSpaces(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotEqualsIgnoreWhiteSpaces(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Not Equals To The Expected Value Ignoring White Spaces"));
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotStartsWith(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotStartsWith(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotStartsWith(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).notStartsWith(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotStartsWith(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyNotStartsWith(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotStartsWith(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotStartsWith(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Not Starts With The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotStartsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotStartsWithIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotStartsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).notStartsWithIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotStartsWithIgnoreCase(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyNotStartsWithIgnoreCase(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotStartsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotStartsWithIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Not Starts With The Expected Value Ignoring Case Sensitivity"));
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param subString         the substring to count, may be null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNumberOfMatchesEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNumberOfMatchesEquals(
        verificationQueue,
        subString,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param subString              the substring to count, may be null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNumberOfMatchesEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).numberOfMatchesEquals(subString, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param subString         the substring to count, may be null
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNumberOfMatchesEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds) {
    verifyNumberOfMatchesEquals(
        verificationQueue,
        subString,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param subString              the substring to count, may be null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNumberOfMatchesEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNumberOfMatchesEquals(
        verificationQueue,
        subString,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Contains Exact Number Of Substring"));
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param subString         the substring to count, may be null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNumberOfMatchesNotEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNumberOfMatchesNotEquals(
        verificationQueue,
        subString,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param subString              the substring to count, may be null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNumberOfMatchesNotEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).numberOfMatchesNotEquals(subString, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param subString         the substring to count, may be null
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNumberOfMatchesNotEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds) {
    verifyNumberOfMatchesNotEquals(
        verificationQueue,
        subString,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param subString              the substring to count, may be null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNumberOfMatchesNotEquals(
      CVerificationQueue verificationQueue,
      String subString,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNumberOfMatchesNotEquals(
        verificationQueue,
        subString,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Not Contains Exact Number Of Substring"));
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveEndEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeEndEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveEndEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveEndEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveEndEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveEndEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' From End, Equals To The Expected Value", remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveEndIgnoreCaseEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeEndIgnoreCaseEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveEndIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveEndIgnoreCaseEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveEndIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveEndIgnoreCaseEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' From End Ignoring Case Sensitivity, Equals To The Expected Value",
            remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveEndIgnoreCaseNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeEndIgnoreCaseNotEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveEndIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveEndIgnoreCaseNotEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveEndIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveEndIgnoreCaseNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' From End Ignoring Case Sensitivity, Not Equals To The Expected Value",
            remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveEndNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEndNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeEndNotEquals(remove, expected),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveEndNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveEndNotEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveEndNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveEndNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s', Not Equals To The Expected Value", remove));
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s', Equals To The Expected Value", remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveIgnoreCaseEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeIgnoreCaseEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveIgnoreCaseEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveIgnoreCaseEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' Ignoring Case Sensitivity, Equals To The Expected Value",
            remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveIgnoreCaseNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeIgnoreCaseNotEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveIgnoreCaseNotEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveIgnoreCaseNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' Ignoring Case Sensitivity, Not Equals To The Expected Value",
            remove));
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeNotEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveNotEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s', Not Equals To The Expected Value", remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveStartEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeStartEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveStartEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveStartEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveStartEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveStartEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' From Start, Equals To The Expected Value", remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveStartIgnoreCaseEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeStartIgnoreCaseEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveStartIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveStartIgnoreCaseEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveStartIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveStartIgnoreCaseEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' From Start Ignoring Case Sensitivity, Equals To The Expected Value",
            remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveStartIgnoreCaseNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeStartIgnoreCaseNotEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for (case insensitive) and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveStartIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveStartIgnoreCaseNotEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for (case insensitive) and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveStartIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveStartIgnoreCaseNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' From Start Ignoring Case Sensitivity, Not Equals To The Expected Value",
            remove));
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRemoveStartNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRemoveStartNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).removeStartNotEquals(remove, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param remove            the String to search for and remove, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRemoveStartNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds) {
    verifyRemoveStartNotEquals(
        verificationQueue, remove, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param remove                 the String to search for and remove, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRemoveStartNotEquals(
      CVerificationQueue verificationQueue,
      String remove,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRemoveStartNotEquals(
        verificationQueue,
        remove,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Removing '%s' From Start, Not Equals To The Expected Value",
            remove));
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing '%s' With '%s', Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceIgnoreCaseEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceIgnoreCaseEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceIgnoreCaseEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceIgnoreCaseEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing '%s' With '%s' Ignoring Case Sensitivity, Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceIgnoreCaseNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceIgnoreCaseNotEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceIgnoreCaseNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is NOT equals
   * to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceIgnoreCaseNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing '%s' With '%s' Ignoring Case Sensitivity, Not Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceNotEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace it with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace it with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing '%s' With '%s', Not Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for, may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceOnceEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for, may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceOnceEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for, may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceOnceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceOnceEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for, may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceOnceEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceOnceEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing Once '%s' With '%s', Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is equals
   * to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceOnceIgnoreCaseEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is equals
   * to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceOnceIgnoreCaseEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is equals
   * to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceOnceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceOnceIgnoreCaseEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is equals
   * to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceOnceIgnoreCaseEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceOnceIgnoreCaseEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing Once '%s' With '%s' Ignoring Case Sensitivity, Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is NOT
   * equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceOnceIgnoreCaseNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is NOT
   * equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceOnceIgnoreCaseNotEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is NOT
   * equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for (case insensitive), may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceOnceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceOnceIgnoreCaseNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is NOT
   * equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for (case insensitive), may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceOnceIgnoreCaseNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceOnceIgnoreCaseNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing Once '%s' With '%s' Ignoring Case Sensitivity, Not Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for, may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReplaceOnceNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for, may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReplaceOnceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).replaceOnceNotEquals(searchString, replacement, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchString      the String to search for, may be null
   * @param replacement       the String to replace with, may be null
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReplaceOnceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds) {
    verifyReplaceOnceNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchString           the String to search for, may be null
   * @param replacement            the String to replace with, may be null
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReplaceOnceNotEquals(
      CVerificationQueue verificationQueue,
      String searchString,
      String replacement,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReplaceOnceNotEquals(
        verificationQueue,
        searchString,
        replacement,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Replacing Once '%s' With '%s', Not Equals To The Expected Value",
            searchString, replacement));
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReverseEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReverseEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReverseEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).reverseEquals(expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReverseEquals(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyReverseEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReverseEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReverseEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Reversing Order Of Characters, Equals To The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyReverseNotEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyReverseNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyReverseNotEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).reverseNotEquals(expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyReverseNotEquals(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyReverseNotEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyReverseNotEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyReverseNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value, After Reversing Order Of Characters, Not Equals To The Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRightPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRightPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRightPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).rightPadEquals(size, padStr, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRightPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds) {
    verifyRightPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRightPadEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRightPadEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Equals To The Actual Value Right Pad With '%s' And The Length Of '%d'",
            padStr, size));
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRightPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRightPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRightPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).rightPadNotEquals(size, padStr, expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param size              the size to pad to
   * @param padStr            the String to pad with, null or empty treated as single space
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRightPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds) {
    verifyRightPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param size                   the size to pad to
   * @param padStr                 the String to pad with, null or empty treated as single space
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRightPadNotEquals(
      CVerificationQueue verificationQueue,
      int size,
      String padStr,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRightPadNotEquals(
        verificationQueue,
        size,
        padStr,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Not Equals To The Actual Value Right Pad With '%s' And The Length Of '%d'",
            padStr, size));
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRightValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRightValueEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRightValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).rightValueEquals(len, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRightValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds) {
    verifyRightValueEquals(
        verificationQueue, len, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRightValueEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRightValueEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Equals To The Right '%d' Character Of Actual Value", len));
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyRightValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyRightValueNotEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyRightValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).rightValueNotEquals(len, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param len               the length of the required String
   * @param waitInSeconds     maximum wait time
   */
  default void verifyRightValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds) {
    verifyRightValueNotEquals(
        verificationQueue, len, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param len                    the length of the required String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyRightValueNotEquals(
      CVerificationQueue verificationQueue,
      int len,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyRightValueNotEquals(
        verificationQueue,
        len,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Expected Value Not Equals To The Right '%d' Character Of Actual Value", len));
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWith(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStartsWith(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWith(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).startsWith(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStartsWith(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyStartsWith(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStartsWith(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStartsWith(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Starts With Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWithAny(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStartsWithAny(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWithAny(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        searchInputs,
        false,
        (a, b) -> _toState(a).startsWithAny(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStartsWithAny(
      CVerificationQueue verificationQueue, List<String> searchInputs, final int waitInSeconds) {
    verifyStartsWithAny(
        verificationQueue, searchInputs, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStartsWithAny(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStartsWithAny(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Starts With Any Expected Value"));
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStartsWithIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).startsWithIgnoreCase(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStartsWithIgnoreCase(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyStartsWithIgnoreCase(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStartsWithIgnoreCase(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStartsWithIgnoreCase(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Starts With Expected Value Ignoring Case Sensitivity"));
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWithNone(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStartsWithNone(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStartsWithNone(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        searchInputs,
        false,
        (a, b) -> _toState(a).startsWithNone(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param searchInputs      the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                          null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStartsWithNone(
      CVerificationQueue verificationQueue, List<String> searchInputs, final int waitInSeconds) {
    verifyStartsWithNone(
        verificationQueue, searchInputs, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param searchInputs           the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                               null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStartsWithNone(
      CVerificationQueue verificationQueue,
      List<String> searchInputs,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStartsWithNone(
        verificationQueue,
        searchInputs,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Value Starts With None Of Expected Value"));
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedEndValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStripedEndValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedEndValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).stripedEndValue(stripChars, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStripedEndValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds) {
    verifyStripedEndValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStripedEndValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStripedEndValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Striped End '%s' Characters, Equals To The Expected Value", stripChars));
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedEndValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStripedEndValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedEndValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).stripedEndValueNot(stripChars, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStripedEndValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds) {
    verifyStripedEndValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#stripEnd(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStripedEndValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStripedEndValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Striped End '%s' Characters, Not Equals To The Expected Value",
            stripChars));
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedStartValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStripedStartValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedStartValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).stripedStartValue(stripChars, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStripedStartValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds) {
    verifyStripedStartValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStripedStartValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStripedStartValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Striped Start '%s' Characters, Equals To The Expected Value",
            stripChars));
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedStartValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStripedStartValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedStartValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).stripedStartValueNot(stripChars, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStripedStartValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds) {
    verifyStripedStartValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#stripStart(String, String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStripedStartValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStripedStartValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Striped End '%s' Characters, Not Equals To The Expected Value",
            stripChars));
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStripedValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).stripedValue(stripChars, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStripedValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds) {
    verifyStripedValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStripedValue(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStripedValue(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Striped '%s' Characters, Equals To The Expected Value", stripChars));
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyStripedValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyStripedValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).stripedValueNot(stripChars, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param stripChars        the characters to remove, null treated as whitespace
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyStripedValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds) {
    verifyStripedValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#strip(String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param stripChars             the characters to remove, null treated as whitespace
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyStripedValueNot(
      CVerificationQueue verificationQueue,
      String stripChars,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyStripedValueNot(
        verificationQueue,
        stripChars,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Striped '%s' Characters, Not Equals To The Expected Value", stripChars));
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringAfterEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringAfterEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringAfterEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringAfterEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringAfterEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringAfterEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring After '%s', Equals To The Expected Value", separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringAfterLastEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringAfterLastEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringAfterLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringAfterLastEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringAfterLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringAfterLastEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring After Last '%s', Equals To The Expected Value", separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringAfterLastNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringAfterLastNotEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringAfterLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringAfterLastNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringAfterLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringAfterLastNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring After Last '%s', Not Equals To The Expected Value", separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringAfterNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringAfterNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringAfterNotEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringAfterNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringAfterNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringAfterNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringAfterNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring After '%s', Not Equals To The Expected Value", separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringBeforeEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringBeforeEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringBeforeEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringBeforeEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringBeforeEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringBeforeEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Before '%s', Equals To The Expected Value", separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringBeforeLastEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringBeforeLastEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringBeforeLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringBeforeLastEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringBeforeLastEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringBeforeLastEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Before Last '%s', Equals To The Expected Value", separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringBeforeLastNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringBeforeLastNotEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringBeforeLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringBeforeLastNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringBeforeLastNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringBeforeLastNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Before Last '%s', Not Equals To The Expected Value",
            separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringBeforeNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBeforeNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringBeforeNotEquals(separator, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param separator         the String to search for, may be {@code null}
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringBeforeNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds) {
    verifySubstringBeforeNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param separator              the String to search for, may be {@code null}
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringBeforeNotEquals(
      CVerificationQueue verificationQueue,
      String separator,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringBeforeNotEquals(
        verificationQueue,
        separator,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Before '%s', Not Equals To The Expected Value", separator));
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringBetweenEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).substringBetweenEquals(open, close, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds) {
    verifySubstringBetweenEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringBetweenEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Between '%s' and '%s', Equals To The Expected Value",
            open, close));
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringBetweenNotEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringBetweenNotEquals(open, close, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds) {
    verifySubstringBetweenNotEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringBetweenNotEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Between '%s' and '%s', Not Equals To The Expected Value",
            open, close));
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringEquals(
        verificationQueue,
        start,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param end               the position to end at (exclusive), negative means count back from the end of the
   *                          String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringEquals(
        verificationQueue,
        start,
        end,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringEquals(start, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param end                    the position to end at (exclusive), negative means count back from the end of the
   *                               String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringEquals(start, end, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds) {
    verifySubstringEquals(
        verificationQueue, start, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringEquals(
        verificationQueue,
        start,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring After Position '%s', Equals To The Expected Value", start));
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param end               the position to end at (exclusive), negative means count back from the end of the
   *                          String
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds) {
    verifySubstringEquals(
        verificationQueue,
        start,
        end,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param end                    the position to end at (exclusive), negative means count back from the end of the
   *                               String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringEquals(
        verificationQueue,
        start,
        end,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring From Position '%s' To '%s', Equals To The Expected Value",
            start, end));
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringNotEquals(
        verificationQueue,
        start,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param end               the position to end at (exclusive), negative means count back from the end of the
   *                          String
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringNotEquals(
        verificationQueue,
        start,
        end,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringNotEquals(start, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param end                    the position to end at (exclusive), negative means count back from the end of the
   *                               String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringNotEquals(start, end, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds) {
    verifySubstringNotEquals(
        verificationQueue, start, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} NOT equals to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringNotEquals(
        verificationQueue,
        start,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring After Position '%s', Not Equals To The Expected Value", start));
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} NOT equals to expected
   * value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param start             the position to start from, negative means count back from the end of the String
   * @param end               the position to end at (exclusive), negative means count back from the end of the
   *                          String
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds) {
    verifySubstringNotEquals(
        verificationQueue,
        start,
        end,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} NOT equals to expected
   * value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param start                  the position to start from, negative means count back from the end of the String
   * @param end                    the position to end at (exclusive), negative means count back from the end of the
   *                               String
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringNotEquals(
      CVerificationQueue verificationQueue,
      int start,
      int end,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringNotEquals(
        verificationQueue,
        start,
        end,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring From Position '%s' To '%s', Not Equals To The Expected Value",
            start, end));
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} contains to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringsBetweenContains(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} contains to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringsBetweenContains(open, close, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} Contains
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringsBetweenContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds) {
    verifySubstringsBetweenContains(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} Contains
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringsBetweenContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringsBetweenContains(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Between '%s' To '%s' Characters, Contains The Expected Value",
            open, close));
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringsBetweenEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringsBetweenEquals(open, close, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringsBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds) {
    verifySubstringsBetweenEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringsBetweenEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringsBetweenEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Between '%s' To '%s' Characters, Equals To The Expected Value",
            open, close));
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT contains
   * to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenNotContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringsBetweenNotContains(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT contains
   * to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenNotContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringsBetweenNotContains(open, close, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT Contains
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringsBetweenNotContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds) {
    verifySubstringsBetweenNotContains(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT Contains
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringsBetweenNotContains(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringsBetweenNotContains(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Between '%s' To '%s' Characters, Not Contains The Expected Value",
            open, close));
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySubstringsBetweenNotEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySubstringsBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).substringsBetweenNotEquals(open, close, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param open              the String identifying the start of the substring, empty returns null
   * @param close             the String identifying the end of the substring, empty returns null
   * @param waitInSeconds     maximum wait time
   */
  default void verifySubstringsBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds) {
    verifySubstringsBetweenNotEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param open                   the String identifying the start of the substring, empty returns null
   * @param close                  the String identifying the end of the substring, empty returns null
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySubstringsBetweenNotEquals(
      CVerificationQueue verificationQueue,
      String open,
      String close,
      List<String> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySubstringsBetweenNotEquals(
        verificationQueue,
        open,
        close,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Value Substring Between '%s' To '%s' Characters, Not Equals The Expected Value",
            open, close));
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyTrimmedValueEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyTrimmedValueEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyTrimmedValueEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).trimmedValueEquals(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyTrimmedValueEquals(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyTrimmedValueEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyTrimmedValueEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyTrimmedValueEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Trimmed Value, Equals The Expected Value"));
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyTrimmedValueNotEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyTrimmedValueNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyTrimmedValueNotEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).trimmedValueNotEquals(b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyTrimmedValueNotEquals(
      CVerificationQueue verificationQueue, final String expected, final int waitInSeconds) {
    verifyTrimmedValueNotEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#trim(String)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyTrimmedValueNotEquals(
      CVerificationQueue verificationQueue,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyTrimmedValueNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Actual Trimmed Value, Not Equals The Expected Value"));
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyTruncatedValueEquals(
        verificationQueue,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param offset            left edge of string to start truncate from
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyTruncatedValueEquals(
        verificationQueue,
        offset,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).truncatedValueEquals(maxWidth, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param offset                 left edge of string to start truncate from
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).truncatedValueEquals(offset, maxWidth, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds) {
    verifyTruncatedValueEquals(
        verificationQueue,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyTruncatedValueEquals(
        verificationQueue,
        maxWidth,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Truncated Value With Maximum Width Of %s, Equals The Expected Value",
            maxWidth));
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param offset            left edge of string to start truncate from
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds) {
    verifyTruncatedValueEquals(
        verificationQueue,
        offset,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param offset                 left edge of string to start truncate from
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyTruncatedValueEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyTruncatedValueEquals(
        verificationQueue,
        offset,
        maxWidth,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Truncated Value With Maximum Width Of %s With Offset %s, Equals The Expected Value",
            maxWidth, offset));
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyTruncatedValueNotEquals(
        verificationQueue,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param offset            left edge of string to start truncate from
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyTruncatedValueNotEquals(
        verificationQueue,
        offset,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, b) -> _toState(a).truncatedValueNotEquals(maxWidth, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param offset                 left edge of string to start truncate from
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, b) -> _toState(a).truncatedValueNotEquals(offset, maxWidth, b),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds) {
    verifyTruncatedValueNotEquals(
        verificationQueue,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int)} value equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyTruncatedValueNotEquals(
        verificationQueue,
        maxWidth,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Truncated Value With Maximum Width Of %s, Not Equals The Expected Value",
            maxWidth));
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value NOT equals the expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param offset            left edge of string to start truncate from
   * @param maxWidth          maximum length of truncated string, must be positive
   * @param expected          the expected result.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds) {
    verifyTruncatedValueNotEquals(
        verificationQueue,
        offset,
        maxWidth,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify if {@link CStringUtil#truncate(String, int, int)} value NOT equals the expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param offset                 left edge of string to start truncate from
   * @param maxWidth               maximum length of truncated string, must be positive
   * @param expected               the expected result.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyTruncatedValueNotEquals(
      CVerificationQueue verificationQueue,
      int offset,
      int maxWidth,
      final String expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyTruncatedValueNotEquals(
        verificationQueue,
        offset,
        maxWidth,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage(
            "Actual Truncated Value With Maximum Width Of %s With Offset %s, Not Equals The Expected Value",
            maxWidth, offset));
  }
}

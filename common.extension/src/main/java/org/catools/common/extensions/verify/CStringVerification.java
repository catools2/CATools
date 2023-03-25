package org.catools.common.extensions.verify;

import org.catools.common.extensions.verify.interfaces.CStringVerifier;
import org.catools.common.utils.CStringUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * String verification class contains all verification method which is related to String
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CStringVerification<T extends CVerificationBuilder> extends CBaseVerification<T> {
  public CStringVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the int size of new String, negative treated as zero
   * @param padStr   the String to pad the new String with, must not be null or empty
   * @param expected the expected result.
   */
  public void centerPadEquals(String actual, int size, String padStr, String expected) {
    toVerifier(actual).verifyCenterPadEquals(verifier, size, padStr, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the int size of new String, negative treated as zero
   * @param padStr   the String to pad the new String with, must not be null or empty
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void centerPadEquals(
      String actual,
      int size,
      String padStr,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyCenterPadEquals(verifier, size, padStr, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the int size of new String, negative treated as zero
   * @param padStr   the String to pad the new String with, must not be null or empty
   * @param expected the expected result.
   */
  public void centerPadNotEquals(String actual, int size, String padStr, String expected) {
    toVerifier(actual).verifyCenterPadNotEquals(verifier, size, padStr, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#center(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the int size of new String, negative treated as zero
   * @param padStr   the String to pad the new String with, must not be null or empty
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void centerPadNotEquals(
      String actual,
      int size,
      String padStr,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyCenterPadNotEquals(verifier, size, padStr, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#compare(String, String)} equals to the expected value.
   *
   * @param actual          value to compare
   * @param stringToCompare the string value to compare against
   * @param expected        the expected result.
   */
  public void compare(String actual, String stringToCompare, int expected) {
    toVerifier(actual).verifyCompare(verifier, stringToCompare, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#compare(String, String)} equals to the expected value.
   *
   * @param actual          value to compare
   * @param stringToCompare the string value to compare against
   * @param expected        the expected result.
   * @param message         information about the propose of this verification.
   * @param params          parameters in case if message is a format {@link String#format}
   */
  public void compare(
      String actual,
      String stringToCompare,
      int expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyCompare(verifier, stringToCompare, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#compareIgnoreCase(String, String)} equals to the
   * expected value.
   *
   * @param actual          value to compare
   * @param stringToCompare the string value to compare against
   * @param expected        the expected result.
   */
  public void compareIgnoreCase(String actual, String stringToCompare, int expected) {
    toVerifier(actual).verifyCompareIgnoreCase(verifier, stringToCompare, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#compareIgnoreCase(String, String)} equals to the
   * expected value.
   *
   * @param actual          value to compare
   * @param stringToCompare the string value to compare against
   * @param expected        the expected result.
   * @param message         information about the propose of this verification.
   * @param params          parameters in case if message is a format {@link String#format}
   */
  public void compareIgnoreCase(
      String actual,
      String stringToCompare,
      int expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyCompareIgnoreCase(verifier, stringToCompare, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is true.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void contains(String actual, String expected) {
    toVerifier(actual).verifyContains(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is true.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void contains(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyContains(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void containsIgnoreCase(String actual, String expected) {
    toVerifier(actual).verifyContainsIgnoreCase(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void containsIgnoreCase(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyContainsIgnoreCase(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is true
   *
   * @param actual value to compare
   * @param suffix the suffix to find, may be {@code null}
   */
  public void endsWith(String actual, String suffix) {
    toVerifier(actual).verifyEndsWith(verifier, suffix);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is true
   *
   * @param actual  value to compare
   * @param suffix  the suffix to find, may be {@code null}
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void endsWith(String actual, String suffix, final String message, final Object... params) {
    toVerifier(actual).verifyEndsWith(verifier, suffix, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is true.
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequences to find, may be empty or contain {@code
   *                     null}
   */
  public void endsWithAny(String actual, List<String> searchInputs) {
    toVerifier(actual).verifyEndsWithAny(verifier, searchInputs);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is true.
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequences to find, may be empty or contain {@code
   *                     null}
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void endsWithAny(
      String actual, List<String> searchInputs, final String message, final Object... params) {
    toVerifier(actual).verifyEndsWithAny(verifier, searchInputs, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param actual value to compare
   * @param suffix the suffix to find, may be {@code null}
   */
  public void endsWithIgnoreCase(String actual, String suffix) {
    toVerifier(actual).verifyEndsWithIgnoreCase(verifier, suffix);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param suffix  the suffix to find, may be {@code null}
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void endsWithIgnoreCase(
      String actual, String suffix, final String message, final Object... params) {
    toVerifier(actual).verifyEndsWithIgnoreCase(verifier, suffix, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is false.
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequences to find, may be empty or contain {@code
   *                     null}
   */
  public void endsWithNone(String actual, List<String> searchInputs) {
    toVerifier(actual).verifyEndsWithNone(verifier, searchInputs);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithAny(CharSequence, CharSequence...)} is false.
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequences to find, may be empty or contain {@code
   *                     null}
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void endsWithNone(
      String actual, List<String> searchInputs, final String message, final Object... params) {
    toVerifier(actual).verifyEndsWithNone(verifier, searchInputs, message, params);
  }

  /**
   * Verify if {@code equals(String)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void equals(String actual, String expected) {
    toVerifier(actual).verifyEquals(verifier, expected);
  }

  /**
   * Verify if {@code equals(String)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equals(String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAny(CharSequence, CharSequence...)} equals to the
   * expected value.
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   */
  public void equalsAny(String actual, List<String> expectedList) {
    toVerifier(actual).verifyEqualsAny(verifier, expectedList);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAny(CharSequence, CharSequence...)} equals to the
   * expected value.
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void equalsAny(
      String actual, List<String> expectedList, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsAny(verifier, expectedList, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * true.
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   */
  public void equalsAnyIgnoreCase(String actual, List<String> expectedList) {
    toVerifier(actual).verifyEqualsAnyIgnoreCase(verifier, expectedList);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * true.
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void equalsAnyIgnoreCase(
      String actual, List<String> expectedList, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsAnyIgnoreCase(verifier, expectedList, message, params);
  }

  /**
   * Verify if {@code equalsIgnoreCase(String)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void equalsIgnoreCase(String actual, String expected) {
    toVerifier(actual).verifyEqualsIgnoreCase(verifier, expected);
  }

  /**
   * Verify if {@code equalsIgnoreCase(String)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equalsIgnoreCase(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsIgnoreCase(verifier, expected, message, params);
  }

  /**
   * Verify if {@code equalsIgnoreWhiteSpace(String)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void equalsIgnoreWhiteSpaces(String actual, String expected) {
    toVerifier(actual).verifyEqualsIgnoreWhiteSpaces(verifier, expected);
  }

  /**
   * Verify if {@code equalsIgnoreWhiteSpace(String)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equalsIgnoreWhiteSpaces(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsIgnoreWhiteSpaces(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAny(CharSequence, CharSequence...)} is false
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   */
  public void equalsNone(String actual, List<String> expectedList) {
    toVerifier(actual).verifyEqualsNone(verifier, expectedList);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAny(CharSequence, CharSequence...)} is false
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void equalsNone(
      String actual, List<String> expectedList, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsNone(verifier, expectedList, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * false.
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   */
  public void equalsNoneIgnoreCase(String actual, List<String> expectedList) {
    toVerifier(actual).verifyEqualsNoneIgnoreCase(verifier, expectedList);
  }

  /**
   * Verify if result of {@link CStringUtil#equalsAnyIgnoreCase(CharSequence, CharSequence...)} is
   * false.
   *
   * @param actual       value to compare
   * @param expectedList a list of strings, may be {@code null}.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void equalsNoneIgnoreCase(
      String actual, List<String> expectedList, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsNoneIgnoreCase(verifier, expectedList, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isAlpha(String actual) {
    toVerifier(actual).verifyIsAlpha(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isAlpha(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsAlpha(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isAlphaSpace(String actual) {
    toVerifier(actual).verifyIsAlphaSpace(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isAlphaSpace(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsAlphaSpace(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isAlphanumeric(String actual) {
    toVerifier(actual).verifyIsAlphanumeric(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isAlphanumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsAlphanumeric(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isAlphanumericSpace(String actual) {
    toVerifier(actual).verifyIsAlphanumericSpace(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isAlphanumericSpace(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsAlphanumericSpace(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isAsciiPrintable(String actual) {
    toVerifier(actual).verifyIsAsciiPrintable(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isAsciiPrintable(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsAsciiPrintable(verifier, message, params);
  }

  /**
   * Verify if String value is blank (Null or Empty)
   *
   * @param actual value to compare
   */
  public void isBlank(String actual) {
    toVerifier(actual).verifyIsBlank(verifier);
  }

  /**
   * Verify if String value is blank (Null or Empty)
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isBlank(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsBlank(verifier, message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param actual value to compare
   */
  public void isBlankOrAlpha(String actual) {
    toVerifier(actual).verifyIsBlankOrAlpha(verifier);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isBlankOrAlpha(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsBlankOrAlpha(verifier, message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isBlankOrAlphanumeric(String actual) {
    toVerifier(actual).verifyIsBlankOrAlphanumeric(verifier);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isBlankOrAlphanumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsBlankOrAlphanumeric(verifier, message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param actual value to compare
   */
  public void isBlankOrNotAlpha(String actual) {
    toVerifier(actual).verifyIsBlankOrNotAlpha(verifier);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isBlankOrNotAlpha(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsBlankOrNotAlpha(verifier, message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isBlankOrNotAlphanumeric(String actual) {
    toVerifier(actual).verifyIsBlankOrNotAlphanumeric(verifier);
  }

  /**
   * Verify if string value is Blank or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isBlankOrNotAlphanumeric(
      String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsBlankOrNotAlphanumeric(verifier, message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param actual value to compare
   */
  public void isBlankOrNotNumeric(String actual) {
    toVerifier(actual).verifyIsBlankOrNotNumeric(verifier);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isBlankOrNotNumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsBlankOrNotNumeric(verifier, message, params);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * true.
   *
   * @param actual value to compare
   */
  public void isBlankOrNumeric(String actual) {
    toVerifier(actual).verifyIsBlankOrNumeric(verifier);
  }

  /**
   * Verify if string value is Blank or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isBlankOrNumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsBlankOrNumeric(verifier, message, params);
  }

  /**
   * Verify if String value is empty
   *
   * @param actual value to compare
   */
  public void isEmpty(String actual) {
    toVerifier(actual).verifyIsEmpty(verifier);
  }

  /**
   * Verify if String value is empty
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isEmpty(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmpty(verifier, message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param actual value to compare
   */
  public void isEmptyOrAlpha(String actual) {
    toVerifier(actual).verifyIsEmptyOrAlpha(verifier);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isEmptyOrAlpha(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmptyOrAlpha(verifier, message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isEmptyOrAlphanumeric(String actual) {
    toVerifier(actual).verifyIsEmptyOrAlphanumeric(verifier);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isEmptyOrAlphanumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmptyOrAlphanumeric(verifier, message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param actual value to compare
   */
  public void isEmptyOrNotAlpha(String actual) {
    toVerifier(actual).verifyIsEmptyOrNotAlpha(verifier);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isAlpha(CharSequence)} is
   * false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isEmptyOrNotAlpha(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmptyOrNotAlpha(verifier, message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isEmptyOrNotAlphanumeric(String actual) {
    toVerifier(actual).verifyIsEmptyOrNotAlphanumeric(verifier);
  }

  /**
   * Verify if string value is empty or the result of {@link
   * CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isEmptyOrNotAlphanumeric(
      String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmptyOrNotAlphanumeric(verifier, message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param actual value to compare
   */
  public void isEmptyOrNotNumeric(String actual) {
    toVerifier(actual).verifyIsEmptyOrNotNumeric(verifier);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isEmptyOrNotNumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmptyOrNotNumeric(verifier, message, params);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * true.
   *
   * @param actual value to compare
   */
  public void isEmptyOrNumeric(String actual) {
    toVerifier(actual).verifyIsEmptyOrNumeric(verifier);
  }

  /**
   * Verify if string value is empty or the result of {@link CStringUtil#isNumeric(CharSequence)} is
   * true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isEmptyOrNumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmptyOrNumeric(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isNotAlpha(String actual) {
    toVerifier(actual).verifyIsNotAlpha(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlpha(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotAlpha(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotAlpha(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isNotAlphaSpace(String actual) {
    toVerifier(actual).verifyIsNotAlphaSpace(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphaSpace(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotAlphaSpace(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotAlphaSpace(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isNotAlphanumeric(String actual) {
    toVerifier(actual).verifyIsNotAlphanumeric(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumeric(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotAlphanumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotAlphanumeric(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isNotAlphanumericSpace(String actual) {
    toVerifier(actual).verifyIsNotAlphanumericSpace(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAlphanumericSpace(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotAlphanumericSpace(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotAlphanumericSpace(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isNotAsciiPrintable(String actual) {
    toVerifier(actual).verifyIsNotAsciiPrintable(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isAsciiPrintable(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotAsciiPrintable(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotAsciiPrintable(verifier, message, params);
  }

  /**
   * Verify if String value is not blank (Null or Empty)
   *
   * @param actual value to compare
   */
  public void isNotBlank(String actual) {
    toVerifier(actual).verifyIsNotBlank(verifier);
  }

  /**
   * Verify if String value is not blank (Null or Empty)
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotBlank(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotBlank(verifier, message, params);
  }

  /**
   * Verify String value is not empty
   *
   * @param actual value to compare
   */
  public void isNotEmpty(String actual) {
    toVerifier(actual).verifyIsNotEmpty(verifier);
  }

  /**
   * Verify String value is not empty
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotEmpty(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotEmpty(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isNotNumeric(String actual) {
    toVerifier(actual).verifyIsNotNumeric(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotNumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotNumeric(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is false.
   *
   * @param actual value to compare
   */
  public void isNotNumericSpace(String actual) {
    toVerifier(actual).verifyIsNotNumericSpace(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is false.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotNumericSpace(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotNumericSpace(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isNumeric(String actual) {
    toVerifier(actual).verifyIsNumeric(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumeric(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNumeric(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNumeric(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is true.
   *
   * @param actual value to compare
   */
  public void isNumericSpace(String actual) {
    toVerifier(actual).verifyIsNumericSpace(verifier);
  }

  /**
   * Verify if result of {@link CStringUtil#isNumericSpace(CharSequence)} is true.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNumericSpace(String actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNumericSpace(verifier, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   */
  public void leftPadEquals(String actual, int size, String padStr, String expected) {
    toVerifier(actual).verifyLeftPadEquals(verifier, size, padStr, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void leftPadEquals(
      String actual,
      int size,
      String padStr,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyLeftPadEquals(verifier, size, padStr, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   */
  public void leftPadNotEquals(String actual, int size, String padStr, String expected) {
    toVerifier(actual).verifyLeftPadNotEquals(verifier, size, padStr, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#leftPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void leftPadNotEquals(
      String actual,
      int size,
      String padStr,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyLeftPadNotEquals(verifier, size, padStr, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   */
  public void leftValueEquals(String actual, int len, String expected) {
    toVerifier(actual).verifyLeftValueEquals(verifier, len, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void leftValueEquals(
      String actual, int len, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyLeftValueEquals(verifier, len, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   */
  public void leftValueNotEquals(String actual, int len, String expected) {
    toVerifier(actual).verifyLeftValueNotEquals(verifier, len, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#left(String, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void leftValueNotEquals(
      String actual, int len, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyLeftValueNotEquals(verifier, len, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void lengthEquals(String actual, int expected) {
    toVerifier(actual).verifyLengthEquals(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void lengthEquals(
      String actual, int expected, final String message, final Object... params) {
    toVerifier(actual).verifyLengthEquals(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void lengthNotEquals(String actual, int expected) {
    toVerifier(actual).verifyLengthNotEquals(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#length(CharSequence)} is NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void lengthNotEquals(
      String actual, int expected, final String message, final Object... params) {
    toVerifier(actual).verifyLengthNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param actual  value to compare
   * @param pattern regular expression pattern
   */
  public void matches(String actual, final Pattern pattern) {
    toVerifier(actual).verifyMatches(verifier, pattern);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param actual  value to compare
   * @param pattern regular expression pattern
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void matches(
      String actual, final Pattern pattern, final String message, final Object... params) {
    toVerifier(actual).verifyMatches(verifier, pattern, message, params);
  }

  /**
   * Verify if String value match any of provided patterns
   *
   * @param actual   value to compare
   * @param patterns regular expression pattern
   */
  public void waitMatchesAny(String actual, final List<Pattern> patterns) {
    toVerifier(actual).waitMatchesAny(verifier, patterns);
  }

  /**
   * Verify if String value match any of provided patterns
   *
   * @param actual   value to compare
   * @param patterns regular expression pattern
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void waitMatchesAny(
      String actual, final List<Pattern> patterns, final String message, final Object... params) {
    toVerifier(actual).waitMatchesAny(verifier, patterns, message, params);
  }

  /**
   * Verify if String value NOT match any of provided patterns
   *
   * @param actual   value to compare
   * @param patterns regular expression pattern
   */
  public void waitMatchesNone(String actual, final List<Pattern> patterns) {
    toVerifier(actual).waitMatchesNone(verifier, patterns);
  }

  /**
   * Verify if String value NOT match any of provided patterns
   *
   * @param actual   value to compare
   * @param patterns regular expression pattern
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void waitMatchesNone(
      String actual, final List<Pattern> patterns, final String message, final Object... params) {
    toVerifier(actual).waitMatchesNone(verifier, patterns, message, params);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param actual  value to compare
   * @param pattern regular expression pattern
   */
  public void matches(String actual, final String pattern) {
    toVerifier(actual).verifyMatches(verifier, pattern);
  }

  /**
   * Verify if String value match provided pattern
   *
   * @param actual  value to compare
   * @param pattern regular expression pattern
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void matches(
      String actual, final String pattern, final String message, final Object... params) {
    toVerifier(actual).verifyMatches(verifier, pattern, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param pos      the position to start from, negative treated as zero
   * @param len      the length of the required String
   */
  public void midValueEquals(String actual, int pos, int len, String expected) {
    toVerifier(actual).verifyMidValueEquals(verifier, pos, len, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param pos      the position to start from, negative treated as zero
   * @param len      the length of the required String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void midValueEquals(
      String actual,
      int pos,
      int len,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyMidValueEquals(verifier, pos, len, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param pos      the position to start from, negative treated as zero
   * @param len      the length of the required String
   */
  public void midValueNotEquals(String actual, int pos, int len, String expected) {
    toVerifier(actual).verifyMidValueNotEquals(verifier, pos, len, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#mid(String, int, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param pos      the position to start from, negative treated as zero
   * @param len      the length of the required String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void midValueNotEquals(
      String actual,
      int pos,
      int len,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyMidValueNotEquals(verifier, pos, len, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is false.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void notContains(String actual, String expected) {
    toVerifier(actual).verifyNotContains(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#contains(CharSequence, CharSequence)} is false.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notContains(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotContains(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void notContainsIgnoreCase(String actual, String expected) {
    toVerifier(actual).verifyNotContainsIgnoreCase(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#containsIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notContainsIgnoreCase(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotContainsIgnoreCase(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is false
   *
   * @param actual value to compare
   * @param suffix the suffix to find, may be {@code null}
   */
  public void notEndsWith(String actual, String suffix) {
    toVerifier(actual).verifyNotEndsWith(verifier, suffix);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWith(CharSequence, CharSequence)} is false
   *
   * @param actual  value to compare
   * @param suffix  the suffix to find, may be {@code null}
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void notEndsWith(
      String actual, String suffix, final String message, final Object... params) {
    toVerifier(actual).verifyNotEndsWith(verifier, suffix, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param actual value to compare
   * @param suffix the suffix to find, may be {@code null}
   */
  public void notEndsWithIgnoreCase(String actual, String suffix) {
    toVerifier(actual).verifyNotEndsWithIgnoreCase(verifier, suffix);
  }

  /**
   * Verify if result of {@link CStringUtil#endsWithIgnoreCase(CharSequence, CharSequence)} is
   * false.
   *
   * @param actual  value to compare
   * @param suffix  the suffix to find, may be {@code null}
   * @param message information about the propose of this verification.
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void notEndsWithIgnoreCase(
      String actual, String suffix, final String message, final Object... params) {
    toVerifier(actual).verifyNotEndsWithIgnoreCase(verifier, suffix, message, params);
  }

  /**
   * Verify if {@code equals(String)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void notEquals(String actual, String expected) {
    toVerifier(actual).verifyNotEquals(verifier, expected);
  }

  /**
   * Verify if {@code equals(String)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify if {@code equalsIgnoreCase(String)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void notEqualsIgnoreCase(String actual, String expected) {
    toVerifier(actual).verifyNotEqualsIgnoreCase(verifier, expected);
  }

  /**
   * Verify if {@code equalsIgnoreCase(String)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEqualsIgnoreCase(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEqualsIgnoreCase(verifier, expected, message, params);
  }

  /**
   * Verify if {@code equalsIgnoreWhiteSpace(String)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void notEqualsIgnoreWhiteSpaces(String actual, String expected) {
    toVerifier(actual).verifyNotEqualsIgnoreWhiteSpaces(verifier, expected);
  }

  /**
   * Verify if {@code equalsIgnoreWhiteSpace(String)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEqualsIgnoreWhiteSpaces(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEqualsIgnoreWhiteSpaces(verifier, expected, message, params);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param actual  value to compare
   * @param pattern regular expression pattern
   */
  public void notMatches(String actual, final Pattern pattern) {
    toVerifier(actual).verifyNotMatches(verifier, pattern);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param pattern regular expression pattern
   */
  public void notMatches(
      String actual, final Pattern pattern, final String message, final Object... params) {
    toVerifier(actual).verifyNotMatches(verifier, pattern, message, params);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param actual  value to compare
   * @param pattern regular expression pattern
   */
  public void notMatches(String actual, final String pattern) {
    toVerifier(actual).verifyNotMatches(verifier, pattern);
  }

  /**
   * Verify if String value does not match provided pattern
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification.
   * @param pattern regular expression pattern
   */
  public void notMatches(
      String actual, final String pattern, final String message, final Object... params) {
    toVerifier(actual).verifyNotMatches(verifier, pattern, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is false
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notStartsWith(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotStartsWith(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is false
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void notStartsWith(String actual, String expected) {
    toVerifier(actual).verifyNotStartsWith(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * false
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notStartsWithIgnoreCase(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotStartsWithIgnoreCase(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * false
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void notStartsWithIgnoreCase(String actual, String expected) {
    toVerifier(actual).verifyNotStartsWithIgnoreCase(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is equals to
   * expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param subString the substring to count, may be null
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void numberOfMatchesEquals(
      String actual, String subString, int expected, final String message, final Object... params) {
    toVerifier(actual).verifyNumberOfMatchesEquals(verifier, subString, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is equals to
   * expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param subString the substring to count, may be null
   */
  public void numberOfMatchesEquals(String actual, String subString, int expected) {
    toVerifier(actual).verifyNumberOfMatchesEquals(verifier, subString, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is NOT equals
   * to expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param subString the substring to count, may be null
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void numberOfMatchesNotEquals(
      String actual, String subString, int expected, final String message, final Object... params) {
    toVerifier(actual)
        .verifyNumberOfMatchesNotEquals(verifier, subString, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#countMatches(CharSequence, CharSequence)} is NOT equals
   * to expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param subString the substring to count, may be null
   */
  public void numberOfMatchesNotEquals(String actual, String subString, int expected) {
    toVerifier(actual).verifyNumberOfMatchesNotEquals(verifier, subString, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeEndEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveEndEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   */
  public void removeEndEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveEndEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeEndIgnoreCaseEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveEndIgnoreCaseEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   */
  public void removeEndIgnoreCaseEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveEndIgnoreCaseEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeEndIgnoreCaseNotEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual)
        .verifyRemoveEndIgnoreCaseNotEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEndIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   */
  public void removeEndIgnoreCaseNotEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveEndIgnoreCaseNotEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeEndNotEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveEndNotEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeEnd(String, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   */
  public void removeEndNotEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveEndNotEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   */
  public void removeEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeIgnoreCaseEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveIgnoreCaseEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   */
  public void removeIgnoreCaseEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveIgnoreCaseEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeIgnoreCaseNotEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveIgnoreCaseNotEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   */
  public void removeIgnoreCaseNotEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveIgnoreCaseNotEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeNotEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveNotEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#remove(String, String)} is NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   */
  public void removeNotEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveNotEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeStartEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveStartEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   */
  public void removeStartEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveStartEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeStartIgnoreCaseEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual)
        .verifyRemoveStartIgnoreCaseEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   */
  public void removeStartIgnoreCaseEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveStartIgnoreCaseEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeStartIgnoreCaseNotEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual)
        .verifyRemoveStartIgnoreCaseNotEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStartIgnoreCase(String, String)} is NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for (case insensitive) and remove, may be null
   * @param expected the expected result.
   */
  public void removeStartIgnoreCaseNotEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveStartIgnoreCaseNotEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void removeStartNotEquals(
      String actual, String remove, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRemoveStartNotEquals(verifier, remove, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#removeStart(String, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param remove   the String to search for and remove, may be null
   * @param expected the expected result.
   */
  public void removeStartNotEquals(String actual, String remove, String expected) {
    toVerifier(actual).verifyRemoveStartNotEquals(verifier, remove, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is equals to expected
   * value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceEquals(verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is equals to expected
   * value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   */
  public void replaceEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual).verifyReplaceEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceIgnoreCaseEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceIgnoreCaseEquals(
            verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   */
  public void replaceIgnoreCaseEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual).verifyReplaceIgnoreCaseEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is NOT equals
   * to expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceIgnoreCaseNotEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceIgnoreCaseNotEquals(
            verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceIgnoreCase(String, String, String)} is NOT equals
   * to expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   */
  public void replaceIgnoreCaseNotEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual)
        .verifyReplaceIgnoreCaseNotEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceNotEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceNotEquals(verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replace(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param expected     the expected result.
   */
  public void replaceNotEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual).verifyReplaceNotEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceOnceEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceOnceEquals(verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   */
  public void replaceOnceEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual).verifyReplaceOnceEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is equals
   * to expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceOnceIgnoreCaseEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceOnceIgnoreCaseEquals(
            verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is equals
   * to expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   */
  public void replaceOnceIgnoreCaseEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual)
        .verifyReplaceOnceIgnoreCaseEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is NOT
   * equals to expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceOnceIgnoreCaseNotEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceOnceIgnoreCaseNotEquals(
            verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnceIgnoreCase(String, String, String)} is NOT
   * equals to expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   */
  public void replaceOnceIgnoreCaseNotEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual)
        .verifyReplaceOnceIgnoreCaseNotEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void replaceOnceNotEquals(
      String actual,
      String searchString,
      String replacement,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyReplaceOnceNotEquals(verifier, searchString, replacement, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#replaceOnce(String, String, String)} is NOT equals to
   * expected value.
   *
   * @param actual       value to compare
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace with, may be null
   * @param expected     the expected result.
   */
  public void replaceOnceNotEquals(
      String actual, String searchString, String replacement, String expected) {
    toVerifier(actual).verifyReplaceOnceNotEquals(verifier, searchString, replacement, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void reverseEquals(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyReverseEquals(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void reverseEquals(String actual, String expected) {
    toVerifier(actual).verifyReverseEquals(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void reverseNotEquals(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyReverseNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#reverse(String)} is NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void reverseNotEquals(String actual, String expected) {
    toVerifier(actual).verifyReverseNotEquals(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void rightPadEquals(
      String actual,
      int size,
      String padStr,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyRightPadEquals(verifier, size, padStr, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   */
  public void rightPadEquals(String actual, int size, String padStr, String expected) {
    toVerifier(actual).verifyRightPadEquals(verifier, size, padStr, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void rightPadNotEquals(
      String actual,
      int size,
      String padStr,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyRightPadNotEquals(verifier, size, padStr, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#rightPad(String, int, String)} is NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param size     the size to pad to
   * @param padStr   the String to pad with, null or empty treated as single space
   * @param expected the expected result.
   */
  public void rightPadNotEquals(String actual, int size, String padStr, String expected) {
    toVerifier(actual).verifyRightPadNotEquals(verifier, size, padStr, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void rightValueEquals(
      String actual, int len, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRightValueEquals(verifier, len, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   */
  public void rightValueEquals(String actual, int len, String expected) {
    toVerifier(actual).verifyRightValueEquals(verifier, len, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void rightValueNotEquals(
      String actual, int len, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyRightValueNotEquals(verifier, len, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#right(String, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param len      the length of the required String
   */
  public void rightValueNotEquals(String actual, int len, String expected) {
    toVerifier(actual).verifyRightValueNotEquals(verifier, len, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is true
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void startsWith(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyStartsWith(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWith(CharSequence, CharSequence)} is true
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void startsWith(String actual, String expected) {
    toVerifier(actual).verifyStartsWith(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is true
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                     null}
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void startsWithAny(
      String actual, List<String> searchInputs, final String message, final Object... params) {
    toVerifier(actual).verifyStartsWithAny(verifier, searchInputs, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is true
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                     null}
   */
  public void startsWithAny(String actual, List<String> searchInputs) {
    toVerifier(actual).verifyStartsWithAny(verifier, searchInputs);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * true
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void startsWithIgnoreCase(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyStartsWithIgnoreCase(verifier, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithIgnoreCase(CharSequence, CharSequence)} is
   * true
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void startsWithIgnoreCase(String actual, String expected) {
    toVerifier(actual).verifyStartsWithIgnoreCase(verifier, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is false
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                     null}
   * @param message      information about the propose of this verification.
   * @param params       parameters in case if message is a format {@link String#format}
   */
  public void startsWithNone(
      String actual, List<String> searchInputs, final String message, final Object... params) {
    toVerifier(actual).verifyStartsWithNone(verifier, searchInputs, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#startsWithAny(CharSequence, CharSequence...)} is false
   *
   * @param actual       value to compare
   * @param searchInputs the case-sensitive CharSequence prefixes, may be empty or contain {@code
   *                     null}
   */
  public void startsWithNone(String actual, List<String> searchInputs) {
    toVerifier(actual).verifyStartsWithNone(verifier, searchInputs);
  }

  /**
   * Verify if {@code stripEnd(String)} value equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @param message    information about the propose of this verification.
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void stripedEndValue(
      String actual,
      String stripChars,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyStripedEndValue(verifier, stripChars, expected, message, params);
  }

  /**
   * Verify if {@code stripEnd(String)} value equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   */
  public void stripedEndValue(String actual, String stripChars, String expected) {
    toVerifier(actual).verifyStripedEndValue(verifier, stripChars, expected);
  }

  /**
   * Verify if {@code stripEnd(String)} value NOT equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @param message    information about the propose of this verification.
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void stripedEndValueNot(
      String actual,
      String stripChars,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyStripedEndValueNot(verifier, stripChars, expected, message, params);
  }

  /**
   * Verify if {@code stripEnd(String)} value NOT equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   */
  public void stripedEndValueNot(String actual, String stripChars, String expected) {
    toVerifier(actual).verifyStripedEndValueNot(verifier, stripChars, expected);
  }

  /**
   * Verify if {@code stripStart(String)} value equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @param message    information about the propose of this verification.
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void stripedStartValue(
      String actual,
      String stripChars,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyStripedStartValue(verifier, stripChars, expected, message, params);
  }

  /**
   * Verify if {@code stripStart(String)} value equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   */
  public void stripedStartValue(String actual, String stripChars, String expected) {
    toVerifier(actual).verifyStripedStartValue(verifier, stripChars, expected);
  }

  /**
   * Verify if {@code stripStart(String)} value NOT equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @param message    information about the propose of this verification.
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void stripedStartValueNot(
      String actual,
      String stripChars,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyStripedStartValueNot(verifier, stripChars, expected, message, params);
  }

  /**
   * Verify if {@code stripStart(String)} value NOT equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   */
  public void stripedStartValueNot(String actual, String stripChars, String expected) {
    toVerifier(actual).verifyStripedStartValueNot(verifier, stripChars, expected);
  }

  /**
   * Verify if {@code strip(String)} value equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @param message    information about the propose of this verification.
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void stripedValue(
      String actual,
      String stripChars,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyStripedValue(verifier, stripChars, expected, message, params);
  }

  /**
   * Verify if {@code strip(String)} value equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   */
  public void stripedValue(String actual, String stripChars, String expected) {
    toVerifier(actual).verifyStripedValue(verifier, stripChars, expected);
  }

  /**
   * Verify if {@code strip(String)} value NOT equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   * @param message    information about the propose of this verification.
   * @param params     parameters in case if message is a format {@link String#format}
   */
  public void stripedValueNot(
      String actual,
      String stripChars,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyStripedValueNot(verifier, stripChars, expected, message, params);
  }

  /**
   * Verify if {@code strip(String)} value NOT equals the expected value.
   *
   * @param actual     value to compare
   * @param stripChars the characters to remove, null treated as whitespace
   * @param expected   the expected result.
   */
  public void stripedValueNot(String actual, String stripChars, String expected) {
    toVerifier(actual).verifyStripedValueNot(verifier, stripChars, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringAfterEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifySubstringAfterEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringAfterEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringAfterEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringAfterLastEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringAfterLastEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringAfterLastEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringAfterLastEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} NOT equals to
   * expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringAfterLastNotEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringAfterLastNotEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfterLast(String, String)} NOT equals to
   * expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringAfterLastNotEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringAfterLastNotEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} NOT equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringAfterNotEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringAfterNotEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringAfter(String, String)} NOT equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringAfterNotEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringAfterNotEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringBeforeEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifySubstringBeforeEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringBeforeEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringBeforeEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringBeforeLastEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringBeforeLastEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringBeforeLastEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringBeforeLastEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} NOT equals to
   * expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringBeforeLastNotEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringBeforeLastNotEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBeforeLast(String, String)} NOT equals to
   * expected value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringBeforeLastNotEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringBeforeLastNotEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} NOT equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   * @param message   information about the propose of this verification.
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void substringBeforeNotEquals(
      String actual,
      String separator,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringBeforeNotEquals(verifier, separator, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBefore(String, String)} NOT equals to expected
   * value.
   *
   * @param actual    value to compare
   * @param expected  the expected result.
   * @param separator the String to search for, may be {@code null}
   */
  public void substringBeforeNotEquals(String actual, String separator, String expected) {
    toVerifier(actual).verifySubstringBeforeNotEquals(verifier, separator, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String, String)} equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringBetweenEquals(
      String actual,
      String open,
      String close,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringBetweenEquals(verifier, open, close, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String, String)} equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   */
  public void substringBetweenEquals(String actual, String open, String close, String expected) {
    toVerifier(actual).verifySubstringBetweenEquals(verifier, open, close, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringBetweenNotEquals(
      String actual,
      String open,
      String close,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringBetweenNotEquals(verifier, open, close, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   */
  public void substringBetweenNotEquals(String actual, String open, String close, String expected) {
    toVerifier(actual).verifySubstringBetweenNotEquals(verifier, open, close, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringEquals(
      String actual, int start, String expected, final String message, final Object... params) {
    toVerifier(actual).verifySubstringEquals(verifier, start, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   */
  public void substringEquals(String actual, int start, String expected) {
    toVerifier(actual).verifySubstringEquals(verifier, start, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param end      the position to end at (exclusive), negative means count back from the end of the
   *                 String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringEquals(
      String actual,
      int start,
      int end,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifySubstringEquals(verifier, start, end, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param end      the position to end at (exclusive), negative means count back from the end of the
   *                 String
   */
  public void substringEquals(String actual, int start, int end, String expected) {
    toVerifier(actual).verifySubstringEquals(verifier, start, end, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringNotEquals(
      String actual, int start, String expected, final String message, final Object... params) {
    toVerifier(actual).verifySubstringNotEquals(verifier, start, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int)} NOT equals to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   */
  public void substringNotEquals(String actual, int start, String expected) {
    toVerifier(actual).verifySubstringNotEquals(verifier, start, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param end      the position to end at (exclusive), negative means count back from the end of the
   *                 String
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringNotEquals(
      String actual,
      int start,
      int end,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual).verifySubstringNotEquals(verifier, start, end, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substring(String, int, int)} NOT equals to expected
   * value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param start    the position to start from, negative means count back from the end of the String
   * @param end      the position to end at (exclusive), negative means count back from the end of the
   *                 String
   */
  public void substringNotEquals(String actual, int start, int end, String expected) {
    toVerifier(actual).verifySubstringNotEquals(verifier, start, end, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} contains to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringsBetweenContains(
      String actual,
      String open,
      String close,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringsBetweenContains(verifier, open, close, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} contains to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   */
  public void substringsBetweenContains(String actual, String open, String close, String expected) {
    toVerifier(actual).verifySubstringsBetweenContains(verifier, open, close, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringsBetweenEquals(
      String actual,
      String open,
      String close,
      List<String> expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringsBetweenEquals(verifier, open, close, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   */
  public void substringsBetweenEquals(
      String actual, String open, String close, List<String> expected) {
    toVerifier(actual).verifySubstringsBetweenEquals(verifier, open, close, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT contains
   * to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringsBetweenNotContains(
      String actual,
      String open,
      String close,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringsBetweenNotContains(verifier, open, close, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT contains
   * to expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   */
  public void substringsBetweenNotContains(
      String actual, String open, String close, String expected) {
    toVerifier(actual).verifySubstringsBetweenNotContains(verifier, open, close, expected);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void substringsBetweenNotEquals(
      String actual,
      String open,
      String close,
      List<String> expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifySubstringsBetweenNotEquals(verifier, open, close, expected, message, params);
  }

  /**
   * Verify if result of {@link CStringUtil#substringsBetween(String, String, String)} NOT equals to
   * expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param open     the String identifying the start of the substring, empty returns null
   * @param close    the String identifying the end of the substring, empty returns null
   */
  public void substringsBetweenNotEquals(
      String actual, String open, String close, List<String> expected) {
    toVerifier(actual).verifySubstringsBetweenNotEquals(verifier, open, close, expected);
  }

  /**
   * Verify if {@code trim()} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void trimmedValue(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyTrimmedValueEquals(verifier, expected, message, params);
  }

  /**
   * Verify if {@code trim()} value equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void trimmedValue(String actual, String expected) {
    toVerifier(actual).verifyTrimmedValueEquals(verifier, expected);
  }

  /**
   * Verify if {@code trim()} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void trimmedValueNot(
      String actual, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyTrimmedValueNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify if {@code trim()} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param expected the expected result.
   */
  public void trimmedValueNot(String actual, String expected) {
    toVerifier(actual).verifyTrimmedValueNotEquals(verifier, expected);
  }

  /**
   * Verify if {@code truncate(Int)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void truncatedValue(
      String actual, int maxWidth, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyTruncatedValueEquals(verifier, maxWidth, expected, message, params);
  }

  /**
   * Verify if {@code truncate(Int)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   */
  public void truncatedValue(String actual, int maxWidth, String expected) {
    toVerifier(actual).verifyTruncatedValueEquals(verifier, maxWidth, expected);
  }

  /**
   * Verify if {@code truncate(Int, Int)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param offset   left edge of string to start truncate from
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void truncatedValue(
      String actual,
      int offset,
      int maxWidth,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyTruncatedValueEquals(verifier, offset, maxWidth, expected, message, params);
  }

  /**
   * Verify if {@code truncate(Int, Int)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param offset   left edge of string to start truncate from
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   */
  public void truncatedValue(String actual, int offset, int maxWidth, String expected) {
    toVerifier(actual).verifyTruncatedValueEquals(verifier, offset, maxWidth, expected);
  }

  /**
   * Verify if {@code truncate(Int)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void truncatedValueNot(
      String actual, int maxWidth, String expected, final String message, final Object... params) {
    toVerifier(actual).verifyTruncatedValueNotEquals(verifier, maxWidth, expected, message, params);
  }

  /**
   * Verify if {@code truncate(Int)} value equals the expected value.
   *
   * @param actual   value to compare
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   */
  public void truncatedValueNot(String actual, int maxWidth, String expected) {
    toVerifier(actual).verifyTruncatedValueNotEquals(verifier, maxWidth, expected);
  }

  /**
   * Verify if {@code truncate(Int, Int)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param offset   left edge of string to start truncate from
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   * @param message  information about the propose of this verification.
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void truncatedValueNot(
      String actual,
      int offset,
      int maxWidth,
      String expected,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyTruncatedValueNotEquals(verifier, offset, maxWidth, expected, message, params);
  }

  /**
   * Verify if {@code truncate(Int, Int)} value NOT equals the expected value.
   *
   * @param actual   value to compare
   * @param offset   left edge of string to start truncate from
   * @param maxWidth maximum length of truncated string, must be positive
   * @param expected the expected result.
   */
  public void truncatedValueNot(String actual, int offset, int maxWidth, String expected) {
    toVerifier(actual).verifyTruncatedValueNotEquals(verifier, offset, maxWidth, expected);
  }

  private CStringVerifier toVerifier(String actual) {
    return new CStringVerifier() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public String get() {
        return actual;
      }
    };
  }
}

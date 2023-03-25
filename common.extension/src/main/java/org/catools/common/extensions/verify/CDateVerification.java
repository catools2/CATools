package org.catools.common.extensions.verify;

import org.catools.common.date.CDate;
import org.catools.common.extensions.verify.interfaces.CDateVerifier;

import java.util.Date;

/**
 * Date verification class contains all verification method which is related to Date
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CDateVerification<T extends CVerificationBuilder> extends CBaseVerification<T> {

  public CDateVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify that actual and expected have the exact same date value (compare to milliseconds) or
   * they both has null value
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void equals(final Date actual, final Date expected) {
    toVerifier(actual).verifyEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected have the exact same date value (compare to milliseconds) or
   * they both has null value
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equals(
      final Date actual, final Date expected, final String message, final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected have the exact same string value after they converted using the
   * provided date format. Means that verification of "2019-08-09 12:20" and "2019-08-09 11:20"
   * using "yyyy-MM-dd" passes.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param format   date format to be use
   */
  public void equalsByFormat(final Date actual, final Date expected, final String format) {
    toVerifier(actual).verifyEqualsByFormat(verifier, expected, format);
  }

  /**
   * Verify that actual and expected have the exact same string value after they converted using the
   * provided date format. Means that verification of "2019-08-09 12:20" and "2019-08-09 11:20"
   * using "yyyy-MM-dd" passes.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param format   date format to be use
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equalsByFormat(
      final Date actual,
      final Date expected,
      final String format,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyEqualsByFormat(verifier, expected, format, message, params);
  }

  /**
   * Verify that actual and expected have same string value after they converted using "yyyy-MM-dd"
   * for format. Means that verification of "2019-08-09 12:20" and "2019-08-09 11:20" passes
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void equalsDatePortion(final Date actual, final Date expected) {
    toVerifier(actual).verifyEqualsDatePortion(verifier, expected);
  }

  /**
   * Verify that actual and expected have same string value after they converted using "yyyy-MM-dd"
   * for format. Means that verification of "2019-08-09 12:20" and "2019-08-09 11:20" passes
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equalsDatePortion(
      final Date actual, final Date expected, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsDatePortion(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected have same string value after they converted using "HH:mm:ss"
   * for format. Means that verification of "2019-08-09 12:20" and "2019-08-08 12:20" passes
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void equalsTimePortion(final Date actual, final Date expected) {
    toVerifier(actual).verifyEqualsTimePortion(verifier, expected);
  }

  /**
   * Verify that actual and expected have same string value after they converted using "HH:mm:ss"
   * for format. Means that verification of "2019-08-09 12:20" and "2019-08-08 12:20" passes
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equalsTimePortion(
      final Date actual, final Date expected, final String message, final Object... params) {
    toVerifier(actual).verifyEqualsTimePortion(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected does not have the exact same date value (compare to
   * milliseconds) or one of them is null
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void notEquals(final Date actual, final Date expected) {
    toVerifier(actual).verifyNotEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected does not have the exact same date value (compare to
   * milliseconds) or one of them is null
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      final Date actual, final Date expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected have different string value after they converted using the
   * provided date format. Means that verification of "2019-08-09 12:20" and "2019-08-09 11:20"
   * using "yyyy-MM-dd HH" passes (means values are different)
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param format   date format to be use
   */
  public void notEqualsByFormat(final Date actual, final Date expected, final String format) {
    toVerifier(actual).verifyNotEqualsByFormat(verifier, expected, format);
  }

  /**
   * Verify that actual and expected have different string value after they converted using the
   * provided date format. Means that verification of "2019-08-09 12:20" and "2019-08-09 11:20"
   * using "yyyy-MM-dd HH" passes (means values are different)
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param format   date format to be use
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEqualsByFormat(
      final Date actual,
      final Date expected,
      final String format,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyNotEqualsByFormat(verifier, expected, format, message, params);
  }

  /**
   * Verify that actual and expected have different string value after they converted using
   * "yyyy-MM-dd" for format. Means that verification of "2019-08-09 12:20" and "2019-08-08 12:20"
   * passes (means values are different)
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void notEqualsDatePortion(final Date actual, final Date expected) {
    toVerifier(actual).verifyNotEqualsDatePortion(verifier, expected);
  }

  /**
   * Verify that actual and expected have different string value after they converted using
   * "yyyy-MM-dd" for format. Means that verification of "2019-08-09 12:20" and "2019-08-08 12:20"
   * passes (means values are different)
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEqualsDatePortion(
      final Date actual, final Date expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEqualsDatePortion(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected have different string value after they converted using
   * "HH:mm:ss" for format. Means that verification of "2019-08-09 12:20:31" and "2019-08-09
   * 12:20:30" passes (means values are different)
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void notEqualsTimePortion(final Date actual, final Date expected) {
    toVerifier(actual).verifyNotEqualsTimePortion(verifier, expected);
  }

  /**
   * Verify that actual and expected have different string value after they converted using
   * "HH:mm:ss" for format. Means that verification of "2019-08-09 12:20:31" and "2019-08-09
   * 12:20:30" passes (means values are different)
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEqualsTimePortion(
      final Date actual, final Date expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEqualsTimePortion(verifier, expected, message, params);
  }

  private CDateVerifier toVerifier(Date actual) {
    return new CDateVerifier() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public Date get() {
        return actual == null ? null : new CDate(actual);
      }
    };
  }
}

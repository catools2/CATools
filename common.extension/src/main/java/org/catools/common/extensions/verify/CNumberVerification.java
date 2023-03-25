package org.catools.common.extensions.verify;

import org.catools.common.extensions.verify.interfaces.CNumberVerifier;

/**
 * Number verification class contains all verification method which is related to Number
 * (Int,Double,Long,Float,BigDecimal)
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CNumberVerification<T extends CVerificationBuilder, N extends Number & Comparable<N>>
    extends CBaseVerification<T> {

  public CNumberVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   */
  public void betweenExclusive(final N actual, final N lowerBound, final N higherBound) {
    toVerifier(actual).verifyBetweenExclusive(verifier, lowerBound, higherBound);
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   * @param message     information about the propose of this verification
   * @param params      parameters in case if message is a format {@link String#format}
   */
  public void betweenExclusive(
      final N actual,
      final N lowerBound,
      final N higherBound,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyBetweenExclusive(verifier, lowerBound, higherBound, message, params);
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   */
  public void betweenInclusive(final N actual, final N lowerBound, final N higherBound) {
    toVerifier(actual).verifyBetweenInclusive(verifier, lowerBound, higherBound);
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   * @param message     information about the propose of this verification
   * @param params      parameters in case if message is a format {@link String#format}
   */
  public void betweenInclusive(
      final N actual,
      final N lowerBound,
      final N higherBound,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyBetweenInclusive(verifier, lowerBound, higherBound, message, params);
  }

  /**
   * Verify that actual and expected have the exact same value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void equals(final N actual, final N expected) {
    toVerifier(actual).verifyEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected have the exact same value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equals(
      final N actual, final N expected, final String message, final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected have the exact same value or their difference is less than
   * precision value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual    value to compare
   * @param expected  value to compare
   * @param precision the acceptable precision
   */
  public void equalsP(final N actual, final N expected, final N precision) {
    toVerifier(actual).verifyEqualsP(verifier, expected, precision);
  }

  /**
   * Verify that actual and expected have the exact same value or their difference is less than
   * precision value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param actual    value to compare
   * @param expected  value to compare
   * @param precision the acceptable precision
   * @param message   information about the propose of this verification
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void equalsP(
      final N actual,
      final N expected,
      final N precision,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyEqualsP(verifier, expected, precision, message, params);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void greater(final N actual, final N expected) {
    toVerifier(actual).verifyGreater(verifier, expected);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void greater(
      final N actual, final N expected, final String message, final Object... params) {
    toVerifier(actual).verifyGreater(verifier, expected, message, params);
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void greaterOrEqual(final N actual, final N expected) {
    toVerifier(actual).verifyGreaterOrEqual(verifier, expected);
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void greaterOrEqual(
      final N actual, final N expected, final String message, final Object... params) {
    toVerifier(actual).verifyGreaterOrEqual(verifier, expected, message, params);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void less(final N actual, final N expected) {
    toVerifier(actual).verifyLess(verifier, expected);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void less(final N actual, final N expected, final String message, final Object... params) {
    toVerifier(actual).verifyLess(verifier, expected, message, params);
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void lessOrEqual(final N actual, final N expected) {
    toVerifier(actual).verifyLessOrEqual(verifier, expected);
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void lessOrEqual(
      final N actual, final N expected, final String message, final Object... params) {
    toVerifier(actual).verifyLessOrEqual(verifier, expected, message, params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   */
  public void notBetweenExclusive(final N actual, final N lowerBound, final N higherBound) {
    toVerifier(actual).verifyNotBetweenExclusive(verifier, lowerBound, higherBound);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   * @param message     information about the propose of this verification
   * @param params      parameters in case if message is a format {@link String#format}
   */
  public void notBetweenExclusive(
      final N actual,
      final N lowerBound,
      final N higherBound,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyNotBetweenExclusive(verifier, lowerBound, higherBound, message, params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   */
  public void notBetweenInclusive(final N actual, final N lowerBound, final N higherBound) {
    toVerifier(actual).verifyNotBetweenInclusive(verifier, lowerBound, higherBound);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param actual      value to compare
   * @param lowerBound  lower bound inclusive
   * @param higherBound higher bound inclusive
   * @param message     information about the propose of this verification
   * @param params      parameters in case if message is a format {@link String#format}
   */
  public void notBetweenInclusive(
      final N actual,
      final N lowerBound,
      final N higherBound,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyNotBetweenInclusive(verifier, lowerBound, higherBound, message, params);
  }

  /**
   * Verify that actual and expected have different values.
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void notEquals(final N actual, final N expected) {
    toVerifier(actual).verifyNotEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected have different values.
   *
   * <p>Please note that verification consider as passe if one of value is null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      final N actual, final N expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected have different value greater than precision value.
   *
   * <p>Please note that verification consider as passe if one value is null
   *
   * @param actual    value to compare
   * @param expected  value to compare
   * @param precision the acceptable precision
   */
  public void notEqualsP(final N actual, final N expected, final N precision) {
    toVerifier(actual).verifyNotEqualsP(verifier, expected, precision);
  }

  /**
   * Verify that actual and expected have different value greater than precision value.
   *
   * <p>Please note that verification consider as passe if one value is null
   *
   * @param actual    value to compare
   * @param expected  value to compare
   * @param precision the acceptable precision
   * @param message   information about the propose of this verification
   * @param params    parameters in case if message is a format {@link String#format}
   */
  public void notEqualsP(
      final N actual,
      final N expected,
      final N precision,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyNotEqualsP(verifier, expected, precision, message, params);
  }

  private CNumberVerifier<N> toVerifier(Number actual) {
    return new CNumberVerifier<>() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public N get() {
        return (N) actual;
      }
    };
  }
}

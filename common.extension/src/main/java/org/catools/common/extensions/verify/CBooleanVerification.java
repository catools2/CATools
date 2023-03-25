package org.catools.common.extensions.verify;

import org.catools.common.extensions.verify.interfaces.CBooleanVerifier;

/**
 * Boolean verification class contains all verification method which is related to Boolean
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CBooleanVerification<T extends CVerificationBuilder> extends CBaseVerification<T> {

  CBooleanVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify that actual and expected have same boolean value or be null
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void equals(final Boolean actual, final Boolean expected) {
    toVerifier(actual).verifyEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected have same boolean value or be null
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equals(
      final Boolean actual, final Boolean expected, final String message, final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual value is false
   *
   * @param actual value to compare
   */
  public void isFalse(Boolean actual) {
    toVerifier(actual).verifyIsFalse(verifier);
  }

  /**
   * Verify that actual value is false
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isFalse(Boolean actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsFalse(verifier, message, params);
  }

  /**
   * Verify that actual value is true
   *
   * @param actual value to compare
   */
  public void isTrue(Boolean actual) {
    toVerifier(actual).verifyIsTrue(verifier);
  }

  /**
   * Verify that actual value is true
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isTrue(Boolean actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsTrue(verifier, message, params);
  }

  /**
   * Verify that actual and expected has different boolean value
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void notEquals(final Boolean actual, final Boolean expected) {
    toVerifier(actual).verifyNotEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected has different boolean value
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      final Boolean actual, final Boolean expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  private CBooleanVerifier toVerifier(Boolean actual) {
    return new CBooleanVerifier() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public Boolean get() {
        return actual;
      }
    };
  }
}

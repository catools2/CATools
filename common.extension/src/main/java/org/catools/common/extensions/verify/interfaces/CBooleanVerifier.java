package org.catools.common.extensions.verify.interfaces;

import org.catools.common.extensions.states.interfaces.CBooleanState;
import org.catools.common.extensions.verify.CVerificationQueue;

/**
 * CBooleanVerifier is an interface for Boolean verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 */
public interface CBooleanVerifier extends CObjectVerifier<Boolean, CBooleanState> {

  default CBooleanState _toState(Object e) {
    return () -> (Boolean) e;
  }

  /**
   * Verify that actual value is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsFalse(CVerificationQueue verificationQueue) {
    verifyIsFalse(verificationQueue, getDefaultMessage("Is False"));
  }

  /**
   * Verify that actual value is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsFalse(
      CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(verificationQueue, false, false, (a, b) -> _toState(a).isFalse(), message, params);
  }

  /**
   * Verify that actual value is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsTrue(CVerificationQueue verificationQueue) {
    verifyIsTrue(verificationQueue, getDefaultMessage("Is True"));
  }

  /**
   * Verify that actual value is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsTrue(
      CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(verificationQueue, true, false, (a, b) -> _toState(a).isTrue(), message, params);
  }

  /**
   * Verify that actual and expected has different boolean value
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyNotEquals(CVerificationQueue verificationQueue, final Boolean expected) {
    verifyNotEquals(verificationQueue, expected, getDefaultMessage("Not Equals"));
  }

  /**
   * Verify that actual and expected has different boolean value
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEquals(
      CVerificationQueue verificationQueue,
      final Boolean expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue, expected, false, (a, b) -> _toState(a).notEquals(b), message, params);
  }
}

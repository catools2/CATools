package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.extensions.states.interfaces.CBooleanState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CBooleanVerifier;

/**
 * CBooleanVerifier is an interface for Boolean verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 *
 * <p>Please Note that we should extend manually {@link
 * org.catools.common.extensions.verify.CBooleanVerification} for each new added verification here
 */
public interface CBooleanWaitVerifier
    extends CObjectWaitVerifier<Boolean, CBooleanState>, CBooleanVerifier {

  default CBooleanState _toState(Object e) {
    return () -> (Boolean) e;
  }

  /**
   * Verify that actual value is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsFalse(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsFalse(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is false
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsFalse(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsFalse(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual value is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsFalse(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsFalse(
        verificationQueue, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Is False"));
  }

  /**
   * Verify that actual value is false
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsFalse(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        false,
        false,
        (a, b) -> a == b,
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsTrue(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsTrue(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is true
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsTrue(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsTrue(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual value is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsTrue(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsTrue(
        verificationQueue, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Is True"));
  }

  /**
   * Verify that actual value is true
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsTrue(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, b) -> a == b,
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }
}

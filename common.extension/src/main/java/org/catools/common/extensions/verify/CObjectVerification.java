package org.catools.common.extensions.verify;

import org.catools.common.extensions.states.interfaces.CObjectState;
import org.catools.common.extensions.verify.interfaces.CObjectVerifier;

import java.util.Objects;

/**
 * Object verification class contains all verification method which is related to Object
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CObjectVerification<T extends CVerificationBuilder<T>> extends CBaseVerification<T> {

  public CObjectVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void equals(final Object actual, final Object expected) {
    toVerifier(actual).verifyEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void equals(
      final Object actual, final Object expected, final String message, final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param actual value to compare
   */
  public void isNotNull(final Object actual) {
    toVerifier(actual).verifyIsNotNull(verifier);
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNotNull(final Object actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotNull(verifier, message, params);
  }

  /**
   * Verify that actual value is null.
   *
   * @param actual value to compare
   */
  public void isNull(final Object actual) {
    toVerifier(actual).verifyIsNull(verifier);
  }

  /**
   * Verify that actual value is null.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification
   * @param params  parameters in case if message is a format {@link String#format}
   */
  public void isNull(final Object actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNull(verifier, message, params);
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param actual   value to compare
   * @param expected value to compare
   */
  public void notEquals(final Object actual, final Object expected) {
    toVerifier(actual).verifyNotEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   */
  public void notEquals(
      final Object actual, final Object expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  private CObjectVerifier<Object, CObjectState<Object>> toVerifier(Object actual) {
    return new CObjectVerifier<>() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public CObjectState<Object> _toState(Object o) {
        return new CObjectState<>() {
          @Override
          public boolean isEqual(Object expected) {
            return Objects.equals(get(), expected);
          }

          @Override
          public Object get() {
            return actual;
          }
        };
      }

      @Override
      public Object get() {
        return actual;
      }
    };
  }
}

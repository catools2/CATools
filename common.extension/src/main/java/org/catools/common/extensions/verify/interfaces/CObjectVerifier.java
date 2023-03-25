package org.catools.common.extensions.verify.interfaces;

import org.catools.common.collections.CList;
import org.catools.common.extensions.states.interfaces.CObjectState;
import org.catools.common.extensions.verify.CVerificationQueue;

import java.util.List;

/**
 * CObjectVerifier is an interface for Object verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 */
public interface CObjectVerifier<O, S extends CObjectState<O>> extends CBaseVerifier<O> {

  S _toState(Object o);

  private boolean printDiff(O o) {
    return o instanceof String || (o != null && o.toString().length() < 100);
  }

  private boolean printDiff(List<O> o) {
    return o != null && !o.isEmpty() && printDiff(o.get(0));
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyEquals(final CVerificationQueue verificationQueue, final O expected) {
    verifyEquals(verificationQueue, expected, getDefaultMessage("Equals"));
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        printDiff(expected),
        (o1, o2) -> _toState(o1).isEqual(o2),
        message,
        params);
  }

  /**
   * Verify that actual value equals to at least one of expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   */
  default void verifyEqualsAny(CVerificationQueue verificationQueue, List<O> expectedList) {
    verifyEqualsAny(
        verificationQueue, expectedList, getDefaultMessage("Is Equal To One Of Expected Values"));
  }

  /**
   * Verify that actual value equals to at least one of expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param message           information about the propose of this verification.
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedList,
        printDiff(expectedList),
        (a, b) -> a != null && b != null && new CList<>(b).has(b2 -> _toState(a).isEqual(b2)),
        message,
        params);
  }

  /**
   * Verify that actual value does not equals to any expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   */
  default void verifyEqualsNone(CVerificationQueue verificationQueue, List<O> expectedList) {
    verifyEqualsNone(
        verificationQueue,
        expectedList,
        getDefaultMessage("Is Not Equal To Any Of Expected Values"));
  }

  /**
   * Verify that actual value does not equals to any expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param message           information about the propose of this verification.
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsNone(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedList,
        printDiff(expectedList),
        (a, b) -> a != null && b != null && new CList<O>(b).hasNot(b2 -> _toState(a).isEqual(b2)),
        message,
        params);
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsNotNull(final CVerificationQueue verificationQueue) {
    verifyIsNotNull(verificationQueue, getDefaultMessage("Is Not Null"));
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotNull(
      final CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(verificationQueue, null, false, (o1, o2) -> o1 != o2, message, params);
  }

  /**
   * Verify that actual value is null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsNull(final CVerificationQueue verificationQueue) {
    verifyIsNull(verificationQueue, getDefaultMessage("Is Null"));
  }

  /**
   * Verify that actual value is null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNull(
      final CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(
        verificationQueue, null, false, (o1, o2) -> _toState(o1).isEqual((O) o2), message, params);
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyNotEquals(final CVerificationQueue verificationQueue, final O expected) {
    verifyNotEquals(verificationQueue, expected, getDefaultMessage("Not Equals"));
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        printDiff(expected),
        (o1, o2) -> _toState(o1).notEquals(o2),
        message,
        params);
  }
}

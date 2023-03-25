package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.collections.CList;
import org.catools.common.extensions.states.interfaces.CObjectState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CBaseVerifier;
import org.catools.common.extensions.verify.interfaces.CObjectVerifier;

import java.util.List;

/**
 * CObjectVerifier is an interface for Object verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 */
public interface CObjectWaitVerifier<O, S extends CObjectState<O>>
    extends CObjectVerifier<O, S>, CBaseVerifier<O> {

  S _toState(Object o);

  private boolean printDiff(O o) {
    return o instanceof String;
  }

  private boolean printDiff(List<O> o) {
    return o != null && !o.isEmpty() ? printDiff(o.get(0)) : false;
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEquals(
      final CVerificationQueue verificationQueue, final O expected, final int waitInSeconds) {
    verifyEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Are Equals"));
  }

  /**
   * Verify that actual and expected value are equal objects.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        printDiff(expected),
        (o1, o2) -> _toState(o1).isEqual(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value equals to at least one of expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue, List<O> expectedList, final int waitInSeconds) {
    verifyEqualsAny(
        verificationQueue, expectedList, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value equals to at least one of expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsAny(
        verificationQueue,
        expectedList,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual value equals to at least one of expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsAny(
        verificationQueue,
        expectedList,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Equal To One Of Expected Values"));
  }

  /**
   * Verify that actual value equals to at least one of expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsAny(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedList,
        printDiff(expectedList),
        (a, b) -> a != null && b != null && new CList<>(b).has(b2 -> _toState(a).isEqual(b2)),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value does not equals to any expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsNone(
      CVerificationQueue verificationQueue, List<O> expectedList, final int waitInSeconds) {
    verifyEqualsNone(
        verificationQueue, expectedList, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value does not equals to any expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expectedList      a list of strings, may be {@code null}.
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsNone(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsNone(
        verificationQueue,
        expectedList,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual value does not equals to any expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsNone(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsNone(
        verificationQueue,
        expectedList,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Not Equal To Any Of Expected Values"));
  }

  /**
   * Verify that actual value does not equals to any expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expectedList           a list of strings, may be {@code null}.
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsNone(
      CVerificationQueue verificationQueue,
      List<O> expectedList,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expectedList,
        printDiff(expectedList),
        (a, b) -> a != null && b != null && new CList<O>(b).hasNot(b2 -> _toState(a).isEqual(b2)),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotNull(
      final CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotNull(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotNull(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotNull(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotNull(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotNull(
        verificationQueue, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Is Not Null"));
  }

  /**
   * Verify that actual value is NOT null.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotNull(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        new Object(),
        false,
        (o1, o2) -> o1 != null,
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value is null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNull(final CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNull(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is null.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNull(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNull(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual value is null.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNull(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNull(
        verificationQueue, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Is Null"));
  }

  /**
   * Verify that actual value is null.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNull(
      final CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        null,
        false,
        (o1, o2) -> _toState(o1).isEqual((O) o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotEquals(
      final CVerificationQueue verificationQueue, final O expected, final int waitInSeconds) {
    verifyNotEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Are Not Equals"));
  }

  /**
   * Verify that actual and expected value are not equal objects.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEquals(
      final CVerificationQueue verificationQueue,
      final O expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        printDiff(expected),
        (o1, o2) -> _toState(o1).notEquals(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }
}

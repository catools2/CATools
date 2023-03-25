package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.extensions.states.interfaces.CNumberState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CNumberVerifier;

/**
 * CBooleanVerifier is an interface for Boolean verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 */
public interface CNumberWaitVerifier<N extends Number & Comparable<N>>
    extends CNumberVerifier<N>, CObjectWaitVerifier<N, CNumberState<N>> {
  default CNumberState<N> _toState(Object e) {
    return () -> (N) e;
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   */
  default void verifyBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds) {
    verifyBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Between %s And %s (Exclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    CHashMap<String, N> map = new CHashMap<>();
    map.put("Lower Bound", lowerBound);
    map.put("Higher Bound", higherBound);
    _verify(
        verificationQueue,
        map,
        false,
        (o, o2) -> _toState(o).betweenExclusive(lowerBound, higherBound),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   */
  default void verifyBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds) {
    verifyBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Between %s And %s (Inclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    CHashMap<String, N> map = new CHashMap<>();
    map.put("Lower Bound", lowerBound);
    map.put("Higher Bound", higherBound);
    _verify(
        verificationQueue,
        map,
        false,
        (o, o2) -> _toState(o).betweenInclusive(lowerBound, higherBound),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected have the exact same value or their difference is less than
   * precision value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param precision         the acceptable precision
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds) {
    verifyEqualsP(
        verificationQueue,
        expected,
        precision,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected have the exact same value or their difference is less than
   * precision value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param precision         the acceptable precision
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEqualsP(
        verificationQueue,
        expected,
        precision,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected have the exact same value or their difference is less than
   * precision value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param precision              the acceptable precision
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEqualsP(
        verificationQueue,
        expected,
        precision,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Equal To The Expected Value (with precision %s)", precision));
  }

  /**
   * Verify that actual and expected have the exact same value or their difference is less than
   * precision value.
   *
   * <p>Please note that verification consider as passe if both value is null
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param precision              the acceptable precision
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).isEqual(expected, precision),
        waitInSeconds,
        intervalInMilliSeconds,
        "[precision: " + precision + "]" + message,
        params);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyGreater(
      final CVerificationQueue verificationQueue, final N expected, final int waitInSeconds) {
    verifyGreater(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyGreater(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyGreater(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyGreater(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyGreater(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Greater Than The Expected Value"));
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyGreater(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).greater(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyGreaterOrEqual(
      final CVerificationQueue verificationQueue, final N expected, final int waitInSeconds) {
    verifyGreaterOrEqual(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyGreaterOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyGreaterOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyGreaterOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyGreaterOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Greater Than Or Equal To The Expected Value"));
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyGreaterOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).greaterOrEqual(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLess(
      final CVerificationQueue verificationQueue, final N expected, final int waitInSeconds) {
    verifyLess(verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLess(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLess(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLess(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLess(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Less Than The Expected Value"));
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLess(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).less(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyLessOrEqual(
      final CVerificationQueue verificationQueue, final N expected, final int waitInSeconds) {
    verifyLessOrEqual(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLessOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyLessOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyLessOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyLessOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Less Than Or Equal To The Expected Value"));
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyLessOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).lessOrEqual(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds) {
    verifyNotBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Not Is Between %s And %s (Exclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    CHashMap<String, N> map = new CHashMap<>();
    map.put("Lower Bound", lowerBound);
    map.put("Higher Bound", higherBound);
    _verify(
        verificationQueue,
        map,
        false,
        (o, o2) -> _toState(o).notBetweenExclusive(lowerBound, higherBound),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds) {
    verifyNotBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Not Is Between %s And %s (Inclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param lowerBound             lower bound inclusive
   * @param higherBound            higher bound inclusive
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    CHashMap<String, N> map = new CHashMap<>();
    map.put("Lower Bound", lowerBound);
    map.put("Higher Bound", higherBound);
    _verify(
        verificationQueue,
        map,
        false,
        (o, o2) -> _toState(o).notBetweenInclusive(lowerBound, higherBound),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected have different value greater than precision value.
   *
   * <p>Please note that verification consider as passe if one value is null
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param precision         the acceptable precision
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds) {
    verifyNotEqualsP(
        verificationQueue,
        expected,
        precision,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected have different value greater than precision value.
   *
   * <p>Please note that verification consider as passe if one value is null
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param precision         the acceptable precision
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotEqualsP(
        verificationQueue,
        expected,
        precision,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual and expected have different value greater than precision value.
   *
   * <p>Please note that verification consider as passe if one value is null
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param precision              the acceptable precision
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotEqualsP(
        verificationQueue,
        expected,
        precision,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Not Equal To The Expected Value (with precision %s)", precision));
  }

  /**
   * Verify that actual and expected have different value greater than precision value.
   *
   * <p>Please note that verification consider as passe if one value is null
   *
   * @param verificationQueue      verification _verify builder for verification
   * @param expected               value to compare
   * @param precision              the acceptable precision
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).notEquals(o2, precision),
        waitInSeconds,
        intervalInMilliSeconds,
        "[precision: " + precision + "]" + message,
        params);
  }
}

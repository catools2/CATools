package org.catools.common.extensions.verify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.extensions.states.interfaces.CNumberState;
import org.catools.common.extensions.verify.CVerificationQueue;

/**
 * CBooleanVerifier is an interface for Boolean verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 *
 * <p>Please Note that we should extend manually {@link
 * org.catools.common.extensions.verify.CNumberVerification} for each new added verification here
 */
public interface CNumberVerifier<N extends Number & Comparable<N>>
    extends CObjectVerifier<N, CNumberState<N>> {
  default CNumberState<N> _toState(Object e) {
    return () -> (N) e;
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   */
  default void verifyBetweenExclusive(
      final CVerificationQueue verificationQueue, final N lowerBound, final N higherBound) {
    verifyBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        getDefaultMessage("Is Between %s And %s (Exclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is between lower and higher bound values (exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
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
        message,
        params);
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   */
  default void verifyBetweenInclusive(
      final CVerificationQueue verificationQueue, final N lowerBound, final N higherBound) {
    verifyBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        getDefaultMessage("Is Between %s And %s (Inclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
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
   */
  default void verifyEqualsP(
      final CVerificationQueue verificationQueue, final N expected, final N precision) {
    verifyEqualsP(
        verificationQueue,
        expected,
        precision,
        getDefaultMessage("Is Equal To The Expected Value"));
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
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).isEqual(expected, precision),
        "[precision: " + precision + "]" + message,
        params);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   */
  default void verifyGreater(final CVerificationQueue verificationQueue, final N expected) {
    verifyGreater(
        verificationQueue, expected, getDefaultMessage("Is Greater Than The Expected Value"));
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyGreater(
      final CVerificationQueue verificationQueue,
      final N expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue, expected, false, (o, o2) -> _toState(o).greater(o2), message, params);
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   */
  default void verifyGreaterOrEqual(final CVerificationQueue verificationQueue, final N expected) {
    verifyGreaterOrEqual(
        verificationQueue,
        expected,
        getDefaultMessage("Is Greater Than Or Equal To The Expected Value"));
  }

  /**
   * Verify that actual has value greater or equal to expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyGreaterOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).greaterOrEqual(o2),
        message,
        params);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   */
  default void verifyLess(final CVerificationQueue verificationQueue, final N expected) {
    verifyLess(verificationQueue, expected, getDefaultMessage("Is Less Than The Expected Value"));
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLess(
      final CVerificationQueue verificationQueue,
      final N expected,
      final String message,
      final Object... params) {
    _verify(verificationQueue, expected, false, (o, o2) -> _toState(o).less(o2), message, params);
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   */
  default void verifyLessOrEqual(final CVerificationQueue verificationQueue, final N expected) {
    verifyLessOrEqual(
        verificationQueue,
        expected,
        getDefaultMessage("Is Less Than Or Equal To The Expected Value"));
  }

  /**
   * Verify that actual has value less or equal than expected.
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyLessOrEqual(
      final CVerificationQueue verificationQueue,
      final N expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).lessOrEqual(o2),
        message,
        params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   */
  default void verifyNotBetweenExclusive(
      final CVerificationQueue verificationQueue, final N lowerBound, final N higherBound) {
    verifyNotBetweenExclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        String.format("Not Is Between %s And %s (Exclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Exclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotBetweenExclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
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
        message,
        params);
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   */
  default void verifyNotBetweenInclusive(
      final CVerificationQueue verificationQueue, final N lowerBound, final N higherBound) {
    verifyNotBetweenInclusive(
        verificationQueue,
        lowerBound,
        higherBound,
        String.format("Not Is Between %s And %s (Inclusive)", lowerBound, higherBound));
  }

  /**
   * Verify that actual value is NOT between lower and higher bound values (Inclusive).
   *
   * @param verificationQueue verification _verify builder for verification
   * @param lowerBound        lower bound inclusive
   * @param higherBound       higher bound inclusive
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotBetweenInclusive(
      final CVerificationQueue verificationQueue,
      final N lowerBound,
      final N higherBound,
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
   */
  default void verifyNotEqualsP(
      final CVerificationQueue verificationQueue, final N expected, final N precision) {
    verifyNotEqualsP(
        verificationQueue,
        expected,
        precision,
        String.format("Is Not Equal To The Expected Value"));
  }

  /**
   * Verify that actual and expected have different value greater than precision value.
   *
   * <p>Please note that verification consider as passe if one value is null
   *
   * @param verificationQueue verification _verify builder for verification
   * @param expected          value to compare
   * @param precision         the acceptable precision
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotEqualsP(
      final CVerificationQueue verificationQueue,
      final N expected,
      final N precision,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).notEquals(o2, precision),
        "[precision: " + precision + "]" + message,
        params);
  }
}

package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CLinkedMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CCollection;
import org.catools.common.extensions.states.interfaces.CCollectionState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CCollectionVerifier;

import java.util.Collection;
import java.util.Map;

/**
 * CCollectionVerifier is an interface for Collection verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 *
 * @see Map
 * @see CCollection
 * @see CHashMap
 * @see CLinkedMap
 * @see CSet
 * @see CList
 */
public interface CCollectionWaitVerifier<E>
    extends CCollectionVerifier<E>, CIterableWaitVerifier<E> {

  default CCollectionState<E> _toState(Object e) {
    return () -> (Collection<E>) e;
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifySizeEquals(
      CVerificationQueue verificationQueue, int expected, final int waitInSeconds) {
    verifySizeEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySizeEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySizeEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySizeEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySizeEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Size Equals"));
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySizeEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).sizeEquals(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifySizeIsGreaterThan(
      CVerificationQueue verificationQueue, int expected, final int waitInSeconds) {
    verifySizeIsGreaterThan(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySizeIsGreaterThan(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySizeIsGreaterThan(
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
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySizeIsGreaterThan(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySizeIsGreaterThan(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Size Is Greater Than"));
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySizeIsGreaterThan(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).sizeIsGreaterThan(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifySizeIsLessThan(
      CVerificationQueue verificationQueue, int expected, final int waitInSeconds) {
    verifySizeIsLessThan(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySizeIsLessThan(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySizeIsLessThan(
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
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySizeIsLessThan(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySizeIsLessThan(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Size Is Less Than"));
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifySizeIsLessThan(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).sizeIsLessThan(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }
}

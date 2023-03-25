package org.catools.common.extensions.verify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CLinkedMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CCollection;
import org.catools.common.extensions.states.interfaces.CCollectionState;
import org.catools.common.extensions.verify.CVerificationQueue;

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
public interface CCollectionVerifier<E> extends CIterableVerifier<E> {

  default CCollectionState<E> _toState(Object e) {
    return () -> (Collection<E>) e;
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifySizeEquals(CVerificationQueue verificationQueue, int expected) {
    verifySizeEquals(verificationQueue, expected, getDefaultMessage("Size Equals"));
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySizeEquals(
      CVerificationQueue verificationQueue,
      int expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue, expected, false, (o, o2) -> _toState(o).sizeEquals(o2), message, params);
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifySizeIsGreaterThan(CVerificationQueue verificationQueue, int expected) {
    verifySizeIsGreaterThan(verificationQueue, expected, getDefaultMessage("Size Is Greater Than"));
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySizeIsGreaterThan(
      CVerificationQueue verificationQueue,
      int expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).sizeIsGreaterThan(o2),
        message,
        params);
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifySizeIsLessThan(CVerificationQueue verificationQueue, int expected) {
    verifySizeIsLessThan(verificationQueue, expected, getDefaultMessage("Size Is Less Than"));
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifySizeIsLessThan(
      CVerificationQueue verificationQueue,
      int expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).sizeIsLessThan(o2),
        message,
        params);
  }
}

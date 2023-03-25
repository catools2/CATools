package org.catools.common.extensions.verify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CLinkedMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CIterable;
import org.catools.common.extensions.states.interfaces.CIterableState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.utils.CIterableUtil;

import java.util.Map;
import java.util.function.Predicate;

/**
 * CIterableVerifier is an interface for Iterable verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 *
 * @see Map
 * @see CIterable
 * @see CHashMap
 * @see CLinkedMap
 * @see CSet
 * @see CList
 */
public interface CIterableVerifier<E> extends CObjectVerifier<Iterable<E>, CIterableState<E>> {
  default CIterableState<E> _toState(Object e) {
    return () -> (Iterable<E>) e;
  }

  /**
   * Verify that actual collection contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyContains(CVerificationQueue verificationQueue, E expected) {
    verifyContains(verificationQueue, expected, getDefaultMessage("Contains The Record"));
  }

  /**
   * Verify that actual collection contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      E expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> CIterableUtil.contains(get(), e),
        message,
        params);
  }

  /**
   * Verify that actual collection contains all elements from the expected collection. Please note
   * that actual collection might have more elements.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyContainsAll(CVerificationQueue verificationQueue, Iterable<E> expected) {
    verifyContainsAll(verificationQueue, expected, getDefaultMessage("Contains All"));
  }

  /**
   * Verify that actual collection contains all elements from the expected collection. Please note
   * that actual collection might have more elements.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> {
          if (expected == null) {
            return false;
          }
          CList<E> diff = new CList<>();
          _toState(a).containsAll(e, e1 -> diff.add(e1));
          if (!diff.isEmpty()) {
            verificationQueue
                .getLogger()
                .trace("Actual list does not contain following records:\n" + diff);
          }
          return diff.isEmpty();
        },
        message,
        params);
  }

  /**
   * Verify that actual collection contains none of elements from the expected collection.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyContainsNone(CVerificationQueue verificationQueue, Iterable<E> expected) {
    verifyContainsNone(verificationQueue, expected, getDefaultMessage("Contains None"));
  }

  /**
   * Verify that actual collection contains none of elements from the expected collection.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> {
          CList<E> diff = new CList<>();
          _toState(a).containsNone(expected, e1 -> diff.add(e1));
          if (!diff.isEmpty()) {
            verificationQueue.getLogger().trace("Actual list contains following records:\n" + diff);
          }
          return !CIterableUtil.isEmpty(e) && diff.isEmpty();
        },
        message,
        params);
  }

  /**
   * Verify that actual collection either is empty or contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyEmptyOrContains(CVerificationQueue verificationQueue, E expected) {
    verifyEmptyOrContains(
        verificationQueue, expected, getDefaultMessage("Is Empty Or Contains The Record"));
  }

  /**
   * Verify that actual collection either is empty or contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      E expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).emptyOrContains(e),
        message,
        params);
  }

  /**
   * Verify that actual collection either is empty or does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyEmptyOrNotContains(CVerificationQueue verificationQueue, E expected) {
    verifyEmptyOrNotContains(
        verificationQueue, expected, getDefaultMessage("Is Empty Or Not Contains The Record"));
  }

  /**
   * Verify that actual collection either is empty or does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).emptyOrNotContains(e),
        message,
        params);
  }

  /**
   * Verify that actual and expected collections have the exact same elements. (Ignore element
   * order) First we compare that actual collection contains all expected collection elements and
   * then we verify that expected has all elements from actual.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyEquals(CVerificationQueue verificationQueue, Iterable<E> expected) {
    verifyEquals(verificationQueue, expected, getDefaultMessage("Records Are Equals"));
  }

  /**
   * Verify that actual and expected collections have the exact same elements. (Ignore element
   * order) First we compare that actual collection contains all expected collection elements and
   * then we verify that expected has all elements from actual.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        true,
        (a, e) -> {
          CList<E> diffActual = new CList<>();
          CList<E> diffExpected = new CList<>();
          boolean result =
              _toState(a).isEqual(expected, e1 -> diffActual.add(e1), e1 -> diffExpected.add(e1));
          if (!diffExpected.isEmpty()) {
            verificationQueue
                .getLogger()
                .trace("Actual list does not contain following records:\n" + diffExpected);
          }

          if (!diffActual.isEmpty()) {
            verificationQueue
                .getLogger()
                .trace("Expected list does not contain following records:\n" + diffActual);
          }
          return result;
        },
        message,
        params);
  }

  /**
   * Verify that actual collection contains the expected predication.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          predicate
   */
  default void verifyHas(CVerificationQueue verificationQueue, Predicate<E> expected) {
    verifyHas(
        verificationQueue, expected, getDefaultMessage("Has The Record With Defined Condition"));
  }

  /**
   * Verify that actual collection contains the expected predication.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          predicate
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyHas(
      CVerificationQueue verificationQueue,
      Predicate<E> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        "true",
        false,
        (a, e) -> CIterableUtil.has(get(), expected),
        message,
        params);
  }

  /**
   * Verify that actual collection does not contains the expected predication.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          predicate
   */
  default void verifyHasNot(CVerificationQueue verificationQueue, Predicate<E> expected) {
    verifyHasNot(
        verificationQueue,
        expected,
        getDefaultMessage("Has Not The Record With Defined Condition"));
  }

  /**
   * Verify that actual collection does not contains the expected predication.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          predicate
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyHasNot(
      CVerificationQueue verificationQueue,
      Predicate<E> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        "true",
        false,
        (a, e) -> !CIterableUtil.has(get(), expected),
        message,
        params);
  }

  /**
   * Verify that actual collection is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsEmpty(CVerificationQueue verificationQueue) {
    verifyIsEmpty(verificationQueue, getDefaultMessage("Is Empty"));
  }

  /**
   * Verify that actual collection is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(
        verificationQueue, true, false, (a, e) -> CIterableUtil.isEmpty(get()), message, params);
  }

  /**
   * Verify that actual collection is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsNotEmpty(CVerificationQueue verificationQueue) {
    verifyIsNotEmpty(verificationQueue, getDefaultMessage("Is Not Empty"));
  }

  /**
   * Verify that actual collection is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(
        verificationQueue, true, false, (a, e) -> !CIterableUtil.isEmpty(get()), message, params);
  }

  /**
   * Verify that actual collection does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyNotContains(CVerificationQueue verificationQueue, E expected) {
    verifyNotContains(
        verificationQueue, expected, getDefaultMessage("Does Not Contains The Record"));
  }

  /**
   * Verify that actual collection does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue, expected, false, (a, e) -> _toState(a).notContains(e), message, params);
  }

  /**
   * Verify that actual collection contains some but not all elements from the expected collection.
   * Please note that actual collection might have some of elements but the point is to ensure that
   * not all expected elements are exist in it.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyNotContainsAll(CVerificationQueue verificationQueue, Iterable<E> expected) {
    verifyNotContainsAll(verificationQueue, expected, getDefaultMessage("Does Not Contains All"));
  }

  /**
   * Verify that actual collection contains some but not all elements from the expected collection.
   * Please note that actual collection might have some of elements but the point is to ensure that
   * not all expected elements are exist in it.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).notContainsAll(expected),
        message,
        params);
  }
}

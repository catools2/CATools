package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CLinkedMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CIterable;
import org.catools.common.extensions.states.interfaces.CIterableState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CIterableVerifier;
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
 * <p>Please Note that we should extend manually {@link
 * org.catools.common.extensions.verify.CIterableVerification} for each new added verification here
 *
 * @see Map
 * @see CIterable
 * @see CHashMap
 * @see CLinkedMap
 * @see CSet
 * @see CList
 */
public interface CIterableWaitVerifier<E>
    extends CIterableVerifier<E>, CObjectWaitVerifier<Iterable<E>, CIterableState<E>> {
  default CIterableState<E> _toState(Object e) {
    return () -> (Iterable<E>) e;
  }

  /**
   * Verify that actual collection contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyContains(
      CVerificationQueue verificationQueue, E expected, final int waitInSeconds) {
    verifyContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection contains the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Contains The Record"));
  }

  /**
   * Verify that actual collection contains the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> CIterableUtil.contains(get(), e),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection contains all elements from the expected collection. Please note
   * that actual collection might have more elements.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue, Iterable<E> expected, final int waitInSeconds) {
    verifyContainsAll(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection contains all elements from the expected collection. Please note
   * that actual collection might have more elements.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyContainsAll(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection contains all elements from the expected collection. Please note
   * that actual collection might have more elements.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContainsAll(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Contains All"));
  }

  /**
   * Verify that actual collection contains all elements from the expected collection. Please note
   * that actual collection might have more elements.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
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
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection contains none of elements from the expected collection.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue, Iterable<E> expected, final int waitInSeconds) {
    verifyContainsNone(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection contains none of elements from the expected collection.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyContainsNone(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection contains none of elements from the expected collection.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContainsNone(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Contains None"));
  }

  /**
   * Verify that actual collection contains none of elements from the expected collection.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
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
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection either is empty or contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue, E expected, final int waitInSeconds) {
    verifyEmptyOrContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection either is empty or contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEmptyOrContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection either is empty or contains the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEmptyOrContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Empty Or Contains The Record"));
  }

  /**
   * Verify that actual collection either is empty or contains the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).emptyOrContains(e),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection either is empty or does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue, E expected, final int waitInSeconds) {
    verifyEmptyOrNotContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection either is empty or does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEmptyOrNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection either is empty or does not contain the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEmptyOrNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Empty Or Not Contains The Record"));
  }

  /**
   * Verify that actual collection either is empty or does not contain the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).emptyOrNotContains(e),
        waitInSeconds,
        intervalInMilliSeconds,
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
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue, Iterable<E> expected, final int waitInSeconds) {
    verifyEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected collections have the exact same elements. (Ignore element
   * order) First we compare that actual collection contains all expected collection elements and
   * then we verify that expected has all elements from actual.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
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
   * Verify that actual and expected collections have the exact same elements. (Ignore element
   * order) First we compare that actual collection contains all expected collection elements and
   * then we verify that expected has all elements from actual.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Records Are Equals"));
  }

  /**
   * Verify that actual and expected collections have the exact same elements. (Ignore element
   * order) First we compare that actual collection contains all expected collection elements and
   * then we verify that expected has all elements from actual.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
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
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection contains the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          predicate
   */
  default void verifyHas(CVerificationQueue verificationQueue, Predicate<E> expected) {
    verifyHas(
        verificationQueue,
        expected,
        getDefaultWaitInSeconds(),
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection contains the expected element.
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
        verificationQueue, expected, false, (a, e) -> CIterableUtil.has(get(), e), message, params);
  }

  /**
   * Verify that actual collection contains the element which returns true from expected predicate.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          predicate
   * @param waitInSeconds     maximum wait time
   */
  default void verifyHas(
      CVerificationQueue verificationQueue, Predicate<E> expected, final int waitInSeconds) {
    verifyHas(verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection contains the element which returns true from expected predicate.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          predicate
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyHas(
      CVerificationQueue verificationQueue,
      Predicate<E> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyHas(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection contains the element which returns true from expected predicate.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               predicate
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyHas(
      CVerificationQueue verificationQueue,
      Predicate<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyHas(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Has The Record With Defined Condition"));
  }

  /**
   * Verify that actual collection contains the element which returns true from expected predicate.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               predicate
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyHas(
      CVerificationQueue verificationQueue,
      Predicate<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> CIterableUtil.has(get(), e),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmpty(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmpty(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsEmpty(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual collection is empty.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsEmpty(
        verificationQueue, waitInSeconds, intervalInMilliSeconds, getDefaultMessage("Is Empty"));
  }

  /**
   * Verify that actual collection is empty.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, e) -> CIterableUtil.isEmpty(get()),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotEmpty(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotEmpty(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyIsNotEmpty(
        verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds(), message, params);
  }

  /**
   * Verify that actual collection is not empty. (might contains null values)
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyIsNotEmpty(
        verificationQueue,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Not Empty"));
  }

  /**
   * Verify that actual collection is not empty. (might contains null values)
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (a, e) -> !CIterableUtil.isEmpty(get()),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue, E expected, final int waitInSeconds) {
    verifyNotContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection does not contain the expected element.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection does not contain the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Does Not Contains The Record"));
  }

  /**
   * Verify that actual collection does not contain the expected element.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      E expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).notContains(e),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual collection contains some but not all elements from the expected collection.
   * Please note that actual collection might have some of elements but the point is to ensure that
   * not all expected elements are exist in it.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue, Iterable<E> expected, final int waitInSeconds) {
    verifyNotContainsAll(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual collection contains some but not all elements from the expected collection.
   * Please note that actual collection might have some of elements but the point is to ensure that
   * not all expected elements are exist in it.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotContainsAll(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual collection contains some but not all elements from the expected collection.
   * Please note that actual collection might have some of elements but the point is to ensure that
   * not all expected elements are exist in it.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotContainsAll(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Does Not Contains All"));
  }

  /**
   * Verify that actual collection contains some but not all elements from the expected collection.
   * Please note that actual collection might have some of elements but the point is to ensure that
   * not all expected elements are exist in it.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters in case if message is a format {@link String#format}
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Iterable<E> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).notContainsAll(expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }
}

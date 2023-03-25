package org.catools.common.extensions.waitVerify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CLinkedMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CCollection;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.extensions.states.interfaces.CMapState;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.verify.interfaces.CMapVerifier;

import java.util.Map;
import java.util.Objects;

/**
 * CMapVerifier is an interface for Map verification related methods.
 *
 * <p>We need this interface to have possibility of adding verification to any exists objects with
 * the minimum change in the code. In the meantime adding verification method in one place can be
 * extend cross all other objects:
 *
 * <p>Please Note that we should extend manually {@link
 * org.catools.common.extensions.verify.CMapVerification} for each new added verification here
 *
 * @see Map
 * @see CCollection
 * @see CHashMap
 * @see CLinkedMap
 * @see CSet
 * @see CList
 */
public interface CMapWaitVerifier<K, V>
    extends CMapVerifier<K, V>, CObjectWaitVerifier<Map<K, V>, CMapState<K, V>> {

  default CMapState<K, V> _toState(Object e) {
    return () -> (Map<K, V>) e;
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds) {
    verifyContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyContains(
      CVerificationQueue verificationQueue, Map.Entry<K, V> expected, final int waitInSeconds) {
    verifyContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
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
   * Verify that actual map contains the expected key and value.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds);
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Contains The Expected Value"));
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).contains(e),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue, Map<K, V> expected, final int waitInSeconds) {
    verifyContainsAll(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContainsAll(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Contains All The Expected Map"));
  }

  /**
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
          CMap<K, V> diff = new CHashMap<>();
          boolean result =
              _toState(a).containsAll(e, entry -> diff.put(entry.getKey(), entry.getValue()));
          if (!diff.isEmpty()) {
            verificationQueue
                .getLogger()
                .trace("Actual list does not contain following records:\n" + diff);
          }

          return result;
        },
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue, Map<K, V> expected, final int waitInSeconds) {
    verifyContainsNone(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyContainsNone(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Not Contains Any Record From The Expected Map"));
  }

  /**
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
          CMap<K, V> diff = new CHashMap<>();
          boolean result =
              _toState(a).containsNone(e, entry -> diff.put(entry.getKey(), entry.getValue()));
          if (!diff.isEmpty()) {
            verificationQueue.getLogger().trace("Actual list contains following records:\n" + diff);
          }

          return result;
        },
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds) {
    verifyEmptyOrContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEmptyOrContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue, Map.Entry<K, V> expected, final int waitInSeconds) {
    Objects.requireNonNull(expected.getKey());
    verifyEmptyOrContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    Objects.requireNonNull(expected.getKey());
    verifyEmptyOrContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEmptyOrContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEmptyOrContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEmptyOrContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Empty Or Contains The Expected Value"));
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    Objects.requireNonNull(expected.getKey());
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
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds) {
    verifyEmptyOrNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyEmptyOrNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue, Map.Entry<K, V> expected, final int waitInSeconds) {
    Objects.requireNonNull(expected.getKey());
    verifyEmptyOrNotContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    Objects.requireNonNull(expected.getKey());
    verifyEmptyOrNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEmptyOrNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyEmptyOrNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEmptyOrNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Is Empty Or Not Contains The Expected Value"));
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    Objects.requireNonNull(expected.getKey());
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).emptyOrNotContains(expected),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual and expected maps have the exact same entries. (Ignore entry order) First we
   * compare that actual map contains all expected map entries and then we verify that expected has
   * all entries from actual.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          map to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue, Map<K, V> expected, final int waitInSeconds) {
    verifyEquals(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual and expected maps have the exact same entries. (Ignore entry order) First we
   * compare that actual map contains all expected map entries and then we verify that expected has
   * all entries from actual.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          map to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
   * Verify that actual and expected maps have the exact same entries. (Ignore entry order) First we
   * compare that actual map contains all expected map entries and then we verify that expected has
   * all entries from actual.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               map to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyEquals(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Equals Expected Value"));
  }

  /**
   * Verify that actual and expected maps have the exact same entries. (Ignore entry order) First we
   * compare that actual map contains all expected map entries and then we verify that expected has
   * all entries from actual.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               map to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> {
          CMap<K, V> diffActual = new CHashMap<>();
          CMap<K, V> diffExpected = new CHashMap<>();
          boolean equals =
              _toState(o)
                  .isEqual(
                      o2,
                      entry -> diffActual.put(entry.getKey(), entry.getValue()),
                      entry -> diffExpected.put(entry.getKey(), entry.getValue()));
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
          return equals;
        },
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsEmpty(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsEmpty(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
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
   * Verify that actual map is empty.
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
   * Verify that actual map is empty.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
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
        (o, o2) -> get() == null || get().isEmpty(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   */
  default void verifyIsNotEmpty(CVerificationQueue verificationQueue, final int waitInSeconds) {
    verifyIsNotEmpty(verificationQueue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
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
   * Verify that actual map is not empty. (might contains null values)
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
   * Verify that actual map is not empty. (might contains null values)
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
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
        (o, o2) -> _toState(o).isNotEmpty(),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds) {
    verifyNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifyNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue, Map.Entry<K, V> expected, final int waitInSeconds) {
    verifyNotContains(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
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
   * Verify that actual map does not contain the expected entry.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expectedKey            key to compare
   * @param expectedValue          value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    verifyNotContains(
        verificationQueue,
        Map.entry(expectedKey, expectedValue),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotContains(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Not Contains Expected Value"));
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
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
   * Verify that actual map might contains some but not all entries from the expected map. Please
   * note that actual map might have some of entries but the point is to ensure that not all
   * expected entries are exist in it. We do verify that both key and value match in this
   * comparision
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue, Map<K, V> expected, final int waitInSeconds) {
    verifyNotContainsAll(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual map might contains some but not all entries from the expected map. Please
   * note that actual map might have some of entries but the point is to ensure that not all
   * expected entries are exist in it. We do verify that both key and value match in this
   * comparision
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
   * Verify that actual map might contains some but not all entries from the expected map. Please
   * note that actual map might have some of entries but the point is to ensure that not all
   * expected entries are exist in it. We do verify that both key and value match in this
   * comparision
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifyNotContainsAll(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Not Contains All Expected Values"));
  }

  /**
   * Verify that actual map might contains some but not all entries from the expected map. Please
   * note that actual map might have some of entries but the point is to ensure that not all
   * expected entries are exist in it. We do verify that both key and value match in this
   * comparision
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).notContainsAll(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
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
   * @param params            parameters for message if message is a string format
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
        getDefaultMessage("Size Is Equal To Expected Value"));
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
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
   * @param params            parameters for message if message is a string format
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
        getDefaultMessage("Size Is Greater Than Expected Value"));
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
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
   * Verify that actual has value greater than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifySizeIsGreaterThanOrEqual(
      CVerificationQueue verificationQueue, int expected, final int waitInSeconds) {
    verifySizeIsGreaterThanOrEqual(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value greater than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifySizeIsGreaterThanOrEqual(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySizeIsGreaterThanOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual has value greater than or equals to expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySizeIsGreaterThanOrEqual(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySizeIsGreaterThanOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Size Is Greater Than Expected Value"));
  }

  /**
   * Verify that actual has value greater than or equals to expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifySizeIsGreaterThanOrEqual(
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
        (o, o2) -> _toState(o).sizeIsGreaterThanOrEqual(o2),
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
   * @param params            parameters for message if message is a string format
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
        getDefaultMessage("Size Is Less Than Expected Value"));
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
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

  /**
   * Verify that actual has value less than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   */
  default void verifySizeIsLessThanOrEqual(
      CVerificationQueue verificationQueue, int expected, final int waitInSeconds) {
    verifySizeIsLessThanOrEqual(
        verificationQueue, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Verify that actual has value less than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param waitInSeconds     maximum wait time
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifySizeIsLessThanOrEqual(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    verifySizeIsLessThanOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  /**
   * Verify that actual has value less than or equals to expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   */
  default void verifySizeIsLessThanOrEqual(
      CVerificationQueue verificationQueue,
      int expected,
      final int waitInSeconds,
      final int intervalInMilliSeconds) {
    verifySizeIsLessThanOrEqual(
        verificationQueue,
        expected,
        waitInSeconds,
        intervalInMilliSeconds,
        getDefaultMessage("Size is less than or equal to the expected value"));
  }

  /**
   * Verify that actual has value less than or equals to expected.
   *
   * @param verificationQueue      CTest, CVerifier or any other verification queue instance
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @param message                information about the propose of this verification
   * @param params                 parameters for message if message is a string format
   */
  default void verifySizeIsLessThanOrEqual(
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
        (o, o2) -> _toState(o).sizeIsLessThanOrEqual(o2),
        waitInSeconds,
        intervalInMilliSeconds,
        message,
        params);
  }
}

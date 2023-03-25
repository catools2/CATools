package org.catools.common.extensions.verify.interfaces;

import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CLinkedMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.CSet;
import org.catools.common.collections.interfaces.CCollection;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.extensions.states.interfaces.CMapState;
import org.catools.common.extensions.verify.CVerificationQueue;

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
public interface CMapVerifier<K, V> extends CObjectVerifier<Map<K, V>, CMapState<K, V>> {

  default CMapState<K, V> _toState(Object e) {
    return () -> (Map<K, V>) e;
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   */
  default void verifyContains(
      CVerificationQueue verificationQueue, K expectedKey, V expectedValue) {
    verifyContains(verificationQueue, Map.entry(expectedKey, expectedValue));
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    verifyContains(verificationQueue, Map.entry(expectedKey, expectedValue), message, params);
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expected value to compare
   */
  default void verifyContains(CVerificationQueue verificationQueue, Map.Entry<K, V> expected) {
    verifyContains(verificationQueue, expected, getDefaultMessage(("Contains The Expected Value")));
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters for message if message is a string format
   */
  default void verifyContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> {
          if (get() == null || expected == null) {
            return false;
          }

          if (!get().keySet().contains(expected.getKey())) {
            verificationQueue
                .getLogger()
                .warn(
                    "Key does not exists in map. Expected: '{}' Actual: '{}'",
                    expected.getKey(),
                    get().keySet());
            return false;
          }

          V value = get().get(expected.getKey());
          if (!Objects.equals(value, expected.getValue())) {
            verificationQueue
                .getLogger()
                .warn(
                    "Expected Value does not match the map entity. Expected: '{}' Actual: '{}'",
                    expected.getValue(),
                    value);
            return false;
          }

          return true;
        },
        message,
        params);
  }

  /**
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
                .warn("Actual list does not contain following records:\n" + diff);
          }

          return result;
        },
        message,
        params);
  }

  /**
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyContainsAll(CVerificationQueue verificationQueue, Map<K, V> expected) {
    verifyContainsAll(
        verificationQueue, expected, getDefaultMessage(("Contains All The Expected Map")));
  }

  /**
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyContainsNone(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
            verificationQueue.getLogger().warn("Actual list contains following records:\n" + diff);
          }

          return result;
        },
        message,
        params);
  }

  /**
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyContainsNone(CVerificationQueue verificationQueue, Map<K, V> expected) {
    verifyContainsNone(
        verificationQueue,
        expected,
        getDefaultMessage(("Not Contains Any Record From The Expected Map")));
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue, K expectedKey, V expectedValue) {
    verifyEmptyOrContains(verificationQueue, Map.entry(expectedKey, expectedValue));
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    verifyEmptyOrContains(
        verificationQueue, Map.entry(expectedKey, expectedValue), message, params);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expected value to compare
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue, Map.Entry<K, V> expected) {
    verifyEmptyOrContains(
        verificationQueue,
        expected,
        getDefaultMessage(("Is Empty Or Contains The Expected Value")));
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters for message if message is a string format
   */
  default void verifyEmptyOrContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final String message,
      final Object... params) {
    Objects.requireNonNull(expected.getKey());
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> {
          if (get() == null || expected == null) {
            return false;
          }

          if (get().isEmpty()) {
            return true;
          }

          if (!get().keySet().contains(expected.getKey())) {
            verificationQueue
                .getLogger()
                .warn(
                    "Key does not exists in map. Expected Key: '{}' Actual keys: '{}'",
                    expected.getKey(),
                    get().keySet());
            return false;
          }

          V value = get().get(expected.getKey());
          if (!Objects.equals(value, expected.getValue())) {
            verificationQueue
                .getLogger()
                .warn(
                    "Expected Value does not match the map entity. Expected Key: '{}' Actual keys: '{}'",
                    expected.getValue(),
                    value);
            return false;
          }

          return true;
        },
        message,
        params);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue, K expectedKey, V expectedValue) {
    verifyEmptyOrNotContains(verificationQueue, Map.entry(expectedKey, expectedValue));
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    verifyEmptyOrNotContains(
        verificationQueue, Map.entry(expectedKey, expectedValue), message, params);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expected value to compare
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue, Map.Entry<K, V> expected) {
    verifyEmptyOrNotContains(
        verificationQueue,
        expected,
        getDefaultMessage(("Is Empty Or Not Contains The Expected Value")));
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters for message if message is a string format
   */
  default void verifyEmptyOrNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final String message,
      final Object... params) {
    Objects.requireNonNull(expected.getKey());
    _verify(
        verificationQueue,
        expected,
        false,
        (a, e) -> _toState(a).emptyOrNotContains(expected),
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
   */
  default void verifyEquals(CVerificationQueue verificationQueue, Map<K, V> expected) {
    verifyEquals(verificationQueue, expected, getDefaultMessage(("Equals Expected Value")));
  }

  /**
   * Verify that actual and expected maps have the exact same entries. (Ignore entry order) First we
   * compare that actual map contains all expected map entries and then we verify that expected has
   * all entries from actual.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          map to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyEquals(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
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
                .warn("Actual list does not contain following records:\n" + diffExpected);
          }

          if (!diffActual.isEmpty()) {
            verificationQueue
                .getLogger()
                .warn("Expected list does not contain following records:\n" + diffActual);
          }
          return equals;
        },
        message,
        params);
  }

  /**
   * Verify that actual map is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsEmpty(CVerificationQueue verificationQueue) {
    verifyIsEmpty(verificationQueue, getDefaultMessage(("Is Empty")));
  }

  /**
   * Verify that actual map is empty.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyIsEmpty(
      CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(
        verificationQueue,
        true,
        false,
        (o, o2) -> get() == null || get().isEmpty(),
        message,
        params);
  }

  /**
   * Verify that actual map is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   */
  default void verifyIsNotEmpty(CVerificationQueue verificationQueue) {
    verifyIsNotEmpty(verificationQueue, getDefaultMessage(("Is Not Empty")));
  }

  /**
   * Verify that actual map is not empty. (might contains null values)
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyIsNotEmpty(
      CVerificationQueue verificationQueue, final String message, final Object... params) {
    _verify(verificationQueue, true, false, (o, o2) -> _toState(o).isNotEmpty(), message, params);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue, K expectedKey, V expectedValue) {
    verifyNotContains(verificationQueue, Map.entry(expectedKey, expectedValue));
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters for message if message is a string format
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    verifyNotContains(verificationQueue, Map.entry(expectedKey, expectedValue), message, params);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expected value to compare
   */
  default void verifyNotContains(CVerificationQueue verificationQueue, Map.Entry<K, V> expected) {
    verifyNotContains(
        verificationQueue, expected, getDefaultMessage(("Not Contains Expected Value")));
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters for message if message is a string format
   */
  default void verifyNotContains(
      CVerificationQueue verificationQueue,
      Map.Entry<K, V> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue, expected, false, (a, e) -> _toState(a).notContains(e), message, params);
  }

  /**
   * Verify that actual map might contains some but not all entries from the expected map. Please
   * note that actual map might have some of entries but the point is to ensure that not all
   * expected entries are exist in it. We do verify that both key and value match in this
   * comparision
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifyNotContainsAll(CVerificationQueue verificationQueue, Map<K, V> expected) {
    verifyNotContainsAll(
        verificationQueue, expected, getDefaultMessage(("Not Contains All Expected Values")));
  }

  /**
   * Verify that actual map might contains some but not all entries from the expected map. Please
   * note that actual map might have some of entries but the point is to ensure that not all
   * expected entries are exist in it. We do verify that both key and value match in this
   * comparision
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifyNotContainsAll(
      CVerificationQueue verificationQueue,
      Map<K, V> expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).notContainsAll(o2),
        message,
        params);
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifySizeEquals(CVerificationQueue verificationQueue, int expected) {
    verifySizeEquals(
        verificationQueue, expected, getDefaultMessage(("Size Is Equal To Expected Value")));
  }

  /**
   * Verify the map size is equal to expected value.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
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
    verifySizeIsGreaterThan(
        verificationQueue, expected, getDefaultMessage(("Size Is Greater Than Expected Value")));
  }

  /**
   * Verify that actual has value greater than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
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
   * Verify that actual has value greater than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifySizeIsGreaterThanOrEqual(CVerificationQueue verificationQueue, int expected) {
    verifySizeIsGreaterThanOrEqual(
        verificationQueue, expected, getDefaultMessage(("Size Is Greater Than Expected Value")));
  }

  /**
   * Verify that actual has value greater than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifySizeIsGreaterThanOrEqual(
      CVerificationQueue verificationQueue,
      int expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).sizeIsGreaterThanOrEqual(o2),
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
    verifySizeIsLessThan(
        verificationQueue, expected, getDefaultMessage(("Size Is Less Than Expected Value")));
  }

  /**
   * Verify that actual has value less than expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
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

  /**
   * Verify that actual has value less than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   */
  default void verifySizeIsLessThanOrEqual(CVerificationQueue verificationQueue, int expected) {
    verifySizeIsLessThanOrEqual(
        verificationQueue,
        expected,
        getDefaultMessage(("Size Is Less Than Or Equal Expected Value")));
  }

  /**
   * Verify that actual has value less than or equals to expected.
   *
   * @param verificationQueue CTest, CVerifier or any other verification queue instance
   * @param expected          value to compare
   * @param message           information about the propose of this verification
   * @param params            parameters for message if message is a string format
   */
  default void verifySizeIsLessThanOrEqual(
      CVerificationQueue verificationQueue,
      int expected,
      final String message,
      final Object... params) {
    _verify(
        verificationQueue,
        expected,
        false,
        (o, o2) -> _toState(o).sizeIsLessThanOrEqual(o2),
        message,
        params);
  }
}

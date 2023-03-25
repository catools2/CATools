package org.catools.common.extensions.verify;

import org.catools.common.extensions.verify.interfaces.CMapVerifier;

import java.util.Map;

/**
 * Map verification class contains all verification method which is related to Map
 *
 * @param <T> represent any classes which extent {@link CVerificationBuilder}.
 */
public class CMapVerification<T extends CVerificationBuilder> extends CBaseVerification<T> {
  public CMapVerification(T verifier) {
    super(verifier);
  }

  /**
   * Verify that actual map contains the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void contains(Map<K, V> actual, Map.Entry<K, V> expected) {
    toVerifier(actual).verifyContains(verifier, expected);
  }

  /**
   * Verify that actual map contains the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void contains(
      Map<K, V> actual, Map.Entry<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyContains(verifier, expected, message, params);
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param actual        map to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void contains(Map<K, V> actual, K expectedKey, V expectedValue) {
    toVerifier(actual).verifyContains(verifier, expectedKey, expectedValue);
  }

  /**
   * Verify that actual map contains the expected key and value.
   *
   * @param actual        map to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void contains(
      Map<K, V> actual,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyContains(verifier, expectedKey, expectedValue, message, params);
  }

  /**
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void containsAll(Map<K, V> actual, Map<K, V> expected) {
    toVerifier(actual).verifyContainsAll(verifier, expected);
  }

  /**
   * Verify that actual map contains all entries from the expected map. Please note that actual map
   * might have more entries.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void containsAll(
      Map<K, V> actual, Map<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyContainsAll(verifier, expected, message, params);
  }

  /**
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void containsNone(Map<K, V> actual, Map<K, V> expected) {
    toVerifier(actual).verifyContainsNone(verifier, expected);
  }

  /**
   * Verify that actual map contains none of entries from the expected map.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void containsNone(
      Map<K, V> actual, Map<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyContainsNone(verifier, expected, message, params);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param actual        value to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void emptyOrContains(Map<K, V> actual, K expectedKey, V expectedValue) {
    toVerifier(actual).verifyEmptyOrContains(verifier, expectedKey, expectedValue);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param actual        value to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void emptyOrContains(
      Map<K, V> actual,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyEmptyOrContains(verifier, expectedKey, expectedValue, message, params);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void emptyOrContains(Map<K, V> actual, Map.Entry<K, V> expected) {
    toVerifier(actual).verifyEmptyOrContains(verifier, expected);
  }

  /**
   * Verify that actual map either is empty or contains the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void emptyOrContains(
      Map<K, V> actual, Map.Entry<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyEmptyOrContains(verifier, expected, message, params);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param actual        value to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void emptyOrNotContains(Map<K, V> actual, K expectedKey, V expectedValue) {
    toVerifier(actual).verifyEmptyOrNotContains(verifier, expectedKey, expectedValue);
  }

  /**
   * Verify that actual map either is empty or does not contains the expected entry.
   *
   * @param actual        value to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void emptyOrNotContains(
      Map<K, V> actual,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    toVerifier(actual)
        .verifyEmptyOrNotContains(verifier, expectedKey, expectedValue, message, params);
  }

  /**
   * Verify that actual map either is empty or does not contain the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void emptyOrNotContains(Map<K, V> actual, Map.Entry<K, V> expected) {
    toVerifier(actual).verifyEmptyOrNotContains(verifier, expected);
  }

  /**
   * Verify that actual map either is empty or does not contain the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void emptyOrNotContains(
      Map<K, V> actual, Map.Entry<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyEmptyOrNotContains(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected maps have the exact same entries. (Ignore entry order) First we
   * compare that actual map contains all expected map entries and then we verify that expected has
   * all entries from actual.
   *
   * @param actual   map to compare
   * @param expected map to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void equals(Map<K, V> actual, Map<K, V> expected) {
    toVerifier(actual).verifyEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected maps have the exact same entries. (Ignore entry order) First we
   * compare that actual map contains all expected map entries and then we verify that expected has
   * all entries from actual.
   *
   * @param actual   map to compare
   * @param expected map to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void equals(
      Map<K, V> actual, Map<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual and expected maps does not have the exact same entries. (Ignore entry order)
   * First we compare that actual map does not contains all expected map entries and then if all
   * records matched we verify that expected does not have all entries from actual.
   *
   * @param actual   map to compare
   * @param expected map to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void notEquals(Map<K, V> actual, Map<K, V> expected) {
    toVerifier(actual).verifyNotEquals(verifier, expected);
  }

  /**
   * Verify that actual and expected maps does not have the exact same entries. (Ignore entry order)
   * First we compare that actual map does not contains all expected map entries and then if all
   * records matched we verify that expected does not have all entries from actual.
   *
   * @param actual   map to compare
   * @param expected map to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void notEquals(
      Map<K, V> actual, Map<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotEquals(verifier, expected, message, params);
  }

  /**
   * Verify that actual map is empty.
   *
   * @param actual value to compare
   * @param <K>    type of map key
   * @param <V>    type of map value
   */
  public <K, V> void isEmpty(Map<K, V> actual) {
    toVerifier(actual).verifyIsEmpty(verifier);
  }

  /**
   * Verify that actual map is empty.
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification
   * @param params  parameters in case if message is a format {@link String#format}
   * @param <K>     type of map key
   * @param <V>     type of map value
   */
  public <K, V> void isEmpty(Map<K, V> actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsEmpty(verifier, message, params);
  }

  /**
   * Verify that actual map is not empty. (might contains null values)
   *
   * @param actual value to compare
   * @param <K>    type of map key
   * @param <V>    type of map value
   */
  public <K, V> void isNotEmpty(Map<K, V> actual) {
    toVerifier(actual).verifyIsNotEmpty(verifier);
  }

  /**
   * Verify that actual map is not empty. (might contains null values)
   *
   * @param actual  value to compare
   * @param message information about the propose of this verification
   * @param params  parameters in case if message is a format {@link String#format}
   * @param <K>     type of map key
   * @param <V>     type of map value
   */
  public <K, V> void isNotEmpty(Map<K, V> actual, final String message, final Object... params) {
    toVerifier(actual).verifyIsNotEmpty(verifier, message, params);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void notContains(Map<K, V> actual, Map.Entry<K, V> expected) {
    toVerifier(actual).verifyNotContains(verifier, expected);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void notContains(
      Map<K, V> actual, Map.Entry<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotContains(verifier, expected, message, params);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param actual        value to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void notContains(Map<K, V> actual, K expectedKey, V expectedValue) {
    toVerifier(actual).verifyNotContains(verifier, expectedKey, expectedValue);
  }

  /**
   * Verify that actual map does not contain the expected entry.
   *
   * @param actual        value to compare
   * @param expectedKey   key to compare
   * @param expectedValue value to compare
   * @param message       information about the propose of this verification
   * @param params        parameters in case if message is a format {@link String#format}
   * @param <K>           type of map key
   * @param <V>           type of map value
   */
  public <K, V> void notContains(
      Map<K, V> actual,
      K expectedKey,
      V expectedValue,
      final String message,
      final Object... params) {
    toVerifier(actual).verifyNotContains(verifier, expectedKey, expectedValue, message, params);
  }

  /**
   * Verify that actual map does not contain all entries from the expected map. Please note that
   * actual map might have some of entries but the point is to ensure that not all expected entries
   * are exist in it.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void notContainsAll(Map<K, V> actual, Map<K, V> expected) {
    toVerifier(actual).verifyNotContainsAll(verifier, expected);
  }

  /**
   * Verify that actual map does not contain all entries from the expected map. Please note that
   * actual map might have some of entries but the point is to ensure that not all expected entries
   * are exist in it.
   *
   * @param actual   value to compare
   * @param expected value to compare
   * @param message  information about the propose of this verification
   * @param params   parameters in case if message is a format {@link String#format}
   * @param <K>      type of map key
   * @param <V>      type of map value
   */
  public <K, V> void notContainsAll(
      Map<K, V> actual, Map<K, V> expected, final String message, final Object... params) {
    toVerifier(actual).verifyNotContainsAll(verifier, expected, message, params);
  }

  private <K, V> CMapVerifier<K, V> toVerifier(Map<K, V> actual) {
    return new CMapVerifier<K, V>() {
      @Override
      public boolean _useWaiter() {
        return false;
      }

      @Override
      public Map<K, V> get() {
        return actual;
      }
    };
  }
}

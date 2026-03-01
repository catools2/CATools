package org.catools.common.tests;

import java.util.Map;
import org.catools.common.collections.CHashMap;

/**
 * CTestStateData is a specialized HashMap implementation designed for managing test state data. It
 * extends CHashMap to provide type-safe storage and retrieval of test-related data using string
 * keys and generic object values.
 *
 * <p>This class is particularly useful for maintaining state information across test execution
 * phases, allowing tests to store and retrieve arbitrary data objects during test lifecycle.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>{@code
 * // Create a new test state data container
 * CTestStateData testData = new CTestStateData();
 *
 * // Store different types of test data
 * testData.updateDataState("userId", 12345L);
 * testData.updateDataState("userName", "john.doe");
 * testData.updateDataState("testStartTime", System.currentTimeMillis());
 *
 * // Retrieve typed data
 * Long userId = testData.getDataState("userId");
 * String userName = testData.getDataState("userName");
 * Long startTime = testData.getDataState("testStartTime");
 * }</pre>
 *
 * @see CHashMap
 * @since 1.0
 */
public class CTestStateData extends CHashMap<String, Object> {
  /**
   * Constructs an empty CTestStateData with the default initial capacity (16) and the default load
   * factor (0.75).
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CTestStateData testData = new CTestStateData();
   * testData.updateDataState("testCase", "UserLoginTest");
   * }</pre>
   */
  public CTestStateData() {}

  /**
   * Constructs an empty CTestStateData with the specified initial capacity and load factor.
   *
   * @param initialCapacity the initial capacity of the hash map
   * @param loadFactor the load factor of the hash map
   * @throws IllegalArgumentException if the initial capacity is negative or the load factor is
   *     nonpositive
   *     <p>Example usage:
   *     <pre>{@code
   * // Create with custom capacity and load factor for performance optimization
   * CTestStateData testData = new CTestStateData(32, 0.8f);
   * testData.updateDataState("executionId", UUID.randomUUID().toString());
   *
   * }</pre>
   */
  public CTestStateData(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  /**
   * Constructs an empty CTestStateData with the specified initial capacity and the default load
   * factor (0.75).
   *
   * @param initialCapacity the initial capacity of the hash map
   * @throws IllegalArgumentException if the initial capacity is negative
   *     <p>Example usage:
   *     <pre>{@code
   * // Create with custom initial capacity for expected data size
   * CTestStateData testData = new CTestStateData(50);
   *
   * // Store multiple test parameters
   * for (int i = 0; i < 20; i++) {
   *     testData.updateDataState("param" + i, "value" + i);
   * }
   *
   * }</pre>
   */
  public CTestStateData(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Constructs a new CTestStateData with the same mappings as the specified Map. The CTestStateData
   * is created with default load factor (0.75) and an initial capacity sufficient to hold the
   * mappings in the specified Map.
   *
   * @param m the map whose mappings are to be placed in this map
   * @throws NullPointerException if the specified map is null
   *     <p>Example usage:
   *     <pre>{@code
   * // Initialize from existing map data
   * Map<String, Object> existingData = Map.of(
   *     "testSuite", "IntegrationTests",
   *     "environment", "staging",
   *     "timeout", 30000L
   * );
   *
   * CTestStateData testData = new CTestStateData(existingData);
   *
   * // Add additional test-specific data
   * testData.updateDataState("currentTest", "DatabaseConnectionTest");
   *
   * }</pre>
   */
  public CTestStateData(Map<? extends String, ?> m) {
    super(m);
  }

  /**
   * Updates the test state data by associating the specified value with the specified key. If the
   * map previously contained a mapping for the key, the old value is replaced.
   *
   * @param key the key with which the specified value is to be associated
   * @param value the value to be associated with the specified key
   *     <p>This method is a convenience wrapper around the standard {@code put} method, providing a
   *     more descriptive name for test state management contexts.
   *     <p>Example usage:
   *     <pre>{@code
   * CTestStateData testData = new CTestStateData();
   *
   * // Store various types of test data
   * testData.updateDataState("sessionId", "ABC123XYZ");
   * testData.updateDataState("retryCount", 0);
   * testData.updateDataState("lastError", null);
   * testData.updateDataState("testStarted", true);
   *
   * // Update existing values
   * testData.updateDataState("retryCount", 1);  // Updates retry count
   * testData.updateDataState("lastError", new RuntimeException("Connection failed"));
   *
   * }</pre>
   */
  public void updateDataState(String key, Object value) {
    put(key, value);
  }

  /**
   * Retrieves the test state data associated with the specified key and casts it to the expected
   * type. Returns {@code null} if this map contains no mapping for the key.
   *
   * @param <T> the expected type of the returned value
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped, cast to type T, or {@code null} if this
   *     map contains no mapping for the key
   * @throws ClassCastException if the value cannot be cast to the expected type T
   *     <p>This method provides type-safe retrieval of stored values. The caller is responsible for
   *     ensuring the correct type is specified to avoid ClassCastException.
   *     <p>Example usage:
   *     <pre>{@code
   * CTestStateData testData = new CTestStateData();
   *
   * // Store data
   * testData.updateDataState("userId", 42L);
   * testData.updateDataState("userName", "alice.smith");
   * testData.updateDataState("isActive", Boolean.TRUE);
   * testData.updateDataState("scores", Arrays.asList(85, 92, 78));
   *
   * // Retrieve with proper typing
   * Long userId = testData.getDataState("userId");                    // Returns 42L
   * String userName = testData.getDataState("userName");              // Returns "alice.smith"
   * Boolean isActive = testData.getDataState("isActive");             // Returns true
   * List<Integer> scores = testData.getDataState("scores");           // Returns [85, 92, 78]
   *
   * // Handle missing keys
   * String nonExistent = testData.getDataState("missingKey");         // Returns null
   *
   * // Safe null checking
   * String address = testData.getDataState("address");
   * if (address != null) {
   *     System.out.println("Address: " + address);
   * }
   *
   * }</pre>
   */
  public <T> T getDataState(String key) {
    return (T) get(key);
  }
}

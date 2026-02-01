package org.catools.mcp.context;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * McpDataStorage provides a simple key-value storage mechanism that can operate in two modes: -
 * Thread-local mode (default): each thread gets its own independent map via ThreadLocal. -
 * Shared/global mode: all threads share a single global map.
 *
 * <p>Use setSharedBetweenThreads(true) to switch to shared/global mode when multiple threads need
 * to access the same stored data (for example, when running an Mcp server).
 *
 * <p>Note: This class stores McpStorageRecord objects in the maps. The convenience write/read
 * overloads accept different value forms but the underlying storage entries are McpStorageRecord
 * instances.
 *
 * <p>A shutdown hook is registered to clear storage at JVM shutdown to reduce the risk of leaking
 * resources across test runs or process lifecycle.
 */
public class CMcpDataStorage {
  private static final AtomicBoolean sharedBetweenThreads = new AtomicBoolean(false);

  private static final HashMap<String, CMcpStorageRecord> storage = new HashMap<>();

  private static final ThreadLocal<HashMap<String, CMcpStorageRecord>> threadLocalData =
      new ThreadLocal<>();

  static {
    Runtime.getRuntime().addShutdownHook(new Thread(CMcpDataStorage::clear));
  }

  /**
   * Enable or disable sharing of the storage map between threads.
   *
   * @param enabled true to use a single shared/global map for all threads; false to use
   *     thread-local maps
   */
  public static void setSharedBetweenThreads(boolean enabled) {
    sharedBetweenThreads.set(enabled);
  }

  /**
   * Check whether storage is configured to be shared between threads.
   *
   * @return true if global/shared mode is enabled; false if thread-local mode is used
   */
  public static boolean getThreadSafeMode() {
    return sharedBetweenThreads.get();
  }

  /**
   * Store a value associated with the given key. The value is wrapped as an McpStorageRecord.
   *
   * @param clazz String identifier for the schema/class associated with the value; may be used by
   *     consumers
   * @param key The key under which the record will be stored
   * @param value The JSON value to store (wrapped in McpStorageRecord)
   */
  public static void write(String clazz, String key, JsonNode value) {
    getMap().put(key, new CMcpStorageRecord(clazz, key, value));
  }

  /**
   * Store a value associated with the given key. This overload accepts a JSON schema node to build
   * the record.
   *
   * @param schema JSON schema or metadata node associated with the stored value (may be null)
   * @param key The key under which the record will be stored
   * @param value The JSON value to store (wrapped in McpStorageRecord)
   */
  public static void write(JsonNode schema, String key, JsonNode value) {
    getMap().put(key, CMcpStorageRecord.of(schema, key, value));
  }

  /**
   * Store a generic object under the provided key. The object will be wrapped into an
   * McpStorageRecord.
   *
   * @param key The key under which the value will be stored
   * @param value The object to store (wrapped in McpStorageRecord)
   */
  public static void write(String key, Object value) {
    getMap().put(key, CMcpStorageRecord.of(null, key, value));
  }

  /**
   * Read the stored record for the given key.
   *
   * <p>Note: the map stores McpStorageRecord instances. The method signature is generic to allow
   * callers to cast to the expected type, but callers will typically receive an McpStorageRecord.
   *
   * @param key The key to read
   * @param <T> Expected return type (caller should ensure correct casting)
   * @return The stored value (typically an McpStorageRecord) associated with the key, or null if
   *     not found
   */
  @SuppressWarnings("unchecked")
  public static <T> T read(String key) {
    return (T) getMap().get(key);
  }

  /**
   * Remove the mapping for the specified key.
   *
   * @param key The key to remove
   * @return true if an entry was removed, false if the key was not present
   */
  @SuppressWarnings("unchecked")
  public static boolean remove(String key) {
    return null != getMap().remove(key);
  }

  /**
   * Read the value associated with the given key, or compute and store it if absent.
   *
   * <p>If the key is not present, the mappingFunction is invoked and its result is stored using the
   * provided clazz identifier and key.
   *
   * @param clazz Identifier for the schema/class associated with the value; may be used by
   *     consumers
   * @param key The key to read or compute
   * @param mappingFunction Supplier that produces a JsonNode to store when the key is absent
   * @param <T> Expected return type (caller should ensure correct casting)
   * @return The value associated with the key after the call (may be the newly computed value)
   */
  @SuppressWarnings("unchecked")
  public static <T> T read(String clazz, String key, Supplier<JsonNode> mappingFunction) {
    if (!getMap().containsKey(key)) {
      write(clazz, key, mappingFunction.get());
    }
    return read(key);
  }

  /**
   * Read the value associated with the given key, or compute and store it if absent.
   *
   * <p>If the key is not present, the mappingFunction is invoked and its result is stored using the
   * provided clazz identifier and key.
   *
   * @param key The key to read or compute
   * @param mappingFunction Supplier that produces a JsonNode to store when the key is absent
   * @param <T> Expected return type (caller should ensure correct casting)
   * @return The value associated with the key after the call (may be the newly computed value)
   */
  @SuppressWarnings("unchecked")
  public static <T> T read(String key, Supplier<T> mappingFunction) {
    if (!getMap().containsKey(key)) {
      write(key, mappingFunction.get());
    }
    return read(key);
  }

  /**
   * Clear the storage maps.
   *
   * <p>- In shared/global mode, the single global storage map is cleared. - In thread-local mode,
   * the ThreadLocal map for the current thread is removed.
   *
   * <p>This method is also registered as a JVM shutdown hook to ensure cleanup at process exit.
   */
  // Important: Clean up after the test/thread to prevent memory leaks
  public static void clear() {
    storage.clear();
    threadLocalData.remove();
  }

  /**
   * Get a shallow copy of the current storage map for inspection or testing.
   *
   * @return A new HashMap containing the current entries from the active storage map
   */
  public static HashMap<String, CMcpStorageRecord> getCopy() {
    return new HashMap<>(getMap());
  }

  private static HashMap<String, CMcpStorageRecord> getMap() {
    if (sharedBetweenThreads.get()) return storage;

    HashMap<String, CMcpStorageRecord> map = threadLocalData.get();
    if (map == null) {
      map = new HashMap<>();
      threadLocalData.set(map);
    }
    return map;
  }
}

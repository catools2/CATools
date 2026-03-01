package org.catools.common.datastate;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.catools.common.utils.CJsonUtil;

/**
 * CDataState provides a simple key-value storage mechanism that can operate in either thread-safe
 * mode (using ThreadLocal storage) or global mode (shared across all threads). You should turn
 * thread-safe mode off only when you are sure that you want to share data across threads like when
 * using Mcp Servers where multiple threads share data across server.
 */
public class CDataState {
  private static final AtomicBoolean sharedBetweenThreads = new AtomicBoolean(false);

  private static final HashMap<String, Object> storage = new HashMap<>();

  private static final ThreadLocal<HashMap<String, Object>> threadLocalData = new ThreadLocal<>();

  static {
    Runtime.getRuntime().addShutdownHook(new Thread(CDataState::clear));
  }

  /**
   * Enable or disable thread-safe mode.
   *
   * @param enabled
   */
  public static void setSharedBetweenThreads(boolean enabled) {
    sharedBetweenThreads.set(enabled);
  }

  /**
   * Check if thread-safe mode is enabled.
   *
   * @return true if enabled, false otherwise
   */
  public static boolean getThreadSafeMode() {
    return sharedBetweenThreads.get();
  }

  /**
   * Check if thread-safe mode is enabled.
   *
   * @return true if enabled, false otherwise
   */
  public static void write(String key, Object value) {
    getMap().put(key, value);
  }

  /**
   * Read a value from the data state.
   *
   * @param key The key to read
   * @param <T> The expected type of the value
   * @return The value associated with the key, or null if not found
   */
  @SuppressWarnings("unchecked")
  public static <T> T read(String key) {
    return (T) getMap().get(key);
  }

  /**
   * Read a value from the data state.
   *
   * @param key The key to read
   * @return True if value was exist otherwise false
   */
  @SuppressWarnings("unchecked")
  public static boolean remove(String key) {
    return null != getMap().remove(key);
  }

  /**
   * Read a value from the data state, or compute and store it if not present.
   *
   * @param key The key to read
   * @param mappingFunction The function to compute the value if not present
   * @param <T> The expected type of the value
   * @return The value associated with the key
   */
  @SuppressWarnings("unchecked")
  public static <T> T read(String key, Supplier<T> mappingFunction) {
    return (T) getMap().computeIfAbsent(key, s -> mappingFunction.get());
  }

  /** Clear the data state for the current thread. */
  // Important: Clean up after the test/thread to prevent memory leaks
  public static void clear() {
    storage.clear();
    threadLocalData.remove();
  }

  /**
   * Get a copy of the current data state map.
   *
   * @return A cloned copy of the current data state map
   */
  public static HashMap<String, Object> getCopy() {
    return CJsonUtil.clone(getMap());
  }

  private static HashMap<String, Object> getMap() {
    if (sharedBetweenThreads.get()) return storage;

    HashMap<String, Object> map = threadLocalData.get();
    if (map == null) {
      map = new HashMap<>();
      threadLocalData.set(map);
    }
    return map;
  }
}

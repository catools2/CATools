package org.catools.common.collections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.utils.CJsonUtil;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@JsonDeserialize(as = CLinkedMap.class)
public class CLinkedMap<K, V> extends LinkedHashMap<K, V> implements CMap<K, V> {

  public static <A, B> CLinkedMap<A, B> of(Map map) {
    return new CLinkedMap<>(map);
  }

  public static <A, B> CLinkedMap<A, B> of(int initialCapacity) {
    return new CLinkedMap<>(initialCapacity);
  }

  public static <A, B> CLinkedMap<A, B> of(int initialCapacity, float loadFactor) {
    return new CLinkedMap<>(initialCapacity, loadFactor);
  }

  public static <A, B> CLinkedMap<A, B> of(
      int initialCapacity, float loadFactor, boolean accessOrder) {
    return new CLinkedMap<>(initialCapacity, loadFactor, accessOrder);
  }

  public CLinkedMap() {
  }

  public CLinkedMap(java.util.Map m) {
    super(m);
  }

  public CLinkedMap(int initialCapacity) {
    super(initialCapacity);
  }

  public CLinkedMap(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public CLinkedMap(int initialCapacity, float loadFactor, boolean accessOrder) {
    super(initialCapacity, loadFactor, accessOrder);
  }

  /**
   * Returns a {@link CSet} of the keys contained in this map.
   *
   * @return a CSet of the keys contained in this map
   */
  @Override
  public CSet<K> keySet() {
    return new CSet<>(super.keySet());
  }

  /**
   * Returns a {@link CList} of the values contained in this map.
   *
   * @return a {@link CList} of the values contained in this map.
   */
  @Override
  public CList<V> values() {
    return new CList<>(super.values());
  }

  @Override
  public boolean equals(Object c) {
    return c != null
        && c instanceof Map
        && ((Map) c).size() == size()
        && containsAll((Map) c)
        && (CHashMap.of((Map) c)).containsAll(this);
  }

  @Override
  public String toString() {
    try {
      return CJsonUtil.toString(this);
    } catch (Throwable t) {
      return new CList<>(keySet().map(CJsonUtil::toString)).toString();
    }
  }

  @Override
  public boolean _useWaiter() {
    return false;
  }

  @Override
  public Logger getLogger() {
    return log;
  }
}

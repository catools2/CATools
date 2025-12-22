package org.catools.ws.model;

import java.util.Map;
import org.catools.common.collections.CHashMap;

public class CRequestHeaders extends CHashMap<String, String> {
  public CRequestHeaders() {}

  public CRequestHeaders(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public CRequestHeaders(int initialCapacity) {
    super(initialCapacity);
  }

  public CRequestHeaders(Map<? extends String, ? extends String> m) {
    super(m);
  }
}

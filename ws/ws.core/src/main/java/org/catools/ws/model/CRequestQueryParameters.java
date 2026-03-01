package org.catools.ws.model;

import java.util.Map;
import org.catools.common.collections.CHashMap;

public class CRequestQueryParameters extends CHashMap<String, Object> {
  public CRequestQueryParameters() {}

  public CRequestQueryParameters(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public CRequestQueryParameters(int initialCapacity) {
    super(initialCapacity);
  }

  public CRequestQueryParameters(Map<? extends String, ? extends String> m) {
    super(m);
  }
}

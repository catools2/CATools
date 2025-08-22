package org.catools.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CSet;

import java.util.stream.Stream;

public class CResponseHeaders extends CSet<CResponseHeader> {
  public CResponseHeaders() {
  }

  public CResponseHeaders(CResponseHeader... c) {
    super(c);
  }

  public CResponseHeaders(Stream<CResponseHeader> stream) {
    super(stream);
  }

  public CResponseHeaders(Iterable<CResponseHeader> iterable) {
    super(iterable);
  }

  @JsonIgnore
  public String getValue(String name) {
    CResponseHeader firstOrNull =
        getFirstOrNull(h -> StringUtils.equalsIgnoreCase(name, h.getName()));
    if (firstOrNull == null) {
      return "";
    }
    return firstOrNull.getValue();
  }

  @JsonIgnore
  public CResponseHeader getHeader(String name) {
    return getFirstOrNull(h -> StringUtils.equalsIgnoreCase(name, h.getName()));
  }
}

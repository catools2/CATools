package org.catools.etl.tms.model;

import java.util.stream.Stream;
import org.catools.common.collections.CSet;

public class CEtlItems extends CSet<CEtlItem> {
  public CEtlItems() {}

  public CEtlItems(CEtlItem... c) {
    super(c);
  }

  public CEtlItems(Stream<CEtlItem> stream) {
    super(stream);
  }

  public CEtlItems(Iterable<CEtlItem> iterable) {
    super(iterable);
  }
}

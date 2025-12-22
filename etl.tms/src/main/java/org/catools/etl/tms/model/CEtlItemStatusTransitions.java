package org.catools.etl.tms.model;

import java.util.stream.Stream;
import org.catools.common.collections.CSet;

public class CEtlItemStatusTransitions extends CSet<CEtlItemStatusTransition> {
  public CEtlItemStatusTransitions() {}

  public CEtlItemStatusTransitions(CEtlItemStatusTransition... c) {
    super(c);
  }

  public CEtlItemStatusTransitions(Stream<CEtlItemStatusTransition> stream) {
    super(stream);
  }

  public CEtlItemStatusTransitions(Iterable<CEtlItemStatusTransition> iterable) {
    super(iterable);
  }
}

package org.catools.etl.tms.model;

import java.util.stream.Stream;
import org.catools.common.collections.CSet;

public class CEtlExecutions extends CSet<CEtlExecution> {
  public CEtlExecutions() {}

  public CEtlExecutions(CEtlExecution... c) {
    super(c);
  }

  public CEtlExecutions(Stream<CEtlExecution> stream) {
    super(stream);
  }

  public CEtlExecutions(Iterable<CEtlExecution> iterable) {
    super(iterable);
  }
}

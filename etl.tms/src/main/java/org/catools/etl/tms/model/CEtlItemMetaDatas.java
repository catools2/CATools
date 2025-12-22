package org.catools.etl.tms.model;

import java.util.stream.Stream;
import org.catools.common.collections.CSet;

public class CEtlItemMetaDatas extends CSet<CEtlItemMetaData> {
  public CEtlItemMetaDatas() {}

  public CEtlItemMetaDatas(CEtlItemMetaData... c) {
    super(c);
  }

  public CEtlItemMetaDatas(Stream<CEtlItemMetaData> stream) {
    super(stream);
  }

  public CEtlItemMetaDatas(Iterable<CEtlItemMetaData> iterable) {
    super(iterable);
  }
}

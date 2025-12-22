package org.catools.etl.tms.model;

import java.util.Objects;
import java.util.stream.Stream;
import org.catools.common.collections.CSet;

public class CEtlStatuses extends CSet<CEtlStatus> {
  public CEtlStatuses() {}

  public CEtlStatuses(CEtlStatus... c) {
    super(c);
  }

  public CEtlStatuses(Stream<CEtlStatus> stream) {
    super(stream);
  }

  public CEtlStatuses(Iterable<CEtlStatus> iterable) {
    super(iterable);
  }

  public CEtlStatus getByIdOrAddIfNotExists(CEtlStatus newStatus) {
    if (newStatus == null) {
      return null;
    }
    CEtlStatus status = getFirstOrNull(u -> Objects.equals(u.getId(), newStatus.getId()));
    if (status != null) {
      return status;
    }
    add(newStatus);
    return newStatus;
  }
}

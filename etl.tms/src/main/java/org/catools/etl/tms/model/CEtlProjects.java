package org.catools.etl.tms.model;

import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CSet;

public class CEtlProjects extends CSet<CEtlProject> {
  public CEtlProjects() {}

  public CEtlProjects(CEtlProject... c) {
    super(c);
  }

  public CEtlProjects(Stream<CEtlProject> stream) {
    super(stream);
  }

  public CEtlProjects(Iterable<CEtlProject> iterable) {
    super(iterable);
  }

  public CEtlProject getByName(String name) {
    return getFirstOrNull(p -> StringUtils.equalsIgnoreCase(p.getName(), name));
  }

  public CEtlProject getByName(CEtlProject project) {
    return getFirstOrNull(p -> p.equals(project));
  }
}

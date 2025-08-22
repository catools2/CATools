package org.catools.etl.tms.model;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CSet;

import java.util.Objects;
import java.util.stream.Stream;

public class CEtlVersions extends CSet<CEtlVersion> {
  public CEtlVersions() {
  }

  public CEtlVersions(CEtlVersion... c) {
    super(c);
  }

  public CEtlVersions(Stream<CEtlVersion> stream) {
    super(stream);
  }

  public CEtlVersions(Iterable<CEtlVersion> iterable) {
    super(iterable);
  }

  public CEtlVersion getByNameForProject(String name) {
    return getFirst(v -> StringUtils.equalsIgnoreCase(name, v.getName()));
  }

  public CEtlVersion getByNameForProject(String name, CEtlProject project) {
    Objects.requireNonNull(project);
    return getFirst(
        v -> StringUtils.equalsIgnoreCase(name, v.getName()) && v.getProject().equals(project));
  }
}

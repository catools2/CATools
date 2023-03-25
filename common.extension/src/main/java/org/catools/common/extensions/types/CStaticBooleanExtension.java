package org.catools.common.extensions.types;

import org.catools.common.extensions.states.interfaces.CBooleanState;
import org.catools.common.extensions.verify.interfaces.CBooleanVerifier;
import org.catools.common.extensions.wait.interfaces.CBooleanWaiter;

/**
 * CStaticBooleanExtension is an central interface where we extend all boolean related interfaces so
 * adding new functionality will be much easier.
 */
public abstract class CStaticBooleanExtension
    implements CBooleanWaiter, CBooleanVerifier, CBooleanState {

  @Override
  public boolean _useWaiter() {
    return false;
  }

  @Override
  public int hashCode() {
    return get().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;

    if (!(obj instanceof Boolean))
      return false;

    return isEqual((Boolean) obj);
  }

  @Override
  public String toString() {
    return get().toString();
  }
}

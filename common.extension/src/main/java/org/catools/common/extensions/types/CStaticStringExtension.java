package org.catools.common.extensions.types;

import org.catools.common.extensions.states.interfaces.CStringState;
import org.catools.common.extensions.verify.interfaces.CStringVerifier;
import org.catools.common.extensions.wait.interfaces.CStringWaiter;

/**
 * CStaticStringExtension is an central interface where we extend all String related interfaces so
 * adding new functionality will be much easier.
 */
public abstract class CStaticStringExtension
    implements CStringWaiter, CStringVerifier, CStringState {

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

    if (!(obj instanceof String))
      return false;

    return isEqual((String) obj);
  }

  @Override
  public String toString() {
    return get();
  }
}

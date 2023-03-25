package org.catools.common.extensions.types;

import org.catools.common.extensions.states.interfaces.CNumberState;
import org.catools.common.extensions.verify.interfaces.CNumberVerifier;
import org.catools.common.extensions.wait.interfaces.CNumberWaiter;

/**
 * CStaticNumberExtension is an central interface where we extend all Number related interfaces so
 * adding new functionality will be much easier.
 */
public abstract class CStaticNumberExtension<N extends Number & Comparable<N>>
    implements CNumberWaiter<N>, CNumberVerifier<N>, CNumberState<N> {

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

    try {
      return isEqual((N) obj);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return get().toString();
  }
}

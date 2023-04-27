package org.catools.common.extensions.types;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.states.interfaces.CNumberState;
import org.catools.common.extensions.verify.interfaces.CNumberVerifier;
import org.catools.common.extensions.wait.interfaces.CNumberWaiter;
import org.slf4j.Logger;

/**
 * CStaticNumberExtension is an central interface where we extend all Number related interfaces so
 * adding new functionality will be much easier.
 */
@Slf4j
public abstract class CStaticNumberExtension<N extends Number & Comparable<N>> implements CNumberWaiter<N>, CNumberVerifier<N>, CNumberState<N> {

  @Override
  public boolean _useWaiter() {
    return false;
  }

  @Override
  public int hashCode() {
    return get().hashCode();
  }

  @Override
  public Logger getLogger() {
    return log;
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

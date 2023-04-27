package org.catools.common.extensions.types;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.extensions.states.interfaces.CObjectState;
import org.catools.common.extensions.verify.interfaces.CObjectVerifier;
import org.catools.common.extensions.wait.interfaces.CObjectWaiter;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * CStaticObjectExtension is an central interface where we extend all Object related interfaces so
 * adding new functionality will be much easier.
 */
@Slf4j
public abstract class CStaticObjectExtension<O> implements CObjectWaiter<O>, CObjectVerifier<O, CObjectState<O>>, CObjectState<O> {

  @Override
  public boolean _useWaiter() {
    return false;
  }

  @Override
  public boolean isEqual(O expected) {
    return Objects.equals(get(), expected);
  }

  @Override
  public Logger getLogger() {
    return log;
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
      return isEqual((O) obj);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return get().toString();
  }

  @Override
  public CObjectState<O> _toState(Object o) {
    return new CObjectState() {
      @Override
      public boolean isEqual(Object expected) {
        return Objects.equals(get(), expected);
      }

      @Override
      public Object get() {
        return o;
      }
    };
  }
}

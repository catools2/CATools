package org.catools.common.extensions.types;

import org.catools.common.extensions.states.interfaces.CObjectState;
import org.catools.common.extensions.waitVerify.interfaces.CObjectWaitVerifier;

import java.util.Objects;

/**
 * CDynamicObjectExtension is an central interface where we extend all Object related interfaces so
 * adding new functionality will be much easier.
 */
public abstract class CDynamicObjectExtension<O extends Object> extends CStaticObjectExtension<O>
    implements CObjectWaitVerifier<O, CObjectState<O>> {

  @Override
  public boolean _useWaiter() {
    return true;
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

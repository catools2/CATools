package org.catools.common.extensions.types;

import org.catools.common.extensions.waitVerify.interfaces.CNumberWaitVerifier;

/**
 * CDynamicNumberExtension is an central interface where we extend all Number related interfaces so
 * adding new functionality will be much easier.
 */
public abstract class CDynamicNumberExtension<N extends Number & Comparable<N>>
    extends CStaticNumberExtension<N> implements CNumberWaitVerifier<N> {

  @Override
  public boolean _useWaiter() {
    return true;
  }
}

package org.catools.common.extensions.types;

import org.catools.common.extensions.waitVerify.interfaces.CStringWaitVerifier;

/**
 * CDynamicStringExtension is an central interface where we extend all String related interfaces so
 * adding new functionality will be much easier.
 */
public abstract class CDynamicStringExtension extends CStaticStringExtension implements CStringWaitVerifier {

  @Override
  public boolean _useWaiter() {
    return true;
  }
}

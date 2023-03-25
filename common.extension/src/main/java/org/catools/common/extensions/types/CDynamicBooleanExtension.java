package org.catools.common.extensions.types;

import org.catools.common.extensions.waitVerify.interfaces.CBooleanWaitVerifier;

/**
 * CDynamicBooleanExtension is an central interface where we extend all boolean related interfaces
 * so adding new functionality will be much easier.
 */
public abstract class CDynamicBooleanExtension extends CStaticBooleanExtension
    implements CBooleanWaitVerifier {

  @Override
  public boolean _useWaiter() {
    return true;
  }
}

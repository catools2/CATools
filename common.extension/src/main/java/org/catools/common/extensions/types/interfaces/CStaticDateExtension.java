package org.catools.common.extensions.types.interfaces;

import org.catools.common.extensions.states.interfaces.CDateState;
import org.catools.common.extensions.verify.interfaces.CDateVerifier;
import org.catools.common.extensions.wait.interfaces.CDateWaiter;

/**
 * CStaticDateExtension is an central interface where we extend all Date related interfaces so
 * adding new functionality will be much easier. <strong>
 *
 * <p>Java does not allow to override Object methods in interface level so this is something we
 * should care about it manually.
 *
 * <p>Make sure to override equals, hashCode (and if needed toString methods) in your
 * implementations. <code>
 *
 * @Override public int hashCode() {
 * return getValue().hashCode();
 * }
 * @Override public boolean equals(Object obj) {
 * return isEqual(obj);
 * }
 * </code> </strong>
 */
public interface CStaticDateExtension extends CDateWaiter, CDateVerifier, CDateState {
  @Override
  default boolean _useWaiter() {
    return false;
  }
}

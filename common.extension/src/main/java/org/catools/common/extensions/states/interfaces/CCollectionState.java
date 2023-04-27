package org.catools.common.extensions.states.interfaces;

import java.util.Collection;

/**
 * CCollectionState is an interface for Collection state related methods.
 *
 * <p>We need this interface to have possibility of adding state to any exists objects with the
 * minimum change in the code.
 */
public interface CCollectionState<E> extends CIterableState<E>, CObjectState<Iterable<E>> {

  /**
   * Verify the map size is equal to expected value.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean sizeEquals(int expected) {
    return ((Collection) get()).size() == expected;
  }

  /**
   * Check if actual has value greater than expected.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean sizeIsGreaterThan(int expected) {
    return ((Collection) get()).size() > expected;
  }

  /**
   * Check if actual has value less than expected.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean sizeIsLessThan(int expected) {
    return ((Collection) get()).size() < expected;
  }
}

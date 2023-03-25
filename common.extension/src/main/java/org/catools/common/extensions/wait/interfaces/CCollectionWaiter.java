package org.catools.common.extensions.wait.interfaces;

import org.catools.common.extensions.states.interfaces.CCollectionState;

import java.util.Collection;

/**
 * CCollectionWaiter is an interface for Collection waiter related methods.
 */
public interface CCollectionWaiter<E> extends CIterableWaiter<E>, CObjectWaiter<Iterable<E>> {

  /**
   * Wait for {@code CTypeExtensionConfigs.getDefaultWaitInSeconds()} number of milliseconds till
   * the map size is equal to expected value.
   *
   * @param expected value to compare
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeEquals(final int expected) {
    return waitSizeEquals(
        expected, getDefaultWaitInSeconds(), getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Wait for defined number of seconds till the map size is equal to expected value.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeEquals(final int expected, final int waitInSeconds) {
    return waitSizeEquals(expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Wait for defined number of seconds till the map size is equal to expected value.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeEquals(
      final int expected, final int waitInSeconds, final int intervalInMilliSeconds) {
    return _waiter(o -> toState(o).sizeEquals(expected), waitInSeconds, intervalInMilliSeconds);
  }

  /**
   * Wait for {@code CTypeExtensionConfigs.getDefaultWaitInSeconds()} number of milliseconds till
   * the actual has value greater than expected.
   *
   * @param expected value to compare
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeIsGreaterThan(final int expected) {
    return waitSizeIsGreaterThan(
        expected, getDefaultWaitInSeconds(), getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Wait for defined number of seconds till the actual has value greater than expected.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeIsGreaterThan(final int expected, final int waitInSeconds) {
    return waitSizeIsGreaterThan(expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Wait for defined number of seconds till the actual has value greater than expected.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeIsGreaterThan(
      final int expected, final int waitInSeconds, final int intervalInMilliSeconds) {
    return _waiter(
        o -> toState(o).sizeIsGreaterThan(expected), waitInSeconds, intervalInMilliSeconds);
  }

  /**
   * Wait for {@code CTypeExtensionConfigs.getDefaultWaitInSeconds()} number of milliseconds till
   * the actual has value less than expected.
   *
   * @param expected value to compare
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeIsLessThan(final int expected) {
    return waitSizeIsLessThan(
        expected, getDefaultWaitInSeconds(), getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Wait for defined number of seconds till the actual has value less than expected.
   *
   * @param expected      value to compare
   * @param waitInSeconds maximum wait time
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeIsLessThan(final int expected, final int waitInSeconds) {
    return waitSizeIsLessThan(expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
  }

  /**
   * Wait for defined number of seconds till the actual has value less than expected.
   *
   * @param expected               value to compare
   * @param waitInSeconds          maximum wait time
   * @param intervalInMilliSeconds interval between retries in milliseconds
   * @return true if wait operation succeed otherwise return false
   */
  default boolean waitSizeIsLessThan(
      final int expected, final int waitInSeconds, final int intervalInMilliSeconds) {
    return _waiter(o -> toState(o).sizeIsLessThan(expected), waitInSeconds, intervalInMilliSeconds);
  }

  private CCollectionState<E> toState(Object e) {
    return () -> (Collection<E>) e;
  }
}

package org.catools.common.extensions.wait.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.catools.common.extensions.CTypeExtensionConfigs;
import org.catools.common.extensions.states.interfaces.CBaseState;

/**
 * CBaseWaiter is an interface to hold shared method between all waiter classes.
 */
public interface CBaseWaiter<O> extends CBaseState<O> {

  /**
   * The default interval in milliseconds during wait for defined state. It is possible to set this
   * value for whole system using {@code EXTENSION_DEFAULT_WAIT_INTERVAL_IN_MILLIS} config parameter
   * or just overwrite this method for particular implementation
   *
   * @return the interval time in milliseconds
   */
  @JsonIgnore
  default int getDefaultWaitIntervalInMilliSeconds() {
    return CTypeExtensionConfigs.getDefaultWaitIntervalInMilliSeconds();
  }

  /**
   * The default max time to wait for defined state. It is possible to set this value for whole
   * system using {@code EXTENSION_DEFAULT_WAIT_IN_SECONDS} config parameter or just overwrite this
   * method for particular implementation.
   *
   * @return max time to wait for defined state.
   */
  @JsonIgnore
  default int getDefaultWaitInSeconds() {
    return CTypeExtensionConfigs.getDefaultWaitInSeconds();
  }
}

package org.catools.common.extensions.states.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * CBaseExtension is an interface to hold shared method between all extensions.
 */
public interface CBaseState<O> extends Serializable {
  long serialVersionUID = 6067874018185613757L;

  /**
   * For internal use only
   */
  @JsonIgnore
  O get();
}

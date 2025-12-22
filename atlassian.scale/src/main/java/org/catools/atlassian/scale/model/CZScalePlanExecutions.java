package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of Scale plan executions.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation for managing
 * {@link CZScalePlanExecution} objects. It includes constructors for creating the collection from
 * various input sources, such as arrays, streams, or iterables.
 */
public class CZScalePlanExecutions extends CSet<CZScalePlanExecution> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of Scale plan executions.
   */
  public CZScalePlanExecutions() {}

  /**
   * Constructs a collection of Scale plan executions from an array of executions.
   *
   * @param c an array of {@link CZScalePlanExecution} objects to initialize the collection
   */
  public CZScalePlanExecutions(CZScalePlanExecution... c) {
    super(c);
  }

  /**
   * Constructs a collection of Scale plan executions from a stream of executions.
   *
   * @param stream a {@link Stream} of {@link CZScalePlanExecution} objects to initialize the
   *     collection
   */
  public CZScalePlanExecutions(Stream<CZScalePlanExecution> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of Scale plan executions from an iterable of executions.
   *
   * @param iterable an {@link Iterable} of {@link CZScalePlanExecution} objects to initialize the
   *     collection
   */
  public CZScalePlanExecutions(Iterable<CZScalePlanExecution> iterable) {
    super(iterable);
  }
}

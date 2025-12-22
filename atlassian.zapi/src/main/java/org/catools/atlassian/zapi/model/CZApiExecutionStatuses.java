package org.catools.atlassian.zapi.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of execution statuses in the ZAPI system.
 *
 * <p>This class extends {@link CSet} to provide additional functionality for managing a set of
 * {@link CZApiExecutionStatus} objects, including constructors for various input types.
 */
public class CZApiExecutionStatuses extends CSet<CZApiExecutionStatus> {

  /** Default constructor for creating an empty collection of execution statuses. */
  public CZApiExecutionStatuses() {}

  /**
   * Constructs a collection of execution statuses from an array of {@link CZApiExecutionStatus}
   * objects.
   *
   * @param c an array of execution statuses to initialize the collection
   */
  public CZApiExecutionStatuses(CZApiExecutionStatus... c) {
    super(c);
  }

  /**
   * Constructs a collection of execution statuses from a {@link Stream} of {@link
   * CZApiExecutionStatus} objects.
   *
   * @param stream a stream of execution statuses to initialize the collection
   */
  public CZApiExecutionStatuses(Stream<CZApiExecutionStatus> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of execution statuses from an {@link Iterable} of {@link
   * CZApiExecutionStatus} objects.
   *
   * @param iterable an iterable of execution statuses to initialize the collection
   */
  public CZApiExecutionStatuses(Iterable<CZApiExecutionStatus> iterable) {
    super(iterable);
  }
}

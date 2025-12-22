package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of Scale change histories.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation for managing
 * {@link CZScaleChangeHistory} objects. It includes constructors for creating the collection from
 * various input sources, such as arrays, streams, or iterables.
 */
public class CZScaleChangeHistories extends CSet<CZScaleChangeHistory> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of Scale change histories.
   */
  public CZScaleChangeHistories() {}

  /**
   * Constructs a collection of Scale change histories from an array of change histories.
   *
   * @param c an array of {@link CZScaleChangeHistory} objects to initialize the collection
   */
  public CZScaleChangeHistories(CZScaleChangeHistory... c) {
    super(c);
  }

  /**
   * Constructs a collection of Scale change histories from a stream of change histories.
   *
   * @param stream a {@link Stream} of {@link CZScaleChangeHistory} objects to initialize the
   *     collection
   */
  public CZScaleChangeHistories(Stream<CZScaleChangeHistory> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of Scale change histories from an iterable of change histories.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleChangeHistory} objects to initialize the
   *     collection
   */
  public CZScaleChangeHistories(Iterable<CZScaleChangeHistory> iterable) {
    super(iterable);
  }
}

package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of change history items in the Scale system.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation
 * for managing {@link CZScaleChangeHistoryItem} objects. It includes constructors
 * for creating the collection from various input sources, such as arrays, streams,
 * or iterables.</p>
 */
public class CZScaleChangeHistoryItems extends CSet<CZScaleChangeHistoryItem> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of change history items.</p>
   */
  public CZScaleChangeHistoryItems() {
  }

  /**
   * Constructs a collection of change history items from an array of items.
   *
   * @param c an array of {@link CZScaleChangeHistoryItem} objects to initialize the collection
   */
  public CZScaleChangeHistoryItems(CZScaleChangeHistoryItem... c) {
    super(c);
  }

  /**
   * Constructs a collection of change history items from a stream of items.
   *
   * @param stream a {@link Stream} of {@link CZScaleChangeHistoryItem} objects to initialize the collection
   */
  public CZScaleChangeHistoryItems(Stream<CZScaleChangeHistoryItem> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of change history items from an iterable of items.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleChangeHistoryItem} objects to initialize the collection
   */
  public CZScaleChangeHistoryItems(Iterable<CZScaleChangeHistoryItem> iterable) {
    super(iterable);
  }
}
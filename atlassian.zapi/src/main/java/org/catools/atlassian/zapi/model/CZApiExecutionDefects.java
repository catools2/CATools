package org.catools.atlassian.zapi.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of defects associated with ZAPI executions.
 *
 * <p>This class extends {@link CSet} to provide additional functionality for managing a set of
 * {@link CZApiExecutionDefect} objects, including constructors for various input types.
 */
public class CZApiExecutionDefects extends CSet<CZApiExecutionDefect> {

  /** Default constructor for creating an empty collection of execution defects. */
  public CZApiExecutionDefects() {}

  /**
   * Constructs a collection of execution defects from an array of {@link CZApiExecutionDefect}
   * objects.
   *
   * @param c an array of execution defects to initialize the collection
   */
  public CZApiExecutionDefects(CZApiExecutionDefect... c) {
    super(c);
  }

  /**
   * Constructs a collection of execution defects from a {@link Stream} of {@link
   * CZApiExecutionDefect} objects.
   *
   * @param stream a stream of execution defects to initialize the collection
   */
  public CZApiExecutionDefects(Stream<CZApiExecutionDefect> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of execution defects from an {@link Iterable} of {@link
   * CZApiExecutionDefect} objects.
   *
   * @param iterable an iterable of execution defects to initialize the collection
   */
  public CZApiExecutionDefects(Iterable<CZApiExecutionDefect> iterable) {
    super(iterable);
  }
}

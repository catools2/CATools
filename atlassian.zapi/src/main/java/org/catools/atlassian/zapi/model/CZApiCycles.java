package org.catools.atlassian.zapi.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of ZAPI test cycles.
 *
 * <p>This class extends {@link CSet} to provide additional functionality for managing
 * a set of {@link CZApiCycle} objects, including constructors for various input types
 * and a method to retrieve a cycle by its unique identifier.</p>
 */
public class CZApiCycles extends CSet<CZApiCycle> {

  /**
   * Default constructor for creating an empty collection of test cycles.
   */
  public CZApiCycles() {
  }

  /**
   * Constructs a collection of test cycles from an array of {@link CZApiCycle} objects.
   *
   * @param c an array of test cycles to initialize the collection
   */
  public CZApiCycles(CZApiCycle... c) {
    super(c);
  }

  /**
   * Constructs a collection of test cycles from a {@link Stream} of {@link CZApiCycle} objects.
   *
   * @param stream a stream of test cycles to initialize the collection
   */
  public CZApiCycles(Stream<CZApiCycle> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of test cycles from an {@link Iterable} of {@link CZApiCycle} objects.
   *
   * @param iterable an iterable of test cycles to initialize the collection
   */
  public CZApiCycles(Iterable<CZApiCycle> iterable) {
    super(iterable);
  }

  /**
   * Retrieves a test cycle by its unique identifier.
   *
   * @param id the unique identifier of the test cycle
   * @return the test cycle with the specified ID, or {@code null} if not found
   */
  public CZApiCycle getById(long id) {
    return getFirstOrNull(v -> v.getId() == id);
  }
}
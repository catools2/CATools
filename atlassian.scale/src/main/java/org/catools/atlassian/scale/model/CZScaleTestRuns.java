package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of test runs in the Scale system.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation for managing
 * {@link CZScaleTestRun} objects. It includes constructors for creating the collection from various
 * input sources, such as arrays, streams, or iterables. Additionally, it provides a method to
 * retrieve a test run by its key.
 */
public class CZScaleTestRuns extends CSet<CZScaleTestRun> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of test runs.
   */
  public CZScaleTestRuns() {}

  /**
   * Constructs a collection of test runs from an array of test runs.
   *
   * @param c an array of {@link CZScaleTestRun} objects to initialize the collection
   */
  public CZScaleTestRuns(CZScaleTestRun... c) {
    super(c);
  }

  /**
   * Constructs a collection of test runs from a stream of test runs.
   *
   * @param stream a {@link Stream} of {@link CZScaleTestRun} objects to initialize the collection
   */
  public CZScaleTestRuns(Stream<CZScaleTestRun> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of test runs from an iterable of test runs.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleTestRun} objects to initialize the
   *     collection
   */
  public CZScaleTestRuns(Iterable<CZScaleTestRun> iterable) {
    super(iterable);
  }

  /**
   * Retrieves the first test run that matches the specified key.
   *
   * @param key the unique key of the test run to retrieve
   * @return the {@link CZScaleTestRun} with the specified key, or null if no match is found
   */
  public CZScaleTestRun getById(String key) {
    return getFirstOrNull(v -> v.getKey().equals(key));
  }
}

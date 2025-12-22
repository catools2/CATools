package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of test results in the Scale system.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation for managing
 * {@link CZScaleTestResult} objects. It includes constructors for creating the collection from
 * various input sources, such as arrays, streams, or iterables.
 */
public class CZScaleTestResults extends CSet<CZScaleTestResult> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of test results.
   */
  public CZScaleTestResults() {}

  /**
   * Constructs a collection of test results from an array of test results.
   *
   * @param c an array of {@link CZScaleTestResult} objects to initialize the collection
   */
  public CZScaleTestResults(CZScaleTestResult... c) {
    super(c);
  }

  /**
   * Constructs a collection of test results from a stream of test results.
   *
   * @param stream a {@link Stream} of {@link CZScaleTestResult} objects to initialize the
   *     collection
   */
  public CZScaleTestResults(Stream<CZScaleTestResult> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of test results from an iterable of test results.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleTestResult} objects to initialize the
   *     collection
   */
  public CZScaleTestResults(Iterable<CZScaleTestResult> iterable) {
    super(iterable);
  }
}

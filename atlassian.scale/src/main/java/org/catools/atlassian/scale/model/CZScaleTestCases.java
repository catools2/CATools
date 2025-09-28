package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of test cases in the Scale system.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation
 * for managing {@link CZScaleTestCase} objects. It includes constructors
 * for creating the collection from various input sources, such as arrays, streams,
 * or iterables.</p>
 */
public class CZScaleTestCases extends CSet<CZScaleTestCase> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of test cases.</p>
   */
  public CZScaleTestCases() {
  }

  /**
   * Constructs a collection of test cases from an array of test cases.
   *
   * @param c an array of {@link CZScaleTestCase} objects to initialize the collection
   */
  public CZScaleTestCases(CZScaleTestCase... c) {
    super(c);
  }

  /**
   * Constructs a collection of test cases from a stream of test cases.
   *
   * @param stream a {@link Stream} of {@link CZScaleTestCase} objects to initialize the collection
   */
  public CZScaleTestCases(Stream<CZScaleTestCase> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of test cases from an iterable of test cases.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleTestCase} objects to initialize the collection
   */
  public CZScaleTestCases(Iterable<CZScaleTestCase> iterable) {
    super(iterable);
  }
}
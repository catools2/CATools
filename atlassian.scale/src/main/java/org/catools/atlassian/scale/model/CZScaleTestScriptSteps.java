package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of test script steps in the Scale system.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation
 * for managing {@link CZScaleTestScriptStep} objects. It includes constructors
 * for creating the collection from various input sources, such as arrays, streams,
 * or iterables.</p>
 */
public class CZScaleTestScriptSteps extends CSet<CZScaleTestScriptStep> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of test script steps.</p>
   */
  public CZScaleTestScriptSteps() {
  }

  /**
   * Constructs a collection of test script steps from an array of test script steps.
   *
   * @param c an array of {@link CZScaleTestScriptStep} objects to initialize the collection
   */
  public CZScaleTestScriptSteps(CZScaleTestScriptStep... c) {
    super(c);
  }

  /**
   * Constructs a collection of test script steps from a stream of test script steps.
   *
   * @param stream a {@link Stream} of {@link CZScaleTestScriptStep} objects to initialize the collection
   */
  public CZScaleTestScriptSteps(Stream<CZScaleTestScriptStep> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of test script steps from an iterable of test script steps.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleTestScriptStep} objects to initialize the collection
   */
  public CZScaleTestScriptSteps(Iterable<CZScaleTestScriptStep> iterable) {
    super(iterable);
  }
}
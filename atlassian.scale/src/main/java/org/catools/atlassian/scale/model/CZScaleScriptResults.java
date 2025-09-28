package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of script execution results in the Scale system.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation
 * for managing {@link CZScaleScriptResult} objects. It includes constructors
 * for creating the collection from various input sources, such as arrays, streams,
 * or iterables. Additionally, it provides a method to retrieve a script result
 * by its index.</p>
 */
public class CZScaleScriptResults extends CSet<CZScaleScriptResult> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of script execution results.</p>
   */
  public CZScaleScriptResults() {
  }

  /**
   * Constructs a collection of script execution results from an array of results.
   *
   * @param c an array of {@link CZScaleScriptResult} objects to initialize the collection
   */
  public CZScaleScriptResults(CZScaleScriptResult... c) {
    super(c);
  }

  /**
   * Constructs a collection of script execution results from a stream of results.
   *
   * @param stream a {@link Stream} of {@link CZScaleScriptResult} objects to initialize the collection
   */
  public CZScaleScriptResults(Stream<CZScaleScriptResult> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of script execution results from an iterable of results.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleScriptResult} objects to initialize the collection
   */
  public CZScaleScriptResults(Iterable<CZScaleScriptResult> iterable) {
    super(iterable);
  }

  /**
   * Retrieves the first script execution result that matches the specified index.
   *
   * @param index the index of the script execution result to retrieve
   * @return the {@link CZScaleScriptResult} with the specified index, or null if no match is found
   */
  public CZScaleScriptResult getByIndex(int index) {
    return getFirstOrNull(v -> v.getIndex() == index);
  }
}
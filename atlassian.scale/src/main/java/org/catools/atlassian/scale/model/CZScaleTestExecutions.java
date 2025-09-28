package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of test executions in the Scale system.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation
 * for managing {@link CZScaleTestExecution} objects. It includes constructors
 * for creating the collection from various input sources, such as arrays, streams,
 * or iterables. Additionally, it provides methods to retrieve a test execution
 * by its ID or filter executions based on issue IDs.</p>
 */
public class CZScaleTestExecutions extends CSet<CZScaleTestExecution> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of test executions.</p>
   */
  public CZScaleTestExecutions() {
  }

  /**
   * Constructs a collection of test executions from an array of test executions.
   *
   * @param c an array of {@link CZScaleTestExecution} objects to initialize the collection
   */
  public CZScaleTestExecutions(CZScaleTestExecution... c) {
    super(c);
  }

  /**
   * Constructs a collection of test executions from a stream of test executions.
   *
   * @param stream a {@link Stream} of {@link CZScaleTestExecution} objects to initialize the collection
   */
  public CZScaleTestExecutions(Stream<CZScaleTestExecution> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of test executions from an iterable of test executions.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleTestExecution} objects to initialize the collection
   */
  public CZScaleTestExecutions(Iterable<CZScaleTestExecution> iterable) {
    super(iterable);
  }

  /**
   * Retrieves the first test execution that matches the specified ID.
   *
   * @param id the unique identifier of the test execution to retrieve
   * @return the {@link CZScaleTestExecution} with the specified ID, or null if no match is found
   */
  public CZScaleTestExecution getById(long id) {
    return getFirstOrNull(v -> v.getId() == id);
  }

  /**
   * Retrieves a collection of test executions that match the specified issue IDs.
   *
   * @param issueIds a {@link CSet} of issue IDs to filter the test executions
   * @return a new {@link CZScaleTestExecutions} object containing the filtered test executions
   */
  public CZScaleTestExecutions getExecution(CSet<String> issueIds) {
    return new CZScaleTestExecutions(getAll(e -> issueIds.contains(e.getTestCaseKey())));
  }
}
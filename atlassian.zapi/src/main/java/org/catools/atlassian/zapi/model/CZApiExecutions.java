package org.catools.atlassian.zapi.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of ZAPI executions.
 *
 * <p>This class extends {@link CSet} to provide additional functionality for managing
 * a set of {@link CZApiExecution} objects, including constructors for various input types
 * and methods to retrieve executions by specific criteria.</p>
 */
public class CZApiExecutions extends CSet<CZApiExecution> {

  /**
   * Default constructor for creating an empty collection of executions.
   */
  public CZApiExecutions() {
  }

  /**
   * Constructs a collection of executions from an array of {@link CZApiExecution} objects.
   *
   * @param c an array of executions to initialize the collection
   */
  public CZApiExecutions(CZApiExecution... c) {
    super(c);
  }

  /**
   * Constructs a collection of executions from a {@link Stream} of {@link CZApiExecution} objects.
   *
   * @param stream a stream of executions to initialize the collection
   */
  public CZApiExecutions(Stream<CZApiExecution> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of executions from an {@link Iterable} of {@link CZApiExecution} objects.
   *
   * @param iterable an iterable of executions to initialize the collection
   */
  public CZApiExecutions(Iterable<CZApiExecution> iterable) {
    super(iterable);
  }

  /**
   * Retrieves an execution by its unique identifier.
   *
   * @param id the unique identifier of the execution
   * @return the execution with the specified ID, or {@code null} if not found
   */
  public CZApiExecution getById(long id) {
    return getFirstOrNull(v -> v.getId() == id);
  }

  /**
   * Retrieves a collection of executions based on a set of issue keys.
   *
   * @param issueIds a set of issue keys to filter the executions
   * @return a new {@link CZApiExecutions} object containing the filtered executions
   */
  public CZApiExecutions getExecution(CSet<String> issueIds) {
    return new CZApiExecutions(getAll(e -> issueIds.contains(e.getIssueKey())));
  }
}

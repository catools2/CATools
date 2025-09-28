package org.catools.atlassian.zapi.model;

import org.apache.commons.lang3.StringUtils;
import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of projects in the ZAPI system.
 *
 * <p>This class extends {@link CSet} to provide additional functionality for managing
 * a set of {@link CZApiProject} objects, including constructors for various input types
 * and methods to retrieve projects by specific criteria.</p>
 */
public class CZApiProjects extends CSet<CZApiProject> {

  /**
   * Default constructor for creating an empty collection of projects.
   */
  public CZApiProjects() {
  }

  /**
   * Constructs a collection of projects from an array of {@link CZApiProject} objects.
   *
   * @param c an array of projects to initialize the collection
   */
  public CZApiProjects(CZApiProject... c) {
    super(c);
  }

  /**
   * Constructs a collection of projects from a {@link Stream} of {@link CZApiProject} objects.
   *
   * @param stream a stream of projects to initialize the collection
   */
  public CZApiProjects(Stream<CZApiProject> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of projects from an {@link Iterable} of {@link CZApiProject} objects.
   *
   * @param iterable an iterable of projects to initialize the collection
   */
  public CZApiProjects(Iterable<CZApiProject> iterable) {
    super(iterable);
  }

  /**
   * Retrieves a project by its unique identifier.
   *
   * @param id the unique identifier of the project
   * @return the project with the specified ID, or {@code null} if not found
   */
  public CZApiProject getById(long id) {
    return getFirstOrNull(v -> v.getId() == id);
  }

  /**
   * Retrieves a project by its name.
   *
   * @param name the name of the project
   * @return the project with the specified name, or {@code null} if not found
   */
  public CZApiProject getByName(String name) {
    return getFirstOrNull(v -> StringUtils.equals(v.getName(), name));
  }
}

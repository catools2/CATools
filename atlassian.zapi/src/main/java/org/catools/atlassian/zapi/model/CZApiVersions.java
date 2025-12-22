package org.catools.atlassian.zapi.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of versions in the ZAPI system.
 *
 * <p>This class extends {@link CSet} to provide additional functionality for managing a set of
 * {@link CZApiVersion} objects, including constructors for various input types and a method to
 * retrieve a version by its unique identifier.
 */
public class CZApiVersions extends CSet<CZApiVersion> {

  /** Default constructor for creating an empty collection of versions. */
  public CZApiVersions() {}

  /**
   * Constructs a collection of versions from an array of {@link CZApiVersion} objects.
   *
   * @param c an array of versions to initialize the collection
   */
  public CZApiVersions(CZApiVersion... c) {
    super(c);
  }

  /**
   * Constructs a collection of versions from a {@link Stream} of {@link CZApiVersion} objects.
   *
   * @param stream a stream of versions to initialize the collection
   */
  public CZApiVersions(Stream<CZApiVersion> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of versions from an {@link Iterable} of {@link CZApiVersion} objects.
   *
   * @param iterable an iterable of versions to initialize the collection
   */
  public CZApiVersions(Iterable<CZApiVersion> iterable) {
    super(iterable);
  }

  /**
   * Retrieves a version by its unique identifier.
   *
   * @param id the unique identifier of the version
   * @return the version with the specified ID, or {@code null} if not found
   */
  public CZApiVersion getById(long id) {
    return getFirstOrNull(v -> v.getId() == id);
  }
}

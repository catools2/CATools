package org.catools.atlassian.zapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Represents the versions of a project in the ZAPI system.
 *
 * <p>This class provides methods to manage and retrieve both released and unreleased versions of a
 * project, as well as utility methods for equality checks and string representation.
 */
public class CZApiProjectVersions {

  /** A collection of unreleased versions associated with the project. */
  private CZApiVersions unreleasedVersions;

  /** A collection of released versions associated with the project. */
  private CZApiVersions releasedVersions;

  /**
   * Retrieves the collection of unreleased versions.
   *
   * @return the unreleased versions
   */
  public CZApiVersions getUnreleasedVersions() {
    return unreleasedVersions;
  }

  /**
   * Sets the collection of unreleased versions.
   *
   * @param unreleasedVersions the unreleased versions to set
   * @return the current instance of {@link CZApiProjectVersions}
   */
  public CZApiProjectVersions setUnreleasedVersions(CZApiVersions unreleasedVersions) {
    this.unreleasedVersions = unreleasedVersions;
    return this;
  }

  /**
   * Retrieves the collection of released versions.
   *
   * @return the released versions
   */
  public CZApiVersions getReleasedVersions() {
    return releasedVersions;
  }

  /**
   * Sets the collection of released versions.
   *
   * @param releasedVersions the released versions to set
   * @return the current instance of {@link CZApiProjectVersions}
   */
  public CZApiProjectVersions setReleasedVersions(CZApiVersions releasedVersions) {
    this.releasedVersions = releasedVersions;
    return this;
  }

  /**
   * Retrieves a version by its name, ignoring case.
   *
   * @param name the name of the version to retrieve
   * @return the version with the specified name, or {@code null} if not found
   */
  @JsonIgnore
  public CZApiVersion getByName(String name) {
    return getAllVersions().getFirst(v -> StringUtils.equalsIgnoreCase(name, v.getName()));
  }

  /**
   * Retrieves all versions (both released and unreleased) as a single collection.
   *
   * @return a {@link CZApiVersions} object containing all versions
   */
  @JsonIgnore
  public CZApiVersions getAllVersions() {
    CZApiVersions czApiVersions = new CZApiVersions(releasedVersions);
    czApiVersions.addAll(unreleasedVersions);
    return czApiVersions;
  }

  /**
   * Checks if this object is equal to another object.
   *
   * @param o the object to compare with
   * @return {@code true} if the objects are equal, {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CZApiProjectVersions that = (CZApiProjectVersions) o;
    return Objects.equals(unreleasedVersions, that.unreleasedVersions)
        && Objects.equals(releasedVersions, that.releasedVersions);
  }

  /**
   * Computes the hash code for this object.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(unreleasedVersions, releasedVersions);
  }

  /**
   * Returns a string representation of this object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString() {
    return "CZApiProjectVersions{"
        + "unreleasedVersions="
        + unreleasedVersions
        + ", releasedVersions="
        + releasedVersions
        + '}';
  }
}

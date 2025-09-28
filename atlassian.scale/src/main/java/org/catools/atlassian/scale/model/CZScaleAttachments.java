package org.catools.atlassian.scale.model;

import org.catools.common.collections.CSet;

import java.util.stream.Stream;

/**
 * Represents a collection of Scale attachments.
 *
 * <p>This class extends {@link CSet} to provide a specialized set implementation
 * for managing {@link CZScaleAttachment} objects. It includes constructors for
 * creating the collection from various input sources, such as arrays, streams,
 * or iterables.</p>
 */
public class CZScaleAttachments extends CSet<CZScaleAttachment> {

  /**
   * Default constructor.
   *
   * <p>Creates an empty collection of Scale attachments.</p>
   */
  public CZScaleAttachments() {
  }

  /**
   * Constructs a collection of Scale attachments from an array of attachments.
   *
   * @param c an array of {@link CZScaleAttachment} objects to initialize the collection
   */
  public CZScaleAttachments(CZScaleAttachment... c) {
    super(c);
  }

  /**
   * Constructs a collection of Scale attachments from a stream of attachments.
   *
   * @param stream a {@link Stream} of {@link CZScaleAttachment} objects to initialize the collection
   */
  public CZScaleAttachments(Stream<CZScaleAttachment> stream) {
    super(stream);
  }

  /**
   * Constructs a collection of Scale attachments from an iterable of attachments.
   *
   * @param iterable an {@link Iterable} of {@link CZScaleAttachment} objects to initialize the collection
   */
  public CZScaleAttachments(Iterable<CZScaleAttachment> iterable) {
    super(iterable);
  }
}
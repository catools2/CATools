package org.catools.web.collections;

import org.catools.common.collections.interfaces.CIterable;
import org.catools.common.extensions.types.interfaces.CDynamicIterableExtension;

import javax.ws.rs.NotSupportedException;
import java.util.Iterator;

public interface CWebIterable<E> extends CIterable<E>, CDynamicIterableExtension<E> {

  /**
   * The Zero based index which means the first record has index 0. In case if you want to use it in
   * your java code, you should decrement it for 0.
   *
   * @param idx
   * @return
   */
  E getRecord(int idx);

  /**
   * The Zero based index which means the first record has index 0. In case if you want to use it in
   * your java code, you should decrement it for 0. You really should not need to change this
   * method.
   *
   * @param idx
   * @return
   */
  boolean hasRecord(int idx);

  @Override
  default Iterable<E> get() {
    return () -> iterator();
  }

  @Override
  default boolean _useWaiter() {
    return true;
  }

  @Override
  default Iterator<E> iterator() {
    return new Iterator<>() {
      int cursor = 0;

      @Override
      public boolean hasNext() {
        return hasRecord(cursor);
      }

      @SuppressWarnings("unchecked")
      @Override
      public E next() {
        return getRecord(cursor++);
      }

      @Override
      public void remove() {
        throw new NotSupportedException();
      }
    };
  }
}

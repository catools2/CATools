package org.catools.common.extensions.states.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.catools.common.utils.CIterableUtil;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * CIterableState is an interface for Iterable state related methods.
 *
 * <p>We need this interface to have possibility of adding state to any exists objects with the
 * minimum change in the code.
 */
public interface CIterableState<E> extends CObjectState<Iterable<E>> {

  /**
   * Check if iterable contains any value.
   *
   * @return execution boolean result
   */
  default boolean has(Predicate<E> expected) {
    for (E a : get()) {
      if (expected.test(a)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if iterable contains any value.
   *
   * @return execution boolean result
   */
  default boolean hasNot(Predicate<E> expected) {
    for (E a : get()) {
      if (expected.test(a)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if iterable contains any value.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isNotEmpty() {
    return CIterableUtil.isNotEmpty(get());
  }

  /**
   * Check if iterable is empty.
   *
   * @return execution boolean result
   */
  @JsonIgnore
  default boolean isEmpty() {
    return !isNotEmpty();
  }

  /**
   * Returns {@code true} if this collection contains the specified element. More formally, returns
   * {@code true} if and only if this collection contains at least one element {@code e} such that
   * {@code Objects.equals(o, e)}.
   *
   * @param o element whose presence in this collection is to be tested
   * @return {@code true} if this collection contains the specified element
   * @throws ClassCastException   if the type of the specified element is incompatible with this
   *                              collection
   * @throws NullPointerException if the specified element is null and this collection does not
   *                              permit null elements
   */
  default boolean contains(Object o) {
    for (E a : get()) {
      if (Objects.equals(a, o)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if actual collection contains any element from the expected collection.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean containsAny(Iterable<E> expected) {
    if (expected == null) {
      return false;
    }
    Iterable<E> actual = get();
    for (E t : expected) {
      if (CIterableUtil.contains(actual, t)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if actual collection contains all elements from the expected collection. Please note that
   * actual collection might have more elements.
   *
   * @param expected   value to compare
   * @param onNotMatch consumer to call if no match found
   * @return execution boolean result
   */
  default boolean containsAll(Iterable<E> expected, Consumer<E> onNotMatch) {
    if (expected == null) {
      return false;
    }
    Iterable<E> actual = get();
    boolean result = true;
    for (E t : expected) {
      if (!CIterableUtil.contains(actual, t)) {
        if (onNotMatch == null) {
          return false;
        }
        result = false;
        onNotMatch.accept(t);
      }
    }
    return result;
  }

  /**
   * Check if actual collection contains none of elements from the expected collection.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean containsNone(Iterable<E> expected) {
    return containsNone(expected, null);
  }

  /**
   * Check if actual collection contains none of elements from the expected collection.
   *
   * @param expected value to compare
   * @param onMatch  consumer to call if match found
   * @return execution boolean result
   */
  default boolean containsNone(Iterable<E> expected, Consumer<E> onMatch) {
    if (expected == null) {
      return false;
    }
    if (CIterableUtil.isEmpty(expected)) {
      return false;
    }
    Iterable<E> actual = get();
    boolean result = true;
    for (E t : expected) {
      if (CIterableUtil.contains(actual, t)) {
        if (onMatch == null) {
          return false;
        }
        result = false;
        onMatch.accept(t);
      }
    }
    return result;
  }

  /**
   * Check if actual collection either is empty or contains the expected element.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean emptyOrContains(E expected) {
    Iterable<E> a = get();
    return a == null || CIterableUtil.isEmpty(a) || CIterableUtil.contains(a, expected);
  }

  /**
   * Check if actual collection either is empty or does not contain the expected element.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean emptyOrNotContains(E expected) {
    Iterable<E> a = get();
    return a == null || CIterableUtil.isEmpty(a) || !CIterableUtil.contains(a, expected);
  }

  /**
   * Check if actual and expected collections have the exact same elements. (Ignore element order)
   * First we compare that actual collection contains all expected collection elements and then we
   * verify that expected has all elements from actual.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean isEqual(Iterable<E> expected) {
    return isEqual(expected, null, null);
  }

  /**
   * Check if actual and expected collections have the exact same elements. (Ignore element order)
   * First we compare that actual collection contains all expected collection elements and then we
   * verify that expected has all elements from actual.
   *
   * @param expected              value to compare
   * @param onActualNotContains   consumer to call if match found
   * @param onExpectedNotContains consumer to call if match found
   * @return execution boolean result
   */
  default boolean isEqual(
      Iterable<E> expected, Consumer<E> onActualNotContains, Consumer<E> onExpectedNotContains) {
    if (expected == null) {
      return false;
    }
    Iterable<E> actual = get();
    boolean result = true;

    int actualSize = 0;
    int expectedSize = 0;
    for (E t : expected) {
      expectedSize++;
      if (!CIterableUtil.contains(actual, t)) {
        if (onActualNotContains == null) {
          return false;
        }
        result = false;
        onActualNotContains.accept(t);
      }
    }

    for (E t : actual) {
      actualSize++;
      if (!CIterableUtil.contains(expected, t)) {
        if (onExpectedNotContains == null) {
          return false;
        }
        result = false;
        onExpectedNotContains.accept(t);
      }
    }
    return expectedSize == actualSize && result;
  }

  /**
   * Check if actual collection does not contain the expected element.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean notContains(E expected) {
    if (expected == null) {
      return false;
    }
    return !CIterableUtil.contains(get(), expected);
  }

  /**
   * Check if actual collection does not contain all elements from the expected collection. Please
   * note that actual collection might have some of elements but the point is to ensure that not all
   * expected elements are exist in it.
   *
   * @param expected value to compare
   * @return execution boolean result
   */
  default boolean notContainsAll(Iterable<E> expected) {
    return notContainsAll(expected, null);
  }

  /**
   * Check if actual collection does not contain all elements from the expected collection. Please
   * note that actual collection might have some of elements but the point is to ensure that not all
   * expected elements are exist in it.
   *
   * @param onActualContains consumer to call if match found
   * @param expected         value to compare
   * @return execution boolean result
   */
  default boolean notContainsAll(Iterable<E> expected, Consumer<E> onActualContains) {
    if (expected == null) {
      return false;
    }

    boolean contains = false;
    Iterable<E> actual = get();

    for (E t : expected) {
      if (!CIterableUtil.contains(actual, t)) {
        if (onActualContains == null) {
          return true;
        }
        contains = false;
      }
    }
    return contains;
  }
}

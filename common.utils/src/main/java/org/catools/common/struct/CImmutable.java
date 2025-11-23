package org.catools.common.struct;

/**
 * A simple immutable wrapper class for a single value. Instances of this class are immutable,
 * meaning that once created, their value cannot be changed.
 *
 * @param <T> the type of the value
 */
public record CImmutable<T>(T value) {

  /**
   * Creates a new instance of {@code CImmutable} with the specified value.
   *
   * @param value the value to be wrapped
   * @param <T>   the type of the value
   * @return a new instance of {@code CImmutable} with the specified value
   */
  public static <T> CImmutable<T> of(T value) {
    return new CImmutable<>(value);
  }

  /**
   * Returns the value wrapped by this {@code CImmutable} instance. It can also use {@link #value()}
   * to get the value, but {@code #get()} is more readable.
   *
   * @return the value wrapped by this {@code CImmutable} instance
   */
  public T get() {
    return value;
  }
}

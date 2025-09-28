package org.catools.common.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.concurrent.exceptions.CThreadTimeoutException;
import org.catools.common.utils.CDateUtil;
import org.catools.common.utils.CSleeper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * CStorage is a thread-safe storage system that manages a pool of objects.
 * It allows borrowing and releasing objects with optional predicates and timeouts.
 *
 * <p>Example usage:
 * <pre>{@code
 * CStorage<String> storage = new CStorage<>("ExampleStorage", 5, 30);
 * storage.init(Arrays.asList("Object1", "Object2"));
 *
 * String borrowed = storage.borrow("User1");
 * storage.release(borrowed);
 * }</pre>
 * </p>
 *
 * @param <T> the type of objects managed by the storage
 */
@Slf4j
public class CStorage<T> {
  private final Object lock = new Object();
  private final List<T> available = new ArrayList<>();
  private final List<T> borrowed = new ArrayList<>();
  private final String name;
  private final int requestIntervalInSeconds;
  private final int requestTimeoutInSeconds;

  /**
   * Constructs a new CStorage instance.
   *
   * <p>Example:
   * <pre>{@code
   * CStorage<String> storage = new CStorage<>("MyStorage", 5, 30);
   * }</pre>
   * </p>
   *
   * @param name the name of the storage
   * @param requestIntervalInSeconds the interval between borrow attempts in seconds
   * @param requestTimeoutInSeconds the maximum time to wait for an object in seconds
   */
  public CStorage(String name, int requestIntervalInSeconds, int requestTimeoutInSeconds) {
    this.name = name;
    this.requestIntervalInSeconds = requestIntervalInSeconds;
    this.requestTimeoutInSeconds = requestTimeoutInSeconds;
  }

  /**
   * Initializes the storage with a list of objects.
   *
   * <p>Example:
   * <pre>{@code
   * storage.init(Arrays.asList("Object1", "Object2"));
   * }</pre>
   * </p>
   *
   * @param initialObjects the list of objects to initialize the storage with
   */
  public void init(List<T> initialObjects) {
    Objects.requireNonNull(initialObjects);
    log.info("Storage {} initiation started with {} records.", name, initialObjects.size());
    performActionOnQueue(
        () -> {
          available.addAll(initialObjects);
          return true;
        });
    log.info("Storage {} initiated.", name);
  }

  /**
   * Returns the number of available objects in the storage.
   *
   * @return the size of the available list
   */
  public int getAvailableSize() {
    return available.size();
  }

  /**
   * Returns the number of borrowed objects in the storage.
   *
   * @return the size of the borrowed list
   */
  public int getBorrowedSize() {
    return borrowed.size();
  }

  /**
   * Performs an action on a borrowed object and releases it afterward.
   *
   * <p>Example:
   * <pre>{@code
   * String result = storage.performAction("User1", obj -> obj.toUpperCase());
   * }</pre>
   * </p>
   *
   * @param borrower the name of the borrower
   * @param action the action to perform on the borrowed object
   * @param <R> the type of the result produced by the action
   * @return the result of the action
   */
  public <R> R performAction(String borrower, Function<T, R> action) {
    return performAction(borrower, t -> true, action);
  }

  /**
   * Performs an action on a borrowed object that matches a predicate and releases it afterward.
   *
   * <p>Example:
   * <pre>{@code
   * String result = storage.performAction("User1", obj -> obj.startsWith("A"), obj -> obj.toUpperCase());
   * }</pre>
   * </p>
   *
   * @param borrower the name of the borrower
   * @param predicate the predicate to filter objects
   * @param action the action to perform on the borrowed object
   * @param <R> the type of the result produced by the action
   * @return the result of the action
   */
  public <R> R performAction(String borrower, Predicate<T> predicate, Function<T, R> action) {
    T t = null;
    try {
      t = borrow(borrower, predicate);
      return action.apply(t);
    } finally {
      if (t != null) {
        release(t);
      }
    }
  }

  /**
   * Borrows an object from the storage.
   *
   * <p>Example:
   * <pre>{@code
   * String obj = storage.borrow("User1");
   * }</pre>
   * </p>
   *
   * @param borrower the name of the borrower
   * @return the borrowed object
   */
  public T borrow(String borrower) {
    return borrow(borrower, t -> true);
  }

  /**
   * Borrows an object from the storage that matches a predicate.
   *
   * <p>Example:
   * <pre>{@code
   * String obj = storage.borrow("User1", obj -> obj.startsWith("A"));
   * }</pre>
   * </p>
   *
   * @param borrower the name of the borrower
   * @param predicate the predicate to filter objects
   * @return the borrowed object
   */
  public T borrow(String borrower, Predicate<T> predicate) {
    performActionOnQueue(
        () -> {
          log.trace("Attempt to borrow object for " + borrower);
          log.trace(
              "Storage contains {} available and {} borrowed objects",
              available.size(),
              borrowed.size());
          return true;
        });
    Request<T> request = new Request<>(borrower, requestTimeoutInSeconds, predicate);
    return waitForObjectToBeAvailable(request);
  }

  /**
   * Releases a borrowed object back to the storage.
   *
   * <p>Example:
   * <pre>{@code
   * storage.release(obj);
   * }</pre>
   * </p>
   *
   * @param t the object to release
   * @return true if the object was successfully released, false otherwise
   */
  public boolean release(T t) {
    if (t != null) {
      return performActionOnQueue(
          () -> {
            log.trace("Object returned to storage {}.\n" + t, name);
            borrowed.remove(t);
            available.add(t);
            log.trace(
                "Storage contains {} available and {} borrowed objects",
                available.size(),
                borrowed.size());
            return true;
          });
    }
    return false;
  }

  /**
   * Waits for an object to become available in the storage.
   *
   * @param request the request containing the borrower's details and predicate
   * @return the borrowed object
   */
  private T waitForObjectToBeAvailable(Request<T> request) {
    do {
      T response =
          performActionOnQueue(
              () -> {
                T firstOrElse =
                    available.isEmpty()
                        ? null
                        : available.stream().filter(request.predicate).findFirst().orElse(null);
                if (firstOrElse != null) {
                  available.remove(firstOrElse);
                  borrowed.add(firstOrElse);
                  return firstOrElse;
                }
                if (request.isTimeOuted()) {
                  throw new CThreadTimeoutException("Request Timeout triggered on storage " + name + " for TestCase:" + request.borrower);
                }
                return null;
              });

      if (response != null) {
        return response;
      }

      CSleeper.sleepTightInSeconds(requestIntervalInSeconds);
    } while (true);
  }

  /**
   * Performs a synchronized action on the storage queues.
   *
   * @param supplier the action to perform
   * @param <O> the type of the result produced by the action
   * @return the result of the action
   */
  private synchronized <O> O performActionOnQueue(Supplier<O> supplier) {
    synchronized (lock) {
      return supplier.get();
    }
  }

  /**
   * Represents a request to borrow an object from the storage.
   *
   * @param <T> the type of objects in the storage
   */
  static class Request<T> {
    private final Date timeoutAt;
    private final String borrower;
    private final Predicate<T> predicate;

    /**
     * Constructs a new Request instance.
     *
     * @param borrower the name of the borrower
     * @param timeoutInSeconds the timeout duration in seconds
     * @param predicate the predicate to filter objects
     */
    public Request(String borrower, int timeoutInSeconds, Predicate<T> predicate) {
      this.timeoutAt = CDateUtil.add(new Date(), Calendar.SECOND, timeoutInSeconds);
      this.predicate = predicate;
      this.borrower = borrower;
    }

    /**
     * Checks if the request has timed out.
     *
     * @return true if the request has timed out, false otherwise
     */
    public boolean isTimeOuted() {
      return timeoutAt.before(new Date());
    }
  }
}

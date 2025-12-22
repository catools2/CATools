// language: java
package org.catools.common.concurrent;

import org.catools.common.concurrent.exceptions.CInterruptedException;
import org.catools.common.utils.CSleeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A simple implementation of {@link ExecutorService} to simplify the interface for most automation
 * needs.
 *
 * <p>Provides a lightweight wrapper around a fixed thread pool and a queue of {@link Callable}
 * tasks. Exceptions from tasks are captured and can optionally stop all processing.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Create a service with 4 threads
 * CExecutorService<Integer> svc = new CExecutorService<>("MyService", 4);
 *
 * // Add tasks
 * svc.addCallable(() -> {
 *   // perform work
 *   return 1;
 * });
 * svc.addCallable(() -> 2);
 *
 * // Run all tasks and wait for completion
 * try {
 *   svc.invokeAll();
 * } catch (Throwable t) {
 *   // handle error
 * }
 * }</pre>
 *
 * @param <T> the type of the result produced by the tasks in the executor service
 */
public class CExecutorService<T> {
  private final AtomicReference<Throwable> throwableReference = new AtomicReference<>();
  private final List<Callable<T>> queue = Collections.synchronizedList(new ArrayList<>());
  private final AtomicBoolean started = new AtomicBoolean();
  private final AtomicBoolean finished = new AtomicBoolean();
  private final ExecutorService executor;
  private final String name;
  private final Long timeout;
  private final TimeUnit unit;
  private final boolean stopOnException;

  /**
   * Constructs a new CExecutorService with the specified name and thread count.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CExecutorService<String> svc = new CExecutorService<>("svc", 2);
   * }</pre>
   *
   * @param name the name of the executor service
   * @param threadCount the number of threads in the thread pool
   */
  public CExecutorService(String name, int threadCount) {
    this(name, threadCount, null, null, true);
  }

  /**
   * Constructs a new CExecutorService with the specified name, thread count, and exception handling
   * behavior.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // stopOnException = false allows other tasks to continue if one fails
   * CExecutorService<String> svc = new CExecutorService<>("svc", 4, false);
   * }</pre>
   *
   * @param name the name of the executor service
   * @param threadCount the number of threads in the thread pool
   * @param stopOnException whether to stop execution on encountering an exception
   */
  public CExecutorService(String name, int threadCount, boolean stopOnException) {
    this(name, threadCount, null, null, stopOnException);
  }

  /**
   * Constructs a new CExecutorService with the specified name, thread count, timeout, and time
   * unit.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // tasks will be invoked with the provided timeout if not overridden
   * CExecutorService<Integer> svc = new CExecutorService<>("timed", 3, 30L, TimeUnit.SECONDS);
   * }</pre>
   *
   * @param name the name of the executor service
   * @param threadCount the number of threads in the thread pool
   * @param timeout the maximum time to wait for tasks to complete
   * @param unit the time unit of the timeout argument
   */
  public CExecutorService(String name, int threadCount, Long timeout, TimeUnit unit) {
    this(name, threadCount, timeout, unit, true);
  }

  /**
   * Constructs a new CExecutorService with the specified parameters.
   *
   * @param name the name of the executor service
   * @param threadCount the number of threads in the thread pool
   * @param timeout the maximum time to wait for tasks to complete
   * @param unit the time unit of the timeout argument
   * @param stopOnException whether to stop execution on encountering an exception
   */
  public CExecutorService(
      String name, int threadCount, Long timeout, TimeUnit unit, boolean stopOnException) {
    this.executor = Executors.newFixedThreadPool(threadCount);
    this.name = name;
    this.timeout = timeout;
    this.unit = unit;
    this.stopOnException = stopOnException;
  }

  /**
   * Checks if the executor has been started.
   *
   * @return true if the executor has been started, otherwise false
   */
  public boolean isStarted() {
    return started.get();
  }

  /**
   * Checks if the executor has finished execution.
   *
   * @return true if the executor has finished, otherwise false
   */
  public boolean isFinished() {
    return finished.get();
  }

  /**
   * Checks if the executor has been shut down.
   *
   * @return true if the executor has been shut down, otherwise false
   */
  public boolean isShutdown() {
    return executor.isShutdown();
  }

  /**
   * Checks if the executor has been terminated.
   *
   * @return true if the executor has been terminated, otherwise false
   */
  public boolean isTerminated() {
    return executor.isTerminated();
  }

  /**
   * Adds a new task to the task queue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * svc.addCallable(() -> {
   *   // compute and return result
   *   return "result";
   * });
   * }</pre>
   *
   * @param callable the task to be added to the queue
   */
  public void addCallable(Callable<T> callable) {
    this.queue.add(
        () -> {
          try {
            return callable.call();
          } catch (Throwable t) {
            throwableReference.set(t);
            if (stopOnException) {
              shutdownNow();
            }
            throw t;
          }
        });
  }

  /**
   * Executes all tasks in the queue, waiting for their completion.
   *
   * <p>Blocks until tasks complete or an exception occurs. If a timeout was configured in the
   * constructor it will delegate to {@link #invokeAll(long, TimeUnit)}.
   *
   * <p>Example:
   *
   * <pre>{@code
   * try {
   *   svc.invokeAll();
   * } catch (Throwable t) {
   *   // handle task-level exception
   * }
   * }</pre>
   *
   * @throws Throwable if an exception occurs during task execution
   */
  public void invokeAll() throws Throwable {
    if (timeout != null && unit != null) {
      invokeAll(timeout, unit);
    } else {
      doInvoke(
          () -> {
            try {
              executor.invokeAll(this.queue);
            } catch (InterruptedException e) {
              throw new CInterruptedException("Parallel execution interrupted for " + name, e);
            }
            return true;
          });
    }
  }

  /**
   * Executes all tasks in the queue, waiting for their completion or until the timeout expires.
   *
   * <p>Example:
   *
   * <pre>{@code
   * try {
   *   svc.invokeAll(10, TimeUnit.SECONDS);
   * } catch (Throwable t) {
   *   // handle timeout or task exception
   * }
   * }</pre>
   *
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   * @throws Throwable if an exception occurs during task execution
   */
  public void invokeAll(long timeout, TimeUnit unit) throws Throwable {
    doInvoke(
        () -> {
          try {
            executor.invokeAll(this.queue, timeout, unit);
          } catch (InterruptedException e) {
            throw new CInterruptedException("Parallel execution interrupted for " + name, e);
          }
          return true;
        });
  }

  /**
   * Initiates an orderly shutdown of the executor service.
   *
   * <p>Example:
   *
   * <pre>{@code
   * svc.shutdown();
   * }</pre>
   */
  public void shutdown() {
    executor.shutdown();
  }

  /**
   * Attempts to stop all actively executing tasks and halts the processing of waiting tasks.
   *
   * <p>Example:
   *
   * <pre>{@code
   * svc.shutdownNow();
   * }</pre>
   */
  public void shutdownNow() {
    executor.shutdownNow();
  }

  /**
   * Helper method to execute tasks and handle termination.
   *
   * @param supplier the supplier to execute tasks
   * @throws Throwable if an exception occurs during task execution
   */
  private void doInvoke(Supplier<?> supplier) throws Throwable {
    started.set(true);
    supplier.get();

    executor.shutdown();
    // TODO: Expand this to be configuration or parameter driven
    int counter = 3000;
    while (!executor.isTerminated() && counter-- > 0) {
      CSleeper.sleepTight(100);
    }

    finished.set(true);

    if (throwableReference.get() != null) {
      throw throwableReference.get();
    }

    assert executor.isTerminated() : "executor is terminated";
  }
}

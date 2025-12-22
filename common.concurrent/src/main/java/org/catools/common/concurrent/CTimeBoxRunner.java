package org.catools.common.concurrent;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import org.catools.common.concurrent.exceptions.CThreadTimeoutException;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * CTimeBoxRunner is a utility class that executes a task within a specified time frame. If the task
 * exceeds the defined timeout, it either throws an exception or returns null, depending on the
 * configuration.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * try {
 *     String result = CTimeBoxRunner.get(() -> {
 *         // Task logic here
 *         return "Task Result";
 *     }, 10, TimeUnit.SECONDS, true);
 * } catch (CThreadTimeoutException e) {
 *     System.err.println("Task timed out");
 * }
 * }</pre>
 *
 * @param <R> the type of the result produced by the task
 */
public class CTimeBoxRunner<R> implements Runnable {
  private final Supplier<R> job;
  private final int timeoutInSeconds;
  private final boolean throwExceptionIfTimeout;
  private Throwable ex;
  private R r;

  /**
   * Constructs a new CTimeBoxRunner instance.
   *
   * @param job the task to be executed
   * @param timeoutInSeconds the maximum time allowed for the task to complete, in seconds
   * @param throwExceptionIfTimeout whether to throw an exception if the task times out
   */
  private CTimeBoxRunner(Supplier<R> job, int timeoutInSeconds, boolean throwExceptionIfTimeout) {
    this.job = job;
    this.timeoutInSeconds = timeoutInSeconds;
    this.throwExceptionIfTimeout = throwExceptionIfTimeout;
  }

  /**
   * Executes a task and returns the result. If the task exceeds the timeout, it returns null.
   *
   * @param job the task to be executed
   * @param timeoutInSeconds the maximum time allowed for the task to complete, in seconds
   * @param <R> the type of the result produced by the task
   * @return the result of the task, or null if it times out
   */
  public static <R> R get(Supplier<R> job, int timeoutInSeconds) {
    return get(job, timeoutInSeconds, false);
  }

  /**
   * Executes a task and returns the result. If the task exceeds the timeout, it either throws an
   * exception or returns null, based on the configuration.
   *
   * @param job the task to be executed
   * @param timeout the maximum time allowed for the task to complete
   * @param unit the time unit of the timeout
   * @param throwExceptionIfTimeout whether to throw an exception if the task times out
   * @param <R> the type of the result produced by the task
   * @return the result of the task, or null if it times out
   * @throws CThreadTimeoutException if the task times out and throwExceptionIfTimeout is true
   */
  public static <R> R get(
      Supplier<R> job, long timeout, TimeUnit unit, boolean throwExceptionIfTimeout) {
    return new CTimeBoxRunner<>(
            job, (int) TimeUnit.SECONDS.convert(timeout, unit), throwExceptionIfTimeout)
        .get();
  }

  /**
   * Executes a task and returns the result. If the task exceeds the timeout, it either throws an
   * exception or returns null, based on the configuration.
   *
   * @param job the task to be executed
   * @param timeoutInSeconds the maximum time allowed for the task to complete, in seconds
   * @param throwExceptionIfTimeout whether to throw an exception if the task times out
   * @param <R> the type of the result produced by the task
   * @return the result of the task, or null if it times out
   * @throws CThreadTimeoutException if the task times out and throwExceptionIfTimeout is true
   */
  public static <R> R get(Supplier<R> job, int timeoutInSeconds, boolean throwExceptionIfTimeout) {
    return new CTimeBoxRunner<>(job, timeoutInSeconds, throwExceptionIfTimeout).get();
  }

  /** Executes the task in a separate thread and handles timeout logic. */
  @Override
  public void run() {
    try {
      r =
          SimpleTimeLimiter.create(Executors.newFixedThreadPool(1))
              .callWithTimeout(job::get, timeoutInSeconds, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      if (throwExceptionIfTimeout) {
        throw new CThreadTimeoutException("Job execution takes more time than expected");
      }
      this.ex = e;
    } catch (Exception e) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Executes the task and returns the result.
   *
   * @return the result of the task, or null if it times out
   */
  private R get() {
    run();
    return r;
  }
}

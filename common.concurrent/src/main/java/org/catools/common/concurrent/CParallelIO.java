package org.catools.common.concurrent;

import org.catools.common.concurrent.exceptions.CThreadTimeoutException;
import org.catools.common.utils.CSleeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * CParallelIO allows two threads to run in parallel while sharing the same resources.
 *
 * <p>This class manages parallel input and output operations using shared resources. It provides
 * mechanisms for thread synchronization, exception handling, and timeout management.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CParallelIO<String> parallelIO = new CParallelIO<>("Example", 2, 2);
 *
 * parallelIO.setInputExecutor(eof -> {
 *   if (eof.get()) return null;
 *   return "inputData";
 * });
 *
 * parallelIO.setOutputExecutor((eof, data) -> {
 *   System.out.println("Processing: " + data);
 *   if (data.equals("stop")) eof.set(true);
 * });
 *
 * try {
 *   parallelIO.run();
 * } catch (Throwable t) {
 *   t.printStackTrace();
 * }
 * }</pre>
 *
 * @param <T> the type of elements processed by the input and output threads
 */
public class CParallelIO<T> {
  private final AtomicReference<Throwable> throwableReference = new AtomicReference<>();
  private final AtomicInteger activeInputThreads = new AtomicInteger(0);
  private final AtomicBoolean eof = new AtomicBoolean(false);
  private final Object lock = new Object();
  private final String name;
  private final int parallelInputCount;
  private final int parallelOutputCount;
  private final Long timeout;
  private final TimeUnit unit;

  private final List<T> sharedQueue = new ArrayList<>();
  private final List<T> outputQueue = new ArrayList<>();

  private CParallelRunner<Boolean> inputExecutor;
  private CParallelRunner<Boolean> outputExecutor;

  /**
   * Constructs a new instance with the specified name and thread counts for input and output.
   *
   * <p>Example:
   *
   * <pre>{@code
   * CParallelIO<String> io = new CParallelIO<>("MyIO", 3, 2);
   * }</pre>
   *
   * @param name the name of the parallel IO instance
   * @param parallelInputCount the number of input threads
   * @param parallelOutputCount the number of output threads
   */
  public CParallelIO(String name, int parallelInputCount, int parallelOutputCount) {
    this(name, parallelInputCount, parallelOutputCount, null, null);
  }

  /**
   * Constructs a new instance with the specified name, thread counts, timeout, and time unit.
   *
   * <p>Example with timeout:
   *
   * <pre>{@code
   * CParallelIO<String> io = new CParallelIO<>("TimedIO", 2, 2, 30L, TimeUnit.SECONDS);
   * }</pre>
   *
   * @param name the name of the parallel IO instance
   * @param parallelInputCount the number of input threads
   * @param parallelOutputCount the number of output threads
   * @param timeout the maximum time to wait for tasks to complete
   * @param unit the time unit of the timeout argument
   */
  public CParallelIO(
      String name, int parallelInputCount, int parallelOutputCount, Long timeout, TimeUnit unit) {
    this.name = "Parallel IO " + name;
    this.parallelInputCount = parallelInputCount;
    this.parallelOutputCount = parallelOutputCount;
    this.timeout = timeout;
    this.unit = unit;
  }

  /**
   * Sets the input executor with the specified input function.
   *
   * <p>The provided function receives an {@link AtomicBoolean} that indicates EOF; when true, the
   * producer should stop supplying new items. The function should return an item of type {@code T}
   * to be added to the shared queue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * io.setInputExecutor(eof -> {
   *   if (eof.get()) return null;
   *   return fetchNextItem();
   * });
   * }</pre>
   *
   * @param inputFunction a function that generates input data
   */
  public void setInputExecutor(Function<AtomicBoolean, T> inputFunction) {
    this.inputExecutor =
        new CParallelRunner<>(
            name + " Input",
            parallelInputCount,
            () -> {
              try {
                activeInputThreads.incrementAndGet();
                do {
                  T input = inputFunction.apply(eof);
                  performActionOnSharedQueue(() -> sharedQueue.add(input));
                } while (!eof.get() && isLive());
              } catch (Throwable t) {
                throwableReference.set(t);
                throw t;
              } finally {
                activeInputThreads.decrementAndGet();
              }
              return true;
            });
  }

  /**
   * Sets the output executor with the specified output function.
   *
   * <p>The consumer receives the EOF flag and a single item to process. The consumer should handle
   * normal processing and may set the EOF flag if it determines the pipeline should stop.
   *
   * <p>Example:
   *
   * <pre>{@code
   * io.setOutputExecutor((eof, item) -> {
   *   processItem(item);
   *   if (shouldStop()) eof.set(true);
   * });
   * }</pre>
   *
   * @param outputFunction a consumer that processes elements from the shared queue
   */
  public void setOutputExecutor(BiConsumer<AtomicBoolean, T> outputFunction) {
    this.outputExecutor =
        new CParallelRunner<>(
            name + " Output",
            parallelOutputCount,
            () -> {
              do {
                if (throwableReference.get() != null) {
                  break;
                }

                AtomicReference<T> clone = new AtomicReference<>();
                performActionOnSharedQueue(
                    () -> {
                      outputQueue.addAll(sharedQueue);
                      sharedQueue.clear();
                      if (!outputQueue.isEmpty()) {
                        clone.set(outputQueue.remove(0));
                      }
                      return true;
                    });

                if (clone.get() == null) {
                  CSleeper.sleepTight(500);
                  continue;
                }

                outputFunction.accept(eof, clone.get());
                if (throwableReference.get() != null) {
                  break;
                }
              } while ((!eof.get()
                      || activeInputThreads.get() > 0
                      || !outputQueue.isEmpty()
                      || !sharedQueue.isEmpty())
                  && isLive());
              return true;
            });
  }

  /**
   * Runs the input and output executors, waiting for their completion.
   *
   * <p>Blocks until both input and output tasks complete or an exception occurs. Any exception
   * thrown by user-provided input or output functions is propagated.
   *
   * <p>Example:
   *
   * <pre>{@code
   * try {
   *   io.run();
   * } catch (Throwable t) {
   *   // handle exception
   * }
   * }</pre>
   *
   * @throws Throwable if an exception occurs during execution
   */
  public void run() throws Throwable {
    if (timeout == null || unit == null) {
      try {
        CThreadRunner.run(
            () -> {
              try {
                inputExecutor.invokeAll();
              } catch (Throwable t) {
                throwableReference.set(t);
              } finally {
                eof.set(true);
              }
            });
        try {
          outputExecutor.invokeAll();
        } catch (Throwable t) {
          throwableReference.set(t);
        }
      } catch (CThreadTimeoutException e) {
        eof.set(true);
        inputExecutor.shutdownNow();
        outputExecutor.shutdownNow();
        throw e;
      }

      if (throwableReference.get() != null) {
        throw throwableReference.get();
      }
    } else {
      run(timeout, unit);
    }
  }

  /**
   * Runs the input and output executors with a specified timeout.
   *
   * <p>Example with timeout:
   *
   * <pre>{@code
   * try {
   *   io.run(60, TimeUnit.SECONDS);
   * } catch (Throwable t) {
   *   // handle exception
   * }
   * }</pre>
   *
   * @param timeout the maximum time to wait for tasks to complete
   * @param unit the time unit of the timeout argument
   * @throws Throwable if an exception occurs during execution
   */
  public void run(long timeout, TimeUnit unit) throws Throwable {
    try {
      CTimeBoxRunner.get(
          () -> {
            CThreadRunner.run(
                () -> {
                  try {
                    inputExecutor.invokeAll(timeout, unit);
                  } catch (Throwable t) {
                    throwableReference.set(t);
                  }
                  eof.set(true);
                });

            try {
              outputExecutor.invokeAll(timeout, unit);
            } catch (Throwable t) {
              throwableReference.set(t);
            }
            return true;
          },
          timeout,
          unit,
          true);
    } catch (CThreadTimeoutException e) {
      eof.set(true);
      inputExecutor.shutdownNow();
      outputExecutor.shutdownNow();
      throw e;
    }

    if (throwableReference.get() != null) {
      throw throwableReference.get();
    }
  }

  /**
   * Checks if the input or output executor has started.
   *
   * <p>This method is useful for monitoring the state of the parallel IO instance to determine if
   * any execution has begun.
   *
   * <p>Example:
   *
   * <pre>{@code
   * if (!io.isStarted()) {
   *   System.out.println("Execution has not started yet");
   * }
   * }</pre>
   *
   * @return true if either executor has started, otherwise false
   */
  public boolean isStarted() {
    return inputExecutor.isStarted() || outputExecutor.isStarted();
  }

  /**
   * Checks if the parallel IO instance is live.
   *
   * <p>An instance is considered live if it has not finished, shut down, or terminated, and no
   * throwable exceptions have occurred. This is useful for determining if the parallel IO
   * operations can continue processing.
   *
   * <p>Example:
   *
   * <pre>{@code
   * while (io.isLive()) {
   *   // Continue processing
   *   Thread.sleep(100);
   * }
   * }</pre>
   *
   * @return true if the instance is live, otherwise false
   */
  public boolean isLive() {
    return !(isFinished() || isShutdown() || isTerminated()) && throwableReference.get() == null;
  }

  /**
   * Checks if the input and output executors have finished execution.
   *
   * <p>This method returns true when both input and output processing have completed. Useful for
   * determining when all work is done.
   *
   * <p>Example:
   *
   * <pre>{@code
   * io.run();
   * if (io.isFinished()) {
   *   System.out.println("All processing completed successfully");
   * }
   * }</pre>
   *
   * @return true if both executors have finished, otherwise false
   */
  public boolean isFinished() {
    return inputExecutor.isFinished() && outputExecutor.isFinished();
  }

  /**
   * Checks if the input and output executors have been shut down.
   *
   * <p>A shutdown executor will not accept new tasks but may still be executing previously
   * submitted tasks. Use this method to check if shutdown has been initiated.
   *
   * <p>Example:
   *
   * <pre>{@code
   * try {
   *   io.run(30, TimeUnit.SECONDS);
   * } catch (CThreadTimeoutException e) {
   *   if (io.isShutdown()) {
   *     System.out.println("Executors were shut down due to timeout");
   *   }
   * }
   * }</pre>
   *
   * @return true if both executors have been shut down, otherwise false
   */
  public boolean isShutdown() {
    return inputExecutor.isShutdown() && outputExecutor.isShutdown();
  }

  /**
   * Checks if the input and output executors have been terminated.
   *
   * <p>Terminated executors have completed shutdown and all tasks have finished. This is the final
   * state after shutdown completion.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // After a timeout or forced shutdown
   * while (!io.isTerminated()) {
   *   System.out.println("Waiting for termination...");
   *   Thread.sleep(1000);
   * }
   * System.out.println("All threads have been terminated");
   * }</pre>
   *
   * @return true if both executors have been terminated, otherwise false
   */
  public boolean isTerminated() {
    return inputExecutor.isTerminated() && outputExecutor.isTerminated();
  }

  /**
   * Performs a synchronized action on the shared queue.
   *
   * @param supplier the action to perform
   * @param <C> the type of the result produced by the action
   */
  private synchronized <C> void performActionOnSharedQueue(Supplier<C> supplier) {
    synchronized (lock) {
      supplier.get();
    }
  }
}

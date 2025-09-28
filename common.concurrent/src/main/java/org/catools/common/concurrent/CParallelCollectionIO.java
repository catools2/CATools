package org.catools.common.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.concurrent.exceptions.CThreadTimeoutException;
import org.catools.common.utils.CSleeper;

import java.util.Collection;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class for managing parallel input and output operations using shared resources.
 *
 * <p>This class allows two sets of threads (input and output) to run in parallel, sharing a common
 * resource (a stack) for optimized data processing. It provides mechanisms for handling timeouts,
 * exceptions, and thread synchronization.</p>
 *
 * <p>Example usage:
 * <pre>{@code
 * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("Example", 2, 2);
 *
 * // Input: supply batches until done (signal EOF via the AtomicBoolean if needed)
 * io.setInputExecutor(eof -> {
 *   if (eof.get()) return Collections.emptyList();
 *   // produce a collection of items or empty list when there's nothing right now
 *   return Arrays.asList("a", "b", "c");
 * });
 *
 * // Output: consume items until EOF and queue drained
 * io.setOutputExecutor((eof, item) -> {
 *   System.out.println("Processed: " + item);
 * });
 *
 * io.run();
 * }</pre>
 * </p>
 *
 * @param <T> the type of elements processed by the input and output threads
 */
@Slf4j
public class CParallelCollectionIO<T> {
  private final AtomicReference<Throwable> throwableReference = new AtomicReference<>();
  private final AtomicInteger activeInputThreads = new AtomicInteger(0);
  private final AtomicBoolean eof = new AtomicBoolean(false);
  private final Object lock = new Object();
  private final String name;
  private final int parallelInputCount;
  private final int parallelOutputCount;
  private final Long timeout;
  private final TimeUnit unit;

  private final Stack<T> sharedQueue = new Stack<>();

  private CParallelRunner<Boolean> inputExecutor;
  private CParallelRunner<Boolean> outputExecutor;

  /**
   * Constructs a new instance with the specified name and thread counts for input and output.
   *
   * <p>Example:
   * <pre>{@code
   * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("MyIO", 3, 2);
   * }</pre>
   * </p>
   *
   * @param name the name of the parallel IO instance
   * @param parallelInputCount the number of input threads
   * @param parallelOutputCount the number of output threads
   */
  public CParallelCollectionIO(String name, int parallelInputCount, int parallelOutputCount) {
    this(name, parallelInputCount, parallelOutputCount, null, null);
  }

  /**
   * Constructs a new instance with the specified name, thread counts, timeout, and time unit.
   *
   * <p>Example with timeout:
   * <pre>{@code
   * // run with a 30 second timeout for the overall operation
   * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("TimedIO", 2, 2, 30L, TimeUnit.SECONDS);
   * }</pre>
   * </p>
   *
   * @param name the name of the parallel IO instance
   * @param parallelInputCount the number of input threads
   * @param parallelOutputCount the number of output threads
   * @param timeout the maximum time to wait for tasks to complete
   * @param unit the time unit of the timeout argument
   */
  public CParallelCollectionIO(
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
   * producer should stop supplying new items. The function should return a {@link Collection} of
   * items (can be empty) to be placed on the shared queue.</p>
   *
   * <p>Example:
   * <pre>{@code
   * io.setInputExecutor(eof -> {
   *   if (noMoreSourceData()) {
   *     eof.set(true);
   *     return Collections.emptyList();
   *   }
   *   return fetchNextBatch();
   * });
   * }</pre>
   * </p>
   *
   * @param inputFunction a function that generates a collection of input data
   */
  public void setInputExecutor(Function<AtomicBoolean, Collection<T>> inputFunction) {
    this.inputExecutor =
        new CParallelRunner<>(
            name + " Input",
            parallelInputCount,
            () -> {
              try {
                activeInputThreads.incrementAndGet();

                do {
                  Collection<T> result = inputFunction.apply(eof);

                  if (result != null && !result.isEmpty()) {
                    addInputResultToSharedStack(result);
                  } else {
                    CSleeper.sleepTight(500);
                  }

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
   * normal processing and may set the EOF flag if it determines the pipeline should stop.</p>
   *
   * <p>Example:
   * <pre>{@code
   * io.setOutputExecutor((eof, item) -> {
   *   processItem(item);
   *   if (shouldStop()) eof.set(true);
   * });
   * }</pre>
   * </p>
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

                T temp = readFromSharedQueue();

                if (temp == null) {
                  CSleeper.sleepTight(500);
                  continue;
                }

                outputFunction.accept(eof, temp);

                if (throwableReference.get() != null) {
                  break;
                }
              } while ((!eof.get()
                  || activeInputThreads.get() > 0
                  || !sharedQueue.isEmpty())
                  && isLive());
              return true;
            });
  }

  /**
   * Runs the input and output executors, waiting for their completion.
   *
   * <p>Blocks until both input and output tasks complete or an exception occurs. Any exception
   * thrown by user-provided input or output functions is propagated.</p>
   *
   * <p>Example:
   * <pre>{@code
   * io.run();
   * }</pre>
   * </p>
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
   * <pre>{@code
   * io.run(60, TimeUnit.SECONDS);
   * }</pre>
   * </p>
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
   * <p>This method is useful for monitoring the lifecycle of the parallel IO operation.
   * It returns true as soon as either the input or output threads begin execution.</p>
   *
   * <p>Example:
   * <pre>{@code
   * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("Monitor", 2, 2);
   * io.setInputExecutor(eof -> fetchData());
   * io.setOutputExecutor((eof, item) -> processItem(item));
   * 
   * // Start execution in a separate thread
   * CompletableFuture.runAsync(() -> {
   *   try {
   *     io.run();
   *   } catch (Throwable t) {
   *     // handle exception
   *   }
   * });
   * 
   * // Monitor startup
   * while (!io.isStarted()) {
   *   Thread.sleep(100);
   * }
   * System.out.println("Parallel IO has started!");
   * }</pre>
   * </p>
   *
   * @return true if either executor has started, otherwise false
   */
  public boolean isStarted() {
    return inputExecutor.isStarted() || outputExecutor.isStarted();
  }

  /**
   * Checks if the parallel IO instance is live.
   *
   * <p>A parallel IO instance is considered live if it is not finished, shutdown, or terminated,
   * and no exceptions have been encountered. This is useful for monitoring the health and
   * active status of the operation.</p>
   *
   * <p>Example:
   * <pre>{@code
   * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("HealthCheck", 2, 2);
   * io.setInputExecutor(eof -> fetchData());
   * io.setOutputExecutor((eof, item) -> processItem(item));
   * 
   * // Start execution in a separate thread
   * CompletableFuture.runAsync(() -> {
   *   try {
   *     io.run();
   *   } catch (Throwable t) {
   *     // handle exception
   *   }
   * });
   * 
   * // Monitor health
   * while (io.isLive()) {
   *   System.out.println("System is healthy and processing...");
   *   Thread.sleep(1000);
   * }
   * System.out.println("Processing completed or encountered an error");
   * }</pre>
   * </p>
   *
   * @return true if the instance is live, otherwise false
   */
  public boolean isLive() {
    return !(isFinished() || isShutdown() || isTerminated()) && throwableReference.get() == null;
  }

  /**
   * Checks if the input and output executors have finished execution.
   *
   * <p>This method returns true when both input and output processing have completed successfully.
   * It's the preferred way to check for normal completion of the parallel IO operation.</p>
   *
   * <p>Example:
   * <pre>{@code
   * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("BatchJob", 2, 2);
   * io.setInputExecutor(eof -> {
   *   List<String> batch = fetchNextBatch();
   *   if (batch.isEmpty()) eof.set(true);
   *   return batch;
   * });
   * io.setOutputExecutor((eof, item) -> writeToDatabase(item));
   * 
   * // Start execution in a separate thread
   * CompletableFuture.runAsync(() -> {
   *   try {
   *     io.run();
   *   } catch (Throwable t) {
   *     // handle exception
   *   }
   * });
   * 
   * // Wait for completion
   * while (!io.isFinished()) {
   *   System.out.println("Still processing...");
   *   Thread.sleep(1000);
   * }
   * System.out.println("All processing completed successfully!");
   * }</pre>
   * </p>
   *
   * @return true if both executors have finished, otherwise false
   */
  public boolean isFinished() {
    return inputExecutor.isFinished() && outputExecutor.isFinished();
  }

  /**
   * Checks if the input and output executors have been shut down.
   *
   * <p>Shutdown indicates that the executors will not accept new tasks, but may still be
   * processing existing tasks. This is typically the result of a graceful shutdown operation.</p>
   *
   * <p>Example:
   * <pre>{@code
   * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("GracefulShutdown", 2, 2);
   * io.setInputExecutor(eof -> fetchData());
   * io.setOutputExecutor((eof, item) -> processItem(item));
   * 
   * // Start execution in a separate thread
   * CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
   *   try {
   *     io.run();
   *   } catch (Throwable t) {
   *     // handle exception
   *   }
   * });
   * 
   * // Simulate external shutdown trigger
   * Thread.sleep(5000);
   * task.cancel(true); // This may trigger shutdown
   * 
   * if (io.isShutdown()) {
   *   System.out.println("Executors have been shut down");
   * }
   * }</pre>
   * </p>
   *
   * @return true if both executors have been shut down, otherwise false
   */
  public boolean isShutdown() {
    return inputExecutor.isShutdown() && outputExecutor.isShutdown();
  }

  /**
   * Checks if the input and output executors have been terminated.
   *
   * <p>Termination indicates that all tasks have completed execution and the executors have
   * been fully shut down. This is the final state after shutdown completes.</p>
   *
   * <p>Example:
   * <pre>{@code
   * CParallelCollectionIO<String> io = new CParallelCollectionIO<>("TerminationCheck", 2, 2);
   * io.setInputExecutor(eof -> fetchData());
   * io.setOutputExecutor((eof, item) -> processItem(item));
   * 
   * try {
   *   io.run(30, TimeUnit.SECONDS); // Run with timeout
   * } catch (CThreadTimeoutException e) {
   *   System.out.println("Operation timed out");
   * } catch (Throwable t) {
   *   System.out.println("Operation failed: " + t.getMessage());
   * }
   * 
   * // Check final state
   * if (io.isTerminated()) {
   *   System.out.println("All threads have been terminated");
   * } else if (io.isShutdown()) {
   *   System.out.println("Shutdown initiated but not yet terminated");
   * }
   * }</pre>
   * </p>
   *
   * @return true if both executors have been terminated, otherwise false
   */
  public boolean isTerminated() {
    return inputExecutor.isTerminated() && outputExecutor.isTerminated();
  }

  /**
   * Adds a collection of input data to the shared queue.
   *
   * @param data the collection of data to add
   */
  private void addInputResultToSharedStack(Collection<T> data) {
    performActionOnSharedQueue(() -> sharedQueue.addAll(data));
    log.debug("{} new records added to the queue {}, new queue size is {}", data.size(), name, sharedQueue.size());
  }

  /**
   * Reads an element from the shared queue.
   *
   * @return the element read from the queue, or null if the queue is empty
   */
  private T readFromSharedQueue() {
    if (sharedQueue.isEmpty())
      log.debug("The {} queue is empty", name);
    else
      log.debug("1 record removing from the queue {}, new queue size is {}", name, sharedQueue.size());

    return performActionOnSharedQueue(() -> sharedQueue.isEmpty() ? null : sharedQueue.remove(0));
  }

  /**
   * Performs a synchronized action on the shared queue.
   *
   * @param supplier the action to perform
   * @param <O> the type of the result produced by the action
   * @return the result of the action
   */
  private synchronized <O> O performActionOnSharedQueue(Supplier<O> supplier) {
    synchronized (lock) {
      return supplier.get();
    }
  }
}

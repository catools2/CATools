package org.catools.common.concurrent;

import java.util.concurrent.Executors;

/**
 * Utility class for thread-related operations providing convenient methods for running tasks concurrently.
 * 
 * <p>This class simplifies thread management by providing static methods that handle thread creation
 * and execution without requiring manual thread instantiation or configuration. It uses the default
 * thread factory from {@link Executors} to ensure consistent thread creation patterns.</p>
 * 
 * <p>The class is designed to be used in scenarios where you need to quickly spawn background threads
 * for asynchronous task execution, such as I/O operations, background processing, or parallel computations.</p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Basic usage - fire and forget
 * CThreadRunner.run(() -> {
 *     // Some background work
 *     processData();
 * });
 * 
 * // With thread management
 * Thread workerThread = CThreadRunner.run(() -> {
 *     performLongRunningTask();
 * });
 * 
 * // Wait for completion
 * try {
 *     workerThread.join();
 *     System.out.println("Task completed");
 * } catch (InterruptedException e) {
 *     Thread.currentThread().interrupt();
 * }
 * 
 * // Multiple parallel tasks
 * List<Thread> threads = new ArrayList<>();
 * for (int i = 0; i < 5; i++) {
 *     final int taskId = i;
 *     Thread t = CThreadRunner.run(() -> {
 *         System.out.println("Processing task " + taskId);
 *         // Task logic here
 *     });
 *     threads.add(t);
 * }
 * 
 * // Wait for all tasks to complete
 * for (Thread t : threads) {
 *     try {
 *         t.join();
 *     } catch (InterruptedException e) {
 *         Thread.currentThread().interrupt();
 *         break;
 *     }
 * }
 * }</pre>
 * 
 * @author CA Tools Team
 * @since 1.0
 * @see Thread
 * @see Runnable
 * @see Executors#defaultThreadFactory()
 */
public class CThreadRunner {
  /**
   * Executes a task in a new thread.
   *
   * <p>This method creates a new thread using the default thread factory, starts it, and returns the thread object.
   * It is useful for running tasks concurrently without manually managing thread creation.</p>
   *
   * <p>Example usage:
   * <pre>{@code
   * Runnable task = () -> System.out.println("Task is running");
   * Thread thread = CThreadRunner.run(task);
   * thread.join(); // Wait for the thread to complete
   * }</pre>
   * </p>
   *
   * @param task the task to be executed in the new thread
   * @return the thread object that is executing the task
   */
  public static Thread run(Runnable task) {
    Thread thread = Executors.defaultThreadFactory().newThread(task);
    thread.start();
    return thread;
  }
}

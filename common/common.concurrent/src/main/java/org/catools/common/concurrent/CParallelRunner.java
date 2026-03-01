package org.catools.common.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A simple implementation to execute parallel tasks using {@link CExecutorService} with a
 * simplified interface for the majority of automation needs.
 *
 * <p>This class provides a convenient way to run the same task multiple times in parallel threads.
 * It automatically manages thread creation, task execution, and resource cleanup.
 *
 * <h3>Basic Usage Example:</h3>
 *
 * <pre>{@code
 * // Run a simple computation task on 4 threads
 * CParallelRunner<Boolean> runner = new CParallelRunner<>(
 *     "ExampleRunner",
 *     4,
 *     () -> {
 *         // Simulate some work
 *         Thread.sleep(1000);
 *         System.out.println("Task completed on thread: " + Thread.currentThread().getName());
 *         return true;
 *     }
 * );
 *
 * try {
 *     runner.invokeAll();
 *     System.out.println("All tasks completed successfully");
 * } catch (Throwable t) {
 *     t.printStackTrace();
 * } finally {
 *     runner.shutdown();
 * }
 * }</pre>
 *
 * <h3>Web Load Testing Example:</h3>
 *
 * <pre>{@code
 * // Simulate concurrent web requests for load testing
 * CParallelRunner<String> loadTester = new CParallelRunner<>(
 *     "LoadTester",
 *     10, // 10 concurrent threads
 *     () -> {
 *         // Simulate HTTP request
 *         String url = "http://example.com/api/test";
 *         long startTime = System.currentTimeMillis();
 *
 *         // Your HTTP client code here
 *         // HttpResponse response = httpClient.get(url);
 *
 *         long endTime = System.currentTimeMillis();
 *         return "Request completed in " + (endTime - startTime) + "ms";
 *     }
 * );
 *
 * try {
 *     loadTester.invokeAll(30, TimeUnit.SECONDS); // 30 second timeout
 * } catch (Throwable t) {
 *     System.err.println("Load test failed: " + t.getMessage());
 * } finally {
 *     loadTester.shutdown();
 * }
 * }</pre>
 *
 * <h3>Database Connection Testing Example:</h3>
 *
 * <pre>{@code
 * // Test database connection pool under concurrent load
 * CParallelRunner<Integer> dbTester = new CParallelRunner<>(
 *     "DatabaseTester",
 *     20, // 20 concurrent database connections
 *     () -> {
 *         try (Connection conn = dataSource.getConnection()) {
 *             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users");
 *             ResultSet rs = stmt.executeQuery();
 *             rs.next();
 *             return rs.getInt(1);
 *         }
 *     },
 *     false // Don't stop on exception - continue testing other connections
 * );
 *
 * try {
 *     dbTester.invokeAll();
 * } catch (Throwable t) {
 *     System.err.println("Database test encountered issues: " + t.getMessage());
 * } finally {
 *     dbTester.shutdown();
 * }
 * }</pre>
 *
 * @param <T> the type of the result produced by the tasks in the runner
 * @see CExecutorService
 * @since 1.0
 */
public class CParallelRunner<T> {
  private final CExecutorService<T> executor;

  /**
   * Constructs a new CParallelRunner with the specified name, thread count, and task. Uses the
   * default behavior of stopping execution when an exception occurs.
   *
   * <p>This constructor is ideal for scenarios where you want to ensure that if any thread
   * encounters an error, the entire parallel execution stops immediately.
   *
   * <h4>Example - File Processing:</h4>
   *
   * <pre>{@code
   * // Process multiple files concurrently
   * List<File> filesToProcess = Arrays.asList(
   *     new File("file1.txt"),
   *     new File("file2.txt"),
   *     new File("file3.txt")
   * );
   * AtomicInteger fileIndex = new AtomicInteger(0);
   *
   * CParallelRunner<String> fileProcessor = new CParallelRunner<>(
   *     "FileProcessor",
   *     3,
   *     () -> {
   *         int index = fileIndex.getAndIncrement();
   *         if (index < filesToProcess.size()) {
   *             File file = filesToProcess.get(index);
   *             return processFile(file); // Your file processing logic
   *         }
   *         return "No more files";
   *     }
   * );
   * }</pre>
   *
   * <h4>Example - Mathematical Computation:</h4>
   *
   * <pre>{@code
   * // Calculate prime numbers in parallel
   * AtomicLong counter = new AtomicLong(2);
   * List<Long> primes = Collections.synchronizedList(new ArrayList<>());
   *
   * CParallelRunner<Void> primeCalculator = new CParallelRunner<>(
   *     "PrimeCalculator",
   *     Runtime.getRuntime().availableProcessors(),
   *     () -> {
   *         long num = counter.getAndIncrement();
   *         if (isPrime(num)) {
   *             primes.add(num);
   *         }
   *         return null;
   *     }
   * );
   * }</pre>
   *
   * @param name the name of the runner (used for thread naming and identification)
   * @param threadCount the number of threads to use (should be > 0)
   * @param callable the task to be executed in parallel by each thread
   * @throws IllegalArgumentException if threadCount is <= 0
   */
  public CParallelRunner(String name, int threadCount, Callable<T> callable) {
    this(name, threadCount, callable, true);
  }

  /**
   * Constructs a new CParallelRunner with the specified name, thread count, task, and exception
   * handling behavior.
   *
   * <p>This constructor provides full control over how the runner behaves when exceptions occur.
   * When {@code stopOnException} is false, the runner will continue executing remaining tasks even
   * if some threads encounter exceptions.
   *
   * <h4>Example - Resilient Data Processing:</h4>
   *
   * <pre>{@code
   * // Process data records, continuing even if some fail
   * List<DataRecord> records = getDataRecords();
   * AtomicInteger recordIndex = new AtomicInteger(0);
   * List<String> results = Collections.synchronizedList(new ArrayList<>());
   * List<Exception> errors = Collections.synchronizedList(new ArrayList<>());
   *
   * CParallelRunner<String> dataProcessor = new CParallelRunner<>(
   *     "DataProcessor",
   *     5,
   *     () -> {
   *         int index = recordIndex.getAndIncrement();
   *         if (index < records.size()) {
   *             try {
   *                 DataRecord record = records.get(index);
   *                 String result = processRecord(record);
   *                 results.add(result);
   *                 return result;
   *             } catch (Exception e) {
   *                 errors.add(e);
   *                 throw e; // Re-throw to be handled by framework
   *             }
   *         }
   *         return null;
   *     },
   *     false // Continue processing even if some records fail
   * );
   * }</pre>
   *
   * <h4>Example - Critical System Validation:</h4>
   *
   * <pre>{@code
   * // Validate system components, stop immediately on any failure
   * List<SystemComponent> components = getSystemComponents();
   * AtomicInteger componentIndex = new AtomicInteger(0);
   *
   * CParallelRunner<Boolean> systemValidator = new CParallelRunner<>(
   *     "SystemValidator",
   *     components.size(),
   *     () -> {
   *         int index = componentIndex.getAndIncrement();
   *         if (index < components.size()) {
   *             SystemComponent component = components.get(index);
   *             boolean isValid = validateComponent(component);
   *             if (!isValid) {
   *                 throw new ValidationException("Component " + component.getName() + " failed validation");
   *             }
   *             return isValid;
   *         }
   *         return true;
   *     },
   *     true // Stop immediately on any validation failure
   * );
   * }</pre>
   *
   * @param name the name of the runner (used for thread naming and identification)
   * @param threadCount the number of threads to use (should be > 0)
   * @param callable the task to be executed in parallel by each thread
   * @param stopOnException whether to stop execution immediately when any thread encounters an
   *     exception
   * @throws IllegalArgumentException if threadCount is <= 0
   */
  public CParallelRunner(
      String name, int threadCount, Callable<T> callable, boolean stopOnException) {
    this.executor = new CExecutorService<>(name, threadCount, stopOnException);
    for (int i = 0; i < threadCount; i++) {
      executor.addCallable(callable);
    }
  }

  /**
   * Checks if the executor has been started.
   *
   * <p>This method is useful for monitoring the lifecycle of the parallel runner and ensuring that
   * tasks have begun execution before proceeding with other operations.
   *
   * <h4>Example - Monitoring Execution State:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> runner = new CParallelRunner<>("Monitor", 3, () -> "task");
   *
   * // Start execution in a separate thread
   * CompletableFuture.runAsync(() -> {
   *     try {
   *         runner.invokeAll();
   *     } catch (Throwable t) {
   *         t.printStackTrace();
   *     }
   * });
   *
   * // Monitor progress
   * while (!runner.isStarted()) {
   *     Thread.sleep(10);
   *     System.out.println("Waiting for runner to start...");
   * }
   * System.out.println("Runner has started!");
   * }</pre>
   *
   * @return true if the executor has been started, otherwise false
   */
  public boolean isStarted() {
    return executor.isStarted();
  }

  /**
   * Checks if the executor has finished execution.
   *
   * <p>This method returns true when all submitted tasks have completed execution, either
   * successfully or with exceptions. It's useful for determining when results are ready to be
   * processed.
   *
   * <h4>Example - Waiting for Completion:</h4>
   *
   * <pre>{@code
   * CParallelRunner<Integer> calculator = new CParallelRunner<>("Calculator", 4, () -> {
   *     // Perform some calculation
   *     return (int)(Math.random() * 1000);
   * });
   *
   * // Start execution asynchronously
   * CompletableFuture.runAsync(() -> {
   *     try {
   *         calculator.invokeAll();
   *     } catch (Throwable t) {
   *         t.printStackTrace();
   *     }
   * });
   *
   * // Poll for completion
   * while (!calculator.isFinished()) {
   *     System.out.println("Calculations in progress...");
   *     Thread.sleep(100);
   * }
   *
   * System.out.println("All calculations completed!");
   * calculator.shutdown();
   * }</pre>
   *
   * @return true if the executor has finished, otherwise false
   */
  public boolean isFinished() {
    return executor.isFinished();
  }

  /**
   * Checks if the executor has been shut down.
   *
   * <p>This method returns true if {@link #shutdown()} or {@link #shutdownNow()} has been called,
   * regardless of whether all tasks have actually finished executing. Use {@link #isTerminated()}
   * to check if all tasks have completed after shutdown.
   *
   * <h4>Example - Shutdown State Monitoring:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> runner = new CParallelRunner<>("StateMonitor", 3, () -> {
   *     Thread.sleep(2000); // 2 second task
   *     return "Task completed";
   * });
   *
   * // Start execution in background
   * CompletableFuture.runAsync(() -> {
   *     try {
   *         runner.invokeAll();
   *     } catch (Throwable t) {
   *         t.printStackTrace();
   *     }
   * });
   *
   * Thread.sleep(500); // Let tasks start
   *
   * System.out.println("Is shutdown: " + runner.isShutdown()); // false
   * System.out.println("Is terminated: " + runner.isTerminated()); // false
   *
   * runner.shutdown(); // Initiate shutdown
   *
   * System.out.println("Is shutdown: " + runner.isShutdown()); // true
   * System.out.println("Is terminated: " + runner.isTerminated()); // likely false (tasks still running)
   *
   * // Wait for termination
   * while (!runner.isTerminated()) {
   *     Thread.sleep(100);
   *     System.out.println("Waiting for termination...");
   * }
   *
   * System.out.println("Is terminated: " + runner.isTerminated()); // true
   * }</pre>
   *
   * @return true if the executor has been shut down, otherwise false
   * @see #shutdown()
   * @see #shutdownNow()
   * @see #isTerminated()
   */
  public boolean isShutdown() {
    return executor.isShutdown();
  }

  /**
   * Checks if the executor has been terminated.
   *
   * <p>This method returns true only after {@link #shutdown()} or {@link #shutdownNow()} has been
   * called AND all tasks have completed execution. This is useful for confirming that all resources
   * have been properly released and all threads have finished.
   *
   * <h4>Example - Complete Lifecycle Management:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> runner = new CParallelRunner<>("LifecycleRunner", 4, () -> {
   *     Thread.sleep(1000); // Simulate work
   *     return "Task completed";
   * });
   *
   * // Start execution in background
   * CompletableFuture<Void> execution = CompletableFuture.runAsync(() -> {
   *     try {
   *         runner.invokeAll();
   *     } catch (Throwable t) {
   *         t.printStackTrace();
   *     }
   * });
   *
   * Thread.sleep(500); // Let tasks start
   *
   * System.out.println("Started: " + runner.isStarted());     // true
   * System.out.println("Finished: " + runner.isFinished());   // false
   * System.out.println("Shutdown: " + runner.isShutdown());   // false
   * System.out.println("Terminated: " + runner.isTerminated()); // false
   *
   * // Wait for completion and then shutdown
   * execution.join();
   * runner.shutdown();
   *
   * System.out.println("After shutdown:");
   * System.out.println("Finished: " + runner.isFinished());   // true
   * System.out.println("Shutdown: " + runner.isShutdown());   // true
   * System.out.println("Terminated: " + runner.isTerminated()); // true
   * }</pre>
   *
   * <h4>Example - Cleanup Verification:</h4>
   *
   * <pre>{@code
   * // Ensure proper cleanup after forced shutdown
   * CParallelRunner<Void> resourceRunner = new CParallelRunner<>("ResourceRunner", 5, () -> {
   *     try {
   *         // Simulate resource-intensive task
   *         for (int i = 0; i < 1000; i++) {
   *             if (Thread.currentThread().isInterrupted()) {
   *                 // Cleanup resources
   *                 cleanupResources();
   *                 return null;
   *             }
   *             performWork(); // Some work that uses resources
   *             Thread.sleep(10);
   *         }
   *     } catch (InterruptedException e) {
   *         Thread.currentThread().interrupt();
   *         cleanupResources();
   *     }
   *     return null;
   * });
   *
   * try {
   *     CompletableFuture.runAsync(() -> {
   *         try {
   *             resourceRunner.invokeAll();
   *         } catch (Throwable t) {
   *             // Handle interruption
   *         }
   *     });
   *
   *     Thread.sleep(200); // Let tasks start
   *     resourceRunner.shutdownNow(); // Force shutdown
   *
   *     // Wait for termination to ensure cleanup is complete
   *     long startTime = System.currentTimeMillis();
   *     while (!resourceRunner.isTerminated()) {
   *         Thread.sleep(50);
   *         if (System.currentTimeMillis() - startTime > 5000) {
   *             System.err.println("Warning: Termination taking longer than expected");
   *             break;
   *         }
   *     }
   *
   *     if (resourceRunner.isTerminated()) {
   *         System.out.println("All resources properly cleaned up");
   *     }
   *
   * } catch (InterruptedException e) {
   *     Thread.currentThread().interrupt();
   * }
   * }</pre>
   *
   * @return true if the executor has been terminated, otherwise false
   * @see #isShutdown()
   * @see #shutdown()
   * @see #shutdownNow()
   */
  public boolean isTerminated() {
    return executor.isTerminated();
  }

  /**
   * Executes all tasks in the runner, waiting for their completion.
   *
   * <p>This method blocks the calling thread until all tasks have completed execution. If any task
   * throws an exception and {@code stopOnException} is true (default), this method will throw that
   * exception and attempt to cancel remaining tasks.
   *
   * <h4>Example - Simple Parallel Execution:</h4>
   *
   * <pre>{@code
   * AtomicInteger counter = new AtomicInteger(0);
   * CParallelRunner<Integer> runner = new CParallelRunner<>(
   *     "Counter",
   *     5,
   *     () -> {
   *         int value = counter.incrementAndGet();
   *         System.out.println("Thread " + Thread.currentThread().getName() + " got value: " + value);
   *         return value;
   *     }
   * );
   *
   * try {
   *     runner.invokeAll();
   *     System.out.println("Final counter value: " + counter.get());
   * } catch (Throwable t) {
   *     System.err.println("Execution failed: " + t.getMessage());
   * } finally {
   *     runner.shutdown();
   * }
   * }</pre>
   *
   * <h4>Example - Exception Handling:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> runner = new CParallelRunner<>(
   *     "ExceptionDemo",
   *     3,
   *     () -> {
   *         if (Math.random() < 0.3) { // 30% chance of exception
   *             throw new RuntimeException("Random failure occurred");
   *         }
   *         return "Success";
   *     }
   * );
   *
   * try {
   *     runner.invokeAll();
   *     System.out.println("All tasks completed successfully");
   * } catch (Throwable t) {
   *     System.err.println("One or more tasks failed: " + t.getMessage());
   *     // Handle the exception appropriately
   * } finally {
   *     runner.shutdown();
   * }
   * }</pre>
   *
   * @throws Throwable if an exception occurs during task execution
   * @see #invokeAll(long, TimeUnit)
   */
  public void invokeAll() throws Throwable {
    executor.invokeAll();
  }

  /**
   * Executes all tasks in the runner, waiting for their completion or until the timeout expires.
   *
   * <p>This method provides a way to limit the maximum execution time for all parallel tasks. If
   * the timeout expires before all tasks complete, the method will attempt to cancel remaining
   * tasks and throw a TimeoutException.
   *
   * <h4>Example - API Stress Testing with Timeout:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> apiTester = new CParallelRunner<>(
   *     "APIStressTester",
   *     10,
   *     () -> {
   *         // Simulate API call that might take varying amounts of time
   *         long delay = (long)(Math.random() * 5000); // 0-5 seconds
   *         Thread.sleep(delay);
   *         return "API call completed in " + delay + "ms";
   *     }
   * );
   *
   * try {
   *     // Allow maximum 3 seconds for all API calls to complete
   *     apiTester.invokeAll(3, TimeUnit.SECONDS);
   *     System.out.println("All API calls completed within timeout");
   * } catch (TimeoutException e) {
   *     System.err.println("Some API calls exceeded the 3-second timeout");
   * } catch (Throwable t) {
   *     System.err.println("API test failed: " + t.getMessage());
   * } finally {
   *     apiTester.shutdownNow(); // Force shutdown any remaining tasks
   * }
   * }</pre>
   *
   * <h4>Example - Batch Processing with Time Constraints:</h4>
   *
   * <pre>{@code
   * List<Document> documents = getDocumentsToProcess();
   * AtomicInteger docIndex = new AtomicInteger(0);
   *
   * CParallelRunner<String> docProcessor = new CParallelRunner<>(
   *     "DocumentProcessor",
   *     4,
   *     () -> {
   *         int index = docIndex.getAndIncrement();
   *         if (index < documents.size()) {
   *             Document doc = documents.get(index);
   *             return processDocument(doc); // Your document processing logic
   *         }
   *         return null;
   *     }
   * );
   *
   * try {
   *     // Process for maximum 30 minutes
   *     docProcessor.invokeAll(30, TimeUnit.MINUTES);
   *     System.out.println("Document processing completed");
   * } catch (Throwable t) {
   *     System.err.println("Document processing interrupted: " + t.getMessage());
   *     // Save progress or handle partial completion
   * } finally {
   *     docProcessor.shutdown();
   * }
   * }</pre>
   *
   * @param timeout the maximum time to wait for task completion
   * @param unit the time unit of the timeout argument
   * @throws Throwable if an exception occurs during task execution or if timeout expires
   * @throws TimeoutException if the timeout expires before all tasks complete
   * @see #invokeAll()
   */
  public void invokeAll(long timeout, TimeUnit unit) throws Throwable {
    executor.invokeAll(timeout, unit);
  }

  /**
   * Initiates an orderly shutdown of the runner.
   *
   * <p>This method initiates a graceful shutdown where previously submitted tasks are allowed to
   * complete, but no new tasks will be accepted. This method does not wait for previously submitted
   * tasks to complete execution.
   *
   * <h4>Example - Graceful Shutdown:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> runner = new CParallelRunner<>("GracefulRunner", 4, () -> {
   *     // Simulate long-running task
   *     Thread.sleep(2000);
   *     return "Task completed";
   * });
   *
   * try {
   *     // Start execution in separate thread
   *     CompletableFuture.runAsync(() -> {
   *         try {
   *             runner.invokeAll();
   *         } catch (Throwable t) {
   *             t.printStackTrace();
   *         }
   *     });
   *
   *     // Let it run for a while
   *     Thread.sleep(1000);
   *
   *     // Initiate graceful shutdown
   *     runner.shutdown();
   *
   *     // Wait for existing tasks to complete
   *     while (!runner.isTerminated()) {
   *         System.out.println("Waiting for tasks to complete...");
   *         Thread.sleep(100);
   *     }
   *
   *     System.out.println("All tasks completed, runner shut down gracefully");
   * } catch (InterruptedException e) {
   *     Thread.currentThread().interrupt();
   * }
   * }</pre>
   *
   * @see #shutdownNow()
   * @see ExecutorService#shutdown()
   */
  public void shutdown() {
    executor.shutdown();
  }

  /**
   * Attempts to stop all actively executing tasks and halts the processing of waiting tasks.
   *
   * <p>This method attempts to forcefully shutdown the runner by interrupting all currently running
   * tasks. It does not wait for actively executing tasks to terminate, but it does return a list of
   * tasks that were waiting for execution.
   *
   * <h4>Example - Emergency Shutdown:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> runner = new CParallelRunner<>("EmergencyRunner", 5, () -> {
   *     try {
   *         // Simulate long-running task that checks for interruption
   *         for (int i = 0; i < 100; i++) {
   *             if (Thread.currentThread().isInterrupted()) {
   *                 System.out.println("Task was interrupted, cleaning up...");
   *                 return "Interrupted";
   *             }
   *             Thread.sleep(100); // This will throw InterruptedException when interrupted
   *         }
   *         return "Task completed normally";
   *     } catch (InterruptedException e) {
   *         Thread.currentThread().interrupt();
   *         System.out.println("Task interrupted via InterruptedException");
   *         return "Interrupted via exception";
   *     }
   * });
   *
   * try {
   *     // Start execution in separate thread
   *     CompletableFuture.runAsync(() -> {
   *         try {
   *             runner.invokeAll();
   *         } catch (Throwable t) {
   *             System.out.println("Execution was interrupted: " + t.getMessage());
   *         }
   *     });
   *
   *     // Let it run briefly
   *     Thread.sleep(500);
   *
   *     // Force immediate shutdown
   *     System.out.println("Forcing immediate shutdown...");
   *     runner.shutdownNow();
   *
   *     // Brief wait to see the interruption effects
   *     Thread.sleep(1000);
   *
   * } catch (InterruptedException e) {
   *     Thread.currentThread().interrupt();
   * }
   * }</pre>
   *
   * <h4>Example - Timeout-based Forced Shutdown:</h4>
   *
   * <pre>{@code
   * CParallelRunner<String> runner = new CParallelRunner<>("TimeoutRunner", 3, () -> {
   *     // Long-running task
   *     Thread.sleep(10000); // 10 seconds
   *     return "Should not complete";
   * });
   *
   * try {
   *     // Try to complete within 2 seconds
   *     runner.invokeAll(2, TimeUnit.SECONDS);
   * } catch (Exception e) {
   *     System.out.println("Tasks exceeded timeout, forcing shutdown");
   *     runner.shutdownNow();
   * }
   * }</pre>
   *
   * @see #shutdown()
   * @see ExecutorService#shutdownNow()
   */
  public void shutdownNow() {
    executor.shutdownNow();
  }
}

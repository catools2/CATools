package org.catools.common.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread factory that creates threads with a specified name prefix.
 */
@Slf4j
public final class CNamedThread implements ThreadFactory {

  /**
   * The pool number counter for threads created by this factory.
   */
  private static final AtomicInteger poolNumber = new AtomicInteger(1);

  /**
   * The thread number counter for threads created by this factory.
   */
  private final AtomicInteger threadNumber = new AtomicInteger(1);

  /**
   * The name prefix for threads created by this factory.
   */
  private final String namePrefix;

  /**
   * Creates a new instance of {@code NamedThreadFactory} with the specified name prefix.
   *
   * @param namePrefix the name prefix for threads created by this factory
   */
  public CNamedThread(String namePrefix) {
    this.namePrefix = namePrefix + "-" + poolNumber.getAndIncrement() + "-thread-";
  }

  @Override
  public Thread newThread(Runnable runnable) {
    Thread thread = new Thread(runnable, namePrefix + threadNumber.getAndIncrement());
    thread.setUncaughtExceptionHandler(this::handleUncaughtException);
    thread.setDaemon(true);
    return thread;
  }

  /**
   * Handles uncaught exceptions for threads created by this factory.
   *
   * @param t the thread that threw the exception
   * @param e the exception that was thrown
   */
  private void handleUncaughtException(Thread t, Throwable e) {
    log.error("Thread {} uncaught exception", t.getName(), e);
  }
}

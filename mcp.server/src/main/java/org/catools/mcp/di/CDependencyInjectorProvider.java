package org.catools.mcp.di;

/**
 * This enum provides a singleton instance of {@link CDependencyInjectorProvider} that can be used to
 * initialize and retrieve the {@link CDependencyInjector} instance.
 */
public enum CDependencyInjectorProvider {

  /**
   * The singleton instance of {@link CDependencyInjectorProvider}.
   */
  INSTANCE;

  /**
   * The {@link CDependencyInjector} instance.
   */
  private volatile CDependencyInjector injector;

  /**
   * Initializes the {@link CDependencyInjectorProvider} with the specified {@link
   * CDependencyInjector}.
   *
   * @param injector the {@link CDependencyInjector} to initialize with
   * @return the {@link CDependencyInjectorProvider} instance
   */
  public CDependencyInjectorProvider initialize(CDependencyInjector injector) {
    if (this.injector == null) {
      synchronized (this) {
        if (this.injector == null) {
          this.injector = injector;
        }
      }
    }
    return this;
  }

  /**
   * Returns the {@link CDependencyInjector} instance.
   *
   * @return the {@link CDependencyInjector} instance
   * @throws IllegalStateException if the {@link CDependencyInjector} has not been initialized yet
   */
  public CDependencyInjector getInjector() {
    CDependencyInjector current = this.injector;
    if (current == null) {
      throw new IllegalStateException("DependencyInjector has not been initialized yet");
    }
    return current;
  }
}

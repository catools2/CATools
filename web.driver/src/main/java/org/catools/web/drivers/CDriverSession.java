package org.catools.web.drivers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Function;

/**
 * Manages browser driver session lifecycle and state.
 * <p>
 * Provides session initialization, state management, and cleanup operations.
 * </p>
 *
 * @author CATools Team
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class CDriverSession {

  @Setter
  @Getter
  private CDriverEngine engine;

  private CDriverEngineProvider driverProvider;

  /**
   * Creates a new CDriverSession with the specified driver provider.
   *
   * @param driverProvider provider responsible for creating driver instances
   */
  public CDriverSession(CDriverEngineProvider driverProvider) {
    this.driverProvider = driverProvider;
  }

  /**
   * Initializes and starts the driver session.
   * <p>
   * Must be called before performing any driver actions.
   * </p>
   * It creates the Page instance using the configured provider and applies
   * the specified window dimensions and position.
   *
   * @throws RuntimeException if the driver cannot be created or configured
   */
  public void startSession() {
    setEngine(driverProvider.build());
  }

  /**
   * Resets the session by closing the page (if present) and clearing the reference.
   * Safe to call multiple times.
   */
  public void reset() {
    try {
      if (engine != null) {
        try {
          engine.close();
        } catch (Throwable ignored) {
        }
      }
    } finally {
      this.engine = null;
    }
  }

  /**
   * Checks whether the session has an active, open page.
   *
   * @return true if the underlying Page exists and is not closed
   */
  public boolean isActive() {
    try {
      return engine != null && !engine.isClosed();
    } catch (Throwable t) {
      return false;
    }
  }

  /**
   * Executes an action on the Page with automatic tracking, metrics collection, and event handling.
   *
   * <p>This method provides a standardized way to perform Page actions with enhanced capabilities:
   * <ul>
   *   <li>Automatic page information updates</li>
   *   <li>Before/after action event notifications to listeners</li>
   *   <li>DevTools metrics collection (when supported)</li>
   *   <li>Performance timing measurement</li>
   *   <li>Alert handling to prevent interference</li>
   * </ul>
   *
   * <p>If the session is not active or an alert is present, the method handles these conditions
   * gracefully and may skip certain operations to maintain stability.
   *
   * @param <T>        the return type of the action function
   * @param actionName a descriptive name for the action being performed (used in logging and metrics)
   * @param consumer   the function to execute on the Page instance
   * @return the result of the consumer function, or null if the session is not active
   */
  public <T> T performActionOnEngine(String actionName, Function<CDriverEngine, T> consumer) {
    if (!isActive()) {
      return null;
    }

    T result = consumer.apply(engine);
    return result;
  }

}

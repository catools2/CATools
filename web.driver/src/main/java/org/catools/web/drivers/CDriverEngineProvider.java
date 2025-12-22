package org.catools.web.drivers;

import org.catools.web.enums.CBrowser;

/**
 * Provider interface for creating CDriverEngine instances with various deployment configurations.
 *
 * <p>Supports local, remote, and containerized browser automation engines (Playwright, Selenium,
 * etc.).
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CDriverEngineProvider provider = new CChromiumPlaywrightProvider();
 * CDriverEngine engine = provider.build();
 * engine.open("https://example.com");
 * engine.click("#submit-button");
 * }</pre>
 *
 * @author CATools Team
 * @see CDriverEngine
 * @since 2.0
 */
public interface CDriverEngineProvider {
  /**
   * Builds a CDriverEngine instance based on current configuration.
   *
   * <p>Deployment mode is determined by: remote config, test container config, or local (default).
   *
   * @return fully initialized CDriverEngine instance
   * @throws RuntimeException if driver initialization fails
   */
  CDriverEngine build();

  /**
   * Gets the browser type supported by this provider.
   *
   * @return CBrowser enum indicating supported browser type
   */
  CBrowser getBrowser();
}

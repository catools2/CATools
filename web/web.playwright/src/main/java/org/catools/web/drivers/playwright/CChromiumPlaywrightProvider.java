package org.catools.web.drivers.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.enums.CBrowser;

/**
 * Chromium Playwright provider supporting headed and headless modes.
 *
 * <p>This provider is thread-safe, using ThreadLocal to ensure each thread has its own Playwright
 * and Browser instances as required by Playwright's architecture.
 *
 * <p>Configurable options: headless mode, viewport settings, custom launch arguments.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CChromiumPlaywrightProvider provider = new CChromiumPlaywrightProvider();
 * CDriverEngine engine = provider.build();
 * engine.open("https://example.com");
 * }</pre>
 *
 * @author Alireza Keshmiri
 */
@Slf4j
public class CChromiumPlaywrightProvider implements CPlaywrightProvider {

  private final ThreadLocal<Playwright> playwrightThreadLocal =
      ThreadLocal.withInitial(
          () -> {
            log.debug(
                "Creating new Playwright instance for thread: {}",
                Thread.currentThread().getName());
            return Playwright.create();
          });

  private final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

  protected CChromiumPlaywrightProvider() {
    // Register shutdown hook to clean up ThreadLocal resources
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  cleanupBrowser();
                  playwrightThreadLocal.remove();
                  browserThreadLocal.remove();
                }));
  }

  /** Cleans up the browser instance for the current thread. */
  private void cleanupBrowser() {
    try {
      Browser browser = browserThreadLocal.get();
      if (browser != null && browser.isConnected()) {
        browser.close();
      }
    } catch (Exception e) {
      log.debug("Error closing browser during cleanup", e);
    }
  }

  /** Creates a new browser instance for the current thread. */
  private Browser createBrowser() {
    BrowserType.LaunchOptions launchOptions = getLaunchOptions();
    if (launchOptions == null) {
      launchOptions = new BrowserType.LaunchOptions();
    }

    Playwright playwright = playwrightThreadLocal.get();
    Browser browser = playwright.chromium().launch(launchOptions);

    log.info(
        "Chromium browser initialized for thread: {}. Launch Options: {}",
        Thread.currentThread().getName(),
        launchOptions);

    return browser;
  }

  @Override
  public Playwright getPlaywright() {
    return playwrightThreadLocal.get();
  }

  @Override
  public Browser getBrowserInstance() {
    Browser browser = browserThreadLocal.get();

    // Create browser if not yet initialized for this thread
    if (browser == null) {
      synchronized (browserThreadLocal) {
        browser = browserThreadLocal.get();
        if (browser == null) {
          browser = createBrowser();
          browserThreadLocal.set(browser);
        }
      }
    }

    // Check if browser is still connected, if not recreate it
    try {
      if (!browser.isConnected()) {
        log.debug(
            "Browser disconnected for thread: {}, creating new instance",
            Thread.currentThread().getName());
        browserThreadLocal.remove();
        browser = createBrowser();
        browserThreadLocal.set(browser);
      }
    } catch (Exception e) {
      log.debug("Error checking browser connection, recreating: {}", e.getMessage());
      browserThreadLocal.remove();
      browser = createBrowser();
      browserThreadLocal.set(browser);
    }

    return browser;
  }

  @Override
  public CBrowser getBrowser() {
    return CBrowser.CHROMIUM;
  }
}

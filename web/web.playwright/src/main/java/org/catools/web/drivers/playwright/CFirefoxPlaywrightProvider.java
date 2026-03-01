package org.catools.web.drivers.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.catools.web.enums.CBrowser;

/**
 * Firefox Playwright provider supporting headed and headless modes.
 *
 * <p>This provider is thread-safe, using ThreadLocal to ensure each thread has its own Playwright
 * and Browser instances as required by Playwright's architecture.
 *
 * <p>Configurable options: headless mode, viewport settings, custom launch arguments.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CFirefoxPlaywrightProvider provider = new CFirefoxPlaywrightProvider();
 * CDriverEngine engine = provider.build();
 * engine.open("https://example.com");
 * }</pre>
 *
 * @author Alireza Keshmiri
 */
@Slf4j
public class CFirefoxPlaywrightProvider implements CPlaywrightProvider {

  private final ThreadLocal<Playwright> playwrightThreadLocal =
      ThreadLocal.withInitial(
          () -> {
            log.debug(
                "Creating new Playwright instance for thread: {}",
                Thread.currentThread().getName());
            return Playwright.create();
          });

  private final ThreadLocal<Browser> browserThreadLocal =
      ThreadLocal.withInitial(
          () -> {
            BrowserType.LaunchOptions launchOptions = getLaunchOptions();
            if (launchOptions == null) {
              launchOptions = new BrowserType.LaunchOptions();
            }

            Playwright playwright = playwrightThreadLocal.get();
            Browser browser = playwright.firefox().launch(launchOptions);

            log.info(
                "Firefox browser initialized for thread: {}. Launch Options: {}",
                Thread.currentThread().getName(),
                launchOptions);

            return browser;
          });

  protected CFirefoxPlaywrightProvider() {
    // Register shutdown hook to clean up ThreadLocal resources
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  playwrightThreadLocal.remove();
                  browserThreadLocal.remove();
                }));
  }

  @Override
  public Playwright getPlaywright() {
    return playwrightThreadLocal.get();
  }

  @Override
  public Browser getBrowserInstance() {
    return browserThreadLocal.get();
  }

  @Override
  public CBrowser getBrowser() {
    return CBrowser.FIREFOX;
  }
}

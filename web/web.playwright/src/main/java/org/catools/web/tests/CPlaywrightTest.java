package org.catools.web.tests;

import org.catools.web.config.CBrowserConfigs;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverEngineProvider;
import org.catools.web.drivers.playwright.CPlaywrightProvider;
import org.catools.web.drivers.playwright.CPlaywrightProviderFactory;

public abstract class CPlaywrightTest<DR extends CDriver> extends CWebTest<DR> {

  private CPlaywrightProvider playwrightProvider;

  @Override
  protected CDriverEngineProvider getDriverEngineProvider() {
    // Create a new driver session for each driver instance to avoid thread-safety issues
    // Each session gets its own browser page/context
    if (playwrightProvider == null) {
      synchronized (this) {
        if (playwrightProvider == null) {
          playwrightProvider =
              CPlaywrightProviderFactory.create(CBrowserConfigs.getCurrentBrowser());
        }
      }
    }

    return playwrightProvider;
  }

  @Override
  public void afterClass() {
    try {
      super.afterClass();
    } finally {
      playwrightProvider = null;
    }
  }
}

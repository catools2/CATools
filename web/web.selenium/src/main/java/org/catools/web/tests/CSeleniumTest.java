package org.catools.web.tests;

import org.catools.web.config.CBrowserConfigs;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverEngineProvider;
import org.catools.web.drivers.selenium.CSeleniumProvider;
import org.catools.web.drivers.selenium.CSeleniumProviderFactory;
import org.testng.annotations.AfterClass;

public abstract class CSeleniumTest<DR extends CDriver> extends CWebTest<DR> {

  private CSeleniumProvider seleniumProvider;

  @Override
  protected CDriverEngineProvider getDriverEngineProvider() {
    // Create a new driver session for each driver instance to avoid thread-safety issues
    // Each session gets its own browser page/context
    if (seleniumProvider == null) {
      synchronized (this) {
        if (seleniumProvider == null) {
          seleniumProvider = CSeleniumProviderFactory.create(CBrowserConfigs.getCurrentBrowser());
        }
      }
    }

    return seleniumProvider;
  }

  @AfterClass(alwaysRun = true)
  @Override
  public void afterClass() {
    try {
      super.afterClass();
    } finally {
      seleniumProvider = null;
    }
  }
}

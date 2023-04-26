package org.catools.web.tests;

import org.apache.logging.log4j.Level;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.date.CDate;
import org.catools.common.io.CFile;
import org.catools.common.tests.CTest;
import org.catools.common.utils.CRetry;
import org.catools.media.utils.CImageUtil;
import org.catools.reportportal.utils.CReportPortalUtil;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.config.CWebConfigs;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverProvider;
import org.catools.web.drivers.CDriverSession;
import org.catools.web.drivers.providers.CChromeDriverProvider;
import org.catools.web.drivers.providers.CFireFoxDriverProvider;
import org.catools.web.listeners.CDriverListener;
import org.catools.web.pages.CWebPage;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CWebTest<DR extends CDriver> extends CTest {
  protected static final int DEFAULT_TIMEOUT = CDriverConfigs.getTimeout();

  private static final String DEFAULT_SESSION = "DEFAULT_SESSION";
  private final ThreadLocal<String> currentSession = ThreadLocal.withInitial(() -> DEFAULT_SESSION);

  private final CMap<String, DR> drivers = new CHashMap<>();

  public CWebTest() {
    super();
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() {
    super.afterClass();
    quitAll();
  }

  public void switchSession(String currentSession) {
    this.currentSession.set(currentSession);
  }

  public DR getDriver() {
    String session = currentSession.get();
    if (!drivers.containsKey(session)) {
      drivers.put(session, getDefaultDriver());
    }
    return drivers.get(session);
  }

  public boolean isCurrentSessionActive() {
    try {
      return getDriver().isActive();
    } catch (Throwable t) {
      return false;
    }
  }

  public CFile takeScreenShot() {
    return takeScreenShot("");
  }

  public CFile takeScreenShot(String filename) {
    return takeScreenShot(getDriver(), filename);
  }

  public CFile takeScreenShot(CDriver driver, String filename) {
    if (driver == null) {
      return null;
    }
    String fileName = getName() + "-" + filename + "-" + CDate.now().toTimeStampForFileName();
    try {
      CFile output =
          CWebConfigs.getScreenShotsFolder().getChildFile(fileName.replaceAll("\\W", "_") + ".png");
      BufferedImage baseValue = driver.ScreenShot.get();
      if (baseValue != null) {
        CImageUtil.writePNG(baseValue, output);
        logger.info("ScreenShot saved to " + output);
      }
      if (output != null && output.exists()) {
        CReportPortalUtil.sendToReportPortal(Level.INFO, "ScreenShot from " + fileName, output);
      }
      return output;
    } catch (Throwable t) {
      logger.warn("Could not save screen shot image buffer to file.");
      logger.trace("Could not save screen shot image buffer to file.", t);
    }
    return null;
  }

  public void takeScreenShotIfFail(ITestResult result) {
    if (result.getStatus() != ITestResult.SUCCESS) {
      for (CDriver driver : drivers.values()) {
        if (driver != null) {
          takeScreenShot(driver, "");
        }
      }
    }
  }

  public void takeScreenShotAndQuit() {
    try {
      takeScreenShot();
    } finally {
      quit();
    }
  }

  public void quitAll() {
    drivers
        .values()
        .getAll(dr -> dr != null)
        .forEach(
            dr -> {
              try {
                dr.quit();
              } catch (Throwable t) {
              }
            });
  }

  public void quit() {
    CDriver driver = getDriver();
    if (driver != null) {
      try {
        tryLogOut();
      } finally {
        if (driver != null) {
          try {
            CRetry.retry(
                idx -> {
                  driver.quit();
                  return true;
                },
                2,
                1000);
          } catch (Throwable t) {
          }
        }
      }
      drivers.remove(currentSession.get());
      logger.trace("Quit driver.");
    }
  }

  public void open(String url) {
    open(url, !isCurrentSessionActive());
  }

  public void open(String url, boolean restartSession) {
    open(url, restartSession, null);
  }

  public <P extends CWebPage<DR>> P open(String url, Supplier<P> expectedPage) {
    return open(url, !isCurrentSessionActive(), expectedPage);
  }

  public <P extends CWebPage<DR>> P open(
      String url, boolean restartSession, Supplier<P> expectedPage) {
    if (restartSession) {
      logger.trace("start new session");
      getDriver().startSession();
    }
    logger.info("Navigate to " + url);
    getDriver().open(url);
    return expectedPage == null ? null : expectedPage.get();
  }

  public void tryLogOut() {
  }

  protected final void addDriverListeners(CDriverListener... listeners) {
    getDriverSession().addListeners(listeners);
  }

  protected final void addBeforeDriverInitListener(
      Consumer<Capabilities> beforeInitDriverConsumer) {
    addDriverListeners(
        new CDriverListener() {
          @Override
          public void beforeInit(Capabilities capabilities) {
            beforeInitDriverConsumer.accept(capabilities);
          }
        });
  }

  protected final void addAfterDriverInitListener(
      Consumer<RemoteWebDriver> afterInitDriverConsumer) {
    addDriverListeners(
        new CDriverListener() {
          @Override
          public void afterInit(RemoteWebDriver remoteWebDriver) {
            afterInitDriverConsumer.accept(remoteWebDriver);
          }
        });
  }

  protected CDriverSession getDriverSession() {
    return getDriver().getDriverSession();
  }

  protected CDriverSession switchDriverProvider(CDriverProvider provider) {
    String session = currentSession.get();
    if (!drivers.containsKey(session)) {
      drivers.put(session, getDefaultDriver());
    }
    return drivers.get(session).getDriverSession().setDriverProvider(provider);
  }


  public CFireFoxDriverProvider switchToFireFox() {
    CFireFoxDriverProvider driverProvider = new CFireFoxDriverProvider();
    switchDriverProvider(driverProvider);
    return driverProvider;
  }

  public CChromeDriverProvider switchToChrome() {
    CChromeDriverProvider driverProvider = new CChromeDriverProvider();
    switchDriverProvider(driverProvider);
    return driverProvider;
  }

  @SuppressWarnings("unchecked")
  protected DR getDefaultDriver() {
    CDriverProvider driverProvider = null;

    if (CWebConfigs.getCurrentBrowser().isChrome()) {
      driverProvider = new CChromeDriverProvider();
    } else if (CWebConfigs.getCurrentBrowser().isFirefox()) {
      driverProvider = new CFireFoxDriverProvider();
    }

    CDriverSession driverSession = new CDriverSession(driverProvider);
    driverSession.startSession();

    return (DR) new CDriver(driverSession);
  }
}

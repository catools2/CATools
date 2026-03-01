package org.catools.web.tests;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;
import org.apache.logging.log4j.Level;
import org.catools.common.date.CDate;
import org.catools.common.io.CFile;
import org.catools.common.tests.CTest;
import org.catools.common.utils.CRetry;
import org.catools.common.utils.CStringUtil;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;
import org.catools.media.utils.CImageUtil;
import org.catools.reportportal.utils.CReportPortalUtil;
import org.catools.web.config.CBrowserConfigs;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverEngineProvider;
import org.catools.web.drivers.CDriverSession;
import org.catools.web.pages.CWebPage;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;

public abstract class CWebTest<DR extends CDriver> extends CTest {

  private final ThreadLocal<DR> driver = new ThreadLocal<>();

  public CWebTest() {
    super();
    // Register cleanup hook to ensure driver is removed from ThreadLocal
    Runtime.getRuntime().addShutdownHook(new Thread(driver::remove));
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() {
    super.afterClass();
    // Final cleanup - quit any remaining drivers
    quit();
  }

  public DR getDriver() {
    DR currentDriver = driver.get();
    if (currentDriver == null) {
      synchronized (this) {
        // Double-check after acquiring lock
        currentDriver = driver.get();
        if (currentDriver == null) {
          currentDriver = getDefaultDriver();
          driver.set(currentDriver);
        }
      }
    }
    return currentDriver;
  }

  public boolean isCurrentSessionActive() {
    try {
      return getDriver().isActive();
    } catch (Exception e) {
      return false;
    }
  }

  public CFile takeScreenShot() {
    return takeScreenShot(CStringUtil.EMPTY);
  }

  public CFile takeScreenShot(String filename) {
    return takeScreenShot(getDriver(), filename);
  }

  public CFile takeScreenShot(CDriver driver, String filename) {
    if (!driver.isActive()) {
      return null;
    }
    String fileName = getName() + "-" + filename + "-" + CDate.now().toTimeStampForFileName();
    try {
      CFile output =
          CBrowserConfigs.getScreenShotsFolder()
              .getChildFile(fileName.replaceAll("\\W", "_") + ".png");
      BufferedImage baseValue = driver.getScreenShot();
      if (baseValue != null) {
        CImageUtil.writePNG(baseValue, output);
        logger.info("ScreenShot saved to " + output);
      }
      if (output != null && output.exists()) {
        CReportPortalUtil.sendToReportPortal(Level.INFO, "ScreenShot from " + fileName, output);
      }
      return output;
    } catch (Exception e) {
      logger.trace("Could not save screen shot image buffer to file.", e);
    }
    return null;
  }

  public void takeScreenShotIfFail(ITestResult result) {
    if (result.getStatus() != ITestResult.SUCCESS) {
      DR currentDriver = driver.get();
      if (currentDriver != null) {
        takeScreenShot(currentDriver, CStringUtil.EMPTY);
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

  @CMcpTool(
      groups = {"web", "driver"},
      name = "driver_close",
      title = "Close Page",
      description = "Close active driver using by CATools")
  public void quit() {
    try {
      DR currentDriver = driver.get();
      if (currentDriver != null) {
        try {
          tryLogOut();
        } finally {
          try {
            CRetry.retry(
                idx -> {
                  currentDriver.quit();
                  return true;
                },
                2,
                1000);
          } catch (Exception e) {
            logger.debug("Exception while retrying driver.quit()", e);
          }
        }
      }
      logger.trace("Quit driver.");
    } catch (Exception e) {
      logger.debug("Error while quitting driver", e);
    } finally {
      // Always remove from ThreadLocal to prevent memory leaks
      driver.remove();
    }
  }

  @CMcpTool(
      groups = {"web", "driver"},
      name = "driver_open_url",
      title = "Open URL",
      description = "Open URL using CATools Page")
  public void open(
      @CMcpToolParam(name = "url", description = "The URL to navigate to") String url) {
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

  public void tryLogOut() {}

  protected CDriverSession getDriverSession() {
    return getDriver().getDriverSession();
  }

  protected DR getDefaultDriver() {
    CDriverSession session = new CDriverSession(getDriverEngineProvider());
    return buildDriver(session);
  }

  protected abstract CDriverEngineProvider getDriverEngineProvider();

  protected abstract DR buildDriver(CDriverSession session);
}

package org.catools.mcp.web;

import static org.catools.web.drivers.CDriver.ACTIVE_WEB_ELEMENT;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.datastate.CDataState;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverSession;
import org.catools.web.tests.CPlaywrightTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

@Slf4j
public class CMcpWebTest extends CPlaywrightTest<CDriver> {

  @Override
  protected CDriver buildDriver(CDriverSession driverSession) {
    return new CDriver(driverSession);
  }

  public CWebElement getWebElement() {
    return CDataState.read(ACTIVE_WEB_ELEMENT);
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod(ITestResult result) {
    try {
      // Take screenshot if test failed
      takeScreenShotIfFail(result);
    } finally {
      // Always quit the driver after each test method to prevent browser leaks
      quit();
    }
  }
}

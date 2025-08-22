package org.catools.web.tests;

import org.catools.web.drivers.CDriver;
import org.testng.annotations.Test;

public class CWebPageTest extends CWebTest<CDriver> {

  @Test(enabled = false)
  public void buildPageWithValidField() {
    open("https://google.com");
    getDriver().sendKeys("Selenium");
  }
}

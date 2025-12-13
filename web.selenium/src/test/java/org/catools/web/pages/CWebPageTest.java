package org.catools.web.pages;

import org.catools.common.extensions.verify.CVerify;
import org.catools.web.collections.CWebElements;
import org.catools.web.controls.CWebElement;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverSession;
import org.catools.web.exceptions.CPageNotOpenedException;
import org.catools.web.factory.CFindBy;
import org.catools.web.factory.CFindBys;
import org.catools.web.tests.CSeleniumTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class CWebPageTest extends CSeleniumTest<CDriver> {

  @Test
  public void openPage() {
    open("https://google.com");
    getDriver().sendKeys("Selenium");
  }

  @Test(expectedExceptions = CPageNotOpenedException.class)
  public void buildPageWithInValidTitle() {
    new WebTest1(getDriver(), "INVALID");
  }

  @Test
  public void buildPageWithValidField() {
    open("https://google.com");
    WebTest1 webTest1 = new WebTest1(getDriver(), ".*");
    CVerify.Object.isNotNull(webTest1.element1);
    CVerify.Object.isNotNull(webTest1.elements1);
  }

  @Test
  public void buildPageWithWebElementWithoutAnnotation() {
    open("https://google.com");
    WebTest2 webTest2 = new WebTest2(getDriver(), ".*");
    CVerify.Object.isNotNull(webTest2.element1);
    CVerify.Object.isNotNull(webTest2.element2);
    CVerify.Object.isNotNull(webTest2.element3);
    CVerify.Object.isNotNull(webTest2.elements4);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void buildPageWithWebElementsWithoutAnnotation() {
    new WebTest3(getDriver(), ".*");
  }

  public static class WebTest1 extends CWebPage<CDriver> {
    @CFindBy(locator = "[name='name']",
        name = "elemName",
        waitInSeconds = 10)
    private CWebElement<CDriver> element1;
    @CFindBys(xpath = "//input",
        name = "elemName",
        waitForFirstElementInSecond = 10)
    private CWebElements<CDriver> elements1;

    public WebTest1(CDriver driver, String titlePattern) {
      super(driver, titlePattern);
    }
  }

  public static class WebTest2 extends CWebPage<CDriver> {
    private CWebElement<CDriver> element1;
    @CFindBy(locator = "[name='name']")
    private CWebElement<CDriver> element2;
    @CFindBy(locator = "[name='name']",
        name = "elemName",
        waitInSeconds = 10)
    private CWebElement<CDriver> element3;
    @CFindBys(xpath = "//input",
        name = "elemName",
        waitForFirstElementInSecond = 10)
    private CWebElements<CDriver> elements4;

    public WebTest2(CDriver driver, String titlePattern) {
      super(driver, titlePattern);
    }
  }

  public static class WebTest3 extends CWebPage<CDriver> {
    @CFindBy(locator = "[name='name']",
        name = "elemName",
        waitInSeconds = 10)
    private CWebElements<CDriver> elements2;

    public WebTest3(CDriver driver, String titlePattern) {
      super(driver, titlePattern);
    }
  }

  @Override
  protected CDriver buildDriver(CDriverSession session) {
    return new CDriver(session);
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

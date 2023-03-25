package org.catools.web.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.catools.common.utils.CFileUtil;
import org.catools.common.utils.CStringUtil;
import org.catools.web.config.CGridConfigs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;

@UtilityClass
public class CGridUtil {
  public static String[] getHostNameAndPort(WebDriver driver) {
    if (!CGridConfigs.isUseRemoteDriver()) {
      return null;
    }
    String[] hostAndPort = new String[2];
    String errorMsg = "Failed to acquire remote webdriver node and port info. Root cause: ";

    try {
      String targetURL =
          String.format(
              "http://%s:%s/grid/api/testsession?session=%s",
              CGridConfigs.getGridHubIP(),
              CGridConfigs.getGridHubPort(),
              ((RemoteWebDriver) driver).getSessionId());
      Response response = RestAssured.post(targetURL);

      String proxyId = response.body().jsonPath().getString("proxyId");
      if (CStringUtil.isNotBlank(proxyId)) {
        URL myURL = new URL(proxyId);
        if ((myURL.getHost() != null) && (myURL.getPort() != -1)) {
          hostAndPort[0] = myURL.getHost();
          hostAndPort[1] = Integer.toString(myURL.getPort());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(errorMsg, e);
    }
    return hostAndPort;
  }

  public static String copyFileToNode(WebDriver driver, File srcFile) {
    return copyFileToNode(driver, srcFile, srcFile);
  }

  public static String copyFileToNode(WebDriver driver, File srcFile, File destFolder) {
    return CFileUtil.copyToRemoteFolder(srcFile, getHostNameAndPort(driver)[0], destFolder);
  }
}

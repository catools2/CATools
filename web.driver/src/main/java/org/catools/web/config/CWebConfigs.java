package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.io.CFile;
import org.catools.web.enums.CBrowser;
import org.catools.web.utils.CGridUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

public class CWebConfigs {

  public static CFile getScreenShotsFolder() {
    return CFile.of(CPathConfigs.getOutputChildFolder("screenshots"));
  }

  public static String getExpectedImagesFolderResourcePath() {
    return CHocon.get(
        Configs.WEB_EXPECTED_IMAGES_RESOURCE_PATH).asString("/images/expected");
  }

  public static CBrowser getCurrentBrowser() {
    return CBrowser.valueOf(
        CHocon.get(Configs.WEB_DEFAULT_BROWSER).asString(CBrowser.CHROME.name()));
  }

  public static CFile getDownloadFolder(WebDriver driver) {
    CFile downloadedFile;
    if (CGridConfigs.isUseRemoteDriver() && !CGridConfigs.isUseHubFolderModeIsOn()) {
      downloadedFile =
          CFile.fromRemote(
              CGridUtil.getHostNameAndPort(driver)[0], CPathConfigs.getTmpDownloadFolder());
    } else {
      downloadedFile = CFile.of(CPathConfigs.getTmpDownloadFolder());
    }
    if (!downloadedFile.exists()) {
      downloadedFile.mkdirs();
    }
    return downloadedFile;
  }

  /**
   * CDriverConfigs.getTimeout()
   *
   * @return
   */
  @Deprecated
  public static int getTimeout() {
    return CDriverConfigs.getTimeout();
  }

  public static Point getWindowsPosition() {
    String size = CHocon.get(Configs.WEB_BROWSER_WINDOWS_POSITION).asString("");
    if (StringUtils.isBlank(size)) {
      return null;
    }
    String[] split = size.split(",");
    return new Point(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
  }

  public static Dimension getWindowsDimension() {
    String size = CHocon.get(Configs.WEB_BROWSER_WINDOWS_DIMENSION).asString("");
    if (StringUtils.isBlank(size)) {
      return null;
    }
    String[] split = size.split(",");
    return new Dimension(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    WEB_EXPECTED_IMAGES_RESOURCE_PATH("catools.web.expected_images_resource_path"),
    WEB_DEFAULT_BROWSER("catools.web.browser.default"),
    WEB_BROWSER_WINDOWS_POSITION("catools.web.browser.windows_position"),
    WEB_BROWSER_WINDOWS_DIMENSION("catools.web.browser.windows_dimension"),
    WEB_BROWSER_TIMEOUT("catools.web.browser.timeout");

    private final String path;
  }
}

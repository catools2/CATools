package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.io.CFile;
import org.catools.web.enums.CBrowser;
import org.catools.web.utils.CGridUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.SessionId;

import java.util.Objects;

/**
 * Utility class that provides configuration methods for browser-related settings
 * including screenshots, downloads, window positioning, and browser selection.
 * 
 * <p>This class centralizes all browser configuration management and provides
 * convenient methods to access various browser settings from the configuration files.</p>
 * 
 * @author CATools
 * @since 1.0
 */
@UtilityClass
public class CBrowserConfigs {

  /**
   * Gets the folder where screenshots will be stored.
   * 
   * <p>This method creates a "screenshots" subfolder within the configured output directory.
   * The folder is automatically created if it doesn't exist.</p>
   * 
   * @return a CFile object representing the screenshots folder
   * 
   * @example
   * <pre>{@code
   * CFile screenshotFolder = CBrowserConfigs.getScreenShotsFolder();
   * // Saves a screenshot to the configured folder
   * File screenshot = new File(screenshotFolder.getPath(), "test_screenshot.png");
   * }</pre>
   */
  public static CFile getScreenShotsFolder() {
    return CFile.of(CPathConfigs.getOutputChildFolder("screenshots"));
  }

  /**
   * Gets the resource path for expected images used in visual testing.
   * 
   * <p>This method retrieves the configured path where baseline/expected images
   * are stored for comparison during visual regression testing.</p>
   * 
   * @return the resource path as a String, or null if not configured
   * 
   * @example
   * <pre>{@code
   * String expectedImagesPath = CBrowserConfigs.getExpectedImagesFolderResourcePath();
   * // Use path to load expected images for comparison
   * File expectedImage = new File(expectedImagesPath, "login_page_expected.png");
   * }</pre>
   */
  public static String getExpectedImagesFolderResourcePath() {
    return CHocon.asString(Configs.CATOOLS_WEB_BROWSER_EXPECTED_IMAGES_RESOURCE_PATH);
  }

  /**
   * Gets the currently configured default browser.
   * 
   * <p>This method retrieves the browser type that should be used by default
   * when no specific browser is specified for test execution.</p>
   * 
   * @return the configured default browser as a CBrowser enum value
   * 
   * @example
   * <pre>{@code
   * CBrowser defaultBrowser = CBrowserConfigs.getCurrentBrowser();
   * switch (defaultBrowser) {
   *     case CHROME:
   *         // Set up Chrome-specific options
   *         break;
   *     case FIREFOX:
   *         // Set up Firefox-specific options
   *         break;
   *     // Handle other browser types...
   * }
   * }</pre>
   * 
   * @see CBrowser
   */
  public static CBrowser getCurrentBrowser() {
    return CHocon.asEnum(Configs.CATOOLS_WEB_BROWSER_DEFAULT, CBrowser.class);
  }

  /**
   * Gets the download folder for the browser session.
   * 
   * <p>This method determines the appropriate download folder based on whether
   * a remote WebDriver is being used. For remote drivers (when not using hub folder mode),
   * it creates a remote file reference using the session's host information.
   * For local drivers, it uses the local user's download folder.</p>
   * 
   * <p>The method automatically creates the download folder if it doesn't exist.</p>
   * 
   * @param sessionId the WebDriver session ID, required when using remote driver
   *                 without hub folder mode
   * @return a CFile object representing the download folder
   * @throws NullPointerException if sessionId is null and required for remote driver setup
   * 
   * @example
   * <pre>{@code
   * // For local execution
   * CFile downloadFolder = CBrowserConfigs.getDownloadFolder(null);
   * 
   * // For remote execution
   * WebDriver driver = new RemoteWebDriver(capabilities);
   * SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
   * CFile remoteDownloadFolder = CBrowserConfigs.getDownloadFolder(sessionId);
   * 
   * // Check if a file was downloaded
   * File downloadedFile = new File(downloadFolder.getPath(), "document.pdf");
   * }</pre>
   */
  public static CFile getDownloadFolder(SessionId sessionId) {
    CFile downloadedFile;
    if (CGridConfigs.isUseRemoteDriver() && !CGridConfigs.isUseHubFolderModeIsOn()) {
      Objects.requireNonNull(sessionId);
      String[] hostNameAndPort = CGridUtil.getHostNameAndPort(sessionId);
      Objects.requireNonNull(hostNameAndPort);
      downloadedFile = CFile.fromRemote(hostNameAndPort[0], CPathConfigs.getUserDownloadFolder());
    } else {
      downloadedFile = CFile.of(CPathConfigs.getUserDownloadFolder());
    }
    if (!downloadedFile.exists()) {
      downloadedFile.mkdirs();
    }
    return downloadedFile;
  }

  /**
   * Gets the configured window position for the browser.
   * 
   * <p>This method retrieves the configured starting position (x, y coordinates)
   * for the browser window. The position is specified as comma-separated values
   * in the configuration (e.g., "100,200" for x=100, y=200).</p>
   * 
   * @return a Point object representing the window position, or null if not configured
   *         or if the configuration string is blank
   * @throws NumberFormatException if the configuration contains invalid number format
   * 
   * @example
   * <pre>{@code
   * Point windowPosition = CBrowserConfigs.getWindowsPosition();
   * if (windowPosition != null) {
   *     // Set the browser window position
   *     driver.manage().window().setPosition(windowPosition);
   *     System.out.println("Window positioned at: " + windowPosition.getX() + ", " + windowPosition.getY());
   * } else {
   *     // Use browser's default position
   *     System.out.println("Using default window position");
   * }
   * }</pre>
   */
  public static Point getWindowsPosition() {
    String size = CHocon.asString(Configs.CATOOLS_WEB_BROWSER_WINDOWS_POSITION);
    if (StringUtils.isBlank(size)) {
      return null;
    }
    String[] split = size.split(",");
    return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
  }

  /**
   * Gets the configured window dimensions for the browser.
   * 
   * <p>This method retrieves the configured window size (width and height)
   * for the browser window. The dimensions are specified as comma-separated values
   * in the configuration (e.g., "1920,1080" for width=1920, height=1080).</p>
   * 
   * @return a Dimension object representing the window size, or null if not configured
   *         or if the configuration string is blank
   * @throws NumberFormatException if the configuration contains invalid number format
   * 
   * @example
   * <pre>{@code
   * Dimension windowSize = CBrowserConfigs.getWindowsDimension();
   * if (windowSize != null) {
   *     // Set the browser window size
   *     driver.manage().window().setSize(windowSize);
   *     System.out.println("Window resized to: " + windowSize.getWidth() + "x" + windowSize.getHeight());
   * } else {
   *     // Maximize the window or use browser's default size
   *     driver.manage().window().maximize();
   *     System.out.println("Window maximized");
   * }
   * }</pre>
   */
  public static Dimension getWindowsDimension() {
    String size = CHocon.asString(Configs.CATOOLS_WEB_BROWSER_WINDOWS_DIMENSION);
    if (StringUtils.isBlank(size)) {
      return null;
    }
    String[] split = size.split(",");
    return new Dimension(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_BROWSER_EXPECTED_IMAGES_RESOURCE_PATH("catools.web.browser.expected_images_resource_path"),
    CATOOLS_WEB_BROWSER_DEFAULT("catools.web.browser.default"),
    CATOOLS_WEB_BROWSER_WINDOWS_POSITION("catools.web.browser.windows_position"),
    CATOOLS_WEB_BROWSER_WINDOWS_DIMENSION("catools.web.browser.windows_dimension");

    private final String path;
  }
}

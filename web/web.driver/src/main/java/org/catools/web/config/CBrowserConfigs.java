package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.io.CFile;
import org.catools.web.enums.CBrowser;

/**
 * Utility class that provides configuration methods for browser-related settings including
 * screenshots, downloads, window positioning, and browser selection.
 *
 * <p>This class centralizes all browser configuration management and provides convenient methods to
 * access various browser settings from the configuration files.
 *
 * @author CATools
 * @since 1.0
 */
@Slf4j
@UtilityClass
public class CBrowserConfigs {

  /**
   * Gets the folder where screenshots will be stored.
   *
   * <p>This method creates a "screenshots" subfolder within the configured output directory. The
   * folder is automatically created if it doesn't exist.
   *
   * @return a CFile object representing the screenshots folder
   * @example
   *     <pre>{@code
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
   * <p>This method retrieves the configured path where baseline/expected images are stored for
   * comparison during visual regression testing.
   *
   * @return the resource path as a String, or null if not configured
   * @example
   *     <pre>{@code
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
   * <p>This method retrieves the browser type that should be used by default when no specific
   * browser is specified for test execution.
   *
   * @return the configured default browser as a CBrowser enum value
   * @example
   *     <pre>{@code
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
   * <p>This method determines the appropriate download folder based on whether a remote Page is
   * being used. For remote drivers (when not using hub folder mode), it creates a remote file
   * reference using the session's host information. For local drivers, it uses the local user's
   * download folder.
   *
   * <p>The method automatically creates the download folder if it doesn't exist.
   *
   * @param sessionId the Page session ID, required when using remote driver without hub folder mode
   * @return a CFile object representing the download folder
   * @throws NullPointerException if sessionId is null and required for remote driver setup
   * @example
   *     <pre>{@code
   * // For local execution
   * CFile downloadFolder = CBrowserConfigs.getDownloadFolder(null);
   *
   * // For remote execution
   * Page driver = new Page(capabilities);
   * CFile remoteDownloadFolder = CBrowserConfigs.getDownloadFolder(sessionId);
   *
   * // Check if a file was downloaded
   * File downloadedFile = new File(downloadFolder.getPath(), "document.pdf");
   * }</pre>
   */
  public static CFile getDownloadFolder() {
    CFile downloadedFile = CFile.of(CPathConfigs.getUserDownloadFolder());
    if (!downloadedFile.exists() && !downloadedFile.mkdirs()) {
      return null;
    }
    return downloadedFile;
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_BROWSER_EXPECTED_IMAGES_RESOURCE_PATH(
        "catools.web.browser.expected_images_resource_path"),
    CATOOLS_WEB_BROWSER_DEFAULT("catools.web.browser.default");

    private final String path;
  }
}

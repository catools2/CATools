package org.catools.common.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.catools.common.utils.CConfigUtil;
import org.catools.common.utils.CFileUtil;

import java.io.File;

/**
 * Utility class for managing file paths used in the application.
 * Provides methods to retrieve various directories and files based on configuration values.
 */
@UtilityClass
public class CPathConfigs {

  /**
   * Retrieves the path to the temporary resources folder.
   *
   * @return A `File` object pointing to the temporary resources folder.
   */
  public static File getTmpResourcesFolder() {
    return getTempChildFolder("resources");
  }

  /**
   * Retrieves the path to the user's download folder.
   *
   * @return A `File` object pointing to the downloads folder in the user's home directory.
   */
  public static File getUserDownloadFolder() {
    return getHomeChildFolder("downloads");
  }

  /**
   * Retrieves the path to the temporary uploads folder.
   *
   * @return A `File` object pointing to the temporary uploads folder.
   */
  public static File getTmpUploadFolder() {
    return getTempChildFolder("uploads");
  }

  /**
   * Retrieves the path to the local configuration folder.
   * Ensures the folder exists by creating it if necessary.
   *
   * @return A `File` object pointing to the local configuration folder.
   */
  public static File getLocalConfigFolder() {
    File properties = getStorageChildFolder("configs");
    properties.mkdirs();
    return properties;
  }

  /**
   * Retrieves the path to the output folder.
   * The folder name is based on the current run name.
   *
   * @return A `File` object pointing to the output folder.
   */
  public static File getOutputFolder() {
    String runName = StringUtils.defaultString(CConfigUtil.getRunName());
    File file = new File(CHocon.asString(Configs.CATOOLS_PATH_OUTPUT_DIRECTORY) + runName);
    file.mkdirs();
    return file;
  }

  /**
   * Retrieves the canonical path to the output folder.
   *
   * @return A `String` representing the canonical path to the output folder.
   */
  public static String getOutputPath() {
    return CFileUtil.getCanonicalPath(getOutputFolder());
  }

  /**
   * Retrieves the root directory for output files.
   * Ensures the directory exists by creating it if necessary.
   *
   * @return A `File` object pointing to the output root directory.
   */
  public static File getOutputRoot() {
    File output = new File(CHocon.asString(Configs.CATOOLS_PATH_OUTPUT_DIRECTORY));
    CFileUtil.forceMkdir(output);
    return output;
  }

  /**
   * Creates a `File` object pointing to a file in the current directory.
   *
   * @param child The name of the file.
   * @return A `File` object pointing to the specified file in the current directory.
   */
  public static File fromCurrent(String child) {
    return new File(".", child);
  }

  /**
   * Creates a `File` object pointing to a file in the output folder.
   *
   * @param child The name of the file.
   * @return A `File` object pointing to the specified file in the output folder.
   */
  public static File fromOutput(String child) {
    return new File(CPathConfigs.getOutputPath(), child);
  }

  /**
   * Creates a `File` object pointing to a file in the storage folder.
   *
   * @param child The name of the file.
   * @return A `File` object pointing to the specified file in the storage folder.
   */
  public static File fromStorage(String child) {
    File file = new File(CPathConfigs.getStorageFolder(), child);
    file.mkdirs();
    return file;
  }

  /**
   * Creates a `File` object pointing to a file in the temporary folder.
   *
   * @param child The name of the file.
   * @return A `File` object pointing to the specified file in the temporary folder.
   */
  public static File fromTmp(String child) {
    return new File(CPathConfigs.getTempFolder(), child);
  }

  /**
   * Creates a `File` object pointing to a file in the user's home directory.
   *
   * @param child The name of the file.
   * @return A `File` object pointing to the specified file in the user's home directory.
   */
  public static File fromHome(String child) {
    return new File(CPathConfigs.getHomeFolder(), child);
  }

  /**
   * Retrieves the storage folder path.
   * Ensures the folder exists by creating it if necessary.
   *
   * @return A `File` object pointing to the storage folder.
   */
  public static File getStorageFolder() {
    File file = new File(CHocon.asString(Configs.CATOOLS_PATH_STORAGE_DIRECTORY));
    file.mkdirs();
    return file;
  }

  /**
   * Retrieves the path to the images folder inside the output directory.
   *
   * @return A `File` object pointing to the images folder.
   */
  public static File getImagesFolder() {
    return fromOutput("images");
  }

  /**
   * Retrieves the user's home directory path.
   * Ensures the directory exists by creating it if necessary.
   *
   * @return A `File` object pointing to the user's home directory.
   */
  public static File getHomeFolder() {
    File file = new File(CHocon.asString(Configs.CATOOLS_PATH_HOME_DIRECTORY));
    file.mkdirs();
    return file;
  }

  /**
   * Retrieves the path to the temporary folder inside the output directory.
   *
   * @return A `File` object pointing to the temporary folder.
   */
  public static File getTempFolder() {
    return getOutputChildFolder("tmp");
  }

  /**
   * Retrieves the path to a child folder inside the output directory.
   * Ensures the folder exists by creating it if necessary.
   *
   * @param childFolder The name of the child folder.
   * @return A `File` object pointing to the specified child folder.
   */
  public static File getOutputChildFolder(String childFolder) {
    File file = fromOutput(childFolder);
    file.mkdirs();
    return file;
  }

  /**
   * Retrieves the path to a child folder inside the user's home directory.
   * Ensures the folder exists by creating it if necessary.
   *
   * @param childFolder The name of the child folder.
   * @return A `File` object pointing to the specified child folder.
   */
  public static File getHomeChildFolder(String childFolder) {
    File file = fromHome(childFolder);
    file.mkdirs();
    return file;
  }

  /**
   * Retrieves the path to a child folder inside the temporary directory.
   * Ensures the folder exists by creating it if necessary.
   *
   * @param childFolder The name of the child folder.
   * @return A `File` object pointing to the specified child folder.
   */
  public static File getTempChildFolder(String childFolder) {
    File file = fromTmp(childFolder);
    file.mkdirs();
    return file;
  }

  /**
   * Retrieves the path to a child folder inside the storage directory.
   * Ensures the folder exists by creating it if necessary.
   *
   * @param childFolder The name of the child folder.
   * @return A `File` object pointing to the specified child folder.
   */
  public static File getStorageChildFolder(String childFolder) {
    File file = fromStorage(childFolder);
    file.mkdirs();
    return file;
  }

  /**
   * Retrieves the path to the actual images folder inside the images directory.
   *
   * @return A `File` object pointing to the actual images folder.
   */
  public static File getActualImagesFolder() {
    return CFileUtil.getChildFolder(getImagesFolder(), "actual");
  }

  /**
   * Retrieves the path to the expected images folder inside the images directory.
   *
   * @return A `File` object pointing to the expected images folder.
   */
  public static File getExpectedImagesFolder() {
    return CFileUtil.getChildFolder(getImagesFolder(), "expected");
  }

  /**
   * Retrieves the path to the diff images folder inside the images directory.
   *
   * @return A `File` object pointing to the diff images folder.
   */
  public static File getDiffImagesFolder() {
    return CFileUtil.getChildFolder(getImagesFolder(), "diff");
  }

  /**
   * Enum representing configuration keys used in the `CPathConfigs` class.
   * Each enum constant corresponds to a specific configuration path.
   */
  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    /**
     * Configuration key for the home directory path.
     * Path: `catools.paths.home_directory`
     */
    CATOOLS_PATH_HOME_DIRECTORY("catools.paths.home_directory"),

    /**
     * Configuration key for the storage directory path.
     * Path: `catools.paths.storage_directory`
     */
    CATOOLS_PATH_STORAGE_DIRECTORY("catools.paths.storage_directory"),

    /**
     * Configuration key for the output directory path.
     * Path: `catools.paths.output_directory`
     */
    CATOOLS_PATH_OUTPUT_DIRECTORY("catools.paths.output_directory");

    private final String path; // The HOCON configuration path for the enum constant.
  }
}
package org.catools.web.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.utils.CFileUtil;
import org.catools.web.config.CGridConfigs;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * Utility class for interacting with Selenium Grid infrastructure.
 * Provides methods to retrieve node information and copy files to remote grid nodes.
 * 
 * <p>This class is designed to work with Selenium Grid setups where tests run on remote nodes.
 * It helps with common tasks like getting node connection details and file transfers.</p>
 * 
 * @author catools
 * @since 1.0.0
 */
@UtilityClass
public class CGridUtil {
  /**
   * Retrieves the hostname and port of the Selenium Grid node for a given session.
   * 
   * <p>This method queries the Selenium Grid Hub to find which node is handling
   * the specified session and returns the connection details.</p>
   * 
   * @param sessionId the Selenium Page session ID to look up
   * @return a String array where index 0 is the hostname and index 1 is the port,
   *         or null if remote driver is not being used
   * @throws RuntimeException if unable to retrieve node information from the grid
   * 
   * @example
   * <pre>{@code
   * // Get the current session ID from a Page instance
   * String sessionId = ((Page) driver).getSessionId();
   * 
   * // Retrieve the node's hostname and port
   * String[] nodeInfo = CGridUtil.getHostNameAndPort(sessionId);
   * if (nodeInfo != null) {
   *     String hostname = nodeInfo[0];  // e.g., "192.168.1.100"
   *     String port = nodeInfo[1];      // e.g., "5555"
   *     System.out.println("Test is running on: " + hostname + ":" + port);
   * }
   * }</pre>
   */
  public static String[] getHostNameAndPort(String sessionId) {
    if (!CGridConfigs.isUseRemoteDriver()) {
      return null;
    }
    String[] hostAndPort = new String[2];
    String errorMsg = "Failed to acquire remote page node and port info. Root cause: ";

    try {
      String targetURL =
          String.format(
              "http://%s:%s/grid/api/testsession?session=%s",
              CGridConfigs.getGridHubIP(),
              CGridConfigs.getGridHubPort(),
              sessionId);
      Response response = RestAssured.post(targetURL);

      String proxyId = response.body().jsonPath().getString("proxyId");
      if (StringUtils.isNotBlank(proxyId)) {
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

  /**
   * Copies a file to the remote Selenium Grid node using the same filename.
   * 
   * <p>This is a convenience method that copies the source file to the remote node
   * while preserving the original filename and directory structure.</p>
   * 
   * @param sessionId the Selenium Page session ID to identify the target node
   * @param srcFile the source file to be copied to the remote node
   * @return the path where the file was copied on the remote node
   * @throws RuntimeException if the file copy operation fails or node information cannot be retrieved
   * 
   * @example
   * <pre>{@code
   * // Copy a test data file to the remote node
   * File testDataFile = new File("/local/path/test-data.json");
   * String sessionId = ((Page) driver).getSessionId();
   * 
   * String remotePath = CGridUtil.copyFileToNode(sessionId, testDataFile);
   * System.out.println("File copied to: " + remotePath);
   * 
   * // Now the file can be accessed on the remote node during test execution
   * }</pre>
   */
  public static String copyFileToNode(String sessionId, File srcFile) {
    return copyFileToNode(sessionId, srcFile, srcFile);
  }

  /**
   * Copies a file to a specific destination folder on the remote Selenium Grid node.
   * 
   * <p>This method allows you to specify both the source file and the destination
   * folder on the remote node, providing more control over where the file is placed.</p>
   * 
   * @param sessionId the Selenium Page session ID to identify the target node
   * @param srcFile the source file to be copied to the remote node
   * @param destFolder the destination folder on the remote node where the file should be placed
   * @return the path where the file was copied on the remote node
   * @throws RuntimeException if the file copy operation fails or node information cannot be retrieved
   * @throws NullPointerException if unable to retrieve hostname and port information
   * 
   * @example
   * <pre>{@code
   * // Copy a configuration file to a specific directory on the remote node
   * File configFile = new File("/local/config/app.properties");
   * File remoteDestination = new File("/remote/test/config/");
   * String sessionId = ((Page) driver).getSessionId();
   * 
   * String remotePath = CGridUtil.copyFileToNode(sessionId, configFile, remoteDestination);
   * System.out.println("Configuration file copied to: " + remotePath);
   * 
   * // The file is now available at the specified location on the remote node
   * }</pre>
   */
  public static String copyFileToNode(String sessionId, File srcFile, File destFolder) {
    return CFileUtil.copyToRemoteFolder(srcFile, Objects.requireNonNull(getHostNameAndPort(sessionId))[0], destFolder);
  }
}

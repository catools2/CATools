package org.catools.web.utils;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Utility class for common WebDriver operations and configurations.
 * <p>
 * This class provides helper methods for managing WebDriver window properties
 * such as size and position. It is designed as a utility class with static methods
 * to support common WebDriver setup operations.
 * </p>
 * 
 * <h3>Usage Examples:</h3>
 * <pre>{@code
 * // Example 1: Set specific window position and size
 * RemoteWebDriver driver = new ChromeDriver();
 * Point position = new Point(100, 50);
 * Dimension size = new Dimension(1024, 768);
 * CWebDriverUtil.setDriverWindowsSize(driver, position, size);
 * 
 * // Example 2: Only set position, maximize window
 * Point position = new Point(0, 0);
 * CWebDriverUtil.setDriverWindowsSize(driver, position, null);
 * 
 * // Example 3: Use default position and size (top-left corner, maximized)
 * CWebDriverUtil.setDriverWindowsSize(driver, null, null);
 * }</pre>
 * 
 * @author CATools Team
 * @version 1.0
 * @since 1.0
 */
@UtilityClass
public class CWebDriverUtil {

  /**
   * Configures the WebDriver window position and size according to the specified parameters.
   * <p>
   * This method provides flexible window configuration options:
   * <ul>
   *   <li>If {@code windowsPosition} is null, the window is positioned at (0,0)</li>
   *   <li>If {@code windowsSize} is null, the window is maximized</li>
   *   <li>If an exception occurs during configuration, the WebDriver is automatically closed</li>
   * </ul>
   * </p>
   * 
   * <h4>Usage Examples:</h4>
   * <pre>{@code
   * // Example 1: Set window to specific position and size
   * RemoteWebDriver driver = new ChromeDriver();
   * Point position = new Point(200, 100);
   * Dimension size = new Dimension(1280, 800);
   * driver = CWebDriverUtil.setDriverWindowsSize(driver, position, size);
   * 
   * // Example 2: Position window at top-left and maximize
   * Point topLeft = new Point(0, 0);
   * driver = CWebDriverUtil.setDriverWindowsSize(driver, topLeft, null);
   * 
   * // Example 3: Use defaults (position 0,0 and maximized)
   * driver = CWebDriverUtil.setDriverWindowsSize(driver, null, null);
   * 
   * // Example 4: Center window with specific size
   * Point center = new Point(300, 200);
   * Dimension standardSize = new Dimension(1024, 768);
   * driver = CWebDriverUtil.setDriverWindowsSize(driver, center, standardSize);
   * }</pre>
   * 
   * @param webDriver the RemoteWebDriver instance to configure. Must not be null.
   * @param windowsPosition the desired window position on screen. If null, defaults to Point(0,0).
   * @param windowsSize the desired window dimensions. If null, the window will be maximized.
   * @return the same RemoteWebDriver instance after configuration
   * @throws RuntimeException if an error occurs during window configuration. 
   *                         The WebDriver will be automatically closed before rethrowing the exception.
   * 
   * @see org.openqa.selenium.Point
   * @see org.openqa.selenium.Dimension
   * @see org.openqa.selenium.remote.RemoteWebDriver#manage()
   */
  public static RemoteWebDriver setDriverWindowsSize(
      RemoteWebDriver webDriver, Point windowsPosition, Dimension windowsSize) {
    try {
      if (windowsPosition != null) {
        webDriver.manage().window().setPosition(windowsPosition);
      } else {
        webDriver.manage().window().setPosition(new Point(0, 0));
      }

      if (windowsSize != null) {
        webDriver.manage().window().setSize(windowsSize);
      } else {
        webDriver.manage().window().maximize();
      }
    } catch (Exception e) {
      if (webDriver != null) {
        webDriver.quit();
      }
      throw e;
    }
    return webDriver;
  }
}

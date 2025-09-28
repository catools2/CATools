package org.catools.web.drivers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;
import org.catools.common.tests.CTest;
import org.catools.common.utils.CObjectUtil;
import org.catools.web.config.CBrowserConfigs;
import org.catools.web.config.CProxyConfigs;
import org.catools.web.entities.CWebPageInfo;
import org.catools.web.listeners.CDriverListener;
import org.catools.web.metrics.CWebPageTransitionInfo;
import org.catools.web.utils.CWebDriverUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.catools.web.entities.CWebPageInfo.BLANK_PAGE;

/**
 * CDriverSession manages the lifecycle and interactions of a WebDriver session with enhanced capabilities
 * for page tracking, metrics collection, and event handling.
 * 
 * <p>This class provides a wrapper around Selenium WebDriver that includes:
 * <ul>
 *   <li>Automatic page transition detection and tracking</li>
 *   <li>Integration with Chrome DevTools for performance metrics</li>
 *   <li>Event-driven architecture with customizable listeners</li>
 *   <li>Session state management and validation</li>
 *   <li>Proxy configuration support</li>
 * </ul>
 * 
 * <p>The driver automatically handles alert dialogs to prevent interference with page tracking
 * and listener notifications. When an alert is present, session updates and listener calls
 * are suspended to maintain flow integrity.
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Create a driver session for a test
 * CTest test = new CTest("MyTestCase");
 * CDriverProvider provider = new CDriverProvider(CDriverType.CHROME);
 * CDriverSession session = new CDriverSession(test, provider);
 * 
 * // Add custom listeners
 * session.addListeners(new MyCustomListener());
 * 
 * // Start the session
 * session.startSession();
 * 
 * // Perform actions with automatic tracking
 * session.performActionOnDriver("navigate", driver -> {
 *     driver.get("https://example.com");
 *     return null;
 * });
 * 
 * // Check if session is active
 * if (session.isActive()) {
 *     // Session is ready for use
 * }
 * }</pre>
 * 
 * @author CATools Team
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class CDriverSession {
  @Getter(AccessLevel.PRIVATE)
  private final CList<CDriverListener> listeners = new CList<>();

  @Getter(AccessLevel.PRIVATE)
  private final CDevTools devTools = new CDevTools();

  @Getter(AccessLevel.PRIVATE)
  private final CTest testInstance;

  @Setter(AccessLevel.PRIVATE)
  private CWebPageInfo previousPage = BLANK_PAGE;

  @Setter(AccessLevel.PRIVATE)
  private CWebPageInfo currentPage = BLANK_PAGE;

  @Setter(AccessLevel.PRIVATE)
  @Getter(AccessLevel.PRIVATE)
  private RemoteWebDriver webDriver;

  private CDriverProvider driverProvider;
  private Dimension windowsSize = CBrowserConfigs.getWindowsDimension();
  private Point windowsPosition = CBrowserConfigs.getWindowsPosition();

  @Setter(AccessLevel.PRIVATE)
  @Getter(AccessLevel.PRIVATE)
  private BiPredicate<CDriverSession, WebDriver> pageTransitionIndicator;

  /**
   * Creates a new CDriverSession with the specified test instance and driver provider.
   * Uses default page transition detection logic.
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CTest test = new CTest("LoginTest");
   * CDriverProvider provider = new CDriverProvider(CDriverType.CHROME);
   * CDriverSession session = new CDriverSession(test, provider);
   * }</pre>
   * 
   * @param testInstance the test instance associated with this session
   * @param driverProvider the provider responsible for creating WebDriver instances
   */
  public CDriverSession(CTest testInstance, CDriverProvider driverProvider) {
    this(testInstance, driverProvider, null);
  }

  /**
   * Creates a new CDriverSession with the specified test instance, driver provider, and custom
   * page transition indicator.
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CTest test = new CTest("NavigationTest");
   * CDriverProvider provider = new CDriverProvider(CDriverType.FIREFOX);
   * 
   * // Custom page transition detector
   * BiPredicate<CDriverSession, WebDriver> customDetector = (session, driver) -> {
   *     String currentUrl = driver.getCurrentUrl();
   *     return !currentUrl.equals(session.getCurrentPage().getUrl());
   * };
   * 
   * CDriverSession session = new CDriverSession(test, provider, customDetector);
   * }</pre>
   * 
   * @param testInstance the test instance associated with this session
   * @param driverProvider the provider responsible for creating WebDriver instances
   * @param pageTransitionIndicator custom logic to detect when a page transition has occurred
   */
  public CDriverSession(CTest testInstance, CDriverProvider driverProvider, BiPredicate<CDriverSession, WebDriver> pageTransitionIndicator) {
    this.testInstance = testInstance;
    this.driverProvider = driverProvider;
    this.pageTransitionIndicator = pageTransitionIndicator;
    addProxyIfEnabled();
  }

  /**
   * Initializes and starts the WebDriver session by creating the driver instance and
   * configuring window size and position.
   * 
   * <p>This method must be called before performing any actions on the driver.
   * It creates the WebDriver instance using the configured provider and applies
   * the specified window dimensions and position.
   * 
   * <p><strong>Example:</strong>
   * <pre>{@code
   * CDriverSession session = new CDriverSession(test, provider);
   * 
   * // Configure window size before starting (optional)
   * session.setWindowsSize(new Dimension(1920, 1080));
   * session.setWindowsPosition(new Point(0, 0));
   * 
   * // Start the session
   * session.startSession();
   * 
   * // Now ready to perform actions
   * session.performActionOnDriver("navigate", driver -> {
   *     driver.get("https://example.com");
   *     return null;
   * });
   * }</pre>
   * 
   * @throws RuntimeException if the driver cannot be created or configured
   */
  public void startSession() {
    setWebDriver(driverProvider.build(listeners));
    CWebDriverUtil.setDriverWindowsSize(webDriver, windowsPosition, windowsSize);
  }

  /**
   * Executes an action on the WebDriver with automatic tracking, metrics collection, and event handling.
   * 
   * <p>This method provides a standardized way to perform WebDriver actions with enhanced capabilities:
   * <ul>
   *   <li>Automatic page information updates</li>
   *   <li>Before/after action event notifications to listeners</li>
   *   <li>DevTools metrics collection (when supported)</li>
   *   <li>Performance timing measurement</li>
   *   <li>Alert handling to prevent interference</li>
   * </ul>
   * 
   * <p>If the session is not active or an alert is present, the method handles these conditions
   * gracefully and may skip certain operations to maintain stability.
   * 
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * // Navigate to a page
   * session.performActionOnDriver("navigate to login", driver -> {
   *     driver.get("https://example.com/login");
   *     return null;
   * });
   * 
   * // Fill a form and return element text
   * String welcomeMessage = session.performActionOnDriver("login user", driver -> {
   *     driver.$(By.id("username")).sendKeys("testuser");
   *     driver.$(By.id("password")).sendKeys("password123");
   *     driver.$(By.id("loginButton")).click();
   *     return driver.$(By.id("welcomeMessage")).getText();
   * });
   * 
   * // Perform complex interactions
   * Boolean isElementVisible = session.performActionOnDriver("check visibility", driver -> {
   *     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   *     try {
   *         WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("target")));
   *         return element.isDisplayed();
   *     } catch (TimeoutException e) {
   *         return false;
   *     }
   * });
   * }</pre>
   * 
   * @param <T> the return type of the action function
   * @param actionName a descriptive name for the action being performed (used in logging and metrics)
   * @param consumer the function to execute on the WebDriver instance
   * @return the result of the consumer function, or null if the session is not active
   */
  public <T> T performActionOnDriver(String actionName, Function<RemoteWebDriver, T> consumer) {
    if (!isActive()) {
      return null;
    }

    updatePageInfo(null, CDate.now());

    if (!alertPresent()) {
      if (listeners.isNotEmpty()) {
        listeners.forEach(event -> event.beforeAction(webDriver, currentPage, actionName));
      }
      if (webDriver instanceof HasDevTools || webDriver.getCapabilities().asMap().containsKey("se:cdp"))
        devTools.startRecording(testInstance, actionName, webDriver);
    }

    CDate startTime = CDate.now();
    CWebPageInfo pageBeforeAction = CObjectUtil.clone(currentPage);

    T apply = consumer.apply(webDriver);

    if (!alertPresent()) {
      CWebPageTransitionInfo driverMetricInfo = devTools.stopRecording(previousPage, currentPage);
      updatePageInfo(driverMetricInfo, startTime);
    }

    if (listeners.isNotEmpty()) {
      listeners.forEach(event ->
          event.afterAction(
              actionName,
              webDriver,
              pageBeforeAction,
              CObjectUtil.clone(currentPage),
              null,
              startTime,
              startTime.getDurationToNow().getNano()));
    }

    return apply;
  }

  /**
   * Checks if the WebDriver session is currently active and ready for use.
   * 
   * <p>A session is considered active when:
   * <ul>
   *   <li>The WebDriver instance is not null</li>
   *   <li>The WebDriver has a valid session ID</li>
   * </ul>
   * 
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * CDriverSession session = new CDriverSession(test, provider);
   * 
   * // Before starting session
   * if (!session.isActive()) {
   *     session.startSession();
   * }
   * 
   * // Before performing actions
   * if (session.isActive()) {
   *     session.performActionOnDriver("click", driver -> {
   *         driver.$(By.id("button")).click();
   *         return null;
   *     });
   * } else {
   *     throw new IllegalStateException("Session is not active");
   * }
   * 
   * // In cleanup methods
   * if (session.isActive()) {
   *     // Perform cleanup actions
   *     session.reset();
   * }
   * }</pre>
   * 
   * @return true if the session is active and ready for use, false otherwise
   */
  public boolean isActive() {
    return webDriver != null && webDriver.getSessionId() != null;
  }

  /**
   * Resets the session state by clearing page information and WebDriver reference.
   * 
   * <p>This method:
   * <ul>
   *   <li>Resets previous and current page information to blank pages</li>
   *   <li>Clears the WebDriver reference</li>
   *   <li>Does not close the actual WebDriver session - call quit() on the driver separately if needed</li>
   * </ul>
   * 
   * <p>Use this method when you need to clean up session state without necessarily
   * terminating the underlying WebDriver session.
   * 
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * // Reset after test completion
   * @AfterEach
   * public void tearDown() {
   *     if (session.isActive()) {
   *         // First quit the actual driver
   *         session.performActionOnDriver("quit", driver -> {
   *             driver.quit();
   *             return null;
   *         });
   *     }
   *     
   *     // Then reset session state
   *     session.reset();
   * }
   * 
   * // Reset between test steps while keeping driver alive
   * public void resetForNextStep() {
   *     session.reset();
   *     // Start fresh session with same driver provider
   *     session.startSession();
   * }
   * 
   * // Emergency cleanup
   * try {
   *     // Some risky operation
   *     session.performActionOnDriver("risky", driver -> { ... });
   * } catch (Exception e) {
   *     session.reset(); // Clean state on error
   *     throw e;
   * }
   * }</pre>
   */
  public void reset() {
    this.previousPage = this.currentPage = BLANK_PAGE;
    this.webDriver = null;
  }

  /**
   * Adds one or more event listeners to the session for receiving notifications
   * about driver actions and page changes.
   * 
   * <p>Listeners receive callbacks for:
   * <ul>
   *   <li>Before driver initialization</li>
   *   <li>Before and after action execution</li>
   *   <li>Page change events</li>
   * </ul>
   * 
   * <p>Null listeners are automatically filtered out and ignored.
   * 
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * // Add a single listener
   * CDriverListener loggingListener = new CDriverListener() {
   *     @Override
   *     public void beforeAction(WebDriver driver, CWebPageInfo currentPage, String actionName) {
   *         System.out.println("About to execute: " + actionName);
   *     }
   *     
   *     @Override
   *     public void afterAction(String actionName, WebDriver driver, CWebPageInfo pageBefore, 
   *                           CWebPageInfo pageAfter, Throwable error, CDate startTime, long durationNanos) {
   *         System.out.println("Completed: " + actionName + " in " + (durationNanos / 1_000_000) + "ms");
   *     }
   * };
   * session.addListeners(loggingListener);
   * 
   * // Add multiple listeners
   * CDriverListener metricsListener = new MetricsCollectorListener();
   * CDriverListener screenshotListener = new ScreenshotOnErrorListener();
   * session.addListeners(metricsListener, screenshotListener);
   * 
   * // Add listeners with null safety
   * CDriverListener conditionalListener = someCondition ? new MyListener() : null;
   * session.addListeners(conditionalListener); // null is safely ignored
   * 
   * // Custom page change listener
   * CDriverListener pageListener = new CDriverListener() {
   *     @Override
   *     public void onPageChanged(WebDriver driver, CWebPageTransitionInfo transitionInfo, 
   *                              CDate startTime, long durationNanos) {
   *         System.out.println("Page changed to: " + driver.getCurrentUrl());
   *         if (transitionInfo != null) {
   *             System.out.println("Transition metrics: " + transitionInfo);
   *         }
   *     }
   * };
   * session.addListeners(pageListener);
   * }</pre>
   * 
   * @param listeners one or more listener instances to add (null values are ignored)
   */
  public void addListeners(CDriverListener... listeners) {
    this.listeners.addAll(CList.of(listeners).getAll(Objects::nonNull));
  }

  /**
   * Updates the current page information and triggers page change events if a transition is detected.
   * 
   * <p>This method:
   * <ul>
   *   <li>Skips updates when alerts are present to avoid interference</li>
   *   <li>Resets to blank pages when session is inactive</li>
   *   <li>Uses the configured page transition indicator to detect changes</li>
   *   <li>Triggers listener notifications when page changes occur</li>
   * </ul>
   * 
   * <p>Page transitions are detected using the configured {@code pageTransitionIndicator}.
   * If no custom indicator is provided, updates rely on external calls to trigger changes.
   * 
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * // Manual page update (typically called internally)
   * CDate actionStart = CDate.now();
   * session.updatePageInfo(null, actionStart);
   * 
   * // Update with transition metrics from DevTools
   * CWebPageTransitionInfo metrics = devTools.getLastTransition();
   * session.updatePageInfo(metrics, actionStart);
   * 
   * // Custom usage in action wrapper
   * public void customNavigate(String url) {
   *     CDate startTime = CDate.now();
   *     webDriver.get(url);
   *     
   *     // Force page info update after navigation
   *     session.updatePageInfo(null, startTime);
   * }
   * 
   * // Integration with custom page detection
   * BiPredicate<CDriverSession, WebDriver> customDetector = (session, driver) -> {
   *     // Custom logic to detect page changes
   *     return !driver.getCurrentUrl().equals(session.getCurrentPage().getUrl());
   * };
   * CDriverSession session = new CDriverSession(test, provider, customDetector);
   * }</pre>
   * 
   * @param driverMetricInfo optional transition metrics from DevTools or other sources
   * @param startTime the time when the action that might cause a page change was initiated
   */
  public void updatePageInfo(CWebPageTransitionInfo driverMetricInfo, CDate startTime) {
    if (alertPresent()) {
      return;
    }

    if (!isActive()) {
      previousPage = BLANK_PAGE;
      currentPage = BLANK_PAGE;
      return;
    }

    if (pageTransitionIndicator != null && pageTransitionIndicator.test(this, webDriver)) {
      previousPage = currentPage;
      currentPage = getPageInfo(webDriver);
      onPageChangeEvents(driverMetricInfo, startTime);
    }
  }

  /**
   * Creates a CWebPageInfo object from the current state of the WebDriver.
   * 
   * <p>This static utility method safely extracts page information from a WebDriver instance,
   * handling exceptions gracefully by returning a blank page info object when errors occur.
   * 
   * <p>The returned {@link CWebPageInfo} contains:
   * <ul>
   *   <li>Current page URL</li>
   *   <li>Page title</li>
   *   <li>Timestamp of page load</li>
   *   <li>Other page metadata</li>
   * </ul>
   * 
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * // Get current page info from active driver
   * WebDriver driver = new ChromeDriver();
   * driver.get("https://example.com");
   * CWebPageInfo pageInfo = CDriverSession.getPageInfo(driver);
   * System.out.println("Current page: " + pageInfo.getUrl());
   * System.out.println("Page title: " + pageInfo.getTitle());
   * 
   * // Safe usage with potentially null driver
   * WebDriver nullDriver = null;
   * CWebPageInfo blankInfo = CDriverSession.getPageInfo(nullDriver);
   * assert blankInfo == CWebPageInfo.BLANK_PAGE;
   * 
   * // Usage in custom page transition detection
   * BiPredicate<CDriverSession, WebDriver> detector = (session, driver) -> {
   *     CWebPageInfo currentInfo = CDriverSession.getPageInfo(driver);
   *     CWebPageInfo previousInfo = session.getCurrentPage();
   *     return !currentInfo.getUrl().equals(previousInfo.getUrl());
   * };
   * 
   * // Error handling example
   * try {
   *     driver.get("invalid-url");
   * } catch (Exception e) {
   *     // Even if navigation fails, getPageInfo won't throw
   *     CWebPageInfo safeInfo = CDriverSession.getPageInfo(driver);
   *     // Will return BLANK_PAGE if driver state is invalid
   * }
   * }</pre>
   * 
   * @param webDriver the WebDriver instance to extract page information from
   * @return a CWebPageInfo object containing current page details, or BLANK_PAGE if driver is null or throws an exception
   */
  public static CWebPageInfo getPageInfo(WebDriver webDriver) {
    try {
      return webDriver == null
          ? BLANK_PAGE
          : new CWebPageInfo(webDriver);
    } catch (Throwable t) {
      return BLANK_PAGE;
    }
  }

  private void onPageChangeEvents(CWebPageTransitionInfo driverMetricInfo, CDate startTime) {
    if (listeners.isNotEmpty()) {
      listeners.forEach(event -> event.onPageChanged(webDriver, driverMetricInfo, startTime, CDate.now().getDurationToNow().getNano()));
    }
  }

  /**
   * Gets the platform name of the current WebDriver session.
   * 
   * <p>Returns the platform information from the WebDriver capabilities,
   * which indicates the operating system where the browser is running.
   * This is useful for cross-platform testing and platform-specific logic.
   * 
   * <p><strong>Examples:</strong>
   * <pre>{@code
   * // Check platform for conditional logic
   * Platform platform = session.getPlatform();
   * if (platform == Platform.WINDOWS) {
   *     // Windows-specific test steps
   *     session.performActionOnDriver("windows action", driver -> {
   *         // Use Windows-specific keyboard shortcuts
   *         driver.$(By.tagName("body")).sendKeys(Keys.CONTROL + "a");
   *         return null;
   *     });
   * } else if (platform == Platform.MAC) {
   *     // Mac-specific test steps
   *     session.performActionOnDriver("mac action", driver -> {
   *         // Use Mac-specific keyboard shortcuts
   *         driver.$(By.tagName("body")).sendKeys(Keys.COMMAND + "a");
   *         return null;
   *     });
   * }
   * 
   * // Platform-aware test reporting
   * @Test
   * public void testAcrossPlatforms() {
   *     session.startSession();
   *     Platform platform = session.getPlatform();
   *     
   *     System.out.println("Running test on platform: " + platform);
   *     
   *     // Perform test steps...
   *     
   *     // Include platform in test results
   *     TestResult result = new TestResult();
   *     result.setPlatform(platform.toString());
   * }
   * 
   * // Null safety check
   * Platform platform = session.getPlatform();
   * if (platform != null) {
   *     String platformName = platform.toString();
   *     // Use platform information
   * } else {
   *     // Session not active or platform unavailable
   *     System.out.println("Platform information not available");
   * }
   * }</pre>
   * 
   * @return the Platform enum value representing the operating system, or null if session is not active
   */
  public Platform getPlatform() {
    return !isActive() ? null : webDriver.getCapabilities().getPlatformName();
  }

  private boolean alertPresent() {
    try {
      if (!isActive()) return false;
      webDriver.switchTo().alert();
      return true;
    } catch (Throwable t) {
      return false;
    }
  }

  private void addProxyIfEnabled() {
    if (!CProxyConfigs.isEnabled()) return;
    addListeners(new CDriverListener() {
      @Override
      public void beforeInit(Capabilities capabilities) {
        MutableCapabilities mc = (MutableCapabilities) capabilities;
        mc.setCapability(CapabilityType.PROXY, CProxyConfigs.getProxy());
      }
    });
  }
}

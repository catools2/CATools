package org.catools.web.listeners;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.otel.COTelConfig;
import org.catools.web.config.CDriverConfigs;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v143.network.Network;
import org.openqa.selenium.devtools.v143.network.model.Request;
import org.openqa.selenium.devtools.v143.network.model.Response;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OpenTelemetry-enabled network listener for Selenium WebDriver that tracks HTTP requests and
 * responses using Chrome DevTools Protocol. This listener provides comprehensive network monitoring
 * and tracing capabilities for web automation tests.
 *
 * <p>The listener captures detailed information about network requests including:
 *
 * <ul>
 *   <li>Request method, URL, headers, and timing
 *   <li>Response status codes, MIME types, and headers
 *   <li>Network latency measurements
 *   <li>Error handling for failed requests
 * </ul>
 *
 * <p>All network activities are traced using OpenTelemetry spans, providing distributed tracing
 * capabilities and integration with observability platforms.
 *
 * <h3>Basic Usage Example:</h3>
 *
 * <pre>{@code
 * // Initialize the network listener
 * CDriverOTelNetworkListener networkListener = new CDriverOTelNetworkListener(
 *     "WebDriverTracer",
 *     "LoginPageTest"
 * );
 *
 * // Get DevTools from Chrome/Edge driver
 * WebDriver driver = new ChromeDriver();
 * DevTools devTools = ((HasDevTools) driver).getDevTools();
 * devTools.createSession();
 *
 * // Attach the listener to start monitoring
 * networkListener.attach(devTools);
 *
 * try {
 *     // Perform web actions - all network requests will be traced
 *     driver.get("https://example.com/login");
 *     driver.$(By.id("username")).sendKeys("testuser");
 *     driver.$(By.id("password")).sendKeys("password");
 *     driver.$(By.id("loginBtn")).click();
 * } finally {
 *     // Clean up and finish tracing
 *     networkListener.detach();
 *     driver.quit();
 * }
 * }</pre>
 *
 * <h3>Page Load Performance Testing Example:</h3>
 *
 * <pre>{@code
 * public class PageLoadTest {
 *     private WebDriver driver;
 *     private CDriverOTelNetworkListener networkListener;
 *
 *     @BeforeMethod
 *     public void setup() {
 *         driver = new ChromeDriver();
 *         DevTools devTools = ((HasDevTools) driver).getDevTools();
 *         devTools.createSession();
 *
 *         // Create listener for this specific test
 *         networkListener = new CDriverOTelNetworkListener(
 *             "PerformanceTracer",
 *             "HomePage_LoadTime_Test"
 *         );
 *         networkListener.attach(devTools);
 *     }
 *
 *     @Test
 *     public void testHomePageLoadPerformance() {
 *         long startTime = System.currentTimeMillis();
 *         driver.get("https://myapp.com/home");
 *
 *         // Wait for page to fully load
 *         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
 *         wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mainContent")));
 *
 *         long loadTime = System.currentTimeMillis() - startTime;
 *         System.out.println("Page loaded in: " + loadTime + "ms");
 *
 *         // All network requests during page load are automatically traced
 *         // Check OpenTelemetry traces for detailed network timing
 *     }
 *
 *     @AfterMethod
 *     public void teardown() {
 *         networkListener.detach();
 *         driver.quit();
 *     }
 * }
 * }</pre>
 *
 * <h3>API Testing with Network Monitoring Example:</h3>
 *
 * <pre>{@code
 * // Monitor AJAX calls and API requests during user interactions
 * CDriverOTelNetworkListener apiMonitor = new CDriverOTelNetworkListener(
 *     "APITracer",
 *     "UserDashboard_API_Calls"
 * );
 *
 * DevTools devTools = ((HasDevTools) driver).getDevTools();
 * devTools.createSession();
 * apiMonitor.attach(devTools);
 *
 * // Navigate to dashboard
 * driver.get("https://app.example.com/dashboard");
 *
 * // Trigger actions that make API calls
 * driver.$(By.id("refreshData")).click();
 * driver.$(By.id("loadUserProfile")).click();
 *
 * // All API requests (GET /api/dashboard, GET /api/user/profile, etc.)
 * // will be captured with timing information in OpenTelemetry traces
 *
 * apiMonitor.detach();
 * }</pre>
 *
 * <p><strong>Configuration Requirements:</strong>
 *
 * <ul>
 *   <li>Network metrics collection must be enabled via {@link
 *       CDriverConfigs#isCollectNetworkMetricsEnable()}
 *   <li>OpenTelemetry must be enabled via {@link COTelConfig#isEnable()}
 *   <li>Chrome or Edge WebDriver is required (supports Chrome DevTools Protocol)
 * </ul>
 *
 * <p><strong>Thread Safety:</strong>
 *
 * <p>This class is thread-safe and uses concurrent collections to handle multiple simultaneous
 * network requests. Each request is tracked independently using request IDs.
 *
 * @see COTelConfig
 * @see CDriverConfigs
 */
public class CDriverOTelNetworkListener {
  private final Map<String, Span> inFlightSpans = new ConcurrentHashMap<>();
  private final Map<String, Instant> startTimes = new ConcurrentHashMap<>();
  private final Tracer tracer;
  private final Span parent;

  /**
   * Creates a new network listener with OpenTelemetry tracing capabilities.
   *
   * <p>This constructor initializes the tracer and creates a parent span that will contain all
   * network request spans. The parent span represents the overall action or test scenario being
   * traced.
   *
   * @param tracerName the name of the OpenTelemetry tracer to use for creating spans. This should
   *     be descriptive of the component or module being traced (e.g., "WebDriverTracer",
   *     "SeleniumTests")
   * @param actionName the name of the parent span that represents the high-level action being
   *     performed (e.g., "LoginTest", "CheckoutProcess", "PageLoad"). All network requests will be
   *     traced as children of this span
   * @throws IllegalStateException if OpenTelemetry is not properly configured
   *     <h4>Example:</h4>
   *     <pre>{@code
   * // Create listener for login test scenario
   * CDriverOTelNetworkListener listener = new CDriverOTelNetworkListener(
   *     "AuthenticationTracer",
   *     "UserLogin_E2E_Test"
   * );
   *
   * // Create listener for performance monitoring
   * CDriverOTelNetworkListener perfListener = new CDriverOTelNetworkListener(
   *     "PerformanceTracer",
   *     "HomePage_LoadTime_Analysis"
   * );
   *
   * }</pre>
   */
  public CDriverOTelNetworkListener(String tracerName, String actionName) {
    this.tracer = COTelConfig.getOpenTelemetry().getTracer(tracerName);
    this.parent = tracer.spanBuilder(actionName).setSpanKind(SpanKind.CLIENT).startSpan();
  }

  /**
   * Attaches the network listener to Chrome DevTools to begin monitoring network activity.
   *
   * <p>This method registers event listeners for three types of network events:
   *
   * <ul>
   *   <li><strong>Request Will Be Sent:</strong> Captures outgoing requests with method, URL, and
   *       headers
   *   <li><strong>Response Received:</strong> Captures response data including status, timing, and
   *       headers
   *   <li><strong>Loading Failed:</strong> Handles network errors and connection failures
   * </ul>
   *
   * <p>Each network request is traced as an individual span with detailed attributes. The method
   * automatically handles span lifecycle management and parent-child relationships.
   *
   * @param devTools the Chrome DevTools instance to attach listeners to. Must be from a Chrome or
   *     Edge WebDriver with an active DevTools session
   * @throws IllegalArgumentException if devTools is null
   * @throws IllegalStateException if DevTools session is not active
   *     <h4>Usage Example:</h4>
   *     <pre>{@code
   * WebDriver driver = new ChromeDriver();
   * DevTools devTools = ((HasDevTools) driver).getDevTools();
   *
   * // Create and start DevTools session
   * devTools.createSession();
   *
   * // Create and attach network listener
   * CDriverOTelNetworkListener listener = new CDriverOTelNetworkListener(
   *     "E2ETracer", "CheckoutFlow"
   * );
   * listener.attach(devTools);
   *
   * // Now all network requests will be traced
   * driver.get("https://shop.example.com");
   * driver.$(By.id("addToCart")).click();
   *
   * // Remember to detach when done
   * listener.detach();
   *
   * }</pre>
   *     <h4>Conditional Attachment:</h4>
   *     <p>The listener will only attach if both network metrics collection and OpenTelemetry are
   *     enabled in the configuration. If either is disabled, this method returns immediately
   *     without attaching any listeners.
   *     <h4>Traced Attributes:</h4>
   *     <p>The following attributes are automatically captured for each request:
   *     <ul>
   *       <li><code>method</code> - HTTP method (GET, POST, etc.)
   *       <li><code>schema</code> - URL scheme (http, https)
   *       <li><code>host</code> - Target hostname
   *       <li><code>path</code> - URL path
   *       <li><code>port</code> - Target port number
   *       <li><code>status</code> - HTTP response status code
   *       <li><code>mime</code> - Response content type
   *       <li><code>protocol</code> - HTTP protocol version
   *       <li><code>latency_ms</code> - Request/response timing in milliseconds
   *       <li><code>header.*</code> - All request and response headers
   *     </ul>
   *
   * @see #detach()
   * @see CDriverConfigs#isCollectNetworkMetricsEnable()
   * @see COTelConfig#isEnable()
   */
  public void attach(DevTools devTools) {
    if (!CDriverConfigs.isCollectNetworkMetricsEnable() || !COTelConfig.isEnable()) return;

    devTools.addListener(
        Network.requestWillBeSent(),
        event -> {
          Request req = event.getRequest();
          try (Scope scope = parent.makeCurrent()) {
            // Any spans started here will be children of parent

            URI uri = URI.create(req.getUrl());

            Span span =
                tracer
                    .spanBuilder("%s %s".formatted(req.getMethod(), uri.getHost()))
                    .setSpanKind(SpanKind.CLIENT)
                    .startSpan();

            inFlightSpans.put(event.getRequestId().toString(), span);
            startTimes.put(event.getRequestId().toString(), Instant.now());

            addUriAttributes(span, uri);
            span.setAttribute("method", req.getMethod());
            req.getHeaders().forEach((k, v) -> span.setAttribute("header." + k, String.valueOf(v)));
          }
        });

    devTools.addListener(
        Network.responseReceived(),
        event -> {
          String id = event.getRequestId().toString();
          Span span = inFlightSpans.remove(id);
          Instant start = startTimes.remove(id);

          if (span != null) {
            try (Scope scope = span.makeCurrent()) {
              Span child = tracer.spanBuilder("response").setSpanKind(SpanKind.SERVER).startSpan();

              // Duration
              if (start != null) {
                long latency = Instant.now().toEpochMilli() - start.toEpochMilli();
                child.setAttribute("latency_ms", latency);
              }

              Response res = event.getResponse();
              URI uri = URI.create(res.getUrl());
              addUriAttributes(child, uri);
              child.setAttribute("status", res.getStatus());
              child.setAttribute("mime", res.getMimeType());
              res.getProtocol().ifPresent(p -> child.setAttribute("protocol", p));
              res.getHeaders()
                  .forEach((k, v) -> child.setAttribute("header." + k, String.valueOf(v)));

              if (res.getStatus() >= 400) {
                child.setStatus(StatusCode.ERROR, "HTTP " + res.getStatus());
              }

              child.end();
            } finally {
              span.end();
            }
          }
        });

    // Cleanup in a case when request failed
    devTools.addListener(
        Network.loadingFailed(),
        fail -> {
          String id = fail.getRequestId().toString();
          Span span = inFlightSpans.remove(id);
          startTimes.remove(id);

          if (span != null) {
            span.setStatus(StatusCode.ERROR, fail.getErrorText());
            span.setAttribute("error.type", "network");
            span.end();
          }
        });
  }

  /**
   * Detaches the network listener and finalizes the parent trace span.
   *
   * <p>This method should be called when network monitoring is no longer needed. It properly closes
   * the parent span, ensuring all trace data is correctly recorded in the OpenTelemetry backend.
   *
   * <p><strong>Important:</strong> Failing to call this method may result in incomplete traces or
   * resource leaks. Always call this method in a finally block or equivalent cleanup mechanism.
   *
   * <h4>Usage Example:</h4>
   *
   * <pre>{@code
   * CDriverOTelNetworkListener listener = new CDriverOTelNetworkListener(
   *     "TestTracer", "MyTest"
   * );
   *
   * try {
   *     listener.attach(devTools);
   *
   *     // Perform test actions
   *     driver.get("https://example.com");
   *     // ... more test steps ...
   *
   * } finally {
   *     // Always detach to complete the trace
   *     listener.detach();
   * }
   * }</pre>
   *
   * <h4>TestNG Integration Example:</h4>
   *
   * <pre>{@code
   * public class NetworkMonitoredTest {
   *     private CDriverOTelNetworkListener networkListener;
   *     private WebDriver driver;
   *
   *     @BeforeMethod
   *     public void setup() {
   *         driver = new ChromeDriver();
   *         DevTools devTools = ((HasDevTools) driver).getDevTools();
   *         devTools.createSession();
   *
   *         networkListener = new CDriverOTelNetworkListener(
   *             "TestSuite", getClass().getSimpleName()
   *         );
   *         networkListener.attach(devTools);
   *     }
   *
   *     @AfterMethod
   *     public void teardown() {
   *         if (networkListener != null) {
   *             networkListener.detach(); // Ensures trace completion
   *         }
   *         if (driver != null) {
   *             driver.quit();
   *         }
   *     }
   * }
   * }</pre>
   *
   * <p><strong>Conditional Behavior:</strong>
   *
   * <p>Similar to {@link #attach(DevTools)}, this method will only perform cleanup if network
   * metrics collection and OpenTelemetry are both enabled. If either is disabled, this method
   * returns immediately.
   *
   * @see #attach(DevTools)
   * @see CDriverConfigs#isCollectNetworkMetricsEnable()
   * @see COTelConfig#isEnable()
   */
  public void detach() {
    if (!CDriverConfigs.isCollectNetworkMetricsEnable() || !COTelConfig.isEnable()) return;

    parent.end();
  }

  private static void addUriAttributes(Span span, URI uri) {
    if (StringUtils.isNotBlank(uri.getScheme())) span.setAttribute("schema", uri.getScheme());

    if (StringUtils.isNotBlank(uri.getHost())) span.setAttribute("host", uri.getHost());

    if (StringUtils.isNotBlank(uri.getPath())) span.setAttribute("path", uri.getPath());

    if (uri.getPort() > 0) span.setAttribute("port", uri.getPort());
  }
}

package org.catools.web.metrics;

import java.util.Date;
import lombok.Data;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.interfaces.CMap;
import org.catools.web.entities.CWebPageInfo;

/**
 * Represents information about a web page transition, capturing the state before and after an
 * action along with performance metrics and timing information.
 *
 * <p>This class is used to track user actions that result in page transitions or state changes,
 * such as navigation, form submissions, or AJAX requests. It provides a comprehensive view of the
 * transition including page information, performance metrics, and execution timing.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Create page info objects
 * CWebPageInfo beforePage = new CWebPageInfo(driver);
 *
 * // Perform some action (e.g., click a link)
 * driver.$(By.linkText("Next Page")).click();
 *
 * CWebPageInfo afterPage = new CWebPageInfo(driver);
 *
 * // Create transition info
 * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
 *     "NavigateToNextPage",
 *     beforePage,
 *     afterPage
 * );
 *
 * // Access transition information
 * System.out.println("Action: " + transition.getActionName());
 * System.out.println("URL before: " + transition.getUrlBeforeAction());
 * System.out.println("URL after: " + transition.getUrlAfterAction());
 * }</pre>
 *
 * @author CATools Team
 * @since 1.0
 */
@Data
public class CWebPageTransitionInfo {
  /**
   * The name or description of the action that caused the page transition. Examples:
   * "NavigateToHomePage", "SubmitLoginForm", "ClickSearchButton"
   */
  private String actionName;

  /**
   * Information about the page state before the action was performed. Contains the page title and
   * URL before the transition.
   */
  private CWebPageInfo pageBeforeAction;

  /**
   * Information about the page state after the action was performed. Contains the page title and
   * URL after the transition.
   */
  private CWebPageInfo pageAfterAction;

  /**
   * Performance metrics collected during the page transition. Common metrics include loading times,
   * render times, and resource usage.
   */
  private CMap<String, Number> metrics = new CHashMap<>();

  /** The timestamp when the action was performed. */
  private Date actionTime;

  private CWebPageTransitionInfo() {}

  /**
   * Creates a new page transition info with basic information about the action and page states.
   *
   * <p>This constructor is used when you want to track a simple page transition without performance
   * metrics or specific timing information.
   *
   * @param actionName the descriptive name of the action performed
   * @param pageBeforeAction the page information before the action
   * @param pageAfterAction the page information after the action
   * @throws IllegalArgumentException if actionName is null or empty
   * @example
   *     <pre>{@code
   * CWebPageInfo homePage = new CWebPageInfo(driver);
   * driver.$(By.linkText("About")).click();
   * CWebPageInfo aboutPage = new CWebPageInfo(driver);
   *
   * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *     "NavigateToAboutPage",
   *     homePage,
   *     aboutPage
   * );
   *
   * assertEquals("NavigateToAboutPage", transition.getActionName());
   * assertEquals("Home", transition.getTitleBeforeAction());
   * assertEquals("About Us", transition.getTitleAfterAction());
   * }</pre>
   */
  public CWebPageTransitionInfo(
      String actionName, CWebPageInfo pageBeforeAction, CWebPageInfo pageAfterAction) {
    this.actionName = actionName;
    this.pageBeforeAction = pageBeforeAction;
    this.pageAfterAction = pageAfterAction;
  }

  /**
   * Creates a new page transition info with comprehensive information including performance metrics
   * and timing.
   *
   * <p>This constructor is used when you want to capture detailed performance information about a
   * page transition, including metrics like load times, paint times, and other performance
   * indicators collected from the browser's DevTools.
   *
   * @param actionName the descriptive name of the action performed
   * @param pageBeforeAction the page information before the action
   * @param pageAfterAction the page information after the action
   * @param metadata the list of performance metrics collected during the transition
   * @param actionTime the timestamp when the action was performed
   * @throws IllegalArgumentException if actionName is null or empty
   * @throws NullPointerException if metadata is null
   * @example
   *     <pre>{@code
   * // Capture page state before action
   * CWebPageInfo beforePage = new CWebPageInfo(driver);
   * Date startTime = new Date();
   *
   * // Collect performance metrics using DevTools
   * CList<Metric> perfMetrics = new CList<>();
   * // ... collect metrics from DevTools
   *
   * // Perform action
   * driver.$(By.id("submit-button")).click();
   *
   * // Capture page state after action
   * CWebPageInfo afterPage = new CWebPageInfo(driver);
   *
   * // Create comprehensive transition info
   * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *     "SubmitForm",
   *     beforePage,
   *     afterPage,
   *     perfMetrics,
   *     startTime
   * );
   *
   * // Access performance metrics
   * Number loadTime = transition.getMetrics().get("domContentLoadedEventEnd");
   * System.out.println("DOM Content Loaded: " + loadTime + "ms");
   * }</pre>
   */
  public CWebPageTransitionInfo(
      String actionName,
      CWebPageInfo pageBeforeAction,
      CWebPageInfo pageAfterAction,
      CMap<String, String> metadata,
      Date actionTime) {
    this.actionName = actionName;
    this.pageBeforeAction = pageBeforeAction;
    this.pageAfterAction = pageAfterAction;

    metadata.forEach(
        (key, value) -> {
          try {
            metrics.put(key, Double.valueOf(value));
          } catch (NumberFormatException ignored) {
          }
        });

    this.actionTime = actionTime;
  }

  /**
   * Gets the title of the page before the action was performed.
   *
   * <p>This method provides safe access to the page title from the 'before' state, returning null
   * if no page information was captured before the action.
   *
   * @return the page title before the action, or null if pageBeforeAction is null
   * @example
   *     <pre>{@code
   * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *     "LoadHomePage", null, homePage);
   *
   * String beforeTitle = transition.getTitleBeforeAction();
   * // beforeTitle will be null since pageBeforeAction is null
   *
   * String afterTitle = transition.getTitleAfterAction();
   * // afterTitle will contain the actual page title
   * }</pre>
   */
  public String getTitleBeforeAction() {
    return pageBeforeAction == null ? null : pageBeforeAction.getTitle();
  }

  /**
   * Gets the URL of the page before the action was performed.
   *
   * <p>This method provides safe access to the page URL from the 'before' state, returning null if
   * no page information was captured before the action.
   *
   * @return the page URL before the action, or null if pageBeforeAction is null
   * @example
   *     <pre>{@code
   * // Navigate from home page to search results
   * CWebPageInfo homePage = new CWebPageInfo(driver); // URL: "https://example.com"
   * driver.$(By.name("search")).sendKeys("selenium");
   * driver.$(By.id("search-btn")).click();
   * CWebPageInfo resultsPage = new CWebPageInfo(driver); // URL: "https://example.com/search?q=selenium"
   *
   * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *     "SearchQuery", homePage, resultsPage);
   *
   * assertEquals("https://example.com", transition.getUrlBeforeAction());
   * assertEquals("https://example.com/search?q=selenium", transition.getUrlAfterAction());
   * }</pre>
   */
  public String getUrlBeforeAction() {
    return pageBeforeAction == null ? null : pageBeforeAction.getUrl();
  }

  /**
   * Gets the title of the page after the action was performed.
   *
   * <p>This method provides safe access to the page title from the 'after' state, returning null if
   * no page information was captured after the action.
   *
   * @return the page title after the action, or null if pageAfterAction is null
   * @example
   *     <pre>{@code
   * // Login transition example
   * CWebPageInfo loginPage = new CWebPageInfo(driver); // Title: "Login - MyApp"
   * driver.$(By.name("username")).sendKeys("user@example.com");
   * driver.$(By.name("password")).sendKeys("password123");
   * driver.$(By.id("login-btn")).click();
   * CWebPageInfo dashboardPage = new CWebPageInfo(driver); // Title: "Dashboard - MyApp"
   *
   * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *     "UserLogin", loginPage, dashboardPage);
   *
   * assertEquals("Login - MyApp", transition.getTitleBeforeAction());
   * assertEquals("Dashboard - MyApp", transition.getTitleAfterAction());
   * }</pre>
   */
  public String getTitleAfterAction() {
    return pageAfterAction == null ? null : pageAfterAction.getTitle();
  }

  /**
   * Gets the URL of the page after the action was performed.
   *
   * <p>This method provides safe access to the page URL from the 'after' state, returning null if
   * no page information was captured after the action.
   *
   * @return the page URL after the action, or null if pageAfterAction is null
   * @example
   *     <pre>{@code
   * // Shopping cart checkout flow
   * CWebPageInfo cartPage = new CWebPageInfo(driver); // URL: "https://shop.com/cart"
   * driver.$(By.id("checkout-btn")).click();
   * CWebPageInfo checkoutPage = new CWebPageInfo(driver); // URL: "https://shop.com/checkout"
   *
   * CWebPageTransitionInfo transition = new CWebPageTransitionInfo(
   *     "ProceedToCheckout", cartPage, checkoutPage);
   *
   * assertTrue(transition.getUrlBeforeAction().contains("/cart"));
   * assertTrue(transition.getUrlAfterAction().contains("/checkout"));
   *
   * // Verify that navigation occurred
   * assertNotEquals(transition.getUrlBeforeAction(), transition.getUrlAfterAction());
   * }</pre>
   */
  public String getUrlAfterAction() {
    return pageAfterAction == null ? null : pageAfterAction.getUrl();
  }
}

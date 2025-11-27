package org.catools.web.factory;

import org.catools.common.utils.CStringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for finding web elements using XPath expressions with optional timing controls.
 * This annotation can be applied to fields or types to specify how to locate elements on a web page.
 * 
 * <p>Usage examples:</p>
 * <pre>
 * // Basic usage with XPath only
 * &#64;CFindBys(xpath = "//button[@id='submit']")
 * private WebElement submitButton;
 * 
 * // With custom name and timing
 * &#64;CFindBys(
 *     xpath = "//div[@class='content']",
 *     name = "contentArea",
 *     waitForFirstElementInSecond = 10,
 *     waitForOtherElementInSecond = 5
 * )
 * private WebElement contentDiv;
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface CFindBys {
  
  /**
   * Specifies the XPath expression used to locate the web element(s).
   * This is a required parameter that defines how to find the element on the page.
   * 
   * <p>Examples of valid XPath expressions:</p>
   * <ul>
   *   <li>{@code "//button[@id='login']"} - Find button with specific ID</li>
   *   <li>{@code "//div[contains(@class, 'error')]"} - Find div containing error class</li>
   *   <li>{@code "//input[@type='text' and @name='username']"} - Find input with type and name</li>
   *   <li>{@code "//tr[position()>1]/td[2]"} - Find second column of all rows except header</li>
   * </ul>
   * 
   * @return the XPath expression as a String
   */
  String xpath();

  /**
   * Optional name identifier for the element(s) found by this annotation.
   * This can be used for logging, debugging, or referencing purposes.
   * If not specified, defaults to an empty string.
   * 
   * <p>Examples:</p>
   * <pre>
   * // Using a descriptive name
   * &#64;CFindBys(xpath = "//form[@id='login-form']", name = "loginForm")
   * 
   * // Name can help with debugging
   * &#64;CFindBys(xpath = "//table//tr", name = "dataRows")
   * </pre>
   * 
   * @return the name identifier as a String, empty string by default
   */
  String name() default CStringUtil.EMPTY;

  /**
   * Specifies the maximum time in seconds to wait for the first element to appear.
   * This is useful when dealing with dynamic content that may take time to load.
   * A value of -1 (default) means no explicit wait time is set.
   * 
   * <p>Usage scenarios:</p>
   * <ul>
   *   <li>Waiting for AJAX content to load</li>
   *   <li>Waiting for page transitions</li>
   *   <li>Waiting for dynamic elements to appear</li>
   * </ul>
   * 
   * <p>Examples:</p>
   * <pre>
   * // Wait up to 15 seconds for a loading spinner to appear
   * &#64;CFindBys(xpath = "//div[@class='spinner']", waitForFirstElementInSecond = 15)
   * 
   * // Wait for search results after submitting a query
   * &#64;CFindBys(xpath = "//div[@id='results']", waitForFirstElementInSecond = 10)
   * </pre>
   * 
   * @return the wait time in seconds for the first element, -1 by default (no wait)
   */
  int waitForFirstElementInSecond() default -1;

  /**
   * Specifies the maximum time in seconds to wait for additional elements beyond the first one.
   * This is particularly useful when dealing with lists or collections of elements that may
   * load progressively. A value of -1 (default) means no explicit wait time is set.
   * 
   * <p>Common use cases:</p>
   * <ul>
   *   <li>Waiting for all items in a dynamic list to load</li>
   *   <li>Progressive loading of table rows</li>
   *   <li>Incremental content loading (like infinite scroll)</li>
   * </ul>
   * 
   * <p>Examples:</p>
   * <pre>
   * // Wait for additional search results to load after the first one
   * &#64;CFindBys(
   *     xpath = "//div[@class='search-result']",
   *     waitForFirstElementInSecond = 10,
   *     waitForOtherElementInSecond = 5
   * )
   * 
   * // Wait for table rows to populate progressively
   * &#64;CFindBys(
   *     xpath = "//table[@id='data-table']//tr[position()>1]",
   *     waitForFirstElementInSecond = 8,
   *     waitForOtherElementInSecond = 3
   * )
   * </pre>
   * 
   * @return the wait time in seconds for additional elements, -1 by default (no wait)
   */
  int waitForOtherElementInSecond() default -1;
}

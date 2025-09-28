package org.catools.web.metrics;

import lombok.Data;
import lombok.experimental.Accessors;
import org.catools.web.entities.CWebPageInfo;

import java.util.Date;

/**
 * Represents metrics and information about a web action performed during automated testing.
 * This class captures the state of a web page before and after an action, along with timing information.
 * 
 * <p>The class uses Lombok annotations to automatically generate getters, setters, equals, hashCode, 
 * and toString methods. The {@code @Accessors(chain = true)} annotation enables method chaining 
 * for fluent API usage.</p>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <h4>Creating a new metric:</h4>
 * <pre>{@code
 * CWebActionMetric metric = new CWebActionMetric()
 *     .setName("Click Login Button")
 *     .setActionTime(new Date())
 *     .setDuration(250L);
 * }</pre>
 * 
 * <h4>Capturing page state before and after action:</h4>
 * <pre>{@code
 * CWebPageInfo beforePage = new CWebPageInfo().setUrl("https://example.com/login");
 * CWebPageInfo afterPage = new CWebPageInfo().setUrl("https://example.com/dashboard");
 * 
 * CWebActionMetric metric = new CWebActionMetric()
 *     .setName("Login Process")
 *     .setPageBeforeAction(beforePage)
 *     .setPageAfterAction(afterPage)
 *     .setActionTime(new Date())
 *     .setDuration(1200L);
 * }</pre>
 * 
 * <h4>Building a complete action metric:</h4>
 * <pre>{@code
 * Date startTime = new Date();
 * // ... perform web action ...
 * long actionDuration = System.currentTimeMillis() - startTime.getTime();
 * 
 * CWebActionMetric metric = new CWebActionMetric()
 *     .setName("Fill Registration Form")
 *     .setPageBeforeAction(capturedBeforeState)
 *     .setPageAfterAction(capturedAfterState)
 *     .setActionTime(startTime)
 *     .setDuration(actionDuration);
 * 
 * // Access the metrics
 * System.out.println("Action: " + metric.getName());
 * System.out.println("Duration: " + metric.getDuration() + "ms");
 * System.out.println("Before URL: " + metric.getPageBeforeAction().getUrl());
 * System.out.println("After URL: " + metric.getPageAfterAction().getUrl());
 * }</pre>
 * 
 * @author CATools
 * @version 1.0
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class CWebActionMetric {
  
  /**
   * The name or description of the web action that was performed.
   * This field helps identify the specific action for logging and reporting purposes.
   * 
   * <p>Examples: "Click Submit Button", "Fill Username Field", "Navigate to Page"</p>
   */
  private String name;
  
  /**
   * Captures the state of the web page before the action was performed.
   * This includes information such as URL, page title, and other relevant page details.
   * 
   * <p>This field is useful for understanding the initial state and context 
   * before the action execution.</p>
   */
  private CWebPageInfo pageBeforeAction;
  
  /**
   * Captures the state of the web page after the action was performed.
   * This includes information such as URL, page title, and other relevant page details.
   * 
   * <p>This field is useful for verifying the action's effect and the resulting 
   * state of the web page.</p>
   */
  private CWebPageInfo pageAfterAction;
  
  /**
   * The timestamp when the action was performed.
   * This field records the exact moment when the web action was executed.
   * 
   * <p>Useful for creating timelines, debugging timing issues, and performance analysis.</p>
   */
  private Date actionTime;
  
  /**
   * The duration of the action execution in milliseconds.
   * This measures how long the web action took to complete from start to finish.
   * 
   * <p>This metric is crucial for performance monitoring, identifying slow operations,
   * and establishing performance baselines for web automation tests.</p>
   */
  private long duration;
}

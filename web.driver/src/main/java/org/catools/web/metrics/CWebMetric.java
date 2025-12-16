package org.catools.web.metrics;

import lombok.Data;
import org.catools.common.date.CDate;
import org.catools.web.drivers.CDriverEngine;
import org.catools.web.entities.CWebPageInfo;

/**
 * Web metrics collection class that aggregates action, transition and page load metrics
 * during automated web testing sessions.
 *
 * @since 1.0
 * @see CWebActionMetric
 * @see CWebPageTransitionInfo
 * @see CWebPageLoadMetric
 * @see CWebPageInfo
 */
@Data
public class CWebMetric {
  
  /**
   * Collection of individual web action performance metrics.
   */
  private CWebActionMetrics actionPerformances = new CWebActionMetrics();
  
  /**
   * Collection of page transition and navigation performance information.
   */
  private CWebPageTransitionsInfo pagePerformances = new CWebPageTransitionsInfo();
  
  /**
   * Collection of page loading performance metrics.
   */
  private CWebPageLoadMetrics pageLoadMetrics = new CWebPageLoadMetrics();

  /**
   * Records a web action metric with timing and before/after page info.
   *
   * @param name A descriptive name for the action.
   * @param pageBeforeAction The page state before the action.
   * @param pageAfterAction The page state after the action.
   * @param startTime The timestamp when the action was initiated.
   * @since 1.0
   */
  public void addActionMetric(String name, CWebPageInfo pageBeforeAction, CWebPageInfo pageAfterAction, CDate startTime) {
    CWebActionMetric action = new CWebActionMetric()
        .setName(name)
        .setPageBeforeAction(pageBeforeAction)
        .setPageAfterAction(pageAfterAction)
        .setActionTime(startTime)
        .setDuration(startTime.getDurationToNow().getNano());
    actionPerformances.add(action);
  }

  /**
   * Adds a page transition performance record.
   *
   * @param pageTransitionInfo Information about the page transition.
   * @since 1.0
   * @see CWebPageTransitionInfo
   */
  public void addPagePerformance(CWebPageTransitionInfo pageTransitionInfo) {
    pagePerformances.add(pageTransitionInfo);
  }

  /**
   * Records a page load performance metric using the current engine state.
   *
   * @param engine The engine instance to extract page information from.
   * @param startTime The timestamp when the page load operation was initiated.
   * @since 1.0
   * @see CWebPageLoadMetric
   */
  public void addPageLoadMetric(CDriverEngine engine, CDate startTime) {
    CWebPageLoadMetric pageLoad = new CWebPageLoadMetric()
        .setName("Page Load")
        .setTitle(engine.title())
        .setUrl(engine.url())
        .setActionTime(startTime)
        .setDuration(startTime.getDurationToNow().getNano());
    pageLoadMetrics.add(pageLoad);
  }
}

package org.catools.web.drivers;

import org.catools.common.collections.CList;
import org.catools.common.date.CDate;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.config.CGridConfigs;
import org.catools.web.entities.CWebPageInfo;
import org.catools.web.metrics.CWebPageTransitionInfo;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v138.log.Log;
import org.openqa.selenium.devtools.v138.performance.Performance;
import org.openqa.selenium.devtools.v138.performance.model.Metric;

import javax.ws.rs.NotSupportedException;
import java.util.Optional;

public class CDevTools {

  private DevTools devTools;

  public void startRecording(CDriverProvider driverProvider, HasDevTools webDriver) {
    if (!isEnable()) return;

    init(driverProvider, webDriver);
    devTools.createSession();
    devTools.send(Log.disable());
    devTools.send(Performance.enable(Optional.empty()));
  }

  public synchronized CWebPageTransitionInfo stopRecording(CWebPageInfo previousPage, CWebPageInfo currentPage) {
    if (!isEnable())
      return new CWebPageTransitionInfo("Page Transition", previousPage, currentPage);

    try {
      CList<Metric> metricList = readPerformanceMetrics();
      return new CWebPageTransitionInfo("Page Transition", previousPage, currentPage, metricList, CDate.now());
    } catch (DevToolsException exception) {
      return null;
    }
  }

  private CList<Metric> readPerformanceMetrics() {
    if (devTools == null)
      return CList.of();

    try {
      return new CList<>(devTools.send(Performance.getMetrics()));
    } finally {
      devTools.disconnectSession();
    }
  }

  private void init(CDriverProvider driverProvider, HasDevTools webDriver) {
    if (devTools != null) {
      return;
    }

    if (driverProvider.getBrowser().isFirefox()) {
      throw new NotSupportedException("DevTools doesn't support by Firefox");
    }

    devTools = webDriver.getDevTools();
  }

  private boolean isEnable() {
    return CDriverConfigs.isCollectPerformanceMetricsEnable() && !CGridConfigs.isUseRemoteDriver() && !CGridConfigs.isUseTestContainer();
  }
}

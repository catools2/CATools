package org.catools.web.drivers;

import lombok.extern.slf4j.Slf4j;
import org.catools.common.collections.CList;
import org.catools.common.date.CDate;
import org.catools.common.tests.CTest;
import org.catools.common.utils.CStringUtil;
import org.catools.web.config.CDriverConfigs;
import org.catools.web.config.CGridConfigs;
import org.catools.web.entities.CWebPageInfo;
import org.catools.web.listeners.CDriverOTelNetworkListener;
import org.catools.web.metrics.CWebPageTransitionInfo;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.DevToolsProvider;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v138.log.Log;
import org.openqa.selenium.devtools.v138.network.Network;
import org.openqa.selenium.devtools.v138.performance.Performance;
import org.openqa.selenium.devtools.v138.performance.model.Metric;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Optional;

@Slf4j
public class CDevTools {

  private DevTools devTools;
  private CDriverOTelNetworkListener listener;

  public void startRecording(CTest testInstance, String actionName, RemoteWebDriver webDriver) {
    init(webDriver);

    if (canNotRecord(webDriver)) return;

    devTools.createSession();
    devTools.send(Log.disable());

    if (CDriverConfigs.isCollectNetworkMetricsEnable())
      devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
    else
      devTools.send(Network.disable());

    if (CDriverConfigs.isCollectPerformanceMetricsEnable())
      devTools.send(Performance.enable(Optional.empty()));
    else
      devTools.send(Performance.disable());

    String title = webDriver.getTitle();
    String tracerName = CStringUtil.isBlank(title) ? actionName : "%s on \"%s\"".formatted(actionName, title);
    listener = new CDriverOTelNetworkListener(testInstance.getName(), tracerName);
    listener.attach(devTools);
  }

  public synchronized CWebPageTransitionInfo stopRecording(CWebPageInfo previousPage, CWebPageInfo currentPage) {
    try {
      CList<Metric> metricList = readPerformanceMetrics();
      listener.detach();

      return new CWebPageTransitionInfo("Page Transition", previousPage, currentPage, metricList, CDate.now());
    } catch (DevToolsException exception) {
      return new CWebPageTransitionInfo("Page Transition", previousPage, currentPage);
    }
  }

  private CList<Metric> readPerformanceMetrics() {
    if (devTools == null || !CDriverConfigs.isCollectPerformanceMetricsEnable())
      return CList.of();

    try {
      return new CList<>(devTools.send(Performance.getMetrics()));
    } finally {
      devTools.disconnectSession();
    }
  }

  private void init(RemoteWebDriver webDriver) {
    if (devTools != null) {
      return;
    }

    try {
      if (webDriver instanceof HasDevTools hasDevTools) {
        devTools = hasDevTools.getDevTools();
      } else if (webDriver.getCapabilities().asMap().containsKey("se:cdp")) {
        DevToolsProvider devToolsProvider = new DevToolsProvider();
        ChromeOptions other = new ChromeOptions();
        other.setCapability("se:cdp", CGridConfigs.getCdpURL(webDriver.getSessionId().toString()));
        Capabilities merged = webDriver.getCapabilities().merge(other);
        devTools = devToolsProvider.getImplementation(merged, new RemoteExecuteMethod(webDriver)).getDevTools();
      } else {
        throw new UnsupportedOperationException("DevTools support only with chromedriver or Grid with CDP enabled feature");
      }
    } catch (Exception ex) {
      log.warn("Failed to init DevTools", ex);
    }
  }

  private boolean canNotRecord(RemoteWebDriver webDriver) {
    return !(devTools != null && (webDriver instanceof HasDevTools || webDriver.getCapabilities().asMap().containsKey("se:cdp")));
  }
}

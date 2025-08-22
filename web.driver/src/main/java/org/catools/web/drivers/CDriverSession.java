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
 * Driver close the alert when it opens and we try to get Title or URL. So to avoid any impact on
 * flow we do not perform any session update and we do not call listeners
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

  public CDriverSession(CTest testInstance, CDriverProvider driverProvider) {
    this(testInstance, driverProvider, null);
  }

  public CDriverSession(CTest testInstance, CDriverProvider driverProvider, BiPredicate<CDriverSession, WebDriver> pageTransitionIndicator) {
    this.testInstance = testInstance;
    this.driverProvider = driverProvider;
    this.pageTransitionIndicator = pageTransitionIndicator;
    addProxyIfEnabled();
  }

  public void startSession() {
    setWebDriver(driverProvider.build(listeners));
    CWebDriverUtil.setDriverWindowsSize(webDriver, windowsPosition, windowsSize);
  }

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

  public boolean isActive() {
    return webDriver != null && webDriver.getSessionId() != null;
  }

  public void reset() {
    this.previousPage = this.currentPage = BLANK_PAGE;
    this.webDriver = null;
  }

  public void addListeners(CDriverListener... listeners) {
    this.listeners.addAll(CList.of(listeners).getAll(Objects::nonNull));
  }

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

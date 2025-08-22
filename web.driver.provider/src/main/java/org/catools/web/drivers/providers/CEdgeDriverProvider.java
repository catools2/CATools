package org.catools.web.drivers.providers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.catools.web.config.CGridConfigs;
import org.catools.web.drivers.CDriverProvider;
import org.catools.web.drivers.config.CEdgeConfigs;
import org.catools.web.drivers.config.CWebDriverManagerConfigs;
import org.catools.web.enums.CBrowser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.devtools.Connection;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.Objects;
import java.util.logging.Level;

import static org.catools.web.config.CGridConfigs.getHubURL;

public class CEdgeDriverProvider implements CDriverProvider {

  static {
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(CGridConfigs.getLogLevel());
    java.util.logging.Logger.getLogger(Connection.class.getPackage().getName()).setLevel(Level.SEVERE);
    System.setProperty("webdriver.http.factory", "jdk-http-client");
    if (CWebDriverManagerConfigs.isEnabled()) {
      WebDriverManager.edgedriver().setup();
    }
  }

  private EdgeOptions options = new EdgeOptions();

  public CEdgeDriverProvider() {
    if (StringUtils.isNotBlank(CEdgeConfigs.getBinaryPath())) {
      setBinary(CEdgeConfigs.getBinaryPath());
    }
    addArguments(CEdgeConfigs.getDefaultArguments());
    setPageLoadStrategy(CEdgeConfigs.getPageLoadStrategy());

    if (CEdgeConfigs.isInHeadLessMode()) {
      addArguments(CEdgeConfigs.getHeadLessArguments());
    }
  }

  @Override
  public Capabilities getCapabilities() {
    options.setEnableDownloads(true);
    return options;
  }

  @Override
  public RemoteWebDriver buildTestContainer() {
    BrowserWebDriverContainer<?> driverContainer = new BrowserWebDriverContainer<>("selenium/standalone-edge:latest");
    driverContainer.withCapabilities(getCapabilities());
    driverContainer.start();
    return new RemoteWebDriver(driverContainer.getSeleniumAddress(), getCapabilities());
  }

  @Override
  public RemoteWebDriver buildLocalDriver() {
    getCapabilities();
    return new EdgeDriver(options);
  }

  @Override
  public RemoteWebDriver buildRemoteWebDrier() {
    return new RemoteWebDriver(Objects.requireNonNull(getHubURL()), getCapabilities());
  }

  @Override
  public CBrowser getBrowser() {
    return CBrowser.EDGE;
  }

  public CEdgeDriverProvider setPageLoadStrategy(PageLoadStrategy pageLoadStrategy) {
    options.setPageLoadStrategy(pageLoadStrategy);
    return this;
  }

  public CEdgeDriverProvider addArguments(Iterable<String> args) {
    for (String arg : args) {
      options.addArguments(arg);
    }
    return this;
  }

  public CEdgeDriverProvider setBinary(String path) {
    options.setBinary(path);
    return this;
  }
}

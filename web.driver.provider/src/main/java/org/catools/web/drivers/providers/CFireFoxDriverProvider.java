package org.catools.web.drivers.providers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.catools.web.config.CGridConfigs;
import org.catools.web.drivers.CDriverProvider;
import org.catools.web.drivers.config.CFireFoxConfigs;
import org.catools.web.drivers.config.CWebDriverManagerConfigs;
import org.catools.web.enums.CBrowser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.devtools.Connection;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.Objects;
import java.util.logging.Level;

import static org.catools.web.config.CGridConfigs.getHubURL;

public class CFireFoxDriverProvider implements CDriverProvider {
  static {
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(CGridConfigs.getLogLevel());
    java.util.logging.Logger.getLogger(Connection.class.getPackage().getName()).setLevel(Level.SEVERE);
    if (CWebDriverManagerConfigs.isEnabled()) {
      WebDriverManager.firefoxdriver().setup();
    }
  }

  private FirefoxOptions options = new FirefoxOptions();
  private FirefoxProfile profile = getFirefoxProfile(options);

  public CFireFoxDriverProvider() {
    if (StringUtils.isNotBlank(CFireFoxConfigs.getBinaryPath())) {
      setBinary(CFireFoxConfigs.getBinaryPath());
    }
    addArguments(CFireFoxConfigs.getDefaultArguments());
    setPageLoadStrategy(CFireFoxConfigs.getPageLoadStrategy());
    setOpenPdfInNewTab(true);

    if (CFireFoxConfigs.isInHeadLessMode()) {
      addArguments(CFireFoxConfigs.getHeadLessArguments());
    }
  }

  @Override
  public Capabilities getCapabilities() {
    options.setEnableDownloads(true);
    return options;
  }

  @Override
  public RemoteWebDriver buildTestContainer() {
    BrowserWebDriverContainer<?> driverContainer = new BrowserWebDriverContainer<>("selenium/standalone-firefox:latest");
    driverContainer.withCapabilities(getCapabilities());
    driverContainer.start();
    return new RemoteWebDriver(driverContainer.getSeleniumAddress(), getCapabilities());
  }

  @Override
  public RemoteWebDriver buildLocalDriver() {
    getCapabilities();
    return new FirefoxDriver(options.setProfile(profile));
  }

  @Override
  public RemoteWebDriver buildRemoteWebDrier() {
    return new RemoteWebDriver(Objects.requireNonNull(getHubURL()), getCapabilities());
  }

  @Override
  public CBrowser getBrowser() {
    return CBrowser.FIREFOX;
  }

  public CFireFoxDriverProvider setBinary(String path) {
    options.setBinary(path);
    return this;
  }

  public CFireFoxDriverProvider addArguments(Iterable<String> args) {
    for (String arg : args) {
      options.addArguments(arg);
    }
    return this;
  }

  public CFireFoxDriverProvider setOpenPdfInNewTab(boolean value) {
    profile.setPreference("pdfjs.disabled", value);
    return this;
  }

  public CFireFoxDriverProvider setPageLoadStrategy(PageLoadStrategy pageLoadStrategy) {
    options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, pageLoadStrategy.toString());
    return this;
  }

  private FirefoxProfile getFirefoxProfile(FirefoxOptions options) {
    FirefoxProfile profile = new FirefoxProfile();
    profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
    profile.setPreference("startup.homepage_welcome_url.additional", "about:blank");

    options.setProfile(profile);

    profile.setAcceptUntrustedCertificates(true);
    profile.setAlwaysLoadNoFocusLib(true);

    options.setLogLevel(FirefoxDriverLogLevel.FATAL);
    return profile;
  }
}

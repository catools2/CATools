package org.catools.web.drivers.providers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.catools.common.configs.CPathConfigs;
import org.catools.common.io.CFile;
import org.catools.common.utils.CStringUtil;
import org.catools.web.config.CGridConfigs;
import org.catools.web.drivers.CDriverProvider;
import org.catools.web.drivers.config.CFireFoxConfigs;
import org.catools.web.drivers.config.CWebDriverManagerConfigs;
import org.catools.web.enums.CBrowser;
import org.catools.web.listeners.CDriverListener;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.List;

import static org.catools.web.config.CGridConfigs.getHubURL;
import static org.catools.web.config.CGridConfigs.isUseRemoteDriver;

public class CFireFoxDriverProvider implements CDriverProvider {
  private static final String GECKO_DRIVER_PATH_ENV = "webdriver.gecko.driver";
  private FirefoxOptions options = new FirefoxOptions();
  private FirefoxProfile profile = getFirefoxProfile(options);

  static {
    if (CWebDriverManagerConfigs.isEnabled()) {
      WebDriverManager.firefoxdriver().setup();
    }
  }

  public CFireFoxDriverProvider() {
    if (CStringUtil.isNotBlank(CFireFoxConfigs.getBinaryPath())) {
      setBinary(CFireFoxConfigs.getBinaryPath());
    }
    addArguments(CFireFoxConfigs.getDefaultArguments());
    setDownloadFolder(CPathConfigs.getTmpDownloadFolder());
    setPageLoadStrategy(CFireFoxConfigs.getPageLoadStrategy());
    setOpenPdfInNewTab(true);

    if (CFireFoxConfigs.isInHeadLessMode()) {
      addArguments(CFireFoxConfigs.getHeadLessArguments());
    }
  }

  @Override
  public RemoteWebDriver build(List<CDriverListener> listeners) {
    if (listeners != null) {
      listeners.forEach(b -> b.beforeInit(options));
    }

    RemoteWebDriver webDriver =
        isUseRemoteDriver()
            ? new RemoteWebDriver(getHubURL(), options)
            : new FirefoxDriver(options.setProfile(profile));

    if (listeners != null) {
      listeners.forEach(b -> b.afterInit(webDriver));
    }
    return webDriver;
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

  public CFireFoxDriverProvider setHeadless(boolean value) {
    options.setHeadless(value);
    return this;
  }

  public CFireFoxDriverProvider setOpenPdfInNewTab(boolean value) {
    profile.setPreference("pdfjs.disabled", value);
    return this;
  }

  public CFireFoxDriverProvider setDownloadFolder(File tempDownloadFolder) {
    profile.setPreference("browser.download.dir", CFile.of(tempDownloadFolder).getCanonicalPath());
    profile.setPreference("browser.download.manager.showWhenStarting", false);
    profile.setPreference("browser.download.folderList", 2);
    profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream");
    return this;
  }

  public CFireFoxDriverProvider setPageLoadStrategy(PageLoadStrategy pageLoadStrategy) {
    options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, pageLoadStrategy.toString());
    return this;
  }

  private FirefoxProfile getFirefoxProfile(FirefoxOptions options) {
    setSystemProperties();

    FirefoxProfile profile = new FirefoxProfile();
    profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
    profile.setPreference("startup.homepage_welcome_url.additional", "about:blank");

    profile.setAcceptUntrustedCertificates(true);
    profile.setAlwaysLoadNoFocusLib(true);

    options.setCapability(FirefoxDriver.SystemProperty.BROWSER_PROFILE, profile);
    options.setCapability("browser.tabs.remote.autostart", "true");
    options.setCapability("browser.tabs.remote.autostart.2", "true");
    options.setCapability("webgl.force-enabled", "true");
    options.setCapability("marionette", true);

    options.setLogLevel(FirefoxDriverLogLevel.FATAL);
    return profile;
  }

  private void setSystemProperties() {
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(CGridConfigs.getLogLevel());
    System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
    if (!CWebDriverManagerConfigs.isEnabled()
        && CStringUtil.isNotBlank(CFireFoxConfigs.getDriverPath())) {
      System.setProperty(GECKO_DRIVER_PATH_ENV, CFireFoxConfigs.getDriverPath());
    }
  }
}

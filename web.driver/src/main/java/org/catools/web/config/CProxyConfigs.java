package org.catools.web.config;

import com.microsoft.playwright.options.Proxy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Configuration utility class for managing web driver proxy settings.
 * 
 * <p>This class provides methods to check if proxy is enabled and to create
 * a configured Selenium Proxy object based on configuration values stored
 * in HOCON format.</p>
 * 
 * <p>The proxy configuration supports various proxy types including HTTP, HTTPS,
 * FTP, SOCKS proxies, and proxy auto-configuration URLs.</p>
 * 
 * @author CATools
 * @since 1.0
 */
@Slf4j
public class CProxyConfigs {

  /**
   * Checks if proxy configuration is enabled.
   * 
   * <p>This method reads the proxy enabled flag from the configuration
   * and returns true if proxy should be used, false otherwise.</p>
   * 
   * @return {@code true} if proxy is enabled in configuration, {@code false} otherwise
   * 
   * @example
   * <pre>{@code
   * // Check if proxy is enabled before configuring Page
   * if (CProxyConfigs.isEnabled()) {
   *     Page driver = new ChromeDriver(options.setProxy(CProxyConfigs.getProxy()));
   * } else {
   *     Page driver = new ChromeDriver();
   * }
   * }</pre>
   * 
   * @example Configuration example in application.conf:
   * <pre>
   * catools.web.proxy.enabled = true
   * </pre>
   */
  public static boolean isEnabled() {
    return CHocon.get(Configs.CATOOLS_WEB_PROXY_ENABLED).asBoolean(false);
  }

  /**
   * Creates and configures a Selenium Proxy object based on configuration settings.
   * 
   * <p>This method reads various proxy configuration parameters from HOCON configuration
   * and creates a fully configured Selenium Proxy object. It supports multiple proxy
   * types including HTTP, HTTPS, FTP, SOCKS proxies, and proxy auto-configuration.</p>
   * 
   * <p>The method only sets proxy parameters that are explicitly defined in the
   * configuration. Undefined parameters are left with their default values.</p>
   * 
   * @return a configured {@link Proxy} object ready to be used with Page
   * 
   * @example Basic HTTP proxy configuration:
   * <pre>{@code
   * // Get configured proxy and use with ChromeDriver
   * Proxy proxy = CProxyConfigs.getProxy();
   * ChromeOptions options = new ChromeOptions();
   * options.setProxy(proxy);
   * Page driver = new ChromeDriver(options);
   * }</pre>
   * 
   * @example Configuration example in application.conf:
   * <pre>
   * catools.web.proxy {
   *   enabled = true
   *   proxy_type = "MANUAL"
   *   auto_detect = false
   *   http_proxy = "proxy.company.com:8080"
   *   ssl_proxy = "proxy.company.com:8080"
   *   no_proxy = "localhost,127.0.0.1,*.local"
   * }
   * </pre>
   * 
   * @example SOCKS proxy configuration:
   * <pre>
   * catools.web.proxy {
   *   enabled = true
   *   socks_proxy = "socks.proxy.com:1080"
   *   socks_version = 5
   *   socks_username = "user"
   *   socks_password = "password"
   * }
   * </pre>
   * 
   * @example Proxy auto-configuration:
   * <pre>
   * catools.web.proxy {
   *   enabled = true
   *   proxy_type = "PAC"
   *   proxy_autoconfig_url = "http://proxy.company.com/proxy.pac"
   * }
   * </pre>
   * 
   * @see org.openqa.selenium.Proxy
   * @see org.openqa.selenium.Proxy.ProxyType
   */
  public static Proxy getProxy() {
    Proxy proxy = null;
    if (definedAsString(Configs.CATOOLS_WEB_PROXY_HTTP_PROXY)) {
      proxy = new Proxy(CHocon.asString(Configs.CATOOLS_WEB_PROXY_HTTP_PROXY));
    } else if (definedAsString(Configs.CATOOLS_WEB_PROXY_SOCKS_PROXY)) {
      proxy = new Proxy(CHocon.asString(Configs.CATOOLS_WEB_PROXY_SOCKS_PROXY));
    }

    // Playwright Proxy supports server, bypass list, username/password via the Proxy builder
    if (proxy != null) {
      if (definedAsString(Configs.CATOOLS_WEB_PROXY_NO_PROXY)) {
        proxy.setBypass(CHocon.asString(Configs.CATOOLS_WEB_PROXY_NO_PROXY));
      }
      if (definedAsString(Configs.CATOOLS_WEB_PROXY_SOCKS_USERNAME) && definedAsString(Configs.CATOOLS_WEB_PROXY_SOCKS_PASSWORD)) {
        proxy.setUsername(CHocon.asString(Configs.CATOOLS_WEB_PROXY_SOCKS_USERNAME));
        proxy.setPassword(CHocon.asString(Configs.CATOOLS_WEB_PROXY_SOCKS_PASSWORD));
      }
    }

    return proxy;
  }

  private static boolean definedAsString(Configs config) {
    return CHocon.has(config) && StringUtils.isNotBlank(CHocon.get(config).asString(null));
  }

  private static boolean definedAsInteger(Configs config) {
    return CHocon.has(config) && CHocon.get(config).asInteger(null) != null;
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WEB_PROXY_ENABLED("catools.web.proxy.enabled"),
    CATOOLS_WEB_PROXY_PROXY_TYPE("catools.web.proxy.proxy_type"),
    CATOOLS_WEB_PROXY_AUTO_DETECT("catools.web.proxy.auto_detect"),
    CATOOLS_WEB_PROXY_FTP_PROXY("catools.web.proxy.ftp_proxy"),
    CATOOLS_WEB_PROXY_HTTP_PROXY("catools.web.proxy.http_proxy"),
    CATOOLS_WEB_PROXY_NO_PROXY("catools.web.proxy.no_proxy"),
    CATOOLS_WEB_PROXY_SSL_PROXY("catools.web.proxy.ssl_proxy"),
    CATOOLS_WEB_PROXY_SOCKS_PROXY("catools.web.proxy.socks_proxy"),
    CATOOLS_WEB_PROXY_SOCKS_VERSION("catools.web.proxy.socks_version"),
    CATOOLS_WEB_PROXY_SOCKS_USERNAME("catools.web.proxy.socks_username"),
    CATOOLS_WEB_PROXY_SOCKS_PASSWORD("catools.web.proxy.socks_password"),
    CATOOLS_WEB_PROXY_PROXY_AUTOCONFIG_URL("catools.web.proxy.proxy_autoconfig_url");

    private final String path;
  }
}

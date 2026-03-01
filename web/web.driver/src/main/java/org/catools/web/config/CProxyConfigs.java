package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

/**
 * Configuration utility class for managing web driver proxy settings.
 *
 * <p>This class provides methods to check if proxy is enabled and to create a configured Selenium
 * Proxy object based on configuration values stored in HOCON format.
 *
 * <p>The proxy configuration supports various proxy types including HTTP, HTTPS, FTP, SOCKS
 * proxies, and proxy auto-configuration URLs.
 *
 * @author CATools
 * @since 1.0
 */
@Slf4j
public class CProxyConfigs {

  /**
   * Checks if proxy configuration is enabled.
   *
   * <p>This method reads the proxy enabled flag from the configuration and returns true if proxy
   * should be used, false otherwise.
   *
   * @return {@code true} if proxy is enabled in configuration, {@code false} otherwise
   * @example
   *     <pre>{@code
   * // Check if proxy is enabled before configuring Page
   * if (CProxyConfigs.isEnabled()) {
   *     Page driver = new ChromeDriver(options.setProxy(CProxyConfigs.getProxy()));
   * } else {
   *     Page driver = new ChromeDriver();
   * }
   * }</pre>
   *
   * @example Configuration example in application.conf:
   *     <pre>
   * catools.web.proxy.enabled = true
   * </pre>
   */
  public static boolean isEnabled() {
    return CHocon.get(Configs.CATOOLS_WEB_PROXY_ENABLED).asBoolean(false);
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

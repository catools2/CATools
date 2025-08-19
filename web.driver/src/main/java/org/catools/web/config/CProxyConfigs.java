package org.catools.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;
import org.openqa.selenium.Proxy;

@Slf4j
public class CProxyConfigs {

  public static boolean isEnabled() {
    return CHocon.get(Configs.CATOOLS_WEB_PROXY_ENABLED).asBoolean(false);
  }

  public static Proxy getProxy() {
    Proxy proxy = new Proxy();

    proxy.setAutodetect(CHocon.get(Configs.CATOOLS_WEB_PROXY_AUTO_DETECT).asBoolean(false));
    proxy.setProxyType(CHocon.get(Configs.CATOOLS_WEB_PROXY_PROXY_TYPE).asEnum(Proxy.ProxyType.class, Proxy.ProxyType.MANUAL));

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_FTP_PROXY)) {
      proxy.setFtpProxy(CHocon.get(Configs.CATOOLS_WEB_PROXY_FTP_PROXY).asString(null));
    }

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_HTTP_PROXY)) {
      proxy.setHttpProxy(CHocon.get(Configs.CATOOLS_WEB_PROXY_HTTP_PROXY).asString(null));
    }

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_NO_PROXY)) {
      proxy.setNoProxy(CHocon.get(Configs.CATOOLS_WEB_PROXY_NO_PROXY).asString(null));
    }

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_SSL_PROXY)) {
      proxy.setSslProxy(CHocon.get(Configs.CATOOLS_WEB_PROXY_SSL_PROXY).asString(null));
    }

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_SOCKS_PROXY)) {
      proxy.setSocksProxy(CHocon.get(Configs.CATOOLS_WEB_PROXY_SOCKS_PROXY).asString(null));
    }

    if (definedAsInteger(Configs.CATOOLS_WEB_PROXY_SOCKS_VERSION)) {
      proxy.setSocksVersion(CHocon.get(Configs.CATOOLS_WEB_PROXY_SOCKS_VERSION).asInteger(null));
    }

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_SOCKS_USERNAME)) {
      proxy.setSocksUsername(CHocon.get(Configs.CATOOLS_WEB_PROXY_SOCKS_USERNAME).asString(null));
    }

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_SOCKS_PASSWORD)) {
      proxy.setSocksPassword(CHocon.get(Configs.CATOOLS_WEB_PROXY_SOCKS_PASSWORD).asString(null));
    }

    if (definedAsString(Configs.CATOOLS_WEB_PROXY_PROXY_AUTOCONFIG_URL)) {
      proxy.setProxyAutoconfigUrl(CHocon.get(Configs.CATOOLS_WEB_PROXY_PROXY_AUTOCONFIG_URL).asString(null));
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

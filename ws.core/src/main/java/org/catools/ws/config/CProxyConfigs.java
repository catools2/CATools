package org.catools.ws.config;

import io.restassured.specification.ProxySpecification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.hocon.CHocon;
import org.catools.common.hocon.model.CHoconPath;

@Slf4j
public class CProxyConfigs {

  public static boolean isEnabled() {
    return CHocon.get(Configs.CATOOLS_WS_PROXY_ENABLED).asBoolean(false);
  }

  public static ProxySpecification getProxy() {

    ProxySpecification proxySpecification =
        new ProxySpecification(getHost(), getPort(), getSchema());

    if (definedAsString(Configs.CATOOLS_WS_PROXY_USERNAME)
        && definedAsString(Configs.CATOOLS_WS_PROXY_PASSWORD)) {
      return proxySpecification.withAuth(getUsername(), getPassword());
    }

    return proxySpecification;
  }

  private static String getPassword() {
    return CHocon.get(Configs.CATOOLS_WS_PROXY_PASSWORD).asString();
  }

  private static String getUsername() {
    return CHocon.get(Configs.CATOOLS_WS_PROXY_USERNAME).asString();
  }

  private static Integer getPort() {
    return CHocon.get(Configs.CATOOLS_WS_PROXY_PORT).asInteger();
  }

  private static String getSchema() {
    return CHocon.get(Configs.CATOOLS_WS_PROXY_SCHEMA).asString();
  }

  private static String getHost() {
    return CHocon.get(Configs.CATOOLS_WS_PROXY_HOST).asString();
  }

  private static boolean definedAsString(Configs config) {
    return CHocon.has(config) && StringUtils.isNotBlank(CHocon.get(config).asString(null));
  }

  @Getter
  @AllArgsConstructor
  private enum Configs implements CHoconPath {
    CATOOLS_WS_PROXY_ENABLED("catools.ws.proxy.enabled"),
    CATOOLS_WS_PROXY_HOST("catools.ws.proxy.host"),
    CATOOLS_WS_PROXY_PORT("catools.ws.proxy.port"),
    CATOOLS_WS_PROXY_SCHEMA("catools.ws.proxy.schema"),
    CATOOLS_WS_PROXY_USERNAME("catools.ws.proxy.username"),
    CATOOLS_WS_PROXY_PASSWORD("catools.ws.proxy.password");

    private final String path;
  }
}

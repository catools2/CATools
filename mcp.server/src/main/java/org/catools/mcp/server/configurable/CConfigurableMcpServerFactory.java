package org.catools.mcp.server.configurable;

import org.catools.mcp.configuration.CMcpServerConfiguration;

/**
 * This factory class is used to create instances of {@link CConfigurableMcpServer}
 * implementations based on the specified {@link CMcpServerConfiguration}.
 */
public final class CConfigurableMcpServerFactory {

  /**
   * Factory class should not be instantiated
   */
  private CConfigurableMcpServerFactory() {
    throw new UnsupportedOperationException("Factory class should not be instantiated");
  }

  /**
   * Creates a new instance of {@link CConfigurableMcpServer} implementations based on the
   * specified {@link CMcpServerConfiguration}.
   *
   * @param config the configuration to use for the server
   * @return a new instance of {@link CConfigurableMcpServer}
   */
  public static CConfigurableMcpServer getServer(CMcpServerConfiguration config) {
    return switch (config.mode()) {
      case STDIO -> new CConfigurableMcpStdioServer(config);
      case SSE -> new CConfigurableMcpSseServer(config);
      case STREAMABLE -> new CConfigurableMcpStreamableServer(config);
    };
  }
}

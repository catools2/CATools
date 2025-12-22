package org.catools.mcp;

import com.google.inject.Guice;
import com.google.inject.Module;
import io.modelcontextprotocol.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.configuration.CMcpServerConfiguration;
import org.catools.mcp.configuration.CYamlConfigurationLoader;
import org.catools.mcp.di.CDependencyInjector;
import org.catools.mcp.di.CDependencyInjectorProvider;
import org.catools.mcp.di.CGuiceInjectorModule;
import org.catools.mcp.server.CBaseMcpSseServer;
import org.catools.mcp.server.CBaseMcpStdioServer;
import org.catools.mcp.server.CBaseMcpStreamableServer;
import org.catools.mcp.server.CMcpServerInfo;
import org.catools.mcp.server.CMcpSseServerInfo;
import org.catools.mcp.server.CMcpStreamableServerInfo;
import org.catools.mcp.server.configurable.CConfigurableMcpServerFactory;

/**
 * This class is a singleton that provides methods to start MCP servers.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * McpServerInfo serverInfo = McpServerInfo.builder().build();
 * McpServers.run(MyApplication.class, args).startStdioServer(serverInfo);
 *
 * McpSseServerInfo sseServerInfo = McpSseServerInfo.builder().build();
 * McpServers.run(MyApplication.class, args).startSseServer(sseServerInfo);
 *
 * McpStreamableServerInfo streamableServerInfo = McpStreamableServerInfo.builder().build();
 * McpServers.run(MyApplication.class, args).startStreamableServer(streamableServerInfo);
 *
 * McpServers.run(MyApplication.class, args).startServer("mcp-server-config.yml");
 *
 * McpServers.run(MyApplication.class, args).startServer();
 * }</pre>
 */
@Slf4j
public final class CMcpServers {

  /** The singleton instance of McpServers. */
  private static final CMcpServers INSTANCE = new CMcpServers();

  /** The dependency injector used to inject server components. */
  private static CDependencyInjector injector;

  /**
   * The constructor of McpServers. Using singleton design pattern should have private constructor.
   */
  private CMcpServers() {}

  /**
   * Initializes the McpServers instance with the specified application main class and arguments.
   *
   * @param applicationMainClass the main class of the application
   * @return the singleton instance of McpServers
   */
  public static CMcpServers run(Class<?> applicationMainClass) {
    CGuiceInjectorModule module = new CGuiceInjectorModule(applicationMainClass);
    return run(module);
  }

  /**
   * Initializes the McpServers instance with the specified application main class and arguments.
   *
   * @param modules the Guice modules to use for dependency injection
   * @return the singleton instance of McpServers
   */
  public static CMcpServers run(Module... modules) {
    CDependencyInjector injector = new CDependencyInjector(Guice.createInjector(modules));
    CDependencyInjectorProvider.INSTANCE.initialize(injector);
    CMcpServers.injector = injector;
    return INSTANCE;
  }

  /**
   * Starts a standard input/output (stdio) server with the specified server info.
   *
   * @param serverInfo the server info for the stdio server
   */
  public void startStdioServer(CMcpServerInfo serverInfo) {
    injector.getInstance(CBaseMcpStdioServer.class).start(serverInfo);
  }

  /**
   * Starts a http server-sent events (sse) server with the specified server info.
   *
   * @param serverInfo the server info for the sse server
   */
  public void startSseServer(CMcpSseServerInfo serverInfo) {
    injector.getInstance(CBaseMcpSseServer.class).start(serverInfo);
  }

  /**
   * Starts a streamable http server with the specified server info.
   *
   * @param serverInfo the server info for the streamable server
   */
  public void startStreamableServer(CMcpStreamableServerInfo serverInfo) {
    injector.getInstance(CBaseMcpStreamableServer.class).start(serverInfo);
  }

  /**
   * Starts a server with the specified configuration file name.
   *
   * @param configFileName the name of the configuration file
   */
  public void startServer(String configFileName) {
    Assert.notNull(configFileName, "configFileName must not be null");
    CYamlConfigurationLoader configLoader = new CYamlConfigurationLoader(configFileName);
    doStartServer(configLoader.loadConfig());
  }

  /** Starts a server with the default configuration file name. */
  public void startServer() {
    CYamlConfigurationLoader configLoader = new CYamlConfigurationLoader();
    doStartServer(configLoader.loadConfig());
  }

  /**
   * Starts a server with the specified server configuration.
   *
   * @param configuration the server configuration
   */
  private void doStartServer(CMcpServerConfiguration configuration) {
    if (configuration.enabled()) {
      CConfigurableMcpServerFactory.getServer(configuration).startServer();
    } else {
      log.warn("MCP server is disabled, please check your configuration file.");
    }
  }
}

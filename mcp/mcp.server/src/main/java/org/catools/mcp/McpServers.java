package org.catools.mcp;

import org.catools.mcp.configuration.McpServerConfiguration;
import org.catools.mcp.configuration.YamlConfigurationLoader;
import org.catools.mcp.di.DependencyInjector;
import org.catools.mcp.di.DependencyInjectorProvider;
import org.catools.mcp.di.GuiceInjectorModule;
import org.catools.mcp.server.*;
import org.catools.mcp.server.configurable.CConfigurableMcpServerFactory;
import com.google.inject.Guice;
import com.google.inject.Module;
import io.modelcontextprotocol.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

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
public final class McpServers {

    /**
     * The singleton instance of McpServers.
     */
    private static final McpServers INSTANCE = new McpServers();

    /**
     * The dependency injector used to inject server components.
     */
    private static DependencyInjector injector;

    /**
     * Initializes the McpServers instance with the specified application main class and arguments.
     *
     * @param modules the Guice modules to use for dependency injection
     * @return the singleton instance of McpServers
     */
    public static McpServers run(Module... modules) {
        DependencyInjector injector = new DependencyInjector(Guice.createInjector(modules));
        DependencyInjectorProvider.INSTANCE.initialize(injector);
        McpServers.injector = injector;
        return INSTANCE;
    }

    /**
     * Starts a standard input/output (stdio) server with the specified server info.
     *
     * @param serverInfo the server info for the stdio server
     */
    public void startStdioServer(McpServerInfo serverInfo) {
        injector.getInstance(BaseMcpStdioServer.class).start(serverInfo);
    }

    /**
     * Starts a http server-sent events (sse) server with the specified server info.
     *
     * @param serverInfo the server info for the sse server
     */
    public void startSseServer(McpSseServerInfo serverInfo) {
        injector.getInstance(BaseMcpSseServer.class).start(serverInfo);
    }

    /**
     * Starts a streamable http server with the specified server info.
     *
     * @param serverInfo the server info for the streamable server
     */
    public void startStreamableServer(McpStreamableServerInfo serverInfo) {
        injector.getInstance(BaseMcpStreamableServer.class).start(serverInfo);
    }

    /**
     * Starts a server with the specified configuration file name.
     *
     * @param configFileName the name of the configuration file
     */
    public void startServer(String configFileName) {
        Assert.notNull(configFileName, "configFileName must not be null");
        YamlConfigurationLoader configLoader = new YamlConfigurationLoader(configFileName);
        startServer(configLoader.loadConfig());
    }

    /**
     * Starts a server with the default configuration file name.
     */
    public void startServer() {
        YamlConfigurationLoader configLoader = new YamlConfigurationLoader();
        startServer(configLoader.loadConfig());
    }

    /**
     * Starts a server with the specified server configuration.
     *
     * @param configuration the server configuration
     */
    private void startServer(McpServerConfiguration configuration) {
        if (configuration.enabled()) {
            CConfigurableMcpServerFactory.getServer(configuration).startServer();
        } else {
            log.warn("MCP server is disabled, please check your configuration file.");
        }
    }
}

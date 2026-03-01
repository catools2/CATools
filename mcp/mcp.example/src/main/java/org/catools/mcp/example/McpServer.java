package org.catools.mcp.example;

import java.util.Set;

import org.catools.mcp.McpServers;
import org.catools.mcp.annotation.McpServerApplication;
import org.catools.mcp.configuration.YamlConfigurationLoader;
import org.catools.mcp.context.McpDataStorage;
import org.catools.mcp.di.GuiceInjectorModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple runner for the MCP web server PoC.
 * Starts the MCP server using the default `mcp-server.yml` configuration on the
 * classpath.
 */
@Slf4j
@McpServerApplication(basePackages = "org.catools")
public class McpServer {

    public static void main(String[] args) {
        log.info("Starting MCP Web Server (PoC) using mcp-server.yml on classpath...");

        McpDataStorage.setSharedBetweenThreads(true);

        String configFileName = "mcp-calc-server.yml";

        // Extract config from JAR if needed
        YamlConfigurationLoader configLoader = new YamlConfigurationLoader(configFileName);

        Set<String> groups = configLoader.loadConfig().groups();

        // Create base module that scans for @McpTool annotations
        GuiceInjectorModule baseModule = new GuiceInjectorModule(McpServer.class, groups);

        // Create override module that provides concrete implementations
        McpInjectorModule overrideModule = new McpInjectorModule();

        // Combine modules with override - this allows overrideModule to replace
        // bindings from baseModule
        Module combinedModule = Modules.override(baseModule).with(overrideModule);

        McpServers.run(combinedModule).startServer();
    }
}

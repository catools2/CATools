package org.catools.mcp.web;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.CMcpServers;
import org.catools.mcp.annotation.CMcpServerApplication;
import org.catools.mcp.di.CGuiceInjectorModule;

/**
 * Simple runner for the MCP web server PoC.
 * Starts the MCP server using the default `mcp-server.yml` configuration on the classpath.
 */
@Slf4j
@CMcpServerApplication(basePackages = "org.catools")
public class CWebMcpServer {

  public static void main(String[] args) {
    log.info("Starting MCP Web Server (PoC) using mcp-server.yml on classpath...");

    try {
      // Create base module that scans for @CMcpTool annotations
      CGuiceInjectorModule baseModule = new CGuiceInjectorModule(CWebMcpServer.class);

      // Create override module that provides concrete implementations
      CWebMcpInjectorModule overrideModule = new CWebMcpInjectorModule();

      // Combine modules with override - this allows overrideModule to replace bindings from baseModule
      Module combinedModule = Modules.override(baseModule).with(overrideModule);

      CMcpServers.run(combinedModule).startServer();
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      System.exit(1);
    }
  }
}

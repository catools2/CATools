package org.catools.mcp.example;

import com.google.inject.AbstractModule;

/**
 * Guice module for Web MCP Server dependency injection.
 * This module overrides the automatic interface binding from CGuiceInjectorModule
 * with concrete implementations.
 */
public class McpInjectorModule extends AbstractModule {

    private static final Calc WEB_TEST = new Calc();

    @Override
    protected void configure() {
        bind(Calc.class).toInstance(WEB_TEST);
    }
}

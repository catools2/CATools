package org.catools.mcp.web;

import com.google.inject.AbstractModule;
import org.catools.web.controls.CWebElement;
import org.catools.web.controls.CWebElementActions;
import org.catools.web.controls.CWebElementStates;
import org.catools.web.drivers.CDriver;
import org.catools.web.drivers.CDriverActions;
import org.catools.web.tests.CWebTest;

/**
 * Guice module for Web MCP Server dependency injection.
 * This module overrides the automatic interface binding from CGuiceInjectorModule
 * with concrete implementations.
 *
 * <p>This module is designed to be used with Modules.override() to replace the
 * CDriverActions binding that CGuiceInjectorModule creates when it scans for @CMcpTool methods.
 */
public class CWebMcpInjectorModule extends AbstractModule {

  private static final CMcpWebTest WEB_TEST = new CMcpWebTest();

  @Override
  protected void configure() {
    bind(CWebTest.class).toInstance(WEB_TEST);
    bind(CMcpWebTest.class).toInstance(WEB_TEST);

    bind(CDriver.class).toInstance(WEB_TEST.getDriver());
    bind(CDriverActions.class).toInstance(WEB_TEST.getDriver());

    // Use lambda providers to get the current webElement instance dynamically
    bind(CWebElement.class).toProvider(WEB_TEST::getWebElement);
    bind(CWebElementActions.class).toProvider(WEB_TEST::getWebElement);
    bind(CWebElementStates.class).toProvider(WEB_TEST::getWebElement);
  }
}

package org.catools.mcp.server.component;

import java.lang.reflect.Method;
import org.catools.mcp.di.CDependencyInjector;
import org.catools.mcp.di.CDependencyInjectorProvider;

/**
 * This interface represents an MCP server component (resource/prompt/tool) that is responsible for
 * creating instances of a specific type {@code T} for a given method.
 *
 * @param <T> the type of the component
 */
public interface CMcpServerComponent<T> {
  /**
   * Creates an instance of the component for the specified method.
   *
   * @param method the method for which to create an instance
   * @return an instance of the component for the specified method
   */
  T create(Method method);

  /**
   * Gets the dependency injector to use for injecting component attributes.
   *
   * @return the dependency injector
   */
  default CDependencyInjector getInjector() {
    return CDependencyInjectorProvider.INSTANCE.getInjector();
  }
}

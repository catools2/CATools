package org.catools.mcp.server.component;

import org.catools.mcp.di.DependencyInjector;
import org.catools.mcp.di.DependencyInjectorProvider;

import java.lang.reflect.Method;

/**
 * This interface represents an MCP server component (resource/prompt/tool) that is responsible for
 * creating instances of a specific type {@code T} for a given method.
 *
 * @param <T> the type of the component
 */
public interface McpServerComponent<T> {
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
    default DependencyInjector getInjector() {
        return DependencyInjectorProvider.INSTANCE.getInjector();
    }
}

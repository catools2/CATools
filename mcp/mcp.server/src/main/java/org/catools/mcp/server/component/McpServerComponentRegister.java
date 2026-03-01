package org.catools.mcp.server.component;

import org.catools.mcp.annotation.McpPrompt;
import org.catools.mcp.annotation.McpResource;
import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.di.DependencyInjector;
import org.catools.mcp.di.DependencyInjectorProvider;
import org.catools.mcp.struct.Immutable;
import org.catools.mcp.util.ReflectionUtil;
import io.modelcontextprotocol.server.McpSyncServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Registers MCP server components (resources, prompts, and tools) with the specified server.
 * Uses Lombok to reduce boilerplate code for field access.
 */
@Slf4j
@Getter
public final class McpServerComponentRegister {
    /**
     * The dependency injector for MCP server components.
     */
    private final DependencyInjector injector;

    /**
     * The MCP sync server to register components with.
     */
    private final Immutable<McpSyncServer> server;

    /**
     * Creates a new instance of {@code CMcpServerComponentRegister} with the specified server.
     * Initializes the dependency injector and reflections from the provider.
     *
     * @param server the MCP sync server to register components with
     */
    public McpServerComponentRegister(McpSyncServer server) {
        this.injector = DependencyInjectorProvider.INSTANCE.getInjector();
        this.server = Immutable.of(server);
    }

    /**
     * Creates a new instance of {@code CMcpServerComponentRegister} with the specified server.
     *
     * @param server the MCP sync server to register components with
     * @return a new instance of {@code CMcpServerComponentRegister} with the specified server
     */
    public static McpServerComponentRegister of(McpSyncServer server) {
        return new McpServerComponentRegister(server);
    }

    /**
     * Registers MCP server components (resources, prompts, and tools) with the specified server.
     */
    public void registerComponents(Set<String> groupsFilter) {
        register(McpResource.class, groupsFilter, McpServerResource.class, McpSyncServer::addResource);
        register(McpPrompt.class, groupsFilter, McpServerPrompt.class, McpSyncServer::addPrompt);
        register(McpTool.class, groupsFilter, McpServerTool.class, McpSyncServer::addTool);
    }

    /**
     * Registers MCP server components with the specified server.
     *
     * @param annotationClass    the annotation class to use for component discovery
     * @param componentClass     the component class to use for component creation
     * @param serverAddComponent the method to use for adding components to the server
     * @param <T>                the type of the component to register
     */
    private <T> void register(
            Class<? extends Annotation> annotationClass,
            Set<String> groupsFilter,
            Class<? extends McpServerComponent<T>> componentClass,
            BiConsumer<McpSyncServer, T> serverAddComponent) {

        for (Reflections reflections : McpReflections.getReflections()) {
            Set<Method> methods = reflections.getMethodsAnnotatedWith(annotationClass);
            McpServerComponent<T> component = injector.getInstance(componentClass);
            for (Method method : methods) {
                if (groupsFilter != null && !groupsFilter.isEmpty()) {
                    // changed to Set<String> to match updated helper return type
                    Set<String> methodGroups = ReflectionUtil.getGroupsFieldFromAnnotation(method, annotationClass);
                    if (methodGroups.isEmpty()) {
                        log.debug("Skipping registration of method {} due to missing/empty groups", method);
                        continue;
                    }

                    boolean hasIntersection = groupsFilter.stream().anyMatch(methodGroups::contains);
                    if (!hasIntersection) {
                        log.debug("Skipping registration of method {} due to group filter", method);
                        continue;
                    }
                }
                serverAddComponent.accept(server.get(), component.create(method));
            }
        }
    }
}


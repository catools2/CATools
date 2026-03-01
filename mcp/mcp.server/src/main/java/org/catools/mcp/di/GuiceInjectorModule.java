package org.catools.mcp.di;

import org.catools.mcp.annotation.McpPrompt;
import org.catools.mcp.annotation.McpResource;
import org.catools.mcp.annotation.McpServerApplication;
import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.server.BaseMcpSseServer;
import org.catools.mcp.server.BaseMcpStdioServer;
import org.catools.mcp.server.BaseMcpStreamableServer;
import org.catools.mcp.server.component.McpReflections;
import org.catools.mcp.server.component.McpServerPrompt;
import org.catools.mcp.server.component.McpServerResource;
import org.catools.mcp.server.component.McpServerTool;
import org.catools.mcp.server.converter.McpPromptParameterConverter;
import org.catools.mcp.server.converter.McpToolParameterConverter;
import org.catools.mcp.util.ReflectionUtil;
import com.google.inject.AbstractModule;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import static com.google.inject.Scopes.SINGLETON;
import static java.util.stream.Collectors.toSet;

/**
 * This class is a Guice module that configures bindings for classes annotated with {@link
 * McpServerApplication}, {@link McpResource}, {@link McpPrompt}, and {@link McpTool}.
 */
public final class GuiceInjectorModule extends AbstractModule {

    private Set<String> groups;

    /**
     * Constructs a new {@link GuiceInjectorModule} with the specified main class.
     *
     * @param mainClass the main class to use for configuration
     */
    public GuiceInjectorModule(Class<?> mainClass, Set<String> groups) {
        McpReflections.registerReflections(mainClass);
        this.groups = groups;
    }

    @Override
    protected void configure() {
        // Bind classes of methods annotated by McpResource, McpPrompt, McpTool
        bindClassesOfMethodsAnnotatedWith(McpResource.class);
        bindClassesOfMethodsAnnotatedWith(McpPrompt.class);
        bindClassesOfMethodsAnnotatedWith(McpTool.class);

        // Bind all implementations of McpServerComponent
        bind(McpServerResource.class).in(SINGLETON);
        bind(McpServerPrompt.class).in(SINGLETON);
        bind(McpServerTool.class).in(SINGLETON);

        // Bind all implementations of ParameterConverter
        bind(McpPromptParameterConverter.class).in(SINGLETON);
        bind(McpToolParameterConverter.class).in(SINGLETON);

        // Bind all implementations of org.catools.mcp.mcp.server.McpServer
        bind(BaseMcpStdioServer.class).in(SINGLETON);
        bind(BaseMcpSseServer.class).in(SINGLETON);
        bind(BaseMcpStreamableServer.class).in(SINGLETON);
    }

    /**
     * Binds all classes of methods annotated with the specified annotation.
     *
     * @param annotation the annotation to scan for methods
     */
    private void bindClassesOfMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        Set<Reflections> reflections = McpReflections.getReflections();
        for (Reflections reflection : reflections) {
            Set<Method> methods = reflection.getMethodsAnnotatedWith(annotation).stream().filter(method -> filterGroups(method, annotation)).collect(toSet());
            Set<Class<?>> classes = methods.stream().map(Method::getDeclaringClass).collect(toSet());
            classes.forEach(clazz -> bind(clazz).in(SINGLETON));
        }
    }

    public boolean filterGroups(Method method, Class<? extends Annotation> annotationClass) {
        if (groups == null || groups.isEmpty()) {
            return true;
        }
        // changed to Set<String> to match updated helper return type
        Set<String> methodGroups = ReflectionUtil.getGroupsFieldFromAnnotation(method, annotationClass);
        if (methodGroups.isEmpty()) {
            return true;
        }

        return groups.stream().anyMatch(methodGroups::contains);
    }
}

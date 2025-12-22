package org.catools.mcp.di;

import static com.google.inject.Scopes.SINGLETON;
import static java.util.stream.Collectors.toSet;

import com.google.inject.AbstractModule;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import org.catools.mcp.annotation.CMcpPrompt;
import org.catools.mcp.annotation.CMcpResource;
import org.catools.mcp.annotation.CMcpServerApplication;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.server.CBaseMcpSseServer;
import org.catools.mcp.server.CBaseMcpStdioServer;
import org.catools.mcp.server.CBaseMcpStreamableServer;
import org.catools.mcp.server.component.CMcpReflections;
import org.catools.mcp.server.component.CMcpServerPrompt;
import org.catools.mcp.server.component.CMcpServerResource;
import org.catools.mcp.server.component.CMcpServerTool;
import org.catools.mcp.server.converter.CMcpPromptParameterConverter;
import org.catools.mcp.server.converter.CMcpToolParameterConverter;
import org.reflections.Reflections;

/**
 * This class is a Guice module that configures bindings for classes annotated with {@link
 * CMcpServerApplication}, {@link CMcpResource}, {@link CMcpPrompt}, and {@link CMcpTool}.
 */
public final class CGuiceInjectorModule extends AbstractModule {

  /**
   * Constructs a new {@link CGuiceInjectorModule} with the specified main class.
   *
   * @param mainClass the main class to use for configuration
   */
  public CGuiceInjectorModule(Class<?> mainClass) {
    CMcpReflections.registerReflections(mainClass);
  }

  @Override
  protected void configure() {
    // Bind classes of methods annotated by McpResource, McpPrompt, McpTool
    bindClassesOfMethodsAnnotatedWith(CMcpResource.class);
    bindClassesOfMethodsAnnotatedWith(CMcpPrompt.class);
    bindClassesOfMethodsAnnotatedWith(CMcpTool.class);

    // Bind all implementations of McpServerComponent
    bind(CMcpServerResource.class).in(SINGLETON);
    bind(CMcpServerPrompt.class).in(SINGLETON);
    bind(CMcpServerTool.class).in(SINGLETON);

    // Bind all implementations of ParameterConverter
    bind(CMcpPromptParameterConverter.class).in(SINGLETON);
    bind(CMcpToolParameterConverter.class).in(SINGLETON);

    // Bind all implementations of org.catools.mcp.server.McpServer
    bind(CBaseMcpStdioServer.class).in(SINGLETON);
    bind(CBaseMcpSseServer.class).in(SINGLETON);
    bind(CBaseMcpStreamableServer.class).in(SINGLETON);
  }

  /**
   * Binds all classes of methods annotated with the specified annotation.
   *
   * @param annotation the annotation to scan for methods
   */
  private void bindClassesOfMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
    Set<Reflections> reflections = CMcpReflections.getReflections();
    for (Reflections reflection : reflections) {
      Set<Method> methods = reflection.getMethodsAnnotatedWith(annotation);
      Set<Class<?>> classes = methods.stream().map(Method::getDeclaringClass).collect(toSet());
      classes.forEach(clazz -> bind(clazz).in(SINGLETON));
    }
  }
}

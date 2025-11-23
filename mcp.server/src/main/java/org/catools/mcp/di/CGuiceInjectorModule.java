package org.catools.mcp.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.catools.mcp.annotation.CMcpPrompt;
import org.catools.mcp.annotation.CMcpResource;
import org.catools.mcp.annotation.CMcpServerApplication;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.server.CBaseMcpSseServer;
import org.catools.mcp.server.CBaseMcpStdioServer;
import org.catools.mcp.server.CBaseMcpStreamableServer;
import org.catools.mcp.server.component.CMcpServerPrompt;
import org.catools.mcp.server.component.CMcpServerResource;
import org.catools.mcp.server.component.CMcpServerTool;
import org.catools.mcp.server.converter.CMcpPromptParameterConverter;
import org.catools.mcp.server.converter.CMcpToolParameterConverter;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import static com.google.inject.Scopes.SINGLETON;
import static java.util.stream.Collectors.toSet;
import static org.reflections.scanners.Scanners.FieldsAnnotated;
import static org.reflections.scanners.Scanners.MethodsAnnotated;

/**
 * This class is a Guice module that configures bindings for classes annotated with {@link
 * CMcpServerApplication}, {@link CMcpResource}, {@link CMcpPrompt}, and {@link CMcpTool}.
 */
public final class CGuiceInjectorModule extends AbstractModule {

  /**
   * The main class to use for configuration.
   */
  private final Class<?> mainClass;

  /**
   * Constructs a new {@link CGuiceInjectorModule} with the specified main class.
   *
   * @param mainClass the main class to use for configuration
   */
  public CGuiceInjectorModule(Class<?> mainClass) {
    this.mainClass = mainClass;
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
   * Provides a {@link Reflections} instance for the main class.
   *
   * @return a {@link Reflections} instance for the main class
   */
  @Provides
  @Singleton
  public Reflections provideReflections() {
    CMcpServerApplication application = mainClass.getAnnotation(CMcpServerApplication.class);
    final String basePackage = determineBasePackage(application);
    return new Reflections(basePackage, MethodsAnnotated, FieldsAnnotated);
  }

  /**
   * Determines the base package for the {@link Reflections} instance to scan.
   *
   * @param application the {@link CMcpServerApplication} annotation
   * @return the base package for the {@link Reflections} instance to scan
   */
  private String determineBasePackage(CMcpServerApplication application) {
    if (application != null) {
      if (!application.basePackage().trim().isBlank()) {
        return application.basePackage();
      }
      if (application.basePackageClass() != Object.class) {
        return application.basePackageClass().getPackageName();
      }
    }
    return mainClass.getPackageName();
  }

  /**
   * Binds all classes of methods annotated with the specified annotation.
   *
   * @param annotation the annotation to scan for methods
   */
  private void bindClassesOfMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
    Reflections reflections = provideReflections();
    Set<Method> methods = reflections.getMethodsAnnotatedWith(annotation);
    Set<Class<?>> classes = methods.stream().map(Method::getDeclaringClass).collect(toSet());
    classes.forEach(clazz -> bind(clazz).in(SINGLETON));
  }
}

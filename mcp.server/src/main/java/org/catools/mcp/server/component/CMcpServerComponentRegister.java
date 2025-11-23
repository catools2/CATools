package org.catools.mcp.server.component;

import io.modelcontextprotocol.server.McpSyncServer;
import lombok.Getter;
import org.catools.common.struct.CImmutable;
import org.catools.mcp.annotation.CMcpPrompt;
import org.catools.mcp.annotation.CMcpResource;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.di.CDependencyInjector;
import org.catools.mcp.di.CDependencyInjectorProvider;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Registers MCP server components (resources, prompts, and tools) with the specified server.
 * Uses Lombok to reduce boilerplate code for field access.
 */
@Getter
public final class CMcpServerComponentRegister {

  /**
   * The dependency injector for MCP server components.
   */
  private final CDependencyInjector injector;

  /**
   * The reflections instance for MCP server components.
   */
  private final Reflections reflections;

  /**
   * The MCP sync server to register components with.
   */
  private final CImmutable<McpSyncServer> server;

  /**
   * Creates a new instance of {@code CMcpServerComponentRegister} with the specified server.
   * Initializes the dependency injector and reflections from the provider.
   *
   * @param server the MCP sync server to register components with
   */
  public CMcpServerComponentRegister(McpSyncServer server) {
    this.injector = CDependencyInjectorProvider.INSTANCE.getInjector();
    this.reflections = injector.getInstance(Reflections.class);
    this.server = CImmutable.of(server);
  }

  /**
   * Creates a new instance of {@code CMcpServerComponentRegister} with the specified server.
   *
   * @param server the MCP sync server to register components with
   * @return a new instance of {@code CMcpServerComponentRegister} with the specified server
   */
  public static CMcpServerComponentRegister of(McpSyncServer server) {
    return new CMcpServerComponentRegister(server);
  }

  /**
   * Registers MCP server components (resources, prompts, and tools) with the specified server.
   */
  public void registerComponents() {
    register(CMcpResource.class, CMcpServerResource.class, McpSyncServer::addResource);
    register(CMcpPrompt.class, CMcpServerPrompt.class, McpSyncServer::addPrompt);
    register(CMcpTool.class, CMcpServerTool.class, McpSyncServer::addTool);
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
      Class<? extends CMcpServerComponent<T>> componentClass,
      BiConsumer<McpSyncServer, T> serverAddComponent) {

    Set<Method> methods = reflections.getMethodsAnnotatedWith(annotationClass);
    CMcpServerComponent<T> component = injector.getInstance(componentClass);
    for (Method method : methods) {
      serverAddComponent.accept(server.get(), component.create(method));
    }
  }
}

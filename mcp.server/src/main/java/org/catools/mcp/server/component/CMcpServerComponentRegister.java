package org.catools.mcp.server.component;

import io.modelcontextprotocol.server.McpSyncServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.common.struct.CImmutable;
import org.catools.mcp.annotation.CMcpPrompt;
import org.catools.mcp.annotation.CMcpResource;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.di.CDependencyInjector;
import org.catools.mcp.di.CDependencyInjectorProvider;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Registers MCP server components (resources, prompts, and tools) with the specified server.
 * Uses Lombok to reduce boilerplate code for field access.
 */
@Slf4j
@Getter
public final class CMcpServerComponentRegister {
  /**
   * The dependency injector for MCP server components.
   */
  private final CDependencyInjector injector;

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
  public void registerComponents(Set<String> groupsFilter) {
    register(CMcpResource.class, groupsFilter, CMcpServerResource.class, McpSyncServer::addResource);
    register(CMcpPrompt.class, groupsFilter, CMcpServerPrompt.class, McpSyncServer::addPrompt);
    register(CMcpTool.class, groupsFilter, CMcpServerTool.class, McpSyncServer::addTool);
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
      Class<? extends CMcpServerComponent<T>> componentClass,
      BiConsumer<McpSyncServer, T> serverAddComponent) {

    for (Reflections reflections : CMcpReflections.getReflections()) {
      Set<Method> methods = reflections.getMethodsAnnotatedWith(annotationClass);
      CMcpServerComponent<T> component = injector.getInstance(componentClass);
      for (Method method : methods) {
        if (groupsFilter != null && !groupsFilter.isEmpty()) {
          // changed to Set<String> to match updated helper return type
          Set<String> methodGroups = getGroupsFiledFromAnnotation(method, annotationClass);
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

  private static Set<String> getGroupsFiledFromAnnotation(Method method, Class<? extends Annotation> annotationClass) {
    if (method == null || annotationClass == null) return Collections.emptySet();
    Annotation annotation = method.getAnnotation(annotationClass);

    if (annotation == null) return Collections.emptySet();
    String fieldName = "groups";
    try {
      Method m = annotation.annotationType().getMethod(fieldName);
      Object raw = m.invoke(annotation);
      if (raw == null) return Collections.emptySet();

      // String[]
      if (raw instanceof String[] sArr) {
        return Arrays.stream(sArr).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
      }

      // single String
      if (raw instanceof String s) {
        if (StringUtils.isNotBlank(s)) {
          return Collections.singleton(s);
        }
      }

      return Collections.emptySet();
    } catch (NoSuchMethodException e) {
      log.debug("Annotation {} does not declare field '{}'", annotation.annotationType().getName(), fieldName);
      return Collections.emptySet();
    } catch (Throwable t) {
      log.warn("Failed to read field '{}' from annotation {}", fieldName, annotation.annotationType().getName(), t);
      return Collections.emptySet();
    }
  }
}

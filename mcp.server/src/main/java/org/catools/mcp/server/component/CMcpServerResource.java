package org.catools.mcp.server.component;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.utils.CJsonUtil;
import org.catools.common.utils.CStringUtil;
import org.catools.mcp.annotation.CMcpResource;
import org.catools.mcp.reflect.CInvocationResult;
import org.catools.mcp.reflect.CMethodCache;
import org.catools.mcp.util.CReflectionUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * This class represents an MCP server resource component.
 */
@Slf4j
public class CMcpServerResource implements CMcpServerComponent<McpServerFeatures.SyncResourceSpecification> {

  @Override
  public McpServerFeatures.SyncResourceSpecification create(Method method) {
    // Use reflection cache for performance optimization
    CMethodCache methodCache = CReflectionUtil.INSTANCE.getOrCache(method);
    Object instance = getInjector().getInstance(methodCache.getDeclaringClass());

    CMcpResource res = methodCache.getMcpResourceAnnotation();
    final String name = CStringUtil.defaultIfBlank(res.name(), methodCache.getMethodName());
    final String title = res.title();
    final String description = res.description();

    McpSchema.Resource resource =
        McpSchema.Resource.builder()
            .uri(res.uri())
            .name(name)
            .title(title)
            .description(description)
            .mimeType(res.mimeType())
            .annotations(new McpSchema.Annotations(List.of(res.roles()), res.priority()))
            .build();

    log.debug("Registering resource: {}", CJsonUtil.toString(resource));

    return new McpServerFeatures.SyncResourceSpecification(
        resource, (exchange, request) -> invoke(instance, methodCache, resource));
  }

  /**
   * Invokes the resource method with the specified arguments.
   *
   * @param instance    the instance of the class that declares the resource method
   * @param methodCache the cached method information
   * @param resource    the resource specification
   * @return the result of the resource invocation
   */
  private McpSchema.ReadResourceResult invoke(
      Object instance, CMethodCache methodCache, McpSchema.Resource resource) {

    CInvocationResult invocation = CReflectionUtil.INSTANCE.invoke(instance, methodCache);
    final String uri = resource.uri();
    final String mimeType = resource.mimeType();
    final String text = invocation.result().toString();
    McpSchema.ResourceContents contents = new McpSchema.TextResourceContents(uri, mimeType, text);
    return new McpSchema.ReadResourceResult(List.of(contents));
  }
}

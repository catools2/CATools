package org.catools.mcp.util;

import lombok.extern.slf4j.Slf4j;
import org.catools.mcp.reflect.CInvocationResult;
import org.catools.mcp.reflect.CMethodCache;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility providing reflection-based method caching and invocation capabilities for MCP server components.
 * Implements singleton pattern to maintain a centralized method metadata cache across the application.
 *
 * <p>This enum-based singleton ensures thread-safe method caching and provides optimized
 * reflection operations by avoiding repeated method metadata extraction.
 */
@Slf4j
public enum CReflectionUtil {

  /**
   * Singleton instance providing reflection utility operations.
   */
  INSTANCE;

  /**
   * Thread-safe cache storing method metadata to optimize repeated reflection operations.
   */
  private final ConcurrentHashMap<Method, CMethodCache> methodCache = new ConcurrentHashMap<>();

  /**
   * Retrieves cached method metadata or creates and caches it if not present.
   * Uses compute-if-absent pattern for thread-safe lazy initialization.
   *
   * @param method the method to cache metadata for
   * @return cached method metadata containing parameter types, return type, and signature
   */
  public CMethodCache getOrCache(Method method) {
    return methodCache.computeIfAbsent(
        method,
        m -> {
          final String className = m.getDeclaringClass().getName();
          log.debug("Caching method metadata: {}.{}", className, m.getName());
          return CMethodCache.of(m);
        });
  }

  /**
   * Invokes a method using cached metadata with the provided parameters.
   * Handles both successful invocations and exceptions, returning a structured result.
   *
   * @param instance    the object instance on which to invoke the method
   * @param methodCache cached method metadata containing invocation details
   * @param params      list of parameters to pass to the method
   * @return invocation result containing either the return value or exception details
   */
  public CInvocationResult invoke(Object instance, CMethodCache methodCache, List<Object> params) {
    Method method = methodCache.getMethod();
    CInvocationResult.Builder builder = CInvocationResult.builder();
    try {
      Object result = method.invoke(instance, params.toArray());

      Class<?> returnType = method.getReturnType();
      if (returnType == void.class || returnType == Void.class) {
        return builder.result("Method executed successfully with void return type").build();
      }

      final String resultIfNull = "The method call succeeded but the return value is null";
      return builder.result(Objects.requireNonNullElse(result, resultIfNull)).build();
    } catch (Exception e) {
      final String errorMessage = "Failed to invoke method: " + methodCache.getMethodSignature();
      log.error(errorMessage, e);
      return builder.result(errorMessage).exception(e).build();
    }
  }

  /**
   * Invokes a parameterless method using cached metadata.
   * Convenience method for zero-argument method invocations.
   *
   * @param instance    the object instance on which to invoke the method
   * @param methodCache cached method metadata containing invocation details
   * @return invocation result containing either the return value or exception details
   */
  public CInvocationResult invoke(Object instance, CMethodCache methodCache) {
    return invoke(instance, methodCache, List.of());
  }
}

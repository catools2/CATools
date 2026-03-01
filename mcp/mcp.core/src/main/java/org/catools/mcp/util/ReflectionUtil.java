package org.catools.mcp.util;

import org.catools.mcp.reflect.InvocationResult;
import org.catools.mcp.reflect.MethodCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Utility providing reflection-based method caching and invocation capabilities for MCP server components.
 * Implements singleton pattern to maintain a centralized method metadata cache across the application.
 *
 * <p>This enum-based singleton ensures thread-safe method caching and provides optimized
 * reflection operations by avoiding repeated method metadata extraction.
 */
@Slf4j
public enum ReflectionUtil {

    /**
     * Singleton instance providing reflection utility operations.
     */
    INSTANCE;

    /**
     * Thread-safe cache storing method metadata to optimize repeated reflection operations.
     */
    private final ConcurrentHashMap<Method, MethodCache> methodCache = new ConcurrentHashMap<>();

    /**
     * Retrieves cached method metadata or creates and caches it if not present.
     * Uses compute-if-absent pattern for thread-safe lazy initialization.
     *
     * @param method the method to cache metadata for
     * @return cached method metadata containing parameter types, return type, and signature
     */
    public MethodCache getOrCache(Method method) {
        return methodCache.computeIfAbsent(
                method,
                m -> {
                    final String className = m.getDeclaringClass().getName();
                    log.debug("Caching method metadata: {}.{}", className, m.getName());
                    return MethodCache.of(m);
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
    public InvocationResult invoke(Object instance, MethodCache methodCache, List<Object> params) {
        Method method = methodCache.getMethod();
        InvocationResult.Builder builder = InvocationResult.builder();
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
    public InvocationResult invoke(Object instance, MethodCache methodCache) {
        return invoke(instance, methodCache, List.of());
    }

    /**
     * Retrieves the 'groups' field from a specified annotation on a method.
     * Supports both single String and String array types for the 'groups' field.
     *
     * @param method          the method to inspect
     * @param annotationClass the annotation class to look for
     * @return a set of group names extracted from the annotation, or an empty set if not found
     */
    public static Set<String> getGroupsFieldFromAnnotation(Method method, Class<? extends Annotation> annotationClass) {
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

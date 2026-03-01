package org.catools.mcp.reflect;

import org.catools.mcp.annotation.McpPrompt;
import org.catools.mcp.annotation.McpResource;
import org.catools.mcp.annotation.McpTool;
import org.catools.mcp.struct.Immutable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * This class caches information about a Java method, including its name, declaring class,
 * parameters, and annotations.
 */
@EqualsAndHashCode
@ToString
@Getter
public final class MethodCache {

    /**
     * The cached method.
     */
    private final Immutable<Method> method;

    /**
     * The name of the cached method.
     */
    private final String methodName;

    /**
     * The class that declares the cached method.
     */
    private final Class<?> declaringClass;

    /**
     * The parameters of the cached method.
     */
    private final Parameter[] parameters;

    /**
     * The return type of the cached method.
     */
    private final Class<?> returnType;

    /**
     * The signature of the cached method.
     */
    private final String methodSignature;

    /**
     * The annotation {@link McpResource} on the cached method.
     */
    private final McpResource mcpResourceAnnotation;

    /**
     * The annotation {@link McpPrompt} on the cached method.
     */
    private final McpPrompt mcpPromptAnnotation;

    /**
     * The annotation {@link McpTool} on the cached method.
     */
    private final McpTool mcpToolAnnotation;

    /**
     * Creates a new instance of {@code MethodCache} with the specified method.
     *
     * @param method the method to cache
     */
    public MethodCache(Method method) {
        this.method = Immutable.of(method);
        this.methodName = method.getName();
        this.declaringClass = method.getDeclaringClass();
        this.parameters = method.getParameters();
        this.returnType = method.getReturnType();
        this.methodSignature = method.toGenericString();
        this.mcpResourceAnnotation = method.getAnnotation(McpResource.class);
        this.mcpPromptAnnotation = method.getAnnotation(McpPrompt.class);
        this.mcpToolAnnotation = method.getAnnotation(McpTool.class);
    }

    /**
     * Creates a new instance of {@code MethodCache} with the specified method.
     *
     * @param method the method to cache
     * @return a new instance of {@code MethodCache} with the specified method
     */
    public static MethodCache of(Method method) {
        return new MethodCache(method);
    }

    /**
     * Returns the method cached by this {@code MethodCache} instance.
     *
     * @return the method cached by this {@code MethodCache} instance
     */
    public Method getMethod() {
        return method.get();
    }

    /**
     * Returns the parameters of the cached method.
     *
     * @return the parameters of the cached method
     */
    public Parameter[] getParameters() {
        return parameters.clone();
    }
}

package org.catools.mcp.reflect;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.catools.mcp.annotation.CMcpPrompt;
import org.catools.mcp.annotation.CMcpResource;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.common.struct.CImmutable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * This class caches information about a Java method, including its name, declaring class,
 * parameters, and annotations.
 */
@EqualsAndHashCode
@ToString
@Getter
public final class CMethodCache {

    /**
     * The cached method.
     */
    private final CImmutable<Method> method;

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
     * The annotation {@link CMcpResource} on the cached method.
     */
    private final CMcpResource mcpResourceAnnotation;

    /**
     * The annotation {@link CMcpPrompt} on the cached method.
     */
    private final CMcpPrompt mcpPromptAnnotation;

    /**
     * The annotation {@link CMcpTool} on the cached method.
     */
    private final CMcpTool mcpToolAnnotation;

    /**
     * Creates a new instance of {@code MethodCache} with the specified method.
     *
     * @param method the method to cache
     */
    public CMethodCache(Method method) {
        this.method = CImmutable.of(method);
        this.methodName = method.getName();
        this.declaringClass = method.getDeclaringClass();
        this.parameters = method.getParameters();
        this.returnType = method.getReturnType();
        this.methodSignature = method.toGenericString();
        this.mcpResourceAnnotation = method.getAnnotation(CMcpResource.class);
        this.mcpPromptAnnotation = method.getAnnotation(CMcpPrompt.class);
        this.mcpToolAnnotation = method.getAnnotation(CMcpTool.class);
    }

    /**
     * Creates a new instance of {@code MethodCache} with the specified method.
     *
     * @param method the method to cache
     * @return a new instance of {@code MethodCache} with the specified method
     */
    public static CMethodCache of(Method method) {
        return new CMethodCache(method);
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

package org.catools.mcp.server.component;

import org.catools.mcp.annotation.McpPrompt;
import org.catools.mcp.annotation.McpPromptParam;
import org.catools.mcp.reflect.InvocationResult;
import org.catools.mcp.reflect.MethodCache;
import org.catools.mcp.server.converter.McpPromptParameterConverter;
import org.catools.mcp.util.ReflectionUtil;
import org.catools.mcp.utils.JsonUtil;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents an MCP server prompt component.
 */
@Slf4j
public class McpServerPrompt implements McpServerComponent<McpServerFeatures.SyncPromptSpecification> {

    /**
     * The converter for MCP prompt parameters.
     */
    private final McpPromptParameterConverter parameterConverter;

    /**
     * Creates a new instance of {@code McpServerPrompt}.
     */
    public McpServerPrompt() {
        this.parameterConverter = getInjector().getInstance(McpPromptParameterConverter.class);
    }

    @Override
    public McpServerFeatures.SyncPromptSpecification create(Method method) {
        // Use reflection cache for performance optimization
        MethodCache methodCache = ReflectionUtil.INSTANCE.getOrCache(method);
        Object instance = getInjector().getInstance(methodCache.getDeclaringClass());

        McpPrompt promptMethod = methodCache.getMcpPromptAnnotation();
        final String name =
                StringUtils.defaultIfBlank(promptMethod.name(), methodCache.getMethodName());
        final String title = promptMethod.title();
        final String description = promptMethod.description();

        List<McpSchema.PromptArgument> promptArgs = createPromptArguments(methodCache.getParameters());
        McpSchema.Prompt prompt = new McpSchema.Prompt(name, title, description, promptArgs);

        log.debug("Registering prompt: {}", JsonUtil.toString(prompt));

        return new McpServerFeatures.SyncPromptSpecification(
                prompt, (exchange, request) -> invoke(instance, methodCache, description, request));
    }

    /**
     * Invokes the prompt method with the specified arguments.
     *
     * @param instance    the instance of the class that declares the prompt method
     * @param methodCache the cached method information
     * @param description the description of the prompt
     * @param request     the request for the prompt
     * @return the result of the prompt invocation
     */
    private McpSchema.GetPromptResult invoke(
            Object instance,
            MethodCache methodCache,
            String description,
            McpSchema.GetPromptRequest request) {

        Map<String, Object> arguments = request.arguments();
        List<Object> params = parameterConverter.convertAll(methodCache.getParameters(), arguments);
        InvocationResult invocation = ReflectionUtil.INSTANCE.invoke(instance, methodCache, params);

        McpSchema.Content content = new McpSchema.TextContent(invocation.result().toString());
        McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
        return new McpSchema.GetPromptResult(description, List.of(message));
    }

    /**
     * Creates a list of prompt arguments from the method parameters.
     *
     * @param methodParams the method parameters
     * @return the list of prompt arguments
     */
    private List<McpSchema.PromptArgument> createPromptArguments(Parameter[] methodParams) {
        List<McpSchema.PromptArgument> promptArguments = new ArrayList<>(methodParams.length);

        for (Parameter param : methodParams) {
            if (param.isAnnotationPresent(McpPromptParam.class)) {
                McpPromptParam promptParam = param.getAnnotation(McpPromptParam.class);
                final String name = promptParam.name();
                final String title = promptParam.title();
                final String description = promptParam.description();
                final boolean required = promptParam.required();
                promptArguments.add(new McpSchema.PromptArgument(name, title, description, required));
            }
        }

        return promptArguments;
    }
}

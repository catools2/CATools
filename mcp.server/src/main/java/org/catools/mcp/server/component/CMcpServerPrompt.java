package org.catools.mcp.server.component;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.utils.CJsonUtil;
import org.catools.common.utils.CStringUtil;
import org.catools.mcp.annotation.CMcpPrompt;
import org.catools.mcp.annotation.CMcpPromptParam;
import org.catools.mcp.reflect.CInvocationResult;
import org.catools.mcp.reflect.CMethodCache;
import org.catools.mcp.server.converter.CMcpPromptParameterConverter;
import org.catools.mcp.util.CReflectionUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents an MCP server prompt component.
 */
@Slf4j
public class CMcpServerPrompt implements CMcpServerComponent<McpServerFeatures.SyncPromptSpecification> {

  /**
   * The converter for MCP prompt parameters.
   */
  private final CMcpPromptParameterConverter parameterConverter;

  /**
   * Creates a new instance of {@code McpServerPrompt}.
   */
  public CMcpServerPrompt() {
    this.parameterConverter = getInjector().getInstance(CMcpPromptParameterConverter.class);
  }

  @Override
  public McpServerFeatures.SyncPromptSpecification create(Method method) {
    // Use reflection cache for performance optimization
    CMethodCache methodCache = CReflectionUtil.INSTANCE.getOrCache(method);
    Object instance = getInjector().getInstance(methodCache.getDeclaringClass());

    CMcpPrompt promptMethod = methodCache.getMcpPromptAnnotation();
    final String name =
        CStringUtil.defaultIfBlank(promptMethod.name(), methodCache.getMethodName());
    final String title = promptMethod.title();
    final String description = promptMethod.description();

    List<McpSchema.PromptArgument> promptArgs = createPromptArguments(methodCache.getParameters());
    McpSchema.Prompt prompt = new McpSchema.Prompt(name, title, description, promptArgs);

    log.debug("Registering prompt: {}", CJsonUtil.toString(prompt));

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
      CMethodCache methodCache,
      String description,
      McpSchema.GetPromptRequest request) {

    Map<String, Object> arguments = request.arguments();
    List<Object> params = parameterConverter.convertAll(methodCache.getParameters(), arguments);
    CInvocationResult invocation = CReflectionUtil.INSTANCE.invoke(instance, methodCache, params);

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
      if (param.isAnnotationPresent(CMcpPromptParam.class)) {
        CMcpPromptParam promptParam = param.getAnnotation(CMcpPromptParam.class);
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

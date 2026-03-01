package org.catools.mcp.server.component;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.catools.common.utils.CJsonUtil;
import org.catools.common.utils.CStringUtil;
import org.catools.mcp.annotation.CMcpJsonSchemaDefinition;
import org.catools.mcp.annotation.CMcpJsonSchemaProperty;
import org.catools.mcp.annotation.CMcpTool;
import org.catools.mcp.annotation.CMcpToolParam;
import org.catools.mcp.enums.CJavaTypeToJsonSchemaMapper;
import org.catools.mcp.reflect.CInvocationResult;
import org.catools.mcp.reflect.CMethodCache;
import org.catools.mcp.server.CMcpStructuredContent;
import org.catools.mcp.server.converter.CMcpToolParameterConverter;
import org.catools.mcp.util.CReflectionUtil;
import org.reflections.Reflections;

/** This class represents an MCP server tool component. */
@Slf4j
public class CMcpServerTool
    implements CMcpServerComponent<McpServerFeatures.SyncToolSpecification> {

  /** The parameter converter for MCP tool parameters. */
  private final CMcpToolParameterConverter parameterConverter;

  /** Creates a new instance of {@code McpServerTool}. */
  public CMcpServerTool() {
    this.parameterConverter = getInjector().getInstance(CMcpToolParameterConverter.class);
  }

  @Override
  public McpServerFeatures.SyncToolSpecification create(Method method) {
    // Use reflection cache for performance optimization
    CMethodCache methodCache = CReflectionUtil.INSTANCE.getOrCache(method);

    CMcpTool toolMethod = methodCache.getMcpToolAnnotation();
    final String name = CStringUtil.defaultIfBlank(toolMethod.name(), methodCache.getMethodName());
    final String title = toolMethod.title();
    final String description = toolMethod.description();

    McpSchema.JsonSchema inputSchema = createJsonSchema(methodCache.getParameters());
    Map<String, Object> outputSchema = createJsonSchemaDefinition(methodCache.getReturnType());
    McpSchema.Tool tool =
        McpSchema.Tool.builder()
            .name(name)
            .title(title)
            .description(description)
            .inputSchema(inputSchema)
            .outputSchema(outputSchema)
            .build();

    log.debug("Registering tool: {}", CJsonUtil.toString(tool));

    return McpServerFeatures.SyncToolSpecification.builder()
        .tool(tool)
        .callHandler(
            (exchange, request) -> {
              // Get a fresh instance on each invocation to support Provider bindings
              Object instance = getInjector().getInstance(methodCache.getDeclaringClass());
              return invoke(instance, methodCache, request);
            })
        .build();
  }

  /**
   * Invokes the tool method with the specified arguments.
   *
   * @param instance the instance of the class that declares the tool method
   * @param methodCache the cached method information
   * @param request the tool request containing the arguments
   * @return the result of the tool invocation
   */
  private McpSchema.CallToolResult invoke(
      Object instance, CMethodCache methodCache, McpSchema.CallToolRequest request) {

    Map<String, Object> arguments = request.arguments();
    List<Object> params = parameterConverter.convertAll(methodCache.getParameters(), arguments);
    CInvocationResult invocation = CReflectionUtil.INSTANCE.invoke(instance, methodCache, params);

    Object result = invocation.result();
    String textContent = result.toString();
    Object structuredContent = Map.of();

    if (result instanceof CMcpStructuredContent CMcpStructuredContent) {
      textContent = CMcpStructuredContent.asTextContent();
      structuredContent = CMcpStructuredContent;
    }

    return McpSchema.CallToolResult.builder()
        .content(List.of(new McpSchema.TextContent(textContent)))
        .structuredContent(structuredContent)
        .isError(invocation.isError())
        .build();
  }

  /**
   * Creates a JSON schema for the tool method parameters.
   *
   * @param methodParams the method parameters
   * @return the JSON schema for the tool method parameters
   */
  private McpSchema.JsonSchema createJsonSchema(Parameter[] methodParams) {
    Map<String, Object> properties = new LinkedHashMap<>();
    Map<String, Object> definitions = new LinkedHashMap<>();
    List<String> required = new ArrayList<>();

    for (Parameter param : methodParams) {
      if (param.isAnnotationPresent(CMcpToolParam.class)) {
        CMcpToolParam toolParam = param.getAnnotation(CMcpToolParam.class);
        final String parameterName = toolParam.name();
        Class<?> definitionClass = param.getType();
        Map<String, String> property = new HashMap<>();

        if (definitionClass.isAnnotationPresent(CMcpJsonSchemaDefinition.class)) {
          final String definitionClassName = definitionClass.getSimpleName();
          property.put("$ref", "#/definitions/" + definitionClassName);
          Map<String, Object> definition = createJsonSchemaDefinition(definitionClass);
          definitions.put(definitionClassName, definition);
        } else {
          property.put("type", CJavaTypeToJsonSchemaMapper.getJsonSchemaType(definitionClass));
          property.put("description", toolParam.description());
        }
        properties.put(parameterName, property);

        if (toolParam.required()) {
          required.add(parameterName);
        }
      }
    }

    final boolean hasAdditionalProperties = false;
    return new McpSchema.JsonSchema(
        CJavaTypeToJsonSchemaMapper.OBJECT.getJsonSchemaType(),
        properties,
        required,
        hasAdditionalProperties,
        definitions,
        definitions);
  }

  /**
   * Creates a JSON schema definition for the specified class.
   *
   * @param definitionClass the class to create the JSON schema definition for
   * @return the JSON schema definition for the specified class
   */
  private Map<String, Object> createJsonSchemaDefinition(Class<?> definitionClass) {
    Map<String, Object> definitionJsonSchema = new HashMap<>();
    definitionJsonSchema.put("type", CJavaTypeToJsonSchemaMapper.OBJECT.getJsonSchemaType());

    Map<String, Object> properties = new LinkedHashMap<>();
    List<String> required = new ArrayList<>();

    Reflections reflections = getInjector().getInstance(Reflections.class);
    Set<Field> definitionFields = reflections.getFieldsAnnotatedWith(CMcpJsonSchemaProperty.class);
    List<Field> fields =
        definitionFields.stream().filter(f -> f.getDeclaringClass() == definitionClass).toList();

    for (Field field : fields) {
      CMcpJsonSchemaProperty property = field.getAnnotation(CMcpJsonSchemaProperty.class);
      if (property == null) {
        continue;
      }

      Map<String, Object> fieldProperties = new HashMap<>();
      fieldProperties.put("type", CJavaTypeToJsonSchemaMapper.getJsonSchemaType(field.getType()));
      fieldProperties.put("description", property.description());

      final String fieldName = CStringUtil.defaultIfBlank(property.name(), field.getName());
      properties.put(fieldName, fieldProperties);

      if (property.required()) {
        required.add(fieldName);
      }
    }

    definitionJsonSchema.put("properties", properties);
    definitionJsonSchema.put("required", required);

    return definitionJsonSchema;
  }
}

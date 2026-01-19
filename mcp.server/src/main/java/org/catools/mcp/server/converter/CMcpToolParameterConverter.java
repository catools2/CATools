package org.catools.mcp.server.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Parameter;
import java.util.Map;
import org.catools.mcp.annotation.CMcpToolParam;
import org.catools.mcp.di.CDependencyInjector;
import org.catools.mcp.di.CDependencyInjectorProvider;
import org.catools.mcp.util.CTypeConverter;

/**
 * This class is used to convert the value of a parameter annotated with {@link CMcpToolParam} to
 * the required type.
 */
public class CMcpToolParameterConverter implements CParameterConverter<CMcpToolParam> {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  /**
   * Converts the value of the parameter annotated with {@link CMcpToolParam} to the required type.
   *
   * @param parameter the parameter annotated with {@link CMcpToolParam}
   * @param annotation the annotation instance
   * @param args the arguments passed to the method
   * @return the converted value of the parameter
   */
  @Override
  public Object convert(Parameter parameter, CMcpToolParam annotation, Map<String, Object> args) {
    CDependencyInjector injector = CDependencyInjectorProvider.INSTANCE.getInjector();
    Object rawValue = args.get(annotation.name());

    // First try to resolve via injector using the raw value as a constructor/qualifier hint
    Object injected = tryInject(injector, parameter, annotation, rawValue);
    if (injected != null) {
      return injected;
    }

    return convertValue(rawValue, parameter.getType());
  }

  private Object tryInject(
      CDependencyInjector injector,
      Parameter parameter,
      CMcpToolParam annotation,
      Object rawValue) {
    Class<?> targetType = parameter.getType();

    // If rawValue is present, prefer it over injection - this allows explicit args to override DI
    if (rawValue != null
        && !(rawValue instanceof String str && (str.isBlank() || str.equals("null")))) {
      // Check if it already matches the target type
      if (targetType.isAssignableFrom(rawValue.getClass())) {
        return rawValue;
      }
      // Otherwise let TypeConverter handle it and skip injection
      return null;
    }

    // Only try injection when no argument was provided
    // Try simple type binding
    Object instance = injector.tryGetInstance(targetType).orElse(null);
    if (instance != null) {
      return instance;
    }

    // Try @Named binding using the explicit MCP param name first, then the reflective name
    String paramName = annotation.name().isBlank() ? parameter.getName() : annotation.name();
    return injector.tryGetVariable(targetType, paramName).orElse(null);
  }

  /**
   * Converts a value to the target type using TypeConverter for primitives or Jackson for complex
   * types.
   *
   * @param value the value to convert
   * @param targetType the target type
   * @return the converted value
   */
  private Object convertValue(Object value, Class<?> targetType) {
    // First try TypeConverter (handles primitives and common types)
    Object converted = CTypeConverter.convert(value, targetType);

    // If TypeConverter just returned the same value and it's not the target type,
    // use Jackson for complex type conversion
    if (value != null && converted == value && !targetType.isAssignableFrom(value.getClass())) {
      try {
        return OBJECT_MAPPER.convertValue(value, targetType);
      } catch (IllegalArgumentException e) {
        // If Jackson fails, return the TypeConverter result
        return converted;
      }
    }

    return converted;
  }

  /**
   * Returns the type of the annotation this converter handles.
   *
   * @return the type of the annotation this converter handles
   */
  @Override
  public Class<CMcpToolParam> getAnnotationType() {
    return CMcpToolParam.class;
  }
}

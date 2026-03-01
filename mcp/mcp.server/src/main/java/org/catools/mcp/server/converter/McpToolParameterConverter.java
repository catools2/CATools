package org.catools.mcp.server.converter;

import org.catools.mcp.annotation.McpToolParam;
import org.catools.mcp.context.McpDataStorage;
import org.catools.mcp.di.DependencyInjector;
import org.catools.mcp.di.DependencyInjectorProvider;
import org.catools.mcp.util.TypeConverter;
import org.catools.mcp.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * This class is used to convert the value of a parameter annotated with {@link McpToolParam} to the
 * required type.
 */
public class McpToolParameterConverter implements ParameterConverter<McpToolParam> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Converts the value of the parameter annotated with {@link McpToolParam} to the required type.
     *
     * @param parameter  the parameter annotated with {@link McpToolParam}
     * @param annotation the annotation instance
     * @param args       the arguments passed to the method
     * @return the converted value of the parameter
     */
    @Override
    public Object convert(Parameter parameter, McpToolParam annotation, Map<String, Object> args) {
        DependencyInjector injector = DependencyInjectorProvider.INSTANCE.getInjector();
        Object rawValue = args.get(annotation.name());

        // First try to resolve via injector using the raw value as a constructor/qualifier hint
        Object injected = tryInject(injector, parameter, annotation, rawValue);
        if (injected != null) {
            return injected;
        }

        return convertValue(rawValue, parameter.getType());
    }

    private Object tryInject(DependencyInjector injector, Parameter parameter, McpToolParam annotation, Object rawValue) {
        Class<?> targetType = parameter.getType();

        // If rawValue is present, prefer it over injection - this allows explicit args to override DI
        if (rawValue != null) {
            if (rawValue instanceof String str) {
                if (!(str.isBlank() || str.equals("null"))) {
                    // Check if it already matches the target type
                    if (targetType.isAssignableFrom(rawValue.getClass())) {
                        return rawValue;
                    }

                    // try to read the string as JSON for complex types,
                    // this allows passing JSON strings for complex objects directly
                    Object fromJson = JsonUtil.read(str, parameter.getType());
                    if (fromJson != null) {
                        return fromJson;
                    }

                    // try to read value from storage if the string is reference name to the variable in storage,
                    // this allows passing reference name for complex objects stored in McpDataStorage
                    Object fromStorage = McpDataStorage.read(str);
                    if (fromStorage != null) {
                        return fromStorage;
                    }

                    return null;
                }
            }
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
     * Converts a value to the target type using TypeConverter for primitives or Jackson for complex types.
     *
     * @param value      the value to convert
     * @param targetType the target type
     * @return the converted value
     */
    private Object convertValue(Object value, Class<?> targetType) {
        // First try TypeConverter (handles primitives and common types)
        Object converted = TypeConverter.convert(value, targetType);

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
    public Class<McpToolParam> getAnnotationType() {
        return McpToolParam.class;
    }
}

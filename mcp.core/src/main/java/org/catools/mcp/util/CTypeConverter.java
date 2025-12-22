package org.catools.mcp.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.annotation.Nullable;
import lombok.experimental.UtilityClass;
import org.catools.common.utils.CStringUtil;

/**
 * Utility class providing type conversion operations between Java types and JSON schema types.
 * Maintains cached converters for common primitive and wrapper types to optimize performance.
 *
 * <p>Supports automatic type coercion from string representations to target types, with intelligent
 * handling of numeric types and default value provision.
 */
@UtilityClass
public class CTypeConverter {

  /** Thread-safe map caching type conversion functions for each supported Java class. */
  private static final Map<Class<?>, Function<String, Object>> CLASS_CONVERTERS;

  /** Thread-safe map storing default values for each supported Java type. */
  private static final Map<Class<?>, Object> DEFAULT_VALUES;

  static {
    CLASS_CONVERTERS = new ConcurrentHashMap<>();
    DEFAULT_VALUES = new ConcurrentHashMap<>();
    initializeClassConverters();
    initializeDefaultValues();
  }

  /**
   * Initializes the converter registry with standard Java type parsing functions. Registers
   * converters for primitives, wrappers, and common types.
   */
  private static void initializeClassConverters() {
    CLASS_CONVERTERS.put(String.class, value -> value);
    CLASS_CONVERTERS.put(int.class, Integer::parseInt);
    CLASS_CONVERTERS.put(Integer.class, Integer::parseInt);
    CLASS_CONVERTERS.put(long.class, Long::parseLong);
    CLASS_CONVERTERS.put(Long.class, Long::parseLong);
    CLASS_CONVERTERS.put(float.class, Float::parseFloat);
    CLASS_CONVERTERS.put(Float.class, Float::parseFloat);
    CLASS_CONVERTERS.put(double.class, Double::parseDouble);
    CLASS_CONVERTERS.put(Double.class, Double::parseDouble);
    CLASS_CONVERTERS.put(Number.class, CTypeConverter::parseNumber);
    CLASS_CONVERTERS.put(boolean.class, Boolean::parseBoolean);
    CLASS_CONVERTERS.put(Boolean.class, Boolean::parseBoolean);
  }

  /**
   * Initializes the default value registry for all supported Java types. Provides sensible defaults
   * matching Java language specifications.
   */
  private static void initializeDefaultValues() {
    DEFAULT_VALUES.put(String.class, CStringUtil.EMPTY);
    DEFAULT_VALUES.put(int.class, 0);
    DEFAULT_VALUES.put(Integer.class, 0);
    DEFAULT_VALUES.put(long.class, 0L);
    DEFAULT_VALUES.put(Long.class, 0L);
    DEFAULT_VALUES.put(float.class, 0.0F);
    DEFAULT_VALUES.put(Float.class, 0.0F);
    DEFAULT_VALUES.put(double.class, 0.0);
    DEFAULT_VALUES.put(Double.class, 0.0);
    DEFAULT_VALUES.put(Number.class, 0.0);
    DEFAULT_VALUES.put(boolean.class, false);
    DEFAULT_VALUES.put(Boolean.class, false);
  }

  /**
   * Intelligently parses a string representation of a number to the most appropriate type. Uses
   * double precision for decimal values and attempts integer before long for whole numbers.
   *
   * @param number string representation of the numeric value
   * @return parsed number as Integer, Long, or Double depending on format
   */
  private static Number parseNumber(String number) {
    // Use double precision for decimal numbers
    if (number.contains(CStringUtil.DOT)) {
      return Double.parseDouble(number);
    }

    // Attempt integer parsing first for whole numbers
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException e) {
      // Fall back to long for values exceeding integer range
      return Long.parseLong(number);
    }
  }

  /**
   * Converts a value to the specified target type using registered converters. Returns default
   * value for the target type when input is null.
   *
   * @param value the value to convert (nullable)
   * @param targetType the desired type for conversion
   * @return converted value or default value if input is null
   */
  public static Object convert(@Nullable Object value, Class<?> targetType) {
    if (value == null) {
      return DEFAULT_VALUES.get(targetType);
    }

    if (CLASS_CONVERTERS.containsKey(targetType)) {
      Function<String, Object> converter = CLASS_CONVERTERS.get(targetType);
      return converter.apply(value.toString());
    }

    return value;
  }
}
